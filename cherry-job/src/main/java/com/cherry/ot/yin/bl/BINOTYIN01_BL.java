/*	
 * @(#)BINOTYIN01_BL.java     1.0 @2013-3-11		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.ot.yin.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM06_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ot.yin.service.BINOTYIN01_Service;

/**
 *
 * 颖通接口：颖通销售数据导出(销售+支付)BL
 *
 * @author jijw
 *
 * @version  2013-3-11
 */
public class BINOTYIN01_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINOTYIN01_BL.class);	
	@Resource
	private BINOTYIN01_Service binOTYIN01_Service;
	
	/** 系统配置项 共通 **/
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLCM06_Service")
	private BINOLCM06_Service binOLCM06_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 销售主数据每次导出数量:2000条 */
	private final int UPDATE_SIZE = 1000;
	
	/** 销售记录同步状态:1 可同步 */
	private final String SYNCH_FLAG_1 = "1";
	
	/** 销售记录同步状态:2 同步处理中 */
	private final String SYNCH_FLAG_2 = "2";
	
	/** 销售记录同步状态:3 已完成 */
	private final String SYNCH_FLAG_3 = "3";
	
	private Map<String, Object> comMap;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 产品列表的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchOTYIN01(Map<String, Object> map)
			throws CherryBatchException {
		// 初始化
		comMap = getComMap(map);
		
		while (true) {
			// 预处理可能失败的件数
			int prepFailCount = 0;
			try {
				
				// Step1:查看新后台销售数据中是否存在处于同步中且未导入到颖通接口的数据（synchFlag=2)
				int srCountBySync = getSRCountBySynch();
				
				// 更新新后台销售数据从[synchFlag=1]更新为[synchFlag=2]
				Map<String, Object> s1ToS2Map = new HashMap<String, Object>();
				s1ToS2Map.putAll(comMap);
				s1ToS2Map.put("upCount", UPDATE_SIZE);
				s1ToS2Map.put("synchFlag_Old", SYNCH_FLAG_1);
				s1ToS2Map.put("synchFlag_New", SYNCH_FLAG_2);
				int upCountSucc = binOTYIN01_Service.updSaleRecordBySync(s1ToS2Map);
				
				// 新后台数据源commit
				binOTYIN01_Service.manualCommit();
				
				// 当前需要导入颖通的数据count
				int impCount = upCountSucc + srCountBySync;
				prepFailCount = impCount;
				// 统计总条数
				totalCount += impCount;
				
				// 没有可用于导出的数据时，结束程序
				if( impCount == 0){
					break;
				}
				
				// (Step2、Step3)取得新后台同步状态为"同步处理中"[synchFlag=2]的销售数据及销售支付数据分别插入到颖通销售接口表、支付构成接口表
				insertOTSaleAndPay(prepFailCount);
				
				// Step2、Step3失败时结束批处理
				// 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
				if(flag == CherryBatchConstants.BATCH_WARNING || impCount < UPDATE_SIZE){
					break;
				}
				
			} catch(Exception e){
				// 新后台数据源回滚
				binOTYIN01_Service.manualRollback();
				// 失败件数
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00001");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				// 失败时结束批处理
				// 程序出现异常后，后面的批处理依然会遇到这样的问题。
				break;
			}
		}
		
		outMessage();
		return flag;
	}
	
	/**
	 * 查看新后台销售数据中是否存在处于同步中（synchFlag=2）
	 * 若存在，则查询其是否已导出到颖通销售接口（是：将新后台的同步状态由同步中[synchFlag=2]改为已完成[synchFlag=3]）
	 * @return
	 * @throws CherryBatchException 
	 */
	@SuppressWarnings("unchecked")
	private int getSRCountBySynch() throws CherryBatchException{
		
		// 取得新后台[synchFlag=2]的销售单据号List
		Map<String, Object> synch2Map = new HashMap<String, Object>(); // 同步处理中的销售数据
		synch2Map.putAll(comMap);
		synch2Map.put("synchFlag", SYNCH_FLAG_2);
		List<String> billCodeOfBackENDList = binOTYIN01_Service.getBillCodeOfSRListBySync(synch2Map);
		
		if(null != billCodeOfBackENDList && !billCodeOfBackENDList.isEmpty()){
			// 根据新后台[synchFlag=2]的销售单据号List查询颖通销售接口是否存在（存在则说明已被新后台相应销售数据已导出到颖通销售接口）
			Map<String,Object> bcMap = new HashMap<String, Object>();
			bcMap.putAll(comMap);
			bcMap.put("billCodeList", billCodeOfBackENDList);
			List<String> billCodeListOfOT = binOTYIN01_Service.getSRListByBillCodeForOT(bcMap);
			
			// 将已导出到颖通销售接口的单据号作为条件更新新后台的同步状态([synchFlag=2]更新为[synchFlag=3])
			if(null != billCodeListOfOT && !billCodeListOfOT.isEmpty()){
				Map<String,Object> upTo3Map = new HashMap<String, Object>();
				upTo3Map.putAll(comMap);
				upTo3Map.put("billCodeList", billCodeListOfOT);
				upTo3Map.put("synchFlag_New", SYNCH_FLAG_3);
				binOTYIN01_Service.updSaleRecordBySync(upTo3Map);
				// 新后台数据源Commit
				binOTYIN01_Service.manualCommit();
			}
			
			// 计算去除掉在颖通接口上存在并更新成[synchFlag=3]的数据后还剩余为[synchFlag=2]的销售数据
			billCodeOfBackENDList.removeAll(billCodeListOfOT);
		}
		
		return billCodeOfBackENDList.size();
	}
	
	/**
	 * →据将销售主表union明细表的导入到颖通销售接口表
	 * →销售支付数据导入到颖通支付接口表
	 * →更新之前进行的销售数据从[synchFlag=2]更新为[synchFlag=3]
	 * 
	 * @param srCountBySync
	 * @throws CherryBatchException
	 */
	private void insertOTSaleAndPay(int prepFailCount) throws CherryBatchException{
		
		try {
			// Step2:
			// 取得新后台同步状态为"同步处理中"[synchFlag=2]销售数据（主数据、明细数据）导出到颖通销售接口表
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(comMap);
			map.put("synchFlag", SYNCH_FLAG_2);
			List<Map<String,Object>> srListBySynchFlagList = binOTYIN01_Service.getSRListBySynchFlag(map);
			if(null != srListBySynchFlagList && !srListBySynchFlagList.isEmpty()){
				// 插入颖通销售接口表
				binOTYIN01_Service.insertCpsImportSales(srListBySynchFlagList);
				
				// 插入颖通会员信息表
				Map<String,Object> insMap = new HashMap<String,Object>(); // 销售数据中有重复的会员数据，只处理一条
				for(Map<String,Object> memInfo : srListBySynchFlagList){
					String memId = ConvertUtil.getString(memInfo.get("BIN_MemberInfoID"));
					if(!memId.isEmpty() && !insMap.containsKey(memId)){
						insMap.put(memId, 1);
						binOTYIN01_Service.insertMemVisitInfo(memInfo);
					}
				}
			}
			// 取得新后台同步状态为"同步处理中"[synchFlag=2]销售支付数据导出到颖通支付接口表
			List<Map<String,Object>> salePayBySynchFlagList = binOTYIN01_Service.getSalePayListBySynchFlag(map);
			if(null != salePayBySynchFlagList && !salePayBySynchFlagList.isEmpty()){
				// 插入到颖通支付构成接口表
				binOTYIN01_Service.insertCpsImportPayment(salePayBySynchFlagList);
			}
			
			// 颖通数据源commit
			binOTYIN01_Service.tpifManualCommit();
			
			try {
				// Step3:销售数据从[synchFlag=2]更新为[synchFlag=3]
				Map<String, Object> s2ToS3Map = new HashMap<String, Object>();
				s2ToS3Map.putAll(comMap);
				s2ToS3Map.put("synchFlag_Old", SYNCH_FLAG_2);
				s2ToS3Map.put("synchFlag_New", SYNCH_FLAG_3);
				binOTYIN01_Service.updSaleRecordBySync(s2ToS3Map);
				
				// 新后台数据源commit
				binOTYIN01_Service.manualCommit();
				
			} catch(Exception e){
				try{
					// 新后台数据源回滚
					binOTYIN01_Service.manualRollback();
				}catch(Exception e2){
					
				}
				// 失败件数
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00003");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			}
		} catch (Exception e) {
			try{
				// 颖通数据源回滚
				binOTYIN01_Service.tpifManualRollback();
			} catch(Exception e2){
				
			}
			// 失败件数
			failCount += prepFailCount;
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EOT00002");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO1);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
		}
		
	}
	
	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOTYIN01");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOTYIN01");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		
		// 币种
		String curType = binOLCM14_BL.getConfigValue("1069",
				String.valueOf(baseMap.get("organizationInfoId")),
				String.valueOf(baseMap.get("brandInfoId")));
		baseMap.put("curType", curType);
		
		// 是否测试模式
		String testMod = binOLCM14_BL.getConfigValue("1080",
				String.valueOf(baseMap.get("organizationInfoId")),
				String.valueOf(baseMap.get("brandInfoId")));
		baseMap.put("testMod", testMod);	
		
		//品牌code
		baseMap.put(CherryBatchConstants.BRAND_CODE,binOLCM06_Service.getBrandCode(map));
		
		return baseMap;
	}
	
	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}

}

/*	
 * @(#)BINBAT124_BL.java     1.0 @2015-12-16		
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
package com.cherry.middledbout.stand.stockTaking.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM06_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.stockTaking.service.BINBAT124_Service;

/**
 *
 * 标准接口：产品盘点单据导出BL
 *
 * @author jijw
 *
 * @version  2015-12-16
 */
public class BINBAT124_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT124_BL.class);	
	@Resource
	private BINBAT124_Service binBAT124_Service;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** 系统配置项 共通 **/
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLCM06_Service")
	private BINOLCM06_Service binOLCM06_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 产品盘点主数据每次导出数量:2000条 */
	private final int UPDATE_SIZE = 1000;
	
	/** 产品盘点主数据同步状态:1 可同步 */
	private final String SYNCH_FLAG_1 = "1";
	
	/** 产品盘点主数据同步状态:2 同步处理中 */
	private final String SYNCH_FLAG_2 = "2";
	
	/** 产品盘点主数据同步状态:3 已完成 */
	private final String SYNCH_FLAG_3 = "3";
	
	private Map<String, Object> comMap;
	
	/** 处理总条数 */
	private int totalCount = 0;
	
	/** 失败条数 */
	private int failCount = 0;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchExec(Map<String, Object> map)
			 throws CherryBatchException,Exception {
		// 初始化
		comMap = getComMap(map);
		map.put("JobCode", "BAT124");
		comMap.put("JobCode", "BAT124");
		// 程序【开始运行时间】
		String runStartTime = binBAT124_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		while (true) {
			// 预处理可能失败的件数
			int prepFailCount = 0;
			try {
				// Step1:查看新后台盘点数据中是否存在处于同步中且未导入到标准盘点接口表的数据（synchFlag=2)
				int srCountBySync = getStockTakingCountBySynch();
				
				// Step1:查看新后台产品盘点主数据中是否存在处于同步中（synchFlag=2）或可同步（synchFlag=1）的数据
				// 更新新后台产品盘点主数据从[synchFlag=1或者sycnFlag = null]更新为[synchFlag=2]
				Map<String, Object> s1ToS2Map = new HashMap<String, Object>();
				s1ToS2Map.putAll(comMap);
				s1ToS2Map.put("upCount", UPDATE_SIZE);
				s1ToS2Map.put("synchFlag_Null", "null");
				s1ToS2Map.put("synchFlag_Old", SYNCH_FLAG_1);
				s1ToS2Map.put("synchFlag_New", SYNCH_FLAG_2);
				int upCountSucc = binBAT124_Service.updProductStockTakingBySync(s1ToS2Map);
				
				// 新后台数据源commit
				binBAT124_Service.manualCommit();
				
				// 当前需要导入标准的数据count
				int impCount = upCountSucc + srCountBySync;
				prepFailCount = impCount;
				// 统计总条数
				totalCount += impCount;
				
				// 没有可用于导出的数据时，结束程序
				if(impCount == 0){
					break;
				}
				
				// (Step2、Step3)取得新后台同步状态为"同步处理中"[synchFlag=2]的产品盘点主数据插入到标准盘点单接口表
				insertOTStkake(prepFailCount);
				
				// Step2、Step3失败时结束批处理
				// 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
				if(flag == CherryBatchConstants.BATCH_WARNING || impCount < UPDATE_SIZE){
					break;
				}
				
			} catch(Exception e){
				// 新后台数据源回滚
				binBAT124_Service.manualRollback();
				// 失败件数
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00005");
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
		// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
		programEnd(map);
		return flag;
	}
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		
		paraMap.putAll(comMap);
		
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			// 程序结束时，更新Job控制表 
			binbecm01_IF.updateJobControl(paraMap);
		}		
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
 
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
	
	/**
	 * 查看新后台盘点数据中是否存在处于同步中（synchFlag=2）
	 * 若存在，则查询其是否已导出到标准盘点接口表（是：将新后台的同步状态由同步中[synchFlag=2]改为已完成[synchFlag=3]）
	 * @return
	 * @throws CherryBatchException 
	 */
	@SuppressWarnings("unchecked")
	private int getStockTakingCountBySynch() throws CherryBatchException{
		
		// 取得新后台[synchFlag=2]的盘点单据号List
		Map<String, Object> synch2Map = new HashMap<String, Object>(); // [同步中]的盘点单据数据
		synch2Map.putAll(comMap);
		synch2Map.put("synchFlag", SYNCH_FLAG_2);
		List<String> stNoIFOfBackENDList = binBAT124_Service.getSTNoIFOfPrtStockTakingListBySync(synch2Map);
		
		if(null != stNoIFOfBackENDList && !stNoIFOfBackENDList.isEmpty()){
			// 根据新后台[synchFlag=2]的产品盘点单据号List查询标准盘点接口表是否存在（存在则说明新后台相应盘点单数据已导出到标准盘点盘点单接口表）
			Map<String,Object> bcMap = new HashMap<String, Object>();
			bcMap.put("stNoIFList", stNoIFOfBackENDList);
			bcMap.putAll(comMap);
			List<String> stNoIFListOfOT = binBAT124_Service.getPstListBySTNoIFForOT(bcMap);
			
			// 将已导出到标准盘点接口的单据号作为条件更新新后台的同步状态([synchFlag=2]更新为[synchFlag=3])
			if(null != stNoIFListOfOT && !stNoIFListOfOT.isEmpty()){
				Map<String,Object> upTo3Map = new HashMap<String, Object>();
				upTo3Map.putAll(comMap);
				upTo3Map.put("stNoIFList", stNoIFListOfOT);
				upTo3Map.put("synchFlag_New", SYNCH_FLAG_3);
				binBAT124_Service.updProductStockTakingBySync(upTo3Map);
				// 新后台数据源Commit
				binBAT124_Service.manualCommit();
			}
			
			// 计算去除掉在标准接口上存在并更新成[synchFlag=3]的数据后还剩余为[synchFlag=2]的盘点单数据
			stNoIFOfBackENDList.removeAll(stNoIFListOfOT);
		}
		
		return stNoIFOfBackENDList.size();
	}
	
	/**
	 * →产品盘点明细表的导入到标准盘点接口表
	 * →更新之前进行的盘点数据从[synchFlag=2]更新为[synchFlag=3]
	 * 
	 * @throws CherryBatchException
	 */
	private void insertOTStkake(int prepFailCount) throws CherryBatchException{
		
		try {
			// Step2:
			// 取得新后台同步状态为"同步处理中"[synchFlag=2]产品盘点数据（主数据、明细数据）导出到标准盘点单接口表
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(comMap);
			map.put("synchFlag", SYNCH_FLAG_2);
			// 盘点单主表
			List<Map<String,Object>> stkakeListBySynchFlagList = binBAT124_Service.getProductStockListBySyncFlag(map);
			if(null != stkakeListBySynchFlagList && !stkakeListBySynchFlagList.isEmpty()){
				String[] stockBillCodeArr = new String[stkakeListBySynchFlagList.size()];
				for(int i =0;i<stkakeListBySynchFlagList.size();i++){
					stockBillCodeArr[i] = stkakeListBySynchFlagList.get(i).get("BillCode").toString();
				}
				map.put("stockBillCodeArr", stockBillCodeArr);
								
				// 盘点明细
				List<Map<String,Object>> stkakeDetailListBySynchFlagList = binBAT124_Service.getProductStockDetailListBySyncFlag(map);
				// 插入盘点单接口表
				binBAT124_Service.insertIF_StockTaking(stkakeListBySynchFlagList);
				binBAT124_Service.insertIF_StockTakingDetail(stkakeDetailListBySynchFlagList);
				// 标准数据源commit
				binBAT124_Service.tpifManualCommit();
			}			
			try {
				// Step3 盘点数据从[synchFlag=2]更新为[synchFlag=3]
				Map<String, Object> s2ToS3Map = new HashMap<String, Object>();
				s2ToS3Map.putAll(comMap);
				s2ToS3Map.put("synchFlag_Old", SYNCH_FLAG_2);
				s2ToS3Map.put("synchFlag_New", SYNCH_FLAG_3);
				binBAT124_Service.updProductStockTakingBySync(s2ToS3Map);
				
				// 新后台数据源commit
				binBAT124_Service.manualCommit();
				
			} catch(Exception e){
				try{
					// 新后台数据源回滚
					binBAT124_Service.manualRollback();

				}catch(Exception e2){
					
				}
				// 失败件数
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00007");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			}
		} catch (Exception e) {
			try{
				// 标准数据源回滚
				binBAT124_Service.tpifManualRollback();
			} catch(Exception e2){
				
			}
			// 失败件数
			failCount += prepFailCount;
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EOT00106");
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
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT124_BL");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT124_BL");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binBAT124_Service.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap
				.get(CherryBatchConstants.BUSINESS_DATE));
		baseMap.put("businessDate", businessDate);
		
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
		
		if(totalCount!=0){
			if(failCount!=0){
				fReason="柜台盘点单据导出到标准接口失败，详情请查看log日志！";
			}
		}
	}

}

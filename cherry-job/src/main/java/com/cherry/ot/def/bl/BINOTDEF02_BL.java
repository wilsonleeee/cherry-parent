/*	
 * @(#)BINOTDEF02_BL.java     1.0 @2013-7-11		
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
package com.cherry.ot.def.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.ot.def.service.BINOTDEF02_Service;

/**
 *
 * 标准订单数据导出BL
 *
 * @author jijw
 *
 * @version  2013-7-11
 */
public class BINOTDEF02_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINOTDEF02_BL.class);	
	@Resource
	private BINOTDEF02_Service binOTDEF02_Service;
	
	/** 系统配置项 共通 **/
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 订单主数据每次导出数量:2000条 */
	private final int UPDATE_SIZE = 2000;
	
	/** 订单记录同步状态:1 可同步 */
	private final String SYNCH_FLAG_1 = "1";
	
	/** 订单记录同步状态:2 同步处理中 */
	private final String SYNCH_FLAG_2 = "2";
	
	/** 订单记录同步状态:3 已完成 */
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
	public int tran_batchOTDEF02(Map<String, Object> map)
			throws CherryBatchException {
		// 初始化
		comMap = getComMap(map);
		
		while (true) {
			// 预处理可能失败的件数
			int prepFailCount = 0;
			try {
				
				// Step1:查看新后台订单数据中是否存在处于同步中且未导入到订单数据接口表中的数据（synchFlag=2)
				int poCountBySync = getPrtOrderCountBySynch();
				
				// 更新新后台订单数据从[synchFlag=1]更新为[synchFlag=2]
				Map<String, Object> s1ToS2Map = new HashMap<String, Object>();
				s1ToS2Map.putAll(comMap);
				s1ToS2Map.put("upCount", UPDATE_SIZE);
				s1ToS2Map.put("synchFlag_Old", SYNCH_FLAG_1);
				s1ToS2Map.put("synchFlag_New", SYNCH_FLAG_2);
				int upCountSucc = binOTDEF02_Service.updPrtOrderBySync(s1ToS2Map);
				
				// 新后台数据源commit
				binOTDEF02_Service.manualCommit();
				
				// 当前需要导入订单接口表的数据count
				int impCount = upCountSucc + poCountBySync;
				prepFailCount = impCount;
				// 统计总条数
				totalCount += impCount;
				
				// 没有可用于导出的数据时，结束程序
				if( impCount == 0){
					break;
				}
				
				// (Step2、Step3)取得新后台同步状态为"同步处理中"[synchFlag=2]的订单数据订单数据接口表（订单主表、明细表）
				insertOTPoAndPoD(prepFailCount);
				
				// Step2、Step3失败时结束批处理
				// 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
				if(flag == CherryBatchConstants.BATCH_WARNING || impCount < UPDATE_SIZE){
					break;
				}
				
			} catch(Exception e){
				// 新后台数据源回滚
				binOTDEF02_Service.manualRollback();
				// 失败件数
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00035");
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
	 * 查看新后台订单数据中是否存在处于同步中（synchFlag=2）
	 * 若存在，则查询其是否已导出到订单接口表[I_WITPOS_Orders](是：将新后台的同步状态由同步中[synchFlag=2]改为已完成[synchFlag=3])
	 * @return
	 * @throws CherryBatchException 
	 */
	@SuppressWarnings("unchecked")
	private int getPrtOrderCountBySynch() throws CherryBatchException{
		
		// 取得新后台[synchFlag=2]的订单单据号List
		Map<String, Object> synch2Map = new HashMap<String, Object>(); // 同步处理中的订单数据
		synch2Map.putAll(comMap);
		synch2Map.put("synchFlag", SYNCH_FLAG_2);
		List<String> prtOrderListBySynch = binOTDEF02_Service.getPrtOrderListBySynch(synch2Map);
		
		if(null != prtOrderListBySynch && !prtOrderListBySynch.isEmpty()){
			// 根据新后台[synchFlag=2]的订单单据号List查询订单数据接口表是否存在（存在则说明已被新后台相应订单数据已导出到订单接口表）
			Map<String,Object> bcMap = new HashMap<String, Object>();
			bcMap.putAll(comMap);
			bcMap.put("orderNoIFList", prtOrderListBySynch);
			List<String> orderNoListOfOT = binOTDEF02_Service.getPrtOrderListByOrderNoForOT(bcMap);
			
			// 将已导出到订单接口表[Interfaces.BIN_Sale]的单据号作为条件更新新后台的同步状态([synchFlag=2]更新为[synchFlag=3])
			if(null != orderNoListOfOT && !orderNoListOfOT.isEmpty()){
				Map<String,Object> upTo3Map = new HashMap<String, Object>();
				upTo3Map.putAll(comMap);
				upTo3Map.put("orderNoIFList", orderNoListOfOT);
				upTo3Map.put("synchFlag_New", SYNCH_FLAG_3);
				binOTDEF02_Service.updPrtOrderBySync(upTo3Map);
				// 新后台数据源Commit
				binOTDEF02_Service.manualCommit();
			}
			
			// 计算去除掉在订单相关接口表存在并更新成[synchFlag=3]的数据后还剩余为[synchFlag=2]的订单数据
			prtOrderListBySynch.removeAll(orderNoListOfOT);
		}
		
		return prtOrderListBySynch.size();
	}
	
	/**
	 * →据将订单主表数据导入到订单数据接口表
	 * →据将订单明细表数据导入到订单明细数据接口表
	 * →更新之前进行的订单数据从[synchFlag=2]更新为[synchFlag=3]
	 * 
	 * @param srCountBySync
	 * @throws CherryBatchException
	 */
	private void insertOTPoAndPoD(int prepFailCount) throws CherryBatchException{
		
		try {
			// Step2:
			// 取得新后台同步状态为"同步处理中"[synchFlag=2]订单数据（主数据）导出到订单数据接口主表
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(comMap);
			map.put("synchFlag", SYNCH_FLAG_2);
			List<Map<String,Object>> poListBySynchFlagList = binOTDEF02_Service.getPOListBySynchFlag(map);
			if(null != poListBySynchFlagList && !poListBySynchFlagList.isEmpty()){
				// 插入订单数据接口表[I_WITPOS_Orders]
				binOTDEF02_Service.insertI_WITPOS_Orders(poListBySynchFlagList);
				// 插入订单数据接口日志表[I_WITPOS_Orders_Log]
				binOTDEF02_Service.insertI_WITPOS_Orders_Log(poListBySynchFlagList);
			}
			List<Map<String,Object>> poDetailListBySynchFlagList = binOTDEF02_Service.getPODetailListBySynchFlag(map);
			if(null != poDetailListBySynchFlagList && !poDetailListBySynchFlagList.isEmpty()){
				// 插入订单数据明细接口表[I_WITPOS_OrdersDetail]
				binOTDEF02_Service.insertI_WITPOS_OrdersDetail(poDetailListBySynchFlagList);
				// 插入订单数据明细接日志口表[I_WITPOS_OrdersDetail_Log]
				binOTDEF02_Service.insertI_WITPOS_OrdersDetail_Log(poDetailListBySynchFlagList);
			}
			// 数据源commit
			binOTDEF02_Service.tpifManualCommit();
			
			try {
				// Step3:订单数据从[synchFlag=2]更新为[synchFlag=3]
				Map<String, Object> s2ToS3Map = new HashMap<String, Object>();
				s2ToS3Map.putAll(comMap);
				s2ToS3Map.put("synchFlag_Old", SYNCH_FLAG_2);
				s2ToS3Map.put("synchFlag_New", SYNCH_FLAG_3);
				binOTDEF02_Service.updPrtOrderBySync(s2ToS3Map);
				
				// 新后台数据源commit
				binOTDEF02_Service.manualCommit();
				
			} catch(Exception e){
				try{
					// 新后台数据源回滚
					binOTDEF02_Service.manualRollback();
				}catch(Exception e2){
					
				}
				// 失败件数
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00037");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			}
		} catch (Exception e) {
			try{
				// 数据源回滚
				binOTDEF02_Service.tpifManualRollback();
			} catch(Exception e2){
				
			}
			// 失败件数
			failCount += prepFailCount;
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EOT00036");
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
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOTDEF02");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOTDEF02");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		
		// 是否测试模式
		String testMod = binOLCM14_BL.getConfigValue("1080",
				String.valueOf(baseMap.get("organizationInfoId")),
				String.valueOf(baseMap.get("brandInfoId")));
		baseMap.put("testMod", testMod);	
		
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

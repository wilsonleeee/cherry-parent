/*
 * @(#)BINBETLBAT01_BL.java     1.0 2010/12/24
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
package com.cherry.tl.bat.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.DateUtil;
import com.cherry.tl.bat.service.BINBETLBAT01_Service;

/**
 * 
 * 清空各种表BL
 * 
 * 
 * @author ZHANGJIE
 * @version 1.0 2010.12.24
 */
public class BINBETLBAT01_BL {

	/** 清空各种表service */
	@Resource
	private BINBETLBAT01_Service binbetlbat01srvice;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 成功条数 */
	private int successCount = 0;
	/** 失败条数 */
	private int failCount = 0;

	/**
	 * 
	 * 清空采番表处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * @throws Exception 
	 * 
	 */
	public int clearTicketNumber(Map<String, Object> map) throws Exception {
		try {
            // 业务日期
            String bussinessDate = binbetlbat01srvice.getBussinessDate(map);
            // 保留业务日期前7天的取号记录
            map.put("ControlDate", DateUtil.addDateByDays(DateUtil.DATE_PATTERN, bussinessDate, -7));
			// 清空采番表
			binbetlbat01srvice.clearTicketNumber(map);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00001");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}
	
	/**
	 * 
	 * 整理各种取号表处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * @throws Exception 
	 * 
	 */
	public int tran_clearSequenceCode(Map<String, Object> map) throws Exception {
		
		// 从各类编号取号表中取得每种类型的最大番号
		List<Map<String, Object>> maxSeqCodeList = binbetlbat01srvice.getMaxSequenceCode(map);
		if(maxSeqCodeList != null && !maxSeqCodeList.isEmpty()) {
			for(int i = 0; i < maxSeqCodeList.size(); i++) {
				Map<String, Object> maxSeqCodeMap = maxSeqCodeList.get(i);
				// 取得编号类型
				String type = (String)maxSeqCodeMap.get("type");
				maxSeqCodeMap.putAll(map);
				try {
					// 删除各类编号取号表中小于最大番号的数据
					binbetlbat01srvice.clearSequenceCode(maxSeqCodeMap);
					// 统计成功条数
					successCount++;
					// Cherry数据库事务提交
					binbetlbat01srvice.manualCommit();
				} catch (Exception e) {
					// 统计失败条数
					failCount++;
					try {
						// Cherry数据库回滚事务
						binbetlbat01srvice.manualRollback();
					} catch (Exception ex) {
						
					}
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ETL00003");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 编号类型
					batchLoggerDTO1.addParam(type);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					flag =  CherryBatchConstants.BATCH_WARNING;
				}
			}
		}
		
		// 下面是对不按品牌分的采番数据做清理处理（不按品牌分的采番数据也就是品牌ID为-9999的数据）
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.BRANDINFOID, CherryBatchConstants.BRAND_INFO_ID_VALUE);
		// 从各类编号取号表中取得仓库类型的最大番号（由于仓库是不分品牌的）
		maxSeqCodeList = binbetlbat01srvice.getMaxSequenceCode(paramMap);
		if(maxSeqCodeList != null && !maxSeqCodeList.isEmpty()) {
			for(int i = 0; i < maxSeqCodeList.size(); i++) {
				Map<String, Object> maxSeqCodeMap = maxSeqCodeList.get(i);
				// 取得编号类型
				String type = (String)maxSeqCodeMap.get("type");
				maxSeqCodeMap.putAll(paramMap);
				try {
					// 删除各类编号取号表中小于最大番号的数据
					binbetlbat01srvice.clearSequenceCode(maxSeqCodeMap);
					// 统计成功条数
					successCount++;
					// Cherry数据库事务提交
					binbetlbat01srvice.manualCommit();
				} catch (Exception e) {
					// 统计失败条数
					failCount++;
					try {
						// Cherry数据库回滚事务
						binbetlbat01srvice.manualRollback();
					} catch (Exception ex) {
						
					}
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("ETL00003");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 编号类型
					batchLoggerDTO1.addParam(type);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					flag =  CherryBatchConstants.BATCH_WARNING;
				}
			}
		}
		
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount+failCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		
		return flag;
	}
	
	/**
	 * 
	 * 清空OSWF序号表处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * @throws Exception 
	 * 
	 */
	public int clearOSWF(Map<String, Object> map) throws Exception {
		try {
			// 清空OS_STEPIDS表
			binbetlbat01srvice.clearOsStepIds(map);
			// 清空OS_ENTRYIDS表
			binbetlbat01srvice.clearOsEntryIds(map);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00004");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			flag =  CherryBatchConstants.BATCH_WARNING;
		}
		return flag;
	}
	
}

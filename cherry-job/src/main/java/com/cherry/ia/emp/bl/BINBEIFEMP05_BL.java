/*
 * @(#)BINBEIFEMP05_BL.java     1.0 2013/10/29
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
package com.cherry.ia.emp.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ia.emp.service.BINBEIFEMP05_Service;

/**
 * 刷新老后台U盘绑定柜台数据BL
 * 
 * @author JiJW
 * @version 1.0 2013-10-29
 */
public class BINBEIFEMP05_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEIFEMP05_BL.class);	
	
	@Resource(name="binBEIFEMP05_Service")
	private BINBEIFEMP05_Service binBEIFEMP05_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 刷新老后台U盘绑定柜台数据batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchUDiskCounter(Map<String, Object> map)
			throws CherryBatchException {
		
		try{
			
			// Setp1:清空老后台U盘柜台关系表
			binBEIFEMP05_Service.truncateUDiskCounter(map);
			
			// Setp2:将新后台查询到的U盘与柜台的关系数据循环插入老后台
			// 数据抽出次数
			int currentNum = 0;
			while (true) {
				// 查询开始位置
				int startNum = CherryBatchConstants.DATE_SIZE * currentNum + 1;
				currentNum += 1;
				// 查询结束位置
				int endNum = startNum + CherryBatchConstants.DATE_SIZE - 1;
				map.put(CherryBatchConstants.START, startNum);
				map.put(CherryBatchConstants.END, endNum);
				map.put(CherryBatchConstants.SORT_ID, "counterCode");
				
				// 取得新后台具有U盘与柜台的关系数据
				List<Map<String,Object>> udiskCounterList = binBEIFEMP05_Service.getUDiskCounterList(map);
				
				if (CherryBatchUtil.isBlankList(udiskCounterList)) {
					break;
				}
				
				// 统计总条数
				totalCount += udiskCounterList.size();
				
				// 插入老后台
				binBEIFEMP05_Service.addUDiskCounter(udiskCounterList);
				// 数据少于一页，跳出循环
				if (udiskCounterList.size() < CherryBatchConstants.DATE_SIZE) {
					break;
				}
			}
			
			// 老后台数据源提交
			binBEIFEMP05_Service.witManualCommit();
			
		}catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EIF06011");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO1);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			try{
				// 老后台数据源回滚
				binBEIFEMP05_Service.witManualRollback();
			}catch (Exception e2) {
			}
			
			// 失败件数
			failCount = totalCount;
			flag = CherryBatchConstants.BATCH_ERROR;
		}
		outMessage();
		return flag;
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
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}
	

}

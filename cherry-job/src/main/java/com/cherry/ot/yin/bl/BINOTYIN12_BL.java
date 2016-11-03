/*	
 * @(#)BINOTYIN11_BL.java     1.0 @2014-01-26
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ot.yin.service.BINOTYIN12_Service;

/**
 *
 * 颖通接口：颖通BA信息导出BL
 *
 * @author jijw
 *
 * @version  2014-01-26
 */
public class BINOTYIN12_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINOTYIN12_BL.class);	
	
	/** 颖通BA信息导出Service */
	@Resource
	private BINOTYIN12_Service binOTYIN12_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws Exception 
	 */
	public int tran_batchOTYIN12(Map<String, Object> map)
			throws Exception {
		
		try{
			
			// Setp1:清空颖通接口BA信息表记录
			binOTYIN12_Service.truncateCpsImpBaInfo(map);
			
			// Setp2:将新后台查询到的BA信息环插入颖通BA信息接口表
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
				map.put(CherryBatchConstants.SORT_ID, "EmployeeCode");
				
				// 取得新后台BA信息
				map.put("categoryCode", CherryBatchConstants.CATRGORY_CODE_BA);
				List<Map<String,Object>> baInfoList = binOTYIN12_Service.getBAInfoList(map);
				
				if (CherryBatchUtil.isBlankList(baInfoList)) {
					break;
				}
				
				// 统计总条数
				totalCount += baInfoList.size();
				
				// 插入颖通BA信息接口表
				binOTYIN12_Service.insertCpsImportBaInfo(baInfoList);
				// 数据少于一页，跳出循环
				if (baInfoList.size() < CherryBatchConstants.DATE_SIZE) {
					break;
				}
			}
			
			// 颖通数据源提交
			binOTYIN12_Service.tpifManualCommit();
			
		}catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EOT00039");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO1);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			try{
				// 颖通数据源回滚
				binOTYIN12_Service.tpifManualRollback();
			}catch (Exception e2) {
				throw new Exception("颖通数据源回滚失败");
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

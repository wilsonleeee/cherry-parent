/*
 * @(#)BINBEIFREG01_BL.java v1.0 2015-2-6
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
package com.cherry.ia.reg.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.ia.reg.service.BINBEIFREG01_Service;

/**
 * 区域信息下发BL
 * 
 * @author JiJW
 * @version 1.0 2015-2-6
 */
public class BINBEIFREG01_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEIFREG01_BL.class);

	@Resource
	private BINBEIFREG01_Service binBEIFREG01_Service;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	
	/**
	 * 区域信息下发的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchRegion(Map<String, Object> map)
			throws CherryBatchException,Exception {
		
		try {
			// 取得新后台的区域信息（根据终端表的数据结构）
			List<Map<String, Object>> regionList = binBEIFREG01_Service.getRegionList(map);
			
			totalCount = regionList != null ? regionList.size() : 0;
			
			// 清空终端区域表数据
			binBEIFREG01_Service.truncateUDiskCounter(map);
			
			// 插入新后台的区域数据到终端
			for(Map<String,Object> regionMap : regionList){
				binBEIFREG01_Service.addRegion(regionMap);
			}
			
			// 更新Brand中记载的终端区域表版本号
			binBEIFREG01_Service.updWP3PCSA_Ver(map);
			
		}catch(Exception e){
			flag = CherryBatchConstants.BATCH_WARNING;
			failCount = totalCount;
			throw new Exception(e);
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
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(totalCount - failCount));
		// 更新件数
		//BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		//batchLoggerDTO4.setCode("IIF00004");
		//batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		//batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		//logger.BatchLogger(batchLoggerDTO4);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}
}

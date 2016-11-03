/*
 * @(#)BINBETLBAT03_BL.java     1.0 2011/07/13
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

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.tl.bat.service.BINBETLBAT04_Service;

/**
 * 
 * 更新日结状态BL
 * 
 * 
 * @author TongLin
 * @version 1.0 2012/04/09
 */
public class BINBETLBAT04_BL {
	
	/** 更新日结状态service */
	@Resource
	private BINBETLBAT04_Service binBETLBAT04_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/**
	 * 
	 * 更新日结状态处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 * @throws Exception 
	 * 
	 */
	public int updateCloseFlag(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		try {
			// 更新日结状态
			binBETLBAT04_Service.updateCloseFlag(map);
			
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00008");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("ETL00007");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			flag =  CherryBatchConstants.BATCH_ERROR;
		}
		return flag;
	}

}

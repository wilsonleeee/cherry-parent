/*
 * @(#)CherryBatchException.java     1.0 2010/11/12
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
package com.cherry.cm.core;

/**
 * 
 *共通Batch 异常处理
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.12
 */
public class CherryBatchException extends Exception {

	private static final long serialVersionUID = -229892839660263185L;

	public CherryBatchException() {
		super();
	}

	/**
	 * <p>
	 * 异常输出
	 * </p>
	 * 
	 * 
	 * @param BatchExceptionDTO
	 *            共通异常 DTO
	 * @return 无
	 * 
	 */
	public CherryBatchException(BatchExceptionDTO batchExceptionDTO)
			throws CherryBatchException {
		synchronized (this) {
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			// 资源文件定义的code
			batchLoggerDTO.setCode(batchExceptionDTO.getErrorCode());
			// 等级
			int errorLevel = batchExceptionDTO.getErrorLevel();
			if (CherryBatchConstants.LOGGER_NONE == errorLevel) {
				errorLevel = CherryBatchConstants.LOGGER_ERROR;
			}
			batchLoggerDTO.setLevel(errorLevel);
			// 参数
			batchLoggerDTO
					.setParamsList(batchExceptionDTO.getErrorParamsList());
			CherryBatchLogger batchLogger = new CherryBatchLogger(
					batchExceptionDTO.getBatchName());
			batchLogger.BatchLogger(batchLoggerDTO, batchExceptionDTO
					.getException());
		}
	}
}

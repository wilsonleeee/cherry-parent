/*
 * @(#)CherryBatchLogger.java     1.0 2010/11/12
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *共通Batch Log处理
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.12
 */
public class CherryBatchLogger {
	private Logger logger;

	public CherryBatchLogger(Class<?> className) {
		logger = LoggerFactory.getLogger(className);
	}

	/**
	 * <p>
	 * Log输出
	 * </p>
	 * 
	 * 
	 * @param BatchLoggerDTO
	 *            共通BatchLogger DTO
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	public void BatchLogger(BatchLoggerDTO batchLoggerDTO)
			throws CherryBatchException {
		try {
			// 资源文件定义的code
			String code = batchLoggerDTO.getCode();
			// 等级
			int level = batchLoggerDTO.getLevel();
			// 参数
			List<String> paramsList = batchLoggerDTO.getParamsList();
			String[] params = null;
			if (paramsList != null) {
				params = paramsList.toArray(new String[] {});
			}
			// LOG内容
			String msg = PropertiesUtil.getMessage(code, params);
			// 日志输出
			outLog(msg,level);
		} catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace();
			e.printStackTrace(new PrintWriter(writer, true));
			try {
				// 打印LOG发生异常
				String errMsg = PropertiesUtil.getMessage("ECM00001", null);
				logger.error(errMsg);
			} catch (Exception exception) {
				throw new CherryBatchException();
			}
		}
	}

	/**
	 * <p>
	 * Log输出
	 * </p>
	 * 
	 * 
	 * @param BatchLoggerDTO
	 *            共通BatchLogger DTO
	 * @param Exception
	 *            异常信息
	 * 
	 * @return 无
	 * @throws CherryBatchException
	 * 
	 */
	public void BatchLogger(BatchLoggerDTO batchLoggerDTO, Exception exp)
			throws CherryBatchException {
		synchronized (this) {
			try {
				// 资源文件定义的code
				String code = batchLoggerDTO.getCode();
				// 等级
				int level = batchLoggerDTO.getLevel();
				// 参数
				List<String> paramsList = batchLoggerDTO.getParamsList();
				String[] params = null;
				if (null != paramsList) {
					params = paramsList.toArray(new String[] {});
				}
				// LOG内容
				String msg = PropertiesUtil.getMessage(code, params);
				// 日志输出
				outLog(msg,level);
				if (null != exp) {
					logger.error(exp.getMessage());
					StringWriter writer = new StringWriter();
					exp.printStackTrace();
					exp.printStackTrace(new PrintWriter(writer, true));
				}
			} catch (Exception e) {
				StringWriter writer = new StringWriter();
				e.printStackTrace();
				e.printStackTrace(new PrintWriter(writer, true));
				try {
					// 打印LOG发生异常
					String errMsg = PropertiesUtil.getMessage("ECM00001", null);
					logger.error(errMsg);
				} catch (Exception exception) {
					throw new CherryBatchException();
				}
			}
		}
	}
	
	/**
	 * 日志输出
	 * @param msg
	 * @param level
	 */
	public void outLog(String msg, int level){
		switch (level) {
		case CherryBatchConstants.LOGGER_DEBUG:
			logger.debug(msg);
			break;
		case CherryBatchConstants.LOGGER_INFO:
			logger.info(msg);
			break;
		case CherryBatchConstants.LOGGER_WARNING:
			logger.warn(msg);
			break;
		case CherryBatchConstants.LOGGER_ERROR:
			logger.error(msg);
		}
	}
	
	/**
	 * 异常日志输出
	 * @param msg
	 */
	public void outExceptionLog(Exception e){
			logger.error(e.getMessage(),e);
	}
	
	/**
	 * 日志输出
	 * @param msg
	 * @param level
	 */
	public void outLog(String msg){
		outLog(msg,CherryBatchConstants.LOGGER_INFO);
	}
}

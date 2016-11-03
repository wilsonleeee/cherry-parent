/*	
 * @(#)RefreshSchedulesJob.java     1.0 2013/02/28	
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
package com.cherry.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM06_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.ct.smg.bl.BINBECTSMG02_BL;

/**
 * 沟通任务动态调度处理Job
 * 
 * @author WangCT
 * @version 1.0 2013/02/28	
 */
public class RefreshSchedulesJob {
	
	private static Logger logger = LoggerFactory.getLogger(RefreshSchedulesJob.class.getName());
	
	/** JOB执行锁*/
	private static int execFlag = 0;
	
	@Resource
	private BINOLCM06_BL binOLCM06_BL;
	
	@Resource
	private BINBECTSMG02_BL binBECTSMG02_BL;
	
	public void work() {
		
		synchronized (this) {
			// 已有其他线程正在执行该JOB
			if (1 == execFlag) {
				// 启动JOB发生异常
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECM00004");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				try {
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				} catch (CherryBatchException e) {
					logger.error(e.getMessage(),e);
				}
				return;
			}
			// 锁定
			execFlag = 1;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> confBrandList = binOLCM06_BL.getConfBrandInfoList(map);
			// 品牌信息
			if (null != confBrandList) {
				for (Map<String, Object> brandInfo : confBrandList) {
					try {
						// 新后台品牌数据源
						String dataSource = (String) brandInfo.get("dataSourceName");
						CustomerContextHolder.setCustomerDataSourceType(dataSource);
						Map<String, Object> osbrandInfo = null;
						// 如果发生DB连接异常，将尝试重新连接
						for (int i = 0; i < CherryBatchConstants.DB_RECONN_TIMES + 1; i++) {
							try {
								osbrandInfo = binOLCM06_BL.getOSBrandInfo(brandInfo);
								break;
							} catch (Exception ec) {
								if (i == CherryBatchConstants.DB_RECONN_TIMES) {
									throw ec;
								}
								// 重连数据库
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECM00003");
								batchLoggerDTO.addParam(String.valueOf(i + 1));
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
								long millis = CherryBatchConstants.RECONN_SLEEP_MINUTE * 60 * 1000L;
								// 等待间隔时间
								Thread.sleep(millis);
							}
						}
						if (null == osbrandInfo) {
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("EOS00001");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							continue;
						}
						Map<String, Object> brandMap = new HashMap<String, Object>();
						brandMap.putAll(osbrandInfo);
						// 组织Code
						brandMap.put("orgCode", brandInfo.get("orgCode"));
						// 品牌code
						brandMap.put("brandCode", brandInfo.get("brandCode"));
						// 设置batch处理标志
						int flg = CherryBatchConstants.BATCH_SUCCESS;
						String brandCode = (String)brandInfo.get("brandCode");
						try {
							logger.info("******************************"+brandCode+"品牌沟通任务动态调度处理开始***************************");
							// 沟通任务动态调度处理
							flg = binBECTSMG02_BL.tran_ScheduleTask(brandMap);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							flg = CherryBatchConstants.BATCH_ERROR;
						} finally {
							if (flg == CherryBatchConstants.BATCH_SUCCESS) {
								logger.info("******************************"+brandCode+"品牌沟通任务动态调度处理正常结束***************************");
							} else if (flg == CherryBatchConstants.BATCH_WARNING) {
								logger.info("******************************"+brandCode+"品牌沟通任务动态调度处理警告结束***************************");
							} else if (flg == CherryBatchConstants.BATCH_ERROR) {
								logger.info("******************************"+brandCode+"品牌沟通任务动态调度处理异常结束***************************");
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					} finally {
						// 清除新后台品牌数据源
						CustomerContextHolder.clearCustomerDataSourceType();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} catch (Throwable t) {
			logger.error("throwable message: " + t.getMessage(),t);
		} finally {
			// 释放锁
			execFlag = 0;
		}
	}
	
	public void start() throws Exception {
		this.work();
	}

}

/*	
 * @(#)BINBE01Job.java     1.0 2012/06/15		
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cherry.cm.cmbussiness.bl.BINOLCM06_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryFileStore;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.CustomerSmsContextHolder;
import com.cherry.cm.core.CustomerTpifContextHolder;
import com.cherry.cm.core.CustomerWitContextHolder;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.job.core.BatchErrorNotice;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.config.SpringConfiguration;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 官网会员资料数据导入处理 Job
 * 
 * @author hub
 * @version 1.0 2012/06/15
 */
public class BINBE01Job implements ApplicationContextAware{
	private static Logger logger = LoggerFactory
	.getLogger(BINBE01Job.class.getName());
	
	/** JOB执行锁*/
	private static int execFlag = 0;
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	public void work() {
		synchronized (this) {
			// 已有其他线程正在执行该JOB
			if (1 == execFlag) {
				// 启动JOB发生异常
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECM00004");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
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
		String brandCode="";
		String orgCode="";
		String brandName="";
		int organizationInfoId =0;
		int brandInfoId=0;
		long wf_id = -1;
		try {
			SpringConfiguration conf = (SpringConfiguration) applicationContext
			.getBean("osworkflowConfiguration");
			Workflow wf = (Workflow) applicationContext.getBean("workflow");
			wf.setConfiguration(conf);
			BINOLCM06_BL binOLCM06_BL = (BINOLCM06_BL) applicationContext.getBean("binOLCM06_BL");
			CherryFileStore binolcm30IF = (CherryFileStore) applicationContext.getBean("binolcm30IF");
			try{
				// 刷新工作流文件
				binolcm30IF.reloadAllFiles();
			}catch (Exception e){
				// 刷新工作流文件有异常时停止工作
				logger.error("刷新工作流文件异常：" + e.getMessage(),e);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> confBrandList = binOLCM06_BL.getConfBrandInfoList(map);
			
			// 品牌信息
			if (null != confBrandList) {
				for (Map<String, Object> brandInfo : confBrandList) {
					wf_id = -1;
					try {
						Map<String, Object> checkMap = new HashMap<String, Object>();
						checkMap.put("orgCode", brandInfo.get("orgCode"));
						checkMap.put("brandCode", brandInfo.get("brandCode"));
						checkMap.put("fileCode", "BatchJobFlow01");
						if (!binOLCM06_BL.checkFile(checkMap)) {
							continue;
						}
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
						continue;
					}
					try {
						orgCode = (String) brandInfo.get("orgCode");
						brandCode = (String) brandInfo.get("brandCode");
						brandName = (String) brandInfo.get("BrandName");
						// 新后台品牌数据源
						String dataSource = (String) brandInfo.get("dataSourceName");
						// 老后台品牌数据源
						String witDataSource = (String) brandInfo.get("oldDataSourceName");
						// 第三方接口数据源
						String ifDataSourceName = (String) brandInfo.get("ifDataSourceName");
						CustomerContextHolder.setCustomerDataSourceType(dataSource);
						CustomerWitContextHolder.setCustomerWitDataSourceType(witDataSource);
						CustomerTpifContextHolder.setCustomerTpifDataSourceType(ifDataSourceName);
						CustomerSmsContextHolder.setCustomerSmsDataSourceType( (String) brandInfo.get("SMSDataSourceName"));
						Map<String, Object> brandMap = new HashMap<String, Object>();
						// 组织Code
						brandMap.put("orgCode", orgCode);
						// 品牌code
						brandMap.put("brandCode", brandCode);
						Map<String, Object> osbrandInfo = null;
						// 如果发生DB连接异常，将尝试重新连接
						for (int i = 0; i < CherryBatchConstants.DB_RECONN_TIMES + 1; i++) {
							try {
								osbrandInfo = binOLCM06_BL.getOSBrandInfo(brandMap);
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
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
										this.getClass());
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
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
									this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							continue;
						}
						brandInfo.putAll(osbrandInfo);
						// 组织ID
						organizationInfoId = Integer.parseInt(brandInfo.get("organizationInfoId").toString());
						// 品牌ID
						brandInfoId = Integer.parseInt(brandInfo.get("brandInfoId").toString());
						// JOB代号
						brandInfo.put(CherryBatchConstants.BATCH_JOB_ID, CherryBatchConstants.BATCHJOBFLOW01_CODE);
						// JOB名称
						brandInfo.put(CherryBatchConstants.BATCH_JOB_NAME, CherryBatchConstants.BATCHJOBFLOW01_NAME);
						String wf_name = ConvertUtil.getWfName(orgCode, brandCode, "BatchJobFlow01");
						wf_id = wf.initialize(wf_name, 1000, null);
						// 当前可执行的动作
						int[] actionIdArr = wf.getAvailableActions(wf_id, null);
						while (null != actionIdArr && actionIdArr.length > 0) {
							for (int actionId : actionIdArr) {
								// 执行动作
								wf.doAction(wf_id, actionId, brandInfo);
							}
							actionIdArr = wf.getAvailableActions(wf_id, null);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					} finally {
						if(-1 != wf_id && wf.getEntryState(wf_id)!=WorkflowEntry.COMPLETED){
							BatchErrorNotice batchErrorNotice = (BatchErrorNotice) applicationContext.getBean("batchErrorNotice");
							batchErrorNotice.execute(organizationInfoId,brandInfoId,orgCode,brandCode,brandName);
						}
						// 清除新后台品牌数据源
						CustomerContextHolder.clearCustomerDataSourceType();
						// 清除老后台品牌数据源
						CustomerWitContextHolder.clearCustomerWitDataSourceType();
						// 清除第三方接口数据源
						CustomerTpifContextHolder.clearCustomerTpifDataSourceType();
						CustomerSmsContextHolder.clearCustomerSmsDataSourceType();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} catch (Throwable t) {
			logger.error("throwable message: " + t.getMessage(),t);
		} finally {
			// 释放锁
			execFlag = 0;
		}
	}
}

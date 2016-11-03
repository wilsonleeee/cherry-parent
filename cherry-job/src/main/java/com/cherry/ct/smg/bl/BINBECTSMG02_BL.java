/*	
 * @(#)BINBECTSMG02_BL.java     1.0 2013/02/28	
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
package com.cherry.ct.smg.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.quartz.CommTaskExecuJob;
import com.cherry.cm.quartz.CustomJob;
import com.cherry.cm.quartz.QuartzManager;
import com.cherry.ct.smg.service.BINBECTSMG02_Service;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;

/**
 * 沟通任务动态调度管理BL
 * 
 * @author WangCT
 * @version 1.0 2013/02/28	
 */
public class BINBECTSMG02_BL implements CherryMessageHandler_IF {
	
	/** 保存每个品牌的初始化状态 **/
	private static Map<String, String> brandInitMap = new HashMap<String, String>();
	
	/** 沟通任务动态调度管理Service **/
	@Resource
	private BINBECTSMG02_Service binBECTSMG02_Service;
	
	/** 沟通任务执行Job **/
	@Resource
	private CommTaskExecuJob commTaskExecuJob;
	
	/**
	 * 接收MQ消息处理
	 * 
	 * @param msg MQ消息
	 * @throws Exception 
	 */
	@Override
    public void handleMessage(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("organizationInfoId", map.get("organizationInfoID"));
    	paramMap.put("brandInfoId", map.get("brandInfoID"));
    	paramMap.put("orgCode", map.get("orgCode"));
    	paramMap.put("brandCode", map.get("brandCode"));
    	int result = this.tran_ScheduleTask(paramMap);
    	if(result != CherryBatchConstants.BATCH_SUCCESS) {
    		throw new CherryMQException(PropertiesUtil.getMessage("ECT00055", new String[]{map.get("brandCode").toString()}));
    	}
	}
	
	/**
	 * 沟通任务动态调度处理
	 * 
	 * @param map 
	 */
	public int tran_ScheduleTask(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 任务加载成功件数
		int loadSuccessCount = 0;
		// 任务加载失败件数
		int loadErrorCount = 0;
		// 删除过期任务成功件数
		int delSuccessCount = 0;
		// 删除过期任务失败件数
		int delErrorCount = 0;
		
		// 品牌代码
		String brandCode = (String)map.get(CherryBatchConstants.BRAND_CODE);
		
		// 取得初始化状态
		String initStatus = brandInitMap.get(brandCode);
		// 未初始化的场合，把所有的沟通任务更新成未加载待运行状态（每个品牌只初始化一次）
		if(initStatus == null) {
			try {
				// 初次加载沟通任务时把所有的沟通任务更新成未加载待运行状态
				binBECTSMG02_Service.updSchedulesInit(map);
				binBECTSMG02_Service.updSchedulesStatusInit(map);
				binBECTSMG02_Service.updCommPlanInit(map);
				binBECTSMG02_Service.manualCommit();
				brandInitMap.put(brandCode, "1");
			} catch (Exception e) {
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				// 把所有的沟通任务更新成未加载待运行状态失败
				batchLoggerDTO.setCode("ECT00054");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				return flag;
			}
		}
		
		// 取得业务时间
		//String bussinessDate = binBECTSMG02_Service.getBussinessDate(map);
		String bussinessDate = binBECTSMG02_Service.getDateYMD();
		map.put("endDate", bussinessDate);

		// 查询已过期非运行状态的沟通调度信息List
		List<Map<String, Object>> expiredSchedulesList = binBECTSMG02_Service.getExpiredSchedulesList(map);
		if(expiredSchedulesList != null && !expiredSchedulesList.isEmpty()) {
			for(Map<String, Object> expiredSchedulesMap : expiredSchedulesList) {
				String jobId = expiredSchedulesMap.get("schedulesId").toString();
				try {
					// 删除已过期非运行状态的沟通调度信息
					int result = binBECTSMG02_Service.delExpiredSchedules(expiredSchedulesMap);
					if(result > 0) {
						// 从内存中删除Job
						if(QuartzManager.disableSchedule(jobId, brandCode)) {
							binBECTSMG02_Service.manualCommit();
							delSuccessCount++;
						} else {
							try {
								binBECTSMG02_Service.manualRollback();
							} catch (Exception ex) {
								
							}
							flag = CherryBatchConstants.BATCH_WARNING;
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							// 从内存中删除Job失败
							batchLoggerDTO.setCode("ECT00050");
							batchLoggerDTO.addParam(jobId);
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
						}
					}
				} catch (Exception e) {
					try {
						binBECTSMG02_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 删除已过期非运行状态的沟通调度信息失败
					batchLoggerDTO.setCode("ECT00050");
					batchLoggerDTO.addParam(jobId);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				}
			}
			delErrorCount = expiredSchedulesList.size() - delSuccessCount;
		}
		
		
		// 查询未过期未加载的沟通调度信息List
		List<Map<String, Object>> schedulesList = binBECTSMG02_Service.getSchedulesList(map);
		if(schedulesList != null && !schedulesList.isEmpty()) {
			for(Map<String, Object> schedulesMap : schedulesList) {
				String jobId = schedulesMap.get("schedulesId").toString();
				try {
					// 把未过期未加载的沟通调度信息更新成已加载
					int result = binBECTSMG02_Service.updSchedules(schedulesMap);
					if(result > 0) {
						String runTime = (String)schedulesMap.get("runTime");
						CustomJob customJob = new CustomJob(jobId, brandCode, runTime, commTaskExecuJob, "execuCommTask", new String[]{brandCode,jobId}, true);
						// 加载沟通任务Job
						if(QuartzManager.enableCronSchedule(customJob)) {
							binBECTSMG02_Service.manualCommit();
							loadSuccessCount++;
						} else {
							try {
								binBECTSMG02_Service.manualRollback();
							} catch (Exception ex) {
								
							}
							flag = CherryBatchConstants.BATCH_WARNING;
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							// 加载沟通任务失败
							batchLoggerDTO.setCode("ECT00052");
							batchLoggerDTO.addParam(jobId);
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
						}
					}
				} catch (Exception e) {
					try {
						binBECTSMG02_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 把未过期未加载的沟通调度信息更新成已加载失败
					batchLoggerDTO.setCode("ECT00053");
					batchLoggerDTO.addParam(jobId);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				}
			}
			loadErrorCount = schedulesList.size() - loadSuccessCount;
		}
		
		// 任务加载成功件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("ICT00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(loadSuccessCount));
		
		// 任务加载失败件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("ICT00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(loadErrorCount));
		
		// 删除过期任务成功件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("ICT00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(delSuccessCount));
		
		// 删除过期任务失败件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("ICT00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(delErrorCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		cherryBatchLogger.BatchLogger(batchLoggerDTO4);
		
		return flag;
		
	}
	
	/**
	 * 查询调度信息List
	 * 
	 * @param map 查询条件
     * @return 调度信息List
	 */
	public List<Map<String, Object>> getJobList(Map<String, Object> map) {
		
		String brandCode = (String)map.get(CherryBatchConstants.BRAND_CODE);
		String[] jobNames = QuartzManager.getJobNames(brandCode);
		if(jobNames != null && jobNames.length > 0) {
			map.put("jobNames", jobNames);
			// 查询调度信息List
			List<Map<String, Object>> scheduleList = binBECTSMG02_Service.getMemorySchedulesList(map);
			List<Map<String, Object>> jobList = new ArrayList<Map<String,Object>>();
			for(String jobName : jobNames) {
				boolean flag = false;
				if(scheduleList != null && !scheduleList.isEmpty()) {
					for(int i = 0; i < scheduleList.size(); i++) {
						if(jobName.equals(scheduleList.get(i).get("schedulesId").toString())) {
							jobList.add(scheduleList.get(i));
							flag = true;
							break;
						}
					}
				}
				if(!flag) {
					Map<String, Object> jobMap = new HashMap<String, Object>();
					jobMap.put("schedulesId", jobName);
					jobMap.put("runTime", QuartzManager.getCronExpression(jobName, brandCode));
					jobList.add(jobMap);
				}
			}
			return jobList;
		} else {
			return null;
		}
	}
	
	/**
	 * 删除指定调度任务
	 * 
	 * @param map 删除条件
	 */
	public void tran_deleteJob(Map<String, Object> map) {
		
		// 删除数据库中的调度信息
		binBECTSMG02_Service.delExpiredSchedules(map);
		String brandCode = (String)map.get(CherryBatchConstants.BRAND_CODE);
		String jobId = (String)map.get("schedulesId");
		// 从内存中删除Job
		QuartzManager.disableSchedule(jobId, brandCode);
	}

}

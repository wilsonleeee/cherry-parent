/*	
 * @(#)BatchJobLog.java     1.0 2012/06/15		
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
package com.cherry.job.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;

/**
 * 
 *共通 记录Job运行日志
 * 
 * 
 * @author hub
 * @version 1.0 2012.06.15
 */
public class BatchJobLog implements FunctionProvider{
	
	private static Logger logger = LoggerFactory
	.getLogger(BatchJobLog.class.getName());
	
	@Resource
	private BINBECM01_IF binbecm01_IF;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织ID
		map.put("organizationInfoId", transientVars.get("organizationInfoId"));
		// 品牌ID
		map.put("brandInfoId", transientVars.get("brandInfoId"));
		// JOB代号
		String jobId = (String) transientVars.get(CherryBatchConstants.BATCH_JOB_ID);
		// JOB名称
		String jobName = (String) transientVars.get(CherryBatchConstants.BATCH_JOB_NAME);
		if (CherryChecker.isNullOrEmpty(jobId, true)) {
			WorkflowDescriptor workflowDescriptor = (WorkflowDescriptor)transientVars.get("descriptor");
			Map workflowMetas = workflowDescriptor.getMetaAttributes();
			if (null != workflowMetas) {
				jobId = (String) workflowMetas.get(CherryBatchConstants.BATCH_JOB_ID);
				jobName = (String) workflowMetas.get(CherryBatchConstants.BATCH_JOB_NAME);
			}
		}
		// JOB代号
		map.put("jobId", jobId);
		// JOB名称
		map.put("jobName", jobName);
		// 程序代号
		map.put("pgmId", CherryBatchConstants.PGM_BATCHJOBLOG);
		// 程序名称
		map.put("pgmName", CherryBatchConstants.PGM_BATCHJOBLOG);
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.PGM_BATCHJOBLOG);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.PGM_BATCHJOBLOG);
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, CherryBatchConstants.PGM_BATCHJOBLOG);
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, CherryBatchConstants.PGM_BATCHJOBLOG);
		List<Step> stepLists = (List)transientVars.get("currentSteps");
		WorkflowDescriptor workflowDescriptor = (WorkflowDescriptor)transientVars.get("descriptor");
		// STEP代号
		int stepId = 0;
		// STEP名称
		String stepName = null;
		for(Step stepList:stepLists){
			stepId = stepList.getStepId();
			stepName = workflowDescriptor.getStep(stepId).getName();
			break;
		}
		// STEP代号
		map.put("stepId", String.valueOf(stepId));
		// STEP名称
		map.put("stepName", stepName);
		// 日志状态
		String startFlag = (String) args.get("startFlag");
		// JOB开始
		if ("0".equals(startFlag)) {
			// 取得运行日志状态
			String preStatus = binbecm01_IF.getBatchLogStatus(map);
			if (CherryBatchConstants.JOBLOG_STATUS9.equals(preStatus)) {
				// 删除Job运行日志表
				binbecm01_IF.delBatchLog(map);
			} else {
				// STEP执行开始时间
				map.put("starTime", binbecm01_IF.getSYSDate());
				// Job运行状态: 进行中
				map.put("status", CherryBatchConstants.JOBLOG_STATUS2);
				// 删除Job运行日志表
				binbecm01_IF.delBatchLog(map);
				// 插入Job运行日志表
				binbecm01_IF.insertBatchLog(map);
			}
			// JOB结束
		} else if ("1".equals(startFlag)){
			int result = 0;
			try {
				result = ps.getInt("result");
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			// 取得运行日志状态
			String preStatus = binbecm01_IF.getBatchLogStatus(map);
			if (null != preStatus) {
				if (!CherryBatchConstants.JOBLOG_STATUS9.equals(preStatus)) {
					// STEP执行完了时间
					map.put("endTime", binbecm01_IF.getSYSDate());
					// Job运行状态
					String status = (result != -1)? CherryBatchConstants.JOBLOG_STATUS0 : CherryBatchConstants.JOBLOG_STATUS1;
					map.put("status", status);
					// 更新Job运行日志表
					binbecm01_IF.updateBatchLog(map);
				} else {
					// 删除Job运行日志表
					binbecm01_IF.delBatchLog(map);
				}
			}
		}
	}
}

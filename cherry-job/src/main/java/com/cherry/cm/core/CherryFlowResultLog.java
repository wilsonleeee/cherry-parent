/*
 * @(#)CherryFlowResultLog.java     1.0 2011/06/01
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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 
 *共通 打印执行结果信息
 * 
 * 
 * @author szg
 * @version 1.0 2011.06.01
 */
public class CherryFlowResultLog implements FunctionProvider {
	private static Logger logger = LoggerFactory
			.getLogger(CherryFlowResultLog.class.getName());

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		int result = -99;
		result = ps.getInt("result");
		if (result != -99) {

			if (result == -1) {
				logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$工作流动作执行结果:异常结束$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				//设置本工作流实例的执行结果为异常
				ps.setInt("TotalResult", -1);

			} else if (result == 1) {
				logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$工作流动作执行结果:有警告结束$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				//设置本工作流实例的执行结果为警告
				ps.setInt("TotalResult", 1);
			} else {
				logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$工作流动作执行结果:正常结束$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				//设置本工作流实例的执行结果为正常
				ps.setInt("TotalResult", 0);
			}
			WorkflowDescriptor workflowDescriptor = (WorkflowDescriptor)transientVars.get("descriptor");
			WorkflowEntry workflowEntry = (WorkflowEntry) transientVars.get("entry");
			logger.info("****  工作流名称:" + workflowEntry.getWorkflowName());
			logger.info("****  工作流实例:" + workflowEntry.getId());
			
			List<Step> stepLists = (List)transientVars.get("currentSteps");
			int stepId;
			for(Step stepList:stepLists){
				stepId = stepList.getStepId();
				logger.info("****  当前步骤:" +workflowDescriptor.getStep(stepId).getName()+"("+transientVars.get("currentSteps")+")");
			}
						
			int actionId = ((Integer)transientVars.get("actionId")).intValue();
			logger.info("****  当前完成动作:" + workflowDescriptor.getAction(actionId).getName()+"("+transientVars.get("actionId")+")");
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			
			//清除上次的执行结果
			ps.remove("result");
		}
	}
}
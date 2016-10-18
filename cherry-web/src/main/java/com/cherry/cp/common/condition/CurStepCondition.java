/*
 * @(#)CurStepCondition.java     1.0 2011/11/01
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
package com.cherry.cp.common.condition;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;

/**
 * 当前步骤验证 Condition
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class CurStepCondition implements Condition{
	
	protected static final Logger logger = LoggerFactory.getLogger(CurStepCondition.class);
	
	/**
	 * 
	 *验证是否是当前步骤
	 * @param Map
	 * @param Map
	 * @param PropertySet
	 * 
	 * @return boolean
	 * @throws WorkflowException
	 * @author hub
	 * @version 1.0 2011.11.01
	 */
	@Override
	public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			int stepId = 0;
			// 步骤ID
	        String stepIdVal = (String) args.get("stepId");
	        stepId = Integer.parseInt(stepIdVal);
			WorkflowDescriptor workflowDescriptor = (WorkflowDescriptor)transientVars.get("descriptor");
			List<Step> stepList = (List<Step>) transientVars.get("currentSteps");
			Step step = stepList.get(0);
			// 当前步骤ID
			int curStepId = step.getStepId();
			if (stepId == curStepId) {
				return true;
			}
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
        }
		return false;
	}

}

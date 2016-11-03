/*	
 * @(#)CherryFlowLogRegister.java     1.0 2010/10/12		
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

import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.util.TextUtils;
import com.opensymphony.workflow.Register;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.WorkflowEntry;

public class CherryFlowLogRegister implements Register{

	
	@Override
	public Object registerVariable(WorkflowContext context,
			WorkflowEntry entry, Map args, PropertySet ps)
			throws WorkflowException {
		 String workflowname = "unknown";
	        long workflow_id = -1;

	        if (entry != null) {
	            workflowname = entry.getWorkflowName();
	            workflow_id = entry.getId();
	        }

	        boolean groupByInstance = false;
	        String useInstance = (String) args.get("addInstanceId");

	        if (useInstance != null) {
	            groupByInstance = TextUtils.parseBoolean(useInstance);
	        }

	        String categoryName = "com.cherry.osworkflow";

	        if (args.get("Category") != null) {
	            categoryName = (String) args.get("Category");
	        }

	        String category = categoryName + "." + workflowname;

	        if (groupByInstance) {
	            category += ("." + (Long.toString(workflow_id)));
	        }

	        Logger log = LoggerFactory.getLogger(category);

	        return log;
	}

}

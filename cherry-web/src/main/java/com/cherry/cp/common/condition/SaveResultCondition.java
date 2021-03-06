/*
 * @(#)SaveResultCondition.java     1.0 2011/05/31
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

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

public class SaveResultCondition implements Condition{
	
	/**
	 * 
	 *共通 结果条件判定处理，如果执行结果与Save_status一致，返回True；否则返回False
	 * @param Map
	 * @param Map
	 * @param PropertySet
	 * 
	 * @return boolean
	 * @throws WorkflowException
	 * @author hub
	 * @version 1.0 2011.05.31
	 */
	@Override
	public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		int result = -99;
		if (null != transientVars.get("SaveStatus")) {
			result = Integer.parseInt(transientVars.get("SaveStatus").toString());
		}
		boolean flag = Boolean.FALSE;
		if(result == (Integer.parseInt((String)args.get("Save_status")))){			
			flag = Boolean.TRUE;
			}
		else{
			flag = Boolean.FALSE;
		}
		
		return flag;
	}
}

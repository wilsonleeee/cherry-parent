/*  
 * @(#)BINOLBSRES02_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.res.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.res.interfaces.BINOLBSRES02_IF;
import com.cherry.cm.core.BaseAction;

public class BINOLBSRES02_Action extends BaseAction {


	private static final long serialVersionUID = 8204570523907177638L;
	@Resource
	private BINOLBSRES02_IF binolbsres02if;

	private int resellerInfoId;

	@SuppressWarnings("rawtypes")
	private Map resellerDetail;

	@SuppressWarnings("rawtypes")
	public void setResellerDetail(Map resellerDetail) {
		this.resellerDetail = resellerDetail;
	}

	@SuppressWarnings("rawtypes")
	public Map getResellerDetail() {
		return resellerDetail;
	}

	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resellerInfoId", resellerInfoId);
		resellerDetail = binolbsres02if.resellerDetail(map);
		return SUCCESS;
	}

	public int getResellerInfoId() {
		return resellerInfoId;
	}

	public void setResellerInfoId(int resellerInfoId) {
		this.resellerInfoId = resellerInfoId;
	}
}
/*  
 * @(#)BaseAction.java     1.0 2011/05/31      
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
/**
 * Dao基类接口
 * 
 * @author dingyc
 * @version 
 */
public class BaseAction extends ActionSupport implements SessionAware,ServletRequestAware,
ServletResponseAware,CookiesAware{
	private static final long serialVersionUID = -3234622489225525601L;
	protected Map<String, Object> session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	@Override
	public void setSession(Map<String, Object> argSession) {
		
		this.session = argSession;
	}	
	@Override
	public void setServletRequest(HttpServletRequest argRequest) {
		
		this.request = argRequest;
	}

	@Override
	public void setCookiesMap(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServletResponse(HttpServletResponse argresponse) {
		
		this.response = argresponse;
	}
}

/*  
 * @(#)DemoAction.java     1.0 2011/05/31      
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
package com.cherry.ps.act.action;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class DemoAction extends ActionSupport{
	private String message;
	public void setMessage(String message){
		this.message=message;
	}
	public String getMessage(){
		return message;
	}
	
	public String execute(){
		message="";
		return SUCCESS;
	}
}

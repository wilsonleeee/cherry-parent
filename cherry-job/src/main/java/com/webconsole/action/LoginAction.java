/*  
 * @(#)LoginAction.java     1.0 2011/05/31      
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
package com.webconsole.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.BatchWebException;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.CustomerWitContextHolder;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.webconsole.bl.LoginBusinessLogic;

@SuppressWarnings("serial")
public  class LoginAction extends BaseAction {

	@Resource
    private LoginBusinessLogic loginbusinesslogic;  
	private String codeText ;	
	public String doLogin() throws Exception {
		try {
			
			if (request.getParameterMap().containsKey("codeText")) {
				ImageCaptchaService instance = (ImageCaptchaService) session
						.get("sessioncheckimage");
				boolean bl = instance.validateResponseForID(request
						.getSession().getId(), codeText);
				if (!bl) {
					request.setAttribute("validateimage", "true");
					this.addActionError("Wrong Security Code!Please input again!");
					return "false";
				}
			}
			String language = "";
			language = "zh_CN";
			
			
			// 将语言放入session中
			//session.put(CherryConstants.SESSION_LANGUAGE, language);
			
			Map<String,String> retMap  = loginbusinesslogic.userLogin(txtname);
			
			//datasource
			session.put(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY,retMap.get("NewDataSource"));
			//session.put(CherryConstants.CHERRY_WIT_SECURITY_CONTEXT_KEY,retMap.get("OldDataSource"));

			// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
			CustomerContextHolder.setCustomerDataSourceType(retMap.get("NewDataSource"));
			//CustomerWitContextHolder.setCustomerWitDataSourceType(retMap.get("OldDataSource"));
			
			String csrftoken = (String)session.get("csrftoken");
			UserInfo userinfo  = loginbusinesslogic.checkUser(txtname, txtpsd, csrftoken);
			userinfo.setLoginIP(request.getRemoteHost());
			userinfo.setLoginTime(new Date());
		    userinfo.setUserAgent(request.getHeader("User-Agent"));
			session.put(CherryConstants.SESSION_USERINFO, userinfo);
			return "OK";

		} catch (Exception ex) {
			String message ="";
			if(ex instanceof BatchWebException){
				String errcode = ((BatchWebException)ex).getErrCode();
				if("E001".equals(errcode)){
					message="账号或密码错误";
				}else if("E002".equals(errcode)){
					message="没有权限运行Batch";
				}
			}else{
				message = ex.getMessage();
			}
			this.addActionError(message);
			return "false";
		}
	}
 
    /*
     * 初始化登录页面
     */
    public String initialLogin(){
    	//TODO:英文版暂时没有完成，固定设为中文，后期完成后将该行代码去掉
    	String language = "zh_CN";
    	if (session.get("WW_TRANS_I18N_LOCALE") == null) {
			//language = request.getLocale().toString();
		} else {
			language = String.valueOf(session.put("WW_TRANS_I18N_LOCALE",language));
		} 		
		// 将语言放入session中
		session.put(CherryConstants.SESSION_LANGUAGE, language);
		//TODOEND
		csrftoken = UUID.randomUUID().toString().replace("-", "");
		session.put("csrftoken",csrftoken);
		
        return SUCCESS;
    }
    
    /**
     * 用戶名
     */
    private String txtname;
    
    /**
     * 密碼
     */
    private String txtpsd;
    
    private String csrftoken;
    
    /**
	 * @return the txtpsd
	 */
	public String getTxtpsd() {
		return txtpsd;
	}
	/**
	 * @param txtpsd the txtpsd to set
	 */
	public void setTxtpsd(String txtpsd) {
		this.txtpsd = txtpsd;
	}
	/**
     * @return the txtname
     */
    public String getTxtname() {
        return txtname;
    }
    /**
     * @param txtname the txtname to set
     */
    public void setTxtname(String txtname) {
        this.txtname = txtname;
    }
  
//    private List<TaskSummary> taskList;
//    
//    /**
//	 * @return the taskList
//	 */
//	public List<TaskSummary> getTaskList() {
//		return taskList;
//	}
//	/**
//	 * @param taskList the taskList to set
//	 */
//	public void setTaskList(List<TaskSummary> taskList) {
//		this.taskList = taskList;
//	}

	public String getCsrftoken() {
		return csrftoken;
	}

	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

	/**
	 * @return the codeText
	 */
	public String getCodeText() {
		return codeText;
	}

	/**
	 * @param codeText the codeText to set
	 */
	public void setCodeText(String codeText) {
		this.codeText = codeText;
	}
	

}

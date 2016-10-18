/*  
 * @(#)CherryLogger.java     1.0 2011/05/31      
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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.mongo.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class CherryLogger {//implements ServletContextListener{	

	public static final String logCollName = "MGO_Log";
	
	private DBObject dbObject ;	

	public void access(String action,long costtime,String comment) {
		String orgCode ="0";
		String brandCode ="0";
		String bIN_EmployeeID ="0";
		String loginName="0";
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String userAgent = request.getHeader("User-Agent");
		Object o =request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
		if(o!=null){
			UserInfo user = (UserInfo)o;
			orgCode= user.getOrgCode();
			brandCode=user.getBrandCode();
			loginName=user.getLoginName();
			bIN_EmployeeID = String.valueOf(user.getBIN_EmployeeID());
		}

		dbObject = new BasicDBObject();
		dbObject.put("OrgCode", orgCode);
		dbObject.put("BrandCode", brandCode);
		dbObject.put("AccessTime", getTime());
		dbObject.put("LoginName", loginName);
		dbObject.put("BIN_EmployeeID", bIN_EmployeeID);		
		dbObject.put("Action", action);	
		dbObject.put("CostTime", costtime);	
		dbObject.put("Comment", comment);
		dbObject.put("UserAgent", userAgent);
		dbObject.put("RemoteIP", getUserIP());
		try {
			WriteMongoLog wr = new WriteMongoLog(dbObject);
			Thread t = new Thread(wr);
			t.start();
			//MongoDB.insert(logCollName, dbObject);
			
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			//throw new CherryException("ECM00004",ex);
		} 
	}
	
	private String getTime(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		return bartDateFormat.format(new Date());
	}
	
	private String getUserName(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Object o =request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
		String userName = "unLogin";
		if(o!=null){
			userName = ((UserInfo)o).getLoginName();
		}	
		return userName;
	}
	
	private String getUserIP(){
		HttpServletRequest request = ServletActionContext.getRequest();
		//HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
		String ip = request.getHeader("x-forwarded-for");
		if (CherryChecker.isNullOrEmpty(ip)) {
			ip = request.getRemoteAddr();
		} 	
		if(CherryChecker.isNullOrEmpty(ip)){
			ip = "0.0.0.0";
		}			
		return ip;
	}
	
	private String getUserAgent(){
		HttpServletRequest request = ServletActionContext.getRequest();
		//HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
		String ret = request.getHeader("User-Agent");
		return ret;
	}
//	@Override
//	public void contextDestroyed(ServletContextEvent servletcontextevent) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void contextInitialized(ServletContextEvent servletcontextevent) {
//		String temp = servletcontextevent.getServletContext().getInitParameter("cherryLogLevel");		 
//		if(temp.equals("INFO")){
//			logLevel = INFO; 
//		}else if(temp.equals("DEBUG")){
//			logLevel = DEBUG;
//		}else if(temp.equals("ACCESS")){
//			logLevel = ACCESS;
//		}else if(temp.equals("WARNING")){
//			logLevel = WARNING;
//		}else if(temp.equals("ERROR")){
//			logLevel = ERROR;
//		}else if(temp.equals("NONE")){
//			logLevel = NONE;
//		}
//	}
}
class WriteMongoLog implements Runnable { 
    private DBObject dbObject; 

    public WriteMongoLog(DBObject argDbObject) { 
        this.dbObject = argDbObject; 
    } 

    public void run() { 
    	try {
			MongoDB.insert(CherryLogger.logCollName, dbObject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
}
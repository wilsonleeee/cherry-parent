/*  
 * @(#)CherryException.java     1.0 2011/05/31      
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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 自定义异常类
 * 
 * @author dingyc
 * @date 2010-08-24
 * @version 
 */
public class BatchWebException extends Exception implements Serializable{
	
	//自定义异常ID
	private String errCode;
	//待包装的异常
	private Exception innerException = null;
	/* 
	 * 构造函数
	 * @param argErrCode 
	 *        异常ID
	 */
     public BatchWebException(String argErrCode){
    	 errCode = argErrCode;    	 
     }
  	/* 
  	 * 构造函数(用于包装JDK的异常)
  	 * @param argErrCode 
  	 *        异常ID
  	 * @param ex
  	 *        原始的异常       
  	 */
       public BatchWebException(String argErrCode,Exception ex){
    	  errCode = argErrCode;    	 
     	  innerException = ex;  
       }
	/**
	 * @return the errCode
	 */
	public String getErrCode() {
		return errCode;
	}
	/**
	 * @param errCode the errCode to set
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	/**
	 * @return the innerException
	 */
	public Exception getInnerException() {
		return innerException;
	}
	/**
	 * @param innerException the innerException to set
	 */
	public void setInnerException(Exception innerException) {
		this.innerException = innerException;
	}
   
}

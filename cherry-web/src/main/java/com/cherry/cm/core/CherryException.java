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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 自定义异常类
 * 
 * @author dingyc
 * @date 2010-08-24
 * @version
 */
public class CherryException extends Exception implements TextProvider,
		Serializable, LocaleProvider {

	// 自定义异常ID
	private String errCode;

	// 自定义异常的消息内容
	private String errMessage;

	// private String occurdTime;
	// 待包装的异常
	private Exception innerException = null;
	private Container container;
	private transient TextProvider textProvider;

	/*
	 * 构造函数
	 * 
	 * @param argErrCode 异常ID
	 */
	public CherryException(String argErrCode) {
		errCode = argErrCode;
		try {
			errMessage = getText(argErrCode);
		} catch (Exception ex) {
			errMessage = LocalizedTextUtil.findDefaultText(argErrCode, Locale.CHINA);
		}
		if (errMessage == null || "null".equals(errMessage)
				|| argErrCode.equals(errMessage)) {
			errMessage = argErrCode + ":Can not find the error message.";
		}
		this.printStackTrace();
		// innerException = new Exception(errMessage);
	}

	/*
	 * 构造函数
	 * 
	 * @param argErrCode 异常ID
	 * 
	 * @param argArr 要进行置换的内容
	 */
	public CherryException(String argErrCode, String[] argArr) {
		errCode = argErrCode;
		try {
			errMessage = getText(argErrCode, argArr);
		} catch (Exception ex) {
			errMessage = LocalizedTextUtil.findDefaultText(argErrCode, Locale.CHINA,argArr);
		}
		if (errMessage == null || "null".equals(errMessage)
				|| argErrCode.equals(errMessage)) {
			errMessage = argErrCode + ":Can not find the error message.";
		}
		//this.printStackTrace();
	}

	/*
	 * 构造函数(用于包装JDK的异常)
	 * 
	 * @param argErrCode 异常ID
	 * 
	 * @param ex 原始的异常
	 */
	public CherryException(String argErrCode, Exception ex) {
		errCode = argErrCode;
		try {
			errMessage = getText(argErrCode);
		} catch (Exception e) {
			errMessage = LocalizedTextUtil.findDefaultText(argErrCode, Locale.CHINA);
		}
		if (errMessage == null || "null".equals(errMessage)
				|| argErrCode.equals(errMessage)) {
			errMessage = argErrCode + ":Can not find the error message.";
		}
		innerException = ex;
		this.printStackTrace();
	}

	/*
	 * 构造函数(用于包装JDK的异常)
	 * 
	 * @param argErrCode 异常ID
	 * 
	 * @param argArr 要进行置换的内容
	 * 
	 * @param ex 原始的异常
	 */
	public CherryException(String argErrCode, String[] argArr, Exception ex) {

		errCode = argErrCode;
		try {
			errMessage = getText(argErrCode, argArr);
		} catch (Exception e) {
			errMessage = LocalizedTextUtil.findDefaultText(argErrCode, Locale.CHINA,argArr);
		}
		if (errMessage == null || "null".equals(errMessage)
				|| argErrCode.equals(errMessage)) {
			errMessage = argErrCode + ":Can not find the error message.";
		}
		innerException = ex;
		this.printStackTrace();
	}

	/**
	 * @return the errCode
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * @return the errMessage
	 */
	public String getErrMessage() {
		return errMessage;
	}

	/**
	 * @return the innerException
	 */
	public Exception getInnerException() {
		return innerException;
	}

	public boolean hasKey(String key) {
		return getTextProvider().hasKey(key);
	}

	public String getText(String aTextName) {
		return getTextProvider().getText(aTextName);
	}

	public String getText(String aTextName, String defaultValue) {
		return getTextProvider().getText(aTextName, defaultValue);
	}

	public String getText(String aTextName, String defaultValue, String obj) {
		return getTextProvider().getText(aTextName, defaultValue, obj);
	}

	public String getText(String aTextName, List<?> args) {
		return getTextProvider().getText(aTextName, args);
	}

	public String getText(String key, String[] args) {
		return getTextProvider().getText(key, args);
	}

	public String getText(String aTextName, String defaultValue,
			List<?> args) {
		return getTextProvider().getText(aTextName, defaultValue, args);
	}

	public String getText(String key, String defaultValue, String[] args) {
		return getTextProvider().getText(key, defaultValue, args);
	}

	public String getText(String key, String defaultValue, List<?> args,
			ValueStack stack) {
		return getTextProvider().getText(key, defaultValue, args, stack);
	}

	public String getText(String key, String defaultValue, String[] args,
			ValueStack stack) {
		return getTextProvider().getText(key, defaultValue, args, stack);
	}

	public ResourceBundle getTexts() {
		return getTextProvider().getTexts();
	}

	public ResourceBundle getTexts(String aBundleName) {
		return getTextProvider().getTexts(aBundleName);
	}

	private TextProvider getTextProvider() {
		if (textProvider == null) {
			TextProviderFactory tpf = new TextProviderFactory();
			if (container != null) {
				container.inject(tpf);
			}
			textProvider = tpf.createInstance(getClass(), this);
		}
		return textProvider;
	}

	@Inject
	public void setContainer(Container container) {
		this.container = container;
	}

	public Locale getLocale() {

		ActionContext ctx = ActionContext.getContext();
		if (ctx != null) {
			return ctx.getLocale();
		} else {
			return null;
		}
	}

	/**
	 * @param errMessage
	 *            the errMessage to set
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	
	public String getMessage(){
		return errMessage;
	}
}

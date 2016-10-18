/*  
 * @(#)PropertiesFile.java     1.0 2012/04/19      
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
package com.cherry.cm.util;

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
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * 资源文件管理类
 * 
 * @author hub
 * 
 */
public class PropertiesFile implements TextProvider, LocaleProvider, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1945229504411819598L;

	private transient TextProvider textProvider;
	
	private Container container;
	
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

     public String getText(String aTextName, String defaultValue, List<?> args) {
         return getTextProvider().getText(aTextName, defaultValue, args);
     }

     public String getText(String key, String defaultValue, String[] args) {
         return getTextProvider().getText(key, defaultValue, args);
     }

     public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
         return getTextProvider().getText(key, defaultValue, args, stack);
     }

     public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
         return getTextProvider().getText(key, defaultValue, args, stack);
     }
     public ResourceBundle getTexts() {
         return getTextProvider().getTexts();
     }

     public ResourceBundle getTexts(String aBundleName) {
         return getTextProvider().getTexts(aBundleName);
     }

	@Override
	public Locale getLocale() {
		 ActionContext ctx = ActionContext.getContext();
	        if (ctx != null) {
	            return ctx.getLocale();
	        } else {
	            //LOG.debug("Action context not initialized");
	            return null;
	     }
	}

}

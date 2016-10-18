/*
 * @(#)PlIndexAction.java     1.0 2010/10/27
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

package com.cherry.pl.common;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMenu;

/**
 * 权限管理首页Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class PlIndexAction extends BaseAction{
	
	private static final long serialVersionUID = 7830040462314710948L;

	/**
	 * action名称
	 */
	private String actionName;
	/**
	 * 权限管理首页初期化
	 * 
	 * @return String 
	 */
	public String init(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		
		Map<String, Object> xmldocumentmap = (Map<String, Object>)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		CherryMenu doc = (CherryMenu)xmldocumentmap.get(request.getParameter("MENU_ID"));
		String url = "";
		if(doc.getChildList()!=null && doc.getChildList().size()>0){
			url=doc.getChildList().get(0).getMenuURL();
		}
		this.setActionName(url);
		return SUCCESS;
	}
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}
	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

}

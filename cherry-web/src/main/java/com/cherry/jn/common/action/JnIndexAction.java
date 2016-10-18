/*	
 * @(#)JnIndexAction.java     1.0 2011/4/18		
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
package com.cherry.jn.common.action;

import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

public class JnIndexAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7947960668524163020L;
	
	public String initial(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}
}

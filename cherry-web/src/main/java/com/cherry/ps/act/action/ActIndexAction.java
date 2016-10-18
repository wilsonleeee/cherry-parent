/*  
 * @(#)ActIndexAction.java     1.0 2011/05/31      
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

import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;


/**
 * �½��Action
 * @author huzude
 * @version 1.0 2010.08.06
 */
@SuppressWarnings("unchecked")
public class ActIndexAction extends BaseAction{
	
	public String actindex(){
		//request.getSession().getAttribute("", "");
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return "OK";
	}
}

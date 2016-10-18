/*
 * @(#)MBIndexAction.java     1.0 2011/03/22
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

package com.cherry.mb.common;

import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

/**
 * 会员管理首页Action
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class MBIndexAction extends BaseAction {
	
	private static final long serialVersionUID = 1201970655404899086L;

	/**
	 * 权限管理首页初期化
	 * 
	 * @return String 
	 */
	public String init(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}

}

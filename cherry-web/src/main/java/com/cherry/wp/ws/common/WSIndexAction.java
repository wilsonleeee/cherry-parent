package com.cherry.wp.ws.common;

import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

public class WSIndexAction extends BaseAction {
	
	private static final long serialVersionUID = 7526868753363576256L;

	/**
	 * 库存管理首页初期化
	 * 
	 * @return 库存管理首页
	 */
	public String init(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}

}

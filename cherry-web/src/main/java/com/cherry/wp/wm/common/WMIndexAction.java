package com.cherry.wp.wm.common;

import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

/**
 * 会员管理首页Action
 * 
 * @author WangCT
 * @version 1.0 2014.10.24
 */
public class WMIndexAction extends BaseAction {
	
	private static final long serialVersionUID = -2024076442630353495L;

	/**
	 * 会员管理首页初期化
	 * 
	 * @return 会员管理首页
	 */
	public String init(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}

}

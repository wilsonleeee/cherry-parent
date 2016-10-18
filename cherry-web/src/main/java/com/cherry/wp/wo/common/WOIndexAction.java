package com.cherry.wp.wo.common;

import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

/**
 * 日常管理首页Action
 * 
 * @author WangCT
 * @version 1.0 2014.09.16
 */
public class WOIndexAction extends BaseAction {
	
	private static final long serialVersionUID = -191975809224803203L;

	/**
	 * 日常管理首页初期化
	 * 
	 * @return 日常管理首页
	 */
	public String init(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}

}

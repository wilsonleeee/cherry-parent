package com.cherry.wp.wr.common;

import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

/**
 * 门店报表首页Action
 * 
 * @author WangCT
 * @version 1.0 2014.09.09
 */
public class WRIndexAction extends BaseAction {
	
	private static final long serialVersionUID = -1387136200817753211L;

	/**
	 * 门店报表首页初期化
	 * 
	 * @return 门店报表首页 
	 */
	public String init(){
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}

}

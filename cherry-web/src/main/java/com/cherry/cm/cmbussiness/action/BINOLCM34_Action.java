/*	
 * @(#)BINOLCM34_Action.java     1.0 @2013-1-8		
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
package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM34_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.opensymphony.xwork2.ActionContext;

/**
 *
 * 帮助Action
 *
 * @author jijw
 *
 * @version  2013-1-8
 */
public class BINOLCM34_Action extends BaseAction    {

	private static final long serialVersionUID = -8965900381660264967L;
	
	@Resource(name="binOLCM34_BL")
	private BINOLCM34_IF binOLCM34_BL;
	
	/** 返回当前非noHelp页面ID动态值 **/
	private String pageIDOfCurrentMenu;
	
	/** 返回帮助页面对应的语言 **/
	private String cherry_language;

	/**
	 * 弹出当前页面对应的帮助页面
	 * 注：弹出相应帮助页面的jsp文件名前缀必须以当前页面ID命名，否则无法相应帮助页面且会出现异常
	 * @return
	 * @throws Exception
	 */
	public String getHelp() throws Exception {
		
		// 定义初始返回页面
		String result = "noHelp";
		
		// 根据当前国际化语言确定返回的帮助页面国际化部分
		cherry_language = String.valueOf(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		
		// 当前页面ID
		String currentMenuID = request.getParameter("currentMenuID");
		
		// 是否拥有帮助菜单  0：没有；1：有
		Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put("menuId", currentMenuID);
		String helpFlag = binOLCM34_BL.getHelpFlagByMenu(pramMap);
		
		if(null != helpFlag && "1".equals(helpFlag)){
			pageIDOfCurrentMenu = currentMenuID;
			result = "currentMenuID";
		} 
		
		return result;
	}
	
	public String getPageIDOfCurrentMenu() {
		return pageIDOfCurrentMenu;
	}

	public void setPageIDOfCurrentMenu(String pageIDOfCurrentMenu) {
		this.pageIDOfCurrentMenu = pageIDOfCurrentMenu;
	}
	
	public String getCherry_language() {
		return cherry_language;
	}

	public void setCherry_language(String cherry_language) {
		this.cherry_language = cherry_language;
	}
	
}

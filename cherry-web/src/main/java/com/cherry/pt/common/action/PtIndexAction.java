/*  
 * @(#)PtIndexAction.java     1.0 2011/05/31      
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
package com.cherry.pt.common.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM02_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM02_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;


/**
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class PtIndexAction extends BaseAction implements ModelDriven<BINOLCM02_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 基础模块POPUP画面共通Form */
	private BINOLCM02_Form form = new BINOLCM02_Form();
	
	/** 基础模块POPUP画面共通BL */
	@Resource
	private BINOLCM02_BL binOLCM02_BL;
	
	public String initial(){
		//request.getSession().getAttribute("", "");
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}

	public String popCounter() throws Exception{
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得部门总数
		map.put("businessType", 1);
		int count = binOLCM02_BL.getDepartInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门List
			form.setDepartInfoList(binOLCM02_BL.getDepartInfoList(map));
		}
		return SUCCESS;
	}
	@Override
	public BINOLCM02_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
}

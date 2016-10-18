/*  
 * @(#)MoIndexAction.java     1.0 2011/05/31      
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
package com.cherry.mo.common.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.common.form.BINOLBSCOM01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.opensymphony.xwork2.ModelDriven;


/**
 * @author dingyc
 *
 */
public class MoIndexAction extends BaseAction implements ModelDriven<BINOLBSCOM01_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 基础模块POPUP画面共通Form */
	private BINOLBSCOM01_Form form = new BINOLBSCOM01_Form();
	
	/** 基础模块POPUP画面共通BL */
	@Resource
	private BINOLMOCOM01_BL binOLMOCOM01_BL;

	public String initial(){
		//request.getSession().getAttribute("", "");
		request.getSession().setAttribute(CherryConstants.SESSION_TOPMENU_CURRENT, request.getParameter("MENU_ID"));
		Map map = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
		request.getSession().setAttribute(CherryConstants.SESSION_LEFTMENUALL_CURRENT, map.get(request.getParameter("MENU_ID")));
		return SUCCESS;
	}
	
	public String popEmployee(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户登录时
		if (form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId()) 
				&& CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}else{
			map.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("employeeKw", form.getSSearch());
		}
		// 员工节点
		if(form.getEmpPath() != null && !"".equals(form.getEmpPath())) {
			map.put("empPath", form.getEmpPath());
		}
		// 员工ID
		if(form.getEmployeeId() != null && !"".equals(form.getEmployeeId())) {
			map.put("employeeId", form.getEmployeeId());
		}
		// 岗位类别存在的场合
		if(form.getPositionCategoryId() != null && !"".equals(form.getPositionCategoryId())) {
			map.put("positionCategoryId", form.getPositionCategoryId());
			if(form.getFromPage() != null) {
				map.put("likeEmpGrade", binOLMOCOM01_BL.getPosCategoryGrade(map));
			} else {
				map.put("higherGrade", binOLMOCOM01_BL.getPosCategoryGrade(map));
			}
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得用户总数
		int count = binOLMOCOM01_BL.getEmployeeCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得用户List
			form.setEmployeeList(binOLMOCOM01_BL.getEmployeeList(map));
		}
		if(form.getFromPage() != null) {
			return "likeEmployee";
		}
		return SUCCESS;
	}
	
	@Override
	public BINOLBSCOM01_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
}

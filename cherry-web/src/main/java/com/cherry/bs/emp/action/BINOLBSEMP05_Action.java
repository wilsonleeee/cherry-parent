/*	
 * @(#)BINOLBSEMP05_Action.java     1.0 2011.05.17	
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

package com.cherry.bs.emp.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.emp.bl.BINOLBSEMP05_BL;
import com.cherry.bs.emp.form.BINOLBSEMP05_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 停用启用员工Action
 * 
 * @author WangCT
 * @version 1.0 2011.05.17
 */
public class BINOLBSEMP05_Action extends BaseAction implements ModelDriven<BINOLBSEMP05_Form> {

	private static final long serialVersionUID = -1594391267130244552L;
	
	/** 停用启用员工Form */
	private BINOLBSEMP05_Form form = new BINOLBSEMP05_Form();
	
	/** 停用启用员工BL */
	@Resource
	private BINOLBSEMP05_BL binOLBSEMP05_BL;
	
	/**
	 * 
	 * 停用启用柜台处理
	 * 
	 * @param 无
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String delEmployee() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		//组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 组织代号
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		//登录名
		map.put("loginName", userInfo.getLoginName());
		//品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		//创建者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		//创建程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSEMP05");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP05");
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 停用启用员工处理
		binOLBSEMP05_BL.tran_updateEmployee(map);
		return null;
	}

	@Override
	public BINOLBSEMP05_Form getModel() {
		return form;
	}

}

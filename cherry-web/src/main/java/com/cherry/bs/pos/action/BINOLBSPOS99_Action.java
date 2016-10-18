/*  
 * @(#)BINOLBSPOS99_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.pos.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS99_BL;
import com.cherry.bs.pos.form.BINOLBSPOS99_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLBSPOS99_Action extends BaseAction implements ModelDriven<BINOLBSPOS99_Form> {

	private static final long serialVersionUID = 1956568117809644560L;
	
	private BINOLBSPOS99_Form form = new BINOLBSPOS99_Form();
	
	@Resource
	private BINOLBSPOS99_BL binOLBSPOS99_BL;
	
	/**
	 * 员工一览画面生成处理
	 * 
	 * @return 员工一览画面
	 */
	public String popEmployee() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("employeeKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得员工总数
		int count = binOLBSPOS99_BL.getEmployeeCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得员工List
			form.setEmployeeList(binOLBSPOS99_BL.getEmployeeList(map));
		}
		return SUCCESS;
	}

	@Override
	public BINOLBSPOS99_Form getModel() {
		return form;
	}

}

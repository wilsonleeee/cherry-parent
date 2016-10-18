/*
 * @(#)BINOLBSDEP05_Action.java     1.0 2010/10/27
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

package com.cherry.bs.dep.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP05_BL;
import com.cherry.bs.dep.form.BINOLBSDEP05_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 停用启用部门Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP05_Action extends BaseAction implements ModelDriven<BINOLBSDEP05_Form> {

	private static final long serialVersionUID = -6148850221244642420L;
	
	/** 停用启用部门Form */
	private BINOLBSDEP05_Form form = new BINOLBSDEP05_Form();
	
	/** 停用启用部门BL */
	@Resource
	private BINOLBSDEP05_BL binOLBSDEP05_BL;
	
	/**
	 * 
	 * 停用启用部门处理
	 * 
	 * @param 无
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String delDepart() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSCNT05");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT05");
		// 停用启用柜台处理
		binOLBSDEP05_BL.tran_updateDepartInfo(map);
		return null;
	}

	@Override
	public BINOLBSDEP05_Form getModel() {
		return form;
	}

}

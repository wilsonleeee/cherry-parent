/*
 * @(#)BINOLBSREG05_Action.java     1.0 2011/11/23
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
package com.cherry.bs.reg.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.form.BINOLBSREG05_Form;
import com.cherry.bs.reg.interfaces.BINOLBSREG05_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 区域 启用停用画面Action
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG05_Action extends BaseAction implements ModelDriven<BINOLBSREG05_Form> {
	
	private static final long serialVersionUID = 5434532286642869626L;

	/** 区域 启用停用画面BL */
	@Resource
	private BINOLBSREG05_IF binOLBSREG05_BL;
	
	/**
	 * 
	 * 启用停用区域处理
	 * 
	 * @param 无
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String updRegValidFlag() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSREG05");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSREG05");
		// 启用停用区域处理
		binOLBSREG05_BL.tran_updateRegValidFlag(map);
		return null;
	}
	
	/** 区域 启用停用画面Form */
	private BINOLBSREG05_Form form = new BINOLBSREG05_Form();

	@Override
	public BINOLBSREG05_Form getModel() {
		return form;
	}

}

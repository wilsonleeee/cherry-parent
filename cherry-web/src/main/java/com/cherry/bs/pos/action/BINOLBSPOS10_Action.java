/*
 * @(#)BINOLBSPOS10_Action.java     1.0 2011/11/04
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

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS10_BL;
import com.cherry.bs.pos.form.BINOLBSPOS10_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 启用停用岗位画面Action
 * 
 * @author WangCT
 * @version 1.0 2011/11/04
 */
public class BINOLBSPOS10_Action extends BaseAction implements ModelDriven<BINOLBSPOS10_Form> {
	
	private static final long serialVersionUID = -6912361632168804619L;
	
	/** 启用停用岗位画面BL */
	@Resource
	private BINOLBSPOS10_BL binOLBSPOS10_BL;
	
	/**
	 * 
	 * 停用启用岗位处理
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String delPosition() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSPOS10");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPOS10");
		// 启用停用岗位处理
		binOLBSPOS10_BL.tran_updatePosition(map);
		return null;
	}
	
	/** 启用停用岗位画面Form */
	private BINOLBSPOS10_Form form = new BINOLBSPOS10_Form();

	@Override
	public BINOLBSPOS10_Form getModel() {
		return form;
	}

}

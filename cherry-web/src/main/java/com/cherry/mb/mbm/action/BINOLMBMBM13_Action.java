/*
 * @(#)BINOLMBMBM13_Action.java     1.0 2013/04/11
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
package com.cherry.mb.mbm.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mb.mbm.bl.BINOLMBMBM13_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM13_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员资料修改履历明细画面Action
 * 
 * @author WangCT
 * @version 1.0 2013/04/11
 */
public class BINOLMBMBM13_Action extends BaseAction implements ModelDriven<BINOLMBMBM13_Form> {

	private static final long serialVersionUID = 7572985272262788070L;
	
	/** 会员资料修改履历明细画面BL **/
	@Resource
	private BINOLMBMBM13_BL binOLMBMBM13_BL;
	
	/**
	 * 会员资料修改履历查询画面初期处理
	 * 
	 * @return 会员资料修改履历查询画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 所属品牌Code
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		map.put("language", language);
		// 查询会员资料修改履历
		memInfoRecordInfo = binOLMBMBM13_BL.getMemInfoRecordInfo(map);
		
		return SUCCESS;
	}
	
	/** 会员资料修改履历 */
	private Map memInfoRecordInfo;
	
	public Map getMemInfoRecordInfo() {
		return memInfoRecordInfo;
	}

	public void setMemInfoRecordInfo(Map memInfoRecordInfo) {
		this.memInfoRecordInfo = memInfoRecordInfo;
	}

	/** 会员资料修改履历查询画面Form */
	private BINOLMBMBM13_Form form = new BINOLMBMBM13_Form();

	@Override
	public BINOLMBMBM13_Form getModel() {
		return form;
	}

}

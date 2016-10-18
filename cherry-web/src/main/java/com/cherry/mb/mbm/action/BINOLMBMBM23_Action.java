/*
 * @(#)BINOLMBMBM23_Action.java     1.0 2013.08.29
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM23_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM23_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员短信沟通明细画面Action
 * 
 * @author WangCT
 * @version 1.0 2013.08.29
 */
public class BINOLMBMBM23_Action extends BaseAction implements ModelDriven<BINOLMBMBM23_Form> {
	
	private static final long serialVersionUID = -2841655391638510305L;
	
	/** 会员短信沟通明细画面BL */
	@Resource
	private BINOLMBMBM23_BL binOLMBMBM23_BL;
	
	/**
	 * 会员短信沟通明细画面
	 * 
	 * @return 会员短信沟通明细画面
	 */
	public String init() throws Exception {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		form.setBrandInfoId(String.valueOf(userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}
	
	/**
	 * AJAX取得会员短信沟通明细信息
	 * 
	 * @return 会员短信沟通明细画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 取得会员短信沟通总数
		int count = binOLMBMBM23_BL.getSmsSendDetailCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得会员短信沟通List
			smsSendDetailList = binOLMBMBM23_BL.getSmsSendDetailList(map);
		}
		return SUCCESS;
	}
	
	/** 会员短信沟通List **/
	private List<Map<String, Object>> smsSendDetailList;
	
	public List<Map<String, Object>> getSmsSendDetailList() {
		return smsSendDetailList;
	}

	public void setSmsSendDetailList(List<Map<String, Object>> smsSendDetailList) {
		this.smsSendDetailList = smsSendDetailList;
	}

	/** 会员短信沟通明细画面Form */
	private BINOLMBMBM23_Form form = new BINOLMBMBM23_Form();

	@Override
	public BINOLMBMBM23_Form getModel() {
		return form;
	}

}

/*
 * @(#)BINOLMBMBM26_Action.java     1.0 2013.09.23
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

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM26_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM26_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员问题画面Action
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM26_Action extends BaseAction implements ModelDriven<BINOLMBMBM26_Form> {

	private static final long serialVersionUID = -6779936329419848594L;
	
	/** 会员问题画面BL */
	@Resource
	private BINOLMBMBM26_BL binOLMBMBM26_BL;
	
	/**
	 * 会员问题一览画面
	 * 
	 * @return 会员问题一览画面
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * AJAX取得会员问题List
	 * 
	 * @return 会员问题List
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 取得会员问题总数
		int count = binOLMBMBM26_BL.getIssueCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得会员问题List
			issueList = binOLMBMBM26_BL.getIssueList(map);
		}
		return SUCCESS;
	}
	
	/** 会员问题List **/
	private List<Map<String, Object>> issueList;

	public List<Map<String, Object>> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Map<String, Object>> issueList) {
		this.issueList = issueList;
	}

	/** 会员问题画面Form */
	private BINOLMBMBM26_Form form = new BINOLMBMBM26_Form();

	@Override
	public BINOLMBMBM26_Form getModel() {
		return form;
	}

}

/*
 * @(#)BINOLMBMBM22_Action.java     1.0 2013.08.13
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
import com.cherry.mb.mbm.bl.BINOLMBMBM22_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM22_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员答卷画面Action
 * 
 * @author WangCT
 * @version 1.0 2013.08.13
 */
public class BINOLMBMBM22_Action extends BaseAction implements ModelDriven<BINOLMBMBM22_Form> {

	private static final long serialVersionUID = -1981628799418796304L;
	
	/** 会员答卷画面BL */
	@Resource
	private BINOLMBMBM22_BL binOLMBMBM22_BL;
	
	/**
	 * 会员答卷画面
	 * 
	 * @return 会员答卷画面
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * AJAX取得会员答卷信息
	 * 
	 * @return 会员答卷画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 取得会员答卷信息总数
		int count = binOLMBMBM22_BL.getMemAnswerCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得会员答卷信息List
			memAnswerList = binOLMBMBM22_BL.getMemAnswerList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询会员答卷明细信息
	 * 
	 * @return 会员答卷明细信息画面
	 */
	public String searchAnswerDetail() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 根据问卷ID和答卷ID获取问题和答案明细信息
		memAnswerDetailList = binOLMBMBM22_BL.getMemQuestionList(map);
		return SUCCESS;
	}
	
	/** 会员答卷信息List */
	private List<Map<String, Object>> memAnswerList;
	
	/** 会员答卷明细信息List */
	private List<Map<String, Object>> memAnswerDetailList;
	
	public List<Map<String, Object>> getMemAnswerList() {
		return memAnswerList;
	}

	public void setMemAnswerList(List<Map<String, Object>> memAnswerList) {
		this.memAnswerList = memAnswerList;
	}

	public List<Map<String, Object>> getMemAnswerDetailList() {
		return memAnswerDetailList;
	}

	public void setMemAnswerDetailList(List<Map<String, Object>> memAnswerDetailList) {
		this.memAnswerDetailList = memAnswerDetailList;
	}

	/** 会员推荐会员画面Form */
	private BINOLMBMBM22_Form form = new BINOLMBMBM22_Form();

	@Override
	public BINOLMBMBM22_Form getModel() {
		return form;
	}

}

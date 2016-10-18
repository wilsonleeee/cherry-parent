/*
 * @(#)BINOLPLSCF10_Action.java     1.0 2011/4/1
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

package com.cherry.pl.scf.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.bl.BINOLPLSCF10_BL;
import com.cherry.pl.scf.form.BINOLPLSCF07_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * code值一览Action
 * 
 * @author zhangjie
 * @version 1.0 2011.4.1
 */
public class BINOLPLSCF10_Action extends BaseAction implements
		ModelDriven<BINOLPLSCF07_Form> {

	private static final long serialVersionUID = 1901329623751585647L;

	/** code值一览BL */
	@Resource
	private BINOLPLSCF10_BL binOLPLSCF10_BL;

	/** code值一览Form */
	private BINOLPLSCF07_Form form = new BINOLPLSCF07_Form();

	/**
	 * code值一览初期表示
	 * 
	 * @return code值一览画面
	 */
	public String init() {
		return SUCCESS;
	}
	
	/**
	 * 取得code值表一览
	 * 
	 * @return code值表一览画面
	 */
	public String coderList() {

		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		if (language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put("orgCode", form.getOrgCode());
		// 所属品牌
		if (form.getBrandCode() != null && !"".equals(form.getBrandCode())) {
			map.put("brandCode", form.getBrandCode());
		} 
		// Code类别
		if (form.getCodeType() != null && !"".equals(form.getCodeType())) {
			map.put("codeType", form.getCodeType());
		}

		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);

		// 查询审核审批配置信息总数
		int count = binOLPLSCF10_BL.getCoderCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			// 查询审核审批配置信息List
			coderList = binOLPLSCF10_BL.getCoderList(map);
		}

		return SUCCESS;
	}

	/** coderList */
	private List<Map<String, Object>> coderList;


	public List<Map<String, Object>> getCoderList() {
		return coderList;
	}

	public void setCoderList(List<Map<String, Object>> coderList) {
		this.coderList = coderList;
	}

	@Override
	public BINOLPLSCF07_Form getModel() {
		return form;
	}

}

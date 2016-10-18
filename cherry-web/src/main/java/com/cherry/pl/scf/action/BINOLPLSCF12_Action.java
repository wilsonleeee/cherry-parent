/*
 * @(#)BINOLPLSCF12_Action.java     1.0 2011/3/25
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
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.pl.scf.bl.BINOLPLSCF12_BL;
import com.cherry.pl.scf.form.BINOLPLSCF07_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * code值详细Action
 * 
 * @author zhangjie
 * @version 1.0 2011.3.25
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF12_Action extends BaseAction  implements
ModelDriven<BINOLPLSCF07_Form>{

	private static final long serialVersionUID = 6238093725846904869L;

	/** code值详细BL */
	@Resource
	private BINOLPLSCF12_BL binolplscf12_BL;
	
	/** Code值表ID */
	private String coderID;
	
	/** CODE表详细 */
	private Map coderDetail;
	
	/** code值添加Form */
	private BINOLPLSCF07_Form form = new BINOLPLSCF07_Form();
	
	/**
	 * code值管理详细初期表示
	 * 
	 * @return code值管理详细画面
	 */
	public String init() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 接收CODE管理表ID
		map.put("coderID",coderID);
		// 取得CODE管理表详细信息
		coderDetail = binolplscf12_BL.getCoderDetail(map);

		return SUCCESS;
		
	}



	public String getCoderID() {
		return coderID;
	}



	public void setCoderID(String coderID) {
		this.coderID = coderID;
	}



	public Map getCoderDetail() {
		return coderDetail;
	}



	public void setCoderDetail(Map coderDetail) {
		this.coderDetail = coderDetail;
	}


	@Override
	public BINOLPLSCF07_Form getModel() {
		return form;
	}




}

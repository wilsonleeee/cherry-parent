/*	
 * @(#)BINOLBSFAC02_Action.java     1.0 2011/02/16	
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
package com.cherry.bs.fac.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.fac.bl.BINOLBSFAC02_BL;
import com.cherry.bs.fac.form.BINOLBSFAC02_Form;
import com.cherry.cm.core.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 生产厂商详细Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.02.16
 */
@SuppressWarnings("unchecked")
public class BINOLBSFAC02_Action extends BaseAction implements
		ModelDriven<BINOLBSFAC02_Form> {

	private static final long serialVersionUID = -604695484013225266L;

	/** 参数FORM */
	private BINOLBSFAC02_Form form = new BINOLBSFAC02_Form();

	@Resource
	private BINOLBSFAC02_BL binolbsfac02BL;

	/** 厂商基本信息 */
	private Map facInfo;

	/** 厂商地址List */
	private List addList;

	@Override
	public BINOLBSFAC02_Form getModel() {
		return form;
	}

	public Map getFacInfo() {
		return facInfo;
	}

	public void setFacInfo(Map facInfo) {
		this.facInfo = facInfo;
	}

	public List getAddList() {
		return addList;
	}

	public void setAddList(List addList) {
		this.addList = addList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// 接收生产厂商ID
		map.put("manufacturerInfoId", form.getManufacturerInfoId());
		// 取得生产厂商基本信息
		facInfo = binolbsfac02BL.getFacInfo(map);
		// 取得厂商地址List
		addList = binolbsfac02BL.getAddList(map);

		return SUCCESS;
	}
}

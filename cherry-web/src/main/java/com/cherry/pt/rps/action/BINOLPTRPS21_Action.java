/*
 * @(#)BINOLPTRPS21_Action.java     1.0 2010/11/05
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
package com.cherry.pt.rps.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.pt.rps.form.BINOLPTRPS21_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS21_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点单明细Action
 * 
 * 
 * 
 * @author weisc
 * @version 1.0 2010.11.05
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS21_Action extends BaseAction
		implements ModelDriven<BINOLPTRPS21_Form> {

	private static final long serialVersionUID = 657072601835066344L;

	@Resource
	private BINOLPTRPS21_IF binolptrps21IF;
			
	/** 参数FORM */
	private BINOLPTRPS21_Form form = new BINOLPTRPS21_Form();
	
	/** 盘点单信息 */
	private Map takingInfo;
	
	/** 盘点单明细List */
	private List takingDetailList;
	
	@Override
	public BINOLPTRPS21_Form getModel() {
		return form;
	}
	
	public Map getTakingInfo() {
		return takingInfo;
	}

	public void setTakingInfo(Map takingInfo) {
		this.takingInfo = takingInfo;
	}

	public List getTakingDetailList() {
		return takingDetailList;
	}

	public void setTakingDetailList(List takingDetailList) {
		this.takingDetailList = takingDetailList;
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
	public String init(){
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 产品盘点ID
		map.put("stockTakingId", form.getStockTakingId());
		// 盈亏
		map.put("profitKbn", form.getProfitKbn());
		// 盘点单信息
		takingInfo = binolptrps21IF.searchTakingInfo(map);
		// 盘点单明细List
		takingDetailList = binolptrps21IF.searchTakingDetailList(map);
		return SUCCESS;
	}
}

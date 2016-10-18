/*	
 * @(#)BINOLSSPRM42_Action.java     1.0 2010/11/23		
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM42_BL;
import com.cherry.ss.prm.form.BINOLSSPRM42_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 退库记录详细Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.11.23
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM42_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM42_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -571661955558205391L;

	@Resource
	private BINOLSSPRM42_BL binolssprm42BL;

	/** 参数FORM */
	private BINOLSSPRM42_Form form = new BINOLSSPRM42_Form();

	/** 退库记录信息 */
	private Map returnInfo;

	/** 退库详细记录LIST */
	private List returnList;

	@Override
	public BINOLSSPRM42_Form getModel() {
		return form;
	}
	
	public Map getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(Map returnInfo) {
		this.returnInfo = returnInfo;
	}

	public List getReturnList() {
		return returnList;
	}

	public void setReturnList(List returnList) {
		this.returnList = returnList;
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
		// 参数MAP
		Map<String, Object> map = getSearchMap();
		// 退库记录信息
		returnInfo = binolssprm42BL.getReturnInfo(map);
		// 退库详细记录LIST
		returnList = binolssprm42BL.getReturnList(map);
		return SUCCESS;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 退货记录Id
		map.put("proReturnId", form.getProReturnId());
		return map;
	}
}

/*	
 * @(#)BINOLPTRPS20_Action.java     1.0 2010/11/29		
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
import com.cherry.pt.rps.bl.BINOLPTRPS20_BL;
import com.cherry.pt.rps.form.BINOLPTRPS20_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 发货单详细
 * 
 * @author weisc
 * @version 1.0 2011.5.16
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS20_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS20_Form> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 3005653828034942939L;

	@Resource
	private BINOLPTRPS20_BL binolptrps20BL;

	/** 参数FORM */
	private BINOLPTRPS20_Form form = new BINOLPTRPS20_Form();

	private Map returnInfo;

	/** 调拨详细记录LIST */
	private List returnList;

	@Override
	public BINOLPTRPS20_Form getModel() {
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
		// 调拨记录信息
		returnInfo = binolptrps20BL.getDeliverMainInfo(map);
		// 调拨详细记录LIST
		returnList = binolptrps20BL.getDeliverDetailList(map);
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

		map.put("deliverId", form.getDeliverId());
		return map;
	}
}

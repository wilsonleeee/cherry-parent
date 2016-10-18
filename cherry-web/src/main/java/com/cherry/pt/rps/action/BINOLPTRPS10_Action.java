/*	
 * @(#)BINOLPTRPS10_Action.java     1.0 2011/03/10	
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
import com.cherry.pt.rps.bl.BINOLPTRPS10_BL;
import com.cherry.pt.rps.form.BINOLPTRPS10_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 出入库记录详细Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.03.10
 */
public class BINOLPTRPS10_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS10_Form> {

	private static final long serialVersionUID = -5467767362264456314L;

	@Resource
	private BINOLPTRPS10_BL binolptrps10BL;

	/** 参数FORM */
	private BINOLPTRPS10_Form form = new BINOLPTRPS10_Form();

	/** 出入库记录信息 */
	@SuppressWarnings("unchecked")
	private Map returnInfo;

	/** 出入库详细记录LIST */
	@SuppressWarnings("unchecked")
	private List returnList;

	@Override
	public BINOLPTRPS10_Form getModel() {
		return form;
	}
	@SuppressWarnings("unchecked")
	public Map getReturnInfo() {
		return returnInfo;
	}
	@SuppressWarnings("unchecked")
	public void setReturnInfo(Map returnInfo) {
		this.returnInfo = returnInfo;
	}
	@SuppressWarnings("unchecked")
	public List getReturnList() {
		return returnList;
	}
	@SuppressWarnings("unchecked")
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
		// 取得入出库单详细信息
		returnInfo = binolptrps10BL.getProInOutInfo(map);
		// 取得入出库物品清单LIST
		returnList = binolptrps10BL.getProInOutList(map);
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
		// 入出库记录Id
		map.put("proIOId", form.getProIOId());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		return map;
	}
}

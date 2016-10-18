/*	
 * @(#)BINOLPTRPS18_Action.java     1.0 2010/11/29		
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
import com.cherry.pt.rps.form.BINOLPTRPS18_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS18_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品调出单详细Action
 * 
 * 
 * 
 * @author weisc
 * @version 1.0 2011.4.3
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS18_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS18_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8198041214819936138L;

	@Resource
	private BINOLPTRPS18_IF binolptrps18IF;

	/** 参数FORM */
	private BINOLPTRPS18_Form form = new BINOLPTRPS18_Form();

	/** 调拨记录信息 */
	private Map returnInfo;

	/** 调拨详细记录LIST */
	private List returnList;

	@Override
	public BINOLPTRPS18_Form getModel() {
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
		returnInfo = binolptrps18IF.getAllocationInfo(map);
		// 调拨详细记录LIST
		returnList = binolptrps18IF.getAllocationList(map);
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
		// 调拨记录Id
		map.put("proAllocationId", form.getProAllocationId());
		
		// 调拨类型
		map.put("allocationType", form.getAllocationType());
		return map;
	}
}

/*
 * @(#)BINOLCM08_Action.java     1.0 2010/12/08
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
package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * 标准区域共通 Action
 * 
 * @author hub
 * @version 1.0 2010.12.08
 */
@SuppressWarnings("unchecked")
public class BINOLCM08_Action extends BaseAction{

	private static final long serialVersionUID = 8709164587010131061L;
	
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	/** 区域ID */
	private String regionId;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
	/**
	 * AJAX 取得区域下属LIST并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void querySubRegion() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 区域Id
		map.put("regionId", regionId);
		// 取得区域下属list
		List list = binOLCM08_BL.getChildRegionList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, list);
	}
	
	/**
	 * AJAX 取得区域所有下属LIST并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryAllSubRegion() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 区域Id
		map.put("regionId", regionId);
		// 取得区域下属list
		List list = binOLCM08_BL.getAllChildStandRegionList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, list);
	}
	
	public void refresh() throws Exception {
		String result = "1";
		try {
			binOLCM08_BL.refreshRegionTreeList();
		} catch (Exception e) {
			result = "0";
		}
		ConvertUtil.setResponseByAjax(response, result);
	}
}

/*
 * @(#)BINOLBSPOS07_Action.java     1.0 2010/10/27
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

package com.cherry.bs.pos.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS07_BL;
import com.cherry.cm.core.BaseAction;

/**
 * 岗位类别详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS07_Action extends BaseAction {

	private static final long serialVersionUID = 1L;

	/** 岗位类别详细画面BL */
	@Resource
	private BINOLBSPOS07_BL binOLBSPOS07_BL;
	
	/**
	 * 岗位类别详细画面初期处理
	 * 
	 * @return 岗位类别详细画面 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 岗位类别ID
		map.put("positionCategoryId", positionCategoryId);
		// 查询岗位类别信息
		posCategoryInfo = binOLBSPOS07_BL.getPosCategoryInfo(map);
		
		return SUCCESS;
	}
	
	/** 岗位类别ID */
	private String positionCategoryId;
	
	/** 岗位类别信息 */
	private Map posCategoryInfo;

	public String getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public Map getPosCategoryInfo() {
		return posCategoryInfo;
	}

	public void setPosCategoryInfo(Map posCategoryInfo) {
		this.posCategoryInfo = posCategoryInfo;
	}
}

/*
 * @(#)BINOLPLRLA03_Action.java     1.0 2010/11/01
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

package com.cherry.pl.rla.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.rla.bl.BINOLPLRLA03_BL;

/**
 * 岗位角色分配Action
 * 
 * @author WangCT
 * @version 1.0 2010.11.01
 */
public class BINOLPLRLA03_Action extends BaseAction {

	private static final long serialVersionUID = 7395460757188168911L;
	
	/** 岗位角色分配BL */
	@Resource
	private BINOLPLRLA03_BL binOLPLRLA03_BL;
	
	/**
	 * 岗位角色分配画面初期化
	 * 
	 * @return String 
	 * @throws Exception 
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * AJAX取得某一岗位的直属下级岗位
	 * 
	 * @return String 
	 * @throws Exception 
	 */
	public String next() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 岗位节点位置
		map.put(CherryConstants.PATH, path);
		// 取得某一岗位的直属下级岗位
		positionTree = binOLPLRLA03_BL.getNextPositionList(map);
		
		ConvertUtil.setResponseByAjax(response, positionTree);
		return null;
	}
	
	/** 岗位结构JSON树 */
	private String positionTree;
	
	/** 岗位节点位置 */
	private String path;

	public String getPositionTree() {
		return positionTree;
	}

	public void setPositionTree(String positionTree) {
		this.positionTree = positionTree;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}

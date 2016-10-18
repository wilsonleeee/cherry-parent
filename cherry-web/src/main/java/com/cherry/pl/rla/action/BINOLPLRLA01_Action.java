/*
 * @(#)BINOLPLRLA01_Action.java     1.0 2010/10/27
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
import com.cherry.pl.rla.bl.BINOLPLRLA01_BL;

/**
 * 组织角色分配Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLA01_Action extends BaseAction {
	
	private static final long serialVersionUID = 5911852165970893282L;

	/** 组织角色分配BL */
	@Resource
	private BINOLPLRLA01_BL binOLPLRLA01_BL;
	
	/**
	 * 组织角色分配画面初期化
	 * 
	 * @return String 
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * AJAX取得某一组织的直属下级组织
	 * 
	 * @return String 
	 * @throws Exception 
	 */
	public String next() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 组织节点位置
		map.put(CherryConstants.PATH, path);
		// 取得某一组织的直属下级组织
		organizationTree = binOLPLRLA01_BL.getNextOrganizationList(map);
		
		ConvertUtil.setResponseByAjax(response, organizationTree);
		return null;
	}
	
	/** 组织结构JSON树 */
	private String organizationTree;
	
	/** 组织节点位置 */
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOrganizationTree() {
		return organizationTree;
	}

	public void setOrganizationTree(String organizationTree) {
		this.organizationTree = organizationTree;
	}

}

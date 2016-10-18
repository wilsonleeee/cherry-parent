/*
 * @(#)BINOLPLRLA02_Action.java     1.0 2010/11/01
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.rla.bl.BINOLPLRLA02_BL;

/**
 * 岗位类别角色分配Action
 * 
 * @author WangCT
 * @version 1.0 2010.11.01
 */
public class BINOLPLRLA02_Action extends BaseAction {

	private static final long serialVersionUID = -9023337169498795245L;

	/** 岗位类别角色分配BL */
	@Resource
	private BINOLPLRLA02_BL binOLPLRLA02_BL;

	/**
	 * 岗位类别分配画面初期化
	 * 
	 * @return String
	 */
	public String init() {

		return SUCCESS;
	}

	public String loadTree() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		if (language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());

		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {// 不是总部的场合
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得当前品牌的岗位类别信息List
			positionCategoryList = binOLPLRLA02_BL.getPositionCategoryList(map);
		} else {// 总部的场合
				// 取得总部所有岗位类别信息List
			positionCategoryList = binOLPLRLA02_BL.getPositionCategoryList(map);
			for (int i = 0; i < positionCategoryList.size(); i++) {
				Map<String, Object> brandMap = positionCategoryList.get(i);
				if (CherryConstants.BRAND_INFO_ID_VALUE == Integer
						.parseInt(brandMap.get("id").toString())) {
					brandMap.put("name", getText("PPL00006"));
				}
			}
		}

		ConvertUtil.setResponseByAjax(response, positionCategoryList);

		return null;
	}

	/** 岗位类别信息List */
	private List<Map<String, Object>> positionCategoryList;

	public List<Map<String, Object>> getPositionCategoryList() {
		return positionCategoryList;
	}

	public void setPositionCategoryList(
			List<Map<String, Object>> positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}

}

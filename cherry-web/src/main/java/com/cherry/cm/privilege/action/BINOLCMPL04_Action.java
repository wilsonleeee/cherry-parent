/*
 * @(#)BINOLCMPL04_Action.java     1.0 2012/11/01
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
package com.cherry.cm.privilege.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.privilege.bl.BINOLCMPL04_BL;

/**
 * 实时刷新数据权限Action
 * 
 * @author WangCT
 * @version 1.0 2012/11/01
 */
public class BINOLCMPL04_Action extends BaseAction {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLCMPL04_Action.class);

	private static final long serialVersionUID = 2925906052623833384L;
	
	/** 部门数据过滤权限共通BL */
	@Resource
	private BINOLCMPL04_BL binOLCMPL04_BL;
	
	public String sendRefreshPlMsg() {
		
		try {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 不是总部用户的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 组织ID
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 品牌ID
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 组织代码
				map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
				// 品牌代码
				map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
				if(isReOrgPl != null && !"".equals(isReOrgPl)) {
					map.put("isReOrgPl", isReOrgPl);
				} else {
					map.put("isReOrgPl", "1");
				}
				if(isReEmpPl != null && !"".equals(isReEmpPl)) {
					map.put("isReEmpPl", isReEmpPl);
				} else {
					map.put("isReEmpPl", "1");
				}
				if(isReOrgRelPl != null && !"".equals(isReOrgRelPl)) {
					map.put("isReOrgRelPl", isReOrgRelPl);
				} else {
					map.put("isReOrgRelPl", "1");
				}
				map.put("refreshFlag", refreshFlag);
				// 发送实时刷新数据权限MQ消息
				binOLCMPL04_BL.sendRefreshPlMsg(map);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			return null;
		}
		return null;
	}
	
	/** 是否刷新部门权限（0：不刷新，1：刷新） **/
	private String isReOrgPl;
	
	/** 是否刷新人员权限（0：不刷新，1：刷新） **/
	private String isReEmpPl;
	
	/** 是否刷新部门从属权限（0：不刷新，1：刷新） **/
	private String isReOrgRelPl;
	
	/** 是否刷新权限（1：不管配置项需要刷新权限） **/
	private String refreshFlag;

	public String getIsReOrgPl() {
		return isReOrgPl;
	}

	public void setIsReOrgPl(String isReOrgPl) {
		this.isReOrgPl = isReOrgPl;
	}

	public String getIsReEmpPl() {
		return isReEmpPl;
	}

	public void setIsReEmpPl(String isReEmpPl) {
		this.isReEmpPl = isReEmpPl;
	}

	public String getIsReOrgRelPl() {
		return isReOrgRelPl;
	}

	public void setIsReOrgRelPl(String isReOrgRelPl) {
		this.isReOrgRelPl = isReOrgRelPl;
	}

	public String getRefreshFlag() {
		return refreshFlag;
	}

	public void setRefreshFlag(String refreshFlag) {
		this.refreshFlag = refreshFlag;
	}

}

/*
 * @(#)BINOLCMPL02_Action.java     1.0 2010/11/04
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
import com.cherry.cm.privilege.bl.BINOLCMPL02_BL;

/**
 * 岗位数据过滤权限共通Action
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
public class BINOLCMPL02_Action extends BaseAction {

	private static final long serialVersionUID = -781645377761111408L;

	private static Logger logger = LoggerFactory.getLogger(BINOLCMPL02_Action.class);

	/** 岗位数据过滤权限共通BL */
	@Resource
	private BINOLCMPL02_BL binOLCMPL02_BL;
	
	public String binbepldpl02Exec() {
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLCMPL02");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLCMPL02");
			// 创建岗位数据过滤权限
			binOLCMPL02_BL.tran_createDataPrivilege(map);
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			return null;
		}
		return null;
	}
}

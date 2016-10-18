/*	
 * @(#)BINOLBSCNT05_Action.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.bl.BINOLBSCNT05_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT05_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 	停用启用柜台处理Action
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT05_Action extends BaseAction implements ModelDriven<BINOLBSCNT05_Form> {

	private static final long serialVersionUID = -3802351579156596000L;
	
	/** 停用启用柜台处理Form */
	private BINOLBSCNT05_Form form = new BINOLBSCNT05_Form();
	
	/** 停用启用柜台处理BL */
	@Resource(name="binOLBSCNT05_BL")
	private BINOLBSCNT05_BL binOLBSCNT05_BL;
	
	/**
	 * 
	 * 停用启用柜台处理
	 * 
	 * @param 无
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String delCounterInfo() throws Exception {
		try{
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			//所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			//语言
			map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
			// 组织代号
			map.put("orgCode", userInfo.getOrganizationInfoCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLBSCNT05");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT05");
			//登录名
			map.put("loginName", userInfo.getLoginName());
			// 停用启用柜台处理
			binOLBSCNT05_BL.tran_updateCounterInfo(map);
			this.addActionMessage(getText("ICM00002"));
		}catch(Exception e){
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(e.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	@Override
	public BINOLBSCNT05_Form getModel() {
		return form;
	}

}

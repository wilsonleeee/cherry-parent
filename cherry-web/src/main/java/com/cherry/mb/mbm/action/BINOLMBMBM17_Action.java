/*
 * @(#)BINOLMBMBM16_Action.java     1.0 2013/05/15
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
package com.cherry.mb.mbm.action;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mb.mbm.bl.BINOLMBMBM17_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM17_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员启用停用处理Action
 * 
 * @author WangCT
 * @version 1.0 2013/05/15
 */
public class BINOLMBMBM17_Action extends BaseAction implements ModelDriven<BINOLMBMBM17_Form> {

	private static final long serialVersionUID = -4697297803956786494L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBMBM17_Action.class);
	
	@Resource
	private BINOLMBMBM17_BL binOLMBMBM17_BL;
	
	/**
	 * 
	 * 会员启用停用处理
	 * 
	 */
	public String editValidFlag() throws Exception {
		try{
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 品牌代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMBMBM16");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM16");
			// 操作员工
			map.put("modifyEmployee", userInfo.getEmployeeCode());
			// 会员启用停用处理
			binOLMBMBM17_BL.tran_editValidFlag(map);
			this.addActionMessage(getText("ICM00002"));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/** 会员启用停用处理Form */
	private BINOLMBMBM17_Form form = new BINOLMBMBM17_Form();

	@Override
	public BINOLMBMBM17_Form getModel() {
		return form;
	}

}

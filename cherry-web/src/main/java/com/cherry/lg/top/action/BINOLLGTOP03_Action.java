/*
 * @(#)BINOLLGTOP03_Action.java     1.0 2011/02/2
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

package com.cherry.lg.top.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.emp.bl.BINOLBSEMP04_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.JsclPBKDF2WithHMACSHA256;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.lg.top.bl.BINOLLGTOP03_BL;
import com.cherry.lg.top.form.BINOLLGTOP03_Form;
import com.cherry.pl.upm.bl.BINOLPLUPM01_BL;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 更新用户Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLLGTOP03_Action extends BaseAction implements
ModelDriven<BINOLLGTOP03_Form> {
	
	private static final long serialVersionUID = 7052727878284081396L;

	@Resource
	private BINOLLGTOP03_BL binollgtop03_BL;
	
	@Resource
	private BINOLPLUPM01_BL binolplupm01_BL;
	
	@Resource
	private BINOLBSEMP04_BL binolbsemp04BL;
	
	//form
	private BINOLLGTOP03_Form form = new BINOLLGTOP03_Form();
	
	//用户信息
	private Map user;


	@Override
	public BINOLLGTOP03_Form getModel() {
		
		return form;
	}
	
	public Map getUser() {
		return user;
	}

	/**
	 * 更新用户初期表示
	 * 
	 * @return String 
	 */
	public String init() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		//登入用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 用户信息
		user = binolplupm01_BL.getUser(map);
		
		// 总部用户登录的时候
		if (null != user){
			String brandInfoId =  String.valueOf(user.get("brandInfoId"));
			if (String.valueOf(CherryConstants.BRAND_INFO_ID_VALUE).equals(brandInfoId)){
				user.put("brandName", getText("PPL00006"));
			}
		}
		//弹出类型区分，弹出对话框还是新窗口
		String popType = ConvertUtil.getString(form.getPopType());
		if(popType.equals("dialog")){
		    return "popUpdatePwd";
		}
		return SUCCESS;
	}
	
	/**
	 * 更新用户处理
	 * 
	 * @return String 
	 */
	public String updateUser() throws Exception {
		
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 登入用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌代码
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 用户名
		map.put("longinName", userInfo.getLoginName());
		//更新时间
		map.put("modifyTime", form.getModifyTime());
		//更新次数
		map.put("modifyCount", form.getModifyCount());
		// 新密码
		map.put("newPassWord", form.getNewPassWord());
		try {
			binollgtop03_BL.trans_UpdateUser(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
	    //弹出类型区分，弹出对话框还是新窗口
        String popType = ConvertUtil.getString(form.getPopType());
        if(popType.equals("dialog")){
            return "popUpdatePwd_1";
        }
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 更新用户参数验证处理
	 * 
	 */
	public boolean validateForm() throws Exception {
		
		// 验证结果
		boolean isCorrect = true;
		Map<String, Object> map = new HashMap<String, Object>();
		// 登入用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put("userId", userInfo.getBIN_UserID());
		// 取得用户密码
		String password = binollgtop03_BL.getUserPassWord(map);
		String newPassWord = null;
		try {
			newPassWord = JsclPBKDF2WithHMACSHA256.DecrptPBKDF2WithHMACSHA256(password, form.getNewPassWord());
			form.setNewPassWord(newPassWord);
		} catch (Exception e) {
			
		}
		if(newPassWord == null) {
			this.addFieldError("oldPassWord", getText("EPL00013"));
			isCorrect = false;
		} else {
			// 新密码不能为空验证
			if (CherryChecker.isNullOrEmpty(form.getNewPassWord())) {
				this.addFieldError("newPassWord", getText("ECM00009",
						new String[] { getText("PPL00032") }));
				isCorrect = false;
			} else {
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 密码安全配置信息
				Map pswConfig = binolbsemp04BL.getPassWordConfig(map);
				if(pswConfig == null){
					this.addFieldError("newPassWord", getText("EPL00012"));
					isCorrect = false;
				}else{
					boolean lengthFlg = true;
					// 密码最小长度
					String minLengthStr = String.valueOf(pswConfig.get("minLength"));
					if (null != minLengthStr) {
						int minLength = Integer.parseInt(minLengthStr);
						// 密码不能小于密码最小长度验证
						if (form.getNewPassWord().length() < minLength) {
							this.addFieldError("newPassWord", getText("EPL00008",
									new String[] { getText("PPL00032"), minLengthStr }));
							isCorrect = false;
							lengthFlg = false;
						}
					}
					// 密码最大长度
					String maxLengthStr = String.valueOf(pswConfig.get("maxLength"));
					if (lengthFlg && null != maxLengthStr) {
						int maxLength = Integer.parseInt(maxLengthStr);
						// 密码不能超过密码最大长度验证
						if (form.getNewPassWord().length() > maxLength) {
							this.addFieldError("newPassWord", getText("ECM00020",
									new String[] { getText("PPL00032"), maxLengthStr }));
							isCorrect = false;
							lengthFlg = false;
						}
					}
					//根据密码复杂度正则表达式判断密码复杂度
				    if (lengthFlg){
				    	//密码复杂度
					    String complexity = String.valueOf(pswConfig.get("complexity"));
					    if (!CherryChecker.isNullOrEmpty(complexity)){
					    	if (!form.getNewPassWord().matches(complexity)){
					    		this.addFieldError("newPassWord", getText("EPL00007"));
					    		isCorrect = false;
					    	}
					    }
					}
				}
			}
		}
		return isCorrect;
	}
}

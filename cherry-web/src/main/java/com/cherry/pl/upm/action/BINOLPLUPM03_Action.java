/*
 * @(#)BINOLPLUPM03_Action.java     1.0 2010/12/27
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

package com.cherry.pl.upm.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.upm.bl.BINOLPLUPM01_BL;
import com.cherry.pl.upm.bl.BINOLPLUPM02_BL;
import com.cherry.pl.upm.bl.BINOLPLUPM03_BL;
import com.cherry.pl.upm.bl.BINOLPLUPM04_BL;
import com.cherry.pl.upm.form.BINOLPLUPM03_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 更新用户Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM03_Action extends BaseAction implements
ModelDriven<BINOLPLUPM03_Form> {
	
	private static final long serialVersionUID = 7052727878284081396L;

	@Resource
	private BINOLPLUPM03_BL binolplupm03_BL;
	
	@Resource
	private BINOLPLUPM01_BL binolplupm01_BL;
	
	@Resource
	private BINOLPLUPM02_BL binolplupm02_BL;
	
	@Resource
	private BINOLPLUPM04_BL binOLPLUPM04_BL;
	
	//form
	private BINOLPLUPM03_Form form = new BINOLPLUPM03_Form();
	
	//用户信息
	private Map user;
	
	/** 密码安全配置信息 */
	private Map pwConfInfo;
	
	@Override
	public BINOLPLUPM03_Form getModel() {

		return form;
	}
	
	public void setUser(Map user) {
		this.user = user;
	}

	public Map getUser() {
		return user;
	}
	
	public Map getPwConfInfo() {
		return pwConfInfo;
	}

	public void setPwConfInfo(Map pwConfInfo) {
		this.pwConfInfo = pwConfInfo;
	}

	/**
	 * 更新用户初期表示
	 * 
	 * @return String 
	 */
	public String init() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put("userId", form.getUserId());
		// 用户信息
		user = binolplupm01_BL.getUser(map);
		// 总部用户登录的时候
		if (null != user){
			String brandInfoId =  ConvertUtil.getString(user.get("brandInfoId"));
			if (String.valueOf(CherryConstants.BRAND_INFO_ID_VALUE).equals(brandInfoId)){
				user.put("brandName", getText("PPL00006"));
			}
		}
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		//密码安全配置信息
		pwConfInfo = binolplupm02_BL.getPassWordConfig(map);
		// 取得密码组合
		binOLPLUPM04_BL.getPwComb(pwConfInfo);
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
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		//用户ID
		map.put("userId", form.getUserId());
		//更新时间
		map.put("modifyTime", form.getModifyTime());
		//更新次数
		map.put("modifyCount", form.getModifyCount());
		//确认密码
		map.put("confirmPW", form.getConfirmPW());
		//密码安全配置信息
		Map pswConfig = binolplupm01_BL.getUser(map);
		//是否加密
		String isEncryption = String.valueOf(pswConfig.get("isEncryption"));
		if (!CherryChecker.isNullOrEmpty(isEncryption)){
		   //加密后密码
		   String psw = changePsd(form.getPassWord(),isEncryption);
		   map.put("passWord", psw);
		}else{
		   map.put("passWord", form.getPassWord());
		}
		try {
			binolplupm03_BL.tran_UpdateUser(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_DIALOG;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_DIALOG;
	}
	
	//根据密码是否加密对用户输入的密码进行处理
	private String changePsd(String passWord,String isEncryption){
		if("0".equals(isEncryption)){
			//"0"表示不加密
			return passWord;
		}else{
			//加密
			//TODO:按照一定的加密算法将输入的密码进行加密
			return passWord;
		}
	}
	
	/**
	 * 更新用户参数验证处理
	 * 
	 */
	public boolean validateForm() {
		
		// 验证结果
		boolean isCorrect = true;
		// 密码不能超过30位验证
		if (CherryChecker.isNullOrEmpty(form.getPassWord())) {
			this.addFieldError("passWord", getText("ECM00009",
					new String[] { getText("PPL00028") }));
			isCorrect = false;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			//密码安全配置信息
			Map pswConfig = binolplupm02_BL.getPassWordConfig(map);
			if(pswConfig == null){
				this.addFieldError("passWord", getText("EPL00012"));
				   isCorrect = false;
			}else{
				boolean lengthFlg = true;
				// 密码最小长度
				String minLengthStr = String.valueOf(pswConfig.get("minLength"));
				if (null != minLengthStr) {
					int minLength = Integer.parseInt(minLengthStr);
					// 密码不能小于密码最小长度验证
					if (form.getPassWord().length() < minLength) {
						this.addFieldError("passWord", getText("EPL00008",
								new String[] { getText("PPL00028"), minLengthStr }));
						isCorrect = false;
						lengthFlg = false;
					}
				}
				// 密码最大长度
				String maxLengthStr = String.valueOf(pswConfig.get("maxLength"));
				if (lengthFlg && null != maxLengthStr) {
					int maxLength = Integer.parseInt(maxLengthStr);
					// 密码不能超过密码最大长度验证
					if (form.getPassWord().length() > maxLength) {
						this.addFieldError("passWord", getText("ECM00020",
								new String[] { getText("PPL00028"), maxLengthStr }));
						isCorrect = false;
						lengthFlg = false;
					}
				}
				//根据密码复杂度正则表达式判断密码复杂度
			    if (lengthFlg){
			    	//密码复杂度
				    String complexity = String.valueOf(pswConfig.get("complexity"));
				    if (!CherryChecker.isNullOrEmpty(complexity)){
				    	if (!form.getPassWord().matches(complexity)){
				    		this.addFieldError("passWord", getText("EPL00007"));
				    		isCorrect = false;
				    	}
				    }
				}
			}
		}
		
		// 确认密码验证
		if (CherryChecker.isNullOrEmpty(form.getConfirmPW())) {
			this.addFieldError("confirmPW", getText("ECM00009",
					new String[] { getText("PPL00027") }));
			isCorrect = false;
		} else {
				//判断确认密码和密码是否相同
				if (!(form.getPassWord()).equals(form.getConfirmPW())){
					this.addFieldError("confirmPW", getText("EPL00006"));
					isCorrect = false; 
				}
		}
		return isCorrect;
	}
}

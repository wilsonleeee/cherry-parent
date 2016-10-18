/*
 * @(#)BINOLPLUPM02_Action.java     1.0 2010/12/29
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.pl.upm.bl.BINOLPLUPM02_BL;
import com.cherry.pl.upm.bl.BINOLPLUPM04_BL;
import com.cherry.pl.upm.form.BINOLPLUPM02_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 用户添加Action
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM02_Action extends BaseAction implements
		ModelDriven<BINOLPLUPM02_Form>{

	private static final long serialVersionUID = -6676412305227501560L;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLPLUPM02_BL binolplupm02_BL;
	
	@Resource
	private BINOLPLUPM04_BL binOLPLUPM04_BL;
	
	/** 参数FORM */
	private BINOLPLUPM02_Form form = new BINOLPLUPM02_Form();;

	public BINOLPLUPM02_Form getModel() {
		
		return form;
	}
	
	/** 所属品牌List */
	private List brandInfoList;
	
	/** 密码安全配置信息 */
	private Map pwConfInfo;

	/** 所属品牌List */
	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public List getBrandInfoList() {
		return brandInfoList;
	}
	
	public Map getPwConfInfo() {
		return pwConfInfo;
	}

	public void setPwConfInfo(Map pwConfInfo) {
		this.pwConfInfo = pwConfInfo;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String init() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map brandMap = new HashMap();
			// 品牌ID
			brandMap.put("brandInfoId", brandInfoId);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList();
				brandInfoList.add(brandMap);
			}
		} else {
			// 品牌信息
			Map brandInfo = new HashMap();
			// 品牌ID
			brandInfo.put("brandInfoId", brandInfoId);
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, ((Map)brandInfoList.get(0)).get("brandInfoId"));
		//密码安全配置信息
		pwConfInfo = binolplupm02_BL.getPassWordConfig(map);
		// 取得密码组合
		binOLPLUPM04_BL.getPwComb(pwConfInfo);
		return SUCCESS;
	}
    
    /**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
     * @throws Exception 
	 * 
	 */
    public String save() throws Exception{
    	// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		//确认密码
		map.put("confirmPW", form.getConfirmPW());
		//员工ID
		map.put("employeeId", form.getEmployeeId());
		//登入账号
		map.put("loginName", form.getLoginName());
		//密码安全配置信息
		Map pswConfig = binolplupm02_BL.getPassWordConfig(map);
		//是否加密
		String isEncryption = String.valueOf(pswConfig.get("isEncryption"));
		if (!CherryChecker.isNullOrEmpty(isEncryption)){
		   //加密后密码
		   String psw = changePsd(form.getPassWord(),isEncryption);
		   map.put("passWord", psw);
		}else{
		   map.put("passWord", form.getPassWord());
		}
		// 用户添加插表处理
		binolplupm02_BL.tran_addUser(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
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
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
     * @throws Exception 
	 * 
	 */
	private boolean validateForm() throws Exception {
		// 验证结果
		boolean isCorrect = true;
		// 登入账号不为空验证
		if (CherryChecker.isNullOrEmpty(form.getLoginName())) {
			this.addFieldError("loginName", getText("ECM00009",
					new String[] { getText("PPL00026") }));
			isCorrect = false;
		} else {
			// 登入账号不能超过30位验证
			if (form.getLoginName().length() > 30) {
				this.addFieldError("loginName", getText("ECM00020",
						new String[] { getText("PPL00026"), "30" }));
				isCorrect = false;
			}else{
				Map<String, Object> map = new HashMap<String, Object>();
				//登入账号
				map.put("loginName", form.getLoginName());
				String loginName = binolplupm02_BL.getLoginNameCheck(map);
				if(loginName != null && !"".equals(loginName) && form.getLoginName().equals(loginName)) {
					this.addFieldError("loginName", getText("EPL00009"));
					isCorrect = false;
				}
			}
		}
		// 密码必须入力验证
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
				if (!(form.getConfirmPW()).equals(form.getPassWord())){
					this.addFieldError("confirmPW", getText("EPL00006"));
					isCorrect = false; 
				}
		}
		
		return isCorrect;
	}
	
	/**
	 * 根据品牌取得密码安全配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String change() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 取得密码安全配置信息
		pwConfInfo = binolplupm02_BL.getPassWordConfig(map);
		// 取得密码组合
		binOLPLUPM04_BL.getPwComb(pwConfInfo);
		return SUCCESS;
	}
}

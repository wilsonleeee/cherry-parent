/*	
 * @(#)BINOLPLUPM05_Action.java     1.0 2010/12/28		
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
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.upm.bl.BINOLPLUPM04_BL;
import com.cherry.pl.upm.bl.BINOLPLUPM05_BL;
import com.cherry.pl.upm.form.BINOLPLUPM05_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 安全策略添加Action
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM05_Action extends BaseAction implements
		ModelDriven<BINOLPLUPM05_Form> {

	private static final long serialVersionUID = -1100900331966231952L;

	/** 参数FORM */
	private BINOLPLUPM05_Form form = new BINOLPLUPM05_Form();
	
	@Resource
	private BINOLPLUPM04_BL binOLPLUPM04_BL;

	@Resource
	private BINOLPLUPM05_BL binOLPLUPM05_BL;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	/** 品牌名称 */
	private String brandName;

	public BINOLPLUPM05_Form getModel() {
		return form;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * 画面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 总部用户登录的时候
		if (ConvertUtil.getString(CherryConstants.BRAND_INFO_ID_VALUE).equals(
				form.getBrandInfoId())) {
			brandName = getText("PPL00006");
		} else {
			// 取得品牌名称
			brandName = binOLCM05_BL.getBrandName(map);
		}
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
	 * 
	 */
	public String save() throws Exception {
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 取得密码复杂度
		binOLPLUPM04_BL.getComplexity(map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLUPM05");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLUPM05");
		// 插入密码安全配置表
		binOLPLUPM05_BL.tran_addPwConfig(map);
		// 处理成功
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_DIALOG;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	private boolean validateForm() throws Exception {
		// 验证结果
		boolean isCorrect = true;
		// 密码有效期必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getDuration())) {
			this.addFieldError("duration", getText("ECM00009",
					new String[] { getText("PPL00007") }));
			isCorrect = false;
		} else {
			if (form.getDuration().length() > 9) {
				// 密码有效期不能超过20位验证
				this.addFieldError("duration", getText("ECM00020",
						new String[] { getText("PPL00007"), "9" }));
				isCorrect = false;
			}
			// 是否为数字
			if (!CherryChecker.isNumeric(form.getDuration())) {
				this.addFieldError("duration", getText("ECM00021",
						new String[] { getText("PPL00007") }));
				isCorrect = false;
			}
		}
		// 密码修改提醒验证
		if (null != form.getRemindAhead() && !"".equals(form.getRemindAhead())) {
			if (form.getRemindAhead().length() > 2) {
				// 密码修改提醒不能超过2位验证
				this.addFieldError("remindAhead", getText("ECM00020",
						new String[] { getText("PPL00008"), "2" }));
				isCorrect = false;
			}
			// 是否为数字
			if (!CherryChecker.isNumeric(form.getRemindAhead())) {
				this.addFieldError("remindAhead", getText("ECM00021",
						new String[] { getText("PPL00008") }));
				isCorrect = false;
			}
		}
		// 密码重复间隔验证
		if (null != form.getRepetitionInterval() && !"".equals(form.getRepetitionInterval())) {
			if (form.getRepetitionInterval().length() > 2) {
				//  密码重复间隔不能超过2位验证
				this.addFieldError("repetitionInterval", getText("ECM00020",
						new String[] { getText("PPL00009"), "2" }));
				isCorrect = false;
			}
			// 是否为数字
			if (!CherryChecker.isNumeric(form.getRepetitionInterval())) {
				this.addFieldError("repetitionInterval", getText("ECM00021",
						new String[] { getText("PPL00009") }));
				isCorrect = false;
			}
		}
		// 英文和数字必须选择一种验证
		if (CherryChecker.isNullOrEmpty(form.getHasAlpha()) 
				&& CherryChecker.isNullOrEmpty(form.getHasNumeric())) {
			this.addFieldError("showMsg", getText("EPL00014"));
			isCorrect = false;
		}
		// 其他字符必须入力验证
		if (!CherryChecker.isNullOrEmpty(form.getHasOtherChar())) {
			String otherChar = form.getOtherChar().trim();
			if (CherryChecker.isNullOrEmpty(otherChar)) {
				this.addFieldError("showMsg", getText("EPL00015"));
				isCorrect = false;
				// 其他字符重复验证
			} else if (!CherryChecker.isNoDuplicate(otherChar)){
				this.addFieldError("showMsg", getText("EPL00016"));
				isCorrect = false;
			} else {
				String regex = "^[_.*,]+$";
				if (!otherChar.matches(regex)) {
					this.addFieldError("showMsg", getText("EPL00017"));
					isCorrect = false;
				}
			}
		}
		boolean minLengthFlg = true;
		// 密码最小长度必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getPwLength())) {
			this.addFieldError("pwLength", getText("ECM00009",
					new String[] { getText("PPL00011") }));
			isCorrect = false;
			minLengthFlg = false;
		} else {
			if (form.getPwLength().length() > 2) {
				//  密码最小长度不能超过2位验证
				this.addFieldError("pwLength", getText("ECM00020",
						new String[] { getText("PPL00011"), "2" }));
				isCorrect = false;
				minLengthFlg = false;
				// 是否为数字
			} else if (!CherryChecker.isNumeric(form.getPwLength())) {
				this.addFieldError("pwLength", getText("ECM00021",
						new String[] { getText("PPL00011") }));
				isCorrect = false;
				minLengthFlg = false;
			}
		}
		// 密码最大长度必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getMaxLength())) {
			this.addFieldError("maxLength", getText("ECM00009",
					new String[] { getText("PPL00025") }));
			isCorrect = false;
		} else {
			if (form.getMaxLength().length() > 2) {
				//  密码最大长度不能超过2位验证
				this.addFieldError("maxLength", getText("ECM00020",
						new String[] { getText("PPL00025"), "2" }));
				isCorrect = false;
				// 是否为数字
			} else if (!CherryChecker.isNumeric(form.getMaxLength())) {
				this.addFieldError("maxLength", getText("ECM00021",
						new String[] { getText("PPL00025") }));
				isCorrect = false;
			} else if (minLengthFlg) {
				int minLength = Integer.parseInt(form.getPwLength());
				int maxLength = Integer.parseInt(form.getMaxLength());
				// 密码最大长度与密码最小长度比较验证
				if (maxLength < minLength) {
					this.addFieldError("maxLength", getText("ECM00033",
							new String[] { getText("PPL00025"), getText("PPL00011")}));
					isCorrect = false;
				}
			}
		}
		// 重试次数
		if (null != form.getRetryTimes() && !"".equals(form.getRetryTimes())) {
			if (form.getRetryTimes().length() > 2) {
				//  重试次数不能超过2位验证
				this.addFieldError("retryTimes", getText("ECM00020",
						new String[] { getText("PPL00012"), "2" }));
				isCorrect = false;
			}
			// 是否为数字
			if (!CherryChecker.isNumeric(form.getRetryTimes())) {
				this.addFieldError("retryTimes", getText("ECM00021",
						new String[] { getText("PPL00012") }));
				isCorrect = false;
			}
		}
		// 账号锁定时间
//		if (null != form.getLockPeriod() && !"".equals(form.getLockPeriod())) {
//			if (form.getLockPeriod().length() > 9) {
//				//  账号锁定时间不能超过2位验证
//				this.addFieldError("lockPeriod", getText("ECM00020",
//						new String[] { getText("PPL00013"), "9" }));
//				isCorrect = false;
//			}
//			// 是否为数字
//			if (!CherryChecker.isNumeric(form.getLockPeriod())) {
//				this.addFieldError("lockPeriod", getText("ECM00021",
//						new String[] { getText("PPL00013") }));
//				isCorrect = false;
//			}
//		}
		return isCorrect;
	}
}

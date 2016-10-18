/*
 * @(#)BINOLPLSCF11_Action.java     1.0 2011/3/25
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

package com.cherry.pl.scf.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.scf.bl.BINOLPLSCF11_BL;
import com.cherry.pl.scf.form.BINOLPLSCF07_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * code值添加Action
 * 
 * @author zhangjie
 * @version 1.0 2011.3.25
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF11_Action extends BaseAction implements
		ModelDriven<BINOLPLSCF07_Form> {

	private static final long serialVersionUID = -9089605010044324018L;

	/** code值添加BL */
	@Resource
	private BINOLPLSCF11_BL binOLPLSCF11_BL;

	/** code值添加Form */
	private BINOLPLSCF07_Form form = new BINOLPLSCF07_Form();

	/**
	 * code值添加初期表示
	 * 
	 * @return code值添加画面
	 */
	public String init() {

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
	public String saveCoder() throws Exception {
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 参数MAP取得
		Map<String, Object> map = getParamsMap();
		// 添加cod值到数据库
		binOLPLSCF11_BL.tran_saveCoder(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 参数MAP取得
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, Object> getParamsMap() throws Exception {
		// form参数放入Map中
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 循环map剔除value值前后空格
		 for(Map.Entry<String, Object> en:map.entrySet()){
			 	Object value = en.getValue();
			 	if (value instanceof String) {
			 			value = ((String) value).trim();
			 			en.setValue(value);
			 			//map.put(key, value);
			 	}
			}
		 /*
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object value = map.get(key);
			if (value instanceof String) {
				value = ((String) value).trim();
				map.put(key, value);
			}
		}*/
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());

		return map;
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
		// Key必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getCodeKey())) {
			this.addFieldError("codeKey", getText("ECM00009", new String[]{getText("PPL00033")}));
			isCorrect = false;
		} else if (form.getCodeKey().length() > 4) {
			// Key不能超过4位验证
			this.addFieldError("codeKey", getText("ECM00020", new String[] {
					getText("PPL00033"), "4" }));
			isCorrect = false;
		} else if (!CherryChecker.isAlphanumeric(form.getCodeKey())) {
			// Key英数验证
			this.addFieldError("codeKey", getText("ECM00031", new String[]{getText("PPL00033")}));
			isCorrect = false;
		}
		// 值1不能超过20位验证
		if (!CherryChecker.isNullOrEmpty(form.getValue1()) && form.getValue1().length() > 20) {
			this.addFieldError("value1", getText("ECM00020", new String[] {
					getText("PPL00034"), "20" }));
			isCorrect = false;
		}
		// 值2不能超过20位验证
		if (!CherryChecker.isNullOrEmpty(form.getValue2()) && form.getValue2().length() > 20) {
			this.addFieldError("value2", getText("ECM00020", new String[] {
					getText("PPL00035"), "20" }));
			isCorrect = false;
		}
		// 值3不能超过20位验证
		if (!CherryChecker.isNullOrEmpty(form.getValue3()) && form.getValue3().length() > 20) {
			this.addFieldError("value3", getText("ECM00020", new String[] {
					getText("PPL00036"), "20" }));
			isCorrect = false;
		}
		// 级别是否为数字验证
		if (!CherryChecker.isNullOrEmpty(form.getGrade()) && !CherryChecker.isNumeric(form.getGrade())) {
			this.addFieldError("grade", getText("ECM00021",
					new String[] { getText("PPL00037") }));
			isCorrect = false;
		}
		// 显示顺序是否为数字验证
		if (!CherryChecker.isNullOrEmpty(form.getCodeOrder()) && !CherryChecker.isNumeric(form.getCodeOrder())) {
			this.addFieldError("codeOrder", getText("ECM00021",
					new String[] { getText("PPL00038") }));
			isCorrect = false;
		}
		return isCorrect;
	}
	/** 组织List */
	private List<Map<String, Object>> orgInfoList;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getOrgInfoList() {
		return orgInfoList;
	}

	public void setOrgInfoList(List<Map<String, Object>> orgInfoList) {
		this.orgInfoList = orgInfoList;
	}

	/** codeMList */
	private List<Map<String, Object>> codeList;

	public List<Map<String, Object>> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<Map<String, Object>> codeList) {
		this.codeList = codeList;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLPLSCF07_Form getModel() {
		return form;
	}

}

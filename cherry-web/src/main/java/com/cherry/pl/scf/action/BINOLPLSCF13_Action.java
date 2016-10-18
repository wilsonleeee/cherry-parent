/*
 * @(#)BINOLPLSCF13_Action.java     1.0 2011/3/25
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.scf.bl.BINOLPLSCF13_BL;
import com.cherry.pl.scf.form.BINOLPLSCF07_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * code值编辑Action
 * 
 * @author zhangjie
 * @version 1.0 2011.3.25
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF13_Action extends BaseAction  implements
ModelDriven<BINOLPLSCF07_Form>{

	private static final long serialVersionUID = 6238093725846904869L;

	/** code值编辑BL */
	@Resource
	private BINOLPLSCF13_BL binolplscf13_BL;
	
	/** Code值表ID */
	private String coderID;
	
	/** CODE表详细 */
	private Map coderDetail;
	
	/** code值添加Form */
	private BINOLPLSCF07_Form form = new BINOLPLSCF07_Form();
	
	/**
	 * code值编辑初期表示
	 * 
	 * @return code值编辑画面
	 */
	public String init() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 接收CODE管理表ID
		map.put("coderID",coderID);
		// 取得CODE管理表详细信息
		coderDetail = binolplscf13_BL.getCoderDetail(map);

		return SUCCESS;
		
	}

	/**
	 * <p>
	 * 更新
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
		map.put("coderID", coderID);
		// 更新code管理值数据
		binolplscf13_BL.tran_updateCoder(map);
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
	
	public String getCoderID() {
		return coderID;
	}



	public void setCoderID(String coderID) {
		this.coderID = coderID;
	}



	public Map getCoderDetail() {
		return coderDetail;
	}



	public void setCoderDetail(Map coderDetail) {
		this.coderDetail = coderDetail;
	}


	@Override
	public BINOLPLSCF07_Form getModel() {
		return form;
	}




}

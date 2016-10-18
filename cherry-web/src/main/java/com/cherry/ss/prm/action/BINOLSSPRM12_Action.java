/*
 * @(#)BINOLSSPRM12_Action.java     1.0 2010/11/29
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM12_BL;
import com.cherry.ss.prm.form.BINOLSSPRM12_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品类别Action
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM12_Action extends BaseAction
		implements ModelDriven<BINOLSSPRM12_Form> {
			
	private static final long serialVersionUID = 1109452674313801683L;

	@Resource
	private BINOLSSPRM12_BL binolssprm12_BL;
			
	/** 参数FORM */
	private BINOLSSPRM12_Form form = new BINOLSSPRM12_Form();
	
	/** 促销品类别信息 */
	private Map prmCategoryInfo;
	
	/** 列表和树模式迁移判断flg */
	private String modeFlg;
	
	@Override
	public BINOLSSPRM12_Form getModel() {
		return form;
	}


	public void setPrmCategoryInfo(Map prmCategoryInfo) {
		this.prmCategoryInfo = prmCategoryInfo;
	}


	public Map getPrmCategoryInfo() {
		return prmCategoryInfo;
	}


	public void setModeFlg(String modeFlg) {
		this.modeFlg = modeFlg;
	}


	public String getModeFlg() {
		return modeFlg;
	}


	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init(){
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 促销品类别ID
		map.put("prmCategoryId", form.getPrmCategoryId());;
		// 促销品类别信息
		prmCategoryInfo = binolssprm12_BL.searchPrmCategoryInfo(map);
		if(modeFlg != null && !"".equals(modeFlg)) {
			return "success_tree";
		}
		return SUCCESS;
	}
}

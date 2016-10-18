/*
 * @(#)BINOLSSPRM08_Action.java     1.0 2010/11/29
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
import com.cherry.ss.prm.bl.BINOLSSPRM08_BL;
import com.cherry.ss.prm.form.BINOLSSPRM08_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品分类详细Action
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM08_Action extends BaseAction
		implements ModelDriven<BINOLSSPRM08_Form> {
			
	private static final long serialVersionUID = 1109452674313801683L;

	@Resource
	private BINOLSSPRM08_BL binolssprm08_BL;
			
	/** 参数FORM */
	private BINOLSSPRM08_Form form = new BINOLSSPRM08_Form();
	
	/** 促销品分类信息 */
	private Map prmTypeInfo;

	@Override
	public BINOLSSPRM08_Form getModel() {
		return form;
	}
	
	public void setPrmTypeInfo(Map prmTypeInfo) {
		this.prmTypeInfo = prmTypeInfo;
	}


	public Map getPrmTypeInfo() {
		return prmTypeInfo;
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
		// 促销品分类ID
		map.put("prmTypeId", form.getPrmTypeId());
		// 促销品分类信息
		prmTypeInfo = binolssprm08_BL.searchPrmTypeInfo(map);
		return SUCCESS;
	}


}

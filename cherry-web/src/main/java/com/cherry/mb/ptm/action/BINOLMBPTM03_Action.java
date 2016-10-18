/*
 * @(#)BINOLMBPTM03_Action.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.action;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mb.ptm.bl.BINOLMBPTM03_BL;
import com.cherry.mb.ptm.form.BINOLMBPTM03_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 查询积分明细信息Action
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM03_Action extends BaseAction implements ModelDriven<BINOLMBPTM03_Form> {
	
	private static final long serialVersionUID = -2845565288481338337L;
	
	/** 查询积分明细信息BL */
	@Resource
	private BINOLMBPTM03_BL binOLMBPTM03_BL;
	
	/**
	 * 积分明细信息画面初期处理
	 * 
	 * @return 积分明细信息画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 取得积分明细信息
		pointInfoMap = binOLMBPTM03_BL.getPointInfoDetail(map);
		return SUCCESS;
	}
	
	/** 积分明细信息 */
	private Map pointInfoMap;
	
	public Map getPointInfoMap() {
		return pointInfoMap;
	}

	public void setPointInfoMap(Map pointInfoMap) {
		this.pointInfoMap = pointInfoMap;
	}

	/** 查询积分明细信息Form */
	private BINOLMBPTM03_Form form = new BINOLMBPTM03_Form();

	@Override
	public BINOLMBPTM03_Form getModel() {
		return form;
	}

}

/*
 * @(#)BINOLBSREG02_Action.java     1.0 2011/11/23
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
package com.cherry.bs.reg.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.form.BINOLBSREG02_Form;
import com.cherry.bs.reg.interfaces.BINOLBSREG02_IF;
import com.cherry.cm.core.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 区域详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
@SuppressWarnings("unchecked")
public class BINOLBSREG02_Action extends BaseAction implements ModelDriven<BINOLBSREG02_Form> {

	private static final long serialVersionUID = -1417425486045744571L;
	
	/** 区域详细画面BL */
	@Resource
	private BINOLBSREG02_IF binOLBSREG02_BL;
	
	/**
	 * 
	 * 区域详细画面初期处理
	 * 
	 * @return 区域详细画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("regionId", form.getRegionId());
		regionInfo = binOLBSREG02_BL.getRegionInfo(map);
		
		return SUCCESS;
	}
	
	/** 区域详细信息 */
	private Map regionInfo;
	
	public Map getRegionInfo() {
		return regionInfo;
	}

	public void setRegionInfo(Map regionInfo) {
		this.regionInfo = regionInfo;
	}

	/** 区域详细画面Form */
	private BINOLBSREG02_Form form = new BINOLBSREG02_Form();

	@Override
	public BINOLBSREG02_Form getModel() {
		return form;
	}

}

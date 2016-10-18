/*
 * @(#)BINOLSSPRM44_Action.java     1.0 2012/04/06
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM44_BL;
import com.cherry.ss.prm.form.BINOLSSPRM44_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 收货单明细Action
 * 
 * 
 * 
 * @author LuoHong
 * @version 1.0 2012.04.06
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM44_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM44_Form> {

	private static final long serialVersionUID = 272601907452723030L;

	@Resource
	private BINOLSSPRM44_BL binolssprm44BL;

	/** 参数FORM */
	private BINOLSSPRM44_Form form = new BINOLSSPRM44_Form();

	/** 收货单信息 */
	private Map deliverInfo;

	/** 收货单明细List */
	private List deliverDetailList;

	public BINOLSSPRM44_Form getModel() {
		return form;
	}
	
	public Map getDeliverInfo() {
		return deliverInfo;
	}

	public void setDeliverInfo(Map deliverInfo) {
		this.deliverInfo = deliverInfo;
	}

	public List getDeliverDetailList() {
		return deliverDetailList;
	}

	public void setDeliverDetailList(List deliverDetailList) {
		this.deliverDetailList = deliverDetailList;
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
	public String init() {
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收货ID
		map.put("deliverId", form.getDeliverId());
		// 收货单信息
		deliverInfo = binolssprm44BL.searchDeliverInfo(map).get(0);
		// 收货单明细List
		deliverDetailList = binolssprm44BL.searchDeliverDetailList(map);
        
        return SUCCESS;
    }
}

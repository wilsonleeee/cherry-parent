/*
 * @(#)BINOLSSPRM26_Action.java     1.0 2010/11/05
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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM26_BL;
import com.cherry.ss.prm.form.BINOLSSPRM26_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点单明细Action
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.05
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM26_Action extends BaseAction
		implements ModelDriven<BINOLSSPRM26_Form> {
			
	private static final long serialVersionUID = 1109452674313801683L;

	@Resource
	private BINOLSSPRM26_BL binolssprm26BL;
    /** 共通BL */
    @Resource
    private BINOLCM14_BL binOLCM14_BL;
			
	/** 参数FORM */
	private BINOLSSPRM26_Form form = new BINOLSSPRM26_Form();
	
	/** 盘点单信息 */
	private Map takingInfo;
	
	/** 盘点单明细List */
	private List takingDetailList;
	/** 汇总信息 */
    private Map<String, Object> sumInfo;
	
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}
	@Override
	public BINOLSSPRM26_Form getModel() {
		return form;
	}
	
	public Map getTakingInfo() {
		return takingInfo;
	}

	public void setTakingInfo(Map takingInfo) {
		this.takingInfo = takingInfo;
	}

	public List getTakingDetailList() {
		return takingDetailList;
	}

	public void setTakingDetailList(List takingDetailList) {
		this.takingDetailList = takingDetailList;
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
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        //组织ID
        String organizationId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        //品牌ID
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
		// 促销产品盘点ID
		map.put("stockTakingId", form.getStockTakingId());
		// 盈亏
		String profitKbn = form.getProfitKbn();
		map.put("profitKbn", profitKbn);
		if(CherryChecker.isNullOrEmpty(profitKbn)){
			// 无盈亏参数时，盘点单明细按商品盘点的商品排序字段
	        String detailOrderBy = binOLCM14_BL.getConfigValue("1092", organizationId, brandInfoId);
	        map.put("detailOrderBy", detailOrderBy);
		}
		// 盘点单信息
		takingInfo = binolssprm26BL.searchTakingInfo(map);
		// 盘点单明细List
		takingDetailList = binolssprm26BL.searchTakingDetailList(map);
		//汇总信息sumInfo
		sumInfo = binolssprm26BL.getSumInfo(map);
		return SUCCESS;
	}
}

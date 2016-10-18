/*
 * @(#)BINOLSTJCS12_Action.java     1.0 2015/12/18
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
package com.cherry.st.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS12_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS12_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 电商产品对应关系一览Action
 * 
 * @author lzs
 * @version 1.0 2015.12.18
 */
public class BINOLSTJCS12_Action extends BaseAction implements ModelDriven<BINOLSTJCS12_Form> {

	private static final long serialVersionUID = -1595080394666513447L;

	/** 参数From */
	private BINOLSTJCS12_Form form = new BINOLSTJCS12_Form();
	
	/** 接口 */
	@Resource(name = "binOLSTJCS12_BL")
	private BINOLSTJCS12_IF binOLSTJCS12_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/**电商产品对应关系一览List**/
	private List<Map<String, Object>> productRelationList;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

    /**
     * <p>
     * 画面初期显示
     * </p>
     * 
     * @param 无
     * @return String 跳转页面
     * 
     */
    public String init() throws Exception {
    	Map<String,Object> map = getCommonMap();
    	
		String brandInfoIdtemp = ConvertUtil.getString(form.getBrandInfoId());
		// 取得品牌List
		if(!"".equals(brandInfoIdtemp)) {
			// 其他页面调用此页面的情况
		} else if ("-9999".equals(map.get(CherryConstants.BRANDINFOID))) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", map.get(CherryConstants.BRANDINFOID));
			// 品牌名称
			brandMap.put("brandName", map.get(CherryConstants.BRAND_NAME));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		}
      
        return SUCCESS;
    }
    /**
     * 电商产品对应关系一览查询
     * @return
     */
	public String search() {
		Map<String, Object> map = getCommonMap();
		
		map.put("outCode", form.getOutCode());
		map.put("unitCode", form.getUnitCode());
		map.put("barCode", form.getBarCode());
		map.put("skuCode", form.getSkuCode());
		ConvertUtil.setForm(form, map);	
		int count = binOLSTJCS12_BL.getProductRelationCount(map);
		if(count > 0){
			productRelationList = binOLSTJCS12_BL.getProductRelationList(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		}
		return SUCCESS;
	}
	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getCommonMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 当前用户的所属品牌
		if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		//品牌名称
		map.put(CherryConstants.BRAND_NAME,userInfo.getBrandName());
		return map;
	}
	
	public List<Map<String, Object>> getProductRelationList() {
		return productRelationList;
	}
	public void setProductRelationList(List<Map<String, Object>> productRelationList) {
		this.productRelationList = productRelationList;
	}
	@Override
	public BINOLSTJCS12_Form getModel() {
		return form;
	}
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
}

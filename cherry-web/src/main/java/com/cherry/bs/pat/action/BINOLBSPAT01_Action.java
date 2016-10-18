/*  
 * @(#)BINOLBSPAT01_Action.java     1.0 2011/10/19    
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
package com.cherry.bs.pat.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pat.form.BINOLBSPAT01_Form;
import com.cherry.bs.pat.interfaces.BINOLBSPAT01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 单位一览
 * @author LuoHong
 *
 */

public class BINOLBSPAT01_Action extends BaseAction implements
ModelDriven<BINOLBSPAT01_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4026423981177737034L;
	@Resource(name="binOLBSPAT01_BL")
	private BINOLBSPAT01_IF binOLBSPAT01_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	        
	/** 参数FORM */
	private BINOLBSPAT01_Form form = new BINOLBSPAT01_Form();
	
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		// 语言
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 取得品牌List
		if (userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
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
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		}
		// 所属品牌(用于查询出区域信息)
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得区域List
		reginList = binOLCM00_BL.getReginList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 根据品牌ID筛选下拉列表
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 取得区域List
		resultMap.put("reginList", binOLCM00_BL.getReginList(map));
		
		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}
	
	/**
	 * <p>
	 *往来单位一览
	 * </p>
	 * 
	 * @return
	 */

	public String search() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap= getSearchMap();
		// 取得总数
		int count = binOLBSPAT01_BL.searchPartnerCount(searchMap);
		if (count > 0) {
			// 取得单位List
			form.setPartnerList(binOLBSPAT01_BL.searchPartnerList(searchMap));
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLBSPAT01_1";
	}	
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId", form.getBrandInfoId());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 单位ID
		map.put("partnerId", form.getPartnerId());
		// 单位编码
		map.put("code", form.getCode());
		//单位名称
		map.put("name", form.getName());
		//单位地址
		map.put("address", form.getAddress());	
		// 联系电话
		map.put("phoneNumber", form.getPhoneNumber());
		// 邮编	
		map.put("postalCode", form.getPostalCode());
		// 所属省份
		map.put("provinceId", form.getProvinceId());
		// 所属城市
		map.put("cityId", form.getCityId());
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		return map;
	}

    /**
     * <p>
     * 单位启用
     * </p>
     * 
     * @return
     */
    public String enable() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("partnerId",form.getPartnerIdArr());
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPAT01");	
		binOLBSPAT01_BL.tran_enablePartner(map);
        return "BINOLBSPAT01_1";
    }
	
    /**
     * <p>
     * 单位停用
     * </p>
     * 
     * @return
     */
    public String disable() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session
                .get(CherryConstants.SESSION_USERINFO);
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("partnerId",form.getPartnerIdArr());
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPAT01");	
		binOLBSPAT01_BL.tran_disablePartner(map);
        return "BINOLBSPAT01_1";
    }

	public BINOLBSPAT01_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

}

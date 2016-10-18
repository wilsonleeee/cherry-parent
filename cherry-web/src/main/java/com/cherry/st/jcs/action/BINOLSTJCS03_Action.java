package com.cherry.st.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS03_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS03_IF;
import com.opensymphony.xwork2.ModelDriven;

/*  
 * @(#)BINOLSTJCS03_Action.java    1.0 2012-6-18     
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
public class BINOLSTJCS03_Action extends BaseAction implements ModelDriven<BINOLSTJCS03_Form>{

	private static final long serialVersionUID = 1L;

	private BINOLSTJCS03_Form form = new BINOLSTJCS03_Form();
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	/**接口*/
	@Resource(name="binOLSTJCS03_BL")
	private BINOLSTJCS03_IF binOLSTJCS03_BL;
	
	/**实体仓库信息*/
	private Map depotInfoMap = null;
	
	/**部门仓库关系*/
	private List<Map> relInfoList = null;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/**
	 * 初始化
	 * 
	 * */
	public String init(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		map.put("depotId", form.getDepotId());
		
		//调用接口取得实体仓库信息
		depotInfoMap = binOLSTJCS03_BL.getDepotInfoById(map);
		
		// 总部用户的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		
		if(null != brandInfoList && !brandInfoList.isEmpty()){
			
			String brandInfoId = ConvertUtil.getString(brandInfoList.get(0).get("brandInfoId"));
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("brandInfoId", brandInfoId);
			//调用共通取得区域list
			reginList = binOLCM00_BL.getReginList(paraMap);
			
		}
		
		if(null == depotInfoMap){
			depotInfoMap = new HashMap();
		}
		
		depotInfoMap.put("depotId", map.get("depotId"));
		
		if(null == depotInfoMap.get("brandInfoId") || "".equals(depotInfoMap.get("brandInfoId"))){
			depotInfoMap.put("brandInfoId", brandInfoList.get(0).get("brandInfoId"));
			depotInfoMap.put("brandName", brandInfoList.get(0).get("brandName"));
		}
		
		//调用接口取得部门仓库关系
		relInfoList = binOLSTJCS03_BL.getRelInfoByDepotId(map);
		if(null == relInfoList){
			relInfoList = new ArrayList<Map>();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 创建保存
	 * 
	 * */
	public String save() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 语言
			String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
			if(language != null) {
				map.put(CherryConstants.SESSION_LANGUAGE, language);
			}
			if(userInfo.getBIN_BrandInfoID() == -9999){
				//品牌
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			}else{
				//品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
			//仓库ID
			map.put("depotId", form.getDepotId());
			//归属部门
			map.put("organizationID", form.getOrganizationID().trim());
			//测试区分
			map.put("testType", form.getTestType());
			//仓库编码
			map.put("depotCode", form.getDepotCode().trim());
			//仓库名称中文
			map.put("depotNameCN", form.getDepotNameCN().trim());
			//仓库名称英文
			map.put("depotNameEN", form.getDepotNameEN().trim());
			//关联部门ID
			map.put("relOrganizationIDArr", form.getRelOrganizationIDArr());
			//关联部门备注
			map.put("commentArr", form.getCommentArr());
			//默认区分
			map.put("defaultFlagArr", form.getDefaultFlagArr());
			//区域ID
//			map.put("regionID", form.getRegionId().trim());
//			//省份ID
//			map.put("provinceID", form.getProvinceId().trim());
//			//市ID
//			map.put("cityID", form.getCityId().trim());
//			//县级市ID
//			map.put("countyID", form.getCountyId().trim());
			//地址
			map.put("address", form.getAddress().trim());
			// 更新者为当前用户
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名为当前程序
			map.put(CherryConstants.UPDATEPGM, "BINOLSTJCS02");
			
			//调用接口进行保存
			binOLSTJCS03_BL.tran_updateDepotInfo(map);
			
			//操作成功!
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}catch(Exception e){
			if(e instanceof CherryException){
				this.addActionError(((CherryException) e).getErrMessage());
			}else{
				this.addActionError(e.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	} 
	
	/**
	 * 保存验证
	 * 
	 * */
	public void validateSave() throws Exception{
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 参数MAP
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		
		String depotCode = form.getDepotCode().trim();
		//验证code是否为空
		if (CherryChecker.isNullOrEmpty(depotCode)) {
			this.addFieldError("depotCode", getText(
					"ECM00009", new String[] {getText("PST00001")}));
		}
		//验证code长度是否超出最大限制(最大长度为10)
		if(ConvertUtil.getStringLength(depotCode) > 10){
			this.addFieldError("depotCode", getText(
					"ECM00020", new String[] {getText("PST00001"),"10"}));
		}
		//验证仓库名称是否为空
		if(form.getDepotNameCN().trim().length()<=0){
			this.addFieldError("depotNameCN", getText(
					"ECM00009", new String[] {getText("PST00014")}));
		}
		//验证仓库中文名称最大长度不能超过60
		if(form.getDepotNameCN().trim().length() > 60){
			this.addFieldError("depotNameCN", getText(
					"ECM00020", new String[] {getText("PST00014"),"60"}));
		}
		//验证仓库英文名称最大长度不能超过60
		if(form.getDepotNameEN().trim().length() > 60){
			this.addFieldError("depotNameCN", getText(
					"ECM00020", new String[] {getText("PST00012"),"60"}));
		}
		//验证地址长度不能超过100
		if(form.getAddress().trim().length() > 100){
			this.addFieldError("address", getText(
					"ECM00020", new String[] {getText("PST00013"),"100"}));
		}
		
		 //仓库编码
		paramsMap.put("depotCode", depotCode);
		 //所属组织
		paramsMap.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		//仓库ID
		paramsMap.put("depotId", form.getDepotId());
		
		//是否已经存在
		List<Map> list =binOLSTJCS03_BL.isExist(paramsMap);
		if(null != list && !list.isEmpty()){
			this.addFieldError("depotCode",getText("ECM00032",new String[]{getText("PST00001")}));
		}
		
	}
	
	@Override
	public BINOLSTJCS03_Form getModel() {
		return form;
	}

	public Map getDepotInfoMap() {
		return depotInfoMap;
	}

	public void setDepotInfoMap(Map depotInfoMap) {
		this.depotInfoMap = depotInfoMap;
	}

	public List<Map> getRelInfoList() {
		return relInfoList;
	}

	public void setRelInfoList(List<Map> relInfoList) {
		this.relInfoList = relInfoList;
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

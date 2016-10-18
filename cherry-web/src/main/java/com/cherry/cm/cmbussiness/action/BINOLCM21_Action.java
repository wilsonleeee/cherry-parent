
/*  
 * @(#)BINOLCM21_Action.java    1.0 2011-9-16     
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
package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM21_Form;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM21_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM20_Service;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCM21_Action extends BaseAction implements
		ModelDriven<BINOLCM21_Form> {

	private static final long serialVersionUID = 1L;

	private BINOLCM21_Form form = new BINOLCM21_Form();
	
	@Resource
	private BINOLCM21_IF binOLCM21_BL;
	
	
	/** 系统配置项 共通BL */
	
	@Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
	
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM20_Service bINOLCM20_Service;
	
	
	public Map<String,Object> getSessionMap(){
		
		Map<String,Object> sessionMap = new HashMap<String,Object>();
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		//登录用户组织
		sessionMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//登录语言
		sessionMap.put("language", userInfo.getLanguage());
		//登录用户所属品牌
		sessionMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		sessionMap.put("userId", String.valueOf(userInfo.getBIN_UserID()));
		
		return sessionMap;
	}
	
	/**
	 * 取得产品信息
	 * @throws Exception 
	 * 
	 * */
	public void getProductInfo() throws Exception{
		
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("productInfoStr", form.getProductInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		String resultStr = binOLCM21_BL.getProductInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	
	/**
	 * 取得产品信息（浓妆淡抹订货商城）
	 * @throws Exception 
	 * 
	 * */
	public void getProductInfoNZDM() throws Exception{
		
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("productInfoStr", form.getProductInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		String resultStr = binOLCM21_BL.getProductInfoNZDM(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 取得柜台产品信息
	 * @throws Exception 
	 * 
	 * */
	public void getCntProductInfo() throws Exception{
		
		Map<String,Object> paramMap = this.getSessionMap();
		
		
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		//语言
        String language = userInfo.getLanguage();
        
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String isSocket=binOLCM14_BL.getWebposConfigValue("9015", organizationInfoId, brandInfoId);
        
		//实体仓库ID
		String organizationId = "";
		if(counterInfo != null){
			organizationId = ConvertUtil.getString(counterInfo.getOrganizationId());
		}else{
			organizationId = ConvertUtil.getString(userInfo.getBIN_OrganizationID());
		}
		List<Map<String,Object>> departList = binOLCM18_BL.getDepotsByDepartID(organizationId, language);
		String entitySocketId=null;
		if(null == departList || departList.size()==0){
			entitySocketId="";
		}else{
			entitySocketId=ConvertUtil.getString(departList.get(0).get("BIN_DepotInfoID"));
		}
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("counterCode", form.getCounterCode() !=null ? form.getCounterCode().trim() : null);
		paramMap.put("productInfoStr", form.getProductInfoStr().trim());
		paramMap.put("originalBrand", ConvertUtil.getString(form.getOriginalBrandStr()));
		paramMap.put("number", form.getNumber());
		paramMap.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		String resultStr=null;
		if("N".equals(isSocket)){
			resultStr = binOLCM21_BL.getCntProductInfoAddSocket(entitySocketId, paramMap);
		}else{
			resultStr=binOLCM21_BL.getCntProductInfo(paramMap);
			
		}
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	
	/**
	 * 取得Codes
	 * @throws Exception 
	 * 
	 * */
	public void getCodes() throws Exception{
		
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("originalBrandStr", form.getOriginalBrandStr().trim());
		paramMap.put("codeType", form.getCodeType().trim());
		
		paramMap.put("number", form.getNumber());
		String resultStr = binOLCM21_BL.getCodes(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
    /**
     * 取得促销品信息
     * @throws Exception 
     * 
     * */
    public void getPrmProductInfo() throws Exception{
        
        Map<String,Object> paramMap = this.getSessionMap();
        
        if("-9999".equals(paramMap.get("brandInfoId"))){
            paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
        }
        paramMap.put("productInfoStr", form.getProductInfoStr().trim());
        paramMap.put("number", form.getNumber());
        paramMap.put(CherryConstants.VALID_FLAG, form.getValidFlag());
        String resultStr = binOLCM21_BL.getPrmProductInfo(paramMap);
        
        ConvertUtil.setResponseByAjax(response, resultStr);
    }
	
	/**
	 * 取得柜台信息
	 * 
	 * */
	public void getCounterInfo() throws Exception{
		
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(ConvertUtil.getString(paramMap.get("brandInfoId")))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("counterInfoStr", form.getCounterInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		paramMap.put("privilegeFlag", form.getPrivilegeFlag());
		// 业务类型
		paramMap.put("businessType", "0");
		// 操作类型
		paramMap.put("operationType", "1");
		String resultStr = binOLCM21_BL.getCounterInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 取得部门信息
	 * 
	 * 
	 * */
	public void getDepartInfo() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("departInfoStr", form.getDepartInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		paramMap.put("flag", form.getFlag());
		paramMap.put("privilegeFlag", form.getPrivilegeFlag());
		// 业务类型
		paramMap.put("businessType", "0");
		// 操作类型
		paramMap.put("operationType", "1");
		String resultStr = binOLCM21_BL.getDepartInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 取得产品分类
	 * 
	 * */
	public void getProductCategory() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("prtCatInfoStr", form.getPrtCatInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		String resultStr = binOLCM21_BL.getProductCategory(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 獲取實體倉庫信息，根據設定的參數可以控制是否包含區域信息
	 * 
	 * 
	 * */
	public void getDeportInfo() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("deportInfoStr", form.getDeportInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		paramMap.put("onlyNoneCounterDeport", form.getOnlyNoneCounterDeport());
		paramMap.put("onlyCounterDeport", form.getOnlyCounterDeport());
		paramMap.put("includeRegion", form.getFlag());
		paramMap.put("includeDepart", form.getIncludeDepart());
		paramMap.put("onlyDepart", form.getOnlyDepart());
		String resultStr = binOLCM21_BL.getDeportInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 根据输入的字符串模糊查询员工信息
	 * 
	 * 
	 * */
	public void getEmployeeInfo() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("employeeInfoStr", form.getEmployeeInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		paramMap.put("privilegeFlag", form.getPrivilegeFlag());
		// 业务类型
		paramMap.put("businessType", "0");
		// 操作类型
		paramMap.put("operationType", "1");
		
		String resultStr = binOLCM21_BL.getEmployeeInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 根据输入的字符串模糊查询营业员信息
	 * @throws Exception
	 */
	public void getBaInfo() throws Exception {
		Map<String, Object> paramMap = this.getSessionMap();
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("employeeInfoStr", form.getEmployeeInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		paramMap.put("privilegeFlag", form.getPrivilegeFlag());
		// 需要过滤掉的特殊BA编码值
		paramMap.put("filterBaCode", form.getFilterBaCode());
		// 业务类型
		paramMap.put("businessType", "0");
		// 操作类型
		paramMap.put("operationType", "1");
		
		String resultStr = binOLCM21_BL.getBaInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 根据输入的字符串模糊查询代理商信息
	 * @throws Exception
	 */
	public void getResellerInfo() throws Exception {
		Map<String, Object> paramMap = this.getSessionMap();
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("resellerInfoStr", form.getTextInfoStr().trim());
		paramMap.put("parentResellerCode", form.getParentValue());
		paramMap.put("levelCode", form.getFlag());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		paramMap.put("resellerType", form.getResellerType());
		paramMap.put("provinceId", form.getProvinceId());
		paramMap.put("cityId", form.getCityId());
		
		String resultStr = binOLCM21_BL.getResellerInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 根据输入的字符串模糊查询销售人员信息
	 * 
	 * 
	 * */
	public void getSalesStaffInfo() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("employeeInfoStr", form.getEmployeeInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		String categoryCode = ConvertUtil.getString(form.getCategoryCode());
		if(!"ALL".equals(categoryCode)){
			if("".equals(categoryCode)){
				paramMap.put("categoryCode", CherryConstants.CATRGORY_CODE_BA); // BA岗位类别Code
			}else{
				paramMap.put("categoryCode", categoryCode); // BA岗位类别Code
			}
		}
		paramMap.put("privilegeFlag", form.getPrivilegeFlag());
		// 业务类型
		paramMap.put("businessType", "0");
		// 操作类型
		paramMap.put("operationType", "1");
		String resultStr = binOLCM21_BL.getSalesStaffInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 取得往来单位(合作伙伴)
	 * 
	 * */
	public void getBussinessPartnerInfo() throws Exception{
		
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("partnerInfoStr", form.getPartnerInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		
		String resultStr = binOLCM21_BL.getBussinessPartnerInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 根据输入的字符串模糊查询区域信息
	 * 
	 * 
	 * */
	public void getRegionInfo() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("regionInfoStr", form.getRegionInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		
		String resultStr = binOLCM21_BL.getRegionInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 根据输入的字符串模糊查询逻辑仓库信息
	 * 
	 * 
	 * */
	public void getLogicInventoryInfo() throws Exception{
		
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("logicInventoryInfoStr", form.getLogicInventoryInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		
		String resultStr = binOLCM21_BL.getLogicInventoryInfo(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
		
	}
	
	/**
	 * 根据输入的字符串模糊查询渠道信息
	 * @throws Exception
	 */
	public void getChannelInfo() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("channelInfoStr", form.getChannelInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		String resultStr = binOLCM21_BL.getChannelInfo(paramMap);
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 根据输入的字符串模糊查询渠道信息
	 * @throws Exception
	 */
	public void getIssueInfo() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("issueNoKw", form.getIssueNoKw().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("selected", form.getSelected().trim());
		paramMap.put("memberInfoId", form.getMemberInfoId());
		String resultStr = binOLCM21_BL.getIssueInfo(paramMap);
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 取得组织详细信息
	 * 
	 * 
	 * */
	public void getOrganizationDetail() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("departInfoStr", form.getDepartInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("flag", form.getFlag());
		paramMap.put("privilegeFlag", form.getPrivilegeFlag());
		// 业务类型
		paramMap.put("businessType", "0");
		// 操作类型
		paramMap.put("operationType", "1");
		String resultStr = binOLCM21_BL.getOrganizationDetail(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	/**
	 * 取得组织详细信息
	 * 
	 * 
	 * */
	public void getCounterDetail() throws Exception{
		Map<String,Object> paramMap = this.getSessionMap();
		if("-9999".equals(paramMap.get("brandInfoId"))){
			paramMap.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		paramMap.put("counterInfoStr", form.getCounterInfoStr().trim());
		paramMap.put("number", form.getNumber());
		paramMap.put("conditionType", form.getConditionType());
		paramMap.put("conditionValue", form.getConditionValue());
		paramMap.put("privilegeFlag", form.getPrivilegeFlag());
		String resultStr = binOLCM21_BL.getCounterDetail(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	public void getBaByCounter() throws Exception{
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
				
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("baInfoStr", form.getBaInfoStr().trim());
		paramMap.put("number", form.getNumber());
		// 柜台部门ID
		paramMap.put("organizationId", counterInfo.getOrganizationId());
		// 获取BA列表
		String resultStr = binOLCM21_BL.getBaByCounter(paramMap);
		
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	@Override
	public BINOLCM21_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}

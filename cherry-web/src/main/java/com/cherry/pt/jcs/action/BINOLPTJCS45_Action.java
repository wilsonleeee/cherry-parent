/*  
 * @(#)BINOLPTJCS45_Action.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS45_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS45_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 方案一览
 * @author jijw
 *
 */
public class BINOLPTJCS45_Action extends BaseAction implements
ModelDriven<BINOLPTJCS45_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4026423981177737034L;
    
	@Resource
	private BINOLCM00_BL binOLCM00BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLPTJCS45_IF binOLPTJCS45_IF;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 是否小店云模式 */
	private String isPosCloud;
	        
	/** 参数FORM */
	private BINOLPTJCS45_Form form = new BINOLPTJCS45_Form();
	
	private List<Map<String, Object>> brandInfoList;
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public String getIsPosCloud() {
		return isPosCloud;
	}

	public void setIsPosCloud(String isPosCloud) {
		this.isPosCloud = isPosCloud;
	}
	
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
	@SuppressWarnings("unchecked")
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID,userInfo.getBIN_BrandInfoID());
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
//			Map<String, Object> brandMap = new HashMap<String, Object>();
//			// 品牌ID
//			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
//			// 品牌名称
//			brandMap.put("brandName", getText("PPL00006"));
//			if (null != brandInfoList && !brandInfoList.isEmpty()) {
//				brandInfoList.add(0, brandMap);
//			} else {
//				brandInfoList = new ArrayList<Map<String, Object>>();
//				brandInfoList.add(brandMap);
//			}
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
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		form.setHolidays(binOLCM00BL.getHolidays(map));
		// 开始日期
		//form.setStartDate(binOLCM00BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		//form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		
		// 是否小店云系统模式
		isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		return SUCCESS;
	}
	
	/**
	 * <p>
	 *方案一览
	 * </p>
	 * 
	 * @return
	 */  

	public String search() throws Exception {
		// 验证提交的参数
//		if (!validateForm()) {
//			return CherryConstants.GLOBAL_ACCTION_RESULT;
//		}
		// 取得参数MAP
		Map<String, Object> searchMap= getParamMap();
		
		// 方案名称
		searchMap.put("solutionName", form.getSolutionName().trim());	
		// 方案Code
		searchMap.put("solutionCode", form.getSolutionCode().trim());
		// 有效区分
		searchMap.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		
		// ******************* WITPOSQA-15735票  start *******************
		// 取得当前用户是否是柜台用户
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		if(userInfo.getDepartType().equals("4")){
			searchMap.put("isCntDepart", 1);
			
			searchMap.put("counter", 1);	
			String [] locationList = new String[]{userInfo.getDepartCode()};
			searchMap.put("locationList", locationList);
			
			searchMap.put("placeType", "7");
		}
		// ******************* WITPOSQA-15735票  end *******************
		
		// 取得总数
		int count = binOLPTJCS45_IF.searchSolutionCount(searchMap);
		if (count > 0) {
			// 取得渠道List
			form.setSolutionList(binOLPTJCS45_IF.searchSolutionList(searchMap));
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		// 是否小店云系统模式
		isPosCloud = binOLCM14_BL.getConfigValue("1304", String.valueOf(searchMap.get("organizationInfoId")), String.valueOf(searchMap.get("brandInfoId")));
//		isPosCloud = "";
		// AJAX返回至dataTable结果页面
		return "BINOLPTJCS45_1";
	}	
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getParamMap() {
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
		
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS45");	
		return map;
	}

    /**
     * <p>
     * 方案停用/启用
     * </p>
     * 
     * @return
     */
    public String disOrEnableSolu() throws Exception {
    	try{
        // 参数MAP
        Map<String, Object> map = getParamMap();
        map.put("solutionIdArr",form.getSolutionIdArr());
        map.put("validFlag",form.getValidFlag());

		binOLPTJCS45_IF.tran_disOrEnableSolu(map);
		
		this.addActionMessage(getText("ICM00002"));
    	}catch(Exception e){
    		this.addActionError(e.getMessage());
    	}
    	return CherryConstants.GLOBAL_ACCTION_RESULT;
    }

	public BINOLPTJCS45_Form getModel() {
		return form;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
//		// 开始日期
//		String startDate = form.getStartDate();
//		// 结束日期
//		String endDate = form.getEndDate();
//		//开始日期验证
//		if (startDate != null && !"".equals(startDate)) {
//			// 日期格式验证
//			if(!CherryChecker.checkDate(startDate)) {
//				this.addActionError(getText("ECM00008", new String[]{"开始日期"}));
//				isCorrect = false;
//			}
//		}
//		//结束日期验证
//		if (endDate != null && !"".equals(endDate)) {
//			// 日期格式验证
//			if(!CherryChecker.checkDate(endDate)) {
//				this.addActionError(getText("ECM00008", new String[]{"结束日期"}));
//				isCorrect = false;
//			}
//		}
//		if (isCorrect && startDate != null && !"".equals(startDate)&& 
//				endDate != null && !"".equals(endDate)) {
//			// 开始日期在结束日期之后
//			if(CherryChecker.compareDate(startDate, endDate) > 0) {
//				this.addActionError(getText("ECM00019"));
//				isCorrect = false;
//			}
//		}
	return isCorrect;
	}
}

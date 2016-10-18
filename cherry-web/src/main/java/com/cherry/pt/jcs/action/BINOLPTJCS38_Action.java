/*  
 * @(#)BINOLPTJCS38_Action.java     1.0 2015/01/19      
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
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.jcs.form.BINOLPTJCS38_Form;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS38_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品功能开启时间一览
 * @author jijw
 *
 */
public class BINOLPTJCS38_Action extends BaseAction implements
ModelDriven<BINOLPTJCS38_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4026423981177737034L;
    
	@Resource
	private BINOLCM00_BL binOLCM00BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLPTJCS38_IF binOLPTJCS38_IF;
	        
	/** 参数FORM */
	private BINOLPTJCS38_Form form = new BINOLPTJCS38_Form();
	
	private List<Map<String, Object>> brandInfoList;
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
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
		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
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
		return SUCCESS;
	}
	
	/**
	 * <p>
	 *产品功能开启时间一览
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
		
		// 产品功能类别
		searchMap.put("prtFunType", form.getPrtFunType());
		searchMap.put("startDate", form.getStartDate());
		searchMap.put("endDate", form.getEndDate());
		// 有效区分
		searchMap.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		
		// 取得总数
		int count = binOLPTJCS38_IF.searchPrtFunCount(searchMap);
		if (count > 0) {
			// 取得产品功能开启时间List
			form.setPrtFunList(binOLPTJCS38_IF.searchPrtFunList(searchMap));
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTJCS38_1";
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
		map.put(CherryConstants.UPDATEPGM, "BINOLPTJCS38");	
		return map;
	}

    /**
     * 功能停用/启用
     * 
     * @return
     */
    public String disOrEnableFun() throws Exception {
    	try{
	        // 参数MAP
	        Map<String, Object> map = getParamMap();
	        map.put("prtFunIdArr",form.getPrtFunIdArr());
	        map.put("validFlag",form.getValidFlag());
	
			binOLPTJCS38_IF.tran_disOrEnableFun(map);
		
			this.addActionMessage(getText("ICM00002"));
    	}catch(Exception e){
    		this.addActionError(e.getMessage());
    	}
    	return CherryConstants.GLOBAL_ACCTION_RESULT;
    }

	public BINOLPTJCS38_Form getModel() {
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

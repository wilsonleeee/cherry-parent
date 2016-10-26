/*  
 * @(#)BINOLBSCHA01_Action.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cha.form.BINOLBSCHA01_Form;
import com.cherry.bs.cha.interfaces.BINOLBSCHA01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 渠道一览
 * @author weisc
 *
 */
public class BINOLBSCHA01_Action extends BaseAction implements
ModelDriven<BINOLBSCHA01_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4026423981177737034L;
    
	@Resource
	private BINOLCM00_BL binOLCM00BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLBSCHA01_IF binOLBSCHA01IF;
	        
	/** 参数FORM */
	private BINOLBSCHA01_Form form = new BINOLBSCHA01_Form();
	
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
		return SUCCESS;
	}
	
	/**
	 * <p>
	 *渠道一览
	 * </p>
	 * 
	 * @return
	 */

	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap= getSearchMap();
		// 取得总数
		int count = binOLBSCHA01IF.searchChannelCount(searchMap);
		if (count > 0) {
			// 取得渠道List
			form.setChannelList(binOLBSCHA01IF.searchChannelList(searchMap));
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLBSCHA01_1";
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
	    // 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		// 渠道类型
		map.put("status", form.getStatus());
		//渠道名称
        map.put("channelCode", form.getChannelCode().trim());
		//渠道名称
        map.put("channelName", form.getChannelName().trim());
		//制单人
		map.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
		// 渠道ID
		map.put("channelId", form.getChannelId());
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		return map;
	}

    /**
     * <p>
     * 渠道启用
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
        map.put("channelId",form.getChannelIdArr());
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCHA01");	
		binOLBSCHA01IF.tran_enableChannel(map);
        return "BINOLBSCHA01_1";
    }
	
    /**
     * <p>
     * 渠道停用
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
        map.put("channelId",form.getChannelIdArr());
        // 更新者
        map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCHA01");	
		binOLBSCHA01IF.tran_disableChannel(map);
        return "BINOLBSCHA01_1";
    }

	public BINOLBSCHA01_Form getModel() {
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
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		//开始日期验证
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{"开始日期"}));
				isCorrect = false;
			}
		}
		//结束日期验证
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{"结束日期"}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
	return isCorrect;
	}
}

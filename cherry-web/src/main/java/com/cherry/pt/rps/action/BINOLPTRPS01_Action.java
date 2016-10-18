/*
 * @(#)BINOLPTRPS01_Action.java     1.0 2011/3/11
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

package com.cherry.pt.rps.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.form.BINOLPTRPS01_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点查询Action
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.3.11
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS01_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS01_Form> {

    private static final long serialVersionUID = 5582161952782895825L;

	@Resource
	private BINOLCM00_BL binOLCM00_BL;

	@Resource
	private BINOLPTRPS01_IF binOLPTRPS01_BL;

	/** 参数FORM */
	private BINOLPTRPS01_Form form = new BINOLPTRPS01_Form();

	/** 盘点单List */
	private List takingList;

	/** 节日 */
	private String holidays;
	
    /** 汇总信息 */
    private Map<String, Object> sumInfo;

	public BINOLPTRPS01_Form getModel() {

		return form;
	}

	public List getTakingList() {
		return takingList;
	}

	public void setTakingList(List takingList) {
		this.takingList = takingList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
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
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE,
						CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}

	/**
	 * <p>
	 * AJAX盘点单查询
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
		Map<String, Object> searchMap = getSearchMap();
		// 取得盘点单总数
		int count = binOLPTRPS01_BL.searchTakingCount(searchMap);
        String prtVendorId = ConvertUtil.getString(searchMap.get(ProductConstants.PRT_VENDORID));
        String productName = ConvertUtil.getString(searchMap.get("productName"));
		if (count > 0) {
			// 取得盘点单List
			takingList = binOLPTRPS01_BL.searchTakingList(searchMap);
			
            if (!CherryConstants.BLANK.equals(prtVendorId) || !CherryConstants.BLANK.equals(productName)) {
                sumInfo = binOLPTRPS01_BL.getSumInfo(searchMap);
            }
		}else{
            if (!CherryConstants.BLANK.equals(prtVendorId) || !CherryConstants.BLANK.equals(productName)) {
                sumInfo = new HashMap<String,Object>();
                sumInfo.put("sumQuantity", 0);
                sumInfo.put("sumAmount", 0);
            }
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS01_1";
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 * @throws JSONException 
	 */
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 盘点单号
		map.put("stockTakingNo", form.getStockTakingNo());
		// 开始日期
		map.put("startDate", form.getStartDate());
		// 结束日期
		map.put("endDate", form.getEndDate());
		// 盈亏
		map.put("profitKbn", form.getProfitKbn());
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
	    //产品厂商ID
        map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
        //产品名称
        map.put("productName", form.getProductName());
        // 部门类型
        map.put("departType", form.getDepartType());

        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);

		return map;
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
		/*开始日期验证*/
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
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

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }
}

/*
 * @(#)BINOLSSPRM43_Action.java     1.0 2012/04/01
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
import com.cherry.ss.prm.bl.BINOLSSPRM27_BL;
import com.cherry.ss.prm.form.BINOLSSPRM27_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 收货单查询Action
 * 
 * 
 * 
 * @author LuoHong
 * @version 1.0 2012.04.01
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM43_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM27_Form> {

	private static final long serialVersionUID = 6934346836734458784L;
	
	@Resource
	private BINOLCM00_BL binOLCM00BL;
	
	@Resource
	private BINOLSSPRM27_BL binOLSSPRM27BL;
	
	/** 参数FORM */
	private BINOLSSPRM27_Form form = new BINOLSSPRM27_Form();
	
	/** 收货单List */
	private List deliverList;
	
	/** 节日  */
	private String holidays;
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public BINOLSSPRM27_Form getModel() {
		return form;
	}
	
	public List getDeliverList() {
		return deliverList;
	}

	public void setDeliverList(List deliverList) {
		this.deliverList = deliverList;
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
		map.put(CherryConstants.ORGANIZATIONINFOID, 
				userInfo.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 查询假日
		holidays = binOLCM00BL.getHolidays(map);
		// 开始日期
		form.setStartDate(binOLCM00BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * AJAX收货单查询
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
		Map<String, Object> sumInfo = binOLSSPRM27BL.getSumInfo(searchMap);
		// 取得总数
		int count = CherryUtil.obj2int(sumInfo.get("count"));
		// 取得收货单总数
		if (count > 0) {
			// 取得发货单List
			deliverList = binOLSSPRM27BL.searchDeliverList(searchMap);
		}
		// form表单设置
		form.setSumInfo(sumInfo);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
			sumInfo = binOLSSPRM27BL.getSumInfo(searchMap);
		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM43_1";
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap()throws Exception {
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
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 发货单号
		map.put("deliverRecNo", form.getDeliverRecNo());
		// 关联单号
		map.put("relevanceNo", form.getRelevanceNo());
		// 业务类型
		map.put("tradeType", CherryConstants.TRADE_TYPE_2);
		// 开始日
		map.put("startDate", CherryUtil.suffixDate(form.getStartDate(), 0));
		// 结束日
		map.put("endDate", CherryUtil.suffixDate(form.getEndDate(), 1));
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
//		// 打印状态
//		map.put("printStatus", form.getPrintStatus());
		// 入库区分
		map.put("stockInFlag", form.getStockInFlag());
		//促销产品厂商ID
		map.put("prmVendorId", form.getPrmVendorId());
		//部门类型
		map.put("departType", form.getDepartType());
		//发货部门Id
		map.put("outOrganizationId", form.getOutOrganizationId());
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
}

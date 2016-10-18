/*	
 * @(#)BINOLSSPRM30_Action.java     1.0 2010/11/29		
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
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM55_BL;
import com.cherry.ss.prm.form.BINOLSSPRM55_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 调拨记录查询修改
 * @author dingyc
 *
 */
public class BINOLSSPRM55_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM55_Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2409264522559128413L;

	@Resource
	private BINOLCM00_BL binolcm00BL;

	@Resource
	private BINOLSSPRM55_BL binolssprm55BL;

	/** 参数FORM */
	private BINOLSSPRM55_Form form = new BINOLSSPRM55_Form();

    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
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
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put("operationType", 1);
		// 开始日期
		form.setStartDate(binolcm00BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
						.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 调拨部门（办事处，柜台）List
		form.setOrgList(binolcm00BL.getDepartList(map));
		// 查询假日
		form.setHolidays(binolcm00BL.getHolidays(map));
		return SUCCESS;
	}

	/**
	 * <p>
	 * 调拨记录查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
    public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得调拨记录总数
		int count = binolssprm55BL.getAllocationCount(searchMap);
		if(count>0){
		    // 取得调拨记录List
	        form.setAllocationList(binolssprm55BL.getAllocationList(searchMap));
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
//      ================= LuoHong修改：显示统计信息 ================== //
//		String prmVendorId = ConvertUtil.getString(searchMap.get("prmVendorId"));
//		if (!CherryConstants.BLANK.equals(prmVendorId)) {
		    sumInfo = binolssprm55BL.getSumInfo(searchMap);
//		}
		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM55_1";
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		// 调拨单号
		map.put("allocationNo", form.getAllocationNo().trim());
		// 调拨业务类型
		//map.put("tradeType", form.getTradeType());
		// 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		// 调拨部门
		//map.put("organizationId", form.getOrganizationId());
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
		// 处理状态
		map.put("tradeStatus", form.getTradeStatus());
		//制单人
		map.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		//促销产品厂商ID
        map.put("prmVendorId", form.getPrmVendorId());
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
				this.addActionError(getText("ECM00008", new String[]{"开始日期"}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
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
	
	@Override
	public BINOLSSPRM55_Form getModel() {
		return form;
	}

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }
}

/*  
 * @(#)BINOLMOCIO03_Action.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.action;

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
import com.cherry.mo.cio.form.BINOLMOCIO03_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO03_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO03_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO03_Form> {

	/**
	 * 
	 */
	@Resource
	private BINOLMOCIO03_IF binOLMOCIO03_BL;

	private static final long serialVersionUID = 1L;
	// 申明form
	private BINOLMOCIO03_Form form = new BINOLMOCIO03_Form();

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	@Resource
	private BINOLCM00_BL binOLCM00_BL;

	private String holidays;

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public BINOLMOCIO03_Form getForm() {
		return form;
	}

	public void setForm(BINOLMOCIO03_Form form) {
		this.form = form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLMOCIO03_Form getModel() {
		return form;
	}

	/**
	 * 页面初始化
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 取得品牌List
		if (userInfo.getBIN_BrandInfoID() == -9999) {
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
		return SUCCESS;
	}

	/**
	 *保存问卷，在点击添加问题的时候
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String savePaper() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 当前用户的ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 作成者为当前用户
			map.put("createdBy", userInfo.getBIN_UserID());
			// 作成程序名为当前程序
			map.put("createPGM", "BINOLMOCIO03");
			// 更新者为当前用户
			map.put("updatedBy", userInfo.getBIN_UserID());
			// 更新程序名为当前程序
			map.put("updatePGM", "BINOLMOCIO03");
			// 所属品牌
			map.put("brandInfoId", form.getBrandInfoId());
			// 试卷名称
			map.put("paperName", form.getPaperName());
			// 试卷类型
			map.put("paperType", form.getPaperType());
			// 问卷总分
			if ((form.getMaxPoint() == null)
					|| (("").equals(form.getMaxPoint()))) {
				map.put("maxPoint", "0");
			} else {
				map.put("maxPoint", form.getMaxPoint());
			}
			// 开始日期
			map.put("startTime", form.getStartDate().trim());
			// 开始时间-时
			map.put("startHour", form.getStartHour().trim());
			// 开始时间-分
			map.put("startMinute", form.getStartMinute().trim());
			// 开始时间-秒
			map.put("startSecond", form.getStartSecond().trim());
			// 结束日期
			map.put("endTime", form.getEndDate().trim());
			//结束时间-时
			map.put("endHour", form.getEndHour().trim());
			//结束时间-分
			map.put("endMinute", form.getEndMinute().trim());
			//结束时间-秒
			map.put("endSecond", form.getEndSecond().trim());
			// 试卷状态
			map.put("paperStatus", form.getPaperStatus());
			String json = form.getQueStr();
			//String json1 = json.replace("*amp*", "&");
			List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil
					.deserialize(json);

			binOLMOCIO03_BL.tran_savePaper(map, list);
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
	}

	public void validateSavePaper() throws Exception {
		if (CherryChecker.isNullOrEmpty(form.getPaperName().trim())) {
			this.addFieldError("paperName", getText("EMO00039",
					new String[] { getText("PMO00013") }));
		}
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("paperName", form.getPaperName().trim());
		paramMap.put("brandInfoId", form.getBrandInfoId());
		
		if(binOLMOCIO03_BL.isExsitSameNamePaper(paramMap)){
			this.addFieldError("paperName", getText("EMO00058"));
		}
		
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate().trim();
		// 结束日期
		String endDate = form.getEndDate().trim();
		if (CherryChecker.isNullOrEmpty(startDate)) {
			this.addFieldError("startDate", getText("ECM00009",
					new String[] { getText("PCM00001") }));
		}
		if (CherryChecker.isNullOrEmpty(endDate)) {
			this.addFieldError("endDate", getText("ECM00009",
					new String[] { getText("PCM00002") }));
		}
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addFieldError("startDate",getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期验证 */
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addFieldError("endDate",getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)
				&& endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addFieldError("endDate",getText("ECM00019"));
				isCorrect = false;
			}
		}
	}
}

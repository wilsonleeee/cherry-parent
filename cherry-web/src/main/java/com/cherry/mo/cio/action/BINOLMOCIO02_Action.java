/*  
 * @(#)BINOLMOCIO02_Action.java     1.0 2011/05/31      
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.cherry.mo.cio.form.BINOLMOCIO02_Form;
import com.cherry.mo.cio.interfaces.BINOLMOCIO02_IF;
import com.cherry.mo.common.MonitorConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOCIO02_Action extends BaseAction implements
		ModelDriven<BINOLMOCIO02_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 查询结果中问卷数量
	private int count = 0;
	// 查询出的问卷List
	private List<Map<String, Object>> paperList;

	private BINOLMOCIO02_Form form = new BINOLMOCIO02_Form();
	// 注入BL
	@Resource
	private BINOLMOCIO02_IF binOLMOCIO02_BL;
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	// private int userBrandInfoId = 0;
	//    
	// private String userBrandName = null;

	// 假期
	private String holidays;

	@SuppressWarnings("unchecked")
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// userBrandInfoId = userInfo.getBIN_BrandInfoID();
		// userBrandName = userInfo.getBrandName();
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
			// System.out.print(brandInfoList.size());
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
		return SUCCESS;
	}

	public String search() throws Exception {
		
		Map<String, Object> searchMap = getSearchMap();
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String paperStatus = "";

		if(searchMap.get("FILTER_VALUE")!=null){
			 
			searchMap.put("paperStatus", 2);
			
			//问卷状态
			 paperStatus = searchMap.get("FILTER_VALUE").toString();
			 
			 if(paperStatus.equals("in_progress")||paperStatus.equals("past_due")||paperStatus.equals("not_start")){
				 
				 if(paperStatus.equals("in_progress")){
					 
					 //进行中
					 searchMap.put("dateStatus",1);
				 }else if(paperStatus.equals("past_due")){
					 
					//已过期
					 searchMap.put("dateStatus",2);					 
				 }else if(paperStatus.equals("not_start")){
					 
					//未开始
					 searchMap.put("dateStatus",3);						 
				 }
			 }
		}else{
			
			 searchMap.put("paperStatus", 2);
			 //默认设置显示进行中
			 searchMap.put("dateStatus",1);
		}
	
		paperList = binOLMOCIO02_BL.getPaperList(searchMap);
		

		//判断问卷是否过期
		for(int i = 0 ; i<paperList.size();i++){
			
			//问卷开始时间
			String startTime = (String) paperList.get(i).get("startTime");
			paperList.get(i).put("startTime", startTime.substring(0,10));
			Date startTimes = date.parse(startTime);
			//问卷结束时间
			String endTime = (String) paperList.get(i).get("endTime");
			paperList.get(i).put("endTime", endTime.substring(0,10));
			Date endTimes = date.parse(endTime);
			//当前系统时间
			String currentTime = (String) paperList.get(i).get("currentTime");
			paperList.get(i).put("currentTime", currentTime.substring(0,10));
			Date currentTimes = date.parse(currentTime);
			
			//比较当前时间是否过期
			if(currentTimes.getTime() <= endTimes.getTime()){
				//未过期（进行中或者未开始）
				paperList.get(i).put("pastState", "0");
			}else{
				//过期
				paperList.get(i).put("pastState", "1");
			}
		}
		count = binOLMOCIO02_BL.getPaperCount(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLMOCIO02_1";
	}

	// 获取查询条件
	public Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		ConvertUtil.setForm(form, map);
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
		// 品牌
		map.put("brandInfoId", form.getBrandInfoId());
		// 问卷类型
		map.put("paperType", form.getPaperType());
		// 问卷状态
		map.put("paperStatus", form.getPaperStatus());
		// 问卷名称
		map.put("paperName", form.getPaperName());
		//开始时间
		map.put("startDate", form.getStartDate().trim());
		//结束时间
		map.put("endDate", form.getEndDate().trim());
		return map;
	}

	/**
	 * 问卷启用、停用
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String paperDisableOrEnable() throws Exception {
		try {
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("updatedBy", userInfo.getBIN_UserID());
			map.put("updatePGM", "BINOLMOCIO02");
			String json = form.getCheckedPaperIdArr();
			List<Map<String, Object>> idList = (List<Map<String, Object>>) JSONUtil
					.deserialize(json);
			if(null != idList && idList.size() > 0){
				map.putAll(idList.get(0));
			}
			binOLMOCIO02_BL.tran_paperDisableOrEnable(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 问卷删除
	 * 
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public String paperDelete() throws Exception {
		try {
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("updatedBy", userInfo.getBIN_UserID());
			map.put("updatePGM", "BINOLMOCIO02");
			String json = form.getCheckedPaperIdArr();
			List<Map<String, Object>> idList = (List<Map<String, Object>>) JSONUtil
					.deserialize(json);
			//idList只有一条paperId数据（删除键在操作框中）
			if(null != idList && idList.size() > 0){
				map.put("paperId", idList.get(0).get("paperId"));
			}
			binOLMOCIO02_BL.tran_deletePaper(map);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			throw e;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 会员问卷下发之前判断系统中是否已经有下发过的其他会员问卷
	 * 
	 * */
	public void isExistSomePaper() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		//问卷类型为会员问卷
		map.put("paperType",MonitorConstants.PAPER_TYPE_MEMBER);
		//问卷状态为可使用
		map.put("paperStatus", MonitorConstants.PAPER_STATUS_ENABLE);
		List<Map<String,Object>> list = binOLMOCIO02_BL.isExistSomePaper(map);
		ConvertUtil.setResponseByAjax(response, list);
	}
	
	/**
	 * 取得问卷下发信息
	 * 
	 * 
	 * */
	public String getPaperIssum(){
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			//语言
			map.put("language", userInfo.getLanguage());
			//问卷ID
			map.put("BIN_PaperID", form.getPaperId());
			
			List<Map<String,Object>> list = binOLMOCIO02_BL.getPaperIssum(map);
			
			ConvertUtil.setResponseByAjax(response, list);
			return null;
		}catch(Exception e){
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
	}
	
	
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate().trim();
		// 结束日期
		String endDate = form.getEndDate().trim();
		/* 开始日期验证 */
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期验证 */
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)
				&& endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	@Override
	public BINOLMOCIO02_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Map<String, Object>> getPaperList() {
		return paperList;
	}

	public void setPaperList(List<Map<String, Object>> paperList) {
		this.paperList = paperList;
	}

	public BINOLMOCIO02_Form getForm() {
		return form;
	}

	public void setForm(BINOLMOCIO02_Form form) {
		this.form = form;
	}

}

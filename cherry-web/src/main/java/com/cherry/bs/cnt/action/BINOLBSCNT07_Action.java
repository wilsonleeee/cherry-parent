/*	
 * @(#)BINOLBSCNT07_Action.java     1.0 2016/11/24
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
package com.cherry.bs.cnt.action;

import com.cherry.bs.cnt.bl.BINOLBSCNT07_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT07_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 	积分计划柜台处理Action
 * 
 * @author Wangminze
 * @version 1.0 2016.11.23
 */
public class BINOLBSCNT07_Action extends BaseAction implements ModelDriven<BINOLBSCNT07_Form>{


	//打印异常日志
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSCNT07_Action.class);

	/** 积分计划柜台查询画面Form */
	private BINOLBSCNT07_Form form = new BINOLBSCNT07_Form();

	/** 积分计划柜台查询画面BL */
	@Resource
	private BINOLBSCNT07_BL binOLBSCNT07_BL;

	/** 共通BL */
	@Resource
	private BINOLCM00_BL binolcm00BL;

	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	/** 共通 */
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;

	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/** 积分计划柜台List */
	private List<Map<String, Object>> counterPointPlanList;

	private Map<String,Object> counterPointPlanInfo;

	public String enableInit() throws Exception {
		return SUCCESS;
	}
	public String disableInit() throws Exception {
		return SUCCESS;
	}
	public String pointChangeInit() throws Exception {
		return SUCCESS;
	}
	/**
	 *
	 * 画面初期显示
	 *
	 * @return String 积分计划柜台查询画面
	 *
	 */
	@SuppressWarnings("unchecked")
	public String init() throws Exception {

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
		// 总部用户的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}

		return SUCCESS;
	}

	/**
	 * 启用柜台积分计划
	 */
	public String enablePointPlan() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = commonParam();
			binOLBSCNT07_BL.tran_enablePointPlan(paramMap);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(e.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 停用柜台积分计划
	 */
	public String disablePointPlan() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = commonParam();
			binOLBSCNT07_BL.tran_disablePointPlan(paramMap);
			this.addActionMessage(getText("ICM00002"));
		} catch (Exception e) {
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(e.getMessage());
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	/**
	 * 柜台积分额度变更
	 */
	public void pointChange() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = commonParam();
			binOLBSCNT07_BL.tran_pointChange(paramMap);
			resultMap.put("errorCode", "0");
		} catch (Exception e) {
			resultMap.put("errorCode", "1");
		}
		ConvertUtil.setResponseByAjax(response, resultMap);
	}

	/**
	 * <p>
	 * 取得柜台积分计划List
	 * </p>
	 *
	 * @return
	 */
	public String search() throws Exception {
		try {
			// 取得参数MAP
			Map<String, Object> searchMap = getSearchMap();
			// 取得柜台积分计划总数
			int count = binOLBSCNT07_BL.getCounterPointPlanCount(searchMap);
			if(count != 0) {
				// 取得柜台积分计划List
				counterPointPlanList = binOLBSCNT07_BL.getCounterPointPlanList(searchMap);
			}

			String currentDate = binOLBSCNT07_BL.getSYSDate();
			form.setCurrentDate(currentDate);
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// 系统发生异常，请联系管理员
			this.addActionError(getText("ECM00036"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	/**
	 *
	 * 查询参数MAP取得
	 *
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		} else {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		map.put("counterInfoId",form.getCounterInfoId());
		map.put("counterCode",form.getCounterCode());
		map.put("counterName",form.getCounterName());
		map.put("pointLimitBegin",form.getPointLimitBegin());
		map.put("pointLimitEnd",form.getPointLimitEnd());
		map.put("pointDateBegin",form.getPointDateBegin());
		map.put("pointDateEnd",form.getPointDateEnd());
		map.put("pointPlanStatus",form.getPointPlanStatus());
		map.put("counterStatus",form.getCounterStatus());
		return map;
	}

	private Map<String, Object> commonParam() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_EmployeeID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_EmployeeID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		map.put("employeeId",userInfo.getBIN_EmployeeID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSCNT07");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSCNT07");
		return map;
	}
	public String importLimitPlanCounter(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 总部用户的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		return SUCCESS;
	}

	/**
	 * 积分计划柜台履历详情初始化
	 */
	public String counterPointPlanDetailInit() throws Exception {

		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();

		// 取得柜台积分计划List
		counterPointPlanList = binOLBSCNT07_BL.getCounterPointPlanAllList(searchMap);

		if(counterPointPlanList.size() > 0){

			counterPointPlanInfo = (Map<String, Object>)ConvertUtil.byteClone(counterPointPlanList.get(0));
			form.setCurrentDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}

		counterPointPlanList.clear();
		counterPointPlanList = null;

		return SUCCESS;
	}


	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;



	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	/**
	 * 导出Excel
	 * @throws JSONException
	 */
	public String export() throws JSONException{
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得考勤信息List
		try {
			String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
			downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLBSCNT07", language, "downloadFileName");
			setExcelStream(new ByteArrayInputStream(binOLBSCNT07_BL.exportExcel(searchMap)));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			e.printStackTrace();
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}

		return "BINOLBSCNT07_excel";
	}
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	@Override
	public BINOLBSCNT07_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getCounterPointPlanList() {
		return counterPointPlanList;
	}

	public void setCounterPointPlanList(List<Map<String, Object>> counterPointPlanList) {
		this.counterPointPlanList = counterPointPlanList;
	}

	public Map<String, Object> getCounterPointPlanInfo() {
		return counterPointPlanInfo;
	}

	public void setCounterPointPlanInfo(Map<String, Object> counterPointPlanInfo) {
		this.counterPointPlanInfo = counterPointPlanInfo;
	}
}

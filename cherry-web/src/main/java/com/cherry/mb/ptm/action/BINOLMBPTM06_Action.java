/*
 * @(#)BINOLMBPTM06_Action.java     1.0 2012/08/08
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
package com.cherry.mb.ptm.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.ptm.bl.BINOLMBPTM06_BL;
import com.cherry.mb.ptm.form.BINOLMBPTM06_Form;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.common.ProductConstants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 查询积分信息Action
 * 
 * @author WangCT
 * @version 1.0 2012/08/08
 */
public class BINOLMBPTM06_Action extends BaseAction implements ModelDriven<BINOLMBPTM06_Form> {
	
	private static final long serialVersionUID = -1696486411084575198L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBPTM06_Action.class);
	/** 查询积分信息BL */
	@Resource
	private BINOLMBPTM06_BL binOLMBPTM06_BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	private String clubMod;
	/** 积分信息List */
	private Map<String, Object> pointInfoSummy;
	/** 积分信息List */
	private List<Map<String, Object>> pointInfoList;
	/** 查询积分信息Form */
	private BINOLMBPTM06_Form form = new BINOLMBPTM06_Form();

	public String getClubMod() {
		return clubMod;
	}
	
	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}
	
	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}
	
	/**
	 * 查询积分信息画面初期处理
	 *
	 * @return 查询积分信息画面
	 */
	public String init() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		String sysDateTime = CherryUtil.getSysDateTime("yyyy-MM-dd");
		// 扫描开始日期初期值
		form.setStartDate(DateUtil.suffixDate(CherryUtil.getMinMonthDate(sysDateTime), 0).substring(0, 10));
		// 扫描开始日期初期值
		form.setEndDate(DateUtil.suffixDate(sysDateTime, 1).substring(0, 10));
		return SUCCESS;
	}
	
	/**
	 * AJAX查询积分信息
	 *
	 * @return 查询积分信息画面
	 */
	@SuppressWarnings("unchecked")
	public String searchReportSummy() throws Exception {

		Map<String, Object> map = getSearchMap();
		// 取得积分统计信息
		pointInfoSummy = binOLMBPTM06_BL.getScanQRPointsReportSummary(map);
		return SUCCESS;
	}
	
	/**
	 * AJAX查询积分信息
	 *
	 * @return 查询积分信息画面
	 */
	@SuppressWarnings("unchecked")
	public String searchReportDetailList() throws Exception {

		Map<String, Object> map = getSearchMap();
		// 取得积分信息总数
		int count = binOLMBPTM06_BL.getDetailCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得积分信息List
			pointInfoList = binOLMBPTM06_BL.getDetailList(map);
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * 查询前字段验证处理
	 *
	 */
	@SuppressWarnings("unchecked")
	public void validateSearchReportSummy() throws Exception {
		// 扫描开始日期必须入力验证
		if (CherryChecker.isNullOrEmpty(form.getStartDate())) {
			this.addFieldError("startDate",
					getText("ECM00009", new String[] { getText("PMB00090") }));
		}
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();

		int count = binOLMBPTM06_BL.getDetailCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}

	/**
	 * 一览excel导出
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 设置排序ID（必须）
		map.put("SORT_ID", "grantPointTime desc");
		try {
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			String extName = binOLCM37_BL.getResourceValue("BINOLMBPTM06", language, "BINOLMBPTM06_exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLMBPTM06_BL.exportExcel(map), extName+".xls"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLMBPTM06_excel";
	}

	/**
	 * 查询参数MAP取得
	 *
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);

		// 当前语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		if(!CherryChecker.isNullOrEmpty(form.getStartDate())) {
			map.put("startDate", DateUtil.suffixDate(form.getStartDate(), 0));
			// 用于导出时显示查询条件
			map.put("startDateShow", form.getStartDate());
		}

		if(!CherryChecker.isNullOrEmpty(form.getEndDate())) {
			map.put("endDate", DateUtil.suffixDate(form.getEndDate(), 1));
			// 用于导出时显示查询条件
			map.put("endDateShow", form.getEndDate());
		} else {
			map.put("endDate", DateUtil.suffixDate(CherryUtil.getSysDateTime("yyyy-MM-dd"), 1));
			// 用于导出时显示查询条件
			map.put("endDateShow", form.getEndDate());
		}

		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
//		map = CherryUtil.removeEmptyVal(map);

		return map;
	}

	public Map<String, Object> getPointInfoSummy() {
		return pointInfoSummy;
	}
	
	public void setPointInfoSummy(Map<String, Object> pointInfoSummy) {
		this.pointInfoSummy = pointInfoSummy;
	}

	public List<Map<String, Object>> getPointInfoList() {
		return pointInfoList;
	}

	public void setPointInfoList(List<Map<String, Object>> pointInfoList) {
		this.pointInfoList = pointInfoList;
	}

	@Override
	public BINOLMBPTM06_Form getModel() {
		return form;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

}

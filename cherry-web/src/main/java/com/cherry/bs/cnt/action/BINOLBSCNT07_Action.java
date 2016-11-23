/*	
 * @(#)BINOLBSCNT06_Action.java     1.0 2011/05/09		
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
		map.put("counterCode",form.getCounterCode());
		map.put("counterName",form.getCounterName());
		map.put("pointLimitBegin",form.getPointLimitBegin());
		map.put("pointLimitEnd",form.getPointLimitEnd());
		map.put("pointPlanStatus",form.getPointPlanStatus());
		map.put("counterStatus",form.getCounterStatus());
		return map;
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
			downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLBSCNT01", language, "downloadFileName");
			//setExcelStream(new ByteArrayInputStream(binOLBSCNT01_BL.exportExcel(searchMap)));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			e.printStackTrace();
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}

		return "BINOLBSCNT01_excel";
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
}

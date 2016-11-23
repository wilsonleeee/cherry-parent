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

import com.cherry.bs.cnt.bl.BINOLBSCNT01_BL;
import com.cherry.bs.cnt.bl.BINOLBSCNT06_BL;
import com.cherry.bs.cnt.form.BINOLBSCNT01_Form;
import com.cherry.bs.cnt.form.BINOLBSCNT06_Form;
import com.cherry.bs.cnt.form.BINOLBSCNT07_Form;
import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
	private static final long serialVersionUID = 944446482429999813L;

	/** 积分计划柜台查询画面Form */
	private BINOLBSCNT07_Form form = new BINOLBSCNT07_Form();

	/** 积分计划柜台查询画面BL */
	@Resource
	private BINOLBSCNT01_BL binOLBSCNT01_BL;

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
			// 取得区域List
			reginList = binolcm00BL.getReginList(map);
			// 取得渠道List
			channelList = binolcm00BL.getChannelList(map);
		}

		//是否支持柜台协同
		form.setMaintainCoutSynergy(binOLCM14_BL.isConfigOpen("1050", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}

	/**
	 * 根据品牌ID筛选下拉列表
	 *
	 */
	public String filterByBrandInfo() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 取得区域List
		resultMap.put("reginList", binolcm00BL.getReginList(map));
		// 取得渠道List
		resultMap.put("channelList", binolcm00BL.getChannelList(map));

		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}

	/**
	 *
	 * AJAX积分计划柜台查询处理
	 *
	 * @return AJAX返回至dataTable结果页面
	 */
	public String search() throws Exception {

		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 柜台总数取得
		int count = binOLBSCNT01_BL.getCounterCount(map);
		if(count != 0) {
			// 取得柜台信息List
			counterList = binOLBSCNT01_BL.getCounterList(map);
		}
		// form表单设置
		//是否支持柜台协同
		form.setMaintainCoutSynergy(binOLCM14_BL
				.isConfigOpen("1050", ConvertUtil.getInt(map
						.get(CherryConstants.ORGANIZATIONINFOID)), ConvertUtil
						.getInt(map.get(CherryConstants.BRANDINFOID))));
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}

	/**
	 * 柜台主管模糊查询
	 * @throws Exception
	 */
	public void getCounterBAS() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
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
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		//显示行数
		map.put("number", form.getNumber());
		//柜台主管模糊查询
		map.put("counterBAS", form.getCounterBAS().trim());
		String resultStr = binOLBSCNT01_BL.getCounterBAS(map);
		ConvertUtil.setResponseByAjax(response, resultStr);
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
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 柜台号
		map.put("counterCode", form.getCounterCode());
		// 柜台中文名称
		map.put("counterNameIF", form.getCounterNameIF());
		// 渠道ID
		map.put("channelId", form.getChannelId());
		// 柜台状态
		map.put("status", form.getStatus());
		// 柜台地址
		map.put("counterAddress", form.getCounterAddress());
		// 所属省份
		map.put("provinceId", form.getProvinceId());
		// 所属城市
		map.put("cityId", form.getCityId());
		// 有效区分
		map.put("validFlag", form.getValidFlag());
		//柜台主管
		map.put("counterBAS", form.getCounterBAS().trim());

		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		map.put("busniessPrincipal", form.getBusniessPrincipal());
		return map;
	}

	/** 柜台信息List */
	private List<Map<String, Object>> counterList;

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/** 区域List */
	private List<Map<String, Object>> reginList;

	/** 渠道List */
	private List<Map<String, Object>> channelList;

	public List<Map<String, Object>> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<Map<String, Object>> counterList) {
		this.counterList = counterList;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
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
			setExcelStream(new ByteArrayInputStream(binOLBSCNT01_BL.exportExcel(searchMap)));
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
}

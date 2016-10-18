/*
 * @(#)BINOLPTRPS01_Action.java     1.0 2012/12/26
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.ss.common.bl.BINOLSSCM02_BL;
import com.cherry.ss.prm.form.BINOLSSPRM45_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM45_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点查询Action
 * 
 * 
 * 
 * @author liuminghao
 * @version 1.0 2012.12.26
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM45_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM45_Form> {

	/** 参数FORM */
	private BINOLSSPRM45_Form form = new BINOLSSPRM45_Form();

	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Resource(name = "binOLSSPRM45_BL")
	private BINOLSSPRM45_IF binOLSSPRM45_BL;

	@Resource(name="binOLSSCM02_BL")
	private BINOLSSCM02_BL binolsscm02BL;
	
	/** 节日 */
	private String holidays;

	/** 盘点单List */
	private List takingList;

	/** 汇总信息 */
	private Map<String, Object> sumInfo;

	/** 下载文件名 */
	private String exportName;

	/** Excel输入流 */
	private InputStream excelStream;

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
	    String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
	    form.setOrganizationId(organizationInfoID);
	    String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,organizationInfoID);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(
				userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
	    //从配置项取默认合并方式
        String configValue = binOLCM14_BL.getConfigValue("1126", organizationInfoID, brandInfoID);
        form.setCodeMergeType(configValue);
    	//取得大分类列表
    	form.setLargeCategoryList(binolsscm02BL.getPrimaryCategory(userInfo));
		return SUCCESS;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @param
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
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 开始日期
		map.put("startDate", form.getStartDate());
		// 结束日期
		map.put("endDate", form.getEndDate());
		// 组织ID
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		String selPrmVendorIdArr = form.getSelPrmVendorIdArr();
		if(!CherryChecker.isNullOrEmpty(selPrmVendorIdArr) && !"null".equalsIgnoreCase(selPrmVendorIdArr)) {
			List prmVendorIdList = ConvertUtil.json2List(selPrmVendorIdArr);
			if(null != prmVendorIdList && prmVendorIdList.size() > 0) {
				if(!CherryChecker.isNullOrEmpty(form.getCodeMergeType()) && "Custom".equals(form.getCodeMergeType())) {//如果是自定义合并方式，则查询促销品关联表
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
					tempMap.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
					tempMap.put("prmVendorIdList", prmVendorIdList);
					tempMap.put("BIN_MerchandiseType", "P");
					prmVendorIdList = binOLSSPRM45_BL.getConjunctionPrmList(tempMap);
				}
				map.put("prmVendorId", prmVendorIdList);
			}
		}
	    //是否排除促销品标志
        map.put("includeFlag", form.getIncludeFlag());
        //合并方式
        map.put("codeMergeType", form.getCodeMergeType());
		if (form.getParams() != null) {
			Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil
					.deserialize(form.getParams());
			map.putAll(paramsMap);
			map = CherryUtil.removeEmptyVal(map);
		}
		//大中小分类
		map.put("PrimaryCategoryCode", form.getLargeCategory());
		map.put("SecondryCategoryCode", form.getMiddleCategory());
		map.put("SmallCategoryCode", form.getSmallCategory());
		map = CherryUtil.removeEmptyVal(map);
		return map;
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
		int count = binOLSSPRM45_BL.searchTakingCount(searchMap);

		if (count > 0) {
			// 取得盘点单List
			takingList = binOLSSPRM45_BL.searchTakingList(searchMap);

		}

		sumInfo = binOLSSPRM45_BL.getSumInfo(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM45_1";
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
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

	/**
	 * 导出Excel
	 * 
	 * @throws JSONException
	 */
	public String export() throws JSONException {
		Map<String, Object> map = getSearchMap();
		// 取得库存信息List
		try {
			exportName = binOLSSPRM45_BL.getExportName(map);
			excelStream = new ByteArrayInputStream(
					binOLSSPRM45_BL.exportExcel(map));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
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

	public List getTakingList() {
		return takingList;
	}

	public void setTakingList(List takingList) {
		this.takingList = takingList;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	@Override
	public BINOLSSPRM45_Form getModel() {
		return form;
	}



}

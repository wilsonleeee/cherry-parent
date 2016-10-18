/*	
 * @(#)BINOLSSPRM31_Action.java     1.0 2010/11/04		
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
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.prm.bl.BINOLSSPRM31_BL;
import com.cherry.ss.prm.form.BINOLSSPRM31_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 库存查询Action
 * 
 * @author lipc
 * @version 1.0 2010.11.04
 */
public class BINOLSSPRM31_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM31_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5113325845359666482L;

	@Resource
	private BINOLCM00_BL binolcm00BL;

	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource
	private BINOLSSPRM31_BL binolssprm31BL;

	/** 参数FORM */
	private BINOLSSPRM31_Form form = new BINOLSSPRM31_Form();

	/** 库存记录List */
	private List<Map<String, Object>> proStockList;

	/** 假日信息 */
	private String holidays;

	/** 汇总信息 */
	private Map<String, Object> sumInfo;

	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	@Override
	public BINOLSSPRM31_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getProStockList() {
		return proStockList;
	}

	public void setProStockList(List<Map<String, Object>> proStockList) {
		this.proStockList = proStockList;
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
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 开始日期
		form.setStartDate(binolcm00BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form
				.setEndDate(CherryUtil
						.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 库存记录查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return
	 * 
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得库存记录总数
		// int count = binolssprm31BL.getProStockCount(searchMap);
		// 取得汇总信息
		sumInfo = binolssprm31BL.getSumInfo(searchMap);
		// 取得库存记录List
		proStockList = binolssprm31BL.getProStockList(searchMap);
		// 取得库存记录总数
		int count = ConvertUtil.getInt(sumInfo.get("count"));
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);

		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM31_1";
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得库存信息List
		try {
			exportName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM32",
					language, "PRM32_exportName")
					+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
			excelStream = new ByteArrayInputStream(binolssprm31BL
					.exportExcel(map));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 库存相关的日期参数设置到paramMap中
		binolcm00BL.setParamsMap(map, orgInfoId, form.getStartDate(), form
				.getEndDate(), "Prm");
		// form中dataTable相关参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		  // 用户组织
        map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 促销产品厂商ID
		map.put("prmVendorId", form.getPrmVendorId());
		// 促销产品名称
		map.put(CherryConstants.NAMETOTAL,form.getNameTotal());
		// 促销产品状态
		map.put(CherryConstants.VALID_FLAG,form.getValidFlag());
		// 共通条参数
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil
				.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		return map;
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
		if (CherryChecker.isNullOrEmpty(startDate, true)) {
			// 开始日期不能为空
			this.addActionError(getText("ECM00009",
					new String[] { getText("PCM00001") }));
			isCorrect = false;
		}
		if (CherryChecker.isNullOrEmpty(endDate, true)) {
			// 结束日期不能为空
			this.addActionError(getText("ECM00009",
					new String[] { getText("PCM00002") }));
			isCorrect = false;
		}

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
}

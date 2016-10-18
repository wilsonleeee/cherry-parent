/*
 * @(#)BINOLPTRPS28_Action.java     1.0 2013/08/12
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
package com.cherry.pt.rps.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.rps.bl.BINOLPTRPS28_BL;
import com.cherry.pt.rps.form.BINOLPTRPS28_Form;
import com.cherry.pt.rps.service.BINOLPTRPS28_Service;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 销售完成进度报表Action
 * 
 * @author WangCT
 * @version 1.0 2013/08/12
 */
public class BINOLPTRPS28_Action extends BaseAction implements ModelDriven<BINOLPTRPS28_Form> {
	
	private static final long serialVersionUID = 6299946302329534872L;
	
	/** 销售完成进度报表Service **/
	@Resource
	private BINOLPTRPS28_Service binOLPTRPS28_Service;
	
	/** 销售完成进度报表BL **/
	@Resource
	private BINOLPTRPS28_BL binOLPTRPS28_BL;
	
	/** 导出excel共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/**
	 * 销售完成进度报表画面初期显示
	 * 
	 * @return String 销售完成进度报表画面
	 * @throws Exception
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得业务日期
		String bussinessDate = binOLPTRPS28_Service.getBussinessDate(map);
		map.put("dateValue", bussinessDate);
		// 查询指定日期所在的财务年月
		Map<String, Object> fiscalMonthMap = binOLPTRPS28_Service.getFiscalMonth(map);
		int lastYear = Integer.parseInt(bussinessDate.substring(0, 4));
		if(fiscalMonthMap != null && !fiscalMonthMap.isEmpty()) {
			form.setFiscalYear(String.valueOf(fiscalMonthMap.get("fiscalYear")));
			form.setFiscalMonth(String.valueOf(fiscalMonthMap.get("fiscalMonth")));
			lastYear = (Integer)fiscalMonthMap.get("fiscalYear");
		}
		yearList = new ArrayList<String>();
		int yearLength = 10;
		for(int i = lastYear; i > lastYear - yearLength; i--) {
			yearList.add(String.valueOf(i));
		}
		monthList = new ArrayList<String>();
		for(int i = 1; i <= 12; i++) {
			monthList.add(String.valueOf(i));
		}
		return SUCCESS;
	}
	
	/**
	 * 销售完成进度报表查询
	 * 
	 * @return String 销售完成进度报表
	 * @throws Exception
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		
		String fiscalYearMonth = form.getFiscalYear();
		if(form.getFiscalMonth().length() == 1) {
			fiscalYearMonth = fiscalYearMonth + "0" + form.getFiscalMonth();
		} else {
			fiscalYearMonth = fiscalYearMonth + form.getFiscalMonth();
		}
		map.put("fiscalYearMonth", fiscalYearMonth);
		// 查询指定财务月的最小最大自然日
		Map<String, Object> minMaxDateValue = binOLPTRPS28_Service.getMinMaxDateValue(map);
		map.putAll(minMaxDateValue);
		
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
		// 查询销售完成进度报表信息总件数
		int count = binOLPTRPS28_BL.getSaleTargetRptCount(map);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count > 0) {
			// 查询销售完成进度报表信息List
			saleTargetRptList = binOLPTRPS28_BL.getSaleTargetRptList(map);
		}
		
		return SUCCESS;
	}
	
	/**
     * 导出Excel
     */
    public String export() throws Exception {
    	
    	try {
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		}
    		// 用户ID
    		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
    		
    		String fiscalYearMonth = form.getFiscalYear();
    		if(form.getFiscalMonth().length() == 1) {
    			fiscalYearMonth = fiscalYearMonth + "0" + form.getFiscalMonth();
    		} else {
    			fiscalYearMonth = fiscalYearMonth + form.getFiscalMonth();
    		}
    		map.put("fiscalYearMonth", fiscalYearMonth);
    		// 查询指定财务月的最小最大自然日
    		Map<String, Object> minMaxDateValue = binOLPTRPS28_Service.getMinMaxDateValue(map);
    		map.putAll(minMaxDateValue);
    		
    		// form参数设置到paramMap中
    		ConvertUtil.setForm(form, map);
    		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
    		map.putAll(paramsMap);
    		map = CherryUtil.removeEmptyVal(map);
    		
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		
    		String countModel = (String)map.get("countModel");
    		if(countModel == null || "".equals(countModel)) {
    			countModel = "0";
    		}
    		
    		String zipName = binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "downloadFileName");
            downloadFileName = zipName +".zip";
            map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "sheetName"));
            if("0".equals(countModel)) {
            	String[][] titleRows = {
                		{"departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_departCode"), "20", "", ""},
                		{"departName", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_departName"), "30", "", ""},
                		{"busniessPrincipal", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_busniessPrincipal"), "20", "", ""},
                		{"provinceName", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_provinceName"), "15", "", ""},
                		{"cityName", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_cityName"), "15", "", ""},
                		{"channelName", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_channelName"), "15", "", ""},
                		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_amount"), "15", "float", ""},
                		{"targetMoney", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_targetMoney"), "15", "float", ""},
                		{"percent", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_percent"), "15", "", ""},
                		{"datePercent", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_datePercent"), "15", "", ""},
                		{"lastMoney", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_lastMoney"), "15", "float", ""},
                		{"predict", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_predict"), "15", "float", ""}};
        		map.put("titleRows", titleRows);
            } else if("1".equals(countModel)) {
            	String[][] titleRows = {
                		{"departName", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_regionName"), "15", "", ""},
                		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_amount"), "15", "float", ""},
                		{"targetMoney", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_targetMoney"), "15", "float", ""},
                		{"percent", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_percent"), "15", "", ""},
                		{"datePercent", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_datePercent"), "15", "", ""},
                		{"lastMoney", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_lastMoney"), "15", "float", ""},
                		{"predict", binOLCM37_BL.getResourceValue("BINOLPTRPS28", language, "binolptrps28_predict"), "15", "float", ""}};
        		map.put("titleRows", titleRows);
            }
     		map.put(CherryConstants.SORT_ID, "amount desc");
            byte[] byteArray = binOLCM37_BL.exportExcel(map, binOLPTRPS28_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
            this.addActionError(getText(""));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	/** 销售完成进度报表信息List **/
	private List<Map<String, Object>> saleTargetRptList;
	
	/** 画面条件年List **/
	private List<String> yearList;
	
	/** 画面条件月List **/
	private List<String> monthList;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getSaleTargetRptList() {
		return saleTargetRptList;
	}

	public void setSaleTargetRptList(List<Map<String, Object>> saleTargetRptList) {
		this.saleTargetRptList = saleTargetRptList;
	}

	public List<String> getYearList() {
		return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 销售完成进度报表Form **/
	private BINOLPTRPS28_Form form = new BINOLPTRPS28_Form();

	@Override
	public BINOLPTRPS28_Form getModel() {
		return form;
	}

}

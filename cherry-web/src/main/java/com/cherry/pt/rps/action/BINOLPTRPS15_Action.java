/*	
 * @(#)BINOLPTRPS15_Action.java     1.0 2012/10/31		
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS03_IF;
import com.cherry.pt.rps.bl.BINOLPTRPS15_BL;
import com.cherry.pt.rps.form.BINOLPTRPS15_Form;
import com.cherry.pt.rps.service.BINOLPTRPS15_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 统计销售信息Action
 * 
 * @author WangCT
 * @version 1.0 2012/10/31
 */
public class BINOLPTRPS15_Action extends BaseAction implements ModelDriven<BINOLPTRPS15_Form> {

	private static final long serialVersionUID = -4719162381035577289L;
	
	/** 统计销售信息IF **/
	@Resource
	private BINOLPTRPS15_BL binOLPTRPS15_BL;
	
	
	@Resource(name="binOLPTJCS03_IF")
	private BINOLPTJCS03_IF binOLPTJCS03_IF;
	
	/** 产品分类List */
	private List<Map<String, Object>> cateList;
	
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}

	/** 统计销售信息service **/
	@Resource
	private BINOLPTRPS15_Service binOLPTRPS15_Service;
	
	/** 导出excel共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	public String init() throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		String date = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
		// 开始日期
		form.setSaleTimeStart(date);
		// 截止日期
		form.setSaleTimeEnd(date);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		  }
		cateList = binOLPTJCS03_IF.getCategoryList(map);
		// 取得渠道List
		channelList=binOLCM00_BL.getChannelList(map);
		return SUCCESS;
	}
	/**
	 * 查询分类信息
	 * @param map
	 * @throws JSONException 
	 */
	private void getCateInfo(Map<String,Object> map) throws JSONException{
		//取得分类信息
		String cateInfo=form.getCateInfo();
		//大分类
		StringBuffer bigCateInfoBuffer=new StringBuffer();
		//中分类
		StringBuffer meduimCateInfoBuffer=new StringBuffer();
		//小分类
		StringBuffer samllCateInfoBuffer=new StringBuffer();
		//临时分类信息list
		List<String> tempList=new ArrayList<String>();
		// 产品分类信息List
		if (!ConvertUtil.isBlank(cateInfo)) {
			List<Map<String, Object>> cateInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(cateInfo);
			if (!CherryUtil.isBlankList(cateInfoList)) {
				for (Map<String, Object> cateInfoMap : cateInfoList) {
					String teminalFlag = ConvertUtil.getString(cateInfoMap.get("teminalFlag"));
					List<String> cateList = (List<String>) cateInfoMap.get("propValArr");
					tempList.addAll(cateList);
					if (!CherryUtil.isBlankList(cateList)) {
						if ("1".equals(teminalFlag)) {
							for (int i = 0; i < cateList.size(); i++) {
								bigCateInfoBuffer.append(cateList.get(i) + ",");
							}
							map.put("bigCateInfo", bigCateInfoBuffer.substring(0,bigCateInfoBuffer.length() - 1));
						} else if ("3".equals(teminalFlag)) {
							for (int j = 0; j < cateList.size(); j++) {
								meduimCateInfoBuffer.append(cateList.get(j)+ ",");
							}
							map.put("mediumCateInfo",meduimCateInfoBuffer.substring(0,meduimCateInfoBuffer.length() - 1));
						} else if ("2".equals(teminalFlag)) {
							for (int k = 0; k < cateList.size(); k++) {
								samllCateInfoBuffer.append(cateList.get(k)+ ",");
							}
							map.put("samllCateInfo", samllCateInfoBuffer.substring(0,samllCateInfoBuffer.length() - 1));
						}
					}
				}
			}
		}
		//大中小分类如没有选择，则参数默认为空
		if (CherryUtil.isBlankList(tempList)) {
			map.put("bigCateInfo", "");
			map.put("mediumCateInfo", "");
			map.put("samllCateInfo", "");
			//List使用完毕，重置为NUll
			tempList=null;
		}
	}
	
	/**
	 * 查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		//产品ID
		String selPrtVendorIdStr = form.getSelPrtVendorIdArr();
		map.remove("selPrtVendorIdArr");
		if(!"null".equalsIgnoreCase(selPrtVendorIdStr) && null != selPrtVendorIdStr && !"".equals(selPrtVendorIdStr)) {
			List selPrtVendorIdList = ConvertUtil.json2List(selPrtVendorIdStr);
			map.put("selPrtVendorIdArr", selPrtVendorIdList);
		}
		String fiscalFlagStr = getFiscalFlagINT(map);
		//得到分类信息
		getCateInfo(map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoID);
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, brandInfoID);
		}
		
		// 系统配置项1349：销售统计是否只统计柜台
		String isOnlyCounterFlag = binOLCM14_BL.getConfigValue("1349", organizationInfoID, brandInfoID);
		map.put("isOnlyCounterFlag", isOnlyCounterFlag);
		
		//统计标记，0：不统计，1：月度统计，2：年度统计
		map.put("saleYearMonthDateFlag", "0");
		// 销售统计开始时间和结束时间所属财务月相同的场合，需要显示月度统计
		if (!CherryChecker.isNullOrEmpty(fiscalFlagStr)) {
			if("1".equals(fiscalFlagStr)){//月度统计
				map.put("dateValue", form.getSaleTimeStart());
				// 查询指定日期所在的财务年月
				Map<String, Object> fiscalMonthMap = binOLPTRPS15_Service.getFiscalMonth(map);
				if(fiscalMonthMap != null) {
					int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
					int fiscalMonth = (Integer)fiscalMonthMap.get("fiscalMonth");
					String saleYearMonth = String.valueOf(fiscalYear);
					if(fiscalMonth < 10) {
						saleYearMonth = saleYearMonth + "0" + String.valueOf(fiscalMonth);
					} else {
						saleYearMonth = saleYearMonth + String.valueOf(fiscalMonth);
					}
					map.put("saleYearMonthDateFlag", "1");
					//用于读取统计表（SaleYearMonth：201309）
					map.put("saleYearMonth", saleYearMonth);
					// 查询指定月份是否存在统计信息
					String saleCountHistoryID = binOLPTRPS15_Service.getSaleCountHistoryID(map);
					// 存在月度统计信息时，从月度统计信息中取月度统计
					if(saleCountHistoryID != null) {
						map.put("saleCountFlag", "1");
					}
					map.putAll(fiscalMonthMap);
					// 查询指定财务月的最小最大自然日
//					Map<String, Object> minMaxDateValueMap = binOLPTRPS15_Service.getMinMaxDateValue(map);
					String[] minMaxDateValue = binOLCM00_BL
							.getFiscalPeriodByNatural(
									userInfo.getBIN_OrganizationInfoID(),
									form.getSaleTimeStart());
					map.put("minDateValue", minMaxDateValue[0]);
					map.put("maxDateValue", minMaxDateValue[1]);
	
					//显示财务年月及财务月时间段
					fiscalShow = "1";
					fiscalYearDisplay = fiscalYear;
					fiscalMonthDisplay = fiscalMonth;
					fiscalMonthStart = minMaxDateValue[0];
					fiscalMonthEnd = minMaxDateValue[1];
				}
			} else if("2".equals(fiscalFlagStr)){//年度统计
				map.put("dateValue", form.getSaleTimeStart());
				// 查询指定日期所在的财务年月
				Map<String, Object> fiscalMonthMap = binOLPTRPS15_Service.getFiscalMonth(map);
				if(fiscalMonthMap != null) {
					int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
					String minSaleYearMonth = fiscalYear + "01";
					String maxSaleYearMonth = fiscalYear + "12";
					map.put("saleYearMonthDateFlag", "2");
					//年度统计则取当前财务年【所有月度（01~12）】统计信息
					map.put("minSaleYearMonth", minSaleYearMonth);
					map.put("maxSaleYearMonth", maxSaleYearMonth);
					// 查询指定月份是否存在统计信息
					String saleCountHistoryID = binOLPTRPS15_Service.getSaleCountHistoryID(map);
					// 存在月度统计信息(此时是一个年度的)时，从月度统计信息中取月度统计
					if(saleCountHistoryID != null) {
						map.put("saleCountFlag", "1");
					}
					map.putAll(fiscalMonthMap);
					// 查询指定财务年的最小最大自然日
					Map<String, Object> yearMinMaxDateValue = binOLPTRPS15_Service
							.getFiscalYearAndMinMaxDate(map);
					String minYearDateValue = yearMinMaxDateValue.get("minYearDateValue").toString();
					String maxYearDateValue = yearMinMaxDateValue.get("maxYearDateValue").toString();
					map.put("minDateValue", minYearDateValue);
					map.put("maxDateValue", maxYearDateValue);
	
					//显示财务年月及财务月时间段
					this.setFiscalShow("2");
					fiscalYearDisplay = fiscalYear;
					fiscalMonthStart = minYearDateValue;
					fiscalMonthEnd = maxYearDateValue;
				}
			}
		}
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		
		if(map.get("saleCountFlag") != null) {//此时saleCountHistoryID != null
			// 查询未进行销售统计且为销售记录修改的记录数
			int modifiedSaleCount = binOLPTRPS15_Service.getModifiedSaleCount(map);
			if(modifiedSaleCount > 0) {
				map.remove("saleCountFlag");
			}
		}
		
		// 查询销售统计信息0
		saleCountInfo = binOLPTRPS15_BL.getSaleCountInfo(map);
		if(saleCountInfo != null && !saleCountInfo.isEmpty()) {
			int count = (Integer)saleCountInfo.get("count");
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count > 0) {
				// 查询销售统计信息List
				saleCountInfoList = binOLPTRPS15_BL.getSaleCountInfoList(map);
			}
		}
		return SUCCESS;
	}
	
	/**
     * 导出Excel
     */
    public String export() throws Exception {
    	
    	try {
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		//产品ID
    		String selPrtVendorIdStr = form.getSelPrtVendorIdArr();
    		map.remove("selPrtVendorIdArr");
    		if(!"null".equalsIgnoreCase(selPrtVendorIdStr) && null != selPrtVendorIdStr && !"".equals(selPrtVendorIdStr)) {
    			List selPrtVendorIdList = ConvertUtil.json2List(selPrtVendorIdStr);
    			map.put("selPrtVendorIdArr", selPrtVendorIdList);
    		}
    		//得到分类信息
    		getCateInfo(map);
    		// 用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    		String brandInfoID = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
    		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoID);
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, brandInfoID);
    		}
    		
    		// 系统配置项1349：销售统计是否只统计柜台
    		String isOnlyCounterFlag = binOLCM14_BL.getConfigValue("1349", organizationInfoID, brandInfoID);
    		map.put("isOnlyCounterFlag", isOnlyCounterFlag);
    		
    		//统计标记，0：不统计，1：月度统计，2：年度统计
    		map.put("saleYearMonthDateFlag", "0");
			if (!CherryChecker.isNullOrEmpty(form.getSaleTimeStart())
					&& !CherryChecker.isNullOrEmpty(form.getSaleTimeEnd())) {
				String saleTimeStart = form.getSaleTimeStart();
				String saleTimeEnd = form.getSaleTimeEnd();
				Map<String, Object> flagAndYearValue = binOLPTRPS15_BL.getFiscalFlagAndYearValue(map,saleTimeStart,saleTimeEnd);
				//查询出开始、截止日期所处的财务月的起止日期
				String[] minMaxDateValueStart = binOLCM00_BL
						.getFiscalPeriodByNatural(
								userInfo.getBIN_OrganizationInfoID(),
								saleTimeStart);
				String[] minMaxDateValueEnd = binOLCM00_BL
						.getFiscalPeriodByNatural(
								userInfo.getBIN_OrganizationInfoID(),
								saleTimeEnd);
				// 销售统计开始时间和结束时间所属财务月相同的场合，需要显示月度统计
				if (minMaxDateValueStart[0].compareTo(minMaxDateValueEnd[0])
						+ minMaxDateValueStart[1]
								.compareTo(minMaxDateValueEnd[1]) == 0) {
	    			map.put("dateValue", form.getSaleTimeStart());
	    			// 查询指定日期所在的财务年月
	    			Map<String, Object> fiscalMonthMap = binOLPTRPS15_Service.getFiscalMonth(map);
	    			if(fiscalMonthMap != null) {
	    				int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
	    				int fiscalMonth = (Integer)fiscalMonthMap.get("fiscalMonth");
	    				String saleYearMonth = String.valueOf(fiscalYear);
	    				if(fiscalMonth < 10) {
	    					saleYearMonth = saleYearMonth + "0" + String.valueOf(fiscalMonth);
	    				} else {
	    					saleYearMonth = saleYearMonth + String.valueOf(fiscalMonth);
	    				}
	    				map.put("saleYearMonthDateFlag", "1");
						//用于读取统计表（SaleYearMonth：201309）
						map.put("saleYearMonth", saleYearMonth);
	    				// 查询指定月份是否存在统计信息
	    				String saleCountHistoryID = binOLPTRPS15_Service.getSaleCountHistoryID(map);
	    				// 存在月度统计信息时，从月度统计信息中取月度统计
	    				if(saleCountHistoryID != null) {
	    					map.put("saleCountFlag", "1");
	    				}
	    				map.putAll(fiscalMonthMap);
	    				// 查询指定财务月的最小最大自然日
//	    				Map<String, Object> minMaxDateValueMap = binOLPTRPS15_Service.getMinMaxDateValue(map);
	    				map.put("minDateValue", minMaxDateValueStart[0]);
	    				map.put("maxDateValue", minMaxDateValueStart[1]);

	    				//显示财务年月及财务月时间段
	    				fiscalShow = "1";
	    				fiscalYearDisplay = fiscalYear;
	    				fiscalMonthDisplay = fiscalMonth;
	    				fiscalMonthStart = minMaxDateValueStart[0];
	    				fiscalMonthEnd = minMaxDateValueStart[1];
	    			}
	    		} else if(flagAndYearValue != null && !flagAndYearValue.isEmpty()){
	    			map.put("dateValue", form.getSaleTimeStart());
					// 查询指定日期所在的财务年月
					Map<String, Object> fiscalMonthMap = binOLPTRPS15_Service.getFiscalMonth(map);
					if(fiscalMonthMap != null) {
						int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
						String minSaleYearMonth = fiscalYear + "01";
						String maxSaleYearMonth = fiscalYear + "12";
						map.put("saleYearMonthDateFlag", "2");
						map.put("minSaleYearMonth", minSaleYearMonth);
						map.put("maxSaleYearMonth", maxSaleYearMonth);
						// 查询指定月份是否存在统计信息
						String saleCountHistoryID = binOLPTRPS15_Service.getSaleCountHistoryID(map);
						// 存在月度统计信息(此时是一个年度的)时，从月度统计信息中取月度统计
						if(saleCountHistoryID != null) {
							map.put("saleCountFlag", "1");
						}
						map.putAll(fiscalMonthMap);
						// 查询指定财务月的最小最大自然日
						Map<String, Object> yearMinMaxDateValue = binOLPTRPS15_Service
								.getFiscalYearAndMinMaxDate(map);
						String minYearDateValue = yearMinMaxDateValue.get("minYearDateValue").toString();
						String maxYearDateValue = yearMinMaxDateValue.get("maxYearDateValue").toString();
						map.put("minDateValue", minYearDateValue);
						map.put("maxDateValue", maxYearDateValue);
		
						//显示财务年月及财务月时间段
						this.setFiscalShow("2");
						fiscalYearDisplay = fiscalYear;
						fiscalMonthStart = minYearDateValue;
						fiscalMonthEnd = maxYearDateValue;
					}
	    		}
			}
    		
    		// 用户ID
    		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
    		// form参数设置到paramMap中
    		ConvertUtil.setForm(form, map);
    		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
    		map.putAll(paramsMap);
    		map = CherryUtil.removeEmptyVal(map);
    		
    		if(map.get("saleCountFlag") != null) {
    			// 查询未进行销售统计且为销售记录修改的记录数
    			int modifiedSaleCount = binOLPTRPS15_Service.getModifiedSaleCount(map);
    			if(modifiedSaleCount > 0) {
    				map.remove("saleCountFlag");
    			}
    		}
    		
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		
    		// 查询销售统计信息
    		saleCountInfo = binOLPTRPS15_BL.getSaleCountInfo(map);
    		if(saleCountInfo != null && !saleCountInfo.isEmpty()) {
    			
    			String quantity = new DecimalFormat("0").format(saleCountInfo.get("quantity"));
        		String amount = new DecimalFormat("0.00").format(saleCountInfo.get("amount"));
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_totalQuantity")+quantity);
        		strBuf.append("\0\0\0\0");
        		strBuf.append(binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_totalAmount")+amount);
        		if("1".equals(fiscalShow)) {
        			strBuf.append("\0\0\0\0\0\0");
        			strBuf.append(binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_fiscalMonthInfo")
        					+ fiscalYearDisplay
        					+ binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_fiscalYear")
        					+ fiscalMonthDisplay
        					+ binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_fiscalMonth")
        					+ "(\0" + fiscalMonthStart + "\0~\0" + fiscalMonthEnd + "\0)");
        		} else if("2".equals(fiscalShow)) {
        			strBuf.append("\0\0\0\0\0\0");
        			strBuf.append(binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_fiscalYearInfo")
        					+ fiscalYearDisplay
        					+ binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_fiscalYear")
        					+ "(\0" + fiscalMonthStart + "\0~\0" + fiscalMonthEnd + "\0)");
        		}
        		map.put("header", strBuf.toString());
    		}
    		
    		String countModel = (String)map.get("countModel");
    		if(countModel == null || "".equals(countModel)) {
    			countModel = "1";
    		}
    		
    		String zipName = binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "downloadFileName");
            downloadFileName = zipName +".zip";
            map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "sheetName"));
            if("0".equals(countModel)) {
            	if("1".equals(fiscalShow)) {
            		String[][] titleRows = {
                    		{"departNameExp", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departName"), "25", "", ""},
                    		{"departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departCode"), "20", "", ""},
                    		{"busniessPrincipal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_busniessPrincipal"), "20", "", ""},
                    		{"unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_unitCode"), "20", "", ""},
                    		{"barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_barCode"), "20", "", ""},
                    		{"nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_nameTotal"), "20", "", ""},
                    		{"saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_saleType"), "15", "", "1106"},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""},
                    		{"monthQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_monthQuantity"), "20", "int", ""},
                    		{"monthAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_monthAmount"), "20", "float", ""}};
            		map.put("titleRows", titleRows);
            	} else if("2".equals(fiscalShow)) {
            		String[][] titleRows = {
                    		{"departNameExp", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departName"), "25", "", ""},
                    		{"departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departCode"), "20", "", ""},
                    		{"busniessPrincipal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_busniessPrincipal"), "20", "", ""},
                    		{"unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_unitCode"), "20", "", ""},
                    		{"barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_barCode"), "20", "", ""},
                    		{"nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_nameTotal"), "20", "", ""},
                    		{"saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_saleType"), "15", "", "1106"},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""},
                    		{"yearQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_yearQuantity"), "20", "int", ""},
                    		{"yearAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_yearAmount"), "20", "float", ""}};
            		map.put("titleRows", titleRows);
            	} else {
            		String[][] titleRows = {
            				{"departNameExp", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departName"), "25", "", ""},
                    		{"departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departCode"), "20", "", ""},
                    		{"busniessPrincipal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_busniessPrincipal"), "20", "", ""},
                    		{"unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_unitCode"), "20", "", ""},
                    		{"barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_barCode"), "20", "", ""},
                    		{"nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_nameTotal"), "20", "", ""},
                    		{"saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_saleType"), "15", "", "1106"},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""}};
            		map.put("titleRows", titleRows);
            	}
            } else if("2".equals(countModel)) {
            	if("1".equals(fiscalShow)) {
            		String[][] titleRows = {
                			{"unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_unitCode"), "20", "", ""},
                    		{"barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_barCode"), "20", "", ""},
                    		{"nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_nameTotal"), "20", "", ""},
                    		{"saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_saleType"), "15", "", "1106"},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""},
                    		{"monthQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_monthQuantity"), "20", "int", ""},
                    		{"monthAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_monthAmount"), "20", "float", ""}};
            		map.put("titleRows", titleRows);
            	} else if("2".equals(fiscalShow)) {
            		String[][] titleRows = {
                			{"unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_unitCode"), "20", "", ""},
                    		{"barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_barCode"), "20", "", ""},
                    		{"nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_nameTotal"), "20", "", ""},
                    		{"saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_saleType"), "15", "", "1106"},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""},
                    		{"yearQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_yearQuantity"), "20", "int", ""},
                    		{"yearAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_yearAmount"), "20", "float", ""}};
            		map.put("titleRows", titleRows);
            	} else {
            		String[][] titleRows = {
                			{"unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_unitCode"), "20", "", ""},
                    		{"barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_barCode"), "20", "", ""},
                    		{"nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_nameTotal"), "20", "", ""},
                    		{"saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_saleType"), "15", "", "1106"},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""}};
            		map.put("titleRows", titleRows);
            	}
            } else {
            	if("1".equals(fiscalShow)) {
            		String[][] titleRows = {
            				{"departNameExp", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departName"), "25", "", ""},
                    		{"departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departCode"), "20", "", ""},
                    		{"busniessPrincipal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_busniessPrincipal"), "20", "", ""},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""},
                    		{"monthQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_monthQuantity"), "20", "int", ""},
                    		{"monthAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_monthAmount"), "20", "float", ""}};
                    map.put("titleRows", titleRows);
            	} else if("2".equals(fiscalShow)) {
            		String[][] titleRows = {
            				{"departNameExp", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departName"), "25", "", ""},
                    		{"departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departCode"), "20", "", ""},
                    		{"busniessPrincipal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_busniessPrincipal"), "20", "", ""},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""},
                    		{"yearQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_yearQuantity"), "20", "int", ""},
                    		{"yearAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_yearAmount"), "20", "float", ""}};
                    map.put("titleRows", titleRows);
            	} else {
            		String[][] titleRows = {
            				{"departNameExp", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departName"), "25", "", ""},
                    		{"departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_departCode"), "20", "", ""},
                    		{"busniessPrincipal", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_busniessPrincipal"), "20", "", ""},
                    		{"quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_quantity"), "15", "int", ""},
                    		{"amount", binOLCM37_BL.getResourceValue("BINOLPTRPS15", language, "binolptrps15_amount"), "15", "float", ""}};
                    map.put("titleRows", titleRows);
            	}
            }
     		map.put(CherryConstants.SORT_ID, "amount desc");
            byte[] byteArray = binOLCM37_BL.exportExcel(map, binOLPTRPS15_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
    
	/**
	 * 根据起止日期取得是否为同一财务月标记
	 * 
	 * @throws Exception
	 */
	public void getFiscalFlag() throws Exception {

		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		
		String fiscalFlagStr = getFiscalFlagINT(map);
		
		ConvertUtil.setResponseByAjax(response, fiscalFlagStr);

	}
	
	/**
	 * 根据起止日期取得是否为同一财务月标记
	 * @param map
	 * @return
	 */
	private String getFiscalFlagINT(Map<String, Object> map){
		String fiscalFlagStr = "0";
		// 用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID,
						userInfo.getBIN_OrganizationInfoID());
				// 不是总部的场合
				if (userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
				if (!CherryChecker.isNullOrEmpty(form.getSaleTimeStart(), true)
						&& !CherryChecker.isNullOrEmpty(form.getSaleTimeEnd(), true)) {
					int orgInfoId = userInfo.getBIN_OrganizationInfoID();
					String saleTimeStart = form.getSaleTimeStart();
					String saleTimeEnd = form.getSaleTimeEnd();
					// 分别取得开始、截止日期所属财务月的起止日期
					String[] minMaxDateValueStart = binOLCM00_BL
							.getFiscalPeriodByNatural(orgInfoId, saleTimeStart);
					String[] minMaxDateValueEnd = binOLCM00_BL
							.getFiscalPeriodByNatural(orgInfoId, saleTimeEnd);
					Map<String, Object> flagAndYearValue = binOLPTRPS15_BL.getFiscalFlagAndYearValue(map,saleTimeStart,saleTimeEnd);
					if (minMaxDateValueStart[0].compareTo(minMaxDateValueEnd[0])
							+ minMaxDateValueStart[1].compareTo(minMaxDateValueEnd[1]) == 0) {
						//起止日期所属同一财务月时
						fiscalFlagStr = "1";
					} else if(flagAndYearValue != null && !flagAndYearValue.isEmpty()){
						//起止日期属于同一财务年时
						fiscalFlagStr = flagAndYearValue.get("fiscalFlag")+"";
					} else {
						//起止日期不属于同一财务年、财务月时
						fiscalFlagStr = "0";
					}
				} else {
					fiscalFlagStr = "0";
				}
		return fiscalFlagStr;
	}
	
	/** 销售统计信息List **/
	private List<Map<String, Object>> saleCountInfoList;
	
	/** 查询销售统计信息 **/
	private Map<String, Object> saleCountInfo;
	
	/** 是否显示统计(1:月度，2：年度)信息 **/
	private String fiscalShow;
	
	/** 显示财务年*/
	private int fiscalYearDisplay;
	/** 显示财务月*/
	private int fiscalMonthDisplay;
	
	/** 财务月开始时间 **/
	private String fiscalMonthStart;
	
	/** 财务月结束时间 **/
	private String fiscalMonthEnd;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public int getFiscalYearDisplay() {
		return fiscalYearDisplay;
	}

	public void setFiscalYearDisplay(int fiscalYearDisplay) {
		this.fiscalYearDisplay = fiscalYearDisplay;
	}

	public int getFiscalMonthDisplay() {
		return fiscalMonthDisplay;
	}

	public void setFiscalMonthdisplay(int fiscalMonthDisplay) {
		this.fiscalMonthDisplay = fiscalMonthDisplay;
	}

	public List<Map<String, Object>> getSaleCountInfoList() {
		return saleCountInfoList;
	}

	public void setSaleCountInfoList(List<Map<String, Object>> saleCountInfoList) {
		this.saleCountInfoList = saleCountInfoList;
	}

	public Map<String, Object> getSaleCountInfo() {
		return saleCountInfo;
	}

	public void setSaleCountInfo(Map<String, Object> saleCountInfo) {
		this.saleCountInfo = saleCountInfo;
	}

	public String getFiscalMonthStart() {
		return fiscalMonthStart;
	}

	public void setFiscalMonthStart(String fiscalMonthStart) {
		this.fiscalMonthStart = fiscalMonthStart;
	}

	public String getFiscalMonthEnd() {
		return fiscalMonthEnd;
	}

	public void setFiscalMonthEnd(String fiscalMonthEnd) {
		this.fiscalMonthEnd = fiscalMonthEnd;
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

	/** 统计销售信息Form **/
	private BINOLPTRPS15_Form form = new BINOLPTRPS15_Form();

	@Override
	public BINOLPTRPS15_Form getModel() {
		return form;
	}

	public String getFiscalShow() {
		return fiscalShow;
	}

	public void setFiscalShow(String fiscalShow) {
		this.fiscalShow = fiscalShow;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}

}

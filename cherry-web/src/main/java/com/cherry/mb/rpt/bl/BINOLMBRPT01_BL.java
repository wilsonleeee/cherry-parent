/*
 * @(#)BINOLMBRPT01_BL.java     1.0 2013/10/12
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
package com.cherry.mb.rpt.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM02_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT01_Service;

/**
 * 会员销售报表BL
 * 
 * @author WangCT
 * @version 1.0 2013/10/12
 */
public class BINOLMBRPT01_BL {
	
	/** 会员销售报表Service **/
	@Resource
	private BINOLMBRPT01_Service binOLMBRPT01_Service;
	
	/** 弹出datatable 共通 **/
	@Resource
	private BINOLCM02_BL binOLCM02_BL;
	
	/** 导出会员信息共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;
	
	/**
     * 导出Excel
     */
    public InputStream export(Map<String, Object> map) throws Exception {
    	
    	// 取得会员销售报表
    	List<Map<String, Object>> dataList = this.getMemSaleRpt(map);
    	if(dataList == null || dataList.isEmpty()) {
    		return null;
    	}
    	String sysTime = binOLMBRPT01_Service.getSYSDateTime();
    	
    	String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    	
    	Map<String, Object> exportMap = new HashMap<String, Object>();
    	// 所属组织
    	exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
    	exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
    	exportMap.put("dataList", dataList);
    	
    	// 设定查询条件
//    	String dateMode = (String)map.get("dateMode");
//    	String header = "";
//    	if(dateMode != null && "0".equals(dateMode)) {
//    		header = binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_dateMode0");
//    		String fiscalYear = (String)map.get("fiscalYear");
//    		String fiscalMonth = (String)map.get("fiscalMonth");
//    		if(fiscalMonth.length() == 1) {
//    			fiscalMonth = "0"+fiscalMonth;
//    		}
//    		header += "：" + fiscalYear + fiscalMonth;
//    	} else {
//    		header = binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_dateMode1");
//    		String saleDateStart = (String)map.get("saleDateStart");
//    		String saleDateEnd = (String)map.get("saleDateEnd");
//    		header += "：" + saleDateStart + "-" + saleDateEnd;
//    	}
//    	header += "\0\0\0\0\0\0" + binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_exportDateTime") + "：" + sysTime;
//    	exportMap.put("header", header);
    	String header = this.getConditionStr(map);
    	header += "\0\0\0\0\0\0" + binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_exportDateTime") + "：" + sysTime;
    	exportMap.put("header", header);
    	
		// 设定sheet名称
		exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "sheetName"));
		
		List<Map<String, Object>> gradeList = code.getCodesByGrade("1000");
		int lenght = 0;
		
		// 设定标题行
		List<String[][]> titleList = new ArrayList<String[][]>();
		List<String[]> title1 = new ArrayList<String[]>();
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_number"), "1", "2"});
		// 设定部门类型
		for(int i = 0; i < gradeList.size(); i++) {
			Map<String, Object> gradeMap = gradeList.get(i);
			String codeKey = (String)gradeMap.get("CodeKey");
			if("0".equals(codeKey) || "1".equals(codeKey) || "4".equals(codeKey)) {
				continue;
			}
			String value = (String)gradeMap.get("Value");
			title1.add(new String[]{value, "1", "2"});
			lenght++;
		}
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_employeeName"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_provinceName"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_cityName"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_channelName"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_counterCode"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_counterName"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_fiscalMonth"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleDays"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_memNumber"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_newMemNumber"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_newMemNumberPre"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_memNumberRate"), "1", "2"});
		
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_totalQuantity"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_totalAmount"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_totalSaleCount"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_totalSaleNumber"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_unitPrice"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_joinRate"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_memSaleNumberRate"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_memQuantityRate"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_memAmountRate"), "1", "2"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_memSaleCountRate"), "1", "2"});
		
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_newMemAnalysis"), "10", "1"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_oldMemAnalysis"), "10", "1"});
		title1.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_nonMemAnalysis"), "8", "1"});
		
		titleList.add(title1.toArray(new String[][]{}));
		
		List<String[]> title2 = new ArrayList<String[]>();
		title2.add(new String[]{"", String.valueOf(23+lenght), "1"});
		
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleNumber"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleQuantity"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleAmount"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleCount"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_unitPrice"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_joinRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleNumberRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleQuantityRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleAmountRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleCountRate"), "1", "1"});
		
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleNumber"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleQuantity"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleAmount"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleCount"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_unitPrice"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_joinRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleNumberRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleQuantityRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleAmountRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleCountRate"), "1", "1"});
		
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleQuantity"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleAmount"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleCount"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_unitPrice"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_joinRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleQuantityRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleAmountRate"), "1", "1"});
		title2.add(new String[]{binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_saleCountRate"), "1", "1"});
		
		titleList.add(title2.toArray(new String[][]{}));
		exportMap.put("titleList", titleList);
		
		// 设定数据行格式
        List<String[]> titleRows = new ArrayList<String[]>();
        titleRows.add(new String[]{"number", "", "5", "int", ""});
        for(int i = 0; i < gradeList.size(); i++) {
			Map<String, Object> gradeMap = gradeList.get(i);
			String codeKey = (String)gradeMap.get("CodeKey");
			if("0".equals(codeKey) || "1".equals(codeKey) || "4".equals(codeKey)) {
				continue;
			}
			titleRows.add(new String[]{"name_"+codeKey, "", "15", "", ""});
		}
        titleRows.add(new String[]{"employeeName", "", "15", "", ""});
        titleRows.add(new String[]{"provinceName", "", "15", "", ""});
        titleRows.add(new String[]{"cityName", "", "15", "", ""});
        titleRows.add(new String[]{"channelName", "", "15", "", ""});
        titleRows.add(new String[]{"counterCode", "", "15", "", ""});
        titleRows.add(new String[]{"counterName", "", "15", "", ""});
        titleRows.add(new String[]{"fiscalMonth", "", "15", "", ""});
        titleRows.add(new String[]{"saleDays", "", "15", "int", ""});
        titleRows.add(new String[]{"memNumber", "", "15", "int", ""});
        titleRows.add(new String[]{"newMemNumber", "", "15", "int", ""});
        titleRows.add(new String[]{"newMemNumberPre", "", "15", "int", ""});
        titleRows.add(new String[]{"memNumberRate", "", "15", "", ""});
		
        titleRows.add(new String[]{"totalQuantity", "", "15", "int", ""});
        titleRows.add(new String[]{"totalAmount", "", "15", "float", ""});
        titleRows.add(new String[]{"totalSaleCount", "", "15", "int", ""});
        titleRows.add(new String[]{"totalSaleNumber", "", "15", "int", ""});
        titleRows.add(new String[]{"unitPrice", "", "15", "float", ""});
        titleRows.add(new String[]{"joinRate", "", "15", "float", ""});
        titleRows.add(new String[]{"memSaleNumberRate", "", "15", "", ""});
        titleRows.add(new String[]{"memQuantityRate", "", "15", "", ""});
        titleRows.add(new String[]{"memAmountRate", "", "15", "", ""});
        titleRows.add(new String[]{"memSaleCountRate", "", "15", "", ""});
		
        titleRows.add(new String[]{"newMemSaleNumber", "", "15", "int", ""});
        titleRows.add(new String[]{"newMemQuantity", "", "15", "int", ""});
        titleRows.add(new String[]{"newMemAmount", "", "15", "float", ""});
        titleRows.add(new String[]{"newMemSaleCount", "", "15", "int", ""});
        titleRows.add(new String[]{"newMemUnitPrice", "", "15", "float", ""});
        titleRows.add(new String[]{"newMemJoinRate", "", "15", "float", ""});
        titleRows.add(new String[]{"newMemSaleNumberRate", "", "15", "", ""});
        titleRows.add(new String[]{"newMemQuantityRate", "", "15", "", ""});
        titleRows.add(new String[]{"newMemAmountRate", "", "15", "", ""});
        titleRows.add(new String[]{"newMemSaleCountRate", "", "15", "", ""});
		
        titleRows.add(new String[]{"oldMemSaleNumber", "", "15", "int", ""});
        titleRows.add(new String[]{"oldMemQuantity", "", "15", "int", ""});
        titleRows.add(new String[]{"oldMemAmount", "", "15", "float", ""});
        titleRows.add(new String[]{"oldMemSaleCount", "", "15", "int", ""});
        titleRows.add(new String[]{"oldMemUnitPrice", "", "15", "float", ""});
        titleRows.add(new String[]{"oldMemJoinRate", "", "15", "float", ""});
        titleRows.add(new String[]{"oldMemSaleNumberRate", "", "15", "", ""});
        titleRows.add(new String[]{"oldMemQuantityRate", "", "15", "", ""});
		titleRows.add(new String[]{"oldMemAmountRate", "", "15", "", ""});
		titleRows.add(new String[]{"oldMemSaleCountRate", "", "15", "", ""});
		
		titleRows.add(new String[]{"nonMemQuantity", "", "15", "int", ""});
		titleRows.add(new String[]{"nonMemAmount", "", "15", "float", ""});
		titleRows.add(new String[]{"nonMemSaleCount", "", "15", "int", ""});
		titleRows.add(new String[]{"nonMemUnitPrice", "", "15", "float", ""});
		titleRows.add(new String[]{"nonMemJoinRate", "", "15", "float", ""});
		titleRows.add(new String[]{"nonMemQuantityRate", "", "15", "", ""});
		titleRows.add(new String[]{"nonMemAmountRate", "", "15", "", ""});
		titleRows.add(new String[]{"nonMemSaleCountRate", "", "15", "", ""});
        exportMap.put("titleRows", titleRows.toArray(new String[][]{}));
        
    	// 导出excel处理
    	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap);
    	// 压缩包名称
    	String zipName = binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "downloadFileName");
        return new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
    }
	
	/**
	 * 取得会员销售报表
	 * 
	 * @param map 检索条件
	 * @return 会员销售报表
	 */
	public List<Map<String, Object>> getMemSaleRpt(Map<String, Object> map) {
		
		String dateMode = (String)map.get("dateMode");
    	String fiscalYearMonth = null;
    	// 时间范围的场合
    	if(dateMode != null && "0".equals(dateMode)) {
    		String fiscalYear = (String)map.get("fiscalYear");
    		String fiscalMonth = (String)map.get("fiscalMonth");
    		if(fiscalYear == null || "".equals(fiscalYear) 
    				|| fiscalMonth == null || "".equals(fiscalMonth)) {
    			return null;
    		}
    		if(fiscalMonth.length() == 1) {
    			fiscalMonth = "0"+fiscalMonth;
    		}
    		fiscalYearMonth = fiscalYear + fiscalMonth;
    		// 查询指定财务月的最小最大自然日
    		Map<String, Object> minMaxDateValueMap = binOLMBRPT01_Service.getMinMaxDateValue(map);
    		if(minMaxDateValueMap != null) {
    			map.put("saleDateStart", minMaxDateValueMap.get("minDateValue"));
        		map.put("saleDateEnd", minMaxDateValueMap.get("maxDateValue"));
    		} else {
    			return null;
    		}
    	} else {
    		String saleDateStart = (String)map.get("saleDateStart");
    		String saleDateEnd = (String)map.get("saleDateEnd");
    		if(saleDateStart == null || "".equals(saleDateStart) 
    				|| saleDateEnd == null || "".equals(saleDateEnd)) {
    			return null;
    		}
    	}
		
		// 查询柜台信息List
		List<Map<String, Object>> counterList = binOLMBRPT01_Service.getCounterList(map);
		if(counterList != null && !counterList.isEmpty()) {
			
			// 生成柜台组织结构信息
			// 取得所有部门层级结构信息List
			List<Map<String, Object>> orgTreeList = binOLCM02_BL.getAllOrgList(map);
			List<Map<String, Object>> orgList = new ArrayList<Map<String, Object>>();
			// 把树型结构转换成列表结构
			this.convertTreeToList(orgTreeList, orgList, new HashMap<String, Object>());
			
			// 生成柜台会员销售统计信息
			// 按柜台统计总销售金额、数量、单数，会员销售金额、数量、单数 ，非会员销售金额、数量、单数
			List<Map<String, Object>> memSaleCountList = binOLMBRPT01_Service.getMemSaleCount(map);
			// 按柜台统计新会员销售金额、数量、单数
			List<Map<String, Object>> newMemSaleCountList = null;
			// 按柜台统计会员购买人数
			List<Map<String, Object>> memSalePeopleCountList = null;
			// 按柜台统计非会员销售人数
			List<Map<String, Object>> nonMemSalePeopleCount = null;
			// 按柜台统计销售天数
			List<Map<String, Object>> saleDaysList = null;
			if(memSaleCountList != null && !memSaleCountList.isEmpty()) {
				// 按柜台统计新会员销售金额、数量、单数
				newMemSaleCountList = binOLMBRPT01_Service.getNewMemSaleCount(map);
				// 按柜台统计会员购买人数
				memSalePeopleCountList = binOLMBRPT01_Service.getMemSalePeopleCount(map);
				// 按柜台统计非会员销售人数
				nonMemSalePeopleCount = binOLMBRPT01_Service.getNonMemSalePeopleCount(map);
				// 按柜台统计销售天数
				saleDaysList = binOLMBRPT01_Service.getSaleDaysList(map);
			}
			
			// 生成柜台会员人数信息
			// 按柜台统计会员人数
			List<Map<String, Object>> memPeopleCountList = binOLMBRPT01_Service.getMemPeopleCount(map);
			// 查询条件为财务月的场合，按柜台统计上月会员人数信息
			List<Map<String, Object>> memPeopleCountPreList = null;
			if(dateMode != null && "0".equals(dateMode)) {
				String fiscalYearMonthPre = DateUtil.addDateByMonth("yyyyMMdd", fiscalYearMonth+"01", -1);
				Map<String, Object> _map = new HashMap<String, Object>();
				_map.putAll(map);
				_map.put("fiscalYear", fiscalYearMonthPre.substring(0, 4));
				_map.put("fiscalMonth", fiscalYearMonthPre.substring(4, 6));
				// 查询指定财务月的最小最大自然日
	    		Map<String, Object> minMaxDateValueMap = binOLMBRPT01_Service.getMinMaxDateValue(_map);
	    		if(minMaxDateValueMap != null) {
	    			_map.put("saleDateStart", minMaxDateValueMap.get("minDateValue"));
	    			_map.put("saleDateEnd", minMaxDateValueMap.get("maxDateValue"));
	    			// 按柜台统计会员人数
	        		memPeopleCountPreList = binOLMBRPT01_Service.getMemPeopleCount(_map);
	    		}
			}
			
			// 销售总金额
			BigDecimal totalAmountSum = new BigDecimal(0);
			// 销售总数量
			BigDecimal totalQuantitySum = new BigDecimal(0);
			// 销售总单数
			Integer totalSaleCountSum = 0;
			// 会员购买金额
			BigDecimal memAmountSum = new BigDecimal(0);
			// 会员购买数量
			BigDecimal memQuantitySum = new BigDecimal(0);
			// 会员购买单数
			Integer memSaleCountSum = 0;
			// 非会员购买金额
			BigDecimal nonMemAmountSum = new BigDecimal(0);
			// 非会员购买数量
			BigDecimal nonMemQuantitySum = new BigDecimal(0);
			// 非会员购买单数
			Integer nonMemSaleCountSum = 0;
			// 新会员购买金额
			BigDecimal newMemAmountSum = new BigDecimal(0);
			// 新会员购买数量
			BigDecimal newMemQuantitySum = new BigDecimal(0);
			// 新会员购买单数
			Integer newMemSaleCountSum = 0;
			// 老会员购买金额
			BigDecimal oldMemAmountSum = new BigDecimal(0);
			// 老会员购买数量
			BigDecimal oldMemQuantitySum = new BigDecimal(0);
			// 老会员购买单数
			Integer oldMemSaleCountSum = 0;
			// 会员购买人数
			Integer memSaleNumberSum = 0;
			// 新会员购买人数
			Integer newMemSaleNumberSum = 0;
			// 非会员购买人数
			Integer nonMemSaleNumberSum = 0;
			// 老会员购买人数
			Integer oldMemSaleNumberSum = 0;
			// 总购买人数
			Integer totalSaleNumberSum = 0;
			// 会员人数
			Integer memNumberSum = 0;
			// 新会员人数
			Integer newMemNumberSum = 0;
			// 上月新会员人数
			Integer newMemNumberPreSum = 0;
			
			// 生成会员销售报表
			for(int i = 0; i < counterList.size(); i++) {
				Map<String, Object> counterMap = counterList.get(i);
				String organizationId = counterMap.get("organizationId").toString();
				// 柜台组织结构信息
				if(orgList != null && !orgList.isEmpty()) {
					for(int j = 0; j < orgList.size(); j++) {
						Map<String, Object> orgMap = orgList.get(j);
						Object _organizationId = orgMap.get("id_4");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.putAll(orgMap);
							orgList.remove(j);
							break;
						}
					}
				}
				// 总销售金额、数量、单数，会员销售金额、数量、单数 ，非会员销售金额、数量、单数
				if(memSaleCountList != null && !memSaleCountList.isEmpty()) {
					for(int j = 0; j < memSaleCountList.size(); j++) {
						Map<String, Object> memSaleCountMap = memSaleCountList.get(j);
						Object _organizationId = memSaleCountMap.get("organizationId");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.putAll(memSaleCountMap);
							memSaleCountList.remove(j);
							break;
						}
					}
				}
				// 新会员销售金额、数量、单数
				if(newMemSaleCountList != null && !newMemSaleCountList.isEmpty()) {
					for(int j = 0; j < newMemSaleCountList.size(); j++) {
						Map<String, Object> newMemSaleCountMap = newMemSaleCountList.get(j);
						Object _organizationId = newMemSaleCountMap.get("organizationId");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.putAll(newMemSaleCountMap);
							newMemSaleCountList.remove(j);
							break;
						}
					}
				}
				// 会员销售人数
				if(memSalePeopleCountList != null && !memSalePeopleCountList.isEmpty()) {
					for(int j = 0; j < memSalePeopleCountList.size(); j++) {
						Map<String, Object> memSalePeopleCountMap = memSalePeopleCountList.get(j);
						Object _organizationId = memSalePeopleCountMap.get("organizationId");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.putAll(memSalePeopleCountMap);
							memSalePeopleCountList.remove(j);
							break;
						}
					}
				}
				// 非会员销售人数
				if(nonMemSalePeopleCount != null && !nonMemSalePeopleCount.isEmpty()) {
					for(int j = 0; j < nonMemSalePeopleCount.size(); j++) {
						Map<String, Object> nonMemSalePeopleMap = nonMemSalePeopleCount.get(j);
						Object _organizationId = nonMemSalePeopleMap.get("organizationId");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.putAll(nonMemSalePeopleMap);
							nonMemSalePeopleCount.remove(j);
							break;
						}
					}
				}
				// 销售天数
				if(saleDaysList != null && !saleDaysList.isEmpty()) {
					for(int j = 0; j < saleDaysList.size(); j++) {
						Map<String, Object> saleDaysMap = saleDaysList.get(j);
						Object _organizationId = saleDaysMap.get("organizationId");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.putAll(saleDaysMap);
							saleDaysList.remove(j);
							break;
						}
					}
				}
				// 柜台会员人数信息
				if(memPeopleCountList != null && !memPeopleCountList.isEmpty()) {
					for(int j = 0; j < memPeopleCountList.size(); j++) {
						Map<String, Object> memPeopleCountMap = memPeopleCountList.get(j);
						Object _organizationId = memPeopleCountMap.get("organizationId");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.putAll(memPeopleCountMap);
							memPeopleCountList.remove(j);
							break;
						}
					}
				}
				// 柜台上月新会员人数信息
				if(memPeopleCountPreList != null && !memPeopleCountPreList.isEmpty()) {
					for(int j = 0; j < memPeopleCountPreList.size(); j++) {
						Map<String, Object> memPeopleCountPreMap = memPeopleCountPreList.get(j);
						Object _organizationId = memPeopleCountPreMap.get("organizationId");
						if(_organizationId != null && organizationId.equals(_organizationId.toString())) {
							counterMap.put("newMemNumberPre", memPeopleCountPreMap.get("newMemNumber"));
							memPeopleCountPreList.remove(j);
							break;
						}
					}
				}
				
				// 编号
				counterMap.put("number", i+1);
				
				// 销售天数
				Integer saleDays = (Integer)counterMap.get("saleDays");
				if(saleDays == null) {
					saleDays = 0;
					counterMap.put("saleDays", saleDays);
				}
				
				// 销售总金额
				BigDecimal totalAmount = (BigDecimal)counterMap.get("totalAmount");
				if(totalAmount == null) {
					totalAmount = new BigDecimal(0);
					counterMap.put("totalAmount", totalAmount);
				}
				totalAmountSum = totalAmountSum.add(totalAmount);
				// 销售总数量
				BigDecimal totalQuantity = (BigDecimal)counterMap.get("totalQuantity");
				if(totalQuantity == null) {
					totalQuantity = new BigDecimal(0);
					counterMap.put("totalQuantity", totalQuantity);
				}
				totalQuantitySum = totalQuantitySum.add(totalQuantity);
				// 销售总单数
				Integer totalSaleCount = (Integer)counterMap.get("totalSaleCount");
				if(totalSaleCount == null) {
					totalSaleCount = 0;
					counterMap.put("totalSaleCount", totalSaleCount);
				}
				totalSaleCountSum = totalSaleCountSum + totalSaleCount;
				// 会员购买金额
				BigDecimal memAmount = (BigDecimal)counterMap.get("memAmount");
				if(memAmount == null) {
					memAmount = new BigDecimal(0);
					counterMap.put("memAmount", memAmount);
				}
				memAmountSum = memAmountSum.add(memAmount);
				// 会员购买数量
				BigDecimal memQuantity = (BigDecimal)counterMap.get("memQuantity");
				if(memQuantity == null) {
					memQuantity = new BigDecimal(0);
					counterMap.put("memQuantity", memQuantity);
				}
				memQuantitySum = memQuantitySum.add(memQuantity);
				// 会员购买单数
				Integer memSaleCount = (Integer)counterMap.get("memSaleCount");
				if(memSaleCount == null) {
					memSaleCount = 0;
					counterMap.put("memSaleCount", memSaleCount);
				}
				memSaleCountSum = memSaleCountSum + memSaleCount;
				// 非会员购买金额
				BigDecimal nonMemAmount = (BigDecimal)counterMap.get("nonMemAmount");
				if(nonMemAmount == null) {
					nonMemAmount = new BigDecimal(0);
					counterMap.put("nonMemAmount", nonMemAmount);
				}
				nonMemAmountSum = nonMemAmountSum.add(nonMemAmount);
				// 非会员购买数量
				BigDecimal nonMemQuantity = (BigDecimal)counterMap.get("nonMemQuantity");
				if(nonMemQuantity == null) {
					nonMemQuantity = new BigDecimal(0);
					counterMap.put("nonMemQuantity", nonMemQuantity);
				}
				nonMemQuantitySum = nonMemQuantitySum.add(nonMemQuantity);
				// 非会员购买单数
				Integer nonMemSaleCount = (Integer)counterMap.get("nonMemSaleCount");
				if(nonMemSaleCount == null) {
					nonMemSaleCount = 0;
					counterMap.put("nonMemSaleCount", nonMemSaleCount);
				}
				nonMemSaleCountSum = nonMemSaleCountSum + nonMemSaleCount;
				// 新会员购买金额
				BigDecimal newMemAmount = (BigDecimal)counterMap.get("newMemAmount");
				if(newMemAmount == null) {
					newMemAmount = new BigDecimal(0);
					counterMap.put("newMemAmount", newMemAmount);
				}
				newMemAmountSum = newMemAmountSum.add(newMemAmount);
				// 新会员购买数量
				BigDecimal newMemQuantity = (BigDecimal)counterMap.get("newMemQuantity");
				if(newMemQuantity == null) {
					newMemQuantity = new BigDecimal(0);
					counterMap.put("newMemQuantity", newMemQuantity);
				}
				newMemQuantitySum = newMemQuantitySum.add(newMemQuantity);
				// 新会员购买单数
				Integer newMemSaleCount = (Integer)counterMap.get("newMemSaleCount");
				if(newMemSaleCount == null) {
					newMemSaleCount = 0;
					counterMap.put("newMemSaleCount", newMemSaleCount);
				}
				newMemSaleCountSum = newMemSaleCountSum + newMemSaleCount;
				// 老会员购买金额
				BigDecimal oldMemAmount = memAmount.subtract(newMemAmount);
				counterMap.put("oldMemAmount", oldMemAmount);
				oldMemAmountSum = oldMemAmountSum.add(oldMemAmount);
				// 老会员购买数量
				BigDecimal oldMemQuantity = memQuantity.subtract(newMemQuantity);
				counterMap.put("oldMemQuantity", oldMemQuantity);
				oldMemQuantitySum = oldMemQuantitySum.add(oldMemQuantity);
				// 老会员购买单数
				int oldMemSaleCount = memSaleCount - newMemSaleCount;
				counterMap.put("oldMemSaleCount", oldMemSaleCount);
				oldMemSaleCountSum = oldMemSaleCountSum + oldMemSaleCount;
				
				
				// 会员购买人数
				Integer memSaleNumber = (Integer)counterMap.get("memSaleNumber");
				if(memSaleNumber == null) {
					memSaleNumber = 0;
					counterMap.put("memSaleNumber", memSaleNumber);
				}
				memSaleNumberSum = memSaleNumberSum + memSaleNumber;
				// 新会员购买人数
				Integer newMemSaleNumber = (Integer)counterMap.get("newMemSaleNumber");
				if(newMemSaleNumber == null) {
					newMemSaleNumber = 0;
					counterMap.put("newMemSaleNumber", newMemSaleNumber);
				}
				newMemSaleNumberSum = newMemSaleNumberSum + newMemSaleNumber;
				// 非会员购买人数
				Integer nonMemSaleNumber = (Integer)counterMap.get("nonMemSaleNumber");
				if(nonMemSaleNumber == null) {
					nonMemSaleNumber = 0;
					counterMap.put("nonMemSaleNumber", nonMemSaleNumber);
				}
				nonMemSaleNumberSum = nonMemSaleNumberSum + nonMemSaleNumber;
				// 老会员购买人数
				int oldMemSaleNumber = memSaleNumber - newMemSaleNumber;
				counterMap.put("oldMemSaleNumber", oldMemSaleNumber);
				oldMemSaleNumberSum = oldMemSaleNumberSum + oldMemSaleNumber;
				// 总购买人数
				int totalSaleNumber = memSaleNumber + nonMemSaleNumber;
				counterMap.put("totalSaleNumber", totalSaleNumber);
				totalSaleNumberSum = totalSaleNumberSum + totalSaleNumber;
				
				// 会员人数
				Integer memNumber = (Integer)counterMap.get("memNumber");
				if(memNumber == null) {
					memNumber = 0;
					counterMap.put("memNumber", memNumber);
				}
				memNumberSum = memNumberSum + memNumber;
				// 新会员人数
				Integer newMemNumber = (Integer)counterMap.get("newMemNumber");
				if(newMemNumber == null) {
					newMemNumber = 0;
					counterMap.put("newMemNumber", newMemNumber);
				}
				newMemNumberSum = newMemNumberSum + newMemNumber;
				
				if(dateMode != null && "0".equals(dateMode)) {
					// 财务月
					counterMap.put("fiscalMonth", fiscalYearMonth);
					// 上月新会员人数
					Integer newMemNumberPre = (Integer)counterMap.get("newMemNumberPre");
					if(newMemNumberPre == null) {
						newMemNumberPre = 0;
						counterMap.put("newMemNumberPre", newMemNumberPre);
					}
					newMemNumberPreSum = newMemNumberPreSum + newMemNumberPre;
					if(newMemNumberPre > 0) {
						// 会员增长率
						counterMap.put("memNumberRate", CherryUtil.percent(newMemNumber-newMemNumberPre, newMemNumberPre, 2));
					}
				}
				
				if(memNumber > 0) {
					// 会员购买人数占比
					counterMap.put("memSaleNumberRate", CherryUtil.percent(memSaleNumber, memNumber, 2));
					// 新会员购买人数占比
					counterMap.put("newMemSaleNumberRate", CherryUtil.percent(newMemSaleNumber, memNumber, 2));
					// 老会员购买人数占比
					counterMap.put("oldMemSaleNumberRate", CherryUtil.percent(oldMemSaleNumber, memNumber, 2));
				}
				
				if(totalSaleCount > 0) {
					// 客单价
					counterMap.put("unitPrice", CherryUtil.div(totalAmount.doubleValue(), totalSaleCount, 2));
					// 连带率
					counterMap.put("joinRate", CherryUtil.div(totalQuantity.doubleValue(), totalSaleCount, 2));
				}
				if(newMemSaleCount > 0) {
					// 新会员客单价
					counterMap.put("newMemUnitPrice", CherryUtil.div(newMemAmount.doubleValue(), newMemSaleCount, 2));
					// 新会员连带率
					counterMap.put("newMemJoinRate", CherryUtil.div(newMemQuantity.doubleValue(), newMemSaleCount, 2));
				}
				if(oldMemSaleCount > 0) {
					// 老会员客单价
					counterMap.put("oldMemUnitPrice", CherryUtil.div(oldMemAmount.doubleValue(), oldMemSaleCount, 2));
					// 老会员连带率
					counterMap.put("oldMemJoinRate", CherryUtil.div(oldMemQuantity.doubleValue(), oldMemSaleCount, 2));
				}
				if(nonMemSaleCount > 0) {
					// 非会员客单价
					counterMap.put("nonMemUnitPrice", CherryUtil.div(nonMemAmount.doubleValue(), nonMemSaleCount, 2));
					// 非会员连带率
					counterMap.put("nonMemJoinRate", CherryUtil.div(nonMemQuantity.doubleValue(), nonMemSaleCount, 2));
				}
				if(totalQuantity.doubleValue() > 0) {
					// 会员数量占比
					counterMap.put("memQuantityRate", CherryUtil.percent(memQuantity.doubleValue(), totalQuantity.doubleValue(), 2));
					// 新会员数量占比
					counterMap.put("newMemQuantityRate", CherryUtil.percent(newMemQuantity.doubleValue(), totalQuantity.doubleValue(), 2));
					// 老会员数量占比
					counterMap.put("oldMemQuantityRate", CherryUtil.percent(oldMemQuantity.doubleValue(), totalQuantity.doubleValue(), 2));
					// 非会员数量占比
					counterMap.put("nonMemQuantityRate", CherryUtil.percent(nonMemQuantity.doubleValue(), totalQuantity.doubleValue(), 2));
				}
				if(totalAmount.doubleValue() > 0) {
					// 会员金额占比
					counterMap.put("memAmountRate", CherryUtil.percent(memAmount.doubleValue(), totalAmount.doubleValue(), 2));
					// 新会员金额占比
					counterMap.put("newMemAmountRate", CherryUtil.percent(newMemAmount.doubleValue(), totalAmount.doubleValue(), 2));
					// 老会员金额占比
					counterMap.put("oldMemAmountRate", CherryUtil.percent(oldMemAmount.doubleValue(), totalAmount.doubleValue(), 2));
					// 非会员金额占比
					counterMap.put("nonMemAmountRate", CherryUtil.percent(nonMemAmount.doubleValue(), totalAmount.doubleValue(), 2));
				}
				if(totalSaleCount > 0) {
					// 会员单数占比
					counterMap.put("memSaleCountRate", CherryUtil.percent(memSaleCount, totalSaleCount, 2));
					// 新会员单数占比
					counterMap.put("newMemSaleCountRate", CherryUtil.percent(newMemSaleCount, totalSaleCount, 2));
					// 老会员单数占比
					counterMap.put("oldMemSaleCountRate", CherryUtil.percent(oldMemSaleCount, totalSaleCount, 2));
					// 非会员单数占比
					counterMap.put("nonMemSaleCountRate", CherryUtil.percent(nonMemSaleCount, totalSaleCount, 2));
				}
			}
			
			// 设置合计列
			Map<String, Object> counterMap = new HashMap<String, Object>();
			counterMap.put("number", counterList.size()+1);
			String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
			counterMap.put("counterName", binOLCM37_BL.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_total"));
			
			// 销售总金额
			counterMap.put("totalAmount", totalAmountSum);
			// 销售总数量
			counterMap.put("totalQuantity", totalQuantitySum);
			// 销售总单数
			counterMap.put("totalSaleCount", totalSaleCountSum);
			// 会员购买金额
			counterMap.put("memAmount", memAmountSum);
			// 会员购买数量
			counterMap.put("memQuantity", memQuantitySum);
			// 会员购买单数
			counterMap.put("memSaleCount", memSaleCountSum);
			// 非会员购买金额
			counterMap.put("nonMemAmount", nonMemAmountSum);
			// 非会员购买数量
			counterMap.put("nonMemQuantity", nonMemQuantitySum);
			// 非会员购买单数
			counterMap.put("nonMemSaleCount", nonMemSaleCountSum);
			// 新会员购买金额
			counterMap.put("newMemAmount", newMemAmountSum);
			// 新会员购买数量
			counterMap.put("newMemQuantity", newMemQuantitySum);
			// 新会员购买单数
			counterMap.put("newMemSaleCount", newMemSaleCountSum);
			// 老会员购买金额
			counterMap.put("oldMemAmount", oldMemAmountSum);
			// 老会员购买数量
			counterMap.put("oldMemQuantity", oldMemQuantitySum);
			// 老会员购买单数
			counterMap.put("oldMemSaleCount", oldMemSaleCountSum);
			// 会员购买人数
			counterMap.put("memSaleNumber", memSaleNumberSum);
			// 新会员购买人数
			counterMap.put("newMemSaleNumber", newMemSaleNumberSum);
			// 非会员购买人数
			counterMap.put("nonMemSaleNumber", nonMemSaleNumberSum);
			// 老会员购买人数
			counterMap.put("oldMemSaleNumber", oldMemSaleNumberSum);
			// 总购买人数
			counterMap.put("totalSaleNumber", totalSaleNumberSum);
			// 会员人数
			counterMap.put("memNumber", memNumberSum);
			// 新会员人数
			counterMap.put("newMemNumber", newMemNumberSum);
			
			if(memNumberSum > 0) {
				// 会员购买人数占比
				counterMap.put("memSaleNumberRate", CherryUtil.percent(memSaleNumberSum, memNumberSum, 2));
				// 新会员购买人数占比
				counterMap.put("newMemSaleNumberRate", CherryUtil.percent(newMemSaleNumberSum, memNumberSum, 2));
				// 老会员购买人数占比
				counterMap.put("oldMemSaleNumberRate", CherryUtil.percent(oldMemSaleNumberSum, memNumberSum, 2));
			}
			
			if(totalSaleCountSum > 0) {
				// 客单价
				counterMap.put("unitPrice", CherryUtil.div(totalAmountSum.doubleValue(), totalSaleCountSum, 2));
				// 连带率
				counterMap.put("joinRate", CherryUtil.div(totalQuantitySum.doubleValue(), totalSaleCountSum, 2));
			}
			if(newMemSaleCountSum > 0) {
				// 新会员客单价
				counterMap.put("newMemUnitPrice", CherryUtil.div(newMemAmountSum.doubleValue(), newMemSaleCountSum, 2));
				// 新会员连带率
				counterMap.put("newMemJoinRate", CherryUtil.div(newMemQuantitySum.doubleValue(), newMemSaleCountSum, 2));
			}
			if(oldMemSaleCountSum > 0) {
				// 老会员客单价
				counterMap.put("oldMemUnitPrice", CherryUtil.div(oldMemAmountSum.doubleValue(), oldMemSaleCountSum, 2));
				// 老会员连带率
				counterMap.put("oldMemJoinRate", CherryUtil.div(oldMemQuantitySum.doubleValue(), oldMemSaleCountSum, 2));
			}
			if(nonMemSaleCountSum > 0) {
				// 非会员客单价
				counterMap.put("nonMemUnitPrice", CherryUtil.div(nonMemAmountSum.doubleValue(), nonMemSaleCountSum, 2));
				// 非会员连带率
				counterMap.put("nonMemJoinRate", CherryUtil.div(nonMemQuantitySum.doubleValue(), nonMemSaleCountSum, 2));
			}
			if(totalQuantitySum.doubleValue() > 0) {
				// 会员数量占比
				counterMap.put("memQuantityRate", CherryUtil.percent(memQuantitySum.doubleValue(), totalQuantitySum.doubleValue(), 2));
				// 新会员数量占比
				counterMap.put("newMemQuantityRate", CherryUtil.percent(newMemQuantitySum.doubleValue(), totalQuantitySum.doubleValue(), 2));
				// 老会员数量占比
				counterMap.put("oldMemQuantityRate", CherryUtil.percent(oldMemQuantitySum.doubleValue(), totalQuantitySum.doubleValue(), 2));
				// 非会员数量占比
				counterMap.put("nonMemQuantityRate", CherryUtil.percent(nonMemQuantitySum.doubleValue(), totalQuantitySum.doubleValue(), 2));
			}
			if(totalAmountSum.doubleValue() > 0) {
				// 会员金额占比
				counterMap.put("memAmountRate", CherryUtil.percent(memAmountSum.doubleValue(), totalAmountSum.doubleValue(), 2));
				// 新会员金额占比
				counterMap.put("newMemAmountRate", CherryUtil.percent(newMemAmountSum.doubleValue(), totalAmountSum.doubleValue(), 2));
				// 老会员金额占比
				counterMap.put("oldMemAmountRate", CherryUtil.percent(oldMemAmountSum.doubleValue(), totalAmountSum.doubleValue(), 2));
				// 非会员金额占比
				counterMap.put("nonMemAmountRate", CherryUtil.percent(nonMemAmountSum.doubleValue(), totalAmountSum.doubleValue(), 2));
			}
			if(totalSaleCountSum > 0) {
				// 会员单数占比
				counterMap.put("memSaleCountRate", CherryUtil.percent(memSaleCountSum, totalSaleCountSum, 2));
				// 新会员单数占比
				counterMap.put("newMemSaleCountRate", CherryUtil.percent(newMemSaleCountSum, totalSaleCountSum, 2));
				// 老会员单数占比
				counterMap.put("oldMemSaleCountRate", CherryUtil.percent(oldMemSaleCountSum, totalSaleCountSum, 2));
				// 非会员单数占比
				counterMap.put("nonMemSaleCountRate", CherryUtil.percent(nonMemSaleCountSum, totalSaleCountSum, 2));
			}
			if(dateMode != null && "0".equals(dateMode)) {
				counterMap.put("newMemNumberPre", newMemNumberPreSum);
				if(newMemNumberPreSum > 0) {
					// 会员增长率
					counterMap.put("memNumberRate", CherryUtil.percent(newMemNumberSum-newMemNumberPreSum, newMemNumberPreSum, 2));
				}
			}
			counterList.add(counterMap);
			
			return counterList;
		} else {
			return null;
		}
	}
	
	/**
	 * 把树型结构转换成列表结构
	 * 
	 * @param treeList 待转换的树型结构
	 * @param list 转换后的列表结构
	 * @param tempMap 临时存放数据map
	 */
	public void convertTreeToList(List<Map<String, Object>> treeList, List<Map<String, Object>> list, Map<String, Object> tempMap) {
		for(int i = 0; i < treeList.size(); i++) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.putAll(tempMap);
			Map<String, Object> map = treeList.get(i);
			Object type = map.get("type");
			if(type != null && !"".equals(type.toString())) {
				newMap.put("id_"+type.toString(), map.get("id"));
				newMap.put("name_"+type.toString(), map.get("name"));
			}
			if(type != null && "4".equals(type.toString())) {
				list.add(newMap);
			} else {
				List<Map<String, Object>> nextList = (List)map.get("nodes");
				if(nextList != null && !nextList.isEmpty()) {
					convertTreeToList(nextList, list, newMap);
				}
			}
		}
	}
	
	private String getConditionStr(Map<String, Object> map) {
		
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String dateMode = (String)map.get("dateMode");
    	if(dateMode != null && "0".equals(dateMode)) {
    		String paramName = CherryUtil.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_dateMode0");
    		String fiscalYear = (String)map.get("fiscalYear");
    		String fiscalMonth = (String)map.get("fiscalMonth");
    		if(fiscalMonth.length() == 1) {
    			fiscalMonth = "0"+fiscalMonth;
    		}
    		String paramValue = fiscalYear + fiscalMonth;
    		condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
    	} else {
    		String paramName = CherryUtil.getResourceValue("BINOLMBRPT01", language, "binolmbrpt01_dateMode1");
    		String saleDateStart = (String)map.get("saleDateStart");
    		String saleDateEnd = (String)map.get("saleDateEnd");
    		String paramValue = saleDateStart + "-" + saleDateEnd;
    		condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
    	}
		
		String channelName = (String)map.get("channelName");
		if(channelName != null && !"".equals(channelName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT01", language, "binolmbrpt01_channelId");
			String paramValue = channelName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String organizationName = (String)map.get("organizationName");
		if(organizationName != null && !"".equals(organizationName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT01", language, "binolmbrpt01_organizationId");
			String paramValue = organizationName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}

/*	
 * @(#)BINOLPTRPS09_BL.java     1.0 2011/03/09		
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
package com.cherry.pt.rps.bl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS09_IF;
import com.cherry.pt.rps.service.BINOLPTRPS09_Service;

/**
 * 出入库记录查询BL
 * 
 * @author lipc
 * @version 1.0 2011.03.09
 * 
 */
public class BINOLPTRPS09_BL implements BINOLPTRPS09_IF, BINOLCM37_IF {

	@Resource(name="binOLPTRPS09_Service")
	private BINOLPTRPS09_Service binolptrps09Service;
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "regionId", "provinceId",
			"cityId", "countyId", "channelId", "depotId", "lgcInventoryId",
			"departType", "departId", "tradeNo", "relevanceNo", "tradeType",
			"verifiedFlag", "nameTotal", "businessDate" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
			{ "tradeNo", "RPS09_tradeNo", "40", "", "" },// 入出库单
			{ "relevanceNo", "RPS09_relevanceNo", "40", "", "" },// 关联单号
			{ "departCode", "RPS09_departCode", "15", "", "" },// 入出库部门号
			{ "departName", "RPS09_departName", "15", "", "" },// 入出库部门
			{ "depotCode", "RPS09_depotCode", "15", "", "" },// 实体仓库号
			{ "depotName", "RPS09_depotName", "15", "", "" },// 实体仓库
			{ "inventoryCode", "RPS09_inventoryCode", "15", "", "" },// 逻辑仓库号
			{ "inventoryName", "RPS09_inventoryName", "15", "", "" },// 逻辑仓库
			{ "employeeCode", "RPS09_employeeCode", "", "", "" },// 操作员号
			{ "employeeName", "RPS09_employeeName", "", "", "" },// 操作员
			{ "unitCode", "RPS09_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "RPS09_barCode", "15", "", "" },// 产品条码
			{ "productName", "RPS09_prtName", "20", "", "" },// 产品名称
			{ "quantity", "RPS09_totalQuantity", "", "", "" },// 数量
			{ "moduleCode", "RPS09_moduleCode", "", "", "1190" },// 计量单位
			{ "amount", "RPS09_totalAmount", "", "", "" },// 金额
			{ "stockType", "RPS09_stockType", "10", "", "1025" },// 入出库状态
			{ "tradeType", "RPS09_tradeType", "10", "", "1263" },// 业务类型
			{ "verifiedFlag", "RPS09_verifiedFlag", "10", "", "1007" },// 审核状态
			{ "date", "RPS09_inOutDate", "10", "", "" }, // 入出库日期
			{ "dateTime", "RPS09_time", "10", "", "" }, // 入出库时间
			{ "comments", "RPS09_comments", "10", "", "" } // 备注
	};
	
	@Override
	public int getProInOutCount(Map<String, Object> map) {

		return binolptrps09Service.getProInOutCount(map);
	}

	@Override
	public List<Map<String, Object>> getProInOutList(Map<String, Object> map) {
		
		return binolptrps09Service.getProInOutList(map);
	}
	/**
	 * 出入库记录汇总信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
	
		return binolptrps09Service.getSumInfo(map);
	}

	/**
	 * 导出excel处理
	 * 
	 */
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binolptrps09Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS09.getExportDetailList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS09");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}

	/**
	 * 查询参数内容
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		// 防止查询相关区域、城市、部门名称时篡改ibatis_sql_id
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		String language = ConvertUtil.getString(conditionMap
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		int lineNum = 0;
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(conditionMap.get(con));
			if (con.equals("businessDate")) {// 业务日期始终显示
				paramValue = "showBusinessDate";
			}
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("tradeNo".equals(con)) {
					// 入出库单
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS09", language, "RPS09_tradeNo");
				} else if ("relevanceNo".equals(con)) {
					// 关联单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS09", language, "RPS09_relevanceNo");
				} else if ("businessDate".equals(con)) {
					// 日期范围
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS09", language, "RPS09_date");
					// 日期
					String startDate = (conditionMap.get("startDate") != null) ? ConvertUtil
							.getString(conditionMap.get("startDate")).replace('-', '/')
							: "----/--/--";
					String endDate = (conditionMap.get("endDate") != null) ? ConvertUtil
							.getString(conditionMap.get("endDate")).replace('-', '/')
							: "----/--/--";
					paramValue = startDate + "\0~\0" + endDate;
				} else if ("tradeType".equals(con)) {
					// 业务类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS09", language, "RPS09_tradeType");
					paramValue = codeTable.getVal("1263", paramValue);
				} else if ("verifiedFlag".equals(con)) {
					// 审核状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS09", language, "RPS09_verifiedFlag");
					paramValue = codeTable.getVal("1007", paramValue);
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS09", language, "RPS09_prtName");
				} else if ("departType".equals(con)) {
					// 部门类型
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.departType");
					//组织类型参数为list
					List<String> paramValueTemp = (List<String>) conditionMap.get(con);
					StringBuffer sb = new StringBuffer();
					for(String str : paramValueTemp){
						sb.append(codeTable.getVal("1000", str)+"|");
					}
					//去掉最后一个"|"
					paramValue = sb.substring(0, sb.length()-1);
				} else if ("channelId".equals(con)) {
					// 渠道
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.channel");
					paramValue = binOLCM00_Service.getChannelName(conditionMap);
				} else if ("depotId".equals(con)) {
					// 仓库
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depot");
					paramValue = binOLCM00_Service.getDepotName(conditionMap);
				} else if ("lgcInventoryId".equals(con)) {
					// 逻辑仓库
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.logicInventory");
					paramValue = binOLCM00_Service.getLogicInventoryName(conditionMap);
				} else if ("departId".equals(con)) {
					// 部门
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depart");
					paramValue = binOLCM00_Service.getDepartName(conditionMap);
				} else {
					// 区域
					String text = con.substring(0, con.indexOf("Id"));
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + text);
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put(CherryConstants.SESSION_LANGUAGE, language);
					temp.put("regionId", conditionMap.get(con));
					paramValue = binOLCM00_Service.getRegionName(temp);
				}
				lineNum++;
				if (lineNum % 6 == 0) {
					condition.append("\n");
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}

		return condition.toString();
	}

	/**
	 * 导出CSV处理
	 */
	@Override
	public String exportCSV(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportParam(map);
		
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_exportName");
        exportMap.put("tempFileName", downloadFileName);
        
        // 导出CSV处理
        boolean result = binOLCM37_BL.exportCSV(exportMap, this);
        if(result) {
        	// 压缩包名
        	String zipName = downloadFileName+".zip";
        	// 压缩文件处理
        	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+downloadFileName+".csv"), zipName);
        	if(result) {
        		return tempFilePath+File.separator+zipName;
        	}
        }
		return null;
	}

	/**
	 * CSV导出参数
	 * @param map
	 * @return
	 */
	private Map<String, Object> getExportParam(Map<String, Object> map) {
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		// 把搜索条件转换为文字说明
		String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
		
		exportMap.put("conditionMap", map);
		
		exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_exportName"));
		String[][] titleRows = {
				{ "tradeNo", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_tradeNo"), "40", "", "" },// 入出库单
				{ "relevanceNo", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_relevanceNo"), "40", "", "" },// 关联单号
				{ "departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_departCode"), "15", "", "" },// 入出库部门号
				{ "departName", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_departName"), "15", "", "" },// 入出库部门
				{ "depotCode", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_depotCode"), "15", "", "" },// 实体仓库号
				{ "depotName", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_depotName"), "15", "", "" },// 实体仓库
				{ "inventoryCode", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_inventoryCode"), "15", "", "" },// 逻辑仓库号
				{ "inventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_inventoryName"), "15", "", "" },// 逻辑仓库
				{ "employeeCode", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_employeeCode"), "", "", "" },// 操作员
				{ "employeeName", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_employeeName"), "", "", "" },// 操作员
				{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_unitCode"), "15", "", "" },// 厂商编码
				{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_barCode"), "15", "", "" },// 产品条码
				{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_prtName"), "20", "", "" },// 产品名称
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_totalQuantity"), "", "", "" },// 数量
				{ "moduleCode", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_moduleCode"), "", "", "1190" },// 计量单位
				{ "amount", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_totalAmount"), "", "", "" },// 金额
				{ "stockType", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_stockType"), "15", "", "1025" },// 入出库状态
				{ "tradeType", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_tradeType"), "10", "", "1263" },// 业务类型
				{ "verifiedFlag", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_verifiedFlag"), "10", "", "1007" },// 审核状态
				{ "date", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_inOutDate"), "10", "", "" }, // 入出库日期
				{ "dateTime", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_time"), "10", "", "" }, // 入出库时间
				{ "comments", binOLCM37_BL.getResourceValue("BINOLPTRPS09", language, "RPS09_comments"), "10", "", "" } // 备注	
		};
		exportMap.put("titleRows", titleRows);
		
		return exportMap;
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binolptrps09Service.getExportDetailList(map);
	}

	@Override
	public int getProInOutDetailCount(Map<String, Object> map) {
		return binolptrps09Service.getExportDetailCount(map);
	}
}

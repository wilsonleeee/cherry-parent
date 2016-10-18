/*	
 * @(#)BINOLPTRPS05_BL.java     1.0 2010/11/25		
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
import com.cherry.pt.rps.interfaces.BINOLPTRPS05_IF;
import com.cherry.pt.rps.service.BINOLPTRPS05_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 产品调出单查询BL
 * 
 * @author weisc
 * @version 1.0 2010.11.25
 * 
 */
public class BINOLPTRPS05_BL extends SsBaseBussinessLogic implements BINOLPTRPS05_IF, BINOLCM37_IF{

	@Resource(name="binOLPTRPS05_Service")
	private BINOLPTRPS05_Service binOLPTRPS05_Service;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	
	@Resource(name="CodeTable")
	private CodeTable CodeTable;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "regionId", "provinceId",
			"cityId", "countyId", "channelId", "depotId", "lgcInventoryId",
			"departType", "departId", "allocationNo", "relevanceNo", "allocationType",
			"verifiedFlag", "nameTotal", "businessDate" };
	
	// Excel导出列数组
	private final static String[][] proArrayOut = {
			{ "allocationNo", "RPS05_allocationNo", "40", "", "" },// 入出库单
			{ "bin_OrganizationIDOut", "RPS05_sendOrgCode", "15", "", "" },// 调出部门编码
			{ "sendDepart", "RPS05_sendOrgName", "20", "", "" },// 调出部门名称
			{ "bin_OrganizationIDIn", "RPS05_receiveOrgCode", "15", "", "" },// 调入部门编码
			{ "receiveDepart", "RPS05_receiveOrgName", "20", "", "" },// 调入部门名称
			{ "inventoryCode", "RPS05_inventoryCodeOut", "20", "", "" },// 调出实体仓库编码
			{ "inventoryName", "RPS05_inventoryNameOut", "20", "", "" },// 调出实体仓库名称
			{ "logicInventoryCode", "RPS05_logicInventoryCodeOut", "20", "", "" },// 调出逻辑仓库编码
			{ "logicInventoryName", "RPS05_logicInventoryNameOut", "20", "", "" },// 调出逻辑仓库名称
			{ "employeeCode", "RPS05_empCode", "15", "", "" },// 人员编码
			{ "employeeName", "RPS05_empName", "15", "", "" },// 人员名称
			{ "unitCode", "RPS05_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "RPS05_barCode", "15", "", "" },// 产品条码
			{ "productName", "RPS05_productName", "20", "", "" },// 产品名称
			{ "quantity", "RPS05_totalQuantity", "", "", "" },// 数量
			{ "amount", "RPS05_totalAmount", "", "", "" },// 金额
			{ "verifiedFlag", "RPS05_verifiedFlag", "10", "", "1007" },// 审核状态
			{ "allocationDate", "RPS05_dateOut", "15", "", "" }, // 调出日期
			{ "comments", "RPS05_comments", "10", "", "" } // 备注
	};
	// Excel导出列数组
	private final static String[][] proArrayIn = {
			{ "allocationNo", "RPS05_allocationNo", "40", "", "" },// 入出库单
			{ "bin_OrganizationIDIn", "RPS05_receiveOrgCode", "15", "", "" },// 调入部门编码
			{ "receiveDepart", "RPS05_receiveOrgName", "20", "", "" },// 调入部门名称
			{ "bin_OrganizationIDOut", "RPS05_sendOrgCode", "15", "", "" },// 调出部门编码
			{ "sendDepart", "RPS05_sendOrgName", "20", "", "" },// 调出部门名称
			{ "inventoryCode", "RPS05_inventoryCodeIn", "20", "", "" },// 调入实体仓库编码
			{ "inventoryName", "RPS05_inventoryNameIn", "20", "", "" },// 调入实体仓库名称
			{ "logicInventoryCode", "RPS05_logicInventoryCodeIn", "20", "", "" },// 调入逻辑仓库编码
			{ "logicInventoryName", "RPS05_logicInventoryNameIn", "20", "", "" },// 调入逻辑仓库名称
			{ "employeeCode", "RPS05_empCode", "15", "", "" },// 人员编码
			{ "employeeName", "RPS05_empName", "15", "", "" },// 人员名称
			{ "unitCode", "RPS05_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "RPS05_barCode", "15", "", "" },// 产品条码
			{ "productName", "RPS05_productName", "20", "", "" },// 产品名称
			{ "quantity", "RPS05_totalQuantity", "", "", "" },// 数量
			{ "amount", "RPS05_totalAmount", "", "", "" },// 金额
			{ "verifiedFlag", "RPS05_verifiedFlag", "10", "", "1007" },// 审核状态
			{ "allocationDate", "RPS05_dateIn", "15", "", "" }, // 调入日期
			{ "comments", "RPS05_comments", "10", "", "" } // 备注
	};


	/**
	 * 取得调拨记录总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getAllocationCount(Map<String, Object> map) {
		return binOLPTRPS05_Service.getAllocationCount(map);
	}

	/**
	 * 取得调拨记录LIST
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		return binOLPTRPS05_Service.getAllocationList(map);
	}

	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLPTRPS05_Service.getSumInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		// 调拨类型
		String allocationType =  ConvertUtil.getString(map.get("allocationType"));
		//需导出数据总量
		int dataLen = binOLPTRPS05_Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS05.getExportDetailList");
		ep.setMap(map);
		// 导出数据列数组
		if("1".equals(allocationType)) {
			ep.setArray(proArrayOut);
		} else if("2".equals(allocationType)) {
			ep.setArray(proArrayIn);
		}
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS05");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
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
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_exportName");
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
     * 获取导出参数
     */
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		
		
		// 把会员搜索条件转换为文字说明
		String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_exportName"));
        // 调拨类型
     	String allocationType =  ConvertUtil.getString(map.get("allocationType"));
     	if("1".equals(allocationType)) {
	        String[][] titleRows = {
	        		{ "allocationNo", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_allocationNo"), "40", "", "" },// 入出库单
	    			{ "bin_OrganizationIDOut", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_sendOrgCode"), "15", "", "" },// 调出部门编码
	    			{ "sendDepart", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_sendOrgName"), "20", "", "" },// 调出部门名称
	    			{ "bin_OrganizationIDIn", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_receiveOrgCode"), "15", "", "" },// 调入部门编码
	    			{ "receiveDepart", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_receiveOrgName"), "20", "", "" },// 调入部门名称
	    			{ "inventoryCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_inventoryCodeOut"), "15", "", "" },// 调出实体仓库编码
	    			{ "inventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_inventoryNameOut"), "20", "", "" },// 调出实体仓库名称
	    			{ "logicInventoryCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_logicInventoryCodeOut"), "15", "", "" },// 调出逻辑仓库编码
	    			{ "logicInventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_logicInventoryNameOut"), "20", "", "" },// 调出实体仓库名称
	    			{ "employeeCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_empCode"), "15", "", "" },// 人员编码
	    			{ "employeeName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_empName"), "15", "", "" },// 人员名称
	    			{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_unitCode"), "15", "", "" },// 厂商编码
	    			{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_barCode"), "15", "", "" },// 产品条码
	    			{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_productName"), "20", "", "" },// 产品名称
	    			{ "quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_totalQuantity"), "", "", "" },// 数量
	    			{ "amount", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_totalAmount"), "", "", "" },// 金额
	    			{ "verifiedFlag", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_verifiedFlag"), "10", "", "1007" },// 审核状态
	    			{ "allocationDate", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_dateOut"), "15", "", "" }, // 调出日期
	    			{ "comments", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_comments"), "10", "", "" } // 备注
	    	};
	        exportMap.put("titleRows", titleRows);
     	} else if("2".equals(allocationType)) {
     		String[][] titleRows = {
	        		{ "allocationNo", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_allocationNo"), "40", "", "" },// 入出库单
	    			{ "bin_OrganizationIDIn", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_receiveOrgCode"), "15", "", "" },// 调入部门编码
	    			{ "receiveDepart", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_receiveOrgName"), "20", "", "" },// 调入部门名称
	    			{ "bin_OrganizationIDOut", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_sendOrgCode"), "15", "", "" },// 调出部门编码
	    			{ "sendDepart", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_sendOrgName"), "20", "", "" },// 调出部门名称
	    			{ "inventoryCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_inventoryCodeIn"), "15", "", "" },// 调入实体仓库编码
	    			{ "inventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_inventoryNameIn"), "20", "", "" },// 调入实体仓库名称
	    			{ "logicInventoryCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_logicInventoryCodeIn"), "15", "", "" },// 调入逻辑仓库编码
	    			{ "logicInventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_logicInventoryNameIn"), "20", "", "" },// 调入实体仓库名称
	    			{ "employeeCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_empCode"), "15", "", "" },// 人员编码
	    			{ "employeeName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_empName"), "15", "", "" },// 人员名称
	    			{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_unitCode"), "15", "", "" },// 厂商编码
	    			{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_barCode"), "15", "", "" },// 产品条码
	    			{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_productName"), "20", "", "" },// 产品名称
	    			{ "quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_totalQuantity"), "", "", "" },// 数量
	    			{ "amount", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_totalAmount"), "", "", "" },// 金额
	    			{ "verifiedFlag", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_verifiedFlag"), "10", "", "1007" },// 审核状态
	    			{ "allocationDate", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_dateIn"), "15", "", "" }, // 调入日期
	    			{ "comments", binOLCM37_BL.getResourceValue("BINOLPTRPS05", language, "RPS05_comments"), "10", "", "" } // 备注
	    	};
     		exportMap.put("titleRows", titleRows);
     	}
        
        return exportMap;
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
				if ("allocationNo".equals(con)) {
					// 入出库单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS05", language, "RPS05_allocationNo");
				} else if ("businessDate".equals(con)) {
					// 日期范围
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS05", language, "RPS05_allocationDate");
					// 日期
					String startDate = (conditionMap.get("startDate") != null) ? ConvertUtil
							.getString(conditionMap.get("startDate")).replace('-', '/')
							: "----/--/--";
					String endDate = (conditionMap.get("endDate") != null) ? ConvertUtil
							.getString(conditionMap.get("endDate")).replace('-', '/')
							: "----/--/--";
					paramValue = startDate + "\0~\0" + endDate;
				} else if ("allocationType".equals(con)) {
					// 调拨类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS05", language, "RPS05_allocationType");
					paramValue = CodeTable.getVal("1265", paramValue);
				} else if ("verifiedFlag".equals(con)) {
					// 审核状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS05", language, "RPS05_verifiedFlag");
					paramValue = CodeTable.getVal("1007", paramValue);
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS05", language, "RPS05_productName");
				} else if ("departType".equals(con)) {
					// 部门类型
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.departType");
					//组织类型参数为list
					List<String> paramValueTemp = (List<String>) conditionMap.get(con);
					StringBuffer sb = new StringBuffer();
					for(String str : paramValueTemp){
						sb.append(CodeTable.getVal("1000", str)+"|");
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

	@Override
	public int getExportDetailCount(Map<String, Object> map) {
		return binOLPTRPS05_Service.getExportDetailCount(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLPTRPS05_Service.getExportDetailList(map);
	}
}

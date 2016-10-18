/*  
 * @(#)BINOLSTBIL17_BL.java     1.0 2012/11/28      
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
package com.cherry.st.bil.bl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.bil.interfaces.BINOLSTBIL17_IF;
import com.cherry.st.bil.service.BINOLSTBIL17_Service;

/**
 * 
 * 产品调拨申请单详细BL
 * @author niushunjie
 * @version 1.0 2012.11.28
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL17_BL implements BINOLSTBIL17_IF,BINOLCM37_IF{
    
	@Resource(name="binOLSTBIL17_Service")
	private BINOLSTBIL17_Service binOLSTBIL17_Service;
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "allocationrNo", "nameTotal",
			"departType", "regionId", "provinceId", "cityId", "countyId",
			"channelId", "departId", "depotId", "tradeStatus", "verifiedFlag",
			"operateDate" };

	// Excel导出列数组
	private final static String[][] proArray = {
			{ "area", "BIL17_area", "", "", "" },// 大区
			{ "region", "BIL17_region", "", "", "" },// 区域
			{ "province", "BIL17_province", "", "", "" },// 省份
			{ "city", "BIL17_city", "", "", "" },// 城市
			{ "allocationrNo", "BIL17_allocationrNo", "30", "", "" },// 单据号
			{ "relevanceNo", "BIL17_relevanceNo", "30", "", "" },// 关联单据号
			{ "departCode", "BIL17_departCode", "15", "", "" },// 申请部门代码
			{ "departName", "BIL17_departNameExp", "20", "", "" },// 申请部门名称
			{ "employeeCode", "BIL17_empCode", "15", "", "" },// 操作人员代号
			{ "employeeName", "BIL17_empName", "15", "", "" },// 操作人员名称
			{ "depotCode", "BIL17_depotCode", "15", "", "" },// 实体仓库代码
			{ "depotName", "BIL17_depotName", "20", "", "" },// 实体仓库名称
			{ "logicInventoryCode", "BIL17_logicInventoryCode", "20", "", "" },// 逻辑仓库代号
			{ "logicInventoryName", "BIL17_logicInventoryName", "20", "", "" },// 逻辑仓库名称
			{ "departCodeOut", "BIL17_departCodeOut", "15", "", "" },// 申请部门代码
			{ "departNameOut", "BIL17_departNameOutExp", "20", "", "" },// 申请部门名称
			{ "employeeCodeAudit", "BIL17_employeeCodeAudit", "15", "", "" },// 审核者代号
			{ "employeeNameAudit", "BIL17_employeeNameAudit", "15", "", "" },// 审核者名称
			{ "unitCode", "BIL17_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "BIL17_barCode", "15", "", "" },// 产品条码
			{ "productName", "BIL17_productName", "20", "", "" },// 产品名称
			{ "detailQuantity", "BIL17_detailQuantity", "", "right", "" },// 实盘数量
			{ "detailPrice", "BIL17_detailPrice", "", "right", "" },// 价格
			{ "detailAmount", "BIL17_detailAmount", "", "right", "" },// 盘差金额
			{ "tradeStatus", "BIL17_tradeStatus", "", "", "1200" },// 处理状态
			{ "verifiedFlag", "BIL17_verifiedFlag", "15", "", "1007" },// 审核区分
			{ "date", "BIL17_tradeDate", "15", "", "" }, // 调拨申请日期
			{ "comments", "BIL17_comments", "20", "", "" } // 备注
	};
	
	/**
	 * 取得调拨申请单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchProductAllocationCount(Map<String, Object> map) {
		// 取得调拨申请单总数
		return binOLSTBIL17_Service.getProductAllocationCount(map);
	}

	/**
	 * 取得调拨申请单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchProductAllocationList(Map<String, Object> map) {
		// 取得调拨申请单List
		return binOLSTBIL17_Service.getProductAllocationList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSTBIL17_Service.getSumInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLSTBIL17_Service.getExportDetailCount(map);
		if(dataLen <= 0){
			throw new CherryException("EMO00022");
		}
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "allocationrNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL17.getPrtAllocationExportList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSTBIL17");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
	 * excel导出的头部查询条件信息
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
			if (con.equals("operateDate")) {// 申请日期始终显示
				paramValue = "showDate";
			}
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("allocationrNo".equals(con)) {
					// 申请单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL17", language, "BIL17_allocationrNo");
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL17", language, "BIL17_productName");
				} else if ("operateDate".equals(con)) {
					// 申请日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL17", language, "BIL17_date");
					// 日期
					String startDate = (map.get("startDate") != null) ? ConvertUtil
							.getString(map.get("startDate")).replace('-', '/')
							: "----/--/--";
					String endDate = (map.get("endDate") != null) ? ConvertUtil
							.getString(map.get("endDate")).replace('-', '/')
							: "----/--/--";
					paramValue = startDate + "\0~\0" + endDate;
				} else if ("verifiedFlag".equals(con)) {
					// 审核状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL17", language, "BIL17_verifiedFlag");
					paramValue = codeTable.getVal("1007", paramValue);
				} else if ("tradeStatus".equals(con)) {
					// 处理状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL17", language, "BIL17_tradeStatus");
					paramValue = codeTable.getVal("1200", paramValue);
				} else if ("departType".equals(con)) {
					// 部门类型
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.departType");
					//组织类型参数为list
					List<String> paramValueTemp = (List<String>) map.get(con);
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
				} else if ("departId".equals(con)) {
					// 部门
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depart");
					paramValue = binOLCM00_Service.getDepartName(conditionMap);
				} else if ("depotId".equals(con)) {
					// 实体仓库
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depot");
					paramValue = binOLCM00_Service.getDepotName(conditionMap);
				} else {
					// 区域
					String text = con.substring(0, con.indexOf("Id"));
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + text);
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put(CherryConstants.SESSION_LANGUAGE, language);
					temp.put("regionId", map.get(con));
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
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLSTBIL17", language, "BIL17_exportName");
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
        
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLSTBIL17", language, "STBIL17_exportName"));
        String[][] titleRows = {
                { "area", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_area"), "", "", "" },// 大区
                { "region", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_region"), "", "", "" },// 区域
                { "province", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_province"), "", "", "" },// 省份
                { "city", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_city"), "", "", "" },// 城市
                { "allocationrNo", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_allocationrNo"), "30", "", "" },// 单据号
                { "relevanceNo", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_relevanceNo"), "30", "", "" },// 关联单据号
                { "departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_departCode"), "15", "", "" },// 申请部门代码
                { "departName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_departNameExp"), "20", "", "" },// 申请部门名称
                { "employeeCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_empCode"), "15", "", "" },// 操作人员代号
                { "employeeName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_empName"), "15", "", "" },// 操作人员名称
                { "depotCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_depotCode"), "", "15", "" },// 实体仓库代码
                { "depotName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_depotName"), "20", "", "" },// 实体仓库名称
                { "logicInventoryCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_logicInventoryCode"), "20", "", "" },// 逻辑仓库代号
                { "logicInventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_logicInventoryName"), "20", "", "" },// 逻辑仓库名称
                { "departCodeOut", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_departCodeOut"), "15", "", "" },// 调出部门代码
                { "departNameOut", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_departNameOutExp"), "20", "", "" }, // 调出部门名称
                { "employeeCodeAudit", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_employeeCodeAudit"), "15", "", "" }, // 审核者代号
                { "employeeNameAudit", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_employeeNameAudit"), "15", "", "" }, // 审核者名称
                { "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_unitCode"), "15", "", "" }, // 厂商编码
                { "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_barCode"), "15", "", "" }, // 产品条码
                { "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_productName"), "20", "", "" }, // 产品名称
                { "detailQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_detailQuantity"), "", "right", "" }, // 实盘数量
                { "detailPrice", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_detailPrice"), "", "", "" }, // 价格
                { "detailAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_detailAmount"), "", "right", "" }, // 金额
                { "tradeStatus", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_tradeStatus"), "", "", "1200" }, // 处理状态
                { "verifiedFlag", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_verifiedFlag"), "15", "", "1007" }, // 审核区分
                { "date", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_tradeDate"), "15", "", "" }, // 调拨申请日期
                { "comments", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "BIL17_comments"), "20", "", "" }, // 备注
        };
        exportMap.put("titleRows", titleRows);
        
        return exportMap;
    }

    @Override
    public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
        return binOLSTBIL17_Service.getPrtAllocationExportList(map);
    }
    
    @Override
    public int getExportDetailCount(Map<String, Object> map) {
        return binOLSTBIL17_Service.getExportDetailCount(map);
    }
}
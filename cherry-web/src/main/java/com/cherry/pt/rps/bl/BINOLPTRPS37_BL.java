/*	
 * @(#)BINOLPTRPS37_BL.java     1.0 2015-1-21 	
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
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS37_IF;
import com.cherry.pt.rps.service.BINOLPTRPS37_Service;

/**
 * 
 * @ClassName: BINOLPTRPS37_BL 
 * @Description: TODO(实时库存预警BL) 
 * @author menghao
 * @version v1.0.0 2015-1-21 
 *
 */
public class BINOLPTRPS37_BL implements BINOLPTRPS37_IF {
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	
	@Resource(name="binOLPTRPS37_Service")
	private BINOLPTRPS37_Service binOLPTRPS37_Service;
	
	/** 导出会员信息共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "regionId", "provinceId",
			"cityId", "countyId", "channelId", "depotId", "lgcInventoryId",
			"departType", "departId", "prtVendorId", "minLimit" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
			{ "departCode", "RPS37_departCode", "20", "", "" },
			{ "departName", "RPS37_departNameOnly", "20", "", "" },
			{ "depotCode", "RPS37_depotCode", "20", "", "" },
			{ "depotName", "RPS37_depotName", "20", "", "" },
			{ "logicDepotCode", "RPS37_logicDepotName", "15", "", "" },
			{ "logicDepotName", "RPS37_logicDepotName", "20", "", "" },
			{ "nameTotal", "RPS37_productName", "20", "", "" },
			{ "unitCode", "RPS37_unitCode", "12", "", "" },
			{ "barCode", "RPS37_barCode", "12", "", "" },
			{ "moduleCode", "RPS37_moduleCode", "", "", "1190" },
            { "stockQuantity", "RPS37_stockQuantity", "15", "right", "" }};
	
	@Override
	public int getProStockCount(Map<String, Object> map) {
		return binOLPTRPS37_Service.getProStockCount(map);
	}

	@Override
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		return binOLPTRPS37_Service.getProStockList(map);
	}
	
	/**
	 * Excel导出
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		//需导出数据总量
		int dataLen = binOLPTRPS37_Service.getProStockCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS37.getProStockList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS37");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
     * 导出处理
     */
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
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_exportName");
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
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_exportName"));
        String[][] titleRows = {
        		{ "departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_departCode"), "20", "", "" },
    			{ "departName", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_departNameOnly"), "20", "", "" },
    			{ "depotCode", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_depotCode"), "20", "", "" },
    			{ "depotName", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_depotName"), "20", "", "" },
    			{ "logicDepotCode", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_logicDepotName"), "15", "", "" },
    			{ "logicDepotName", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_logicDepotName"), "20", "", "" },
    			{ "nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_productName"), "20", "", "" },
    			{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_unitCode"), "12", "", "" },
    			{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_barCode"), "12", "", "" },
    			{ "moduleCode", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_moduleCode"), "", "", "1190" },
                { "stockQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS37", language, "RPS37_stockQuantity"), "15", "right", "" }
    	};
        exportMap.put("titleRows", titleRows);
        
        return exportMap;
	}

	/**
	 * 取得条件字符串
	 * 
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
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(conditionMap.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				// 产品
				if ("prtVendorId".equals(con)) {
					// 取得国际化条件名
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS37", language, "RPS37_productName");
					paramValue = ConvertUtil.getString(conditionMap
							.get(CherryConstants.NAMETOTAL));
				} else if("minLimit".equals(con)){ 
					// 取得国际化条件名
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS37", language, "RPS37_minLimit");
					paramValue = ConvertUtil.getString(conditionMap
							.get("minLimit"));
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
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLPTRPS37_Service.getProStockList(map);
	}
	
}

/*	
 * @(#)BINOLPTRPS11_BL.java     1.0 2010/03/15		
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS11_IF;
import com.cherry.pt.rps.service.BINOLPTRPS11_Service;

/**
 * 库存记录查询BL
 * 
 * @author lipc
 * @version 1.0 2011.03.15
 * 
 */
public class BINOLPTRPS11_BL implements BINOLPTRPS11_IF, BINOLCM37_IF {
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	@Resource(name="binOLPTRPS11_Service")
	private BINOLPTRPS11_Service binolptrps11Service;
	
	/** 导出会员信息共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/**打印必要的错误日志*/
	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS11_BL.class.getName());
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "regionId", "provinceId",
			"cityId", "countyId", "channelId", "depotId", "lgcInventoryId",
			"departType", "departId", "validFlag", "prtVendorId","productId", "startDate",
			"endDate", "bigClassName" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
			// 1
			{ "regionName", "RPS12_regionName", "6", "", "" },
			// 2
			{ "cityName", "RPS12_cityName", "8", "", "" },
			// 3
			{ "departName", "RPS12_departName", "15", "", "" },
			// 4
			{ "departCode", "RPS12_departCode", "12", "", "" },
			// 5
			{ "inventoryName", "RPS12_inventory", "20", "", "" },
			// 5
			{ "lgcInventoryName", "RPS12_lgcInventory", "20", "", "" },
			// 6大分类
			{ "classifyBig", "RPS12_classifyBig", "20", "", "" },
			// 7中分类
			{ "classifyMid", "RPS12_classifyMid", "20", "", "" },
			// 8小分类
			{ "classifySmall", "RPS12_classifySmall", "20", "", "" },
			// 9
			{ "nameTotal", "RPS12_name", "20", "", "" },
			// 10
			{ "unitCode", "RPS12_unitCode", "12", "", "" },
			// 11
			{ "barCode", "RPS12_barCode", "12", "", "" },
			// 11.1
			{ "originalBrand", "RPS12_originalBrand", "", "", "1299" },
			// 12
			{ "moduleCode", "RPS12_moduleCode", "", "", "1190" },
			// 12.1销售单位
			{ "saleUnit", "RPS12_saleUnit", "", "", "" },
			// 12.2产品规格
			{ "spec", "RPS12_spec", "", "", "" },
			// 13销售价格
	        { "price", "RPS12_price", "10", "right", "" },
			// 14
			{ "startQuantity", "RPS12_startQuantity", "10", "right", "" },
			// 15期初结存金额
		    { "startAmount", "RPS12_startAmount", "15", "right", "" },
			// 16
			{ "inQuantity1", "RPS12_in1", "8", "right", "" },
			// 17
			{ "inQuantity2", "RPS12_in2", "8", "right", "" },
			// 18
			{ "inQuantity3", "RPS12_in3", "8", "right", "" },
			// 19
			{ "inQuantity4", "RPS12_in4", "8", "right", "" },
			// 20
			{ "inQuantity5", "RPS12_in5", "8", "right", "" },
			// 21
			{ "inQuantity6", "RPS12_in6", "8", "right", "" },
			// 22
			{ "inQuantity7", "RPS12_in7", "8", "right", "" },
			// 23
			{ "outQuantity1", "RPS12_out1", "8", "right", "" },
			// 24
			{ "outQuantity2", "RPS12_out2", "8", "right", "" },
			// 25
			{ "outQuantity3", "RPS12_out3", "8", "right", "" },
			// 26
			{ "outQuantity4", "RPS12_out4", "8", "right", "" },
			// 27
			{ "outQuantity5", "RPS12_out5", "8", "right", "" },
			// 28
			{ "outQuantity6", "RPS12_out6", "8", "right", "" },
			// 29
			{ "outQuantity7", "RPS12_out7", "8", "right", "" },
			// 30
			{ "outQuantity8", "RPS12_out8", "8", "right", "" },
			// 31
			{ "outQuantity9", "RPS12_out9", "8", "right", "" },
			// 32
			{ "outQuantity10", "RPS12_out10", "8", "right", "" },
			// 33
			{ "endQuantity", "RPS12_endQuantity", "10", "right", "" },
			// 34期末结存金额
            { "endAmount", "RPS12_endAmount", "15", "right", "" }};
	
	/** 库存记录一览概要导出列数组（显示barCode） */
	private final static String[][] proSummaryArray1 = {
			{ "nameTotal", "RPS11_nameTotal", "20", "", "" },
			{ "unitCode", "RPS11_unitCode", "12", "", "" },
			{ "barCode", "RPS11_barCode", "12", "", "" },
			{ "originalBrand", "RPS11_originalBrand", "", "", "1299" },
			{ "moduleCode", "RPS11_moduleCode", "", "", "1190" },
			{ "saleUnit", "RPS11_saleUnit", "", "", "" },
			{ "spec", "RPS11_spec", "", "", "" },
			{ "price", "RPS11_price", "10", "right", "" },
			{ "startQuantity", "RPS11_startQuantity", "10", "right", "" },
			{ "startAmount", "RPS11_startAmount", "15", "right", "" },
			{ "inQuantity", "RPS11_inQuantity", "10", "right", "" },
			{ "outQuantity", "RPS11_outQuantity", "15", "right", "" },
			{ "endQuantity", "RPS11_endQuantity", "10", "right", "" },
			{ "endAmount", "RPS11_endAmount", "15", "right", "" } };

	/** 库存记录一览概要导出列数组（不显示barCode） */
	private final static String[][] proSummaryArray2 = {
			{ "nameTotal", "RPS11_nameTotal", "20", "", "" },
			{ "unitCode", "RPS11_unitCode", "12", "", "" },
			{ "originalBrand", "RPS11_originalBrand", "", "", "1299" },
			{ "moduleCode", "RPS11_moduleCode", "", "", "1190" },
			{ "saleUnit", "RPS11_saleUnit", "", "", "" },
			{ "spec", "RPS11_spec", "", "", "" },
			{ "price", "RPS11_price", "10", "right", "" },
			{ "startQuantity", "RPS11_startQuantity", "10", "right", "" },
			{ "startAmount", "RPS11_startAmount", "15", "right", "" },
			{ "inQuantity", "RPS11_inQuantity", "10", "right", "" },
			{ "outQuantity", "RPS11_outQuantity", "15", "right", "" },
			{ "endQuantity", "RPS11_endQuantity", "10", "right", "" },
			{ "endAmount", "RPS11_endAmount", "15", "right", "" } };

	@Override
	public int getProStockCount(Map<String, Object> map) {
		return binolptrps11Service.getProStockCount(map);
	}

	@Override
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		return binolptrps11Service.getProStockList(map);
	}
	
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binolptrps11Service.getSumInfo(map);
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
		int dataLen = this.getDeptStockCount(map);
		map.put("dataLen", dataLen);
//		// 默认逻辑仓库处理
//		if(null != dataList){
//			for(Map<String, Object> temp : dataList){
//				int lgcInventoryId = ConvertUtil.getInt(temp.get("BIN_LogicInventoryInfoID"));
//				if(lgcInventoryId == 0){
//					String language = ConvertUtil.getString(map
//							.get(CherryConstants.SESSION_LANGUAGE));
//					String value = binOLMOCOM01_BL.getResourceValue(null,
//							language, "global.page.defLogicInventory");
//					temp.put("lgcInventoryName", value);
//				}
//			}
//		}
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS11_1.getDeptStock");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS12");
		ep.setSearchCondition(getConditionStr(map));
		return binOLMOCOM01_BL.getBatchExportExcel(ep);
	}
	
	/**
	 * 导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public byte[] exportSummaryExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binolptrps11Service.getProStockSummaryInfo(map);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数据组
		String type = ConvertUtil.getString(map.get("type"));
		if(type.equals("1")) {
			ep.setArray(proSummaryArray1);
		} else {
			ep.setArray(proSummaryArray2);
		}
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS11");
		ep.setSearchCondition(getConditionStr(map));
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
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
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_exportName");
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
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_exportName"));
        String[][] titleRows = {
    			// 1
    			{ "regionName", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_regionName"), "6", "", "" },
    			// 2
    			{ "cityName", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_cityName"), "8", "", "" },
    			// 3
    			{ "departName", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_departName"), "15", "", "" },
    			// 4
    			{ "departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_departCode"), "12", "", "" },
    			// 5
    			{ "inventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_inventory"), "20", "", "" },
    			// 5
    			{ "lgcInventoryName", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_lgcInventory"), "20", "", "" },
    			// 6大分类
    			{ "classifyBig", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_classifyBig"), "20", "", "" },
    			// 7中分类
    			{ "classifyMid", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_classifyMid"), "20", "", "" },
    			// 8小分类
    			{ "classifySmall", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_classifySmall"), "20", "", "" },
    			// 6
    			{ "nameTotal", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_name"), "20", "", "" },
    			// 7
    			{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_unitCode"), "12", "", "" },
    			// 8
    			{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_barCode"), "12", "", "" },
    			// 8.0
    			{ "originalBrand", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_originalBrand"), "", "", "1299" },
    			// 8.1
    			{ "moduleCode", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_moduleCode"), "", "", "1190" },
    			// 8.2销售单位
    			{ "saleUnit", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_saleUnit"), "", "", "" },
    			// 8.3产品规格
    			{ "spec", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_spec"), "", "", "" },
    			//销售价格
                { "price", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_price"), "10", "right", "" },
    			// 9
    			{ "startQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_startQuantity"), "10", "right", "" },
    			//期初结存金额
                { "startAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_startAmount"), "15", "right", "" },
    			// 10
    			{ "inQuantity1", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_in1"), "8", "right", "" },
    			// 11
    			{ "inQuantity2", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_in2"), "8", "right", "" },
    			// 12
    			{ "inQuantity3", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_in3"), "8", "right", "" },
    			// 13
    			{ "inQuantity4", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_in4"), "8", "right", "" },
    			// 14
    			{ "inQuantity5", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_in5"), "8", "right", "" },
    			// 15
    			{ "inQuantity6", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_in6"), "8", "right", "" },
    			// 16
    			{ "inQuantity7", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_in7"), "8", "right", "" },
    			// 17
    			{ "outQuantity1", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out1"), "8", "right", "" },
    			// 18
    			{ "outQuantity2", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out2"), "8", "right", "" },
    			// 19
    			{ "outQuantity3", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out3"), "8", "right", "" },
    			// 20
    			{ "outQuantity4", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out4"), "8", "right", "" },
    			// 21
    			{ "outQuantity5", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out5"), "8", "right", "" },
    			// 22
    			{ "outQuantity6", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out6"), "8", "right", "" },
    			// 23
    			{ "outQuantity7", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out7"), "8", "right", "" },
    			// 24
    			{ "outQuantity8", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out8"), "8", "right", "" },
    			// 25
    			{ "outQuantity9", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out9"), "8", "right", "" },
    			// 26
    			{ "outQuantity10", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_out10"), "8", "right", "" },
    			// 27
    			{ "endQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_endQuantity"), "10", "right", "" },
    			//期末结存金额
    			{ "endAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_endAmount"), "15", "right", "" }
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
				if ("prtVendorId".equals(con) || "productId".equals(con)) {
					// 取得国际化条件名
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS11", language, "RPS11_nameTotal");
					paramValue = ConvertUtil.getString(conditionMap
							.get(CherryConstants.NAMETOTAL));
				} else if ("validFlag".equals(con)) {
					// 产品状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS11", language, "RPS11_validFlag");
					paramValue = codeTable.getVal("1137", paramValue);
				} else if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
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
				} else if ("bigClassName".equals(con)) {
					// 商品大类
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.bigClassName");
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
	
	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getExportName(Map<String, Object> map) throws Exception{
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS12",
				language, "RPS12_exportName")
				+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
		return exportName;
	}
	
	/**
	 * 取得仓库某些产品库存List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDepotStockList(Map<String, Object> map) {
		// 组织ID
		int orgInfoId = ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID));
		String startDate = ConvertUtil.getString(map.get("startDate"));
		String endDate = ConvertUtil.getString(map.get("endDate"));
		// 库存相关的日期参数设置到paramMap中
		binolcm00BL.setParamsMap(map, orgInfoId, startDate,endDate,"Pro");
		return binolptrps11Service.getDepotStockList(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binolptrps11Service.getDeptStock(map);
	}
	
	/**
	 * 查询产品库存详细总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getDeptStockCount(Map<String, Object> map) throws Exception {
		int count = 0;
		try {
			count = binolptrps11Service.getDeptStockCount(map);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// 库存数据导出经常出现查询时间过长（目前数据查询timeout时间为20分钟）而报【导出失败】的错误（失败原因不明确）
    		// 系统繁忙，暂时无法导出大数据量，请尝试缩小导出的数据量或者稍后再试！
			throw new CherryException("ECM000105");
		}
		return count;
	}
}

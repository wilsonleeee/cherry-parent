/*	
 * @(#)BINOLPTRPS38_BL.java     1.0 2010/03/15		
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
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.interfaces.BINOLPTRPS38_IF;
import com.cherry.pt.rps.service.BINOLPTRPS38_Service;

/**
 * 库存记录查询BL
 * 
 * @author lipc
 * @version 1.0 2011.03.15
 * 
 */
public class BINOLPTRPS38_BL implements BINOLPTRPS38_IF, BINOLCM37_IF {
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource(name="binOLCM00_Service")
	private BINOLCM00_Service binOLCM00_Service;
	@Resource(name="binOLPTRPS38_Service")
	private BINOLPTRPS38_Service binolptrps38Service;
	
	/** 导出会员信息共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "regionId", "provinceId",
			"cityId", "countyId", "channelId", "depotId", "lgcInventoryId",
			"departType", "departId", "validFlag", "prtVendorId","productId", "startDate",
			"endDate", "bigClassName" };
	
	/** 库存记录一览概要导出列数组（显示barCode） */
	private final static String[][] proSummaryArray1 = {
			{ "nameTotal", "RPS38_nameTotal", "20", "", "" },
			{ "unitCode", "RPS38_unitCode", "12", "", "" },
			{ "barCode", "RPS38_barCode", "12", "", "" },
			{ "moduleCode", "RPS38_moduleCode", "", "", "1190" },
			{ "price", "RPS38_price", "10", "right", "" },
			{ "quantity", "RPS38_quantity", "10", "right", "" } };

	/** 库存记录一览概要导出列数组（不显示barCode） */
	private final static String[][] proSummaryArray2 = {
			{ "nameTotal", "RPS38_nameTotal", "20", "", "" },
			{ "unitCode", "RPS38_unitCode", "12", "", "" },
			{ "barCode", "RPS38_barCode", "12", "", "" },
			{ "moduleCode", "RPS38_moduleCode", "", "", "1190" },
			{ "price", "RPS38_price", "10", "right", "" },
			{ "quantity", "RPS38_quantity", "10", "right", "" } };

	@Override
	public int getProStockCount(Map<String, Object> map) {
		return binolptrps38Service.getProStockCount(map);
	}

	@Override
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		return binolptrps38Service.getProStockList(map);
	}
	
	
	
	/**
	 * 导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public byte[] exportSummaryExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binolptrps38Service.getProStockSummaryInfo(map);
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
		ep.setBaseName("BINOLPTRPS38");
		ep.setSearchCondition(getConditionStr(map));
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
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
							"BINOLPTRPS38", language, "RPS38_nameTotal");
					paramValue = ConvertUtil.getString(conditionMap
							.get(CherryConstants.NAMETOTAL));
				} else if ("validFlag".equals(con)) {
					// 产品状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLPTRPS38", language, "RPS38_validFlag");
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
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS38",
				language, "RPS38_exportName")
				+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
		return exportName;
	}
	

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binolptrps38Service.getDeptStock(map);
	}

	@Override
	public Map<String, Object> getProProduct(Map<String, Object> map) {
		Map<String, Object> prtMap = binolptrps38Service.getProProduct(map);
		String productId = ConvertUtil.getString(map.get(ProductConstants.PRODUCTID));
		if(!"".equals(productId)){
			List<String> barCode = binolptrps38Service.getBarCodeList(map);
			prtMap.put("list", barCode);
		}
		return prtMap;
	}

	@Override
	public List<Map<String, Object>> getProStockDetails(Map<String, Object> map) {
		
		return binolptrps38Service.getProStockDetails(map);
	}
	
}

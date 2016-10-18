/*	
 * @(#)BINOLSSPRM31_BL.java     1.0 2010/11/04		
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
package com.cherry.ss.prm.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.prm.service.BINOLSSPRM31_Service;

/**
 * 库存记录查询BL
 * 
 * @author lipc
 * @version 1.0 2010.11.04
 * 
 */
public class BINOLSSPRM31_BL {
	@Resource
	private CodeTable codeTable;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM00_Service binOLCM00_Service;
	@Resource
	private BINOLSSPRM31_Service binolssprm31Service;

	// Excel导出数据查询条件数组
	private final static String[] prmCondition = { "regionId", "provinceId",
			"cityId", "countyId", "channelId", "depotId", "lgcInventoryId",
			"departType", "departId", "validFlag", "prmVendorId", "startDate",
			"endDate" };

	// Excel导出列数组
	private final static String[][] prmArray = {
			// 1
			{ "regionName", "PRM32_regionName", "6", "", "" },
			// 2
			{ "cityName", "PRM32_cityName", "8", "", "" },
			// 3
			{ "departName", "PRM32_departName", "15", "", "" },
			// 4
			{ "departCode", "PRM32_departCode", "12", "", "" },
			// 21
			{ "inventoryName", "PRM32_inventory", "20", "", "" },
			// 22
			{ "lgcInventoryName", "PRM32_lgcInventory", "20", "", "" },
			// 23
			{ "nameTotal", "PRM32_name", "20", "", "" },
			// 24
			{ "unitCode", "PRM32_unitCode", "12", "", "" },
			// 25
			{ "barCode", "PRM32_barCode", "12", "", "" },
			// 5
			{ "startQuantity", "PRM32_startQuantity", "10", "right", "" },
			// 6
			{ "inQuantity1", "PRM32_in1", "8", "right", "" },
			// 7
			{ "inQuantity2", "PRM32_in2", "8", "right", "" },
			// 8
			{ "inQuantity3", "PRM32_in3", "8", "right", "" },
			// 9
			{ "inQuantity4", "PRM32_in4", "8", "right", "" },
			// 10
			{ "inQuantity5", "PRM32_in5", "8", "right", "" },
			// 11
			{ "inQuantity6", "PRM32_in6", "8", "right", "" },
			// 12
			{ "inQuantity7", "PRM32_in7", "8", "right", "" },
			// 13
			{ "outQuantity1", "PRM32_out1", "8", "right", "" },
			// 14
			{ "outQuantity2", "PRM32_out2", "8", "right", "" },
			// 15
			{ "outQuantity3", "PRM32_out3", "8", "right", "" },
			// 16
			{ "outQuantity4", "PRM32_out4", "8", "right", "" },
			// 17
			{ "outQuantity5", "PRM32_out5", "8", "right", "" },
			// 18
			{ "outQuantity6", "PRM32_out6", "8", "right", "" },
			// 19
			{ "outQuantity7", "PRM32_out7", "8", "right", "" },
			// 19
			{ "outQuantity8", "PRM32_out8", "8", "right", "" },
			// 20
			{ "endQuantity", "PRM32_endQuantity", "10", "right", "" } };

	/**
	 * 取得库存记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProStockCount(Map<String, Object> map) {
		return binolssprm31Service.getProStockCount(map);
	}

	/**
	 * 取得库存记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		return binolssprm31Service.getProStockList(map);
	}

	/**
	 * 取得汇总信息
	 * 
	 * */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binolssprm31Service.getSumInfo(map);
	}

	/**
	 * Excel导出
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binolssprm31Service
				.getDeptStock(map);
		// 默认逻辑仓库处理
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
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(prmArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSSPRM32");
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
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		for (String con : prmCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				// 促销品
				if ("prmVendorId".equals(con)) {
					// 取得国际化条件名
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSSPRM31", language, "PRM31_nameTotal");
					paramValue = ConvertUtil.getString(map
							.get(CherryConstants.NAMETOTAL));
				} else if ("validFlag".equals(con)) {
					// 促销品状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSSPRM31", language, "PRM31_validFlag");
					paramValue = codeTable.getVal("1137", paramValue);
				} else if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
				} else if ("departType".equals(con)) {
					// 部门类型
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.departType");
					paramValue = codeTable.getVal("1000", paramValue);
				} else if ("channelId".equals(con)) {
					// 渠道
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.channel");
					paramValue = binOLCM00_Service.getChannelName(map);
				} else if ("depotId".equals(con)) {
					// 仓库
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depot");
					paramValue = binOLCM00_Service.getDepotName(map);
				} else if ("lgcInventoryId".equals(con)) {
					// 逻辑仓库
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.logicInventory");
					paramValue = binOLCM00_Service.getLogicInventoryName(map);
				} else if ("departId".equals(con)) {
					// 部门
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depart");
					paramValue = binOLCM00_Service.getDepartName(map);
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
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}
}

/*
 * @(#)BINOLSSPRM25_BL.java     1.0 2010/10/27
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
import com.cherry.ss.prm.service.BINOLSSPRM25_Service;

/**
 * 
 * 盘点查询BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM25_BL {
	@Resource
	private CodeTable codeTable;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLSSPRM25_Service binolssprm25_Service;
	@Resource
	private BINOLCM00_Service binolcm00_Service;
	
	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	public int searchTakingCount(Map<String, Object> map) {
		// 取得盘点单总数
		return binolssprm25_Service.getTakingCount(map);
	}

	/**
	 * 取得盘点单List
	 * 
	 * @param map
	 * @return
	 */
	public List searchTakingList(Map<String, Object> map) {
		// 取得盘点单List
		return binolssprm25_Service.getTakingList(map);
	}
	
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binolssprm25_Service.getSumInfo(map);
    }
    
 // Excel导出数据查询条件数组
	private final static String[] prmCondition = { "regionId", "provinceId",
			"cityId", "countyId", "channelId", "depotId", "departType", 
			"departId", "profitKbn","stockTakingNo", "prmVendorId", "startDate",
			"endDate" };

	// Excel一览明细导出列数组
	private final static String[][] prmArray = {
			// 1
			{ "regionName", "PRM26_regionName", "6", "", "" },
			// 2
			{ "cityName", "PRM26_cityName", "8", "", "" },
			// 3
			{ "stockTakingNoIF", "PRM26_stockTakingNoIF", "30", "", "" },
			// 4
			{ "departName", "PRM26_departName", "15", "", "" },
			// 5
			{ "depotName", "PRM26_inventName", "25", "", "" },
			// 6
			{ "nameTotal", "PRM26_nameTotal", "25", "", "" },
			// 7
			{ "unitCode", "PRM26_unitCode", "20", "", "" },
			// 8 
			{ "barCode", "PRM26_barCode", "20", "", "" }, 
			// 9
			{ "packageName", "PRM26_packageName", "12", "", "" },
			// 10
			{ "Quantity", "PRM26_quantity", "12", "right", "" },
			// 11
			{ "RealQuantity", "PRM26_realQuantity", "12", "right", "" },
			// 12
			{ "GainQuantity", "PRM26_gainQuantity", "12", "right", "" },
			// 13
			{ "DetailAmount", "PRM26_detailAmount", "20", "right", "" },
			// 14
			{ "Type", "PRM26_type", "20", "", "1019" },
			// 15
			{ "TradeDateTime", "PRM26_tradeDateTime", "20", "", "" },
			// 16
			{ "PrimaryCategoryName", "PRM26_lblLCategory", "12", "", "" },
			// 17
			{ "SecondCategoryName", "PRM26_lblMCategory", "12", "", "" },
			// 18
			{ "SmallCategoryName", "PRM26_lblSCategory", "12", "", "" },
			// 19
			{ "HandleType", "PRM26_handleType", "12", "", "1020" },
			// 20
			{ "EmployeeName", "PRM26_employeeName", "12", "", "" }
	};
	/**
	 * Excel导出一览明细
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binolssprm25_Service.gettakingStock(map);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(prmArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSSPRM26");
		ep.setSearchCondition(getConditionStr(map));
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}
	
	// Excel一览导出列数组
	private final static String[][] prmArray1 = {
			// 1
			{ "stockTakingNo", "PRM25_stockTakingNo", "30", "", "" },
			// 2
			{ "departName", "PRM25_departName", "30", "", "" },
			// 3
			{ "inventoryName", "PRM25_inventName", "25", "", "" },
			// 4
			{ "sumrealQuantity", "PRM25_realQuantity", "12", "right", "" },
			// 5
			{ "summQuantity", "PRM25_gainQuantity", "12", "right", "" },
			// 6
			{ "summAmount", "PRM25_summAmount", "20", "right", "" },
			// 7
			{ "takingType", "PRM25_type", "20", "", "1019" },
			// 8
			{ "tradeDateTime", "PRM25_tradeDateTime", "20", "", "" },
			// 9
			{ "employeeName", "PRM25_employeeName", "12", "", "" }
		
	};
	/**
	 * Excel导出一览
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportTakingInfo(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binolssprm25_Service.getTakingInfo(map);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(prmArray1);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSSPRM25");
		ep.setSearchCondition(getConditionStr(map));
		ep.setDataList(dataList);
		return binOLMOCOM01_BL.getExportExcel(ep);
	}
	
	/**
	 * 取得促销品盘点库存数据总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getTakingStockCount(Map<String, Object> map) throws Exception {
		return binolssprm25_Service.getTakingStockCount(map);
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
							"BINOLSSPRM25", language, "PRM25_nameTotal");
					paramValue = ConvertUtil.getString(map
							.get(CherryConstants.NAMETOTAL));
				} else if ("profitKbn".equals(con)) {
					// 促销品盈亏状态
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSSPRM25", language, "PRM25_profitKbn");
					paramValue = codeTable.getVal("1018", paramValue);
				} else if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
				}else if ("stockTakingNo".equals(con)) {
					// 单据号
					paramName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM25",
							language,"PRM25_stockTakingNo");
				} else if ("departType".equals(con)) {
					// 部门类型
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.departType");
					paramValue = codeTable.getVal("1000", paramValue);
				} else if ("channelId".equals(con)) {
					// 渠道
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.channel");
					paramValue = binolcm00_Service.getChannelName(map);
				} else if ("depotId".equals(con)) {
					// 仓库
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depot");
					paramValue = binolcm00_Service.getDepotName(map);
				} 
				else if ("departId".equals(con)) {
					// 部门
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page.depart");
					paramValue = binolcm00_Service.getDepartName(map);
				} else {
					// 区域
					String text = con.substring(0, con.indexOf("Id"));
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + text);
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put(CherryConstants.SESSION_LANGUAGE, language);
					temp.put("regionId", map.get(con));
					paramValue = binolcm00_Service.getRegionName(temp);
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}

}

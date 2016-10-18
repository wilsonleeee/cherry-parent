/*
 * @(#)BINOLPTRPS01_BL.java     1.0 2012/12/04
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS16_IF;
import com.cherry.pt.rps.service.BINOLPTRPS16_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 盘点查询BL
 * 
 * 
 * 
 * @author liuminghao
 * @version 1.0 2012.12.04
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS16_BL extends SsBaseBussinessLogic implements
		BINOLPTRPS16_IF {

	@Resource(name = "binOLPTRPS16_Service")
	private BINOLPTRPS16_Service binOLPTRPS16_Service;

	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource(name = "CodeTable")
	private CodeTable codeTable;

	// Excel导出列数组
	private final static String[][] proArray = {
			// 1
			{ "region", "regionName", "6", "", "" },
			// 2
			{ "city", "cityName", "8", "", "" },
			
			{ "Type", "RPS16_department", "15", "", "" },
			// 3
			{ "DepartCode", "counterCode", "15", "", "" },
			// 4
			{ "departName", "counterNameIF", "15", "", "" },
			// 5
			{ "stockTakingNo", "RPS16_stockTakingNo", "20", "", "" },
			// 6
			{ "EmployeeCode", "employeeCode", "20", "", "" },
			// 7
			{ "stockTakingDate", "createTime", "20", "", "" },
			// 8
			{ "takingType", "RPS16_type", "20", "", "" },
			// 9
			{ "Comments", "RPS16_Comments", "12", "", "" },
			//10
			{ "Quantity", "RPS16_Quantity", "12", "", "" },
			// 11
			{ "realQuantity", "RPS16_realQuantity", "10", "right", "" },
			// 12
			{ "summQuantity", "RPS16_gainQuantity", "10", "right", "" },
			
			{ "OverQuantity", "RPS16_profitKbn0", "8", "right", "" },
			
			{ "ShortQuantity", "RPS16_profitKbn1", "8", "right", "" }
						
			};

	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "makeDate", "startDate",
			"endDate" };

	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchTakingCount(Map<String, Object> map) {
		// 取得盘点单总数
		return binOLPTRPS16_Service.getTakingCount(map);
	}

	/**
	 * 取得盘点单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchTakingList(Map<String, Object> map) {
		// 取得盘点单List
		return binOLPTRPS16_Service.getTakingList(map);
	}

	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLPTRPS16_Service.getSumInfo(map);
	}

	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getExportName(Map<String, Object> map) throws Exception {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS16",
				language, "downloadFileName")
				+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
		return exportName;
	}

	/**
	 * 导出信息Excel
	 * 
	 * @param map
	 * @return 返回导出信息List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = binOLPTRPS16_Service
				.getTakingInfoListExcel(map);
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> maps = dataList.get(i);
			if (maps.get("region") == null) {
				maps.put("region", "");
			}
			if (maps.get("city") == null) {
				maps.put("city", "");
			}
			if (maps.get("EmployeeCode") == null) {
				maps.put("EmployeeCode", "");
			}
			if (maps.get("CreateTime") == null) {
				maps.put("CreateTime", "");
			}
			if (maps.get("stockTakingNo") == null) {
				maps.put("stockTakingNo", "");
			}
			if (maps.get("takingType") == null) {
				maps.put("takingType", "");
			} else {
				maps.put("takingType",
						codeTable.getVal("1054", maps.get("takingType")));
			}
			if (maps.get("Comments") == null) {
				maps.put("Comments", "");
			}
			if (maps.get("Quantity") == null) {
				maps.put("Quantity", "");
			}
			if (maps.get("summQuantity") == null) {
				maps.put("summQuantity", "");
			}
			if (maps.get("realQuantity") == null) {
				maps.put("realQuantity", "");
			}
			if (maps.get("Type") == null) {
				maps.put("Type", "");
			}else{
				maps.put("Type",
						codeTable.getVal("1000", maps.get("Type")));
			}
		}

		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLPTRPS16");
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

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		condition.append(binOLMOCOM01_BL.getResourceValue("BINOLPTRPS16",
				language, "takeStockDate")
				+ "："
				+ df.format(new Date())
				+ "\0\0\0\0\0");
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";

				if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}

	@Override
	public List getConjunctionProList(Map<String, Object> map) {
		List<String> proVendorIdListTemp = (List<String>) map.get("proVendorIdList");
		List<Map<String, Object>> list = binOLPTRPS16_Service.getConjunctionProList(map);
		List<String> proVendorIdList = new ArrayList<String>();
		if (null != list && list.size() > 0 ) {
			for (Map<String, Object> tempMap : list) {
				String proVendorId = String.valueOf(tempMap.get("BIN_ProductVendorID"));
				if (!CherryChecker.isEmptyString(proVendorId) && !"null".equalsIgnoreCase(proVendorId)) {
					proVendorIdList.add(proVendorId);
				}
			}
		}
		proVendorIdList.addAll(proVendorIdListTemp);
		proVendorIdList = new ArrayList(new HashSet(proVendorIdList));// 去重
		return proVendorIdList;
	}
}

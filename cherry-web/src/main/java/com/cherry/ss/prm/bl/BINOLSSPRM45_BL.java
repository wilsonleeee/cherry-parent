/*
 * @(#)BINOLPTRPS01_BL.java     1.0 2012/12/26
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
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.ss.prm.interfaces.BINOLSSPRM45_IF;
import com.cherry.ss.prm.service.BINOLSSPRM45_Service;

/**
 * 
 * 盘点查询BL
 * 
 * 
 * 
 * @author liuminghao
 * @version 1.0 2012.12.26
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM45_BL extends SsBaseBussinessLogic implements
		BINOLSSPRM45_IF {

	@Resource(name = "binOLSSPRM45_Service")
	private BINOLSSPRM45_Service binOLSSPRM45_Service;

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

			{ "Type", "PRM45_department", "15", "", "" },
			// 3
			{ "DepartCode", "counterCode", "15", "", "" },
			// 4
			{ "departName", "counterNameIF", "15", "", "" },
			// 5
			{ "stockTakingNo", "PRM45_stockTakingNo", "20", "", "" },
			// 6
			{ "EmployeeCode", "employeeCode", "20", "", "" },
			// 7
			{ "stockTakingDate", "createTime", "20", "", "" },
			// 8
			{ "takingType", "PRM45_type", "20", "", "" },
			// 9
			{ "Comments", "PRM45_Comments", "12", "", "" },
			// 10
			{ "Quantity", "PRM45_Quantity", "12", "", "" },
			// 11
			{ "realQuantity", "PRM45_realQuantity", "10", "right", "" },
			// 12
			{ "summQuantity", "PRM45_gainQuantity", "10", "right", "" },

			{ "OverQuantity", "PRM45_profitKbn0", "8", "right", "" },

			{ "ShortQuantity", "PRM45_profitKbn1", "8", "right", "" }

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
		return binOLSSPRM45_Service.getTakingCount(map);
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
		return binOLSSPRM45_Service.getTakingList(map);
	}

	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSSPRM45_Service.getSumInfo(map);
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
		String exportName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM45",
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
		List<Map<String, Object>> dataList = binOLSSPRM45_Service
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
						codeTable.getVal("1019", maps.get("takingType")));
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
			} else {
				maps.put("Type", codeTable.getVal("1000", maps.get("Type")));
			}
		}

		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSSPRM45");
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
		condition.append(binOLMOCOM01_BL.getResourceValue("BINOLSSPRM45",
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

	/**
	 * 获取相关联的促销品
	 * @param map
	 * @return
	 */
	@Override
	public List getConjunctionPrmList(Map<String, Object> map) {
		List<String> prmVendorIdListTemp = (List<String>) map.get("prmVendorIdList");
		List<Map<String, Object>> list = binOLSSPRM45_Service.getConjunctionPrmList(map);
		List<String> prmVendorIdList = new ArrayList<String>();
		if (null != list && list.size() > 0 && !list.isEmpty()) {
			for (Map<String, Object> tempMap : list) {
				String prmVendorId = String.valueOf(tempMap.get("BIN_ProductVendorID"));
				if (!CherryChecker.isEmptyString(prmVendorId) && !"null".equalsIgnoreCase(prmVendorId)) {
					prmVendorIdList.add(prmVendorId);
				}
			}
		}
		prmVendorIdList.addAll(prmVendorIdListTemp);
		// 去重
		prmVendorIdList = new ArrayList(new HashSet(prmVendorIdList));
		return prmVendorIdList;
	}
}

/*
 * @(#)BINOLPTRPS01_BL.java     1.0 2011/3/11
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
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.bil.interfaces.BINOLSTBIL09_IF;
import com.cherry.st.bil.service.BINOLSTBIL09_Service;

/**
 * 
 * 盘点查询BL
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.3.11
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL09_BL extends SsBaseBussinessLogic implements BINOLSTBIL09_IF{
    
	@Resource
	private BINOLSTBIL09_Service binOLSTBIL09_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM00_Service binOLCM00_Service;
	@Resource
	private CodeTable codeTable;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "stockTakingNo",
			"productName", "departType", "regionId", "provinceId", "cityId",
			"countyId", "channelId", "departId", "depotId", "profitKbn",
			"verifiedFlag", "operateDate" };
	
	// Excel导出列数组
	private final static String[][] proArray = {
			{ "area", "BIL09_area", "", "", "" },// 大区
			{ "region", "BIL09_region", "", "", "" },// 区域
			{ "province", "BIL09_province", "", "", "" },// 省份
			{ "city", "BIL09_city", "", "", "" },// 城市
			{ "stockTakingNo", "BIL09_stockTakingNo", "30", "", "" },// 单据号
			{ "departCode", "BIL09_departCode", "15", "", "" },// 盘点部门代码
			{ "departName", "BIL09_departNameExp", "20", "", "" },// 盘点部门名称
			{ "employeeCode", "BIL09_empCode", "15", "", "" },// 操作人员代号
			{ "employeeName", "BIL09_empName", "15", "", "" },// 操作人员名称
			{ "depotCode", "BIL09_depotCode", "15", "", "" },// 实体仓库代码
			{ "depotName", "BIL09_depotName", "20", "", "" },// 实体仓库名称
			{ "logicInventoryCode", "BIL09_logicInventoryCode", "15", "", "" },// 逻辑仓库代号
			{ "logicInventoryName", "BIL09_logicInventoryName", "20", "", "" },// 逻辑仓库名称
			{ "employeeCodeAudit", "BIL09_employeeCodeAudit", "15", "", "" },// 审核者代号
			{ "employeeNameAudit", "BIL09_employeeNameAudit", "15", "", "" },// 审核者名称
			{ "unitCode", "BIL09_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "BIL09_barCode", "15", "", "" },// 产品条码
			{ "productName", "BIL09_productName", "20", "", "" },// 产品名称
			{ "quantity", "BIL09_quantity", "", "right", "" },// 账面数量
			{ "realQuantity", "BIL09_realQuantity", "", "right", "" },// 实盘数量
			{ "gainQuantity", "BIL09_gainQuantity", "", "right", "" },// 盘差
			{ "price", "BIL09_price", "", "right", "" },// 产品价格
			{ "amount", "BIL09_summAmount", "", "right", "" },// 盘差金额
			{ "profitKbn", "BIL09_profitKbn", "", "", "1018" },// 盈亏
			{ "takingType", "BIL09_type", "25", "", "1054" },// 盘点类型
			{ "verifiedFlag", "BIL09_verifiedFlag", "15", "", "1322" },// 盘点审核区分
			{ "tradeDateTime", "BIL09_tradeDateTime", "20", "", "" }, // 盘点时间
			{ "comments", "BIL09_comments", "20", "", "" } // 备注
	};
	
	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchTakingCount(Map<String, Object> map) {
		// 取得盘点单总数
		return binOLSTBIL09_Service.getTakingCount(map);
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
		return binOLSTBIL09_Service.getTakingList(map);
	}

	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLSTBIL09_Service.getSumInfo(map);
    }

    @Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLSTBIL09_Service.getExportDetailCount(map);
		if(dataLen <= 0){
			throw new CherryException("EMO00022");
		}
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "stockTakingNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL09.getPrtTakingExportList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSTBIL09");
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
			if (con.equals("operateDate")) {// 盘点日期始终显示
				paramValue = "showDate";
			}
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("stockTakingNo".equals(con)) {
					// 盘点单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL09", language, "BIL09_stockTakingNo");
				} else if ("productName".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL09", language, "BIL09_productName");
				} else if ("operateDate".equals(con)) {
					// 盘点日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL09", language, "BIL09_date");
					// 日期
					String startDate = (map.get("startDate") != null) ? ConvertUtil
							.getString(map.get("startDate")).replace('-', '/')
							: "----/--/--";
					String endDate = (map.get("endDate") != null) ? ConvertUtil
							.getString(map.get("endDate")).replace('-', '/')
							: "----/--/--";
					paramValue = startDate + "\0~\0" + endDate;
				} else if ("profitKbn".equals(con)) {
					// 盈亏情况
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL09", language, "BIL09_profitKbn");
					paramValue = codeTable.getVal("1018", paramValue);
				} else if ("verifiedFlag".equals(con)) {
					// 审核区分
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL09", language, "BIL09_verifiedFlag");
					paramValue = codeTable.getVal("1007", paramValue);
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
	public int getExportDetailCount(Map<String, Object> map) {
		return binOLSTBIL09_Service.getExportDetailCount(map);
	}
}

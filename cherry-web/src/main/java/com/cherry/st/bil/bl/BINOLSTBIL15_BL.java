/*  
 * @(#)BINOLSTBIL15_BL.java     1.0 2013/8/23      
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
import com.cherry.st.bil.interfaces.BINOLSTBIL15_IF;
import com.cherry.st.bil.service.BINOLSTBIL15_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 产品盘点申请单详细BL
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL15_BL extends SsBaseBussinessLogic implements BINOLSTBIL15_IF{
    
	@Resource(name="binOLSTBIL15_Service")
	private BINOLSTBIL15_Service binOLSTBIL15_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM00_Service binOLCM00_Service;
	@Resource
	private CodeTable codeTable;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "stockTakingNo", "nameTotal",
			"departType", "regionId", "provinceId", "cityId", "countyId",
			"channelId", "departId", "depotId", "tradeType", "verifiedFlag",
			"operateDate" };

	// Excel导出列数组
	private final static String[][] proArray = {
			{ "area", "BIL15_area", "", "", "" },// 大区
			{ "region", "BIL15_region", "", "", "" },// 区域
			{ "province", "BIL15_province", "", "", "" },// 省份
			{ "city", "BIL15_city", "", "", "" },// 城市
			{ "stockTakingNo", "BIL15_stockTakingNo", "30", "", "" },// 单据号
			{ "relevanceNo", "BIL15_relevanceNo", "30", "", "" },// 关联单据号
			{ "departCode", "BIL15_departCode", "15", "", "" },// 申请部门代码
			{ "departName", "BIL15_departNameExp", "20", "", "" },// 申请部门名称
			{ "employeeCode", "BIL15_empCode", "15", "", "" },// 操作人员代号
			{ "employeeName", "BIL15_empName", "15", "", "" },// 操作人员名称
			{ "depotCode", "BIL15_depotCode", "15", "", "" },// 实体仓库代码
			{ "depotName", "BIL15_depotName", "20", "", "" },// 实体仓库名称
			{ "logicInventoryCode", "BIL15_logicInventoryCode", "20", "", "" },// 逻辑仓库代号
			{ "logicInventoryName", "BIL15_logicInventoryName", "20", "", "" },// 逻辑仓库名称
			{ "employeeCodeAudit", "BIL15_employeeCodeAudit", "15", "", "" },// 审核者代号
			{ "employeeNameAudit", "BIL15_employeeNameAudit", "15", "", "" },// 审核者名称
			{ "unitCode", "BIL15_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "BIL15_barCode", "15", "", "" },// 产品条码
			{ "productName", "BIL15_productName", "20", "", "" },// 产品名称
			{ "bookQuantity", "BIL15_bookQuantity", "", "right", "" },// 账面数量
			{ "checkQuantity", "BIL15_checkQuantity", "", "right", "" },// 实盘数量
			{ "gainQuantity", "BIL15_gainQuantity", "", "right", "" },// 盘差
			{ "price", "BIL15_detailPrice", "", "right", "" },// 价格
			{ "gainAmount", "BIL15_gainAmount", "", "right", "" },// 盘差金额
			{ "stocktakeType", "BIL15_type", "15", "", "1054" },// 盘点类型
			{ "verifiedFlag", "BIL15_verifiedFlag", "15", "", "1238" },// 审核区分
			{ "tradeDateTime", "BIL15_tradeDateTime", "20", "", "" }, // 盘点申请时间
			{ "comments", "BIL15_comments", "20", "", "" } // 备注
	};
	
	/**
	 * 取得盘点申请单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchProStocktakeRequestCount(Map<String, Object> map) {
		// 取得盘点申请单总数
		return binOLSTBIL15_Service.getProStocktakeRequestCount(map);
	}

	/**
	 * 取得盘点申请单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchProStocktakeRequestList(Map<String, Object> map) {
		// 取得退库申请单List
		return binOLSTBIL15_Service.getProStocktakeRequestList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSTBIL15_Service.getSumInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLSTBIL15_Service.getExportDetailCount(map);
		if(dataLen <= 0){
			throw new CherryException("EMO00022");
		}
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "stockTakingNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL15.getPrtStocktakeReqExportList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSTBIL15");
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
				if ("stockTakingNo".equals(con)) {
					// 申请单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL15", language, "BIL15_stockTakingNo");
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL15", language, "BIL15_productName");
				} else if ("operateDate".equals(con)) {
					// 申请日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL15", language, "BIL15_date");
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
							"BINOLSTBIL15", language, "BIL15_verifiedFlag");
					paramValue = codeTable.getVal("1238", paramValue);
				} else if ("tradeType".equals(con)) {
					// 业务类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL15", language, "BIL15_tradeType");
					paramValue = codeTable.getVal("1189", paramValue);
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
}

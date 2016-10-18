/*  
 * @(#)BINOLSTBIL13_BL.java     1.0 2013/7/24      
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
import com.cherry.st.bil.interfaces.BINOLSTBIL13_IF;
import com.cherry.st.bil.service.BINOLSTBIL13_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 产品退库申请单详细BL
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL13_BL extends SsBaseBussinessLogic implements BINOLSTBIL13_IF{
    
	@Resource(name="binOLSTBIL13_Service")
	private BINOLSTBIL13_Service binOLSTBIL13_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM00_Service binOLCM00_Service;
	@Resource
	private CodeTable codeTable;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "billNo", "nameTotal",
			"departType", "regionId", "provinceId", "cityId", "countyId",
			"channelId", "departId", "depotId", "tradeType", "verifiedFlag",
			"operateDate" };

	// Excel导出列数组
	private final static String[][] proArray = {
			{ "area", "BIL13_area", "", "", "" },// 大区
			{ "region", "BIL13_region", "", "", "" },// 区域
			{ "province", "BIL13_province", "", "", "" },// 省份
			{ "city", "BIL13_city", "", "", "" },// 城市
			{ "billNo", "BIL13_billNo", "30", "", "" },// 单据号
			{ "relevanceNo", "BIL13_relevanceNo", "30", "", "" },// 关联单据号
			{ "departCode", "BIL13_departCode", "15", "", "" },// 申请部门代码
			{ "departName", "BIL13_departNameExp", "20", "", "" },// 申请部门名称
			{ "employeeCode", "BIL13_empCode", "15", "", "" },// 操作人员代号
			{ "employeeName", "BIL13_empName", "15", "", "" },// 操作人员名称
			{ "depotCode", "BIL13_depotCode", "20", "", "" },// 申请实体仓库代码
			{ "depotName", "BIL13_depotName", "20", "", "" },// 申请实体仓库名称
			{ "logicInventoryCode", "BIL13_logicInventoryCode", "20", "", "" },// 逻辑仓库代号
			{ "logicInventoryName", "BIL13_logicInventoryName", "20", "", "" },// 逻辑仓库名称
			{ "departCodeReceive", "BIL13_departCodeReceive", "15", "", "" },// 接受部门代码
			{ "departNameReceive", "BIL13_departNameReceive", "20", "", "" },// 接受部门名称
			{ "depotCodeReceive", "BIL13_depotCodeReceive", "15", "", "" },// 接受实体仓库代码
			{ "depotNameReceive", "BIL13_depotNameReceive", "20", "", "" },// 接受实体仓库名称
			{ "logicInventoryCodeReceive", "BIL13_logicInventoryCodeReceive", "20", "", "" },// 接受逻辑仓库代号
			{ "logicInventoryNameReceive", "BIL13_logicInventoryNameReceive", "20", "", "" },// 接受逻辑仓库名称
			{ "employeeCodeAudit", "BIL13_employeeCodeAudit", "15", "", "" },// 审核者代号
			{ "employeeNameAudit", "BIL13_employeeNameAudit", "15", "", "" },// 审核者名称
			{ "unitCode", "BIL13_unitCode", "15", "", "" },// 厂商编码
			{ "barCode", "BIL13_barCode", "15", "", "" },// 产品条码
			{ "productName", "BIL13_productName", "20", "", "" },// 产品名称
			{ "detailQuantity", "BIL13_detailQuantity", "", "right", "" },// 数量
			{ "detailPrice", "BIL13_detailPrice", "", "right", "" },// 价格
			{ "detailAmount", "BIL13_detailAmount", "", "right", "" },// 金额
			{ "verifiedFlag", "BIL13_verifiedFlag", "15", "", "1252" },// 审核区分
			{ "tradeDate", "BIL13_date", "10", "", "" }, // 申请日期
			{ "comments", "BIL13_comments", "20", "", "" } // 备注
	};
	
	/**
	 * 取得退库申请单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchProReturnRequestCount(Map<String, Object> map) {
		// 取得退库申请单总数
		return binOLSTBIL13_Service.getProReturnRequestCount(map);
	}

	/**
	 * 取得退库申请单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchProReturnRequestList(Map<String, Object> map) {
		// 取得退库申请单List
		return binOLSTBIL13_Service.getProReturnRequestList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSTBIL13_Service.getSumInfo(map);
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLSTBIL13_Service.getExportDetailCount(map);
		if(dataLen <= 0){
			throw new CherryException("EMO00022");
		}
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		map.put(CherryConstants.SORT_ID, "billNo");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL13.getPrtReturnReqExportList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLSTBIL13");
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
				if ("billNo".equals(con)) {
					// 申请单号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL13", language, "BIL13_billNo");
				} else if ("nameTotal".equals(con)) {
					// 产品名称
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL13", language, "BIL13_productName");
				} else if ("operateDate".equals(con)) {
					// 申请日期
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL13", language, "BIL13_date");
					// 日期
					String startDate = (map.get("startDate") != null) ? ConvertUtil
							.getString(map.get("startDate")).replace('-', '/')
							: "----/--/--";
					String endDate = (map.get("endDate") != null) ? ConvertUtil
							.getString(map.get("endDate")).replace('-', '/')
							: "----/--/--";
					paramValue = startDate + "\0~\0" + endDate;
				} else if ("verifiedFlag".equals(con)) {
					// 审核区分
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL13", language, "BIL13_verifiedFlag");
					paramValue = codeTable.getVal("1252", paramValue);
				} else if ("tradeType".equals(con)) {
					// 业务类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLSTBIL13", language, "BIL13_tradeType");
					paramValue = codeTable.getVal("1188", paramValue);
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

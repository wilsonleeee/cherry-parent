/*	
 * @(#)BINOLCPACT10_BL.java     1.0 @2013-08-15		
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
package com.cherry.cp.act.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.service.BINOLCPACT10_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;


/**
 * 兑换结果一览BL
 * 
 * @author LuoHong
 * 
 */
public class BINOLCPACT10_BL {
	@Resource(name="binOLCPACT10_Service")
	private BINOLCPACT10_Service binOLCPACT10_Service;
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private CodeTable codeTable;
	/**
	 * 兑换结果List
	 * 
	 * @param map 
	 * @return List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getExchangeList(Map<String, Object> map) throws Exception {
		//兑换结果List
		List<Map<String, Object>>  memExchangeList = binOLCPACT10_Service.getExchangeList(map);
		if (memExchangeList != null && !memExchangeList.isEmpty()) {
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			for (Map<String, Object> memExchangeMap : memExchangeList) {
				// 会员【手机号】字段解密
				if (!CherryChecker.isNullOrEmpty(memExchangeMap.get("mobilePhone"), true)) {
					String mobilePhone = ConvertUtil.getString(memExchangeMap.get("mobilePhone"));
					memExchangeMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
				}
			}
		}
		return memExchangeList;
	}
	/**
	 * 兑换结果数
	 * 
	 * @param map
	 * @return
	 */
	public int getExchangeCount(Map<String, Object> map) {
		// 兑换结果数
		return binOLCPACT10_Service.getExchangeCount(map);
	}
	// Excel导出数据查询条件数组
		private final static String[] campCondition = {"billCode","memberCode","startDate","endDate","testType","departCode","mobilePhone"};
		// Excel一览导出列数组
		private final static String[][] campArray = {
			// 1
			{ "SubCampaignCode", "ACT10_campCode", "20", "", "" },
			// 2
			{ "SubCampaignName", "ACT10_campName", "20", "", "" },
			// 3
			{ "billCode", "ACT10_billCode", "40", "", "" },
			// 4
			{ "saleDate", "ACT10_getDate", "15", "", "" },
			// 5
			{ "saleProductTime", "ACT10_getTime", "10", "", "" },
			// 6
			{ "departCode", "ACT10_counterCode", "15", "", "" },
			// 7
			{ "counterName", "ACT10_counterName", "20", "", "" },
			// 19
			{ "employeeCode", "ACT10_employeeCode", "15", "", "" },
			// 20
			{ "employeeName", "ACT10_employeeName", "15", "", "" },
			// 8
			{ "memberCode", "ACT10_memberCode", "15", "", "" },
			// 9
			{ "memName", "ACT10_member", "20", "", "" },
			// 10
			{ "mobilePhone", "ACT10_memberPhone", "20", "", "" },
			// 11
			{ "testType", "ACT10_testType", "10", "", "1256" },
			// 14
			{ "UnitCode", "ACT10_unitCode", "20", "", "" },
			// 14
			{ "BarCode", "ACT10_barCode", "20", "", "" },
			// 15
			{ "nameTotal", "ACT10_productName", "25", "", "" },
			// 16
			{ "prtType", "ACT10_prtType", "10", "", "1134" },
			// 17
			{ "buyQuantity", "ACT10_quantity", "10", "right", "" },
			// 18
			{ "buyAmount", "ACT10_payPrice", "15", "right", "" }};
		/**
		 * Excel导出一览
		 * 
		 * @param map
		 * @return 返回导出信息
		 * @throws CherryException
		 */
		public byte[] exportExcel(Map<String, Object> map) throws Exception {
			//Excel导出总数量
			int dataLen = binOLCPACT10_Service.getExcelCount(map);
			map.put("dataLen", dataLen);
			BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
			// 排序字段(ID)
			map.put(CherryConstants.SORT_ID, "saleRecordId");
			// 必须设置sqlID（必须）,用于批查询
			map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT10.getExcelResultList");
			ep.setMap(map);
			// 导出数据列数组
			ep.setArray(campArray);
			// 导出数据列头国际化资源文件
			ep.setBaseName("BINOLCPACT10");
			ep.setShowTitleStyle(true);
			ep.setSearchCondition(getConditionStr(map));
			return binOLMOCOM01_BL.getBatchExportExcel(ep);
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
			int num = 0;
			for (String con : campCondition) {
				// 条件值
				String paramValue = ConvertUtil.getString(map.get(con));
				if (!"".equals(paramValue)) {
					// 条件名
					String paramName = "";
					if ("billCode".equals(con)) {
						// 活动单据
						paramName = binOLMOCOM01_BL.getResourceValue(
								"BINOLCPACT10", language, "ACT10_billCode");
					} else if ("memberCode".equals(con)) {
						// 会员卡号
						paramName = binOLMOCOM01_BL.getResourceValue(
								"BINOLCPACT10", language, "ACT10_memberCode");
					} else if ("mobilePhone".equals(con)) {
						// 会员手机
						paramName = binOLMOCOM01_BL
								.getResourceValue("BINOLCPACT10", language,
										"ACT10_memberPhone");
					}else if ("testType".equals(con)) {
						// 会员类型
						paramName = binOLMOCOM01_BL.getResourceValue(
								"BINOLCPACT10", language, "ACT10_testType");
						paramValue = codeTable.getVal("1256", paramValue);
					}else if ("startDate".equals(con) || "endDate".equals(con)) {
						// 兑换日期
						paramName = binOLMOCOM01_BL.getResourceValue(
								"BINOLCPACT10",language, "ACT10_getDate");
						// 日期
						paramName = binOLMOCOM01_BL.getResourceValue(
								"BINOLCPACT10",language, "ACT10_change" + con);
					}else if ("departCode".equals(con)) {
						// 兑换柜台
						paramName = binOLMOCOM01_BL.getResourceValue(
								"BINOLCPACT10", language, "ACT10_counter");
					}
					num++;
					String splitStr;
					// 换行，空格控制
					if (num % 4 == 0) {
						splitStr = "\n";
					} else {
						splitStr = "\0\0\0\0\0";
					}
					condition.append(paramName + "：" + paramValue + splitStr);
				}
			}
			return condition.toString().trim();
		}
}

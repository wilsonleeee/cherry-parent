/*	
 * @(#)BINOLCPACT07_BL.java     1.0 @2013-07-15		
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

import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.interfaces.BINOLCPACT07_IF;
import com.cherry.cp.act.service.BINOLCPACT07_Service;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;

/**
 * 礼品领用报表BL
 * 
 * @author menghao
 * 
 * @version 1.0 2013/07/15
 */
public class BINOLCPACT07_BL implements BINOLCPACT07_IF {

	@Resource
	private BINOLCPACT07_Service binOLCPACT07_Service;
	
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private CodeTable codeTable;
	
	// Excel导出数据查询条件数组
	private final static String[] proCondition = { "brandInfoId", "billNoIF",
			"memberCode", "mobilePhone", "couponCode", "counterCode",
			"testType", "activityName", "startDate", "endDate" };

	// Excel导出列数组
	private final static String[][] proArray = {
			{ "activityCodeExport", "ACT07_activityCode", "22", "", "" },// 主题活动码
			{ "activityName", "ACT07_activityName", "25", "", "" },// 主题活动名称
			{ "subActivityCode", "ACT07_subActivityCode", "22", "", "" },// 活动码
			{ "subActivityName", "ACT07_subActivityName", "25", "", "" },// 活动名称
			{ "billNoIF", "ACT07_billNoIFs", "40", "", "" },// 单据号
			{ "billCodePre", "ACT07_relevanceNo", "40", "", "" },// 关联单据号
			{ "getTime", "ACT07_getTime", "20", "", "" }, // 领用时间
			{ "memberCode", "ACT07_memberCode", "15", "", "" },// 会员卡号
			{ "memberNameExport", "ACT07_memberName", "10", "", "" },// 会员姓名
			{ "memberPhone", "ACT07_memberPhone", "15", "", "" },//会员手机
			{ "testType", "ACT07_testType", "10", "", "1256" },//会员类型
			{ "couponCode", "ACT07_couponCode", "20", "", "" },// Coupon码
			{ "giftDrawType", "ACT07_giftDrawType", "10", "", "1178" },//领用类型
			{ "departCodeExport", "ACT07_counterCode", "15", "", "" },// 柜台号
			{ "departNameExport", "ACT07_counterName", "25", "", "" },// 柜台
			{ "employeeCodeExport", "ACT07_empCode", "15", "", "" },// BACode
			{ "employeeName", "ACT07_empName", "15", "", "" },// BA姓名
			{ "unitCode", "ACT07_unitCode", "20", "", "" },// 厂商编码
			{ "barCode", "ACT07_barCode", "15", "", "" },// 产品条码
			{ "productName", "ACT07_productName", "25", "", "" },// 产品名称
			{ "giftType", "ACT07_productType", "", "", "1134" },// 产品类型
			{ "quantity", "ACT07_getQuantity", "", "right", "" },// 数量
			{ "amount", "ACT07_getAmount", "15", "right", "" }// 金额
	};

	@Override
	public int getGiftDrawCount(Map<String, Object> map) {
		return binOLCPACT07_Service.getGiftDrawCount(map);
	}

	@Override
	public List<Map<String, Object>> getGiftDrawList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>>  giftDrawList = binOLCPACT07_Service.getGiftDrawList(map);
		if (giftDrawList != null && !giftDrawList.isEmpty()) {
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			for (Map<String, Object> memExchangeMap : giftDrawList) {
				// 会员【手机号】字段解密
				if (!CherryChecker.isNullOrEmpty(memExchangeMap.get("memberPhone"), true)) {
					String memberPhone = ConvertUtil.getString(memExchangeMap.get("memberPhone"));
					memExchangeMap.put("memberPhone", CherrySecret.decryptData(brandCode,memberPhone));
				}
			}
		}
		return giftDrawList;
	}

	@Override
	public String getActivity(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binOLCPACT07_Service.getActivity(map);
		StringBuffer sb = new StringBuffer();
		if("code".equals(map.get("selected"))){
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> tempMap = resultList.get(i);
				sb.append((String) tempMap.get("code"));
				sb.append("|");
				sb.append((String) tempMap.get("name"));
				sb.append("|");
				sb.append(String.valueOf(tempMap.get("id")));
				if (i != resultList.size()) {
					sb.append("\n");
				}
			}
		} else {
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> tempMap = resultList.get(i);
				sb.append((String) tempMap.get("name"));
				sb.append("|");
				sb.append((String) tempMap.get("code"));
				sb.append("|");
				sb.append(String.valueOf(tempMap.get("id")));
				if (i != resultList.size()) {
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		int dataLen = binOLCPACT07_Service.getExportDetailCount(map);
		map.put("dataLen", dataLen);
		BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
		//必须设置sqlID（必须）,用于批查询
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT07.getExportDetailList");
		ep.setMap(map);
		// 导出数据列数组
		ep.setArray(proArray);
		ep.setShowTitleStyle(true);
		// 导出数据列头国际化资源文件
		ep.setBaseName("BINOLCPACT07");
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
		int lineNum = 0;
		for (String con : proCondition) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";
				if ("brandInfoId".equals(con)) {
					// 所属品牌
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_brand");
					paramValue = binOLCM05_BL.getBrandName(map);
				} else if ("billNoIF".equals(con)) {
					// 领用单据号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_billNoIFs");
				} else if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
				} else if ("mobilePhone".equals(con)) {
					// 会员手机
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_memberPhone");
				} else if ("memberCode".equals(con)) {
					//会员卡号
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_memberCode");
				} else if ("testType".equals(con)) {
					//会员类型
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_testType");
					paramValue = codeTable.getVal("1256", paramValue);
				} else if ("counterCode".equals(con)) {
					//领用柜台
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_counter");
				} else if ("couponCode".equals(con)) {
					//coupon码
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_couponCode");
				} else {
					//主题活动
					paramName = binOLMOCOM01_BL.getResourceValue(
							"BINOLCPACT07", language, "ACT07_activity");
				}
				lineNum ++;
				if(lineNum % 6 == 0){
					condition.append("\n");
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}

		return condition.toString().trim();
	}

}

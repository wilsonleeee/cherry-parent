/*
 * @(#)SaleInfoHandler.java     1.0 2011/11/01
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
package com.cherry.shindig.gadgets.bl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.shindig.gadgets.service.SaleInfoService;
import com.cherry.shindig.gadgets.util.GadgetsConstants;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 销售信息取得Handler
 * 
 * @author WangCT
 * @version 1.0 2011/11/01
 */
public class SaleInfoBL {
	
	/** 销售信息取得Service **/
	@Resource
	private SaleInfoService saleInfoService;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSaleInfo(String data) throws Exception {
		
		Map paramMap = (Map)JSONUtil.deserialize(data);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String pageNo = (String)paramMap.get("pageNo");
		int length = GadgetsConstants.PAGE_LENGTH;
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length -1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "OccurTime DESC");
		map.put("START", start);
		map.put("END", end);
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		}
		map.put("businessType", "3");
		map.put("operationType", "1");
		String sysDate = saleInfoService.getDateYMD();
		map.put("saleDate", sysDate);
		// 查询销售信息List
		List<Map<String, Object>> saleInfoList = saleInfoService.getSaleInfoList(map);
		// 返回结果
		Map resultData = new HashMap();
		if(saleInfoList != null && !saleInfoList.isEmpty()) {
			map.put("saleInfoList", saleInfoList);
			List<Map<String, Object>> employeeList = saleInfoService.getEmployeeList(map);
			for(int i = 0; i < saleInfoList.size(); i++) {
				Map<String, Object> saleInfo = saleInfoList.get(i);
				String tradeEntityCode = (String)saleInfo.get("TradeEntityCode");
				// 非会员的场合
				if(tradeEntityCode == null || "".equals(tradeEntityCode) || MessageConstants.ON_MEMBER_CARD.equals(tradeEntityCode)) {
					saleInfo.put("memberType", "0");
				} else {
					saleInfo.put("memberType", "1");
				}
				if(employeeList != null && !employeeList.isEmpty()) {
					String saleRecordId = saleInfo.get("saleRecordId").toString();
					for(Map<String, Object> employeeMap : employeeList) {
						String _saleRecordId = employeeMap.get("saleRecordId").toString();
						if(_saleRecordId.equals(saleRecordId)) {
							saleInfo.putAll(employeeMap);
							employeeList.remove(employeeMap);
							break;
						}
					}
				}
			}
			resultData.put("saleInfoMes",saleInfoList);
			if(saleInfoList.size() == length) {
				if(Integer.parseInt(pageNo) + 1 <= GadgetsConstants.PAGE_MAX_COUNT) {
					resultData.put("pageNo", Integer.parseInt(pageNo) + 1);
				}
			}
		}
		// 第一次请求的场合，统计当日销售信息
		if("1".equals(pageNo)) {
			// 只对正式柜台统计
			map.put("counterKind", 0);
			map.put("memberInfoFlg", "0");
			// 统计非会员销售总金额和总数量
			Map<String, Object> saleAmountSumMap = saleInfoService.getSaleAmountSum(map);
			map.put("memberInfoFlg", "1");
			// 统计会员销售总金额和总数量
			Map<String, Object> memSaleAmountSumMap = saleInfoService.getSaleAmountSum(map);
			// 销售统计信息
			Map<String, Object> saleCountInfo = new HashMap<String, Object>();
			BigDecimal totalAmount = null;
			BigDecimal totalQuantity = null;
			if(saleAmountSumMap != null && !saleAmountSumMap.isEmpty()) {
				totalAmount = (BigDecimal)saleAmountSumMap.get("amount");
				totalQuantity = (BigDecimal)saleAmountSumMap.get("quantity");
				saleCountInfo.put("notMemCount", saleAmountSumMap);
			}
			if(memSaleAmountSumMap != null && !memSaleAmountSumMap.isEmpty()) {
				BigDecimal amount = (BigDecimal)memSaleAmountSumMap.get("amount");
				BigDecimal quantity = (BigDecimal)memSaleAmountSumMap.get("quantity");
				if(totalAmount != null) {
					if(amount != null) {
						totalAmount = totalAmount.add(amount);
					}
				} else {
					totalAmount = amount;
				}
				if(totalQuantity != null) {
					if(quantity != null) {
						totalQuantity = totalQuantity.add(quantity);
					}
				} else {
					totalQuantity = quantity;
				}
				saleCountInfo.put("memCount", memSaleAmountSumMap);
			}
			if(totalAmount != null) {
				saleCountInfo.put("TotalAmount", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			if(totalQuantity != null) {
				saleCountInfo.put("TotalQuantity", totalQuantity.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			resultData.put("saleCountInfo", saleCountInfo);
			resultData.put("sysDate", sysDate);
		}
		return resultData;
	}

}

/*
 * @(#)SaleTargetRptHandler.java     1.0 2013/08//08
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
package com.cherry.shindig.gadgets.container.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.Service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.shindig.gadgets.service.SaleTargetRptService;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 销售完成进度报表Handler
 * 
 * @author WangCT
 * @version 1.0 2013/08//08
 */
@Service(name = "saleTargetRpt")
public class SaleTargetRptHandler {
	
	/** 销售完成进度报表Service **/
	@Resource
	private SaleTargetRptService saleTargetRptService;
	
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getSaleInfo(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		String pageNo = (String)paramMap.get("pageNo");
		String pageNumber = (String)paramMap.get("pageNumber");
		int length = Integer.parseInt(pageNumber);
		int start = (Integer.parseInt(pageNo) - 1) * length + 1;
		int end = start + length -1;
		// 返回结果
		Map resultData = new HashMap();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SORT_ID", "amount DESC");
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
		map.put("countModel", paramMap.get("countModel"));
		// 取得业务日期
		String bussinessDate = saleTargetRptService.getBussinessDate(map);
		map.put("dateValue", bussinessDate);
		// 查询指定日期所在的财务年月
		Map<String, Object> fiscalMonthMap = saleTargetRptService.getFiscalMonth(map);
		if(fiscalMonthMap != null && !fiscalMonthMap.isEmpty()) {
			int fiscalYear = (Integer)fiscalMonthMap.get("fiscalYear");
			int fiscalMonth = (Integer)fiscalMonthMap.get("fiscalMonth");
			String fiscalYearMonth = String.valueOf(fiscalYear);
			if(fiscalMonth < 10) {
				fiscalYearMonth = fiscalYearMonth + "0" + String.valueOf(fiscalMonth);
			} else {
				fiscalYearMonth = fiscalYearMonth + String.valueOf(fiscalMonth);
			}
			map.put("fiscalYearMonth", fiscalYearMonth);
			map.putAll(fiscalMonthMap);
			// 查询指定财务月的最小最大自然日
			Map<String, Object> minMaxDateValue = saleTargetRptService.getMinMaxDateValue(map);
			map.putAll(minMaxDateValue);
			
			String minDateValue = (String)map.get("minDateValue");
			String maxDateValue = (String)map.get("maxDateValue");
			int dayNum = DateUtil.getIntervalDays(DateUtil.coverString2Date(minDateValue), DateUtil.coverString2Date(bussinessDate))+1;
			int dayCount = DateUtil.getIntervalDays(DateUtil.coverString2Date(minDateValue), DateUtil.coverString2Date(maxDateValue))+1;
			double datePercent = CherryUtil.div(dayNum*100, dayCount, 2);
			double predictPer = CherryUtil.div(dayNum, dayCount, 4);
			resultData.put("overDay", dayCount-dayNum);
			resultData.put("pageNo", Integer.parseInt(pageNo));
			resultData.put("pageNumber", length);
			// 查询销售完成进度报表信息总件数
			int count = saleTargetRptService.getSaleTargetRptCount(map);
			resultData.put("saleTargetRptCount", count);
			if(count > 0) {
				// 查询销售完成进度报表信息List
				List<Map<String, Object>> saleTargetRptList = saleTargetRptService.getSaleTargetRptList(map);
				if(saleTargetRptList != null && !saleTargetRptList.isEmpty()) {
					for(Map<String, Object> saleTargetRptMap : saleTargetRptList) {
						Object amount = saleTargetRptMap.get("amount");
						Object targetMoney = saleTargetRptMap.get("targetMoney");
						if(targetMoney != null && !"".equals(targetMoney.toString())) {
							if(Double.parseDouble(targetMoney.toString()) != 0) {
								double percent = CherryUtil.div(Double.parseDouble(amount.toString())*100, Double.parseDouble(targetMoney.toString()), 2);
								saleTargetRptMap.put("percent", Double.toString(percent)+"%");
							}
						}
						saleTargetRptMap.put("datePercent", Double.toString(datePercent)+"%");
						if(predictPer != 0) {
							saleTargetRptMap.put("predict", CherryUtil.div(Double.parseDouble(amount.toString()), predictPer, 2));
						}
					}
				}
				resultData.put("saleTargetRptList", saleTargetRptList);
			}
		}
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}

package com.cherry.shindig.gadgets.container.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
import com.cherry.shindig.gadgets.service.SaleInfoService;
import com.googlecode.jsonplugin.JSONUtil;

@Service(name = "saleCountByHours")
public class SaleCountByHoursHandler {
	
	/** 销售信息取得Service **/
	@Resource
	private SaleInfoService saleInfoService;
	
	@SuppressWarnings("unchecked")
	@Operation(httpMethods = "POST", bodyParam = "data")
	public Future<DataCollection> getSaleInfo(RequestItem request) throws Exception {
		
		String bodyparams = request.getParameter("data");
		Map paramMap = (Map)JSONUtil.deserialize(bodyparams);
		Map userInfoMap = (Map)paramMap.get("userInfo");
		// 时间段
		int interval = Integer.parseInt(paramMap.get("interval").toString());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.USERID, userInfoMap.get("BIN_UserID"));
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfoMap.get("BIN_OrganizationInfoID"));
		Object brandInfoId = userInfoMap.get("BIN_BrandInfoID");
		if(brandInfoId != null && CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(brandInfoId.toString())) {
			map.put(CherryConstants.BRANDINFOID, userInfoMap.get("BIN_BrandInfoID"));
		}
		map.put("businessType", "3");
		map.put("operationType", "1");
		String sysDate = saleInfoService.getSYSDateTime();
		int curHours = Integer.parseInt(sysDate.substring(11,13));
		String curDate = sysDate.substring(0, 10);
		map.put("saleDate", curDate);
		// 只对正式柜台统计
		map.put("counterKind", 0);
		
		// 返回结果
		Map resultData = new HashMap();
		resultData.put("sysDate", curDate);
		// 统计销售总金额和总数量
		Map<String, Object> saleAmountMap = saleInfoService.getSaleAmountSum(map);
		if(saleAmountMap != null) {
			BigDecimal totalAmount = (BigDecimal)saleAmountMap.get("amount");
			BigDecimal totalQuantity = (BigDecimal)saleAmountMap.get("quantity");
			if(totalAmount == null) {
				totalAmount = new BigDecimal(0);
			}
			if(totalQuantity == null) {
				totalQuantity = new BigDecimal(0);
			}
			resultData.put("amount", totalAmount);
			resultData.put("quantity", totalQuantity);
			
			
			// 按时间统计销售金额和数量
			List<Map<String, Object>> saleByHoursList = saleInfoService.getSaleByHours(map);
			if(saleByHoursList != null && !saleByHoursList.isEmpty()) {
				Map<String, Object> _saleByHours = new HashMap<String, Object>();
				for(int i = 0; i < saleByHoursList.size(); i++) {
					Map<String, Object> saleByHoursMap = saleByHoursList.get(i);
					Integer hours = (Integer)saleByHoursMap.get("hours");
					BigDecimal amount = (BigDecimal)saleByHoursMap.get("amount");
					_saleByHours.put(String.valueOf(hours), amount);
				}
				
				Map<String, Object> saleByHours = new HashMap<String, Object>();
				BigDecimal amountSum = new BigDecimal(0);
				for(int i = 0; i < 24; i++) {
					BigDecimal amount = (BigDecimal)_saleByHours.get(String.valueOf(i));
					if(amount == null) {
						amount = new BigDecimal(0);
					}
					amountSum = amountSum.add(amount);
					Map<String, Object> amountMap = new HashMap<String, Object>();
					amountMap.put("amountSum", amountSum);
					amountMap.put("amount", amount);
					saleByHours.put(String.valueOf(i+1), amountMap);
				}
				List<Map<String, Object>> hoursList = new ArrayList<Map<String, Object>>();
				for(int i = 24; i >= 8; i-=interval) {
					Map<String, Object> hoursMap = new HashMap<String, Object>();
					hoursMap.put("xaxes", String.valueOf(i));
					
					Map<String, Object> amountMap = (Map)saleByHours.get(String.valueOf(i));
					amountSum = (BigDecimal)amountMap.get("amountSum");
					hoursMap.put("amountSum", amountSum);
					
					int startHours = i - interval;
					if(startHours < 0) {
						startHours = 0;
					}
					BigDecimal amount = new BigDecimal(0);
					for(int j = startHours+1; j <= i; j++) {
						amountMap = (Map)saleByHours.get(String.valueOf(j));
						if(amountMap != null) {
							BigDecimal _amount = (BigDecimal)amountMap.get("amount");
							amount = amount.add(_amount);
						}
					}
					if(amount.doubleValue() != 0) {
						hoursMap.put("amount", amount);
					}
					hoursList.add(hoursMap);
				}
				
				for(int i = 0; i < hoursList.size(); i++) {
					Map<String, Object> hoursMap = hoursList.get(i);
					String xaxes = (String)hoursMap.get("xaxes");
					if(Integer.parseInt(xaxes) > curHours) {
						if(i+1 < hoursList.size()) {
							String _xaxes = (String)hoursList.get(i+1).get("xaxes");
							if(Integer.parseInt(_xaxes) > curHours) {
								hoursMap.remove("amountSum");
								hoursMap.remove("amount");
							} else {
								break;
							}
						}
					} else {
						break;
					}
				}
				Collections.reverse(hoursList);
				
				BigDecimal _amountSum = (BigDecimal)hoursList.get(0).get("amountSum");
				if(_amountSum.doubleValue() != 0) {
					hoursList.get(0).put("amount", _amountSum);
				}
				Map<String, Object> hoursMap = new HashMap<String, Object>();
				hoursMap.put("xaxes", "0");
				hoursMap.put("amountSum", new BigDecimal(0));
				hoursList.add(0,hoursMap);
				
//				for(int i = 0; i < hoursList.size(); i++) {
//					Map<String, Object> hoursMap = hoursList.get(i);
//					amountSum = (BigDecimal)hoursMap.get("amountSum");
//					if(amountSum != null && amountSum.doubleValue() == 0) {
//						if(i+1 < hoursList.size()) {
//							BigDecimal _amountSum = (BigDecimal)hoursList.get(i+1).get("amountSum");
//							if(_amountSum != null && _amountSum.doubleValue() == 0) {
//								hoursMap.remove("amount");
//								hoursMap.remove("amountSum");
//							} else {
//								break;
//							}
//						}
//					} else {
//						break;
//					}
//				}
				
				resultData.put("saleByHoursList", hoursList);
				
			}
		}
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}

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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.shindig.gadgets.service.SaleInfoService;
import com.googlecode.jsonplugin.JSONUtil;

@Service(name = "saleCountByHours1")
public class SaleCountByHours1Handler {
	
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
		
		String channelId = (String)paramMap.get("channelId");
		if(channelId == null || "".equals(channelId)) {
			// 取得渠道柜台信息List
			List<Map<String, Object>> channelCounterList = saleInfoService.getChannelCounterList(map);
			if(channelCounterList != null && !channelCounterList.isEmpty()) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				List<String[]> keyList = new ArrayList<String[]>();
				String[] key1 = {"channelId","channelName"};
				keyList.add(key1);
				ConvertUtil.convertList2DeepList(channelCounterList,list,keyList,0);
				resultData.put("channelCounterList", list);
			}
		} else {
			if(!"-1".equals(channelId)) {
				map.put("channelId", channelId);
				map.put("organizationId", paramMap.get("organizationId"));
			}
		}
		
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
				Map<String, Object> saleByHours = new HashMap<String, Object>();
				BigDecimal amountSum = new BigDecimal(0);
				BigDecimal quantitySum = new BigDecimal(0);
				for(int i = 0; i < saleByHoursList.size(); i++) {
					Map<String, Object> saleByHoursMap = saleByHoursList.get(i);
					Integer hours = (Integer)saleByHoursMap.get("hours");
					if(hours < 8) {
						BigDecimal _amount = (BigDecimal)saleByHoursMap.get("amount");
						if(_amount == null) {
							_amount = new BigDecimal(0);
						}
						amountSum = amountSum.add(_amount);
						
						BigDecimal _quantity = (BigDecimal)saleByHoursMap.get("quantity");
						if(_quantity == null) {
							_quantity = new BigDecimal(0);
						}
						quantitySum = quantitySum.add(_quantity);
					} else {
						saleByHours.put(String.valueOf(hours+1), saleByHoursMap);
					}
				}
				Map<String, Object> saleByHoursMap = new HashMap<String, Object>();
				saleByHoursMap.put("amount", amountSum);
				saleByHoursMap.put("quantity", quantitySum);
				saleByHours.put("8", saleByHoursMap);
				
				List<Map<String, Object>> hoursList = new ArrayList<Map<String, Object>>();
				for(int i = 24; i >= 8; i-=interval) {
					Map<String, Object> hoursMap = new HashMap<String, Object>();
					hoursMap.put("xaxes", String.valueOf(i));
					
					Map<String, Object> amountMap = (Map)saleByHours.get(String.valueOf(i));
					
					int startHours = i - interval;
					if(startHours < 0) {
						startHours = 0;
					}
					BigDecimal amount = new BigDecimal(0);
					BigDecimal quantity = new BigDecimal(0);
					for(int j = startHours+1; j <= i; j++) {
						amountMap = (Map)saleByHours.get(String.valueOf(j));
						if(amountMap != null) {
							BigDecimal _amount = (BigDecimal)amountMap.get("amount");
							if(_amount == null) {
								_amount = new BigDecimal(0);
							}
							amount = amount.add(_amount);
							
							BigDecimal _quantity = (BigDecimal)amountMap.get("quantity");
							if(_quantity == null) {
								_quantity = new BigDecimal(0);
							}
							quantity = quantity.add(_quantity);
						}
					}
					hoursMap.put("amount", amount);
					if(quantity.doubleValue() != 0) {
						hoursMap.put("quantity", quantity);
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
								hoursMap.remove("quantity");
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
				
				Map<String, Object> hoursMap = new HashMap<String, Object>();
				hoursMap.put("xaxes", "0");
				hoursMap.put("amount", new BigDecimal(0));
				hoursList.add(0,hoursMap);
				
				resultData.put("saleByHoursList", hoursList);
			}
		}
		return ImmediateFuture.newInstance(new DataCollection(resultData));
	}

}

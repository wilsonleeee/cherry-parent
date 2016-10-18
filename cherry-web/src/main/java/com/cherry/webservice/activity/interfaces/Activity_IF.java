package com.cherry.webservice.activity.interfaces;

import java.util.List;
import java.util.Map;

public interface Activity_IF {
	
	public Map<String,Object> getGetSubCampInfo(Map<String,Object> map);
	
	public Map<String,Object> getPromotionInfo(Map<String,Object> map);
	
	public Map<String,Object> getCampHistory(Map<String,Object> map);
	
	public Map<String,Object> tran_receiveOrder(Map<String,Object> map) throws Exception;
	
	public Map<String,Object> getOrderInfo(Map<String,Object> map);
	
	public Map<String,Object> getOrderTimeRange(Map<String,Object> map);
	
	public Map<String,Object> tran_changeOrderState(Map<String,Object> map) throws Exception;
	
	public Map<String,Object> tran_changeOrder(Map<String,Object> map) throws Exception;
	
	public Map<String,Object> tran_applyCoupon(Map<String,Object> map) throws Exception;
	
	public Map<String,Object> tran_campaignBespeak(Map<String,Object> map) throws Exception;
	
	public List<Map<String,Object>> getPrtList(int brandInfoId,String subCampCode);
	
	public Map<String, Object> makeOrder(Map<String, Object> map,Map<String, Object> campInfo,List<Map<String, Object>> prtList) throws Exception;
	
	public Map<String,Object> getCouponList(Map<String,Object> map);
	
	public Map<String,Object> tran_updateOrderInfo(Map<String,Object> map);
}

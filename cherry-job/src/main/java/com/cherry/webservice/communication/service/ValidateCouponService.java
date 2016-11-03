package com.cherry.webservice.communication.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class ValidateCouponService extends BaseService{
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> checkCoupon(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "VALIDATECOUPON.checkCoupon");
		return baseServiceImpl.getList(map);
	}
	
	public void updateExpiredTime(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "VALIDATECOUPON.updateExpiredTime");
		baseServiceImpl.update(paramMap);
	}
}

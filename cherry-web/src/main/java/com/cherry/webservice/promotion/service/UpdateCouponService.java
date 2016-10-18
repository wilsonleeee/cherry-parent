package com.cherry.webservice.promotion.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class UpdateCouponService extends BaseService{

	public Map<String,Object> getCouponStatus(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "UpdateCoupon.getCouponStatus");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	public int updateCouponStatus(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "UpdateCoupon.updateCouponStatus");
		return baseServiceImpl.update(map);
	}
}

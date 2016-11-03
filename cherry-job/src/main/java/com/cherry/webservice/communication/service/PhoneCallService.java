package com.cherry.webservice.communication.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class PhoneCallService extends BaseService{
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMemberCouponInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "PHONECALL.getMemberCouponInfo");
		return baseServiceImpl.getList(map);
	}
}

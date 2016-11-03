package com.cherry.webservice.communication.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class GetCouponCodeService extends BaseService {

	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrderInfoByMobile(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "GetCouponCode.getOrderInfoByMobile");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
	
	public void addCouponCreateLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "GetCouponCode.addCouponCreateLog");
		baseServiceImpl.save(map);
	}
	
}

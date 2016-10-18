package com.cherry.cm.pay.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class AliPayService extends BaseService {

	public List<Map<String, Object>> getAllConfig(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AliPay.getAllConfig");
		return baseServiceImpl.getList(map);
	}
	
	
	
}

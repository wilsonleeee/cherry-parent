package com.cherry.webservice.basis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BaCouponCodeService extends BaseService {

	/**
	 * 获取新后台中使用状态为未使用的优惠券码
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaCouponCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BaCouponCode.getBaCouponCode");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新同步状态
	 * @param map
	 * @return
	 */
	public int updCouponSynchFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BaCouponCode.updCouponSynchFlag");
		return baseServiceImpl.update(paramMap);
	}
	
}

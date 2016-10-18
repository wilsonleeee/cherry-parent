package com.cherry.bs.emp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLBSEMP08_Service extends BaseService {

	/**
	 * 取得当前批次的代理商优惠券数量
	 * @param map
	 * @return
	 */
	public int getBaCouponCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP08.getBaCouponCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得当前批次的代理商优惠券List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaCouponList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP08.getBaCouponList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 删除代理商优惠券
	 * @param map
	 * @return
	 */
	public int deleteBaCoupon(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP08.deleteBaCoupon");
		return baseServiceImpl.remove(paramMap);
	}
}

package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

public interface Coupon_IF {
	
	/**
	 * 查询会员优惠券信息
	 * 
	 * @param map 订单信息
	 * @return 会员优惠券List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getCouponList(Map<String, Object> map) throws Exception;
	
	/**
	 * 检验优惠券
	 * 
	 * @param map 订单及优惠券
	 * @return 检验结果
	 */
	public Map<String, Object> checkCoupon(Map<String, Object> map);
	
	/**
	 * 查询会员代物券信息
	 * 
	 * @param map 订单信息
	 * @return 会员代物券信息
	 * @throws Exception 
	 */
	public Map<String, Object> getDwqInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 检验代物券
	 * 
	 * @param map 订单及优惠券
	 * @return 检验结果
	 */
	public Map<String, Object> checkDwq(Map<String, Object> map);
	
	/**
	 * 发券查询
	 * 
	 * @param map 订单信息
	 * @return 优惠券活动List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getCouponRuleList(Map<String, Object> map) throws Exception;
	
	/**
	 * 生成券
	 * 
	 * @param map 订单及优惠券
	 * @return 处理结果
	 * @throws Exception 
	 */
	public Map<String, Object> tran_createCoupon(Map<String, Object> map) throws Exception;
}

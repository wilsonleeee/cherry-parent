package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 家化优惠券推送Service
 * 
 * @author WangCT
 * @version 2015-08-18 1.0.0
 */
public class BINBESSPRM08_Service extends BaseService {
	
	/**
	 * 获取券活动列表
	 * 
	 * @param map 基本参数
	 * @return List 券活动列表
	 */
	public List<Map<String, Object>> getCouponRuleList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBESSPRM08.getCouponRuleList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 获取券活动列表（去重）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCouponRuleDistinctList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBESSPRM08.getCouponRuleDistinctList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 取得优惠券总数
	 * 
	 * @param map 检索条件
	 * @return 优惠券总数
	 */
	public int getCouponCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM08.getCouponCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取优惠券列表
	 * 
	 * @param map 基本参数
	 * @return List 优惠券列表
	 */
	public List<Map<String, Object>> getCouponList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBESSPRM08.getCouponList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新推送结果
	 * 
	 * @param List 优惠券列表
	 * @return 
	 */
	public void updateSendResultList(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBESSPRM08.updateSendResult");
	}
	
	
	/**
	 * 获取优惠券状态变更列表
	 * 
	 * @param map 基本参数
	 * @return List 优惠券列表
	 */
	public List<Map<String, Object>> getCouponUpdateList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBESSPRM08.getCouponUpdateList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 获取对应到产品的券信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCouponBycart(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBESSPRM08.getCouponBycart");
		return baseServiceImpl.getList(map);
	}
}

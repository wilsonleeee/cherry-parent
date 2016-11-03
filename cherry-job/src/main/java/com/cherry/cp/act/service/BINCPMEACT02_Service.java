package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员活动预约单下发Service
 * @author lipc
 *
 */
@SuppressWarnings("unchecked")
public class BINCPMEACT02_Service extends BaseService{
	
	/**
	 * 取得需要下发的预约单据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOrderList(Map<String, Object> map){
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT02.getOrderList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 
	 * 更新预约单据下发区分
	 * 
	 * @param list 更新条件
	 * 
	 */	
	public void updCampOrder(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT02.updCampOrder");
	}
	
	/**
	 * 删除活动预约信息(接口主表)数据
	 * 
	 * @param list
	 * 
	 */
	public void delCouponMain(List<Map<String, Object>> list) {
		ifServiceImpl.deleteAll(list, "BINCPMEACT02.delCouponMain");
	}
	
	/**
	 * 删除活动预约信息(接口明细表)数据
	 * @param list
	 */
	public void delCouponDetail(List<Map<String, Object>> list){
		ifServiceImpl.deleteAll(list, "BINCPMEACT02.delCouponDetail");
	}
	
	/**
	 * 新增活动预约信息(接口主表)数据 
	 * @param map
	 */
	public void addCouponMain(List<Map<String, Object>> list){
		ifServiceImpl.saveAll(list, "BINCPMEACT02.addCouponMain");
	}
	
	/**
	 * 新增活动预约信息(接口明细表)数据
	 * @param map
	 */
	public void addCouponDetail(List<Map<String, Object>> list){
		ifServiceImpl.saveAll(list, "BINCPMEACT02.addCouponDetail");
	}
}

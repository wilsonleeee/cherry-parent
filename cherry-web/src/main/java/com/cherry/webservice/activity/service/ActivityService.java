package com.cherry.webservice.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class ActivityService extends BaseService{
	
	/**
	 * 取得会员活动LIST信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getActResultList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getActResultList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员活动LIST信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getActivityList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getActivityList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员活动地点LIST信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPlaceList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getPlaceList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员活动柜台by城市
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPlaceListByCity(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getPlaceListByCity");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 取得会员活动柜台by渠道
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPlaceListByChannel(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getPlaceListByChannel");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌促销活动
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPromotionList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getPromotionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌促销礼品
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPrmGiftList(int ruleId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ruleId", ruleId);
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getPrmGiftList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得预约单据
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getOrderList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getOrderList");
		return baseServiceImpl.getList(map);
	}
	
	public int getOrderCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getOrderCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得预约时间段
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getOrderTimeRange(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getOrderTimeRange");
		return baseServiceImpl.getList(map);
	}
	

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCampHistoryList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getCampHistoryList");
		return baseServiceImpl.getList(map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCampHistoryCodeList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getCampHistoryCodeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得单据状态
	 * @param map
	 * @return
	 */
	public String getOrderState(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getOrderState");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新单据状态
	 * @param map
	 * @return
	 */
	public int changeOrderState(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.changeOrderState");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得虚拟促销品
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getDHCPInfoList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getDHCPInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得虚拟促销品
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getPrmInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getPrmInfo");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 修改预约单据
	 * @param map
	 * @return
	 */
	public int changeOrder(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.changeOrder");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新活动礼品库存
	 * @param map
	 * @return
	 */
	public void updCampaignStock(List<Map<String,Object>> list){
		baseServiceImpl.updateAll(list, "ActivityInfo.updCampaignStock");
	}
	
	/**
	 * 取得活动礼品库存
	 * @param map
	 * @return
	 */
	public Integer getCampaignStock(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.getCampaignStock");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新预约单据
	 * @param map
	 * @return
	 */
	public int updateOrderInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "ActivityInfo.updateOrderInfo");
		return baseServiceImpl.update(map);
	}
}

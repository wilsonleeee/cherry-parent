package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cp.common.CampConstants;

public class BINOLCPACT06_Service extends BaseService{
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 根据活动档次mainCode取得主题活动信息
	 * @return
	 */
	public Map<String,Object> getCampInfo(String campCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CampConstants.CAMP_CODE, campCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getCampInfo");
        return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	 /**
	 * 活动结果List
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCampOrderList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getCampOrderList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 活动结果数
	 * 
	 * @param map
	 * @return
	 */
	public int getOrderCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getOrderCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 活动Excel导出数量
	 * 
	 * @param map
	 * @return
	 */
	public int getExcelCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getExcelCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 活动Excel导出coupon数量
	 * 
	 * @param map
	 * @return
	 */
	public int getExcelCouponCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getExcelCouponCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
     *  活动明细基本信息
     * 
     */
    public Map<String,Object> getCampDetailMap(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLCPACT06.getCampDetailMap");
    }
    /**
     * 更新活动明细
     * 
     * @param map
     * @return
     */
    public int updateDetailInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.updateDetailInfo");
        return baseServiceImpl.update(map);
    }
        
    /**
     * 取得柜台Id
     * @param map
     * @return
     */
    public Object getCounterId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getCounterId");
		return baseServiceImpl.get(paramMap);
	}
	 /**
	 * 礼品信息List
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.getPrtInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
    /**
     *  取得单据信息getEditInfo
     * 
     */
    public Map<String,Object> getTradeNo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLCPACT06.getTradeNo");
    }
    /**
     *  取得编辑后的信息
     * 
     */
    public Map<String,Object> getEditInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLCPACT06.getEditInfo");
    }
    
    /**
     *  提前或延后的领用开始日期
     * 
     */
    public String getDateAddFrom(Map<String,Object> map){
        return (String)baseServiceImpl.get(map, "BINOLCPACT06.getDateAddFrom");
    }
    
    /**
     *  提前或延后的领用结束日期
     * 
     */
    public String getDateAddTo(Map<String,Object> map){
        return (String)baseServiceImpl.get(map, "BINOLCPACT06.getDateAddTo");
    }
    
    /**
	 * 更新会员活动预约表
	 * @param list
	 */
	public void batchUpdCampOrder(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINOLCPACT06.batchUpdCampOrder");
	}
	
	
	/**
	 * 会员发开柜台作为领用柜台
	 * @param map
	 * @return
	 */
	public int updCounterIssue(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.updCounterIssue");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 会员预约柜台作为领用柜台
	 * @param map
	 * @return
	 */
	public int updResCounter(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.updResCounter");
		return baseServiceImpl.update(parameterMap);
	}

	/**
	 * 会员预约柜台作为领用柜台
	 * @param map
	 * @return
	 */
	public int updCampOrder(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.updCampOrder");
		return baseServiceImpl.update(map);
	}

	/**
	 * 会员预约柜台作为领用柜台
	 * @param map
	 * @return
	 */
	public int updCampOrderHis(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT06.updCampOrderHis");
		return baseServiceImpl.update(map);
	}
}

package com.cherry.ct.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员沟通效果统计Service
 * @author menghao
 *
 */
public class BINOLCTRPT06_Service extends BaseService{
	
	/**
     * 获取会员沟通效果统计记录总数
     * 
     * @param map
     * 
     */
	public int getMemCommunStatisticsCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT06.getMemCommunStatisticsCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
     *  获取会员沟通效果统计记录List
     * 
     * @param map
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMemCommunStatisticsList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT06.getMemCommunStatisticsList");
		return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取指定沟通的会员销售情况记录总数
     * 
     * @param map
     * 
     */
	public int getCommunEffectDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT06.getCommunEffectDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
     * 获取指定沟通的会员销售情况记录List
     * 
     * @param map
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCommunEffectDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT06.getCommunEffectDetailList");
		return baseServiceImpl.getList(paramMap);
    }
	
	/**
	 * 获取会员沟通效果统计信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMemCommunResultInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT06.getMemCommunResultInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	
}

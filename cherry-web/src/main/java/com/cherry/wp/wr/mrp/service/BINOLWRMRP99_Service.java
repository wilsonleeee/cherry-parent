package com.cherry.wp.wr.mrp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员报表Service
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP99_Service extends BaseService {
	
	/**
	 * 获取会员数量
	 * 
	 * @param map 查询条件
	 * @return 会员数量
	 */
	public int getMemberCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getMemberCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取会员列表List
	 * 
	 * @param map 查询条件
	 * @return 会员列表List
	 */
	public List<Map<String, Object>> getMemberList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getMemberList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取会员销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 会员销售统计信息
	 */
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getSaleCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取礼券使用数量
	 * 
	 * @param map 查询条件
	 * @return 礼券使用数量
	 */
	public int getGiftDrawCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getGiftDrawCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取礼券使用统计信息
	 * 
	 * @param map 查询条件
	 * @return 礼券使用统计信息
	 */
	public Map<String, Object> getGiftDrawCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getGiftDrawCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取礼券使用信息List
	 * 
	 * @param map 查询条件
	 * @return 礼券使用信息List
	 */
	public List<Map<String, Object>> getGiftDrawList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getGiftDrawList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取无会员资料的销售记录总数
	 * 
	 * @param map 查询条件
	 * @return 无会员资料的销售记录总数
	 */
	public int getMemSaleCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getMemSaleCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取无会员资料的销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 无会员资料的销售统计信息
	 */
	public Map<String, Object> getMemSaleCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getMemSaleCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取无会员资料的销售记录List
	 * 
	 * @param map 查询条件
	 * @return 无会员资料的销售记录List
	 */
	public List<Map<String, Object>> getMemSaleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getMemSaleList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取会员积分兑换记录总数
	 * 
	 * @param map 查询条件
	 * @return 会员积分兑换记录总数
	 */
	public int getPxCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getPxCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取会员积分兑换统计信息
	 * 
	 * @param map 查询条件
	 * @return 会员积分兑换统计信息
	 */
	public Map<String, Object> getPxCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getPxCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取会员积分兑换记录List
	 * 
	 * @param map 查询条件
	 * @return 会员积分兑换记录List
	 */
	public List<Map<String, Object>> getPxInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRMRP99.getPxInfoList");
		return baseServiceImpl.getList(paramMap);
	}

}

package com.cherry.wp.wr.srp.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售报表Service
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRSRP99_Service extends BaseService {
	
	/**
	 * 获取BA销售数量
	 * 
	 * @param map 查询条件
	 * @return BA销售数量
	 */
	public int getBASaleCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getBASaleCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取BA销售统计信息
	 * 
	 * @param map 查询条件
	 * @return BA销售统计信息
	 */
	public Map<String, Object> getBASaleCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getBASaleCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取BA销售List
	 * 
	 * @param map 查询条件
	 * @return BA销售List
	 */
	public List<Map<String, Object>> getBASaleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getBASaleList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取销售记录单数量
	 * 
	 * @param map 查询条件
	 * @return 销售记录单数量
	 */
	public int getSaleRecordCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleRecordCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售统计信息
	 */
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取销售记录list
	 * 
	 * @param map 查询条件
	 * @return 销售记录list
	 */
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	public List<Map<String, Object>> getSaleRecordListWithoutPage(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleRecordListWithoutPage");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取销售明细记录单数量
	 * 
	 * @param map 查询条件
	 * @return 销售明细记录单数量
	 */
	public int getSaleRecordDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleRecordDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取销售明细记录list
	 * 
	 * @param map 查询条件
	 * @return 销售明细记录list
	 */
	public List<Map<String, Object>> getSaleRecordDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleRecordDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 按分类统计销售
	 * 
	 * @param map 查询条件
	 * @return 统计结果list
	 */
	public List<Map<String, Object>> getSaleCountByClass(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleCountByClass");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取销售分类明细数量
	 * 
	 * @param map 查询条件
	 * @return 销售分类明细数量
	 */
	public int getClassDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getClassDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取销售分类明细统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售分类明细统计信息
	 */
	public Map<String, Object> getClassDetaiCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getClassDetaiCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取销售分类明细List
	 * 
	 * @param map 查询条件
	 * @return 销售分类明细List
	 */
	public List<Map<String, Object>> getClassDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getClassDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取商品销售排行数量
	 * 
	 * @param map 查询条件
	 * @return 商品销售排行数量
	 */
	public int getSaleByPrtCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByPrtCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取商品销售排行统计信息
	 * 
	 * @param map 查询条件
	 * @return 商品销售排行统计信息
	 */
	public Map<String, Object> getSaleByPrtCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByPrtCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取商品销售排行List
	 * 
	 * @param map 查询条件
	 * @return 商品销售排行List
	 */
	public List<Map<String, Object>> getSaleByPrtList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByPrtList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取会员销售排行数量
	 * 
	 * @param map 查询条件
	 * @return 会员销售排行数量
	 */
	public int getSaleByMemCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByMemCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取会员销售排行统计信息
	 * 
	 * @param map 查询条件
	 * @return 会员销售排行统计信息
	 */
	public Map<String, Object> getSaleByMemCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByMemCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取会员销售排行List
	 * 
	 * @param map 查询条件
	 * @return 会员销售排行List
	 */
	public List<Map<String, Object>> getSaleByMemList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByMemList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取柜台的销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 柜台的销售统计信息
	 */
	public Map<String, Object> getSaleByDayCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByDayCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}

	/**
	 * 获取支付构成报表List
	 *
	 * @param map 查询条件
	 * @return 支付构成报表List
	 */
	public List<Map<String, Object>> getPayTypeSaleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getPayTypeSaleList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 获取支付构成报表统计信息
	 *
	 * @param map 查询条件
	 * @return 支付构成报表统计信息
	 */
	public Map<String, Object> getPayTypeCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getPayTypeCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}

	/**
	 * 获取柜台每天的销售统计信息List
	 * 
	 * @param map 查询条件
	 * @return 柜台每天的销售统计信息List
	 */
	public List<Map<String, Object>> getSaleByDayList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getSaleByDayList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取柜台每天的会员入会数量List
	 * 
	 * @param map 查询条件
	 * @return 柜台每天的会员入会数量List
	 */
	public List<Map<String, Object>> getMemCountByDayList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getMemCountByDayList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 获取门店排行List
	 * 
	 * @param map 查询条件
	 * @return 门店排行List
	 */
	public List<Map<String, Object>> getStoreRankingList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getStoreRankingList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 获取门店排行统计信息
	 * 
	 * @param map 查询条件
	 * @return 门店排行统计信息
	 */
	public Map<String, Object> getStoreRankingCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getStoreRankingCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	/**
	 * 获英雄榜List
	 * 
	 * @param map 查询条件
	 * @return 英雄榜List
	 */
	public List<Map<String, Object>> getHeroList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getHeroList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 获取英雄榜统计信息
	 * 
	 * @param map 查询条件
	 * @return 英雄榜统计信息
	 */
	public Map<String, Object> getHeroCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRSRP99.getHeroCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
}

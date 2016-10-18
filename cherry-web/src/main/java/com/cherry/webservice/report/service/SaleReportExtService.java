package com.cherry.webservice.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 销售报表Service
 * 
 * @author WangCT
 * @version 1.0 2015.06.16
 */
public class SaleReportExtService extends BaseService {
	
	/**
	 * 统计每天的销售金额和数量
	 * 
	 * @param map 查询条件
	 * @return 每天的销售金额和数量
	 */
	public List<Map<String, Object>> getSaleCountByDay(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getSaleCountByDay");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得BAS管辖柜台的销售统计List（当月）
	 * 
	 * @param map 查询条件
	 * @return BAS管辖柜台的销售统计List（当月）
	 */
	public List<Map<String, Object>> getBASCouSaleByMonthList(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getBASCouSaleByMonthList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得BAS管辖柜台的销售统计List（当天）
	 * 
	 * @param map 查询条件
	 * @return BAS管辖柜台的销售统计List（当天）
	 */
	public List<Map<String, Object>> getBASCouSaleByDayList(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getBASCouSaleByDayList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 统计柜台每天的销售金额和数量
	 * 
	 * @param map 查询条件
	 * @return 柜台每天的销售金额和数量
	 */
	public List<Map<String, Object>> getCouSaleCountByDay(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getCouSaleCountByDay");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得柜台销售排名
	 * 
	 * @param map 查询条件
	 * @return 柜台销售排名
	 */
	public Map<String, Object> getCouSaleRanking(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getCouSaleRanking");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 柜台的销售统计（按BA统计）
	 * 
	 * @param map 查询条件
	 * @return 柜台的销售统计（按BA统计）
	 */
	public List<Map<String, Object>> getCouSaleByBA(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getCouSaleByBA");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 柜台的销售统计（按产品统计）
	 * 
	 * @param map 查询条件
	 * @return 柜台的销售统计（按产品统计）
	 */
	public List<Map<String, Object>> getCouSaleByPro(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getCouSaleByPro");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询柜台的销售明细
	 * 
	 * @param map 查询条件
	 * @return 柜台的销售明细
	 */
	public List<Map<String, Object>> getCouSaleList(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getCouSaleList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 柜台的销售统计
	 * 
	 * @param map 查询条件
	 * @return 柜台的销售统计
	 */
	public Map<String, Object> getCouSaleCount(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getCouSaleCount");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 按分类统计销售
	 * 
	 * @param map 查询条件
	 * @return 分类统计结果
	 */
	public List<Map<String, Object>> getSaleCountByClass(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getSaleCountByClass");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 柜台的销售分类明细
	 * 
	 * @param map 查询条件
	 * @return 销售分类明细结果
	 */
	public List<Map<String, Object>> getSaleCategoryDetail(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getSaleCategoryDetail");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 按时间统计销售金额和数量
	 * 
	 * @param map 查询条件
	 * @return 没时间段的销售金额和数量
	 */
	public List<Map<String, Object>> getSaleByHours(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReportExt.getSaleByHours");
		return baseServiceImpl.getList(paramMap);
	}

}

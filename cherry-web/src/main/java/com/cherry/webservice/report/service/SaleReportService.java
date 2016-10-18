package com.cherry.webservice.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 销售报表Service
 * 
 * @author WangCT
 * @version 1.0 2014.07.29
 */
public class SaleReportService extends BaseService {
	
	/**
	 * 统计每天的销售金额和数量
	 * 
	 * @param map 查询条件
	 * @return 每天的销售金额和数量
	 */
	public List<Map<String, Object>> getSaleCountByDay(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleCountByDay");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得销售目标
	 * 
	 * @param map 查询条件
	 * @return 销售目标
	 */
	public Map<String, Object> getSaleTargetAmount(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleTargetAmount");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得销售排名
	 * 
	 * @param map 查询条件
	 * @return 销售排名
	 */
	public Integer getSaleRanking(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleRanking");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得BAS总数
	 * 
	 * @param map 查询条件
	 * @return BAS总数
	 */
	public Integer getBASCount(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getBASCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 根据BAS代码取得员工ID
	 * 
	 * @param map 查询条件
	 * @return 员工ID
	 */
	public String getEmployeeId(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getEmployeeId");
		return (String)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getBASCouSaleByMonthList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得有效柜台总数
	 * 
	 * @param map 查询条件
	 * @return 有效柜台总数
	 */
	public Integer getCounterCount(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getCounterCount");
		return baseServiceImpl.getSum(paramMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getBASCouSaleByDayList");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getCouSaleCountByDay");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getCouSaleRanking");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据柜台号取得部门ID
	 * 
	 * @param map 查询条件
	 * @return 部门ID
	 */
	public String getOrganizationId(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getOrganizationId");
		return (String)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getCouSaleByBA");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getCouSaleByPro");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getCouSaleList");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getCouSaleCount");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得购买会员数
	 * 
	 * @param map 查询条件
	 * @return 购买会员数
	 */
	public Integer getSaleMemCount(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleMemCount");
		return baseServiceImpl.getSum(paramMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleCountByClass");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleCategoryDetail");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleByHours");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得大区、办事处LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionOfficeList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getRegionOfficeList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据区域部门code取得部门ID
	 * 
	 * @param map 查询条件
	 * @return 部门ID
	 */
	public String getOrganizationIdByRegionCode(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getOrganizationIdByRegionCode");
		return ConvertUtil.getString(baseServiceImpl.get(paramMap));
	}
	
	/**
	 * 根据区域部门code取得部门信息 
	 * 
	 * @param map 查询条件
	 * @return 部门ID
	 */
	public Map<String, Object> getDepartInfoByRegionCode(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getDepartInfoByRegionCode");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得指定大区、办事处下的柜台销售情况（月报）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionCouSaleByMonthList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getRegionCouSaleByMonthList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得指定大区、办事处下的柜台销售情况（日报）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionCouSaleByDayList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getRegionCouSaleByDayList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得指定大区或者大区下的办事处的销售统计情况
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionSaleRankingList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getRegionSaleRankingList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询指定财务月的最小最大自然日及天数
	 * 
	 * @param map 查询条件
	 * @return 指定财务月的最小最大自然日
	 */
	public Map<String, Object> getFiscalMonthLen(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getFiscalMonthLen");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}

}

package com.cherry.webservice.report.interFaces;

import java.util.Map;

public interface SaleReport_IF {
	
	/**
	 * BAS的月销售统计
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getBASSaleCount(Map<String, Object> map);
	
	/**
	 * BAS管辖柜台的销售统计（当月）
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getBASCouSaleByMonth(Map<String, Object> map);
	
	/**
	 * BAS管辖柜台的销售统计（当天）
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getBASCouSaleByDay(Map<String, Object> map);
	
	/**
	 * 柜台的月销售统计
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getCouSaleCount(Map<String, Object> map);
	
	/**
	 * 柜台的销售统计（按BA和产品统计）
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getCouSaleDetailCount(Map<String, Object> map);
	
	/**
	 * 柜台的销售明细
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getCouSaleList(Map<String, Object> map);
	
	/**
	 * 柜台的销售分类报表
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getSaleCategoryCount(Map<String, Object> map);
	
	/**
	 * 柜台的销售分类明细报表
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getSaleCategoryDetail(Map<String, Object> map);
	
	/**
	 * BAS的天销售统计(按小时统计)
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getBASSaleCountByDay(Map<String, Object> map);
	
	/**
	 * 柜台的销售统计(按天统计)
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getCouSaleCountByDay(Map<String, Object> map);
	
	/**
	 * 获取服务器时间
	 * 
	 * @param map 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getSysDateTime(Map<String, Object> map);
	
	/**
	 * 获取大区、城市
	 * @param map
	 * @return
	 */
	Map<String, Object> getRegionOfficeList(Map<String, Object> map);
	
	/**
	 * 区域柜台的销售统计（月报）
	 * @param map
	 * @return
	 */
	Map<String, Object> getRegionCouSaleByMonth(Map<String, Object> map);

	/**
	 * 区域柜台的销售统计（日报）
	 * @param map
	 * @return
	 */
	Map<String, Object> getRegionCouSaleByDay(Map<String, Object> map);
	
	/**
	 * 区域办事处销售统计排名
	 * @param map
	 * @return
	 */
	Map<String, Object> getRegionSaleRanking(Map<String, Object> map);
}

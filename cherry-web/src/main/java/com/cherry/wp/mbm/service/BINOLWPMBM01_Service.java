package com.cherry.wp.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员管理Service
 * 
 * @author WangCT
 * @version 1.0 2014/08/15
 */
public class BINOLWPMBM01_Service extends BaseService {
	
	/**
	 * 按天统计会员的购买金额
	 * 
	 * @param map 查询条件
	 * @return 不同天的购买金额List
	 */
	public List<Map<String, Object>> getSaleAmountByDay(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPMBM01.getSaleAmountByDay");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 按天统计会员的积分
	 * 
	 * @param map 查询条件
	 * @return 不同天的积分List
	 */
	public List<Map<String, Object>> getPointByDay(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPMBM01.getPointByDay");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 统计不同产品类别的销售数量和金额
	 * 
	 * @param map 查询条件
	 * @return 不同产品类别的销售数量和金额List
	 */
	public List<Map<String, Object>> getSaleCountByProCat(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPMBM01.getSaleCountByProCat");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据柜台号取得柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCouInfoByCouId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPMBM01.getCouInfoByCouId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据员工ID查询员工信息
	 * 
	 * @param map 查询条件
	 * @return 员工信息
	 */
	public Map<String, Object> getEmpInfoByEmpId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPMBM01.getEmpInfoByEmpId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取短信模版
	 * @param map
	 * @return
	 */
	public String getContents(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPMBM01.getContents");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 获取短信CouponCode
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCouponCodeList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPMBM01.getCouponCodeList");
		return baseServiceImpl.getList(map);
	}
}

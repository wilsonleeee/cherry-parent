package com.cherry.mb.rpt.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员连带销售报表Service
 * 
 * @author hub
 * @version 1.0 2016-04-12
 */
public class BINOLMBRPT12_Service extends BaseService{
	
	/**
	 * 取得产品总数
	 * @param map
	 * @return
	 */
	public int getProductInfoCount (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getProductInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得产品信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getProductInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询连带产品总数
	 * @param map
	 * @return
	 */
	public int getJointProductInfoCount (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointProductInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询连带产品信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointProductInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointProductInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询连带产品总数(根据大类ID)
	 * @param map
	 * @return
	 */
	public int getJointPrtByCateIdCount (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointPrtByCateIdCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询连带产品信息(根据大类ID)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointPrtByCateIdList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointPrtByCateIdList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 查询大类总数
	 * @param map
	 * @return
	 */
	public int getCateCount (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getCateCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询大类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCateList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询连带大类总数
	 * @param map
	 * @return
	 */
	public int getJointCateCount (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointCateCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询连带大类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointCateList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询连带大类总数(根据大类ID)
	 * @param map
	 * @return
	 */
	public int getJointCateBycateIdCount (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointCateBycateIdCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询连带大类信息(根据大类ID)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJointCateBycateIdList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getJointCateBycateIdList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询会员总数
	 * @param map
	 * @return
	 */
	public int getMemberCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getRptMemberCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemberList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT12.getRptMemberList");
		return baseServiceImpl.getList(map);
	}
}

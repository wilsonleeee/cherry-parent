package com.cherry.bs.emp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 生成代理商优惠券Service
 * @author menghao
 * @version 1.0 2014-08-27
 */
public class BINOLBSEMP07_Service extends BaseService {

	/**
	 * 取得BA模式下的一览明细数量
	 * @param map
	 * @return
	 */
	public int getBaModelCouponCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getBaModelCouponCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得BA模式下的一览LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaModelCouponList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getBaModelCouponList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得批次模式下的一览数据量
	 * @param map
	 * @return
	 */
	public int getBatchCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getBatchCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得批次模式下的一览LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBatchList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getBatchList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 校验当前批次号是否已经存在
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBatchCodeByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getBatchCodeByCode");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得所有BA的ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSelectedResellerInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getSelectedResellerInfo");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 当前批次生成后还可生成的优惠券数量【即此批次后剩余优惠券资源】
	 * @param map
	 * @return
	 */
	@Deprecated
	public int getMaxBatchCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getMaxBatchCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 为指定代理商生成还未包含验证码的批次优惠券码
	 * 此方法为直接生成顺序码
	 * @param map
	 * @return
	 */
//	public List<Map<String, Object>> createCouponCodeForBa(Map<String, Object> map) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.putAll(map);
//		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.createCouponCodeForBa");
//		return baseServiceImpl.getList(paramMap);
//	}
	
	/**
	 * 取得已经存在的优惠券码中的随机码【用于限制不能生成重复随机码】
	 * @param map
	 * @return
	 */
	public List<Long> getExistRandom(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getExistRandom");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 为一个BA插入优惠券LIST
	 * @param list
	 */
	public void insertBaCouponList(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLBSEMP07.insertBaCouponList");
	}
	
	/**
	 * 取得已经同步的代理商优惠券数量
	 * @param map
	 * @return
	 */
	public int getSynchronizedCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.getSynchronizedCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 删除批次代理商优惠券
	 * @param map
	 */
	public void deleteBatchCoupon(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP07.deleteBatchCoupon");
		baseServiceImpl.remove(paramMap);
	}
}

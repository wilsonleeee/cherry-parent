package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLPTRPS32_IF extends BINOLCM37_IF {

	/**
	 * 取得代理商优惠券使用情况信息LIST的行数
	 * @param map
	 * @return
	 */
	public int getBaCouponUsedInfoCount(Map<String, Object> map);
	
	/**
	 * 取得代理商优惠券使用情况信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaCouponUsedInfoList(Map<String, Object> map);
	
	/**
	 * 取得库存汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
	/**
	 * 根据代理商code取得代理商名称
	 * @param map
	 * @return
	 */
	public Map<String, Object> getResellerNameFromCode(Map<String, Object> map);
	
	/**
	 * 取得指定代理商的优惠券的使用情况详细一览行数 
	 * @param map
	 * @return
	 */
	public int getCouponUsedDetailCount(Map<String, Object> map);
	
	/**
	 * 取得指定代理商的优惠券的使用情况详细一览LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCouponUsedDetailList(Map<String, Object> map);

	/**
	 * 获取代理商优惠券使用情况详细导出的map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	/**
	 * CSV导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportCsv(Map<String, Object> map) throws Exception;
}

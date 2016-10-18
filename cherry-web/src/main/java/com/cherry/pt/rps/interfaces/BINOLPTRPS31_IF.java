package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLPTRPS31_IF extends BINOLCM37_IF {

	/**
	 * 取得代理商提成统计信息LIST的行数
	 * @param map
	 * @return
	 */
	public int getBaCommissionCount(Map<String, Object> map);
	
	/**
	 * 取得代理商提成统计信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaCommissionList(Map<String, Object> map);
	
	/**
	 * 取得库存汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
	/**
	 * 根据代理商ID取得代理商名称
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBaNameFromId(Map<String, Object> map);
	
	/**
	 * 取得指定代理商的推荐会员购买信息的数量
	 * @param map
	 * @return
	 */
	public int getMemberBuyInfoCount(Map<String, Object> map);
	
	/**
	 * 取得指定代理商的推荐会员购买信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemberBuyInfoList(Map<String, Object> map);
	
	/**
	 * 获取代理商推荐会员购买详细导出的map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMemberBuyExportMap(Map<String, Object> map);
	
	/**
	 * 取得代理商推荐购买信息的数量
	 * @param map
	 * @return
	 */
	public int getBaSaleInfoCount(Map<String, Object> map);
	
	/**
	 * 取得代理商推荐购买信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaSaleInfoList(Map<String, Object> map);
	
	/**
	 * 获取代理商推荐购买详细导出的map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBaSaleExportMap(Map<String, Object> map);
	
	/**
	 * CSV导出共通
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportCsvCommon(Map<String, Object> map) throws Exception;
}

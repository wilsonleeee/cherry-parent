package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

public class BINOLCPACT01_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
	 *会员主题活动List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMainList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT01.getMainList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 会员主题活动数
	 * 
	 * @param map
	 * @return
	 */
	public int getMainCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT01.getMainCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 会员活动List
	 * 
	 * @param map 
	 * @returnList
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT01.getSubList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 会员活动数
	 * 
	 * @param map
	 * @return
	 */
	public int getSubCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT01.getSubCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 根据输入字符串模糊查询会员活动名称信息
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCampName(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCPACT01.getCampName");
	}
	
	/**
	 * 根据输入字符串模糊查询会员活动名称信息
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSubCampName(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCPACT01.getSubCampName");
	}
	/**
	 * 伦理删除主题活动
	 * @param map
	 * @return
	 */
	public int stopCampaign (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT01.stopCampaign");
		return baseServiceImpl.update(map);
	}
	/**
	 * 伦理删除活动
	 * @param map
	 * @return
	 */
	public int stopSubCampaign (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT01.stopSubCampaign");
		return baseServiceImpl.update(map);
	}
}

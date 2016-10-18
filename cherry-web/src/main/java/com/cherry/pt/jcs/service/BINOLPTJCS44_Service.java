package com.cherry.pt.jcs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLPTJCS44_Service  extends BaseService{
	public Map<String, Object> getDropCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS44.getDropCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	public List<Map<String, Object>> getDropList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS44.getDropList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据输入的字符串模糊查询出对应的产品信息
	 * 
	 * */
	public List<Map<String,Object>> getProductList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS44.getProductList");
		return baseServiceImpl.getList(map);
	}
	
	public int getProductCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS44.getProductCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 检查柜台是否有分配可用的方案
	 * 
	 * */
	public List<Map<String,Object>> chkCntSoluData(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLPTJCS44.chkCntSoluData");
	}
	
	/**
	 * 根据输入的字符串模糊查询出柜台对应的产品信息
	 * 
	 * */
	public List<Map<String,Object>> getCntProductList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS44.getCntProductList");
		return baseServiceImpl.getList(map);
	}
	
	public int getCntProductCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS44.getCntProductCount");
		return baseServiceImpl.getSum(map);
	}
	
}

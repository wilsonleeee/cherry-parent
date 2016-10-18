package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBMBM24_Service extends BaseService{
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
	 *会员关键属性导入一览List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemImportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM24.getMemImportList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 会员关键属性导入Count
	 * 
	 * @param map
	 * @return
	 */
	public int getCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM24.getCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 根据输入字符串模糊查询导入名称信息
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getImportName(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLMBMBM24.getImportName");
	}
	/**
	 *会员关键属性导入明细一览List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM24.getDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 会员关键属性导入明细Count
	 * 
	 * @param map
	 * @return
	 */
	public int getDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM24.getDetailCount");
		return baseServiceImpl.getSum(map);
	}
	 /**
     * 取得导入结果数量
     * 
     */
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLMBMBM24.getSumInfo");
    }
    /**
     * 取得查询条件
     * 
     */
    public Map<String,Object> getImportInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLMBMBM24.getImportInfo");
    }
}

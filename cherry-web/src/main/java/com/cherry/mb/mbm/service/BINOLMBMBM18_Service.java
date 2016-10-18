package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBMBM18_Service extends BaseService{
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
	 *会员档案导入一览List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemImportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM18.getMemImportList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 会员档案导入Count
	 * 
	 * @param map
	 * @return
	 */
	public int getCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM18.getCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 根据输入字符串模糊查询导入名称信息
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getImportName(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLMBMBM18.getImportName");
	}
	/**
	 *会员档案导入明细一览List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM18.getDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 会员档案导入明细Count
	 * 
	 * @param map
	 * @return
	 */
	public int getDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM18.getDetailCount");
		return baseServiceImpl.getSum(map);
	}
	 /**
     * 取得导入结果数量
     * 
     */
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLMBMBM18.getSumInfo");
    }
    /**
     * 取得查询条件
     * 
     */
    public Map<String,Object> getImportInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLMBMBM18.getImportInfo");
    }
}

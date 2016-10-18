package com.cherry.pl.scf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLPLSCF18_Service extends BaseService{
	/**
	 * 取得基本配置信息List
	 * 
	 * @param map 查询条件
	 * @return 基本配置信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSystemConfigList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF18.getSystemConfigList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 给某品牌添加默认的基本配置信息
	 * 
	 * @param map 添加内容
	 */
	@Deprecated
	public void addSystemConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF18.addSystemConfig");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 更新基本配置信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateSystemConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF18.updateSystemConfig");
		return baseServiceImpl.update(parameterMap);
	}

    /**
     * 取得Admin基本配置信息List
     * 
     * @param map 查询条件
     * @return 基本配置信息List
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAdminSystemConfigList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF18.getAdminSystemConfigList");
        return baseServiceImpl.getList(parameterMap);
    }	
	
	/**
     * 取得基本配置信息List
     * 
     * @param map 查询条件
     * @return 基本配置信息List
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDefaultSystemConfigList() {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF18.getDefaultSystemConfigList");
        return baseServiceImpl.getList(parameterMap);
    }
	
	/**
     * 删除基本配置信息
     * 
     * @param map 删除条件
     */
    public int delSystemConfig(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF18.delSystemConfig");
        return baseServiceImpl.remove(parameterMap);
    }
	
    /**
     * 插入基本配置信息
     * 
     * @param map 更新条件
     * @return 更新件数
     */
    public void insertSystemConfig(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF18.insertSystemConfig");
        baseServiceImpl.save(parameterMap);
    }
}

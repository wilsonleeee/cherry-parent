package com.cherry.mo.pmc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOPMC01_Service extends BaseService {

	/**
	 * 取得菜单组总数  
	 * @param map
	 * @return
	 */
	public int getMenuGrpCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getMenuGrpCount");
        return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得菜单组List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMenuGrpList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getMenuGrpList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 新增一条菜单组信息
	 * @param map
	 * @return
	 */
	public int addMenuGrp(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.addMenuGrp");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 取得菜单组信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMenuGrpInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getMenuGrpInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	/**
	 * 更新菜单组信息
	 * @param map
	 * @return
	 */
	public int updateMenuGrp(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.updateMenuGrp");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 根据菜单组ID取得其菜单配置
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMenuGrpConfig(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getMenuGrpConfig");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入复制的菜单组的菜单配置
	 * @param list
	 */
	public void insertMenuGrpConfig(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list,"BINOLMOPMC01.insertMenuGrpConfig");
	}
	
	/**
	 * 删除菜单组
	 * @param map
	 * @return
	 */
	public int deleteMenuGrp(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.deleteMenuGrp");
		return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 删除菜单分组配置表
	 * @param map
	 * @return
	 */
	public int deletePosMenuGrpConfig(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.deletePosMenuGrpConfig");
		return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 取得已下发的菜单组的下发柜台及其所属组织结构
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPublishOrganize(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getPublishOrganize");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得大区信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCOM01.getRegionList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得已下发的菜单组的下发柜台及其所属区域
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPosMenuRegion(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getPosMenuRegion");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得已下发的菜单组的下发柜台及其所属区域
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPosMenuChannel(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getPosMenuChannel");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查找指定分组名称的菜单分组
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMenuGrpByName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC01.getMenuGrpByName");
		return baseServiceImpl.getList(paramMap);
	}
	
}

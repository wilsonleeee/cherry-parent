package com.cherry.mo.pmc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOPMC02_Service extends BaseService {
	
	/**
	 * 取得菜单组基本信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPosMenuGrpInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC02.getPosMenuGrpInfo");
   	 	List<Map<String, Object>> list = baseServiceImpl.getList(paramMap);
   	 	if(list.size() > 0){
   	 		return list.get(0);
   	 	} else {
   	 		return null;
   	 	}
	}

	/**
	 * 取得分组菜单配置信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMenuGrpConfig(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC02.getMenuGrpConfig");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得菜单信息（用于快捷定位 ）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPosMenuInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC02.getPosMenuInfo");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得菜单组配置差分表中的信息(用于判断更改勾选状态的节点是否为个性配置菜单)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGrpConfigList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC02.getGrpConfigList");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 删除分组菜单配置差分表
	 * @param map
	 * @return
	 */
	public int delPosMenuGrpConfig(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC02.delPosMenuGrpConfig");
   	 	return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 插入分组菜单配置差分表
	 * @param map
	 */
	public void insertPosMenuGrpConfig(List<Map<String, Object>> list) {
   	 	baseServiceImpl.saveAll(list,"BINOLMOPMC02.insertPosMenuGrpConfig");
	}
	
	/**
	 * 删除因品牌菜单管理表改变而造成的多余的菜单组的菜单配置
	 * @param map
	 */
	public void refreshPosMenuGrpConfig(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
   	 	paramMap.putAll(map);
   	 	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPMC02.refreshPosMenuGrpConfig");
   	 	baseServiceImpl.remove(paramMap);
	}
}

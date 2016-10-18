package com.cherry.mo.pmc.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOPMC02_IF extends ICherryInterface {
	
	/**
	 * 刷新菜单分组的菜单配置
	 * @param map
	 * @throws Exception
	 */
	public void refreshPosMenuConfig(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得菜单组基本信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPosMenuGrpInfo(Map<String, Object> map) throws Exception;

	/**
	 * 取得菜单组配置树结构信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuGrpConfigTree(Map<String, Object> map) throws Exception;

	/**
	 * 取得菜单信息（用于快捷定位 ）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getPosMenuInfo(Map<String, Object> map) throws Exception;
	/**
	 * 保存菜单组个性菜单配置
	 * @param map
	 * @param diffentList ： 与品牌菜单配置有差异的菜单
	 * @throws Exception
	 */
	public void tran_saveMenuGrpConfig(Map<String, Object> map,List<Map<String, Object>> diffentList) throws Exception;
}

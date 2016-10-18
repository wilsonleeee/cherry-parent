package com.cherry.mo.pmc.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOPMC01_IF extends ICherryInterface {

	/**
	 * 取得菜单组总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getMenuGrpCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得菜单组List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuGrpList(Map<String, Object> map) throws Exception;
	
	/**
	 * 新增一条菜单组信息
	 * @param map
	 * @throws Exception
	 */
	public void tran_addMenuGrp(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据菜单组ID取得菜单组信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMenuGrpInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新菜单组信息
	 * @param map
	 * @throws Exception
	 */
	public void tran_updateMenuGrp(Map<String, Object> map) throws Exception;
	
	/**
	 * 复制菜单组及其菜单配置
	 * @param map
	 * @throws Exception
	 */
//	public void tran_copyMenuGrpAndConfig(Map<String, Object> map) throws Exception;
	
	/**
	 * 删除菜单组
	 * @param map
	 * @throws Exception
	 */
	public void tran_deleteMenuGrp(Map<String, Object> map) throws Exception;

	/**
	 * 取得已下发菜单组的下发柜台及其所属区域
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPosMenuRegion(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得已下发菜单组的下发柜台及其所属大区
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPosMenuChannel(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得已下发菜单组的下发柜台及其所属组织结构
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPosMenuOrganize(Map<String, Object> map) throws Exception;
	
	/**
	 * 查找是否存在指定分组名称的菜单分组
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuGrpByName(Map<String, Object> map) throws Exception;
}

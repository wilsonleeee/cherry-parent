package com.cherry.synchro.mo.interfaces;

import java.util.Map;

import com.cherry.cm.core.CherryException;

public interface PosMenuSynchro_IF {

	/**
	 * 下发品牌柜台菜单配置差分表
	 * @param param
	 * @throws CherryException
	 */
	public void publishPosMenuBrandCounter(Map param) throws CherryException;
	
	/**
	 * 删除指定柜台组的品牌柜台菜单配置差分表信息
	 * @param param
	 * @throws CherryException
	 */
	public void delPosMenuBrandCounter(Map param) throws CherryException;
	
	/**
	 * 增加菜单
	 * @param param
	 * @throws CherryException
	 */
	public void addPosMenu(Map param) throws CherryException;
	
	/**
	 * 更新菜单
	 * @param param
	 * @throws CherryException
	 */
	public void updPosMenuBrand(Map param) throws CherryException;
	
	/**
	 * 增加品牌菜单管理信息
	 * @param param
	 * @throws CherryException
	 */
	public void addPosMenuBrand(Map param) throws CherryException;
	
}

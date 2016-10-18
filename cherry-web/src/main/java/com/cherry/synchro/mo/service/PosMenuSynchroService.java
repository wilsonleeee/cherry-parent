package com.cherry.synchro.mo.service;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.synchro.common.BaseSynchroService;

public class PosMenuSynchroService {
	@Resource(name="baseSynchroService")
	private BaseSynchroService baseSynchroService;
	
	/**
	 * POS品牌柜台菜单配置差分下发
	 * @param param
	 */
	public void publishPosMenuBrandCounter(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PosMenuSynchro.publishPosMenuBrandCounter");
		baseSynchroService.execProcedure(param);
	}
	
	/**
	 * 删除指定菜单组原来配置的柜台配置信息
	 * @param param
	 */
	public void delPosMenuBrandCounter(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PosMenuSynchro.delPosMenuBrandCounter");
		baseSynchroService.execProcedure(param);
	}
	
	/**
	 * 新增新的菜单项
	 * @param param
	 */
	public void addPosMenu(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PosMenuSynchro.addPosMenu");
		baseSynchroService.execProcedure(param);
	}
	
	public void addPosMenuBrand(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PosMenuSynchro.addPosMenuBrand");
		baseSynchroService.execProcedure(param);
	}
	
	/**
	 * 更新菜单
	 * @param param
	 */
	public void updPosMenuBrand(Map param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "PosMenuSynchro.updPosMenuBrand");
		baseSynchroService.execProcedure(param);
	}
}

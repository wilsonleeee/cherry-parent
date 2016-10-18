/*	
 * @(#)BINOLSSCM01_Service.java     1.0 2010/10/29		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.ss.common.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * @author dingyc
 *
 */
public class BINOLSSCM01_Service extends BaseService{
	
	/**
	 * 取得指定仓库中指定促销品的库存数量
	 * @param map
	 * @return
	 */
	public List getStockCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.getStockCount");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 插入库存操作流水表，返回一个流水ID
	 * @param map
	 * @return
	 */
	public int insertPromotionInventoryLog(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.insertPromotionInventoryLog");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 取得收发货单据信息(主表和明细)
	 * @param pramap
	 * @return
	 */
	public List<Map<String, Object>> getPromotionDeliverAllInfo(Map<String,Object> pramap){
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.getPromotionDeliverAllInfo");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
		return ret;		
	}	
	
	/**
	 * 插入入出库总表，返回总表ID
	 * @param map
	 * @return
	 */
	public int insertPromotionStockInOutMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.insertPromotionStockInOutMain");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 向入出库明细表中插入一条数据
	 * @param map
	 * @return
	 */
	public void insertPromotionStockDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.insertPromotionStockDetail");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 根据增量更新库存表
	 * @param map
	 * @return
	 */
	public int updatePromotionStockByIncrement (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.updatePromotionStockByIncrement");		
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 向库存表中插入一条数据
	 * @param map
	 */
	public void insertPromotionStock (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.insertPromotionStock");		
		 baseServiceImpl.save(map);
	}	
	
	/**
	 * 取得入出库表信息(主表和明细)
	 * @param pramap
	 * @return
	 */
	public List<Map<String, Object>> getPromotionStockInOutAllInfo(Map<String,Object> pramap){
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.getPromotionStockInOutAllInfo");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
		return ret;		
	}
	
	/**
	 * 取得调拨单信息(主表和明细)
	 * @param pramap
	 * @return
	 */
	public List<Map<String, Object>> getPromotionAllocationAllInfo(Map<String,Object> pramap){
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.getPromotionAllocationAllInfo");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
		return ret;		
	}
	//==================================
	/**
	 * 更新收发货单主表
	 * @param map
	 * @return
	 */
	public int updatePrmDeliverMain (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.updatePrmDeliverMain");		
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得部门名称
	 * 
	 * */
	public String getDepartName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.getDepartName");	
		return (String) baseServiceImpl.get(map);
	}
	
	public Map<String, Object> getPrmStock(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.getPrmStock");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得指定组织下的所有实体仓库信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrmStockNofrozen(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM01.getPrmStockNofrozen");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
}

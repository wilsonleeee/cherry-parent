/*	
 * @(#)BINOLSSCM02_Service.java     1.0 2010/11/25		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 库存操作的共通机能
 * @author dingyc
 *
 */
public class BINOLSSCM02_Service extends BaseService{	

	/**
	 * 取得指定组织和品牌下的所有大分类
	 * @param map
	 * @return
	 */
	public List getPrimaryCategory(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.getPrimaryCategory");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得指定组织，品牌，大分类下的所有中分类 
	 * @param map
	 * @return
	 */
	public List getSecondryCategory(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.getSecondryCategory");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得指定组织，品牌，大分类，中分类下的所有小分类 
	 * @param map
	 * @return
	 */
	public List getSmallCategory(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.getSmallCategory");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得要盘点的商品列表
	 * @param map
	 * @return
	 */
	public List getStocktakingPromotionList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.getStocktakingPromotionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得指定仓库中指定促销品的库存数量
	 * @param map
	 * @return
	 */
	public List getStockCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.getStockCount");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得指定促销产品厂商Id是否需要管理库存状态 
	 * @param map
	 * @return
	 */
	public List getIsStock(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.getIsStock");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入【促销产品盘点业务单据表】
	 * @param map
	 * @return
	 */
	public int insertPromotionStockTaking(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.insertPromotionStockTaking");
		return baseServiceImpl.saveBackId(map);
	}
	/**
	 * 插入【促销产品盘点业务单据明细表】
	 * @param list
	 */
	public void insertPromotionTakingDetail(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list,"BINOLSSCM02.insertPromotionTakingDetail");
	}
	
	/**
	 * 根据自增长ID取得盘点单据及其详细信息
	 * @param promotionInventoryLogID
	 * @return
	 */
	public List<Map<String, Object>> getStockTakingInfoByID(int promotionStockTakingID){
		Map<String,Object> pramap = new HashMap<String,Object>();
		pramap.put("BIN_PromotionStockTakingID", promotionStockTakingID);
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.getStockTakingInfoByID");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
		return ret;		
	}
	
	/**
	 * 插入入出库总表，返回总表ID
	 * @param map
	 * @return
	 */
	public int insertPromotionStockInOut(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.insertPromotionStockInOut");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入入出库表明细
	 * @param map
	 * @return
	 */
	public void insertPromotionStockDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.insertPromotionStockDetail");
		baseServiceImpl.save(map);
	}
	
	public int updatePromotionStock (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.updatePromotionStock");		
		return baseServiceImpl.update(map);
	}
	public void insertPromotionStock (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM02.insertPromotionStock");		
		 baseServiceImpl.save(map);
	}
}

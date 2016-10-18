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

public class BINOLSSCM03_Service extends BaseService{	

	/**
	 * 取得调拨单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAllocationMain(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM03.getAllocationMain");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入【促销产品调拨业务单据表】，返回主键ID
	 * @param map
	 * @return
	 */
	public int insertAllocationMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM03.insertAllocationMain");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入【促销产品调拨业务单据明细表】
	 * @param list
	 */
	public void insertAllocationDetail(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list,"BINOLSSCM03.insertAllocationDetail");
	}
	/**
	 * 修改【促销产品调拨业务单据表】 
	 * @param map
	 * @return
	 */
	public int updateAllocationMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM03.updateAllocationMain");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 物理删除【促销产品调拨业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteAllocationDetailPhysical(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM03.deleteAllocationDetailPhysical");
		return baseServiceImpl.update(map);
	}
	/**
	 * 逻辑删除【促销产品调拨业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteAllocationDetailLogic(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM03.deleteAllocationDetailLogic");
		return baseServiceImpl.update(map);
	}
	/**
	 * 取得【促销产品调拨业务单据表】信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPromotionAllocationMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM03.getPromotionAllocationMain");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	/**
	 * 取得调拨单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPromotionAllocationDetail(Map<String, Object> map) {		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM03.getPromotionAllocationDetail");
		return baseServiceImpl.getList(map);
	}
}

/*	
 * @(#)BINOLSSCM04_Service.java     1.0 2010/10/29		
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSSCM04_Service extends BaseService{
	
	/**
	 * 插入【促销产品收发货业务单据主表】，返回主键ID
	 * @param map
	 * @return
	 */
	public int insertPromotionDeliverMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM04.insertPromotionDeliverMain");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入【促销产品收发货业务单据明细表】
	 * @param list
	 */
	public void insertPromotionDeliverDetail(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list,"BINOLSSCM04.insertPromotionDeliverDetail");
	}
	
	/**
	 * 更新【促销产品收发货业务单据主表】
	 * @param map
	 * @return
	 */
	public int updatePrmDeliverMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM04.updatePrmDeliverMain");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 物理删除【促销产品发货业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteDeliverDetailPhysical(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM04.deleteDeliverDetailPhysical");
		return baseServiceImpl.update(map);
	}
	/**
	 * 逻辑删除【促销产品发货业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteDeliverDetailLogic(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM04.deleteDeliverDetailLogic");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得【促销产品收发货业务单据主表】信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPromotionDeliverMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM04.getPromotionDeliverMain");
		return (Map)baseServiceImpl.get(map);
	}

	/**
	 * 取得发货单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeliverDetailList(Map<String, Object> map) {		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM04.getDeliverDetailList");
		return baseServiceImpl.getList(map);
	}
}

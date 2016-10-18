/*	
 * @(#)BINOLSSCM01_BL.java     1.0 2010/10/29		
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
package com.cherry.ss.common.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.common.service.BINOLSSCM04_Service;

/**
 * 操作促销品发货单据表
 * @author dingyc
 *
 */
public class BINOLSSCM04_BL {
	@Resource
	private BINOLSSCM04_Service binolsscm04_service;
	
	/**
	 * 插入数据到【促销产品收发货业务单据主表表】
	 * @param map
	 * @return
	 */
	public int insertPromotionDeliverMain(Map<String,Object> map){
		int bIN_PromotionDeliverID = binolsscm04_service.insertPromotionDeliverMain(map);
		return bIN_PromotionDeliverID;
	}
	
	/**
	 * 取得促销品发货单主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPromotionDeliverMain(Map<String,Object> map){
		return binolsscm04_service.getPromotionDeliverMain(map);
	}
	
	/**
	 * 插入数据到【促销产品收发货业务单据明细表】
	 * @param argList
	 */
	public void insertPromotionDeliverDetail(List<Map<String,Object>> argList){
		for(int i=0;i<argList.size();i++){
			Map<String, Object> temp=argList.get(i);
			if(temp.get("BIN_ProductVendorPackageID")==null){
				temp.put("BIN_ProductVendorPackageID","0");
			}
			if(temp.get("BIN_LogicInventoryInfoID")==null){
				temp.put("BIN_LogicInventoryInfoID","0");
			}
			if(temp.get("BIN_StorageLocationInfoID")==null){
				temp.put("BIN_StorageLocationInfoID","0");
			}
		}
		binolsscm04_service.insertPromotionDeliverDetail(argList);
	}
	
	/**
	 * 更新【促销产品收发货业务单据主表】（可以带排他）
	 * @param map
	 * @return
	 */
	public int updatePrmDeliverMain(Map<String, Object> map){
		return binolsscm04_service.updatePrmDeliverMain(map);
	}	
	
	/**
	 * 物理删除【促销产品发货业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteDeliverDetailPhysical (Map<String, Object> map){
		return binolsscm04_service.deleteDeliverDetailPhysical(map);	
	}
	
	
	
	/**
	 * 逻辑删除【促销产品发货业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteDeliverDetailLogic (Map<String, Object> map){
		return binolsscm04_service.deleteDeliverDetailLogic(map);	
	}
	/**
	 * 取得发货单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeliverDetailList(Map<String, Object> map) {
		return binolsscm04_service.getDeliverDetailList(map);
	}
	
}

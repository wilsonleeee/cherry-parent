/*	
 * @(#)BINOLSSCM03_BL.java     1.0 2010/10/29		
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

import com.cherry.ss.common.service.BINOLSSCM03_Service;

/**
 * 库存操作的共通机能(调拨使用)
 * @author dingyc
 *
 */
public class BINOLSSCM03_BL {
	@Resource
	private BINOLSSCM03_Service binolsscm03_service;
	
	
	/**
	 * 取得调拨单详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAllocationMain(Map<String, Object> map) {
		return binolsscm03_service.getAllocationMain(map);
	}
	/**
	 * 向调拨单主表中插入数据，返回ID
	 * @param map
	 * @return
	 */
	public int insertAllocationMain(Map<String, Object> map){
		int bIN_PromotionAllocationID = binolsscm03_service.insertAllocationMain(map);
		return bIN_PromotionAllocationID;
	}
	/**
	 * 取得促销品调拨单主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPromotionAllocationMain(Map<String,Object> map){
		return binolsscm03_service.getPromotionAllocationMain(map);
	}
	
	/**
	 * 取得促销品调拨单明细信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPromotionAllocationDetail(Map<String,Object> map){
		return binolsscm03_service.getPromotionAllocationDetail(map);
	}
	/**
	 * 插入【促销产品调拨业务单据明细表】
	 * @param list
	 */
	public void insertAllocationDetail(List<Map<String, Object>> list){
		for(int i=0;i<list.size();i++){
			Map<String, Object> temp=list.get(i);
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
		binolsscm03_service.insertAllocationDetail(list);
	}
	
	/**
	 * 更新调拨单主表（可以排他）
	 * 
	 * @param paramMap
	 * @return
	 */
	public int updateAllocationMain(Map<String,Object> paramMap){	
		return binolsscm03_service.updateAllocationMain(paramMap);
	}
	
	/**
	 * 物理删除【促销产品调拨业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteAllocationDetailPhysical (Map<String, Object> map){
		return binolsscm03_service.deleteAllocationDetailPhysical(map);	
	}
	
	
	
	/**
	 * 逻辑删除【促销产品调拨业务单据明细表】
	 * @param map
	 * @return
	 */
	public int deleteAllocationDetailLogic (Map<String, Object> map){
		return binolsscm03_service.deleteAllocationDetailLogic(map);	
	}
}

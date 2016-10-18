/*
 * @(#)BINOLSSPRM03_Service.java     1.0 2010/10/27
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
package com.cherry.ss.prm.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品编辑
 * @author Administrator
 *
 */

public class BINOLSSPRM03_Service extends BaseService {
	
	/**
	 * 更新促销产品信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updatePrmInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新促销产品价格信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmPriceInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updatePrmPriceInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新促销产品历史价格信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmHistoryPrice(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updatePrmHistoryPrice");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 添加促销产品价格信息
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrmPriceInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.insertPrmPriceInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 删除促销产品价格信息
	 * 
	 * @param map
	 * @return int
	 */
	public int deletePrmPrice(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.deletePrmPrice");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 添加部门机构促销产品价格
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrmPriceDepart(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.insertPrmPriceDepart");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新部门机构促销产品价格信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmPriceDepart(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updatePrmPriceDepart");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新部门机构促销产品历史价格信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmHistoryPriceDepart(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updatePrmHistoryPriceDepart");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 删除部门机构促销产品价格信息
	 * 
	 * @param map
	 * @return int
	 */
	public int deletePrmPriceDepart(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.deletePrmPriceDepart");
		return baseServiceImpl.remove(map);
	}
	/**
	 * 添加促销产品厂商信息
	 * 
	 * @param map
	 * @return int
	 */
	public void insertPrmFac(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.insertPrmFac");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新促销产品厂商信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmFac(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updatePrmFac");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 删除促销产品厂商信息
	 * 
	 * @param map
	 * @return int
	 */
	public int deletePrmFac(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.deletePrmFac");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新促销产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updatePromotionPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updatePromotionPrtBarCode");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新停用日时
	 * 
	 * @param map
	 * @return int
	 */
	public int updateClosingTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.updateClosingTime");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 插入促销产品条码对应关系表
	 * 
	 * @param map
	 * @return int
	 */
	public void insertPromotionPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.insertPromotionPrtBarCode");
		baseServiceImpl.save(map);
	}
	/**
	 * 添加促销产品信息扩展
	 * 
	 * @param map
	 * @return int
	 */
	public void insertPrmExt(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.insertPrmExt");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 取得促销活动使用的促销品件数
	 * 
	 * @param map
	 * @return
	 */
	public int getActUsePrmCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.getActUsePrmCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销活动使用的促销品件数
	 * 
	 * @param map
	 * @return
	 */
	public int getActPrmCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM03.getActPrmCount");
		return baseServiceImpl.getSum(map);
	}
}

/*	
 * @(#)BINOLPTJCS07_Service.java     1.0 2011/04/28
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
package com.cherry.pt.jcs.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品编辑service
 * 
 * @author lipc
 * @version 1.0 2011.04.28
 *
 */
public class BINOLPTJCS37_Service extends BaseService {

	/**
	 * 更新产品信息
	 * @param map
	 * @return
	 * 
	 * */
	public int updProduct(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.updProduct");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品价格表
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.updProductPrice");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除产品分类
	 * @param map
	 * @return
	 * 
	 * */
	public int delPrtCategory(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.delPrtCategory");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int delProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.delProductPrice");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 更新产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.updPrtBarCode");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 根据厂商ID取得ClosingTime不为空的编码条码关系记录
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.getPrtBarCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 更新停用日时
	 * 
	 * @param map
	 */
	public void updClosingTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.updClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 删除BOM信息
	 * 
	 * @param map
	 */
	public void delProductBOM(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.delProductBOM");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得促销活动所用的产品件数(已下发)
	 * 
	 * @param map
	 */
	public int getActUsePrtCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.getActUsePrtCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销活动所用的产品件数
	 * 
	 * @param map
	 */
	public int getActPrtCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS37.getActPrtCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 更新停用日时
	 * @param map
	 */
	public void updateClosingTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS37.updateClosingTime");
		baseServiceImpl.update(map);
	}
}

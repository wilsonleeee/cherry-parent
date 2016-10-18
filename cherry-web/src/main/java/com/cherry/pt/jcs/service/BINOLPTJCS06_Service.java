/*	
 * @(#)BINOLPTJCS06_Service.java     1.0 2011/04/26	
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品详细service
 * 
 * @author lipc
 * @version 1.0 2011.04.26
 *
 */
public class BINOLPTJCS06_Service extends BaseService {

	/**
	 * 取得产品详细信息
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS06.getDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得产品条码List
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getBarCodeList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS06.getBarCodeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品分类List
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCateList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS06.getCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品销售价格List
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSellPriceList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS06.getSellPriceList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品图片List
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getImgList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS06.getImgList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品BOM组件List
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getBOMList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS06.getBOMList");
		return baseServiceImpl.getList(map);
	}
	
	/**
     * 取得产品编码条码修改履历
     * 
     * @param map
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtBCHistoryList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS06.getPrtBCHistoryList");
        return baseServiceImpl.getList(map);
    }
}

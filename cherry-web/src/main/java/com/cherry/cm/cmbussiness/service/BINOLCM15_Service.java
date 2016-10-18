/*	
 * @(#)BINOLCM15_Service.java     1.0 2010/11/08		
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.PromotionConstants;

/**
 * 各类编号取号共通Service
 * @author WangCT
 *
 */
public class BINOLCM15_Service extends BaseService {
	
	/**
	 * 以递增的形式生成一个新的编号
	 * @param map
	 * @return 返回新生成编号的该条记录ID
	 */
	public int insertSequenceCode (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM15.insertSequenceCode");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 取得新生成的编号
	 * @param map
	 * @return 返回新生成的编号
	 */
	public String getSequenceId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM15.getSequenceId");
		return CherryUtil.getMaxNoInList(baseServiceImpl.getList(map));
	}
	
	/**
	 * 取得当前的编号
	 * @param map
	 * @return 返回当前的编号
	 */
	public String getCurrentSequenceId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM15.getCurrentSequenceId");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据code取得促销品个数
	 * 
	 * @param map
	 * @param brandId
	 * @return
	 */
	public int getPrmCount (Map<String, Object> map,int brandId){
		map.put(CherryConstants.BRANDINFOID, brandId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM15.getPrmCount");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 根据code取得促销品个数
	 * 
	 * @param map
	 * @param brandId
	 * @return
	 */
	public Integer getPrmVendorId (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM15.getPrmVendorId");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入促销产品表
	 * 
	 * @param map
	 * @return int
	 */
	public int addPrmInfo(Map<String, Object> map,int orgId,int brandId) {
		Map<String, Object> temp = new HashMap<String, Object>(map);
		temp.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		temp.put(CherryConstants.BRANDINFOID, brandId);
		temp.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM15.addPrmInfo");
		int promotionId = baseServiceImpl.saveBackId(temp);
		temp.put("promotionId", promotionId);
		int prmVendorId = addPrmVendor(temp);
		return prmVendorId;
	}
	
	/**
	 * update促销产品表
	 * 
	 * @param map
	 * @return
	 */
	public void updPrmInfo(Map<String, Object> map,int prmVendorId) {
		Map<String, Object> temp = new HashMap<String, Object>(map);
		temp.put(PromotionConstants.PRMVENDORID, prmVendorId);
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM15.updatePrmInfo");
		baseServiceImpl.save(map);
	}
	/**
	 * 插入促销产品厂商表
	 * 
	 * @param map
	 * @return
	 */
	public int addPrmVendor(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM15.addPrmVendor");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 查询虚拟条码数量
	 * @param map
	 * @return
	 */
	public int getBarCodeCount (Map<String, Object> map){
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM15.getBarCodeCount");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 * 取得虚拟促销品
	 * @param map
	 * @return
	 */
	public String getVirtualPrmVendorId(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM15.getVirtualPrmVendorId");
		return (String) baseServiceImpl.get(paramMap);
	}
	
	
	/**
	 * 插入促销产品表并返回促销产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPromotionProductBackId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM15.insertPromotionProduct");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入促销产品厂商表
	 * 
	 * @param map
	 * @return
	 */
	public int insertPromProductVendor(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM15.insertPromProductVendor");
		return baseServiceImpl.saveBackId(paramMap);
	}
}

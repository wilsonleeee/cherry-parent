/*	
 * @(#)BINBAT139_Service.java     1.0 @2016-3-17
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
package com.cherry.ot.jh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 *
 * SAP接口(WSDL)：产品导入SERVICE
 *
 * @author jijw
 *
 * @version  2016-3-17
 */
@SuppressWarnings("unchecked")
public class BINBAT139_Service extends BaseService {
	
	/**
	 * 更新备份表备份次数
	 * 
	 * @param int
	 * 
	 * 
	 * @return 无
	 * 
	 */
	public void updateBackupCount() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT139.updateBackupCount");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 清理产品备份表
	 * 
	 * @param int
	 * 
	 * 
	 * @return 无
	 * 
	 */
	public void clearBackupData(int count) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("count", count);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT139.clearBackupData");
		baseServiceImpl.remove(paramMap);

	}
	
	/**
	 * 备份产品信息表
	 * 
	 * @param map
	 * @return 无
	 * 
	 */
	public void backupProducts(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT139.backupProducts");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询产品ID
	 * @param map
	 * @return
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT139.searchProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得产品厂商信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProductVendorInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT139.getProductVendorInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 删除无效的产品数据
	 * 
	 * @param Map
	 * @return List
	 * 
	 */
	public void delInvalidProducts(List<Map<String, Object>> delList) {
		baseServiceImpl.deleteAll(delList, "BINBAT139.delInvalidProducts");
	}
	
	/**
	 * 删除无效的产品厂商数据
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public void delInvalidProVendors(List<Map<String, Object>> delList) {
		baseServiceImpl.deleteAll(delList, "BINBAT139.delInvalidProVendors");
	}
	
	/**
	 * 设置对应关系表停用时日
	 * 
	 * @param map
	 */
	public void setClosingTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT139.setClosingTime");
		baseServiceImpl.update(map);
	}
	
	
	/**
	 * 更新停用日时
	 * @param map
	 */
	public void updateClosingTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT139.updateClosingTime");
		baseServiceImpl.update(map);
	}
	
	
	
	/**
	 * 插入产品表
	 * @param productMap
	 * @return
	 */
	public int insertProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT139.insertProductInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 更新产品信息表
	 * @param productMap
	 * @return
	 */
	public int updateProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT139.updateProductInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 插入产品价格表
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT139.insertProductPrice");
		baseServiceImpl.save(map);
	}	
	
	/**
	 * 查询产品价格是否存在
	 * @param map
	 * @return
	 */
	public String selProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT139.selProductPrice");
		return (String) baseServiceImpl.get(map);
	}	
	
	/**
	 * 更新产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT139.updProductPrice");
		return baseServiceImpl.update(map);
	}	
	
	/**
	 * 清空产品对应的价格数据
	 * 
	 * @param Map
	 * @return List
	 * 
	 */
	public void deleteProductPrice(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT139.deleteProductPrice");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 插入产品厂商信息
	 * @param map
	 * @return
	 */
	public int insertProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT139.insertProductVendor");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT139.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT139.updPrtBarCode");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 查询要伦理删除的产品数据
	 * 
	 * @param 无
	 * 
	 * 
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelList(int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT139.getDelList");
		paramMap.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
		return baseServiceImpl.getList(paramMap);
	}

}

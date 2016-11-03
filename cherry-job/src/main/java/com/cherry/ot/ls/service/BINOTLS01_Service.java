/*	
 * @(#)BINOTLS01_Service.java     1.0 @2014-11-18
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
package com.cherry.ot.ls.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 *
 * LotionSpa接口：产品导入Service
 *
 * @author jijw
 *
 * @version  2014-11-18
 */
@SuppressWarnings("unchecked")
public class BINOTLS01_Service extends BaseService {
	
	/**
	 * 取得LotionSpa产品接口表数据
	 * 
	 * @param map
	 * @return
	 */
	public List getErpProductList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTLS01.getErpProductList");
		return ifServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品ID
	 * @param map
	 * @return
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.searchProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 插入产品表
	 * @param productMap
	 * @return
	 */
	public int insertProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.insertProductInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.updateProductInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 插入产品价格表
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOTLS01.insertProductPrice");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询产品价格是否存在
	 * @param map
	 * @return
	 */
	public String selProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTLS01.selProductPrice");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTLS01.updProductPrice");
		return baseServiceImpl.update(map);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 取得颖产品接口表数据
	 * 
	 * @param map
	 * @return
	 */
	public List getItemListForOT(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN09.getItemListForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 备份颖通产品接口表
	 * @param map
	 */
	public void backupItems(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOTYIN09.backupItems");
	}


	
	/**
	 * 查询促销产品ID
	 * @param map
	 * @return
	 */
	public int searchPromProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN09.searchPromProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 删除已成功导入新后台的数据
	 * 
	 * @param list 删除条件
	 */
	public void deleteItemForOT(List<Map<String, Object>> list) {
		tpifServiceImpl.deleteAll(list, "BINOTYIN09.deleteItemForOT");
	}
	
	/**
	 * 插入促销产品表
	 * @param productMap
	 * @return
	 */
	public int insertPromProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN09.insertPromProductInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	

	
	/**
	 * 更新产品信息表
	 * @param productMap
	 * @return
	 */
	public int updatePromProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN09.updatePromProductInfo");
		return baseServiceImpl.update(paramMap);
	}
	

	/** ################################################## 更新产品条码信息 start ######################################################################## **/	
	
	/**
	 * 插入产品厂商信息
	 * @param map
	 * @return
	 */
	public int insertProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.insertProductVendor");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 取得产品厂商信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProductVendorInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTLS01.getProductVendorInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTLS01.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTLS01.updPrtBarCode");
		baseServiceImpl.update(map);
	}
	
	/** ################################################## 更新产品条码信息 end ######################################################################## **/	
	
	/** ################################################## 更新产品分类 end ######################################################################## **/	
	
	/**
	 * 根据分类名查询分类Id
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.getCatPropId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 添加分类
	 * 
	 * @param map
	 * @return
	 */
	public int addCatProperty(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.addCatProperty");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 根据分类终端显示区分查询分类Id
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.getCatPropId2");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	
	/**
	 * 分类终端下发区分更新
	 * 
	 * @param map
	 * @return
	 */
	public int updProp(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTLS01.updProp");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void delPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.delPrtCategory");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTLS01.getCatPropValId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 添加分类属性选项值
	 * 
	 * @param map
	 * @return
	 */
	public int addPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTLS01.addPropVal");
		return baseServiceImpl.saveBackId(map);
	}	
	
	/**
	 * 更新分类选项值
	 * 
	 * @param map
	 * @return
	 */
	public int updPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTLS01.updPropVal");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN09.insertPrtCategory");
		baseServiceImpl.save(map);
	}

}

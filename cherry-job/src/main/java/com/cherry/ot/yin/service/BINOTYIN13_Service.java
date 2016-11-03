/*	
 * @(#)BINOTYIN13_Service.java     1.0 @2015-1-28
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
package com.cherry.ot.yin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;

/**
 *
 * 颖通接口：颖通产品导入Service
 *
 * @author jijw
 *
 * @version  2015-1-28
 */
@SuppressWarnings("unchecked")
public class BINOTYIN13_Service extends BaseService {
	
	/**
	 * 取得颖产品接口表数据
	 * 
	 * @param map
	 * @return
	 */
	public List getItemListForOT(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.getItemListForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新颖通产品接口表导入失败的产品信息的CreateDate
	 * @param productMap
	 * @return
	 */
	public int updateOtItem(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.updateOtItem");
		return tpifServiceImpl.update(paramMap);
	}
	
	/**
	 * 备份颖通产品接口表
	 * @param map
	 */
	public void backupItems(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOTYIN13.backupItems");
	}

	/**
	 * 查询产品ID
	 * @param map
	 * @return
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.searchProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 查询产品ID(根据unitcode)
	 * @param map
	 * @return
	 */
	public int searchProductIdByUnitCode(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.searchProductIdByUnitCode");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 查询促销产品ID
	 * @param map
	 * @return
	 */
	public int searchPromProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.searchPromProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 删除已成功导入新后台的数据
	 * 
	 * @param list 删除条件
	 */
	public void deleteItemForOT(List<Map<String, Object>> list) {
		tpifServiceImpl.deleteAll(list, "BINOTYIN13.deleteItemForOT");
	}
	
	/**
	 * 插入产品表
	 * @param productMap
	 * @return
	 */
	public int insertProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.insertProductInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入促销产品表
	 * @param productMap
	 * @return
	 */
	public int insertPromProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.insertPromProductInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.updateProductInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新产品信息表
	 * @param productMap
	 * @return
	 */
	public int updatePromProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.updatePromProductInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 删除产品价格信息
	 * @param map
	 */
	public void delProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.delProductPrice");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 更新当前在业务日期内的产品价格的结束日期
	 * @param map
	 */
	public void updatePrtPrice(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.updatePrtPrice");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品价格表
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOTYIN13.insertProductPrice");
		baseServiceImpl.save(map);
	}

	/**
	 * 查询产品价格是否存在
	 * @param map
	 * @return
	 */
	public String selProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.selProductPrice");
		return (String) baseServiceImpl.get(map);
		
	}
	/**
	 * 更新产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.updProductPrice");
		return baseServiceImpl.update(map);
	}

	/** ################################################## 更新产品条码信息 start ######################################################################## **/	
	
	/**
	 * 插入产品厂商信息
	 * @param map
	 * @return
	 */
	public int insertProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.insertProductVendor");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品厂商信息
	 * @param map
	 * @return
	 */
	public int insertPrmProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.insertPrmProductVendor");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.getProductVendorInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得促销产品厂商信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPrmProductVendorInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.getPrmProductVendorInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrmVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.updPrmVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.updPrtBarCode");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新促销产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updPrmBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.updPrmBarCode");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品停用日时
	 * 
	 * @param map
	 * @return int
	 */
	public void updatePrtClosingTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.updatePrtClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新促销品停用日时
	 * 
	 * @param map
	 * @return int
	 */
	public void updatePrmClosingTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN13.updatePrmClosingTime");
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
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.getCatPropId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据分类终端显示区分查询分类Id
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.getCatPropId2");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据分类类别属性查询分类Id(动态分类)
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId3(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.getCatPropId3");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 添加分类
	 * 
	 * @param map
	 * @return
	 */
	public int addCatProperty(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.addCatProperty");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 分类终端下发区分更新
	 * 
	 * @param map
	 * @return
	 */
	public int updProp(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN13.updProp");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void delPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.delPrtCategory");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.getCatPropValId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据属性值名称查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.getCatPropValId2");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 添加分类属性选项值
	 * 
	 * @param map
	 * @return
	 */
	public int addPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN13.addPropVal");
		return baseServiceImpl.saveBackId(map);
	}	
	
	/**
	 * 更新分类选项值
	 * 
	 * @param map
	 * @return
	 */
	public int updPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN13.updPropVal");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.insertPrtCategory");
		baseServiceImpl.save(map);
	}
	
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN13.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}

}

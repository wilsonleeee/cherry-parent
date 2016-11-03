/*	
 * @(#)BINOTYIN01_Service.java     1.0 @2015-4-29
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
package com.cherry.webserviceout.kingdee.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 *
 * Kingdee接口：产品导入Service
 *
 * @author jijw
 *
 * @version  2015-4-29
 */
@SuppressWarnings("unchecked")
public class BINBEKDPRO01_Service extends BaseService {
	
	/**
	 * 取得电商接口表信息表
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getESInterfaceInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDPRO01.getESInterfaceInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新电商接口表信息表
	 * @param productMap
	 * @return
	 */
	public int updateESInterfaceInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.updateESInterfaceInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询产品ID
	 * @param map
	 * @return
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.searchProductId");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.insertProductInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.updateProductInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 插入产品价格表
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEKDPRO01.insertProductPrice");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询产品价格是否存在
	 * @param map
	 * @return
	 */
	public String selProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDPRO01.selProductPrice");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDPRO01.updProductPrice");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品厂商信息
	 * @param map
	 * @return
	 */
	public int insertProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.insertProductVendor");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDPRO01.getProductVendorInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 删除无效的产品数据
	 * @param Map
	 * 
	 * @return List
	 * 
	 */
	public void delInvalidProducts(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEKDPRO01.delInvalidProducts");
		baseServiceImpl.update(map);
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
	public void delInvalidProVendors(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEKDPRO01.delInvalidProVendors");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新停用日时
	 * @param map
	 */
	public void updateClosingTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEKDPRO01.updateClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 备份产品下发数据备份履历表
	 * @param map
	 */
	public void backProductImpKingdee(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list,"BINBEKDPRO01.backProductImpKingdee");
	}
	/**
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDPRO01.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDPRO01.updPrtBarCode");
		baseServiceImpl.update(map);
	}

	
	/** ################################################## 更新产品条码信息 end ######################################################################## **/	
	
	/** ################################################## 更新产品分类 start ######################################################################## **/	
	
	/**
	 * 根据分类名查询分类Id
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.getCatPropId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据分类终端显示区分查询分类Id
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.getCatPropId2");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据分类类别属性查询分类Id(动态分类)
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId3(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.getCatPropId3");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 添加分类
	 * 
	 * @param map
	 * @return
	 */
	public int addCatProperty(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.addCatProperty");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 分类终端下发区分更新
	 * 
	 * @param map
	 * @return
	 */
	public int updProp(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEKDPRO01.updProp");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void delPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.delPrtCategory");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.getCatPropValId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据属性值名称查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.getCatPropValId2");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 添加分类属性选项值
	 * 
	 * @param map
	 * @return
	 */
	public int addPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEKDPRO01.addPropVal");
		return baseServiceImpl.saveBackId(map);
	}	
	
	/**
	 * 更新分类选项值
	 * 
	 * @param map
	 * @return
	 */
	public int updPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEKDPRO01.updPropVal");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEKDPRO01.insertPrtCategory");
		baseServiceImpl.save(map);
	}

}

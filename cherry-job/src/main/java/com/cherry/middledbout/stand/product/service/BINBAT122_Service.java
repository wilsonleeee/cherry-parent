/*	
 * @(#)BINBAT122_Service.java     1.0 @2015-10-13		
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
package com.cherry.middledbout.stand.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 
 * 产品导入(标准接口)(IF_Product)FN
 * @author jijw
 *  * @version 1.0 2015.10.13
 *
 */
@SuppressWarnings("unchecked")
public class BINBAT122_Service extends BaseService {
	
	/**
	 * 取得标准产品接口表数据
	 * 
	 * @param map
	 * @return
	 */
	public List getStandardProductList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.getStandardProductList");
		return tpifServiceImpl.getList(map);
	}

	/**
	 * 查询产品ID
	 * @param map
	 * @return
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT122.searchProductId");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT122.insertProductInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT122.updateProductInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得产品厂商信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductVendorInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.getProductVendorInfo");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 删除无效的产品数据
	 * @param map
	 * 
	 * @return List
	 * 
	 */
	public void delInvalidProducts(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT122.delInvalidProducts");
		baseServiceImpl.update(map);
	}
	
	
	/**
	 * 更新停用日时
	 * @param map
	 */
	public void updateClosingTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT122.updateClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品价格表
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT122.insertProductPrice");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询产品价格是否存在
	 * @param map
	 * @return
	 */
	public String selProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.selProductPrice");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.updProductPrice");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品厂商信息
	 * @param map
	 * @return
	 */
	public int insertProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT122.insertProductVendor");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 删除无效的产品厂商数据
	 * 
	 * @param map
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
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品条码对应关系信息
	 * 
	 * @param map
	 * @return int
	 */
	public void updPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.updPrtBarCode");
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
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT122.delPrtCategory"); 
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT122.getCatPropValId1");
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
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT122.addPropVal");
		return baseServiceImpl.saveBackId(map);
	}	
	
	/**
	 * 更新分类选项值
	 * 
	 * @param map
	 * @return
	 */
	public int updPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT122.updPropVal");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT122.insertPrtCategory"); 
		baseServiceImpl.save(map);
	}


	/**
	 * 取得产品属性信息
	 * 
	 * @param map
	 * @return
	 */
	public List getCatPropValInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.getCatPropValInfo");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 将产品接口表中SynchFlag为NULL的更新为1
	 * @param map
     */
	public void updateIFSynchFlagFromNullToOne(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.updateIFSynchFlagFromNullToOne");
		tpifServiceImpl.update(map);
	}

	/**
	 * 将产品接口表中SynchFlag从1更新为2或者3
	 * @param map
	 */
	public void updateIFSynchFlagFromOneToAnother(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT122.updateIFSynchFlagFromOneToAnother");
		tpifServiceImpl.update(map);
	}
}

/*
 * @(#)BINBEIFPRO01_Service.java     1.0 2010/10/27
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
package com.cherry.ia.pro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 
 *产品列表导入SERVICE
 * 
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINBEIFPRO01_Service extends BaseService {

	/**
	 * 查询产品列表信息
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.getProductList");
		return ifServiceImpl.getList(map);
	}

	/**
	 * 更新产品信息表
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public int updateProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.updateProductInfo");
		return baseServiceImpl.update(paramMap);

	}

	/**
	 * 插入产品信息
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public int insertProductInfo(Map<String, Object> productMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(productMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.insertProductInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}

	/**
	 * 删除产品价格信息
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public void delProductPrice(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.delProductPrice");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 查询产品价格记录数
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public int getPriceCount(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.getPriceCount");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	/**
	 * 插入产品价格信息
	 * 
	 * @param Map
	 * 
	 * 
	 * @return null
	 * 
	 */
	public void insertProductPrice(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.insertProductPrice");
		baseServiceImpl.save(map);
	}

	/**
	 * 伦理删除产品条码信息
	 * 
	 * @param Map
	 * 
	 * 
	 * @return
	 * 
	 */
	public void delProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.delProductVendor");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 查询产品条码信息 
	 * 
	 * @param Map
	 * 
	 * 
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBarCodeList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.getBarCodeList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 插入产品厂商信息
	 * 
	 * @param Map
	 * 
	 * 
	 * @return null
	 * 
	 */
	public int insertProductVendor(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.insertProductVendor");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 查询产品ID
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.searchProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.getDelList");
		paramMap.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 删除无效的产品数据
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public void delInvalidProducts(List<Map<String, Object>> delList) {
		baseServiceImpl.deleteAll(delList, "BINBEIFPRO01.delInvalidProducts");
	}
	/**
	 * 删除无效的产品数据
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public void delInvalidProCate(List<Map<String, Object>> delList) {
		baseServiceImpl.deleteAll(delList, "BINBEIFPRO01.delInvalidProCate");
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
		baseServiceImpl.deleteAll(delList, "BINBEIFPRO01.delInvalidProVendors");
	}

	/**
	 * 删除无效的产品价格数据
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public void delInvalidProPrices(List<Map<String, Object>> delList) {
		baseServiceImpl.deleteAll(delList, "BINBEIFPRO01.delInvalidProPrices");
	}
	
	/**
	 * 删除无效的产品价格数据
	 * 
	 * @param Map
	 * 
	 * 
	 * @return List
	 * 
	 */
	public void validPirce(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO01.validPirce");
		baseServiceImpl.update(map);
	}

	/**
	 * 备份产品信息表
	 * 
	 * @param map
	 * 
	 * 
	 * @return 无
	 * 
	 */
	public void backupProducts(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.backupProducts");
		baseServiceImpl.save(map);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFPRO01.clearBackupData");
		baseServiceImpl.remove(paramMap);

	}

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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO01.updateBackupCount");
		baseServiceImpl.update(paramMap);

	}

	/**
	 * 添加分类
	 * 
	 * @param map
	 * @return
	 */
	public int addCatProperty(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.addCatProperty");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 添加分类选项值
	 * 
	 * @param map
	 * @return
	 */
	public int addPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFPRO01.addPropVal");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 分类终端下发区分更新
	 * 
	 * @param map
	 * @return
	 */
	public int updProp(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updProp");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新分类选项值
	 * 
	 * @param map
	 * @return
	 */
	public int updPropVal(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updPropVal");
		return baseServiceImpl.update(map);
	}

	/**
	 * 根据分类名查询分类Id
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
						"BINBEIFPRO01.getCatPropId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据分类终端显示区分查询分类Id
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropId2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
						"BINBEIFPRO01.getCatPropId2");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}

	/**
	 * 根据属性值,品牌查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
						"BINBEIFPRO01.getCatPropValId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId1(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
						"BINBEIFPRO01.getCatPropValId1");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据属性值名查询分类属性值ID
	 * 
	 * @param map
	 * @return
	 */
	public int getCatPropValId2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
						"BINBEIFPRO01.getCatPropValId2");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 插入产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.insertPrtCategory");
		baseServiceImpl.save(map);
	}

	/**
	 * 删除产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void delPrtCategory(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO01.delPrtCategory");
		baseServiceImpl.remove(map);
	}
	/**
	 * 更新产品厂商成有效
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	/**
	 * 更新产品厂商表
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public void updProductVendor(Map<String, Object> map,String validflag) {
		Map<String, Object>	paramMap = new HashMap<String, Object>(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updProductVendor");
		paramMap.put(CherryConstants.VALID_FLAG, validflag);
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 设置对应关系表停用时日
	 * 
	 * @param map
	 */
	public void setClosingTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.setClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 清空对应关系表停用时日
	 * 
	 * @param map
	 */
	public void cleanClosingTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.cleanClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品条码对应关系表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtBarCode(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO01.insertPrtBarCode");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新产品条码
	 * @param map
	 * @return
	 * 
	 * */
	public int updBarCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updBarCode");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品条码对应关系表
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtBarCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updPrtBarCode");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询产品价格是否存在
	 * @param map
	 * @return
	 */
	public String selProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.selProductPrice");
		return (String) baseServiceImpl.get(map);
		
	}
	/**
	 * 更新产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updProductPrice");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询分类属性名称在分类预设值表中已经存在
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List getExistPvCN(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.getExistPvCN");
//		return CherryUtil.obj2int(baseServiceImpl.get(map));
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 产品变动后更新产品方案明细表的version字段
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtSolutionDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.updPrtSolutionDetail");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 查询CodeType在当前用户品牌下是否存在
	 * 
	 * @param map
	 * @return
	 */
	public int getCodeMCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.getCodeMCount");
		return baseConfServiceImpl.getSum(map);
	}
	
	/**
	 * 查询CodeType的最后一个key值
	 * 
	 * @param map
	 * @return
	 */
	public String getLastCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.getLastCode");
		return (String) baseConfServiceImpl.get(map);
	}
	
	/**
	 * 将-9999的codeM复制到当前用户品牌下
	 * 
	 * */
	public void copyCodeManager(Map<String,Object> map){
		baseConfServiceImpl.save(map,"BINBEIFPRO01.copyCodeManager");
	}
	
	/**
	 * 将-9999的code复制到当前用户品牌下
	 * 
	 * */
	public void copyCode(Map<String,Object> map){
		baseConfServiceImpl.save(map,"BINBEIFPRO01.copyCode");
	}
	
	/**
	 * 根据通用Code值插入品牌Code值
	 * 
	 * */
	public void insertCode(Map<String,Object> map){
		baseConfServiceImpl.save(map,"BINBEIFPRO01.insertCode");
	}
	
	/**
	 * 查询品牌对应的CODE及组织CODE
	 * 
	 * @param Map
	 * 
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrgBrand(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO01.getOrgBrand");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 根据Val查询CodeKey
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCodeByVal(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.getCodeByVal");
		return baseConfServiceImpl.getList(map);
	}
	

	/**
	 * 根据Val查询CodeKey
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getCodeVal(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO01.getCodeVal");
		return (Map<String,Object>) baseConfServiceImpl.get(map);
	}
}

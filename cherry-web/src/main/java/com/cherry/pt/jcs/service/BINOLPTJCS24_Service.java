/*
 * @(#)BINOLPTJCS04_Service.java     1.0 2011/03/28
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.synchro.common.BaseSynchroService;

/**
 * 产品查询Service
 * @author lipc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS24_Service extends BaseService {

	@Resource
	private BaseSynchroService baseSynchroService;
	
	/**
	 * 取得产品总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getProCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询产品是否已存在有效的相同条码
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrtBarCodeVF(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getPrtBarCodeVF");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得产品信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getProList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getBrandList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getBrandList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得树形显示顺序 List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Integer> getCatPropList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getCatPropList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得path下级分类List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCateValList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getCateValList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得path下级分类数
	 * 
	 * @param map
	 * @return
	 */
	public int getChildCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getChildCount");
		return baseServiceImpl.getSum(map);
	}
	/**
     * 取得产品信息List（Excel）
     * 
     * @param map
     * 
     * @return
     */
    public List<Map<String, Object>> getProductInfoListExcel(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getProductInfoListExcel");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得产品类别基本属性定义List
     * 
     * @param map
     * 
     * @return
     */
    public List<Map<String,Object>> getPrtCatPropertyList(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getPrtCatPropertyList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得产品分类的上级分类
     * @param map
     * @return
     */
    public List<Map<String,Object>> getLocateCatHigher(Map<String, Object> map){
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getLocateCatHigher");
    	return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得已绑定产品的产品分类排序  
     * @param map
     * @return
     */
    public List<Map<String,Object>> getCatSortByBing(Map<String, Object> map){
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getCatSortByBing");
    	return baseServiceImpl.getList(map);
    }
    
	/**
	 * 查询定位到的产品预设值ID
	 * 
	 * @param map 检索条件
	 * @return 定位到的部门ID
	 */
	public String getLocationPrtCatValId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getLocationPrtCatValId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新产品信息
	 * @param map
	 * @return
	 * 
	 * */
	public int updProduct(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.updProduct");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品信息版本号
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductVersion(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.updProductVersion");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 产品变动后更新产品方案明细表的version字段
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtSolutionDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.updPrtSolutionDetail");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得同产品下非当前厂商ID的有效厂商记录集合
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPrtVendorDetailList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getPrtVendorDetailList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品厂商信息根据厂商ID
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtVendorInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getPrtVendorInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得同产品下有效或无效的厂商信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtDetailInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getPrtDetailInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品厂商
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtVendor(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.updPrtVendor");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新停用日时
	 * @param map
	 */
	public void updateClosingTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS24.updateClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品条码对应关系表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtBarCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS24.insertPrtBarCode");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新产品条码对应关系信息
	 * 
	 * @param map
	 */
	public void updPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.updPrtBarCode");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 停用启用柜台
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int delProductInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.delProductInfo");
		return 1;//baseServiceImpl.update(parameterMap);
	}
	
	/* #########################################################################################################################################  */
	
	/**
	 * 查询新后台产品数据list
	 * 
	 * @param int
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS24.getProductList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 将编码条码变更后，老的编码条码在product_SCS中停用
	 * @param map
	 * @return
	 */
	public Integer disProductSCS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS24.disProductSCS");
		return baseSynchroService.update(map);
	}
	
	/**
	 * 更新productSCS表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergeProductSCS(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.mergeProductSCS");
		return (Map<String, Object>)baseSynchroService.get(map);
	}
	
	/**
	 * 物理删除接口数据库某品牌的产品信息
	 * 
	 * @param map
	 * @return
	 */
	public int delIFProductInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.delIFProductInfo");
		return baseSynchroService.remove(map);
	}
	
	/**
	 * 插入产品接口数据库
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addProductSCS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS24.addProductSCS");
		baseSynchroService.save(map);
	}
	
	/**
	 * 查询对应关系件数
	 * 
	 * @param map
	 * @return
	 */
	public int getBarCodeCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getBarCodeCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询编码条码变更数据
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getBarCodeModify(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS24.getBarCodeModify");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入ProductSetting
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addProductSetting(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS24.addProductSetting");
		baseSynchroService.save(map);
	}
	
	/**
	 * 手动提交事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void ifManualCommit() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLCMINC99.commit");
		baseSynchroService.update(paramMap);
	}

	/**
	 * 手动回滚事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void ifManualRollback() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLCMINC99.rollback");
		baseSynchroService.update(paramMap);
	}
}

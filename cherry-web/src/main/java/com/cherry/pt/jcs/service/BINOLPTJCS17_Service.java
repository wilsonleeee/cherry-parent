/*
 * @(#)BINOLPTJCS17_Service.java v1.0 2014-8-12
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
 * 产品方案柜台分配维护Service
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public class BINOLPTJCS17_Service extends BaseService {
	
	@Resource
	private BaseSynchroService baseSynchroService;
	
	/**
	 * 取得方案对应的配置信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getDPConfigDetailBySolu(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getDPConfigDetailBySolu");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得区域信息
	 * @param map
	 * @return
	 */
	public List getRegionInfoListOld (Map<String, Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getRegionInfoListOld");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得渠道信息
	 * @param map
	 * @return
	 */
	public List getChannelInfoListOld (Map<String,Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getChannelInfoListOld");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得区域信息
	 * @param map
	 * @return
	 */
	public List getRegionInfoList (Map<String, Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getRegionInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得区域信息(门店用)
	 * @param map
	 * @return
	 */
	public List getRegionInfoListCnt (Map<String, Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getRegionInfoListCnt");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得渠道信息
	 * @param map
	 * @return
	 */
	public List getChannelInfoList (Map<String,Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getChannelInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新柜台产品配置主表信息
	 * @param map
	 * @return
	 */
	public int updDepartProductConfig(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.updDepartProductConfig");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得方案明细与关联地点List
	 * @param map
	 * @return
	 */
	public List getSoluDetailProductDepartPriceList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getSoluDetailProductDepartPriceList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据城市ID或区域ID 取得权限内的柜台号
	 * @param map
	 * @return
	 */
	public List getCounterInfoList(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getCounterInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 将方案绑定柜台对应的部门产品表的数据无效
	 * @param map
	 * @return
	 */
	public int updProductDepart(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.updProductDepart");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新更新产品方案主表的树形结构等信息
	 * @param map
	 * @return
	 */
	public int updPrtSoluDepartRelation(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.updPrtSoluDepartRelation");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除方案绑定柜台对应的部门产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int delProductDepartPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.delProductDepartPrice");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 更新柜台产品配置明细表的数据  
	 * @param map
	 * @return 
	 */
	public String mergeDepartProductConfigDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.mergeDepartProductConfigDetail");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 更新部门产品表的数据
	 * @param map
	 * @return 
	 */
	public String mergeProductDepartInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.mergeProductDepartInfo");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * merge产品方案部门关系表
	 * @param map
	 * @return 
	 */
	public String mergePrtSoluDepartRelation(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.mergePrtSoluDepartRelation");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得新方案明细与关联柜台List
	 * @param map
	 * @return
	 */
	public List getCouProductList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getCouProductList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询新后台产品方案柜台关联数据list
	 * @param map
	 * @return
	 */
	public List getPrtSoluCouList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getPrtSoluCouList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询新后台产品方案柜台关联数据list
	 * @param map
	 * @return
	 */
	public List getPrtSoluWithDepartHis(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getPrtSoluWithDepartHis");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据指定Version取方案明细的产品信息List
	 * @param map
	 * @return
	 */
	public List getPrtSoluDetailByVersionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getPrtSoluDetailByVersionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 物理删除接口数据库某品牌的柜台产品
	 * 
	 * @param map
	 * @return
	 */
	public int delIFProductWithCounter(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.delIFProductWithCounter");
		return baseSynchroService.remove(map);
	}
	
	/**
	 * 物理删除产品方案柜台接口表
	 * 
	 * @param map
	 * @return
	 */
	public int delIFPrtSoluWithCounter(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.delIFPrtSoluWithCounter");
		return baseSynchroService.remove(map);
	}
	
	/**
	 * 物理删除产品方案明细接口表
	 * 
	 * @param map
	 * @return
	 */
	public int delIFPrtSoluSCS(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.delIFPrtSoluSCS");
		return baseSynchroService.remove(map);
	}
	
	/**
	 * 插入柜台产品接口表
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addIFProductWithCounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS17.addIFProductWithCounter");
		baseSynchroService.save(map);
	}
	
	/**
	 * 插入产品方案柜台关联 接口表
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addIFPrtSoluWithCounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS17.addIFPrtSoluWithCounter");
		baseSynchroService.save(map);
	}
	
	/**
	 * 插入产品方案明细接口表
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addIFPrtSoluSCS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLPTJCS17.addIFPrtSoluSCS");
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
	
	/**
	 * 更新产品方案配置履历表  
	 * @param map
	 * @return 
	 */
	public String mergePrtSoluWithDepartHis(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.mergePrtSoluWithDepartHis");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 删除方案配置履历表中指定的方案
	 * 
	 * @param map
	 * @return
	 */
	public int delPrtSoluWithDepartHis(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.delPrtSoluWithDepartHis");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得产品方案配置信息List(UpdateTime升序)
	 * @param map
	 * @return
	 */
	public List getDPConfigDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getDPConfigDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得方案配置的区域或渠道实际的的柜台与以前配置的差异(区域城市/渠道)List
	 * @param map
	 * @return
	 */
	public List getCntForPrtSoluCityChannelDiff(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put("businessType", "1");
		parameterMap.put("operationType", "1");
		parameterMap.put("userId", map.get("userID"));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getCntForPrtSoluCityChannelDiff");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取方案明细的产品信息List
	 * @param map
	 * @return
	 */
	public List getPrtPriceSoluDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getPrtPriceSoluDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得当前方案分配的地点是否已被其他方案分配过的List
	 * @param map
	 * @return
	 */
	public List getExistPrtSoluWithDepartHis(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getExistPrtSoluWithDepartHis");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得当前方案分配的地点(柜台)是否已被其他方案分配过的List
	 * @param map
	 * @return
	 */
	public List getExistCntForPrtSoluWithDepartHis(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS17.getExistCntForPrtSoluWithDepartHis");
		return baseServiceImpl.getList(parameterMap);
	}
	
}

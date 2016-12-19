/*		
 * @(#)BINOLCM02_BL.java     1.0 2010/10/27		
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
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 弹出datatable共通Service
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM02_Service extends BaseService{
	/**
	 * 取得促销产品总数
	 * @param map
	 * @return
	 */
	public int getPromotionInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPromotionCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销产品信息
	 * @param map
	 * @return
	 */
	public List getPromotionShortInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPromotionShortInfoList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得促销产品总数(薇诺娜，合并)
	 * @param map
	 * @return
	 */
	public int getPromotionInfoCountOne (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPromotionCountOne");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销产品信息
	 * @param map
	 * @return
	 */
	public List getPromotionShortInfoListOne(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPromotionShortInfoListOne");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品总数
	 * @param map
	 * @return
	 */
	public int getProductInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得分类树信息
	 * @param map
	 * @return
	 */
	public List getCategoryTreeInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCategoryTreeInfoList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得产品信息
	 * @param map
	 * @return
	 */
	public List getProductShortInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductShortInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品分类信息（大分类）
	 * @param map
	 * @return
	 */
	public List getProductCategoryList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductCategoryList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据产品ItemCode得到对应的库存信息（库存从金蝶实时获取）
	 * @param map
	 * @return
	 */
	public List getInventoryByItemCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getInventoryByItemCode");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品信息(带图片模式)
	 * @param map
	 * @return
	 */
	public List getProductImageDInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductImageDInfoList");
		return baseServiceImpl.getList(map);
	}
	
	
	
	/**
	 * 取得产品总数(薇诺娜)
	 * @param map
	 * @return
	 */
	public int getProductInfoCountOne (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductCountOne");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得产品信息(薇诺娜)
	 * @param map
	 * @return
	 */
	public List getProductShortInfoListOne(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductShortInfoListOne");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品总数(浓妆淡抹订货)
	 * @param map
	 * @return
	 */
	public int getProductInfoCountTwo (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductCountTwo");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得产品信息(浓妆淡抹订货)
	 * @param map
	 * @return
	 */
	public List getProductShortInfoListTwo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getProductShortInfoListTwo");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品分类总数
	 * @param map
	 * @return
	 */
	public int getCateInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCateCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得产品分类信息
	 * @param map
	 * @return
	 */
	public List getCateInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCateInfoList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map 查询条件
	 * @return 返回柜台总数
	 */
	public int getCounterInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCounterInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得柜台List
	 * 
	 * @param map 查询条件
	 * @return 柜台List
	 */
	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCounterInfoList");
		return (List)baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得子品牌总数
	 * 
	 * @param map 查询条件
	 * @return 返回子品牌总数
	 */
	public int getOrigBrandCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getOrigBrandCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得子品牌List
	 * 
	 * @param map 查询条件
	 * @return 子品牌List
	 */
	public List<Map<String, Object>> getOrigBrandList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getOrigBrandList");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据柜台ID取得柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCounterInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得厂商List
	 * 
	 * @param map 查询条件
	 * @return 厂商List
	 */
	public List getFactoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getFactoryList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询某一区域的所有上级区域
	 * 
	 * @param map 查询条件
	 * @return 区域List
	 */
	public List getHigherRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getHigherRegionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门总数
	 * 
	 * @param map 查询条件
	 * @return 返回部门总数
	 */
	public int getDepartInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		if(parameterMap.get("operationType") == null) {
			parameterMap.put("operationType", "0");
		}
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getDepartInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List getDepartInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		if(parameterMap.get("operationType") == null) {
			parameterMap.put("operationType", "0");
		}
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getDepartInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得员工总数
	 * 
	 * @param map 查询条件
	 * @return 返回员工总数
	 */
	public int getEmployeeCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getEmployeeCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得员工List
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得员工部门、岗位信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getEmpDptPstList (String employeeId,String language) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeId", employeeId);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getEmpDptPstList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得考核问卷总数
	 * @param map
	 * @return
	 */
	public int getCheckPaperCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCheckPaperCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得考核问卷List
	 * @param map
	 * @return
	 */
	public List getCheckPaperList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCheckPaperList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得问卷总数
	 * @param map
	 * @return
	 */
	public int getPaperCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPaperCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得问卷List
	 * @param map
	 * @return
	 */
	public List getPaperList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPaperList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得产品分类List
	 * @param map
	 * @return
	 */
	public List getPrtCategoryList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPrtCategoryList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品分类值List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCateValList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCateValList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品分类值总数
	 * @param map
	 * @return
	 */
	public int getCateValCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCateValCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销品分类List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrmCateList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPrmCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得促销品分类总数
	 * @param map
	 * @return
	 */
	public int getPrmCateCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPrmCateCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 取得会员信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getMemberInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员总数
	 * @param map
	 * @return
	 */
	public int getMemberInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getMemberInfoCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 取得对象批次信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getObjBatchInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getObjBatchInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得对象批次总数
	 * @param map
	 * @return
	 */
	public int getObjBatchCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getObjBatchCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询员工信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getEmployeeInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询员工信息总数
	 * @param map
	 * @return
	 */
	public int getEmployeeInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getEmployeeInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询员工信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getUserInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getUserInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询员工信息总数
	 * @param map
	 * @return
	 */
	public int getUserInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getUserInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得所有部门List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllDepartInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getAllDepartInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得所有部门总数
	 * @param map
	 * @return
	 */
	public int getAllDepartInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getAllDepartInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询区域信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getRegionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询大区信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionIdList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getRegionIdList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询渠道信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getChannelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询部门信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getDepartList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询系统柜台List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBelongCounterList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getBelongCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询定位到的区域ID
	 * @param map
	 * @return
	 */
	public String getLocationRegionId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getLocationRegionId");
		return (String)baseServiceImpl.get(map);
	}
	/**
	 * 根据输入字符串模糊查询柜台Code
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCounterCode(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCM02.getCounterCode");
	}
	
	/**
	 * 查询会员活动信息总数
	 * 
	 * @param map 查询条件
	 * @return 会员活动信息总数
	 */
	public int getMemCampaignCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getMemCampaignCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询会员活动信息List
	 * 
	 * @param map 查询条件
	 * @return 会员活动信息List
	 */
	public List<Map<String, Object>> getMemCampaignList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getMemCampaignList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 查询会员活动信息总数
	 *
	 * @param map 查询条件
	 * @return 会员活动信息总数
	 */
	public int getMemCampaignCount2 (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getMemCampaignCount2");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 查询会员活动信息List
	 *
	 * @param map 查询条件
	 * @return 会员活动信息List
	 */
	public List<Map<String, Object>> getMemCampaignList2(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getMemCampaignList2");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 查询优惠券活动信息总数
	 *
	 * @param map 查询条件
	 * @return 会员活动信息总数
	 */
	public int getCouponRuleCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCouponRuleCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 查询优惠券活动信息List(JH)
	 *
	 * @param map 查询条件
	 * @return 会员活动信息List
	 */
	public List<Map<String, Object>> getCouponRuleList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCouponRuleList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询促销活动信息总数(JH)
	 *
	 * @param map 查询条件
	 * @return 促销活动信息总数
	 */
	public int getPrmCampaignCount2 (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPrmCampaignCount2");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 查询促销活动信息List
	 *
	 * @param map 查询条件
	 * @return 促销活动信息List
	 */
	public List<Map<String, Object>> getPrmCampaignList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPrmCampaignList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 查询促销活动信息总数
	 *
	 * @param map 查询条件
	 * @return 促销活动信息总数
	 */
	public int getPrmCampaignCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPrmCampaignCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 查询促销活动信息List
	 *
	 * @param map 查询条件
	 * @return 促销活动信息List
	 */
	public List<Map<String, Object>> getPrmCampaignList2(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getPrmCampaignList2");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得所有部门信息List
	 * 
	 * @param map 查询条件
	 * @return 所有部门信息List
	 */
	public List<Map<String, Object>> getAllOrgList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getAllOrgList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询销售总数
	 * 
	 * @param map 查询条件
	 * @return 销售总数
	 */
	public int getSaleRecordCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getSaleRecordCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询销售信息List
	 * 
	 * @param map 查询条件
	 * @return 销售信息List
	 */
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getSaleRecordList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得对象批次名称
	 * 
	 * @param map 查询条件
	 * @return 对象批次名称
	 */
	public Map<String, Object> getSearchMemInfo(Map<String, Object> map){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getSearchMemInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询销售总数
	 * 
	 * @param map 查询条件
	 * @return 销售总数
	 */
	public int getCouponCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCouponCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询经销商信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getResellerList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getResellerList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询经销商总数
	 * @param map
	 * @return
	 */
	public int getResellerCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getResellerCount");
		return baseServiceImpl.getSum(map);
	}

	public List<Map<String, Object>> getBrandCodeListSrh(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getBrandCodeListSrh");
		return baseServiceImpl.getList(paramMap);
	}

	public List<Map<String, Object>> getSortListSrh(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getSortListSrh");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 查询活动对象为搜索结果的活动List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampObjList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCampObjList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 查询活动对象为搜索结果的活动总数
	 * @param map
	 * @return
	 */
	public int getCampObjCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM02.getCampObjCount");
		return baseServiceImpl.getSum(map);
	}
	
}

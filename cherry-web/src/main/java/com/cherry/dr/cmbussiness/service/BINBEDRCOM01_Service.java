/*	
 * @(#)BINBEDRCOM01_Service.java     1.0 2011/05/17
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
package com.cherry.dr.cmbussiness.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员活动共通Service
 * 
 * @author hub
 * @version 1.0 2011.05.17
 */
public class BINBEDRCOM01_Service extends BaseService{
	
	/**
	 * 插入规则执行履历表
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public void addRuleExecRecord(CampBaseDTO campBaseDTO){
		baseServiceImpl.save(campBaseDTO, "BINBEDRCOM01.addRuleExecRecord");
	}
	
	/**
	 * 更新规则执行履历记录
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updRuleExecRecord(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRCOM01.updRuleExecRecord");
	}
	
	/**
	 * 清除规则执行履历记录
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int removeRuleExecRecord(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRCOM01.removeRuleExecRecord");
	}
	
	/**
	 * 删除清零记录
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int removeZCRecord(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRCOM01.removeZCRecord");
	}
	
	/**
	 * 更新会员信息表（会员等级）
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updMemberInfo(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRCOM01.updMemberInfo");
	}
	
	/**
	 * 更新会员信息表（会员俱乐部等级）
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updClubMemberInfo(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRCOM01.updClubMemberInfo");
	}
	
	
	/**
	 * 通过会员卡号取得会员ID
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			会员ID
	 * 
	 */
	public int getMemberInfoId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 会员卡号
		paramMap.put("memberCode", map.get("memberCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getMemberInfoId");
		int memberInfoId = 0;
		Object memberInfoIdObj = baseServiceImpl.get(paramMap);
		if (null != memberInfoIdObj) {
			memberInfoId = Integer.parseInt(memberInfoIdObj.toString());
		}
		return memberInfoId;
	}
	
	/**
	 * 取得会员卡号
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			会员卡号
	 * 
	 */
	public String getMemCard(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getMemCard");
		return (String) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据单据号取得规则执行信息(单据产生时间)
	 * 
	 * @param map
	 * @return
	 */
	public String getTicketDate(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINBEDRCOM01.getTicketDate");
		// 品牌
		paramMap.put("brandInfoId", map.get("brandInfoID"));
		// 单据号
		paramMap.put("billId", map.get("tradeNoIF"));
		// 会员ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		return (String) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得规则执行记录数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			规则执行记录数
	 * 
	 */
	public int getRuleExecCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 单据产生日期
		paramMap.put("saleTime", map.get("saleTime"));
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		// 会员俱乐部ID
		paramMap.put("memberClubId", map.get("memberClubId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getRuleExecCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得规则执行记录数(调整等级用)
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			规则执行记录数
	 * 
	 */
	public int getRuleByKbnCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 单据产生日期
		paramMap.put("saleTime", map.get("saleTime"));
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getRuleByKbnCount");
		return baseServiceImpl.getSum(paramMap);
	}
	/**
	 * 取得重算信息记录数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			重算信息记录数
	 * 
	 */
	public int getReCalcInfoCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		// 比较日期
		paramMap.put("reCalcDate", map.get("reCalcDate"));
		// 重算类型
		paramMap.put("reCalcType", map.get("reCalcType"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getReCalcInfoCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得重算信息记录数(BATCH)
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			重算信息记录数
	 * 
	 */
	public int getBTReCalcCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getCmReCalcCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询重算日期信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			重算信息
	 * 
	 */
	public Map<String, Object> getReCalcDateInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		// 重算类型
		paramMap.put("reCalcType", map.get("reCalcType"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getReCalcDateInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 插入重算信息表 
	 * 
	 * @param map
	 * @return
	 */
	public void insertReCalcInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.insertReCalcInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新重算信息表
	 * 
	 * @param map
	 * @return int
	 */
	public int updateReCalcInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.updateReCalcInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得会员信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return CampBaseDTO
	 * 			会员信息
	 * 
	 */
	public CampBaseDTO getCampBaseDTO(Map<String, Object> map) {
		// 取得会员信息(会员资料上传)
		CampBaseDTO campBaseDTO = getCampBaseDTOMB(map);
		if (null != campBaseDTO) {
			// 默认等级
			if (!CherryConstants.LEVELSTATUS_2.equals(campBaseDTO.getLevelStatus()) 
					&& 0 == campBaseDTO.getInitLevel()) {
				// 等级为默认等级时将当前等级至为无等级状态
				campBaseDTO.setCurLevelId(0);
			}
		}
		return campBaseDTO;
	}
	
	/**
	 * 取得会员信息(会员资料上传)
	 * 
	 * @param map
	 * 			查询参数
	 * @return CampBaseDTO
	 * 			会员信息
	 * 
	 */
	public CampBaseDTO getCampBaseDTOMB(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getCampBaseDTO");
		CampBaseDTO campBaseDTO = (CampBaseDTO) baseServiceImpl.get(paramMap);
		if (null != campBaseDTO) {
			// 包含会员俱乐部
			if (!CherryChecker.isNullOrEmpty(map.get("memberClubId")) &&
					!"0".equals(String.valueOf(map.get("memberClubId")))) {
				// 会员俱乐部ID
				paramMap.put("memberClubId", map.get("memberClubId"));

				paramMap.put(CherryConstants.IBATIS_SQL_ID,
						"BINBEDRCOM01.getCMFirstTicketTime");
				// 查询首单时间
				campBaseDTO.setFirstTicketTime((String) baseServiceImpl.get(paramMap));
			}
		}
		return campBaseDTO;
	}
	
	/**
	 * 查询基准点的 累计金额, 等级, 化妆次数
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			基准点的 累计金额, 等级, 化妆次数
	 * 
	 */
	public String getNewValue(CampBaseDTO campBaseDTO) {
		return (String) baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getNewValue");
	}
	
	/**
	 * 查询当前累计金额, 等级, 化妆次数
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			基准点的 累计金额, 等级, 化妆次数
	 * 
	 */
	public String getCurNewValue(CampBaseDTO campBaseDTO) {
		return (String) baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getCurNewValue");
	}
	
	/**
	 * 取得会员销售记录数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			会员销售记录数
	 * 
	 */
	public int getSaleRecordCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRCOM01.getSaleRecordCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 查询销售记录
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			销售记录
	 * 
	 */
	public Map<String, Object> getSaleRecordInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 单据号
		paramMap.put("billCode", map.get("tradeNoIF"));
		// 所属品牌ID
		paramMap.put("brandInfoId", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoId", map.get("organizationInfoID"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM01.getSaleRecordInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询销售记录(订单)
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			销售记录
	 * 
	 */
	public Map<String, Object> getESOrderInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 单据号
		paramMap.put("billCode", map.get("tradeNoIF"));
		// 所属品牌ID
		paramMap.put("brandInfoId", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoId", map.get("organizationInfoID"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM01.getESOrderInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得规则执行履历信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return List
	 * 			规则执行履历信息List
	 * 
	 */
	public List<Map<String, Object>> getRuleExecList(CampBaseDTO campBaseDTO) {
		return (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getRuleExecList");
	}
	
	/**
	 * 通过会员等级ID取得会员等级代码
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			会员等级代码
	 * 
	 */
	public String getLevelCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员等级ID
		paramMap.put("memberLevelId", map.get("memberLevelId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM01.getLevelCode");
		return (String) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得规则内容
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			规则内容List
	 * 
	 */
	public List<Map<String, Object>> getRuleContentList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 规则ID
		paramMap.put("ruleIdArr", map.get("ruleIdArr"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM01.getRuleContentList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得同一单据号和履历区分的最大重算次数
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			最大重算次数
	 * 
	 */
	public int getReCalcCount(CampBaseDTO campBaseDTO) {
		return (Integer)baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getReCalcCount");
	}
	
	/**
	 * 取得同一天清零的最大重算次数
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			最大重算次数
	 * 
	 */
	public Map<String, Object> getReCalcCountByTicDate(CampBaseDTO campBaseDTO) {
		return (Map)baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getReCalcCountByTicDate");
	}
	
	/**
	 * 取得会员等级List
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return List
	 * 			会员等级List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getMemLevelcomList(CampBaseDTO campBaseDTO) throws Exception{	
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getMemLevelcomList");
		// 循环等级列表
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级有效期
				String periodValidity = (String) levelInfo.get("periodValidity");
				if (!CherryChecker.isNullOrEmpty(periodValidity)) {
					Map<String, Object> periodValidityMap = (Map<String,Object>) 
					JSONUtil.deserialize(periodValidity);
					levelInfo.put("periodValidity", periodValidityMap);
				}
			}
		}
		return levelList;
	}
	
	/**
	 * 取得购买的产品明细List
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return List
	 * 			产品明细List
	 */
	public List<Map<String, Object>> getBuyProductList(CampBaseDTO campBaseDTO){	
		return (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getBuyProductList");
	}
	
	/**
	 * 取得购买的产品明细List(订单)
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return List
	 * 			产品明细List
	 */
	public List<Map<String, Object>> getESBuyProductList(CampBaseDTO campBaseDTO){	
		return (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getESBuyProductList");
	}
	
	/**
	 * 取得关联退货的产品明细List
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return List
	 * 			产品明细List
	 */
	public List<Map<String, Object>> getSrProductList(CampBaseDTO campBaseDTO){	
		return (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getSrProductList");
	}
	
	/**
	 * 取得单据信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			单据信息
	 */
	public Map<String, Object> getBuyInfo(CampBaseDTO campBaseDTO){	
		return (Map<String, Object>) baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getBuyInfo");
	}
	
	/**
	 * 取得订单信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			单据信息
	 */
	public Map<String, Object> getESBuyInfo(CampBaseDTO campBaseDTO){	
		return (Map<String, Object>) baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getESBuyInfo");
	}
	
	/**
	 * 查询促销产品信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> selPrmProductInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.selPrmProductVendorIdInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询barcode变更后的促销产品信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> selPrmProductPrtBarCodeInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmProductPrtBarCodeInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销产品信息  根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> selPrmAgainByPrmVenID(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmAgainByPrmVenID");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> selProductInfo (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selProductInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询barcode变更后的产品信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> selPrtBarCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrtBarCode");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询产品信息  根据产品厂商ID
	 * @param map
	 * @return
	 */
	public Map<String, Object> selProductInfoByPrtVenID(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selProductInfoByPrtVenID");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销产品信息  根据促销产品厂商ID
	 * @param map
	 * @return
	 */
	public Map<String, Object> selPrmProductInfoByPrmVenID(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.selPrmProductByVenID");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> selProAgainByPrtVenID(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selProAgainByPrtVenID");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询促销产品类别信息  根据促销产品厂商ID
	 * @param map
	 * @return
	 */
	public Map<String, Object> selPromotionCateCd(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.selPromotionCateCd");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询产品分类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> selPrtCateList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.selPrtCateList");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得开始时间点的值
	 * @param map
	 * @return
	 */
	public String getFromNewValue(Map<String, Object> map){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.putAll(map);
		// 取得开始时间点的累计金额
		searchMap.put("datekbn", "1");
		// 查询规则执行履历表
		String result = getNewValue(searchMap);
		if (null == result || "".equals(result)) {
			// 查询某个时间点的值(规则履历历史表)
			result = getHistoryValue(searchMap);
		}
		return result;
	}
	
	/**
	 * 查询某个时间点的值(规则履历历史表)
	 * @param map
	 * @return
	 */
	public String getHistoryValue(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getHistoryValueByTime");
		Map<String, Object> resultMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != resultMap && !resultMap.isEmpty()) {
			return (String) resultMap.get("newValue");
		}
		return null;
	}
	
	/**
	 * 取得首单金额信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getFromAmountInfo(Map<String, Object> map){
		// 查询规则执行履历表
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getFromAmountInfo");
		Map<String, Object> resultMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null == resultMap || resultMap.isEmpty()) {
			// 查询规则执行履历历史表
			map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getHistoryAmountInfo");
			resultMap = (Map<String, Object>) baseServiceImpl.get(map);
		}
		return resultMap;
	}
	
	/**
	 * 取得结束时间点的值
	 * @param map
	 * @return
	 */
	public String getToNewValue(Map<String, Object> map){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.putAll(map);
		// 取得开始时间点的累计金额
		searchMap.put("datekbn", "2");
		return getNewValue(searchMap);
	}
	
	/**
	 * 取得某个时间点的值
	 * @param map
	 * @return
	 */
	public String getNewValue(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getNewValueByTime");
		Map<String, Object> resultMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != resultMap && !resultMap.isEmpty()) {
			return (String) resultMap.get("newValue");
		}
		return null;
	}
	
	/**
	 * 查询某个时间段内的购买次数
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBillList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getBillList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得原会员卡号
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			原会员卡号
	 */
	public Map<String, Object> getOldMemCodeInfo(CampBaseDTO campBaseDTO){	
		 List<Map<String, Object>> oldMemCodeList = (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getOldMemCodeList");
		 if (null != oldMemCodeList && !oldMemCodeList.isEmpty()) {
			 return oldMemCodeList.get(0);
		 }
		 return null;
	}
	
	/**
	 * 查询最后一次引起某一属性变化的单据信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			单据信息
	 */
	public Map<String, Object> getLastChangeInfo(CampBaseDTO campBaseDTO){	
		Map<String, Object> lastChangeInfo = (Map<String, Object>) baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getLastChangeInfo");
		if (null == lastChangeInfo || lastChangeInfo.isEmpty()) {
			lastChangeInfo = (Map<String, Object>) baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getLastChangeHistoryInfo");
		}
		return lastChangeInfo;
	}
	
	/**
	 * 查询有效期开始的单据信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			单据信息
	 */
	public List<Map<String, Object>> getValidStartList(CampBaseDTO campBaseDTO){
		List<Map<String, Object>> validStartList = new ArrayList<Map<String, Object>>();
		// 查询有效期开始的单据信息
		List<Map<String, Object>> validList = (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getValidStartList");
		if (null != validList && !validList.isEmpty()) {
			validStartList.addAll(validList);
		}
		// 查询有效期开始的单据信息(历史履历)
		validList = (List<Map<String, Object>>) baseServiceImpl.getList(campBaseDTO, "BINBEDRCOM01.getValidStartHistoryList");
		if (null != validList && !validList.isEmpty()) {
			validStartList.addAll(validList);
		}
		return validStartList;
	}
	
	/**
	 * 根据业务类型，业务时间取得执行次数
	 * @param map
	 * @return
	 */
	public int getCountByType(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getCountByType");
		Integer result = (Integer) baseServiceImpl.get(map);
		if (null != result) {
			return result;
		}
		return 0;
	}
	
	/**
	 * 根据履历区分取得履历次数
	 * @param map
	 * @return
	 */
	public int getRecordCountByKbn(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getRecordCountByKbn");
		Integer result = (Integer) baseServiceImpl.get(map);
		if (null != result) {
			return result;
		}
		return 0;
	}
	
	/**
	 * 取得会员当前的积分信息
	 * @param map
	 * 			查询条件
	 * @return PointDTO
	 * 			会员当前的积分信息
	 */
	public PointDTO getCurMemPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRCOM01.getCurMemPointInfo");
		return (PointDTO)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得关联单据信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			销售记录
	 * 
	 */
	public Map<String, Object> getRelevantSaleInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 单据号
		paramMap.put("relevantNo", map.get("relevantNo"));
		// 所属品牌ID
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		// 所属组织ID
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			paramMap.put("memberClubId", map.get("memberClubId"));
		}
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM01.getRelevantSaleInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得会员扩展信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			会员扩展信息
	 * 
	 */
	public Map<String, Object> getMemberExtInfo(CampBaseDTO campBaseDTO) {
		return (Map)baseServiceImpl.get(campBaseDTO, "BINBEDRCOM01.getCurMemberExtInfo");
	}
	
	/**
	 * 取得关联会员信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			关联会员信息
	 * 
	 */
	public Map<String, Object> getRefMemLevelInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM01.getRefMemLevelInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员所属俱乐部列表
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			关联会员信息
	 * 
	 */
	public List<Map<String, Object>> getMemClubLevelList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINBEDRCOM01.getCmMemClubLevelList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
}

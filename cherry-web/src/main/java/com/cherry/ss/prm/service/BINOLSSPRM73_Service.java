/*		
 * @(#)BINOLSSPRM73_Service.java     1.0 2016/03/28		
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.CouponDTO;
import com.cherry.ss.prm.dto.CouponRuleDTO;

/**
 * 优惠券规则Service
 * @author hub
 * @version 1.0 2016.03.28
 */
public class BINOLSSPRM73_Service extends BaseService{
	
	/**
	 * 取得优惠券规则总数
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则总数
	 */
	public int getRuleInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponRuleInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得优惠券规则List
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则List
	 */
	public List<Map<String, Object>> getRuleInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponRuleInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得优惠券规则详细信息
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则详细信息
	 */
	public CouponRuleDTO getCouponRuleInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponRuleInfo");
		return (CouponRuleDTO) baseServiceImpl.get(map);
	}
	
	/**
	 * 新增电子券批量生成记录
	 * @param map
	 * @return
	 */
	public void addCouponBatchRecord(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.addCouponBatchRecord");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 新增优惠券记录
	 * @param map
	 * @return
	 */
	public void addMemberCoupon(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.addMemberCoupon");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 新增优惠券记录(批量)
	 * @param list
	 */
	public void addMemberCouponList(List<CouponDTO> list) {
		baseServiceImpl.saveAllObjs(list, "BINOLSSPRM73.addMemberCoupon");
	}
	
	/**
	 * 更新优惠券规则信息
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	public int updateCouponRuleInfo(CouponRuleDTO couponRule){
		return baseServiceImpl.update(couponRule, "BINOLSSPRM73.updateCouponRuleInfo");
		 
	}
	
	/**
	 * 更新优惠券规则信息
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	public int updateCouponRuleStatus(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.updateCouponRuleStatus");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新优惠券条件
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	public int updateCouponCondition(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.updateCouponCondition");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 取得促销品信息
	 * 
	 * @param map 检索条件
	 * @return 促销品信息
	 */
	public Map<String, Object> getPrmInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getPrmInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得优惠券List
	 * 
	 * @param memberCode 会员卡号
	 * @param mobile 手机号码
	 * @param brandInfoId 品牌ID
	 * @param saleDate 销售日期
	 * 
	 * @return 优惠券List
	 */
	public List<Map<String, Object>> getCouponList(String memberCode, String mobile, int brandInfoId, String saleDate,String bpCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberCode", memberCode);
		map.put("mobile", mobile);
		map.put("brandInfoId", brandInfoId);
		map.put("saleDate", saleDate);
		map.put("bpCode", bpCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得代物券List
	 * 
	 * @param memberCode 会员卡号
	 * @param mobile 手机号码
	 * @param brandInfoId 品牌ID
	 * @param saleDate 销售日期
	 * 
	 * @return 优惠券List
	 */
	public List<Map<String, Object>> getDwqList(String memberCode, String mobile, int brandInfoId, String saleDate,String bpCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberCode", memberCode);
		map.put("mobile", mobile);
		map.put("brandInfoId", brandInfoId);
		map.put("saleDate", saleDate);
		map.put("bpCode", bpCode);
		map.put("couponType", CouponConstains.COUPONTYPE_2);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得单个优惠券信息
	 * 
	 * @param couponNo 券号
	 * @return 单个优惠券信息
	 */
	public Map<String, Object> getCouponInfo(String couponNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("couponNo", couponNo);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 通过规则代码取得优惠券规则详细信息
	 * 
	 * @param ruleCode 规则代码
	 * @return 优惠券规则详细信息
	 */
	public CouponRuleDTO getCouponRuleInfoByCode(String ruleCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", ruleCode);
		return getCouponRuleInfo(map);
	}
	
	/**
	 * 取得部门ID
	 * 
	 * @param map 部门代号
	 * @return Integer 部门ID
	 */
	public Integer getOrganizationId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getOrganizationId");
		return (Integer) baseServiceImpl.get(map);
	}
	
	/**
	 * 新增电子券门店明细
	 * @param list
	 */
	public void addCouponCounterList(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLSSPRM73.addCouponCounter");
	}
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map 检索条件
	 * @return 柜台总数
	 */
	public int getCounterInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCounterInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得柜台信息List
	 * 
	 * @param map 检索条件
	 * @return 柜台信息List
	 */
	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCounterInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemberInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getMemberInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员信息List
	 * 
	 * @param map 检索条件
	 * @return 会员信息List
	 */
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getMemberInfoList");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String, Object>> getMemberInfoListWithoutPage(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getMemberInfoListWithoutPage");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 删除电子券门店明细
	 * 
	 * @param map
	 * 			删除条件
	 * 
	 */
	public int delCounterDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.delCounterDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得等级列表
	 * 
	 * @param brandInfoId 品牌ID
	 * @return 等级列表
	 */
	public List<Map<String, Object>> getLevelList(int brandInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", brandInfoId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getLevelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 删除电子券会员明细
	 * 
	 * @param map
	 * 			删除条件
	 * 
	 */
	public int delMemberDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.delMemberDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 新增电子券会员明细
	 * @param list
	 */
	public void addCouponMemberList(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLSSPRM73.addCouponMember");
	}
	
	/**
	 * 查询产品信息
	 * 
	 * @param map 查询条件
	 * @return 产品信息
	 */
	public Map<String, Object> getProInfo(int prtVendorId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prtVendorId", prtVendorId);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getProInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询产品价格信息
	 * 
	 * @param map 查询条件
	 * @return 产品价格信息
	 */
	public Map<String, Object> getProPriceInfo(int prtVendorId, String saleDate) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prtVendorId", prtVendorId);
		paramMap.put("saleDate", saleDate);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getProPriceInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询产品分类信息
	 * 
	 * @param map 查询条件
	 * @return 产品分类信息
	 */
	public Map<String, Object> getProTypeInfo(int cateValId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cateValId", cateValId);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getProTypeInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询促销活动信息
	 * 
	 * @param map 查询条件
	 * @return 促销活动信息
	 */
	public Map<String, Object> getPromActivityInfo(String campaignCode, int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("campaignCode", campaignCode);
		paramMap.put("brandInfoId", brandInfoId);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getPromActivityInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询会员活动信息
	 * 
	 * @param map 查询条件
	 * @return 会员活动信息
	 */
	public Map<String, Object> getMemActivityInfo(String campaignCode, int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("campaignCode", campaignCode);
		paramMap.put("brandInfoId", brandInfoId);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getMemActivityInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得配置数据库品牌List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			配置数据库品牌List
	 */
	public List<Map<String, Object>> getConfBrandInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getConfBrandList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			品牌信息
	 */
	public Map<String, Object> getOSBrandInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织Code
		paramMap.put("orgCode", map.get("orgCode"));
		// 品牌code
		paramMap.put("brandCode", map.get("brandCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getOSBrandInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得优惠券规则执行列表
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则执行列表
	 */
	public List<Map<String, Object>> getCouponRuleExecList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponRuleExecList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得优惠券规则执行
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则执行
	 */
	public Map<String, Object> getCouponRuleExecInfo(String ruleCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", ruleCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponRuleExecInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得门店总数
	 * 
	 * @param map 检索条件
	 * @return 门店总数
	 */
	public int getCouponCounterCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCouponCounterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 根据柜台code取得柜台门店ID和渠道ID
	 * @param counterCode
	 * @return
	 */
	public Map<String,Object> getCounterInfoByCode(String counterCode){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("counterCode", counterCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getCounterInfoByCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getMemCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得活动发券总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getAllSendCount(String ruleCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", ruleCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getAllSendCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员发券总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getAllMemSendCount(String ruleCode, String memCode, String mobile,String bpCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bpCode", bpCode);
		map.put("ruleCode", ruleCode);
		map.put("memCode", memCode);
		map.put("mobile", mobile);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getAllMemSendCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询产品分类信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> selPrtCateList(int prtVendorId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prtVendorId", prtVendorId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.selPrtCateList");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得等级ID
	 * 
	 * @param map 检索条件
	 * @return 等级ID
	 */
	public int getLevelId(String levelCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("levelCode", levelCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getLevelId");
		Map<String, Object> levelMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != levelMap && !levelMap.isEmpty()) {
			return Integer.parseInt(String.valueOf(levelMap.get("levelId")));
		}
		return 0;
	}
	
	/**
	 * 取得券类型
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			券类型
	 * 
	 */
	public String getCouponTypeByCode(String couponNo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 券号
		paramMap.put("couponNo", couponNo);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM73.getCouponTypeByCode");
		return (String) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 添加短信发送记录
	 * 
	 * @param map 短信发送记录
	 * @return 短信ID
	 */
	public int addCouponSmsInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.addCouponSmsInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新短信发送记录
	 * 
	 * @param 短信发送记录
	 * @return int
	 */
	public int updateCouponSmsInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.updateCouponSmsInfo");
		return baseServiceImpl.update(map); 
	}
	
	/**
	 * 查询该单据发券的信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSendCoupon(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getSendCoupon");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询该单据发送短信情况
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSendSMSInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getSendSMSInfo");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	public List<Map<String, Object>> getSMSCouponInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getSMSCouponInfo");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取渠道信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM73.getChannelList");
		return baseServiceImpl.getList(map);
	}
}

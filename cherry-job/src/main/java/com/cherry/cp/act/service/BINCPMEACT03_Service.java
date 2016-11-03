/*	
 * @(#)BINCPMEACT03_Service.java     1.0 2013/02/20		
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
package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 导入会员活动和会员活动预约信息处理Service
 * 
 * @author WangCT
 * @version 1.0 2013/02/20
 */
public class BINCPMEACT03_Service extends BaseService {
	
	/**
	 * 从会员活动接口表取得未下发的活动记录
	 * 
	 * @param map 查询条件
	 * @return 未下发的活动记录
	 */
	public List<Map<String, Object>> getCampaignImport(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getCampaignImport");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得活动ID
	 * 
	 * @param map 查询条件
	 * @return 活动ID
	 */
	public String getCampaignId(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getCampaignId");
		return (String)baseServiceImpl.get(param);
	}
	
	/**
	 * 添加活动
	 * 
	 * @param map 添加内容
	 * @return 活动ID
	 */
	public int addCampaign(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.addCampaign");
		return baseServiceImpl.saveBackId(param);
	}
	
	/**
	 * 更新活动
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updCampaign(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampaign");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 取得子活动ID
	 * 
	 * @param map 查询条件
	 * @return 子活动ID
	 */
	public String getSubCampaignId(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getSubCampaignId");
		return (String)baseServiceImpl.get(param);
	}
	
	/**
	 * 添加子活动
	 * 
	 * @param map 添加内容
	 * @return 子活动ID
	 */
	public int addSubCampaign(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.addSubCampaign");
		return baseServiceImpl.saveBackId(param);
	}
	
	/**
	 * 更新子活动
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updSubCampaign(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updSubCampaign");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 删除会员活动规则条件明细
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delCamRuleCondition(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.delCamRuleCondition");
		return baseServiceImpl.remove(param);
	}
	
	/**
	 * 添加会员活动规则条件明细
	 * 
	 * @param list 添加内容
	 */
	public void addCamRuleCondition(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINCPMEACT03.addCamRuleCondition");
	}
	
	/**
	 * 删除会员活动规则结果明细
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delCampaignRuleResult(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.delCampaignRuleResult");
		return baseServiceImpl.remove(param);
	}
	
	/**
	 * 添加会员活动规则结果明细
	 * 
	 * @param list 添加内容
	 */
	public void addCampaignRuleResult(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINCPMEACT03.addCampaignRuleResult");
	}
	
	/**
	 * 取得未下发的会员活动预约记录
	 * 
	 * @param map 查询条件
	 * @return 未下发的会员活动预约记录
	 */
	public List<Map<String, Object>> getCampaignOrderImport(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getCampaignOrderImport");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得会员活动预约信息
	 * 
	 * @param map 查询条件
	 * @return 会员活动预约信息
	 */
	public Map<String, Object> getCampaignOrderInfo(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getCampaignOrderInfo");
		return (Map)baseServiceImpl.get(param);
	}
	
	/**
	 * 添加会员活动预约信息
	 * 
	 * @param map 添加内容
	 * @return 会员活动预约ID
	 */
	public int addCampaignOrder(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.addCampaignOrder");
		return baseServiceImpl.saveBackId(param);
	}
	
	/**
	 * 批量添加会员活动预约信息
	 * 
	 * @param map 添加内容
	 * @return 会员活动预约IDList
	 */
	public void addCampaignOrderList(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINCPMEACT03.addCampaignOrder");
	}
	
	/**
	 * 取得会员活动预约信息IDList
	 * 
	 * @param map 查询条件
	 * @return 会员活动预约信息IDList
	 */
	public List<Map<String, Object>> getCampaignOrderIdList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getCampaignOrderIdList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 更新会员活动预约信息下发状态
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int updCampaignOrderSendFlag(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampaignOrderSendFlag");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 更新会员活动预约信息
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int updCampaignOrder(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampaignOrder");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 删除会员活动预约明细信息
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delCampaignOrderDetail(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.delCampaignOrderDetail");
		return baseServiceImpl.remove(param);
	}
	
	/**
	 * 添加会员活动预约明细信息
	 * 
	 * @param map 添加内容
	 */
	public void addCampaignOrderDetail(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINCPMEACT03.addCampaignOrderDetail");
	}
	
	/**
	 * 更新会员活动接口表为下发失败状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public void updCampaignImportError(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT03.updCampaignImportError");
	}
	
	/**
	 * 更新会员活动接口表为下发成功状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updCampaignImportSuccess(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampaignImportSuccess");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 更新会员活动接口表把下发失败的数据更新成未下发状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updCampaignImportWait(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampaignImportWait");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 更新会员活动预约接口表主表为下发成功状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public void updCampOrderSuccess(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT03.updCampOrderSuccess");
	}
	
	/**
	 * 更新会员活动预约接口表明细表为下发成功状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public void updCampOrderDetailSuccess(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT03.updCampOrderDetailSuccess");
	}
	
	/**
	 * 更新会员活动预约接口表主表为下发失败状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updCampOrderErrorTxt(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampOrderErrorTxt");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 更新会员活动预约接口表明细表为下发失败状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updCampOrderDetailErrorTxt(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampOrderDetailErrorTxt");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 更新会员活动预约接口表主表为下发失败状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public void updCampOrderError(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT03.updCampOrderError");
	}
	
	/**
	 * 更新会员活动预约接口表明细表为下发失败状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public void updCampOrderDetailError(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT03.updCampOrderDetailError");
	}
	
	/**
	 * 更新会员活动预约接口表主表为待下发状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updCampOrderWait(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampOrderWait");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 更新会员活动预约接口表明细表为待下发状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updCampOrderDetailWait(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.updCampOrderDetailWait");
		return baseServiceImpl.update(param);
	}
	
	/**
	 * 取得所有会员活动基础属性ID
	 * 
	 * @param map 查询条件
	 * @return 所有会员活动基础属性ID
	 */
	public List<Map<String, Object>> getCampaignBasePropId(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getCampaignBasePropId");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得所有活动代码
	 * 
	 * @param map 查询条件
	 * @return 所有活动代码
	 */
	public List<String> getCampaignCodeList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getCampaignCodeList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得产品厂商ID
	 * 
	 * @param map 查询条件
	 * @return 产品厂商ID
	 */
	public String getProductVendorId(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getProductVendorId");
		return (String)baseServiceImpl.get(param);
	}
	
	/**
	 * 取得促销品厂商ID
	 * 
	 * @param map 查询条件
	 * @return 促销品厂商ID
	 */
	public String getPromotionProductVendorId(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT03.getPromotionProductVendorId");
		return (String)baseServiceImpl.get(param);
	}

}

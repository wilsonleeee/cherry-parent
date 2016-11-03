/*	
 * @(#)BINBAT141_Service.java     1.0 @2016-3-17
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
package com.cherry.ot.jh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * SAP接口(WSDL)：活动导入SERVICE
 *
 * @author jijw
 *
 * @version  2016-3-17
 */
public class BINBAT141_Service extends BaseService {
	
	/**
	 * 新增促销活动
	 * @param map
	 * @return
	 */
	public int addPromotionActivity(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.addPromotionActivity");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 新增促销活动规则
	 * @param map
	 * @return
	 */
	public int addPromotionActivityRule(Map<String, Object> map){
		// 促销活动规则文件名
		map.put("ruleFileName", "");
		// 促销活动规则
		map.put("activityRule", "");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.addPromotionActivityRule");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 新增促销活动规则
	 * @param map
	 */
	public void addActRuleInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.addActRuleInfo");
		baseServiceImpl.save(map);
	}
	/**
	 * 新增促销活动规则条件
	 * @param map
	 */
	public void addPromotionRuleCondition(List<Map<String,Object>> list){
		// 批量插入
		baseServiceImpl.saveAll(list, "BINBAT141.addPromotionRuleCondition");
	}
	
	/**
	 * 取得基础规则属性信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrmBasePropInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.getPrmBasePropInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台属性
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> getCntList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.getCntList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得渠道属性
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.getChannelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员等级属性
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemLevelList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.getMemLevelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得规则ID
	 * @param map
	 * @return
	 */
	public Object getRuleId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.getRuleId");
		return baseServiceImpl.get(map);
	}
	
	/**
	 * 删除促销活动规则
	 * @param map
	 * @return
	 */
	public int delPrmActivityRule (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.delPrmActivityRule");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销活动规则
	 * @param map
	 * @return
	 */
	public int delPrmActivity (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.delPrmActivity");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销活动条件
	 * @param map
	 * @return
	 */
	public int delPrmActCondition (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.delPrmActCondition");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销活动结果 
	 * @param map
	 * @return
	 */
	public int delPrmActResult (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.delPrmActResult");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则表 
	 * @param map
	 * @return
	 */
	public int delPrmActRule (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.delPrmActRule");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则分类表
	 * @param map
	 * @return
	 */
	public int delPrmActRuleCate(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.delPrmActRuleCate");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 新增沟通对象
	 * @param list
	 */
	public void addCustomerInfo(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBAT141.addCustomerInfo");
	}
	/**
	 * 新增沟通对象搜索记录
	 * @param map
	 */
	public void addMemSearchLog(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.addMemSearchLog");
		baseServiceImpl.save(map);
	}
	/**
	 * 新增优惠券活动
	 * @param map
	 */
	public void savePromotionCouponRule_exec(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT141.addPromotionCouponRule");
		baseServiceImpl.save(map);
	}
}

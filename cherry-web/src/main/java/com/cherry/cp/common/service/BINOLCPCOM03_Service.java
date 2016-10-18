/*	
 * @(#)BINOLCPCOM03_Service.java     1.0 2011/7/18		
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
package com.cherry.cp.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员活动共通调用方法 Service
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM03_Service extends BaseService {
	
	/**
	 * 取得会员活动组信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampaignGrpList (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getCpcom03CampaignGrpList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 新增沟通对象搜索记录
	 * @param map
	 */
	public void addMemSearchLog(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.addMemSearchLog");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 新增沟通对象
	 * @param list
	 */
	public void addCustomerInfo(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLCPCOM03.addCustomerInfo");
	}
	
	/**
	 * 更新对象记录数
	 * @param map
	 */
	public void updRecordCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.updRecordCount");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 取得会员信息List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getMemInfoList (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getMemInfoList");
		return baseServiceImpl.getList(map);
	}
    /**
     * 总数
     * 
     * @param map
     * @return 
     */
	public Map<String,Object> getMemberInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getMemberInfo");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	/**
     * 活动对象总数
     * 
     * @param map
     * @return 
     */
	public Map<String,Object> getRecordCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getRecordCount");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	/**
	 * 更新对象个数，名称
	 * @param map
	 */
	public void updMemSearchLog(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.updMemSearchLog");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 删除活动对象
	 * @param map
	 */
	public int delCustomer(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.delCustomer");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 复制SearchLog表信息
	 * @param map
	 */
	public void copySearchLogInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.copySearchLogInfo");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 复制CustomerInfo表信息
	 * @param map
	 */
	public void copyCustomerInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.copyCustomerInfo");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 取得活动档次Id 
	 * @param map
	 * @return
	 */
	public List<String> getCampRuleIdList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getCampRuleIdList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 新增Coupon
	 * @param list
	 */
	public void addCouponCreateLog(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLCPCOM03.addCouponCreateLog");
	}
	
	/**
	 * 删除Coupon
	 * @param map
	 */
	public int delCouponCreateLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.delCouponCreateLog");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得Coupon信息List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCouponList (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getCouponList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 会员主题活动数
	 * 
	 * @param map
	 * @return
	 */
	public int getCouponCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getCouponCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得已经存在Coupon记录
	 * @param map
	 * @return
	 */
	public String getCouponNum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM03.getCouponNum");
		return (String)baseServiceImpl.get(parameterMap);
	}
}

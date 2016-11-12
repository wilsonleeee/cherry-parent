/*
 * @(#)BINOLSSPRM68_Service.java     1.0 2015/09/21
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


import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLSSPRM74_Service extends BaseService {
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProductInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getProductInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActivityInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getActivityInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	public void insertMain(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.insertMain");
		baseServiceImpl.save(map);
	}
	
	public void insertCoupon(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSSPRM74.insertCoupon");
	}
	
	public void insertRule(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSSPRM74.insertRule");
	}
	
	public void insertCart(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSSPRM74.insertCart");
	}
	
	public Map<String,Object> getDateSourceName(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getDateSourceName");
		return (Map<String,Object>)baseConfServiceImpl.get(map);
	}
	
	public Map<String,Object> getOrganizationID(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getOrganizationID");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	public int checkMain(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.checkMain");
		return baseServiceImpl.getSum(map);
	}
	
	public int delmain_all(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.delmain_all");
		return baseServiceImpl.remove(map);
	}
	
	public Map<String,Object> getMemberLevel(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getMemberLevel");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	public List<Map<String, Object>> getAllActivity(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getAllActivity");
		return baseServiceImpl.getList(map);
	}
	
	public Map<String,Object> collect2pro(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.collect2pro");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	public Map<String,Object> getMainByTradeNo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getMainByTradeNo");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	public List<Map<String, Object>> getShoppingCartByTradeNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getShoppingCartByTradeNo");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String, Object>> getRuleByTradeNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getRuleByTradeNo");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String, Object>> getCouponByTradeNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getCouponByTradeNo");
		return baseServiceImpl.getList(map);
	}
	
	public void updateProCoupon(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.updateProCoupon");
		baseServiceImpl.update(map);
	}

	public int getNoMemberCouponCount(){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getNoMemberCouponCount");
		return baseServiceImpl.getSum(map);
	}

	public List<Map<String, Object>> getCartDetailByTradeNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getCartDetailByTradeNo");
		return baseServiceImpl.getList(map);
	}

	public List<Map<String, Object>> getRuleDetailByTradeNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getRuleDetailByTradeNo");
		return baseServiceImpl.getList(map);
	}

	public List<Map<String, Object>> getCouponDetailByTradeNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM74.getCouponDetailByTradeNo");
		return baseServiceImpl.getList(map);
	}

	public void deleteCartDetail(List<Map<String,Object>> coupon_list) {
		baseServiceImpl.deleteAll(coupon_list,"BINOLSSPRM74.deleteCartDetail");
	}

	public void deleteRuleDetail(List<Map<String,Object>> coupon_list) {
		baseServiceImpl.deleteAll(coupon_list,"BINOLSSPRM74.deleteRuleDetail");
	}

	public void deleteCouponDetail(List<Map<String,Object>> coupon_list) {
		baseServiceImpl.deleteAll(coupon_list,"BINOLSSPRM74.deleteCouponDetail");
	}

}

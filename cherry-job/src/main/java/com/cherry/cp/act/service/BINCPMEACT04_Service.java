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
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 导入会员活动和会员活动预约信息处理Service
 * 
 * @author WangCT
 * @version 1.0 2013/02/20
 */
public class BINCPMEACT04_Service extends BaseService {
	
	/**
	 * 取得需要batch生成单据的活动List
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public List<Map<String, Object>> getSubCampList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getSubCampList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得历史预约会员
	 * @param map
	 * @return
	 */
	public List<Integer> getOrderHisMebList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getOrderHisMebList");
		return baseServiceImpl.getList(param);
	}
	/**
	 * 取得coupon
	 * @param map
	 * @return
	 */
	public List<String> getCouponList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getCouponList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得历史预约会员
	 * @param map
	 * @return
	 */
	public List<String> getCouponHisList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getCouponHisList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得历史预约会员size
	 * @param map
	 * @return
	 */
	public int getCouponHisSize(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getCouponHisSize");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 * 取得历史预约非会员
	 * @param map
	 * @return
	 */
	public List<String> getOrderHisNoMebList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getOrderHisNoMebList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得会员活动奖励结果
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public List<Map<String, Object>> getPrtList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getPrtList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得会员活动对象
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public String getMemConInfo(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getMemConInfo");
		return CherryBatchUtil.getString(baseServiceImpl.get(param));
	}
	
	/**
	 * 取得过期未领的单据LIST
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public List<Map<String, Object>> getNGBillList(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getNGBillList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 更新会员活动预约表
	 * @param list
	 */
	public void updCampOrderNG(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINCPMEACT04.updCampOrderNG");
	}
	
	/**
	 * 更新会员活动预约表
	 * @param list
	 */
	public void updCampOrderHisNG(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINCPMEACT04.updCampOrderHisNG");
	}
	
	/**
	 * 取得过期未领的单据LIST
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public List<Integer> getCampOrderIdList(List<String> orderNoList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderNoList",orderNoList);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT04.getCampOrderIdList");
		return baseServiceImpl.getList(param);
	}
}

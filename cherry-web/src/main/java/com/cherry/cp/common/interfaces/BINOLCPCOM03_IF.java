/*	
 * @(#)BINOLCPCOM03_IF.java     1.0 2011/7/18		
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
package com.cherry.cp.common.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 会员活动共通调用方法 IF
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public interface BINOLCPCOM03_IF {
	
	/**
     * 取得会员等级List
     * 
     * @param map
     * @return
     * 		会员等级List
     */
    public List<Map<String, Object>> getMemberLevelList(Map<String, Object> map);
    
    /**
     * 取得会员活动组信息List
     * 
     * @param map
     * @return
     * 		会员活动组信息List
     */
    public List<Map<String, Object>> getCampaignGrpList(Map<String, Object> map);
    
	/**
     * 取得导入成功会员信息List
     * 
     * @param map
     * @return
     * 		取得会员信息List
     */
    public List<Map<String, Object>> getMemInfoList(Map<String, Object> map);
    /**
     * 取得导入成功会员数量
     * @param map
     * @return
     */
	public Map<String, Object> getMemberInfo(Map<String, Object> map);
    
    /**
	 * 保存活动对象
	 * @param map
	 * @param list
	 * @return
	 */
    public Map<String, Object> tran_SaveMember(Map<String, Object> map,List<Map<String, Object>> list)throws Exception;
    
    
    /**
	 * 新增沟通对象搜索记录
	 * @param map
	 * @param recordType
	 */
	public String addMemSearchLog(Map<String, Object> map, String recordType)throws Exception;
	
	/**
	 * 新增沟通对象
	 * @param list
	 */
	public void addCustomerInfo(List<Map<String, Object>> list)throws Exception;
	
	/**
	 * 更新对象记录数
	 * @param map
	 */
	public void updRecordCount(Map<String, Object> map) throws Exception;
	
	 /**
     * 取得活动对象数量
     * @param map
     * @return
     */
	public Map<String, Object> getRecordCount(Map<String, Object> map);
	
	 /**
     * 取得活动对象数量
     * @param map
     * @return
     */
	public int searchMemCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 生成新的SearchCode
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getNewSearchCode(Map<String, Object> map)throws Exception;

	
	/**
	 * 取得活动档次Id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<String> getCampRuleIdList(Map<String, Object> map)throws Exception;

	/**
	 * 保存Coupon信息
	 * @param map
	 * @param list
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> tran_SaveCoupon(Map<String, Object> map, List<Map<String, Object>> list) throws Exception;


	/**
     * 取得导入成功Coupon List
     * 
     * @param map
     * @return
     * 		取得会员信息List
     */
    public List<Map<String, Object>> getCouponList(Map<String, Object> map);

	public int getCouponCount(Map<String, Object> map);

	public Object getCouponNum(Map<String, Object> map);
	
}

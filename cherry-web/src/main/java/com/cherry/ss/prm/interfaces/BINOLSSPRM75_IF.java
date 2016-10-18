/*		
 * @(#)BINOLSSPRM75_IF.java     1.0 2016/05/05		
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
package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.ss.prm.dto.CouponRuleDTO;

/**
 * 优惠券IF
 * @author ghq
 * @version 1.0 2016.05.05
 */
public interface BINOLSSPRM75_IF {
	
	/**
	 * 取得优惠券总数
	 * 
	 * @param map 检索条件
	 * @return 优惠券总数
	 */
	public int getCouponInfoCount(Map<String, Object> map);
	
	/**
	 * 取得优惠券List
	 * 
	 * @param map 检索条件
	 * @return 优惠券List
	 */
	public List<Map<String, Object>> getCouponInfoList(Map<String, Object> map);
	
	/**
	 * 取得优惠券信息
	 * 
	 * @param map 检索条件
	 * @return 优惠券信息map
	 */
	public Map<String, Object> getCouponInfo(Map<String, Object> map);
	
	/**
	 * 保存优惠券使用时间
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveCoupon(Map<String, Object> map) throws Exception;
	
	/**
	 * 取消优惠券
	 * 
	 * @param map
	 * @return int
	 */
	public void stopCoupon(Map<String, Object> map);
}

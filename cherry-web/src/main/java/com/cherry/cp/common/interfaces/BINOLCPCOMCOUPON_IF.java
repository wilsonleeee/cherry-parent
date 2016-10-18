/*	
 * @(#)BINOLCPCOMCOUPON_IF.java     1.0 2013/04/11		
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

import com.cherry.cm.core.CherryConstants;

/**
 * 会员活动Coupon码生成 IF
 * 
 * @author shenzg
 * @version 1.0 2013.04.11
 */
public interface BINOLCPCOMCOUPON_IF {
	
	/**
     * 根据给定的主题活动，生成指定数量的不重复的Coupon码
     * 
     * @param map
     * @return
     * 		生成的8位Coupon码List
	 * @throws Exception 
     */
    public List<String> generateCoupon(Map<String, Object> map) throws Exception;
    
    /**
     * 初始化生成Coupon码的设定信息，RSA算法必须的参数
     * 
     * @param map
     * @return
     * 		RSA算法中需要的参数P，Q，E等
     */
    public Map<String, Object> initCouponFact(Map<String, Object> map);
    

	/**
	 * 取得活动已生成的coupon信息List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCouponHisList(Map<String, Object> map) throws Exception;
	
	/**
	 * 删除活动已生成的coupon信息List
	 * @param list
	 * @throws Exception
	 */
	public void delCouponHisList(List<Map<String, Object>> list)throws Exception;
}

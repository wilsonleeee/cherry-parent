/*	
 * @(#)BINOLCPCOMCOUPON_BL.java     1.0 2013/04/11		
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
package com.cherry.cp.common.bl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cherry.cp.common.service.BINOLCPCOMCOUPON_Service;

/**
 * 会员活动Coupon码生成
 * 
 * @author shenzg
 * @version 1.0 2013.04.11
 */
@Component
public class BINOLCPCOMCOUPON_COM_BL{

	@Resource(name = "cherryCouponService")
	private BINOLCPCOMCOUPON_Service ser;
	
	
	/**
	 * 新增Coupon设定记录，初始化时被调用
	 * 
	 * @param map
	 */
	@Transactional(value = "txManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Map<String, Object> addCouponInfo(Map<String, Object> map)
			throws Exception {
		Map<String, Object> couponInfo = ser.addCouponInfo(map);
		return couponInfo;
	}

	/**
	 * 更新Coupon设定记录中的序列数
	 * 
	 * @param map
	 */
	@Transactional(value = "txManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Map<String, Object> updCouponSeq(Map<String, Object> map) throws Exception {
		ser.updCouponSeq(map);
		Map<String, Object> couponInfo = ser.getCouponInfo(map);
		if(null != couponInfo){
			int couponCount = Integer.parseInt(map.get("couponCount").toString());
			int seqNum = Integer.parseInt(couponInfo.get("seqNum").toString());
			// 原始序列号 = 新序列号 - 取号数量
			couponInfo.put("seqNum", seqNum - couponCount);
		}
		return couponInfo;
	}

	/**
	 * 求最小公约数
	 */
	public long gcd(long a, long b) {
		long gcd;
		if (b == 0)
			gcd = a;
		else
			gcd = gcd(b, a % b);
		return gcd;
	}
}

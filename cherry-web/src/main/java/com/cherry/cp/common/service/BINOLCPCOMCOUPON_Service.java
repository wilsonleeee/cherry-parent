/*	
 * @(#)BINOLCPCOMCOUPON_Service.java     1.0 2013/04/11		
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


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员活动共通 Service
 * 
 * @author shenzg
 * @version 1.0 2013.04.11
 */
@Component("cherryCouponService")
public class BINOLCPCOMCOUPON_Service extends BaseService{
	
	/**
	 * 更新Coupon设定记录中的序列数并返回更新前Coupon设定信息
	 * @param map
	 */
	public Map<String, Object> getCouponInfo(Map<String, Object> map) throws Exception{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOMCOUPON.getCouponInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新Coupon设定记录中的序列数并返回更新前Coupon设定信息
	 * @param map
	 */
	public void updCouponSeq(Map<String, Object> map) throws Exception{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOMCOUPON.updCouponSeq");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 新增Coupon设定记录，初始化时被调用
	 * @param map
	 */
	public Map<String, Object> addCouponInfo(Map<String, Object> map) throws Exception{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOMCOUPON.addCouponInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得活动已生成的coupon信息List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCouponHisList(Map<String, Object> map) throws Exception{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOMCOUPON.getCouponHisList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 删除活动已生成的coupon信息List
	 * @param list
	 * @throws Exception
	 */
	public void delCouponHisList(List<Map<String, Object>> list)throws Exception{
		baseServiceImpl.deleteAll(list, "BINOLCPCOMCOUPON.delCouponHisList");
	}
}

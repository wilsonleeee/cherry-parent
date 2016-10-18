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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.cp.common.service.BINOLCPCOMCOUPON_Service;

/**
 * 会员活动Coupon码生成
 * 
 * @author shenzg
 * @version 1.0 2013.04.11
 */
@Component
public class BINOLCPCOMCOUPON_BL implements BINOLCPCOMCOUPON_IF {

	protected static final Logger logger = LoggerFactory
			.getLogger(BINOLCPCOMCOUPON_BL.class);

	public static final int bitCount = 13;// 2的13次方的大质数
	public static final int radix = 10;// 加密进制
	public static final String TEMPLETE = "00000000";
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	@Resource(name = "cherryCouponService")
	private BINOLCPCOMCOUPON_Service ser;
	
	@Resource(name = "coupon_com_bl")
	private BINOLCPCOMCOUPON_COM_BL bl;

	/**
	 * 根据给定的主题活动，生成指定数量的不重复的8位Coupon码
	 * 
	 * @param map
	 * @return 生成的8位Coupon码List
	 * @throws Exception
	 */
	@Override
	public List<String> generateCoupon(Map<String, Object> map)
			throws Exception {
		BigInteger bi;
		String couponCode = "";
		Map<String, Object> couponInfoResult = new HashMap<String, Object>();
		Map<String, Object> CouponInfoMap = new HashMap<String, Object>();
		List<String> CouponCodeList = new ArrayList<String>();
		// coupon前缀
		String couponPrefix = binOLCM14_BL.getConfigValue("1127", 
				ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)), 
				ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		int couponCount = 0, oldSeq = 1;
		long e = 0L, r = 0L;
		// 组织信息ID
		CouponInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		CouponInfoMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		// 主题活动代号
		CouponInfoMap.put(CampConstants.CAMP_CODE,
				map.get(CampConstants.CAMP_CODE));
		// 需要获取的Coupon码数量
		CouponInfoMap.put("couponCount", map.get("couponCount"));

		// 更新主题活动的Coupon设定信息
		couponInfoResult = bl.updCouponSeq(CouponInfoMap);
		if (couponInfoResult != null) {
			couponCount = Integer.parseInt(map.get("couponCount").toString());
			e = Long.parseLong(couponInfoResult.get("primeE").toString());
			r = Long.parseLong(couponInfoResult.get("npq").toString());
			oldSeq = Integer
					.parseInt(couponInfoResult.get("seqNum").toString());

		} else {
			// 初始化计算Coupon码所需的参数
			CouponInfoMap = initCouponFact(CouponInfoMap);
			// 将主题活动的Coupon设定信息保持到会员活动Coupon信息表中
			couponInfoResult = bl.addCouponInfo(CouponInfoMap);
			// 需要转成8为字符，不足部分前面补零
			couponCount = Integer.parseInt(map.get("couponCount").toString());
			e = Long.parseLong(couponInfoResult.get("primeE").toString());
			r = Long.parseLong(couponInfoResult.get("npq").toString());
			// 第一次有序数从2开始
			oldSeq = 2;

		}

		// 生成指定数量的随机数并返回
		for (long i = 1; i <= couponCount; i++) {
			bi = BigInteger.valueOf(oldSeq + i);
			couponCode = bi
					.modPow(BigInteger.valueOf(e), BigInteger.valueOf(r))
					.toString();
			if (couponCode.length() < 8) {
				couponCode = TEMPLETE.substring(0, 8 - couponCode.length())
						+ couponCode;
			}
			CouponCodeList.add(couponPrefix+couponCode);
		}
		return CouponCodeList;
	}

	/**
	 * 初始化生成Coupon码的设定信息，RSA算法必须的参数
	 * 
	 * @param map
	 * @return RSA算法中需要的参数P，Q，E等
	 */
	@Override
	public Map<String, Object> initCouponFact(Map<String, Object> map) {
		Random rdm = new SecureRandom();

		long p = BigInteger.probablePrime(bitCount, rdm).longValue();
		long q = BigInteger.probablePrime(bitCount, rdm).longValue();
		// P不能等于Q
		while(true){
			if(p!=q)
				break;
			q = BigInteger.probablePrime(bitCount, rdm).longValue();
		}
		long r = p * q;
		long olr = (p - 1) * (q - 1);
		long e = BigInteger.probablePrime(bitCount, rdm).longValue();
		while (true) {
			// e必须小于(p - 1) * (q - 1)
			if(e<olr){
				//e和(p - 1) * (q - 1)互质
				if (bl.gcd(e, olr) == 1) {
					break;
				}
			}
			e = BigInteger.probablePrime(bitCount, rdm).longValue();
		}
		Map<String, Object> CouponInfoMap = new HashMap<String, Object>();
		// 组织信息ID
		CouponInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		CouponInfoMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		// 主题活动代号
		CouponInfoMap.put(CampConstants.CAMP_CODE,
				map.get(CampConstants.CAMP_CODE));
		// 大质数P
		CouponInfoMap.put("primeP", p);
		// 大质数Q
		CouponInfoMap.put("primeQ", q);
		// 大质数P与大质数Q的乘积
		CouponInfoMap.put("npq", r);
		// 大质数E
		CouponInfoMap.put("primeE", e);
		// 初始序列数，从2开始加上需要获取数量
		int i = 2 + Integer.parseInt(map.get("couponCount").toString());
		CouponInfoMap.put("seqNum", i);

		return CouponInfoMap;
	}

	@Override
	public List<Map<String, Object>> getCouponHisList(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return ser.getCouponHisList(map);
	}

	@Override
	public void delCouponHisList(List<Map<String, Object>> list)
			throws Exception {
		ser.delCouponHisList(list);
	}

}

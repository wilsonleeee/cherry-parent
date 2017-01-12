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

import com.cherry.cm.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cp.common.CampConstants;

/**
 * 会员活动Coupon码生成
 * 
 * @author shenzg
 * @version 1.0 2013.04.11
 */
@Component
public class BINOLCPCOMCOUPON_6_BL{

	protected static final Logger logger = LoggerFactory
			.getLogger(BINOLCPCOMCOUPON_6_BL.class);

	public static final int bitCount = 11;// 2的11次方的大质数
	
	public static final int bitCountQ = 11;// 2的11次方的大质数
	
	public static final int length = 6;// coupon位数
	public static final int defSeqNum = 2;
	public static final int  defMax= 999999;
	
	@Resource(name = "coupon_com_bl")
	private BINOLCPCOMCOUPON_COM_BL bl;

	/**
	 * 根据给定的主题活动，生成指定数量的不重复的6位Coupon码
	 * 
	 * @param map
	 * @return 生成的6位Coupon码List,随机数已取满时返回null
	 * @throws Exception
	 */
	public List<String> generateCoupon(Map<String, Object> map)
			throws Exception {
		BigInteger bi;
		String couponCode = "";
		Map<String, Object> couponInfoResult = new HashMap<String, Object>();
		Map<String, Object> couponInfoMap = new HashMap<String, Object>();
		List<String> CouponCodeList = new ArrayList<String>();

		int couponCount = 0, oldSeq = 1;
		long e = 0L, r = 0L;
		// 组织信息ID
		couponInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		couponInfoMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		// 主题活动代号
		couponInfoMap.put(CampConstants.CAMP_CODE,
				map.get(CampConstants.CAMP_CODE));
		// 需要获取的Coupon码数量
		couponInfoMap.put("couponCount", map.get("couponCount"));
		int getCount = ConvertUtil.getInt(map.get("couponCount"));

		Map<String, Object> couponInfo = bl.getCouponInfo(couponInfoMap);
		if(null != couponInfo && !couponInfo.isEmpty()) {
			long npq = ConvertUtil.getInt(couponInfo.get("npq"));
			long seqNum = ConvertUtil.getInt(couponInfo.get("seqNum"));
			if(npq <= seqNum) {
				// 随机数已取满
				return null;
			}
			long maxCount = npq;//defMax < npq ? defMax : npq;
			// 随机数总数不可超过Npq数量
			if(seqNum + getCount > maxCount) {
				couponInfoMap.put("couponCount",maxCount-seqNum);
			}
		}
		// 获取主题活动的Coupon设定信息
		couponInfoResult = bl.updCouponSeq(couponInfoMap);
		if (couponInfoResult != null) {
			couponCount = ConvertUtil.getInt(couponInfoMap.get("couponCount"));
			e = Long.parseLong(couponInfoResult.get("primeE").toString());
			r = Long.parseLong(couponInfoResult.get("npq").toString());
			oldSeq = Integer.parseInt(couponInfoResult.get("seqNum").toString());
		} else {
			// 初始化计算Coupon码所需的参数
			couponInfoMap = initCouponFact(couponInfoMap);
			// 将主题活动的Coupon设定信息保持到会员活动Coupon信息表中
			couponInfoResult = bl.addCouponInfo(couponInfoMap);

			// 极端情况,首次取数总量>npq
			long maxCount = ConvertUtil.getInt(couponInfoMap.get("npq"));
			//defMax < ConvertUtil.getInt(couponInfoMap.get("npq")) ? defMax : ConvertUtil.getInt(couponInfoMap.get("npq"));

			// 随机数总数不可超过Npq数量
			if(defSeqNum + getCount > maxCount) {
				couponInfoMap.put("couponCount",maxCount-defSeqNum);
			}

			// 需要转成8为字符，不足部分前面补零
			couponCount = ConvertUtil.getInt(couponInfoMap.get("couponCount"));
			e = Long.parseLong(couponInfoResult.get("primeE").toString());
			r = Long.parseLong(couponInfoResult.get("npq").toString());
			// 第一次有序数从2开始
			oldSeq = defSeqNum;

		}

		// 生成指定数量的随机数并返回
		for (long i = 1; i <= couponCount; i++) {
			bi = BigInteger.valueOf(oldSeq + i);
//			if(bi.compareTo(BigInteger.valueOf(r)) >= 0){
//				bi = bi.mod(BigInteger.valueOf(r)).add(BigInteger.valueOf(defSeqNum));
//			}
			BigInteger biResult = bi.modPow(BigInteger.valueOf(e), BigInteger.valueOf(r));

			if(biResult.compareTo(BigInteger.valueOf(defMax)) > 0) {
//				couponCount++;
				continue;
			}
			couponCode = String.format("%0" + length + "d",biResult);
			if( couponCode.length() > length){
				couponCode = couponCode.substring(0, length);
			}
			CouponCodeList.add(couponCode);
		}
		return CouponCodeList;
	}

	/**
	 * 初始化生成Coupon码的设定信息，RSA算法必须的参数
	 * 
	 * @param map
	 * @return RSA算法中需要的参数P，Q，E等
	 */
	private Map<String, Object> initCouponFact(Map<String, Object> map) {
		long p = 0L;
		long q = 0L;
		long e = 0L;
		while(true) {
			Random rdm = new SecureRandom();
			p = BigInteger.probablePrime(bitCount, rdm).longValue();
			if(isPrime(p))break;
		}
		while(true) {
			Random rdm = new SecureRandom();
			q = BigInteger.probablePrime(bitCountQ, rdm).longValue();
			if(isPrime(q))break;
		}
		// P不能等于Q
		while(true){
			if(p!=q)
				break;
			while(true) {
				Random rdm = new SecureRandom();
				q = BigInteger.probablePrime(bitCountQ, rdm).longValue();
				if(isPrime(q))break;
			}

		}
		long r = p * q;
		long olr = (p - 1) * (q - 1);
		while(true) {
			Random rdm = new SecureRandom();
			e = BigInteger.probablePrime(bitCount, rdm).longValue();
			if(isPrime(e))break;
		}
		while (true) {
			// e必须小于(p - 1) * (q - 1)
			if(e<olr){
				//e和(p - 1) * (q - 1)互质
				if (bl.gcd(e, olr) == 1) {
					break;
				}
			}
			while(true) {
				Random rdm = new SecureRandom();
				e = BigInteger.probablePrime(bitCount, rdm).longValue();
				if(isPrime(e))break;
			}
		}
		Map<String, Object> couponInfoMap = new HashMap<String, Object>();
		// 组织信息ID
		couponInfoMap.put(CherryConstants.ORGANIZATIONINFOID,
				map.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		couponInfoMap.put(CherryConstants.BRANDINFOID,
				map.get(CherryConstants.BRANDINFOID));
		// 主题活动代号
		couponInfoMap.put(CampConstants.CAMP_CODE,
				map.get(CampConstants.CAMP_CODE));
		couponInfoMap.put("couponCount",map.get("couponCount"));
		// 大质数P
		couponInfoMap.put("primeP", p);
		// 大质数Q
		couponInfoMap.put("primeQ", q);
		// 大质数P与大质数Q的乘积
		couponInfoMap.put("npq", r);
		// 大质数E
		couponInfoMap.put("primeE", e);
		// 初始序列数，从2开始加上需要获取数量
		int i = defSeqNum + Integer.parseInt(map.get("couponCount").toString());
		couponInfoMap.put("seqNum", i);

		return couponInfoMap;
	}

	/**
	 * <pre>
	 * 用于判断一个数是否为素数，若为素数，返回true,否则返回false
	 * </pre>
	 *
	 * @param a
	 *            输入的值
	 * @return true、false
	 */
	public static boolean isPrime(long a) {

		boolean flag = true;

		if (a < 2) {// 素数不小于2
			return false;
		} else {

			for (int i = 2; i <= Math.sqrt(a); i++) {

				if (a % i == 0) {// 若能被整除，则说明不是素数，返回false

					flag = false;
					break;// 跳出循环
				}
			}
		}
		return flag;
	}
}

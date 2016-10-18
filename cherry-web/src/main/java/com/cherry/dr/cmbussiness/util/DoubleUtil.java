/*	
 * @(#)DoubleUtil.java     1.0 2011/09/28		
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
package com.cherry.dr.cmbussiness.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Double型计算
 * 
 * @author hub
 * @version 1.0 2011.09.28
 */
public class DoubleUtil {
	
	private static final int DEF_DIV_SCALE = 10;
	
	/**
	 * 两个double型相减
	 * 
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(v1));
		BigDecimal b2 = new BigDecimal(String.valueOf(v2));
		double b = b1.subtract(b2).doubleValue();
		return b;
	}
	
	/**
	 * 两个double型相加
	 * 
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(v1));
		BigDecimal b2 = new BigDecimal(String.valueOf(v2));
		double b = b1.add(b2).doubleValue();
		return b;
	}
	
	/**
	* * 两个Double数相乘
	* 
	* @param v1
	* @param v2
	* @return Double
	*/
	public static double mul(double v1, double v2) {
	   BigDecimal b1 = new BigDecimal(String.valueOf(v1));
	   BigDecimal b2 = new BigDecimal(String.valueOf(v2));
	   double b = b1.multiply(b2).doubleValue();
	   return b;
	}
	
	/**
	* * 两个Double数相除 *
	* 
	* @param v1 *
	* @param v2 *
	* @return Double
	*/
	public static double div(double v1, double v2) {
	   BigDecimal b1 = new BigDecimal(String.valueOf(v1));
	   BigDecimal b2 = new BigDecimal(String.valueOf(v2));
	   return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
	     .doubleValue();
	}
	
	/**
	* 提供精确的小数位四舍五入处理。
	* 
	* @param v 需要四舍五入的数字
	* @param scale 小数点后保留几位
	* @return 四舍五入后的结果
	*/
	public static double round(double v, int scale) {
	   if (scale < 0) {
	    throw new IllegalArgumentException(
	      "The scale must be a positive integer or zero");
	   }
	   BigDecimal b = new BigDecimal(Double.toString(v));
	   return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	* 提供精确的小数位只舍不入处理。
	* 
	* @param v 需要只舍不入的数字
	* @param scale 小数点后保留几位
	* @return 只舍不入后的结果
	*/
	public static double roundDown(double v, int scale) {
	   if (scale < 0) {
	    throw new IllegalArgumentException(
	      "The scale must be a positive integer or zero");
	   }
	   BigDecimal b = new BigDecimal(Double.toString(v));
	   return b.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
	}
	
	/**
	* 提供精确的小数位只入不舍处理。
	* 
	* @param v 需要只入不舍的数字
	* @param scale 小数点后保留几位
	* @return 只入不舍后的结果
	*/
	public static double roundCeiling(double v, int scale) {
	   if (scale < 0) {
	    throw new IllegalArgumentException(
	      "The scale must be a positive integer or zero");
	   }
	   BigDecimal b = new BigDecimal(Double.toString(v));
	   return b.setScale(scale, BigDecimal.ROUND_CEILING).doubleValue();
	}
	
	/**
	* 将double型转String(非科学计数法)
	* 
	* @param v 需要转换的数字
	* @return 转换结果
	*/
	public static String douToStr(double v) {
		if (v >= 10000000 || v <= -10000000) {
			// 格式化输出
			DecimalFormat df = new DecimalFormat("#");
			return df.format(v);
		}
		return String.valueOf(v);
	}
}

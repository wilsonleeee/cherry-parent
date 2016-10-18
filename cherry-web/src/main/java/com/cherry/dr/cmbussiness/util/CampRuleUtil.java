/*	
 * @(#)CampRuleUtil.java     1.0 2010/10/12		
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

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.mq.mes.common.CherryMQException;

/**
 * 规则共通方法
 * 
 * @author hub
 * @version 1.0 2010.10.12
 */
public class CampRuleUtil {
	
	protected static final Logger logger = LoggerFactory.getLogger(CampRuleUtil.class);
	
	public static String getMessage(String msg, String[]params) {
		if (msg != null && params != null) {
			for (int i = 0; i < params.length; i++) {
				String reg = "{" + i + "}";
				if (null == params[i]) {
					params[i] = "";
				}
				msg = msg.replace(reg, params[i]);
			}
		}
		return msg;
	}
	
	/**
	 * <p>
	 * 判断对象数组中是否存在相同值的对象
	 * </p>
	 * 
	 * 
	 * @param tArray
	 *            对象数组
	 * @param obj
	 *            比较的对象
	 * @return boolean 比较结果
	 * 
	 */
	public static <T> boolean isContain(T[] tArray, T obj) {
		if (tArray != null) {
			for (T t : tArray) {
				if (t != null && t.equals(obj)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 单笔金额化妆次数计算
	 * 
	 * @param campBaseDTO
	 *            规则执行DTO
	 * @param amount
	 *            每次兑换金额
	 * @param maxBtimes
	 *            最大化妆次数
	 * 
	 */
	public static boolean curBtimesByAmount (CampBaseDTO campBaseDTO, double amount) {
		// 当前累计金额
		double saleAmount = campBaseDTO.getAmount();
		if (saleAmount >= amount && 0 != amount) {
			// 当前化妆次数
			int curBtimes = campBaseDTO.getCurBtimes();
			int btimes = (int) (saleAmount/amount);
			curBtimes += btimes;
			campBaseDTO.setCurBtimes(curBtimes);
			return true;
		}
		return false;
	}
	
	/**
	 * 累计金额化妆次数计算
	 * 
	 * @param campBaseDTO
	 *            规则执行DTO
	 * @param amount
	 *            每次兑换金额
	 * @param maxBtimes
	 *            最大化妆次数
	 * 
	 */
	public static boolean curBtimesByTotal (CampBaseDTO campBaseDTO, double amount, int maxBtimes) {
		// 当前累计金额
		double curBtimesAmount = campBaseDTO.getCurBtimesAmount();
		if (curBtimesAmount >= amount && 0 != amount) {
			// 当前化妆次数
			int curBtimes = campBaseDTO.getCurBtimes();
			if (curBtimes < maxBtimes) {
				int btimes = (int) (curBtimesAmount/amount);
				int newCurBtimes = curBtimes + btimes;
				newCurBtimes = newCurBtimes > maxBtimes? maxBtimes : newCurBtimes;
				btimes = newCurBtimes - curBtimes;
				curBtimesAmount = DoubleUtil.sub(curBtimesAmount, btimes * amount);
				campBaseDTO.setCurBtimes(newCurBtimes);
				campBaseDTO.setCurBtimesAmount(curBtimesAmount);
			} else {
				campBaseDTO.setCurBtimes(maxBtimes);
			}
			return true;
		}
		return false;
	}
	
	
	/**
	 * 根据会员有效期限判断是否做清零处理
	 * 
	 * @param campBaseDTO
	 * @param months
	 * @return boolean true:是      false:否
	 */
	public static boolean isNeedClear(String startDate, String busDate, int months) {
		if (null == startDate || "".equals(startDate)) {
			return false;
		}
		while (true) {
			// 会员有效截止日期
			String endDate = DateUtil.addDateByMonth(
					DroolsConstants.DATE_PATTERN, startDate, months);
			String endDateTemp = DateUtil.addDateByDays(
					DroolsConstants.DATE_PATTERN, endDate, -1);
			int result = DateUtil.compareDate(endDateTemp, busDate);
			if (0 == result) {
				return true;
			} else if (result > 0) {
				return false;
			}
			startDate = endDate;
		}
	}
	
	/**
	 * 根据会员有效期限计算出清零日期
	 * 
	 * @param campBaseDTO
	 * @param months
	 * @return boolean true:是      false:否
	 */
	public static String calcClearDate(String startDate, String busDate, int months) {
		if (null == startDate || "".equals(startDate)) {
			return null;
		}
		while (true) {
			// 会员有效截止日期
			String endDate = DateUtil.addDateByMonth(
					DroolsConstants.DATE_PATTERN, startDate, months);
			String endDateTemp = DateUtil.addDateByDays(
					DroolsConstants.DATE_PATTERN, endDate, -1);
			int result = DateUtil.compareDate(endDateTemp, busDate);
			if (0 <= result) {
				return endDateTemp;
			}
			startDate = endDate;
		}
	}

	/**
	 * 会员初始导入时计算当前可兑换金额(化妆次数)
	 * 
	 * @param campBaseDTO
	 * @param amount
	 *            每次兑换金额
	 * 
	 */
	public static void upCurBtimesAmount(CampBaseDTO campBaseDTO, double amount) {
		double totalAmount = campBaseDTO.getCurTotalAmount();
		int count = (int) (totalAmount / amount);
		// 当前累计金额
		double curBtimesAmount = DoubleUtil.sub(totalAmount, count * amount);
		campBaseDTO.setCurBtimesAmount(curBtimesAmount);
	}
	
	/**
	 * 转换规则执行结果描述
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param params
	 *          参数集合
	 * 
	 */
	public static void convertResultDpt(CampBaseDTO c, Map<String, Object> params) {
		if (null != params && !params.isEmpty()) {
			// 规则结果描述
			String resultDpt = c.getResultDpt();
			if (null != resultDpt && !"".equals(resultDpt)) {
				for(Map.Entry<String, Object> en : params.entrySet()){
					String key = en.getKey();
					String value = String.valueOf(en.getValue());
					resultDpt = resultDpt.replaceAll(key, value);
				}
				// 转换后的规则结果描述
				c.setResultDpt(resultDpt);
			}
		}
	}
	
	 /**
	 * 把map转化成bean
	 * 
	 * @param map 待转化的map对象
	 * @param cls bean的class对象
	 * @return 转化后的bean对象
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */    
	public static <T> T map2Bean(Map<String, Object> map, Class<T> cls) throws Exception {
		T obj = null;
	    obj = cls.newInstance();   
	   
	    // 取出bean里的所有方法
	    Method[] methods = cls.getMethods();
	    for(Method method : methods) {
	    	// 取方法名
	    	String methodName = method.getName();
	    	// 取出方法的类型   
	    	Class<?>[] cc = method.getParameterTypes();
	        if (cc.length != 1) continue;
	        // 如果方法名没有以set开头的则退出本次循环
	        if(methodName.indexOf("set") < 0 ) continue; 
        	// 转成小写
		    String value = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		    // 如果map里有该key
		    if(map.containsKey(value) && map.get(value) != null) {
		    	// 调用set方法把值设置到bean对象
		    	setValue(cc[0], map.get(value), method, obj);
		    }
	    }
	    return obj;
	}

	/**
	 * 调用set方法把值设置到bean对象
	 * 
	 * @param type 参数的数据类型
	 * @param value 参数的值
	 * @param method bean的方法
	 * @param bean bean对象
	 * @throws Exception 
	 */   
	private static void setValue(Class<?> type, Object value, Method method, Object bean) throws Exception {
		if (value != null) {
			String typeName = type.getSimpleName();
			if("String".equals(typeName)) {
				method.invoke(bean, new Object[] {String.valueOf(value)});
            } else if("int".equals(typeName) || "Integer".equals(typeName)) {
            	method.invoke(bean, new Object[] {new Integer(String.valueOf(value))});
            } else if("long".equals(typeName) || "Long".equals(typeName)) {
            	method.invoke(bean, new Object[] {new Long(String.valueOf(value))});
            } else if("float".equals(typeName) || "Float".equals(typeName)) {
            	method.invoke(bean, new Object[] {new Float(String.valueOf(value))});
            } else if("double".equals(typeName) || "Double".equals(typeName)) {
            	method.invoke(bean, new Object[] {new Double(String.valueOf(value))});
            } else if("boolean".equals(typeName) || "Boolean".equals(typeName)) {
            	method.invoke(bean, new Object[] {new Boolean(String.valueOf(value))});
            } else {
            	method.invoke(bean, value);
            }
		}
	}
	
	/**
	 * 取得组合key
	 * 
	 * @param joinChar 拼接字符
	 * @param objs 拼接对象
	 * 
	 */   
	public static String getCombKey(String joinChar, Object... objs) {
		if (null != objs) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < objs.length; i++) {
				if (0 != i) {
					buffer.append(joinChar);
				}
				buffer.append(objs[i]);
			}
			return buffer.toString();
		}
		return null;
	}
	
	/**
	 * 取得组合key(用下划线拼接)
	 * 
	 * @param objs 拼接对象
	 * 
	 */   
	public static String getCombKeyByLine(Object... objs) {
		// 取得组合key
		return getCombKey(DroolsConstants.SYMBOL_UNDERLINE, objs);
	}
	
	/**
	 * 给予默认等级
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @throws Exception 
	 */
	public static void giveDefaultLevel(CampBaseDTO c) throws Exception{
		// 取得会员默认等级信息
		Map<String, Object> levelInfo = getDefaultLevel(c);
		int curLevelId = Integer.parseInt(levelInfo.get("levelId").toString());
		// 会员等级
		c.setCurLevelId(curLevelId);
		// 入会时会员等级
		c.setGrantMemberLevel(curLevelId);
		// 当前等级名称
		c.setCurLevelName((String) levelInfo.get("levelName"));
		// 会员等级级别
		c.setGrade(Integer.parseInt(levelInfo.get("grade").toString()));
		// 入会区分
		c.setJoinKbn(DroolsConstants.JOINKBN_1);
		// 会员等级状态:实际等级
		c.setLevelStatus(CherryConstants.LEVELSTATUS_2);
		// 升级区分
		c.setChangeType(DroolsConstants.UPKBN_1);
		// 生成入会日期
		//CampRuleUtil.createJoinDate(c);
		// 设定等级有效期
		CampRuleUtil.execLevelValidity(c);
	}
	
	/**
	 * 设定等级有效期
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @throws Exception 
	 */
	public static void execLevelValidity(CampBaseDTO c) throws Exception{
		// 通过会员等级Id取得会员等级信息
		Map<String, Object> levelInfo = getLevelInfo(c, c.getCurLevelId());
		Map<String, Object> periodValidity = (Map<String, Object>) levelInfo.get("periodValidity");
		if (null != periodValidity && !periodValidity.isEmpty()) {
			String ticketDateTime = c.getTicketDate();
			String ticketDateStr = DateUtil.coverTime2YMD(ticketDateTime, DateUtil.DATETIME_PATTERN);
			Date ticketDate = DateUtil.coverString2Date(ticketDateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(ticketDate);
			// 单据年份
			int year = cal.get(Calendar.YEAR);
			// 原先的等级有效期开始日
			String oldLevelStartDate = c.getLevelStartDate();
			// 降级
			if (DroolsConstants.CAMPAIGN_TYPE9999.equals(c.getRuleType())) {
				String nextDay = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, ticketDateStr, 1);
				// 等级有效期开始日
				c.setLevelStartDate(DateUtil.suffixDate(nextDay, 0));
				ticketDateStr = nextDay;
			} else {
				// 等级有效期开始日
				c.setLevelStartDate(ticketDateTime);
			}
			// 结束日区分
			String endDateKbn = (String) periodValidity.get("normalYear");
			// 等级有效期结束日
			String levelEndDate =  null;
			// 自然年
			if ("0".equals(endDateKbn)) {
				// 自然年数
				String memberDate = (String) periodValidity.get("memberDate0");
				if (!CherryChecker.isNullOrEmpty(memberDate)) {
					int memberDateInt = Integer.parseInt(memberDate) - 1;
					// 结束日的年份
					year += memberDateInt;
					// 等级有效期结束日
					levelEndDate = DateUtil.createDate(year, 11, 31, DateUtil.DATE_PATTERN);
				}
				// 年(满12个月)
			} else if ("1".equals(endDateKbn)) {
				// 年数
				String memberDate = (String) periodValidity.get("memberDate1");
				if (!CherryChecker.isNullOrEmpty(memberDate)) {
					int memberDateInt = Integer.parseInt(memberDate);
					int months = memberDateInt * 12;
					// 从开始日起加上设定的年数
					String endDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, ticketDateStr, months);
					// 截止日区分
					String endKbn1 = (String) periodValidity.get("endKbn1");
					// 前一天
					if (CherryChecker.isNullOrEmpty(endKbn1) || "0".equals(endKbn1)) {
						levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endDate, -1);
						// 当天
					} else if ("1".equals(endKbn1)) {
						levelEndDate = endDate;
						// 当月月底
					} else {
						levelEndDate = DateUtil.getFirstOrLastDateYMD(endDate, 1);
					}
				}
				// 月
			} else if ("2".equals(endDateKbn)) {
				// 月数
				String memberDate = (String) periodValidity.get("memberDate2");
				if (!CherryChecker.isNullOrEmpty(memberDate)) {
					int memberDateInt = Integer.parseInt(memberDate);
					int months = memberDateInt;
					// 从开始日起加上设定的月数
					String endDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, ticketDateStr, months);
					// 截止日区分
					String endKbn2 = (String) periodValidity.get("endKbn2");
					// 前一天
					if (CherryChecker.isNullOrEmpty(endKbn2) || "0".equals(endKbn2)) {
						levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endDate, -1);
						// 当天
					} else if ("1".equals(endKbn2)) {
						levelEndDate = endDate;
						// 当月月底
					} else {
						levelEndDate = DateUtil.getFirstOrLastDateYMD(endDate, 1);
					}
				}
			}
			// 等级有效期结束日
			c.setLevelEndDate(DateUtil.suffixDate(levelEndDate, 1));
			// 未降级
			if (DroolsConstants.CAMPAIGN_TYPE9999.equals(c.getRuleType()) 
					&& !c.isMatchRule()) {
				// 是否使用原有效期开始日
				String moreDate = (String) periodValidity.get("moreDate");
				if (!"1".equals(moreDate)) {
					// 等级有效期开始日
					c.setLevelStartDate(oldLevelStartDate);
				}
			} 
		}
	}
	
	/**
	 * 通过会员等级Id取得会员等级信息
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param levelId
	 *          入会等级
	 * @throws Exception 
	 */
	public static Map<String, Object> getLevelInfo(CampBaseDTO c, int levelId) throws Exception {
		// 会员等级信息
		List<Map<String, Object>> levels = c.getMemberLevels();
		Map<String, Object> levelMap = null;
		if (null != levels) {
			for (Map<String, Object> levelInfo : levels) {
				Object levelIdObj = levelInfo.get("levelId");
				if (null != levelIdObj) {
					int memberLevelId = Integer.parseInt(levelIdObj.toString());
					if (memberLevelId == levelId) {
						return levelInfo;
					}
				}
			}
		}
		if (null == levelMap) {
			throw new CherryMQException("No Level Information Exception!");
		}
		return levelMap;
	}
	
	/**
	 * 取得会员默认等级信息
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @throws Exception 
	 */
	public static Map<String, Object> getDefaultLevel(CampBaseDTO c) throws Exception {
		// 会员等级信息
		List<Map<String, Object>> levels = c.getMemberLevels();
		Map<String, Object> levelMap = null;
		if (null != levels) {
			for (Map<String, Object> levelInfo : levels) {
				// 默认等级
				if ("1".equals(String.valueOf(levelInfo.get("defaultFlag")))) {
					return levelInfo;
				}
			}
		}
		if (null == levelMap) {
			throw new CherryMQException("No Default Level Information Exception!");
		}
		return levelMap;
	}
	
	/**
	 * 生成入会日期
	 * 
	 * @param campBaseDTO
	 *            规则执行DTO
	 * @throws Exception 
	 * 
	 */
	public static void createJoinDate (CampBaseDTO campBaseDTO) throws Exception {
		if (null != campBaseDTO) {
			// 入会日期
			String joinDate = campBaseDTO.getJoinDate();
			if (CherryChecker.isNullOrEmpty(joinDate)) {
				// 单据产生日期
				String ticketDate = campBaseDTO.getTicketDate();
				// 取得年月日
				String ticketYMD = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
				// 设置入会日期
				campBaseDTO.setJoinDate(ticketYMD);
			}
		}
	}
}

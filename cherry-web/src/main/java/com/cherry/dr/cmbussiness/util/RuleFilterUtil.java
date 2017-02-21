/*	
 * @(#)RuleFilterUtil.java     1.0 2012/02/13		
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

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.*;
import com.googlecode.jsonplugin.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 过滤器共通验证方法
 * 
 * @author hub
 * @version 1.0 2012.02.13
 */
public class RuleFilterUtil {
	
	protected static final Logger logger = LoggerFactory.getLogger(RuleFilterUtil.class);
	
	/**
	 * 
	 * 执行条件验证
	 * 
	 * 
	 * @param oc
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param filterName
	 *            规则条件名称
	 * @param methodName
	 *            验证方法名称
	 * @return boolean 验证结果：true 满足条件, false 不满足条件
	 * @throws Exception 
	 * 
	 */
	public static boolean doCheck(Object oc, List<RuleFilterDTO> allFilters, String filterName, String methodName) throws Exception {
		try {
			CampBaseDTO c = (CampBaseDTO) oc;
			// 规则条件
			RuleFilterDTO filter = null;
			if (!CherryChecker.isNullOrEmpty(filterName)) {
				RuleFilterDTO curRuleFilter = c.getRuleFilter();
				// 当前匹配的规则条件
				if (null != curRuleFilter && filterName.equals(curRuleFilter.getRuleName())) {
					filter = curRuleFilter;
				} else {
					// 规则条件集合
					if (null != allFilters) {
						for (RuleFilterDTO ruleFilter : allFilters) {
							// 通过规则名查找方法规则对应的条件
							if (filterName.equals(ruleFilter.getRuleName())) {
								filter = ruleFilter;
								c.setRuleFilter(filter);
								break;
							}
						}
					}
				}
			}
			if (null == filter || filter.getParams().isEmpty()) {
				return false;
			}
			Method method = RuleFilterUtil.class.getDeclaredMethod(methodName, CampBaseDTO.class, Map.class);
			return (Boolean) method.invoke(RuleFilterUtil.class, c, filter.getParams());
		} catch (Exception e) {
			logger.error("method name:" + methodName + "error message:" + e.getMessage(),e);
			throw e;
		}
	}
	
	/**
	 * 
	 * 执行规则处理
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param filterName
	 *            规则条件名称
	 * @param methodName
	 *            验证方法名称
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public static Object doThen(CampBaseDTO c, List<RuleFilterDTO> allFilters, String filterName, String methodName) throws Exception {
		// 规则条件
		RuleFilterDTO filter = null;
		if (!CherryChecker.isNullOrEmpty(filterName)) {
			RuleFilterDTO rhsFilter = c.getRhsFilter();
			// 当前匹配的规则条件
			if (null != rhsFilter && filterName.equals(rhsFilter.getRuleName())) {
				filter = rhsFilter;
			} else {
				// 规则条件集合
				if (null != allFilters) {
					for (RuleFilterDTO ruleFilter : allFilters) {
						// 通过规则名查找方法规则对应的条件
						if (filterName.equals(ruleFilter.getRuleName())) {
							filter = ruleFilter;
							c.setRhsFilter(ruleFilter);
							break;
						}
					}
				}
			}
		}
		if (null == filter || filter.getRhsParams().isEmpty()) {
			return null;
		}
		try {
			Method method = RuleFilterUtil.class.getDeclaredMethod(methodName, CampBaseDTO.class, Map.class);
			return method.invoke(RuleFilterUtil.class, c, filter.getRhsParams());
		} catch (Exception e) {
			logger.error("method name:" + methodName + "error message:" + e.getMessage(),e);
			throw e;
		}
	}
	
	/**
	 * 
	 * 获取会员生日的活动开始日
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            参数集合
	 *            
	 * @return String 会员生日的活动开始日
	 * @throws Exception 
	 * @throws Exception 
	 * 
	 */
	public static boolean isFromDate01 (CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 单据日期
		String ticketDateStr = DateUtil.coverTime2YMD(c.getTicketDate(), DateUtil.DATETIME_PATTERN);
		// 会员生日
		String memberBirthday = c.getBirthday();
		// 特殊生日
		String specBirth = (String) params.get("specBirth");
		if ("1".equals(specBirth)) {
			Date date1 = DateUtil.coverString2Date(memberBirthday);
			if (null != date1) {
				Calendar ca1 = Calendar.getInstance();
				ca1.setTime(date1);
				int month = ca1.get(Calendar.MONTH);
				// 生日为2月29日
				if (month == 1 && ca1.get(Calendar.DAY_OF_MONTH) == 29) {
					Date ticketDate = DateUtil.coverString2Date(ticketDateStr);
					if (null != ticketDate) {
						Calendar ca2 = Calendar.getInstance();
						ca2.setTime(ticketDate);
						// 单据年份
						int ticketYear = ca2.get(Calendar.YEAR);
						// 平年
						if (!DateUtil.isLeapYear(ticketYear)) {
							// 生日年份
							int year = ca1.get(Calendar.YEAR);
							// 将2月28日作为生日当天
							memberBirthday = DateUtil.createDate(year, month, 28, DateUtil.DATE_PATTERN);
						}
					}
				}
			}
		}
		// 活动周期
		String birthday = (String) params.get("birthday");
		// 开始日期
		String fromDate = null;
		// 结束日期
		String toDate = null;
		// 生日当日
		if ("0".equals(birthday)) {
			if (DateUtil.isSameMonthDay(memberBirthday, ticketDateStr)) {
				fromDate = DateUtil.suffixDate(ticketDateStr, 0);
			}
			// 生日当月
		} else if ("1".equals(birthday)) {
			// 比较两个日期是否是同月
			boolean flag = DateUtil.isSameMonth(memberBirthday, ticketDateStr);
			if (flag) {
				fromDate = DateUtil.suffixDate(DateUtil.getMonthStartOrEnd(ticketDateStr, 0), 0);
			}
			// 生日当周
		} else if ("2".equals(birthday)) {
			if (null == memberBirthday) {
				return false;
			}
			String tempDate = c.getBirthday();
			Date date1 = DateUtil.coverString2Date(tempDate);
			if (null != date1) {
				Calendar ca1 = Calendar.getInstance();
				ca1.setTime(date1);
				// 生日月
				int month = ca1.get(Calendar.MONTH);
				// 生日天
				int day = ca1.get(Calendar.DAY_OF_MONTH);
				
				Date ticketDate = DateUtil.coverString2Date(ticketDateStr);
				if (null == ticketDate) {
					return false;
				}	
				Calendar ca2 = Calendar.getInstance();
				ca2.setTime(ticketDate);
				// 单据年份
				int ticketYear = ca2.get(Calendar.YEAR);
				
				// 销售时间为平年并且生日为2月29日
				if (!DateUtil.isLeapYear(ticketYear) && month == 1 && day == 29) {
					day = 28;
				}
				tempDate = DateUtil.createDate(ticketYear, month, day, DateUtil.DATE_PATTERN);
				// 前几天
				String beforeDay = (String) params.get("befDay");
				// 后几天
				String afterDay = (String) params.get("aftDay");
				int beforeDays = 0;
				int afterDays = 0;
				if (!CherryChecker.isNullOrEmpty(beforeDay)) {
					beforeDays = Integer.parseInt(beforeDay);
				}
				if (!CherryChecker.isNullOrEmpty(afterDay)) {
					afterDays = Integer.parseInt(afterDay);
				}
				String fromDateTemp = beforeDays == 0 ? tempDate : 
					DateUtil.addDateByDays(DateUtil.DATE_PATTERN, tempDate, -beforeDays);
				toDate = afterDays == 0 ? tempDate : 
					DateUtil.addDateByDays(DateUtil.DATE_PATTERN, tempDate, afterDays);
				if (DateUtil.compareDate(ticketDateStr, fromDateTemp) >= 0 
						&& DateUtil.compareDate(ticketDateStr, toDate) <= 0) {
					// 不包含生日当天
					if (!"1".equals(params.get("theBirth")) && DateUtil.compareDate(ticketDateStr, memberBirthday) == 0) {
						return false;
					}
					fromDate = DateUtil.suffixDate(fromDateTemp, 0);
				} else {
					if (!fromDateTemp.substring(0,4).equals(toDate.substring(0,4))) {
						int oyear = ticketYear;
						if (DateUtil.compareDate(ticketDateStr, fromDateTemp) < 0) {
							oyear--;
						} else {
							oyear++;
						}
						if (!DateUtil.isLeapYear(oyear) && month == 1 && day == 29) {
							day = 28;
						}
						tempDate = DateUtil.createDate(oyear, month, day, DateUtil.DATE_PATTERN);
						fromDateTemp = beforeDays == 0 ? tempDate : 
							DateUtil.addDateByDays(DateUtil.DATE_PATTERN, tempDate, -beforeDays);
						toDate = afterDays == 0 ? tempDate : 
							DateUtil.addDateByDays(DateUtil.DATE_PATTERN, tempDate, afterDays);
						if (DateUtil.compareDate(ticketDateStr, fromDateTemp) >= 0 
								&& DateUtil.compareDate(ticketDateStr, toDate) <= 0) {
							// 不包含生日当天
							if (!"1".equals("1") && DateUtil.compareDate(ticketDateStr, memberBirthday) == 0) {
								return false;
							}
							fromDate = DateUtil.suffixDate(fromDateTemp, 0);
						}
					}
				}
			}
		} else if ("3".equals(birthday)) {
			// 月
			String month = (String) params.get("spMonth");
			// 日
			String day = (String) params.get("spDay");
			int m = 0;
			int d = 0;
			if (!CherryChecker.isNullOrEmpty(month)) {
				m = Integer.parseInt(month);
			}
			if (!CherryChecker.isNullOrEmpty(day)) {
				d = Integer.parseInt(day);
			}
			if (!DateUtil.isSameMonthDay(memberBirthday, m, d)) {
				return false;
			}
			fromDate = (String) c.getExtArgs().get("RFDATE");
		}
		if (null == fromDate) {
			return false;
		}
		// 活动开始日期
		c.setRuleFromDate(fromDate);
		return true;
	}
	
	/**
	 * 
	 * 获取特定日期的活动开始日
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            参数集合
	 *            
	 * @return String 特定日期的活动开始日
	 * @throws Exception 
	 * 
	 */
	public static boolean isFromDate02 (CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 特定时间 
		String firstBillDate = (String) params.get("firstBillDate");
		// 单据日期
		String ticketDateStr = DateUtil.coverTime2YMD(c.getTicketDate(), DateUtil.DATETIME_PATTERN);
		Date ticketDate = DateUtil.coverString2Date(ticketDateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(ticketDate);
		// 月份
		int month = cal.get(Calendar.MONTH) + 1;
		// 日期
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// 开始日期
		String fromDate = null;
		// 当日首单
		if ("0".equals(firstBillDate)) {
			fromDate = DateUtil.suffixDate(ticketDateStr, 0);
			// 当月首单
		} else if ("1".equals(firstBillDate)) {
			fromDate = DateUtil.suffixDate(DateUtil.getMonthStartOrEnd(ticketDateStr, 0), 0);
			// 指定月
		} else if ("2".equals(firstBillDate)) {
			// 指定的月份
			String billMonth = (String) params.get("billMonth");
			if (String.valueOf(month).equals(billMonth)) {
				fromDate = DateUtil.suffixDate(DateUtil.getMonthStartOrEnd(ticketDateStr, 0), 0);
			}
			// 指定日期
		} else if ("3".equals(firstBillDate)) {
			// 指定的月份
			String billStartMonth = (String) params.get("billStartMonth");
			// 指定的日期
			String billStartDay = (String) params.get("billStartDay");
			if (String.valueOf(month).equals(billStartMonth) && String.valueOf(day).equals(billStartDay)) {
				fromDate = DateUtil.suffixDate(ticketDateStr, 0);
			}
			// 指定时间段
		} else if ("4".equals(firstBillDate)) {
			// 指定的开始日
			String billStartTime = (String) params.get("billStartTime");
			// 指定的结束日
			String billEndTime = (String) params.get("billEndTime");
			// 在开始日和结束日之间
			if (!CherryChecker.isNullOrEmpty(billStartTime) && 
					DateUtil.compareDate(ticketDateStr, billStartTime) >= 0 &&
					!CherryChecker.isNullOrEmpty(billEndTime) && 
					DateUtil.compareDate(ticketDateStr, billEndTime) <= 0) {
				fromDate = DateUtil.suffixDate(billStartTime, 0);
			}
			// 微信绑定时间
		}  else if ("6".equals(firstBillDate)) {
			String channelCode = c.getChannelCode();
			if (null == channelCode || !"wechat".equalsIgnoreCase(channelCode.trim())) {
				String wechatBindTime = c.getWechatBindTime();
				String bindDayLimit = (String) params.get("bindDayLimit");
				if (!CherryChecker.isNullOrEmpty(wechatBindTime) && !CherryChecker.isNullOrEmpty(bindDayLimit)) {
					int bindDayLimitInt = Integer.parseInt(bindDayLimit);
					String sdate = DateUtil.coverTime2YMD(wechatBindTime, DateUtil.DATETIME_PATTERN);
					String etime = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, sdate, bindDayLimitInt) + " 23:59:59";
					boolean isBindDay = "1".equals(params.get("bindDay"));
					if (isBindDay) {
						fromDate = wechatBindTime;
					} else {
						sdate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, sdate, 1);
						fromDate = sdate + " 00:00:00";
					}
					String ticketTime = c.getTicketDate();
					int index = ticketTime.indexOf(".");
					if (index > 0) {
						ticketTime = ticketTime.substring(0, index);
					}
					if (DateUtil.compDateTime(ticketTime, fromDate) < 0 || DateUtil.compDateTime(ticketTime, etime) > 0) {
						fromDate = null;
					}
				}
			}
		}
//		else if ("7".equals(firstBillDate)) { // 销售时间范围
//			// 销售开始时间
//			String saleStartTime = (String) params.get("saleStartTime");
//			// 销售结束时间
//			String saleEndTime = (String) params.get("saleEndTime");
//			// 在开始日和结束日之间
//			if (!CherryChecker.isNullOrEmpty(saleStartTime) &&
//					DateUtil.compareDate(ticketDateStr, saleStartTime) >= 0 &&
//					!CherryChecker.isNullOrEmpty(saleEndTime) &&
//					DateUtil.compareDate(ticketDateStr, saleEndTime) <= 0) {
//				fromDate = DateUtil.suffixDate(saleStartTime, 0);
//			}
//		}
		if (null == fromDate) {
			return false;
		}
		// 活动开始日期
		c.setRuleFromDate(fromDate);
		return true;
	}
	
	/**
	 * 
	 * 验证会员日
	 * 
	 * @param c
	 *            验证对象
	 *            
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public static boolean checkUpLevel(CampBaseDTO c, String fromLevel, String toLevel) throws Exception {
		// 升级后等级
		if (!CherryChecker.isNullOrEmpty(toLevel)) {
			int toLevelId = Integer.parseInt(toLevel);
			boolean toFlag = false;
			if (c.getCurLevelId() == toLevelId) {
				toFlag = true;
			}
			boolean fromFlag = true;
			// 升级前等级
			if (!CherryChecker.isNullOrEmpty(fromLevel)) {
				int fromLevelId = Integer.parseInt(fromLevel);
				if (-1 != fromLevelId && c.getPrevLevel() != fromLevelId) {
					fromFlag = false;
				}
			}
			return toFlag && fromFlag;
		}
		return false;
	}
	
	/**
	 * 
	 * 验证普通购买
	 * 
	 * 
	 * @param params
	 *            参数集合
	 *            
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public static boolean checkNormalBuy(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType())) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
			
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount <= 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * 验证会员日
	 * 
	 * 
	 * @param params
	 *            参数集合
	 *            
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public static boolean checkMemberDay(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType())) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount <= 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
			}
			// 活动周期
			String dayFlag = (String) params.get("dayFlag");
			// 单据日期
			String ticketDateStr = DateUtil.coverTime2YMD(c.getTicketDate(), DateUtil.DATETIME_PATTERN);
			Date ticketDate = DateUtil.coverString2Date(ticketDateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(ticketDate);
			// 单据年份
			int year = cal.get(Calendar.YEAR);
			// 开始日
			String baseDate = DateUtil.createDate(year, 0, 1, DateUtil.DATE_PATTERN);
			// 活动周期:A
			if ("0".equals(dayFlag)) {
				// 周期：月
				String monthText = (String) params.get("monthText");
				// 周期：天
				String dayText = (String) params.get("dayText");
				if (CherryChecker.isNullOrEmpty(monthText) || CherryChecker.isNullOrEmpty(dayText)) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00013);
					return false;
				}
				int month = Integer.parseInt(monthText);
				int day = Integer.parseInt(dayText);
				while (DateUtil.compareDate(baseDate, ticketDateStr) <= 0) {
					if (DateUtil.monthEquals(baseDate, ticketDateStr)) {
						// 改变日期的天数(如果指定日超过了该月的范围，将取该月最后一天)
						String date1 = DateUtil.changeDateByDay(baseDate, day, DateUtil.DATE_PATTERN);
						// 会员日
						if (DateUtil.compareDate(ticketDateStr, date1) == 0) {
							return true;
						} 
						RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00013);
						return false;
					}
					baseDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, baseDate, month);
				}
				// 活动周期:E
			} else if ("1".equals(dayFlag)) {
				// 周期：月
				String monthText = (String) params.get("monthEveText");
				// 第几个星期
				String dayNum = (String) params.get("dayNum");
				// 星期几
				String dayWeek = (String) params.get("dayWeek");
				if (CherryChecker.isNullOrEmpty(monthText) || CherryChecker.isNullOrEmpty(dayNum) 
						|| CherryChecker.isNullOrEmpty(dayWeek)) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00013);
					return false;
				}
				int month = Integer.parseInt(monthText);
				int weekNum = Integer.parseInt(dayNum);
				int weekDay = Integer.parseInt(dayWeek);
				while (DateUtil.compareDate(baseDate, ticketDateStr) <= 0) {
					if (DateUtil.monthEquals(baseDate, ticketDateStr)) {
						// 是否为指定的第几个星期几
						if (DateUtil.isSameWeekDay(ticketDateStr, weekNum, weekDay)) {
							return true;
						}
						RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00013);
						return false;
					}
					baseDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, baseDate, month);
				}
			}
		}
		RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00013);
		return false;
	}
	
	/**
	 * 
	 * 验证购买产品(是否包含指定产品)
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证条件
	 * @return boolean 验证结果
	 * 
	 */
	public static boolean checkProducts(CampBaseDTO c, Map<String, Object> params) {
		// 产品列表
		List<Map<String, Object>> filterProducts = (List<Map<String, Object>>) params.get("productList");
		if (null != filterProducts) {
			if (null != c.getBuyInfo()) {
				List<Map<String, Object>> prtList = (List<Map<String, Object>>) c.getBuyInfo().get("saleDetailList");
				if (null != prtList) {
					for (Map<String, Object> product : filterProducts) {
						// 产品Id
						String filterProId = (String) product.get("proId");
						for (Map<String, Object> prt : prtList) {
							// 产品ID
							String productId = String.valueOf(prt.get("prtVendorId"));
							if (null != filterProId && filterProId.equals(productId)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 *
	 * 验证购买产品(是否包含所有指定产品)
	 *
	 *
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证条件
	 * @return boolean 验证结果
	 *
	 */
	public static boolean checkContainAllProducts(CampBaseDTO c, Map<String, Object> params) {
		// 规则设定产品列表
		List<Map<String, Object>> filterProducts = (List<Map<String, Object>>) params.get("productList");
		if (filterProducts != null && c.getBuyInfo() != null && c.getBuyInfo().get("saleDetailList") != null) {
			// 订单中产品列表
			List<Map<String, Object>> prtList = (List<Map<String, Object>>) c.getBuyInfo().get("saleDetailList");

			Set<String> productSet = new HashSet<String>();
			Set<String> prtSet = new HashSet<String>();

			for (Map<String, Object> product : filterProducts) {
				productSet.add(ConvertUtil.getString(product.get("proId")));
			}

			for (Map<String, Object> prt : prtList) {
				prtSet.add(ConvertUtil.getString(prt.get("prtVendorId")));
			}
			return prtSet.containsAll(productSet) ;
		}
		return false;
	}
	
	/**
	 * 
	 * 验证会员卡号
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证条件
	 * @return boolean 验证结果
	 * 
	 */
	public static boolean checkMemCard(CampBaseDTO c, Map<String, Object> params) {
		// 卡号开头字符
		String cardStart = (String) params.get("cardStart");
		if (!CherryChecker.isNullOrEmpty(cardStart)) {
			// 会员卡号
			String memCard = c.getMemCode();
			String[] startArr = cardStart.split(",");
			for (String startChar : startArr) {
				String stc = startChar.trim();
				if (!"".equals(stc)) {
					// 不以该字符开头
					if (stc.indexOf("!") == 0) {
						if (stc.length() > 1) {
							stc = stc.substring(1);
							if (!memCard.toUpperCase().startsWith(stc.toUpperCase())) {
								return true;
							}
						}
						// 以该字符开头
					} else if (memCard.toUpperCase().startsWith(stc.toUpperCase())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 验证会员卡号(正则表达式)
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证条件
	 * @return boolean 验证结果
	 * 
	 */
	public static boolean checkCurCard(CampBaseDTO c, Map<String, Object> params) {
		// 正则表达式
		String regex = (String) params.get("cardReg");
		if (!CherryChecker.isNullOrEmpty(regex)) {
			// 会员卡号
			String memCard = c.getMemCode();
			return memCard.matches(regex);
		}
		return false;
	}
	
	/**
	 * 
	 * 验证有效期
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public static boolean checkDateG01(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 活动类型
		String campaignType = (String) params.get("campaignType");
		// 验证等级
		if ("1".equals(campaignType)) {
			if (!"2".equals(c.getExtArgs().get("LCK"))) {
				if (c.getCurLevelId() != 0) {
					return false;
				}
			} else {
				if (String.valueOf(c.getCurLevelId()).equals(params.get("curLevelId"))) {
					return false;
				}
			}
		}
		if ("2".equals(campaignType)) {
			if (!String.valueOf(c.getCurLevelId()).equals(params.get("curLevelId"))) {
				return false;
			}
		}
		// 有效期开始日
		String fromDate = (String) params.get("campaignFromDate");
		// 有效期结束日
		String toDate = (String) params.get("campaignToDate");
		boolean fromFlag = false;
		boolean toFlag = false;
		// 单据产生日期
		String ticketDate = c.getTicketDate();
		// 取得年月日
		String ticketYMD = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
		if (null != ticketYMD) {
			if (!CherryChecker.isNullOrEmpty(fromDate) && 
					DateUtil.compareDate(ticketYMD, fromDate) >= 0) {
				fromFlag = true;
			}
			if (CherryChecker.isNullOrEmpty(toDate) || DateUtil.compareDate(ticketYMD, toDate) <= 0) {
				toFlag = true;
			}
		}
		if (fromFlag && toFlag) {
			String locationType = (String) params.get("locationType");
			if (!CherryChecker.isNullOrEmpty(locationType)
					&& !"0".equals(locationType)) {
				boolean isContain = false;
				String saveJson = (String) params.get("saveJson");
				if (!CherryChecker.isNullOrEmpty(saveJson)) {
					// 取得活动地点list
					List<Object> locationDataList = (List<Object>) JSONUtil
							.deserialize(saveJson);
					if (null != locationDataList && !locationDataList.isEmpty()) {
						// 城市ID
						int counterCityId = c.getCounterCityId();
						// 柜台号
						String counterCode = c.getCounterCode();
						// 渠道ID
						int channelId = c.getChannelId();
						// 开卡柜台
						if ("2".equals(params.get("counterKbn"))) {
							counterCityId = c.getBelCounterCityId();
							counterCode = c.getBelCounterCode();
							channelId = c.getBelChannelId();
						}
						if ("2".equals(locationType) || "4".equals(locationType) || "5".equals(locationType) || "8".equals(locationType)) {
							for (Object propId : locationDataList) {
								// 柜台号
								String counter = String.valueOf(propId);
								if (null != counter && null != counterCode && counter.trim().equalsIgnoreCase(counterCode.trim())) {
									isContain = true;
									break;
								}
							}
						} else if ("1".equals(locationType)) {
							for (Object propId : locationDataList) {
								if (Integer.parseInt(propId.toString().trim()) == counterCityId) {
									isContain = true;
									break;
								}
							}
							
						} else if ("3".equals(locationType)) {
							for (Object propId : locationDataList) {
								if (Integer.parseInt(propId.toString().trim()) == channelId) {
									isContain = true;
									break;
								}
							}
						}
					}
				}
				return isContain;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 验证有效期
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public static boolean checkDateG15(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");
		// 单据产生日期
		String ticketDate = c.getTicketDate();
		if (null == ticketDate) {
			throw new Exception("No ticket date Exception!");
		}
		int index = ticketDate.indexOf(".");
		if (index > 0) {
			ticketDate = ticketDate.substring(0, index);
		}
		Date ticketTime = DateUtil.coverString2Date(ticketDate, DateUtil.DATETIME_PATTERN);
		if (null == ticketTime) {
			throw new Exception("Ticket date coverString2Date Exception!");
		}
		// 有效期开始日
		String fromDate = (String) params.get("campaignFromDate");
		if (null == fromDate) {
			throw new Exception("No start date Exception!");
		}
		// 有效期结束日
		String toDate = (String) params.get("campaignToDate");
		// 是否精确到秒
		String timeSetting = (String) params.get("timeSetting");
		// 开始时间
		String fromTime = "00:00:00";
		if ("1".equals(timeSetting)) {
			// 开始时
			String startHH = (String) params.get("startHH");
			// 开始分
			String startMM = (String) params.get("startMM");
			// 开始秒
			String startSS = (String) params.get("startSS");
			if (!CherryChecker.isNullOrEmpty(startHH, true) && !CherryChecker.isNullOrEmpty(startMM, true) &&
					!CherryChecker.isNullOrEmpty(startSS, true)) {
				fromTime = startHH + ":" + startMM + ":" + startSS;
			}
		}
		Date startDate = DateUtil.coverString2Date(fromDate);
		fromDate = DateUtil.date2String(startDate, DateUtil.DATE_PATTERN) + " " + fromTime;
		Date fromDateTime = DateUtil.coverString2Date(fromDate, DateUtil.DATETIME_PATTERN);
		if (null == fromDateTime) {
			throw new Exception("Start date coverString2Date Exception!");
		}
		if (ticketTime.compareTo(fromDateTime) < 0) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00015);
			return false;
		}
		if ("PT05".equals(params.get("TemType"))) {
			c.getExtArgs().put("RFDATE", fromDate);
		}
		if ("PT04".equals(params.get("TemType"))) {
			c.getExtArgs().put("RFDATE1", fromDate);
		}
		// 入会日期
		String jnDateTy = (String) params.get("jnDateTy");
		Date toDateTime = null;
		if (!CherryChecker.isNullOrEmpty(toDate)) {
			// 结束时间
			String endTime = "23:59:59";
			if ("1".equals(timeSetting)) {
				// 结束时
				String endHH = (String) params.get("endHH");
				// 结束分
				String endMM = (String) params.get("endMM");
				// 结束秒
				String endSS = (String) params.get("endSS");
				if (!CherryChecker.isNullOrEmpty(endHH, true) && !CherryChecker.isNullOrEmpty(endMM, true) &&
						!CherryChecker.isNullOrEmpty(endSS, true)) {
					endTime = endHH + ":" + endMM + ":" + endSS;
				}
			}
			Date endDate = DateUtil.coverString2Date(toDate);
			toDate = DateUtil.date2String(endDate, DateUtil.DATE_PATTERN) + " " + endTime;
			toDateTime = DateUtil.coverString2Date(toDate, DateUtil.DATETIME_PATTERN);
			if (null == toDateTime) {
				throw new Exception("End date coverString2Date Exception!");
			}
			if (ticketTime.compareTo(toDateTime) > 0) {
				RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00016);
				return false;
			}
			// 入会奖励
//			if ("PT01".equals(params.get("TemType")) && "2".equals(jnDateTy)) {
//				String joinDate = c.getJoinDate();
//				if (CherryChecker.isNullOrEmpty(joinDate, true)) {
//					return false;
//				}
//				Date joinTime = DateUtil.coverString2Date(joinDate + " 00:00:00", DateUtil.DATETIME_PATTERN);
//				if (joinTime.compareTo(fromDateTime) < 0 || joinTime.compareTo(toDateTime) > 0) {
//					return false;
//				}
//			}
		}
		// 会员等级区分
		String levelKbn = (String) params.get("levelKbn");
		if ("1".equals(levelKbn)) {
			// 会员等级List
			List<Map<String, Object>> memberLevelList = (List<Map<String, Object>>) params.get("memberLevelList");
			if (null != memberLevelList) {
				boolean isNotEqual = true;
				for (Map<String, Object> memberLevel : memberLevelList) {
					// 等级符合条件
					if (String.valueOf(c.getCurLevelId()).equals(memberLevel.get("memberLevelId"))) {
						isNotEqual = false;
						break;
					}
				}
				if (isNotEqual) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00009);
					return false;
				}
			}
		}
		// 必须是由老会员推荐入会的新会员
		if ("1".equals(params.get("referKbn")) && 0 == c.getReferrerId()) {
			return false;
		}
		if ("1".equals(params.get("refPTKbn"))) {
			if (null == c.getExtArgs().get("RPNUM") || 
					Integer.parseInt(c.getExtArgs().get("RPNUM").toString()) < 1) {
				if (!c.getExtArgs().containsKey("RPCK")) {
					c.getExtArgs().put("RPCK", "1");
				}
				return false;
			}
		}
		if (null != jnDateTy && !"".equals(jnDateTy)) {
			// 取得年月日
			String ticketDay = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
			// 入会日期
			String joinDate = c.getJoinDate();
			boolean isJoinDate = false;
			if (!CherryChecker.isNullOrEmpty(joinDate, true)) {
				// 当天入会
				if ("0".equals(jnDateTy)) {
					if (0 == DateUtil.compareDate(joinDate, ticketDay)) {
						isJoinDate = true;
					}
					// 当月入会
				} else if ("1".equals(jnDateTy)) {
					if (DateUtil.monthEquals(joinDate, ticketDay)) {
						isJoinDate = true;
					}
				} else if ("2".equals(jnDateTy)) {
					Date joinTime = DateUtil.coverString2Date(joinDate + " 00:00:00", DateUtil.DATETIME_PATTERN);
					if (joinTime.compareTo(fromDateTime) >= 0 && (null == toDateTime || joinTime.compareTo(toDateTime) <= 0)) {
						isJoinDate = true;
					}
				}
			}
			// 不符合入会日期条件
			if (!isJoinDate) {
				RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00028);
				return false;
			}
		}
		// 入会途径
		List<Map<String, Object>> chlCdList = (List<Map<String, Object>>) params.get("chlCdList");
		if (null != chlCdList && !chlCdList.isEmpty()) {
			boolean chlFlag = false;
			String channelCode = c.getChannelCode();
			if (!CherryChecker.isNullOrEmpty(channelCode)) {
				String channel = channelCode.trim();
				String chl = null;
				if ("wechat".equalsIgnoreCase(channel)) {
					chl = "WX";
				}
				for (Map<String, Object> chlInfo : chlCdList) {
					String chlCd = (String) chlInfo.get("chlCd");
					// 符合入会途径条件
					if (channel.equalsIgnoreCase(chlCd) || 
							null != chl && chl.equalsIgnoreCase(chlCd)) {
						chlFlag = true;
						break;
					}
				}
			}
			// 不符合入会途径条件
			if (!chlFlag) {
				RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00030);
				return false;
			}
		}
		// 首单时间
		if ("2".equals(params.get("datecp"))) {
			boolean dateFlag = false;
			if (!CherryChecker.isNullOrEmpty(c.getJoinDate()) 
					&& !CherryChecker.isNullOrEmpty(c.getFirstTicketTime())) {
						// 入会时间
						String joinTime = "00:00:00";
						if (!CherryChecker.isNullOrEmpty(c.getJoinTime())) {
							joinTime = c.getJoinTime().trim();
						}
						// 入会日期(带时分秒)
						String joinDate = c.getJoinDate() + " " + joinTime;
						if (DateUtil.compDateTime(c.getFirstTicketTime(), joinDate) > 0) {
							dateFlag = true;
						}
					}
			if (!dateFlag) {
				RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00031);
				return false;
			}
		}
		// 会员积分
		if ("2".equals(params.get("mPoint"))) {
			double minpt = 0;
			double maxpt = 0;
			if (!CherryChecker.isNullOrEmpty(params.get("minpt"))) {
				minpt = Double.parseDouble(String.valueOf(params.get("minpt")).trim());
			}
			if (!CherryChecker.isNullOrEmpty(params.get("maxpt"))) {
				maxpt = Double.parseDouble(String.valueOf(params.get("maxpt")).trim());
			}
			double curPoint = 0;
			if (c.getPointInfo() != null) {
				curPoint = c.getPointInfo().getCurTotalPoint();
			}
			if (minpt != 0 && curPoint < minpt) {
				return false;
			}
			if (maxpt != 0 && curPoint > maxpt) {
				return false;
			}
		}
		// 会员生肖
		String zodiac = (String) params.get("zodiac");
		// 会员生日
		String birth = c.getBirthday();
		if (!CherryChecker.isNullOrEmpty(zodiac)) {
			// 没有会员生日信息
			if (CherryChecker.isNullOrEmpty(birth) 
					|| !CherryChecker.checkDate(birth)) {
				return false;
			}
			String yearStr = birth.substring(0,4);
			// 生日年
			int year = Integer.parseInt(yearStr);
			if (year < 1900) {
				return false;
			}
			int idx = (year-1900) % 12;
			int zoc = Integer.parseInt(zodiac);
			// 生肖不符合
			if (zoc != idx) {
				return false;
			}
		}
		// 地点区分
		String locationType = (String) params.get("locationType");
		// 地点列表
		List<Map<String, Object>> nodesList = (List<Map<String, Object>>) params.get("nodesList");
		// 非开卡柜台消费时
		if ("1".equals(params.get("ctKbn"))) {
			if (c.getCounterCode() != null && c.getBelCounterCode() != null &&
					c.getCounterCode().trim().equalsIgnoreCase(c.getBelCounterCode().trim())) {
				return false;
			}
		}
		boolean isContain = false;
		// 全部柜台
		if ("0".equals(locationType)) {
			isContain = true;
			// 指定区域
		} else  {
			// 柜台类别
			String counterKbn = (String) params.get("counterKbn");
			// 城市ID
			int counterCityId = c.getCounterCityId();
			// 柜台号
			String counterCode = c.getCounterCode();
			// 渠道ID
			int channelId = c.getChannelId();
			// 开卡柜台
			if ("2".equals(counterKbn)) {
				counterCityId = c.getBelCounterCityId();
				counterCode = c.getBelCounterCode();
				channelId = c.getBelChannelId();
			}
			if ("1".equals(locationType)) {
				for (Map<String, Object> nodeInfo : nodesList) {
					// 城市ID
					int cityId = 0;
					Object cityIdObj = nodeInfo.get("city");
					if (!CherryChecker.isNullOrEmpty(cityIdObj, true)) {
						cityId = Integer.parseInt(cityIdObj.toString().trim());
					}
					if (cityId == counterCityId) {
						isContain = true;
						break;
					}
				}
				// 指定柜台
			} else if ("2".equals(locationType) || "4".equals(locationType) || "5".equals(locationType)) {
				for (Map<String, Object> nodeInfo : nodesList) {
					if (nodeInfo.containsKey("counter")) {
						// 柜台号
						String counter = (String) nodeInfo.get("counter");
						if (null != counter && null != counterCode && counter.trim().equalsIgnoreCase(counterCode.trim())) {
							isContain = true;
							break;
						}
					} else if ("2".equals(locationType) && nodeInfo.containsKey("city")) {
						// 城市ID
						int cityId = 0;
						Object cityIdObj = nodeInfo.get("city");
						if (!CherryChecker.isNullOrEmpty(cityIdObj, true)) {
							cityId = Integer.parseInt(cityIdObj.toString().trim());
						}
						if (cityId == counterCityId) {
							isContain = true;
							break;
						}
					} else if ("4".equals(locationType) && nodeInfo.containsKey("channel")) {
						// 渠道ID
						int channel = 0;
						Object channelIdObj = nodeInfo.get("channel");
						if (!CherryChecker.isNullOrEmpty(channelIdObj, true)) {
							channel = Integer.parseInt(channelIdObj.toString().trim());
						}
						if (channel == channelId) {
							isContain = true;
							break;
						}
					}
				}
				// 指定渠道
			} else if ("3".equals(locationType)) {
				for (Map<String, Object> nodeInfo : nodesList) {
					// 渠道ID
					int channel = 0;
					Object channelIdObj = nodeInfo.get("channel");
					if (!CherryChecker.isNullOrEmpty(channelIdObj, true)) {
						channel = Integer.parseInt(channelIdObj.toString().trim());
					}
					if (channel == channelId) {
						isContain = true;
						break;
					}
				}
			}
		}
		if (!isContain) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00017);
		}
		return isContain;
	}
	
	/**
	 * 
	 * 计算升级所需金额(单次购买)
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param minAmount
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public static boolean calcUpAmount(CampBaseDTO c, double minAmount) throws Exception {
		if (minAmount > c.getAmount()) {
			// 记录升级所需金额
			recordUpAmount(c, minAmount);
		}
		return false;
	}
	
	/**
	 * 
	 * 记录升级所需金额
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param upAmount
	 *            升级所需金额
	 * @throws Exception 
	 * 
	 */
	public static void recordUpAmount(CampBaseDTO c, double upAmount) throws Exception {
		Object upLelAmount = c.getExtArgs().get("UPLELAMOUNT");
		if (null != upLelAmount) {
			// 上一次保存的金额
			double preAmount = Double.parseDouble(upLelAmount.toString());
			// 比较本次计算的升级金额是否大于上一次计算金额，保留金额小的
			if (preAmount <= upAmount) {
				return;
			}
		}
		c.getExtArgs().put("UPLELAMOUNT", upAmount);
	}
	/**
	 * 
	 * 验证特定产品
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param params
	 *            验证参数
	 * @return boolean 验证结果
	 * @throws Exception 
	 * 
	 */
	public static boolean checkSpecProduct(CampBaseDTO c, Map<String, Object> params) throws Exception {
		// 规则ID
		Object ruleId = params.get("CAMPAIGNLOGID");	
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(c.getTradeType()) && 
				!"2".equals(c.getExtArgs().get("SRPTKBN"))) {
			RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00003);
			return false;
		}
		// 积分 DTO
		PointDTO pointInfo = c.getPointInfo();
		if (null != pointInfo) {
			// 会员积分变化主 DTO
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null != pointChange) {
				double amount = pointChange.getAmount();
				if (amount < 0) {
					RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00004);
					return false;
				}
			}
			// 会员积分变化明细List
			List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
			List<PointChangeDetailDTO> newChangeDetailList = new ArrayList<PointChangeDetailDTO>();
			// 赠送产品
			List<Map<String, Object>> productList = (List<Map<String, Object>>) params.get("productList");
			// 产品区分
			String prtKbn = (String) params.get("product");
			if (null != changeDetailList) {
				if (null != productList && !productList.isEmpty()) {
					// 商品导入
					if ("5".equals(prtKbn)) {
						for (PointChangeDetailDTO pointChangeDetail : changeDetailList) {
							// 产品ID
							int productId = pointChangeDetail.getPrmPrtVendorId();
							String saleType = pointChangeDetail.getSaleType();
							if (CherryChecker.isNullOrEmpty(saleType)) {
								continue;
							}
							// 产品标识
							boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(saleType);
							for (Map<String, Object> productInfo : productList) {
								String st = (String) productInfo.get("saleType");
								if (null == st) {
									continue;
								}
								// 产品
								boolean isn = "N".equalsIgnoreCase(st.trim());
								if (isPrt && isn || !isPrt && !isn) {
									// 产品ID
									int proId = Integer.parseInt(productInfo.get("proId").toString());
									if (productId == proId) {
										PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
										ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
										newChangeDetailList.add(newChangeDetail);
										break;
									}
								}
							}
						}
					} else {
						for (PointChangeDetailDTO pointChangeDetail : changeDetailList) {
							// 产品ID
							int productId = pointChangeDetail.getPrmPrtVendorId();
							String saleType = pointChangeDetail.getSaleType();
							if (CherryChecker.isNullOrEmpty(saleType)) {
								continue;
							}
							// 产品标识
							boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(saleType);
							// 规则条件是否是促销礼品
							boolean isPrmRule = "4".equals(prtKbn);
							if (isPrt && isPrmRule ||
									!isPrt && !isPrmRule) {
								continue;
							}
							// 产品分类信息
							List<Map<String, Object>> prtCateList = pointChangeDetail.getPrtCateList();
							for (Map<String, Object> productInfo : productList) {
								if (isPrt) {
									// 特定产品
									if ("1".equals(prtKbn)) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
											ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
											newChangeDetailList.add(newChangeDetail);
											break;
										}
										// 产品分类
									} else if ("2".equals(prtKbn) || "3".equals(prtKbn)){
										if (null == prtCateList) {
											break;
										}
										// 产品分类ID
										int cateId = Integer.parseInt(productInfo.get("cateId").toString());
										boolean cateFlag = false;
										for (Map<String, Object> prtCateInfo : prtCateList) {
											// 产品分类ID
											int prtCateId = 0;
											Object prtCateIdObj = prtCateInfo.get("prtCateId");
											if (null != prtCateIdObj) {
												prtCateId = Integer.parseInt(prtCateIdObj.toString());
											}
											if (cateId == prtCateId) {
												cateFlag = true;
												break;
											}
										}
										if (cateFlag) {
											PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
											ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
											newChangeDetailList.add(newChangeDetail);
											break;
										}
									}
								} else {
									// 促销礼品
									if (isPrmRule) {
										// 促销礼品ID
										int prmId = Integer.parseInt(productInfo.get("prmId").toString());
										if (productId == prmId) {
											PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
											ConvertUtil.convertDTO(newChangeDetail, pointChangeDetail, false);
											newChangeDetailList.add(newChangeDetail);
											break;
										}
									} 
								}
							}
						}
					}
				}
			}
			if (!newChangeDetailList.isEmpty()) {
				List<PointChangeDetailDTO> detailList = new ArrayList<PointChangeDetailDTO>();
				// 有关联退货
				if ("1".equals(pointChange.getHasBillSR())) {
					for (PointChangeDetailDTO newChangeDetail : newChangeDetailList) {
						if (!CherryChecker.isNullOrEmpty(newChangeDetail.getBillCodeSR(), true)) {
							continue;
						}
						// 数量
						double qt = newChangeDetail.getQuantity();
						// 产品ID
						int proId = newChangeDetail.getPrmPrtVendorId();
						// 销售类型
						String type = newChangeDetail.getSaleType();
						if (null == type) {
							continue;
						}
						for (PointChangeDetailDTO detailInfo : newChangeDetailList) {
							if (CherryChecker.isNullOrEmpty(detailInfo.getBillCodeSR(), true)) {
								continue;
							}
							if (type.equals(detailInfo.getSaleType()) && 
									proId == detailInfo.getPrmPrtVendorId()) {
								// 退货数量
								double qtz = detailInfo.getQuantity();
								qt = DoubleUtil.add(qt, qtz);
							}
						}
						if (qt > 0) {
							detailList.add(newChangeDetail);
						}
					}
				} else {
					detailList.addAll(newChangeDetailList);
				}
				if (!detailList.isEmpty()) {
					// 商品关系为与
					boolean flag = "1".equals(params.get("proCond"));
					boolean addFlag = false;
					if ("5".equals(prtKbn)) {
						if (flag) {
							addFlag = true;
							for (Map<String, Object> productInfo : productList) {
								String st = (String) productInfo.get("saleType");
								if (null == st) {
									addFlag = false;
									break;
								}
								// 产品
								boolean isn = "N".equalsIgnoreCase(st.trim());
								boolean isMatch = false;
								for (PointChangeDetailDTO newDetail : detailList) {
									// 产品标识
									boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(newDetail.getSaleType());
									int productId = newDetail.getPrmPrtVendorId();
									if (isPrt && isn || !isPrt && !isn) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											isMatch = true;
											break;
										}
									}
								}
								if (!isMatch) {
									addFlag = false;
									break;
								}
							}
						} else {
							for (PointChangeDetailDTO newDetail : detailList) {
								// 产品标识
								boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(newDetail.getSaleType());
								int productId = newDetail.getPrmPrtVendorId();
								for (Map<String, Object> productInfo : productList) {
									String st = (String) productInfo.get("saleType");
									if (null == st) {
										continue;
									}
									// 产品
									boolean isn = "N".equalsIgnoreCase(st.trim());
									if (isPrt && isn || !isPrt && !isn) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											addFlag = true;
											break;
										}
									}
								}
							}
						}
					} else {
						for (Map<String, Object> productInfo : productList) {
							boolean isMatch = false;
							for (PointChangeDetailDTO newDetail : detailList) {
								// 产品标识
								boolean isPrt = DroolsConstants.SALE_TYPE_NORMAL_SALE.equalsIgnoreCase(newDetail.getSaleType());
								int productId = newDetail.getPrmPrtVendorId();
								if (isPrt) {
									// 特定产品
									if ("1".equals(prtKbn)) {
										// 产品ID
										int proId = Integer.parseInt(productInfo.get("proId").toString());
										if (productId == proId) {
											isMatch = true;
											break;
										}
										// 产品分类
									} else if ("2".equals(prtKbn) || "3".equals(prtKbn)){
										// 产品分类信息
										List<Map<String, Object>> prtCateList = newDetail.getPrtCateList();
										if (null != prtCateList) {
											// 产品分类ID
											int cateId = Integer.parseInt(productInfo.get("cateId").toString());
											boolean cateFlag = false;
											for (Map<String, Object> prtCateInfo : prtCateList) {
												// 产品分类ID
												int prtCateId = 0;
												Object prtCateIdObj = prtCateInfo.get("prtCateId");
												if (null != prtCateIdObj) {
													prtCateId = Integer.parseInt(prtCateIdObj.toString());
												}
												if (cateId == prtCateId) {
													cateFlag = true;
													break;
												}
											}
											if (cateFlag) {
												isMatch = true;
												break;
											}
										}
									}
								} else {
									// 促销礼品
									if ("4".equals(prtKbn)) {
										// 促销礼品ID
										int prmId = Integer.parseInt(productInfo.get("prmId").toString());
										if (productId == prmId) {
											isMatch = true;
											break;
										}
									} 
								}
							}
							// 商品关系与
							if (flag) {
								if (isMatch) {
									addFlag = true;
								} else {
									addFlag = false;
									break;
								}
								// 商品关系或
							} else {
								if (isMatch) {
									addFlag = true;
									break;
								}
							}
						}
					}
					if (addFlag) {
						if (detailList.size() < newChangeDetailList.size()) {
							newChangeDetailList.removeAll(detailList);
							detailList.addAll(newChangeDetailList);
						}
						String rangeNumStr = (String) params.get("rangeNum");
						double rangeNum = 0;
						if (!CherryChecker.isNullOrEmpty(rangeNumStr)) {
							rangeNum = Double.parseDouble(rangeNumStr);
						}
						if (rangeNum > 0) {
							Map<String, Object> numMap = new HashMap<String, Object>();
							List<PointChangeDetailDTO> noSpecList = new ArrayList<PointChangeDetailDTO>();
							if ("1".equals(pointChange.getHasBillSR())) {
								double rangeNum1 = rangeNum;
								List<PointChangeDetailDTO> srDetailList = new ArrayList<PointChangeDetailDTO>();
								List <PointChangeDetailDTO> srSpecList = new ArrayList<PointChangeDetailDTO>();
								for (int i = 0; i < detailList.size(); i++) {
									PointChangeDetailDTO detailDto = detailList.get(i);
									if (CherryChecker.isNullOrEmpty(detailDto.getBillCodeSR(), true)) {
										continue;
									}
									detailDto.setNoSpecKbn("1");
									srDetailList.add(detailDto);
									detailList.remove(i);
									i--;
								}
								for (int i = 0; i < detailList.size(); i++) {
									PointChangeDetailDTO detailDto = detailList.get(i);
									// 销售类型
									String type = detailDto.getSaleType();
									// 产品ID
									int proId = detailDto.getPrmPrtVendorId();
									// 数量
									double qt = detailDto.getQuantity();
									String key = proId + type;
									if (i > 0) {
										if (numMap.containsKey(key)) {
											rangeNum = Double.parseDouble(numMap.get(key).toString());
											if (rangeNum == 0) {
												double qtSR1 = qt;
												for (int j = 0; j < srSpecList.size(); j++) {
													PointChangeDetailDTO srSpec = srSpecList.get(j);
													// 销售类型
													String typeSR = srSpec.getSaleType();
													// 产品ID
													int proIdSR = srSpec.getPrmPrtVendorId();
													if (type.equals(typeSR) && proId == proIdSR) {
														// 数量
														double qtSR = srSpec.getQuantity();
														if (qtSR1 > 0) {
															qtSR1 = DoubleUtil.add(qtSR1, qtSR);
															if (qtSR1 < 0) {
																boolean isEqual = false;
																for (PointChangeDetailDTO noSpec : noSpecList) {
																	if (srSpec.getSaleDetailId() == noSpec.getSaleDetailId()) {
																		noSpec.resetQuantity(DoubleUtil.add(noSpec.getQuantity(), DoubleUtil.sub(qtSR, qtSR1)));
																		
																		isEqual = true;
																		break;
																	}
																}
																if (!isEqual) {
																	PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
																	ConvertUtil.convertDTO(newChangeDTO, srSpec, false);
																	newChangeDTO.resetQuantity(DoubleUtil.sub(qtSR, qtSR1));
																	newChangeDTO.setNoSpecKbn("1");
																	noSpecList.add(newChangeDTO);
																}
																srSpec.resetQuantity(qtSR1);
																j--;
																continue;
															} else {
																boolean isEqual = false;
																for (PointChangeDetailDTO noSpec : noSpecList) {
																	if (srSpec.getSaleDetailId() == noSpec.getSaleDetailId()) {
																		noSpec.resetQuantity(DoubleUtil.add(noSpec.getQuantity(), qtSR));
																		isEqual = true;
																		break;
																	}
																}
																if (!isEqual) {
																	PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
																	ConvertUtil.convertDTO(newChangeDTO, srSpec, false);
																	newChangeDTO.resetQuantity(qtSR);
																	newChangeDTO.setNoSpecKbn("1");
																	noSpecList.add(newChangeDTO);
																}
																srSpecList.remove(j);
																j--;
															}
														}
													}
												}
												detailDto.setNoSpecKbn("1");
												noSpecList.add(detailDto);
												detailList.remove(i);
												i--;
												continue;
											}
										} else {
											rangeNum = rangeNum1;
										}
									}
									if (qt > rangeNum) {
										double qt1 = DoubleUtil.sub(qt, rangeNum);
										double qt2 = 0;
										boolean flg = true;
										for (int j = 0; j < srDetailList.size(); j++) {
											PointChangeDetailDTO srDetailDto = srDetailList.get(j);
											// 销售类型
											String typeSR = srDetailDto.getSaleType();
											// 产品ID
											int proIdSR = srDetailDto.getPrmPrtVendorId();
											// 数量
											double qtSR = srDetailDto.getQuantity();
											if (type.equals(typeSR) && proId == proIdSR) {
												if (flg) {
													qt2 = DoubleUtil.add(qt1, qtSR);
													flg = false;
												} else if (qt2 > 0) {
													qt2 = DoubleUtil.add(qt2, qtSR);
												} else if (qt2 <= 0) {
													srDetailDto.setNoSpecKbn(null);
													srSpecList.add(srDetailDto);
													srDetailList.remove(j);
													j--;
													continue;
												}
												if (qt2 < 0) {
													PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
													ConvertUtil.convertDTO(newChangeDTO, srDetailDto, false);
													newChangeDTO.resetQuantity(qt2);
													newChangeDTO.setNoSpecKbn(null);
													srSpecList.add(newChangeDTO);
													srDetailDto.resetQuantity(DoubleUtil.sub(qtSR, qt2));
													noSpecList.add(srDetailDto);
													continue;
												}
											}
										}
										PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
										ConvertUtil.convertDTO(newChangeDTO, detailDto, false);
										detailDto.resetQuantity(rangeNum);
										newChangeDTO.resetQuantity(qt1);
										noSpecList.add(newChangeDTO);
										rangeNum = 0;
									}  else {
										rangeNum = DoubleUtil.sub(rangeNum, qt);
									}
									numMap.put(key, rangeNum);
								}
								if (!srSpecList.isEmpty()) {
									detailList.addAll(srSpecList);
								}
							} else {
								double rangeNum1 = rangeNum;
								for (int i = 0; i < detailList.size(); i++) {
									PointChangeDetailDTO detailDto = detailList.get(i);
									// 销售类型
									String type = detailDto.getSaleType();
									// 产品ID
									int proId = detailDto.getPrmPrtVendorId();
									// 数量
									double qt = detailDto.getQuantity();
									String key = proId + type;
									if (i > 0) {
										if (numMap.containsKey(key)) {
											rangeNum = Double.parseDouble(numMap.get(key).toString());
											if (rangeNum == 0) {
												detailDto.setNoSpecKbn("1");
												noSpecList.add(detailDto);
												detailList.remove(i);
												i--;
												continue;
											}
										} else {
											rangeNum = rangeNum1;
										}
									}
									if (qt > rangeNum) {
										PointChangeDetailDTO newChangeDTO = new PointChangeDetailDTO();
										ConvertUtil.convertDTO(newChangeDTO, detailDto, false);
										detailDto.resetQuantity(rangeNum);
										newChangeDTO.resetQuantity(DoubleUtil.sub(qt, rangeNum));
										newChangeDTO.setNoSpecKbn("1");
										noSpecList.add(newChangeDTO);
										rangeNum = 0;
									}  else {
										rangeNum = DoubleUtil.sub(rangeNum, qt);
									}
									numMap.put(key, rangeNum);
								}
							}
							if (!noSpecList.isEmpty()) {
//								List<PointChangeDetailDTO> prtAllList = new ArrayList<PointChangeDetailDTO>();
//								prtAllList.addAll(changeDetailList);
//								prtAllList.removeAll(noSpecList);
//								prtAllList.addAll(noSpecList);
								c.getExtArgs().put("NoSpecList", noSpecList);
							}
						}
						c.getExtArgs().put("SpecProductList", detailList);
						return true;
					}
				}
			}
		}
		RuleFilterUtil.Log(c, ruleId, false, DroolsMessageUtil.PDR00014);
		return false;
	}
	
	/**
	 * 获取奖励积分值
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param params
	 *          参数集合
	 * @return double 
	 * 				奖励积分值
	 */
	private static double reCalcuVal(CampBaseDTO c,  Map<String, Object> params) {
		// 奖励值 
		double calcuVal = 0;
		Object calcuValObj = params.get("calcuVal");
		if (!CherryChecker.isNullOrEmpty(calcuValObj)) {
			calcuVal = Double.parseDouble(calcuValObj.toString());
		}
		// 会员积分变化主 DTO
		PointChangeDTO pointChangeDTO = null;
		if (null != c.getPointInfo()) {
			pointChangeDTO = c.getPointInfo().getPointChange();
		}
		if (null == pointChangeDTO) {
			return 0;
		}
		List<Map<String, Object>> mlellist = (List<Map<String, Object>>) params.get("MLELList");
		if (null != mlellist) {
			for (Map<String, Object> mlelInfo : mlellist) {
				if (String.valueOf(c.getCurLevelId()).equals(mlelInfo.get("levelId"))) {
					return Double.parseDouble(mlelInfo.get("ptmlt").toString());
				}
			}
		}
		// 该单金额
		double amount = Math.abs(pointChangeDTO.getPtamount());
		// 按金额分段
		List<Map<String, Object>> segmePointList = (List<Map<String, Object>>) params.get("segmePoints");
		if (null != segmePointList && !segmePointList.isEmpty()) {
			boolean flag = false;
			for (int i = 0; i < segmePointList.size(); i++) {
				Map<String, Object> segmePoint = segmePointList.get(i);
				// 下限区分
				String lowerLimit = (String) segmePoint.get("lowerLimit");
				// 上限区分
				String highLimit = (String) segmePoint.get("highLimit");
				// 金额下限
				Object lowerAmountObj = segmePoint.get("lowerAmount");
				// 金额上限
				Object highAmountObj = segmePoint.get("highAmount");
				if (!CherryChecker.isNullOrEmpty(lowerAmountObj, true) || !CherryChecker.isNullOrEmpty(highAmountObj, true)) {
					if (!CherryChecker.isNullOrEmpty(lowerAmountObj, true)) {
						double lowerAmount = Double.parseDouble(lowerAmountObj.toString());
						// 大于等于
						if ("0".equals(lowerLimit) && amount < lowerAmount) {
							continue;
							// 大于
						} else if ("1".equals(lowerLimit) && amount <= lowerAmount) {
							continue;
						}
					}
					if (!CherryChecker.isNullOrEmpty(highAmountObj, true)) {
						double highAmount = Double.parseDouble(highAmountObj.toString());
						// 小于
						if ("0".equals(highLimit) && amount >= highAmount) {
							continue;
							// 小于等于
						} else if ("1".equals(highLimit) && amount > highAmount) {
							continue;
						}
					}
					// 奖励积分值
					Object rewardPointObj = segmePoint.get("rewardPoint");
					if (!CherryChecker.isNullOrEmpty(rewardPointObj, true)) {
						calcuVal = Double.parseDouble(rewardPointObj.toString());
						flag = true;
						break;
					}
				}
			}
			if (!flag) {
				return -9999;
			}
		}
		return calcuVal;
	}
	/**
	 * 计算积分奖励(结果)
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param params
	 *          参数集合
	 * @return PointChangeDTO 
	 * 				计算结果
	 * @throws Exception 
	 */
	public static PointChangeDTO calcuPoint(CampBaseDTO c,  Map<String, Object> params) throws Exception {
		// 计算区分
		int calcuKbn = 0;
		// 奖励值 
		double calcuVal = reCalcuVal(c, params);
		if (-9999 == calcuVal) {
			return null;
		}
		boolean isReason = params.containsKey("MLELList");
		Object calcuKbnObj = params.get("calcuKbn");
		if (!CherryChecker.isNullOrEmpty(calcuKbnObj)) {
			calcuKbn = Integer.parseInt(calcuKbnObj.toString());
		}
		// 规则属性
		String pointRuleKbn = (String) params.get("POINTRULE_KBN");
		// 积分类型
		String pointType = (String) params.get("POINT_TYPE");
		// 附属方式
		String subRuleKbn = (String) params.get("SUBRULE_KBN");
		// 匹配的规则代号
		String subcampCode = (String) params.get("SUBCAMPCODE");
		// 规则描述ID
		String ruledptId = (String) params.get("RULEDPT_ID");
		// 匹配的规则ID
		Integer subcampId = null;
		Object subcampIdObj = params.get("SUBCAMPID");
		if (null != subcampIdObj) {
			subcampId = Integer.parseInt(subcampIdObj.toString());
		}
		// 会员积分变化主 DTO
		PointChangeDTO pointChangeDTO = null;
		if (null != c.getPointInfo()) {
			pointChangeDTO = c.getPointInfo().getPointChange();
		}
		if (null == pointChangeDTO) {
			return null;
		}
		// 会员积分变化明细List
		List<PointChangeDetailDTO> changeDetailList = pointChangeDTO.getChangeDetailList();
		if (null == changeDetailList || changeDetailList.isEmpty()) {
			return null;
		}
		PointChangeDTO newPointChangeDTO = new PointChangeDTO();
		ConvertUtil.convertDTO(newPointChangeDTO, pointChangeDTO, false);
		boolean calcuFlag = true;
		List<PointChangeDetailDTO> newChangeDetailList = new ArrayList<PointChangeDetailDTO>();
		for(int i = 0; i < changeDetailList.size(); i++) {
			PointChangeDetailDTO changeDetail = changeDetailList.get(i);
			PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
			ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
			// 计算后的积分值
			double point = 0;
			if (1 == calcuKbn) {
				// 计算后的积分值
				point = DoubleUtil.mul(changeDetail.getAmount(), calcuVal);
			} else {
				if (calcuFlag) {
					// 计算后的积分值
					point = calcuVal;
					calcuFlag = false;
					// 赠送范围：整单
					newPointChangeDTO.setRangeKbn(DroolsConstants.RANGEKBN_1);
					newChangeDetail.setDetailFlag("1");
				}
			}
			// 积分类型
			newChangeDetail.setPointType(pointType);
			if (calcuFlag || 0 == i) {
				newChangeDetail.setPoint(point);
				// 附属方式
				newPointChangeDTO.setSubRuleKbn(subRuleKbn);
				// 匹配的规则代号
				newChangeDetail.setSubCampaignCode(subcampCode);
				// 匹配的规则ID
				newChangeDetail.setSubCampaignId(subcampId);
				// 规则描述ID
				newChangeDetail.setRuledptId(ruledptId);
				if (isReason) {
					newChangeDetail.addReason("积分系数: " + calcuVal);
				}
			}
			newChangeDetailList.add(newChangeDetail);
			// 附属规则时
			if (!calcuFlag && DroolsConstants.POINTRULEKBN_2.equals(pointRuleKbn)) {
				break;
			}
		}
		// 整单奖励并且是关联退货的特殊处理
		if (!calcuFlag && c.getAmount() == 0 
				&& "1".equals(newPointChangeDTO.getHasBillSR())) {
			// 赋予整单奖励的明细
			PointChangeDetailDTO pointChange0 = newChangeDetailList.get(0);
			// 对应的退货明细
			PointChangeDetailDTO pointChangeSR = null;
			for (int i = 1; i < changeDetailList.size(); i++) {
				PointChangeDetailDTO changeDetail = changeDetailList.get(i);
				// 退货明细
				if (!CherryChecker.isNullOrEmpty(changeDetail.getBillCodeSR(), true)
						&& pointChange0.getPrmPrtVendorId() == changeDetail.getPrmPrtVendorId()) {
					PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
					ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
					pointChangeSR = newChangeDetail;
					break;
				}
			}
			if (null != pointChangeSR) {
				pointChangeSR.setDetailFlag("1");
				// 积分类型
				pointChangeSR.setPointType(pointChange0.getPointType());
				// 积分值
				double srPoint = DoubleUtil.sub(0, pointChange0.getPoint());
				pointChangeSR.setPoint(srPoint);
				// 匹配的规则代号
				pointChangeSR.setSubCampaignCode(pointChange0.getSubCampaignCode());
				// 匹配的规则ID
				pointChangeSR.setSubCampaignId(pointChange0.getSubCampaignId());
				// 规则描述ID
				pointChangeSR.setRuledptId(pointChange0.getRuledptId());
				// 普通规则
				if (!DroolsConstants.POINTRULEKBN_2.equals(pointRuleKbn)) {
					// 替换掉退货奖励的那条明细
					for (int i = 0; i < newChangeDetailList.size(); i++) {
						PointChangeDetailDTO newChangeDetail = newChangeDetailList.get(i);
						if (pointChangeSR.getSaleDetailId() == newChangeDetail.getSaleDetailId()) {
							newChangeDetailList.remove(i);
							newChangeDetailList.add(i, pointChangeSR);
						}
					}
				} else {
					newChangeDetailList.add(pointChangeSR);
				}
			}
		}
		newPointChangeDTO.setChangeDetailList(newChangeDetailList);
		// 活动开始日期
		String ruleFromDate = c.getRuleFromDate();
		// 活动开始日期
		if (null == ruleFromDate) {
			ruleFromDate = (String) params.get("RULEFROMDATE");
		}
		for (PointChangeDetailDTO pointChangeDetail : newChangeDetailList) {
			Map<String, Object> extParams = new HashMap<String, Object>();
			extParams.put("RULEFROMDATE", ruleFromDate);
			extParams.put("POINTLIMITINFO", params.get("POINTLIMITINFO"));
			// 按实收金额倍数
			if (1 == calcuKbn) {
				extParams.put("calcuTime", calcuVal);
			}
			pointChangeDetail.setExtParams(extParams);
		}
		//newPointChangeDTO.setRuleFromDate(ruleFromDate);
		c.setRuleFromDate(null);
		// 执行默认规则区分
		newPointChangeDTO.setDefaultExecKbn((String) params.get("defaultExecSel"));
		return newPointChangeDTO;
	}
	
	/**
	 * 计算促销活动积分奖励(结果)
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param params
	 *          参数集合
	 * @return PointChangeDTO 
	 * 				计算结果
	 * @throws Exception 
	 */
	public static PointChangeDTO calcuActPoint(CampBaseDTO c,  Map<String, Object> params) throws Exception {
		// 会员积分变化明细List
		List<PointChangeDetailDTO> changeDetailList = (List<PointChangeDetailDTO>) c.getExtArgs().get("ActMatchList");
		if (null == changeDetailList) {
			return null;
		}
		c.getExtArgs().remove("ActMatchList");
		// 计算区分
		int calcuKbn = 0;
		// 奖励值 
		double calcuVal = reCalcuVal(c, params);
		if (-9999 == calcuVal) {
			return null;
		}
		Object calcuKbnObj = params.get("calcuKbn");
		if (!CherryChecker.isNullOrEmpty(calcuKbnObj)) {
			calcuKbn = Integer.parseInt(calcuKbnObj.toString());
		}
		// 规则属性
		String pointRuleKbn = (String) params.get("POINTRULE_KBN");
		// 积分类型
		String pointType = (String) params.get("POINT_TYPE");
		// 附属方式
		String subRuleKbn = (String) params.get("SUBRULE_KBN");
		// 匹配的规则代号
		String subcampCode = (String) params.get("SUBCAMPCODE");
		// 规则描述ID
		String ruledptId = (String) params.get("RULEDPT_ID");
		// 匹配的规则ID
		Integer subcampId = null;
		Object subcampIdObj = params.get("SUBCAMPID");
		if (null != subcampIdObj) {
			subcampId = Integer.parseInt(subcampIdObj.toString());
		}
		// 会员积分变化主 DTO
		PointChangeDTO pointChangeDTO = null;
		if (null != c.getPointInfo()) {
			pointChangeDTO = c.getPointInfo().getPointChange();
		}
		if (null == pointChangeDTO) {
			return null;
		}
		PointChangeDTO newPointChangeDTO = new PointChangeDTO();
		ConvertUtil.convertDTO(newPointChangeDTO, pointChangeDTO, false);
		boolean calcuFlag = true;
		List<PointChangeDetailDTO> newChangeDetailList = new ArrayList<PointChangeDetailDTO>();
		if (1 == calcuKbn) {
			changeDetailList = pointChangeDTO.getChangeDetailList();
			if (null == changeDetailList || changeDetailList.isEmpty()) {
				return null;
			}
		}
		for(int i = 0; i < changeDetailList.size(); i++) {
			PointChangeDetailDTO changeDetail = changeDetailList.get(i);
			PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
			ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
			// 计算后的积分值
			double point = 0;
			if (1 == calcuKbn) {
				// 计算后的积分值
				point = DoubleUtil.mul(changeDetail.getAmount(), calcuVal);
			} else {
				if (calcuFlag) {
					// 计算后的积分值
					point = calcuVal;
					calcuFlag = false;
					// 赠送范围：整单
					newPointChangeDTO.setRangeKbn(DroolsConstants.RANGEKBN_1);
					newChangeDetail.setDetailFlag("1");
				}
			}
			// 积分类型
			newChangeDetail.setPointType(pointType);
			if (calcuFlag || 0 == i) {
				newChangeDetail.setPoint(point);
				// 附属方式
				newPointChangeDTO.setSubRuleKbn(subRuleKbn);
				// 匹配的规则代号
				newChangeDetail.setSubCampaignCode(subcampCode);
				// 匹配的规则ID
				newChangeDetail.setSubCampaignId(subcampId);
				// 规则描述ID
				newChangeDetail.setRuledptId(ruledptId);
			}
			newChangeDetailList.add(newChangeDetail);
			// 附属规则时
			if (!calcuFlag && DroolsConstants.POINTRULEKBN_2.equals(pointRuleKbn)) {
				break;
			}
		}
		newPointChangeDTO.setChangeDetailList(newChangeDetailList);
		// 活动开始日期
		String ruleFromDate = c.getRuleFromDate();
		// 活动开始日期
		if (null == ruleFromDate) {
			ruleFromDate = (String) params.get("RULEFROMDATE");
		}
		for (PointChangeDetailDTO pointChangeDetail : newChangeDetailList) {
			Map<String, Object> extParams = new HashMap<String, Object>();
			extParams.put("RULEFROMDATE", ruleFromDate);
			extParams.put("POINTLIMITINFO", params.get("POINTLIMITINFO"));
			// 按实收金额倍数
			if (1 == calcuKbn) {
				extParams.put("calcuTime", calcuVal);
			}
			pointChangeDetail.setExtParams(extParams);
		}
		//newPointChangeDTO.setRuleFromDate(ruleFromDate);
		c.setRuleFromDate(null);
		// 执行默认规则区分
		newPointChangeDTO.setDefaultExecKbn((String) params.get("defaultExecSel"));
		return newPointChangeDTO;
	}
	
	/**
	 * 计算积分奖励(特定产品)
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param params
	 *          参数集合
	 * @return PointChangeDTO 
	 * 				计算结果
	 * @throws Exception 
	 */
	public static PointChangeDTO calcuPrtPoint(CampBaseDTO c,  Map<String, Object> params) throws Exception {
		// 特定产品List
		List<PointChangeDetailDTO> specProductList = (List<PointChangeDetailDTO>) c.getExtArgs().get("SpecProductList");
		if (null == specProductList) {
			return null;
		}
		c.getExtArgs().remove("SpecProductList");
		// 计算区分
		int calcuKbn = 0;
		// 奖励值 
		double calcuVal = reCalcuVal(c, params);
		if (-9999 == calcuVal) {
			return null;
		}
		Object calcuKbnObj = params.get("calcuKbn");
		if (!CherryChecker.isNullOrEmpty(calcuKbnObj)) {
			calcuKbn = Integer.parseInt(calcuKbnObj.toString());
		}
		// 规则属性
		String pointRuleKbn = (String) params.get("POINTRULE_KBN");
		// 积分类型
		String pointType = (String) params.get("POINT_TYPE");
		// 附属方式
		String subRuleKbn = (String) params.get("SUBRULE_KBN");
		// 匹配的规则代号
		String subcampCode = (String) params.get("SUBCAMPCODE");
		// 匹配的规则ID
		Integer subcampId = null;
		Object subcampIdObj = params.get("SUBCAMPID");
		if (null != subcampIdObj) {
			subcampId = Integer.parseInt(subcampIdObj.toString());
		}
		// 规则描述ID
		String ruledptId = (String) params.get("RULEDPT_ID");
		// 奖励范围
		String rangeFlag = (String) params.get("RANGE_FLAG");
		// 会员积分变化主 DTO
		PointChangeDTO pointChangeDTO = null;
		if (null != c.getPointInfo()) {
			pointChangeDTO = c.getPointInfo().getPointChange();
		}
		if (null == pointChangeDTO) {
			return null;
		}
		// 会员积分变化明细List
		List<PointChangeDetailDTO> changeDetailList = pointChangeDTO.getChangeDetailList();
		if (null == changeDetailList || changeDetailList.isEmpty()) {
			return null;
		}
		PointChangeDTO newPointChangeDTO = new PointChangeDTO();
		ConvertUtil.convertDTO(newPointChangeDTO, pointChangeDTO, false);
		boolean calcuFlag = true;
		List<PointChangeDetailDTO> newChangeDetailList = new ArrayList<PointChangeDetailDTO>();
		for(int i = 0; i < specProductList.size(); i++) {
			PointChangeDetailDTO changeDetail = specProductList.get(i);
			PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
			ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
			// 计算后的积分值
			double point = 0;
			if (1 == calcuKbn) {
				// 计算后的积分值
				point = DoubleUtil.mul(changeDetail.getAmount(), calcuVal);
			} else {
				if (calcuFlag) {
					if (DroolsConstants.RANGE_FLAG_1.equals(rangeFlag)) {
						if (DroolsConstants.TRADETYPE_SR.equals(pointChangeDTO.getTradeType())) {
							return null;
						}
						// 计算后的积分值
						point = calcuVal;
						calcuFlag = false;
						// 赠送范围：整单
						newPointChangeDTO.setRangeKbn(DroolsConstants.RANGEKBN_1);
						newChangeDetail.setDetailFlag("1");
					} else {
						// 计算后的积分值
						point = DoubleUtil.mul(changeDetail.getQuantity(), calcuVal);
					}
				}
			}
			// 购买积分
			newChangeDetail.setPointType(pointType);
			if (calcuFlag || 0 == i) {
				newChangeDetail.setPoint(point);
				// 附属方式
				newPointChangeDTO.setSubRuleKbn(subRuleKbn);
				// 匹配的规则代号
				newChangeDetail.setSubCampaignCode(subcampCode);
				// 匹配的规则ID
				newChangeDetail.setSubCampaignId(subcampId);
				// 规则描述ID
				newChangeDetail.setRuledptId(ruledptId);
				// 是否是特殊产品
				newChangeDetail.setPrtKbn("1");
			}
			newChangeDetailList.add(newChangeDetail);
			// 附属规则时
			if (!calcuFlag && DroolsConstants.POINTRULEKBN_2.equals(pointRuleKbn)) {
				break;
			}
		}
		// 整单奖励并且是关联退货的特殊处理
//		if (!calcuFlag
//				&& "1".equals(newPointChangeDTO.getHasBillSR())) {
//			// 赋予整单奖励的明细
//			PointChangeDetailDTO pointChange0 = newChangeDetailList.get(0);
//			// 对应的退货明细
//			PointChangeDetailDTO pointChangeSR = null;
//			for (int i = 1; i < specProductList.size(); i++) {
//				PointChangeDetailDTO changeDetail = specProductList.get(i);
//				// 退货明细
//				if (!CherryChecker.isNullOrEmpty(changeDetail.getBillCodeSR(), true)
//						&& pointChange0.getPrmPrtVendorId() == changeDetail.getPrmPrtVendorId()) {
//					PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
//					ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
//					pointChangeSR = newChangeDetail;
//					break;
//				}
//			}
//			if (null != pointChangeSR) {
//				pointChangeSR.setDetailFlag("1");
//				// 积分类型
//				pointChangeSR.setPointType(pointChange0.getPointType());
//				// 积分值
//				double srPoint = DoubleUtil.sub(0, pointChange0.getPoint());
//				pointChangeSR.setPoint(srPoint);
//				// 匹配的规则代号
//				pointChangeSR.setSubCampaignCode(pointChange0.getSubCampaignCode());
//				// 匹配的规则ID
//				pointChangeSR.setSubCampaignId(pointChange0.getSubCampaignId());
//				// 规则描述ID
//				pointChangeSR.setRuledptId(pointChange0.getRuledptId());
//				// 普通规则
//				if (!DroolsConstants.POINTRULEKBN_2.equals(pointRuleKbn)) {
//					// 替换掉退货奖励的那条明细
//					for (int i = 0; i < newChangeDetailList.size(); i++) {
//						PointChangeDetailDTO newChangeDetail = newChangeDetailList.get(i);
//						if (pointChangeSR.getSaleDetailId() == newChangeDetail.getSaleDetailId()) {
//							newChangeDetailList.remove(i);
//							newChangeDetailList.add(i, pointChangeSR);
//						}
//					}
//				} else {
//					newChangeDetailList.add(pointChangeSR);
//				}
//			}
//		}
		List<PointChangeDetailDTO> noSpecList = (List<PointChangeDetailDTO>) c.getExtArgs().get("NoSpecList");
		if (null == noSpecList) {
			// 普通规则
			if (DroolsConstants.POINTRULEKBN_1.equals(pointRuleKbn) || 
					(DroolsConstants.POINTRULEKBN_2.equals(pointRuleKbn) && 1 == calcuKbn 
							&& DroolsConstants.RANGE_FLAG_1.equals(rangeFlag))) {
				// 非特定商品List
				List<PointChangeDetailDTO> noSpecPrtList = new ArrayList<PointChangeDetailDTO>();
				// 会员积分变化明细List
				for (PointChangeDetailDTO changeDetail : changeDetailList) {
					// 销售明细ID
					int saleDetailId = changeDetail.getSaleDetailId();
					boolean isNotSpec = true;
					if (!"1".equals(changeDetail.getNoSpecKbn())) {
						// 特定产品List
						for (PointChangeDetailDTO specProduct : specProductList) {
							int specSaleId = specProduct.getSaleDetailId();
							if (saleDetailId == specSaleId) {
								isNotSpec = false;
								break;
							}
						}
					}
					if (isNotSpec) {
						PointChangeDetailDTO newChangeDetail = new PointChangeDetailDTO();
						ConvertUtil.convertDTO(newChangeDetail, changeDetail, false);
						// 计算后的积分值
						double point = 0;
						// 整单按金额倍数积分
						if (1 == calcuKbn && DroolsConstants.RANGE_FLAG_1.equals(rangeFlag)) {
							// 计算后的积分值
							point = DoubleUtil.mul(changeDetail.getAmount(), calcuVal);
						}
						// 购买积分
						newChangeDetail.setPointType(pointType);
						if (1 == calcuKbn && DroolsConstants.RANGE_FLAG_1.equals(rangeFlag)) {
							newChangeDetail.setPoint(point);
							// 附属方式
							newPointChangeDTO.setSubRuleKbn(subRuleKbn);
							// 匹配的规则代号
							newChangeDetail.setSubCampaignCode(subcampCode);
							// 匹配的规则ID
							newChangeDetail.setSubCampaignId(subcampId);
							// 规则描述ID
							newChangeDetail.setRuledptId(ruledptId);
							// 是否是特殊产品
							//newChangeDetail.setPrtKbn("1");
						}
						noSpecPrtList.add(newChangeDetail);
					}
				}
				if (!noSpecPrtList.isEmpty()) {
					newChangeDetailList.addAll(noSpecPrtList);
				}
			}
		} else {
			newPointChangeDTO.setNoSpecList(noSpecList);
			c.getExtArgs().remove("NoSpecList");
		}
		newPointChangeDTO.setChangeDetailList(newChangeDetailList);
		// 活动开始日期
		String ruleFromDate = c.getRuleFromDate();
		// 活动开始日期
		if (null == ruleFromDate) {
			ruleFromDate = (String) params.get("RULEFROMDATE");
		}
		for (PointChangeDetailDTO pointChangeDetail : newChangeDetailList) {
			Map<String, Object> extParams = new HashMap<String, Object>();
			extParams.put("RULEFROMDATE", ruleFromDate);
			extParams.put("POINTLIMITINFO", params.get("POINTLIMITINFO"));
			// 按实收金额倍数
			if (1 == calcuKbn) {
				extParams.put("calcuTime", calcuVal);
			}
			pointChangeDetail.setExtParams(extParams);
		}
		//newPointChangeDTO.setRuleFromDate(ruleFromDate);
		c.setRuleFromDate(null);
		// 执行默认规则区分
		newPointChangeDTO.setDefaultExecKbn((String) params.get("defaultExecSel"));
		return newPointChangeDTO;
	}
	
	/**
	 * 小数位处理
	 * 
	 * @param point
	 *          积分值
	 * @param kbn
	 *          小数位处理区分
	 *          
	 */
	public static double roundSet(double point, String kbn){
		// 单品四舍五入
		if ("1".equals(kbn)) {
			point = DoubleUtil.round(point, 0);
			// 只舍不入
		} else if ("2".equals(kbn)) {
			point = DoubleUtil.roundDown(point, 0);
			// 只入不舍
		} else if ("3".equals(kbn)) {
			point = DoubleUtil.roundCeiling(point, 0);
		}
		return point;
	}
	
	/**
	 * 会员入会处理
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param levelId
	 *          入会等级
	 * @return CampBaseDTO  
	 * 			入会结果     
	 * @throws Exception 
	 */
	public static CampBaseDTO joinLevel(CampBaseDTO c, int levelId) throws Exception{
		// 通过会员等级Id取得会员等级信息
		Map<String, Object> levelInfo = CampRuleUtil.getLevelInfo(c, levelId);
		CampBaseDTO cbd = new CampBaseDTO();
		// 当前会员等级
		cbd.setCurLevelId(levelId);
		// 会员等级名称
		cbd.setCurLevelName((String) levelInfo.get("levelName"));
		Object gradeObj = levelInfo.get("grade");
		if (null != gradeObj) {
			// 等级级别
			cbd.setGrade(Integer.parseInt(gradeObj.toString()));
		}
		if ("2".equals(c.getExtArgs().get("LCK"))) {
			// 变更前会员等级
			cbd.setPrevLevel(c.getCurLevelId());
		}
		return cbd;
	}
	
	/**
	 * 会员升级处理
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param levelId
	 *          升级等级
	 * @return CampBaseDTO  
	 * 			升级结果      
	 * @throws Exception 
	 */
	public static CampBaseDTO upLevel(CampBaseDTO c, int levelId) throws Exception{
		// 通过会员等级Id取得会员等级信息
		Map<String, Object> levelInfo = CampRuleUtil.getLevelInfo(c, levelId);
		CampBaseDTO cbd = new CampBaseDTO();
		// 升级前会员等级
		cbd.setPrevLevel(c.getCurLevelId());
		// 当前会员等级
		cbd.setCurLevelId(levelId);
		// 会员等级名称
		cbd.setCurLevelName((String) levelInfo.get("levelName"));
		// 升降级区分:升级
		cbd.setChangeType(DroolsConstants.UPKBN_1);
		Object gradeObj = levelInfo.get("grade");
		if (null != gradeObj) {
			// 等级级别
			cbd.setGrade(Integer.parseInt(gradeObj.toString()));
		}
		return cbd;
	}
	
	/**
	 * 会员降级处理
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param levelId
	 *          降级等级
	 * @param campaignId
	 *          活动ID
	 * @param subCampCode
	 *          子活动代号 
	 * @param ruleDptId
	 *          规则描述ID
	 * @throws Exception 
	 */
	public static void downLevel(CampBaseDTO c, int levelId, String campaignId, String subCampCode, String ruleDptId) throws Exception{
		// 通过会员等级Id取得会员等级信息
		Map<String, Object> levelInfo = CampRuleUtil.getLevelInfo(c, levelId);
		// 等级级别
		int grade = 0;
		Object gradeObj = levelInfo.get("grade");
		if (null != gradeObj) {
			// 等级级别
			grade = Integer.parseInt(gradeObj.toString());
		}
		// 等级失效
		if (DroolsConstants.UPKBN_3.equals(c.getChangeType())) {
			return;
			// 降级
		} else if (DroolsConstants.UPKBN_2.equals(c.getChangeType())) {
			if (grade >= c.getGrade()) {
				return;
			}
			if (0 != c.getGrade()) {
				String dwg = (String) c.getExtArgs().get("DWG");
				if (null != dwg && !dwg.isEmpty()) {
					String[] dwgArr = dwg.split("_");
					//if (String.valueOf(c.getGrade()).equals(dwgArr[0])) {
					c.removeRuleId(dwgArr[1]);
					c.removeSubCode(dwgArr[2]);
					//}
				}
			}
		}
		// 升级前会员等级
		c.setPrevLevel(c.getCurLevelId());
		// 当前会员等级
		c.setCurLevelId(levelId);
		// 当前会员等级
		c.setMemberLevel(levelId);
		// 升级前会员等级
		c.setUpgradeFromLevel(c.getCurLevelId());
		// 会员等级名称
		c.setCurLevelName((String) levelInfo.get("levelName"));
		// 升降级区分:降级
		c.setChangeType(DroolsConstants.UPKBN_2);
		// 等级升降级区分
		c.setLevelChangeType(DroolsConstants.UPKBN_2);
		// 会员等级调整日
		c.setLevelAdjustDay(c.getTicketDate());
		// 入会或者升级首单号
		c.setFirstBillId(null);
		// 等级级别
		c.setGrade(grade);
		// 规则描述ID
		c.addRuleId(ruleDptId);
		// 子活动代码组
		c.addSubCampCodes(subCampCode);
		c.getExtArgs().put("DWG", grade + "_" + ruleDptId + "_" + subCampCode);
		// 会员等级状态:实际等级
		c.setLevelStatus(CherryConstants.LEVELSTATUS_2);
		// 设定等级有效期
		//CampRuleUtil.execLevelValidity(c);
	}
	
	/**
	 * 会员等级失效处理
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param campaignId
	 *          活动ID
	 * @param subCampCode
	 *          子活动代号 
	 * @param ruleDptId
	 *          规则描述ID
	 * @throws Exception 
	 */
	public static void invalidLevel(CampBaseDTO c, String campaignId, String subCampCode, String ruleDptId) throws Exception{
		// 升降级区分:失效
		c.setChangeType(DroolsConstants.UPKBN_3);
		// 规则描述ID
		c.addRuleId(ruleDptId);
		// 子活动代码组
		c.addSubCampCodes(subCampCode);
		// 会员等级状态:实际等级
		c.setLevelStatus(CherryConstants.LEVELSTATUS_2);
	}
	
	
	
	/**
	 * 记录处理结果
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param ruleDTO
	 *          规则执行的结果
	 * @param campaignId
	 *          活动ID
	 * @param subCampCode
	 *          子活动代号 
	 * @param ruleDptId
	 *          规则描述ID
	 */
	public static void recordResult(CampBaseDTO c, Object ruleDTO, String campaignId, String subCampCode, String ruleDptId){
		if (null != c && null != ruleDTO) {
			RuleResultDTO rd = new RuleResultDTO();
			// 活动ID
			rd.setCampaignId(campaignId);
			// 规则执行的结果
			rd.setRuleDTO(ruleDTO);
			// 子活动代号 
			rd.setSubCampCode(subCampCode);
			// 规则描述ID
			rd.setRuleDptId(ruleDptId);
			List<RuleResultDTO> ruleResultList = c.getRuleResultList();
			boolean addFlag = true;
			if (ruleDTO instanceof String && null != ruleResultList && !ruleResultList.isEmpty()) {
				for (RuleResultDTO ruleResult : ruleResultList) {
					if (campaignId.equals(ruleResult.getCampaignId()) 
							&& ruleDTO.equals(ruleResult.getRuleDTO())) {
						addFlag = false;
						break;
					}
				}
			} 
			if (addFlag) {
				ruleResultList.add(rd);
			}
		}
	}
	
	/**
	 * 优先级处理
	 * 
	 * @param c
	 * 			规则执行DTO
	 * @param campType
	 *          会员活动类型
	 * @throws Exception 
	 */
	public static void execPriority(CampBaseDTO c, String campType) throws Exception{
		// 入会
		if (DroolsConstants.CAMPAIGN_TYPE_1.equals(campType)) {
			List<RuleResultDTO> ruleResultList = c.getRuleResultList();
			if (null != ruleResultList && !ruleResultList.isEmpty()) {
				// 执行结果
				RuleResultDTO rrd = ruleResultList.get(0);
				CampBaseDTO cbd = (CampBaseDTO) rrd.getRuleDTO();
				if (!"2".equals(c.getExtArgs().get("LCK"))) {
					// 会员等级
					c.setCurLevelId(cbd.getCurLevelId());
					// 当前会员等级
					c.setMemberLevel(cbd.getCurLevelId());
					// 升级前会员等级
					c.setUpgradeFromLevel(null);
					// 当前等级名称
					c.setCurLevelName(cbd.getCurLevelName());
					// 会员等级级别
					c.setGrade(cbd.getGrade());
					// 入会区分
					c.setJoinKbn(DroolsConstants.JOINKBN_1);
					// 规则描述ID
					c.addRuleId(rrd.getRuleDptId());
					// 子活动代码组
					c.addSubCampCodes(rrd.getSubCampCode());
					// 会员等级状态:实际等级
					c.setLevelStatus(CherryConstants.LEVELSTATUS_2);
					// 升级区分
					c.setChangeType(DroolsConstants.UPKBN_1);
					// 会员等级调整日
					c.setLevelAdjustDay(c.getTicketDate());
					// 入会或者升级首单号
					c.setFirstBillId(c.getBillId());
					// 等级升降级区分
					c.setLevelChangeType(DroolsConstants.UPKBN_1);
					// 入会时会员等级
					//if (0 == c.getGrantMemberLevel()) {
						c.setGrantMemberLevel(c.getCurLevelId());
					//}
					// 生成入会日期
				//	CampRuleUtil.createJoinDate(c);
					// 设定等级有效期
					CampRuleUtil.execLevelValidity(c);
				} else {
					// 变更前会员等级
					c.setOldLevelId(cbd.getPrevLevel());
					// 会员等级
					c.setCurLevelId(cbd.getCurLevelId());
					// 规则描述ID
					c.addRuleId(rrd.getRuleDptId());
				}
			} 
			// 升级
		} else if (DroolsConstants.CAMPAIGN_TYPE_2.equals(campType)) {
			List<RuleResultDTO> ruleResultList = c.getRuleResultList();
			if (null != ruleResultList && !ruleResultList.isEmpty()) {
				// 执行结果
				RuleResultDTO rrd = null;
				// 会员等级级别
				int grade = -1;
				for (RuleResultDTO ruleResult : ruleResultList) {
					CampBaseDTO cbd = (CampBaseDTO) ruleResult.getRuleDTO();
					// 选择级别高的等级
					if (cbd.getGrade() > grade) {
						rrd = ruleResult;
						grade = cbd.getGrade();
					}
				}
				if (null != rrd) {
					CampBaseDTO cbd = (CampBaseDTO) rrd.getRuleDTO();
					// 升级前会员等级
					c.setPrevLevel(cbd.getPrevLevel());
					// 会员等级
					c.setCurLevelId(cbd.getCurLevelId());
					// 当前会员等级
					c.setMemberLevel(cbd.getCurLevelId());
					// 升级前会员等级
					c.setUpgradeFromLevel(cbd.getPrevLevel());
					// 当前等级名称
					c.setCurLevelName(cbd.getCurLevelName());
					// 会员等级级别
					c.setGrade(cbd.getGrade());
					// 规则描述ID
					c.addRuleId(rrd.getRuleDptId());
					// 子活动代码组
					c.addSubCampCodes(rrd.getSubCampCode());
					// 会员等级状态:实际等级
					c.setLevelStatus(CherryConstants.LEVELSTATUS_2);
					// 升级区分
					c.setChangeType(cbd.getChangeType());
					// 会员等级调整日
					c.setLevelAdjustDay(c.getTicketDate());
					// 入会或者升级首单号
					c.setFirstBillId(c.getBillId());
					// 等级升降级区分
					c.setLevelChangeType(cbd.getChangeType());
				}
			}
		}
	}
	
	/**
	 * 
	 * 通过等级ID查询对应的等级有效期信息
	 * 
	 * 
	 * @param levelId
	 *          	等级ID
	 * @param levelList
	 *          	等级列表       
	 * @return Map 
	 * 				等级有效期信息
	 * 
	 * 
	 */
	public static Map<String, Object> findLevelValidInfo(int levelId, List<Map<String, Object>> levelList){
		if (0 != levelId && null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级
				int memLevel = Integer.parseInt(levelInfo.get("levelId").toString());
				if (levelId == memLevel) {
					// 该等级有效期信息
					return (Map<String, Object>) levelInfo.get("periodValidity");
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 比较两个等级的级别
	 * 
	 * 
	 * @param levelId1
	 *          	等级ID1
	 * @param levelId2
	 *          	等级ID2
	 * @param levelList
	 *          	等级列表       
	 * @return int 
	 * 				-1: 前者低于后者 0: 两者相等 1:前者高于后者
	 * 
	 * 
	 */
	public static int compareLevel(int levelId1, int levelId2, List<Map<String, Object>> levelList){
		if (levelId1 == levelId2) {
			return 0;
		}
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级
				int memLevel = Integer.parseInt(levelInfo.get("levelId").toString());
				if (levelId1 == memLevel) {
					return -1;
				} else if (levelId2 == memLevel) {
					return 1;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * 通过等级ID查询对应的等级代码
	 * 
	 * 
	 * @param levelId
	 *          	等级ID
	 * @param levelList
	 *          	等级列表       
	 * @return String 
	 * 				等级代码
	 * 
	 * 
	 */
	public static String findLevelCode(int levelId, List<Map<String, Object>> levelList){
		if (0 != levelId && null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级
				int memLevel = Integer.parseInt(levelInfo.get("levelId").toString());
				if (levelId == memLevel) {
					// 等级代码
					return (String) levelInfo.get("levelCode");
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 通过等级代号查询对应的等级ID
	 * 
	 * 
	 * @param levelCode
	 *          	等级编码
	 * @param levelList
	 *          	等级列表       
	 * @return String 
	 * 				等级代号
	 * 
	 * 
	 */
	public static int findLevelId(String levelCode, List<Map<String, Object>> levelList){
		if (!CherryChecker.isNullOrEmpty(levelCode, true) && null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级代号
				String memLevelCode = (String) levelInfo.get("levelCode");
				if (levelCode.equals(memLevelCode)) {
					// 会员等级ID
					return Integer.parseInt(levelInfo.get("levelId").toString());
				}
			}
		}
		return 0;
	}
	
	/**
	 * 验证是否是正式等级
	 * 
	 * @param levelId
	 * 			会员等级ID
	 * @param levelList
	 * 			会员等级列表
	 * @return boolean
	 * 			true: 正式    false: 非正式
	 */
	public static boolean isFormalLevel(int levelId, List<Map<String, Object>> levelList) {
		// 该等级有效期信息
		Map<String, Object> validityMap = findLevelValidInfo(levelId, levelList);
		if (null != validityMap) {
			// 等级区分
			String levelKbn = (String) validityMap.get("levelKbn");
			// 非正式等级
			if ("1".equals(levelKbn)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证是否是最高等级
	 * 
	 * @param levelId
	 * 			会员等级ID
	 * @param levelList
	 * 			会员等级列表
	 * @return boolean
	 * 			true: 是    false: 否
	 */
	public static boolean isHighestLevel(int levelId, List<Map<String, Object>> levelList) {
		if (null != levelList) {
			// 最高等级
			int highestLevelId = -1;
			// 最高级别
			int highestGrade = -1;
			for (int i = 0; i < levelList.size(); i++) {
				Map<String, Object> levelInfo = levelList.get(i);
				// 会员等级
				int memLevelId = Integer.parseInt(levelInfo.get("levelId").toString());
				// 级别
				int grade = Integer.parseInt(levelInfo.get("grade").toString());
				if (grade > highestGrade) {
					highestLevelId = memLevelId;
					highestGrade = grade;
				}
			}
			if (levelId == highestLevelId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证有效期开始时间是否是成为该等级的时间
	 * 
	 * @param levelId
	 * 			会员等级ID
	 * @param levelList
	 * 			会员等级列表
	 * @return boolean
	 * 			true: 是    false: 否
	 */
	public static boolean isCurLevelTime(int levelId, List<Map<String, Object>> levelList) {
		// 该等级有效期信息
		Map<String, Object> validityMap = findLevelValidInfo(levelId, levelList);
		if (null != validityMap) {
			// 有效期开始时间区分
			String startTimeKbn = (String) validityMap.get("startTimeKbn");
			// 成为正式等级的时间
			if ("1".equals(startTimeKbn)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * 是否打印日志
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @return boolean 验证结果：true 打印, false 不打印
	 * 
	 * 
	 */
	public static boolean isRuleLog(CampBaseDTO c){
		if ("1".equals(c.getExtArgs().get("RULELOG"))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 返回规则名称
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param ruleId
	 *            规则ID
	 * @return String
	 * 			  规则名称
	 * 
	 */
	public static String findRuleName(CampBaseDTO c, Object ruleId){
		// 规则名称列表
		List<Map<String, Object>> ruleNameList = (List<Map<String, Object>>) c.getExtArgs().get("RuleNameList");
		if (null != ruleNameList && null != ruleId) {
			String ruleIdStr = String.valueOf(ruleId);
			for (Map<String, Object> ruleNameMap : ruleNameList) {
				String campId = String.valueOf(ruleNameMap.get("campaignId"));
				if (campId.equals(ruleIdStr)) {
					return (String) ruleNameMap.get("campaignName");
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 返回规则名称(通过子规则ID)
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param subId
	 *            子规则ID
	 * @return String
	 * 			  规则名称
	 * 
	 */
	public static String findNameBySubId(CampBaseDTO c, Object subId){
		// 规则名称列表
		List<Map<String, Object>> ruleNameList = (List<Map<String, Object>>) c.getExtArgs().get("RuleNameList");
		if (null != ruleNameList && null != subId) {
			String ruleIdStr = String.valueOf(subId);
			for (Map<String, Object> ruleNameMap : ruleNameList) {
				String campRuleId = String.valueOf(ruleNameMap.get("campaignRuleId"));
				if (campRuleId.equals(ruleIdStr)) {
					return (String) ruleNameMap.get("campaignName");
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 是否打印日志
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param ruleId
	 *            规则ID
	 * @param result
	 *            匹配结果
	 * @param reason
	 *            原因
	 * 
	 * 
	 */
	public static void Log(CampBaseDTO c, Object ruleId, boolean result, String reason, String... params){
		if (!isRuleLog(c)) {
			return;
		}
		// 组织代号
		String orgCode = c.getOrgCode();
		// 品牌代号
		String brandCode = c.getBrandCode();
		// 规则名称
		String ruleName = findRuleName(c, ruleId);
		// 匹配结果
		String ret = null;
		if (result) {
			ret = DroolsMessageUtil.PDR00002;
		} else {
			ret = DroolsMessageUtil.PDR00001;
		}
		// 执行结果日志
		String infoMsg = null;
		if (null == params || params.length == 0) {
			// 执行结果日志
			infoMsg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.EDR00014, new String[] { ruleName, ret, reason, orgCode, brandCode, c.getBillId(), c.getMemCode() });
		} else {
			String msgCode = params[0];
			if ("EDR00015".equals(msgCode)) {
				// 执行结果日志
				infoMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00015, new String[] { ruleName, ret, reason, orgCode, brandCode, c.getBillId(), c.getMemCode() });
			} else if ("EDR00016".equals(msgCode)) {
				// 执行结果日志
				infoMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00016, new String[] { reason, ruleName, orgCode, brandCode, c.getBillId(), c.getMemCode() });
			}  else if ("EDR00017".equals(msgCode)) {
				// 执行结果日志
				infoMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00017, new String[] { ruleName, reason, orgCode, brandCode, c.getBillId(), c.getMemCode() });
			} else if ("EDR00018".equals(msgCode)) {
				// 执行结果日志
				infoMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00018, new String[] { ruleName, reason, orgCode, brandCode, c.getBillId(), c.getMemCode() });
			} else if ("EDR00019".equals(msgCode)) {
				// 执行结果日志
				infoMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00019, new String[] { ruleName, orgCode, brandCode, c.getBillId(), c.getMemCode() });
			} else if ("EDR00020".equals(msgCode) && params.length > 1) {
				// 执行结果日志
				infoMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00020, new String[] { params[1], reason, orgCode, brandCode, c.getBillId(), c.getMemCode() });
			} else if ("EDR00021".equals(msgCode) && params.length > 1) {
				// 执行结果日志
				infoMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00021, new String[] { params[1], reason, orgCode, brandCode, c.getBillId(), c.getMemCode() });
			}
		}
		// 规则名称列表
		List<Map<String, Object>> ruleLogList = (List<Map<String, Object>>) c.getExtArgs().get("RuleLogList");
		Map<String, Object> ruleLogMap = new HashMap<String, Object>();
		ruleLogMap.put("ruleId", infoMsg);
		ruleLogMap.put("msg", infoMsg);
		if (null == ruleLogList) {
			ruleLogList = new ArrayList<Map<String, Object>>();
			c.getExtArgs().put("RuleLogList", ruleLogList);
		}
		ruleLogList.add(ruleLogMap);
	}
}

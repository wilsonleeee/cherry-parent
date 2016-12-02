/*	
 * @(#)BINOLCM33_BL.java     1.0 2012/01/07		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.service.BINOLCM33_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;

/**
 * 会员检索画面共通BL
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLCM33_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLCM33_BL.class.getName());
	
	/** 会员检索画面共通Service **/
	@Resource
	private BINOLCM33_Service binOLCM33_Service;
	
	/** 访问WebService共通BL **/
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 取得会员搜索条件记录数
	 * 
	 * @param map 查询条件
	 * @return 会员搜索条件记录数
	 */
	public int getSearchRequestCount(Map<String, Object> map) {
		// 取得会员搜索条件记录数
		return binOLCM33_Service.getSearchRequestCount(map);
	}
	
	/**
	 * 取得会员搜索条件List
	 * 
	 * @param map 查询条件
	 * @return 会员搜索条件List
	 */
	public List<Map<String, Object>> getSearchRequestList(Map<String, Object> map) {
		// 取得会员搜索条件List
		return binOLCM33_Service.getSearchRequestList(map);
	}
	
	/**
	 * 添加会员搜索条件
	 * 
	 * @param map 添加内容
	 */
	public void addSearchRequest(Map<String, Object> map) {
		// 添加会员搜索条件
		binOLCM33_Service.addSearchRequest(map);
	}
	
	/**
	 * 删除会员搜索条件
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int deleteSearchRequest(Map<String, Object> map) {
		// 删除会员搜索条件
		return binOLCM33_Service.deleteSearchRequest(map);
	}
	
	/**
	 * 会员搜索条件设定
	 * 
	 * @param map 会员搜索条件
	 */
	public void setCondition(Map<String, Object> map) {
		
		// 取得 日结状态确定的业务日期
		String sysDate = binOLCM33_Service.getBusDate(map);
		String referDate = (String)map.get(CherryConstants.REFERDATE);
		if(referDate != null && !"".equals(referDate)) {
			sysDate = referDate;
		}
		
		// 年龄转化为生日年处理
		String ageStart = (String)map.get("ageStart");
		if(ageStart != null && !"".equals(ageStart)) {
			map.put("birthYearEnd", CherryUtil.getYearByAge(ageStart, sysDate));
		}
		String ageEnd = (String)map.get("ageEnd");
		if(ageEnd != null && !"".equals(ageEnd)) {
			map.put("birthYearStart", CherryUtil.getYearByAge(ageEnd, sysDate));
		}
		// 生日条件设置
		String birthDayMode = (String)map.get("birthDayMode");
		if(birthDayMode != null && !"".equals(birthDayMode)) {
			if("0".equals(birthDayMode)) {// 生日模式为当月生日
				String month = sysDate.substring(5, 7);
				String birthDayDateStart = (String)map.get("birthDayDateStart");
				String birthDayDateEnd = (String)map.get("birthDayDateEnd");
				if(birthDayDateStart != null && !"".equals(birthDayDateStart)) {
					if(birthDayDateStart.length() == 1) {
						birthDayDateStart = "0" + birthDayDateStart;
					}
				} else {
					birthDayDateStart = "01";
				}
				if(birthDayDateEnd != null && !"".equals(birthDayDateEnd)) {
					if(birthDayDateEnd.length() == 1) {
						birthDayDateEnd = "0" + birthDayDateEnd;
					}
				} else {
					birthDayDateEnd = DateUtil.getLastDateByMonth(month);
				}
				map.put("birthDayStart", month + birthDayDateStart);
				map.put("birthDayEnd", month + birthDayDateEnd);
				// 是否去除当月入会会员Flag
				String joinDateFlag = (String)map.get("joinDateFlag");
				if(joinDateFlag != null && "1".equals(joinDateFlag)) {
					map.put("curMonthJoinDate", sysDate.substring(0, 7)+"-01");
				}
			} else if("1".equals(birthDayMode)) {// 生日模式为当天生日
				String month = sysDate.substring(5, 7);
				String day = sysDate.substring(8, 10);
				map.put("birthDayStart", month + day);
				map.put("birthDayEnd", month + day);
				// 是否去除当天入会会员Flag
				String curDayJoinDateFlag = (String)map.get("curDayJoinDateFlag");
				if(curDayJoinDateFlag != null && "1".equals(curDayJoinDateFlag)) {
					map.put("curDayJoinDate", sysDate.substring(0,10));
				}
			} else if("2".equals(birthDayMode)) {// 生日模式相对生日
				String birthDayRange = (String)map.get("birthDayRange");
				if(birthDayRange != null && !"".equals(birthDayRange)) {
					String birthDayPath = (String)map.get("birthDayPath");
					String birthDayUnit = (String)map.get("birthDayUnit");
					if("1".equals(birthDayPath)) {// 生日前的场合
						if("1".equals(birthDayUnit)) {// 生日单位为月的场合
							String preBirthDay = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(birthDayRange));
							String month = preBirthDay.substring(5, 7);
							map.put("birthDayStart", month + "01");
							map.put("birthDayEnd", month + DateUtil.getLastDateByMonth(month));
						} else {// 生日单位为天的场合
							String preBirthDay = DateUtil.addDateByDays("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(birthDayRange));
							String month = preBirthDay.substring(5, 7);
							String day = preBirthDay.substring(8, 10);
							map.put("birthDayStart", month + day);
							map.put("birthDayEnd", month + day);
						}
					} else {// 生日后的场合
						if("1".equals(birthDayUnit)) {// 生日单位为月的场合
							String nextBirthDay = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate.substring(0,10), Integer.parseInt(birthDayRange));
							String month = nextBirthDay.substring(5, 7);
							map.put("birthDayStart", month + "01");
							map.put("birthDayEnd", month + DateUtil.getLastDateByMonth(month));
						} else {// 生日单位为天的场合
							String nextBirthDay = DateUtil.addDateByDays("yyyy-MM-dd", sysDate.substring(0,10), Integer.parseInt(birthDayRange));
							String month = nextBirthDay.substring(5, 7);
							String day = nextBirthDay.substring(8, 10);
							map.put("birthDayStart", month + day);
							map.put("birthDayEnd", month + day);
						}
					}
				}
			} else if("3".equals(birthDayMode)) {// 生日模式为生日范围
				String birthDayMonthRangeStart = (String)map.get("birthDayMonthRangeStart");
				String birthDayMonthRangeEnd = (String)map.get("birthDayMonthRangeEnd");
				String birthDayDateRangeStart = (String)map.get("birthDayDateRangeStart");
				String birthDayDateRangeEnd = (String)map.get("birthDayDateRangeEnd");
				if(birthDayMonthRangeStart != null && !"".equals(birthDayMonthRangeStart)) {
					if(birthDayMonthRangeStart.length() == 1) {
						birthDayMonthRangeStart = "0" + birthDayMonthRangeStart;
					}
					if(birthDayDateRangeStart != null && !"".equals(birthDayDateRangeStart)) {
						if(birthDayDateRangeStart.length() == 1) {
							birthDayDateRangeStart = "0" + birthDayDateRangeStart;
						}
					} else {
						birthDayDateRangeStart = "01";
					}
					map.put("birthDayStart", birthDayMonthRangeStart+birthDayDateRangeStart);
				}
				if(birthDayMonthRangeEnd != null && !"".equals(birthDayMonthRangeEnd)) {
					if(birthDayMonthRangeEnd.length() == 1) {
						birthDayMonthRangeEnd = "0" + birthDayMonthRangeEnd;
					}
					if(birthDayDateRangeEnd != null && !"".equals(birthDayDateRangeEnd)) {
						if(birthDayDateRangeEnd.length() == 1) {
							birthDayDateRangeEnd = "0" + birthDayDateRangeEnd;
						}
					} else {
						birthDayDateRangeEnd = "01";
					}
					map.put("birthDayEnd", birthDayMonthRangeEnd+birthDayDateRangeEnd);
				}
			} else if("9".equals(birthDayMode)) {// 生日模式为指定生日
				String birthDayMonth = (String)map.get("birthDayMonth");
				if(birthDayMonth != null && !"".equals(birthDayMonth)) {
					if(birthDayMonth.length() == 1) {
						birthDayMonth = "0" + birthDayMonth;
					}
					String birthDayDate = (String)map.get("birthDayDate");
					if(birthDayDate != null && !"".equals(birthDayDate)) {
						if(birthDayDate.length() == 1) {
							birthDayDate = "0" + birthDayDate;
						}
						map.put("birthDayStart", birthDayMonth + birthDayDate);
						map.put("birthDayEnd", birthDayMonth + birthDayDate);
					} else {
						map.put("birthDayStart", birthDayMonth + "01");
						map.put("birthDayEnd", birthDayMonth + DateUtil.getLastDateByMonth(birthDayMonth));
					}
				}
			}
		}
		// 第二次选择时的生日模式
		String birthDayDateMode = (String)map.get("birthDayDateMode");
		if(birthDayDateMode != null && !"".equals(birthDayDateMode)) {
			if("0".equals(birthDayDateMode)) {// 生日模式为当月生日
				String month = sysDate.substring(5, 7);
				String birthDayDateMoreStart = (String)map.get("birthDayDateMoreStart");
				String birthDayDateMoreEnd = (String)map.get("birthDayDateMoreEnd");
				if(birthDayDateMoreStart != null && !"".equals(birthDayDateMoreStart)) {
					if(birthDayDateMoreStart.length() == 1) {
						birthDayDateMoreStart = "0" + birthDayDateMoreStart;
					}
				} else {
					birthDayDateMoreStart = "01";
				}
				if(birthDayDateMoreEnd != null && !"".equals(birthDayDateMoreEnd)) {
					if(birthDayDateMoreEnd.length() == 1) {
						birthDayDateMoreEnd = "0" + birthDayDateMoreEnd;
					}
				} else {
					birthDayDateMoreEnd = DateUtil.getLastDateByMonth(month);
				}
				map.put("birthDayMoreStart", month + birthDayDateMoreStart);
				map.put("birthDayMoreEnd", month + birthDayDateMoreEnd);
			} else if("1".equals(birthDayDateMode)) {// 生日模式为当天生日
				String month = sysDate.substring(5, 7);
				String day = sysDate.substring(8, 10);
				map.put("birthDayMoreStart", month + day);
				map.put("birthDayMoreEnd", month + day);
			}
		}
		
		// 入会日期处理
		String joinDateMode = (String)map.get("joinDateMode");
		List<Map<String, Object>> joinDateRangeList = new ArrayList<Map<String, Object>>();
		// 存在入会时间模式查询的场合，按入会时间模式设置入会时间查询条件
		if(joinDateMode != null && !"".equals(joinDateMode)) {
			Map<String, Object> joinDateeRangeMap = new HashMap<String, Object>();
			if("0".equals(joinDateMode)) {// 入会时间模式为动态模式（例如：几年、几月、几天内入会的会员）
				String joinDateRange = (String)map.get("joinDateRange");
				if(joinDateRange != null && !"".equals(joinDateRange)) {
					String joinDateUnit = (String)map.get("joinDateUnit");
					String joinDateUnitFlag = (String)map.get("joinDateUnitFlag");
					if("0".equals(joinDateUnit)) {
						String preJoinDate = DateUtil.addDateByYears("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(joinDateRange));
						if("1".equals(joinDateUnitFlag)) {
							joinDateeRangeMap.put("joinDateStart", preJoinDate);
							joinDateeRangeMap.put("joinDateEnd", sysDate.substring(0,10));
						} else {
							joinDateeRangeMap.put("joinDateStart", preJoinDate);
							joinDateeRangeMap.put("joinDateEnd", preJoinDate);
						}
					} else if("1".equals(joinDateUnit)) {
						String preJoinDate = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(joinDateRange));
						if("1".equals(joinDateUnitFlag)) {
							joinDateeRangeMap.put("joinDateStart", preJoinDate);
							joinDateeRangeMap.put("joinDateEnd", sysDate.substring(0,10));
						} else {
							joinDateeRangeMap.put("joinDateStart", preJoinDate);
							joinDateeRangeMap.put("joinDateEnd", preJoinDate);
						}
					} else if("2".equals(joinDateUnit)) {
						String preJoinDate = DateUtil.addDateByDays("yyyy-MM-dd", sysDate.substring(0,10), 1-Integer.parseInt(joinDateRange));
						if("1".equals(joinDateUnitFlag)) {
							joinDateeRangeMap.put("joinDateStart", preJoinDate);
							joinDateeRangeMap.put("joinDateEnd", sysDate.substring(0,10));
						} else {
							joinDateeRangeMap.put("joinDateStart", preJoinDate);
							joinDateeRangeMap.put("joinDateEnd", preJoinDate);
						}
					}
				}
				joinDateRangeList.add(joinDateeRangeMap);
			} else if("1".equals(joinDateMode)) {// 入会时间模式为当月入会
				// 系统时间所在月份的第一天
				joinDateeRangeMap.put("joinDateStart", DateUtil.getFirstOrLastDateYMD(sysDate.substring(0,10), 0));
				// 系统时间所在月份的最后天
				joinDateeRangeMap.put("joinDateEnd", DateUtil.getFirstOrLastDateYMD(sysDate.substring(0,10), 1));
				joinDateRangeList.add(joinDateeRangeMap);
			} else if("9".equals(joinDateMode)) {
				String joinDateRangeJson = (String)map.get("joinDateRangeJson");
				if(joinDateRangeJson != null && !"".equals(joinDateRangeJson)) {
					joinDateRangeList = ConvertUtil.json2List(joinDateRangeJson);
				}
			}
		}
		if(!joinDateRangeList.isEmpty()) {
			map.put("joinDateRangeList", joinDateRangeList);
			map.put("joinDateTerm", "1");
		}
		
//		// 推荐会员
//		String referFlag = (String)map.get("referFlag");
//		if(referFlag != null && !"".equals(referFlag)) {
//			// 推荐会员选择【被别人推荐的会员】的场合
//			if("2".equals(referFlag)) {
//				// 推荐者卡号
//				String referrerMemCode = (String)map.get("referrerMemCode");
//				if(referrerMemCode != null && !"".equals(referrerMemCode)) {
//					Map<String, Object> paramMap = new HashMap<String, Object>();
//					paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
//					paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
//					paramMap.put("memCode", referrerMemCode);
//					// 根据会员卡号查询会员ID
//					String referrerId = binOLCM33_Service.getMemberInfoId(paramMap);
//					if(referrerId != null && !"".equals(referrerId)) {
//						map.put("referrerId", referrerId);
//					}
//				}
//			}
//		}
		
		// 等级调整日处理
		String levelAdjustDayFlag = (String)map.get("levelAdjustDayFlag");
		if(levelAdjustDayFlag != null && !"".equals(levelAdjustDayFlag)) {
			if(!"3".equals(levelAdjustDayFlag)) {
				map.remove("levelAdjustDayStart");
				map.remove("levelAdjustDayEnd");
			}
			if("1".equals(levelAdjustDayFlag)) {
				map.put("levelAdjustDayStart", sysDate);
				map.put("levelAdjustDayEnd", sysDate);
			} else if("2".equals(levelAdjustDayFlag)) {
				String levelAdjustDayRange = (String)map.get("levelAdjustDayRange");
				if(levelAdjustDayRange != null && !"".equals(levelAdjustDayRange)) {
					String levelAdjustDayUnit = (String)map.get("levelAdjustDayUnit");
					if("1".equals(levelAdjustDayUnit)) {
						String preJoinDate = DateUtil.addDateByDays("yyyy-MM-dd", sysDate, -Integer.parseInt(levelAdjustDayRange));
						map.put("levelAdjustDayStart", preJoinDate);
					} else if("2".equals(levelAdjustDayUnit)) {
						String preJoinDate = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate, -Integer.parseInt(levelAdjustDayRange));
						map.put("levelAdjustDayStart", preJoinDate);
					}
					map.put("levelAdjustDayEnd", sysDate);
				}
			}
		}
		String levelAdjustDayStart = (String)map.get("levelAdjustDayStart");
		String levelAdjustDayEnd = (String)map.get("levelAdjustDayEnd");
		if((levelAdjustDayStart != null && !"".equals(levelAdjustDayStart)) 
				|| (levelAdjustDayEnd != null && !"".equals(levelAdjustDayEnd))) {
			map.put("levelAdjustDayTerm", "1");
			if(levelAdjustDayStart != null && !"".equals(levelAdjustDayStart)) {
				map.put("levelAdjustDayStart", DateUtil.suffixDate(levelAdjustDayStart, 0));
			}
			if(levelAdjustDayEnd != null && !"".equals(levelAdjustDayEnd)) {
				map.put("levelAdjustDayEnd", DateUtil.suffixDate(levelAdjustDayEnd, 1));
			}
		}
		
		// 会员扩展条件去除空处理
		List<Map<String, Object>> propertyInfoList = (List)map.get("propertyInfoList");
		if(propertyInfoList != null && !propertyInfoList.isEmpty()) {
			List<Map<String, Object>> _propertyInfoList = new ArrayList<Map<String, Object>>();
			for(int i = 0; i < propertyInfoList.size(); i++) {
				Map<String, Object> propertyInfoMap = (Map)propertyInfoList.get(i);
				List propertyValue = (List)propertyInfoMap.get("propertyValues");
				if(propertyValue != null && !propertyValue.isEmpty() && !"".equals(propertyValue.get(0))) {
					_propertyInfoList.add(propertyInfoMap);
				} else {
					propertyInfoList.remove(i);
					i--;
				}
			}
			if(propertyInfoList.size() > 0) {
				map.put("propertyInfoList", _propertyInfoList);
			} else {
				map.remove("propertyInfoList");
			}
		}
		
		// 是否带权限查询默认为不按权限查询
		String privilegeFlag = (String)map.get("privilegeFlag");
		if(privilegeFlag == null || "".equals(privilegeFlag)) {
			map.put("privilegeFlag", "0");
		}
		// 业务类型默认为会员业务
		String businessType = (String)map.get("businessType");
		if(businessType == null || "".equals(businessType)) {
			map.put("businessType", "2");
		}
		// 操作类型默认为查询
		String operationType = (String)map.get("operationType");
		if(operationType == null || "".equals(operationType)) {
			map.put("operationType", "1");
		}
		
		// 购买条件查询方式
		String isSaleFlag = (String)map.get("isSaleFlag");
		// 无购买查询条件的场合
		if(isSaleFlag != null && "0".equals(isSaleFlag)) {
			// 无购买日期模式
			String notSaleTimeMode = (String)map.get("notSaleTimeMode");
			// 存在无购买日期模式查询的场合，按无购买日期模式设置无购买日期查询条件
			if(notSaleTimeMode != null && !"".equals(notSaleTimeMode)) {
				if(!"9".equals(notSaleTimeMode)) {
					map.remove("notSaleTimeStart");
					map.remove("notSaleTimeEnd");
				}
				if("0".equals(notSaleTimeMode)) {// 无购买日期模式为动态模式（例如：几年、几月、几天）
					String notSaleTimeRange = (String)map.get("notSaleTimeRange");
					if(notSaleTimeRange != null && !"".equals(notSaleTimeRange)) {
						String notSaleTimeUnit = (String)map.get("notSaleTimeUnit");
						if("0".equals(notSaleTimeUnit)) {
							String preSaleTime = DateUtil.addDateByYears("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(notSaleTimeRange));
							map.put("notSaleTimeStart", preSaleTime);
							map.put("notSaleTimeEnd", sysDate.substring(0,10));
						} else if("1".equals(notSaleTimeUnit)) {
							String preSaleTime = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(notSaleTimeRange));
							map.put("notSaleTimeStart", preSaleTime);
							map.put("notSaleTimeEnd", sysDate.substring(0,10));
						} else if("2".equals(notSaleTimeUnit)) {
							String preSaleTime = DateUtil.addDateByDays("yyyy-MM-dd", sysDate.substring(0,10), 1-Integer.parseInt(notSaleTimeRange));
							map.put("notSaleTimeStart", preSaleTime);
							map.put("notSaleTimeEnd", sysDate.substring(0,10));
						}
					}
				} else if ("8".equals(notSaleTimeMode)) {
					String notSaleTimeRangeLast = (String)map.get("notSaleTimeRangeLast");
					if(notSaleTimeRangeLast != null && !"".equals(notSaleTimeRangeLast)) {
						String notSaleLastTime = DateUtil.addDateByDays("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(notSaleTimeRangeLast));
						map.put("notSaleLastTime", notSaleLastTime);
					}
				}
			}
			String notSaleTimeStart = (String)map.get("notSaleTimeStart");
			String notSaleTimeEnd = (String)map.get("notSaleTimeEnd");
			if((notSaleTimeStart != null && !"".equals(notSaleTimeStart))
					|| (notSaleTimeEnd != null && !"".equals(notSaleTimeEnd))) {
				map.put("notSaleCountTerm", "1");
			}
		} else {// 有购买查询条件的场合
			// 购买日期处理
			String saleTimeMode = (String)map.get("saleTimeMode");
			// 存在购买日期模式查询的场合，按购买日期模式设置购买日期查询条件
			if(saleTimeMode != null && !"".equals(saleTimeMode)) {
				if(!"9".equals(saleTimeMode)) {
					map.remove("saleTimeStart");
					map.remove("saleTimeEnd");
				}
				if("0".equals(saleTimeMode)) {// 购买日期模式为动态模式（例如：几年、几月、几天内购买）
					String saleTimeRange = (String)map.get("saleTimeRange");
					if(saleTimeRange != null && !"".equals(saleTimeRange)) {
						String saleTimeUnit = (String)map.get("saleTimeUnit");
						if("0".equals(saleTimeUnit)) {
							String preSaleTime = DateUtil.addDateByYears("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(saleTimeRange));
							map.put("saleTimeStart", preSaleTime);
							map.put("saleTimeEnd", sysDate.substring(0,10));
						} else if("1".equals(saleTimeUnit)) {
							String preSaleTime = DateUtil.addDateByMonth("yyyy-MM-dd", sysDate.substring(0,10), -Integer.parseInt(saleTimeRange));
							map.put("saleTimeStart", preSaleTime);
							map.put("saleTimeEnd", sysDate.substring(0,10));
						} else if("2".equals(saleTimeUnit)) {
							String preSaleTime = DateUtil.addDateByDays("yyyy-MM-dd", sysDate.substring(0,10), 1-Integer.parseInt(saleTimeRange));
							map.put("saleTimeStart", preSaleTime);
							map.put("saleTimeEnd", sysDate.substring(0,10));
						}
					}
				}
			}
			
			// 购买柜台条件处理
			// 购买柜台所在区域
			String saleRegionId = (String)map.get("saleRegionId");
			// 购买柜台所在省
			String saleProvinceId = (String)map.get("saleProvinceId");
			// 购买柜台所在城市
			String saleCityId = (String)map.get("saleCityId");
			// 购买柜台所在部门
			String saleMemCounterId = (String)map.get("saleMemCounterId");
			// 购买柜台所在渠道
			String saleChannelId = (String)map.get("saleChannelId");
			// 所属系统ID
			String saleBelongId = (String)map.get("saleBelongId");
			if(saleRegionId != null && !"".equals(saleRegionId)) {
				map.put("saleRegionId", saleRegionId.split(","));
			}
			if(saleBelongId != null && !"".equals(saleBelongId)) {
				map.put("saleBelongId", saleBelongId.split(","));
			}
			if(saleProvinceId != null && !"".equals(saleProvinceId)) {
				map.put("saleProvinceId", saleProvinceId.split(","));
			}
			if(saleCityId != null && !"".equals(saleCityId)) {
				map.put("saleCityId", saleCityId.split(","));
			}
			if(saleMemCounterId != null && !"".equals(saleMemCounterId)) {
				map.put("saleMemCounterId", saleMemCounterId.split(","));
			}
			if(saleChannelId != null && !"".equals(saleChannelId)) {
				map.put("saleChannelId", saleChannelId.split(","));
			}
			//未购买时间段处理
			String unBuyInterval=(String) map.get("unBuyInterval");
			if(unBuyInterval != null && !"".equals(unBuyInterval)){
				if("3".equals(unBuyInterval)){
					map.put("unBuyIntervalStart", 0);
					map.put("unBuyIntervalEnd", 3);
				}else if("6".equals(unBuyInterval)){
					map.put("unBuyIntervalStart", 3);
					map.put("unBuyIntervalEnd", 6);
				}else if("9".equals(unBuyInterval)){
					map.put("unBuyIntervalStart", 6);
					map.put("unBuyIntervalEnd", 9);
				}else{
					map.put("unBuyIntervalStart", 9);
				}
			}
			// 销售条件处理
			//购买支数
			String payQuantityStart=(String) map.get("payQuantityStart");
			String payQuantityEnd=(String) map.get("payQuantityEnd");
			String saleCountStart = (String)map.get("saleCountStart");
			String saleCountEnd = (String)map.get("saleCountEnd");
			String payAmountStart = (String)map.get("payAmountStart");
			String payAmountEnd = (String)map.get("payAmountEnd");
			Object buyPrtVendorId = map.get("buyPrtVendorId");
			if(buyPrtVendorId != null) {
				List<String> _buyPrtVendorId = new ArrayList<String>();
				if(buyPrtVendorId instanceof String) {
					if(!"".equals(buyPrtVendorId.toString())) {
						_buyPrtVendorId.add(buyPrtVendorId.toString());
					}
				} else {
					_buyPrtVendorId = (List)buyPrtVendorId;
				}
				map.put("buyPrtVendorId", _buyPrtVendorId);
			}
			Object buyCateValId = map.get("buyCateValId");
			if(buyCateValId != null) {
				List<String> _buyCateValId = new ArrayList<String>();
				if(buyCateValId instanceof String) {
					if(!"".equals(buyCateValId.toString())) {
						_buyCateValId.add(buyCateValId.toString());
					}
				} else {
					_buyCateValId = (List)buyCateValId;
				}
				map.put("buyCateValId", _buyCateValId);
			}
			Object notPrtVendorId = map.get("notPrtVendorId");
			if(notPrtVendorId != null) {
				List<String> _notPrtVendorId = new ArrayList<String>();
				if(notPrtVendorId instanceof String) {
					if(!"".equals(notPrtVendorId.toString())) {
						_notPrtVendorId.add(notPrtVendorId.toString());
					}
				} else {
					_notPrtVendorId = (List)notPrtVendorId;
				}
				map.put("notPrtVendorId", _notPrtVendorId);
			}
			Object notCateValId = map.get("notCateValId");
			if(notCateValId != null) {
				List<String> _notCateValId = new ArrayList<String>();
				if(notCateValId instanceof String) {
					if(!"".equals(notCateValId.toString())) {
						_notCateValId.add(notCateValId.toString());
					}
				} else {
					_notCateValId = (List)notCateValId;
				}
				map.put("notCateValId", _notCateValId);
			}
			
			List<String> newBuyPrtVendorId = (List)map.get("buyPrtVendorId");
			List<String> newBuyCateValId = (List)map.get("buyCateValId");
			List<String> newNotPrtVendorId = (List)map.get("notPrtVendorId");
			List<String> newNotCateValId = (List)map.get("notCateValId");
			if((newBuyPrtVendorId != null && !newBuyPrtVendorId.isEmpty())
					|| (newBuyCateValId != null && !newBuyCateValId.isEmpty())) {
				map.put("saleTerm", "1");
			}
			if((newNotPrtVendorId != null && !newNotPrtVendorId.isEmpty())
					|| (newNotCateValId != null && !newNotCateValId.isEmpty())) {
				map.put("notSaleTerm", "1");
			}
			if(saleCountStart != null && !"".equals(saleCountStart)) {
				if(Integer.parseInt(saleCountStart) < 0) {
					map.put("saleCountStart", "0");
					saleCountStart = "0";
				}
			}
			if(saleCountEnd != null && !"".equals(saleCountEnd)) {
				if(Integer.parseInt(saleCountEnd) < 0) {
					map.put("saleCountEnd", "0");
					saleCountEnd = "0";
				}
			}
			if((saleCountStart != null && !"".equals(saleCountStart)) 
					|| (saleCountEnd != null && !"".equals(saleCountEnd))) {
				if(ConvertUtil.getInt(saleCountStart)> 0) {
					map.put("saleCountTerm", "2");
				} else {
					map.put("saleCountTerm", "1");
				}
			}
			boolean flag = false;
			double payAmountStartDou = 0;
			if(payAmountStart != null && !"".equals(payAmountStart)) {
				payAmountStartDou = Double.parseDouble(payAmountStart);
				if(payAmountStartDou < 0) {
					map.put("payAmountStart", "0");
					payAmountStart = "0";
				}
				flag = true;
			}
			if(payAmountEnd != null && !"".equals(payAmountEnd)) {
				if(Double.parseDouble(payAmountEnd) < 0) {
					map.put("payAmountEnd", "0");
					payAmountEnd = "0";
				}
				flag = true;
			}
			double quantityStart = 0;
			if(payQuantityStart != null && !"".equals(payQuantityStart)){
				quantityStart = Double.parseDouble(payQuantityStart);
				if(quantityStart < 0){
					map.put("payQuantityStart", "0");
				}
				flag = true;
			}
			if(payQuantityEnd != null && !"".equals(payQuantityEnd)){
				if(Double.parseDouble(payQuantityEnd) < 0){
					map.put("payQuantityEnd", "0");
				}
				flag = true;
			}
			if(flag) {
				if(payAmountStartDou > 0 || quantityStart > 0) {
					map.put("saleAmountTerm", "2");
				} else {
					map.put("saleAmountTerm", "1");
				}
			}
			if((saleCountStart == null || "".equals(saleCountStart))
					&& (saleCountEnd == null || "".equals(saleCountEnd))
					&& !flag
					&& (newBuyPrtVendorId == null || newBuyPrtVendorId.isEmpty())
					&& (newBuyCateValId == null || newBuyCateValId.isEmpty())
					&& (newNotPrtVendorId == null || newNotPrtVendorId.isEmpty())
					&& (newNotCateValId == null || newNotCateValId.isEmpty())) {
				String saleTimeStart = (String)map.get("saleTimeStart");
				String saleTimeEnd = (String)map.get("saleTimeEnd");
				String saleCounterId = (String)map.get("saleCounterId");
				String saleModeFlag = (String)map.get("saleModeFlag");
				if((saleTimeStart != null && !"".equals(saleTimeStart))
						|| (saleTimeEnd != null && !"".equals(saleTimeEnd))
						|| (saleCounterId != null && !"".equals(saleCounterId))
						|| (saleModeFlag != null && !"".equals(saleModeFlag))) {
					map.put("saleCountStart", "1");
					map.put("saleCountTerm", "2");
				}
			}
		}
		// 发卡柜台条件处理
		// 发卡柜台所在区域
		String regionId = (String)map.get("regionId");
		// 发卡柜台所在省
		String provinceId = (String)map.get("provinceId");
		// 发卡柜台所在城市
		String cityId = (String)map.get("cityId");
		// 发卡柜台所在部门
		String memCounterId = (String)map.get("memCounterId");
		// 发卡柜台所在渠道
		String channelId = (String)map.get("channelId");
		// 选择的发卡柜台条件是包含还是排除flag（1：包含，2：排除）
		String exclusiveFlag = (String)map.get("exclusiveFlag");
		// 所属系统ID
		String belongId = (String) map.get("belongId");
		if(regionId != null && !"".equals(regionId)) {
			map.put("regionId", regionId.split(","));
		}
		if(belongId != null && !"".equals(belongId)) {
			map.put("belongId", belongId.split(","));
		}
		if(provinceId != null && !"".equals(provinceId)) {
			map.put("provinceId", provinceId.split(","));
		}
		if(cityId != null && !"".equals(cityId)) {
			map.put("cityId", cityId.split(","));
		}
		if(memCounterId != null && !"".equals(memCounterId)) {
			map.put("memCounterId", memCounterId.split(","));
		}
		if(channelId != null && !"".equals(channelId)) {
			map.put("channelId", channelId.split(","));
		}
		// 选择的发卡柜台条件是包含还是排除flag默认设置为包含
		if(exclusiveFlag == null || "".equals(exclusiveFlag)) {
			map.put("exclusiveFlag", "1");
		}
		String clubMod = null;
		String orgId = map.get(CherryConstants.ORGANIZATIONINFOID).toString();
		String brandId = map.get(CherryConstants.BRANDINFOID).toString();
		if (map.containsKey("clubMod")) {
			clubMod = (String) map.get("clubMod");
		} else {
			clubMod = binOLCM14_BL.getConfigValue("1299", orgId, brandId);
		}
		// 俱乐部发卡柜台
		if (null != clubMod && !"3".equals(clubMod)) {
			// 发卡柜台条件处理
			// 发卡柜台所在区域
			String clubRegionId = (String)map.get("clubRegionId");
			// 发卡柜台所在省
			String clubProvinceId = (String)map.get("clubProvinceId");
			// 发卡柜台所在城市
			String clubCityId = (String)map.get("clubCityId");
			// 发卡柜台所在部门
			String clubMemCounterId = (String)map.get("clubMemCounterId");
			// 发卡柜台所在渠道
			String clubChannelId = (String)map.get("clubChannelId");
			// 选择的发卡柜台条件是包含还是排除flag（1：包含，2：排除）
			String clubExclusiveFlag = (String)map.get("clubExclusiveFlag");
			// 所属系统ID
			String clubBelongId = (String) map.get("clubBelongId");
			if(clubRegionId != null && !"".equals(clubRegionId)) {
				map.put("clubRegionId", clubRegionId.split(","));
			}
			if(clubBelongId != null && !"".equals(clubBelongId)) {
				map.put("clubBelongId", clubBelongId.split(","));
			}
			if(clubProvinceId != null && !"".equals(clubProvinceId)) {
				map.put("clubProvinceId", clubProvinceId.split(","));
			}
			if(clubCityId != null && !"".equals(clubCityId)) {
				map.put("clubCityId", clubCityId.split(","));
			}
			if(clubMemCounterId != null && !"".equals(clubMemCounterId)) {
				map.put("clubMemCounterId", clubMemCounterId.split(","));
			}
			if(clubChannelId != null && !"".equals(clubChannelId)) {
				map.put("clubChannelId", clubChannelId.split(","));
			}
			// 选择的发卡柜台条件是包含还是排除flag默认设置为包含
			if(clubExclusiveFlag == null || "".equals(clubExclusiveFlag)) {
				map.put("clubExclusiveFlag", "1");
			}
			map.put("clubEmployeeIdQ", map.get("clubEmployeeId"));
			map.remove("clubEmployeeId");
		}
		
		String memTag = binOLCM14_BL.getConfigValue("1339", orgId, brandId);
		map.put("memTag", memTag);
		// 会员标签搜索
		if ("1".equals(memTag)) {
//			Object mostCateIdObj = map.get("mostCateId");
//			if(mostCateIdObj != null) {
//				List<String> _mostCateId = new ArrayList<String>();
//				if(mostCateIdObj instanceof String) {
//					if(!"".equals(mostCateIdObj.toString())) {
//						_mostCateId.add(mostCateIdObj.toString());
//					}
//				} else {
//					_mostCateId = (List)mostCateIdObj;
//				}
//				map.put("mostCateId", _mostCateId);
//			}
			Object mostCateBClassIdObj = map.get("mostCateBClassId");
			if(mostCateBClassIdObj != null) {
				List<String> _mostCateBClassId = new ArrayList<String>();
				if(mostCateBClassIdObj instanceof String) {
					if(!"".equals(mostCateBClassIdObj.toString())) {
						_mostCateBClassId.add(mostCateBClassIdObj.toString());
					}
				} else {
					_mostCateBClassId = (List)mostCateBClassIdObj;
				}
				map.put("mostCateBClassId", _mostCateBClassId);
			}
			Object mostCateMClassIdObj = map.get("mostCateMClassId");
			if(mostCateMClassIdObj != null) {
				List<String> _mostCateMClassId = new ArrayList<String>();
				if(mostCateMClassIdObj instanceof String) {
					if(!"".equals(mostCateMClassIdObj.toString())) {
						_mostCateMClassId.add(mostCateMClassIdObj.toString());
					}
				} else {
					_mostCateMClassId = (List)mostCateMClassIdObj;
				}
				map.put("mostCateMClassId", _mostCateMClassId);
			}
//			Object jointCateIdObj = map.get("jointCateId");
//			if(jointCateIdObj != null) {
//				List<String> _jointCateIdId = new ArrayList<String>();
//				if(jointCateIdObj instanceof String) {
//					if(!"".equals(jointCateIdObj.toString())) {
//						_jointCateIdId.add(jointCateIdObj.toString());
//					}
//				} else {
//					_jointCateIdId = (List)jointCateIdObj;
//				}
//				map.put("jointCateId", _jointCateIdId);
//			}
			Object mostPrtIdObj = map.get("mostPrtId");
			if(mostPrtIdObj != null) {
				List<String> _mostPrtId = new ArrayList<String>();
				if(mostPrtIdObj instanceof String) {
					if(!"".equals(mostPrtIdObj.toString())) {
						_mostPrtId.add(mostPrtIdObj.toString());
					}
				} else {
					_mostPrtId = (List)mostPrtIdObj;
				}
				map.put("mostPrtId", _mostPrtId);
			}
//			Object jointPrtIdObj = map.get("jointPrtId");
//			if(jointPrtIdObj != null) {
//				List<String> _jointPrtId = new ArrayList<String>();
//				if(jointPrtIdObj instanceof String) {
//					if(!"".equals(jointPrtIdObj.toString())) {
//						_jointPrtId.add(jointPrtIdObj.toString());
//					}
//				} else {
//					_jointPrtId = (List)jointPrtIdObj;
//				}
//				map.put("jointPrtId", _jointPrtId);
//			}
			Map<String, Object> searchMap = new HashMap<String, Object>();
			//List<String> mostCateId = (List<String>) map.get("mostCateId");
			List<String> mostCateBClassId = (List<String>) map.get("mostCateBClassId");
			List<String> mostCateMClassId = (List<String>) map.get("mostCateMClassId");
//			List<String> jointCateId = (List<String>) map.get("jointCateId");
			List<String> mostPrtId = (List<String>) map.get("mostPrtId");
//			List<String> jointPrtId = (List<String>) map.get("jointPrtId");
//			if (null != mostCateId && !mostCateId.isEmpty()) {
//				searchMap.put("cateValId", mostCateId);
//				List<Map<String, Object>> mostCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//				map.put("mostCateList", mostCateList);
//			}
			//判断最多购买大类是否选以及返回List
			if (null != mostCateBClassId && !mostCateBClassId.isEmpty()) {
				searchMap.put("cateValId", mostCateBClassId);
				List<Map<String, Object>> mostCateBClassList = binOLCM33_Service.getProTypeInfoList(searchMap);
				map.put("mostCateBClassList", mostCateBClassList);
			}
			//判断最多购买中类是否选以及返回List
			if (null != mostCateMClassId && !mostCateMClassId.isEmpty()) {
				searchMap.put("cateValId", mostCateMClassId);
				List<Map<String, Object>> mostCateMClassList = binOLCM33_Service.getProTypeInfoList(searchMap);
				map.put("mostCateMClassList", mostCateMClassList);
			}
//			if (null != jointCateId && !jointCateId.isEmpty()) {
//				searchMap.put("cateValId", jointCateId);
//				List<Map<String, Object>> jointCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//				map.put("jointCateList", jointCateList);
//			}
//			//判断连带购买大类是否选以及返回List
//			if (null != jointCateId && !jointCateId.isEmpty()) {
//				searchMap.put("cateValId", jointCateId);
//				List<Map<String, Object>> jointCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//				map.put("jointCateList", jointCateList);
//			}
//			//判断连带购买中类是否选以及返回List
//			if (null != jointCateId && !jointCateId.isEmpty()) {
//				searchMap.put("cateValId", jointCateId);
//				List<Map<String, Object>> jointCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//				map.put("jointCateList", jointCateList);
//			}
			if (null != mostPrtId && !mostPrtId.isEmpty()) {
				searchMap.put("prtVendorId", mostPrtId);
				List<Map<String, Object>> mostPrtList = binOLCM33_Service.getProInfoList(searchMap);
				map.put("mostPrtList", mostPrtList);
			}
//			if (null != jointPrtId && !jointPrtId.isEmpty()) {
//				searchMap.put("prtVendorId", jointPrtId);
//				List<Map<String, Object>> jointPrtList = binOLCM33_Service.getProInfoList(searchMap);
//				map.put("jointPrtList", jointPrtList);
//			}
		}
		
		// 分页开始位置默认设定为1
		Object start = map.get("START");
		if(start == null || "".equals(start.toString())) {
			map.put("START", "1");
		}
		// 分页结束位置默认设定为50
		Object end = map.get("END");
		if(end == null || "".equals(end.toString())) {
			map.put("END", "50");
		}
		// 排序字段默认设置为会员入会时间
		Object sortId = map.get("SORT_ID");
		if(sortId == null || "".equals(sortId.toString())) {
			map.put("SORT_ID", "joinDate");
		}
		// 会员性别
		Object mebSex = map.get("mebSex");
		if(mebSex != null) {
			List<String> _mebSex = new ArrayList<String>();
			if(mebSex instanceof String) {
				if(!"".equals(mebSex.toString())) {
					_mebSex.add(mebSex.toString());
				}
			} else {
				_mebSex = (List)mebSex;
			}
			map.put("mebSex", _mebSex);
		}
		
		// 会员类型
		Object memType = map.get("memType");
		if(memType != null) {
			List<String> _memType = new ArrayList<String>();
			if(memType instanceof String) {
				if(!"".equals(memType.toString())) {
					_memType.add(memType.toString());
				}
			} else {
				_memType = (List)memType;
			}
			map.put("memType", _memType);
		}
		// 会员等级
		Object memLevel = map.get("memLevel");
		if(memLevel != null) {
			List<String> _memLevel = new ArrayList<String>();
			if(memLevel instanceof String) {
				if(!"".equals(memLevel.toString())) {
					_memLevel.add(memLevel.toString());
				}
			} else {
				_memLevel = (List)memLevel;
			}
			map.put("memLevel", _memLevel);
		}
		
		// 会员已参加活动
		String campaignCode = (String)map.get("campaignCode");
		String participateTimeStart = (String)map.get("participateTimeStart");
		String participateTimeEnd = (String)map.get("participateTimeEnd");
		String campaignCounterId = (String)map.get("campaignCounterId");
		Object campaignState = map.get("campaignState");
		List<String> _campaignState = new ArrayList<String>();
		if(campaignState != null) {
			if(campaignState instanceof String) {
				if(!"".equals(campaignState.toString())) {
					_campaignState.add(campaignState.toString());
				}
			} else {
				_campaignState = (List)campaignState;
			}
			map.put("campaignState", _campaignState);
		}
		if((campaignCode != null && !"".equals(campaignCode))
				|| (participateTimeStart != null && !"".equals(participateTimeStart))
				|| (participateTimeEnd != null && !"".equals(participateTimeEnd))
				|| (campaignCounterId != null && !"".equals(campaignCounterId))
				|| (_campaignState != null && !_campaignState.isEmpty())) {
			map.put("campaignTerm", "1");
		}
		
		List<String> selectors = (List)map.get("selectors");
		if(selectors != null && !selectors.isEmpty()) {
			// 需要显示地址信息的场合
			if(selectors.contains("province") 
					|| selectors.contains("city")
					|| selectors.contains("address")
					|| selectors.contains("zip")) {
				map.put("memAddressShow", "1");
			}
			// 需要显示发卡BA信息的场合
			if (selectors.contains("employeeCode") || selectors.contains("employeeName")) {
				map.put("employeeShow", "1");
			}
			// 需要显示会员首单和末单的场合
			if(selectors.contains("saleBillCode")) {
				map.put("saleBillCodeShow", "1");
			}
		}
		
		map.put("employeeIdQ", map.get("employeeId"));
		map.remove("employeeId");
		
		// 最近一次购买时间
//		String lastSaleDateStart = (String)map.get("lastSaleDateStart");
//		String lastSaleDateEnd = (String)map.get("lastSaleDateEnd");
//		if(lastSaleDateStart != null && !"".equals(lastSaleDateStart)) {
//			map.put("lastSaleDateStart", DateUtil.suffixDate(lastSaleDateStart, 0));
//		}
//		if(lastSaleDateEnd != null && !"".equals(lastSaleDateEnd)) {
//			map.put("lastSaleDateEnd", DateUtil.suffixDate(lastSaleDateEnd, 1));
//		}
		String firstSaleDayFlag = null;
		String noSaleDaysMode = (String) map.get("noSaleDaysMode");
		if ("2".equals(noSaleDaysMode)) {
			String notSaleDays = (String)map.get("notSaleDaysRange");
			if(notSaleDays != null && !"".equals(notSaleDays)) {
				map.put("notSaleDaysTemp", Integer.parseInt(notSaleDays));
				firstSaleDayFlag = "1";
			} else if (!CherryChecker.isNullOrEmpty(map.get("firstStartDay")) 
					|| !CherryChecker.isNullOrEmpty(map.get("firstEndDay"))) {
				firstSaleDayFlag = "1";
			}
		} else {
			String notSaleDays = (String)map.get("notSaleDays");
			if(notSaleDays != null && !"".equals(notSaleDays)) {
				map.put("notSaleDaysTemp", Integer.parseInt(notSaleDays));
				map.put("notSaleSysDate", DateUtil.suffixDate(sysDate.substring(0, 10),1));
				map.put("notSaleDaysNeg", -Integer.parseInt(notSaleDays));
				firstSaleDayFlag = "1";
			}
		}
		if (null != firstSaleDayFlag) {
			map.put("firstSaleDayFlag", firstSaleDayFlag);
		}
		String memPointRangeJson = (String)map.get("memPointRangeJson");
		if(memPointRangeJson != null && !"".equals(memPointRangeJson)) {
			map.put("memPointRangeList",ConvertUtil.json2List(memPointRangeJson));
		}
		String changablePointRangeJson = (String)map.get("changablePointRangeJson");
		if(changablePointRangeJson != null && !"".equals(changablePointRangeJson)) {
			map.put("changablePointRangeList",ConvertUtil.json2List(changablePointRangeJson));
		}
		String lastSaleTimeRangeJson = (String)map.get("lastSaleTimeRangeJson");
		if(lastSaleTimeRangeJson != null && !"".equals(lastSaleTimeRangeJson)) {
			List<Map<String, Object>> lastSaleTimeRangeList = ConvertUtil.json2List(lastSaleTimeRangeJson);
			for(Map<String, Object> lastSaleTimeRangeMap: lastSaleTimeRangeList) {
				String _lastSaleDateStart = (String)lastSaleTimeRangeMap.get("lastSaleDateStart");
				String _lastSaleDateEnd = (String)lastSaleTimeRangeMap.get("lastSaleDateEnd");
				if(_lastSaleDateStart != null && !"".equals(_lastSaleDateStart)) {
					lastSaleTimeRangeMap.put("lastSaleDateStart", DateUtil.suffixDate(_lastSaleDateStart, 0));
				}
				if(_lastSaleDateEnd != null && !"".equals(_lastSaleDateEnd)) {
					lastSaleTimeRangeMap.put("lastSaleDateEnd", DateUtil.suffixDate(_lastSaleDateEnd, 1));
				}
			}
			map.put("lastSaleTimeRangeList",lastSaleTimeRangeList);
		}
		String firstSaleTimeRangeJson = (String)map.get("firstSaleTimeRangeJson");
		if(firstSaleTimeRangeJson != null && !"".equals(firstSaleTimeRangeJson)) {
			List<Map<String, Object>> firstSaleTimeRangeList = ConvertUtil.json2List(firstSaleTimeRangeJson);
			for(Map<String, Object> firstSaleTimeRangeMap: firstSaleTimeRangeList) {
				String firstStartDay = (String)firstSaleTimeRangeMap.get("firstStartDay");
				String firstEndDay = (String)firstSaleTimeRangeMap.get("firstEndDay");
				if(firstStartDay != null && !"".equals(firstStartDay)) {
					firstSaleTimeRangeMap.put("firstStartDay", DateUtil.suffixDate(firstStartDay, 0));
				}
				if(firstEndDay != null && !"".equals(firstEndDay)) {
					firstSaleTimeRangeMap.put("firstEndDay", DateUtil.suffixDate(firstEndDay, 1));
				}
			}
			map.put("firstSaleTimeRangeList",firstSaleTimeRangeList);
		}
	}
	
	/**
	 * 查询会员List
	 * 
	 * @param map 查询条件
	 * @return 查询结果
	 */
	public Map<String, Object> searchMemList(Map<String, Object> map) {
		
		// 查询结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 控制查询结果（0：只查询件数 2：只查询数据 1或者null：件数和数据都查询）
			String resultMode = (String)map.get("resultMode");
			if(resultMode == null || "".equals(resultMode)) {
				resultMode = "1";
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.putAll(map);
			// 会员搜索条件设定
			this.setCondition(paramMap);
			String orgId = paramMap.get(CherryConstants.ORGANIZATIONINFOID).toString();
			String brandId = paramMap.get(CherryConstants.BRANDINFOID).toString();
			// 使用LUCENE索引查询的场合
			if(binOLCM14_BL.isConfigOpen("1071", orgId, brandId)) {
				logger.debug("Lucene search");
				// 查询会员信息
				resultMap = binOLCM27_BL.accessCherryWebService(CherryConstants.WS_MEMINFO, CherryConstants.WS_TYPE_GET, paramMap);
				if(resultMap != null) {
					List<Map<String, Object>> memberInfoList = (List)resultMap.get("list");
					if(memberInfoList != null && !memberInfoList.isEmpty()) {
						// 查询会员等级信息List
						List<Map<String, Object>> memLevelInfoList = binOLCM33_Service.getMemberLevelInfoList(paramMap);
						if(memLevelInfoList != null && !memLevelInfoList.isEmpty()) {
							for(Map<String, Object> memberInfo : memberInfoList) {
								String memLevelId = (String)memberInfo.get("memLevelId");
								if(memLevelId != null && !"".equals(memLevelId)) {
									memberInfo.put("levelName", getMemLevelName(memLevelId, memLevelInfoList));
								}
							}
						}
					}
				}
			} else {// 使用数据库查询的场合
				logger.debug("Database search");
				int count = 0;
				boolean isNoClub = true;
				String clubMod = null;
				if (map.containsKey("clubMod")) {
					clubMod = (String) map.get("clubMod");
				} else {
					clubMod = binOLCM14_BL.getConfigValue("1299", orgId, brandId);
				}
				if (null != clubMod && !"3".equals(clubMod)) {
					isNoClub = false;
				} 
//				else {
//					String memTag = binOLCM14_BL.getConfigValue("1339", orgId, brandId);
//					paramMap.put("memTag", memTag);
//					// 会员标签搜索
//					if ("1".equals(memTag)) {
//						Map<String, Object> searchMap = new HashMap<String, Object>();
//						List<String> mostCateId = (List<String>) paramMap.get("mostCateId");
//						List<String> jointCateId = (List<String>) paramMap.get("jointCateId");
//						List<String> mostPrtId = (List<String>) paramMap.get("mostPrtId");
//						List<String> jointPrtId = (List<String>) paramMap.get("jointPrtId");
//						if (null != mostCateId && !mostCateId.isEmpty()) {
//							searchMap.put("cateValId", mostCateId);
//							List<Map<String, Object>> mostCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//							paramMap.put("mostCateList", mostCateList);
//						}
//						if (null != jointCateId && !jointCateId.isEmpty()) {
//							searchMap.put("cateValId", jointCateId);
//							List<Map<String, Object>> jointCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//							paramMap.put("jointCateList", jointCateList);
//						}
//						if (null != mostPrtId && !mostPrtId.isEmpty()) {
//							searchMap.put("prtVendorId", mostPrtId);
//							List<Map<String, Object>> mostPrtList = binOLCM33_Service.getProInfoList(searchMap);
//							paramMap.put("mostPrtList", mostPrtList);
//						}
//						if (null != jointPrtId && !jointPrtId.isEmpty()) {
//							searchMap.put("prtVendorId", jointPrtId);
//							List<Map<String, Object>> jointPrtList = binOLCM33_Service.getProInfoList(searchMap);
//							paramMap.put("jointPrtList", jointPrtList);
//						}
//					}
//				}
				if(!"2".equals(resultMode)) {
					if (isNoClub) {
						// 查询会员总数
						count = binOLCM33_Service.getMemberInfoCount(paramMap);
					} else {
						// 查询会员总数
						count = binOLCM33_Service.getMemberClubInfoCount(paramMap);
					}
					resultMap.put("total", count);
				}
				if((count > 0 && "1".equals(resultMode)) || "2".equals(resultMode)) {
					// 查询会员List
					List<Map<String, Object>> memberList = null;
					if (isNoClub) {
						memberList = binOLCM33_Service.getMemberInfoList(paramMap);
					} else {
						memberList = binOLCM33_Service.getMemberClubInfoList(paramMap);
					}
					//解密会员【备注1】字段
					getMemDecryptInfo(map,memberList);
					if(memberList != null && !memberList.isEmpty()) {
						// 需要显示地址信息的场合，把省份ID和城市ID转化为名称
						String memAddressShow = (String)paramMap.get("memAddressShow");
						if(memAddressShow != null && !"".equals(memAddressShow)) {
							List<Map<String, Object>> allRegionList = binOLCM33_Service.getAllRegionList(paramMap);
							for(Map<String, Object> memberMap : memberList) {
								Object province = memberMap.get("province");
								if(province != null && !"".equals(province.toString())) {
									memberMap.put("province", this.getNameByKey(province.toString(), allRegionList));
								}
								Object city = memberMap.get("city");
								if(city != null && !"".equals(city.toString())) {
									memberMap.put("city", this.getNameByKey(city.toString(), allRegionList));
								}
							}
						}
					}
					resultMap.put("list", memberList);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 取得会员销售信息
	 * 
	 * @param map 查询条件
	 * @return 会员销售信息
	 */
	public Map<String, Object> getMemSaleRecord(Map<String, Object> map) {
		
		String resultMode = (String)map.get("resultMode");
		if(resultMode == null || "".equals(resultMode)) {
			resultMode = "1";
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		// 会员搜索条件设定
		this.setCondition(paramMap);
		boolean isNoClub = true;
		String clubMod = null;
		if (map.containsKey("clubMod")) {
			clubMod = (String) map.get("clubMod");
		} else {
			if (paramMap.containsKey(CherryConstants.ORGANIZATIONINFOID) && paramMap.containsKey(CherryConstants.BRANDINFOID)) {
				String orgId = paramMap.get(CherryConstants.ORGANIZATIONINFOID).toString();
				String brandId = paramMap.get(CherryConstants.BRANDINFOID).toString();
				clubMod = binOLCM14_BL.getConfigValue("1299", orgId, brandId);
			}
		}
		if (null != clubMod && !"3".equals(clubMod)) {
			isNoClub = false;
		} 
//		else {
//			if (paramMap.containsKey(CherryConstants.ORGANIZATIONINFOID) && paramMap.containsKey(CherryConstants.BRANDINFOID)) {
//				String orgId = paramMap.get(CherryConstants.ORGANIZATIONINFOID).toString();
//				String brandId = paramMap.get(CherryConstants.BRANDINFOID).toString();
//				String memTag = binOLCM14_BL.getConfigValue("1339", orgId, brandId);
//				paramMap.put("memTag", memTag);
//				// 会员标签搜索
//				if ("1".equals(memTag)) {
//					Map<String, Object> searchMap = new HashMap<String, Object>();
//					List<String> mostCateId = (List<String>) paramMap.get("mostCateId");
//					List<String> jointCateId = (List<String>) paramMap.get("jointCateId");
//					List<String> mostPrtId = (List<String>) paramMap.get("mostPrtId");
//					List<String> jointPrtId = (List<String>) paramMap.get("jointPrtId");
//					if (null != mostCateId && !mostCateId.isEmpty()) {
//						searchMap.put("cateValId", mostCateId);
//						List<Map<String, Object>> mostCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//						paramMap.put("mostCateList", mostCateList);
//					}
//					if (null != jointCateId && !jointCateId.isEmpty()) {
//						searchMap.put("cateValId", jointCateId);
//						List<Map<String, Object>> jointCateList = binOLCM33_Service.getProTypeInfoList(searchMap);
//						paramMap.put("jointCateList", jointCateList);
//					}
//					if (null != mostPrtId && !mostPrtId.isEmpty()) {
//						searchMap.put("prtVendorId", mostPrtId);
//						List<Map<String, Object>> mostPrtList = binOLCM33_Service.getProInfoList(searchMap);
//						paramMap.put("mostPrtList", mostPrtList);
//					}
//					if (null != jointPrtId && !jointPrtId.isEmpty()) {
//						searchMap.put("prtVendorId", jointPrtId);
//						List<Map<String, Object>> jointPrtList = binOLCM33_Service.getProInfoList(searchMap);
//						paramMap.put("jointPrtList", jointPrtList);
//					}
//				}
//			}
//		}
		if (isNoClub) {
			Map<String, Object> memSaleRecord = binOLCM33_Service.getMemSaleCount(paramMap);
			if(memSaleRecord != null) {
				Integer count = (Integer)memSaleRecord.get("count");
				if(count != null && count > 0 && "1".equals(resultMode)) {
					List<Map<String, Object>> memSaleList = binOLCM33_Service.getMemSaleList(paramMap);
					memSaleRecord.put("memSaleList", memSaleList);
				}
				return memSaleRecord;
			}
		} else {
			Map<String, Object> memSaleRecord = binOLCM33_Service.getMemClubSaleCount(paramMap);
			if(memSaleRecord != null) {
				Integer count = (Integer)memSaleRecord.get("count");
				if(count != null && count > 0 && "1".equals(resultMode)) {
					List<Map<String, Object>> memSaleList = binOLCM33_Service.getMemClubSaleList(paramMap);
					memSaleRecord.put("memSaleList", memSaleList);
				}
				return memSaleRecord;
			}
		}
		return null;
	}
	
	/**
	 * 解密会员【备注1】字段
	 * @param parameterMap
	 * @param memberList
	 * @throws Exception
	 */
	public void getMemDecryptInfo(Map<String, Object> parameterMap,
			List<Map<String, Object>> memberList) throws Exception {
		if (memberList != null && !memberList.isEmpty()) {
			String brandCode = ConvertUtil.getString(parameterMap
					.get("brandCode"));
			for (Map<String, Object> memberInfoMap : memberList) {
				try {
					// 会员【备注1】字段解密
					if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("memo1"),true)) {
						String memo1 = ConvertUtil.getString(memberInfoMap.get("memo1"));
						memberInfoMap.put("memo1",CherrySecret.decryptData(brandCode, memo1));
					}
					// 会员【电话】字段解密
					if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("telephone"), true)) {
						String telephone = ConvertUtil.getString(memberInfoMap.get("telephone"));
						memberInfoMap.put("telephone", CherrySecret.decryptData(brandCode,telephone));
					}
					// 会员【手机号】字段解密
					if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("mobilePhone"), true)) {
						String mobilePhone = ConvertUtil.getString(memberInfoMap.get("mobilePhone"));
						memberInfoMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
					}
					// 会员【电子邮箱】字段解密
					if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("email"), true)) {
						String email = ConvertUtil.getString(memberInfoMap.get("email"));
						memberInfoMap.put("email", CherrySecret.decryptData(brandCode,email));
					}
				} catch (Exception e) {
					Object memId = memberInfoMap.get("memId");
					Object memCode = memberInfoMap.get("memCode");
					logger.error("解密出错，错误的会员ID为："+memId+"，会员卡号为："+(memCode==null?"":memCode));
					throw e;
				}
			}
		}
	}
	/**
	 * 根据会员ID取得会员等级名称
	 * 
	 * @param memLevelId 会员等级ID
	 * @param memLevelInfoList 会员等级信息List
	 * @return 会员等级名称
	 */
	public String getMemLevelName(String memLevelId, List<Map<String, Object>> memLevelInfoList) {
		
		for(Map<String, Object> memLevelInfo : memLevelInfoList) {
			String _memberLevelId = memLevelInfo.get("memberLevelId").toString();
			if(memLevelId.equals(_memberLevelId)) {
				return (String)memLevelInfo.get("levelName");
			}
		}
		return null;
	}
	
	/**
	 * 查询会员List
	 * 
	 * @param map 查询条件
	 * @return 查询结果
	 */
	public Map<String, Object> searchMemByLuceneList(Map<String, Object> map) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		// 会员搜索条件设定
		this.setCondition(paramMap);
		// 查询会员信息
		Map<String, Object> resultMap = binOLCM27_BL.accessCherryWebService(CherryConstants.WS_MEMINFO, CherryConstants.WS_TYPE_GET, paramMap);
		if(resultMap != null) {
			List<Map<String, Object>> memberInfoList = (List)resultMap.get("list");
			if(memberInfoList != null && !memberInfoList.isEmpty()) {
				// 查询会员等级信息List
				List<Map<String, Object>> memLevelInfoList = binOLCM33_Service.getMemberLevelInfoList(paramMap);
				if(memLevelInfoList != null && !memLevelInfoList.isEmpty()) {
					for(Map<String, Object> memberInfo : memberInfoList) {
						String memLevelId = (String)memberInfo.get("memLevelId");
						if(memLevelId != null && !"".equals(memLevelId)) {
							memberInfo.put("levelName", getMemLevelName(memLevelId, memLevelInfoList));
						}
					}
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 查询会员销售List
	 * 
	 * @param map 查询条件
	 * @return 查询结果
	 */
	public Map<String, Object> searchMemSaleByLuceneList(Map<String, Object> map) {
		
		// 查询会员销售信息
		Map<String, Object> resultMap = binOLCM27_BL.accessCherryWebService(CherryConstants.WS_MEMSALEINFO, CherryConstants.WS_TYPE_GET, map);
		if(resultMap != null) {
			List<Map<String, Object>> memSaleList = (List)resultMap.get("list");
			if(memSaleList != null && !memSaleList.isEmpty()) {
				for(Map<String, Object> memSaleMap : memSaleList) {
					String eventDate = (String)memSaleMap.get("eventDate");
					if(eventDate != null && !"".equals(eventDate)) {
						Date date = new Date(Long.parseLong(eventDate));
						memSaleMap.put("eventDate", DateUtil.date2String(date, "yyyy-MM-dd"));
					}
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 根据key取得相应的名称
	 * 
	 * @param key 指定key
	 * @param list 指定一组数据List
	 * @return 名称
	 */
	public String getNameByKey(String key, List<Map<String, Object>> list) {
		
		if(list != null && !list.isEmpty()) {
			for(Map<String, Object> map : list) {
				String id = map.get("id").toString();
				if(key.equals(id)) {
					return (String)map.get("name");
				}
			}
		}
		return null;
	}
}

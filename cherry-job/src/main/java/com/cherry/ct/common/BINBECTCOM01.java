package com.cherry.ct.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
//import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ct.smg.core.NeedTempSenderFactory;
import com.cherry.ct.smg.interfaces.BINBECTSMG06_IF;
import com.cherry.customize.member.MemberPassword;

public class BINBECTCOM01 {
	
	@Resource
	private BINBECTSMG06_IF binBECTSMG06_IF;
	
	public String replaceTemplate(String template, Map<String, Object> map, List<Map<String, Object>> variableList) throws Exception{
		return replaceTemplate(template, map, variableList, null);
	}
	
	private void addParam(boolean needFlag, Map<String, Object> params, String key, String value) {
		if (needFlag) {
			key = key.replaceAll("<#", "").replaceAll("#>", "");
			params.put(key, value);
		}
	}
	
	// 替换模板变量
	@SuppressWarnings("unchecked")
	public String replaceTemplate(String template, Map<String, Object> map, List<Map<String, Object>> variableList, Map<String, Object> params) throws Exception{
		boolean needParams = null != params;
		// 获取待替换的会员属性
		String memberCode =  ConvertUtil.getString(map.get("memCode"));
		String memberName =  ConvertUtil.getString(map.get("memName"));
		String memberPassword =  ConvertUtil.getString(map.get("memberPassword"));
		String memberSex =  ConvertUtil.getString(map.get("gender"));
		String birthMonth = ConvertUtil.getString(map.get("birthMonth"));
		String birthDay = ConvertUtil.getString(map.get("birthDay"));
		String totalPoint =  ConvertUtil.getString(map.get("totalPoint"));
		String points =  ConvertUtil.getString(map.get("changablePoint"));
		String curDisablePoint =  ConvertUtil.getString(map.get("curDisablePoint"));
		String counterCode =  ConvertUtil.getString(map.get("counterCode"));
		String oldCounterName =  ConvertUtil.getString(map.get("oldCounterName"));
		String counterName =  ConvertUtil.getString(map.get("counterName"));
		String activityName =  ConvertUtil.getString(map.get("campName"));
		String activityBegin =  ConvertUtil.getString(map.get("campFromDate"));
		String activityEnd =  ConvertUtil.getString(map.get("campToDate"));
		String reserveBegin =  ConvertUtil.getString(map.get("orderFromDate"));
		String reserveEnd =  ConvertUtil.getString(map.get("orderToDate"));
		String reCounterName = ConvertUtil.getString(map.get("reCounterName"));
		String getCounterName = ConvertUtil.getString(map.get("getCounterName"));
		String couponCode =  ConvertUtil.getString(map.get("couponCode"));
		String orderID =  ConvertUtil.getString(map.get("orderID"));
		String orderTime =  ConvertUtil.getString(map.get("orderTime"));
		String pointsUsed =  ConvertUtil.getString(map.get("pointsUsed"));
		String pointEndDate = ConvertUtil.getString(map.get("exPointDeadDate"));
		String memberLevel = ConvertUtil.getString(map.get("memberLevel"));
		String nextMemberLevel = ConvertUtil.getString(map.get("nextMemberLevel"));
		String saleDate = ConvertUtil.getString(map.get("saleDate"));
		String changeDate = ConvertUtil.getString(map.get("changeDate"));
		String tradeAmount = ConvertUtil.getString(map.get("tradeAmount"));
		String salePoint = ConvertUtil.getString(map.get("salePoint"));
		String tradePoint = ConvertUtil.getString(map.get("tradePoint"));
		String awardPoint = ConvertUtil.getString(map.get("awardPoint"));
		String upLevelAmount = ConvertUtil.getString(map.get("upLevelAmount"));
		String getToTime = ConvertUtil.getString(map.get("getToTime"));
		String getFromTime = ConvertUtil.getString(map.get("getFromTime"));
		// 推荐人卡号
		String referrerCode = ConvertUtil.getString(map.get("referrerCode"));
		// 推荐人名字
		String referrerName = ConvertUtil.getString(map.get("referrerName"));
		// 获取待替换的用户属性
		String employeeCode = ConvertUtil.getString(map.get("employeeCode"));
		String employeeName = ConvertUtil.getString(map.get("employeeName"));
		String longinName = ConvertUtil.getString(map.get("longinName"));
		String longinPassword = ConvertUtil.getString(map.get("longinPassword"));
		
		// 当前日期
		String nowDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
		// 昨天
		String yesterDay = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, -1);
		// 明天
		String tomorrow = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, nowDate, 1);
		// 当月月初
		String thisMonthBegin = DateUtil.getFirstOrLastDateYMD(nowDate, 0);
		// 下月月初
		String nextMonthBegin = DateUtil.addDateByMonth(CherryBatchConstants.DF_DATE_PATTERN, thisMonthBegin, 1);
		// 当月月底
		String thisMonthEnd = DateUtil.getFirstOrLastDateYMD(nowDate, 1);
		// 下月月底
		String nexMonthEnd = DateUtil.getFirstOrLastDateYMD(nextMonthBegin, 1);
		
		List<Map<String, Object>> campaignPointList = (List<Map<String, Object>>) map.get("campaignPointList");
		String pointDiff = computePointDiff(campaignPointList,totalPoint);
		String memberBirth = "";
		// 判断客户性别，为1时用“先生”替换，为2时用“女士”替换
		if("1".equals(memberSex)){
			memberSex = CherryBatchConstants.GENDER_MAN;
		}else if("2".equals(memberSex)){
			memberSex = CherryBatchConstants.GENDER_WONMAN;
		}
		// 判断柜台名称是否存在，不存在时用柜台号替换
		if("".equals(counterName) || counterName == null){
			counterName = counterCode;
		}
		// 判断预约柜台是否存在，不存在时用当前所属柜台替换
		if("".equals(reCounterName) || reCounterName == null){
			reCounterName = counterName;
		}
		// 判断领取柜台是否存在，不存在时用当前所属柜台替换
		if("".equals(getCounterName) || getCounterName == null){
			getCounterName = counterName;
		}
		if(!"".equals(birthMonth) && !"".equals(birthDay)){
			memberBirth = birthMonth + CherryBatchConstants.STR_MONTH + birthDay + CherryBatchConstants.STR_DAY;
		}
		
		// 替换会员卡号
		if(template.contains(CherryBatchConstants.MEMBER_CODE)){
			if(!"".equals(memberCode)){
				template = template.replaceAll(CherryBatchConstants.MEMBER_CODE, memberCode);
				addParam(needParams, params, CherryBatchConstants.MEMBER_CODE, memberCode);
			}else{
				// 会员卡号为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换会员姓名
		if(template.contains(CherryBatchConstants.MEMBER_NAME)){
			if(!"".equals(memberName)){
				template = template.replaceAll(CherryBatchConstants.MEMBER_NAME, memberName);
				addParam(needParams, params, CherryBatchConstants.MEMBER_NAME, memberName);
			}else{
				// 会员姓名为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换会员密码
		if(template.contains(CherryBatchConstants.MEMBER_PASSWORD)){
			if(!"".equals(memberPassword)){
				template = template.replaceAll(CherryBatchConstants.MEMBER_PASSWORD, memberPassword);
				addParam(needParams, params, CherryBatchConstants.MEMBER_PASSWORD, memberPassword);
			}else{
				// 会员密码为空的情况下返回模板替换结果为NMP，表示模板替换失败，没有获取到可以替换的密码
				return "";
			}
		}
		// 替换会员性别
		if(template.contains(CherryBatchConstants.MEMBER_SEX)){
//			if(!"".equals(memberSex)){
				template = template.replaceAll(CherryBatchConstants.MEMBER_SEX, memberSex);
				addParam(needParams, params, CherryBatchConstants.MEMBER_SEX, memberSex);
//			}
		}
		// 替换参考会员单据领取起始时间
		if(template.contains(CherryBatchConstants.MEMBER_GETFROMTIME)){
//			if(!"".equals(getFromTime)){
				template = template.replaceAll(CherryBatchConstants.MEMBER_GETFROMTIME, getFromTime);
				addParam(needParams, params, CherryBatchConstants.MEMBER_GETFROMTIME, getFromTime);
//			}
		}
		// 替换参考会员单据领取截止时间
		if(template.contains(CherryBatchConstants.MEMBER_GETTOTIME)){
//			if(!"".equals(getToTime)){
				template = template.replaceAll(CherryBatchConstants.MEMBER_GETTOTIME, getToTime);
				addParam(needParams, params, CherryBatchConstants.MEMBER_GETTOTIME, getToTime);
//			}
		}
		// 替换会员生日
		if(template.contains(CherryBatchConstants.BIRTHDAY)){
			if(!"".equals(memberBirth)){
				template = template.replaceAll(CherryBatchConstants.BIRTHDAY, memberBirth);
				addParam(needParams, params, CherryBatchConstants.BIRTHDAY, memberBirth);
			}else{
				return "";
			}
		}		
		// 替换会员当前积分
		if(template.contains(CherryBatchConstants.TOTAL_POINT)){
			if(!"".equals(totalPoint)){
				totalPoint = convertDoubleStrToIntStr(totalPoint, 0);
				template = template.replaceAll(CherryBatchConstants.TOTAL_POINT, totalPoint);
				addParam(needParams, params, CherryBatchConstants.TOTAL_POINT, totalPoint);
			}else{
				// 会员当前积分为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.TOTAL_POINT, "0");
				addParam(needParams, params, CherryBatchConstants.TOTAL_POINT, "0");
			}
		}
		// 替换会员可兑换积分
		if(template.contains(CherryBatchConstants.CHANGABLE_POINT)){
			if(!"".equals(points)){
				points = convertDoubleStrToIntStr(points, 0);
				template = template.replaceAll(CherryBatchConstants.CHANGABLE_POINT, points);
				addParam(needParams, params, CherryBatchConstants.CHANGABLE_POINT, points);
			}else{
				// 会员可兑换积分为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.CHANGABLE_POINT, "0");
				addParam(needParams, params, CherryBatchConstants.CHANGABLE_POINT, "0");
			}
		}
		// 替换会员即将失效积分
		if(template.contains(CherryBatchConstants.CURDISABLE_POINT)){
			if(!"".equals(curDisablePoint)){
				curDisablePoint = convertDoubleStrToIntStr(curDisablePoint, 0);
				template = template.replaceAll(CherryBatchConstants.CURDISABLE_POINT, curDisablePoint);
				addParam(needParams, params, CherryBatchConstants.CURDISABLE_POINT, curDisablePoint);
			}else{
				// 会员即将失效积分为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.CURDISABLE_POINT, "0");
				addParam(needParams, params, CherryBatchConstants.CURDISABLE_POINT, "0");
			}
		}
		// 替换转柜前柜台
		if(template.contains(CherryBatchConstants.OLDCOUNTER_NAME)){
			if(!"".equals(oldCounterName)){
				template = template.replaceAll(CherryBatchConstants.OLDCOUNTER_NAME, oldCounterName);
				addParam(needParams, params, CherryBatchConstants.OLDCOUNTER_NAME, oldCounterName);
			}else{
				// 转柜前柜台为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换会员当前所属柜台
		if(template.contains(CherryBatchConstants.COUNTER_NAME)){
			if(!"".equals(counterName)){
				template = template.replaceAll(CherryBatchConstants.COUNTER_NAME, counterName);
				addParam(needParams, params, CherryBatchConstants.COUNTER_NAME, counterName);
			}else{
				// 当前所属柜台为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动名称
		if(template.contains(CherryBatchConstants.ACTIVITY_NAME)){
			if(!"".equals(activityName)){
				template = template.replaceAll(CherryBatchConstants.ACTIVITY_NAME, activityName);
				addParam(needParams, params, CherryBatchConstants.ACTIVITY_NAME, activityName);
			}else{
				// 活动名称为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动开始时间
		if(template.contains(CherryBatchConstants.ACTIVITY_DATA_BEGIN)){
			if(!"".equals(activityBegin)){
				template = template.replaceAll(CherryBatchConstants.ACTIVITY_DATA_BEGIN, activityBegin);
				addParam(needParams, params, CherryBatchConstants.ACTIVITY_DATA_BEGIN, activityBegin);
			}else{
				// 活动开始时间为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动截止时间
		if(template.contains(CherryBatchConstants.ACTIVITY_DATA_END)){
			if(!"".equals(activityEnd)){
				template = template.replaceAll(CherryBatchConstants.ACTIVITY_DATA_END, activityEnd);
				addParam(needParams, params, CherryBatchConstants.ACTIVITY_DATA_END, activityEnd);
			}else{
				// 活动截止时间为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动预约开始时间
		if(template.contains(CherryBatchConstants.RESERVE_DATA_BEGIN)){
			if(!"".equals(reserveBegin)){
				template = template.replaceAll(CherryBatchConstants.RESERVE_DATA_BEGIN, reserveBegin);
				addParam(needParams, params, CherryBatchConstants.RESERVE_DATA_BEGIN, reserveBegin);
			}else{
				// 活动预约开始时间为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动预约截止时间
		if(template.contains(CherryBatchConstants.RESERVE_DATA_END)){
			if(!"".equals(reserveEnd)){
				template = template.replaceAll(CherryBatchConstants.RESERVE_DATA_END, reserveEnd);
				addParam(needParams, params, CherryBatchConstants.RESERVE_DATA_END, reserveEnd);
			}else{
				// 活动预约截止时间为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换会员预约活动柜台
		if(template.contains(CherryBatchConstants.RECOUNTER_NAME)){
			if(!"".equals(reCounterName)){
				template = template.replaceAll(CherryBatchConstants.RECOUNTER_NAME, reCounterName);
				addParam(needParams, params, CherryBatchConstants.RECOUNTER_NAME, reCounterName);
			}else{
				// 会员预约活动柜台为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换会员礼品领取柜台
		if(template.contains(CherryBatchConstants.GETCOUNTER_NAME)){
			if(!"".equals(getCounterName)){
				template = template.replaceAll(CherryBatchConstants.GETCOUNTER_NAME, getCounterName);
				addParam(needParams, params, CherryBatchConstants.GETCOUNTER_NAME, getCounterName);
			}else{
				// 会员礼品领取柜台为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动验证号
		if(template.contains(CherryBatchConstants.COUPON_CODE)){
			if(!"".equals(couponCode)){
				template = template.replaceAll(CherryBatchConstants.COUPON_CODE, couponCode);
				addParam(needParams, params, CherryBatchConstants.COUPON_CODE, couponCode);
			}else{
				// 活动验证号为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动参与单据号
		if(template.contains(CherryBatchConstants.ORDER_ID)){
			if(!"".equals(orderID)){
				template = template.replaceAll(CherryBatchConstants.ORDER_ID, orderID);
				addParam(needParams, params, CherryBatchConstants.ORDER_ID, orderID);
			}else{
				// 活动参与单据号为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换会员参与活动时间
		if(template.contains(CherryBatchConstants.ORDER_TIME)){
			if(!"".equals(orderTime)){
				template = template.replaceAll(CherryBatchConstants.ORDER_TIME, orderTime);
				addParam(needParams, params, CherryBatchConstants.ORDER_TIME, orderTime);
			}else{
				// 会员参与活动时间为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换活动使用积分
		if(template.contains(CherryBatchConstants.POINTS_USED)){
			if(!"".equals(pointsUsed)){
				pointsUsed = convertDoubleStrToIntStr(pointsUsed, 0);
				template = template.replaceAll(CherryBatchConstants.POINTS_USED, pointsUsed);
				addParam(needParams, params, CherryBatchConstants.POINTS_USED, pointsUsed);
			}else{
				// 活动使用积分为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.POINTS_USED, "0");
				addParam(needParams, params, CherryBatchConstants.POINTS_USED, "0");
			}	
		}
		// 替换参与活动差额积分
		if(template.contains(CherryBatchConstants.POINTS_DIFF)){
			if(!"".equals(pointDiff)){
				pointDiff = convertDoubleStrToIntStr(pointDiff, 0);
				template = template.replaceAll(CherryBatchConstants.POINTS_DIFF, pointDiff);
				addParam(needParams, params, CherryBatchConstants.POINTS_DIFF, pointDiff);
			}else{
				// 参与活动差额积分为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.POINTS_DIFF, "0");
				addParam(needParams, params, CherryBatchConstants.POINTS_DIFF, "0");
			}
		}
		// 替换会员可兑换积分截止时间
		if(template.contains(CherryBatchConstants.POINTS_ENDDATE)){
			if(!"".equals(pointEndDate)){
				template = template.replaceAll(CherryBatchConstants.POINTS_ENDDATE, pointEndDate);
				addParam(needParams, params, CherryBatchConstants.POINTS_ENDDATE, pointEndDate);
			}else{
				// 可兑换积分截止时间为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换会员姓氏
		if(template.contains(CherryBatchConstants.FIRSTNAME)){
			if(!"".equals(memberName)){
				String firstName = getFirstName(memberName);
				template = template.replaceAll(CherryBatchConstants.FIRSTNAME, firstName);
				addParam(needParams, params, CherryBatchConstants.FIRSTNAME, firstName);
			}else{
				// 会员姓名为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}	
		}
		// 替换会员当前等级
		if(template.contains(CherryBatchConstants.MEMBERLEVEL)){
			if(!"".equals(memberLevel)){
				template = template.replaceAll(CherryBatchConstants.MEMBERLEVEL, memberLevel);
				addParam(needParams, params, CherryBatchConstants.MEMBERLEVEL, memberLevel);
			}else{
				// 会员当前等级为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}	
		}
		// 替换会员下一等级
		if(template.contains(CherryBatchConstants.NEXTMEMBERLEVEL)){
			if(!"".equals(nextMemberLevel)){
				template = template.replaceAll(CherryBatchConstants.NEXTMEMBERLEVEL, nextMemberLevel);
				addParam(needParams, params, CherryBatchConstants.NEXTMEMBERLEVEL, nextMemberLevel);
			}else{
				// 会员下一等级为空的情况下替换成会员当前等级
				if(!"".equals(memberLevel)){
					template = template.replaceAll(CherryBatchConstants.NEXTMEMBERLEVEL, memberLevel);
					addParam(needParams, params, CherryBatchConstants.NEXTMEMBERLEVEL, memberLevel);
				}else{
					// 会员当前等级也为空的情况下返回模板替换结果为空，表示模板替换失败
					return "";
				}
			}
		}
		// 替换交易时间
		if(template.contains(CherryBatchConstants.SALEDATE)){
			if(!"".equals(saleDate)){
				template = template.replaceAll(CherryBatchConstants.SALEDATE, saleDate);
				addParam(needParams, params, CherryBatchConstants.SALEDATE, saleDate);
			}else{
				// 交易时间为空的情况下替换成积分变化时间
				if(!"".equals(changeDate)){
					changeDate = DateUtil.coverTime2YMD(changeDate, CherryBatchConstants.DATE_PATTERN);
					template = template.replaceAll(CherryBatchConstants.SALEDATE, changeDate);
					addParam(needParams, params, CherryBatchConstants.SALEDATE, changeDate);
				}else{
					// 积分变化时间也为空的情况下返回模板替换结果为空，表示模板替换失败
					return "";
				}
			}			
		}
		// 替换积分变化时间
		if(template.contains(CherryBatchConstants.POINT_CHANGEDATE)){
			if(!"".equals(changeDate)){
				changeDate = DateUtil.coverTime2YMD(changeDate, CherryBatchConstants.DATE_PATTERN);
				template = template.replaceAll(CherryBatchConstants.POINT_CHANGEDATE, changeDate);
				addParam(needParams, params, CherryBatchConstants.POINT_CHANGEDATE, changeDate);
			}else{
				// 积分变化时间为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换单笔交易金额
		if(template.contains(CherryBatchConstants.TRADEAMOUNT)){
			if(!"".equals(tradeAmount)){
				tradeAmount = convertDoubleStrToIntStr(tradeAmount, 1);
				template = template.replaceAll(CherryBatchConstants.TRADEAMOUNT, tradeAmount);
				addParam(needParams, params, CherryBatchConstants.TRADEAMOUNT, tradeAmount);
			}else{
				// 单笔交易金额为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.TRADEAMOUNT, "0");
				addParam(needParams, params, CherryBatchConstants.TRADEAMOUNT, "0");
			}
		}
		// 替换单笔交易积分
		if(template.contains(CherryBatchConstants.SALEPOINT)){
			if(!"".equals(salePoint)){
				salePoint = convertDoubleStrToIntStr(salePoint, 0);
				template = template.replaceAll(CherryBatchConstants.SALEPOINT, salePoint);
				addParam(needParams, params, CherryBatchConstants.SALEPOINT, salePoint);
			}else{
				// 单笔交易积分为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.SALEPOINT, "0");
				addParam(needParams, params, CherryBatchConstants.SALEPOINT, "0");
			}
		}
		// 替换单次积分变化值
		if(template.contains(CherryBatchConstants.TRADEPOINT)){
			if(!"".equals(tradePoint)){
				tradePoint = convertDoubleStrToIntStr(tradePoint, 0);
				template = template.replaceAll(CherryBatchConstants.TRADEPOINT, tradePoint);
				addParam(needParams, params, CherryBatchConstants.TRADEPOINT, tradePoint);
			}else{
				// 单次积分变化值为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.TRADEPOINT, "0");
				addParam(needParams, params, CherryBatchConstants.TRADEPOINT, "0");
			}
		}
		// 替换奖励积分
		if(template.contains(CherryBatchConstants.AWARDPOINT)){
			if(!"".equals(awardPoint)){
				awardPoint = convertDoubleStrToIntStr(awardPoint, 0);
				template = template.replaceAll(CherryBatchConstants.AWARDPOINT, awardPoint);
				addParam(needParams, params, CherryBatchConstants.AWARDPOINT, awardPoint);
			}else{
				// 奖励积分为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.AWARDPOINT, "0");
				addParam(needParams, params, CherryBatchConstants.AWARDPOINT, "0");
			}
		}
		// 替换升级差额购买金额
		if(template.contains(CherryBatchConstants.UPLEVEL_AMOUNT)){
			if(!"".equals(upLevelAmount)){
				upLevelAmount = convertDoubleStrToIntStr(upLevelAmount, 1);
				template = template.replaceAll(CherryBatchConstants.UPLEVEL_AMOUNT, upLevelAmount);
				addParam(needParams, params, CherryBatchConstants.UPLEVEL_AMOUNT, upLevelAmount);
			}else{
				// 升级差额购买金额为空的情况下替换为0
				template = template.replaceAll(CherryBatchConstants.UPLEVEL_AMOUNT, "0");
				addParam(needParams, params, CherryBatchConstants.UPLEVEL_AMOUNT, "0");
			}
		}
		// 替换“当月”变量
		if(template.contains(CherryBatchConstants.THISMONTH)){
			String curMonth = ConvertUtil.getString(ConvertUtil.getInt(getNowYearAndMonth("MM")));
			template = template.replaceAll(CherryBatchConstants.THISMONTH, 
					curMonth);
			addParam(needParams, params, CherryBatchConstants.THISMONTH, curMonth);
		}
		// 替换“上月”变量
		if(template.contains(CherryBatchConstants.LASTMONTH)){
			String lastMonth = ConvertUtil.getString(getLastNumMonth(1));
			template = template.replaceAll(CherryBatchConstants.LASTMONTH, lastMonth);
			addParam(needParams, params, CherryBatchConstants.LASTMONTH, lastMonth);
		}
		// 替换“下月”变量
		if(template.contains(CherryBatchConstants.NEXTMONTH)){
			String nextMonth = ConvertUtil.getString(getNextNumMonth(1));
			template = template.replaceAll(CherryBatchConstants.NEXTMONTH, nextMonth);
			addParam(needParams, params, CherryBatchConstants.NEXTMONTH, nextMonth);
		}
		// 替换“昨天”变量
		if(template.contains(CherryBatchConstants.YESTERDAY)){
			template = template.replaceAll(CherryBatchConstants.YESTERDAY, yesterDay);
			addParam(needParams, params, CherryBatchConstants.YESTERDAY, yesterDay);
		}
		// 替换“今天”变量
		if(template.contains(CherryBatchConstants.TODAY)){
			template = template.replaceAll(CherryBatchConstants.TODAY, nowDate);
			addParam(needParams, params, CherryBatchConstants.TODAY, nowDate);
		}		
		// 替换“明天”变量
		if(template.contains(CherryBatchConstants.TOMORROW)){
			template = template.replaceAll(CherryBatchConstants.TOMORROW, tomorrow);
			addParam(needParams, params, CherryBatchConstants.TOMORROW, tomorrow);
		}		
		// 替换“当月月底”变量
		if(template.contains(CherryBatchConstants.THISMONTH_ENDDATE)){
			template = template.replaceAll(CherryBatchConstants.THISMONTH_ENDDATE, thisMonthEnd);
			addParam(needParams, params, CherryBatchConstants.THISMONTH_ENDDATE, thisMonthEnd);
		}		
		// 替换“下月月底”变量
		if(template.contains(CherryBatchConstants.NEXTMONTH_ENDDATE)){
			template = template.replaceAll(CherryBatchConstants.NEXTMONTH_ENDDATE, nexMonthEnd);
			addParam(needParams, params, CherryBatchConstants.NEXTMONTH_ENDDATE, nexMonthEnd);
		}		
		// 替换“下月月初”变量
		if(template.contains(CherryBatchConstants.NEXTMONTH_BEGINDATE)){
			template = template.replaceAll(CherryBatchConstants.NEXTMONTH_BEGINDATE, nextMonthBegin);
			addParam(needParams, params, CherryBatchConstants.NEXTMONTH_BEGINDATE, nextMonthBegin);
		}
		// 替换参与计算的变量
		for (Map<String, Object> variableMap : variableList) {
			String variableType = ConvertUtil.getString(variableMap.get("variableType"));
			String variableValue = ConvertUtil.getString(variableMap.get("variableValue"));
			if("1".equals(variableType) || "3".equals(variableType)){
				if("3".equals(variableType)){
					map.put("today", nowDate);
					map.put("thisMonthEnd", thisMonthEnd);
					map.put("nexMonthEnd", nexMonthEnd);
					map.put("nextMonthBegin", nextMonthBegin);
				}
				if(template.contains(variableValue)){
					String newAvailable = computedVariableValue(variableMap, map);
					if(!"".equals(newAvailable)){
						template = template.replaceAll(variableValue, newAvailable);
						addParam(needParams, params, variableValue, newAvailable);
					}else{
						template = "";
					}
				}
			}
		}
		
		// 替换员工号
		if(template.contains(CherryBatchConstants.EMPLOYEE_CODE)){
			if(!"".equals(employeeCode)){
				template = template.replaceAll(CherryBatchConstants.EMPLOYEE_CODE, employeeCode);
				addParam(needParams, params, CherryBatchConstants.EMPLOYEE_CODE, employeeCode);
			}else{
				// 员工号为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换员工姓名
		if(template.contains(CherryBatchConstants.EMPLOYEE_NAME)){
			if(!"".equals(employeeName)){
				template = template.replaceAll(CherryBatchConstants.EMPLOYEE_NAME, employeeName);
				addParam(needParams, params, CherryBatchConstants.EMPLOYEE_NAME, employeeName);
			}else{
				// 员工姓名为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换登陆账号
		if(template.contains(CherryBatchConstants.LONGIN_NAME)){
			if(!"".equals(longinName)){
				template = template.replaceAll(CherryBatchConstants.LONGIN_NAME, longinName);
				addParam(needParams, params, CherryBatchConstants.LONGIN_NAME, longinName);
			}else{
				// 登陆用户名为空的情况下返回模板替换结果为空，表示模板替换失败
				return "";
			}
		}
		// 替换登陆密码
		if(template.contains(CherryBatchConstants.LONGIN_PASSWORD)){
			if(!"".equals(longinPassword)){
				template = template.replaceAll(CherryBatchConstants.LONGIN_PASSWORD, longinPassword);
				addParam(needParams, params, CherryBatchConstants.LONGIN_PASSWORD, longinPassword);
			}else{
				// 登陆密码为空的情况下返回模板替换结果为NUP，表示模板替换失败，没有找到可以替换的登陆密码
				return "";
			}
		}
		// 推荐人姓名
		if(template.contains(CherryBatchConstants.REFERRER_CODE)){
			if (!"".equals(referrerCode)){
				template = template.replaceAll(CherryBatchConstants.REFERRER_CODE,referrerCode);
				addParam(needParams, params, CherryBatchConstants.REFERRER_CODE, referrerCode);
			}else {
				// 推荐人姓名空的情况下返回模板替换结果为NUP，表示模板替换失败，没有找到可以替换的推荐人姓名
				return "";
			}
		}
		// 推荐人名字
		if(template.contains(CherryBatchConstants.REFERRER_NAME)){
			if (!"".equals(referrerName)){
				template = template.replaceAll(CherryBatchConstants.REFERRER_NAME,referrerName);
				addParam(needParams, params, CherryBatchConstants.REFERRER_NAME, referrerName);
			}else {
				// 推荐人卡号空的情况下返回模板替换结果为NUP，表示模板替换失败，没有找到可以替换的推荐人卡号
				return "";
			}
		}
		return template;
	}
	
	// 计算扩展变量的值，以积分为基础变量扩展的情况下如果计算参数缺失则默认返回为0，以日期为基础变量扩展的情况下计算参数缺失返回值为当前日期
	public String computedVariableValue(Map<String, Object> variableMap, Map<String, Object> map){
		String variableType = ConvertUtil.getString(variableMap.get("variableType"));
		String variableValue = ConvertUtil.getString(variableMap.get("variableValue"));
		String basicVariable = ConvertUtil.getString(variableMap.get("basicVariable"));
		String operatorChar = ConvertUtil.getString(variableMap.get("operatorChar"));
		String computedValue = ConvertUtil.getString(variableMap.get("computedValue"));
		if(CherryChecker.isNullOrEmpty(computedValue)){
			computedValue = "0.00";
		}
		String returnValue = "";
		if("1".equals(variableType)){
			// 处理参与积分计算的变量
			float basicValue = 0;
			double resultValue = 0;
			if("TP".equals(basicVariable)){
				String totalPoint =  ConvertUtil.getString(map.get("totalPoint"));
				if(!"".equals(totalPoint)){
					basicValue = ConvertUtil.getFloat(totalPoint);
				}
			}else if("CP".equals(basicVariable)){
				String points =  ConvertUtil.getString(map.get("changablePoint"));
				if(!"".equals(points)){
					basicValue = ConvertUtil.getFloat(points);
				}
			}else if("PU".equals(basicVariable)){
				String pointsUsed =  ConvertUtil.getString(map.get("pointsUsed"));
				if(!"".equals(pointsUsed)){
					basicValue = ConvertUtil.getFloat(pointsUsed);
				}
			}else if("DP".equals(basicVariable)){
				String curDisablePoint =  ConvertUtil.getString(map.get("curDisablePoint"));
				if(!"".equals(curDisablePoint)){
					basicValue = ConvertUtil.getFloat(curDisablePoint);
				}
			}else if("TA".equals(basicVariable)){
				String tradeAmount = ConvertUtil.getString(map.get("tradeAmount"));
				if(!"".equals(tradeAmount)){
					basicValue = ConvertUtil.getFloat(tradeAmount);
				}
			}else if("SP".equals(basicVariable)){
				String salePoint = ConvertUtil.getString(map.get("salePoint"));
				if(!"".equals(salePoint)){
					basicValue = ConvertUtil.getFloat(salePoint);
				}
			}else if("OP".equals(basicVariable)){
				String tradePoint = ConvertUtil.getString(map.get("tradePoint"));
				if(!"".equals(tradePoint)){
					basicValue = ConvertUtil.getFloat(tradePoint);
				}
			}else if("AP".equals(basicVariable)){
				String awardPoint = ConvertUtil.getString(map.get("awardPoint"));
				if(!"".equals(awardPoint)){
					basicValue = ConvertUtil.getFloat(awardPoint);
				}
			}else if("UP".equals(basicVariable)){
				String upLevelAmount = ConvertUtil.getString(map.get("upLevelAmount"));
				if(!"".equals(upLevelAmount)){
					basicValue = ConvertUtil.getFloat(upLevelAmount);
				}
			}
			// 根据基础变量的值进行加、减、乘、除计算
			if(!CherryChecker.isNullOrEmpty(operatorChar)){
				if("1".equals(operatorChar)){
					resultValue = basicValue + ConvertUtil.getFloat(computedValue);
				}else if("2".equals(operatorChar)){
					resultValue = basicValue - ConvertUtil.getFloat(computedValue);
				}else if("3".equals(operatorChar)){
					resultValue = basicValue * ConvertUtil.getFloat(computedValue);
				}else if("4".equals(operatorChar)){
					if("0.00".equals(computedValue)){
						resultValue = basicValue;
					}else{
						resultValue = basicValue / ConvertUtil.getFloat(computedValue);
					}
				}
				// 将计算结果转换成保留一位小数的字符串类型
				returnValue = convertDoubleStrToIntStr(ConvertUtil.getString(resultValue), 1);
			}
		}else if("3".equals(variableType)){
			// 处理参与日期计算的变量
			String basicValue = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
			String resultValue = "";
			int dateValue = ConvertUtil.getInt(convertDoubleStrToIntStr(computedValue, 0));
			if("AB".equals(basicVariable)){
				String activityBegin =  ConvertUtil.getString(map.get("campFromDate"));
				if(!"".equals(activityBegin)){
					basicValue = activityBegin;
				}
			}else if("AE".equals(basicVariable)){
				String activityEnd =  ConvertUtil.getString(map.get("campToDate"));
				if(!"".equals(activityEnd)){
					basicValue = activityEnd;
				}
			}else if("RB".equals(basicVariable)){
				String reserveBegin =  ConvertUtil.getString(map.get("orderFromDate"));
				if(!"".equals(reserveBegin)){
					basicValue = reserveBegin;
				}
			}else if("RE".equals(basicVariable)){
				String reserveEnd =  ConvertUtil.getString(map.get("orderToDate"));
				if(!"".equals(reserveEnd)){
					basicValue = reserveEnd;
				}
			}else if("OT".equals(basicVariable)){
				String orderTime =  ConvertUtil.getString(map.get("orderTime"));
				if(!"".equals(orderTime)){
					basicValue = orderTime;
				}
			}else if("PE".equals(basicVariable)){
				String pointEndDate =  ConvertUtil.getString(map.get("exPointDeadDate"));
				if(!"".equals(pointEndDate)){
					basicValue = pointEndDate;
				}
			}else if("TD".equals(basicVariable)){
				String today =  ConvertUtil.getString(map.get("today"));
				if(!"".equals(today)){
					basicValue = today;
				}
			}else if("TL".equals(basicVariable)){
				String thisMonthEnd =  ConvertUtil.getString(map.get("thisMonthEnd"));
				if(!"".equals(thisMonthEnd)){
					basicValue = thisMonthEnd;
				}
			}else if("ND".equals(basicVariable)){
				String nexMonthEnd =  ConvertUtil.getString(map.get("nexMonthEnd"));
				if(!"".equals(nexMonthEnd)){
					basicValue = nexMonthEnd;
				}
			}else if("NB".equals(basicVariable)){
				String nextMonthBegin =  ConvertUtil.getString(map.get("nextMonthBegin"));
				if(!"".equals(nextMonthBegin)){
					basicValue = nextMonthBegin;
				}
			}
			// 根据基础变量计算提前和推后日期
			if("1".equals(operatorChar)){
				// 计算提前日期
				if(variableValue.equals(CherryBatchConstants.MESSAGE_INVALIDDATE) || variableValue.equals(CherryBatchConstants.MSGVALID_ENDDATE)){
					resultValue = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, basicValue, -dateValue);
				}else{
					resultValue = "";
				}
			}else if("2".equals(operatorChar)){
				// 计算推后日期
				if(variableValue.equals(CherryBatchConstants.NEXT_X_MONTH_BEGINDATE)){
					// 后x月月初的情况下加上指定月然后取月初日期
					resultValue = DateUtil.addDateByMonth(CherryBatchConstants.DF_DATE_PATTERN, basicValue, dateValue);
					resultValue = DateUtil.getFirstOrLastDateYMD(resultValue, 0);
				}else if(variableValue.equals(CherryBatchConstants.NEXT_X_MONTH_LASTDATE)){
					// 后x月月底的情况下加上指定月然后取月底日期
					resultValue = DateUtil.addDateByMonth(CherryBatchConstants.DF_DATE_PATTERN, basicValue, dateValue);
					resultValue = DateUtil.getFirstOrLastDateYMD(resultValue, 1);
				}else{
					resultValue = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, basicValue, dateValue);
				}
			}else{
				// 操作符号为空的情况下默认返回基础变量日期推后指定日期
				resultValue = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, basicValue, dateValue);
			}
			returnValue = resultValue;
		}
		return returnValue;
	}
	
	// 计算积分差额
	/*
	 * 接收参数：按从小到大顺序排序的积分档次数组，会员积分值
	 * 返回值：会员积分离最近档次的差值
	 */
	public String computePointDiff(List<Map<String, Object>> pointList,String point){
		String diffPoint = "";
		if(null != pointList && !pointList.isEmpty()){
			if(null==point || "".equals(point)){
				point = "0";
			}
			for (Map<String, Object> pointMap : pointList) {
				String exPoint = ConvertUtil.getString(pointMap.get("exPoint"));
				point = convertDoubleStrToIntStr(point, 0);
				exPoint = convertDoubleStrToIntStr(exPoint, 0);
				if(ConvertUtil.getInt(point) < ConvertUtil.getInt(exPoint)){
					diffPoint = ConvertUtil.getString(ConvertUtil.getInt(exPoint) - ConvertUtil.getInt(point));
					break;
				}
			}
		}else{
			diffPoint = "0";
		}
		return diffPoint;
	}
	
	// 获取客户姓氏，当客户为复姓时根据百家姓复姓进行判断返回复姓
	public String getFirstName(String fullName){
		String firstName = "";
		boolean isDoubleFirstName = false;
		if(null!=fullName && fullName!=""){
			// 获取预定义好的百家姓复姓列表
			String doubleFirstName = CherryBatchConstants.DOUBLEFIRSTNAME;
			String[] doubleFirstNameList = doubleFirstName.split(",");
			firstName = fullName.substring(0,2);
			for (String doubleFsName : doubleFirstNameList) {
				if(firstName.equals(doubleFsName)){
					isDoubleFirstName = true;
					break;
				}
			}
			if(!isDoubleFirstName){
				firstName = fullName.substring(0,1);
			}
		}
		return firstName;
	}
	
	// 获取当前年、当前月或者当前年月
	// 传入参数YY、MM或YM
	// 输出格式YYYY、MM或者YYYY-MM,默认返回YYYY-MM
	public String getNowYearAndMonth(String param){
		try{
			String nowDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
			String returnValue = "";
			if("YY".equals(param)){
				returnValue = nowDate.substring(0,4);
			}else if("MM".equals(param)){
				returnValue = nowDate.substring(5,7);
			}else if("YM".equals(param)){
				returnValue = nowDate.substring(0,7);
			}else{
				returnValue = nowDate.substring(0,7);
			}
			return returnValue;
		}catch(Exception ex){
			return "1900-01";
		}
	}
	
	
	/*
	 * 获取当前月的前N月
	 * param: 正整数
	 * return: 目标月
	 */
	public int getLastNumMonth(int num){
		if(num > 0){
			if(num > 12){
				num = num%12;
			}
			String thisMonth = getNowYearAndMonth("MM");
			int lastMonth = ConvertUtil.getInt(thisMonth)-num;
			if(lastMonth < 1){
				lastMonth = (ConvertUtil.getInt(thisMonth) + 12) - num;
			}
			return lastMonth;
		}else{
			num = Math.abs(num);
			int lastMonth = getNextNumMonth(num);
			return lastMonth;
		}
	}
	
	/*
	 * 获取当前月的后N月
	 * param: 正整数
	 * return: 目标月
	 */
	public int getNextNumMonth(int num){
		if(num > 0){
			if(num > 12){
				num = num%12;
			}
			String thisMonth = getNowYearAndMonth("MM");
			int nextMonth = ConvertUtil.getInt(thisMonth)+num;
			if(nextMonth > 12){
				nextMonth = (ConvertUtil.getInt(thisMonth) + num) - 12;
			}
			return nextMonth;
		}else{
			num = Math.abs(num);
			int nextMonth = getLastNumMonth(num);
			return nextMonth;
		}		
	}
	
	public boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	// 比较第一个时间是否在第二个时间之前
	public boolean dateBefore(String value1, String value2, String pattern){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Date date1 = sdf.parse(value1);
			Date date2 = sdf.parse(value2);
			int result = date1.compareTo(date2);
			if(result <= 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	// 将Double格式保留两位小数的字符串转换成保留指定位小数的字符串
	public String convertDoubleStrToIntStr(String doubleStr, int value){
		if("".equals(doubleStr)){
			return "0";
		}
		try{
			BigDecimal decimalValue = new BigDecimal(doubleStr).setScale(value, BigDecimal.ROUND_DOWN);
			String intStr = ConvertUtil.getString(decimalValue);
			return intStr;
		}catch(Exception e){
			return doubleStr;
		}
	}
	
	public List<Map<String, Object>> getReceiverCode(String strValue, Map<String, Object> map) throws Exception{
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String mobileRule = ConvertUtil.getString(map.get("mobileRule"));
		// 定义号码列表用于存放解析出来的号码
		List<Map<String, Object>> codeList = new ArrayList<Map<String,Object>>();
		strValue = strValue.replace("；", ";");
		strValue = strValue.replace("，", ";");
		strValue = strValue.replace(",", ";");
		// 解析号码字符串放入数组
		String[] reCodes = strValue.split(";");
		for (String reCode : reCodes) {
			// 判断取到的号码是否为空
			if(!"".equals(reCode)){
				// 定义是否可以获取到会员的标识
				boolean getMemberFlag = true;
				String memInfoId = "";
				String receiveCode = "";
				// 对于会员ID和接收号码同时提供的事件进行号码解析
				String[] codes = reCode.split("#");
				if(codes.length > 1){
					// 判断解析结果，将解析结果保存到变量中
					if(!"".equals(codes[0]) && !"".equals(codes[1])){
						memInfoId = codes[0];
						receiveCode = codes[1];
					}else if(!"".equals(codes[0]) && "".equals(codes[1])){
						memInfoId = codes[0];
						receiveCode = codes[0];
					}else if("".equals(codes[0]) && !"".equals(codes[1])){
						memInfoId = codes[1];
						receiveCode = codes[1];
					}else{
						break;
					}
				}else{
					memInfoId = codes[0];
					receiveCode = codes[0];
				}
				// 根据解析的会员ID查找会员信息是否存在
				if("10".equals(eventType) || "11".equals(eventType) || "13".equals(eventType) || "14".equals(eventType)){
					if(CherryChecker.isNumeric(memInfoId)){
						if(memInfoId.length() < 10){
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("brandInfoId", brandInfoId);
							paramMap.put("organizationInfoId", organizationInfoId);
							paramMap.put("memberInfoId", memInfoId);
							// 根据会员ID获取会员信息
							Map<String, Object> memberMap = new HashMap<String, Object>();
							memberMap = binBECTSMG06_IF.getMemberInfoById(paramMap);
							// 判断是否获取到了会员信息，若获取到了则返回获取到的会员信息
							if(memberMap != null && !memberMap.isEmpty()){
								if(CherryChecker.isPhoneValid(receiveCode, mobileRule)){
									memberMap.put("mobilePhone", receiveCode);
								}
								codeList.add(memberMap);
								getMemberFlag = true;
							}else{
								getMemberFlag = false;
							}
						}else{
							getMemberFlag = false;
						}
					}else{
						getMemberFlag = false;
					}
				}else{
					getMemberFlag = false;
				}
				Map<String, Object> codeMap = new HashMap<String, Object>();
				// 判断是否获取到了会员信息，若未获取到会员信息则由系统组织客户信息
				if(getMemberFlag == false){
					// 判断接收号码是手机号码还是邮箱地址，根据号码类型组织客户信息
					if(CherryChecker.isPhoneValid(receiveCode, mobileRule)){
						codeMap.put("memId", memInfoId);
						codeMap.put("mobilePhone", receiveCode);
						codeMap.put("email", "");
						codeMap.put("receiveMsgFlg", "1");
						codeMap.put("customerType", "4");
						codeList.add(codeMap);
					}else if(CherryChecker.isEmail(receiveCode)){
						String memId = receiveCode.substring(0,reCode.indexOf("@"));
						codeMap.put("memId", memId);
						codeMap.put("mobilePhone", "");
						codeMap.put("email", receiveCode);
						codeMap.put("receiveMsgFlg", "1");
						codeMap.put("customerType", "4");
						codeList.add(codeMap);
					}
				}
			}
		}
		return codeList;
	}
	
	// 去除时间格式字符串的符号，仅保留数字
	public String getTimeNumString(String timeValue){
		if("".equals(timeValue)){
			return timeValue;
		}
		try{
			timeValue = timeValue.replace("-", "");
			timeValue = timeValue.replace(":", "");
			timeValue = timeValue.replace(".", "");
			timeValue = timeValue.replace(" ", "");
			return timeValue;
		}catch(Exception e){
			return timeValue;
		}
	}
	
	// 检查沟通信息是否可以发送，对信息进行去重复处理
	public boolean checkRepeatSend(Map<String, Object> map, String messageCode) throws Exception{
		int sendCount = 0;
		boolean sendFlag = false;
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String repeatRange = ConvertUtil.getString(map.get("repeatRange"));
		String repeatBeginTime = ConvertUtil.getString(map.get("repeatBeginTime"));
		String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
		String batchId = ConvertUtil.getString(map.get("batchId"));
		String planCode = ConvertUtil.getString(map.get("planCode"));
		String phaseNum = ConvertUtil.getString(map.get("phaseNum"));
		String communicationCode = ConvertUtil.getString(map.get("communicationCode"));
		String commSetId = ConvertUtil.getString(map.get("commSetId"));
		String moreInfoFlag = ConvertUtil.getString(map.get("moreInfoFlag"));
		
		// 定义查询重复发送记录参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("brandInfoId", brandInfoId);
		paramMap.put("organizationInfoId", organizationInfoId);
		paramMap.put("receiverCode", mobilePhone);
		paramMap.put("repeatBeginTime", repeatBeginTime);		
		if("NL".equals(repeatRange)){
			// 允许重复发送信息的情况
			paramMap.put("messageCode", messageCode);
		}else if("BC".equals(repeatRange)){
			// 单批次内不允许重复发送信息的情况
			paramMap.put("batchId", batchId);
		}else if("PL".equals(repeatRange)){
			// 单次沟通计划内不允许重复发送信息的情况
			paramMap.put("planCode", planCode);
		}else if("PN".equals(repeatRange)){
			// 单次沟通内不允许重复发送信息的情况
			paramMap.put("planCode", planCode);
			paramMap.put("phaseNum", phaseNum);
		}else{
			paramMap.put("communicationCode", communicationCode);
		}
		// 判断是否同一沟通多沟通模板的情况
		if("Y".equals(moreInfoFlag)){
			// 同一沟通多模板的情况下判断信息是否已发送过，若发送过则判断在相同设置内沟通信息是否已发送过
			sendCount = binBECTSMG06_IF.getSmsSendFlag(paramMap);
			if(sendCount>0){
				paramMap.put("commSetId", commSetId);
				sendCount = binBECTSMG06_IF.getSmsSendFlag(paramMap);
				if(sendCount>0){
					sendFlag = false;
				}else{
					sendFlag = true;
				}
			}else{
				sendFlag = true;
			}
		}else{
			// 不同沟通情况下判断信息是否发送过，若已发送过则不允许再发送
			sendCount = binBECTSMG06_IF.getSmsSendFlag(paramMap);
			if(sendCount>0){
				sendFlag = false;
			}else{
				sendFlag = true;
			}
		}
		return sendFlag;
	}
	
	/**
	 * 根据秒钟添加日期
	 * @param dateTime
	 * @param second
	 * @return
	 */
	public String addDateSecond (String dateTime,int second){
		try{
			SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = spf.parse(dateTime);
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(date); 
			cal.add(Calendar.SECOND, second);
			String strDate = spf.format(cal.getTime());
			return strDate;
		}catch(Exception ex){
			return "";
		}
	}
	
	// 对会员加密的信息进行解密
	public List<Map<String, Object>> getCustomerDecrypt(String brandCode, List<Map<String, Object>> customerList) throws Exception{
		// 对加密字段进行解密
		if(customerList != null && !customerList.isEmpty()){
			for(Map<String,Object> customerMap : customerList){
				try{
					// 会员【密码】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("memberPassword"),true)) {
						String memberPassword = ConvertUtil.getString(customerMap.get("memberPassword"));
						customerMap.put("memberPassword",MemberPassword.decrypt(brandCode, memberPassword));
					}
					// 会员【手机号码】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("mobilePhone"), true)) {
						String mobilePhone = ConvertUtil.getString(customerMap.get("mobilePhone"));
						customerMap.put("mobilePhone", CherryBatchSecret.decryptData(brandCode, mobilePhone));
					}
					// 会员【电话号码】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("telephone"), true)) {
						String telephone = ConvertUtil.getString(customerMap.get("telephone"));
						customerMap.put("telephone", CherryBatchSecret.decryptData(brandCode, telephone));
					}
					// 会员【电子邮箱】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("email"), true)) {
						String email = ConvertUtil.getString(customerMap.get("email"));
						customerMap.put("email", CherryBatchSecret.decryptData(brandCode, email));
					}
					// 用户【密码】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("longinPassword"),true)) {
						String longinPassword = ConvertUtil.getString(customerMap.get("longinPassword"));
						customerMap.put("longinPassword",decryptPwd(longinPassword));
					}
				}catch(Exception e){
					// 解密失败的情况
//					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
//					batchLoggerDTO.setCode("ECT00089");
//					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
//					batchLoggerDTO.addParam(ConvertUtil.getString(customerMap.get("memId")));
//					batchLoggerDTO.addParam(ConvertUtil.getString(customerMap.get("memCode")));
//					batchLoggerDTO.addParam(ConvertUtil.getString(e));
//					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
//					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}
		}
		return customerList;
	}
	
	private String decryptPwd(String psd) throws Exception{
		// 解密
		if(psd != null && !"".equals(psd)) {
			// 解密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			psd =  des.decrypt(psd);
		}			
		return psd;
	}
	
	public Map<String, Object> getIfConfigInfo(Map<String, Object> map, String commIfType) throws Exception{
		String commInterface = (String) map.get("commInterface");
		if (null != commInterface && !"".equals(commInterface) 
				&& NeedTempSenderFactory.isRegister(commInterface)) {
			return null;
		}
		// 接口配置参数
		String supplierCode = "";
		String interfaceType = "";
		String enable = "";
		String serviceUrl = "";
		String sendInterface = "";
		String receiveInterface = "";
		String receiveRptInterface = "";
		// 用户名密码参数
		String userIdKey = "";
		String userIdValue = "";
		String passwordKey = "";
		String passwordValue = "";
		// 信息发送基本参数
		String batchIdKey = "";
		String sendTimeKey = "";
		String receiveCodeKey = "";
		String messageKey = "";
		String numCountKey = "";
		String taskTypeKey = "";
		String taskTypeValue = "";
		String companyIdKey = "";
		String companyIdValue = "";
		// 备用参数
		String otherParamKey = "";
		String otherParamValue = "";
		//首易参数
		String longSmsKey="";
		String longSmsValue="";
		String addNumKey="";
		String addNumValue="";
		// 电话呼叫基本参数
		String enpKey = "";
		String enpValue = "";
		String ifTypeKey = "";
		String ifTypeValue = "";
		String paramNameKey = "";
		String paramNameValue = "";
		String paramTypeKey = "";
		String paramTypeValue = "";
		String ivrIdKey = "";
		String ivrIdValue = "";
		String syncKey = "";
		String syncValue = "";
		
		
		Map<String, Object> ifConfigMap = new HashMap<String, Object>();
		// 获取接口配置List
		List<Map<String, Object>> configInfoList = binBECTSMG06_IF.getConfigInfo(map);
		for(Map<String,Object> configInfoMap : configInfoList){
			String paramCode = ConvertUtil.getString(configInfoMap.get("paramCode"));
			String configGroup = ConvertUtil.getString(configInfoMap.get("configGroup"));
			String paramKey = ConvertUtil.getString(configInfoMap.get("paramKey"));
			String paramValue = ConvertUtil.getString(configInfoMap.get("paramValue"));
			String paramType = ConvertUtil.getString(configInfoMap.get("paramType"));
			
			// 获取接口供应商配置
			if("".equals(supplierCode)){
				if(!"".equals(configGroup)){
					supplierCode = configGroup;
				}
			}
			
			if("1".equals(paramType)){
				// 参数类型为只需要维护值的情况时
				if("SMS".equals(commIfType)){
					// 判断参数代号以确定配置项
					if(paramCode.equals(CherryBatchConstants.SMSIF_TYPE)){
						// 获取接口类型配置值
						interfaceType = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSIF_ENABLE)){
						// 获取接口开放功能配置值
						enable = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSIF_URL)){
						// 获取接口服务地址配置值
						serviceUrl = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSIF_SEND)){
						// 获取发送接口配置值
						sendInterface = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSIF_RECEIVE)){
						// 获取接收接口配置值
						receiveInterface = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSIF_RECEIVERPT)){
						// 获取发送状态报告接收接口配置值
						receiveRptInterface = paramValue;
					}
				}else if("PHONE".equals(commIfType)){
					// 判断参数代号以确定配置项
					if(paramCode.equals(CherryBatchConstants.PHONEIF_TYPE)){
						// 获取接口类型配置值
						interfaceType = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEIF_CALLURL)){
						// 获取接口服务地址配置值
						serviceUrl = paramValue;
					}
				}
			}else if("2".equals(paramType)){
				// 参数类型为键和值都需要维护的情况时
				if("SMS".equals(commIfType)){
					// 判断参数代号以确定配置项
					if(paramCode.equals(CherryBatchConstants.SMSPARAM_USERID)){
						// 获取用户账号配置项键和值
						userIdKey = paramKey;
						userIdValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_PASSWORD)){
						// 获取用户密码配置项键和值
						passwordKey = paramKey;
						passwordValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_OTHER)){
						// 获取其他参数配置项键和值
						otherParamKey = paramKey;
						otherParamValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_TASKTYPE)){
						// 获取任务类型配置项键和值
						taskTypeKey = paramKey;
						taskTypeValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_COMPANYID)){
						// 获取公司ID配置项键和值
						companyIdKey = paramKey;
						companyIdValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_LongSms)){
						longSmsKey = paramKey;
						longSmsValue = paramValue;
					}
				}else if("PHONE".equals(commIfType)){
					// 判断参数代号以确定配置项
					if(paramCode.equals(CherryBatchConstants.PHONEPARAM_USERID)){
						// 获取用户账号配置项键和值
						userIdKey = paramKey;
						userIdValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_PASSWORD)){
						// 获取用户密码配置项键和值
						passwordKey = paramKey;
						passwordValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_ENPID)){
						// 获取其他参数配置项键和值
						enpKey = paramKey;
						enpValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_IFTYPE)){
						// 获取其他参数配置项键和值
						ifTypeKey = paramKey;
						ifTypeValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_PARAMNAME)){
						// 获取任务类型配置项键和值
						paramNameKey = paramKey;
						paramNameValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_PARAMTYPE)){
						// 获取公司ID配置项键和值
						paramTypeKey = paramKey;
						paramTypeValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_IVRID)){
						// 获取公司ID配置项键和值
						ivrIdKey = paramKey;
						ivrIdValue = paramValue;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_SYNC)){
						// 获取公司ID配置项键和值
						syncKey = paramKey;
						syncValue = paramValue;
					}
				}
			}else if("3".equals(paramType)){
				// 参数类型为只需要维护键的情况时
				if("SMS".equals(commIfType)){
					// 判断参数代号以确定配置项
					if(paramCode.equals(CherryBatchConstants.SMSPARAM_BATCHID)){
						// 获取信息发送批次号配置项
						batchIdKey = paramKey;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_SENDTIME)){
						// 获取信息发送时间配置项
						sendTimeKey = paramKey;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_RECEIVECODE)){
						// 获取接收号码配置项
						receiveCodeKey = paramKey;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_MESSAGE)){
						// 获取信息内容配置项
						messageKey = paramKey;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_NUMCOUNT)){
						// 获取接收号码数量配置项
						numCountKey = paramKey;
					}else if(paramCode.equals(CherryBatchConstants.SMSPARAM_AddNum)){
						addNumKey = paramKey;
						addNumValue = paramValue;
					}
				}else if("PHONE".equals(commIfType)){
					if(paramCode.equals(CherryBatchConstants.PHONEPARAM_PHONE)){
						// 获取接收号码配置项
						receiveCodeKey = paramKey;
					}else if(paramCode.equals(CherryBatchConstants.PHONEPARAM_MESSAGE)){
						// 获取信息内容配置项
						messageKey = paramKey;
					}
				}
			}else{
				// 无效的参数类型
				break;
			}
		}
		ifConfigMap.put("supplierCode", supplierCode);
		ifConfigMap.put("interfaceType", interfaceType);
		ifConfigMap.put("enable", enable);
		ifConfigMap.put("serviceUrl", serviceUrl);
		ifConfigMap.put("sendInterface", sendInterface);
		ifConfigMap.put("receiveInterface", receiveInterface);
		ifConfigMap.put("receiveRptInterface", receiveRptInterface);
		ifConfigMap.put("longSmsKey", longSmsKey);
		ifConfigMap.put("longSmsValue", longSmsValue);
		ifConfigMap.put("addNumKey", addNumKey);
		ifConfigMap.put("addNumValue", addNumValue);
		ifConfigMap.put("userIdKey", userIdKey);
		ifConfigMap.put("userIdValue", userIdValue);
		ifConfigMap.put("passwordKey", passwordKey);
		ifConfigMap.put("passwordValue", passwordValue);
		ifConfigMap.put("batchIdKey", batchIdKey);
		ifConfigMap.put("sendTimeKey", sendTimeKey);
		ifConfigMap.put("receiveCodeKey", receiveCodeKey);
		ifConfigMap.put("messageKey", messageKey);
		ifConfigMap.put("numCountKey", numCountKey);
		ifConfigMap.put("otherParamKey", otherParamKey);
		ifConfigMap.put("otherParamValue", otherParamValue);
		ifConfigMap.put("taskTypeKey", taskTypeKey);
		ifConfigMap.put("taskTypeValue", taskTypeValue);
		ifConfigMap.put("companyIdKey", companyIdKey);
		ifConfigMap.put("companyIdValue", companyIdValue);
		
		ifConfigMap.put("enpKey", enpKey);
		ifConfigMap.put("enpValue", enpValue);
		ifConfigMap.put("ifTypeKey", ifTypeKey);
		ifConfigMap.put("ifTypeValue", ifTypeValue);
		ifConfigMap.put("paramNameKey", paramNameKey);
		ifConfigMap.put("paramNameValue", paramNameValue);
		ifConfigMap.put("paramTypeKey", paramTypeKey);
		ifConfigMap.put("paramTypeValue", paramTypeValue);
		ifConfigMap.put("ivrIdKey", ivrIdKey);
		ifConfigMap.put("ivrIdValue", ivrIdValue);
		ifConfigMap.put("syncKey", syncKey);
		ifConfigMap.put("syncValue", syncValue);
		return ifConfigMap;
	}
	
	// 检查模板中设置的参数，判断由模板生成的短信内容是否会相同
	public boolean isTheSameMessage (String template) throws Exception{
		// 检查会员卡号
		if(template.contains(CherryBatchConstants.MEMBER_CODE)){
			return false;
		}
		// 检查会员姓名
		if(template.contains(CherryBatchConstants.MEMBER_NAME)){
			return false;
		}
		// 检查会员密码
		if(template.contains(CherryBatchConstants.MEMBER_PASSWORD)){
			return false;
		}
		// 检查会员生日
		if(template.contains(CherryBatchConstants.BIRTHDAY)){
			return false;
		}		
		// 检查会员当前积分
		if(template.contains(CherryBatchConstants.TOTAL_POINT)){
			return false;
		}
		// 检查会员可兑换积分
		if(template.contains(CherryBatchConstants.CHANGABLE_POINT)){
			return false;
		}
		// 检查会员即将失效积分
		if(template.contains(CherryBatchConstants.CURDISABLE_POINT)){
			return false;
		}
		// 检查会员当前所属柜台
		if(template.contains(CherryBatchConstants.COUNTER_NAME)){
			return false;
		}
		// 检查会员预约活动柜台
		if(template.contains(CherryBatchConstants.RECOUNTER_NAME)){
			return false;
		}
		// 检查会员礼品领取柜台
		if(template.contains(CherryBatchConstants.GETCOUNTER_NAME)){
			return false;
		}
		// 检查活动验证号
		if(template.contains(CherryBatchConstants.COUPON_CODE)){
			return false;
		}
		// 检查活动参与单据号
		if(template.contains(CherryBatchConstants.ORDER_ID)){
			return false;
		}
		// 检查会员参与活动时间
		if(template.contains(CherryBatchConstants.ORDER_TIME)){
			return false;
		}
		// 检查参与活动差额积分
		if(template.contains(CherryBatchConstants.POINTS_DIFF)){
			return false;
		}
		// 检查会员可兑换积分截止时间
		if(template.contains(CherryBatchConstants.POINTS_ENDDATE)){
			return false;
		}
		// 检查会员姓氏
		if(template.contains(CherryBatchConstants.FIRSTNAME)){
			return false;
		}
		// 检查交易时间
		if(template.contains(CherryBatchConstants.SALEDATE)){
			return false;
		}
		// 检查积分变化时间
		if(template.contains(CherryBatchConstants.POINT_CHANGEDATE)){
			return false;
		}
		// 检查单笔交易金额
		if(template.contains(CherryBatchConstants.TRADEAMOUNT)){
			return false;
		}
		// 检查单笔交易积分
		if(template.contains(CherryBatchConstants.SALEPOINT)){
			return false;
		}
		// 检查单次积分变化值
		if(template.contains(CherryBatchConstants.TRADEPOINT)){
			return false;
		}
		// 检查升级差额购买金额
		if(template.contains(CherryBatchConstants.UPLEVEL_AMOUNT)){
			return false;
		}
		// 检查员工号
		if(template.contains(CherryBatchConstants.EMPLOYEE_CODE)){
			return false;
		}
		// 检查员工姓名
		if(template.contains(CherryBatchConstants.EMPLOYEE_NAME)){
			return false;
		}
		// 检查登陆账号
		if(template.contains(CherryBatchConstants.LONGIN_NAME)){
			return false;
		}
		// 检查登陆密码
		if(template.contains(CherryBatchConstants.LONGIN_PASSWORD)){
			return false;
		}
		return true;
	}
	
}



package com.cherry.wp.mbm.bl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM11_BL;
import com.cherry.wp.common.service.BINOLWPCM01_Service;
import com.cherry.wp.mbm.interfaces.BINOLWPMBM01_IF;
import com.cherry.wp.mbm.service.BINOLWPMBM01_Service;

/**
 * 会员管理BL
 * 
 * @author WangCT
 * @version 1.0 2014/08/15
 */
public class BINOLWPMBM01_BL implements BINOLWPMBM01_IF {
	
	/** 会员管理Service **/
	@Resource
	private BINOLWPMBM01_Service binOLWPMBM01_Service;
	
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	@Resource(name="binOLWPCM01_Service")
	private BINOLWPCM01_Service binOLWPCM01_Service;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Override
	public int getMemberCount(Map<String, Object> map) {
		return binOLWPCM01_Service.getMemberCount(map);
	}
	
	
	@Override
	public Map<String, Object> searchMemList(Map<String, Object> map) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int count = binOLWPCM01_Service.getMemberCount(map);
		resultMap.put("total", count);
		if(count > 0) {
			List<Map<String, Object>> memberInfoList = binOLWPCM01_Service.getMemberInfoList(map);
			for(Map<String,Object> memberInfo:memberInfoList){
				memberInfo.put("mobilePhone", CherrySecret.decryptData(ConvertUtil.getString(map.get("brandCode")),ConvertUtil.getString(memberInfo.get("mobilePhone"))));
			}
			resultMap.put("list", memberInfoList);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getMemReport(Map<String, Object> map) {
		
		Map<String, Object> memReport = new HashMap<String, Object>();
		
		int tickLength = (Integer)map.get("TOP");
		Map<String, Map<String, Object>> barsMap = new HashMap<String, Map<String, Object>>();
		// 按天统计会员的购买金额
		List<Map<String, Object>> saleAmountByDayList = binOLWPMBM01_Service.getSaleAmountByDay(map);
		if(saleAmountByDayList != null && !saleAmountByDayList.isEmpty()) {
			for(int i = 0; i < saleAmountByDayList.size(); i++) {
				Map<String, Object> saleAmountByDayMap = saleAmountByDayList.get(i);
				String date = (String)saleAmountByDayMap.get("date");
				BigDecimal value = (BigDecimal)saleAmountByDayMap.get("value");
				Map<String, Object> valueMap = new HashMap<String, Object>();
				valueMap.put("date", date);
				valueMap.put("amount", value);
				barsMap.put(date, valueMap);
			}
		}
		// 按天统计会员的积分
		List<Map<String, Object>> pointByDayList = binOLWPMBM01_Service.getPointByDay(map);
		if(pointByDayList != null && !pointByDayList.isEmpty()) {
			for(int i = 0; i < pointByDayList.size(); i++) {
				Map<String, Object> pointByDayMap = pointByDayList.get(i);
				String date = (String)pointByDayMap.get("date");
				BigDecimal value = (BigDecimal)pointByDayMap.get("value");
				if(barsMap.containsKey(date)) {
					barsMap.get(date).put("point", value);
				} else {
					Map<String, Object> valueMap = new HashMap<String, Object>();
					valueMap.put("date", date);
					valueMap.put("point", value);
					barsMap.put(date, valueMap);
				}
			}
		}
		if(!barsMap.isEmpty()) {
			List<Map<String, Object>> barsList = new ArrayList<Map<String,Object>>();
			for(String key : barsMap.keySet()) {
				barsList.add(barsMap.get(key));
			}
			Collections.sort(barsList, new Comparator<Map<String, Object>>() {
	            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
	            	String date0 = (String)arg0.get("date");
	            	String date1 = (String)arg1.get("date");
	                return date0.compareTo(date1);
	            }
	        });
			int barsLength = barsList.size();
			if(barsLength > tickLength) {
				barsList = barsList.subList(barsLength - tickLength, barsList.size());
			}
			memReport.put("chart1", barsList);
		}
		
		// 统计不同产品类别的销售数量和金额
		List<Map<String, Object>> saleCountByProCatList = binOLWPMBM01_Service.getSaleCountByProCat(map);
		if(saleCountByProCatList != null && !saleCountByProCatList.isEmpty()) {
			int max = 6;
			int otherAmount = 0;
			int otherQuantity = 0;
			
			Collections.sort(saleCountByProCatList, new Comparator<Map<String, Object>>() {
	            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
	            	BigDecimal amount0 = (BigDecimal)arg0.get("amount");
	            	BigDecimal amount1 = (BigDecimal)arg1.get("amount");
	                return amount1.compareTo(amount0);
	            }
	        });
			List<Map<String, Object>> chart2List = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < saleCountByProCatList.size(); i++) {
				Map<String, Object> saleCountByProCatMap = saleCountByProCatList.get(i);
				String propValueChinese = (String)saleCountByProCatMap.get("propValueChinese");
				double amount = ((BigDecimal)saleCountByProCatMap.get("amount")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				if(amount > 0) {
					if(chart2List.size() < max) {
						Map<String, Object> chartMap = new HashMap<String, Object>();
						chartMap.put("name", propValueChinese);
						chartMap.put("value", amount);
						chart2List.add(chartMap);
					} else {
						otherAmount += amount;
					}
				}
			}
			// 超出统计范围的产品归为其他类
			if(otherAmount > 0) {
				Map<String, Object> chartMap = new HashMap<String, Object>();
				chartMap.put("name", "-1");
				chartMap.put("value", otherAmount);
				chart2List.add(chartMap);
			}
			memReport.put("chart2", chart2List);
			
			Collections.sort(saleCountByProCatList, new Comparator<Map<String, Object>>() {
	            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
	            	BigDecimal quantity0 = (BigDecimal)arg0.get("quantity");
	            	BigDecimal quantity1 = (BigDecimal)arg1.get("quantity");
	                return quantity1.compareTo(quantity0);
	            }
	        });
			List<Map<String, Object>> chart3List = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < saleCountByProCatList.size(); i++) {
				Map<String, Object> saleCountByProCatMap = saleCountByProCatList.get(i);
				String propValueChinese = (String)saleCountByProCatMap.get("propValueChinese");
				int quantity = ((BigDecimal)saleCountByProCatMap.get("quantity")).intValue();
				if(quantity > 0) {
					if(chart3List.size() < max) {
						Map<String, Object> chartMap = new HashMap<String, Object>();
						chartMap.put("name", propValueChinese);
						chartMap.put("value", quantity);
						chart3List.add(chartMap);
					} else {
						otherQuantity += quantity;
					}
				}
			}
			// 超出统计范围的产品归为其他类
			if(otherQuantity > 0) {
				Map<String, Object> chartMap = new HashMap<String, Object>();
				chartMap.put("name", "-1");
				chartMap.put("value", otherQuantity);
				chart3List.add(chartMap);
			}
			memReport.put("chart3", chart3List);
		}
		return memReport;
	}

	@Override
	public void addMem(Map<String, Object> map) throws Exception {
		Map<String, Object> couInfo = binOLWPMBM01_Service.getCouInfoByCouId(map);
		if(couInfo != null) {
			map.put("organizationCode", map.get("counterCode"));
			map.put("organizationId", couInfo.get("organizationId"));
			map.put("counterKind", String.valueOf(couInfo.get("counterKind")));
		}
		Map<String, Object> empInfo = binOLWPMBM01_Service.getEmpInfoByEmpId(map);
		if(empInfo != null) {
			map.put("employeeCode", empInfo.get("employeeCode"));
		}
		String sysDateTime = binOLWPMBM01_Service.getSYSDateTime();
		map.put("joinDate", sysDateTime.substring(0, 10));
		map.put("joinTime", sysDateTime.substring(11, 19));
		binOLMBMBM11_BL.tran_addMemberInfo(map);
	}
	
	@Override
	public void setContents(Map<String, Object> map, int type){
		Map<String, Object> praMap = new HashMap<String, Object>(map);
		praMap.put("eventType", 14);
		map.putAll(praMap);
		String contents = binOLWPMBM01_Service.getContents(praMap);
		map.put("contents", contents);
	}

	/**
	 * 对会员新增以及更换非换卡资料coupon校验
	 */
	@Override
	public String couponCheck(String phoneNumber, String couponCode,Map<String, Object> map) {
		String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		//获取系统配置【短信有效时间】
		int seconds = ConvertUtil.getInt(binOLCM14_BL.getConfigValue("1111",organizationInfoId, brandInfoId));
		return check(phoneNumber,couponCode,seconds,map);
	}

	/**
	 * 换卡操作新老手机coupon校验
	 */
	@Override
	public String cardChangeCheck(String phoneNumber, String couponCode,
			String phoneNumberOld, String couponCodeOld,Map<String, Object> map) {
		String organizationInfoId = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
		String brandInfoId = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
		//获取系统配置【短信有效时间】
		int seconds = ConvertUtil.getInt(binOLCM14_BL.getConfigValue("1111",organizationInfoId, brandInfoId));
		//新号码检验flag
		String checkNewFlag = check(phoneNumber,couponCode,seconds,map);
		if("1".equals(checkNewFlag)){
			return "1";
		}
		//老号码检验flag
		String checkOldFlag = check(phoneNumberOld,couponCodeOld,seconds,map);
		if("1".equals(checkOldFlag)){
			//2为旧手机验证码有误
			return "2";
		}
		return "0";
	}
	
	private String check(String mobilePhone, String couponCode,int seconds,Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>(map);
		paramMap.put("mobilePhone", mobilePhone);
		String tempdate = binOLWPMBM01_Service.getSYSDateTime();
		Date date = DateUtil.coverString2Date(tempdate,DateUtil.DATETIME_PATTERN);
		date.setTime(date.getTime()-seconds*1000);
		String startTime = DateUtil.date2String(date, DateUtil.DATETIME_PATTERN);
		paramMap.put("startTime", startTime);
		List<Map<String,Object>> couponList = binOLWPMBM01_Service.getCouponCodeList(paramMap);
		if(couponList.size()>0 && !couponList.isEmpty()){
			for(Map<String,Object> item : couponList){
				String code = ConvertUtil.getString(item.get("CouponCode"));
				if(code.equals(couponCode)){
					return "0";
				}
			}
		}
		return "1";
	}

}

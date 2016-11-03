package com.cherry.webservice.communication.bl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.smg.bl.BINBECTSMG05_BL;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.communication.service.PhoneCallService;

public class PhoneCall implements IWebservice{
	
	@Resource(name = "phoneCallService")
	private PhoneCallService phoneCallService;
	
	@Resource(name = "binBECTSMG05_BL")
	private BINBECTSMG05_BL binBECTSMG05_BL;
	
	@Override
	public Map<String, Object> tran_execute(Map map) throws Exception {
		// 定义返回Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 定义调用参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 获取传入参数
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String brandCode = ConvertUtil.getString(map.get("BrandCode"));
		String eventType = ConvertUtil.getString(map.get("EventType"));
		String mobilePhone = ConvertUtil.getString(map.get("MobilePhone"));
		String campaignCode = ConvertUtil.getString(map.get("CampaignCode"));
		String sourse = ConvertUtil.getString(map.get("Sourse"));
		String sysTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
		String couponValidFlag = "0";
		
		// 检查EventType参数
		if(CherryChecker.isNullOrEmpty(eventType)) {
			returnMap.put("ERRORCODE", "WSE9993");
			returnMap.put("ERRORMSG", "参数EventType是必须的");
			return returnMap;
		}
		// 检查MobilePhone参数
		if(CherryChecker.isNullOrEmpty(mobilePhone)) {
			returnMap.put("ERRORCODE", "WSE9993");
			returnMap.put("ERRORMSG", "参数MobilePhone是必须的");
			return returnMap;
		}
		try{
			paramMap.put("organizationInfoId", organizationInfoId);
			paramMap.put("brandInfoId", brandInfoId);
			paramMap.put("brandCode", brandCode);
			paramMap.put("eventType", eventType);	
			paramMap.put("eventId", mobilePhone);
			paramMap.put("campaignCode", campaignCode);
			paramMap.put("eventDate", sysTime);
			paramMap.put("sourse", sourse);
			
			// 活动编号不为空的情况下检查活动单据信息
			if(!"".equals(campaignCode)){
				couponValidFlag = checkCouponValidFlag(paramMap);
			}
			if("0".equals(couponValidFlag)){
				// 调用沟通程序的信息发送方法
				Map<String,Object> result = binBECTSMG05_BL.wsPhoneCall(paramMap);
				String sendFlag = ConvertUtil.getString(result.get("sendFlag"));
				if(!"0".equals(sendFlag)){
					// 发送失败的情况
					returnMap.put("ERRORCODE", "WSE0036");
					returnMap.put("ERRORMSG", "信息发送失败，请从沟通相关的日志中获取异常原因");
				}
			}else if("ET".equals(couponValidFlag)){
				returnMap.put("ERRORCODE", "WSE0053");
				returnMap.put("ERRORMSG", "未执行信息发送，单据已过期");
			}else if("NG".equals(couponValidFlag)){
				returnMap.put("ERRORCODE", "WSE0054");
				returnMap.put("ERRORMSG", "未执行信息发送，单据无效或取消");
			}else if("NL".equals(couponValidFlag)){
				returnMap.put("ERRORCODE", "WSE0055");
				returnMap.put("ERRORMSG", "未执行信息发送，没有找到可以领用的单据");
			}else if("GET".equals(couponValidFlag)){
				returnMap.put("ERRORCODE", "WSE0056");
				returnMap.put("ERRORMSG", "未执行信息发送，单据已被领用");
			}else{
				returnMap.put("ERRORCODE", "WSE9999");
				returnMap.put("ERRORMSG", "处理过程中发生未知异常");
			}
			return returnMap;
		}catch(Exception ex){
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("WSL00003");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(mobilePhone);
			batchLoggerDTO.addParam(ConvertUtil.getString(ex));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// WebService返回值
			returnMap.put("ERRORCODE", "WSE9999");
			returnMap.put("ERRORMSG", "处理过程中发生未知异常 ");
			return returnMap;
		}
	}
	
	private String checkCouponValidFlag(Map<String, Object> map) throws Exception{
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String mobilePhone = ConvertUtil.getString(map.get("eventId"));
		String campaignCode = ConvertUtil.getString(map.get("campaignCode"));
		String eventDate = ConvertUtil.getString(map.get("eventDate"));
		String couponValidFlag = "GET";	// 设置一个默认值用于标识单据已被领用
		// 定义返回Map
		Map<String, Object> praMap = new HashMap<String, Object>();
		praMap.put("organizationInfoId", organizationInfoId);
		praMap.put("brandInfoId", brandInfoId);
		praMap.put("mobilePhone", mobilePhone);
		praMap.put("campaignCode", campaignCode);
		List<Map<String, Object>> orderList = phoneCallService.getMemberCouponInfo(praMap);
		if(orderList != null && !orderList.isEmpty()) {
    		for(Map<String,Object> orderMap : orderList){
    			String state = ConvertUtil.getString(orderMap.get("state"));
    			String getToTime = ConvertUtil.getString(orderMap.get("getToTime"));
    			if("AR".equals(state)){
    				if("".equals(getToTime)){
    					couponValidFlag = "0";
	    				break;
    				}else{
	    				if(dateBefore(getToTime, eventDate, CherryBatchConstants.DF_TIME_PATTERN)){
	    					// 单据过期的情况（存在多条记录情况下不退出循环，继续判断其它记录是否正常）
	    					couponValidFlag = "ET";
	    				}else{
		    				couponValidFlag = "0";
		    				break;
	    				}
    				}
    			}else if("OK".equals(state)){
    				// 单据已领取的情况（存在多条记录情况下不退出循环，继续判断其它记录是否正常）
    				couponValidFlag = "GET";
    			}else{
    				// 单据无效或取消等情况（存在多条记录情况下不退出循环，继续判断其它记录是否正常）
    				couponValidFlag = "NG";
    			}
    		}
		}else{
			// 没有查询到可领用单据的情况
			couponValidFlag = "NL";
		}
		return couponValidFlag;
	}
	
	// 比较第一个时间是否在第二个时间之前
	private boolean dateBefore(String value1, String value2, String pattern){
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
}

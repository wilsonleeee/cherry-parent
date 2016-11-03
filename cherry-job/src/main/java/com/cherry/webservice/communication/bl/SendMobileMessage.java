package com.cherry.webservice.communication.bl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ct.smg.bl.BINBECTSMG05_BL;
import com.cherry.webservice.common.IWebservice;

public class SendMobileMessage implements IWebservice{
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binBECTSMG05_BL")
	private BINBECTSMG05_BL binBECTSMG05_BL;
	
	@Override
	public Map<String, Object> tran_execute(Map map) throws Exception {
		// 定义返回Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 定义沟通程序调用参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 获取传入参数
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String orgCode = ConvertUtil.getString(map.get("OrgCode"));
		String brandCode = ConvertUtil.getString(map.get("BrandCode"));
		String eventType = ConvertUtil.getString(map.get("EventType"));
		String mobilePhone = ConvertUtil.getString(map.get("MobilePhone"));
		String couponCode = ConvertUtil.getString(map.get("CouponCode"));
		String couponExpireTime = ConvertUtil.getString(map.get("CouponExpireTime"));
		String messageInfo = ConvertUtil.getString(map.get("MessageBody"));
		String sysTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		int expireTimeType = 0;
		boolean isMobile = false;
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
		// 验证手机号码是否符合规则
		mobilePhone = mobilePhone.replace("；", ";");
		mobilePhone = mobilePhone.replace("，", ";");
		mobilePhone = mobilePhone.replace(",", ";");
		// 解析号码字符串放入数组
		String[] mobiles = mobilePhone.split(";");
		for (String mobile : mobiles) {
			if(!"".equals(mobile)){
				if(CherryChecker.isPhoneValid(mobile, mobileRule)){
					isMobile = true;
					break;
				}
			}
		}
		if(!isMobile){
			returnMap.put("ERRORCODE", "WSE0031");
			returnMap.put("ERRORMSG", "MobilePhone参数不合法");
			return returnMap;
		}
		// 验证CouponCode是否超出长度限制
		if(couponCode.length() > 20){
			returnMap.put("ERRORCODE", "WSE0032");
			returnMap.put("ERRORMSG", "CouponCode参数超出长度限制");
			return returnMap;
		}
		// 验证Coupon过期时间是否合法
		if(!"".equals(messageInfo)){
			if(!"".equals(couponExpireTime)){
				if(CherryChecker.isNumeric(couponExpireTime)){
					paramMap.put("couponExpireTime", couponExpireTime);
					expireTimeType = 1;
				}else if(CherryChecker.checkDate(couponExpireTime, CherryBatchConstants.DF_TIME_PATTERN)){
					paramMap.put("couponExpireTime", couponExpireTime);
					expireTimeType = 2;
				}else{
					returnMap.put("ERRORCODE", "WSE0033");
					returnMap.put("ERRORMSG", "CouponExpireTime参数不合法");
					return returnMap;
				}
			}
		}
		paramMap.put("organizationInfoId", organizationInfoId);
		paramMap.put("brandInfoId", brandInfoId);
		paramMap.put("orgCode", orgCode);
		paramMap.put("brandCode", brandCode);
		paramMap.put("eventType", eventType);	//暂定WebService接收的事件全部由新后台14号事件进行处理，后期需要将WebService传入的事件类型保存到数据库另外的字段中
		paramMap.put("eventId", mobilePhone);
		paramMap.put("eventDate", sysTime);
		paramMap.put("couponCode", couponCode);
		paramMap.put("messageContents", messageInfo);
		paramMap.put("sourse", "WS_SENDMOBILEMESSAGE");
		try{
			// 调用沟通程序的信息发送方法

			Map<String,Object> sendResult = binBECTSMG05_BL.wsSendMessage(paramMap);
			String sendFlag = ConvertUtil.getString(sendResult.get("sendFlag"));
			if("0".equals(sendFlag)){
				// 发送成功的情况
				Map<String, Object> resultMap = new HashMap<String, Object>();
				String returnCouponCode = ConvertUtil.getString(sendResult.get("couponCode"));
				String returnExpiredTime = ConvertUtil.getString(sendResult.get("expiredTime"));
				resultMap.put("CouponCode", returnCouponCode);
				if(expireTimeType == 1){
					resultMap.put("CouponExpireTime", couponExpireTime);
				}else if(expireTimeType == 2){
					Date startday = DateUtil.coverString2Date(sysTime, CherryBatchConstants.DF_TIME_PATTERN);
					Date endday = DateUtil.coverString2Date(couponExpireTime, CherryBatchConstants.DF_TIME_PATTERN);
					if(endday != null){
					int expireTime = binBECTSMG05_BL.getIntervalDays(startday, endday);
						resultMap.put("CouponExpireTime", expireTime);
					}else{
						resultMap.put("CouponExpireTime", "");
					}
				}else{
					Date startday = DateUtil.coverString2Date(sysTime, CherryBatchConstants.DF_TIME_PATTERN);
					Date endday = DateUtil.coverString2Date(returnExpiredTime, CherryBatchConstants.DF_TIME_PATTERN);
					if(endday != null){
						int expireTime = binBECTSMG05_BL.getIntervalDays(startday, endday);
						resultMap.put("CouponExpireTime", expireTime);
					}else{
						resultMap.put("CouponExpireTime", "");
					}
				}
				returnMap.put("ResultMap", resultMap);
			}else if("99".equals(sendFlag)){
				// 不在设置允许的发送时间范围内的情况
				returnMap.put("ERRORCODE", "WSE0034");
				returnMap.put("ERRORMSG", "WebService未传入信息内容参数，调用沟通事件模板提供信息发送内容，但当前时间不在设置允许的发送时间范围内");
			}else if("1".equals(sendFlag)){
				// 发送警告的情况
				returnMap.put("ERRORCODE", "WSE0035");
				returnMap.put("ERRORMSG", "WebService未传入信息内容参数，调用沟通事件模板提供信息发送内容，但未能获取到事件设置或发送信息出现部分失败");
			}else if("9".equals(sendFlag)){
				// 沟通对象为空的情况
				returnMap.put("ERRORCODE", "WSE0043");
				returnMap.put("ERRORMSG", "未能获取到符合条件的沟通对象");
			}else if("10".equals(sendFlag)){
				// 设置信息为空的情况
				returnMap.put("ERRORCODE", "WSE0042");
				returnMap.put("ERRORMSG", "未能获取到沟通事件设置");
			}else if("3".equals(sendFlag)){
				// 设置信息为空的情况
				returnMap.put("ERRORCODE", "WSE0076");
				returnMap.put("ERRORMSG", "信息发送失败，没有发送成功的信息");
			}else{
				// 发送失败的情况
				returnMap.put("ERRORCODE", "WSE0036");
				returnMap.put("ERRORMSG", "信息发送失败，请从沟通相关的日志中获取异常原因");
			}
			return returnMap;
		}catch(Exception ex){
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("WSL00001");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(mobilePhone);
			batchLoggerDTO.addParam(couponCode);
			batchLoggerDTO.addParam(ConvertUtil.getString(ex));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// WebService返回值
			returnMap.put("ERRORCODE", "WSE9999");
			returnMap.put("ERRORMSG", "处理过程中发生未知异常 ");
			return returnMap;
		}
	}

}

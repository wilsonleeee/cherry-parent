package com.cherry.webservice.communication.bl;


import java.util.Map;

import com.cherry.webservice.common.IWebservice;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.communication.service.ValidateCouponService;

public class ValidateCoupon implements IWebservice{
	
	@Resource(name = "validateCouponService")
	private ValidateCouponService validateCouponService;
	
	@Override
	public Map<String, Object> tran_execute(Map map) throws Exception {
		// 定义返回Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> ValidateResultMap = new HashMap<String, Object>();
		// 定义调用参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 获取传入参数
		String eventType = ConvertUtil.getString(map.get("EventType"));
		String mobilePhone = ConvertUtil.getString(map.get("MobilePhone"));
		String couponCode = ConvertUtil.getString(map.get("CouponCode"));
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
		// 检查couponCode参数
		if(CherryChecker.isNullOrEmpty(couponCode)) {
			returnMap.put("ERRORCODE", "WSE9993");
			returnMap.put("ERRORMSG", "参数CouponCode是必须的");
			return returnMap;
		}
		try{
			paramMap.put("campaignCode", CherryBatchConstants.EVENT_GETCOUPON_CAMP);
			paramMap.put("mobilePhone", mobilePhone);
			paramMap.put("couponCode", couponCode);
			List<Map<String,Object>> validateResult = validateCouponService.checkCoupon(paramMap);
			if(validateResult != null && !validateResult.isEmpty()){
				for(Map<String,Object> validateMap : validateResult){
					// 获取验证码过期时间值
					int expireSeconds = ConvertUtil.getInt(validateMap.get("Seconds"));
					// 判断验证码是否过期
					if(expireSeconds > 0){
						ValidateResultMap.put("ValidateResult", "NEP");
						returnMap.put("ResultMap", ValidateResultMap);
					}else{
						// 更新验证码过期时间，防止重复验证
						Map<String, Object> updateMap = new HashMap<String, Object>();
						updateMap.put("couponId", validateMap.get("couponId"));
						validateCouponService.updateExpiredTime(updateMap);
						// 验证结果
						ValidateResultMap.put("ValidateResult", "Y");
						returnMap.put("ResultMap", ValidateResultMap);
					}
				}
			}else{
				ValidateResultMap.put("ValidateResult", "NNE");
				returnMap.put("ResultMap", ValidateResultMap);
			}
			return returnMap;
		}catch(Exception ex){
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("WSL00002");
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

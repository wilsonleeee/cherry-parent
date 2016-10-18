package com.cherry.webservice.promotion.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.promotion.service.UpdateCouponService;


/**
 * 优惠券状态变更（家化专用） 接口BL
 * 
 * @author yangcheng
 * @version 2016-04-08 1.0.0
 */
public class UpdateCouponLogic {
	/** 打印日志 */
	private Logger logger = LoggerFactory
			.getLogger(UpdateCouponLogic.class);
	@Resource
	private UpdateCouponService updateCouponService;



	public Map<String, Object> tran_UpdateCouponJH(Map<String, Object> map) {
		Map<String,Object> returnMap=new HashMap<String, Object>();
		try {
			Map<String,Object> check_result=this.checkParam(map);
			if(null != check_result){
				returnMap.put("ERRORCODE", check_result.get("ERRORCODE"));
				returnMap.put("ERRORMSG", check_result.get("ERRORMSG"));
				return returnMap;
			}
			//查看数据库中受否存在该优惠券
			Map<String,Object> couponStatus_map=updateCouponService.getCouponStatus(map);
			if(null == couponStatus_map){
				returnMap.put("ERRORCODE", "WSESP0202");
				returnMap.put("ERRORMSG", "没有查询到券相关信息或者已经被使用/废弃");
				return returnMap;
			}
			String couponStatus=ConvertUtil.getString(couponStatus_map.get("status"));
			if(!"AR".equals(couponStatus)){
				returnMap.put("ERRORCODE", "WSESP0202");
				returnMap.put("ERRORMSG", "该优惠券券已经被使用/废弃");
				return returnMap;
			}
			int result_count=updateCouponService.updateCouponStatus(map);
			if(result_count > 0){
				returnMap.put("ERRORCODE", "0");
				returnMap.put("ERRORMSG", "OK");
			}else{
				returnMap.put("ERRORCODE", "WSESP0205");
				returnMap.put("ERRORMSG", "状态更新失败");
				return returnMap;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			returnMap.put("ERRORCODE", "WSE9999");
			returnMap.put("ERRORMSG", "处理过程中发生未知异常");
		}
		return returnMap;
	}
	
	private Map<String,Object> checkParam(Map<String, Object> map){
		Map<String,Object> result_map=new HashMap<String,Object>();
		String CouponCode=ConvertUtil.getString(map.get("CouponCode"));
		String Status =ConvertUtil.getString(map.get("Status"));
		int flag=1;
		if("".equals(CouponCode)){
			result_map.put("ERRORCODE", "WSESP0201");
			result_map.put("ERRORMSG", "您传入的券号为空");
			return result_map;
		}
		if("".equals(Status)){
			result_map.put("ERRORCODE", "WSESP0202");
			result_map.put("ERRORMSG", "您传入的修改状态为空");
			return result_map;
		}
		if("AR".equals(Status) || "OK".equals(Status) || "CA".equals(Status) ){
			flag=0;
		}
		if(flag == 1){
			result_map.put("ERRORCODE", "WSESP0203");
			result_map.put("ERRORMSG", "您传入的修改状态异常，请核实");
			return result_map;
		}
		return null;
	}
	
}

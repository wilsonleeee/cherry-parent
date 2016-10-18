package com.cherry.webservice.promotion.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.prm.interfaces.BINOLSSPRM74_IF;
import com.cherry.ss.prm.interfaces.Coupon_IF;
import com.cherry.webservice.promotion.service.UpdateCouponService;


/**
 * 优惠券状态变更（家化专用） 接口BL
 * 
 * @author yangcheng
 * @version 2016-04-08 1.0.0
 */
public class VerifyCouponLogic {
	/** 打印日志 */
	private Logger logger = LoggerFactory
			.getLogger(VerifyCouponLogic.class);
	@Resource
	private UpdateCouponService updateCouponService;
	
	@Resource(name="binOLSSPRM74_BL")
	private BINOLSSPRM74_IF binOLSSPRM74_IF;

	@Resource
    private Coupon_IF coupon_IF;


	public Map<String, Object> tran_VerifyCouponJH(Map<String, Object> map) {
		Map<String,Object> returnMap=new HashMap<String, Object>();
		try {
			Map<String,Object> check_result=this.checkParam(map);
			if(null != check_result){
				returnMap.put("ERRORCODE", check_result.get("ERRORCODE"));
				returnMap.put("ERRORMSG", check_result.get("ERRORMSG"));
				return returnMap;
			}
			//使用区分
			String UD=ConvertUtil.getString(map.get("UD"));
			Map<String,Object> convert_map=binOLSSPRM74_IF.convert2Part(map);
//			List<Map<String,Object>> cart_list=(List<Map<String,Object>>) convert_map.get("cart_list");
			Map<String,Object> main_map=(Map<String,Object>)convert_map.get("main_map");
			List<Map<String,Object>> cartOrder_list=(List<Map<String,Object>>) convert_map.get("cartOrder_list");
			List<Map<String,Object>> coupon_list=(List<Map<String,Object>>)map.get("CP");
			for(Map<String,Object> coupon:coupon_list){
				coupon.put("couponCode", coupon.get("CouponCode"));
				coupon.put("password", coupon.get("Password"));
			}
			Map<String,Object> param_map=new HashMap<String, Object>();
			param_map.put("Main_map", main_map);
			param_map.put("cart_map", cartOrder_list);
			param_map.put("coupon_list", coupon_list);
			Map<String, Object> result_coupon=coupon_IF.checkCoupon(param_map);
			String checkCoupon_result=ConvertUtil.getString(result_coupon.get("ResultCode"));
			String checkCoupon_msg=ConvertUtil.getString(result_coupon.get("ResultMsg"));
			returnMap.put("ERRORCODE", checkCoupon_result);
			returnMap.put("ERRORMSG", checkCoupon_msg);
			if("0".equals(checkCoupon_result) && "B".equals(UD)){//使用模式（先校验后使用）
				List<Map<String,Object>> CP=(List<Map<String,Object>>)map.get("CP");
				int updateCount=0;
				for(Map<String,Object> cp:CP){
					int result_count=updateCouponService.updateCouponStatus(cp);
					if(result_count > 0){
						updateCount += 1;
					}
				}
				if(updateCount == CP.size()){
					returnMap.put("ERRORCODE", "0");
					returnMap.put("ERRORMSG", "OK");
				}else{
					returnMap.put("ERRORCODE", "WSESP0227");
					returnMap.put("ERRORMSG", "状态更新失败");
				}
			}
			return returnMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			returnMap.put("ERRORCODE", "WSE9999");
			returnMap.put("ERRORMSG", "处理过程中发生未知异常");
		}
		return returnMap;
	}
	
	private Map<String,Object> checkParam(Map<String, Object> map){
		Map<String,Object> result_map=new HashMap<String,Object>();
		List<Map<String,Object>> CP=(List<Map<String,Object>>)map.get("CP");
		List<Map<String,Object>> SC=(List<Map<String,Object>>)map.get("SC");
		for(Map<String,Object> cp:CP){
			String CouponCode=ConvertUtil.getString(cp.get("CouponCode"));
			String Password=ConvertUtil.getString(cp.get("Password"));
			if("".equals(CouponCode)){
				result_map.put("ERRORCODE", "WSESP0211");
				result_map.put("ERRORMSG", "您传入的券号为空");
				return result_map;
			}
			//查看数据库中受否存在该优惠券(券号和密码)
			Map<String,Object> coupon_param=new HashMap<String, Object>();
			coupon_param.put("CouponCode", CouponCode);
			coupon_param.put("Password", Password);
			Map<String,Object> couponStatus_map=updateCouponService.getCouponStatus(coupon_param);
			if(null == couponStatus_map){
				result_map.put("ERRORCODE", "WSESP0212");
				result_map.put("ERRORMSG", "没有查询到券相关信息或者已经被使用/废弃");
				return result_map;
			}
			String couponStatus=ConvertUtil.getString(couponStatus_map.get("status"));
			if(!"AR".equals(couponStatus)){
				result_map.put("ERRORCODE", "WSESP0213");
				result_map.put("ERRORMSG", "该优惠券券已经被使用/废弃");
				return result_map;
			}
		}
		for(Map<String,Object> sc:SC){
			String B=ConvertUtil.getString(sc.get("B"));
			String P=ConvertUtil.getString(sc.get("P"));
			String Q=ConvertUtil.getString(sc.get("Q"));
			if("".equals(B)){
				result_map.put("ERRORCODE", "WSESP0214");
				result_map.put("ERRORMSG", "您传入的产品编码为空");
				return result_map;
			}
			if("".equals(P)){
				result_map.put("ERRORCODE", "WSESP0215");
				result_map.put("ERRORMSG", "您传入的产品编码为空");
				return result_map;
			}
			if("".equals(Q)){
				result_map.put("ERRORCODE", "WSESP0216");
				result_map.put("ERRORMSG", "您传入的产品编码为空");
				return result_map;
			}
		}
		String BC =ConvertUtil.getString(map.get("BC"));
		if("".equals(BC)){
			result_map.put("ERRORCODE", "WSESP0217");
			result_map.put("ERRORMSG", "您传入的品牌代号为空");
			return result_map;
		}
		String UD =ConvertUtil.getString(map.get("UD"));
		if("".equals(UD)){
			result_map.put("ERRORCODE", "WSESP0218");
			result_map.put("ERRORMSG", "您传入的使用区分为空");
			return result_map;
		}
		String MD =ConvertUtil.getString(map.get("MD"));
		if("".equals(UD)){
			result_map.put("ERRORCODE", "WSESP0219");
			result_map.put("ERRORMSG", "您传入的场景模式为空");
			return result_map;
		}
		String TN =ConvertUtil.getString(map.get("TN"));
		if("".equals(TN)){
			result_map.put("ERRORCODE", "WSESP0220");
			result_map.put("ERRORMSG", "您传入的单据号为空");
			return result_map;
		}
		String TD =ConvertUtil.getString(map.get("TD"));
		if("".equals(TD)){
			result_map.put("ERRORCODE", "WSESP0221");
			result_map.put("ERRORMSG", "您传入的交易日期为空");
			return result_map;
		}
		String TT =ConvertUtil.getString(map.get("TT"));
		if("".equals(TD)){
			result_map.put("ERRORCODE", "WSESP0222");
			result_map.put("ERRORMSG", "您传入的交易时间为空");
			return result_map;
		}
		String CC =ConvertUtil.getString(map.get("CC"));
		if("".equals(CC)){
			result_map.put("ERRORCODE", "WSESP0223");
			result_map.put("ERRORMSG", "您传入的柜台代号为空");
			return result_map;
		}
		String MC =ConvertUtil.getString(map.get("MC"));
		if("".equals(MC)){
			result_map.put("ERRORCODE", "WSESP0224");
			result_map.put("ERRORMSG", "您传入的会员卡号为空");
			return result_map;
		}
		String MP =ConvertUtil.getString(map.get("MP"));
		if("".equals(MP)){
			result_map.put("ERRORCODE", "WSESP0225");
			result_map.put("ERRORMSG", "您传入的会员手机号为空");
			return result_map;
		}
		String ML =ConvertUtil.getString(map.get("ML"));
		if("".equals(ML)){
			result_map.put("ERRORCODE", "WSESP0226");
			result_map.put("ERRORMSG", "您传入的会员等级为空");
			return result_map;
		}
		return null;
	}
	
}

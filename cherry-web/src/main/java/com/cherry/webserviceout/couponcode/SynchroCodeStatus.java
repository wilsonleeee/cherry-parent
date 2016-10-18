package com.cherry.webserviceout.couponcode;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.SpringBeanManager;

public class SynchroCodeStatus {
	/**
	 * 同步Coupon码状态
	 * @param brandCode
	 * @param param
	 * @return 
	 * 注意，如果没有配置品牌专用的同步Bean，则意味着不需要和第三方同步，
	 * 则默认返回代表成功执行的Map
	 */
	public static Map<String, Object> synchroCodeStatus(String brandCode, Map<String, Object> param) throws Exception{
		Object obj = SpringBeanManager.getBean("synchroCodeStatus_"+brandCode.toUpperCase());
		if(null!=obj){
			//如果品牌配置了专用类
			ISynchroCodeStatus bean = (ISynchroCodeStatus)obj;	
			return bean.synchroCodeStatus(brandCode, param);
		}else{
			Map<String,Object> ret = new HashMap<String,Object>();
			ret.put("ERRORCODE", "0");
			ret.put("ERRORMSG", "");
			return ret;
		}
	}
}

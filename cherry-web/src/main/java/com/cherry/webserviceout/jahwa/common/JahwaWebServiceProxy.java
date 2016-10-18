package com.cherry.webserviceout.jahwa.common;

import java.rmi.RemoteException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.webserviceout.jahwa.Dt_DispMember_req;
import com.cherry.webserviceout.jahwa.Dt_DispMember_res;
import com.cherry.webserviceout.jahwa.Si_DispMember_obProxy;
import com.cherry.webserviceout.jahwa.ZSAL_MEMINFO;
import com.cherry.webserviceout.jahwa.sms.Dt_SMSInsert_req;
import com.cherry.webserviceout.jahwa.sms.Dt_SMSInsert_res;
import com.cherry.webserviceout.jahwa.sms.Si_SMS2Insert_obProxy;


public class JahwaWebServiceProxy {
	
	
	private static Logger logger = LoggerFactory.getLogger(JahwaWebServiceProxy.class);
	public static Si_DispMember_obProxy proxy1 = new Si_DispMember_obProxy();
	
	/**优惠券短信发送 webService代理 **/
	public static Si_SMS2Insert_obProxy proxy15 = new Si_SMS2Insert_obProxy();
	
	
	public static ZSAL_MEMINFO[] getMemberList(Map<String,Object> param){
		ZSAL_MEMINFO[] arr = null;
		Dt_DispMember_req req = new Dt_DispMember_req();
		// 组织代码参数
		req.setZTYPE("0002");
		req.setMOB_NUMBER(ConvertUtil.getString(param.get("MP")));
		Dt_DispMember_res res = null;
		// 访问超时等异常时，重试次数
		int i = Config.RECONNECTION_TIMES;
		while(i > 0){
			try {
				res = proxy1.si_DispMember_ob(req);
				i = 0;
			} catch (RemoteException e) {
				i--;
				// 错误
				logger.error("调用CRM会员信息查询接口错误：【"+e.getMessage()+"】",e);
			}
		}
		if(null != res){
			arr = res.getZCRMT316();
		}
		
		return arr;
		
	}
	
	/**
	 * CBI015-短信发送接口
	 * @param Dt_SMSInsert_req：请求参数
	 * @return Dt_SMSInsert_res 返回结果
	 */
	public static Dt_SMSInsert_res sendSMSInsert(Dt_SMSInsert_req req) throws Exception{
		Dt_SMSInsert_res res = proxy15.si_SMS2Insert_ob(req);
		return res;
	}
	
}

package com.cherry.webservice.communication.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.smg.bl.BINBECTSMG05_BL;
import com.cherry.webservice.common.IWebservice;

public class HotlineRequest implements IWebservice {

	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binBECTSMG05_BL")
	private BINBECTSMG05_BL binBECTSMG05_BL;
	
	@Resource(name = "wsSendMobileMessage")
	private SendMobileMessage sendMobileMessage;
	
	private String POINT_EXC_COUPON = "1";//获取积分兑换验证码
	private String BIR_GIFT_COUPON = "2";//获取生日礼验证码
	
	@Override
	public Map tran_execute(Map map) throws Exception {
		
		//定义返回Map
		Map<String, Object> returnMap = null;
		
		//定义热线类型调用参数
		//定义热线类型调用参数
		String phoneNum = (String) map.get("MobilePhone");//客户呼入的手机号码-仅限于手机,Cherry系统中根据手机匹配会员
		String buttonNumber = (String) map.get("ButtonNumber");//按键号码。用户根据语音提示输入的号码
		
		System.out.println("手机号："+phoneNum+",按键号:"+buttonNumber);
		
		//验证参数合法性
		returnMap = validateParam(map);
		if(returnMap != null){
			return returnMap;
		}
		
		if(this.POINT_EXC_COUPON.equals(buttonNumber)){
			//获取积分兑换验证码
			map.put("EventType", "14");
		}else if(this.BIR_GIFT_COUPON.equals(buttonNumber)){
			//获取生日礼验证码
			map.put("EventType", "15");
		}
		
		returnMap = sendMobileMessage.tran_execute(map);
		return returnMap;
	}
	  
	private Map<String, Object> validateParam(Map<String, Object> map){
		
		Map<String, Object> retMap = null;
		
		//定义热线类型调用参数
		String organizationInfoId = ConvertUtil.getString(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
		String mobilePhone = (String) map.get("MobilePhone");
		String buttonNumber = (String) map.get("ButtonNumber");
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		
		if(!CherryChecker.isPhoneValid(mobilePhone,mobileRule)){
			retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "EGT01001");
			retMap.put("ERRORMSG", "缺少参数（或参数格式不合法）MobilePhone");
			return retMap;
		}
		
		if(CherryChecker.isNullOrEmpty(buttonNumber) 
				|| ( ConvertUtil.getInt(buttonNumber) != 1 && ConvertUtil.getInt(buttonNumber) != 2)){
			retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "EGT01002");
			retMap.put("ERRORMSG", "缺少参数（或参数格式不合法）ButtonNumber");
			return retMap;
		}
		
		if(!binBECTSMG05_BL.checkMemExistByMobilePhone(map)){
			retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "EGT01003");
			retMap.put("ERRORMSG", "给出的手机号未找到对应的会员，无法执行后续操作");
			return retMap;
		}
		
		return retMap;
	}

}

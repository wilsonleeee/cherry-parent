package com.cherry.wp.sal.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.sal.interfaces.BINOLWPSAL02_IF;
import com.cherry.wp.sal.service.BINOLWPSAL02_Service;

public class BINOLWPSAL02_BL implements BINOLWPSAL02_IF{
	
	@Resource(name="binOLWPCM01_BL")
	private BINOLWPCM01_IF binOLWPCM01_IF;
	
	@Resource(name="binOLWPSAL02_Service")
	private BINOLWPSAL02_Service binOLWPSAL02_Service;
	
	public List<Map<String, Object>> getOrderCounterCode(Map<String, Object> map){
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		try {
			resultMap = binOLWPCM01_IF.getOrderCounterCode(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	@Override
	public Map<String, Object> getMemberInfo(Map<String, Object> map)
			throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String, Object> memberInfo = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 根据搜索号获取会员数量
		int memberCount = binOLWPCM01_IF.getMemberCount(map);
		if(memberCount > 0){
			// 根据会员卡号获取的会员数量多于0个的情况下判断是否获取到两个以上的会员
			if(memberCount > 1){
				// 获取到两个以上会员的情况下直接返回异常情况
				resultMap.put("returnStatus", CherryConstants.WP_ERROR_STATUS);
				resultMap.put("errorCode", CherryConstants.WP_ERROR_GETMULTIPLEMEMBER);
				resultMap.put("memberInfo", null);
				return resultMap;
			}else{
				// 根据会员搜索条件获取会员信息
				memberInfo = binOLWPCM01_IF.getMemberInfo(map);
				if(memberInfo != null && !memberInfo.isEmpty()){
					// 获取手机号码
					String mobilePhone = ConvertUtil.getString(memberInfo.get("mobilePhone"));
					// 解密手机号码
					try {
						memberInfo.put("mobilePhone", CherrySecret.decryptData(brandCode, mobilePhone));
					} catch (Exception e) {
						memberInfo.put("mobilePhone", mobilePhone);
					}
					// 设置返回参数
					resultMap.put("returnStatus", CherryConstants.WP_SUCCESS_STATUS);
					resultMap.put("errorCode", CherryConstants.WP_SUCCESS_CODE);
					resultMap.put("memberInfo", memberInfo);
				}else{
					// 根据会员搜索条件未能获取到会员信息的情况
					resultMap.put("returnStatus", CherryConstants.WP_ERROR_STATUS);
					resultMap.put("errorCode", CherryConstants.WP_ERROR_NOTGETMEMBER);
					resultMap.put("memberInfo", memberInfo);
				}
				return resultMap;
			}
		}else{
			// 未能获取到会员的情况下返回异常情况
			resultMap.put("returnStatus", CherryConstants.WP_ERROR_STATUS);
			resultMap.put("errorCode", CherryConstants.WP_ERROR_NOTGETMEMBER);
			resultMap.put("memberInfo", null);
			return resultMap;
		}
	}

	@Override
	public String getUserBindCounterCode(Map<String, Object> map)
			throws Exception {
		return binOLWPSAL02_Service.getUserBindCounterCode(map);
	}
	@Override
	public String getCounterPhone(Map<String, Object> map) {
		return binOLWPSAL02_Service.getCounterPhone(map);
	}
	@Override
	public String getCounterAddress(Map<String, Object> map) {
		return binOLWPSAL02_Service.getCounterAddress(map);
	}
	
}

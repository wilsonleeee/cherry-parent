package com.cherry.mb.cct.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.cct.interfaces.BINOLMBCCT01_IF;
import com.cherry.mb.cct.service.BINOLMBCCT01_Service;

public class BINOLMBCCT01_BL implements BINOLMBCCT01_IF{
	
	@Resource
	private BINOLMBCCT01_Service binolmbcct01_Service;
	
	@Override
	public int getMemberCountByPhone(Map<String, Object> map) {
		// 根据来电号码获取匹配的会员数量
		return binolmbcct01_Service.getMemberCountByPhone(map);
	}
	
	@Override
	public int getCustomerCountByPhone(Map<String, Object> map) {
		// 根据来电号码获取匹配的会员数量
		return binolmbcct01_Service.getCustomerCountByPhone(map);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getMemberListByPhone(Map<String, Object> map) {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		List<Map<String, Object>> memberList = binolmbcct01_Service.getMemberListByPhone(map);
		// 对加密信息进行解密
		memberList = getCustomerDecrypt(brandCode, memberList);
		return memberList;
	}

	@Override
	public void saveCallLog(Map<String, Object> map, String type)
			throws Exception {
		// 插表时的共通字段
		Map<String, Object> insertMap = new HashMap<String, Object>();
		// 作成程序名
		insertMap.put(CherryConstants.CREATEPGM, "BINOLMBCCT01");
		// 更新程序名
		insertMap.put(CherryConstants.UPDATEPGM, "BINOLMBCCT01");
		// 增加共通字段
		map.putAll(insertMap);
		if(type.equals("INSERT")){
			binolmbcct01_Service.insertCallLog(map);
		}else{
			binolmbcct01_Service.updateCallLog(map);
		}
	}

	@Override
	public String getMemberIdByPhone(Map<String, Object> map) {
		String memId = "";
		List<Map<String, Object>> memberList = binolmbcct01_Service.getMemberListByPhone(map);
		if(memberList != null && !memberList.isEmpty()) {
    		for(Map<String,Object> memberMap : memberList){
    			memId = ConvertUtil.getString(memberMap.get("memId"));
    			break;
    		}
		}
		return memId;
	}

	@Override
	public String getCustomerIdByPhone(Map<String, Object> map) {
		String customerId = "";
		List<Map<String, Object>> customerList = binolmbcct01_Service.getCustomerListByPhone(map);
		if(customerList != null && !customerList.isEmpty()) {
    		for(Map<String,Object> customerMap : customerList){
    			customerId = ConvertUtil.getString(customerMap.get("customerId"));
    			break;
    		}
		}
		return customerId;
	}
	
	// 对会员加密的信息进行解密
	public List<Map<String, Object>> getCustomerDecrypt(String brandCode, List<Map<String, Object>> customerList) {
		// 对加密字段进行解密
		if(customerList != null && !customerList.isEmpty()){
			for(Map<String,Object> customerMap : customerList){
				try{
					// 会员【手机号码】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("mobilePhone"), true)) {
						String mobilePhone = ConvertUtil.getString(customerMap.get("mobilePhone"));
						customerMap.put("mobilePhone", CherrySecret.decryptData(brandCode, mobilePhone));
					}
					// 会员【电话号码】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("telephone"), true)) {
						String telephone = ConvertUtil.getString(customerMap.get("telephone"));
						customerMap.put("telephone", CherrySecret.decryptData(brandCode, telephone));
					}
					// 会员【电子邮箱】字段解密
					if (!CherryChecker.isNullOrEmpty(customerMap.get("email"), true)) {
						String email = ConvertUtil.getString(customerMap.get("email"));
						customerMap.put("email", CherrySecret.decryptData(brandCode, email));
					}
				}catch(Exception e){
					// 解密失败的情况，不做处理
				}
			}
		}
		return customerList;
	}
}

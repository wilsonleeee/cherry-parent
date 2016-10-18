/*
 * @(#)BINOLMBMBM21_BL.java     1.0 2013.08.01
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.mb.mbm.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM02_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM12_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM21_Service;

/**
 * 推荐会员画面BL
 * 
 * @author WangCT
 * @version 1.0 2013.08.01
 */
public class BINOLMBMBM21_BL {
	
	/** 推荐会员画面Service */
	@Resource
	private BINOLMBMBM21_Service binOLMBMBM21_Service;
	
	/** 会员详细画面Service */
	@Resource
	private BINOLMBMBM02_Service binOLMBMBM02_Service;
	
	/** 会员资料修改履历查询画面Service */
	@Resource
	private BINOLMBMBM12_Service binOLMBMBM12_Service;
	
	/** 会员资料修改履历查询画面BL */
	@Resource
	private BINOLMBMBM12_BL binOLMBMBM12_BL;
	
	/**
	 * 查询推荐者基本信息
	 * 
	 * @param map 检索条件
	 * @return 推荐者基本信息
	 * @throws Exception 
	 */
	public Map<String, Object> getReferrerInfo(Map<String, Object> map) throws Exception {
		
		// 查询推荐者基本信息
		Map<String, Object> referrerMap = binOLMBMBM21_Service.getReferrerInfo(map);
		if(referrerMap != null && !referrerMap.isEmpty()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("memberInfoId", map.get("referrerId"));
			// 查询推荐者地址信息
			List<Map<String, Object>> addressList = binOLMBMBM02_Service.getMemberAddress(paramMap);
			if(addressList != null && !addressList.isEmpty()) {
				referrerMap.putAll(addressList.get(0));
			}
			// 会员【手机】字段解密
	  		if (!CherryChecker.isNullOrEmpty(referrerMap.get("mobilePhone"), true)) {
	  			String brandCode = ConvertUtil.getString(map.get("brandCode"));
	  			String mobilePhone = ConvertUtil.getString(referrerMap.get("mobilePhone"));
	  			referrerMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
	  		}
		}
		return referrerMap;
	}
	
	/**
	 * 查询推荐过的会员总数
	 * 
	 * @param map 检索条件
	 * @return 推荐过的会员总数
	 */
	public int getReferCount(Map<String, Object> map) {
		
		// 查询推荐过的会员总数
		return binOLMBMBM21_Service.getReferCount(map);
	}
	
	/**
	 * 查询推荐过的会员List
	 * 
	 * @param map 检索条件
	 * @return 推荐过的会员List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getReferList(Map<String, Object> map) throws Exception {
		
		// 查询推荐过的会员List
		List<Map<String, Object>> referList = binOLMBMBM21_Service.getReferList(map);
		if(referList != null && !referList.isEmpty()) {
			// 取得所有区域信息List
			List<Map<String, Object>> regionList = binOLMBMBM12_Service.getRegionList(map);
			for(Map<String, Object> referMap : referList) {
				Object standardProvince = referMap.get("standardProvince");
				if(standardProvince != null && !"".equals(standardProvince.toString())) {
					referMap.put("standardProvince", binOLMBMBM12_BL.getNameByKey(standardProvince.toString(), regionList));
				}
				Object standardCity = referMap.get("standardCity");
				if(standardCity != null && !"".equals(standardCity.toString())) {
					referMap.put("standardCity", binOLMBMBM12_BL.getNameByKey(standardCity.toString(), regionList));
				}
				// 会员【电话】字段解密
				if (!CherryChecker.isNullOrEmpty(referMap.get("mobilePhone"), true)) {
					String brandCode = ConvertUtil.getString(map.get("brandCode"));
					String mobilePhone = ConvertUtil.getString(referMap.get("mobilePhone"));
					referMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
				}
			}
		}
		return referList;
	}

}

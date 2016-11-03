/*	
 * @(#)BINBECTSMG09_BL.java     1.0 2016/05/02		
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
package com.cherry.ct.smg.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.ct.smg.interfaces.BINBECTSMG09_IF;
import com.cherry.ct.smg.service.BINBECTSMG09_Service;

/**
 * 短信签名管理BL
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public class BINBECTSMG09_BL implements BINBECTSMG09_IF{
	
	@Resource
	private BINBECTSMG09_Service binBECTSMG09_Service;
	
	/** 短信签名 */
	private static Map<String, String> signMap = new HashMap<String, String>();
	
	/**
     * 取得短信签名
     * 
     * @param brandCode 品牌代码
     * @return String 短信签名
     */
	public String getSignName(String brandCode) {
		// 短信签名
		String signName = signMap.get(brandCode);
		if (null == signName) {
			synchronized (signMap) {
				signName = signMap.get(brandCode);
				if (null == signName) {
					// 取得品牌的短信签名
					signMap.put(brandCode, getBrandSignName(brandCode));
				}
			}
		}
		return signName;
	}
	
	/**
     * 刷新品牌的短信签名
     * 
     * @param brandCode 品牌代码
     * @return
     */
	private void upSignName(String brandCode) {
		synchronized (signMap) {
			// 取得品牌的短信签名
			signMap.put(brandCode, getBrandSignName(brandCode));
		}
	}
	
	/**
     * 取得品牌的短信签名
     * 
     * @param map
     * @return String
     * 		短信签名
     */
    public String getBrandSignName(String brandCode) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("brandCode", brandCode);
    	// 取得品牌的短信签名
    	return binBECTSMG09_Service.getBrandSignName(map);
    }
    
    /**
     * 更新短信签名
     * 
     * @param map
     * @return int
     * 		执行结果
     */
	public int tran_upSignName(Map<String, Object> map) {
		// 共通的参数设置
		commParamsForUp(map);
		// 更新短信签名
		int result = binBECTSMG09_Service.updateSignNameInfo(map);
		if (0 == result) {
			// 新增短信签名
			binBECTSMG09_Service.addSignNameInfo(map);
		}
		// 刷新品牌的短信签名
		upSignName((String) map.get("brandCode"));
		return CherryBatchConstants.BATCH_SUCCESS;
	}
	
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBECTSMG09");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBECTSMG09");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBECTSMG09");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBECTSMG09");
	}
}

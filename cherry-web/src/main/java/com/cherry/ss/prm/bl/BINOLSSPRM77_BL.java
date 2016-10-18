/*		
 * @(#)BINOLSSPRM73_BL.java     1.0 2016/05/05		
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
package com.cherry.ss.prm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.BaseDTO;
import com.cherry.ss.prm.dto.CouponDTO;
import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.dto.ResultDTO;
import com.cherry.ss.prm.interfaces.BINOLSSPRM73_IF;
import com.cherry.ss.prm.interfaces.BINOLSSPRM75_IF;
import com.cherry.ss.prm.service.BINOLSSPRM73_Service;
import com.cherry.ss.prm.service.BINOLSSPRM75_Service;
import com.cherry.ss.prm.service.BINOLSSPRM77_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 优惠券BL
 * @author ghq
 * @version 1.0 2016.05.05
 */
public class BINOLSSPRM77_BL{
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSSPRM77_BL.class);
	
	/** 优惠券Service */
	@Resource
	private BINOLSSPRM77_Service binOLSSPRM77_Service;

	public int getCouponInfoCount(Map<String, Object> map) {	
		return  binOLSSPRM77_Service.getCouponInfoCount(map);
	}

	public List<Map<String, Object>> getCouponInfoList(Map<String, Object> map) {
		return  binOLSSPRM77_Service.getCouponInfoList(map);
	}

	public Map<String, Object> getCouponInfo(Map<String, Object> map) {
		return binOLSSPRM77_Service.getCouponInfo(map);
	}
	
	/**
	 * 通过brandCode获取品牌组织信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBrandInfo(Map<String, Object> map){
		return binOLSSPRM77_Service.getBrandInfo(map);
	}
	
}

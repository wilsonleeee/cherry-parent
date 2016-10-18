/*
 * @(#)BINOLCM41_Service.java v1.0 2014-11-6
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
package com.cherry.cm.cmbussiness.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

/**
 * 产品基础信息   共通Service
 * 
 * @author JiJW
 * @version 1.0 2014-11-6
 */
public class BINOLCM41_Service extends BaseService {

	/**
	 * 取得柜台对应的产品列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getCntProductInfo(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM41.getCntProductInfo");
	}
	
	/**
	 * 取得标准产品数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getStandProductInfo(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM41.getStandProductInfo");
	}
}

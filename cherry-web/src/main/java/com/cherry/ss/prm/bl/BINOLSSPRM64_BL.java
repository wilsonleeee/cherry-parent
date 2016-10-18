/*
 * @(#)BINOLSSPRM64_BL.java     1.0 2013/01/25
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.interfaces.BINOLSSPRM64_IF;
import com.cherry.ss.prm.service.BINOLSSPRM64_Service;

/**
 * 
 * 入库一览BL
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSPRM64_BL implements BINOLSSPRM64_IF{

	@Resource(name="binOLSSPRM64_Service")
	private BINOLSSPRM64_Service binOLSSPRM64_Service;
	
    /**
     * 取得入库单总数
     * 
     * @param map
     * @return 
     */
	@Override
	public int getPrmInDepotCount(Map<String, Object> map) {
		return binOLSSPRM64_Service.getPrmInDepotCount(map);
	}

	/**
	 * 取得入库单list
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPrmInDepotList(Map<String, Object> map) {
		return binOLSSPRM64_Service.getPrmInDepotList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLSSPRM64_Service.getSumInfo(map);
	}
}
/*
 * @(#)BINOLSSPRM65_Service.java     1.0 2013/01/25
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
package com.cherry.ss.prm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

/**
 * 
 * 入库明细Service
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSPRM65_Service extends BaseService {

    /**
     * 根据产品入库单据ID删除该单据内所有的明细
     * @param map
     */
	public void deletePrmInDepotDetail(Map<String,Object> map){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		baseServiceImpl.deleteAll(list, "BINOLSSPRM65.deletePrmInDepotDetail");
	}
}
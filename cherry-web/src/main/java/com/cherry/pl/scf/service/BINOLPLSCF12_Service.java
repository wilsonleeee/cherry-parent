/*
 * @(#)BINOLPLSCF12_Service.java     1.0 2010/10/27
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

package com.cherry.pl.scf.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * code值管理详细Service
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF12_Service extends BaseService {

	/**
	 * 取得code值详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCoderDetail(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF12.getCoderDetail");
		return (Map<String, Object>)baseConfServiceImpl.get(map);
	}
}

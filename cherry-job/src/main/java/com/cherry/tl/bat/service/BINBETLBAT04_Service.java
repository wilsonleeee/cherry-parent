/*
 * @(#)BINBETLBAT03_Service.java     1.0 2011/07/13
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
package com.cherry.tl.bat.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 更新业务日期service
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/07/13
 */
public class BINBETLBAT04_Service extends BaseService {
	
	/**
	 * 
	 * 更新业务日期
	 * 
	 * @param map 更新条件
	 * @return 无
	 * 
	 */	
	public void updateCloseFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT04.updateCloseFlag");
		baseServiceImpl.update(paramMap);
	}

}

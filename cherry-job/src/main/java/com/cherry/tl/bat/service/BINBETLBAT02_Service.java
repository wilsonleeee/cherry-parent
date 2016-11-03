/*
 * @(#)BINBETLBAT02_Service.java     1.0 2010/1/14
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
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 
 *job运行日志写入数据库
 * 
 * 
 * @author zhangjie
 * @version 1.0 2010.1.14
 */
public class BINBETLBAT02_Service extends BaseService {

	/**
	 * 插入新纪录到job运行日志表
	 * 
	 * @param 无
	 * 
	 * 
	 * @return 无
	 * 
	 */	
	public void insertBatchLog(Map<String,Object> paramMap) {
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBETLBAT02.insertBatchLog");
		baseServiceImpl.save(paramMap);
	}

}

/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/11
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
package com.cherry.ct.common.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.interfaces.BINOLCTCOM02_IF;
import com.cherry.ct.common.service.BINOLCTCOM02_Service;

/**
 * 新建沟通计划BL
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM02_BL implements BINOLCTCOM02_IF{
	@Resource
	private BINOLCTCOM02_Service binOLCTCOM02_Service;
	
	@Override
	public List<Map<String, Object>> getSearchRecordList(Map<String, Object> map) {
		return binOLCTCOM02_Service.getSearchRecordList(map);
	}

	@Override
	public int getSearchRecordCount(Map<String, Object> map) {
		return binOLCTCOM02_Service.getSearchRecordCount(map);
	}

	@Override
	public int getSendType(Map<String, Object> map) {
		List<Map<String, Object>> list=binOLCTCOM02_Service.getSendType(map);
		int result = 3;
		for(Map<String,Object> m:list){
			result=Integer.parseInt(ConvertUtil.getString(m.get("runStatus")));
		}
		return result;
	}
	
	
}

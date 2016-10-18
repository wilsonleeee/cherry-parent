/*
 * @(#)BINOLBSRES01_BL.java     1.0 2014/10/29
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
package com.cherry.bs.wem.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.wem.interfaces.BINOLBSWEM03_IF;
import com.cherry.bs.wem.service.BINOLBSWEM03_Service;


/**
 * 设置返点分成
 * 
 * @author 张博
 * @version 1.0 2015/08/13
 */
public class BINOLBSWEM03_BL implements BINOLBSWEM03_IF{
	
	@Resource(name="binOLBSWEM03_Service")
	private BINOLBSWEM03_Service binOLBSWEM03_Service;

	@Override
	public void save(List<Map<String, Object>> list) {
		binOLBSWEM03_Service.save(list);
	}
	
	public List<Map<String, Object>> getDivideConfList() {
		return binOLBSWEM03_Service.getDivideConfList();
	}
		
}
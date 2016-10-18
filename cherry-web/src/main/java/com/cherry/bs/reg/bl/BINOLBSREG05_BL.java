/*
 * @(#)BINOLBSREG05_BL.java     1.0 2011/11/23
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
package com.cherry.bs.reg.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.interfaces.BINOLBSREG05_IF;
import com.cherry.bs.reg.service.BINOLBSREG05_Service;

/**
 * 区域启用停用画面BL
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG05_BL implements BINOLBSREG05_IF {
	
	/** 区域启用停用画面Service */
	@Resource
	private BINOLBSREG05_Service binOLBSREG05_Service;

	/**
	 * 启用停用区域处理
	 * 
	 * @param map 查询条件
	 */
	@Override
	public void tran_updateRegValidFlag(Map<String, Object> map) {
		
		// 启用停用区域处理
		binOLBSREG05_Service.updateRegValidFlag(map);
	}

}

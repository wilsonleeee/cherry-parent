/*
 * @(#)BINOLBSPOS10_Service.java     1.0 2011/11/04
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
package com.cherry.bs.pos.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS10_Service;

/**
 * 启用停用岗位画面BL
 * 
 * @author WangCT
 * @version 1.0 2011/11/04
 */
public class BINOLBSPOS10_BL {
	
	/** 启用停用岗位画面Service */
	@Resource
	private BINOLBSPOS10_Service binOLBSPOS10_Service;
	
	/**
	 * 启用停用岗位处理
	 * 
	 * @param map 更新条件
	 * 
	 */
	public void tran_updatePosition(Map<String, Object> map) throws Exception {
		
		// 停用启用岗位
		binOLBSPOS10_Service.updatePosition(map);		
	}

}

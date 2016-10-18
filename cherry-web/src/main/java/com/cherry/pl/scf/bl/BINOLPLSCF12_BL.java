/*
 * @(#)BINOLPLSCF12_BL.java     1.0 2010/10/27
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

package com.cherry.pl.scf.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.scf.service.BINOLPLSCF12_Service;

/**
 * code值详细BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF12_BL {
	
	/** code值详细Service */
	@Resource
	private BINOLPLSCF12_Service binolplscf12Service;

	/**
	 * 取得code值管理详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCoderDetail(Map<String, Object> map) {
		
		return binolplscf12Service.getCoderDetail(map);
	}


}

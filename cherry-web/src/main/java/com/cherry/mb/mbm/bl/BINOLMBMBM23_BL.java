/*
 * @(#)BINOLMBMBM23_BL.java     1.0 2013.08.29
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
package com.cherry.mb.mbm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.mbm.service.BINOLMBMBM23_Service;

/**
 * 会员短信沟通明细画面BL
 * 
 * @author WangCT
 * @version 1.0 2013.08.29
 */
public class BINOLMBMBM23_BL {
	
	/** 会员短信沟通明细画面service **/
	@Resource
	private BINOLMBMBM23_Service binOLMBMBM23_Service;
	
	/**
	 * 取得会员短信沟通总数
	 * 
	 * @param map 检索条件
	 * @return 会员短信沟通总数
	 */
	public int getSmsSendDetailCount(Map<String, Object> map) {
		
		// 取得会员短信沟通总数
		return binOLMBMBM23_Service.getSmsSendDetailCount(map);
	}
	
	/**
	 * 取得会员短信沟通List
	 * 
	 * @param map 检索条件
	 * @return 会员短信沟通List
	 */
	public List<Map<String, Object>> getSmsSendDetailList(Map<String, Object> map) {
		
		// 取得会员短信沟通List
		return binOLMBMBM23_Service.getSmsSendDetailList(map);
	}

}

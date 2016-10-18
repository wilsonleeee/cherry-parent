/*	
 * @(#)BINOLJNCOM02_BL.java     1.0 2011/4/18		
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
package com.cherry.jn.common.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.jn.common.interfaces.BINOLJNCOM02_IF;
import com.cherry.jn.common.service.BINOLJNCOM02_Service;

/**
 * 会员活动组一览 BL
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM02_BL implements BINOLJNCOM02_IF{
	
	@Resource
	private BINOLJNCOM02_Service binoljncom02_Service;
	
	/**
	 * 取得会员活动组总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getCampaignGrpCount(Map<String, Object> map) {
		return binoljncom02_Service.getCampaignGrpCount(map);
	}
	
	/**
	 * 取得会员活动组信息List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List getCampaignGrpList (Map<String, Object> map) {
		return binoljncom02_Service.getCampaignGrpList(map);
	}

}

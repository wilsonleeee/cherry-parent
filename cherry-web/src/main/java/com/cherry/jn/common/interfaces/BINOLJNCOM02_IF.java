/*	
 * @(#)BINOLJNCOM02_IF.java     1.0 2011/4/18		
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
package com.cherry.jn.common.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 会员活动组一览 IF
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public interface BINOLJNCOM02_IF {
	
	/**
	 * 取得会员活动组总数
	 * 
	 * @param map
	 * @return
	 */
	public int getCampaignGrpCount(Map<String, Object> map);
	
	/**
	 * 取得会员活动组信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getCampaignGrpList (Map<String, Object> map);

}

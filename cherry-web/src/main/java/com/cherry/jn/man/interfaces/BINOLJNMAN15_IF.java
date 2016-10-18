/*	
 * @(#)BINOLJNMAN15_IF.java     1.0 2013/08/28		
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
package com.cherry.jn.man.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 积分清零 IF
 * 
 * @author hub
 * @version 1.0 2013.08.28
 */
public interface BINOLJNMAN15_IF {
	
	/**
     * 取得会员子活动条数
     * 
     * @param map
     * @return
     * 		会员子活动条数
     */
    public int getCampaignRuleCount(Map<String, Object> map);
    
    /**
     * 取得会员活动List
     * 
     * @param map
     * @return
     * 		会员子活动List
     */
    public List<Map<String, Object>> getCampaignList(Map<String, Object> map);
    
    /**
   	 * 停用或者启用规则
   	 * 
   	 * @param map
   	 * @throws Exception
   	 */
   	public void tran_editValid(Map<String, Object> map) throws Exception;

}

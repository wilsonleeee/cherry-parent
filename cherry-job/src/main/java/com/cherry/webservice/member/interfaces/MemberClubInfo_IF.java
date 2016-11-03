/*  
 * @(#)MemberClubInfo_IF.java     1.0 2014/12/08      
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
package com.cherry.webservice.member.interfaces;

import java.util.Map;

/**
 * 
 * 下发俱乐部Interfaces
 * 
 * @author HUB
 * @version 1.0 2014.12.08
 */
public interface MemberClubInfo_IF {
	
	/**
     * 下发会员俱乐部信息
     * @param paramMap
     * @return
     */
    public Map<String, Object> sendClubInfo(Map<String, Object> params);

}

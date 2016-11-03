/*  
 * @(#)MemberPointInfo_IF.java     1.0 2014/12/08      
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
 * 积分业务Interfaces
 * 
 * @author niushunjie
 * @version 1.0 2014.08.01
 */
public interface MemberPointInfo_IF {
    
    /**
     * 校验接口数据，组成MQ消息体，写入老后台MQ_Log表，并发送MQ
     * @param paramMap
     * @return
     */
    public Map<String, Object> tran_changeMemberPointToMQ(Map<String, Object> paramMap);
}
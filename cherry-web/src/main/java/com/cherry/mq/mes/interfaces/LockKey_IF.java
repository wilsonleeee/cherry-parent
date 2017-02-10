/*	
 * @(#)BINBEMQMES10_IF.java     1.0 2012/12/6		
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
package com.cherry.mq.mes.interfaces;

import java.util.Map;

/**
 * 对象锁 IF
 */
public interface LockKey_IF {

    /**
     * 生成对象锁key
     * @param map
     * @return
     */
    public String generateLockKey (Map<String, Object> map);
}
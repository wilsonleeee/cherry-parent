/*
 * @(#)BINOLWSMNG05_IF.java     1.0 2014/10/20
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
package com.cherry.wp.ws.mng.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * 调出IF
 * 
 * @author niushunjie
 * @version 1.0 2014.10.20
 */
public interface BINOLWSMNG05_IF {
	
    public int getProductAllocationCount(Map<String, Object> map);
    
    public List<Map<String, Object>> getProductAllocationList(Map<String, Object> map);	
}

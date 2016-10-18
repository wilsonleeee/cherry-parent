/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/11
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
package com.cherry.ct.common.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.06
 */
public interface BINOLCTCOM02_IF {
	
	public List<Map<String, Object>> getSearchRecordList(Map<String, Object> map);
	
	public int getSearchRecordCount(Map<String, Object> map);
	
	public int getSendType(Map<String,Object> map);
}
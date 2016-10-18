/*
 * @(#)BINOLCTRPT03_IF.java     1.0 2013/08/06
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
package com.cherry.ct.rpt.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLCTRPT03_IF extends BINOLCM37_IF{
	
	public int getMsgDetailCount(Map<String, Object> map);
	
	@SuppressWarnings("rawtypes")
	public List getMsgDetailList (Map<String, Object> map) throws Exception;
	
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	public void tran_sendMsgAgain(Map<String, Object> map) throws Exception;
	
	public String export(Map<String, Object> map) throws Exception;
}

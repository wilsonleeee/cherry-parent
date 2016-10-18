/*
 * @(#)BINOLCTTPL03_IF.java     1.0 2013/10/08
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
package com.cherry.ct.tpl.interfaces;

import java.util.Map;

/**
 * 沟通模板参数一览 IF
 * 
 * @author ZhangLe
 * @version 1.0 2013.10.08
 */
public interface BINOLCTTPL03_IF {
	
	public void tran_updateVariable(Map<String, Object> map);
	
	public int disOrEnable(Map<String, Object> map);
	
}

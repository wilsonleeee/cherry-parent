/*
 * @(#)BINOLCTCOM05_IF.java     1.0 2013.06.06
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

import java.util.Map;
/**
 * 
 * 
 * @author ZhangGS
 * @version 1.0 2013.06.06
 */
public interface BINOLCTCOM05_IF {
	/**
	 * 发送测试信息
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public void tran_sendTestMsg (Map<String, Object> map) throws Exception;
}

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

import java.util.Map;

/**
 * 
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.06
 */
public interface BINOLCTCOM01_IF {
	/**
	 * 获取沟通模板变量
	 * 
	 * @param Map
	 * @return 模板变量List
	 * 			
	 * @throws Exception 
	 */
	public Map<String, Object> getPlanInfo (Map<String, Object> map) throws Exception;
}
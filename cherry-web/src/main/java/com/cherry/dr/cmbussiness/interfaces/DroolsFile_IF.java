/*	
 * @(#)DroolsFile_IF.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.interfaces;

/**
 * 会员活动规则文件 IF
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public interface DroolsFile_IF {
	/**
	 * 取得规则文件的文件头信息
	 * 
	 * @param
	 * 
	 * @return 	String
	 * 				规则文件的文件头信息
	 */
	public String getRuleHeader() throws Exception;
	
}

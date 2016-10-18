/*	
 * @(#)BINOLCM34_IF.java     1.0 @2013-1-9		
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
package com.cherry.cm.cmbussiness.interfaces;

import java.util.Map;

/**
 *
 * 帮助
 *
 * @author jijw
 *
 * @version  2013-1-9
 */
public interface BINOLCM34_IF {
	
	/**
	 * 取得菜单ID对应的是否有帮助文档(HelpFlag)
	 * @param map
	 * @return
	 */
	public String getHelpFlagByMenu(Map<String, Object> map);

}

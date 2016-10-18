/*	
 * @(#)BINOLCM34_BL.java     1.0 @2013-1-9		
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
package com.cherry.cm.cmbussiness.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM34_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM34_Service;

/**
 *
 * 帮助BL
 *
 * @author jijw
 *
 * @version  2013-1-9
 */
public class BINOLCM34_BL implements BINOLCM34_IF {

	@Resource
	private BINOLCM34_Service binOLCM34_Service;
	
	/**
	 * 取得菜单ID对应的是否有帮助文档(HelpFlag)
	 * @param map
	 * @return
	 */
	public String getHelpFlagByMenu(Map<String, Object> map) {
		return binOLCM34_Service.getHelpFlagByMenu(map);
	}

}

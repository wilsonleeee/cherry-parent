/*  
 * @(#)BINOLBSCHA01_Action.java     1.0 2011/05/31      
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
package com.webconsole.bl;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BatchWebException;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.JsclPBKDF2WithHMACSHA256;
import com.webconsole.service.LoginService;
import com.webconsole.service.ViewBatchHistoryService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ViewBatchHistoryLogic {
	@Resource
	private ViewBatchHistoryService viewBatchHistoryService;

	/**
	 *  取得Job运行履历
	 *
	 * @param map
	 * @return List<Map<String,Object>>
	 * 		运行履历
	 */
	public List<Map<String,Object>> getJobFailureRunHistory(Map<String, Object> map) {
		return viewBatchHistoryService.getJobFailureRunHistory(map);
	}
}

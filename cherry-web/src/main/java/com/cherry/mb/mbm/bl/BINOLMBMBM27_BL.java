/*
 * @(#)BINOLMBMBM27_BL.java     1.0 2013.09.23
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
package com.cherry.mb.mbm.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.mb.mbm.service.BINOLMBMBM27_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM28_Service;

/**
 * 添加会员问题画面BL
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM27_BL {
	
	/** 添加会员问题画面Service **/
	@Resource
	private BINOLMBMBM27_Service binOLMBMBM27_Service;
	
	/** 取得各种业务类型的单据流水号  **/
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 会员问题处理画面Service **/
	@Resource
	private BINOLMBMBM28_Service binOLMBMBM28_Service;
	
	/**
	 * 添加会员问题
	 * 
	 * @param map 添加内容
	 */
	public int tran_addIssue(Map<String, Object> map) {
		
		// 取得会员问题单号
		String issueNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()), 
				Integer.parseInt(map.get("brandInfoId").toString()), "", CherryConstants.MSG_MEMBER_MA);
		map.put("issueNo", issueNo);
		
		String sysDate = binOLMBMBM27_Service.getSYSDateTime();
		map.put("createTime", sysDate);
		
		// 解决结果
		String resolution = (String)map.get("resolution");
		// 如果解决结果为未解决那么设置问题状态为待处理，否则设置为已解决
		if(resolution == null || "0".equals(resolution)) {
			map.put("issueStatus", "0");
		} else {
			map.put("issueStatus", "2");
			map.put("resolutionDate", sysDate);
		}
		
		// 添加会员问题
		int issueId = binOLMBMBM27_Service.addIssue(map);
		map.put("issueId", issueId);
		
		// 解决方案
		String actionBody = (String)map.get("actionBody");
		// 解决方案不为空的场合添加解决方案
		if(actionBody != null && !"".equals(actionBody)) {
			map.put("actionBodyAdd", actionBody);
			// 添加会员问题处理内容
			binOLMBMBM28_Service.addIssueAction(map);
		}
		return issueId;
	}

}

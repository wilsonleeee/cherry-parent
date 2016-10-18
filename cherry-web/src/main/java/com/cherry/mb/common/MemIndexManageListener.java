/*
 * @(#)MemIndexManageListener.java     1.0 2011/12/29
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
package com.cherry.mb.common;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;

/**
 * 刷新会员索引的消息处理器
 * 
 * @author WangCT
 * @version 1.0 2011/12/29
 */
public class MemIndexManageListener implements CherryMessageHandler_IF {

	/** 访问WebService共通BL **/
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/**
     * 刷新会员索引的消息处理
     * @param map 消息信息
     * @throws Exception
     */
	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		
		// 会员ID
		String memberInfoId = (String)map.get("memberInfoId");
		if(memberInfoId == null || "".equals(memberInfoId)) {
			// 消息体中的会员ID为空
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_62);
		}
		// 通过webService实时刷新索引数据
		boolean result = binOLCM27_BL.realTimeRefreshMemData(map);
		if(!result) {
			// 通过webService实时刷新索引数据失败
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_63);
		}
	}

}

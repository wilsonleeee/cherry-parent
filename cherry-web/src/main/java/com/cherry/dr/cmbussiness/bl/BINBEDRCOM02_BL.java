/*	
 * @(#)BINBEDRCOM02_BL.java     1.0 2011/09/02	
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
package com.cherry.dr.cmbussiness.bl;

import javax.annotation.Resource;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.mq.MQLogDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM02_IF;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM02_Service;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;

/**
 * MQ收发日志表共通处理 BL
 * 
 * @author hub
 * @version 1.0 2011.09.02
 */
public class BINBEDRCOM02_BL implements BINBEDRCOM02_IF{
	
	@Resource
	private MessageSender messageSender;
	
	@Resource
	private BINBEDRCOM02_Service binbedrcom02_Service;
	
	/**
	 * 保存并发送MQ消息
	 * 
	 * @param mqLogDTO
	 * 				MQ收发日志DTO
	 * @throws Exception 
	 */
	@Override
	public void saveAndSendMsg(MQLogDTO mqLogDTO) throws Exception {
		if (null != mqLogDTO) {
			if (null == mqLogDTO.getSource() || "".equals(mqLogDTO.getSource())) {
				// 数据插入方标志:CHERRY
				mqLogDTO.setSource(DroolsConstants.MQ_SOURCE_CHERRY);
			}
			if (null == mqLogDTO.getSendOrRece() || "".equals(mqLogDTO.getSendOrRece())) {
				// 消息方向:发送
				mqLogDTO.setSendOrRece(DroolsConstants.MQ_SENDORRECE_S);
			}
			// 作成者
			mqLogDTO.setCreatedBy(DroolsConstants.PGM_BINBEDRCOM02);
			// 更新者
			mqLogDTO.setUpdatedBy(DroolsConstants.PGM_BINBEDRCOM02);
			// 做成程序名
			mqLogDTO.setCreatePGM(DroolsConstants.PGM_BINBEDRCOM02);
			// 更新程序名
			mqLogDTO.setUpdatePGM(DroolsConstants.PGM_BINBEDRCOM02);
			// 消息体
			String msg = mqLogDTO.getData();
			if (null == msg || "".equals(msg)) {
				// MQ消息体为空
				String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00005, new String[]{mqLogDTO.getBillCode()});
				throw new CherryDRException(errMsg);
			}
			// 发送MQ消息
			messageSender.sendMessage(msg);
			// 插入MQ收发日志表
			binbedrcom02_Service.insertMQLog(mqLogDTO);
		}
	}
}

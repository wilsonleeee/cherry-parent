package com.mqhelper.common.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.mqhelper.common.service.BINBEMQHELPERCM99_Service;

/*  
 * @(#)BINBEMQHELPERCM99_BL.java    1.0 2012-2-22     
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
public class BINBEMQHELPERCM99_BL {

	@Resource
	private BINBEMQHELPERCM99_Service binBEMQHELPERCM99_Service;
	
	/**
	 * 在进行MQLog日志写入前期做验证
	 * 
	 * 
	 * */
	public int insertMQ_Log(Map<String,Object> mqMap) throws Exception{
		
		//检验单据类型
		if(null == mqMap.get("BillType")|| "".equals(mqMap.get("BillType"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【BillType】字段为空！");
		}
		
		//检验单据号
		if(null == mqMap.get("BillCode")|| "".equals(mqMap.get("BillCode"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【BillCode】字段为空！");
		}
		
		//如果是销售数据修改回溯一定要带上
		if(MessageConstants.MSG_TRADETYPE_SALE.equals(mqMap.get("BillType")) && (null == mqMap.get("ModifyCounts")|| "".equals(mqMap.get("ModifyCounts")))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:"+mqMap.get("BillType")+"业务的消息体没有带上销售回溯！");
		}
		
		//验证消息体
		if(null == mqMap.get("OriginalMsg")|| "".equals(mqMap.get("OriginalMsg"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【OriginalMsg】字段为空！");
		}
		
		//验证收发数据方标志
		if(null == mqMap.get("SendOrRece")|| "".equals(mqMap.get("SendOrRece"))){
			mqMap.put("SendOrRece","S");
		}
		
		//验证消息源
		if(null == mqMap.get("Source")|| "".equals(mqMap.get("Source"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【Source】字段为空！");
		}
		
		int id = binBEMQHELPERCM99_Service.insertMQ_Log(mqMap);
		
		return id;
	}
}

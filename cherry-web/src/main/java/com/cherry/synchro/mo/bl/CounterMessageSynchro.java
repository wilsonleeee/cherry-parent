/*	
 * @(#)CounterMessageSynchro.java     1.0 2011/06/13		
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
package com.cherry.synchro.mo.bl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cherry.cm.core.CherryException;
import com.cherry.synchro.mo.interfaces.CounterMessageSynchro_IF;
import com.cherry.synchro.mo.service.CounterMessageSynchroService;

/**
 * 柜台消息发布
 * @author dingyc
 *
 */
public class CounterMessageSynchro implements CounterMessageSynchro_IF {
	
	@Resource
	private CounterMessageSynchroService counterMessageSynchroService;

	@Override
	public void publishCounterMessage(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			param.put("ParamXml",getMessageCountersXml(param));
			counterMessageSynchroService.publishCounterMessage(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
		
	}

	@Override
	public void updForbiddenCounters(Map param) throws CherryException {
		try {
			param.put("Result", "OK");
			param.put("ParamXml",getCountersXml(param));
			counterMessageSynchroService.updForbiddenCounters(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
		
	}
	
	/**
	 * 
	 * 更新柜台消息的status消息
	 */
	@Override
	public void updCounterMessage(Map param) throws CherryException {
		try {
//			param.put("BrandCode", "mgp");
			param.put("Result", "OK");
			param.put("ParamXml",getUpdMessageCountersXml(param));
			counterMessageSynchroService.updCounterMessage(param);
			String ret = String.valueOf(param.get("Result"));
			if (!"OK".equals(ret)) {
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage() + ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		}catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}
	
	/**
	 * 将参数转换成xml格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getMessageCountersXml(Map param) throws Exception{
		String ret="";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element nodeRoot = doc.addElement("ParamXml");
			
			//柜台消息体节点
			Element mainInfo = nodeRoot.addElement("MessageInfo");
			//品牌代码 
			mainInfo.addAttribute("BrandCode", String.valueOf(param.get("BrandCode")));
			//消息ID 
			String msgID = String.valueOf(param.get("CounterMessageID"));
			mainInfo.addAttribute("CounterMessageID", msgID);
			//消息标题
			mainInfo.addAttribute("MessageTitle", String.valueOf(param.get("MessageTitle")));
			//消息正文
			mainInfo.addAttribute("MessageBody", String.valueOf(param.get("MessageBody")));
			// 消息状态
			mainInfo.addAttribute("Status", String.valueOf(param.get("Status")));
			// 消息生效开始日期
			mainInfo.addAttribute("StartDate", String.valueOf(param.get("StartDate")));
			// 消息生效截止日期
			mainInfo.addAttribute("EndDate", String.valueOf(param.get("EndDate")));
			// 数据的模式（0：禁止模式；1：允许模式）
			String flag = String.valueOf(param.get("flag"));
			mainInfo.addAttribute("Flag", flag);
			
			//禁止接收的柜台
			ArrayList<String> counterList =  (ArrayList<String>)param.get("CounterList");
			if(counterList!=null && counterList.size()>0){
				Iterator<String> it = counterList.iterator();
				String tempMap;
				Element counter ;
				while(it.hasNext()){
					 tempMap = it.next();
					 counter = nodeRoot.addElement("Counter");
					 counter.addAttribute("CounterMessageID",msgID);
					 counter.addAttribute("CounterCode",tempMap);
					 counter.addAttribute("Flag", flag);
				}
			}
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
	
	/**
	 * 将参数转换成xml格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getCountersXml(Map param) throws Exception{
		String ret="";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			Element nodeRoot = doc.addElement("ParamXml");
			
			//柜台消息体节点
			Element mainInfo = nodeRoot.addElement("MessageInfo");
			//品牌代码 
			mainInfo.addAttribute("BrandCode", String.valueOf(param.get("BrandCode")));
			//消息ID 
			String msgID = String.valueOf(param.get("CounterMessageID"));	
			mainInfo.addAttribute("CounterMessageID", msgID);
			// 数据的模式（0：禁止模式；1：允许模式）
			String flag = String.valueOf(param.get("flag"));
			mainInfo.addAttribute("Flag", flag);
			
			//禁止接收的柜台
			ArrayList<String> counterList =  (ArrayList<String>)param.get("CounterList");
			if(counterList!=null && counterList.size()>0){
				Iterator<String> it = counterList.iterator();
				String tempMap;
				Element counter ;
				while(it.hasNext()){
					 tempMap = it.next();
					 counter = nodeRoot.addElement("Counter");
					 counter.addAttribute("CounterMessageID",msgID);
					 counter.addAttribute("CounterCode",tempMap);
					 counter.addAttribute("Flag", flag);
				}
			}
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}
	
	/**
	 * 将参数转换成xml格式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String getUpdMessageCountersXml(Map param) throws Exception{
		String ret="";
		try{
			//创建XML文档
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			//创建根节点
			Element nodeRoot = doc.addElement("ParamXml");
			
			//柜台消息体节点
			Element mainInfo = nodeRoot.addElement("MessageInfo");
			//品牌代码 
			mainInfo.addAttribute("BrandCode", String.valueOf(param.get("BrandCode")));
			//消息ID 
			String msgID = String.valueOf(param.get("counterMessageId"));
			mainInfo.addAttribute("CounterMessageID", msgID);
			// 消息状态
			mainInfo.addAttribute("status", String.valueOf(param.get("status")));
			
			ret = doc.asXML();
		}catch(Exception ex){
			throw ex;
		}
		return ret;
	}

}

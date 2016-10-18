/*	
 * @(#)BINBEMQMES98_BL.java     1.0 2011/8/18		
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
package com.cherry.mq.mes.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.interfaces.RuleHandler_IF;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.cherry.mq.mes.service.BINBEMQMES98_Service;

/**
 * 管理MQ消息处理器和规则计算处理器共通 BL
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public class BINBEMQMES98_BL implements ApplicationContextAware{
	
	/** 管理MQ消息处理器和规则计算处理器共通 Service **/
	@Resource
	private BINBEMQMES98_Service binBEMQMES98_Service;
	
	/** 储存不同业务类型下的处理器（处理器包括MQ消息处理器和规则计算处理器） **/
	private static Map<String,Object> handlerFactory;
	
	private static ApplicationContext applicationContext;
		
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 根据MQ类型取得相对应的规则处理器
	 * 
	 * @param orgCode 组织代码
	 * @param brandCode 品牌代码
	 * @param billType 业务类型         
	 * @return 规则处理器
	 */
	public RuleHandler_IF getHandler(String orgCode, String brandCode, String billType) {
		// 取得处理器名称
		String handlerName = getHandlerName("0", orgCode, brandCode, billType);
		if(handlerName == null || "".equals(handlerName)) {
			handlerName = getHandlerName("0", "-9999", "-9999", billType);
		}
		if (null != handlerName && !"".equals(handlerName) && !"HANDLERSTOP".equals(handlerName)) {
			return (RuleHandler_IF) applicationContext.getBean(handlerName);
		}
		return null;
	}
	
	/**
	 * 根据MQ类型取得相对应的规则执行类
	 * 
	 * @param orgCode 组织代码
	 * @param brandCode 品牌代码
	 * @param billType 业务类型         
	 * @return 
	 * @return 规则执行类
	 */
	public <T> T getRuleExec(String orgCode, String brandCode, String billType) {
		// 取得处理器名称
		String handlerName = getHandlerName("0", orgCode, brandCode, billType);
		if(handlerName == null || "".equals(handlerName)) {
			handlerName = getHandlerName("0", "-9999", "-9999", billType);
		}
		if (null != handlerName && !"".equals(handlerName) && !"HANDLERSTOP".equals(handlerName)) {
			return (T) applicationContext.getBean(handlerName);
		}
		return null;
	}
	
	/**
	 * 根据MQ消息业务类型取得相对应的消息处理器
	 * 
	 * @param orgCode 组织代码
	 * @param brandCode 品牌代码
	 * @param billType MQ消息业务类型
	 * @return 消息处理器
	 */
	public CherryMessageHandler_IF getMessageHandler(String orgCode, String brandCode, String billType) {
		// 取得处理器名称
		String handlerName = getHandlerName("1", orgCode, brandCode, billType);
		if(handlerName == null || "".equals(handlerName)) {
			handlerName = getHandlerName("1", "-9999", "-9999", billType);
		}
		if (null != handlerName && !"".equals(handlerName) && !"HANDLERSTOP".equals(handlerName)) {
			return (CherryMessageHandler_IF) applicationContext.getBean(handlerName);
		}
		return null;
	}
	
	/**
	 * 取得处理器名称
	 * 
	 * @param type 处理器类型
	 * @param orgCode 组织代码
	 * @param brandCode 品牌代码
	 * @param billType 业务类型
	 * @return 处理器名称
	 */
	@SuppressWarnings("unchecked")
	public String getHandlerName(String type, String orgCode, String brandCode, String billType) {
		if(handlerFactory == null || handlerFactory.isEmpty()) {
			// 取得所有不同业务类型下的处理器
			initHandlerFactory();
		}
		String handlerName = null;
		if(handlerFactory != null && !handlerFactory.isEmpty()) {
			List<Map<String,Object>> handlerList = (List)handlerFactory.get(type);
			if(handlerList != null) {
				boolean findHandler = false;
				for(Map<String,Object> map : handlerList) {
					String _orgCode = (String)map.get("orgCode");
					String _brandCode = (String)map.get("brandCode");
					if(orgCode.equals(_orgCode) && brandCode.equals(_brandCode)) {
						List<Map<String,Object>> billTypeList = (List)map.get("list");
						if(billTypeList != null) {
							for(Map<String,Object> billTypeMap : billTypeList) {
								String _billType = (String)billTypeMap.get("billType");
								if(billType.equals(_billType)) {
									// 有效区分
									String valFlag = (String) billTypeMap.get("valFlag");
									// 处理器停用
									if ("0".equals(valFlag)) {
										return "HANDLERSTOP";
									}
									handlerName = (String)billTypeMap.get("handlerName");
									findHandler = true;
									break;
								}
							}
							if(findHandler) {
								break;
							}
						}
					}
				}
			}
		}
		return handlerName;
	}
	
	/**
	 * 取得所有不同业务类型下的处理器
	 */
	public synchronized void initHandlerFactory() {
		// 取得处理器名称List
		List<Map<String, Object>> handlerNameList = binBEMQMES98_Service.getHandlerNameList(new HashMap<String, Object>());
		if(handlerNameList != null && !handlerNameList.isEmpty()) {
			List<Map<String, Object>> handlerNameTempList = new ArrayList<Map<String,Object>>();
			String[] key0 = {"type"};
			String[] key1 = {"orgCode","brandCode"};
			List<String[]> keysList = new ArrayList<String[]>();
			keysList.add(key0);
			keysList.add(key1);
			ConvertUtil.convertList2DeepList(handlerNameList,handlerNameTempList,keysList,0);
			Map<String, Object> _handlerFactory = new HashMap<String, Object>();
			for(Map<String, Object> map : handlerNameTempList) {
				String _type = (String)map.get("type");
				_handlerFactory.put(_type, map.get("list"));
			}
			handlerFactory = _handlerFactory;
		}
	}
	
	/**
	 * 刷新处理器
	 */
	public void refreshHandler() {
		initHandlerFactory();
	}
}

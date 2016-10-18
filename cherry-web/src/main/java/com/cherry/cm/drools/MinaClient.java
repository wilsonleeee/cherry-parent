/*		
 * @(#)MinaClient.java     1.0 2010/10/12		
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
package com.cherry.cm.drools;

import java.util.Random;

import org.drools.SystemEventListenerFactory;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;

/**
 * Mina client
 * @author dingyc
 *
 */
public class MinaClient {
//	public static TaskClient getMinaClient() throws Exception {
//		TaskClient client = null;
//		try {
//			Random r = new Random();
//			int i = r.nextInt();
//			client = new TaskClient(new MinaTaskClientConnector("client " + i,
//					new MinaTaskClientHandler(SystemEventListenerFactory
//							.getSystemEventListener())));
//
//			String ip = PropertiesUtil.pps.getProperty("droolsFlow.MinaIP",
//					"127.0.0.1");
//			int port = CherryUtil.string2int(PropertiesUtil.pps.getProperty(
//					"droolsFlow.MinaIP", "9123"));
//			client.connect(ip, port);
//			return client;
//		} catch (Exception ex) {
//			throw new CherryException("ECM00016",ex);
//		}
//	}
}

/*
 * @(#)UpFilePathServlet.java     1.0 2010/12/10
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
package com.cherry.cm.core;

import javax.servlet.http.HttpServlet;

/**
 * 
 * UpFilePathServlet
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.10
 */
public class UpFilePathServlet extends HttpServlet {

	private static final long serialVersionUID = -8256886145396942650L;

	public UpFilePathServlet() {
		super();
	}

	public void init() {
		UpFilePathUtil.upImagePath = PropertiesUtil.pps
			.getProperty("uploadFilePath.upImagePath");
//		try {
//			// TOMCAT配置路径
//			String tomcatHome = System.getenv("TOMCAT_HOME");
//			if (null != tomcatHome) {
//				// TOMCAT下server配置文件路径
//				String path = tomcatHome + System.getProperty("file.separator")
//						+ "conf" + System.getProperty("file.separator")
//						+ "server.xml";
//				SAXReader saxReader = new SAXReader();
//				Document document = saxReader.read(new File(path));
//				Element port = document.getRootElement();
//				Element a = port.element("Service");
//				Element engine = a.element("Engine");
//				Iterator<?> hIt = engine.elementIterator();
//				while (hIt.hasNext()) {
//					Element element = (Element) hIt.next();
//					Iterator<?> cIt = element.elementIterator();
//					while (cIt.hasNext()) {
//						Element e = (Element) cIt.next();
//						if (e.getName().equals("Context")) {
//							String p = e.attributeValue("path");
//							// 上传图片的虚拟路径
//							if (p.equals(PropertiesUtil.pps
//									.getProperty("uploadFilePath.upImage"))) {
//								// 上传图片的实际保存路径
//								UpFilePathUtil.upImagePath = e
//										.attributeValue("docBase");
//								break;
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void destory() {
		super.destroy();
	}
}

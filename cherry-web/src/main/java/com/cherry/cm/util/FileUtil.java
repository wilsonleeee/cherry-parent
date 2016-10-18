/*		
 * @(#)FileUtil.java     1.0 2011/12/23		
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
package com.cherry.cm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;

/**
 * 文件处理类
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class FileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 将字符串类型的文档内容生成文档
	 * 
	 * @param content 文档内容
	 * @return Document 转换后的文档对象
	 * 
	 */
	public static Document string2Doc(String content){
		// 文档对象
		Document document = null;
		// 输入流
		InputStream in = null;
		SAXReader reader = new SAXReader();
		try {
			in = new ByteArrayInputStream(content.getBytes("UTF-8"));
			reader.setValidation(false);
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			reader.setEncoding("UTF-8");
			document = reader.read(in);
		} catch (Exception e) {
			printError("String2Doc", e.getMessage());
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e1) {
					printError("String2Doc", e1.getMessage());
				}
			}
		}
		return document;
	}
	
	/**
	 * 将文档类型转换为字符串类型
	 * 
	 * @param document 文档对象
	 * @return String 转换后的字符串类型
	 * 
	 */
	public static String Doc2String(Document document){
		// 转换后的字符串类型
		String s = null;
		// 使用输出流来进行转换
		ByteArrayOutputStream out = null;
		XMLWriter writer = null;
		try {
			out = new ByteArrayOutputStream();
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		} catch (Exception e) {
			printError("Doc2String", e.getMessage());
		} finally {
			try {
				if (null != out) {
					out.close();
				}
				if (null != writer) {
					writer.close();
				}
			} catch (IOException e1) {
				printError("String2Doc", e1.getMessage());
			}
		}
		return s;
	}
	
	/**
	 * 根据不同的浏览器采用不同的编码方式来解决导出文件名乱码问题
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeFileName(HttpServletRequest request,
			String FileName) throws UnsupportedEncodingException {
		String downloadFileName = null;
		String agent = request.getHeader("USER-AGENT");
		if (null != agent && -1 != agent.indexOf("MSIE")) { // IE内核
			downloadFileName = URLEncoder.encode(FileName,
					CherryConstants.CHAR_ENCODING);
		}else if(null != agent && agent.toLowerCase().indexOf("rv:11.0")>-1 && agent.toLowerCase().indexOf("trident")>-1){//IE 11
            downloadFileName = URLEncoder.encode(FileName,CherryConstants.CHAR_ENCODING);
        }else if (null != agent && -1 != agent.indexOf("Mozilla")) { // 非IE内核
			downloadFileName = new String(
					FileName.getBytes(CherryConstants.CHAR_ENCODING),
					CherryConstants.CHAR_ENCODING_ISO);
		} 
		return downloadFileName;
	}
	
	/**
	 * 打印错误信息
	 * 
	 * @param mdName 方法名
	 * @param errMsg 错误信息
	 * 
	 */
	private static void printError(String mdName, String errMsg) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("method: ").append(mdName).append("  ")
		.append("error: ").append(errMsg);
		logger.error(buffer.toString());
	}
}

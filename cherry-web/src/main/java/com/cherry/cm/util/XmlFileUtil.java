package com.cherry.cm.util;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlFileUtil {

	/**
	 * doc2String 将xml文档内容转为String
	 * 
	 * @return 字符串
	 * @param document
	 */
	public static String doc2String(Document document) {
		String s = "";
		try {
			// 使用输出流来进行转化
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat format = new OutputFormat("  ", true, "UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	/**
	 * load 载入一个xml文档
	 * 
	 * @return 成功返回Document对象，失败返回null
	 * @param uri
	 *            文件路径
	 */
	public static Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding("UTF-8");
			document = saxReader.read(new File(filename));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
}

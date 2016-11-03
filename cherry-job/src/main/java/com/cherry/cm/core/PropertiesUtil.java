package com.cherry.cm.core;

import java.util.Properties;

public class PropertiesUtil {
	public static Properties pps;
	
	public static String getMessage(String code, String[]params) {
		String msg = pps.getProperty(code);
		if (msg != null && params != null) {
			for (int i = 0; i < params.length; i++) {
				String reg = "{" + i + "}";
				if (null == params[i]) {
					params[i] = "";
				}
				msg = msg.replace(reg, params[i]);
			}
		}
		return msg;
	}
}

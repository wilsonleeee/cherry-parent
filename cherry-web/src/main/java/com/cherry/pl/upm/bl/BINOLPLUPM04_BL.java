/*	
 * @(#)BINOLPLUPM04_BL.java     1.0 2010/12/28		
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
package com.cherry.pl.upm.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.pl.upm.service.BINOLPLUPM04_Service;

/**
 * 
 * 安全策略BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM04_BL {
	
	@Resource
	private BINOLPLUPM04_Service binOLPLUPM04_Service;
	
	/**
	 * 取得密码安全配置信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getPwConfInfo(Map<String, Object> map) {
		// 取得发货单信息
		return binOLPLUPM04_Service.getPwConfInfo(map);
	}
	
	/**
	 * 取得密码组合
	 * 
	 * @param map
	 * @return
	 */
	public void getPwComb(Map<String, Object> map) {
		if (null != map && !map.isEmpty()) {
			// 密码复杂度
			String complexity = (String) map.get("complexity");
			if (null != complexity && !"".equals(complexity)) {
				String regex = complexity.substring(complexity.indexOf("[") + 1, complexity.indexOf("]"));
				if (regex.indexOf("A-Za-z") >= 0) {
					// 包含英文
					map.put("hasAlpha", "1");
					regex = regex.replace("A-Za-z", "");
				}
				if (regex.indexOf("0-9") >= 0) {
					// 包含数字
					map.put("hasNumeric", "1");
					regex = regex.replace("0-9", "");
				}
				if (regex.length() > 0) {
					// 包含其他字符
					map.put("hasOtherChar", "1");
//					regex = regex.replace("\\\\", "\\").replace("\\[", "[")
//								.replace("\\]", "]").replace("\\^", "^")
//								.replace("\\$", "$");
					
					//转义正则表达式特殊字符  ^$()*+?.[\{|]
                    regex = regex.replace("\\\\", "\\").replace("\\^", "^")
                            .replace("\\$", "$").replace("\\(", "(")
                            .replace("\\)", ")").replace("\\*", "*")
                            .replace("\\+", "+").replace("\\?", "?")
                            .replace("\\.", ".").replace("\\[", "[")
                            .replace("\\{", "{").replace("\\|", "|")
                            .replace("\\u005D", "]");
					
					// 其他字符
					map.put("otherChar", regex);
					StringBuffer buffer = new StringBuffer();
					char[] regexArr = regex.toCharArray();
					for (int i = 0; i < regexArr.length; i++) {
						if (i != 0) {
							buffer.append(" ");
						}
						buffer.append(regexArr[i]);
					}
					// 其他字符
					map.put("otherCharSpace", buffer.toString());
				}
			}
		}
	}
	
	/**
	 * 取得密码复杂度
	 * 
	 * @param map
	 * @return
	 */
	public void getComplexity(Map<String, Object> map) {
		if (null != map && !map.isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("^[");
			// 包含英文
			String hasAlpha = (String) map.get("hasAlpha");
			// 包含数字
			String hasNumeric = (String) map.get("hasNumeric");
			// 包含其他字符
			String hasOtherChar = (String) map.get("hasOtherChar");
			// 最小长度
			String length = (String) map.get("pwLength");
			// 最大长度
			String maxLength = (String) map.get("maxLength");
			if ("1".equals(hasAlpha)) {
				buffer.append("A-Za-z");
			}
			if ("1".equals(hasNumeric)) {
				buffer.append("0-9");
			}
			if ("1".equals(hasOtherChar)) {
				String otherChar = (String) map.get("otherChar");
//					.replace("\\", "\\\\").replace("[", "\\[")
//					.replace("]", "\\]").replace("^", "\\^")
//					.replace("$", "\\$");
				
                //转义正则表达式特殊字符  ^$()*+?.[\{|]
                otherChar = otherChar.replace("\\", "\\\\").replace("^", "\\^")
                        .replace("$", "\\$").replace("(", "\\(")
                        .replace(")", "\\)").replace("*", "\\*")
                        .replace("+", "\\+").replace("?", "\\?")
                        .replace(".", "\\.").replace("[", "\\[")
                        .replace("{", "\\{").replace("|", "\\|")
                        .replace("]", "\\u005D");
				
				buffer.append(otherChar);
			}
			if (null == length || "".equals(length)) {
				length = "0";
			}
			buffer.append("]{").append(length).append(",");
			if (null != maxLength && !"".equals(maxLength)) {
				buffer.append(maxLength);
			}
			buffer.append("}$");
			// 密码复杂度
			map.put("complexity", buffer.toString());
		}
	}
	
	/**
	 * 设置【密码更改通知日期】【密码失效日】
	 * @param map
	 * @throws CherryException
	 */
	public void setPwdExpireDate(Map<String,Object> map) throws CherryException{
	    Map<String,Object> param = new HashMap<String,Object>();
	    param.putAll(map);
        Map<String,Object> pwConfInfo = getPwConfInfo(param);
        if(null == pwConfInfo){
            throw new CherryException("EPL00012");
        }
        int duration = CherryUtil.obj2int(pwConfInfo.get("duration"));//密码有效期
        int remindAhead = CherryUtil.obj2int(pwConfInfo.get("remindAhead"));//密码修改提醒提前天数
        String today = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
        //计算【密码更改通知日期】
        String informDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, today, duration-remindAhead);
        map.put("informDate", informDate);
        //计算【密码失效日】
        String expireDate = DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, today, duration);
        map.put("expireDate", expireDate);
	}
}

/*	
 * @(#)BaseValidate.java     1.0 2011/11/01		
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
package com.cherry.cp.common.validate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.ActionErrorDTO;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员活动验证共通 Validate
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class BaseValidate {
	
	protected static final Logger logger = LoggerFactory.getLogger(BaseValidate.class);
	
	/** 参数验证结果 */
	protected boolean isCorrect = true;
	
	/** 错误信息集合 */
	protected List<ActionErrorDTO> actionErrorList = new ArrayList<ActionErrorDTO>();
	
	/**
	 * 验证提交的参数
	 * 
	 * @param String
	 *            活动模板信息
	 * 
	 */
	public boolean validateForm(String camTemps, Map<String, Object> baseMap) {
		try {
			// 活动模板List
			List<Map<String, Object>> camTempList = (List<Map<String, Object>>) JSONUtil
					.deserialize(String.valueOf(camTemps));
			validateCamTemp(camTempList, baseMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			isCorrect = false;
		}
		if (!isCorrect) {
			baseMap.put("actionErrorList", actionErrorList);
		}
		return isCorrect;
	}
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * 
	 */
	private void invokeMd(String mdName, Map<String, Object> map, Map<String, Object> baseMap) {
		try {
			Method[] mdArr = this.getClass().getMethods();
			for (Method method : mdArr) {
				if (method.getName().equals(mdName)) {
					method.invoke(this, map, baseMap);
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 验证提交的模板内容
	 * 
	 * @param List
	 *            活动模板List
	 * 
	 */
	private void validateCamTemp (List<Map<String, Object>> camTempList, Map<String, Object> baseMap) {
		if (null != camTempList) {
			for (Map<String, Object> camTemp : camTempList) {
				// 模板编号
				String tempCode = (String) camTemp.get("tempCode");
				invokeMd(tempCode + "_check", camTemp, baseMap);
				if (camTemp.containsKey("combTemps")) {
					List<Map<String, Object>> combTempList = (List<Map<String, Object>>) camTemp.get("combTemps");
					validateCamTemp(combTempList, baseMap);
				}
			}
		}
	}
	
	public <T> T getBeanByName(String name, Map<String, Object> baseMap) {
		ApplicationContext applicationContext = (ApplicationContext) baseMap.get(CampConstants.APPLI_KEY);
		if (null != applicationContext && !CherryChecker.isNullOrEmpty(name)) {
			T t = (T) applicationContext.getBean(name);
			return t;
		}
		return null;
	}
}

/*
 * @(#)ParamsValidateCondition.java     1.0 2011/11/01
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
package com.cherry.cp.common.condition;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cp.common.annota.BeanReso;
import com.cherry.cp.common.validate.BaseValidate;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ClassLoaderUtil;

/**
 * 验证参数共通 Condition
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class ParamsValidateCondition implements Condition{
	
	protected static final Logger logger = LoggerFactory.getLogger(ParamsValidateCondition.class);
	
	/**
	 * 
	 *共通 结果条件判定处理，如果执行结果与Cherry_status一致，返回True；否则返回False
	 * @param Map
	 * @param Map
	 * @param PropertySet
	 * 
	 * @return boolean
	 * @throws WorkflowException
	 * @author hub
	 * @version 1.0 2011.05.31
	 */
	@Override
	public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		 try {
			 	// 验证参数的类
				String validateClass = (String) args.get("validate_class");
				// 结果类型
				//String isContrary = (String) args.get("isContrary");
	            BaseValidate baseValidate = (BaseValidate) ClassLoaderUtil.loadClass(validateClass.trim(), getClass()).newInstance();
	            Map<String, Object> baseMap = (Map<String, Object>) transientVars.get("stepMap");
	            String camTemps = (String) baseMap.get("camTempsValidate");
	            Field[] fs =baseValidate.getClass().getFields();
	            if (null != fs) {
	            	for (Field f : fs) {
	            		BeanReso br = f.getAnnotation(BeanReso.class);
	            		if (null != br) {
	            			String name = br.name();
	            			Object beanObj = baseValidate.getBeanByName(name, baseMap);
	            			f.setAccessible(true);
	            			f.set(baseValidate, beanObj);
	            		}
	            	}
	            }
	            // 验证参数
	            boolean isValid = baseValidate.validateForm(camTemps, baseMap);
	            if (!isValid) {
	            	baseMap.put("camTemps", camTemps);
	            	baseMap.remove("allCamTempList");
	            }
//	            if ("1".equals(isContrary)) {
//	            	return isValid;
//	            }
	            return isValid;
	        } catch (Exception e) {
	        	logger.error(e.getMessage(),e);
	        }
		return true;
	}

}

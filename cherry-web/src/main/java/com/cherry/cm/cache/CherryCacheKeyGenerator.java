/*  
 * @(#)CherryCacheKeyGenerator.java     1.0 2012/06/10      
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
package com.cherry.cm.cache;


import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.HMACSHA256Coder;


public class CherryCacheKeyGenerator implements KeyGenerator {
	private static final Logger logger = LoggerFactory.getLogger(CherryCacheKeyGenerator.class);
	
	public static final int NO_PARAM_KEY = 0;
	public static final int NULL_PARAM_KEY = 53;
	
	@Override
	public Object generate(Object target, Method method, Object... params) {	    
	    List<Object> cacheKey = new ArrayList<Object>();
	    // 将当前线程的数据源也作为key的一部分，确保多品牌环境下的数据隔离
	    String ds = CustomerContextHolder.getCustomerDataSourceType().intern();
	    cacheKey.add(ds);
	    Class<?> objectclass = AopProxyUtils.ultimateTargetClass(target);
	    cacheKey.add(objectclass.getName().intern());
	    // 将对象的内存地址作为key的一部分，Web Server重启后缓存会失效
	    cacheKey.add(System.identityHashCode(target));
	    cacheKey.add(method.getName().intern());
	    if (params.length == 0) {
	    	cacheKey.add(NO_PARAM_KEY);
		}
	    for (Object param : params){
	    	logger.debug("param type===" + param.getClass().getName());
	    	if(param instanceof Map<?, ?>){
	    		cacheKey.add(((Map<?, ?>)param).hashCode());
	    		 logger.debug("param is hashmap,hashcode===" + ((Map<?, ?>)param).hashCode());
	    	}
	    }
	    cacheKey.addAll(Arrays.asList(params));
	    
	    logger.debug("cacheKey===" + cacheKey.toString());
	    
		try {
			String SHA256CackekeyHex = new String(HMACSHA256Coder.encodeSHA256Hex(
					(cacheKey.toString()).getBytes(Charset.forName("UTF-8"))));
			logger.debug("SHA256CackekeyHex===" + SHA256CackekeyHex);
			
			return ds + SHA256CackekeyHex;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	  }	

}


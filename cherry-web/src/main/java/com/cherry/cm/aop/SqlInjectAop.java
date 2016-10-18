/*  
 * @(#)AOPTest.java     1.0 2011/05/31      
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
package com.cherry.cm.aop;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.HTMLEntityCodec;
import com.cherry.cm.core.SqlInjectEncode;

//@Aspect
public class SqlInjectAop {
	// @Pointcut("within(com.cherry.login.action..*)")

	// @Before("execution(* com.cherry.login.action.LoginAction.*(..))")
	public void before(JoinPoint point) throws CherryException {
		Object[] obArr = point.getArgs();
		if (obArr != null && obArr.length > 0) {

			if (obArr[0] instanceof Map) {
				Map param = (Map) obArr[0];
				Iterator it = param.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					Object value = param.get(key);
					if (value instanceof String) {
						value = String.valueOf(value).replaceAll("'", "~");
						value = String.valueOf(value).replaceAll(" [Oo][Rr] ", "oorr");
						param.put(key, value);
//						Pattern p = Pattern.compile(".*('| +[Oo][Rr] +|;|--).*");
//						Matcher m = p.matcher(String.valueOf(value));
//						if (m.find()) {
//							throw new CherryException("ECM00041");
//						}
					}
				}			
			} else if(obArr[0] instanceof String){
				String tmp = String.valueOf(obArr[0]);
				tmp = tmp.replaceAll("'", "~");
				tmp = tmp.replaceAll(" [Oo][Rr] ", "oorr");
				
				obArr[0] = tmp;
//				Pattern p = Pattern.compile(".*('| +[Oo][Rr] +|;|--).*");
//				Matcher m = p.matcher(String.valueOf(obArr[0]));
//				if (m.find()) {
//					throw new CherryException("ECM00041");
//				}
			}else if(obArr[0] instanceof List){
				List list = (List)obArr[0];
				Iterator it =list.iterator();
				while(it.hasNext()){
					Object ob = it.next();
					if(ob instanceof Map){
						Map tmpMap = (Map)ob;
						Iterator it2 = tmpMap.keySet().iterator();
						while (it2.hasNext()) {
							Object key = it2.next();
							Object value = tmpMap.get(key);
							if (value instanceof String) {
								value = String.valueOf(value).replaceAll("'", "~");
								value = String.valueOf(value).replaceAll(" [Oo][Rr] ", "oorr");
								tmpMap.put(key, value);
							}
					}
				}				
			}
		}
	}
	}
}

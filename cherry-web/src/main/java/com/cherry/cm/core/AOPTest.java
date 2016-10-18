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
package com.cherry.cm.core;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

//@Aspect
public class AOPTest {
	//@Pointcut("within(com.cherry.login.action..*)")
	  public  AOPTest() {
		System.out.println("============================AOPTest");
	}
	//@Before("execution(* com.cherry.login.action.LoginAction.*(..))")
	  public void before(JoinPoint point) {
		  System.out.println("======AOP START:=======" + point.getSignature().getName());
	  }

}

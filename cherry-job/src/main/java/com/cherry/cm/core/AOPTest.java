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

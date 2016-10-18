package com.cherry.cm.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeanManager implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;
	}
    public static Object getBean(String beanName){
		try {
			return applicationContext.getBean(beanName);
		} catch (Exception ex) {
			return null;
		}
	}
}

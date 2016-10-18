package com.cherry.shindig.gadgets.container;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

public class CherryGuiceModule extends AbstractModule implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;  
	
	protected void configure() {
	    // We do this so that jsecurity realms can get access to the jsondbservice singleton

	    Multibinder<Object> handlerBinder = Multibinder.newSetBinder(binder(), Object.class, Names.named("org.apache.shindig.handlers"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("AttendanceHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("TaskListHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("SaleInfoHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("SaleInfo1Handler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("SaleCountByHoursHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("SaleCountByHours1Handler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("SaleTargetRptHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("SaleTargetRpt1Handler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("RuleCalStateHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("DayRuleCalCountHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("PointCalInfoHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("CalMemCountHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("CalMemAmountHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("MemInfoHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("MemSaleInfoHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("MemPointInfoHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("MemSaleCountInfoHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("MemSaleCountByProHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("OrderTaskHandler"));
	    handlerBinder.addBinding().toInstance(applicationContext.getBean("OrderTaskCountHandler"));
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	

}

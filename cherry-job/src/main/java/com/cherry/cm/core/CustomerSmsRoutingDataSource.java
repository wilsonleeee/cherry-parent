package com.cherry.cm.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomerSmsRoutingDataSource extends AbstractRoutingDataSource {
	
	@Override 
    protected Object determineCurrentLookupKey() { 
       return CustomerSmsContextHolder.getCustomerSmsDataSourceType();
    } 

}

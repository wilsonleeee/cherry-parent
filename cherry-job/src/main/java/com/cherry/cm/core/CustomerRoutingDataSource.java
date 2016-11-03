package com.cherry.cm.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomerRoutingDataSource extends AbstractRoutingDataSource {
	
	@Override 
    protected Object determineCurrentLookupKey() { 
       return CustomerContextHolder.getCustomerDataSourceType(); 
    } 

}

package com.cherry.cm.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomerTpifRoutingDataSource extends AbstractRoutingDataSource {
	
	@Override 
    protected Object determineCurrentLookupKey() { 
       return CustomerTpifContextHolder.getCustomerTpifDataSourceType();
    } 

}

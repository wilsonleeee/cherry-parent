package com.cherry.cm.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomerWitRoutingDataSource extends AbstractRoutingDataSource {
	
	@Override 
	protected Object determineCurrentLookupKey() { 
		return CustomerWitContextHolder.getCustomerWitDataSourceType(); 
	} 

}

/*
 * @(#)CustomerRoutingDataSource.java     1.0 2010/12/10
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
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource; 

public class CustomerRoutingDataSource extends AbstractRoutingDataSource { 

   @Override 
   protected Object determineCurrentLookupKey() { 
      return CustomerContextHolder.getCustomerDataSourceType(); 
   } 
} 

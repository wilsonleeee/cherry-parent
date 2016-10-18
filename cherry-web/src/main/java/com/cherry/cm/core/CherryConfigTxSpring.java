/*  
 * @(#)CherryConfigTxSpring.java     1.0 2012/11/02      
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

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 注解方式定义CherryConfig数据库的事务配置用以支持@Transactional主键
 * dataSourceCherryConfig : CherryConfig数据的数据源,在XML中已经配置，此处直接注入
 * 等效于如下配置：
 * <beans>
     <tx:annotation-driven/>
     <bean id="txCherryConfigManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
         <constructor-arg ref="dataSourceCherryConfig"/>
     </bean>
 </beans>
 * @author shenzg
 */
@Configuration
@EnableTransactionManagement
public class CherryConfigTxSpring {
	
	@Resource(name="dataSourceCherryConfig")
    DataSource dataSourceCherryConfig;

     @Bean
     public DataSourceTransactionManager txCherryConfigManager() {
         return new DataSourceTransactionManager(dataSourceCherryConfig);
     }	

}


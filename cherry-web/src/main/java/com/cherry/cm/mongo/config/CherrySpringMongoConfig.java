/*  
 * @(#)CherrySpringMongoConfig.java     1.0 2012/07/19      
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
package com.cherry.cm.mongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.PropertiesUtil;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackages = "com.cherry.cm.mongo.repositories")
public class CherrySpringMongoConfig {
	
	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		
	    // 设置Mongo的连接信息
	    String ip = PropertiesUtil.pps.getProperty("mongoDB.IP", "127.0.0.1");	
		int port = Integer.parseInt(PropertiesUtil.pps.getProperty("mongoDB.Port", "27017"));
		
		MongoOptions options = new MongoOptions();
		options.connectionsPerHost = 25;
	    Mongo mongo = new Mongo(new ServerAddress(ip, port),options);
	    
	 // 获取Mongo数据库登录用户信息
		String uesrname = PropertiesUtil.pps.getProperty("mongoDB.UserName", "");
		String password = PropertiesUtil.pps.getProperty("mongoDB.PassWord", "");
		DESPlus des = new DESPlus("binkun");
		uesrname = des.decrypt(uesrname);
		password = des.decrypt(password);
	    UserCredentials userCredentials = new UserCredentials(uesrname, password);
	    
	    return new CherrySimpleMongoDbFactory(mongo, "cherry", userCredentials);
	  }
    /*
     * 定义MongoTemplate对象
     */
	public @Bean MongoOperations mongoTemplate() throws Exception{
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}

	/*
	 * Use this post processor to translate any MongoExceptions thrown in @Repository annotated classes
	 */

//	public @Bean PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
//		return new PersistenceExceptionTranslationPostProcessor();
//	}
	

}


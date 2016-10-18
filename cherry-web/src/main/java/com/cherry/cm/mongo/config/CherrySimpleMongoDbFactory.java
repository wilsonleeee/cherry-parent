/*  
 * @(#)CherrySimpleMongoDbFactory.java     1.0 2012/09/04      
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

/**
 * 自定义的Mongo的Factory类
 * 
 * @version 1.0 2012/09/04
 */
import org.springframework.dao.DataAccessException;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class CherrySimpleMongoDbFactory extends SimpleMongoDbFactory {
    
	public CherrySimpleMongoDbFactory(Mongo mongo, String databaseName,
			UserCredentials credentials) {
		super(mongo, databaseName, credentials);
	}

	private DB db = null;
	
	/**
	 * 第一次通过使用父类的getDb方法取得DB对象，以后都使用同一个DB对象
	 */
	@Override
	public DB getDb() throws DataAccessException {
		if(db == null) {
			db = super.getDb();
		}
		return db;
	}

}

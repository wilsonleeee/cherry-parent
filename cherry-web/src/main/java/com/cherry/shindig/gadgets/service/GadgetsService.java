/*
 * @(#)GadgetsService.java     1.0 2011/11/01
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
package com.cherry.shindig.gadgets.service;

import java.util.List;

import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.BaseService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 小工具各种消息取得Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/01
 */
public class GadgetsService extends BaseService {
	
	/**
	 * 查询消息List
	 * 
	 * @param dbObject 查询条件
	 * @param orderBy  排序条件
	 * @param skip 跳过的数据量
	 * @param limit 查询的数据量
	 * @return 消息List
	 */
	public List<DBObject> getInfoByMongoDBList(DBObject dbObject, DBObject orderBy, int skip, int limit) throws Exception {
		
		DBObject keys = new BasicDBObject();
		keys.put("TradeNoIF", 1);
		keys.put("TradeEntityCode", 1);
		keys.put("TradeEntityName", 1);
		keys.put("UserCode", 1);
		keys.put("UserName", 1);
		keys.put("UserPost", 1);
		keys.put("DeptCode", 1);
		keys.put("DeptName", 1);
		keys.put("OccurTime", 1);
		keys.put("Content", 1);
		return MongoDB.find("MGO_BusinessLog", dbObject, keys, orderBy, skip, limit);
	}

}

/*  
 * @(#)MongoDB.java     1.0 2011/05/31      
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
package com.cherry.cm.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;


/**
 * @author dingyc
 *
 */
@Component
public class MongoDB {
	// db在MongoDB被实例化时实现注入
	private static DB db;
//	private static boolean flag = true;
	
	// mongoDbFactory在MongoDB被实例化时实现注入
//	private static MongoDbFactory mongoDbFactory;
	
	private static String dbname = "cherry";
		
// 实例化MongoDB，并将mongoDbFactory注入，因为是构造函数注入，所以不能用Resource注解
	@Autowired(required=true)
	public MongoDB(@Qualifier("mongoDbFactory")MongoDbFactory mongoDbFactory) {
//		MongoDB.mongoDbFactory = mongoDbFactory;
		MongoDB.db = mongoDbFactory.getDb(dbname);
	}
	
	/**
	 * @param dbName
	 * @return mongoDB
	 * @throws Exception
	 */
	private static DB getMongoDB(String dbName) throws Exception{
//		if(flag){
//			String ip = PropertiesUtil.pps.getProperty("mongoDB.IP", "127.0.0.1");	
//			int port = Integer.parseInt(PropertiesUtil.pps.getProperty("mongoDB.Port", "27017"));
//			String uesrname = PropertiesUtil.pps.getProperty("mongoDB.UserName", "");
//			String password = PropertiesUtil.pps.getProperty("mongoDB.PassWord", "");
//			DESPlus des = new DESPlus("binkun");
//			uesrname = des.decrypt(uesrname);
//			password = des.decrypt(password);
//			Mongo mon = new Mongo(ip,port);
//			db = mon.getDB(dbName);			
//			boolean loginSuccess=db.authenticate(uesrname, password.toCharArray());  
//            if(!loginSuccess){  
//                throw new CherryException("ECM00037");  
//            }			     
//			 flag = false;
//		}
		
		return MongoDB.db;
	}
	
	/**
	 * 向指定表中插入指定的对象
	 * @param colName 要插入的表名
	 * @param obj 要插入的对象
	 * @throws Exception  
	 */
	public static void insert(String colName,DBObject obj) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		coll.insert(obj);
		
	}
	
	/**
	 * 向指定表中插入指定的对象列表
	 * @param colName 要插入的表名
	 * @param objList 要插入的对象列表
	 * @throws Exception 
	 */
	public static void insert(String colName,List<DBObject> objList) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		coll.insert(objList);
	}
	
	/**
	 * 从指定的集合中查找符合条件的第一条记录
	 * @param colName 集合名
	 * @param condition 条件
	 * @return
	 * @throws Exception
	 */
	public static DBObject findOne(String colName,DBObject condition) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		return coll.findOne(condition);
	}
	
	/**
	 * 从指定的集合中查找符合条件的所有记录
	 * @param colName 集合名
	 * @param condition 条件
	 * @return
	 * @throws Exception
	 */
	public static List<DBObject> findAll(String colName,DBObject condition) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		DBCursor cursor = coll.find(condition);
		List<DBObject> ret = new ArrayList<DBObject>();
		while(cursor.hasNext()) {
			ret.add(cursor.next());
			}
		return ret;
	}
	
	/**
	 * 从指定集合中移除符合条件的记录
	 * @param colName 集合名
	 * @param condition 条件
	 * @throws Exception
	 */
	public static void removeAll(String colName,DBObject condition) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		coll.remove(condition);		
	}
	
	/**
	 * 从指定的集合中查找符合条件的记录
	 * @param colName 集合名
	 * @param condition 条件
	 * @param keys 查询字段
	 * @param orderBy 排序条件
	 * @param skip 跳过的数据量
	 * @param limit 查询的数据量
	 * @return 查询的结果List
	 * @throws Exception
	 */
	public static List<DBObject> find(String colName,DBObject condition,DBObject keys,DBObject orderBy,int skip,int limit) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		DBCursor cursor = coll.find(condition, keys).sort(orderBy).skip(skip).limit(limit);
		if(cursor != null) {
			return cursor.toArray();
		} else {
			return null;
		}
	}
	
	/**
	 * 更新指定集合中符合条件的记录
	 * @param colName 集合名
	 * @param query 条件
	 * @param update 更新内容
	 * @param upsert 如果不存在update的记录，是否插入记录,true为插入，false为不插入
	 * @param multi 如果为false，只更新找到的第一条记录，如果为true,就把按条件查出来多条记录全部更新
	 * @throws Exception
	 */
	public static void update(String colName,DBObject query, DBObject update, boolean upsert, boolean multi) throws Exception {
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		coll.update(query, update, upsert, multi);
	}
	
	/**
	 * 安全更新指定集合中符合条件的记录
	 * @param colName 集合名
	 * @param query 条件
	 * @param update 更新内容
	 * @return 更新成功返回 true,否则返回false
	 * @throws Exception
	 */
	public static boolean updateSafe(String colName,DBObject query, DBObject obj) throws Exception {
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		WriteResult wr = coll.update(query, obj, false, false, WriteConcern.SAFE);
		if(wr.getLastError() == null || wr.getLastError().ok()) {
			return true;
		} else {
			return false;
		}			
	}
	
	/**
	 * 更新指定集合中符合条件的记录并返回更新结果
	 * @param colName 集合名
	 * @param query 条件
	 * @param update 更新内容
	 * @return 返回更新后的查询值
	 * @throws Exception
	 */
	public static DBObject findAndModify(String colName,DBObject query, DBObject update) throws Exception {
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		return coll.findAndModify(query, update);
	}
	
	/**
	 * 从指定的集合中查找符合条件的记录数
	 * @param colName 集合名
	 * @param condition 条件
	 * @return 记录数
	 * @throws Exception
	 */
	public static int findCount(String colName,DBObject condition) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		DBCursor cursor = coll.find(condition);
		return cursor.count();
	}
	

	/**
	 * 更新指定集合中符合条件的记录
	 * @param colName 集合名
	 * @param query 条件
	 * @param obj 更新内容
	 * @return 更新件数
	 * @throws Exception
	 */
	public static int update(String colName,DBObject query, DBObject obj) throws Exception {
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		WriteResult wr = coll.update(query, obj);
		return wr.getN();
	}
	
	/**
	 * 从指定的集合中查找符合条件的记录
	 * @param colName 集合名
	 * @param condition 条件
	 * @param orderBy 排序条件
	 * @return 查询的结果List
	 * @throws Exception
	 */
	public static List<DBObject> find(String colName,DBObject condition,DBObject orderBy) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		DBCursor cursor = coll.find(condition).sort(orderBy);
		if(cursor != null) {
			return cursor.toArray();
		} else {
			return null;
		}
	}
	
	/**
	 * 从指定的集合中判断记录是否存在
	 * @param colName 集合名
	 * @param condition 条件
	 * @return true：存在，false：不存在
	 * @throws Exception
	 */
	public static boolean isExist(String colName,DBObject condition) throws Exception{
		DB db = getMongoDB(dbname);
		DBCollection coll = db.getCollection(colName);
		DBCursor dbCursor = coll.find(condition).limit(1);
		if(dbCursor != null && dbCursor.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}

/*  
 * @(#)BaseServiceImpl.java     1.0 2011/05/31      
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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.DateUtil;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * BaseDao实现类
 * 
 * @author huzd
 * @version 1.0 2010.04.12
 */
@SuppressWarnings("unchecked")
public class BaseServiceImpl extends SqlMapClientDaoSupport implements IBaseService, Serializable {
	private static final long serialVersionUID = 5298707454262075629L;
	protected static final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);
	@Resource
	protected ReadSlaveSQL readSlaveSQL;
	/**
	 * 取得结果集个数
	 * 
	 * @param ob
	 *            Object
	 * @return Integer
	 */
	public Integer getSum(Object ob) {
		
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
		try {
			if (readSlaveSQL.isSlaveOpen()) {
				// 开启了读从库
				if (readSlaveSQL.containsSqlID(sql_id) && readSlaveSQL.containsDataSourceKey(dataSourceType)) {
					// 如果执行的SQL在白名单中，并且该数据源存在对应的从库数据源，那么就切换到从库数据源
					CustomerContextHolder.setCustomerDataSourceType(dataSourceType + "_Slave");
				}
			}
			Integer sum = (Integer) getSqlMapClientTemplate().queryForObject(sql_id, ob);
			return sum;
		} finally {
			CustomerContextHolder.setCustomerDataSourceType(dataSourceType);
		}		
	}
	/**
	 * 取得单个结果
	 * 
	 * @param ob
	 *            Object
	 * @return Object
	 */
	public Object get(Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
		try {
			if (readSlaveSQL.isSlaveOpen()) {
				// 开启了读从库
				if (readSlaveSQL.containsSqlID(sql_id) && readSlaveSQL.containsDataSourceKey(dataSourceType)) {
					// 如果执行的SQL在白名单中，并且该数据源存在对应的从库数据源，那么就切换到从库数据源
					CustomerContextHolder.setCustomerDataSourceType(dataSourceType + "_Slave");
				}
			}
			Object resultObject = getSqlMapClientTemplate().queryForObject(sql_id, ob);
			return resultObject;
		} finally {
			CustomerContextHolder.setCustomerDataSourceType(dataSourceType);
		}
	}
	
	/**
	 * 取得单个结果
	 * 
	 * @param ob
	 *            Object
	 * @param sqlId
	 * 			  String
	 * @return Object
	 */
	public Object get(Object ob, String sqlId) {
		String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
		try {
			if (readSlaveSQL.isSlaveOpen()) {
				// 开启了读从库
				if (readSlaveSQL.containsSqlID(sqlId) && readSlaveSQL.containsDataSourceKey(dataSourceType)) {
					// 如果执行的SQL在白名单中，并且该数据源存在对应的从库数据源，那么就切换到从库数据源
					CustomerContextHolder.setCustomerDataSourceType(dataSourceType + "_Slave");
				}
			}
			Object resultObject = getSqlMapClientTemplate().queryForObject(sqlId, ob);
			return resultObject;
		} finally {
			CustomerContextHolder.setCustomerDataSourceType(dataSourceType);
		}	
	}

	/**
	 * 取得多个结果
	 * 
	 * @param ob
	 *            Object
	 * @return List
	 */
	public List getList(Object ob) {
		
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
		try {
			if (readSlaveSQL.isSlaveOpen()) {
				// 开启了读从库
				if (readSlaveSQL.containsSqlID(sql_id) && readSlaveSQL.containsDataSourceKey(dataSourceType)) {
					// 如果执行的SQL在白名单中，并且该数据源存在对应的从库数据源，那么就切换到从库数据源
					CustomerContextHolder.setCustomerDataSourceType(dataSourceType + "_Slave");
				}
			}
			List list = (List) getSqlMapClientTemplate().queryForList(sql_id, ob);
			return list;
		} finally {
			CustomerContextHolder.setCustomerDataSourceType(dataSourceType);
		}
	}
	
	/**
	 * 取得多个结果
	 * 
	 * @param ob
	 *            Object
	 * @return List
	 */
	public List getList(Object ob, String sqlId) {
		
		String dataSourceType = CustomerContextHolder.getCustomerDataSourceType();
		try {
			if (readSlaveSQL.isSlaveOpen()) {
				// 开启了读从库
				if (readSlaveSQL.containsSqlID(sqlId) && readSlaveSQL.containsDataSourceKey(dataSourceType)) {
					// 如果执行的SQL在白名单中，并且该数据源存在对应的从库数据源，那么就切换到从库数据源
					CustomerContextHolder.setCustomerDataSourceType(dataSourceType + "_Slave");
				}
			}
			List list = (List) getSqlMapClientTemplate().queryForList(sqlId, ob);
			return list;
		} finally {
			CustomerContextHolder.setCustomerDataSourceType(dataSourceType);
		}		
	}

	/**
	 * 插入一组数据
	 * 
	 * @param ob
	 *            Object
	 * @return
	 */
	public void save(final Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		getSqlMapClientTemplate().insert(sql_id, ob);
	}
	
	/**
	 * 插入一组数据
	 * 
	 * @param ob
	 *            Object
	 * @param sqlId
	 * 			  String
	 * @return
	 */
	public void save(Object ob, String sqlId) {
		getSqlMapClientTemplate().insert(sqlId, ob);
	}
	
	/**
	 * 插入一组数据并返回一个自增长id
	 * 
	 * @param ob
	 *            Object
	 * @return int
	 */
	public int saveBackId(Object ob){
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		return Integer.parseInt(String.valueOf(getSqlMapClientTemplate().insert(sql_id, ob)));
		 
	}
	
	/**
	 * 插入一组数据并返回一个自增长id
	 * 
	 * @param ob
	 *            Object
	 * @param sqlId
	 * 			  String  
	 * @return int
	 */
	public int saveBackId(Object ob, String sqlId){
		return Integer.parseInt(String.valueOf(getSqlMapClientTemplate().insert(sqlId, ob)));
		 
	}
	
	/**
	 * 用批处理插入一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void saveAll(final List<Map<String, Object>> list, final String sqlId) {
		
		// 执行回调  
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			// 实现回调接口  
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				// 开始批处理  
				executor.startBatch();
				for (Map<String, Object> map : list) {
					// 插入操作  
					executor.insert(sqlId, map);
				}  
				// 执行批处理  
				executor.executeBatch();
				return null;
			}
		});
	}
	
	/**
	 * 用批处理插入一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void saveAllObjs(final List list, final String sqlId) {
		
		// 执行回调  
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			// 实现回调接口  
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				// 开始批处理  
				executor.startBatch();
				for (Object obj : list) {
					// 插入操作  
					executor.insert(sqlId, obj);
				}  
				// 执行批处理  
				executor.executeBatch();
				return null;
			}
		});
	}
	
	/**
	 * 用批处理更新一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void updateAllObjs(final List list, final String sqlId) {
		
		// 执行回调  
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			// 实现回调接口  
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				// 开始批处理  
				executor.startBatch();
				for (Object obj : list) {
					// 更新操作  
					executor.update(sqlId, obj);
				}  
				// 执行批处理  
				executor.executeBatch();
				return null;
			}
		});
	}
	
	/**
	 * 用批处理插入一组数据(分页)
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void saveAllByPage(final List list, final String sqlId) {
		if (null != list && !list.isEmpty()) {
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			int size = list.size();
			while (true) {
				startNum = CherryConstants.BATCH_PAGE_MAX_NUM * currentNum;
				// 查询结束位置
				endNum = startNum + CherryConstants.BATCH_PAGE_MAX_NUM;
				if (endNum > size) {
					endNum = size;
				}
				// 数据抽出次数累加
				currentNum++;	
				List tempList = list.subList(startNum, endNum);
				saveAllObjs(tempList, sqlId);
				if (endNum == size) {
					break;
				}
			}
		}
	}
	
	/**
	 * 用批处理更新一组数据(分页)
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void updateAllByPage(final List list, final String sqlId) {
		if (null != list && !list.isEmpty()) {
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			int size = list.size();
			while (true) {
				startNum = CherryConstants.BATCH_PAGE_MAX_NUM * currentNum;
				// 查询结束位置
				endNum = startNum + CherryConstants.BATCH_PAGE_MAX_NUM;
				if (endNum > size) {
					endNum = size;
				}
				// 数据抽出次数累加
				currentNum++;	
				List tempList = list.subList(startNum, endNum);
				updateAllObjs(tempList, sqlId);
				if (endNum == size) {
					break;
				}
			}
		}
	}
	
	/**
	 * 更新一组数据
	 * 
	 * @param ob
	 *            Object
	 * @return int
	 */
	public int update(Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		return getSqlMapClientTemplate().update(sql_id, ob);
	}
	
	/**
	 * 更新一组数据
	 * 
	 * @param ob
	 *            Object
	 * @param sqlId
	 *            String
	 * @return int
	 */
	public int update(Object ob, String sqlId) {
		return getSqlMapClientTemplate().update(sqlId, ob);
	}

	/**
	 * 删除一组数据
	 * 
	 * @param ob
	 *            Object
	 * @return int
	 */
	public int remove(Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		return getSqlMapClientTemplate().update(sql_id, ob);
	}
	
	/**
	 * 删除一组数据
	 * 
	 * @param ob
	 *            Object
	 * @param sqlId
	 *            String
	 * @return int
	 */
	public int remove(Object ob, String sqlId) {
		return getSqlMapClientTemplate().update(sqlId, ob);
	}

	/**
	 * 取得系统时间
	 * 
	 * @param
	 * @return String
	 */
	public String getSYSDate() {
		return (String) getSqlMapClientTemplate().queryForObject("BINOLCMINC99.getSYSDate", null);
	}
	
	/**
	 * 取得系统时间(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param
	 * @return String
	 */
	public String getSYSDateTime() {
		return (String) getSqlMapClientTemplate().queryForObject("BINOLCMINC99.getSYSDateTime", null);
	}
	
	/**
	 * 取得系统时间(年月日)
	 * 
	 * @param
	 * @return String
	 */
	public String getDateYMD() {
		return (String) getSqlMapClientTemplate().queryForObject("BINOLCMINC99.getDateYMD", null);
	}

	/**
	 * 执行一个存储过程，查询并取得一个结果集
	 * 
	 * @param ob
	 * 			   Object 
	 * @return Object
	 */
	public Object getProcs(Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		Object resultObject = getSqlMapClientTemplate().queryForObject(sql_id, ob);
		return resultObject;
	}
	
	/**
	 * 执行一个存储过程，查询并取得多个结果集
	 * 
	 * @param ob
	 * 			   Object 
	 * @return Object
	 */
	public List getProcsList(Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		List list = (List) getSqlMapClientTemplate().queryForList(sql_id, ob);
		return list;
	}
	

	/**
	 * 执行一个存储过程，更新DB数据
	 * 
	 * @param ob
	 * 			   Object 
	 * @return String
	 */
	public Object updateProcs(Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		getSqlMapClientTemplate().queryForObject(sql_id, ob);
		return ob;
	}
	/**
	 * 用批处理更新一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void updateAll(final List<Map<String, Object>> list, final String sqlId) {
		
		// 执行回调  
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			// 实现回调接口  
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				// 开始批处理  
				executor.startBatch();
				for (Map<String, Object> map : list) {
					// 更新操作  
					executor.update(sqlId, map);
				}  
				// 执行批处理  
				executor.executeBatch();
				return null;
			}
		});
	}
	
	/**
	 * 用批处理删除一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void deleteAll(final List<Map<String, Object>> list, final String sqlId) {
		
		// 执行回调  
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			// 实现回调接口  
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				// 开始批处理  
				executor.startBatch();
				for (Map<String, Object> map : list) {
					// 删除操作  
					executor.delete(sqlId, map);
				}  
				// 执行批处理  
				executor.executeBatch();
				return null;
			}
		});
	}
	
	/**
	 * 取得前进的系统时间
	 * 
	 * @return String 返回前进的系统时间
	 */
	public String getForwardSYSDate() throws Exception {
		// 取得系统时间
		String sysDate = this.getSYSDate();
		sysDate = sysDate.split("\\.")[0];
		synchronized (this) {
			DBObject query = new BasicDBObject();
			DBObject dbObject = new BasicDBObject();
			while(true) {
				try {
					// 从mongodb中取得保存的系统时间
					List<DBObject> reslutList = MongoDB.find("MGO_SysDate", null, new BasicDBObject("SysDate", -1));
					// 存在的场合
					if(reslutList != null && !reslutList.isEmpty()) {
						// 取得最大的系统时间
						String sysDateMongodb = (String)reslutList.get(0).get("SysDate");
						// 存在多条记录的场合，保留最大的系统时间，其他全部删除
						if(reslutList.size() > 1) {
							query.put("SysDate", new BasicDBObject("$lt", sysDateMongodb));
							MongoDB.removeAll("MGO_SysDate", query);
						}
						// 当前系统时间比mongodb中保存的系统时间小的场合，把mongodb中的系统时间做加一秒处理
						if(sysDate.compareTo(sysDateMongodb) <= 0) {
							Calendar calendar = Calendar.getInstance();
						    calendar.setTime(DateUtil.coverString2Date(sysDateMongodb,"yyyy-MM-dd HH:mm:ss"));
						    calendar.add(Calendar.SECOND, 1);
						    sysDate = DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
						}
						// 更新mongodb中保存的系统时间
						// 由于这里的同步锁是加在对象上的，单个对象多线程调用时是安全的，
						// 但多个对象同时调用该方法时是不安全的（cherry和cherryBatch项目同时使用存在这种情况），
						// 所以这里加入变更前系统时间作为条件来确保本次更新是在原系统时间没有被更新过的情况下做更新处理
						// 如果更新时原系统时间已经被更新过，那么重新再做一次取前进系统时间的逻辑，否则结束处理
						query.put("SysDate", sysDateMongodb);
						dbObject.put("$set", new BasicDBObject("SysDate", sysDate));
						int result = MongoDB.update("MGO_SysDate", query, dbObject);
						if(result > 0) {
							break;
						}
					} else {// 不存在的场合，把MGO_MQSendLog表中最大的发生时间和系统时间进行比较，结果大的那个加入到mongodb中作为下次判断系统时间是否递增的依据
						List<DBObject> _reslutList = MongoDB.find("MGO_MQSendLog", null, null, new BasicDBObject("OccurTime", -1), 0, 1);
						if(_reslutList != null && !_reslutList.isEmpty()) {
							String occurTime = (String)_reslutList.get(0).get("OccurTime");
							if(sysDate.compareTo(occurTime) < 0) {
								sysDate = occurTime;
							}
						}
						DBObject dbObjectInit = new BasicDBObject();
						dbObjectInit.put("SysDate", sysDate);
						MongoDB.insert("MGO_SysDate", dbObjectInit);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					break;
				}
			}
			this.notifyAll();
		}
		return sysDate;
	}
}

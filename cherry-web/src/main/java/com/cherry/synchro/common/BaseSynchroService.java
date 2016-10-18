/*  
 * @(#)BaseSynchroService.java     1.0 2011/05/31      
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
package com.cherry.synchro.common;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.IBaseService;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * BaseDao实现类
 * 
 * @author dingyc
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class BaseSynchroService extends SqlMapClientDaoSupport implements
		IBaseService, Serializable {

	private static final long serialVersionUID = 5298707454262075629L;

	/**
	 * 执行一个存储过程，更新DB数据
	 * 
	 * @param ob
	 *            Object
	 * @return String
	 */
	public Object execProcedure(Object ob) {
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		getSqlMapClientTemplate().queryForObject(sql_id, ob);
		return ob;
	}
	
	/**
	 * 取得结果集个数
	 * 
	 * @param ob
	 *            Object
	 * @return Integer
	 */
	public Integer getSum(Object ob) {
		
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		Integer sum = (Integer) getSqlMapClientTemplate().queryForObject(sql_id, ob);
		return sum;
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
		Object resultObject = getSqlMapClientTemplate().queryForObject(sql_id, ob);
		return resultObject;
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
		Object resultObject = getSqlMapClientTemplate().queryForObject(sqlId, ob);
		return resultObject;
	}

	/**
	 * 取得多个结果
	 * 
	 * @param ob
	 *            Object
	 * @return List
	 */
	public List getList(Object ob) {		
		//SqlMapExecutorDelegate delegate=((ExtendedSqlMapClient)(getSqlMapClientTemplate().getSqlMapClient())).getDelegate(); 
		String sql_id = (String) ((Map) ob).get(CherryConstants.IBATIS_SQL_ID);
		List list = (List) getSqlMapClientTemplate().queryForList(sql_id, ob);
		return list;
	}
	
	/**
	 * 取得多个结果
	 * 
	 * @param ob
	 *            Object
	 * @return List
	 */
	public List getList(Object ob, String sqlId) {
		List list = (List) getSqlMapClientTemplate().queryForList(sqlId, ob);
		return list;
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
}

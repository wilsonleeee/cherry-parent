/*  
 * @(#)IBaseService.java     1.0 2011/05/31      
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

import java.util.List;

@SuppressWarnings("unchecked")
/**
 * Service基类接口
 * 
 * @author huzd
 * @version 1.0 2010.04.12
 */
public interface IBaseService {

	/**
	 * 取得多个结果集
	 * 
	 * @param ob
	 *            Object
	 * @return List
	 */
	public List getList(Object ob);

	/**
	 * 取得结果集件数
	 * 
	 * @param ob
	 *            Object
	 * @return List
	 */
	public Integer getSum(Object ob);

	/**
	 * 取得单个结果集
	 * 
	 * @param ob
	 *            Object
	 * @return Object
	 */
	public Object get(Object ob);

	/**
	 * 插入一组数据
	 * 
	 * @param ob
	 *            Object
	 */
	public void save(Object ob);

	/**
	 * 插入一组数据并返回一个自增长id
	 * 
	 * @param ob
	 *            Object
	 * @return int
	 */
	public int saveBackId(Object ob);

	/**
	 * 删除一组数据
	 * 
	 * @param ob
	 *            Object
	 * @return int
	 */
	public int remove(Object ob);

	/**
	 * 更新一组数据
	 * 
	 * @param ob
	 *            Object
	 * @return int
	 */
	public int update(Object ob);

	/**
	 * 执行一个存储过程，查询并取得结果集
	 * 
	 * @param ob
	 *            Object
	 * @return Object
	 */
	public Object getProcs(Object ob);

	/**
	 * 执行一个存储过程，更新DB数据
	 * 
	 * @param ob
	 *            Object
	 * @return Object
	 */
	public Object updateProcs(Object ob);

	/**
	 * 取得系统时间
	 * 
	 * @return String
	 */
	public String getSYSDate();
}

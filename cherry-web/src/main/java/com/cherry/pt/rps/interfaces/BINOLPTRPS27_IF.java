/*  
 * @(#)BINOLPTRPS27_IF.java     1.0.0 2013/08/08      
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
package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 进销存操作统计接口
 * @author zhangle 1.0.0 2013/08/08
 *
 */
public interface BINOLPTRPS27_IF extends ICherryInterface{
	
	/**
	 * 取得进销存操作统计的总数
	 * @param map
	 * @return
	 */
	public int getCount(Map<String, Object> map);
	
	/**
	 * 取得进销存操作统计的List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getList(Map<String, Object> map);

	/**
	 * 设置进销存操作统计的部门参数
	 * @param map
	 * @throws Exception
	 */
	public void tran_setDepartParameter(Map<String, Object> map) throws Exception;

	/**
	 * 获取进销存操作统计的部门参数
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepartParameter(Map<String, Object> map);
	
	/**
	 * 获得部门类型模式的部门树
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) throws Exception;
	
	/**
	 * 设置进销存操作统计任务
	 * @param map
	 * @throws Exception
	 */
	public void tran_setSchedules(Map<String, Object> map) throws Exception;
	
	/**
	 * 获得进销存操作统计任务
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSchedules(Map<String, Object> map);
	
	
	/**
	 * 导出进销存操作统计报表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception ;

	/**
	 * 获得业务参数
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBussnissParameter(Map<String, Object> map);
	
	/**
	 * 更新业务参数
	 * @param map
	 */
	public void tran_setBussnissParameter(Map<String, Object> map);
	
	/**
	 * 将层级业务List转换为线型
	 * @param departList 部门Id
	 * @param bussinessParameterList 具有层级关系的业务参数
	 * @return
	 */
	public List<Object> getLineBussinessParamter(Map<String, Object> map);
	
	/**
	 * 获取最后一次统计的数据日期
	 * @param map
	 * @return
	 */
	public String getLastStatisticDate(Map<String, Object> map);
}

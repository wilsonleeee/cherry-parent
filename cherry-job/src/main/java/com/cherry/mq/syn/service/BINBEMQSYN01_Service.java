/*	
 * @(#)BINBEMQSYN01_Service.java     1.0 2010/12/01		
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


package com.cherry.mq.syn.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * MQ同步batch处理Service
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
@SuppressWarnings("unchecked")
public class BINBEMQSYN01_Service extends BaseService {
	
	/**
	 * 查询POS品牌数据库MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getWitposMQLogPagingList(Map map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.getWitposMQLogPagingList");
		return (List)witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 查询Cherry的MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getCherryMQLogPagintList(Map map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.getCherryMQLogPagintList");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 判断JMSXGroupID字段是否存在
	 * 
	 * @param map 查询条件
	 * @return true：存在，false：不存在
	 */
	public boolean getJMSXGroupIDCol(Map map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.getJMSXGroupIDCol");
		Map<String, Object> result = (Map)witBaseServiceImpl.get(map);
		if(result != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 查询POS品牌需要重新发送的MQ日志List（带有JMSXGroupID的查询）
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getWitposMQLogSendGroupIDList(Map map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.getWitposMQLogSendGroupIDList");
		return (List)witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 查询POS品牌需要重新发送的MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getWitposMQLogSendList(Map map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.getWitposMQLogSendList");
		return (List)witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 查询Cherry需要重新发送的MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getCherryMQLogSendList(Map map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.getCherryMQLogSendList");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 批量删除POS品牌数据库MQ日志
	 * 
	 * @param list 删除条件
	 */
	public void deleteWitposMQLog(List<Map<String, Object>> list) {
		witBaseServiceImpl.deleteAll(list, "BINBEMQSYN01.deleteWitposMQLog");
	}
	
	/**
	 * 批量删除cherry数据库MQ收发日志
	 * 
	 * @param list 删除条件
	 */
	public void deleteCherryMQLog(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMQSYN01.deleteCherryMQLog");
	}
	
	/**
	 * 批量更新cherry的MQ日志状态
	 * 
	 * @param list 更新条件
	 */
	public void updateCherryMQLog(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMQSYN01.updateCherryMQLog");
	}
	
	/**
	 * 批量更新POS品牌的MQ日志状态
	 * 
	 * @param list 更新条件
	 */
	public void updateWitposMQLog(List<Map<String, Object>> list) {
		witBaseServiceImpl.updateAll(list, "BINBEMQSYN01.updateWitposMQLog");
	}
	
	/**
	 * 把Cherry的MQ日志更新成待处理状态
	 * 
	 * @param Map 更新条件
	 * @return 更新件数
	 */
	public int updateMQLogStatusCherry(Map map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.updateMQLogStatusCherry");
		return baseServiceImpl.update(map);		
	}
	
	/**
	 * 把POS品牌的MQ日志更新成待处理状态
	 * 
	 * @param Map 更新条件
	 * @return 更新件数
	 */
	public int updateMQLogStatusPOS(Map map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.updateMQLogStatusPOS");
		return witBaseServiceImpl.update(map);		
	}
	
	/**
	 * 把Cherry接收失败的MQ日志更新成未比对状态
	 * 
	 * @param Map 更新条件
	 * @return 更新件数
	 */
	public int updateCherryReceiveFlag(Map map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.updateCherryReceiveFlag");
		return baseServiceImpl.update(map);		
	}
	
	/**
	 * 把POS品牌接收失败的MQ日志更新成未比对状态
	 * 
	 * @param Map 更新条件
	 * @return 更新件数
	 */
	public int updateWitReceiveFlag(Map map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.updateWitReceiveFlag");
		return witBaseServiceImpl.update(map);		
	}
	
	/**
	 * 删除重复接收的MQ收发日志
	 * 
	 * @param list 删除条件
	 */
	public void deleteCherryMQLogRe(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMQSYN01.deleteCherryMQLogRe");
	}
	
	/**
	 * 删除POS品牌数据库多余的接收日志
	 * 
	 * @param map 删除条件
	 */
	public void deleteLeftWitposMQLog(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.deleteLeftWitposMQLog");
		witBaseServiceImpl.remove(map);
	}
	
	/**
	 * 删除cherry数据库多余的接收日志
	 * 
	 * @param map 删除条件
	 */
	public void deleteLeftCherryMQLog(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN01.deleteLeftCherryMQLog");
		baseServiceImpl.remove(map);
	}

}

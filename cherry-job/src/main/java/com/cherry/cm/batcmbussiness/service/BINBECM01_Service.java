/*	
 * @(#)BINBECM01_Service.java     1.0 2012/06/15		
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
package com.cherry.cm.batcmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * JOB执行相关共通 Service
 * 
 * @author hub
 * @version 1.0 2012/06/15
 */
public class BINBECM01_Service extends BaseService{
	
	/**
	 * 插入Job运行日志表
	 * 
	 * @param map
	 * @return
	 */
	public void insertBatchLog(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECM01.insertBatchLog");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 更新Job运行日志表
	 * 
	 * @param map
	 * @return int
	 * 			更新件数
	 */
	public int updateBatchLog(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECM01.updateBatchLog");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 删除Job运行日志表
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delBatchLog(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECM01.delBatchLog");
		return baseServiceImpl.remove(parameterMap);
	}
	
	 /**
     * 取得运行日志状态
     * 
     * @param map
     * @return String
     * 		运行日志状态
     */
    public String getBatchLogStatus(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECM01.getBatchLogStatus");
        return (String) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 取得Job控制程序的数据截取开始时间及结束时间
     * map.organizationInfoId  品牌ID
     * map.brandInfoId  品牌ID
     * map.jobCode 程序BatchCD
     * @param map
     * @return
     */
    public Map<String,Object> getJobControlInfo(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECM01.getJobControlInfo");
        return (Map<String,Object>) baseServiceImpl.get(parameterMap);
    }
    
	/**
	 * 更新Job控制表（更新TargetDataStartTime）
	 * 
	 * @param map
     * map.organizationInfoId  品牌ID
     * map.brandInfoId  品牌ID
     * map.jobCode 程序BatchCD
     * map.TargetDataEndTime 数据截取结束时间
     * map.
     * 
	 * @return int
	 * 			更新件数
	 */
	public int updateJobControl(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBECM01.updateJobControl");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 插入Job运行履历表
	 * map.RunType *_FN程序：AT , *_Action程序:MT 
	 * map.RunStartTime 程序开始运行时取得的getSYSDateTime()
	 * map.RunEndTime 程序结束运行时取得的getSYSDateTime()
	 * map.Result 程序整体运行结果  S:正常、W: 警告、E:异常
	 * map.Comments 
	 * @param map
	 * @return
	 */
	public void insertJobRunHistory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBECM01.insertJobRunHistory");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 插入Job运行数据失败履历表
	 * 
	 * @param map
	 * @return
	 */
	public void insertJobRunFaildHistory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBECM01.insertJobRunFaildHistory");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 删除 Job运行数据失败履历表
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delJobRunFaildHistory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECM01.delJobRunFaildHistory");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 批量删除 Job运行数据失败履历表
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public void delAllJobRunFaildHistory(List<Map<String, Object>> list) {
		 baseServiceImpl.deleteAll(list,"BINBECM01.delJobRunFaildHistory");
	}
	
	/**
	 * 更新 Job运行数据失败履历表
	 * 
	 * @param map
	 * @return int
	 * 			更新件数
	 */
	public int updJobRunFaildHistory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBECM01.updJobRunFaildHistory");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 更新JobRunFaildHistory表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergeJobRunFaildHistory(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECM01.mergeJobRunFaildHistory");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}

}

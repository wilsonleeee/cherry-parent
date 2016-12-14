/*	
 * @(#)BINBECM01_IF.java     1.0 2012/06/15		
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
package com.cherry.cm.batcmbussiness.interfaces;

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchException;

/**
 * JOB执行相关共通 IF
 * 
 * @author hub
 * @version 1.0 2012/06/15
 */
public interface BINBECM01_IF {
	
	/**
	 * 插入Job运行日志表
	 * 
	 * @param map
	 * @return
	 */
	public void insertBatchLog(Map<String, Object> map);
	
	/**
	 * 更新Job运行日志表
	 * 
	 * @param map
	 * @return int
	 * 			更新件数
	 */
	public int updateBatchLog(Map<String, Object> map);
	
	/**
	 * 删除Job运行日志表
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delBatchLog(Map<String, Object> map);
	
	 /**
     * 取得运行日志状态
     * 
     * @param map
     * @return String
     * 		运行日志状态
     */
    public String getBatchLogStatus(Map<String, Object> map);
    
    /**
     * 取得系统时间
     * 
     * @return String
     * 		运行日志状态
     */
    public String getSYSDate();
    
    /**
     * 取得系统时间(yyyy-MM-dd HH:mm:ss)
     * 
     * @return String
     * 		运行日志状态
     */
    public String getSYSDateTime();
    
    /**
     * 取得Job控制程序的数据截取开始时间及结束时间
     * 
     * @param map
     * map.organizationInfoId  品牌ID
     * map.brandInfoId  品牌ID
     * map.JobCode 程序BatchCD
     * map.RunStartTime 【开始运行时间】 程序开始执行的DB系统时间(进入程序时取得的时间[yyyy-MM-dd HH:mm:ss]，在处理业务逻辑之前取得 )
     * 
     * @return
     * TargetDataStartTime 数据截取开始时间
     * TargetDataEndTime 数据截取结束时间
     * 
     * @throws CherryBatchException,Exception 
     */
	public Map<String,Object> getJobControlInfo(Map<String, Object> map) throws CherryBatchException,Exception;
	
	/**
	 * 更新Job控制表（更新TargetDataStartTime）
	 * 
	 * @param map
     * map.organizationInfoId  品牌ID
     * map.brandInfoId  品牌ID
     * map.JobCode 程序BatchCD
     * map.RunStartTime 【开始运行时间】 程序开始执行的DB系统时间(进入程序时取得的时间[yyyy-MM-dd HH:mm:ss]，在处理业务逻辑之前取得 )
     * map.TargetDataEndTime 数据截取结束时间
     * 
	 * @return int
	 * 			更新件数
	 */
	public int updateJobControl(Map<String, Object> map);
	
	/**
	 * 插入Job运行履历表(在程序结束时执行)
     * map.organizationInfoId  品牌ID
     * map.brandInfoId  品牌ID
	 * map.JobCode BatchList注册的BATCD
	 * map.RunType *_FN程序：AT , *_Action程序:MT 
	 * map.RunStartTime 程序开始运行时取得的getSYSDateTime()
	 * map.flag BATCH处理标志(BATCH_SUCCESS、BATCH_WARNING、BATCH_ERROR)
	 * map.Comments JSON格式 {"TargetDataCNT":"500","SCNT":"498",""FCNT":"2","ICNT":"2",UCNT":"2","FReason":"字段超长"}  具体见conflunce文档
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public void insertJobRunHistory(Map<String, Object> map) throws Exception;
	
	/**
	 * 插入Job运行数据失败履历表
	 * 
	 * @param map
	 * @return
	 */
	public void insertJobRunFaildHistory(Map<String, Object> map);
	
	/**
	 * 删除 Job运行数据失败履历表
	 * 
	 * @param map 删除条件
     * map.organizationInfoId  品牌ID
     * map.brandInfoId  品牌ID
     * map.JobCode 程序BatchCD
	 * map.UnionIndex 唯一性联合CODE
	 * map.UnionIndex1、map.UnionIndex2、map.UnionIndex3 唯一性联合CODE (根据实际业务，可选)
	 * 
	 * @return 删除件数
	 */
	public int delJobRunFaildHistory(Map<String, Object> map) ;
	
	/**
	 * 删除 Job运行数据失败履历表
	 * 
	 * @param map 删除条件
	 * map.organizationInfoId  品牌ID
	 * map.brandInfoId  品牌ID
	 * map.JobCode 程序BatchCD
	 * map.UnionIndex 唯一性联合CODE
	 * map.UnionIndex1、map.UnionIndex2、map.UnionIndex3 唯一性联合CODE (根据实际业务，可选)
	 * 
	 * @return 删除件数
	 */
	public void delAllJobRunFaildHistory(List<Map<String, Object>> list);
	
	/**
	 * 更新 Job运行数据失败履历表
	 * 
	 * @param map
	 * @return int
	 * 			更新件数
	 */
	public int updJobRunFaildHistory(Map<String, Object> map);
	
	/**
	 * 更新JobRunFaildHistory表
	 * @param map
     * map.organizationInfoId  品牌ID
     * map.brandInfoId  品牌ID
     * map.JobCode 程序BatchCD
	 * map.UnionIndex 唯一性联合CODE
	 * map.UnionIndex1、map.UnionIndex2、map.UnionIndex3 唯一性联合CODE (根据实际业务，可选)
	 * map.ErrorMsg 记录数据失败的原因。
	 * map.Comments 日志
	 * @return 
	 */
	public Map<String,Object> mergeJobRunFaildHistory(Map<String, Object> map);
}

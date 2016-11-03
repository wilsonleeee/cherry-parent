/*	
 * @(#)BINBECM01_BL.java     1.0 2012/06/15		
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
package com.cherry.cm.batcmbussiness.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.batcmbussiness.service.BINBECM01_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;

/**
 * JOB执行相关共通 BL
 * 
 * @author hub
 * @version 1.0 2012/06/15
 */
public class BINBECM01_BL implements BINBECM01_IF{
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBECM01_BL.class);	
	
	@Resource
	private BINBECM01_Service binbecm01_service;
	
	/**
	 * 插入Job运行日志表
	 * 
	 * @param map
	 * @return
	 */
	public void insertBatchLog(Map<String, Object> map) {
		// 插入Job运行日志表
		binbecm01_service.insertBatchLog(map);
	}
	
	/**
	 * 更新Job运行日志表
	 * 
	 * @param map
	 * @return int
	 * 			更新件数
	 */
	public int updateBatchLog(Map<String, Object> map) {
		// 更新Job运行日志表
		return binbecm01_service.updateBatchLog(map);
	}
	
	/**
	 * 删除Job运行日志表
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delBatchLog(Map<String, Object> map) {
		// 删除Job运行日志表
		return binbecm01_service.delBatchLog(map);
	}
	
	 /**
     * 取得运行日志状态
     * 
     * @param map
     * @return String
     * 		运行日志状态
     */
    public String getBatchLogStatus(Map<String, Object> map) {
    	// 取得运行日志状态
		return binbecm01_service.getBatchLogStatus(map);
    }
    
    /**
     * 取得系统时间
     * 
     * @return String
     * 		运行日志状态
     */
    public String getSYSDate() {
    	// 取得运行日志状态
		return binbecm01_service.getSYSDate();
    }
    
    /**
     * 取得系统时间(yyyy-MM-dd HH:mm:ss)
     * 
     * @return String
     * 		运行日志状态
     */
    public String getSYSDateTime() {
    	// 取得运行日志状态
    	return binbecm01_service.getSYSDateTime();
    }
    
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
    @SuppressWarnings("unchecked")
	public Map<String,Object> getJobControlInfo(Map<String, Object> map) throws CherryBatchException,Exception {
    	Map<String,Object> jobControlMap = null;
		// 计算数据【截取结束时间】
		String targetDataEndTime = null;
    	try{
    		
    		// 【开始运行时间】
    		String runStartTime = ConvertUtil.getString(map.get("RunStartTime"));
    		
    		jobControlMap = binbecm01_service.getJobControlInfo(map);
    		if(null != jobControlMap && !jobControlMap.isEmpty()){
    			// 数据截取开始时间
    			String targetDataStartTime = ConvertUtil.getString(jobControlMap.get("TargetDataStartTime"));
    			
    			if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
    				targetDataStartTime = DateUtil.getSpecificDate(targetDataStartTime, DateUtil.DATETIME_PATTERN);
    				
    				// 截取时间跨度 {"CutTimeType":"","CutTime":""} CutTimeType: Day、Hour、Minute
    				String spanTimeStr = ConvertUtil.getString(jobControlMap.get("SpanTime"));
    				
    				
    				if(CherryBatchUtil.isBlankString(spanTimeStr)){
//    			jobControlMap.put("TargetDataEndTime", runStartTime);
    					targetDataEndTime = runStartTime;
    				}else{
    					if(DateUtil.checkDate(spanTimeStr, DateUtil.DATETIME_PATTERN)){
    						// 如果是yyyy-MM-dd HH:mm:ss，则作为【截取结束时间】使用，但要判断这个【截取结束时间】是否大于【截取开始时间】，如果小于则使用【开始运行时间】
    						int dateComp = DateUtil.compareDate(spanTimeStr, targetDataStartTime);
    						if(dateComp <= 0){
    							//jobControlMap.put("TargetDataEndTime", spanTimeStr);
    							targetDataEndTime = runStartTime;
    						}else{
//    					jobControlMap.put("TargetDataEndTime", spanTimeStr);
    							dateComp = DateUtil.compareDate(spanTimeStr, runStartTime);
    							if(dateComp <= 0){
    								targetDataEndTime = spanTimeStr;
    							}else{
    								targetDataEndTime = runStartTime;
    							}
    							
    						}
    					}else{
    						if(spanTimeStr.length() >1){
    							String dateType = spanTimeStr.substring(spanTimeStr.length() - 1, spanTimeStr.length());
    							Integer dateLen = Integer.parseInt(spanTimeStr.substring(0, spanTimeStr.length() - 1));
    							if("Y".equals(dateType)){
    								targetDataEndTime = DateUtil.addDateByYears(DateUtil.DATETIME2_PATTERN, targetDataStartTime, dateLen);
    							}else if("M".equals(dateType)){
    								targetDataEndTime = DateUtil.addDateByMonth(DateUtil.DATETIME2_PATTERN, targetDataStartTime, dateLen);
    							}
    							else if("D".equals(dateType)){
    								targetDataEndTime = DateUtil.addDateByDays(DateUtil.DATETIME2_PATTERN, targetDataStartTime, dateLen);
    							}
    							else if("H".equals(dateType)){
    								targetDataEndTime = DateUtil.addDateByHours(DateUtil.DATETIME2_PATTERN, targetDataStartTime, dateLen);
    							}
    							else if("m".equals(dateType)){
    								targetDataEndTime = DateUtil.addDateByMinutes(DateUtil.DATETIME2_PATTERN, targetDataStartTime, dateLen);
    							}
    							
    						}
    						
    						// 判断【截取结束时间】是否大于【开始运行时间】，如果大于则使用【开始运行时间】
    						int dateComp = DateUtil.compareDate(targetDataEndTime, runStartTime);
    						if(dateComp <= 0){
    							//jobControlMap.put("TargetDataEndTime", spanTimeStr);
//    					targetDataEndTime = targetDataEndTime;
    						}else{
//    					jobControlMap.put("TargetDataEndTime", spanTimeStr);
    							targetDataEndTime = runStartTime;
    						}
    					}
    				}
    			}else{
    				// 【截取数据开始时间】为null时，直接返回null
    				jobControlMap.put("TargetDataStartTime", null);
    			}
    			
    		}
    		   
    	}catch(Exception e){
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EOT00069");
			
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO);
    	}
    	
    	if(null != jobControlMap && !jobControlMap.isEmpty()){
    		jobControlMap.put("TargetDataEndTime", targetDataEndTime);
    	}else{
    		// 查询不到对应的JobControl数据时，截取数据开始/结束时间设为null
    		jobControlMap = new HashMap<String, Object>();
    		jobControlMap.put("TargetDataStartTime", null);
    		jobControlMap.put("TargetDataEndTime", null);
    	}
    	
    	
    	return jobControlMap;
    }
	
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
	public int updateJobControl(Map<String, Object> map) {
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.putAll(map);
		 
		// 【开始运行时间】
		String runStartTime = ConvertUtil.getString(map.get("RunStartTime"));
		// 【数据截取结束时间】
		String targetDataEndTime = ConvertUtil.getString(map.get("TargetDataEndTime"));
		
		// 如果计算得到的【数据截取结束时间】
		// 								大于当前【开始运行时间】，则【数据截取开始时间】更新成【开始运行时间】（程序刚运行时取得的getSYSDateTime()）,
		// 								小于当前【开始运行时间】，则【数据截取开始时间】更新成【数据截取结束时间】
		int dateComp = DateUtil.compareDate(targetDataEndTime, runStartTime);
		if(dateComp <= 0){
			paraMap.put("targetDataStartTime", targetDataEndTime);
		}else if (dateComp > 0){
			paraMap.put("targetDataStartTime", runStartTime);
		}
		
		// 更新Job控制表
		return binbecm01_service.updateJobControl(paraMap);
	}
	
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
	public void insertJobRunHistory(Map<String, Object> map) throws Exception {
		Map<String, Object> insMap = new HashMap<String, Object>();
		insMap.putAll(map);
		
		// RunEndTime 程序结束运行时取得的getSYSDateTime()
		String runEndTime = binbecm01_service.getSYSDateTime();
		insMap.put("RunEndTime", runEndTime);
		
		// 运行结果 整体运行结果 S:正常、W: 警告、E:异常
		Integer flag = (Integer)insMap.get("flag");
		String result = "S";
		switch(flag){
			case CherryBatchConstants.BATCH_WARNING:
				result = "W";
				break;
			case CherryBatchConstants.BATCH_ERROR:
				result = "E";
				break;
			default:
				result = "S";
				break;
		}
		insMap.put("Result", result);
		
		//日志。组装成JSON格式：
		//{"TargetDataCNT":"500","SCNT":"498","FCNT":"2","FReason":"字段超长"}
		//TargetDataCNT：待处理数据件数
		//SCNT：处理成功件数
		//FCNT：处理失败件数
		//Freason：失败的主要原因，受字段长度限制，这里只要记录主要原因即可

		Map<String,Object> commentsMap = new HashMap<String, Object>();
		commentsMap.put("TargetDataCNT", insMap.get("TargetDataCNT"));
		commentsMap.put("SCNT",  insMap.get("SCNT"));
		commentsMap.put("FCNT",  insMap.get("FCNT"));
		
		// 更新件数
		String uCNT = ConvertUtil.getString(insMap.get("UCNT"));
		if(!CherryBatchUtil.isBlankString(uCNT)){
			commentsMap.put("UCNT",  uCNT);
		}
		// 插入件数 
		String iCNT = ConvertUtil.getString(insMap.get("ICNT"));
		if(!CherryBatchUtil.isBlankString(iCNT)){
			commentsMap.put("ICNT",  iCNT);
		}
		// 【数据截取开始时间】
		String targetDataStartTime = ConvertUtil.getString(insMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			commentsMap.put("TargetDataStartTime",  targetDataStartTime);
		}
		// 【数据截取结束时间】
		String targetDataEndTime = ConvertUtil.getString(insMap.get("TargetDataEndTime"));
		if(!CherryBatchUtil.isBlankString(targetDataEndTime)){
			commentsMap.put("TargetDataEndTime",  targetDataEndTime);
		}
		commentsMap.put("FReason", insMap.get("FReason"));
		String commentsJson = CherryUtil.map2Json(commentsMap);
		insMap.put("Comments", commentsJson);
		
		binbecm01_service.insertJobRunHistory(insMap);
	}
	
	/**
	 * 插入Job运行数据失败履历表
	 * 
	 * @param map
	 * @return
	 */
	public void insertJobRunFaildHistory(Map<String, Object> map){
		// 插入Job运行日志表
		binbecm01_service.insertJobRunFaildHistory(map);
	}
	
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
	public int delJobRunFaildHistory(Map<String, Object> map) {
		return binbecm01_service.delJobRunFaildHistory(map);
	}
	
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
	public void delAllJobRunFaildHistory(List<Map<String, Object>> list) {
		binbecm01_service.delAllJobRunFaildHistory(list);
	}
	
	/**
	 * 更新 Job运行数据失败履历表
	 * 
	 * @param map
	 * @return int
	 * 			更新件数
	 */
	public int updJobRunFaildHistory(Map<String, Object> map){
		return binbecm01_service.updJobRunFaildHistory(map);
	}
	
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
	public Map<String,Object> mergeJobRunFaildHistory(Map<String, Object> map){
		return binbecm01_service.mergeJobRunFaildHistory(map);
	}
}

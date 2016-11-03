/*	
 * @(#)BINBEMBVIS03_Service.java     1.0 2012/12/18		
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
package com.cherry.mb.vis.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.mb.vis.service.BINBEMBVIS03_Service;

/**
 * 会员回访任务下发batch处理BL
 * 
 * @author WangCT
 * @version 1.0 2012/12/18
 */
public class BINBEMBVIS03_BL {
	
	/** 会员回访任务下发batch处理Service **/
	@Resource
	private BINBEMBVIS03_Service binBEMBVIS03_Service;
	
	/** BATCH处理标志 **/
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/**
	 * 
	 * 会员回访任务同步主处理
	 * 
	 */
	public int tran_MemVisitTaskSyn(Map<String,Object> map) throws Exception {
		
		try {
			// 从会员回访任务接口表中删除已导入到老后台的回访任务
			binBEMBVIS03_Service.delWitCancelVisitTask(map);
			binBEMBVIS03_Service.ifManualCommit();
		} catch (Exception e) {
			try {
				binBEMBVIS03_Service.ifManualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EMB00012");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		
		try {
			// 同步进行中的回访任务
			this.visitTaskSyn(map);
		} catch (Exception e) {
			try {
				binBEMBVIS03_Service.ifManualRollback();
				binBEMBVIS03_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EMB00013");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		
		try {
			// 同步取消的回访任务
			this.cancelVisitTaskSyn(map);
		} catch (Exception e) {
			try {
				binBEMBVIS03_Service.ifManualRollback();
				binBEMBVIS03_Service.manualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EMB00014");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		
		try {
			// 同步已完成的回访任务
			this.completedVisitTaskSyn(map);
		} catch (Exception e) {
			try {
				binBEMBVIS03_Service.ifManualRollback();
			} catch (Exception ex) {
				
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EMB00011");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return flag;
		}
		return flag;
	}
	
	/**
	 * 
	 * 同步已完成的回访任务
	 * 
	 */
	public void completedVisitTaskSyn(Map<String,Object> map) {
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 取得已完成的会员回访任务List
			List<Map<String, Object>> visitTaskList = binBEMBVIS03_Service.getCompletedVisitTaskList(map);
			// 已完成的会员回访任务不为空
			if (visitTaskList != null && !visitTaskList.isEmpty()) {
				// 从会员回访任务接口表中删除已完成的回访任务
				binBEMBVIS03_Service.delWitCompletedVisitTask(visitTaskList);
				// 从会员回访任务表中把已完成的回访任务更新成接口表已删除
				binBEMBVIS03_Service.updateCompletedVisitTask(visitTaskList);
				
				binBEMBVIS03_Service.ifManualCommit();
				binBEMBVIS03_Service.manualCommit();
				
				// 会员回访任务少于一次抽取的数量，即为最后一页，跳出循环
				if(visitTaskList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * 
	 * 同步进行中的回访任务
	 * 
	 */
	public void visitTaskSyn(Map<String,Object> map) {

		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 取得进行中的未下发的会员回访任务List
			List<Map<String, Object>> visitTaskList = binBEMBVIS03_Service.getVisitTaskList(map);
			// 进行中的未下发的会员回访任务不为空
			if (visitTaskList != null && !visitTaskList.isEmpty()) {
				// 把进行中的未下发的回访任务下发到会员回访任务接口表中
				binBEMBVIS03_Service.insertWitVisitTask(visitTaskList);
				// 从会员回访任务表中把状态为进行中的未下发的回访任务更新成已下发
				binBEMBVIS03_Service.updateVisitTask(visitTaskList);
				
				binBEMBVIS03_Service.ifManualCommit();
				binBEMBVIS03_Service.manualCommit();
				
				// 会员回访任务少于一次抽取的数量，即为最后一页，跳出循环
				if(visitTaskList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * 
	 * 同步取消的回访任务
	 * 
	 */
	public void cancelVisitTaskSyn(Map<String,Object> map) {
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 取得已取消的未下发的会员回访任务List
			List<Map<String, Object>> visitTaskList = binBEMBVIS03_Service.getCancelVisitTaskList(map);
			// 已取消的未下发的会员回访任务不为空
			if (visitTaskList != null && !visitTaskList.isEmpty()) {
				// 把已取消的未下发的回访任务下发到会员回访任务接口表中
				binBEMBVIS03_Service.updateWitVisitTask(visitTaskList);
				// 从会员回访任务表中把状态为取消的未下发的回访任务更新成已下发
				binBEMBVIS03_Service.updateCanlelVisitTask(visitTaskList);
				
				binBEMBVIS03_Service.ifManualCommit();
				binBEMBVIS03_Service.manualCommit();
				
				// 会员回访任务少于一次抽取的数量，即为最后一页，跳出循环
				if(visitTaskList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
	}

}

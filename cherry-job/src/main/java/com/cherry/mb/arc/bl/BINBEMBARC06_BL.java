/*	
 * @(#)BINBEMBARC06_BL.java     1.0 2013/12/19
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
package com.cherry.mb.arc.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.mb.arc.service.BINBEMBARC06_Service;

/**
 * 汇美舍会员积分清零明细下发处理 BL
 * 
 * @author hub
 * @version 1.0 2013/12/19
 */
public class BINBEMBARC06_BL {
	
	/** 汇美舍会员积分清零明细处理Service */
	@Resource
	private BINBEMBARC06_Service binBEMBARC06_Service;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC06_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 失败条数 */
	private int failCount = 0;
	
	/** 共通Batch Log处理*/
	private CherryBatchLogger cherryBatchLogger;
	
	/** 共通BatchLogger*/
	private BatchLoggerDTO batchLoggerDTO;
	
	/**
	 * 
	 * 积分清零batch主处理
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	public int tran_clearDetailSend(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		// 去除执行标识
		binBEMBARC06_Service.updateClearExecFlag(map);
		try {
			// 取得需要更新清零记录的会员信息List
			List<Map<String, Object>> upClearList = binBEMBARC06_Service.getUpClearList(map);
			if (null != upClearList && !upClearList.isEmpty()) {
				// 删除清零记录(小于最大重算次数的记录)
				binBEMBARC06_Service.delPointsClearRecord(upClearList);
				// 将最大重算次数的记录更新为下发状态
				binBEMBARC06_Service.updateSendStatus(upClearList);
				// 提交事务
				binBEMBARC06_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBARC06_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error(e.getMessage(),e);
		}
		// 更新执行标识
		binBEMBARC06_Service.updateExecFlag(map);
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "clearRecordId");
		// 取得需要下发的清零明细总数
		int count = binBEMBARC06_Service.getClearSendCount(map);
		// 没有新增的会员积分清零记录
		if (0 == count) {
			// 未新增积分清零记录，不执行下发
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00010");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			// 分批取得积分清零记录，并处理
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00001");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00009", null));
			batchLoggerDTO.addParam(String.valueOf(count));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			while (true) {
				// 查询开始位置
				startNum = dataSize * currentNum + 1;
				// 查询结束位置
				endNum = startNum + dataSize - 1;
				// 数据抽出次数累加
				currentNum++;
				// 查询开始位置
				map.put(CherryBatchConstants.START, startNum);
				// 查询结束位置
				map.put(CherryBatchConstants.END, endNum);
				// 取得需要下发的清零明细List
				List<Map<String, Object>> clearSendList = binBEMBARC06_Service.getClearSendList(map);
				// 清零明细List不为空
				if (!CherryBatchUtil.isBlankList(clearSendList)) {
					try {
						// 执行下发处理
						executeSendDetail(clearSendList, map, currentNum);
					} catch (Exception e) {
						logger.error("Points clear detail send exception：" + e.getMessage(),e);
					}
					// 清零明细少于一次抽取的数量，即为最后一页，跳出循环
					if(clearSendList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
			}
			try {
				// 去除执行标识
				binBEMBARC06_Service.updateClearExecFlag(map);
				// 提交事务
				binBEMBARC06_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBARC06_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error(e.getMessage(),e);
			}
			// 总件数
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("IIF00001");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO1.addParam(String.valueOf(count));
			// 成功总件数
			BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
			batchLoggerDTO2.setCode("IIF00002");
			batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO2.addParam(String.valueOf(count - failCount));
			// 失败件数
			BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
			batchLoggerDTO5.setCode("IIF00005");
			batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO5.addParam(String.valueOf(failCount));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
					.getClass());
			// 处理总件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
			// 成功总件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO2);
			// 失败件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		}
		return flag;
	}
	
	/**
	 * 执行清零处理
	 * 
	 * @param campBaseDTOList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeSendDetail(List<Map<String, Object>> clearSendList, Map<String, Object> map, int pageNum) throws Exception {
		// 总件数
		int totalCount = clearSendList.size();
		// 失败件数
		int sendFailCount = 0;
		// 新增列表
		List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
		// 更新列表
		List<Map<String, Object>> upList = new ArrayList<Map<String, Object>>();
		// 品牌代号
		String brandCode = (String) map.get("brandCode");
		for (Map<String, Object> clearSendMap : clearSendList) {
			clearSendMap.put("brandCode", brandCode);
			// 取得同一会员同一清零时间的记录
			int count = binBEMBARC06_Service.getIFDetailCount(clearSendMap);
			if (0 == count) {
				addList.add(clearSendMap);
			} else {
				upList.add(clearSendMap);
			}
		}
		if (!addList.isEmpty()) {
			try {
				// 插入积分清零通知清单接口表 
				binBEMBARC06_Service.addIFClearDetail(addList);
				binBEMBARC06_Service.ifManualCommit();
				try {
					// 更新下发状态(成功)
					binBEMBARC06_Service.updateStatusSuccess(addList);
					binBEMBARC06_Service.manualCommit();
				} catch (Exception e) {
					try {
						binBEMBARC06_Service.manualRollback();
					} catch (Exception ex) {	
						
					}
					// 标记下发状态为成功时发生异常
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00030");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					throw e;
				}
			} catch (Exception e) {
				try {
					binBEMBARC06_Service.ifManualRollback();
				} catch (Exception ex) {	
					
				}
				// 打印错误信息
				logger.error(e.getMessage(),e);
				// 新增积分清零明细失败
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00031");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				flag = CherryBatchConstants.BATCH_WARNING;
				sendFailCount = addList.size();
				try {
					// 更新下发状态(失败)
					binBEMBARC06_Service.updateStatusFail(addList);
					binBEMBARC06_Service.manualCommit();
				} catch (Exception exc) {
					try {
						binBEMBARC06_Service.manualRollback();
					} catch (Exception ex) {	
						
					}
					// 标记下发状态为失败时发生异常
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00032");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}
		}
		if (!upList.isEmpty()) {
			try {
				// 更新积分清零通知清单接口表 
				binBEMBARC06_Service.updateIFClearDetail(upList);
				binBEMBARC06_Service.ifManualCommit();
				try {
					// 更新下发状态(成功)
					binBEMBARC06_Service.updateStatusSuccess(upList);
					binBEMBARC06_Service.manualCommit();
				} catch (Exception e) {
					try {
						binBEMBARC06_Service.manualRollback();
					} catch (Exception ex) {	
						
					}
					// 标记下发状态为成功时发生异常
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00030");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					throw e;
				}
			} catch (Exception e) {
				try {
					binBEMBARC06_Service.ifManualRollback();
				} catch (Exception ex) {	
					
				}
				// 打印错误信息
				logger.error(e.getMessage(),e);
				// 更新积分清零明细失败
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMB00033");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				flag = CherryBatchConstants.BATCH_WARNING;
				sendFailCount += upList.size();
				try {
					// 更新下发状态(失败)
					binBEMBARC06_Service.updateStatusFail(upList);
					binBEMBARC06_Service.manualCommit();
				} catch (Exception exc) {
					try {
						binBEMBARC06_Service.manualRollback();
					} catch (Exception ex) {	
						
					}
					// 标记下发状态为失败时发生异常
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00032");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}
		}
		// 本批次下发完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00011");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(pageNum));
		batchLoggerDTO.addParam(String.valueOf(totalCount));
		batchLoggerDTO.addParam(String.valueOf(sendFailCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
	}
}

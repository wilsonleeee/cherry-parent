/*
 * @(#)BINBETLBAT09_BL.java     1.0 2013/10/22
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
package com.cherry.tl.bat.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.Bean2Map;
import com.cherry.mq.mes.bl.BINBEMQMES99_BL;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.dto.SalMainDataDTO;
import com.cherry.mq.mes.dto.SaleReturnMainDataDTO;
import com.cherry.tl.bat.service.BINBETLBAT09_Service;

/**
 * 
 * 修改销售单据处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/10/22
 */
public class BINBETLBAT09_BL {
	
	/** 接收MQ消息共通 BL **/
	@Resource
	private BINBEMQMES99_BL binBEMQMES99_BL;
	
	/** 修改销售单据处理Service **/
	@Resource
	private BINBETLBAT09_Service binBETLBAT09_Service;
	
	/**
	 * 修改销售单据处理
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int tran_analyzeMessage(Map<String, Object> map) throws Exception {
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 处理成功件数
		int successCount = 0;
		// 处理失败件数
		int failCount = 0;
		
		try {
			// 把POS品牌的销售MQ记录更新成待处理状态
			binBETLBAT09_Service.updateWitPosMQLogInit(map);
			binBETLBAT09_Service.witManualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			// 把POS品牌的销售MQ记录更新成待处理状态时发生错误
			batchLoggerDTO.setCode("ETL00019");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 查询POS品牌的销售MQ记录List
			List<Map<String, Object>> witposMQLogList = binBETLBAT09_Service.getWitposMQLogList(map);
			if (witposMQLogList != null && !witposMQLogList.isEmpty()) {
				// 处理成功的记录
				List<Map<String, Object>> delWitMqLogList = new ArrayList<Map<String,Object>>();
				// 处理失败的记录
				List<Map<String, Object>> updWitMqLogList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < witposMQLogList.size(); i++) {
					Map<String, Object> witposMQLogMap = witposMQLogList.get(i);
					String billCode = "";
					try {
						String msg = (String)witposMQLogMap.get("originalMsg");
						// 解析消息体
						Object mainDataDTO = Message2Bean.parseMessage((String) msg);
						Map<String, Object> mainDataMap = (Map)Bean2Map.toHashMap(mainDataDTO);
						billCode = (String)mainDataMap.get("tradeNoIF");
						// 查询组织品牌信息
						binBEMQMES99_BL.selMessageOrganizationInfo(mainDataMap);
						// 处理销售库存数据
						if (mainDataDTO instanceof SalMainDataDTO){
							binBEMQMES99_BL.analyzeSaleStockMessage(mainDataMap);
						} else if(mainDataDTO instanceof SaleReturnMainDataDTO){
						    // 处理新销售退货数据
							binBEMQMES99_BL.analyzeSaleReturnStockMessage(mainDataMap);
						}
						binBETLBAT09_Service.manualCommit();
						delWitMqLogList.add(witposMQLogMap);
					} catch (Exception e) {
						try {
							// Cherry数据库回滚事务
							binBETLBAT09_Service.manualRollback();
						} catch (Exception ex) {
							
						}
						witposMQLogMap.put("flag", "2");
						updWitMqLogList.add(witposMQLogMap);
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						// 处理销售退货数据时发生错误
						batchLoggerDTO.setCode("ETL00020");
						batchLoggerDTO.addParam(billCode);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					} catch (Throwable t) {
						try {
							// Cherry数据库回滚事务
							binBETLBAT09_Service.manualRollback();
						} catch (Exception ex) {
							
						}
						witposMQLogMap.put("flag", "2");
						updWitMqLogList.add(witposMQLogMap);
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						// 处理销售退货数据时发生错误
						batchLoggerDTO.setCode("ETL00020");
						batchLoggerDTO.addParam(billCode);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
					}
				}
				// 删除处理成功的记录
				if(delWitMqLogList.size() > 0) {
					try {
						// 删除POS品牌的销售MQ记录
						binBETLBAT09_Service.deleteWitposMQLog(delWitMqLogList);
						binBETLBAT09_Service.witManualCommit();
					} catch (Exception e) {
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						// 删除POS品牌的销售MQ记录时发生错误
						batchLoggerDTO.setCode("ETL00021");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
						break;
					}
					successCount += delWitMqLogList.size();
				}
				// 更新处理失败的记录
				if(updWitMqLogList.size() > 0) {
					try {
						// 更新POS品牌的销售MQ记录状态
						binBETLBAT09_Service.updateWitposMQLog(updWitMqLogList);
						binBETLBAT09_Service.witManualCommit();
					} catch (Exception e) {
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						// 更新POS品牌的销售MQ记录状态时发生错误
						batchLoggerDTO.setCode("ETL00022");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
						break;
					}
					failCount += updWitMqLogList.size();
				}
				if(witposMQLogList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 处理成功件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("ETL00023");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount));
		
		// 处理失败件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("ETL00024");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(failCount));
		
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		
		return flag;
	}
	
}

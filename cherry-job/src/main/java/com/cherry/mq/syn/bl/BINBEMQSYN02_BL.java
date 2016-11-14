package com.cherry.mq.syn.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mq.mes.bl.BINBEMQMES99_BL;
import com.cherry.mq.mes.common.Message2Bean;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.syn.service.BINBEMQSYN02_Service;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * MQ同步batch处理BL
 * 
 * @author WangCT
 * @version 1.0 2015.04.10
 */
public class BINBEMQSYN02_BL {
	
	/** MQ同步batch处理Service */
	@Resource
	private BINBEMQSYN02_Service binBEMQSYN02_Service;
	
	/** 消息数据接收处理BL */
	@Resource
	private BINBEMQMES99_BL binBEMQMES99_BL;
	
	/** ActiveMQ消息发送类 */
	@Resource
	private MessageSender messageSender;
	
	/**
	 * MQ接收同步处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int tran_receFailMQLogHandle(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 删除接收成功的MQ日志件数
		int successCount = 0;
		// 重新发送接收失败的MQ消息成功件数
		int receMqCount = 0;
		// 重新发送接收失败的MQ消息失败件数
		int failCount = 0;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		
		try {
			// 删除所有的MQ错误信息处理
			this.delAllMqWarnInfo(map);
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EMQ00037");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
		}
		
		map.put("sendOrRecePOS", CherryBatchConstants.SEND);
		map.put("sendOrReceCherry", CherryBatchConstants.RECE);
		try {
			// 把Cherry的MQ接收日志更新成待处理状态
			binBEMQSYN02_Service.updateMQLogStatusCherry(map);
			// 把POS品牌的MQ发送日志更新成待处理状态
			binBEMQSYN02_Service.updateMQLogStatusPOS(map);
			binBEMQSYN02_Service.manualCommit();
			binBEMQSYN02_Service.witManualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			// 把MQ日志更新成待处理状态时发生错误
			batchLoggerDTO.setCode("EMQ00035");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 查询Cherry的MQ接收日志
			List<Map<String, Object>> cherryMQLogList = binBEMQSYN02_Service.getCherryMQLogList(map);
			// Cherry的MQ接收日志存在的场合
			if(cherryMQLogList != null && !cherryMQLogList.isEmpty()) {
				
				try {
					// 删除POS品牌数据库中新后台已接收成功的MQ日志
					binBEMQSYN02_Service.deleteWitposMQLogS(cherryMQLogList);
					binBEMQSYN02_Service.witManualCommit();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 删除MQ日志信息时发生错误
					batchLoggerDTO.setCode("EMQ00025");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					return flag;
				}
				
				try {
					// 删除cherry数据库中新后台已接收成功的MQ收发日志
					binBEMQSYN02_Service.deleteCherryMQLogR(cherryMQLogList);
					binBEMQSYN02_Service.manualCommit();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 删除MQ日志信息时发生错误
					batchLoggerDTO.setCode("EMQ00026");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					return flag;
				}
				
				successCount += cherryMQLogList.size();
				
				// 日志少于一次抽取的数量，即为最后一页，跳出循环
				if(cherryMQLogList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 判断JMSXGroupID字段是否存在
		boolean hasJMSXGroupID = binBEMQSYN02_Service.getJMSXGroupIDCol(map);
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			List<Map<String, Object>> witposMQLogList = new ArrayList<Map<String,Object>>();
			if(hasJMSXGroupID) {
				// 查询POS品牌需要重新发送的MQ日志List（带有JMSXGroupID的查询）
				witposMQLogList = binBEMQSYN02_Service.getWitposMQLogSendGroupIDList(map);
			} else {
				// 查询POS品牌需要重新发送的MQ日志List
				witposMQLogList = binBEMQSYN02_Service.getWitposMQLogSendList(map);
			}
			// POS品牌需要重新发送的MQ日志存在的场合
			if(witposMQLogList != null && !witposMQLogList.isEmpty()) {
				// POS品牌需要删除的日志
				List<Map<String, Object>> delWitMqLogList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < witposMQLogList.size(); i++) {
					// MQ日志信息
					Map<String, Object> wMQLogMap = witposMQLogList.get(i);
					// 单据号
					String billCode = (String)wMQLogMap.get("billCode");
					// 日志ID
					String witId = wMQLogMap.get("id").toString();
					// MQ原始消息
					String originalMsg = (String)wMQLogMap.get("originalMsg");
					// MQ原始消息存在的场合
					if(originalMsg != null && !"".equals(originalMsg)) {
						try {
							// 调用共通将消息体解析成Map
							Map mainDataMap = null;
							if(originalMsg.startsWith("[Version],")){
								mainDataMap = MessageUtil.message2Map(originalMsg);
								if(mainDataMap == null) {
									Object mainDataDTO = Message2Bean.parseMessage(originalMsg);
									if (mainDataDTO == null) {
										// 最新的纯JSON格式的MQ，会走进此if
										mainDataMap = getMessageMainInfo(originalMsg);
									} else {
										mainDataMap = (Map) Bean2Map.toHashMap(mainDataDTO);
									}
									mainDataMap = CherryUtil.dealMap(mainDataMap);
								}
							}else{
								mainDataMap = getMessageMainInfo(originalMsg);
							}
							// 不是重复数据的场合
							if(binBEMQMES99_BL.judgeIfIsRepeatData(mainDataMap)) {
								String jMSXGroupID = (String)wMQLogMap.get("jMSXGroupID");
								if(jMSXGroupID == null || "".equals(jMSXGroupID)) {
									jMSXGroupID = (String)map.get("brandCode");
								}
								String msgQueName = (String)wMQLogMap.get("msgQueueName");
								if(null==msgQueName||"".equals(msgQueName)){
									msgQueName="posToCherryMsgQueue";
								}
								messageSender.sendGroupMessage(originalMsg, msgQueName, jMSXGroupID);
								// 重新做接收消息件数加一
								receMqCount++;
							} else {
								delWitMqLogList.add(wMQLogMap);
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								// 重复的MQ接收数据被忽略
								batchLoggerDTO.setCode("EMQ00021");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								// 日志ID
								batchLoggerDTO.addParam(witId);
								// 单据号
								batchLoggerDTO.addParam(billCode);
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
								// 失败件数加一
								failCount++;
								flag = CherryBatchConstants.BATCH_WARNING;
								continue;
							}
						} catch (Exception e) {
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							// 重新发送接收失败的MQ消息处理时发生异常
							batchLoggerDTO.setCode("EMQ00010");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							// 日志ID
							batchLoggerDTO.addParam(witId);
							// 单据号
							batchLoggerDTO.addParam(billCode);
							cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
							// 失败件数加一
							failCount++;
							flag = CherryBatchConstants.BATCH_WARNING;
							continue;
						}
					} else {
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						// POS品牌数据库MQ日志表中的MQ原始消息不存在
						batchLoggerDTO.setCode("EMQ00011");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 日志ID
						batchLoggerDTO.addParam(witId);
						// 单据号
						batchLoggerDTO.addParam(billCode);
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						// 失败件数加一
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						continue;
					}
				}
				
				if(delWitMqLogList.size() > 0) {
					try {
						// 批量删除POS品牌数据库MQ日志表
						binBEMQSYN02_Service.deleteWitposMQLogR(delWitMqLogList);
						binBEMQSYN02_Service.witManualCommit();
					} catch (Exception e) {
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						// 删除MQ日志信息时发生错误
						batchLoggerDTO.setCode("EMQ00025");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
						return flag;
					}
				}
				
				try {
					// 把POS品牌接收失败的MQ日志更新成未比对状态
					binBEMQSYN02_Service.updateWitReceiveFlag(witposMQLogList);
					binBEMQSYN02_Service.witManualCommit();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 把POS品牌接收失败的MQ日志更新成未比对状态时发生错误
					batchLoggerDTO.setCode("EMQ00036");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					return flag;
				}
				
				// 日志少于一次抽取的数量，即为最后一页，跳出循环
				if(witposMQLogList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		
		// 删除接收成功的MQ日志件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("EMQ00033");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount));
		// 重新发送接收失败的MQ消息成功件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("EMQ00018");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(receMqCount));
		// 重新发送接收失败的MQ消息失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("EMQ00034");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		
		return flag;
	}
	
	/**
	 * MQ发送同步处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int tran_sendFailMQLogHandle(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 删除接收成功的MQ日志件数
		int successCount = 0;
		// 重新发送接收失败的MQ消息成功件数
		int receMqCount = 0;
		// 重新发送接收失败的MQ消息失败件数
		int failCount = 0;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		
		map.put("sendOrRecePOS", CherryBatchConstants.RECE);
		map.put("sendOrReceCherry", CherryBatchConstants.SEND);
		try {
			// 把POS品牌的MQ接收日志更新成待处理状态
			binBEMQSYN02_Service.updateMQLogStatusPOS(map);
			// 把Cherry的MQ发送日志更新成待处理状态
			binBEMQSYN02_Service.updateMQLogStatusCherry(map);
			binBEMQSYN02_Service.witManualCommit();
			binBEMQSYN02_Service.manualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			// 把MQ日志更新成待处理状态时发生错误
			batchLoggerDTO.setCode("EMQ00035");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
			return flag;
		}
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 查询POS品牌数据库MQ日志List
			List<Map<String, Object>> witMQLogList = binBEMQSYN02_Service.getWitposMQLogList(map);
			// POS品牌数据库的MQ日志存在的场合
			if(witMQLogList != null && !witMQLogList.isEmpty()) {
				try {
					// 删除cherry数据库中老后台已接收成功的MQ收发日志
					binBEMQSYN02_Service.deleteCherryMQLogS(witMQLogList);
					binBEMQSYN02_Service.manualCommit();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 删除MQ日志信息时发生错误
					batchLoggerDTO.setCode("EMQ00026");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					return flag;
				}
				try {
					// 删除POS品牌数据库中老后台已接收成功的MQ日志
					binBEMQSYN02_Service.deleteWitposMQLogR(witMQLogList);
					binBEMQSYN02_Service.witManualCommit();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 删除MQ日志信息时发生错误
					batchLoggerDTO.setCode("EMQ00025");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					return flag;
				}
				successCount += witMQLogList.size();
				// 日志数据少于一次抽取的数量，即为最后一页，跳出循环
				if(witMQLogList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 查询Cherry需要重新发送的MQ日志List
			List<Map<String, Object>> cherryMQLogList = binBEMQSYN02_Service.getCherryMQLogSendList(map);
			// 需要重新发送的MQ日志存在的场合
			if(cherryMQLogList != null && !cherryMQLogList.isEmpty()) {
				for(int i = 0; i < cherryMQLogList.size(); i++) {
					Map<String, Object> cMQLogMap = cherryMQLogList.get(i);
					// 单据号
					String billCode = (String)cMQLogMap.get("billCode");
					// Cherry的日志ID
					String cherryId = cMQLogMap.get("id").toString();
					// 消息体
					String data = (String)cMQLogMap.get("data");
					// 消息体存在的场合
					if(data != null && !"".equals(data)) {
						try {
							// 取得发送队列名
							String msgQueueName = (String)cMQLogMap.get("msgQueueName");
							// 队列名为空的场合，默认设发送到老后台的队列名
							if(msgQueueName == null || "".equals(msgQueueName)) {
								msgQueueName = "cherryToPosMsgQueue";
							}
							// MQ消息发送处理
							messageSender.sendMessage(data, msgQueueName);
							// 成功件数加一
							receMqCount++;
						} catch (Exception e) {
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							// 重新发送接收失败的MQ消息处理时发生异常
							batchLoggerDTO.setCode("EMQ00014");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							// 日志ID
							batchLoggerDTO.addParam(cherryId);
							// 单据号
							batchLoggerDTO.addParam(billCode);
							cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
							// 失败件数加一
							failCount++;
							flag = CherryBatchConstants.BATCH_WARNING;
						}
					} else {
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						// cherry的MQ收发日志表中的消息体不存在。
						batchLoggerDTO.setCode("EMQ00015");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 日志ID
						batchLoggerDTO.addParam(cherryId);
						// 单据号
						batchLoggerDTO.addParam(billCode);
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						// 失败件数加一
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
					}
				}
				
				try {
					// 把Cherry接收失败的MQ日志更新成未比对状态
					binBEMQSYN02_Service.updateCherryReceiveFlag(cherryMQLogList);
					binBEMQSYN02_Service.manualCommit();
				} catch (Exception e) {
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					// 把Cherry接收失败的MQ日志更新成未比对状态时发生错误
					batchLoggerDTO.setCode("EMQ00024");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
					return flag;
				}
				
				// 日志数据少于一次抽取的数量，即为最后一页，跳出循环
				if(cherryMQLogList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
		// 删除接收成功的MQ日志件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("EMQ00033");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount));
		// 重新发送接收失败的MQ消息成功件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("EMQ00018");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(receMqCount));
		// 重新发送接收失败的MQ消息失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("EMQ00034");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);

		return flag;
	}
	
	/**
	 * 处理新后台内部发送失败的MQ消息
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int sendFailMQLogCherry(Map<String, Object> map) throws Exception {
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		
		// 查询条件
		DBObject query = new BasicDBObject();
		query.put("OrgCode", map.get("orgCode"));
		query.put("BrandCode", map.get("brandCode"));
		query.put("SendOrRece", "S");
		query.put("ReceiveFlag", "0");
		
		// 查询新后台内部发送的日志List
		List<DBObject> cherryMQLogByMongoDBList = MongoDB.findAll(CherryConstants.MGO_MQLOG, query);
		if(cherryMQLogByMongoDBList != null && !cherryMQLogByMongoDBList.isEmpty()) {
			for(DBObject cMQLog : cherryMQLogByMongoDBList) {
				// 消息体
				String data = (String)cMQLog.get("Data");
				// 取得发送队列名
				String msgQueueName = (String)cMQLog.get("MsgQueueName");
				// JMS协议头中的JMSGROUPID
				String jmsGroupId = (String)cMQLog.get("JmsGroupId");
				// 单据号
				String billCode = (String)cMQLog.get("BillCode");
				if(data == null || "".equals(data)) {
					failCount++;
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EMQ00028");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 单据号
					batchLoggerDTO.addParam(billCode);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					continue;
				}
				if(msgQueueName == null || "".equals(msgQueueName)) {
					failCount++;
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EMQ00029");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 单据号
					batchLoggerDTO.addParam(billCode);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					continue;
				}
				// MQ消息发送处理
				messageSender.sendGroupMessage(data, msgQueueName, jmsGroupId);
				// 成功件数加一
				successCount++;
			}
		}
		
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("EMQ00030");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount+failCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("EMQ00031");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("EMQ00032");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		
		return flag;
	}
	
	/**
	 * 删除所有的MQ错误信息处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 */
	public void delAllMqWarnInfo(Map<String, Object> map) throws Exception {
		
		DBObject query = new BasicDBObject();
		query.put("BrandCode", map.get("brandCode"));
		BasicDBList values = new BasicDBList();
	    values.add(new BasicDBObject("ErrType", null));
	    values.add(new BasicDBObject("ErrType", "0"));
	    query.put("$or", values);
		MongoDB.removeAll("MGO_MQWarn", query);
	}
	
	public static Map getMessageMainInfo(String msg) throws Exception {
		Map map = CherryUtil.json2Map(msg);
		Map retMap = new HashMap<String, Object>();
		if (map.containsKey("BrandCode")) {
			retMap.put("brandCode", map.get("BrandCode"));
		} else {
			return null;
		}

		if (map.containsKey("TradeType")) {
			retMap.put("tradeType", map.get("TradeType"));
		} else {
			return null;
		}

		if (map.containsKey("TradeNoIF")) {
			retMap.put("tradeNoIF", map.get("TradeNoIF"));
		} else {
			return null;
		}
		
		return retMap;
	}
}

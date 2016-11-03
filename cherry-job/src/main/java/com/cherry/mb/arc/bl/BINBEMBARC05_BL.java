package com.cherry.mb.arc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.arc.service.BINBEMBARC05_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 汇美舍官网奖励积分导入处理BL
 * @author menghao
 *
 */
public class BINBEMBARC05_BL {
	
	@Resource(name="binBEMBARC05_Service")
	private BINBEMBARC05_Service binBEMBARC05_Service;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEMBARC05_BL.class);

	/** 数据查询长度*/
	private final int dataSize = CherryBatchConstants.DATE_SIZE;
	/** 共通BatchLogger*/
	private static BatchLoggerDTO batchLoggerDTO;
	
	/** 数据同步状态:1 未处理 */
	private final String getStatus_0 = "0";
	
	/** 数据同步状态:1 即将处理 */
	private final String getStatus_1 = "1";
	
	/** 数据同步状态:2 成功 */
	private final String getStatus_2 = "2";
	
	/** 数据同步状态:3 失败 */
	private final String getStatus_3 = "3";
	
	// 总条数
	private int totalCount = 0;
	// 失败条数
	private int failCount = 0;

	/**
	 * 奖励积分导入处理
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int pointChangeImport(Map<String, Object> map) throws Exception {
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		
		// 预处理：将接口数据状态为【处理中】（即之前更新状态失败的数据）全部更新为【未处理】
		try{
			Map<String, Object> prepMap = new HashMap<String, Object>();
			prepMap.putAll(map);
			prepMap.put("getStatus_New", getStatus_0);
			prepMap.put("getStatus_Old", getStatus_1);
			binBEMBARC05_Service.updateGetStatus(prepMap);
			try {
				binBEMBARC05_Service.ifManualCommit();
			} catch(Exception e) {
				
			}
		} catch(Exception e) {
			try {
				binBEMBARC05_Service.ifManualRollback();
			} catch(Exception ex) {
				
			}
			// 汇美舍官网奖励积分数据从【即将处理状态】更新为【未处理状态】失败。
			batchLoggerDTO.setCode("EMB00027");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00008", null));
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00007", null));
			logger.BatchLogger(batchLoggerDTO, e);
			return CherryBatchConstants.BATCH_WARNING;
		}
		// 循环分批处理接口表数据
		while(true) {
			// 用于判断是否有未处理的数据
			int upSuccess = 0;
			// Step1:更新接口表的同步状态（getStatus:0->1）
			try {
				Map<String, Object> updateMap = new HashMap<String, Object>();
				updateMap.putAll(map);
				// 分批处理的批处理量
				updateMap.put("upCount", dataSize);
				updateMap.put("getStatus_New", getStatus_1);
				updateMap.put("getStatus_Old", getStatus_0);
				upSuccess = binBEMBARC05_Service.updateGetStatus(updateMap);
				try {
					binBEMBARC05_Service.ifManualCommit();
				} catch(Exception e) {
					
				}
			} catch(Exception e){
				try {
					binBEMBARC05_Service.ifManualRollback();
				} catch(Exception ex) {
					
				}
				flag = CherryBatchConstants.BATCH_WARNING;
				batchLoggerDTO.clear();
				// 汇美舍官网奖励积分数据从【未处理状态】更新为【即将处理状态】失败。
				batchLoggerDTO.setCode("EMB00027");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00007", null));
				batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00008", null));
				logger.BatchLogger(batchLoggerDTO, e);
				
				break;
			}
			// 没有可用于导出的数据时，结束程序
			if(0 == upSuccess) {
				break;
			} 
			
			// 查询接口表中getStatus=1的数据
			List<Map<String, Object>> pointChangeList = binBEMBARC05_Service.getPointChangeInfo(map);
			// Step3:循环每一条接口数据生成MQ消息体发送，成功（getStatus置为2，失败置为3）
			if(null != pointChangeList && pointChangeList.size() > 0) {
				// 统计总条数
				totalCount += pointChangeList.size();
				for(Map<String, Object> pointChangeMap : pointChangeList) {
					pointChangeMap.putAll(map);
					try {
						// 将该条积分奖励数据组装成MQ消息体并发送
						this.sendPointChangeMQMeg(pointChangeMap);
						// 发送MQ成功，此条数据的状态置为2
						pointChangeMap.put("getStatus", getStatus_2);
					} catch(Exception e) {
						// 发送MQ失败【不会跳出循环，继续下面的处理】，此条数据的状态置为3且将异常信息更新到getError
						pointChangeMap.put("getStatus", getStatus_3);
						pointChangeMap.put("getError", (null != e.getMessage()) ? e.getMessage() : "null");
						failCount++;
						
						flag = CherryBatchConstants.BATCH_WARNING;
						batchLoggerDTO.clear();
						batchLoggerDTO.setCode("EMB00028");
						batchLoggerDTO.addParam(pointChangeMap.get("MemberCode")+"");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						logger.BatchLogger(batchLoggerDTO, e);
					}
				}
				try {
					// 批量更新发送MQ处理后的接口数据getStatus状态【成功的置为2，失败的置为3】及Gettime
					binBEMBARC05_Service.updateGetStatusEnd(pointChangeList);
					try {
						binBEMBARC05_Service.ifManualCommit();
					} catch(Exception e) {
						
					}
				} catch(Exception e) {
					try {
						binBEMBARC05_Service.ifManualRollback();
					} catch(Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;
					batchLoggerDTO.clear();
					// 汇美舍官网奖励积分处理后批量更新同步状态失败！
					batchLoggerDTO.setCode("EMB00029");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					// 失败时结束批处理
					// 程序出现异常后，后面的批处理依然可能会遇到这样的问题。 
					break;
				}
				// 奖励积分数据少于一次抽取的数量，跳出循环
				if(pointChangeList.size() < dataSize){
					break;
				}
			} else {
				break;
			}
			
		}
		// 输出处理结果信息
		outMessage();
		return flag;
	}
	
	/**
	 * 将积分奖励数据转换成指定MQ消息体进行发送
	 * 
	 * @param map
	 * @throws Exception 
	 */
	private void sendPointChangeMQMeg(Map<String, Object> map) throws Exception {
		//积分维护明细数据
		List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
		Map<String,Object> detailMap = new HashMap<String,Object>();
		//会员卡号
		detailMap.put("MemberCode", map.get("MemberCode"));
		//修改的积分
		detailMap.put("ModifyPoint", map.get("ModifyPoint"));
		//业务时间
		detailMap.put("BusinessTime", map.get("BusinessTime"));
		//备注
		detailMap.put("Reason", map.get("Reason"));
		detailDataList.add(detailMap);
		//设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(ConvertUtil.getString(map.get("brandCode")));
		// 组织代码
		mqInfoDTO.setOrgCode(ConvertUtil.getString(map.get("orgCode")));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))));
		//单据类型
		String billType = CherryConstants.MESSAGE_TYPE_PT;
		// 单据号
		String billCode = binOLCM03_BL
				.getTicketNumber(Integer.parseInt(ConvertUtil.getString(map
						.get("organizationInfoId"))),
						Integer.parseInt(ConvertUtil.getString(map
								.get("brandInfoId"))), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
		// 设定消息内容
		Map<String, Object> msgDataMap = new HashMap<String, Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String, Object> dataLine = new HashMap<String, Object>();
		// 消息的主数据行
		Map<String, Object> mainData = new HashMap<String, Object>();
		// 品牌代码【用当前用户所属品牌】
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		//修改模式(修改积分差值)
		mainData.put("SubTradeType", "2");
		// 积分类型【表示奖励积分】
		mainData.put("MaintainType", "1");
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 数据来源[固定值 "WEB"]
		mainData.put("Sourse", "WEB");
		// 积分主数据
		dataLine.put("MainData", mainData);
		// 积分明细
		dataLine.put("DetailDataDTOList", detailDataList);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 数据来源
		dbObject.put("Sourse", "WEB");
		mqInfoDTO.setDbObject(dbObject);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 输出处理结果信息
	 * 
	 * @throws CherryBatchException
	 */
	private void outMessage() throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO3);
	}

}

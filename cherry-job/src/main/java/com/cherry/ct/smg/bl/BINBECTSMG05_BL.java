package com.cherry.ct.smg.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_10_BL;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_6_BL;
import com.cherry.cp.common.bl.BINOLCPCOMCOUPON_4_BL;
import com.cherry.cp.common.interfaces.BINOLCPCOMCOUPON_IF;
import com.cherry.ct.common.BINBECTCOM01;
import com.cherry.ct.smg.interfaces.BINBECTSMG06_IF;
import com.cherry.ct.smg.service.BINBECTSMG01_Service;
import com.cherry.ct.smg.service.BINBECTSMG03_Service;
import com.cherry.ct.smg.service.BINBECTSMG05_Service;
import com.cherry.ct.smg.service.BINBECTSMG08_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BINBECTSMG05_BL implements CherryMessageHandler_IF{
	
	@Resource(name = "binBECTSMG01_Service")
	private BINBECTSMG01_Service binBECTSMG01_Service;
	
	@Resource(name = "binBECTSMG03_Service")
	private BINBECTSMG03_Service binBECTSMG03_Service;
	
	@Resource(name = "binBECTSMG05_Service")
	private BINBECTSMG05_Service binBECTSMG05_Service;
	
	@Resource(name = "binBECTSMG08_Service")
	private BINBECTSMG08_Service binBECTSMG08_Service;
	
	@Resource(name = "binBECTSMG06_BL")
	private BINBECTSMG06_IF binBECTSMG06_IF;
	
	@Resource(name = "binBECTSMG03_BL")
	private BINBECTSMG03_BL binBECTSMG03_BL;
	
	@Resource(name = "binBECTSMG04_BL")
	private BINBECTSMG04_BL binBECTSMG04_BL;
	
	@Resource(name = "binBECTCOM01")
	private BINBECTCOM01 binBECTCOM01;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLCM33_BL")
	private BINOLCM33_BL binOLCM33_BL;
	
	@Resource(name = "binolcpcomcouponIF")
	private BINOLCPCOMCOUPON_IF cpnSer;
	
	@Resource(name = "binolcpcomcoupon4bl")
	private BINOLCPCOMCOUPON_4_BL cpn4Ser;
	
	@Resource(name = "binolcpcomcoupon6bl")
	private BINOLCPCOMCOUPON_6_BL cpn6Ser;
	
	@Resource(name = "binolcpcomcoupon10bl")
	private BINOLCPCOMCOUPON_10_BL cpn10Ser;
	
	@Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;

	private Logger logger = LoggerFactory.getLogger(BINBECTSMG05_BL.class.getClass());

	/**
	 * 接收MQ消息处理
	 * 
	 * @param map MQ消息
	 * @throws Exception 
	 */
	@Override
    public void handleMessage(Map<String, Object> map) throws Exception {
		Transaction transaction = Cat.newTransaction("message", "BINBECTSMG05_BL");
		int result = CherryBatchConstants.BATCH_SUCCESS;

		try{
			if(checkEventParam(map)){
				// 获取参数
				String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoID"));
				String brandInfoId = ConvertUtil.getString(map.get("brandInfoID"));
				String orgCode = ConvertUtil.getString(map.get("orgCode"));
				String brandCode = ConvertUtil.getString(map.get("brandCode"));
				String eventType = ConvertUtil.getString(map.get("eventType"));
				String eventId = ConvertUtil.getString(map.get("eventId"));
				String eventDate = ConvertUtil.getString(map.get("eventDate"));
				String couponCode = ConvertUtil.getString(map.get("couponCode"));
				String couponExpireTime = ConvertUtil.getString(map.get("couponExpireTime"));
				String messageContents = ConvertUtil.getString(map.get("messageContents"));
				String campaignCode = ConvertUtil.getString(map.get("campaignCode"));
				String sourse = ConvertUtil.getString(map.get("sourse"));
				String smsChannel=ConvertUtil.getString(map.get("smsChannel"));
				eventId = eventId.replace("&", "#");

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("organizationInfoId", organizationInfoId);
				paramMap.put("brandInfoId", brandInfoId);
				paramMap.put("orgCode", orgCode);
				paramMap.put("brandCode", brandCode);
				paramMap.put("eventId", eventId);
				paramMap.put("eventDate", eventDate);
				paramMap.put("eventType", eventType);
				paramMap.put("couponCode", couponCode);
				paramMap.put("couponExpireTime", couponExpireTime);
				paramMap.put("messageContents", messageContents);
				paramMap.put("campaignCode", campaignCode);
				paramMap.put("dataSourse", sourse);
				paramMap.put("smsChannel", smsChannel);
				// 判断事件类型是否为积分变化类型，若是则进行类型分析
				eventType = getNewEventType(paramMap);
				// 加入事件类型
				paramMap.put("eventType", eventType);
				// 检查当前时间是否在允许的发送时间范围内，若在则直接发送信息，不在则记录MQ的信息
				if(checkSendTime(paramMap)){
					// 在信息允许发送时间范围内直接发送信息
					// 打印事件触发日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00040");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					batchLoggerDTO.addParam(brandCode);
					batchLoggerDTO.addParam(sourse);
					batchLoggerDTO.addParam(eventDate);
					batchLoggerDTO.addParam(eventType);
					batchLoggerDTO.addParam(eventId);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);

					result = this.eventSchedule(paramMap);
				}else{
					// 不在信息发送允许时间范围内将事件MQ先保存起来
					try{
						if(eventId.length() < 50){
							// 打印事件触发日志
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("ECT00060");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
							batchLoggerDTO.addParam(brandCode);
							batchLoggerDTO.addParam(sourse);
							batchLoggerDTO.addParam(eventDate);
							batchLoggerDTO.addParam(eventType);
							batchLoggerDTO.addParam(eventId);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);

							paramMap.put("runStatus", "1");
							paramMap.put("createBy", "SMGBATCH");
							paramMap.put("createPGM", "BINBECTSMG05");
							paramMap.put("updateBy", "SMGBATCH");
							paramMap.put("updatePGM", "BINBECTSMG05");
							binBECTSMG05_Service.addDelayEventInfo(paramMap);
							result = CherryBatchConstants.BATCH_SUCCESS;
						}else{
							// 打印事件触发日志
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("ECT00077");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
							batchLoggerDTO.addParam(sourse);
							batchLoggerDTO.addParam(eventDate);
							batchLoggerDTO.addParam(eventType);
							batchLoggerDTO.addParam(eventId);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							result = CherryBatchConstants.BATCH_ERROR;
						}
					}catch(Exception ex){
						// 记录错误日志
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECT00061");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(eventType);
						batchLoggerDTO.addParam(eventId);
						batchLoggerDTO.addParam(ConvertUtil.getString(ex));
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						result = CherryBatchConstants.BATCH_ERROR;
					}
				}

				if(result != CherryBatchConstants.BATCH_SUCCESS) {
					// 记录错误日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00041");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(eventType);
					batchLoggerDTO.addParam(eventId);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}else{
					DBObject dbObject = new BasicDBObject();
					// 组织代号
					dbObject.put("OrgCode", orgCode);
					// 品牌代码
					dbObject.put("BrandCode", brandCode);
					// 业务类型
					dbObject.put("TradeType", map.get("tradeType"));
					// 单据号
					dbObject.put("TradeNoIF", map.get("tradeNoIF"));
					// 修改回数
					dbObject.put("ModifyCounts", map.get("modifyCounts"));
					// 事件类型
					dbObject.put("EventType", eventType);
					// 事件ID
					dbObject.put("EventId", eventId);
					// 沟通时间
					dbObject.put("EventDate", eventDate);
					// 验证号
					dbObject.put("CouponCode", couponCode);
					// 验证号过期时间
					dbObject.put("CouponExpireTime", couponExpireTime);
					// 沟通内容
					dbObject.put("MessageContents", messageContents);
					// 活动编号
					dbObject.put("CampaignCode", campaignCode);
					// 数据来源
					dbObject.put("Sourse", sourse);
					map.put("dbObject", dbObject);
				}
			}else{
				// 打印事件触发日志
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00078");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("organizationInfoID")));
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("brandInfoID")));
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("brandCode")));
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("eventType")));
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("eventId")));
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("eventDate")));
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("sourse")));
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
			}
			transaction.setStatus(Transaction.SUCCESS);
		}catch (Exception t){
			transaction.setStatus(t);
			Cat.logError(t);
			throw t;
		}
		finally {
			transaction.complete();
		}
	}
	
	// 运行延时事件
	public String runDelayEvent(Map<String, Object> map) throws CherryBatchException{
		try{
			// 获取延时事件
			String eventType = ConvertUtil.getString(map.get("taskCode"));
			// 获取系统日期
			String sysDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
			String delayEventBeginDate = DateUtil.addDateByDays(CherryBatchConstants.DF_DATE_PATTERN, sysDate, -3);
			if(!"".equals(eventType)){
				Map<String, Object> eventTypeMap = new HashMap<String, Object>();
				eventTypeMap.put("organizationInfoId", ConvertUtil.getString(map.get("organizationInfoId")));
				eventTypeMap.put("brandInfoId", ConvertUtil.getString(map.get("brandInfoId")));
				eventTypeMap.put("eventType", eventType);
				eventTypeMap.put("eventDate", delayEventBeginDate);
				try{
			    	if(checkSendTime(eventTypeMap)){
			    		// 打印日志
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECT00064");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
						batchLoggerDTO.addParam(eventType);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
			    		// 获取延时事件列表
			    		List<Map<String, Object>> delayEventList = binBECTSMG05_Service.getDelayEventInfoList(eventTypeMap);
			    		if(delayEventList != null && !delayEventList.isEmpty()) {
				    		for(Map<String,Object> delayEventMap : delayEventList){
				    			String eventInfoId = ConvertUtil.getString(delayEventMap.get("eventInfoId"));
				    			String eventId = ConvertUtil.getString(delayEventMap.get("eventId"));
				    			String eventDate = ConvertUtil.getString(delayEventMap.get("eventDate"));
				    			String dataSourse = ConvertUtil.getString(delayEventMap.get("dataSourse"));
				    			String runBeginTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN); 
				    			Map<String, Object> paramMap = new HashMap<String, Object>();
				    			paramMap.put("eventInfoId", eventInfoId);
				    			paramMap.put("runBeginTime", runBeginTime);
				    			paramMap.put("updateBy", "SMGBATCH");
				    			paramMap.put("updatePGM", "BINBECTSMG05");
				    			try{
				    				// 更新运行状态（正在运行）
				    				paramMap.put("runStatus", "2");
				    				paramMap.put("runEndTime", "");
				    				binBECTSMG05_Service.updateDelayEventRunInfo(paramMap);
				    				// 打印日志
									BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
									batchLoggerDTO1.setCode("ECT00068");
									batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
									batchLoggerDTO1.addParam(dataSourse);
									batchLoggerDTO1.addParam(eventDate);
									batchLoggerDTO1.addParam(eventType);
									batchLoggerDTO1.addParam(eventId);
									CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
									cherryBatchLogger1.BatchLogger(batchLoggerDTO1);
									
					    			int result = this.eventSchedule(delayEventMap);
					    			if(result == CherryBatchConstants.BATCH_SUCCESS) {
					    				// 更新运行状态（运行成功）
					    				paramMap.put("runStatus", CherryBatchConstants.SCHEDULES_RUN_SUCCESS);
					    				paramMap.put("runEndTime", CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN));
					    				binBECTSMG05_Service.updateDelayEventRunInfo(paramMap);	
					    				// 打印日志
										BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
										batchLoggerDTO2.setCode("ECT00070");
										batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
										batchLoggerDTO2.addParam(dataSourse);
										batchLoggerDTO2.addParam(eventDate);
										batchLoggerDTO2.addParam(eventType);
										batchLoggerDTO2.addParam(eventId);
										CherryBatchLogger cherryBatchLogger2 = new CherryBatchLogger(this.getClass());
										cherryBatchLogger2.BatchLogger(batchLoggerDTO2);
					    			}else if(result == CherryBatchConstants.BATCH_WARNING){
					    				// 更新运行状态（运行警告）
					    				paramMap.put("runStatus", CherryBatchConstants.SCHEDULES_RUN_WARNING);
					    				paramMap.put("runEndTime", CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN));
					    				binBECTSMG05_Service.updateDelayEventRunInfo(paramMap);	
					    				// 打印日志
										BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
										batchLoggerDTO2.setCode("ECT00071");
										batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
										batchLoggerDTO2.addParam(dataSourse);
										batchLoggerDTO2.addParam(eventDate);
										batchLoggerDTO2.addParam(eventType);
										batchLoggerDTO2.addParam(eventId);
										CherryBatchLogger cherryBatchLogger2 = new CherryBatchLogger(this.getClass());
										cherryBatchLogger2.BatchLogger(batchLoggerDTO2);
					    			}else{
					    				// 更新运行状态（运行失败）
					    				paramMap.put("runStatus", CherryBatchConstants.SCHEDULES_RUN_ERROR);
					    				paramMap.put("runEndTime", CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN));
					    				binBECTSMG05_Service.updateDelayEventRunInfo(paramMap);	
					    				// 打印日志
										BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
										batchLoggerDTO2.setCode("ECT00072");
										batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
										batchLoggerDTO2.addParam(dataSourse);
										batchLoggerDTO2.addParam(eventDate);
										batchLoggerDTO2.addParam(eventType);
										batchLoggerDTO2.addParam(eventId);
										CherryBatchLogger cherryBatchLogger2 = new CherryBatchLogger(this.getClass());
										cherryBatchLogger2.BatchLogger(batchLoggerDTO2);
					    			}
				    			}catch(Exception e){
				    				// 更新运行状态（运行失败）
				    				paramMap.put("runStatus", CherryBatchConstants.SCHEDULES_RUN_ERROR);
				    				paramMap.put("runEndTime", CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN));
				    				binBECTSMG05_Service.updateDelayEventRunInfo(paramMap);	
				    				// 打印日志
									BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
									batchLoggerDTO1.setCode("ECT00069");
									batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO1.addParam(dataSourse);
									batchLoggerDTO1.addParam(eventDate);
									batchLoggerDTO1.addParam(eventType);
									batchLoggerDTO1.addParam(eventId);
									batchLoggerDTO1.addParam(ConvertUtil.getString(e));
									CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
									cherryBatchLogger1.BatchLogger(batchLoggerDTO1);
				    			}
				    		}
			    		}else{
			    			// 延时事件为空的情况
			    			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("ECT00066");
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
							batchLoggerDTO1.addParam(eventType);
							CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
							cherryBatchLogger1.BatchLogger(batchLoggerDTO1);
			    		}
			    	}else{
			    		// 未到执行时间
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECT00065");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
						batchLoggerDTO.addParam(eventType);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
			    	}
				}catch(Exception ep){
					// 打印日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00063");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(eventType);
					batchLoggerDTO.addParam(ConvertUtil.getString(ep));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
				return CherryBatchConstants.RUNFLAG_SUCCESS;
			}else{
				// 打印日志
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00067");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				// 事件类型不存在的情况
				return CherryBatchConstants.RUNFLAG_NOEVENTTYPE;
			}
		}catch(Exception ex){
			// 打印日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00062");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(ex));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// 系统错误
			return CherryBatchConstants.RUNFLAG_SYSERROR;
		}
	}
	
	// WebService调用沟通事件处理程序发送短信
	public Map<String, Object> wsSendMessage(Map<String, Object> map) throws Exception{
		Transaction transaction = Cat.newTransaction("message","wsSendMessage");
		try{
			String eventType = ConvertUtil.getString(map.get("eventType"));
			String eventId = ConvertUtil.getString(map.get("eventId"));
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			String eventDate = ConvertUtil.getString(map.get("eventDate"));
			String sourse = ConvertUtil.getString(map.get("sourse"));
			// 判断事件类型是否为积分变化事件，若为积分变化事件则获取新的事件类型
			eventType = getNewEventType(map);
			// 加入事件类型
			map.put("eventType", eventType);
			// 检查当前时间是否在允许的发送时间范围内，若在则直接发送信息，不在则记录MQ的信息
			if(checkSendTime(map)){
				// 在信息允许发送时间范围内直接发送信息
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00040");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam(brandCode);
				batchLoggerDTO.addParam(sourse);
				batchLoggerDTO.addParam(eventDate);
				batchLoggerDTO.addParam(eventType);
				batchLoggerDTO.addParam(eventId);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				// 执行事件调度
				Map<String, Object> resultMap = this.wsEventSchedule(map);
				transaction.setStatus(Transaction.SUCCESS);
				return resultMap;
			}else{
				// 不在信息发送允许时间范围内
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00090");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(sourse);
				batchLoggerDTO.addParam(eventDate);
				batchLoggerDTO.addParam(eventType);
				batchLoggerDTO.addParam(eventId);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				// 返回结果
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("sendFlag", "99");
				transaction.setStatus(Transaction.SUCCESS);
				return resultMap;
			}

		}catch (Exception t){
			transaction.setStatus(t);
			Cat.logError(t);
			throw t;
		}
		finally {
			transaction.complete();
		}
	}
	
	// WebService调用沟通事件处理程序拨打电话告知活动验证码
	public Map<String, Object> wsPhoneCall(Map<String, Object> map) throws Exception{
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String eventDate = ConvertUtil.getString(map.get("eventDate"));
		String sourse = ConvertUtil.getString(map.get("sourse"));
		// 判断事件类型是否为积分变化事件，若为积分变化事件则获取新的事件类型
		eventType = getNewEventType(map);
    	// 加入事件类型
    	map.put("eventType", eventType);
		// 检查当前时间是否在允许的发送时间范围内，若在则直接发送信息，不在则记录MQ的信息
		if(checkSendTime(map)){
			// 在信息允许发送时间范围内直接发送信息
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00040");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(brandCode);
			batchLoggerDTO.addParam(sourse);
			batchLoggerDTO.addParam(eventDate);
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(eventId);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// 执行事件调度
			Map<String, Object> resultMap = this.wsEventSchedule(map);
			return resultMap;
		}else{
			// 不在信息发送允许时间范围内
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00090");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(sourse);
			batchLoggerDTO.addParam(eventDate);
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(eventId);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// 返回结果
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("sendFlag", "99");
			return resultMap;
		}
	}
		
	public Map<String, Object> wsEventSchedule(Map<String, Object> map) throws CherryBatchException{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		String runCode = DateUtil.getCurrTime();
		String errorText = "";
		try{
			map.put("runCode", runCode);
			Map<String, Object> runResultMap = choiceCommType(map);
			if(runResultMap != null && !runResultMap.isEmpty()){
				String runStatus = ConvertUtil.getString(runResultMap.get("runStatus"));
				if(runStatus.equals(CherryBatchConstants.EXECFLAG_SUCCESS)){
					// 准备日志信息
					errorText = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
					batchLoggerDTO.setCode("ECT00043");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					// 返回运行状态
					returnMap.put("sendFlag", CherryBatchConstants.BATCH_SUCCESS);
					returnMap.putAll(runResultMap);
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_ERROR)){
					// 准备日志信息
					errorText = PropertiesUtil.getMessage("ECT00048", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 返回运行状态
					returnMap.put("sendFlag", CherryBatchConstants.BATCH_ERROR);
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_WARNING)){
					// 准备日志信息
					errorText = PropertiesUtil.getMessage("ECT00048", null);
					batchLoggerDTO.setCode("ECT00044");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					// 返回运行状态
					returnMap.put("sendFlag", CherryBatchConstants.BATCH_WARNING);
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_EVENTSETNULL)){
					// 准备日志信息
					errorText = PropertiesUtil.getMessage("ECT00047", null);
					batchLoggerDTO.setCode("ECT00044");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					// 返回运行状态
					returnMap.put("sendFlag", CherryBatchConstants.EXECFLAG_EVENTSETNULL);
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_CUSTOMERNULL)){
					errorText = PropertiesUtil.getMessage("ECT00014", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					// 返回运行状态
					returnMap.put("sendFlag", CherryBatchConstants.EXECFLAG_CUSTOMERNULL);
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_ALLMSGSENDFAILED)){
					errorText = PropertiesUtil.getMessage("ECT00092", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 返回运行状态
					returnMap.put("sendFlag", CherryBatchConstants.EXECFLAG_ALLMSGSENDFAILED);
				}else{
					// 准备日志信息
					errorText = PropertiesUtil.getMessage("ECT00029", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					// 返回运行状态
					returnMap.put("sendFlag", CherryBatchConstants.BATCH_ERROR);
				}
			}else{
				// 准备日志信息
				errorText = PropertiesUtil.getMessage("ECT00029", null);
				batchLoggerDTO.setCode("ECT00042");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				// 返回运行状态
				returnMap.put("sendFlag", CherryBatchConstants.BATCH_ERROR);
			}
		}catch(Exception ex){
			// 准备日志信息
			errorText = ConvertUtil.getString(ex);
			batchLoggerDTO.setCode("ECT00042");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			// 返回运行状态
			returnMap.put("sendFlag", CherryBatchConstants.BATCH_ERROR);
		}finally{
			// 记录日志
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(eventId);
			batchLoggerDTO.addParam(errorText);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		return returnMap;
	}
	
	public String getNewEventType(Map<String, Object> map) throws Exception{
		String eventType = ConvertUtil.getString(map.get("eventType"));
		// 判断事件类型是否为积分变化类型，若是则进行类型分析
    	if(eventType.equals(CherryBatchConstants.POINTCHANGEEVENTID)){
    		int point = 0;
			// 事件类型为积分变化类型时，获取导致积分变化的业务类型
			List<Map<String, Object>> tradeTypeList = binBECTSMG05_Service.getPointChangeTypeList(map);
			for(Map<String,Object> tradeTypeMap : tradeTypeList){
				String tradeType = ConvertUtil.getString(tradeTypeMap.get("tradeType"));
				// 根据业务类型获取系统定义的触发事件类型
				eventType = getPointChangeEventType(tradeType);
				if(!eventType.equals(CherryBatchConstants.POINTCHANGEEVENTID)){
					break;
				}else{
					point = ConvertUtil.getInt(tradeTypeMap.get("point"));
				}
			}
			// 如果根据业务类型获取不到触发事件类型则根据积分变化值将触发事件类型定义成积分增加或积分减少类型
			if(eventType.equals(CherryBatchConstants.POINTCHANGEEVENTID)){
				if(point < 0){
					eventType = CherryBatchConstants.POINTEVENTTYPE_JS;
				}else{
					eventType = CherryBatchConstants.POINTEVENTTYPE_ZJ;
				}
			}
    	}
		return eventType;
	}
	
	/**
	 * 通过手机号检查会员表中是否存在
	 * 
	 * @param map
	 * @return
	 */
	public boolean checkMemExistByMobilePhone(Map<String,Object> map){
		Map<String, Object> memNumMap = null;
		memNumMap = binBECTSMG05_Service.checkMemExistByMobilePhone(map);
		if(memNumMap != null && memNumMap.get("num") != null ){
			int memNum = (Integer) memNumMap.get("num");
			if(memNum != 0){
				return true;
			}
		}
		return false;
	}
	
	// 检查MQ必填参数是否为空
	public boolean checkEventParam(Map<String, Object> map){
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoID"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoID"));
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		String eventDate = ConvertUtil.getString(map.get("eventDate"));
		String sourse = ConvertUtil.getString(map.get("sourse"));
		if("".equals(organizationInfoId)){
			return false;
		}else if("".equals(brandInfoId)){
			return false;
		}else if("".equals(brandCode)){
			return false;
		}else if("".equals(eventType)){
			return false;
		}else if("".equals(eventId)){
			return false;
		}else if("".equals(eventDate)){
			return false;
		}else if("".equals(sourse)){
			return false;
		}else{
			return true;	
		}
	}
	
	// 检查当前时间是否在允许的发送时间范围内
	public boolean checkSendTime(Map<String, Object> map){
		boolean timeFlag = true;
		String nowDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
		String nowTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
		String eventType = ConvertUtil.getString(map.get("eventType"));
		Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("organizationInfoId", ConvertUtil.getString(map.get("organizationInfoId")));
    	paramMap.put("brandInfoId", ConvertUtil.getString(map.get("brandInfoId")));
    	paramMap.put("eventType", eventType);		
    	List<Map<String, Object>> delaySetList = binBECTSMG05_Service.getDelaySetList(paramMap);
    	if(delaySetList != null && !delaySetList.isEmpty()) {
			for(Map<String,Object> delaySetMap : delaySetList){
				//frequency 频率【0：不限    1：每天    2：每周    3：每月（Code值表1260）】
				String frequency = ConvertUtil.getString(delaySetMap.get("frequency"));
				String dateBegin = ConvertUtil.getString(delaySetMap.get("dateBegin"));
				String dateEnd = ConvertUtil.getString(delaySetMap.get("dateEnd"));
				String timeBegin = ConvertUtil.getString(delaySetMap.get("timeBegin"));
				String timeEnd = ConvertUtil.getString(delaySetMap.get("timeEnd"));
				if("2".equals(frequency)){
					// 开始日为空的情况将开始日默认置为1
					if("".equals(dateBegin)){
						dateBegin = "1";
					}
					// 截止日为空的情况将截止日默认置为7
					if("".equals(dateEnd)){
						dateEnd = "7";
					}
				}
				if("3".equals(frequency)){
					// 开始日为空的情况将开始日默认置为1
					if("".equals(dateBegin)){
						dateBegin = "1";
					}
					// 截止日为空的情况将截止日默认置为当月最后一天
					if("".equals(dateEnd)){
						String nowMonth = binBECTCOM01.getNowYearAndMonth("MM");
						dateEnd = DateUtil.getLastDateByMonth(nowMonth);
					}
				}
				if("1".equals(frequency) || "2".equals(frequency) || "3".equals(frequency)){
					// 开始时间为空的情况将开始时间默认置为00:00:00
					if("".equals(timeBegin)){
						timeBegin = "00:00:00";
					}
					// 截止时间为空的情况将截止时间默认置为23:59:59
					if("".equals(timeEnd)){
						timeEnd = "23:59:59";
					}
				}
				// 根据不同频率验证当前时间是否短信发送时间
				if("0".equals(frequency)){
					// 不限时间的情况
					timeFlag = true;
				}else if("1".equals(frequency)){
					// 频率为每天的情况
					String beginTime = nowDate+" "+timeBegin;
					String endTime = nowDate+" "+timeEnd;
					if(binBECTCOM01.dateBefore(beginTime,nowTime,CherryBatchConstants.DF_TIME_PATTERN)){
						if(binBECTCOM01.dateBefore(nowTime,endTime,CherryBatchConstants.DF_TIME_PATTERN)){
							timeFlag = true;
						}else{
							timeFlag = false;
							break;
						}
					}else{
						timeFlag = false;
						break;
					}
				}else{
					// 频率为空或者其它情况
					timeFlag = true;
				}
			}
    	}else{
    		// 没有取到事件延时设置的情况
    		timeFlag = true;
    	}
		return timeFlag;
	}
	
	public String getPointChangeEventType(String tradeType){
		String eventType = CherryBatchConstants.POINTCHANGEEVENTID;
		if("NS".equals(tradeType)){
			eventType = CherryBatchConstants.POINTEVENTTYPE_NS;
		}else if("SR".equals(tradeType)){
			eventType = CherryBatchConstants.POINTEVENTTYPE_SR;
		}else if("PX".equals(tradeType)){
			eventType = CherryBatchConstants.POINTEVENTTYPE_PX;
		}else if("PB".equals(tradeType)){
			eventType = CherryBatchConstants.POINTEVENTTYPE_PB;
		}else if("PC".equals(tradeType)){
			eventType = CherryBatchConstants.POINTEVENTTYPE_PC;
		}else if("PT".equals(tradeType)){
			eventType = CherryBatchConstants.POINTEVENTTYPE_PT;
		}
		return eventType;
	}
	
	public int eventSchedule(Map<String, Object> map) throws CherryBatchException{
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		String runCode = DateUtil.getCurrTime();
		String errorText = "";
		try{
			map.put("runCode", runCode);
			Map<String, Object> runResultMap = choiceCommType(map);
			if(runResultMap != null && !runResultMap.isEmpty()){
				String runStatus = ConvertUtil.getString(runResultMap.get("runStatus"));
				if(runStatus.equals(CherryBatchConstants.EXECFLAG_SUCCESS)){
					errorText = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
					batchLoggerDTO.setCode("ECT00043");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
					flag = CherryBatchConstants.BATCH_SUCCESS;
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_ERROR)){
					errorText = PropertiesUtil.getMessage("ECT00048", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					flag = CherryBatchConstants.BATCH_ERROR;
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_WARNING)){
					errorText = PropertiesUtil.getMessage("ECT00048", null);
					batchLoggerDTO.setCode("ECT00044");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					flag = CherryBatchConstants.BATCH_WARNING;
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_EVENTSETNULL)){
					errorText = PropertiesUtil.getMessage("ECT00047", null);
					batchLoggerDTO.setCode("ECT00044");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					flag = CherryBatchConstants.BATCH_WARNING;
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_CUSTOMERNULL)){
					errorText = PropertiesUtil.getMessage("ECT00014", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
					flag = CherryBatchConstants.BATCH_ERROR;
				}else if(runStatus.equals(CherryBatchConstants.EXECFLAG_ALLMSGSENDFAILED)){
					errorText = PropertiesUtil.getMessage("ECT00092", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					flag = CherryBatchConstants.BATCH_ERROR;
				}else{
					errorText = PropertiesUtil.getMessage("ECT00029", null);
					batchLoggerDTO.setCode("ECT00042");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					flag = CherryBatchConstants.BATCH_ERROR;
				}
			}else{
				errorText = PropertiesUtil.getMessage("ECT00029", null);
				batchLoggerDTO.setCode("ECT00042");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				flag = CherryBatchConstants.BATCH_ERROR;
			}
		}catch(Exception ex){
			errorText = ConvertUtil.getString(ex);
			batchLoggerDTO.setCode("ECT00042");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			flag = CherryBatchConstants.BATCH_ERROR;
		}finally{
			// 记录日志
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(eventId);
			batchLoggerDTO.addParam(errorText);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		return flag;
	}
	
	public Map<String, Object> choiceCommType(Map<String, Object> map) throws Exception{
		boolean onlySendMsg = true;
		String couponCode = "";
		String expiredTime = "";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//发送验证码短信时是否启用电话外呼告知验证码功能【1为启用，0为不启用】,一般在‘重新获取验证码’时使用
		String phoneCallFlag = binOLCM14_BL.getConfigValue("1316", ConvertUtil.getString(map.get("organizationInfoId")), ConvertUtil.getString(map.get("brandInfoId")));
		if("1".equals(phoneCallFlag)){
			String eventType = ConvertUtil.getString(map.get("eventType"));
			if("9".equals(eventType)){
				onlySendMsg = false;
			}else if("14".equals(eventType)){
				// 查询是否已发送过验证码
				map.put("phoneNum", map.get("eventId"));
				Map<String, Object> couponMap = binBECTSMG08_Service.getCouponByPhone(map);
				if(couponMap != null && !couponMap.isEmpty()){
					couponCode = ConvertUtil.getString(couponMap.get("couponCode"));
					expiredTime = ConvertUtil.getString(couponMap.get("expiredTime"));
					if(!"".equals(couponCode)){
						onlySendMsg = false;
					}
				}
			}
		}
		if(onlySendMsg){
			// 通过短信发送验证码
			returnMap = execEvent(map);
		}else{
			// 先发短信验证码
			returnMap = execEvent(map);
			// 处理返回值
			if(returnMap != null && !returnMap.isEmpty()){
				String smsCouponCode = ConvertUtil.getString(returnMap.get("couponCode"));
				String couponExpiredTime = ConvertUtil.getString(returnMap.get("couponExpiredTime"));
				if(!"".equals(smsCouponCode)){
					couponCode = smsCouponCode;
				}
				if(!"".equals(couponExpiredTime)){
					expiredTime = couponExpiredTime;
				}
				if(!"".equals(couponCode)){
					// 返回验证码
					returnMap.put("couponCode", couponCode);
					returnMap.put("couponExpiredTime", expiredTime);
					returnMap.put("runStatus", CherryBatchConstants.EXECFLAG_SUCCESS);
				}else{
					// 验证码为空的情况下记录日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00096");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(ConvertUtil.getString(map.get("eventId")));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}else{
				if(!"".equals(couponCode)){
					// 返回查询到的历史验证码和过期时间
					returnMap.put("couponCode", couponCode);
					returnMap.put("couponExpiredTime", expiredTime);
					returnMap.put("runStatus", CherryBatchConstants.EXECFLAG_SUCCESS);
				}else{
					// 验证码为空的情况下记录日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00096");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(ConvertUtil.getString(map.get("eventId")));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}
			// 验证码不为空的情况下发送电话外呼MQ
			if(!"".equals(couponCode)){
				// 发送电话外呼MQ
				try{
					String brandCode = ConvertUtil.getString(map.get("brandCode"));
					String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
					String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
					String eventId = ConvertUtil.getString(map.get("eventId"));
					
					MQInfoDTO mqInfoDTO = new MQInfoDTO();
					// 品牌代码
					mqInfoDTO.setBrandCode(brandCode);
					
					String billType = "PC";
					
					String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(organizationInfoId), Integer.parseInt(brandInfoId), "", billType);
					// 业务类型
					mqInfoDTO.setBillType(billType);
					// 单据号
					mqInfoDTO.setBillCode(billCode);
					// 消息发送队列名
					mqInfoDTO.setMsgQueueName("batchMsgToCallInterfaceQueue");
					
					// 设定消息内容
					Map<String,Object> msgDataMap = new HashMap<String,Object>();
					// 设定消息版本号
					msgDataMap.put("Version", "AMQ.112.001");
					// 设定消息命令类型
					msgDataMap.put("Type", "1012");
					// 设定消息数据类型
					msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
					// 设定消息的数据行
					Map<String,Object> dataLine = new HashMap<String,Object>();
					// 消息的主数据行
					Map<String,Object> mainData = new HashMap<String,Object>();
					// 品牌代码
					mainData.put("BrandCode", brandCode);
					// 业务类型
					mainData.put("TradeType", billType);
					// 单据号
					mainData.put("TradeNoIF", billCode);
					// 品牌代码
					mainData.put("brandInfoId", brandInfoId);
					// 业务类型
					mainData.put("organizationInfoId", organizationInfoId);
					// 单据号
					mainData.put("eventId", eventId);
					// 单据号
					mainData.put("couponCode", couponCode);
					
					dataLine.put("MainData", mainData);
					msgDataMap.put("DataLine", dataLine);
					mqInfoDTO.setMsgDataMap(msgDataMap);
					// 发送MQ消息
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
				}catch(Exception e){
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00094");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(ConvertUtil.getString(e));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}
		}
		return returnMap;
	}
	
	// 事件处理
	public Map<String, Object> execEvent(Map<String, Object> map) throws Exception{
		Map<String, Object> execResultMap = new HashMap<String, Object>();
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String runCode = ConvertUtil.getString(map.get("runCode"));
		String couponCode = ConvertUtil.getString(map.get("couponCode"));
		String couponExpireTime = ConvertUtil.getString(map.get("couponExpireTime"));
		String campaignCode = ConvertUtil.getString(map.get("campaignCode"));
		String messageInfo = ConvertUtil.getString(map.get("messageContents"));
		String smsChannel= ConvertUtil.getString(map.get("smsChannel"));
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		
		//获取对应事件在数据库沟通触发事件设置表的设置
		List<Map<String, Object>> eventSetList = getEventSetList(map);
		if(eventSetList != null && !eventSetList.isEmpty()) {
			int runCount = 0, allCount = 0, sendMsgCount = 0;
			boolean getCustomerFlag = false;
			List<String> smsSetList = new ArrayList<String>();
			List<String> mailSetList = new ArrayList<String>();
			String batchId = CherryBatchConstants.BATCHID_PREFIX + eventType + DateUtil.getCurrTime();
			// 定义接口运行配置项Map
			Map<String, Object> ifRunMap = new HashMap<String, Object>();
			// 定义接口参数Map
			Map<String, Object> ifConfigMap = new HashMap<String, Object>();
			// 获取事件触发沟通是否使用实时接口的配置项【当前设置为1】
			String ifType = binOLCM14_BL.getConfigValue("1114", organizationInfoId, brandInfoId);
			if("1".equals(ifType)){		//全部开启实时接口的情况
				// 获取实时短信接口开放状态配置【如：ALDY 阿里大鱼】
				String smsInterface = binOLCM14_BL.getConfigValue("1110", organizationInfoId, brandInfoId);
				// 判断实时短信接口配置是否打开，若实时短信接口配置未打卡则使用非实时短信接口
				if(null!=smsInterface && !"".equals(smsInterface) && !"NG".equals(smsInterface)){
					ifRunMap.put("commInterface", smsInterface);
					// 实时接口打开时，设置运行接口为实时短信接口
					ifRunMap.put("runInterface", "NI");
					// 获取实时接口配置项
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("brandInfoId", brandInfoId);
					tempMap.put("organizationInfoId", organizationInfoId);
					tempMap.put("commInterface", smsInterface);
					ifConfigMap = binBECTCOM01.getIfConfigInfo(tempMap, "SMS");
				}
			}else if("2".equals(ifType)){		//仅验证事件开启实时接口的情况
				// 判断事件类型是否为微信认证事件或WebService短信验证事件
				if("11".equals(eventType) || "14".equals(eventType)){
					// 获取实时短信接口开放状态配置
					String smsInterface = binOLCM14_BL.getConfigValue("1110", organizationInfoId, brandInfoId);
					// 判断实时短信接口配置是否打开，若实时短信接口配置未打卡则使用非实时短信接口
					if(null!=smsInterface && !"".equals(smsInterface) && !"NG".equals(smsInterface)){
						ifRunMap.put("commInterface", smsInterface);
						// 实时接口打开时，设置运行接口为实时短信接口
						ifRunMap.put("runInterface", "NI");
						// 获取实时接口配置项
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.put("brandInfoId", brandInfoId);
						tempMap.put("organizationInfoId", organizationInfoId);
						tempMap.put("commInterface", smsInterface);
						ifConfigMap = binBECTCOM01.getIfConfigInfo(tempMap, "SMS");
					}
				}
			}else if("3".equals(ifType)){		//活动预约事件与验证事件开启实时接口的情况
				// 判断事件类型是否为活动预约事件和验证事件
				if("1".equals(eventType) || "11".equals(eventType) || "14".equals(eventType)){
					// 获取实时短信接口开放状态配置
					String smsInterface = binOLCM14_BL.getConfigValue("1110", organizationInfoId, brandInfoId);
					// 判断实时短信接口配置是否打开，若实时短信接口配置未打卡则使用非实时短信接口
					if(null!=smsInterface && !"".equals(smsInterface) && !"NG".equals(smsInterface)){
						ifRunMap.put("commInterface", smsInterface);
						// 实时接口打开时，设置运行接口为实时短信接口
						ifRunMap.put("runInterface", "NI");
						// 获取实时接口配置项
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.put("brandInfoId", brandInfoId);
						tempMap.put("organizationInfoId", organizationInfoId);
						tempMap.put("commInterface", smsInterface);
						ifConfigMap = binBECTCOM01.getIfConfigInfo(tempMap, "SMS");
					}
				}
			}
			
			// 循环遍历沟通的所有发送信息设置
			for(Map<String,Object> eventSetMap : eventSetList){
				allCount++;
				boolean flag = true;
				boolean errorFlag = false;
				String errorText = "";
				String eventSetId = ConvertUtil.getString(eventSetMap.get("eventSetId"));
				// 获取会员搜索编号以确定会员范围
				String searchCode = ConvertUtil.getString(eventSetMap.get("searchCode"));
				// 获取沟通类型
				String commType = ConvertUtil.getString(eventSetMap.get("commType"));
				try{
					// 获取沟通内容模板
					String messageContents = ConvertUtil.getString(eventSetMap.get("messageInfo"));
					if("90".equals(eventType)){
						if(!"".equals(messageInfo)){
							messageContents = messageInfo;
						}
					}else if("99".equals(eventType)){
						messageContents = messageInfo;
						String eventDate = ConvertUtil.getString(map.get("eventDate"));
						eventId = binBECTCOM01.getTimeNumString(eventDate);
						eventSetMap.put("smsChannel", smsChannel);
					}else if("100".equals(eventType)){
						if(!"".equals(messageInfo)){
							messageContents = messageInfo;
						}
						String eventDate = ConvertUtil.getString(map.get("eventDate"));
						eventId = binBECTCOM01.getTimeNumString(eventDate);
					}else if("10".equals(eventType)){
						messageContents = messageInfo;
						String eventDate = ConvertUtil.getString(map.get("eventDate"));
						eventId = binBECTCOM01.getTimeNumString(eventDate);
					}else if("11".equals(eventType)){
						String eventDate = ConvertUtil.getString(map.get("eventDate"));
						eventId = binBECTCOM01.getTimeNumString(eventDate);
					}else if("14".equals(eventType)){
						String eventDate = ConvertUtil.getString(map.get("eventDate"));
						eventId = binBECTCOM01.getTimeNumString(eventDate);
					}
					String moreInfoFlag = "N";
					// 判断沟通类型是否支持
					if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){	// 短信沟通
						// 判断沟通设置是否属于同一组对象
						if(smsSetList.contains(eventType)){
							moreInfoFlag = "Y";
						}else{
							moreInfoFlag = "N";
						}
						smsSetList.add(eventType);
						flag = true;
					}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){	// 邮件沟通
						// 判断沟通设置是否属于同一组对象
						if(mailSetList.contains(eventType)){
							moreInfoFlag = "Y";
						}else{
							moreInfoFlag = "N";
						}
						mailSetList.add(eventType);
						flag = true;
					}else{		// 不支持的沟通类型
						flag = false;
					}
					if(flag){
						if(!"".equals(messageContents)){
							int sendMsgNum = 0, notReceiveNum = 0, codeIllegalNum = 0, repeatNotSendNum = 0, notCreateMsgNum = 0, sendErrorNum = 0;
							int num = 1, memberCount = 0, customerNum = 0, repeatRunNum = 0, misConditionCount = 0;
							String nowTime = binBECTSMG05_Service.getSYSDate();
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("organizationInfoId", organizationInfoId);
							paramMap.put("brandInfoId", brandInfoId);
							paramMap.put("brandCode", brandCode);
							paramMap.put("runCode", runCode);
							paramMap.put("commSetId", eventSetId);
							paramMap.put("communicationCode", eventId);
							paramMap.put("searchCode", searchCode);
							paramMap.put("batchId", batchId);
							paramMap.put("planCode", eventType);
							paramMap.put("commType", commType);
							paramMap.put("repeatRange", "NL");
							paramMap.put("moreInfoFlag", moreInfoFlag);
							paramMap.putAll(ifRunMap);
							
							//将验证码及其有效时间设置到参数中
							if("9".equals(eventType)){
								String createCoupon = "F";
								// 事件类型为短信重发事件时
								Map<String, Object> msgCodeMap = new HashMap<String, Object>();
								msgCodeMap.put("messageSendCode", ConvertUtil.getString(eventSetMap.get("messageSendCode")));
								campaignCode = binBECTSMG05_Service.getCampCodeFromCreateCouponLog(msgCodeMap);
								if(null != campaignCode && !"".equals(campaignCode)){
									if(campaignCode.equals(CherryBatchConstants.EVENT_GETCOUPON_CAMP)){
										paramMap.put("campCode", campaignCode);
										// 获取验证码有效时间配置
										String expiredSecond = binOLCM14_BL.getConfigValue("1111", organizationInfoId, brandInfoId);
										String expiredTime = binBECTCOM01.addDateSecond(nowTime, CherryUtil.obj2int(expiredSecond));
										paramMap.put("expiredTime", expiredTime);
										createCoupon = "T";
									}
								}
								// 若原有信息的CouponCode不为空则加入从原有信息获取的CouponCode
								String hisCouponCode = ConvertUtil.getString(eventSetMap.get("couponCode"));
								if(null != hisCouponCode && !"".equals(hisCouponCode)){
									paramMap.put("couponCode", hisCouponCode);
									if("T".equals(createCoupon)){
										paramMap.put("createCouponFlag", "Y");
									}
								}
							}else if("11".equals(eventType)){
								// 事件类型为微信认证事件时
								campaignCode = CherryBatchConstants.EVENT_GETCOUPON_CAMP;
								paramMap.put("campCode", campaignCode);
								// 获取验证码有效时间配置
								String expiredSecond = binOLCM14_BL.getConfigValue("1111", organizationInfoId, brandInfoId);
								String expiredTime = binBECTCOM01.addDateSecond(nowTime, CherryUtil.obj2int(expiredSecond));
								paramMap.put("expiredTime", expiredTime);
							}else if("14".equals(eventType)){
								// 事件类型为WebService验证码发送事件时
								campaignCode = CherryBatchConstants.EVENT_GETCOUPON_CAMP;
								paramMap.put("campCode", campaignCode);
								// 获取验证码有效时间配置
								if(!"".equals(messageInfo)){
									// WebService传入的信息内容参数不为空的情况下，判断有没有传入Coupon过期时间参数，若有传入则获取该参数
									if(!"".equals(couponExpireTime)){
										// 判断couponExpireTime是整数型还是日期型，为整数型时在当前时间基础上加上参数值，为日期型时直接使用传入参数，为异常数据时不做处理
										if(CherryChecker.isNumeric(couponExpireTime)){
											String expiredTime = binBECTCOM01.addDateSecond(nowTime, CherryUtil.obj2int(couponExpireTime));
											paramMap.put("expiredTime", expiredTime);
										}else if(CherryChecker.checkDate(couponExpireTime, CherryBatchConstants.DF_TIME_PATTERN)){
											paramMap.put("expiredTime", couponExpireTime);
										}
									}
								}else{
									// WebService传入的信息内容参数为空的情况下，获取新后台的设置
									String expiredSecond = binOLCM14_BL.getConfigValue("1111", organizationInfoId, brandInfoId);
									String expiredTime = binBECTCOM01.addDateSecond(nowTime, CherryUtil.obj2int(expiredSecond));
									paramMap.put("expiredTime", expiredTime);
								}
							}else if("15".equals(eventType)){
								//生日礼事件
								campaignCode = (String) eventSetMap.get("relationActivity");
								map.put("CampaignCode", campaignCode);
								paramMap.put("campCode", campaignCode);
							}
							// 定义沟通日志Map
							Map<String, Object> logMap = new HashMap<String, Object>();
							logMap.putAll(paramMap);							
							logMap.put("runCode", runCode);
							logMap.put("moduleCode", "BINBECTSMG05");
							logMap.put("moduleName", CherryBatchConstants.MODULE_NAME_GT);
							logMap.put("runType", "3");
							logMap.put("runBeginTime", nowTime);
							logMap.put("createBy", "SMGJOB");
							logMap.put("createPGM", "BINBECTSMG05");
							try{
								Map<String, Object> searchRecord = new HashMap<String, Object>();
								if(null != searchCode && !"".equals(searchCode)){
									// 获取搜索记录信息
									Map<String, Object> searchMap = new HashMap<String, Object>();
									searchMap.put("brandInfoId", brandInfoId);
									searchMap.put("organizationInfoId", organizationInfoId);
									searchMap.put("searchCode", searchCode);
									searchRecord = binBECTSMG06_IF.getSearchInfo(searchMap);
								}
								// 定义搜索条件Map
								Map<String, Object> searchInfo = new HashMap<String, Object>();
								searchInfo.putAll(map);
								searchInfo.put("mobileRule", mobileRule);
								searchInfo.put("SORT_ID", "memId");
								// 获取测试会员是否发送事件触发的信息配置项
								String testMemberMsgFlag = binOLCM14_BL.getConfigValue("1097", organizationInfoId, brandInfoId);
								// 当配置项的值为测试会员不发送事件触发信息的情况下给会员信息查询增加查询正式会员的条件
								if("0".equals(testMemberMsgFlag)){
									searchInfo.put("testType", "1");
								}
								// 获取待发送信息的客户总数
								memberCount = getCustomerCount(searchInfo);
								if(memberCount > 0){
									// 如果需要发送随机验证号则获取一个验证号列表
									List<String> couponList = new ArrayList<String>();
									Map<String, Object> CouponInfoMap = new HashMap<String, Object>();
									// 组织信息ID
							    	CouponInfoMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
							    	// 品牌信息ID
							    	CouponInfoMap.put(CherryConstants.BRANDINFOID, brandInfoId);
							    	// 主题活动代号
							    	CouponInfoMap.put(CampConstants.CAMP_CODE, campaignCode);
							    	// 需要获取的Coupon码数量
							    	CouponInfoMap.put("couponCount", (memberCount + CherryBatchConstants.EVENT_GETCOUPON_CONSTANT));
									// 判断信息是否需要发送Coupon号，若需要发送则生成一批Coupon号备用
							    	if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
							    		// 获取生成Coupon号位数配置
							    		String couponCount = binOLCM14_BL.getConfigValue("1124", organizationInfoId, brandInfoId);
							    		if("10".equals(couponCount)){
							    			// 获取CouponCode列表
											couponList = cpn10Ser.generateCoupon(CouponInfoMap);
							    		}else if("8".equals(couponCount)){
							    			// 获取CouponCode列表
											couponList = cpnSer.generateCoupon(CouponInfoMap);
							    		}else if("4".equals(couponCount)){
											// 获取CouponCode列表
											couponList = cpn4Ser.generateCoupon(CouponInfoMap);
							    		}else{
							    			// 获取CouponCode列表
											couponList = cpn6Ser.generateCoupon(CouponInfoMap);
							    		}
							    	}
									// 获取可参与计算的变量对应的计算规则
									Map<String, Object> valueMap = new HashMap<String, Object>();
									List<Map<String, Object>> variableList = binBECTSMG01_Service.getTemplateVariableSet(valueMap);
									// 循环比较最近一批起始值与客户总数，若小于客户总数则继续运行
									while(num <= memberCount){
										try{
											int startNum = num;
											int endNum = num + CherryBatchConstants.GETMEMBERNUMONCE;
											searchInfo.put("START", startNum);
											searchInfo.put("END", endNum);
											// 获取客户信息
											List<Map<String, Object>> resultList = getCustomerInfo(searchInfo);
											if(resultList != null && !resultList.isEmpty()){
												for(Map<String,Object> resultMap : resultList){
													customerNum++;
													String sendflag = "";
													String receiverCode = "";
													boolean getCouponFlag = true;
													boolean sendConditionFlag = false;
													try{
														String relationActivity = ConvertUtil.getString(eventSetMap.get("relationActivity"));
														String customerId = ConvertUtil.getString(resultMap.get("memId"));
														String memCode = ConvertUtil.getString(resultMap.get("memCode"));
														String phoneNum = ConvertUtil.getString(resultMap.get("mobilePhone"));
														String activityCode = ConvertUtil.getString(resultMap.get("campaignCode"));
														// 判断设置中的活动编号是否为空
														if(null != relationActivity && !"".equals(relationActivity)){
															// 判断与活动相关的会员中活动编号是否为空
															if(null != activityCode && !"".equals(activityCode)){
																// 与活动相关的会员中活动编号不为空时判断设置中的活动编号与会员的活动编号是否一致
																if(activityCode.equals(relationActivity)){
																	sendConditionFlag = true;
																}else{
																	sendConditionFlag = false;
																}
															}else{
																// 判断外部传入的活动编号是否为空
																if(null != campaignCode && !"".equals(campaignCode) && !campaignCode.equals(CherryBatchConstants.EVENT_GETCOUPON_CAMP)){
																	// 外部传入的活动编号不为空的情况下判断设置中的活动编号与外部传入的活动编号是否一致
																	if(campaignCode.equals(relationActivity)){
																		sendConditionFlag = true;
																	}else{
																		sendConditionFlag = false;
																	}
																}else{
																	sendConditionFlag = true;
																}
															}
														}else{
															// 设置中的活动编号为空时默认为无活动限制
															sendConditionFlag = true;
														}
														// 如果会员条件的验证通过则进入活动校验
														if(sendConditionFlag){
															if(null != searchCode && !"".equals(searchCode) && null != searchRecord){
																Map<String, Object> customerSearchMap = new HashMap<String, Object>();
																customerSearchMap.put("brandInfoId", brandInfoId);
																customerSearchMap.put("organizationInfoId", organizationInfoId);
																customerSearchMap.put("memberInfoId", customerId);
																customerSearchMap.put("memCode", memCode);
																customerSearchMap.put("phoneNum", phoneNum);
																customerSearchMap.put("eventType", eventType);
																customerSearchMap.put("eventId", eventId);
																customerSearchMap.putAll(searchRecord);
																sendConditionFlag = checkSendConditionFlag(customerSearchMap);
															}else{
																sendConditionFlag = true;
															}
														}
														// 判断沟通对象是否在指定的对象范围内
														if(sendConditionFlag){
															// 根据沟通事件确定所发送的短信类型
															String hisPlan = ConvertUtil.getString(resultMap.get("hisPlan"));
															String smsType;
															if("".equals(ConvertUtil.getString(eventSetMap.get("smsChannel")))){
																smsType = getSmsTypeByEvent(hisPlan, eventType);
															}else{
																smsType=ConvertUtil.getString(eventSetMap.get("smsChannel"));
															}
															// 加入沟通信息
															resultMap.putAll(paramMap);
															resultMap.put("eventCode", eventType);
															resultMap.put("mobileRule", mobileRule);
															resultMap.put("smsType", smsType);
															resultMap.put("dataSource", "2");
															resultMap.put("verifiedFlag", "0");
															// 短信重发事件并且需要实时发送短信时
															if("9".equals(eventType) && "2".equals(smsType) && !"NI".equals(ConvertUtil.getString(ifRunMap.get("runInterface")))){
																// 获取实时短信接口开放状态配置
																String smsInterface = binOLCM14_BL.getConfigValue("1110", organizationInfoId, brandInfoId);
																// 判断实时短信接口配置是否打开，若实时短信接口配置未打卡则使用非实时短信接口
																if(null!=smsInterface && !"".equals(smsInterface) && !"NG".equals(smsInterface)){
																	resultMap.put("commInterface", smsInterface);
																	// 实时接口打开时，设置运行接口为实时短信接口
																	resultMap.put("runInterface", "NI");
																	// 获取实时接口配置项
																	Map<String, Object> tempMap = new HashMap<String, Object>();
																	tempMap.put("brandInfoId", brandInfoId);
																	tempMap.put("organizationInfoId", organizationInfoId);
																	tempMap.put("commInterface", smsInterface);
																	if(null == ifConfigMap || ifConfigMap.isEmpty()){
																		ifConfigMap = binBECTCOM01.getIfConfigInfo(tempMap, "SMS");
																	}
																}
															}
															// 根据沟通类型获取沟通信息接收号码
															if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){
																// 沟通类型为短信沟通时，获取手机号码作为接收号码
																receiverCode = ConvertUtil.getString(resultMap.get("mobilePhone"));
															}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){
																// 沟通类型为邮件沟通时，获取邮件地址作为接收号码
																receiverCode = ConvertUtil.getString(resultMap.get("email"));
															}
															// 判断信息中是否需要发送Coupon码
															if(messageContents.contains(CherryBatchConstants.COUPON_CODE)){
																// 如果需要发送Coupon码但是Coupon码为空的情况
																if("11".equals(eventType)){
																	String msgCouponCode = "";
																	// 如果为同一沟通多沟通模板的情况则尝试获取前一个模板生成的Coupon号，若未获取到则生成新的Coupon码
																	if("Y".equals(moreInfoFlag)){
																		// 获取前一个模板生成的Coupon号
																		msgCouponCode = binBECTSMG01_Service.getCommonSetCoupon(resultMap);
																		msgCouponCode = ConvertUtil.getString(msgCouponCode);
																	}
																	// 若不是同一沟通多模板的情况或不能从同一沟通的前一个模板对应的信息中获取到Coupon号则进入获取系统生成的Coupon号逻辑
																	if("".equals(msgCouponCode)){
																		// 加入沟通程序生成的Coupon号
																		if(couponList != null && !couponList.isEmpty()){
																			// 加入自动生成的Coupon号
																			resultMap.put("couponCode", couponList.get(customerNum-1));
																			resultMap.put("createCouponFlag", "Y");
																		}else{
																			getCouponFlag = false;
																		}
																	}else{
																		// 同一沟通多模板的情况加入前一次生成的Coupon号
																		resultMap.put("couponCode", msgCouponCode);
																	}
																}else if("14".equals(eventType)){
																	// 判断是否由新后台提供信息内容，若不是则尝试获取参数传入的CouponCode
																	if(!"".equals(messageInfo)){
																		// 判断是否传入了CouponCode参数，若传入了该参数则使用该参数并创建Coupon生成记录
																		if(!"".equals(couponCode)){
																			resultMap.put("couponCode", couponCode);
																			resultMap.put("createCouponFlag", "Y");
																		}
																	}else{
																		// 新后台提供信息模板并生成Coupon号的情况
																		String msgCouponCode = "";
																		// 如果为同一沟通多沟通模板的情况则尝试获取前一个模板生成的Coupon号，若未获取到则生成新的Coupon码
																		if("Y".equals(moreInfoFlag)){
																			// 获取前一个模板生成的Coupon号
																			msgCouponCode = binBECTSMG01_Service.getCommonSetCoupon(resultMap);
																			msgCouponCode = ConvertUtil.getString(msgCouponCode);
																		}
																		// 若不是同一沟通多模板的情况或不能从同一沟通的前一个模板对应的信息中获取到Coupon号则进入获取系统生成的Coupon号逻辑
																		if("".equals(msgCouponCode)){
																			// 加入沟通程序生成的Coupon号
																			if(couponList != null && !couponList.isEmpty()){
																				// 加入自动生成的Coupon号
																				resultMap.put("couponCode", couponList.get(customerNum-1));
																				resultMap.put("createCouponFlag", "Y");
																			}else{
																				getCouponFlag = false;
																			}
																		}else{
																			// 同一沟通多模板的情况加入前一次生成的Coupon号
																			resultMap.put("couponCode", msgCouponCode);
																		}
																	}
																}else{
																	if("".equals(ConvertUtil.getString(resultMap.get("couponCode")))){
																		getCouponFlag = false;
																	}
																}
															}
															
															if(getCouponFlag){
																try{
																	String couponCodeValue = ConvertUtil.getString(resultMap.get("couponCode"));
																	String expiredTimeValue = ConvertUtil.getString(resultMap.get("expiredTime"));
																	if(commType.equals(CherryBatchConstants.COMMTYPE_SMS)){
																		// 发送MQ调用短信发送方法
																		try{
																			MQInfoDTO mqInfoDTO = new MQInfoDTO();
																			// 品牌代码
																			mqInfoDTO.setBrandCode(brandCode);
																			
																			String billType = "SM";
																			
																			String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(organizationInfoId), Integer.parseInt(brandInfoId), "", billType);
																			// 业务类型
																			mqInfoDTO.setBillType(billType);
																			// 单据号
																			mqInfoDTO.setBillCode(billCode);
																			// 消息发送队列名
																			mqInfoDTO.setMsgQueueName("batchSendMsgToInterfaceQueue");
																			
																			// 设定消息内容
																			Map<String,Object> msgDataMap = new HashMap<String,Object>();
																			// 设定消息版本号
																			msgDataMap.put("Version", "AMQ.111.001");
																			// 设定消息命令类型
																			msgDataMap.put("Type", "1011");
																			// 设定消息数据类型
																			msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
																			// 设定消息的数据行
																			Map<String,Object> dataLine = new HashMap<String,Object>();
																			// 消息的主数据行
																			Map<String,Object> mainData = new HashMap<String,Object>();
																			// 品牌代码
																			mainData.put("BrandCode", brandCode);
																			// 业务类型
																			mainData.put("TradeType", billType);
																			// 单据号
																			mainData.put("TradeNoIF", billCode);
																			// 品牌代码
																			mainData.put("messageContents", messageContents);
																			// 业务类型
																			mainData.put("memberMap", resultMap);
																			// 单据号
																			mainData.put("variableList", variableList);
																			// 单据号
																			mainData.put("ifConfigMap", ifConfigMap);
																			
																			dataLine.put("MainData", mainData);
																			msgDataMap.put("DataLine", dataLine);
																			mqInfoDTO.setMsgDataMap(msgDataMap);
																			// 发送MQ消息
																			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
																			// 设置发送标识
																			sendflag = CherryBatchConstants.SENDSTATUS_SUCCESS;
																		}catch(Exception e){
																			sendflag = CherryBatchConstants.SENDSTATUS_ERROR;
																		}
																	}else if(commType.equals(CherryBatchConstants.COMMTYPE_EMAIL)){
																		// 调用邮件发送方法
																		sendflag = binBECTSMG04_BL.tran_sendEmail(messageContents, resultMap, variableList);
																	}
																	if(sendflag.equals(CherryBatchConstants.SENDSTATUS_SUCCESS)){
																		sendMsgCount++;
																		execResultMap.put("couponCode", couponCodeValue);
																		execResultMap.put("couponExpiredTime", expiredTimeValue);
																	}
																}catch(Exception ex){
																	// 记录Batch日志
																	BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
																	batchLoggerDTO.setCode("ECT00031");
																	batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
																	batchLoggerDTO.addParam(batchId);
																	batchLoggerDTO.addParam(eventId);
																	batchLoggerDTO.addParam(receiverCode);
																	batchLoggerDTO.addParam(ConvertUtil.getString(ex));
																	CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
																	cherryBatchLogger.BatchLogger(batchLoggerDTO);
																	
																	// 记录发送错误的信息
																	String[] errorInfo = new String[1];
																	errorInfo[0] = ConvertUtil.getString(ex);
																	String message = binBECTCOM01.replaceTemplate(messageContents, resultMap, variableList);
																	String sendTime = binBECTSMG05_Service.getSYSDate();
																	String errMsg = PropertiesUtil.getMessage("ECT00082", errorInfo);
																	if("".equals(message)){
																		message = messageContents;
																	}
																	Map<String, Object> errorLogMap = new HashMap<String, Object>();
																	errorLogMap.put("customerSysId", ConvertUtil.getString(resultMap.get("memId")));
																	errorLogMap.put("receiverCode", receiverCode);
																	errorLogMap.put("sendTime", sendTime);
																	errorLogMap.put("message", message);
																	errorLogMap.put("errorType", "4");
																	errorLogMap.put("errorText", errMsg);
																	errorLogMap.putAll(resultMap);
																	errorLogMap.putAll(binBECTSMG03_BL.getComParam());
																	binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
																	// 设置发送状态
																	sendflag = CherryBatchConstants.SENDSTATUS_ERROR;
																}
															}else{
																// 需要发送Coupon号但是没有获取到Coupon号的情况
																// 记录Batch日志
																BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
																batchLoggerDTO.setCode("ECT00076");
																batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
																batchLoggerDTO.addParam(batchId);
																batchLoggerDTO.addParam(eventId);
																batchLoggerDTO.addParam(receiverCode);
																CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
																cherryBatchLogger.BatchLogger(batchLoggerDTO);
																
																// 记录发送错误的信息
																String message = binBECTCOM01.replaceTemplate(messageContents, resultMap, variableList);
																String sendTime = binBECTSMG05_Service.getSYSDate();
																String errMsg = PropertiesUtil.getMessage("ECT00079", null);
																if("".equals(message)){
																	message = messageContents;
																}
																Map<String, Object> errorLogMap = new HashMap<String, Object>();
																errorLogMap.put("customerSysId", ConvertUtil.getString(resultMap.get("memId")));
																errorLogMap.put("receiverCode", receiverCode);
																errorLogMap.put("sendTime", sendTime);
																errorLogMap.put("message", message);
																errorLogMap.put("errorType", "3");
																errorLogMap.put("errorText", errMsg);
																errorLogMap.putAll(resultMap);
																errorLogMap.putAll(binBECTSMG03_BL.getComParam());
																binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
																// 设置发送状态
																sendflag = CherryBatchConstants.SENDSTATUS_NOTCREATEMSG;
															}
															
															if(sendflag.equals(CherryBatchConstants.SENDSTATUS_SUCCESS)){
																sendMsgNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_ERROR)){
																sendErrorNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_REPEAT)){
																repeatNotSendNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_NOTRECEIVE)){
																notReceiveNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_CODEILLEGAL)){
																codeIllegalNum++;
															}else if(sendflag.equals(CherryBatchConstants.SENDSTATUS_NOTCREATEMSG)){
																notCreateMsgNum++;
															}else{
																sendErrorNum++;
															}
														}else{
															// 沟通对象不在指定的对象范围内，累计不符合条件的客户数量
															misConditionCount++;
															// 记录Batch日志
															BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
															batchLoggerDTO.setCode("ECT00085");
															batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_WARNING);
															batchLoggerDTO.addParam(eventType);
															batchLoggerDTO.addParam(eventId);
															batchLoggerDTO.addParam(customerId);
															CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
															cherryBatchLogger.BatchLogger(batchLoggerDTO);
														}
													}catch(Exception e){
														// 记录Batch日志
														BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
														batchLoggerDTO.setCode("ECT00031");
														batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
														batchLoggerDTO.addParam(batchId);
														batchLoggerDTO.addParam(eventId);
														batchLoggerDTO.addParam(receiverCode);
														batchLoggerDTO.addParam(ConvertUtil.getString(e));
														CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
														cherryBatchLogger.BatchLogger(batchLoggerDTO);
														sendErrorNum++;
													}finally{
														resultMap = null;
													}
												}
												resultList = null;
												repeatRunNum = 0;
												num = endNum + 1;
											}else{
												// 没有获得客户列表
												repeatRunNum ++;
												// 记录Batch日志
												BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
												batchLoggerDTO.setCode("ECT00033");
												batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
												batchLoggerDTO.addParam(eventId);
												batchLoggerDTO.addParam(ConvertUtil.getString(num));
												CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
												cherryBatchLogger.BatchLogger(batchLoggerDTO);
												// 没有获取到客户列表的情况下重试3次，若依然未取到客户列表则取下一批
												if(repeatRunNum > 3){
													repeatRunNum = 0;
													num = endNum + 1;
												}
											}
										}catch(Exception exp){
											repeatRunNum ++;
											// 记录Batch日志
											BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
											batchLoggerDTO.setCode("ECT00049");
											batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
											batchLoggerDTO.addParam(eventType);
											batchLoggerDTO.addParam(eventId);
											batchLoggerDTO.addParam(ConvertUtil.getString(num));
											batchLoggerDTO.addParam(ConvertUtil.getString(exp));
											CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
											cherryBatchLogger.BatchLogger(batchLoggerDTO);
											// 出现问题的情况下重试3次，若依然无法解决问题则抛出错误信息
											if(repeatRunNum > 3){
												throw exp;
											}
										}catch(Throwable t){
											repeatRunNum ++;
											// 记录Batch日志
											BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
											batchLoggerDTO.setCode("ECT00049");
											batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
											batchLoggerDTO.addParam(eventType);
											batchLoggerDTO.addParam(eventId);
											batchLoggerDTO.addParam(ConvertUtil.getString(num));
											batchLoggerDTO.addParam(ConvertUtil.getString(t));
											CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
											cherryBatchLogger.BatchLogger(batchLoggerDTO);
											// 出现问题的情况下重试3次，若依然无法解决问题则抛出错误信息
											if(repeatRunNum > 3){
												throw t;
											}
										}
									}
									logMap.put("runStatus", "0");
									logMap.put("runError", "");
									runCount++;
								}else{
									// 没有沟通对象
									logMap.put("runStatus", "1");
									logMap.put("runError", PropertiesUtil.getMessage("ECT00014", null));
								}
							}catch(Exception ep){
								logMap.put("runStatus", "1");
								logMap.put("runError", ConvertUtil.getString(ep));
							}catch(Throwable t){
								logMap.put("runStatus", "1");
								logMap.put("runError", ConvertUtil.getString(t));
							}finally{
								// 记录执行日志
								int commObjectNum = memberCount-misConditionCount;
								if(commObjectNum > 0){
									getCustomerFlag = true;
									if(null==logMap.get("runStatus") || "".equals(ConvertUtil.getString(logMap.get("runStatus")))){
										logMap.put("runStatus", "3");
									}
									logMap.put("customerNum", commObjectNum);
									logMap.put("sendMsgNum", sendMsgNum);
									logMap.put("notReceiveNum", notReceiveNum);
									logMap.put("codeIllegalNum", codeIllegalNum);
									logMap.put("repeatNotSendNum", repeatNotSendNum);
									logMap.put("notCreateMsgNum", notCreateMsgNum);
									logMap.put("sendErrorNum", sendErrorNum);
									binBECTSMG01_Service.addCommRunLog(logMap);
									logMap = null;
								}
							}
							paramMap = null;
						}else{
							// 沟通内容为空
							errorText = PropertiesUtil.getMessage("ECT00007", null);
							errorFlag = true;
						}
					}else{
						// 沟通类型不支持
						errorText = PropertiesUtil.getMessage("ECT00046", null);
						errorFlag = true;
					}
				}catch(Exception e){
					errorText = ConvertUtil.getString(e);
					errorFlag = true;
				}
				if(errorFlag){
					// 记录错误日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00045");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(eventType);
					batchLoggerDTO.addParam(eventId);
					batchLoggerDTO.addParam(eventSetId);
					batchLoggerDTO.addParam(commType);
					batchLoggerDTO.addParam(errorText);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}
			ifConfigMap = null;
			if(!getCustomerFlag){
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00014");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
			}
			if(runCount > 0){
				if(runCount==allCount){
					// 设置全部执行成功情况下判断是否有成功发送的信息
					if(sendMsgCount > 0){
						// 有短信发送成功的情况
						execResultMap.put("runStatus", CherryBatchConstants.EXECFLAG_SUCCESS);
					}else{
						// 没有任何短信发送成功的情况
						execResultMap.put("runStatus", CherryBatchConstants.EXECFLAG_ALLMSGSENDFAILED);
					}
				}else{
					// 设置部分执行成功情况下判断是否有成功发送的短信
					if(sendMsgCount > 0){
						// 有短信发送成功的情况
						execResultMap.put("runStatus", CherryBatchConstants.EXECFLAG_WARNING);
					}else{
						// 没有任何短信发送成功的情况
						execResultMap.put("runStatus", CherryBatchConstants.EXECFLAG_ALLMSGSENDFAILED);
					}
				}
			}else{
				// 全部失败
				if(!getCustomerFlag){
					execResultMap.put("runStatus", CherryBatchConstants.EXECFLAG_CUSTOMERNULL);
				}else{
					execResultMap.put("runStatus", CherryBatchConstants.EXECFLAG_ERROR);
				}
			}
		}else{
			// 没有取到事件设置的情况
			execResultMap.put("runStatus", CherryBatchConstants.EXECFLAG_EVENTSETNULL);
		}
		return execResultMap;
	}
	
	// 获取沟通事件设置
	public List<Map<String, Object>> getEventSetList(Map<String, Object> map){
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String messageInfo = ConvertUtil.getString(map.get("messageContents"));
		
		List<Map<String, Object>> eventSetList = new ArrayList<Map<String, Object>>();
		if("90".equals(eventType)){
			// 若沟通信息内容参数不为空发送参数里传入的内容，若参数为空则获取新后台的设置
			if(!"".equals(messageInfo)){
				// 事件类型为WebService发送验证码事件时
				Map<String, Object> setMap = new HashMap<String, Object>();
				setMap.put("eventId", eventId);
				setMap.put("searchCode", "");
				setMap.put("isTemplate", "2");//信息是否关联模板【1：是，2：否】
				setMap.put("templateCode", "");
				setMap.put("messageInfo", messageInfo);
				setMap.put("commType", "1");
				setMap.put("relationActivity", "");
				eventSetList.add(setMap);
			}else{
				eventSetList = binBECTSMG05_Service.getEventSetList(map);
			}
		}else if("99".equals(eventType)){
			// 事件类型为发送测试信息时
			Map<String, Object> setMap = new HashMap<String, Object>();
			setMap.put("eventId", eventId);
			setMap.put("searchCode", "");
			setMap.put("isTemplate", "2");
			setMap.put("templateCode", "");
			setMap.put("messageInfo", messageInfo);
			setMap.put("commType", "1");
			setMap.put("relationActivity", "");
			eventSetList.add(setMap);
		}else if("100".equals(eventType)){
			// 事件类型为Batch监控事件时
			eventSetList = binBECTSMG05_Service.getEventSetList(map);
			if(eventSetList == null || eventSetList.isEmpty()){
				eventSetList = new ArrayList<Map<String, Object>>();
				Map<String, Object> setMap = new HashMap<String, Object>();
				setMap.put("eventId", eventId);
				setMap.put("searchCode", "");
				setMap.put("isTemplate", "2");
				setMap.put("templateCode", "");
				setMap.put("messageInfo", messageInfo);
				setMap.put("commType", "1");
				setMap.put("relationActivity", "");
				eventSetList.add(setMap);
			}
		}else if(CherryUtil.string2int(eventType) >= 70 && CherryUtil.string2int(eventType) < 80){
			// 事件类型为积分变化类型时
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("brandInfoId", brandInfoId);
			tMap.put("organizationInfoId", organizationInfoId);
			tMap.put("eventType", eventType);
			eventSetList.addAll(binBECTSMG05_Service.getEventSetList(tMap));
			// 如果没有取到积分变化类型对应的事件设置则根据积分值的正负情况获取积分增加和减少事件的设置
			if(eventSetList == null || eventSetList.isEmpty()){
				List<Map<String, Object>> tradeTypeList = binBECTSMG05_Service.getPointChangeTypeList(map);
				for(Map<String,Object> tradeTypeMap : tradeTypeList){
					int point = ConvertUtil.getInt(tradeTypeMap.get("point"));
					if(point < 0){
						eventType = CherryBatchConstants.POINTEVENTTYPE_JS;
					}else{
						eventType = CherryBatchConstants.POINTEVENTTYPE_ZJ;
					}
					tMap.put("eventType", eventType);
					eventSetList.addAll(binBECTSMG05_Service.getEventSetList(tMap));
				}
			}
		}else if("9".equals(eventType)){
			// 事件类型为短信重发事件时
			String messageSendCode = "";
			String couponCode = "";
			Map<String, Object> messageMap = binBECTSMG05_Service.getRetransmissionMsg(map);
			if(messageMap == null || messageMap.isEmpty()){
				// 会员手机号解密，解密失败的情况下捕获错误，继续使用原有传入的参数
				Map<String, Object> praMap = new HashMap<String, Object>();
				praMap.putAll(map);
				try{
					if (!CherryChecker.isNullOrEmpty(eventId, true)) {
						praMap.put("eventId", CherryBatchSecret.encryptData(brandCode, eventId));
					}
				}catch(Exception e){}
				Map<String, Object> msgMap = binBECTSMG05_Service.getRessMsgByMobile(praMap);
				if(msgMap != null && !msgMap.isEmpty()){
					messageSendCode = ConvertUtil.getString(msgMap.get("messageSendCode"));
					messageInfo = ConvertUtil.getString(msgMap.get("message"));
					couponCode = ConvertUtil.getString(msgMap.get("couponCode"));
				}
			}else{
				messageInfo = ConvertUtil.getString(messageMap.get("message"));
				if(null == messageInfo || "".equals(messageInfo)){
					// 会员手机号解密，解密失败的情况下捕获错误，继续使用原有传入的参数
					Map<String, Object> praMap = new HashMap<String, Object>();
					praMap.putAll(map);
					try{
						if (!CherryChecker.isNullOrEmpty(eventId, true)) {
							praMap.put("eventId", CherryBatchSecret.encryptData(brandCode, eventId));
						}
					}catch(Exception e){}
					Map<String, Object> msgMap = binBECTSMG05_Service.getRessMsgByMobile(praMap);
					if(msgMap != null && !msgMap.isEmpty()){
						messageSendCode = ConvertUtil.getString(msgMap.get("messageSendCode"));
						messageInfo = ConvertUtil.getString(msgMap.get("message"));
						couponCode = ConvertUtil.getString(msgMap.get("couponCode"));
					}
				}else{
					messageSendCode = ConvertUtil.getString(messageMap.get("messageSendCode"));
					messageInfo = ConvertUtil.getString(messageMap.get("message"));
					couponCode = ConvertUtil.getString(messageMap.get("couponCode"));
				}
			}
			Map<String, Object> setMap = new HashMap<String, Object>();
			setMap.put("eventId", eventId);
			setMap.put("searchCode", "");
			setMap.put("isTemplate", "2");
			setMap.put("templateCode", "");
			setMap.put("messageSendCode", messageSendCode);
			setMap.put("messageInfo", messageInfo);
			setMap.put("couponCode", couponCode);
			setMap.put("commType", "1");
			setMap.put("relationActivity", "");
			eventSetList.add(setMap);
		}else if("10".equals(eventType)){
			// 事件类型为手动发送信息时
			Map<String, Object> setMap = new HashMap<String, Object>();
			setMap.put("eventId", eventId);
			setMap.put("searchCode", "");
			setMap.put("isTemplate", "2");
			setMap.put("templateCode", "");
			setMap.put("messageInfo", messageInfo);
			setMap.put("commType", "1");
			setMap.put("relationActivity", "");
			eventSetList.add(setMap);
		}else if("14".equals(eventType)){
			// 若沟通信息内容参数不为空发送参数里传入的内容，若参数为空则获取新后台的设置
			if(!"".equals(messageInfo)){
				// 事件类型为WebService发送验证码事件时
				Map<String, Object> setMap = new HashMap<String, Object>();
				setMap.put("eventId", eventId);
				setMap.put("searchCode", "");
				setMap.put("isTemplate", "2");
				setMap.put("templateCode", "");
				setMap.put("messageInfo", messageInfo);
				setMap.put("commType", "1");
				setMap.put("relationActivity", "");
				eventSetList.add(setMap);
			}else{
				eventSetList = binBECTSMG05_Service.getEventSetList(map);
			}
		}else{
			eventSetList = binBECTSMG05_Service.getEventSetList(map);
		}
		return eventSetList;
	}
	
	// 获取沟通对象信息
	public List<Map<String, Object>> getCustomerInfo(Map<String, Object> map) throws CherryBatchException{
		List<Map<String, Object>> customerList = new ArrayList<Map<String, Object>>();
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		try{
			if("1".equals(eventType)){
				// 客户参与活动预约触发事件
				map.put("orderState", "RV");
				customerList = binBECTSMG05_Service.getActivityCustomerList(map);
			}else if("2".equals(eventType)){
				// 活动发货触发事件
				map.put("orderState", "AR");
				customerList = binBECTSMG05_Service.getActivityCustomerList(map);
			}else if("3".equals(eventType)){
				// 活动柜台收货触发事件
				map.put("orderState", "AR");
				customerList = binBECTSMG05_Service.getActivityCustomerList(map);
			}else if("4".equals(eventType)){
				// 客户取消活动单据触发事件
				map.put("orderState", "CA");
				customerList = binBECTSMG05_Service.getActivityCustomerList(map);
			}else if("5".equals(eventType)){
				// 会员入会触发事件
				customerList = binBECTSMG05_Service.getJoinMemberInfoList(map);
			}else if("6".equals(eventType)){
				// 会员转柜触发事件
				customerList = binBECTSMG05_Service.getChangeCounterMemberList(map);
			}else if(CherryUtil.string2int(eventType) >= 70 && CherryUtil.string2int(eventType) < 80){
				// 会员积分变化事件
				customerList = binBECTSMG05_Service.getPointChangeMemberList(map);
			}else if("8".equals(eventType)){
				// 会员信息变更事件
				customerList = binBECTSMG05_Service.getChangeInfoMemberList(map);
			}else if("9".equals(eventType)){
				// 短信重发事件
				customerList = binBECTSMG05_Service.getRetransmissionMsgMember(map);
				// 如果通过信息编号无法获取到需要重发的信息则尝试通过手机号加活动编号获取需要重发的信息
				if(null == customerList || customerList.isEmpty()){
					// 会员手机号解密，解密失败的情况下捕获错误，继续使用原有传入的参数
					Map<String, Object> praMap = new HashMap<String, Object>();
					praMap.putAll(map);
					try{
						if (!CherryChecker.isNullOrEmpty(eventId, true)) {
							praMap.put("eventId", CherryBatchSecret.encryptData(brandCode, eventId));
						}
					}catch(Exception e){}
					customerList = binBECTSMG05_Service.getRessMsgMemberByMobile(praMap);
				}
			}else if("10".equals(eventType)){
				// 发送信息触发事件
				customerList = binBECTCOM01.getReceiverCode(eventId, map);
			}else if("11".equals(eventType)){
				// 登录验证触发事件
				customerList = binBECTCOM01.getReceiverCode(eventId, map);
			}else if("12".equals(eventType)){
				// 会员升级事件
				customerList = binBECTSMG05_Service.getLevelUpMemberInfoList(map);
			}else if("13".equals(eventType)){
				// 会员信息查询（会员密码查询）
				customerList = binBECTCOM01.getReceiverCode(eventId, map);
			}else if("14".equals(eventType)){
				// WebService发送验证码事件
				customerList = binBECTCOM01.getReceiverCode(eventId, map);
			}else if("15".equals(eventType)){
				// 生日礼
				map.put("SubType", "BIR");
				customerList = binBECTSMG05_Service.getMemInfoByMobile(map);
			}else if("90".equals(eventType)){
				// 查询Cherry用户登录密码事件
				customerList = getReceiverEmployee(eventId, map);
			}else if("99".equals(eventType)){
				// 发送测试信息触发事件
				customerList = binBECTCOM01.getReceiverCode(eventId, map);
			}else if("100".equals(eventType)){
				// 系统触发事件
				customerList = binBECTCOM01.getReceiverCode(eventId, map);
			}else if ("20".equals(eventType)){
				// 推荐会员奖励积分短信
				customerList = binBECTSMG05_Service.getReferrerMemberInfoByMemberId(map);
			}else{
				// 不支持的触发事件类型
				customerList = binBECTSMG05_Service.getMemInfoByMobile(map);
			}
			// 对加密的会员信息进行解密
			customerList = binBECTCOM01.getCustomerDecrypt(brandCode, customerList);
		}catch(Exception e){
			customerList = null;
			// 记录错误日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00074");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(eventId);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		return customerList;
	}
	
	// 获取沟通对象信息
	public int getCustomerCount(Map<String, Object> map) throws CherryBatchException{
		int customerNum = 0;
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		try{
			if("1".equals(eventType)){
				// 客户参与活动预约触发事件
				map.put("orderState", "RV");
				customerNum = binBECTSMG05_Service.getActivityCustomerCount(map);
			}else if("2".equals(eventType)){
				// 活动发货触发事件
				map.put("orderState", "AR");
				customerNum = binBECTSMG05_Service.getActivityCustomerCount(map);
			}else if("3".equals(eventType)){
				// 活动柜台收货触发事件
				map.put("orderState", "AR");
				customerNum = binBECTSMG05_Service.getActivityCustomerCount(map);
			}else if("4".equals(eventType)){
				// 客户取消活动单据触发事件
				map.put("orderState", "CA");
				customerNum = binBECTSMG05_Service.getActivityCustomerCount(map);
			}else if("5".equals(eventType)){
				// 会员入会触发事件
				customerNum = binBECTSMG05_Service.getJoinMemberInfoCount(map);
			}else if("6".equals(eventType)){
				// 会员转柜触发事件
				customerNum = binBECTSMG05_Service.getChangeCounterMemberCount(map);
			}else if(CherryUtil.string2int(eventType) >= 70 && CherryUtil.string2int(eventType) < 80){
				// 会员积分变化事件
				customerNum = 1;
			}else if("8".equals(eventType)){
				// 会员信息变更事件
				customerNum = binBECTSMG05_Service.getChangeInfoMemberCount(map);
			}else if("9".equals(eventType)){
				// 短信重发事件
				customerNum = 1;
			}else if("10".equals(eventType)){
				// 发送信息触发事件
				List<Map<String, Object>> customerList = binBECTCOM01.getReceiverCode(eventId, map);
				customerNum = customerList.size();
			}else if("11".equals(eventType)){
				// 登录验证触发事件
				List<Map<String, Object>> customerList = binBECTCOM01.getReceiverCode(eventId, map);
				customerNum = customerList.size();
			}else if("12".equals(eventType)){
				// 会员升级事件
				customerNum = binBECTSMG05_Service.getLevelUpMemberInfoCount(map);
			}else if("13".equals(eventType)){
				// 会员信息查询（会员密码查询）
				List<Map<String, Object>> customerList = binBECTCOM01.getReceiverCode(eventId, map);
				customerNum = customerList.size();
			}else if("14".equals(eventType)){
				// WebService发送验证码事件
				List<Map<String, Object>> customerList = binBECTCOM01.getReceiverCode(eventId, map);
				customerNum = customerList.size();
			}else if("15".equals(eventType)){
				// 生日礼
				customerNum = 1;
			}else if("90".equals(eventType)){
				// 查询Cherry用户登录密码事件
				customerNum = 1;
			}else if("99".equals(eventType)){
				// 发送测试信息触发事件
				List<Map<String, Object>> customerList = binBECTCOM01.getReceiverCode(eventId, map);
				customerNum = customerList.size();
			}else if("100".equals(eventType)){
				// 系统触发事件
				List<Map<String, Object>> customerList = binBECTCOM01.getReceiverCode(eventId, map);
				customerNum = customerList.size();
			}else if("20".equals(eventType)){
				//推荐会员积分短信
				customerNum = 1;
			}else{
				// 不支持的触发事件类型
				customerNum = 0;
			}
		}catch(Exception e){
			customerNum = 0;
			// 记录错误日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00074");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(eventId);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		return customerNum;
	}
	
	// 根据事件类型定义发送短信属于营销类、验证类或系统维护类，若有需要调整短信类型的情况在此处理
	public String getSmsTypeByEvent(String hisPlan, String eventType){
		// 短信类型：1.营销类，2.验证类，3.系统维护类
		String smsType = "1";
		if("1".equals(eventType)){
			// 客户参与活动预约触发事件
			smsType = "1";
		}else if("2".equals(eventType)){
			// 活动发货触发事件
			smsType = "1";
		}else if("3".equals(eventType)){
			// 活动柜台收货触发事件
			smsType = "1";
		}else if("4".equals(eventType)){
			// 客户取消活动单据触发事件
			smsType = "1";
		}else if("5".equals(eventType)){
			// 会员入会触发事件
			smsType = "1";
		}else if("6".equals(eventType)){
			// 会员转柜触发事件
			smsType = "1";
		}else if(CherryUtil.string2int(eventType) >= 70 && CherryUtil.string2int(eventType) < 80){
			// 会员积分变化事件
			smsType = "1";
		}else if("8".equals(eventType)){
			// 会员信息变更事件
			smsType = "2";
		}else if("9".equals(eventType)){
//			// 短信重发事件
//			if("1".equals(hisPlan) || "4".equals(hisPlan) || "5".equals(hisPlan) || "8".equals(hisPlan) || "11".equals(hisPlan)
//					|| "13".equals(hisPlan) || "14".equals(hisPlan) || "90".equals(hisPlan) || "99".equals(hisPlan)){
//				smsType = "2";
//			}else if("100".equals(hisPlan)){
//				smsType = "3";
//			}else{
//				smsType = "1";
//			}
			if("8".equals(hisPlan) || "11".equals(hisPlan) || "13".equals(hisPlan) 
					|| "14".equals(hisPlan) || "90".equals(hisPlan) || "99".equals(hisPlan)){
				smsType = "2";
			}else if("100".equals(hisPlan)){
				smsType = "3";
			}else{
				smsType = "1";
			}
		}else if("10".equals(eventType)){
			// 发送信息触发事件
			smsType = "1";
		}else if("11".equals(eventType)){
			// 登录验证触发事件
			smsType = "2";
		}else if("12".equals(eventType)){
			// 会员升级事件
			smsType = "1";
		}else if("13".equals(eventType)){
			// 会员信息查询（会员密码查询）
			smsType = "2";
		}else if("14".equals(eventType)){
			// WebService发送验证码事件
			smsType = "2";
		}else if("90".equals(eventType)){
			// 查询Cherry用户登录密码事件
			smsType = "2";
		}else if("99".equals(eventType)){
			// 发送测试信息触发事件
			smsType = "2";
		}else if("100".equals(eventType)){
			// 系统触发事件
			smsType = "3";
		}else{
			// 不支持的触发事件类型
			smsType = "1";
		}
		return smsType;
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkSendConditionFlag(Map<String, Object> map) throws CherryBatchException{
		String recordType = ConvertUtil.getString(map.get("recordType"));
		String customerType = ConvertUtil.getString(map.get("customerType"));
		String conditionInfo = ConvertUtil.getString(map.get("conditionInfo"));
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		String memberInfoId = ConvertUtil.getString(map.get("memberInfoId"));
		String referDate = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN);
		boolean checkResult = true;
		try{
//			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
//			batchLoggerDTO1.setCode("ECT00087");
//			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
//			batchLoggerDTO1.addParam(eventType);
//			batchLoggerDTO1.addParam(eventId);
//			batchLoggerDTO1.addParam(memberInfoId);
//			CherryBatchLogger cherryBatchLogger1 = new CherryBatchLogger(this.getClass());
//			cherryBatchLogger1.BatchLogger(batchLoggerDTO1);
			
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.putAll(map);
			conditionMap.remove("conditionInfo");
			conditionMap.remove("recordName");
			// 判断搜索记录类型
			if("1".equals(recordType)){
				// 搜索记录类型为搜索条件时
				if("1".equals(customerType)){
					Map<String, Object> argMap = new HashMap<String, Object>();
					if(!"ALL".equals(conditionInfo) && !"ALL_MEMBER".equals(conditionInfo)){
						if(conditionInfo!=null && !"".equals(conditionInfo)){
							argMap = CherryUtil.json2Map(conditionInfo);
						}
					}
					argMap.putAll(conditionMap);
					argMap.put("SORT_ID", "memId");
					argMap.put("resultMode", "0");
					argMap.put("privilegeFlag", "0");
					argMap.put("referDate", referDate);
					Map<String, Object> resultMap = binOLCM33_BL.searchMemList(argMap);
					if(resultMap != null && !resultMap.isEmpty()){
						int memberCount = CherryUtil.obj2int(resultMap.get("total"));
						if(memberCount > 0){
							checkResult = true;
						}else{
							checkResult = false;
						}
					}else{
						checkResult = false;
					}
				}else if("2".equals(customerType)){
					// 搜索记录对应的客户类型为非会员时（暂不支持）
					checkResult = true;
				}else if("3".equals(customerType)){
					// 搜索记录对应的客户类型为员工时（暂不支持）
					checkResult = true;
				}else if("4".equals(customerType)){
					// 搜索记录对应的客户类型为不限时（暂不支持）
					checkResult = true;
				}else{
					checkResult = true;
				}
			}else{
				// 搜索记录类型为搜索结果时
				int resultCount = binBECTSMG05_Service.getSearchResultCount(conditionMap);
				if(resultCount > 0){
					checkResult = true;
				}else{
					checkResult = false;
				}
			}
//			BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
//			batchLoggerDTO2.setCode("ECT00088");
//			batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
//			batchLoggerDTO2.addParam(eventType);
//			batchLoggerDTO2.addParam(eventId);
//			batchLoggerDTO2.addParam(memberInfoId);
//			CherryBatchLogger cherryBatchLogger2 = new CherryBatchLogger(this.getClass());
//			cherryBatchLogger2.BatchLogger(batchLoggerDTO2);
		}catch(Exception ex){
			// 记录错误日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00086");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(eventType);
			batchLoggerDTO.addParam(eventId);
			batchLoggerDTO.addParam(memberInfoId);
			batchLoggerDTO.addParam(ConvertUtil.getString(ex));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			checkResult = false;
		}
		return checkResult;
	}
	
	// 计算两个时间间隔的秒数
	public int getIntervalDays(Date startday,Date endday){
		Calendar cal1=Calendar.getInstance();
		Calendar cal2=Calendar.getInstance();
		cal1.setTime(startday);
		cal2.setTime(endday);
		
        if(cal1.after(cal2)){
             Calendar cal=cal1;
             cal1=cal2;
             cal2=cal;
        }    
        long sl=cal1.getTimeInMillis();
        long el=cal2.getTimeInMillis();
        
        long ei=el-sl;           
        return (int)(ei/1000);
	}
	
	public List<Map<String, Object>> getReceiverEmployee(String receiveCode, Map<String, Object> map) throws Exception{
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String mobileRule = ConvertUtil.getString(map.get("mobileRule"));
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String eventId = ConvertUtil.getString(map.get("eventId"));
		// 定义号码列表用于存放解析出来的号码
		List<Map<String, Object>> codeList = new ArrayList<Map<String,Object>>();
		// 判断取到的号码是否为空
		if(!"".equals(receiveCode)){
			// 定义是否可以获取到员工的标识
			boolean getEmployeeFlag = true;
			// 查找员工信息是否存在
			if("90".equals(eventType)){
				try{
					if (!CherryChecker.isNullOrEmpty(eventId, true)) {
						map.put("eventId", CherryBatchSecret.encryptData(brandCode, eventId));
					}
				}catch(Exception e){}
				// 查询Cherry用户登录密码事件
				Map<String, Object> employeeInfo = binBECTSMG05_Service.getUserInfoByMobileList(map);
				// 判断是否获取到了会员信息，若获取到了则返回获取到的会员信息
				if(employeeInfo != null && !employeeInfo.isEmpty()){
					if(CherryChecker.isPhoneValid(receiveCode, mobileRule)){
						employeeInfo.put("mobilePhone", receiveCode);
					}
					codeList.add(employeeInfo);
					getEmployeeFlag = true;
				}else{
					getEmployeeFlag = false;
				}
			}else{
				getEmployeeFlag = false;
			}
			Map<String, Object> codeMap = new HashMap<String, Object>();
			// 判断是否获取到了会员信息，若未获取到会员信息则由系统组织客户信息
			if(getEmployeeFlag == false){
				// 判断接收号码是手机号码还是邮箱地址，根据号码类型组织客户信息
				if(CherryChecker.isPhoneValid(receiveCode, mobileRule)){
					codeMap.put("memId", receiveCode);
					codeMap.put("mobilePhone", receiveCode);
					codeMap.put("email", "");
					codeMap.put("receiveMsgFlg", "1");
					codeMap.put("customerType", "4");
					codeList.add(codeMap);
				}else if(CherryChecker.isEmail(receiveCode)){
					String memId = receiveCode.substring(0,receiveCode.indexOf("@"));
					codeMap.put("memId", memId);
					codeMap.put("mobilePhone", "");
					codeMap.put("email", receiveCode);
					codeMap.put("receiveMsgFlg", "1");
					codeMap.put("customerType", "4");
					codeList.add(codeMap);
				}
			}
		}
		return codeList;
	}
	
	// 新增记录共通参数
		public Map<String, Object> getComParam(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createBy", "SMGJOB");
		paramMap.put("createPGM", "BINBECTSMG05");
		paramMap.put("updateBy", "SMGJOB");
		paramMap.put("updatePGM", "BINBECTSMG05");
		paramMap.put("modifyCount", "0");
		return paramMap;
	}
}

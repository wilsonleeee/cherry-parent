package com.cherry.ct.smg.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.BINBECTCOM01;
import com.cherry.ct.smg.core.NeedTempSenderFactory;
import com.cherry.ct.smg.interfaces.BINBECTSMG09_IF;
import com.cherry.ct.smg.interfaces.BINBECTSMG10_IF;
import com.cherry.ct.smg.service.BINBECTSMG03_Service;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.googlecode.jsonplugin.JSONUtil;

public class BINBECTSMG03_BL implements CherryMessageHandler_IF {
	@Resource(name = "binBECTSMG03_Service")
	private BINBECTSMG03_Service binBECTSMG03_Service;
	
	@Resource(name = "binBECTSMG07_BL")
	private BINBECTSMG07_BL binBECTSMG07_BL;
	
	@Resource(name = "binBECTCOM01")
	private BINBECTCOM01 binBECTCOM01;
	
	/** 各类编号取号共通BL */
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name = "binBECTSMG09_BL")
	private BINBECTSMG09_IF binBECTSMG09_BL;
	
	@Resource(name = "binBECTSMG10_BL")
	private BINBECTSMG10_IF binBECTSMG10_BL;
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINBECTSMG03_BL.class);
	
	// 通过MQ处理短信发送
	@Override
	@SuppressWarnings("unchecked")
	public void handleMessage(Map<String, Object> map) throws Exception {
		Transaction transaction = Cat.newTransaction("message","BINBECTSMG03_BL");

		try{
			if(!CherryChecker.isNullOrEmpty(map.get("memberMap"), true)){
				String messageContents = ConvertUtil.getString(map.get("messageContents"));
				// 获取沟通对象参数
				Map<String, Object> customerMap = (Map<String,Object>)map.get("memberMap");
				// 获取接口配置参数
				Map<String, Object> ifConfigMap = new HashMap<String, Object>();
				if(!CherryChecker.isNullOrEmpty(map.get("ifConfigMap"), true)){
					ifConfigMap = (Map<String,Object>)map.get("ifConfigMap");
				}
				// 获取参与计算的变量参数
				List<Map<String, Object>> variableList = new ArrayList<Map<String, Object>>();
				if(!CherryChecker.isNullOrEmpty(map.get("variableList"), true)){
					variableList = (List<Map<String,Object>>)map.get("variableList");
				}
				tran_sendSms(messageContents, customerMap, variableList, ifConfigMap);
			}else{
				// 记录Batch日志
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00093");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
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
	
	// 沟通短信发送
	public String tran_sendSms(String messageContents, Map<String, Object> map, List<Map<String, Object>> variableList) throws Exception{
		return tran_sendSms(messageContents, map, variableList, null);
	}
	
	// 沟通短信发送
	public String tran_sendSms(String messageContents, Map<String, Object> map, List<Map<String, Object>> variableList, Map<String, Object> ifConfigMap) throws Exception{
		String receiveMsgFlg = ConvertUtil.getString(map.get("receiveMsgFlg"));
		String runInterface = ConvertUtil.getString(map.get("runInterface"));
		String memId = ConvertUtil.getString(map.get("memId"));
		String mobilePhone = ConvertUtil.getString(map.get("mobilePhone")).trim();
		String mobileRule = ConvertUtil.getString(map.get("mobileRule"));
		String replyFlag = ConvertUtil.getString(map.get("replyFlag"));
		String smsType=ConvertUtil.getString(map.get("smsType"));
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 供应商代码
		String commInterface = ConvertUtil.getString(map.get("commInterface"));
		// 判断客户是否接收短信
		if(!"0".equals(receiveMsgFlg)){
			// 接收短信的情况
			// 判断手机号码是否合法
			if(CherryChecker.isPhoneValid(mobilePhone, mobileRule)){
				// 生成信息编号
				String messageCode = binOLCM03_BL.getTicketNumber(CherryUtil.obj2int(map
						.get("organizationInfoId")), CherryUtil.obj2int(map.get("brandInfoId")), "SMGJOB", CherryBatchConstants.BILLTYPE_SM);
				boolean sendFlag = binBECTCOM01.checkRepeatSend(map, messageCode);
				// 短信模板对应的参数
				String params = null;
				// 短信签名
				String signName = null;
				// 短信模板
				String templateCode = null;
				if(sendFlag == true){
					String message = null;
					// 是否注册
					boolean isRegister = !"1".equals(smsType) && NeedTempSenderFactory.isRegister(commInterface);
					if (isRegister) {
						// 需要替换模板内容
						if (messageContents.indexOf("<#") > 0) {
							Map<String, Object> paramsInfo = new HashMap<String, Object>();
							message = binBECTCOM01.replaceTemplate(messageContents, map, variableList, paramsInfo);
							if (null != paramsInfo && !paramsInfo.isEmpty()) {
								try {
									params = JSONUtil.serialize(paramsInfo);
								} catch (Exception e) {
									message = null;
									logger.error("模板参数转换JSON异常:" + e.getMessage(),e);
								}
							}
							if (null != message) {
								// 取得模板编号
								templateCode = binBECTSMG10_BL.getTemplateCode(brandCode, messageContents);
								if (CherryChecker.isNullOrEmpty(templateCode)) {
									message = null;
									logger.error("短信模板不能为空！");
								}
							}
						} else {
							message = messageContents;
							// 通过短信内容反推模板编号及变量值
							Map<String, Object> templateInfo = binBECTSMG10_BL.getTemplateInfo(brandCode, messageContents);
							if (null == templateInfo || templateInfo.isEmpty()) {
								message = null;
								logger.error("通过短信内容反推模板编号及变量值失败！");
							} else {
								// 取得模板编号
								templateCode = (String) templateInfo.get("tempCode");
								Map<String, Object> paramsInfo = (Map<String, Object>) templateInfo.get("paramsInfo");
								if (null != paramsInfo && !paramsInfo.isEmpty()) {
									try {
										params = JSONUtil.serialize(paramsInfo);
									} catch (Exception e) {
										message = null;
										logger.error("模板参数(通过反推获取)转换JSON异常:" + e.getMessage(),e);
									}
								}
							}
						}
						if (null != message) {
							// 取得品牌的短信签名
							signName = binBECTSMG09_BL.getBrandSignName(brandCode);
							if (CherryChecker.isNullOrEmpty(signName)) {
								message = null;
								logger.error("短信签名不能为空！");
							}
						}
					} else {
						message = binBECTCOM01.replaceTemplate(messageContents, map, variableList);
					}
					String sendTime = ConvertUtil.getString(binBECTSMG03_Service.getSYSDate());
					if(null == sendTime || "".equals(sendTime)){
						sendTime = CherryUtil.getSysDateTime(CherryBatchConstants.DF_TIME_PATTERN);
					}
					if(null != message && !"".equals(message)){
						String messageGroupCode = "";
						// 判断短信是否回复短信，若为回复短信则获取回复短信的信息编号作为组编号，不是回复短信直接使用信息编号作为组编号
						if("Y".equals(replyFlag)){
							messageGroupCode = ConvertUtil.getString(map.get("messageCodePre"));
						}else{
							messageGroupCode = messageCode;
						}
//						// 获取Cherry登陆密码失败的情况下发送默认短信
//						if("NMP".equals(message)){
//							message = CherryBatchConstants.NMP_DEFAULT_MESSAGE;
//						}else if("NUP".equals(message)){
//							message = CherryBatchConstants.NUP_DEFAULT_MESSAGE;
//						}
						// 定义并添加发送参数
						Map<String, Object> sendMap = new HashMap<String, Object>();
						sendMap.put("messageCode", messageCode);
						sendMap.put("messageGroupCode", messageGroupCode);
						sendMap.put("customerSysId", memId);
						sendMap.put("message", message);
						sendMap.put("commCode", messageCode);
						sendMap.put("receiverCode", mobilePhone);
						sendMap.put("verifyCode", "");
						sendMap.put("sendPriority", "1");
						sendMap.put("sendTime", sendTime);
						sendMap.put("smsType", smsType);
						sendMap.putAll(map);
						sendMap.putAll(getComParam());
						
						// 向接口数据库写入数据
						boolean insertIfFlag = true;
						// 判断是否可以使用实时接口发送信息，根据参数确定使用的接口
						if("NI".equals(runInterface)){
							if(ifConfigMap != null && !ifConfigMap.isEmpty() || isRegister) {
								String returnStatus = "";
								String sendStatus = null;
								if (isRegister) {
									sendStatus = binBECTSMG07_BL.SmsSend(commInterface, mobilePhone, signName, templateCode, params);
								} else {
									sendStatus = binBECTSMG07_BL.SmsSend(sendMap, ifConfigMap);
								}
								if(!"0".equals(sendStatus)){
									insertIfFlag = false;
									if(!binBECTCOM01.isNumeric(sendStatus)){
										returnStatus = CherryBatchConstants.IFFLAG_IFSENDMSGERROR;
									}
								}
								sendMap.put("sendStatus", sendStatus);
								sendMap.put("resultStatus", returnStatus);
							}else{
								insertIfFlag = insertMsgtoSmsInterface(sendMap);
								sendMap.put("sendStatus", "0");
								sendMap.put("resultStatus", "0");
							}
						}else{
							insertIfFlag = insertMsgtoSmsInterface(sendMap);
							sendMap.put("sendStatus", "0");
							sendMap.put("resultStatus", "0");
						}
						// 添加客户信息发送日志
						binBECTSMG03_Service.addCommunicationLog(sendMap);
						// 判断信息是否成功推送至接口
						if(insertIfFlag){
							// 向信息发送明细表写入数据
							binBECTSMG03_Service.addSmsSendDetail(sendMap);
							// 在沟通计划关联活动的情况下， 若Coupon号由沟通程序生成则将生成的Coupon号写入Coupon生成记录表
							if("Y".equals(ConvertUtil.getString(map.get("createCouponFlag")))){
								binBECTSMG03_Service.addCouponCreateLog(sendMap);
							}
							sendMap = null;
							map = null;
							return CherryBatchConstants.SENDSTATUS_SUCCESS;
						}else{
							// 推送接口失败的情况
							String[] errorCode = new String[1];
							errorCode[0] = ConvertUtil.getString(sendMap.get("sendStatus"));
							String errMsg = PropertiesUtil.getMessage("ECT00083", errorCode);
							if("".equals(message)){
								message = messageContents;
							}
							Map<String, Object> errorLogMap = new HashMap<String, Object>();
							errorLogMap.put("customerSysId", memId);
							errorLogMap.put("receiverCode", mobilePhone);
							errorLogMap.put("sendTime", sendTime);
							errorLogMap.put("message", message);
							errorLogMap.put("errorType", "4");
							errorLogMap.put("errorText", errMsg);
							errorLogMap.putAll(map);
							errorLogMap.putAll(getComParam());
							binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
							errorLogMap = null;
							map = null;
							return CherryBatchConstants.SENDSTATUS_ERROR;
						}
					}else{
						// 短信替换内容不全
						String errMsg = PropertiesUtil.getMessage("ECT00079", null);
						Map<String, Object> errorLogMap = new HashMap<String, Object>();
						errorLogMap.put("customerSysId", memId);
						errorLogMap.put("receiverCode", mobilePhone);
						errorLogMap.put("sendTime", sendTime);
						errorLogMap.put("message", messageContents);
						errorLogMap.put("errorType", "3");
						errorLogMap.put("errorText", errMsg);
						errorLogMap.putAll(map);
						errorLogMap.putAll(getComParam());
						binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
						errorLogMap = null;
						map = null;
						return CherryBatchConstants.SENDSTATUS_NOTCREATEMSG;
					}
				}else{
					// 已经发送过短消息，防止重复发送的情况
					map = null;
					return CherryBatchConstants.SENDSTATUS_REPEAT;
				}
			}else{
				// 接收号码不合法的情况
				String message = binBECTCOM01.replaceTemplate(messageContents, map, variableList);
				String sendTime = binBECTSMG03_Service.getSYSDate();
				String errMsg = PropertiesUtil.getMessage("ECT00080", null);
				if("".equals(message)){
					message = messageContents;
				}
				Map<String, Object> errorLogMap = new HashMap<String, Object>();
				errorLogMap.put("customerSysId", memId);
				errorLogMap.put("receiverCode", mobilePhone);
				errorLogMap.put("sendTime", sendTime);
				errorLogMap.put("message", message);
				errorLogMap.put("errorType", "2");
				errorLogMap.put("errorText", errMsg);
				errorLogMap.putAll(map);
				errorLogMap.putAll(getComParam());
				binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
				errorLogMap = null;
				map = null;
				return CherryBatchConstants.SENDSTATUS_CODEILLEGAL;
			}
		}else{
			// 不接收短信的情况
			String message = binBECTCOM01.replaceTemplate(messageContents, map, variableList);
			String sendTime = binBECTSMG03_Service.getSYSDate();
			String errMsg = PropertiesUtil.getMessage("ECT00081", null);
			if("".equals(message)){
				message = messageContents;
			}
			Map<String, Object> errorLogMap = new HashMap<String, Object>();
			errorLogMap.put("customerSysId", memId);
			errorLogMap.put("receiverCode", mobilePhone);
			errorLogMap.put("sendTime", sendTime);
			errorLogMap.put("message", message);
			errorLogMap.put("errorType", "1");
			errorLogMap.put("errorText", errMsg);
			errorLogMap.putAll(map);
			errorLogMap.putAll(getComParam());
			binBECTSMG03_Service.addErrorMsgLog(errorLogMap);
			errorLogMap = null;
			map = null;
			return CherryBatchConstants.SENDSTATUS_NOTRECEIVE;
		}
	}
	
	// 将数据写入接口
	public boolean insertMsgtoSmsInterface(Map<String, Object> sendMap){
		String sendMsgToIF = PropertiesUtil.pps.getProperty("communication.SendMsgToIntelface");
		if("N".equals(sendMsgToIF)){
			return true;
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("brandCode", ConvertUtil.getString(sendMap.get("brandCode")));
			String smsSendTable = binBECTSMG03_Service.getSmsSendTable(map);
			if(null!=smsSendTable && !"".equals(smsSendTable)){
				sendMap.put("smsSendTable",smsSendTable);
				binBECTSMG03_Service.addMsgtoSmsInterface(sendMap);
				return true;
			}else{
				return false;
			}
		}
	}
	
	// 新增记录共通参数
	public Map<String, Object> getComParam(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createBy", "SMGJOB");
		paramMap.put("createPGM", "BINBECTSMG03");
		paramMap.put("updateBy", "SMGJOB");
		paramMap.put("updatePGM", "BINBECTSMG03");
		paramMap.put("modifyCount", "0");
		return paramMap;
	}
}

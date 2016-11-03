package com.cherry.ct.smg.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.WmgwSoapProxy;

import cn.b2m.eucp.sdkhttp.SDKClientProxy;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ct.common.BINBECTCOM02;
import com.cherry.ct.smg.core.NeedTempSenderFactory;
import com.cherry.ct.smg.dto.SendResult;
import com.cherry.ct.smg.interfaces.BINBECTSMG07_IF;
import com.cherry.ct.smg.interfaces.SmsNeedTempCodeSender;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 沟通实时接口
 * 用于实现与第三方供应商的短信、邮件、电话等接口的调用
 * 
 * @author ZhangGS
 * @version 1.0 2013/11/21	
 */
public class BINBECTSMG07_BL implements BINBECTSMG07_IF{
	
	@Resource(name = "binBECTCOM02")
	private BINBECTCOM02 binBECTCOM02;
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINBECTSMG07_BL.class);
	
	// 信息发送
	public String SmsSend(String senderCode, String phones, String signName, String templateCode, String params) throws Exception{
		String runResult = null;
		try{
			// 创建一个短信发送者对象
			SmsNeedTempCodeSender sender = NeedTempSenderFactory.createSender(senderCode);
			// 发送短信
			SendResult sendResult = sender.smsSend(phones, signName, templateCode, params);
			runResult = sendResult.getResultCode();
			// 发送失败的情况
			if (!CherryBatchConstants.IFFLAG_SUCCESS.equals(runResult)) {
				errorLog(phones, templateCode, sendResult.getErrMsg());
			}
		} catch(Exception e) {
			// 记录Batch日志
			errorLog(phones, templateCode, e.getMessage());
			runResult = CherryBatchConstants.IFFLAG_SETPARAMERROR;
		}
		return runResult;
	}
	
	// 打印错误信息
	private void errorLog(String phones, String templateCode, String errorMsg) {
		StringBuilder builder = new StringBuilder();
		logger.error(builder.append("短信发送失败！手机号码：").append(phones).append(" 模板编号：")
				.append(templateCode).append(" 错误信息：").append(errorMsg).toString());
	}
	
	// 信息发送
	public String SmsSend(Map<String, Object> map, Map<String, Object> ifConfigMap) throws Exception{
		String runResult = "";
		try{
			runResult = setInterfaceParam(map, ifConfigMap, CherryBatchConstants.SMSIF_SEND);
		}catch(Exception e){
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			runResult = CherryBatchConstants.IFFLAG_SETPARAMERROR;
		}
		return runResult;
	}	
	
	// 信息接收
	public boolean SmsReceive(Map<String, Object> map, Map<String, Object> ifConfigMap){
		
		return true;
	}
	
	// 获取发送状态报告
	public boolean SmsGetSendRpt(Map<String, Object> map, Map<String, Object> ifConfigMap){
		
		return true;
	}
	
	public String setInterfaceParam(Map<String, Object> map, Map<String, Object> ifConfigMap, String type) throws Exception{
		String errorNum = "0";
		// 接口配置参数
		String interfaceType = ConvertUtil.getString(ifConfigMap.get("interfaceType"));
		String enable = ConvertUtil.getString(ifConfigMap.get("enable"));
		String serviceUrl = ConvertUtil.getString(ifConfigMap.get("serviceUrl"));
		String sendInterface = ConvertUtil.getString(ifConfigMap.get("sendInterface"));
		String receiveInterface = ConvertUtil.getString(ifConfigMap.get("receiveInterface"));
		String receiveRptInterface = ConvertUtil.getString(ifConfigMap.get("receiveRptInterface"));
		// 用户名密码参数
		String userIdKey = ConvertUtil.getString(ifConfigMap.get("userIdKey"));
		String userIdValue = ConvertUtil.getString(ifConfigMap.get("userIdValue"));
		String passwordKey = ConvertUtil.getString(ifConfigMap.get("passwordKey"));
		String passwordValue = ConvertUtil.getString(ifConfigMap.get("passwordValue"));
		//首易相关参数
		String longSmsKey = ConvertUtil.getString(ifConfigMap.get("longSmsKey"));
		String longSmsValue = ConvertUtil.getString(ifConfigMap.get("longSmsValue"));
		String addNumKey=ConvertUtil.getString(ifConfigMap.get("addNumKey"));
		String addNumValue=ConvertUtil.getString(ifConfigMap.get("addNumValue"));
		// 基本参数
		String timerKey=ConvertUtil.getString(ifConfigMap.get("timerKey"));
		String timerValue=ConvertUtil.getString(ifConfigMap.get("timerValue"));
		String batchIdKey = ConvertUtil.getString(ifConfigMap.get("batchIdKey"));
		String sendTimeKey = ConvertUtil.getString(ifConfigMap.get("sendTimeKey"));
		String receiveCodeKey = ConvertUtil.getString(ifConfigMap.get("receiveCodeKey"));
		String messageKey = ConvertUtil.getString(ifConfigMap.get("messageKey"));
		String numCountKey = ConvertUtil.getString(ifConfigMap.get("numCountKey"));
		String taskTypeKey = ConvertUtil.getString(ifConfigMap.get("taskTypeKey"));
		String taskTypeValue = ConvertUtil.getString(ifConfigMap.get("taskTypeValue"));
		String companyIdKey = ConvertUtil.getString(ifConfigMap.get("companyIdKey"));
		String companyIdValue = ConvertUtil.getString(ifConfigMap.get("companyIdValue"));
		// 备用参数
		String otherParamKey = ConvertUtil.getString(ifConfigMap.get("otherParamKey"));
		String otherParamValue = ConvertUtil.getString(ifConfigMap.get("otherParamValue"));
		
		// 电话相关参数
		String enpKey = ConvertUtil.getString(ifConfigMap.get("enpKey"));
		String enpValue = ConvertUtil.getString(ifConfigMap.get("enpValue"));
		String ifTypeKey = ConvertUtil.getString(ifConfigMap.get("ifTypeKey"));
		String ifTypeValue = ConvertUtil.getString(ifConfigMap.get("ifTypeValue"));
		String paramNameKey = ConvertUtil.getString(ifConfigMap.get("paramNameKey"));
		String paramNameValue = ConvertUtil.getString(ifConfigMap.get("paramNameValue"));
		String paramTypeKey = ConvertUtil.getString(ifConfigMap.get("paramTypeKey"));
		String paramTypeValue = ConvertUtil.getString(ifConfigMap.get("paramTypeValue"));
		String ivrIdKey = ConvertUtil.getString(ifConfigMap.get("ivrIdKey"));
		String ivrIdValue = ConvertUtil.getString(ifConfigMap.get("ivrIdValue"));
		String syncKey = ConvertUtil.getString(ifConfigMap.get("syncKey"));
		String syncValue = ConvertUtil.getString(ifConfigMap.get("syncValue"));
		
		// 定义接口完整地址
		String interfaceUrl = "";
		String IFFlag = "";
		try{
			errorNum = "1";
			// 获取发送信息参数
			String batchIdValue = ConvertUtil.getString(map.get("batchId"));
			String sendTimeValue = ConvertUtil.getString(map.get("sendTime"));
			String messageValue = ConvertUtil.getString(map.get("message"));
			String receiveCodeValue = ConvertUtil.getString(map.get("receiverCode"));
			String smsInterface = ConvertUtil.getString(map.get("commInterface"));
			String resCodeText = receiveCodeValue;
			errorNum = "2";
			if(!"".equals(resCodeText)){
				resCodeText = resCodeText.replace("；", ";");
				resCodeText = resCodeText.replace("，", ";");
				resCodeText = resCodeText.replace(",", ";");
				errorNum = "3";
				String[] resCodes = resCodeText.split(";");
				errorNum = "4";
				int resCount = resCodes.length;
				if(resCount > 0){
					errorNum = "5";
					String numCountValue = ConvertUtil.getString(resCount);
					try{
						// 发送时间减去两个小时防止由于服务器时间不一致导致不能及时发送信息
						if(!"".equals(sendTimeValue)){
							sendTimeValue = DateUtil.addDateByHours(CherryBatchConstants.DF_TIME_PATTERN, sendTimeValue, -2);
						}else{
							sendTimeValue = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN) + " " + CherryBatchConstants.STARTTIMEOFDAY;
						}
					}catch(Exception ep){
						sendTimeValue = CherryUtil.getSysDateTime(CherryBatchConstants.DF_DATE_PATTERN) + " " + CherryBatchConstants.STARTTIMEOFDAY;
					}
					errorNum = "6";
					// 当配置项为接口类型时，判断参数值为HTTP接口还是WebService接口
					if(interfaceType.equals(CherryBatchConstants.SMSIF_TYPE_HTTP)){
						errorNum = "7";
						if(type.equals(CherryBatchConstants.SMSIF_SEND)){
							// 接口为HTTP接口时将消息内容进行UTF8编码
							messageValue = binBECTCOM02.toUtf8String(messageValue);
						}
						errorNum = "8";
						// 判断服务地址配置是否存在
						if(!"".equals(serviceUrl)){
							// 获取到服务地址的情况
							if(type.equals(CherryBatchConstants.PHONEIF_CALL)){
								// 当接口配置值不为空的情况
								sendInterface = serviceUrl;
							}else{
								errorNum = "9";
								// 当配置项为接口开放功能时
								if(enable.equals(CherryBatchConstants.SMSIF_ENABLE_ALLCLOSE)){
									// 全部禁止的情况
									sendInterface = "";
									receiveInterface = "";
									// 记录Batch日志
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00102");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00204", null));
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
									IFFlag = CherryBatchConstants.IFFLAG_CONFIGDISABLED;
								}else if(enable.equals(CherryBatchConstants.SMSIF_ENABLE_ALLOPEN)){
									// 可发送可接收的情况
									// 当发送接口配置值不为空的情况
									if(!"".equals(sendInterface)){
										sendInterface = serviceUrl + sendInterface;
									}
									// 当接收接口配置值不为空的情况
									if(!"".equals(receiveInterface)){
										receiveInterface = serviceUrl + receiveInterface;
									}
								}else if(enable.equals(CherryBatchConstants.SMSIF_ENABLE_ONLYSEND)){
									// 仅发送的情况
									// 当发送接口配置值不为空的情况
									if(!"".equals(sendInterface)){
										sendInterface = serviceUrl + sendInterface;
									}
								}else if(enable.equals(CherryBatchConstants.SMSIF_ENABLE_ONLYRECEIVE)){
									// 仅接收的情况
									// 当接收接口配置值不为空的情况
									if(!"".equals(receiveInterface)){
										receiveInterface = serviceUrl + receiveInterface;
									}
								}else{
									// 配置项值不合法的情况
									sendInterface = "";
									receiveInterface = "";
									// 记录Batch日志
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00102");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00205", null));
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
									IFFlag = CherryBatchConstants.IFFLAG_CONFIGERROR;
								}
							}
							errorNum = "10";
							if("".equals(IFFlag)){
								if(type.equals(CherryBatchConstants.SMSIF_SEND)){
									// 信息发送接口完整地址
									if(!"".equals(sendInterface)){
										interfaceUrl = sendInterface;
									}
								}else if(type.equals(CherryBatchConstants.SMSIF_RECEIVE)){
									// 信息接收接口完整地址
									if(!"".equals(receiveInterface)){
										interfaceUrl = receiveInterface;
									}
								}else if(type.equals(CherryBatchConstants.SMSIF_RECEIVERPT)){
									// 信息发送状态报告
									if(!"".equals(receiveRptInterface)){
										interfaceUrl = serviceUrl + receiveRptInterface;
									}
								}else if(type.equals(CherryBatchConstants.PHONEIF_CALL)){
									if(!"".equals(sendInterface)){
										interfaceUrl = sendInterface;
									}
								}
								errorNum = "11";
								// 访问WebService的参数设定
								MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
								// 加入用户名参数
								if(!"".equals(userIdKey)){
									queryParams.add(userIdKey, userIdValue);
								}
								// 加入用户密码参数
								if(!"".equals(passwordKey)){
									queryParams.add(passwordKey, passwordValue);
								}
								//定时发送短信
								if(!"".equals(timerKey)){
									queryParams.add(timerKey, timerValue);
								}
								//加入是否发送长短信的配置
								if(!"".equals(longSmsKey)){
									queryParams.add(longSmsKey, longSmsValue);
								}
								//加入扩展号码的短信配置
								if(!"".equals(addNumKey)){
									queryParams.add(addNumKey, addNumValue);
								}
								errorNum = "12";
								
								// 加入信息发送批次号参数
								if(!"".equals(batchIdKey)){
									queryParams.add(batchIdKey, batchIdValue);
								}
								// 加入信息发送时间参数
								if(!"".equals(sendTimeKey)){
									queryParams.add(sendTimeKey, sendTimeValue);
								}
								// 加入信息接收号码参数
								if(!"".equals(receiveCodeKey)){
									queryParams.add(receiveCodeKey, receiveCodeValue);
								}
								// 加入信息内容参数
								if(!"".equals(messageKey)){
									queryParams.add(messageKey, messageValue);
								}
								// 加入信息接收号码数量参数
								if(!"".equals(numCountKey)){
									queryParams.add(numCountKey, numCountValue);
								}
								// 加入任务类型参数
								if(!"".equals(taskTypeKey)){
									queryParams.add(taskTypeKey, taskTypeValue);
								}
								// 加入公司ID参数
								if(!"".equals(companyIdKey)){
									queryParams.add(companyIdKey, companyIdValue);
								}
								// 加入其它参数
								if(!"".equals(otherParamKey)){
									queryParams.add(otherParamKey, otherParamValue);
								}
								errorNum = "13";
								if(!"".equals(enpKey)){
									queryParams.add(enpKey, enpValue);
								}
								if(!"".equals(ifTypeKey)){
									queryParams.add(ifTypeKey, ifTypeValue);
								}
								if(!"".equals(paramNameKey)){
									queryParams.add(paramNameKey, paramNameValue);
								}
								if(!"".equals(paramTypeKey)){
									queryParams.add(paramTypeKey, paramTypeValue);
								}
								if(!"".equals(ivrIdKey)){
									queryParams.add(ivrIdKey, ivrIdValue);
								}
								if(!"".equals(syncKey)){
									queryParams.add(syncKey, syncValue);
								}
								errorNum = "14";
								if(!"".equals(interfaceUrl)){
									if(type.equals(CherryBatchConstants.PHONEIF_CALL)){
										// 重复调用信息发送接口，当返回结果为成功时停止调用
										for(int i=0; i<2; i++){
											// 调用消息发送接口
											IFFlag = phoneWebService(interfaceUrl, queryParams, smsInterface);
											if(!"8000".equals(IFFlag) && !"8007".equals(IFFlag)){
												break;
											}else{
												Thread.currentThread();
												Thread.sleep(2000);
											}
										}
									}else{
										errorNum = "15";
										// 重复调用信息发送接口，当返回结果为成功时停止调用
										for(int i=0; i<2; i++){
											// 调用消息发送接口
											IFFlag = sendMsgWebService(interfaceUrl, queryParams, smsInterface);
											errorNum = "16";
											if(!"8000".equals(IFFlag) && !"8007".equals(IFFlag)){
												break;
											}else{
												Thread.currentThread();
												Thread.sleep(2000);
											}
										}
									}
								}else{
									// 接口地址不完整或者不支持的接口调用
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00102");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00203", null));
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
									IFFlag = CherryBatchConstants.IFFLAG_SERVICEURLINCOMPLETE;
								}
							}
						}else{
							// 未获取到服务地址的情况
							// 记录Batch日志
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("ECT00102");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00201", null));
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							IFFlag = CherryBatchConstants.IFFLAG_NOIFSERVICEURL;
						}
					}else if(interfaceType.equals(CherryBatchConstants.SMSIF_TYPE_WEBSERVICE)){
						// 当配置项为接口开放功能时
						if(enable.equals(CherryBatchConstants.SMSIF_ENABLE_ALLCLOSE)){
							// 全部禁止的情况
							// 记录Batch日志
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("ECT00102");
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00204", null));
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO);
							IFFlag = CherryBatchConstants.IFFLAG_CONFIGDISABLED;
						}else{
							// 调用发送信息接口
							if(type.equals(CherryBatchConstants.SMSIF_SEND)){
								Map<String, Object> paramMap = new HashMap<String, Object>();
								paramMap.put("userId", userIdValue);
								paramMap.put("password", passwordValue);
								paramMap.put("mobile", receiveCodeValue);
								paramMap.put("message", messageValue);
								paramMap.put("mobileCount", numCountValue);
								paramMap.put("subPort", otherParamValue);
								// 重复调用信息发送接口，当返回结果为成功时停止调用
								for(int i=0; i<2; i++){
									// 调用消息发送接口
									IFFlag = sendMsgWebService(paramMap, smsInterface);
									if(!"8000".equals(IFFlag) && !"8007".equals(IFFlag)){
										break;
									}else{
										Thread.currentThread();
										Thread.sleep(2000);
									}
								}
							}else{
								// 接口地址不完整或者不支持的接口调用
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECT00102");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00203", null));
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
								IFFlag = CherryBatchConstants.IFFLAG_SERVICEURLINCOMPLETE;
							}
						}
					}else{
						// 接口类型不支持
						// 记录Batch日志
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECT00102");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00202", null));
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						IFFlag = CherryBatchConstants.IFFLAG_IFTYPEERROR;
					}
				}else{
					// 传入接口调用程序的信息接收号码数量为0
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00102");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00207", null));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					IFFlag = CherryBatchConstants.IFFLAG_NOTGETRECEIVERCODE;
				}
			}else{
				// 未能获取到信息接收号码
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00102");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00206", null));
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				IFFlag = CherryBatchConstants.IFFLAG_NOTGETRECEIVERCODE;
			}
		}catch(Exception ex){
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(ex)+" ER:"+errorNum);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			IFFlag = CherryBatchConstants.IFFLAG_SETPARAMERROR;
		}
		return IFFlag;
	}
	
//	public String sendMsg(String webServiceUrl, MultivaluedMap<String, String> queryParams, String smsInterface) {
//		GetFuture<Object> result = null;
//	    OperationStatus status = null;
//	    int backoffexp = 0;
//	    try {
//	    	do {
//		        if (backoffexp > 3) {
//		        	break;
//		        }
//		        
//		        result = couchbaseClient.asyncGet(id);
//		        status = result.getStatus(); // blocking call, improve if needed
//		        
//		        if (status.isSuccess()) {
//		        	break;
//		        }
//		        if (backoffexp > 0) {
//		        	double backoffMillis = Math.pow(2, backoffexp);
//		        	backoffMillis = Math.min(1000, backoffMillis); // 1 sec max
//		        	Thread.sleep((int) backoffMillis);
//		        	logger.error("Backing off, tries so far: " + backoffexp);
//		        }
//		        backoffexp++;
//
//	    	} while (status.getMessage().equals("Temporary failure"));
//	      
//	    	if (result == null) {
//	    		return null;
//		    }
//	    	
//	    	if (status.isSuccess()) {
//	    		Object obj = result.get();
//		    	if(obj != null) {
//		    		final CouchbaseDocument converted = new CouchbaseDocument(id);
//			    	return couchbaseTemplate.getConverter().read(entityClass, (CouchbaseDocument) translateDecode(String.valueOf(obj), converted));
//		    	}
//		    } else {
//		    	logger.error("Failed with status: " + status.getMessage());
//		    }
//	      
//	    } catch (InterruptedException ex) {
//	    	logger.error("Interrupted while trying to asyncGet.  Exception:" + ex.getMessage());
//	    } catch (ExecutionException e) {
//	    	logger.error(e.getMessage(), e);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	    return null;
//	}
	
	// 调用HTTP接口的情况
	public String sendMsgWebService(String webServiceUrl, MultivaluedMap<String, String> queryParams, String smsInterface) throws Exception {
		String callFlag = "";
		try {
			if(null != webServiceUrl && !"".equals(webServiceUrl)){
				WebResource webResource = binBECTCOM02.getWebResource(webServiceUrl);
				// 访问WebService
				Transaction transaction = Cat.newTransaction("message", "soap");
				String resultMsg = "";
				try {
					logger.info("***调用HTTP接口的情况******打印sendMsgWebService的值为："+queryParams.get("receiveCodeKey"));
					resultMsg = webResource.queryParams(queryParams).get(String.class);
					transaction.setStatus(Transaction.SUCCESS);
				}catch (Throwable e){
					transaction.setStatus(e);
					Cat.logError(e);
					throw e;
				}  finally {
					transaction.complete();
				}
				if(null != resultMsg && !"".equals(resultMsg)){
					// 判断使用的接口供应商
					if(smsInterface.equals(CherryBatchConstants.MCIF_SYSTEMCODE)){
						// 接口为铭传接口时，将返回结果解析成Map
						List<Map<String, Object>> resultListMap = mcXmlElements(resultMsg);
						if(resultListMap != null && !resultListMap.isEmpty()){
							String returnInfo = "";
							for(Map<String,Object> resultMap : resultListMap){
								callFlag = ConvertUtil.getString(resultMap.get("returnValue"));
								returnInfo = ConvertUtil.getString(resultMap.get("info"));
							}
							if(!"0".equals(callFlag)){
								// 记录Batch日志
								try{
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00103");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO.addParam(callFlag);
									batchLoggerDTO.addParam(returnInfo);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
								}catch(Exception ep){}
							}
						}else{
							callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
						}
					}else if(smsInterface.equals(CherryBatchConstants.MWIF_SYSTEMCODE)){
						// 接口为梦网接口时，将返回结果解析成Map
						callFlag = mwXmlElements(resultMsg);
						if(!"0".equals(callFlag)){
							// 记录Batch日志
							try{
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECT00103");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								batchLoggerDTO.addParam(callFlag);
								batchLoggerDTO.addParam("");
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
							}catch(Exception ep){}
						}else{
							if("".equals(callFlag)){
								callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
							}
						}
					}else if(smsInterface.equals(CherryBatchConstants.MNIF_SYSTEMCODE)){
						// 接口为梦网新接口时，将返回结果解析成Map
						Map<String, Object> resultMap = mnXmlElements(resultMsg);
						if(resultMap != null && !resultMap.isEmpty()){
							callFlag = ConvertUtil.getString(resultMap.get("returnstatus"));
							String returnInfo = ConvertUtil.getString(resultMap.get("message"));
							if(!"Success".equals(callFlag) && !"SUCCESS".equals(callFlag)){
								// 记录Batch日志
								try{
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00103");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO.addParam(callFlag);
									batchLoggerDTO.addParam(returnInfo);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
								}catch(Exception ep){}
							}else{
								// 重新定义接口调用结果代号
								callFlag = "0";
							}
						}else{
							callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
						}
					}else if(smsInterface.equals(CherryBatchConstants.SYIF_SYSTEMCODE)){
						//首易接口时，直接解析字符串
						callFlag=resultMsg.substring(0,resultMsg.indexOf(","));
						String returnInfo=resultMsg.substring(resultMsg.indexOf(":")+1);
						if(Integer.parseInt(callFlag)<=0){//<0不成功的情况下
							//记录Batch日志
							try{
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECT00103");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								batchLoggerDTO.addParam(callFlag);
								batchLoggerDTO.addParam(returnInfo);
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
							}catch(Exception ep){}
						}else{
							// 重新定义接口调用结果代号
							callFlag = "0";
						}
					}else if(smsInterface.equals(CherryBatchConstants.WEIF_SYSTEMCODE)){
						// 接口为WE接口时，将返回结果解析成Map
						Map<String, Object> resultMap = weXmlElements(resultMsg);
						if(resultMap != null && !resultMap.isEmpty()){
							callFlag = ConvertUtil.getString(resultMap.get("returnstatus"));
							String returnInfo = ConvertUtil.getString(resultMap.get("message"));
							if(!"OK".equals(callFlag)){
								// 记录Batch日志
								try{
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00103");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO.addParam(callFlag);
									batchLoggerDTO.addParam(returnInfo);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
								}catch(Exception ep){}
							}else{
								// 重新定义接口调用结果代号
								callFlag = "0";
							}
						}else{
							callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
						}
					}else if(smsInterface.equals(CherryBatchConstants.MXTIF_SYSTEMCODE)){
						// 接口为MST接口时，将返回结果解析成Map
						Map<String, Object> resultMap = mxtXmlElements(resultMsg);
						if(resultMap != null && !resultMap.isEmpty()){
							callFlag = ConvertUtil.getString(resultMap.get("returnstatus"));
							String returnInfo = ConvertUtil.getString(resultMap.get("message"));
							if(!"Sucess".equals(callFlag)){
								// 记录Batch日志
								try{
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("ECT00103");
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									batchLoggerDTO.addParam(callFlag);
									batchLoggerDTO.addParam(returnInfo);
									batchLoggerDTO.addParam("麦讯通接口返回的xml："+resultMsg);
									CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
									cherryBatchLogger.BatchLogger(batchLoggerDTO);
								}catch(Exception ep){}
							}else{
								// 重新定义接口调用结果代号
								callFlag = "0";
							}
						}else{
							callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
						}
					}else{
						// 不支持的接口供应商
						callFlag = CherryBatchConstants.IFFLAG_UNSUPPORTEDSUPPLIER;
					}
				}else{
					callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
				}
			}else{
				// 没有获取到服务地址
				try{
					// 记录Batch日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00102");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00201", null));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					callFlag = CherryBatchConstants.IFFLAG_NOIFSERVICEURL;
				}catch(Exception ep){
					callFlag = CherryBatchConstants.IFFLAG_NOIFSERVICEURL;
				}
			}
		} catch (Exception e) {
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			callFlag = CherryBatchConstants.IFFLAG_SYSERROR;
		} catch (Throwable t) {
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(t));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			callFlag = CherryBatchConstants.IFFLAG_SYSERROR;
		}
		return callFlag;
	}
	
	// 调用Soap WebService接口的情况
	public String sendMsgWebService(Map<String, Object> paramMap, String smsInterface) throws Exception {
		String callFlag = "";
		try {
			// 判断使用的接口供应商
			if(smsInterface.equals(CherryBatchConstants.MWIF_SYSTEMCODE)){
				String userId = ConvertUtil.getString(paramMap.get("userId"));
				String password = ConvertUtil.getString(paramMap.get("password"));
				String mobile = ConvertUtil.getString(paramMap.get("mobile"));
				String message = ConvertUtil.getString(paramMap.get("message"));
				int mobileCount = ConvertUtil.getInt(paramMap.get("mobileCount"));
				String subPort = ConvertUtil.getString(paramMap.get("subPort"));
				WmgwSoapProxy wmg = new WmgwSoapProxy();
				callFlag = wmg.mongateCsSpSendSmsNew(userId, password, mobile, message, mobileCount, subPort);
				// 判断返回结果
				if(!"0".equals(callFlag)){
					if(callFlag.length()>10 && callFlag.length()<25){
						callFlag = "0";
					}else{
						// 记录Batch日志
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECT00103");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(callFlag);
						batchLoggerDTO.addParam("");
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						if(callFlag.length()>6){
							callFlag = CherryBatchConstants.IFFLAG_IFSENDMSGERROR;
						}
					}
				}else{
					if("".equals(callFlag)){
						callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
					}
				}
			}else if(smsInterface.equals(CherryBatchConstants.EMAY_SYSTEMCODE)){//亿美接口
				String softwareSerialNo = ConvertUtil.getString(paramMap.get("userId"));
				String key = ConvertUtil.getString(paramMap.get("password"));
				String[] mobile = ConvertUtil.getString(paramMap.get("mobile")).split(",");
				String message = ConvertUtil.getString(paramMap.get("message"));
				String sendTime = ConvertUtil.getString(paramMap.get("sendTime"));
				String addSerial = ConvertUtil.getString(paramMap.get("addSerial"));
				int smsPriority = 1;
				long smsID = 0;
				SDKClientProxy proxy = new SDKClientProxy();
				callFlag = String.valueOf(proxy.sendSMS(softwareSerialNo, key, sendTime, mobile, message, addSerial, "GBK", smsPriority, smsID));
				// 判断返回结果
				if(!"0".equals(callFlag)){
					if(callFlag.length()>10 && callFlag.length()<25){
						callFlag = "0";
					}else{
						// 记录Batch日志
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECT00103");
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						batchLoggerDTO.addParam(callFlag);
						batchLoggerDTO.addParam("");
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						if(callFlag.length()>6){
							callFlag = CherryBatchConstants.IFFLAG_IFSENDMSGERROR;
						}
					}
				}else{
					if("".equals(callFlag)){
						callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
					}
				}
			}else{
				// 不支持的接口供应商
				callFlag = CherryBatchConstants.IFFLAG_UNSUPPORTEDSUPPLIER;
			}
		} catch (Exception e) {
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			callFlag = CherryBatchConstants.IFFLAG_SYSERROR;
		} catch (Throwable t) {
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(t));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			callFlag = CherryBatchConstants.IFFLAG_SYSERROR;
		}
		return callFlag;
	}
	
	// 铭传接口返回结果解析
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> mcXmlElements(String xmlDoc) throws Exception {
    	List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
    	Document doc = DocumentHelper.parseText(xmlDoc);
		XPath xpathSelector = DocumentHelper.createXPath("root");
		List nodes = xpathSelector.selectNodes(doc);
		for (Object obj : nodes) {
			Element nts = (Element) obj;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("returnValue", nts.attribute("return").getText());
			map.put("info", nts.attribute("info").getText());
			if("0".equals(nts.attribute("return").getText())){
				map.put("msgid", nts.attribute("msgid").getText());
				map.put("numbers", nts.attribute("numbers").getText());
				map.put("messages", nts.attribute("messages").getText());
			}
			mapList.add(map);
		}
		return mapList;
    }
	
	// 梦网科技接口返回结果解析
	public String mwXmlElements(String xmlDoc) throws Exception {
    	Document doc = DocumentHelper.parseText(xmlDoc);
    	Element root = doc.getRootElement();
    	String result = root.getText();
		return result;
    }
	
	// 梦网科技（松仁电讯）新接口返回结果解析
	public Map<String, Object> mnXmlElements(String xmlDoc) throws Exception {
    	Document doc = DocumentHelper.parseText(xmlDoc);
    	// 获取根节点
		Element root = doc.getRootElement();
		// 获取返回状态节点
		Element returnstatus = root.element("returnstatus");
		// 获取返回信息节点
		Element message = root.element("message");
		// 获取账户余额节点
		Element remainpoint = root.element("remainpoint");
		// 获取任务ID节点
		Element taskID = root.element("taskID");
		// 获取成功提交的短信数量节点
		Element successCounts = root.element("successCounts");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("returnstatus", returnstatus.getText());
		map.put("message", message.getText());
		map.put("remainpoint", remainpoint.getText());
		map.put("taskID", taskID.getText());
		map.put("successCounts", successCounts.getText());
		return map;
    }
	
	// WE接口返回结果解析
	public Map<String, Object> weXmlElements(String xmlDoc) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
    	Document doc = DocumentHelper.parseText(xmlDoc);
    	Element root = doc.getRootElement();
    	String result = root.getText();
    	String resultStatus = "";
    	String resultMessage = "";
    	boolean resultFlag = true;
    	String[] returnStr = result.split(":");
    	if(null != returnStr && returnStr.length > 0){
	    	for (String str : returnStr) {
				if("OK".equals(str)){
					resultFlag = true;
				}else if("ERROR".equals(str)){
					resultFlag = false;
				}else{
					if("".equals(resultMessage)){
						resultMessage = str;
					}else{
						resultMessage = resultMessage + ":" + str;
					}
				}
			}
    	}
    	if(resultFlag){
    		returnMap.put("returnstatus", "OK");
    	}else{
    		if(!"".equals(resultMessage)){
    			resultStatus = resultMessage.substring(0, resultMessage.indexOf(":"));
    		}
    		returnMap.put("returnstatus", resultStatus);
    		returnMap.put("message", resultMessage);
    	}
		return returnMap;
    }
	
	// 麦讯通企业短信平台      返回结果解析
	public Map<String, Object> mxtXmlElements(String xmlDoc) throws Exception {
    	Document doc = DocumentHelper.parseText(xmlDoc);
    	// 获取根节点
		Element root = doc.getRootElement();
		// 获取返回状态节点
		Element returnstatus = root.element("RetCode");
		// 获取返回信息节点
		Element message = root.element("Message");
		// 获取任务ID节点
		Element remainpoint = root.element("JobID");
		// 获取合法的手机号数量节点
		Element taskID = root.element("OKPhoneCounts");
		// 获取成功提交的短信数量节点
		Element successCounts = root.element("StockReduced");
		// 获取以分号分隔的非法手机号码序列节点
		Element errPhones = root.element("ErrPhones");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("returnstatus", returnstatus.getText());
		if(null!=message){
			map.put("message", message.getText());
		}
		map.put("remainpoint", remainpoint.getText());
		map.put("taskID", taskID.getText());
		map.put("successCounts", successCounts.getText());
		map.put("errPhones", errPhones.getText());
		return map;
    }
	// 电话呼出接口
	@Override
	public String phoneCall(Map<String, Object> map,
			Map<String, Object> ifConfigMap) throws Exception {
		String runResult = "";
		try{
			runResult = setInterfaceParam(map, ifConfigMap, CherryBatchConstants.PHONEIF_CALL);
		}catch(Exception e){
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			runResult = CherryBatchConstants.IFFLAG_SETPARAMERROR;
		}
		return runResult;
	}
	
	// 调用HTTP接口的情况
	public String phoneWebService(String webServiceUrl, MultivaluedMap<String, String> queryParams, String phoneInterface) throws Exception {
		String callFlag = "";
		try {
			if(null != webServiceUrl && !"".equals(webServiceUrl)){
				WebResource webResource = binBECTCOM02.getWebResource(webServiceUrl);
				// 访问WebService
				String resultMsg = webResource.queryParams(queryParams).get(String.class);
				// 判断使用的接口供应商
				if(phoneInterface.equals(CherryBatchConstants.TRIF_SYSTEMCODE)){
					// 接口为天润接口时，将返回结果解析成Map
					Map<String, Object> resultMap = trXmlElements(resultMsg);
					if(resultMap != null && !resultMap.isEmpty()){
						callFlag = ConvertUtil.getString(resultMap.get("returnstatus"));
						String returnInfo = ConvertUtil.getString(resultMap.get("message"));
						if(!"1".equals(callFlag)){
							// 记录Batch日志
							try{
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("ECT00103");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								batchLoggerDTO.addParam(callFlag);
								batchLoggerDTO.addParam(returnInfo);
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
								cherryBatchLogger.BatchLogger(batchLoggerDTO);
							}catch(Exception ep){}
							// 重新定义接口调用结果代号
							callFlag = CherryBatchConstants.IFFLAG_IFCALLERROR;
						}else{
							// 重新定义接口调用结果代号
							callFlag = "0";
						}
					}else{
						callFlag = CherryBatchConstants.IFFLAG_NOTGETRETURNMSG;
					}
				}else{
					// 不支持的接口供应商
					callFlag = CherryBatchConstants.IFFLAG_UNSUPPORTEDSUPPLIER;
				}
			}else{
				// 没有获取到服务地址
				try{
					// 记录Batch日志
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("ECT00102");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam(PropertiesUtil.getMessage("ECT00201", null));
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					callFlag = CherryBatchConstants.IFFLAG_NOIFSERVICEURL;
				}catch(Exception ep){
					callFlag = CherryBatchConstants.IFFLAG_NOIFSERVICEURL;
				}
			}
		} catch (Exception e) {
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(e));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			callFlag = CherryBatchConstants.IFFLAG_SYSERROR;
		} catch (Throwable t) {
			// 记录Batch日志
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("ECT00101");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			batchLoggerDTO.addParam(ConvertUtil.getString(t));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			callFlag = CherryBatchConstants.IFFLAG_SYSERROR;
		}
		return callFlag;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> trXmlElements(String resultMsg) throws Exception {
		// 转换成Map
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = CherryUtil.json2Map(resultMsg);
		map.put("returnstatus", resultMap.get("result"));
		map.put("message", resultMap.get("description"));
		return map;
    }
}

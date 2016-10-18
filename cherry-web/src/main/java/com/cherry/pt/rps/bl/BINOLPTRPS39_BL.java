/*	
 * @(#)BINOLPTRPS39_BL.java     1.0 2015/07/08		
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
package com.cherry.pt.rps.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS39_IF;
import com.cherry.pt.rps.service.BINOLPTRPS39_Service;
import com.cherry.webservice.client.WebserviceClient;


/**
 * 产品催单Action
 * 
 * @author Hujh
 * @version 1.0 2015.07.08
 */
public class BINOLPTRPS39_BL implements BINOLPTRPS39_IF {

	private static final Logger logger = LoggerFactory.getLogger(BINOLPTRPS39_BL.class);
	
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource(name="binOLPTRPS39_Service")
	private BINOLPTRPS39_Service binOLPTRPS39_Service;
	
	// Excel导出列数组，收货延迟
	private final static String[][] proArray0 = {
		// 1
		{ "DeliverNo", "rps39_deliverNo", "30", "", "" },
		// 2
		{ "DeliverNoIF", "rps39_deliverNoIF", "30", "", "" },
		// 3
		{ "RelevanceNo", "rps39_relevanceNo", "30", "", "" },
		// 4
		{ "DeliverDepName", "rps39_deliverOrg", "30", "", "" },
		// 5
		{ "ReceiveDepName", "rps39_receiverOrg", "30", "", "" },
		// 6
		{ "DepotName", "rps39_depotName", "25", "", "" },
		// 7
		{ "LogicDepotName", "rps39_logDepotName", "25", "", "" },
		// 8
		{ "TotalQuantity", "rps39_totalQuantity", "15", "", "" },
		// 9
		{ "TotalAmount", "rps39_totalAmount", "15", "", "" },
		// 10
		{ "DeliverDate", "rps39_date", "15", "", "" },
		// 11
		{ "DelayDate", "rps39_delayDate", "15", "", "" },
		// 12
		{ "VerifiedFlag", "rps39_verifiedFlag", "10", "", "1180" },
		// 13
		{ "TradeStatus", "rps39_tradeStatus", "10", "", "1141" },
		// 14
		{ "EmployeeName", "rps39_employeeName", "10", "", "" },
		// 15
		{ "AuditName", "rps39_auditEmpName", "10", "", "" }					
	};
	
	// Excel导出列数组，收货延迟,促销品
	private final static String[][] proArray2 = {
		// 1
		{ "DeliverNo", "rps39_deliverNo", "30", "", "" },
		// 2
		{ "DeliverNoIF", "rps39_deliverNoIF", "30", "", "" },
		// 3
		{ "RelevanceNo", "rps39_relevanceNo", "30", "", "" },
		// 4
		{ "DeliverDepName", "rps39_deliverOrg", "30", "", "" },
		// 5
		{ "ReceiveDepName", "rps39_receiverOrg", "30", "", "" },
		// 8
		{ "TotalQuantity", "rps39_totalQuantity", "15", "", "" },
		// 9
		{ "TotalAmount", "rps39_totalAmount", "15", "", "" },
		// 10
		{ "DeliverDate", "rps39_date", "15", "", "" },
		// 11
		{ "DelayDate", "rps39_delayDate", "15", "", "" },
		// 12
		{ "VerifiedFlag", "rps39_verifiedFlag", "10", "", "1007" },
		// 13
		{ "StockInFlag", "rps39_tradeStatus", "10", "", "1017" }
	};
	
	// Excel导出列数组，反向催单
	private final static String[][] proArray1 = {
			// 1
			{ "BIN_ReminderNo", "rps39_reminderNo", "20", "", "" },
			
			{ "DeliverNo", "rps39_deliverNo", "20", "", "" },
			
			// 2
			{ "BIN_CargoType", "rps39_cargoType", "8", "", "" },
			// 3
			{ "Receive_Date", "rps39_receiveDate", "12", "", "" },
			// 4
			{ "ReceiveCntName", "rps39_receiveCounter", "20", "", "" },
			// 5
			{ "Receive_Quantity", "rps39_amount", "8", "", "" },
			// 6
			{ "EmployeeName", "rps39_reminderMaker", "8", "", "" },
			// 7
			{ "Email", "rps39_BASEmail", "15", "", "" },
			// 8
			{ "MobilePhone", "rps39_BASTel", "15", "", "" },
			// 9
			{ "ExpressBillCode", "rps39_expressID", "20", "", "" },
			// 10
			{ "Trade_Date", "rps39_tradeDate", "12", "", "" },
			// 11
			{ "delayDate", "rps39_delayDate", "12", "", "" },
			// 12
			{ "Reminder_Count", "rps39_reminderCount", "8", "", "" },
			// 13
			{ "Fst_ReminderTime", "rps39_fstRemindTime", "25", "", "" },
			// 14
			{ "Snd_ReminderTime", "rps39_sndRemindTime", "25", "", "" },
			// 15
			{ "Comment", "rps39_comment", "20", "", "" },
			// 16
			{ "Status", "rps39_receiptStatus", "8", "", "" }						
		};
	
	
	/**查询条件组**/
	
	private static final String[] proConditions = {"startDate", "endDate"};
	
	
	/**
	 * 取得催单单据数量
	 */
	@Override
	public int getReminderCount(Map<String, Object> map) {
		
		return binOLPTRPS39_Service.getReminderCount(map);
	}

	/**
	 * 取得催单单据list
	 */
	@Override
	public List<Map<String, Object>> getReminderList(Map<String, Object> map) {
		
		return binOLPTRPS39_Service.getReminderList(map);
	}

	/**
	 * excel文件导出名称
	 */
	@Override
	public String getExportName(Map<String, Object> map) {
		
		String exportName = null;
		
		String cargoType = ConvertUtil.getString(map.get("cargoType"));
		
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		
		String reminderType = ConvertUtil.getString(map.get("reminderType"));

		exportName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS39", language, "downloadFileName"+reminderType+cargoType) + CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;;
		
		return exportName;
	}
	
	/**
	 * excel导出
	 * @throws Exception 
	 */
	@Override
	public byte[] exportExcel(Map<String, Object> map) throws Exception {
		String cargoType = ConvertUtil.getString(map.get("cargoType"));
		List<Map<String, Object>> dataList = null;
		//催单类型，"0":收货延迟；"1":反向催单
		String reminderType = ConvertUtil.getString(map.get("reminderType"));
		if(!CherryChecker.isEmptyString(reminderType) && "0".equals(reminderType)) {
			if(!CherryChecker.isEmptyString(cargoType) && "N".equals(cargoType)) {
				dataList = binOLPTRPS39_Service.getDeliverListExcel(map);
			}
			else if(!CherryChecker.isEmptyString(cargoType) && "P".equals(cargoType)) {
				dataList = binOLPTRPS39_Service.getPrmDeliverListExcel(map);
			}
		} else if (!CherryChecker.isEmptyString(reminderType) && "1".equals(reminderType)) {
			dataList = binOLPTRPS39_Service.getReminderListExcel(map);
		}
		if(dataList.size() > 0 && !dataList.isEmpty()) {
			BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
			ep.setMap(map);
			//导出数据列数组
			if(!CherryChecker.isEmptyString(reminderType) && "0".equals(reminderType)) {
				if(!CherryChecker.isEmptyString(cargoType) && "N".equals(cargoType)) {
					ep.setArray(proArray0);
				}
				if(!CherryChecker.isEmptyString(cargoType) && "P".equals(cargoType)) {
					ep.setArray(proArray2);
				}
			} else if (!CherryChecker.isEmptyString(reminderType) && "1".equals(reminderType)) {
				ep.setArray(proArray1);
			}
			//导出数据列头国际化资源文件
			ep.setBaseName("BINOLPTRPS39");
			ep.setSearchCondition(getConditionStr(map));
			ep.setDataList(dataList);
			return binOLMOCOM01_BL.getExportExcel(ep);
		}
		return null;
	}

	/**
	 * 取得条件字符串
	 * @param map
	 * @return
	 */
	private String getConditionStr(Map<String, Object> map) {
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		StringBuffer condition = new StringBuffer();
		for (String con : proConditions) {
			// 条件值
			String paramValue = ConvertUtil.getString(map.get(con));
			if (!"".equals(paramValue)) {
				// 条件名
				String paramName = "";

				if ("startDate".equals(con) || "endDate".equals(con)) {
					// 日期
					paramName = binOLMOCOM01_BL.getResourceValue(null,
							language, "global.page." + con);
				}
				condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
			}
		}
		return condition.toString().trim();
	}

	/**
	 * 给BAS发送催单消息
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> reminderToBAS(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String errCode = "";
		String errMsg = "";
		try {
			Map<String, Object> BASInfoMap = binOLPTRPS39_Service.getBASInfo(map);
			if(!BASInfoMap.isEmpty() && null != BASInfoMap) {
				String count = ConvertUtil.getString(map.get("reminderCount"));
				if("0".equals(count)) {
					map.put("Fst_ReminderTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
					map.put("reminderCount", "1");//催单次数+1
				} else if("1".equals(count)) {
					map.put("Snd_ReminderTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
					map.put("reminderCount", "2");//催单次数+1
				}
				//手机号码
				String mobilePhone = ConvertUtil.getString(BASInfoMap.get("MobilePhone"));
				//品牌代码
				String brandCode = ConvertUtil.getString(map.get("brandCode"));
				
				String receiveCntName = ConvertUtil.getString(BASInfoMap.get("ReceiveCntName"));
				
				map.put("receiveCntName", receiveCntName);
				
				String messageBody = this.getMsgTemplate(map);
				
				String flag = ConvertUtil.getString(map.get("flag"));
				
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("BrandCode", brandCode);
				paramsMap.put("brandCode", brandCode);
				paramsMap.put("TradeType", "SendMobileMessage");//业务类型
				paramsMap.put("EventType", "14");//事件类型
				paramsMap.put("MobilePhone", mobilePhone);//手机号码
				paramsMap.put("MessageBody", messageBody);//短信正文
				//产品，发给柜台所属经销商业务员，可能业务员会有多个
				if(!CherryChecker.isNullOrEmpty(flag, true)) {
					List<Map<String, Object>> mobilePhoneList = binOLPTRPS39_Service.getMobilePhoneList(map);
					if(mobilePhoneList.size() == 0) {
						resultMap.put("result", "1");
						logger.error("**********【" + receiveCntName + "】柜台说书经销商部门的经销商业务员手机号为空，催单失败！");
					} else {
						Map<String, Object> mobilePhoneMap = new HashMap<String, Object>();
						boolean sendFlag = false;
						for(int i = 0; i < mobilePhoneList.size(); i++) {
							mobilePhoneMap = mobilePhoneList.get(i);
							String mobilePhoneTemp = ConvertUtil.getString(mobilePhoneMap.get("mobilePhone"));
							if(!CherryChecker.isNullOrEmpty(mobilePhoneTemp, true)) {
								paramsMap.put("MobilePhone", mobilePhoneTemp);//手机号码
								//调用webservice发送短信
								Map<String, Object> tempMap = WebserviceClient.accessBatchWebService(paramsMap);
								if(!tempMap.isEmpty() && null != tempMap) {
									errCode = ConvertUtil.getString(tempMap.get("ERRORCODE"));
									errMsg = ConvertUtil.getString(tempMap.get("ERRORMSG"));
									//调用失败
									if(!"0".equals(errCode)) {
										resultMap.put("result", "1");
										resultMap.put("ERRORCODE", errCode);
										resultMap.put("ERRORMSG", errMsg);
										logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORCODE【 "+errCode+" 】************");
										logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORMSG【 "+errMsg+" 】************");
									} else {
										sendFlag = true;
										//调用成功
										resultMap.put("result", "0");
										resultMap.put("ERRORCODE", errCode);
										resultMap.put("ERRORMSG", errMsg);
									}
								} else {
									errCode = "-1";
									errMsg = "webService访问返回结果信息为空！";
									resultMap.put("result", "1");
									resultMap.put("ERRORCODE", errCode);
									resultMap.put("ERRORMSG", errMsg);
									logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORCODE【 "+errCode+" 】************");
									logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORMSG【 "+errMsg+" 】************");
								}
								logger.info("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信处理结束【 "+errCode+" 】************");
							}
						}
						if(sendFlag) {
							binOLPTRPS39_Service.reminderToBAS(map);
						} else {
							logger.error("**********【Webservice接口_Batch】给【" + receiveCntName + "】柜台所属经销商部门的经销商业务员发送短信失败！！************");
						}
					}
				} else {
					//调用webservice发送短信，给BAS发送短信
					Map<String, Object> tempMap = WebserviceClient.accessBatchWebService(paramsMap);
					if(!tempMap.isEmpty() && null != tempMap) {
						errCode = ConvertUtil.getString(tempMap.get("ERRORCODE"));
						errMsg = ConvertUtil.getString(tempMap.get("ERRORMSG"));
						//调用失败
						if(!"0".equals(errCode)) {
							resultMap.put("result", "1");
							resultMap.put("ERRORCODE", errCode);
							resultMap.put("ERRORMSG", errMsg);
							logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORCODE【 "+errCode+" 】************");
							logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORMSG【 "+errMsg+" 】************");
						} else {
							//调用成功
							resultMap.put("result", "0");
							resultMap.put("ERRORCODE", errCode);
							resultMap.put("ERRORMSG", errMsg);
							binOLPTRPS39_Service.reminderToBAS(map);
						}
					} else {
						errCode = "-1";
						errMsg = "webService访问返回结果信息为空！";
						resultMap.put("result", "1");
						resultMap.put("ERRORCODE", errCode);
						resultMap.put("ERRORMSG", errMsg);
						logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORCODE【 "+errCode+" 】************");
						logger.error("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信出现异常ERRORMSG【 "+errMsg+" 】************");
					}
					logger.info("**********【Webservice接口_Batch】短信对外开放接口给BAS发送短信处理结束【 "+errCode+" 】************");
				}
			}
		} catch (Exception e) {
			resultMap.put("result", "1");
			logger.error(e.getMessage(), e);
		}
		return resultMap;
	}

	
	/**
	 * 根据用途和客户类型确定模板
	 */
	private String getMsgTemplate(Map<String, Object> map) {
		String receiveCntName = ConvertUtil.getString(map.get("receiveCntName"));
		map.put("customerType", "3");
		map.put("privilegeFlag", "1");
		map.put("businessType", "4");
		map.put("templateUse", "YZXX");
		map.put("templateType", "1");
		map.put("operationType", "1");
		Map<String, Object>	 resultMap = binOLPTRPS39_Service.getMsgTemplate(map);
		String contents = ConvertUtil.getString(resultMap.get("contents"));
		if(!"NULL".equalsIgnoreCase(contents) && !CherryChecker.isEmptyString(contents)) {
			if(contents.contains(CherryConstants.COUNTER_NAME)) {
				if(!"".equals(contents) && !CherryChecker.isEmptyString(contents)) {
					contents = contents.replaceAll(CherryConstants.COUNTER_NAME, receiveCntName);
				} else {
					return "";
				}
			}
		}
		return contents;
	}

	/**
	 * 发货单数量
	 */
	@Override
	public int getDeliverCount(Map<String, Object> map) {
		
		return binOLPTRPS39_Service.getDeliverCount(map);
	}

	/**
	 * 发货单list
	 */
	@Override
	public List<Map<String, Object>> getDeliverList(Map<String, Object> map) {

		String cargoType = ConvertUtil.getString(map.get("cargoType"));
		if("N".equals(cargoType)) {
			return binOLPTRPS39_Service.getDeliverList(map);
		} else {
			return binOLPTRPS39_Service.getPrmDeliverList(map);
		}
	}

	/**
	 * 将发货单号写入催单表中
	 * @param reminderMap billId reminderId
	 * @param userInfo
	 */
	@Override
	public void tran_updateReminder(Map<String, Object> map, UserInfo userInfo) {

		int billId = ConvertUtil.getInt(map.get("billId"));
		String reminderId = ConvertUtil.getString(map.get("reminderId"));
		String cargoType = ConvertUtil.getString(map.get("cargoType"));
		String deliverNo = null;
		if(!CherryChecker.isEmptyString(reminderId) && !CherryChecker.isEmptyString(cargoType) && 0!=billId) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			Map<String, Object> deliverNoMap = new HashMap<String, Object>();
			tempMap.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
			tempMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			tempMap.put("billId", billId);
			tempMap.put("reminderId", reminderId);
			tempMap.put("status", "1");
			tempMap.put("handleDate", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
			tempMap.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
			tempMap.put(CherryConstants.UPDATEPGM, "BINOLPTRPS39_BL");
			if("N".equals(cargoType)) {
				deliverNoMap = binOLPTRPS39_Service.getProductDeliverNo(tempMap);
				if(!deliverNoMap.isEmpty()) {
					deliverNo = ConvertUtil.getString(deliverNoMap.get("deliverNo"));
					if(!CherryChecker.isEmptyString(deliverNo)) {
						tempMap.put("deliverNo", deliverNo);
						binOLPTRPS39_Service.updateReminder(tempMap);
					}
				}
			} else if("P".equals(cargoType)) {
				String log = ConvertUtil.getString(map.get("Log"));//区分暂存和直接发货，促销品发货单两个单据存在不同表中
				if(!CherryChecker.isEmptyString(log) && log.equals("log")) {
					tempMap.put("log", log);
				} 
				deliverNoMap = binOLPTRPS39_Service.getPromotionDeliverNo(tempMap);
				if(!deliverNoMap.isEmpty()) {
					deliverNo = ConvertUtil.getString(deliverNoMap.get("deliverNo"));
					if(!CherryChecker.isEmptyString(deliverNo)) {
						tempMap.put("deliverNo", deliverNo);
						binOLPTRPS39_Service.updateReminder(tempMap);
					}
				}
			}
		}
	}

	@Override
	public boolean verifyDeliverNo(Map<String, Object> map) {
		//发货单号是否存在并且是当前柜台
		int count = binOLPTRPS39_Service.verifyDeliverNo(map);
		//该催单是否已有发货单
		int count2 = binOLPTRPS39_Service.verifyReminderNo(map);
		return count > 0 && count2 == 0;
	
	}

	@Override
	public void tran_updateReminderExists(Map<String, Object> map) {
		
		binOLPTRPS39_Service.updateReminder(map);
		
	}

}

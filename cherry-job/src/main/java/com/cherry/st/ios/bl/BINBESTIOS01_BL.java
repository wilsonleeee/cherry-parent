package com.cherry.st.ios.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.ios.service.BINBESTIOS01_Service;
import com.mqhelper.interfaces.MQHelper_IF;
/*  
 * @(#)BINBESTIOS01_BL.java    1.0 2012-2-21     
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
/**
 * 新后台与金蝶K3发货单/退库单数据同步
 * 
 * @author zhanggl
 * 
 * 
 * */

public class BINBESTIOS01_BL {

	@Resource
	private BINBESTIOS01_Service binBESTIOS01_Service;
	
	/**MQHelper模块接口*/
	@Resource
	private MQHelper_IF mqHelperImpl;
	
	/** 失败条数 */
	private int failCount = 0;
	/** 处理总条数 */
	private int totalCount = 0;
	/** 成功条数 */
	private int successCount = 0;
	
	/**
	 * 取得老后台brand数据库中的发货单/退库单数据
	 * 
	 * @param map
	 * 
	 * 
	 * */
	public int tran_importInvoiceData(Map<String,Object> map) throws Exception{
		failCount = 0;
		totalCount = 0;
		successCount = 0;
		boolean flag = true;
		
		//从老后台brand数据库中取得主表数据
		List<Map<String,Object>> invoiceMainList = binBESTIOS01_Service.getInvoiceMain(map);
		
		if(CherryBatchUtil.isBlankList(invoiceMainList)){
			// 处理总件数：0
			BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
			batchLoggerDTO2.setCode("IIF00001");
			batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO2.addParam(String.valueOf(totalCount));
			// 成功件数：0
			BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
			batchLoggerDTO3.setCode("IIF00002");
			batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO3.addParam(String.valueOf(successCount));
			// 失败件数：0
			BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
			batchLoggerDTO5.setCode("IIF00005");
			batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO5.addParam(String.valueOf(failCount));
			
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			// 处理总件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO2);
			// 成功件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO3);
			// 失败件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO5);
			
			return CherryBatchConstants.BATCH_SUCCESS;
		}
		
		//处理总件数
		totalCount = invoiceMainList.size();
		
		//遍历主数据，并取出相应的明细数据、废弃成功取出的数据、注入DTO中
		for(int i = invoiceMainList.size()-1 ; i >=0 ; i--)
		{
			Map<String,Object> tempMap = invoiceMainList.get(i);
			
			String tradeTypeName = "";
			String direction = CherryBatchUtil.getString(tempMap.get("Direction"));
			if("0".equals(direction)){
			    tradeTypeName = PropertiesUtil.getMessage("PST00001", null);
			}else if("2".equals(direction)){
			    tradeTypeName = PropertiesUtil.getMessage("PST00003", null);
			}else{
			    tradeTypeName = PropertiesUtil.getMessage("PST00002", null);
			}
			
			//根据主数据取明细数据
			List<Map<String,Object>> detailList = binBESTIOS01_Service.getInvoiceDetail(tempMap);
			
			//判断明细数据是否为空
			if(CherryBatchUtil.isBlankList(detailList)){
				
				//打印警告日志：执行金蝶K3接口数据导入时，品牌代码为：{0}，发货单/退库单单据号为：{1}的单据没有明细数据！
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EST00002");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				List<String> paramsList = new ArrayList<String>();
				paramsList.add(String.valueOf(map.get("brandCode")));
				paramsList.add(tradeTypeName);
				paramsList.add(String.valueOf(tempMap.get("ERPTicketCode")));
				batchLoggerDTO1.setParamsList(paramsList);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1);
				
				failCount++;
				flag = false;
				continue;
			}
			
			Map<String,Object> dataMap = null;
			//将数据转化成dto
			try{
				dataMap= this.assemblingData(tempMap, detailList);
				if(null == dataMap || dataMap.isEmpty()){
					throw new Exception();
				}
				//设定MQ消息共通
//				DTOUtil.setBaseDto(mainDto, tempMap);
				
			}catch(Exception e){
				
				//打印警告 日志：执行金蝶K3接口数据导入时，将品牌代码为：{0}，发货单/退库单单据号为：{1}的单据转化成DTO时出错！
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EST00003");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				List<String> paramsList = new ArrayList<String>();
				paramsList.add(String.valueOf(map.get("brandCode")));
				paramsList.add(tradeTypeName);
				paramsList.add(String.valueOf(tempMap.get("ERPTicketCode")));
				batchLoggerDTO1.setParamsList(paramsList);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1,e);
				
				failCount++;
				flag = false;
				continue;
			}
			
			//更新老后台brand数据库中的已经发送的数据废弃
			try{
				int updateCount = 0;
				updateCount = binBESTIOS01_Service.updateInvoiceMain(tempMap);
				if(updateCount != 1){
					throw new Exception();
				}
			}catch(Exception e){
				//事物回滚
				binBESTIOS01_Service.ifManualRollback();
				
				//打印警告 日志：执行金蝶K3接口数据导入时，将品牌代码为：{0}，发货单/退库单单据号为：{1}的单据数据废弃时出错！
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EST00005");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				List<String> paramsList = new ArrayList<String>();
				paramsList.add(String.valueOf(map.get("brandCode")));
				paramsList.add(tradeTypeName);
				paramsList.add(String.valueOf(tempMap.get("ERPTicketCode")));
				batchLoggerDTO1.setParamsList(paramsList);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1,e);
				
				failCount++;
				flag = false;
				continue;
			}
			
			//调用MQHelper接口进行数据发送
			try{
				
				mqHelperImpl.sendData(dataMap, "posToCherryMsgQueue");
				//事务提交
				binBESTIOS01_Service.witManualCommit();
				binBESTIOS01_Service.ifManualCommit();
			}catch(Exception e){
				//接口数据库中事物回滚
				binBESTIOS01_Service.ifManualRollback();
				if(!(e instanceof CherryMQException)){
					//MQ_Log表中事物回滚
					binBESTIOS01_Service.witManualRollback();
				}
				
				//打印警告 日志：执行金蝶K3接口数据导入时，将品牌代码为：{0}，发货单/退库单单据号为：{1}的单据向MQ消息队列发送时出错！
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EST00004");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				List<String> paramsList = new ArrayList<String>();
				paramsList.add(String.valueOf(map.get("brandCode")));
				paramsList.add(tradeTypeName);
				paramsList.add(String.valueOf(tempMap.get("ERPTicketCode")));
				batchLoggerDTO1.setParamsList(paramsList);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1,e);
				
				failCount++;
				flag = false;
				continue;
			}
			
			successCount++;
		}
		
		// 处理总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00001");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount));
		// 成功件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00002");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 成功件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		
		if(flag){
			return CherryBatchConstants.BATCH_SUCCESS;
		}else{
			return CherryBatchConstants.BATCH_ERROR;
		}
	}
	
	/**
	 * 将发货单/退库单的主数据和明细数据组装到一个MAP中，供调用MQHelper模块时使用
	 * 
	 * 
	 * */
	private Map<String,Object> assemblingData(Map<String,Object> mainMap ,List<Map<String,Object>> detailList) throws Exception{
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//MQ版本号
		resultMap.put("Version", MessageConstants.MESSAGE_VERSION);
		
		//MQ消息体数据类型
		resultMap.put("Type", MessageConstants.MESSAGE_TYPE_SALE_STOCK);
		
		//消息主体数据key
		resultMap.put("MainLineKey", new String[]{"BrandCode","TradeNoIF","ModifyCounts",
				"CounterCode","RelevantCounterCode","TotalQuantity","TotalAmount","TradeType","SubType",
				"RelevantNo","Reason","TradeDate","TradeTime","TotalAmountBefore","TotalAmountAfter","MemberCode"});
		
		//消息主体数据
		String[] mainData = new String[((String[])resultMap.get("MainLineKey")).length];
		
		//品牌代码
		mainData[0] = CherryBatchUtil.getString(mainMap.get("brand_abbr"));
		//单据号
		mainData[1] = CherryBatchUtil.getString(mainMap.get("ERPTicketCode"));
		//更改次数
		mainData[2] = "0";
		//接收门店
		mainData[3] = CherryBatchUtil.getString(mainMap.get("CounterCode"));
		//关联柜台号
		mainData[4] = null;
		//总数量
		if(("").equals(CherryBatchUtil.getString(mainMap.get("TotalQuantity")))){
			mainData[5] = "0";
		}else{
			mainData[5] = CherryBatchUtil.getString(mainMap.get("TotalQuantity"));
		}
		//总金额
		if(("").equals(CherryBatchUtil.getString(mainMap.get("TotalAmount")))){
			mainData[6] = "0";
		}else{
			mainData[6] = CherryBatchUtil.getString(mainMap.get("TotalAmount"));
		}
		//单据类型
		mainData[7] = "KS";
		//子类型 SD:发货单；RJ:退库单；2:订货拒绝；3:退库申请拒绝。
		String direction = CherryBatchUtil.getString(mainMap.get("Direction"));
		if("0".equals(direction)){
		    mainData[8] = CherryConstants.OS_BILLTYPE_SD;
		}else if("1".equals(direction)){
		    mainData[8] = CherryConstants.OS_BILLTYPE_RJ;
		}else if("2".equals(direction) || "3".equals(direction)){
		    mainData[8] = direction;
		}else{
		    mainData[8] = null;
		}
		
		//关联单号
		mainData[9] = CherryBatchUtil.getString(mainMap.get("PreTicketCode"));
		//发货理由
		mainData[10] = null;
		
		String generateTime = CherryBatchUtil.getString(mainMap.get("GenerateTime"));
		String[] gTArr = generateTime.split("\\s");
		
		String[] date = gTArr[0].split("-");
		//发货日期
		mainData[11] = date[0]+date[1]+date[2];
		//发货时间
		mainData[12] = gTArr[1].split("\\.")[0];
		
		mainData[13] = null;
		mainData[14] = null;
		mainData[15] = null;
		
		resultMap.put("MainDataLine", mainData);
		
		//明细数据Key
		resultMap.put("DetailLineKey", new String[]{"TradeNoIF","ModifyCounts","BAcode","StockType",
				"Barcode","Unitcode","InventoryTypeCode","Quantity","QuantityBefore","Price","Reason"});
		
		//明细数据
		List<String[]> detailData = new ArrayList<String[]>();
		for(int i = detailList.size()-1 ; i >= 0 ; i--){
			Map<String,Object> temMap = detailList.get(i);
			String[] temp = new String[((String[])resultMap.get("DetailLineKey")).length];
			
			//单据号
			temp[0] = CherryBatchUtil.getString(temMap.get("ERPTicketCode"));
			//修改次数
			temp[1] = "0";
			//制单员
			temp[2] = CherryBatchConstants.K3_DEALER_CODE;
			//入出库类型
			temp[3] = null;
			//产品条码
			temp[4] = CherryBatchUtil.getString(temMap.get("Barcode"));
			//厂商编码
			temp[5] = CherryBatchUtil.getString(temMap.get("Unitcode"));
			//仓库类型(发货为空，退库必填)
			temp[6] = null;
			String inventoryTypeCode = CherryBatchUtil.getString(temMap.get("InventoryTypeCode"));
			if(!"".equals(inventoryTypeCode)){
			    temp[6] = inventoryTypeCode;
			}
			//单品数量
			if("".equals(CherryBatchUtil.getString(temMap.get("Quantity")))){
				temp[7] = "0";
			}else{
				temp[7] = CherryBatchUtil.getString(temMap.get("Quantity"));
			}
			
			temp[8] = null;
			//单品价格
			if("".equals(CherryBatchUtil.getString(temMap.get("Price")))){
				temp[9] = "0";
			}else{
				temp[9] = CherryBatchUtil.getString(temMap.get("Price"));
			}
			
			detailData.add(temp);
		}
		
		resultMap.put("DetailDataLine", detailData);
		
		//设定MQ_Log日志需要的数据
		Map<String, Object> mqLog = new HashMap<String,Object>();
		mqLog.put("BillType", "KS");
		mqLog.put("BillCode", mainData[1]);
		mqLog.put("CounterCode", mainData[3]);
		mqLog.put("Txddate", mainData[11].substring(2));
		String[] timeArr = mainData[12].split(":");
		mqLog.put("Txdtime", timeArr[0]+timeArr[1]+timeArr[2]);
		mqLog.put("Source", "K3");
		mqLog.put("SendOrRece", "S");
		mqLog.put("ModifyCounts", "0");
		
		resultMap.put("Mq_Log", mqLog);
		
		return resultMap;
	}
	
	
	
}

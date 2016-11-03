/*	
 * @(#)BINOTYIN01_BL.java     1.0 @2013-3-11		
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
package com.cherry.ot.yin.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ot.yin.service.BINOTYIN08_Service;
import com.mqhelper.interfaces.MQHelper_IF;

/**
 *
 * 颖通接口：发货单退库单导入BL
 *
 * @author jijw
 *
 * @version  2013-3-18
 */
public class BINOTYIN08_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINOTYIN08_BL.class);	
	@Resource
	private BINOTYIN08_Service binOTYIN08_Service;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 销售主数据每次导出数量:2000条 */
	private final int UPDATE_SIZE = 1000;
	
	/** 发货单接口表同步状态:1 同步中 */
	private final String trxStatus_1 = "1";
	
	/** 发货单接口表同步状态:2 同步完成 */
	private final String trxStatus_2 = "2";
	
	private Map<String, Object> comMap;
	
	/**MQHelper模块接口*/
	@Resource
	private MQHelper_IF mqHelperImpl;
	
	/** 发货接口表分组，用于拼装MQ */
	List<List<Map<String,Object>>> retList = new ArrayList<List<Map<String,Object>>>();
	
	/** 失败的单据号集合 */
	List<String> falidDocEntryList = new ArrayList<String>();
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 发货退库单的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	public int tran_batchOTYIN08(Map<String, Object> map)
			throws CherryBatchException {
		// 初始化
		comMap = getComMap(map);
		
		while (true) {
				
			// 取得颖通发货单接口表中条件[trxStatus is null ]的单据号集合
			Map<String, Object> trxStatusNullMap = new HashMap<String, Object>();
			trxStatusNullMap.putAll(comMap);
			trxStatusNullMap.put("upCount", UPDATE_SIZE);
			trxStatusNullMap.put("falidDocEntryList", falidDocEntryList);
			List<String> docEntryList = binOTYIN08_Service.getDocEntryListForOT(trxStatusNullMap);
			
			if (CherryBatchUtil.isBlankList(docEntryList)) {
				break;
			}
			
			// 统计总条数(单据数量[1个发货单据具有一条或多条物理数据]，此处只统计发货单据数)
			totalCount += docEntryList.size();
			
			// 业务处理(1、更新单据状态，2、取得新后台价格等信息，3、发送MQ及插入MQ_Log日志等)
			updExpTrans(docEntryList);
			
			// 发货单据数少于一页，跳出循环
			if (docEntryList.size() < UPDATE_SIZE) {
				break;
			}

		}
		outMessage();
		return flag;
	}
	
	/**
	 * 颖通发货单业务处理逻辑
	 * @param docEntryList
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	private void updExpTrans(List<String> docEntryList) throws CherryBatchException {
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		
		for(String docEntry : docEntryList){
			try {
				paraMap.clear();
				paraMap.putAll(comMap);
				
				// Step1: 更新颖通发货单接口表的数据从[trxStatus is null ]更新为[trxStatus=1]
				paraMap.put("docEntry", docEntry);
				paraMap.put("trxStatus_Old", "isNull");
				paraMap.put("trxStatus_New", trxStatus_1);
				binOTYIN08_Service.updTrxStatusForOT(paraMap);
				
				// Step2: 取得颖通发货单接口表物理数据(及根据接口表ItemCode取得新后台产品的相关属性)
				paraMap.put("trxStatus", trxStatus_1);
				List<Map<String,Object>> exportTransListForOT = binOTYIN08_Service.getExportTransListForOT(paraMap);
				
				Map<String,Object> ppPriceMap = new HashMap<String, Object>();
				for(Map<String,Object> exportTransMapForOT : exportTransListForOT){
					
					// 取得每单的价格、unitCode、barCode
					String itemCode = (String)exportTransMapForOT.get("ItemCode");
					
					ppPriceMap.clear();
 					ppPriceMap.putAll(comMap);
 					ppPriceMap.put("itemCode", itemCode);
					Map<String,Object> priceMap = binOTYIN08_Service.getProPrmPriceByUB(ppPriceMap);
					
					if(null != priceMap && !priceMap.isEmpty()){
						
 						exportTransMapForOT.put("price", priceMap.get("price"));
						exportTransMapForOT.put("unitCode", priceMap.get("unitCode"));
						exportTransMapForOT.put("barCode", priceMap.get("barCode"));
					} else {
						
						// 异常
		                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
		                batchExceptionDTO.setBatchName(this.getClass());
		                batchExceptionDTO.setErrorCode("EOT00011");
		                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
		                //组织ID：
		                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(ppPriceMap.get(CherryBatchConstants.ORGANIZATIONINFOID)));
		                //品牌ID：
		                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(ppPriceMap.get(CherryBatchConstants.BRANDINFOID)));
		                //接口单据号
		                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(exportTransMapForOT.get("DocEntry")));
		                // ItemCode
		                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemCode));
		                throw new CherryBatchException(batchExceptionDTO);
					}
				
				}
				
				// Step3: 将发货单据拼装成符合发货MQ及插入老后台MQ_Log日志的数据结构
				Map<String,Object> mainMap = exportTransListForOT.get(0);
				
				// 销售商品总数量
				BigDecimal totalQuantity = new BigDecimal(0);
				// 销售商品总金额
				BigDecimal totalAmount = new BigDecimal(0);
				for(Map<String,Object> itemMap : exportTransListForOT){
					BigDecimal quantity =  (BigDecimal)itemMap.get("Quantity");
					BigDecimal price =  (BigDecimal)itemMap.get("price");
					totalQuantity = totalQuantity.add(quantity);
					totalAmount = totalAmount.add(price.multiply(quantity));
				}
				mainMap.put("TotalQuantity", totalQuantity);
				mainMap.put("TotalAmount", totalAmount);
				
				// 拼装MQ
				Map<String,Object> propSendMQMap = assemblingData(mainMap, exportTransListForOT);
				
				// Step4: 发货单接口表数据从[trxStatus=1]更新为[trxStatus=2]
				Map<String, Object> t1ToT2Map = new HashMap<String, Object>();
				t1ToT2Map.putAll(comMap);
				t1ToT2Map.put("docEntry", docEntry);
				t1ToT2Map.put("trxStatus_Old", trxStatus_1);
				t1ToT2Map.put("trxStatus_New", trxStatus_2);
				binOTYIN08_Service.updTrxStatusForOT(t1ToT2Map);
				
				//Step5: 调用MQHelper接口进行数据发送
				mqHelperImpl.sendData(propSendMQMap, "posToCherryMsgQueue");
				
				//事务提交
				binOTYIN08_Service.witManualCommit();
				binOTYIN08_Service.tpifManualCommit();
				
			} catch(Exception ex){
				try{
					// 颖通数据源回滚
					binOTYIN08_Service.tpifManualRollback();
					//MQ_Log表中事物回滚
					binOTYIN08_Service.witManualRollback();
				} catch(Exception exx){
					
				}
				failCount += 1;
				falidDocEntryList.add(docEntry);
				flag = CherryBatchConstants.BATCH_WARNING;
				
				// 错误日志
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00040");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
                //组织ID：
				batchLoggerDTO1.addParam(CherryBatchUtil.getString(paraMap.get(CherryBatchConstants.ORGANIZATIONINFOID)));
                //品牌ID：
				batchLoggerDTO1.addParam(CherryBatchUtil.getString(paraMap.get(CherryBatchConstants.BRANDINFOID)));
                //接口单据号
				batchLoggerDTO1.addParam(docEntry);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, ex);
			}
		}
	}
	
	/**
	 * 将发货单/退库单的主数据和明细数据组装到一个MAP中，供调用MQHelper模块时使用
	 * @param mainMap 主数据行
	 * @param detailList 明细数据行
	 * @return
	 * @throws Exception
	 */
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
		mainData[0] = CherryBatchUtil.getString(mainMap.get("Brand"));
		//单据号
		mainData[1] = CherryBatchUtil.getString(mainMap.get("DocEntry"));
		//更改次数
		mainData[2] = null;
		String invType = CherryBatchUtil.getString(mainMap.get("InvType"));
		String toShop = CherryBatchUtil.getString(mainMap.get("ToShop"));
		String shop = CherryBatchUtil.getString(mainMap.get("Shop"));
		if("SD".equals(invType)){
			//接受订货柜台
			mainData[3] = toShop;
			//关联柜台号
			mainData[4] = shop;
		} 
		else if("RJ".equals(invType)){
			//接受订货柜台
			mainData[3] = shop;
			//关联柜台号
			mainData[4] = toShop;
		}
		//总数量
		mainData[5] = CherryBatchUtil.getString(mainMap.get("TotalQuantity"));
		//总金额
		mainData[6] = CherryBatchUtil.getString(mainMap.get("TotalAmount"));
		//单据类型
		mainData[7] = "KS";
		//子类型 SD:发货单；RJ:退库单； 
		mainData[8]= invType;
		
		//关联单号
		mainData[9] = CherryBatchUtil.getString(mainMap.get("BaseEntry"));
		//发货理由
		mainData[10] = CherryBatchUtil.getString(mainMap.get("ErrCode"));
		
		//日期
		mainData[11] = CherryBatchUtil.getString(mainMap.get("TradeDate"));
		//时间
		mainData[12] = CherryBatchUtil.getString(mainMap.get("TradeTime"));
		
		// 发货前柜台库存总金额
		mainData[13] = null;
		// 发货后柜台库存总金额
		mainData[14] = null;
		// 会员号
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
			
			// 单据号
			temp[0] = CherryBatchUtil.getString(temMap.get("DocEntry"));
			// 修改次数
			temp[1] = null;
			// 员工code
			temp[2] = "TPDEALER";
			// 入出库区分
			temp[3] = null;
			// 产品条码
			temp[4] = CherryBatchUtil.getString(temMap.get("barCode"));
			// 厂商编码
			temp[5] = CherryBatchUtil.getString(temMap.get("unitCode"));
			// 仓库类型
			temp[6] = null;
			// 单品数量
			if("".equals(CherryBatchUtil.getString(temMap.get("Quantity")))){
				temp[7] = "0";
			}else{
				temp[7] = CherryBatchUtil.getString(temMap.get("Quantity"));
			}
			// 入出库前该商品柜台账面数量（针对盘点）
			temp[8] = null;
			//单品价格
			if("".equals(CherryBatchUtil.getString(temMap.get("price")))){
				temp[9] = "0";
			}else{
				temp[9] = CherryBatchUtil.getString(temMap.get("price"));
			}
			// 理由
			temp[10] = null;
			
			detailData.add(temp);
		}
		
		resultMap.put("DetailDataLine", detailData);
		
		//设定MQ_Log日志需要的数据
		Map<String, Object> mqLog = new HashMap<String,Object>();
		mqLog.put("BillType", "KS");
		mqLog.put("BillCode", mainData[1]);
		mqLog.put("CounterCode", mainData[3]);
		String date = mainData[11].replaceAll("-","");
		mqLog.put("Txddate", date);
		String time = mainData[12].replaceAll(":","");
		mqLog.put("Txdtime", time);
		mqLog.put("Source", "K3");
		mqLog.put("SendOrRece", "S");
		mqLog.put("ModifyCounts", "0");
		
		resultMap.put("Mq_Log", mqLog);
		
		return resultMap;
	}
	
	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOTYIN08");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOTYIN08");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		
		// 品牌Code
		String branCode = binOTYIN08_Service.getBrandCode(map);
		// 品牌Code
		baseMap.put(CherryConstants.BRAND_CODE, branCode);
		
		// 业务日期，日结标志
		Map<String, Object> bussDateMap = binOTYIN08_Service.getBussinessDateMap(map);
		// 业务日期
		String businessDate = CherryBatchUtil.getString(bussDateMap.get(CherryBatchConstants.BUSINESS_DATE));
		baseMap.put("businessDate", businessDate);
		
		return baseMap;
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
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		// 失败docEntry集合
		if(!CherryBatchUtil.isBlankList(falidDocEntryList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00041");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(falidDocEntryList.toString());
			logger.BatchLogger(batchLoggerDTO6);
		}
	}
	
	// ---------------------  以下代码为老代码-弃用     --------------------------------------------------------------------------------------------------
	/**
	 * 产品列表的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@Deprecated
	public int tran_batchOTYIN01_Old(Map<String, Object> map)
			throws CherryBatchException {
		// 初始化
		comMap = getComMap(map);
		
		while (true) {
			// 预处理可能失败的件数
			int prepFailCount = 0;
			try {
				
				// Step1:更新颖通发货单接口表的数据从[trxStatus is null ]更新为[trxStatus=1]
				Map<String, Object> tNullToT1Map = new HashMap<String, Object>();
				tNullToT1Map.putAll(comMap);
				tNullToT1Map.put("upCount", UPDATE_SIZE);
				int upCountSucc = binOTYIN08_Service.updTrxStatusNullTo1ForOT(tNullToT1Map);
				// 统计总条数(单据数量[1个发货单可能对应多条数据])
				totalCount += upCountSucc;
				prepFailCount = upCountSucc;
				
				// 没有可用于导出的数据时，结束程序
				if(upCountSucc == 0){
					break;
				}
				
				// (Step2:查询可用于导出的颖通发货单接口表数据并拼装成MQ及老后台MQ_Log
				List<Map<String,Object>> propSendMQList = propSendMQByTransForOT();
				
				// Step3:发货单接口表数据从[trxStatus=1]更新为[trxStatus=2]
				Map<String, Object> t1ToT2Map = new HashMap<String, Object>();
				t1ToT2Map.putAll(comMap);
				t1ToT2Map.put("trxStatus_Old", trxStatus_1);
				t1ToT2Map.put("trxStatus_New", trxStatus_2);
				binOTYIN08_Service.updTrxStatusForOT(t1ToT2Map);
				
				//Step4:调用MQHelper接口进行数据发送
				for(Map<String,Object> dataMap : propSendMQList){
					mqHelperImpl.sendData(dataMap, "posToCherryMsgQueue");
				}
				//事务提交
				binOTYIN08_Service.witManualCommit();
				binOTYIN08_Service.tpifManualCommit();
				
			} catch(Exception e){
				
				try{
					// 颖通数据源回滚
					binOTYIN08_Service.tpifManualRollback();
					//MQ_Log表中事物回滚
					binOTYIN08_Service.witManualRollback();
				} catch(Exception ex){
					
				}
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00004");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				// 失败时结束批处理
				// 程序出现异常后，后面的批处理依然会遇到这样的问题。
				break;
			}
		}
		outMessage();
		return flag;
	}
	
	/**
	 * 查询可用于导出的颖通发货单接口表数据[trxStatus=1]并拼装成MQ发货的新后台及老后台MQ_Log
	 * 
	 * @throws Exception 
	 */
	@Deprecated
	private List<Map<String,Object>> propSendMQByTransForOT() throws CherryBatchException, Exception{
		
		List<Map<String,Object>> propSendMQList = new ArrayList<Map<String,Object>>();
			// Step2:
			// 取得新后台同步状态为"同步处理中"[trxStatus=1]销售数据（主数据、明细数据）导出到颖通销售接口表
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(comMap);
			map.put("trxStatus", trxStatus_1);
			List<Map<String,Object>> exportTransListForOT = getExportTransListForOT(map);
			
			// 根据billCode对发货单据接口表的数据进行分组
			retList.clear();
			getSameArrayList(exportTransListForOT);
			List<List<Map<String,Object>>> groupList = this.getRetList();
			
			for(List<Map<String,Object>> itemList : groupList){
				
				Map<String,Object> mainMap = itemList.get(0);
				
				// 销售商品总数量
				BigDecimal totalQuantity = new BigDecimal(0);
				// 销售商品总金额
				BigDecimal totalAmount = new BigDecimal(0);
				for(Map<String,Object> itemMap : itemList){
					BigDecimal quantity =  (BigDecimal)itemMap.get("Quantity");
					BigDecimal price =  (BigDecimal)itemMap.get("price");
					totalQuantity = totalQuantity.add(quantity);
					totalAmount = totalAmount.add(price.multiply(quantity));
				}
				mainMap.put("TotalQuantity", totalQuantity);
				mainMap.put("TotalAmount", totalAmount);
				
				// 拼装MQ
				Map<String,Object> propSendMQMap = assemblingData(mainMap, itemList);
				propSendMQList.add(propSendMQMap);
			}
			
		return propSendMQList;
		
	}
	
	/**
	 * 取得颖通发货单接口表数据(并包括新后台的价格)
	 * @param map
	 * @return
	 * @throws CherryBatchException 
	 */
	@Deprecated
	private List<Map<String,Object>> getExportTransListForOT(Map<String, Object> map) throws CherryBatchException{
		List<Map<String,Object>> exportTransListForOT = binOTYIN08_Service.getExportTransListForOT(map);
		
		// 取得每单的价格
		Map<String,Object> paraMap = new HashMap<String, Object>();
		for(Map<String,Object> exportTransMapForOT : exportTransListForOT){
			String itemCode = (String)exportTransMapForOT.get("ItemCode");
//			String barCode = (String)exportTransMapForOT.get("Barcode");
			
//			Map<String,Object> paraMap = new HashMap<String, Object>();
			paraMap.clear();
			paraMap.putAll(comMap);
			paraMap.put("itemCode", itemCode);
//			paraMap.put("barCode", barCode);
			Map<String,Object> priceMap = binOTYIN08_Service.getProPrmPriceByUB(paraMap);
			
			if(null != priceMap && !priceMap.isEmpty()){
				
				exportTransMapForOT.put("price", priceMap.get("price"));
				exportTransMapForOT.put("unitCode", priceMap.get("unitCode"));
				exportTransMapForOT.put("barCode", priceMap.get("barCode"));
			} else {
				
				// 异常
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                batchExceptionDTO.setErrorCode("EOT00011");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                //组织ID：
                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(paraMap.get(CherryBatchConstants.ORGANIZATIONINFOID)));
                //品牌ID：
                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(paraMap.get(CherryBatchConstants.BRANDINFOID)));
                //接口单据号
                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(exportTransMapForOT.get("DocEntry")));
                // ItemCode
                batchExceptionDTO.addErrorParam(CherryBatchUtil.getString(itemCode));
                throw new CherryBatchException(batchExceptionDTO);
			}
			
		}
		
		return exportTransListForOT;
	}
	
	/**
	 * 根据billCode对发货单据接口表的数据进行分组
	 * @param arrayList
	 * @return
	 */
	@Deprecated
	public void getSameArrayList(List<Map<String,Object>> arrayList) {
		
		List<Map<String,Object>> sameList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> diffList = new ArrayList<Map<String,Object>>();
		sameList.add(arrayList.get(0));
		for (int i = 1, len = arrayList.size(); i < len; i++) {
			if (arrayList.get(i).get("DocEntry").equals(sameList.get(0).get("DocEntry"))) {
				sameList.add(arrayList.get(i));
			} else {
				diffList.add(arrayList.get(i));
			}
		}
		retList.add(sameList);
		if (!diffList.isEmpty()) {
			getSameArrayList(diffList);
		}
		
	}

	@Deprecated
	public List<List<Map<String,Object>>> getRetList() {
		return retList;
	}

}

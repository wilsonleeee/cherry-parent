/*	
 * @(#)BINBAT110_BL.java     1.0 @2015-6-02
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
package com.cherry.webserviceout.kingdee.productOrder.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webserviceout.kingdee.WebServiceKingdee;
import com.cherry.webserviceout.kingdee.productOrder.service.BINBAT110_Service;

/**
 *
 * Kingdee接口：产品订单推送BL
 * 
 * 新后台产品订单信息集合，通过WebService接口更新到Kingdee
 * @author jijw
 *
 * @version  2015-6-02
 */
public class BINBAT110_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT110_BL.class);	
	
	@Resource
	private BINBAT110_Service binbat110_Service;
	
	/** Kingdee WebService共通接口 */
	@Resource(name="webServiceKingdee")
	private WebServiceKingdee webServiceKingdee;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 100 */
	private final int BATCH_SIZE = 100;
	
	/** 同步状态:1 可同步 */
	private final String SYNCH_FLAG_1 = "1";
	
	/** 同步状态:2 同步处理中 */
	private final String SYNCH_FLAG_2 = "2";
	
	/** 同步状态:3 已完成 */
	private final String SYNCH_FLAG_3 = "3";
	
	private Map<String, Object> comMap = new HashMap<String, Object>();
	
	/** 处理总条数 */
	private int totalCount = 0;
//	/** 插入条数 */
//	private int insertCount = 0;
//	/** 更新条数 */
//	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = "";
	
	/** 导入失败的产品订单 */
	private List<String> faildPrtOrderList = new ArrayList<String>();
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchBat110(Map<String, Object> paraMap)
			throws CherryBatchException,Exception {

		// 初始化
		try{
			init(paraMap);
		}catch(Exception e){
			// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// Step1:查看新后台产品订单数据中是否存在处于同步中（synchFlag=2)的状态
//		int prtOrderCountBySync = getPrtOrderCountBySynch();
		
		// 更新新后台产品订单数据从[synchFlag=1]更新为[synchFlag=2]
		Map<String, Object> s1ToS2Map = new HashMap<String, Object>();
		s1ToS2Map.putAll(comMap);
		s1ToS2Map.put("synchFlag_Old", SYNCH_FLAG_1);
		s1ToS2Map.put("synchFlag_New", SYNCH_FLAG_2);
		binbat110_Service.updPrtOrderBySyncNew(s1ToS2Map);
		
		// 新后台数据源commit
//		binbat110_Service.manualCommit();
		
		// 定义推送失败的订单号集合ignoreOrderNoIFList
//		List<String> ignoreOrderNoIFList = new ArrayList<String>();
		
		// 上一批次(页)最后一条记录
		String bathLastRowID = "";
		
		while (true) {

			// Step2:
			// 取得新后台同步状态为"同步处理中"[synchFlag=2]产品订单数据 推送到KingdeeWebService
			Map<String, Object> selPrtOrderMap = new HashMap<String, Object>();
			selPrtOrderMap.putAll(comMap);
			selPrtOrderMap.put("synchFlag", SYNCH_FLAG_2);
			selPrtOrderMap.put("batchSize", BATCH_SIZE);
			selPrtOrderMap.put("bathLastRowID", bathLastRowID);
			List<Map<String,Object>> prtOrderList = binbat110_Service.getPrtOrderList(selPrtOrderMap);
			
			if (CherryBatchUtil.isBlankList(prtOrderList)) {
				break;
			} else {
				 // 统计总条数
				 totalCount += prtOrderList.size();
				try{
					// 处理新后台产品订单数据，推送送到kingdee webservice
					for(Map<String,Object> prtOrderMap : prtOrderList){
						
						List<Map<String,Object>> pushList = new ArrayList<Map<String,Object>>();
						pushList.add(prtOrderMap);
						pushPrtOrder(selPrtOrderMap, pushList);
						// GC
						pushList = null;
					}
					
				}catch(Exception e){
					
					flag = CherryBatchConstants.BATCH_WARNING;
					failCount += prtOrderList.size();
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EKD00011");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
				}
				
			}
			
			 // 当前批次最后一条数据的FSourceBillNo赋给bathLastRowID，用于当前任务下一批次(页)产品订单数据的筛选条件
			bathLastRowID = CherryBatchUtil.getString(prtOrderList.get(prtOrderList.size()- 1).get("FSourceBillNo"));
			
			 // 产品订单为空或少于一批次(页)处理数量，跳出循环
			 if (prtOrderList.size() < BATCH_SIZE) {
				 // GC
				 prtOrderList = null;
				 break;
			 }
			 // GC
			 prtOrderList = null;
		}
		
		// 日志
		outMessage();
		
		// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
		programEnd(paraMap);
		
		return flag;
	}
	
	/**
	 * 查看新后台产品订单数据中是否存在处于同步中（synchFlag=2）
	 * 可能是前次处理失败的，不确定是否已经推送到kingdee了，所以这里要再推送一次。
	 * @return
	 * @throws CherryBatchException 
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	private int getPrtOrderCountBySynch() throws CherryBatchException{
		
		// 取得新后台[synchFlag=2]的产品订单号List
		Map<String, Object> synch2Map = new HashMap<String, Object>(); // 同步处理中的产品订单数据
		synch2Map.putAll(comMap);
		synch2Map.put("synchFlag", SYNCH_FLAG_2);
		List<String> billCodeOfBackENDList = binbat110_Service.getOrderNoIFOfPrtOrderListBySync(synch2Map);
		
		return billCodeOfBackENDList.size();
	}
	
	/**
	 * →据将销售主表union明细表的导入到颖通销售接口表
	 * →将产品订单数据推送到KingdeeWebService
	 * →更新之前进行的产品订单数据从[synchFlag=2]更新为[synchFlag=3]
	 * 
	 * @param srCountBySync
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	private void updateDate(int prepFailCount) throws CherryBatchException{
		
		try {
			// Step2:
			// 取得新后台同步状态为"同步处理中"[synchFlag=2]产品订单数据 推送到KingdeeWebService
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(comMap);
			map.put("synchFlag", SYNCH_FLAG_2);
			List<Map<String,Object>> prtOrderListBySynchFlagList = binbat110_Service.getPrtOrderList(map);
			
			// 定义推送失败的订单号集合ignoreOrderNoIFList
			List<String> ignoreOrderNoIFList = null;
			
			if(null != prtOrderListBySynchFlagList && !prtOrderListBySynchFlagList.isEmpty()){
				// 处理新后台产品订单数据，推送送到kingdee webservice
				pushPrtOrder(map, prtOrderListBySynchFlagList);
			}
			
			try {
				// Step3:销售数据从[synchFlag=2]更新为[synchFlag=3]
				Map<String, Object> s2ToS3Map = new HashMap<String, Object>();
				s2ToS3Map.putAll(comMap);
				s2ToS3Map.put("ignoreOrderNoIFList", ignoreOrderNoIFList);
				s2ToS3Map.put("synchFlag_Old", SYNCH_FLAG_2);
				s2ToS3Map.put("synchFlag_New", SYNCH_FLAG_3);
				int updPrtOrderCount = binbat110_Service.updPrtOrderBySync(s2ToS3Map);
				
				// 新后台数据源commit
				binbat110_Service.manualCommit();
				
			} catch(Exception e){
				try{
					// 新后台数据源回滚
					binbat110_Service.manualRollback();
				}catch(Exception e2){
					
				}
				// 失败件数
				failCount += prepFailCount;
				flag = CherryBatchConstants.BATCH_WARNING;
				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EKD00013");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			}
		} catch (Exception e) {
			
			// 失败件数
			failCount += prepFailCount;
			flag = CherryBatchConstants.BATCH_WARNING;
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EKD00011");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO1);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
		}
		
	}
	
	/**
	 * 处理新后台产品订单数据，推送送到kingdee webservice
	 * @param map
	 * @param prtOrderList
	 * @param dataClass 数据类别 1：正常数据 0：失败数据
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void pushPrtOrder(Map<String,Object> map,List<Map<String,Object>> prtOrderList) throws CherryBatchException,Exception{
		
		// 定义推送失败的订单号集合ignoreOrderNoIFList
//		List<String> ignoreOrderNoIFList = new ArrayList<String>();
		
		// 电商第三方接口【推送产品订单】对应的数据
		final String tradeCode = "pushPrtOrder";
		map.put("tradeCode", tradeCode); // UpdateInventory
		
		String p_dataJsonStr = null;
		
		Map<String, Object> wsResultMap = null;
		try{
			//将查询出的结果集转换成json，dataJson --业务端Json数据包，业务数据参数请参考具体业务API说明
			p_dataJsonStr = CherryUtil.list2Json(prtOrderList);
			// 条件中插入参数p_dataJson
			map.put("p_dataJson",p_dataJsonStr);
			
			// 调用KIS WebService接口获取数据
			wsResultMap = webServiceKingdee.accessServerResult(map);
			
		}catch(Exception e){
			
			for(Map<String,Object> prtStockMap : prtOrderList){
				faildPrtOrderList.add(prtStockMap.toString());
			}
			flag = CherryBatchConstants.BATCH_WARNING;
			
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EKD00001");
			batchLoggerDTO.addParam(tradeCode);
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO, e);
			
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EKD00001");
			batchExceptionDTO.addErrorParam(tradeCode);
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			fReason = String.format("调用Kingdee Webservice（%1$s）失败。",tradeCode);
			
			throw new CherryBatchException(batchExceptionDTO);
		}
		
		// kis接口方法名
		String kisMethod = ConvertUtil.getString(map.get("kisMethod")); 
		
		if(null != wsResultMap && !wsResultMap.isEmpty()){
			// WS返回结果代码
			String result = ConvertUtil.getString(wsResultMap.get("Result"));
    		if(WebServiceKingdee.Result_200.equals(result)){
    			
    			//  返回业务处理结果数据，详情参考具体API说明
        		Map<String,Object> wsResultDataJsonMap = (Map<String, Object>) wsResultMap.get("DataJson");
        		String dataJsonResult = ConvertUtil.getString(wsResultDataJsonMap.get("Result"));
        		// 业务接口 返回结果代码
        		if(WebServiceKingdee.Result_200.equals(dataJsonResult)){
        			
        			// 产品列表
        			if (wsResultDataJsonMap.get("Data") instanceof List) {
        				List<Map<String,Object>> wsResultDataList = (List<Map<String, Object>>) wsResultDataJsonMap.get("Data"); 
        				
        				if(!CherryBatchUtil.isBlankList(wsResultDataList)){
        					
        					for(Map<String,Object> wsResultDataMap : wsResultDataList){
        						// 业务数据 处理结果
        						String fStatus = ConvertUtil.getString(wsResultDataMap.get("FStatus"));
        						String fSourceBillNo = ConvertUtil.getString(wsResultDataMap.get("FSourceBillNo"));
        							
        						if(WebServiceKingdee.FStatus_Failed.equals(fStatus)){
        							faildPrtOrderList.add(wsResultDataMap.toString());
        							failCount += 1;
        							flag = CherryBatchConstants.BATCH_WARNING;
        							
        							// 日志文件
									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
									batchLoggerDTO.setCode("EKD00002");
									batchLoggerDTO.addParam(wsResultDataMap.toString()); // 请求接口
									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
									logger.BatchLogger(batchLoggerDTO);
        						} else{
        							// 更新新后台产品订单状态
        							Map<String,Object> updMap = new HashMap<String, Object>();
        							updMap.put("synchFlag_New", SYNCH_FLAG_3);
        							updMap.put("FSourceBillNo", fSourceBillNo);
        							updMap.putAll(comMap);
        							binbat110_Service.updPrtOrderBySyncNew(updMap);
        						}
        					}
        				}else{
        					// 返回的Data的size为0，打日志
        					flag = CherryBatchConstants.BATCH_WARNING;
		        			failCount += prtOrderList.size();
		        			
		        			for(Map<String,Object> prtStockMap : prtOrderList){
		        				faildPrtOrderList.add(prtStockMap.toString());
		        				
		        				// 推送失败的订单号集合ignoreOrderNoIFList
//		        				String fSourceBillNo = ConvertUtil.getString(prtStockMap.get("FSourceBillNo"));
//		        				ignoreOrderNoIFList.add(fSourceBillNo);
		        			}
		        			
		        			// 数据返回异常 （非200）
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("EKD00003");
							batchLoggerDTO.addParam(tradeCode +"("+kisMethod+")"); // 请求接口
							
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							logger.BatchLogger(batchLoggerDTO);
							
							fReason = String.format("调用Kingdee Webservice（%1$s）失败，返回数据[DataJson.Data]的Size为0。",tradeCode +"("+kisMethod+")");
        				}
        				
        			}else{
        				// 产品列表为null ？？？？ ******************待处理*****************
//        				Object productOrderObj = wsResultDataJsonMap.get("Data");
        				flag = CherryBatchConstants.BATCH_WARNING;
        				fReason = "WebService返回的产品订单处理反馈列表为null";
        				logger.outLog(fReason, CherryBatchConstants.LOGGER_ERROR);
        				
        				for(Map<String,Object> prtStockMap : prtOrderList){
        					faildPrtOrderList.add(prtStockMap.toString());
        					
        					// 推送失败的订单号集合ignoreOrderNoIFList
//        					String fSourceBillNo = ConvertUtil.getString(prtStockMap.get("FSourceBillNo"));
//        					ignoreOrderNoIFList.add(fSourceBillNo);
        				}
        				
        			}
        			
        		}else{
        			
        			flag = CherryBatchConstants.BATCH_WARNING;
        			failCount += prtOrderList.size();
        			
        			for(Map<String,Object> prtStockMap : prtOrderList){
        				faildPrtOrderList.add(prtStockMap.toString());
        				
        				// 推送失败的订单号集合ignoreOrderNoIFList
//        				String fSourceBillNo = ConvertUtil.getString(prtStockMap.get("FSourceBillNo"));
//        				ignoreOrderNoIFList.add(fSourceBillNo);
        			}
        			
        			// 数据返回异常 （非200）
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EKD00004");
					batchLoggerDTO.addParam(tradeCode +"("+kisMethod+")"); // 请求接口
					batchLoggerDTO.addParam(result); // 返回异常的errorCode
					batchLoggerDTO.addParam(ConvertUtil.getString(wsResultDataJsonMap.get("ErrMsg"))); // 返回错误信息
					batchLoggerDTO.addParam(p_dataJsonStr); // 请求数据
					batchLoggerDTO.addParam(wsResultDataJsonMap.toString()); // 返回数据
					
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO);
					
					fReason = String.format("调用Kingdee Webservice（%1$s）失败。原因：Result=%2$s，ErrMsg=%3$s。请求数据:%4$s，返回数据:%5$s。",
									tradeCode + "(" + kisMethod + ")", 
									result,
									ConvertUtil.getString(wsResultDataJsonMap.get("ErrMsg")),
									p_dataJsonStr,
									wsResultDataJsonMap.toString());
        		}
    		}else{
    			
				flag = CherryBatchConstants.BATCH_WARNING;
				failCount += prtOrderList.size();
				
				for(Map<String,Object> prtStockMap : prtOrderList){
					faildPrtOrderList.add(prtStockMap.toString());
					
					// 推送失败的订单号集合ignoreOrderNoIFList
//					String fSourceBillNo = ConvertUtil.getString(prtStockMap.get("FSourceBillNo"));
//					ignoreOrderNoIFList.add(fSourceBillNo);
				}
				
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EKD00001");
				batchLoggerDTO.addParam(tradeCode +"("+kisMethod+")"); // 请求接口
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO);
    		}
        		
		}else {
			flag = CherryBatchConstants.BATCH_WARNING;
			
			failCount += prtOrderList.size();
			for(Map<String,Object> prtStockMap : prtOrderList){
				faildPrtOrderList.add(prtStockMap.toString());
				
				// 推送失败的订单号集合ignoreOrderNoIFList
//				String fSourceBillNo = ConvertUtil.getString(prtStockMap.get("FSourceBillNo"));
//				ignoreOrderNoIFList.add(fSourceBillNo);
			}
		}
//		return ignoreOrderNoIFList;
	}
	
	/**
	 * 程序结束时，处理Job共通( 插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		 
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("FReason", fReason);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}
	
	/**
	 * 程序初始化参数
	 * @param map
	 * @throws Exception 
	 * @throws CherryBatchException 
	 */
	private void init(Map<String, Object> map) throws CherryBatchException, Exception {
		// 设置共通参数
		setComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT110");
		
		// 程序【开始运行时间】
		String runStartTime = binbecm01_IF.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		
//		// 取得Job控制程序的数据截取开始时间及结束时间
//		Map<String, Object> jobControlInfoMap = binbecm01_IF.getJobControlInfo(map);
//		
//		// 程序【截取数据开始时间】
//		map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
//		// 程序【截取数据结束时间】
//		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));
		
		//是否测试模式（若是则包含测试部门）
		String testMod = binOLCM14_BL.getConfigValue("1080", ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),
				ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		map.put("testMod", testMod);
		
		comMap.putAll(map);
		
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT110");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT110");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		map.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());

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
//		// 插入件数
//		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
//		batchLoggerDTO3.setCode("IIF00003");
//		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
//		batchLoggerDTO3.addParam(String.valueOf(insertCount));
//		// 更新件数
//		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
//		batchLoggerDTO4.setCode("IIF00004");
//		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
//		batchLoggerDTO4.addParam(String.valueOf(updateCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		logger.BatchLogger(batchLoggerDTO2);
		// 插入件数
//		logger.BatchLogger(batchLoggerDTO3);
//		// 更新件数
//		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		// 失败ItemCode集合
		if(!CherryBatchUtil.isBlankList(faildPrtOrderList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EKD00015");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildPrtOrderList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "WebService服务端处理数据失败，具体见log日志";
		}
	}
	

}

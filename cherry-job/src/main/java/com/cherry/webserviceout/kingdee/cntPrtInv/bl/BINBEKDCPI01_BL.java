/*	
 * @(#)BINBEKDCPI01_BL.java     1.0 @2015-5-13
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
package com.cherry.webserviceout.kingdee.cntPrtInv.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webserviceout.kingdee.WebServiceKingdee;
import com.cherry.webserviceout.kingdee.cntPrtInv.service.BINBEKDCPI01_Service;

/**
 *
 * Kingdee接口：柜台产品库存更新BL
 * 
 * 新后台柜台产品库存信息集合，通过WebService接口更新到Kingdee
 * @author jijw
 *
 * @version  2015-5-13
 */
public class BINBEKDCPI01_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBEKDCPI01_BL.class);	
	
	@Resource
	private BINBEKDCPI01_Service binbekdcpi01_Service;
	
	/** Kingdee WebService共通接口 */
	@Resource(name="webServiceKingdee")
	private WebServiceKingdee webServiceKingdee;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 每批次(页)处理数量 100 */
	private final int BATCH_SIZE = 100;
	
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
	
	/** 导入失败的柜台产品库存 */
	private List<String> faildCntPrtInvList = new ArrayList<String>();
	
	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchkdcpi01(Map<String, Object> paraMap)
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
		
		// 处理之前失败的柜台产品库存数据
		handleFaildData(paraMap);
		
		// 处理符合条件的柜台产品库存数据
		handleNormalData(paraMap);
		
		// 日志
		outMessage();
		
		// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
		programEnd(paraMap);
		
		return flag;
	}
	
	/**
	 * 处理符合条件的柜台产品库存数据
	 * 
	 * @param paraMap
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void handleNormalData(Map<String, Object> paraMap) throws CherryBatchException,Exception{
		// 上一批次(页)最后一条记录
		String bathLastRowID = "";
		
		while (true) {

			// 查询柜台产品库存列表
			Map<String, Object> selPrtStockMap = new HashMap<String, Object>();
			selPrtStockMap.putAll(paraMap);
			selPrtStockMap.put("batchSize", BATCH_SIZE);
			selPrtStockMap.put("bathLastRowID", bathLastRowID);
			List<Map<String, Object>> prtStockList = binbekdcpi01_Service.getProductStockList(selPrtStockMap);
			
			if (CherryBatchUtil.isBlankList(prtStockList)) {
				break;
			} else {
				 // 统计总条数
				 totalCount += prtStockList.size();
				try{
					// 处理新后台柜台产品库存的数据，发送到kingdee webservice
					updateDate(paraMap, prtStockList,1);
					
				}catch(Exception e){
					
					flag = CherryBatchConstants.BATCH_ERROR;
					
					failCount += prtStockList.size();
					
					// 待定
//					binbekdcpi01_Service.manualRollback();
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00072");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					
					// 处理失败后，跳转至下一批次(页)
//					continue;
				}
				
			}
			
			 // 当前批次最后一条数据的RowID赋给bathLastRowID，用于当前任务下一批次(页)产品库存数据的筛选条件
			bathLastRowID = CherryBatchUtil.getString(prtStockList.get(prtStockList.size()- 1).get("RowID"));
			 
			 // 柜台产品库存为空或少于一批次(页)处理数量，跳出循环
			 if (prtStockList.size() < BATCH_SIZE) {
				 // GC
				 prtStockList = null;
				 
				 break;
			 }
			 // GC
			 prtStockList = null;
		}
	}
	
	/**
	 * 处理之前失败的柜台产品库存数据
	 * 
	 * @param paraMap
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void handleFaildData(Map<String, Object> paraMap) throws CherryBatchException,Exception{
		// 上一批次(页)最后一条记录
		String bathLastRowID = "";
		
		while (true) {
			
			// 查询柜台产品库存列表
			Map<String, Object> selPrtStockMap = new HashMap<String, Object>();
			selPrtStockMap.putAll(paraMap);
			selPrtStockMap.put("batchSize", BATCH_SIZE);
			selPrtStockMap.put("bathLastRowID", bathLastRowID);
			List<Map<String, Object>> faildPrtStockList = binbekdcpi01_Service.getFaildProductStockList(selPrtStockMap);
			
			if (CherryBatchUtil.isBlankList(faildPrtStockList)) {
				break;
			} else {
				// 统计总条数
				totalCount += faildPrtStockList.size();
				try{
					// 处理新后台柜台产品库存的数据，发送到kingdee webservice
					updateDate(paraMap, faildPrtStockList,0);
					
				}catch(Exception e){
					
					flag = CherryBatchConstants.BATCH_ERROR;
					
					failCount += faildPrtStockList.size();
					
					// 待定
//					binbekdcpi01_Service.manualRollback();
					
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00072");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO, e);
					
					// 处理失败后，跳转至下一批次(页)
//					continue;
				}
				
			}
			
			// 当前批次最后一条数据的RowID赋给bathLastRowID，用于当前任务下一批次(页)产品库存数据的筛选条件
			bathLastRowID = CherryBatchUtil.getString(faildPrtStockList.get(faildPrtStockList.size()- 1).get("RowID"));
			
			// 柜台产品库存为空或少于一批次(页)处理数量，跳出循环
			if (faildPrtStockList.size() < BATCH_SIZE) {
				break;
			}
		}
	}
	
	/**
	 * 处理新后台柜台产品库存的数据，发送到kingdee webservice
	 * @param map
	 * @param prtStockList
	 * @param dataClass 数据类别 1：正常数据 0：失败数据
	 * @throws CherryBatchException,Exception 
	 */
	@SuppressWarnings("unchecked")
	private void updateDate(Map<String,Object> map,List<Map<String,Object>> prtStockList,int dataClass) throws CherryBatchException,Exception{
		
		// 电商第三方接口取得获取更新柜台产品库存对应的数据
		final String tradeCode = "updCntPrtInventory";
		map.put("tradeCode", tradeCode); // UpdateInventory
		
		String p_dataJsonStr = null;
		
		Map<String, Object> wsResultMap = null;
		try{
			//将查询出的结果集转换成json，dataJson --业务端Json数据包，业务数据参数请参考具体业务API说明
			p_dataJsonStr = CherryUtil.list2Json(prtStockList);
			// 条件中插入参数p_dataJson
			map.put("p_dataJson",p_dataJsonStr);
			
			// 调用KIS WebService接口获取数据
			wsResultMap = webServiceKingdee.accessServerResult(map);
			
		}catch(Exception e){
			
			flag = CherryBatchConstants.BATCH_ERROR;
			failCount += prtStockList.size();
			
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EOT00059");
			batchLoggerDTO.addParam(tradeCode);
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			logger.BatchLogger(batchLoggerDTO, e);
			
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EOT00059");
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
        						
        						try {
        							// 设置失败履历表的参数
        							Map<String,Object> falidMap = new HashMap<String, Object>();
        							falidMap.putAll(comMap);
        							falidMap.put("UnionIndex", wsResultDataMap.get("FStockNumber"));
        							falidMap.put("UnionIndex1", wsResultDataMap.get("FItemID"));
        							
            						if(WebServiceKingdee.FStatus_Failed.equals(fStatus)){
            							
            							// 放入失败集合
            							// wsResultDataMap.toString() { "FItemID": "56956","FStockID": "2400","FStockPlaceID": "0","FStatus": "Successful","ErrMsg":null }
            							faildCntPrtInvList.add(wsResultDataMap.toString());
            							 
            							falidMap.put("ErrorMsg", ",{\"" +ConvertUtil.getString(wsResultDataMap.get("ErrMsg"))+ "\"}");
            							binbecm01_IF.mergeJobRunFaildHistory(falidMap);
            							
            							failCount += 1;
            							flag = CherryBatchConstants.BATCH_WARNING;
            							
            							// 日志文件
    									BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
    									batchLoggerDTO.setCode("EOT00068");
    									batchLoggerDTO.addParam(wsResultDataMap.toString()); // 请求接口
    									batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
    									logger.BatchLogger(batchLoggerDTO);
            						}else{
            							// 如果当前处理的是Job运行失败履历表的数据，那么当前如果成功就将其从运行失败履历表中删除
            							if( 0 == dataClass){
            								binbecm01_IF.delJobRunFaildHistory(falidMap);
            							}
            						}
        						}catch(Exception e){
        							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
        							batchLoggerDTO.setCode("EOT00073");
        							batchLoggerDTO.addParam(wsResultDataMap.toString()); // 请求接口
        							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
        							logger.BatchLogger(batchLoggerDTO);
        							
        							continue;
        						}

        					}
        				}else{
        					// 返回的Data的size为0，打日志
		        			flag = CherryBatchConstants.BATCH_ERROR;
		        			failCount += prtStockList.size();
		        			
		        			// 数据返回异常 （非200）
							BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
							batchLoggerDTO.setCode("EOT00066");
							batchLoggerDTO.addParam(tradeCode +"("+kisMethod+")"); // 请求接口
							
							batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
							logger.BatchLogger(batchLoggerDTO);
							
							fReason = String.format("调用Kingdee Webservice（%1$s）失败，返回数据[DataJson.Data]的Size为0。",tradeCode +"("+kisMethod+")");
        				}
        				
        			}else{
        				// 产品列表为null ？？？？ ******************待处理*****************
        				Object productObj = wsResultDataJsonMap.get("Data");
        				flag = CherryBatchConstants.BATCH_ERROR;
        				fReason = "WebService返回的柜台产品处理反馈列表为null";
        				logger.outLog(fReason, CherryBatchConstants.LOGGER_ERROR);
        			}
        			
        		}else{
        			
        			flag = CherryBatchConstants.BATCH_ERROR;
        			failCount += prtStockList.size();
        			
        			// 数据返回异常 （非200）
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOT00061");
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
											  
//					String errMsg = ConvertUtil.getString(resultDataJsonMap.get("ErrMsg"));
//    				String logmsg = String.format("调用Kingdee Webservice（%1$s）失败。原因：Result=%2$s，ErrMsg=%3$s。",kisMethod,dataJsonResult,errMsg);
//					logger.outLog(logmsg, CherryBatchConstants.LOGGER_ERROR);
					
        		}
    		}else{
    			
				flag = CherryBatchConstants.BATCH_ERROR;
				failCount += prtStockList.size();
				
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EOT00061");
				batchLoggerDTO.addParam(tradeCode +"("+kisMethod+")"); // 请求接口
				batchLoggerDTO.addParam(result); // 返回异常的errorCode
				batchLoggerDTO.addParam(ConvertUtil.getString(wsResultMap.get("ErrMsg"))); // 返回错误信息
				batchLoggerDTO.addParam(p_dataJsonStr); // 请求数据
				batchLoggerDTO.addParam(wsResultMap.toString()); // 返回数据
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				logger.BatchLogger(batchLoggerDTO);
				
    			// 非200
//    			String errMsg = ConvertUtil.getString(resultMap.get("ErrMsg"));
//				String logmsg = String.format("调用Kingdee Webservice（%1$s）失败。原因：Result=%2$s，ErrMsg=%3$s。",kisMethod,result,errMsg);
//				logger.outLog(logmsg, CherryBatchConstants.LOGGER_ERROR);
    		}
        		
		}else {
			// 异常处理  ******************待处理*****************
			// 共通里已打日志，此处不再处理
			flag = CherryBatchConstants.BATCH_ERROR;
			
			for(Map<String,Object> prtStockMap : prtStockList){
				faildCntPrtInvList.add(prtStockMap.toString());
				failCount += 1;
			}
		}
		
	}
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		
		String targetDataStartTime = ConvertUtil.getString(paraMap.get("TargetDataStartTime"));
		if(!CherryBatchUtil.isBlankString(targetDataStartTime)){
			// 程序结束时，更新Job控制表 
			binbecm01_IF.updateJobControl(paraMap);
		}
		 
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
		map.put("JobCode", "BAT107");
		
		// 程序【开始运行时间】
		String runStartTime = binbecm01_IF.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		
		// 取得Job控制程序的数据截取开始时间及结束时间
		Map<String, Object> jobControlInfoMap = binbecm01_IF.getJobControlInfo(map);
		
		// 程序【截取数据开始时间】
		map.put("TargetDataStartTime", jobControlInfoMap.get("TargetDataStartTime"));
		// 程序【截取数据结束时间】
		map.put("TargetDataEndTime", jobControlInfoMap.get("TargetDataEndTime"));
		
		comMap.putAll(map);
		
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private void setComMap(Map<String, Object> map) {
		
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEKDCPI01");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEKDCPI01");
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
		if(!CherryBatchUtil.isBlankList(faildCntPrtInvList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00067");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildCntPrtInvList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "WebService服务端处理数据失败，具体见log日志";
		}
	}
	

}

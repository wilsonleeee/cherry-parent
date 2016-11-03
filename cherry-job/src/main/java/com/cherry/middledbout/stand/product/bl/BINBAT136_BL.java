/*	
 * @(#)BINBAT136_BL.java     1.0 @2016-02-14		
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
package com.cherry.middledbout.stand.product.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.middledbout.stand.product.service.BINBAT136_Service;


/**
 * 
 * 柜台产品导入(标准接口)(IF_ProductByCounter)BL
 * @author jijw
 *  * @version 1.0 2016.02.14
 *
 */
public class BINBAT136_BL {
	
	/** 打印当前类的日志信息 **/
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT136_BL.class);
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	@Resource
	private BINBAT136_Service binBAT136_Service;
	
	/** 各类编号取号共通BL */
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 每批次(页)处理数量 1000 */
	private final int BATCH_SIZE = 1000;
	
	private Map<String, Object> comMap;
	
	/** 销售主数据每次导出数量:2000条 */
	private final int UPDATE_SIZE = 2000;	
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/** 导入失败的itemCode */
	private List<String> faildList = new ArrayList<String>();
	
	/** 失败的主要原因，受字段长度限制，这里只要记录主要原因即可 */
	private String fReason = new String();
	private StringBuffer fReasonBuffer = new StringBuffer();

	/**
	 * batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws CherryBatchException
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchExec(Map<String, Object> paraMap) throws CherryBatchException,Exception {
		
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
		
		// 处理符合条件的柜台产品信息数据
		handleNormalData(paraMap);
		
		// 日志
		outMessage();
		
		// 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
		programEnd(paraMap);
		
		
		return flag;
		
	}

	/**
	 * 处理符合条件的柜台产品信息数据
	 * @param paraMap
	 * @throws CherryBatchException
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void handleNormalData(Map<String, Object> paraMap) throws CherryBatchException, Exception {
		
		// 定义存放产品方案主表的编码对应的主表ID(SolutionCode > BIN_ProductPriceSolutionID)
		Map<String, Object> prtSolutionMap = new HashMap<String, Object>();
		
		// =========== Step1: 取得标准柜台产品接口表中的柜台信息(A、生成对应的产品方案主表数据 B、生成柜台与产品方案的关联表数据)
		List<Map<String, Object>> cntIFList = null;
		try{
			// 查询新后台的柜台信息
			List<Map<String, Object>> cntByCherryBrandMapList = binBAT136_Service.getCounterByCherryBrand(paraMap);
			Map<String, Object> cntByCherryBrandMap = new HashMap<String, Object>();
			for(Map<String, Object> counterInfo : cntByCherryBrandMapList){
				cntByCherryBrandMap.put(ConvertUtil.getString(counterInfo.get("CounterCode")), counterInfo.get("CounterNameIF"));
			}
			Set<String> cntInfoSet = cntByCherryBrandMap.keySet();			

			// 获取标准IF柜台产品中柜台号数据
			cntIFList = binBAT136_Service.getCntByIFProductByCounter(paraMap);
			
			if (CherryBatchUtil.isBlankList(cntIFList)) {
				return;
			}else{
				
				for(int i = 0;i< cntIFList.size(); i++){
					Map<String, Object> cntIFMap = cntIFList.get(i);
					String counterCodeByIF = ConvertUtil.getString(cntIFMap.get("CounterCode"));
					cntIFMap.putAll(paraMap);					

					// 查询新后台的柜台信息
					//Map<String, Object> cntByCherryBrandMap = binBAT136_Service.getCounterByCherryBrand(cntIFMap);
					// =========== Step1.A 生成对应的产品方案主表数据
					
					String solutionName = null; // 拼接产品方案名称(柜台名称 + "默认产品方案" + "(柜台号)")
					if(cntInfoSet != null && cntInfoSet.contains(counterCodeByIF)){
						// 拼接产品方案名称(柜台名称 + "默认产品方案" + "(柜台号)")
						solutionName =  ConvertUtil.getString(cntByCherryBrandMap.get(counterCodeByIF)) + "默认产品方案(" + counterCodeByIF + ")"; 
						
					}else{
						solutionName = counterCodeByIF + "默认产品方案(" + counterCodeByIF + ")"; 
						logger.outLog("柜台信息不存在，柜台编号(" + counterCodeByIF + ")" , CherryBatchConstants.LOGGER_ERROR);
						flag = CherryBatchConstants.BATCH_WARNING;
					}
					
					cntIFMap.put("solutionName", solutionName);
					cntIFMap.put("EndDate", "2100-01-01 23:59:59");
					cntIFMap.put("Comments", "导入程序自动生成柜台默认产品方案");
					
					try{
						// merge产品方案主表
						Map<String, Object> newPrtSoluMap = binBAT136_Service.mergeProductPriceSolution(cntIFMap);
						if(null != newPrtSoluMap && !newPrtSoluMap.isEmpty()){
							String newSolutionCode = ConvertUtil.getString(newPrtSoluMap.get("SolutionCode"));
							String newProductPriceSolutionID = ConvertUtil.getString(newPrtSoluMap.get("BIN_ProductPriceSolutionID"));
							
							cntIFMap.put("BIN_SolutionId", newProductPriceSolutionID);
							
							if(!prtSolutionMap.containsKey(newSolutionCode)){
								prtSolutionMap.put(newSolutionCode, newProductPriceSolutionID);
							}
						}
						
					} catch(Exception e){
						// 【柜台方案导入（标准接口）】处理产品方案主表失败。产品方案编码：{0}！
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EOT00114");
						batchLoggerDTO.addParam(counterCodeByIF);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						logger.BatchLogger(batchLoggerDTO, e);
						flag = CherryBatchConstants.BATCH_WARNING;
					}
					
					// =========== Step1.B 生成柜台与产品方案的关联表数据					
					try{
						// 更新产品方案与部门关联表
						binBAT136_Service.mergePrtSoluDepartRelation(cntIFMap);		
						
					}catch(Exception e){
						// EOT00115=【柜台方案导入（标准接口）】处理产品方案与部门关联表失败。产品方案编码：({0})，柜台编码：({1})。
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EOT00115");
						batchLoggerDTO.addParam(counterCodeByIF);
						batchLoggerDTO.addParam(counterCodeByIF);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						logger.BatchLogger(batchLoggerDTO, e);
						flag = CherryBatchConstants.BATCH_WARNING;
					}
					if ((i>0 && i%UPDATE_SIZE==0) || i>= cntIFList.size()-1) {
						binBAT136_Service.manualCommit();
					}
				}

			}
			
		}catch(Exception e){
			binBAT136_Service.manualRollback();
			flag = CherryBatchConstants.BATCH_ERROR;
			// 处理柜台对应的柜台号及产品方案失败。
			logger.outLog(" 处理柜台对应的柜台号及产品方案失败。" , CherryBatchConstants.LOGGER_ERROR);
			logger.outExceptionLog(e);
			return;
		}
		
		// =========== Step2: 处理产品方案明细
		try{
			// =========== Step2.A 更新标准接口表柜台产品数据从[synchFlag=null]更新为[synchFlag=1] 
			try {
				binBAT136_Service.updIFProductByCounterBySync1(paraMap);
				binBAT136_Service.tpifManualCommit();
			} catch (Exception e) {
				//binBAT136_Service.manualRollback();
				flag = CherryBatchConstants.BATCH_ERROR;
				// 处理柜台对应的柜台号及产品方案失败。
				logger.outLog(" 处理柜台对应的柜台号及产品方案失败。" , CherryBatchConstants.LOGGER_ERROR);
				logger.outExceptionLog(e);
				throw e;
			}		
			
			// 取得标准接口表的柜台产品数据
			
			// 上一批次(页)最后一条PuttingTime
			String bathLastPuttingTime = "";
			
			while (true) {
				// 查询接口柜台产品列表
				Map<String, Object> paraMap2 = new HashMap<String, Object>();
				paraMap2.putAll(paraMap);
				paraMap2.put("batchSize", UPDATE_SIZE);
				List<Map<String, Object>> standardProductByCounterList = binBAT136_Service.getStandardProductByCounterList(paraMap2);
				if (CherryBatchUtil.isBlankList(standardProductByCounterList)) {
					break;
				}
				// =========== Step2.B --分组同一CounterCode下的产品
				// 定义存放以counterCode分组的柜台产品集合
				Map<String, List<Map<String, Object>>> groupCntPrtMapList = new HashMap<String, List<Map<String, Object>>>();
				for(Map<String, Object> standardProductByCounter : standardProductByCounterList){
					// 如果在这个map中包含有相同的柜台号，这创建一个集合将其存起来
					String cntCodeByIF = ConvertUtil.getString(standardProductByCounter.get("CounterCode"));
					
					if (groupCntPrtMapList.containsKey(cntCodeByIF)) {
						List<Map<String, Object>> syn = groupCntPrtMapList.get(cntCodeByIF);
						syn.add(standardProductByCounter);
						// 如果没有包含相同的柜台号，在创建一个集合保存数据
					} else {
						List<Map<String, Object>> cntPrtList = new ArrayList<Map<String, Object>>();
						cntPrtList.add(standardProductByCounter);
						groupCntPrtMapList.put(cntCodeByIF, cntPrtList);
					}
				}
				/** 写入中间表更新参数集合*/
				List<Map<String,Object>> ifUpdParamMapList = new ArrayList<Map<String,Object>>();
				/** 写入柜台对应的默认产品方案明细更新参数集合*/
				List<Map<String,Object>> targetUpdParamMapList = new ArrayList<Map<String,Object>>();
				// 逐个处理同一柜台(产品方案)下的的产品信息
				for (Map.Entry<String, List<Map<String, Object>>> groupCntPrtMap : groupCntPrtMapList.entrySet()) {
					
					List<Map<String, Object>> cntPrtList = groupCntPrtMap.getValue();
					for (Map<String, Object> cntPrtMap : cntPrtList) {
						
						String cntCodeByIF = ConvertUtil.getString(cntPrtMap.get("CounterCode")); // 柜台号
						String IFProductId = ConvertUtil.getString(cntPrtMap.get("IFProductId")); // 产品唯一性标识
						cntPrtMap.putAll(paraMap);
						// 查询产品在新后台是否存在
						int oldPrtId = binBAT136_Service.searchProductId(cntPrtMap);
						if(oldPrtId == 0){
							logger.outLog("产品信息不存在，IFProductId(" + IFProductId + ")" , CherryBatchConstants.LOGGER_ERROR);
							flag = CherryBatchConstants.BATCH_WARNING;
							failCount++;
							Map<String, Object> sToS3Map = new HashMap<String, Object>();
							sToS3Map.putAll(paraMap);
							sToS3Map.put("CounterCode", cntCodeByIF);
							sToS3Map.put("IFProductId", IFProductId);
							binBAT136_Service.updIFProductByCounterBySync3(sToS3Map);
						}else{
							
							// =========== Step2.C --写入柜台对应的默认产品方案明细
							cntPrtMap.put("BIN_ProductID", oldPrtId);
							cntPrtMap.put("BIN_ProductPriceSolutionID", prtSolutionMap.get(cntCodeByIF)); // 产品方案主表ID
							
							try{
								Map<String, Object> mergeResultMap = binBAT136_Service.mergeProductPriceSolutionDetail(cntPrtMap);		
								
								Map<String, Object> sToS2Map = new HashMap<String, Object>();
								sToS2Map.putAll(paraMap);
								sToS2Map.put("CounterCode", cntCodeByIF);
								sToS2Map.put("IFProductId", IFProductId);
								binBAT136_Service.updIFProductByCounterBySync2(sToS2Map);
							}catch(Exception ex){
								
								flag = CherryBatchConstants.BATCH_WARNING;
								failCount++;
								
								Map<String, Object> sToS3Map = new HashMap<String, Object>();
								sToS3Map.putAll(paraMap);
								sToS3Map.put("CounterCode", cntCodeByIF);
								sToS3Map.put("IFProductId", IFProductId);
								binBAT136_Service.updIFProductByCounterBySync3(sToS3Map);
								
								// EOT00116=【柜台方案导入（标准接口）】处理写入产品方案明细表失败。产品方案编码：({0})，柜台编码：({1})，IFProductID：({2})。	
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("EOT00116");
								batchLoggerDTO.addParam(cntCodeByIF);
								batchLoggerDTO.addParam(cntCodeByIF);
								batchLoggerDTO.addParam(IFProductId);
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
								logger.BatchLogger(batchLoggerDTO, ex);
							}
						}
					}
				}
				binBAT136_Service.manualCommit();
				binBAT136_Service.tpifManualCommit();
				 // 当前批次最后一个产品的PuttingTime赋给bathLastPuttingTime，用于当前任务下一批次(页)柜台产品数据的筛选条件
				bathLastPuttingTime = CherryBatchUtil.getString(standardProductByCounterList.get(standardProductByCounterList.size()- 1).get("PuttingTime"));
				// 统计总条数
				totalCount += standardProductByCounterList.size();
				// 接口产品列表为空或产品数据少于一批次(页)处理数量，跳出循环
				if (standardProductByCounterList.size() < BATCH_SIZE) {
					 break;
				}
			}
			
		}catch(Exception e){
			binBAT136_Service.manualRollback();
			binBAT136_Service.tpifManualRollback();
			flag = CherryBatchConstants.BATCH_ERROR;
			// 处理产品方案明细失败
			logger.outLog("处理产品方案明细失败。" , CherryBatchConstants.LOGGER_ERROR);
			logger.outExceptionLog(e);
		}
	}
	
	/**
	 * init 
	 * @param map
	 */
	private void init(Map<String, Object> map) throws CherryBatchException, Exception{
		comMap = getComMap(map);
		
		// BatchCD 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
//		map.put("JobCode", "BAT136");
		comMap.put("JobCode", "BAT136");
		
		// 程序【开始运行时间】
		String runStartTime = binBAT136_Service.getSYSDateTime();
		// 作成日时
		comMap.put("RunStartTime", runStartTime);
		
		// 取得当前部门(柜台)产品表版本号
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.putAll(map);
		seqMap.put("type", "F");
		String pdTVersion = binOLCM15_BL.getCurrentSequenceId(seqMap);
		comMap.put("pdTVersion", pdTVersion);
		
		map.putAll(comMap);
	}
	
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		
		paraMap.putAll(comMap);
		
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		paraMap.put("UCNT", updateCount);
		paraMap.put("ICNT", insertCount);
		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
 
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
		
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT136");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT136");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 品牌ID
		baseMap.put(CherryBatchConstants.BRAND_CODE, map.get(CherryBatchConstants.BRAND_CODE).toString());
		
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
		// 插入件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00003");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00004");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(updateCount));
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
		logger.BatchLogger(batchLoggerDTO3);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO4);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO5);
		
		
		// 失败
		if(!CherryBatchUtil.isBlankList(faildList)){
			BatchLoggerDTO batchLoggerDTO6 = new BatchLoggerDTO();
			batchLoggerDTO6.setCode("EOT00100");
			batchLoggerDTO6.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO6.addParam(faildList.toString());
			logger.BatchLogger(batchLoggerDTO6);
			
			fReason = "柜台产品导入部分数据处理失败，具体见log日志";
			
		}
		
	}
}

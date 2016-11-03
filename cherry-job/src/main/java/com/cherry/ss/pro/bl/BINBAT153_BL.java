/*	
 * @(#)BINBAT153_BL.java     1.0 @2016-7-10		
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
package com.cherry.ss.pro.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.service.BINOLCM06_Service;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ss.pro.service.BINBAT153_Service;

/**
 *
 * 标准接口 ：产品入出库批次成本导出 BL
 *
 * @author zw
 *
 * @version  2016-7-10
 */
public class BINBAT153_BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT153_BL.class);	
	@Resource
	private BINBAT153_Service binBAT153_Service;
	
	/** 系统配置项 共通 **/
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLCM06_Service")
	private BINOLCM06_Service binOLCM06_Service;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 产品入出库批次成本每次导出数量:1000条 */
	private final int UPDATE_SIZE = 1000;
	
	/** 产品入出库批次成本记录同步状态:1 同步处理中 */
	private final String SYNCH_FLAG_1 = "1";
	
	/** 产品入出库批次成本记录同步状态:2 已完成 */
	private final String SYNCH_FLAG_2 = "2";
	
	/** 产品入出库批次成本记录同步状态:3 同步异常 */
	private final String SYNCH_FLAG_3 = "3";
	
	private Map<String, Object> comMap;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 产品列表的batch处理
	 * 
	 * @param 无
	 * 
	 * @return Map
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public int tran_batchbat153(Map<String, Object> map)
			throws Exception {
		// 初始化
		comMap = getComMap(map);
		// 读取配置项 是否记录产品入出库成本
		String config = binOLCM14_BL.getConfigValue("1365", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		// 记录产品入出库成本时，程序开始执行
		if("1".equals(config)){
			
			while (true) {
				// 预处理可能失败的件数
				int prepFailCount = 0;
				try {
					
//					// 取出产品入出库批次明细表中有没有成本的产品批次ID List（CostPrice is null)
//					List<String> costPriceNullBillCodeList = binBAT153_Service.getBillCodeOfNullCostPrice(comMap);
//					
//					// 将入出库批次明细表中有没有成本的单据号List转化为数组
//					String[] costPriceNullBillCodeListArr = new String[costPriceNullBillCodeList.size()];
//					for(int i =0;i<costPriceNullBillCodeList.size();i++){
//						costPriceNullBillCodeListArr[i]=costPriceNullBillCodeList.get(i).toString();
//					}
//					comMap.put("costPriceNullBillCodeListArr", costPriceNullBillCodeListArr);
//					comMap.put("costPriceNullBillCodeList", costPriceNullBillCodeList);
					
					// Step1:查看新后台产品入出库批次数据中是否存在处于同步异常且未导入到接口的数据（synchFlag=3)
					int srCountBySync = getSRCountBySynch();

					// 更新新后台产品入出库批次数据（主表）从[synchFlag=NULL]更新为[synchFlag=1]
					Map<String, Object> s1ToS2Map = new HashMap<String, Object>();
					s1ToS2Map.putAll(comMap);
					s1ToS2Map.put("upCount", UPDATE_SIZE);
					s1ToS2Map.put("synchFlag_New", SYNCH_FLAG_1);
					// 取出UPDATE_SIZE 条，synchFlag=NULL的后台产品入出库批次数据（有成本的）
					List<String> billCodeListOfNullSyn = binBAT153_Service.getBillCodeListOfNullSyn(s1ToS2Map);
					
					String[] billCodeListOfNullSynArr = new String[billCodeListOfNullSyn.size()];
					for(int i =0;i<billCodeListOfNullSyn.size();i++){
						billCodeListOfNullSynArr[i]=billCodeListOfNullSyn.get(i).toString();
					}
					
					s1ToS2Map.put("billCodeListOfNullSyn", billCodeListOfNullSyn);
					s1ToS2Map.put("billCodeListOfNullSynArr", billCodeListOfNullSynArr);
					// 修改后台产品入出库批次主表数据（有成本的）的synchFlag（由null改为1）
					int upCountSucc = binBAT153_Service.updProBatBySynNull(s1ToS2Map);
					//  产品入出库批次主表明细（有成本的）的synchFlag（由null改为1）
					binBAT153_Service.updDetailProBatBySynNull(s1ToS2Map);
					// 新后台数据源commit
					binBAT153_Service.manualCommit();
					
					// 当前需要导入接口的数据count
					int impCount = upCountSucc + srCountBySync;
					prepFailCount = impCount;
					// 统计总条数
					totalCount += impCount;
					
					// 没有可用于导出的数据时，结束程序
					if( impCount == 0){
						break;
					}
					
					// (Step2、Step3)取得新后台同步状态为"同步处理中"[synchFlag=1]的品入出库批次数据插入接口表
					insertOTProBat(prepFailCount);
					
					// Step2、Step3失败时结束批处理
					// 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
					if(flag == CherryBatchConstants.BATCH_WARNING || impCount < UPDATE_SIZE){
						break;
					}
					
				} catch(Exception e){
					// 新后台数据源回滚
					binBAT153_Service.manualRollback();
					// 失败件数
					failCount += prepFailCount;
					flag = CherryBatchConstants.BATCH_WARNING;
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EOT00174");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					logger.BatchLogger(batchLoggerDTO1);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					// 失败时结束批处理
					// 程序出现异常后，后面的批处理依然会遇到这样的问题。
					break;
				}
			}
		}
		
		programEnd(map);
		outMessage();
		return flag;
	}
	
	/**
	 * 查看新后台产品入出库批次数据中是否存在处于同步异常的数据（synchFlag=3）
	 * 若存在，则查询其是否已导出到接口（是：将新后台的同步状态由同步中[synchFlag=3]改为已完成[synchFlag=2]）
	 * @param 
	 * @return 
	 * @throws CherryBatchException 
	 */
	private int getSRCountBySynch() throws CherryBatchException{
		
		// 取得新后台产品入出库批次（主表）[synchFlag=3]的单据号List
		Map<String, Object> synch2Map = new HashMap<String, Object>(); 
		synch2Map.putAll(comMap);
		synch2Map.put("synchFlag", SYNCH_FLAG_3);
		// 产品入出库批次主表中同步状态为同步异常（3）的数据(单据号和接口单据号，入出库批次ID集合)		
		List<Map<String,Object>> billCodeOfSy3List=binBAT153_Service.getBillCodeOfSy3(synch2Map);
		// 产品入出库批次主表中同步状态为同步异常（3）的数据不为空
		if(null != billCodeOfSy3List && !billCodeOfSy3List.isEmpty()){
			int billCodeOfSy3Listsize= billCodeOfSy3List.size();
			// 循环数组，查询接口表中中是否存在相关数据
			for(Map<String,Object> mainMap : billCodeOfSy3List){
				Map<String,Object> bcMap = new HashMap<String, Object>();
				bcMap.putAll(mainMap);
				bcMap.putAll(comMap);
				// 根据产品入出库批次主表[synchFlag=3]的单据号和接口单据号查询接口表是否存在（存在则说明已被产品入出库批次主表数据已导出到接口）
				List<String> billCodeListOfOT = binBAT153_Service.getListByBillCodeForOT(bcMap);
				// 将已导出到接口表的单据号集合作为条件更新产品入出库批次的同步状态([synchFlag=3]更新为[synchFlag=2])(已导入到接口表但是同步状态为同步异常的)
				if(null != billCodeListOfOT && !billCodeListOfOT.isEmpty()){
					bcMap.put("synchFlag_New", SYNCH_FLAG_2);
					try{
						// 将已导入到接口表中入出库批次数据的同步状态由三改为二
						binBAT153_Service.updProBatBySync(bcMap);
						// 将已导入到接口表中入出库批次数据（明细）的同步状态由三改为二
						binBAT153_Service.updProBatDeatilBySync(bcMap);
						// 新后台数据源Commit
						binBAT153_Service.manualCommit();
						// 异常数据集合数减一
						billCodeOfSy3Listsize=billCodeOfSy3Listsize-1;
						
					}catch(Exception e){
						try{
							// 新后台数据源数据回滚
							binBAT153_Service.manualRollback();
						}catch(Exception e2){
							
						}
						// 写入日志
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EOT00172");
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(bcMap.get("RelevanceNo")));
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(bcMap.get("TradeNoIF")));
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						logger.BatchLogger(batchLoggerDTO1);
						logger.outExceptionLog(e);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
					
				  } 
					
				}
			      return billCodeOfSy3Listsize;
			 }
				// 不存在同步异常额数据时，直接返回0
				return 0;
	      }
	
		
	/**
	 * →产品入出库批次导入到接口表
	 * →更新之前进行的产品入出库批次数据从[synchFlag=1]更新为[synchFlag=2]
	 * 
	 * @param srCountBySync
	 * @throws CherryBatchException
	 */
	private void insertOTProBat(int prepFailCount) throws CherryBatchException{
		
			// Step2:
			// 取得新后台同步状态为"同步处理中"[synchFlag=1]和"同步处理异常"[synchFlag=3]产品入出库批次（主数据、明细数据）导出到接口表
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(comMap);
			// 产品入出库批次主数据同步状态为同步中和同步异常的数据（同步状态不为LG和AR的）
			List<Map<String,Object>> proBatList = binBAT153_Service.getProBatList(map);
			// 将产品批次主数据导入接口表
			if(null!=proBatList && !proBatList.isEmpty()){
				for(Map<String,Object> mainMap : proBatList){
					mainMap.putAll(comMap);
					try{
						mainMap.put("synchFlag_New", SYNCH_FLAG_2);
						// 根据主表数据中的单据号查询，需要导入到明细表中的数据
						List<Map<String,Object>> proBatDetailList = binBAT153_Service.getBatDetailListNew(mainMap);
						// 给导出到接口表的数据添加次，参数brandCode
						for(int j=0; j<proBatDetailList.size();j++){
							proBatDetailList.get(j).put("brandCode", mainMap.get("brandCode"));
							proBatDetailList.get(j).put("TradeNoIF", mainMap.get("TradeNoIF"));
							
						}
						
						// 插入产品入出库批次数据
						binBAT153_Service.insertProBat(mainMap);
						// 插入产品批次明细数据数据
						binBAT153_Service.insertProBatDetailNew(proBatDetailList);
						// 数据源commit
						binBAT153_Service.tpifManualCommit();
						try{
							
							// 修改同步状态为2（主表）
							binBAT153_Service.updIFProBat(mainMap);
							// 修改同步状态为2（明细）
							binBAT153_Service.updIFProBatDetailNew(mainMap);
							// 新后台数据源提交
							binBAT153_Service.manualCommit();
						}catch(Exception e){
							// 新后台数据源回滚
							binBAT153_Service.manualRollback();
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							throw new CherryBatchException(batchExceptionDTO);
						}
						
					}catch(Exception e){
						
						// 数据源回滚
						binBAT153_Service.tpifManualRollback();
						// 失败条数加一
						failCount +=1;
						mainMap.put("synchFlag_New", SYNCH_FLAG_3);
						try{
							// 产品入出库批次数据的同步状态修改为三
							binBAT153_Service.updIFProBat(mainMap);
							// 产品入出库批次数据（明细）的同步状态修改为三
							binBAT153_Service.updIFProBatDetailNew(mainMap);
							// 新后台数据源提交
							binBAT153_Service.manualCommit();
							
						}catch(Exception e2){
							// 新后台数据源回滚
							binBAT153_Service.manualRollback();
							flag = CherryBatchConstants.BATCH_WARNING;
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("EOT00175");
							// 关联单据号
							batchLoggerDTO1.addParam(CherryBatchUtil.getString(mainMap.get("RelevanceNo")));
							batchLoggerDTO1.addParam(CherryBatchUtil.getString(mainMap.get("TradeNoIF")));
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
							logger.BatchLogger(batchLoggerDTO1);
							logger.outExceptionLog(e2);
							CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
							cherryBatchLogger.BatchLogger(batchLoggerDTO1, e2);
							
						}
						// 写入日志
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						batchLoggerDTO1.setCode("EOT00173");
						// 关联单据号
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(mainMap.get("RelevanceNo")));
						batchLoggerDTO1.addParam(CherryBatchUtil.getString(mainMap.get("TradeNoIF")));
						batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
						logger.BatchLogger(batchLoggerDTO1);
						logger.outExceptionLog(e);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
						// 主表单条数据出现错误时，打印日志
						continue;
					}
				}
				
			}
			
	}
	
	/**
	 * 共通Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();

		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINBAT153");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINBAT153");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID, map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString());
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID, map.get(CherryBatchConstants.BRANDINFOID).toString());
		// 是否测试模式
		String testMod = binOLCM14_BL.getConfigValue("1080",
				String.valueOf(baseMap.get("organizationInfoId")),
				String.valueOf(baseMap.get("brandInfoId")));
		baseMap.put("testMod", testMod);	
		baseMap.put("JobCode", "BAT153");
		//品牌code
		baseMap.put(CherryBatchConstants.BRAND_CODE,binOLCM06_Service.getBrandCode(map));
		
		return baseMap;
	}
	
	/**
	 * 程序结束时，处理Job共通(插入Job运行履历表)
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
//		paraMap.put("FReason", fReasonBuffer.append(fReason).toString());
		binbecm01_IF.insertJobRunHistory(paraMap);
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
	}

}

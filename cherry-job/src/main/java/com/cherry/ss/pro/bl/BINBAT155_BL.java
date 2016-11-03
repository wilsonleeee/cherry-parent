/*	
 * @(#)BINBAT155_BL.java     1.0 @2016-08-02		
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
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.pro.service.BINBAT155_Service;

/**
 * 退库确认单成本导出(浓妆淡抹)BL
 * 
 * @author zw
 * 
 * @version 2016-08-02
 * 
 */
public class BINBAT155_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT155_BL.class);

	@Resource(name = "binbat155_Service")
	private BINBAT155_Service binbat155_Service;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 处理数据的上限数量 **/
	private final int BATCH_SIZE = 500;

	/** 同步状态:null 可同步 */
	private final String SYNCH_FLAG_null = "";
	
	/** 同步状态:1 同步处理中 */
	private final String SYNCH_FLAG_1 = "1";

	/** 同步状态: 2 已完成*/
	private final String SYNCH_FLAG_2 = "2";


	/** 处理总条数 */
	private int totalCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	
	private String fReason = ""; 
	/**
	 * 产品退库确认单导出
	 * 
	 * @param map
	 * @return
	 * @throws CherryBatchException
	 */
	public int tran_binbat155(Map<String, Object> map) throws Exception {
    	try{
    		//参数初始化
	        init(map);
    	}catch(Exception e){
    		e.printStackTrace();
    		// 初始化失败
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("ECM00005");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			batchExceptionDTO.setException(e);
			flag = CherryBatchConstants.BATCH_ERROR;
			fReason = String.format("程序参数初始化时失败,详细信息查看Log日志,%s", e.getMessage());
			throw new CherryBatchException(batchExceptionDTO);
    	}
		while (true) {
			// 预处理可能导出失败件数
			int preFailAmount = 0;
			try {

				// 检索新后台退库确认单据同步状态为【SynchFlag=1】的数据量
				preFailAmount = getProReturnReqCountBySynch(map);
				// 查看新后台中是否存在可导出【synchFlag=null】或导出处理中【synchFlag=1】的数据
				map.put("batchSize", BATCH_SIZE);
				
				// 更改新后台退库确认单据同步状态【SynchFlag=null---->SynchFlag=1】
				int expAmount = updateReturnReqFlag(map, 1);
				// 新后台数据源提交
				binbat155_Service.manualCommit();

				// 当前处于导出处理中的数据量
				expAmount += preFailAmount;
				// 若此次状态设置失败，则失败次数为expAmount
				preFailAmount = expAmount;
				// 无可导出数据跳出循环
				if (expAmount == 0) {
					break;
				}
				// 统计总条数
				totalCount += expAmount;
				// 将退库确认单据数据导出到标准接口表中并将同步状态更改为【SynchFlag=2】
				exportDataToIFOutStorage(preFailAmount,map);

				// 失败时结束批处理
				// 程序出现异常后，后面循环的批处理依然会遇到这样的问题。
				if (expAmount < CherryBatchConstants.BATCH_SIZE || flag == CherryBatchConstants.BATCH_WARNING) {
					break;
				}
			} catch (Exception e) {
				try {
					//新后台数据源回滚
					binbat155_Service.manualRollback();
				} catch (Exception ex) {

				}
				// 失败件数
				failCount += preFailAmount;
				flag = CherryBatchConstants.BATCH_WARNING;

				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				// 数据同步状态从"可同步"更新为"同步处理中"失败！
				batchLoggerDTO1.setCode("EOT00008");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(PropertiesUtil.getMessage("BAT155",null));
				batchLoggerDTO1.addParam(PropertiesUtil.getMessage("BAT155",null));
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				fReason = String.format("退库确认单数据同步状态从【SynchFlag=null--->SynchFlag=1】时更新失败,详细信息查看Log日志,%s", e.getMessage());
				break;
			}
		}
		programEnd(map);
		outMessage();
		return flag;
	}

	/**
	 * 退库确认单数据导出到标准接口表中
	 * 
	 * @param int：一批导出的数据量
	 * 
	 */
	private void exportDataToIFOutStorage(int preFailAmount,Map<String,Object> map) throws CherryBatchException {
		// 查询新后台退库确认单据同步状态为【SynchFlag=1】的主单数据，SQL中直接查出
		List<Map<String, Object>> proReturnReqList = binbat155_Service.getProReturnReqList(map);
		if(!CherryBatchUtil.isBlankList(proReturnReqList)){
			try {
				String[] productReturnIDArr = new String[proReturnReqList.size()];
				for(int i =0;i<proReturnReqList.size();i++){
					productReturnIDArr[i]=ConvertUtil.getString(proReturnReqList.get(i).get("productReturnID"));
				}
				map.put("productReturnIDArr", productReturnIDArr);
				//插入标准退库确认主单接口表
				binbat155_Service.insertIFOutStorage(proReturnReqList);
				//查询新后台退库确认单据同步状态为【SynchFlag=1】的明细数据，SQL中直接查出
				List<Map<String,Object>> proReturnReqDetailList = binbat155_Service.getProReturnReqDetailList(map);
				if(!CherryBatchUtil.isBlankList(proReturnReqDetailList)){
					//插入标准退库确认明细接口表
					binbat155_Service.insertIFOutStorageDetail(proReturnReqDetailList);
				}
				//第三方数据源提交
				binbat155_Service.tpifManualCommit();
				
				updateReturnReqFlag(map, 2);
				//新后台数据源提交
				binbat155_Service.manualCommit();
				
			} catch (Exception e) {
				try {
					//第三方数据源回滚
					binbat155_Service.tpifManualRollback();
					//新后台数据源回滚
					binbat155_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				// 失败件数(失败是以批的形式出现)
				failCount += preFailAmount;
				flag = CherryBatchConstants.BATCH_WARNING;

				BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
				batchLoggerDTO1.setCode("EOT00009");
				batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO1.addParam(PropertiesUtil.getMessage("BAT155",null));
				batchLoggerDTO1.addParam(PropertiesUtil.getMessage("BAT155",null));
				logger.BatchLogger(batchLoggerDTO1);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
				fReason = String.format("新后台退库确认单据导出至标准接口表时失败，详情信息查看Log日志,%s", e.getMessage());
			}
		}
	}

	/**
	 * 检索是否存在synchFlag=1的新后台数据:(同步中的数据) 
	 * 1）若存在，查看标准接口中是否有相应单据的记录:1、若新后台和接口表数据都存在，更新新后台单据同步状态【SynchFlag=2】；2、若不存在，单据归为正常需要导出的数据范围
	 * 2）若不存在，synchFlag=1【单据归为正常的需要导出的数据范围】。
	 * 
	 * @param map
	 * @return int: 未成功导出至标准接口表的退库确认单据同步状态为【SynchFlag=1】的数据量
	 * @throws CherryException
	 */
	private int getProReturnReqCountBySynch(Map<String,Object> map) throws CherryBatchException {
		// 取得退库确认单主表中同步状态为【SynchFlag=1】的退库确认单据
		map.put("synchFlag", SYNCH_FLAG_1);
		List<String> proReturnReqList = binbat155_Service.getProReturnReqListByBillNoIF(map);
		if (!CherryBatchUtil.isBlankList(proReturnReqList)) {
			Map<String,Object> updateSynchMap = new HashMap<String, Object>();
			updateSynchMap.putAll(map);
			updateSynchMap.put("billNoIFList", proReturnReqList);
			//根据新后台已存在的单号查询单号数据在标准接口表中是否存在
			List<String> existsProReturnReqList = binbat155_Service.getExistsProReturnReqList(updateSynchMap);
			// 标准接口表中存在synchFlag=1的数据，即导出成功，状态更新不成功
			if (!CherryBatchUtil.isBlankList(existsProReturnReqList)) {
				try {
					updateSynchMap.put("billNoIFList", existsProReturnReqList);
					//更新已在标准接口表存在的退库单据的同步状态【SynchFlag=1】
					updateReturnReqFlag(updateSynchMap, 2);
					// 新后台数据源提交
					binbat155_Service.manualCommit();
				} catch (Exception e) {
					try {
						//新后台数据源回滚
						binbat155_Service.manualRollback();
					} catch (Exception ex) {
						
					}
					flag = CherryBatchConstants.BATCH_WARNING;

					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					// 数据同步状态从"同步处理中"更新为"已完成"失败
					batchLoggerDTO1.setCode("EOT00010");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO1.addParam(PropertiesUtil.getMessage("BAT155",null));
					batchLoggerDTO1.addParam(PropertiesUtil.getMessage("BAT155",null));
					logger.BatchLogger(batchLoggerDTO1);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					fReason = String.format("退库确认单据更新同步状态【SynchFalg=1----->SynchFlag=2】时更新失败,详细信息查看Log日志,%s", e.getMessage());
				}
			}
			//去除已经成功导出到标准接口表中的退库确认单号list
			proReturnReqList.removeAll(existsProReturnReqList);
		}
		// 返回synchFlag=1且还未导出到标准接口表中的数据量
		return proReturnReqList.size();
	}

	/**
	 * 处理状态字段SynchFlag identifyId=1:SynchFlag(null->1) identifyId=2:SynchFlag(1->2)
	 * 
	 * @param map
	 * @param identifyId
	 * @return
	 * @throws CherryBatchException
	 */
	private int updateReturnReqFlag(Map<String, Object> map, int identifyId) throws CherryBatchException {
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.putAll(map);
		if (identifyId == 1) {
			// 导出状态由可导出（SynchFlag：null）设置为导出处理中（SynchFlag：1）
			updateMap.put("synchFlag_Old", null);
			updateMap.put("synchFlag_New", SYNCH_FLAG_1);
			return binbat155_Service.updateSynchFlag(updateMap);
		} else if (identifyId == 2) {
			// 导出状态由导出处理中（SynchFlag：1）设置为导出完成（SynchFlag：2）
			updateMap.put("synchFlag_Old", SYNCH_FLAG_1);
			updateMap.put("synchFlag_New", SYNCH_FLAG_2);
			return binbat155_Service.updateSynchFlag(updateMap);
		} else {
			return -1;
		}
	}
	/**
	 * 程序结束时，处理Job共通(更新Job控制表 、插入Job运行履历表)
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String, Object> paraMap) throws Exception {
			// 程序结束时，插入Job运行履历表
			paraMap.put("flag", flag);
			paraMap.put("TargetDataCNT", totalCount);
			paraMap.put("SCNT", totalCount - failCount);
			paraMap.put("FCNT", failCount);
			paraMap.put("FReason", fReason);
			binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private void init(Map<String, Object> map) {
		// BatchCD
		// 来自VSS$/01.Cherry/02.设计文档/01.概要设计/00.各种一览/【新设】CherryBatch一览.xlsx
		map.put("JobCode", "BAT155");

		// 程序【开始运行时间】
		String runStartTime = binbat155_Service.getSYSDateTime();
		// 作成日时
		map.put("RunStartTime", runStartTime);
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBAT155");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBAT155");
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY,CherryBatchConstants.UPDATE_NAME);
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY,CherryBatchConstants.UPDATE_NAME);
		//是否测试模式（若是则包含测试部门）
		String testMod = binOLCM14_BL.getConfigValue("1080", ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		map.put("testMod", testMod);
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
		// 导出件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00007");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		logger.BatchLogger(batchLoggerDTO3);
	}
}
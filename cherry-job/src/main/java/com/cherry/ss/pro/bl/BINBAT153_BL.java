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

import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static Logger loger = LoggerFactory.getLogger(BINBAT153_BL.class);

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
	private final int UPDATE_SIZE = 500;
	
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
	 * @param
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
			//将新后台的待处理的数据从NULL置为1
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("synchFlag_New", SYNCH_FLAG_1);
			paramMap.putAll(map);
			// 修改后台产品入出库批次主表数据（有成本的）的synchFlag（由null改为1）,这里进行单独提交
			int upCountSucc = 0;
			try {
				upCountSucc = binBAT153_Service.updProBatBySynNull(paramMap);
				binBAT153_Service.manualCommit();
				loger.info("更新新后台入出库批次主表从[synchFlag=null]更新为[synchFlag=1],cnt="+upCountSucc);
			} catch (Exception e) {
				binBAT153_Service.manualRollback();
				loger.error("(更新新后台入出库批次主表从[synchFlag=null]更新为[synchFlag=1]失败");
				e.printStackTrace();
			}

			while (true) {
					// 取得新后台同步状态为"同步处理中"[synchFlag=1]导出到接口表
					Map<String, Object> parmap = new HashMap<String, Object>();
					parmap.putAll(comMap);
					parmap.put("updateSize",UPDATE_SIZE);
					// 获取批次数量的主表数据
					List<Map<String,Object>> proBatList = binBAT153_Service.getProBatList(parmap);

					if(null==proBatList||proBatList.size()==0){
						break;
					}
					parmap.put("proBatList",proBatList);
					//获取明细数据
					List<Map<String,Object>> proBatDetailList = binBAT153_Service.getBatDetailListNew(parmap);
					// 插入产品批次数据
					for(Map<String,Object> mainMap : proBatList) {
						mainMap.put("brandCode", comMap.get("brandCode"));
					}
					// 插入产品批次明细数据数据
					for(Map<String,Object> detailMap : proBatDetailList) {
						detailMap.put("brandCode", comMap.get("brandCode"));
					}
					Map<String,Object> updMap = new HashMap<String,Object>();
					updMap.put("synchFlag_New","2");
					updMap.put("proBatList",proBatList);
					try {
						// 插入产品入出库批次数据
						binBAT153_Service.insertProBat(proBatList);
						binBAT153_Service.insertProBatDetailNew(proBatDetailList);
						// 修改同步状态为2（主表）
						binBAT153_Service.updIFProBat(updMap);
						binBAT153_Service.tpifManualCommit();
						binBAT153_Service.manualCommit();
					}catch(Exception ex){
						loger.error("ERROR:",ex);
						throw ex;
					}
			}
		}
		programEnd(map);
		outMessage();
		return flag;
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

	/**
	 *
	 * @param map
	 * @throws Exception
	 * 将要处理的数据的SynchFlag从NULL变为1
     */
	public int  changSynchFlagFromNullToOne(Map<String,Object> map) throws Exception{
		int upCountSucc = 0;
		try {
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("synchFlag_New", SYNCH_FLAG_1);
			paramMap.putAll(map);
			// 修改后台产品入出库批次主表数据（有成本的）的synchFlag（由null改为1）
			upCountSucc = binBAT153_Service.updProBatBySynNull(paramMap);
//			//  产品入出库批次主表明细（有成本的）的synchFlag（由null改为1）
//			binBAT153_Service.updDetailProBatBySynNull(paramMap);
			binBAT153_Service.manualCommit();
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
			loger.error("更新新后台入出库批次主表从[synchFlag=null]更新为[synchFlag=1]",e);
			logger.outLog("更新新后台入出库批次主表从[synchFlag=null]更新为[synchFlag=1]" , CherryBatchConstants.LOGGER_ERROR);
			loger.error("更新新后台入出库批次主表从[synchFlag=null]更新为[synchFlag=1]",e);
			logger.outExceptionLog(e);
			throw e;
		}
		return upCountSucc;
	}


}

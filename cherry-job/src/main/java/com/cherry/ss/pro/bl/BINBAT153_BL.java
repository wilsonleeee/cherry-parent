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
 * @author hsq
 *
 * @version  2017-01-10
 */
public class BINBAT153_BL {

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

	/** 产品入出库批次成本记录同步状态:3 失败 */
	private final String SYNCH_FLAG_3 = "3";

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
		loger.info("===========================产品入出库批次成本导出（标准接口）开始===================================");
		// 初始化
		Map<String,Object> comMap = getComMap(map);
		//读取配置信息
		String config = getConfigValue(map);
		try {
			// 记录产品入出库成本时，程序开始执行
			if("1".equals(config)){
                handleMessage(map,comMap);
            }
		} catch (Exception e) {
			throw e;
		}finally {
			programEnd(comMap);
			outMessage();
		}
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
		//RunType
		baseMap.put("RunType","MT");
		// 程序【开始运行时间】
		String runStartTime = binbecm01_IF.getSYSDateTime();
		// 作成日时
		baseMap.put("RunStartTime", runStartTime);
		return baseMap;
	}
	
	/**
	 * 程序结束时，处理Job共通(插入Job运行履历表)
	 * @param paraMap
	 * @throws Exception
	 */
	private void programEnd(Map<String,Object> paraMap) throws Exception{
		// 程序结束时，插入Job运行履历表
		paraMap.put("flag", flag);
		paraMap.put("TargetDataCNT", totalCount);
		paraMap.put("SCNT", totalCount - failCount);
		paraMap.put("FCNT", failCount);
		binbecm01_IF.insertJobRunHistory(paraMap);
	}

	/**
	 * 打印最后的日志消息以及结束信息
	 */
	private void  outMessage(){
		int successCount = totalCount - failCount;
		//打印出总条数，
		loger.info("产品入出库批次成本导出（标准接口）一共处理的条数为:"+totalCount);
		//成功条数
		loger.info("产品入出库批次成本导出（标准接口）处理成功的条数为:"+successCount);
		//失败的条数
		loger.info("产品入出库批次成本导出（标准接口）处理失败的条数为:"+failCount);
		String msg = "===========================产品入出库批次成本导出（标准接口）";
		String endMsg = "结束===================================";
		switch (flag) {
			case CherryBatchConstants.BATCH_SUCCESS:
				loger.info(msg+"正常"+endMsg);
				break;
			case CherryBatchConstants.BATCH_WARNING:
				loger.warn(msg+"警告"+endMsg);
				break;
			case CherryBatchConstants.BATCH_ERROR:
				loger.error(msg+"失败"+endMsg);
			default:
				loger.info(msg+endMsg);
		}
	}

	/***
	 * 处理数据
	 * @param map
	 * @param comMap
	 * @throws Exception
     */
	private void handleMessage(Map<String,Object> map,Map<String,Object> comMap) throws Exception{
			//将新后台的待处理的数据从NULL置为1
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("synchFlag_New", SYNCH_FLAG_1);
			paramMap.putAll(map);
			int upCountSucc = 0;
			try {
				// 修改后台产品入出库批次主表数据（有成本的）的synchFlag（由null改为1）,
				upCountSucc = binBAT153_Service.updProBatBySynNull(paramMap);
				//这里进行单独提交
				binBAT153_Service.manualCommit();
				loger.info("更新新后台入出库批次主表从[synchFlag=null]更新为[synchFlag=1],cnt="+upCountSucc);
			} catch (Exception e) {
				loger.error("更新新后台入出库批次主表从[synchFlag=null]更新为[synchFlag=1]失败",e);
				flag = CherryBatchConstants.BATCH_ERROR;
				throw e;
			}
			while (true) {
				// 取得新后台同步状态为"同步处理中"[synchFlag=1]导出到接口表
				Map<String, Object> parmap = new HashMap<String, Object>();
				Map<String,Object> updMap = new HashMap<String,Object>();
				parmap.putAll(comMap);
				parmap.put("updateSize",UPDATE_SIZE);
				// 获取批次数量的主表数据
				List<Map<String,Object>> proBatList = binBAT153_Service.getProBatList(parmap);
				//记录主数据量
				totalCount += proBatList.size();
				//如果没有取到数据的话，跳出循环
				if(null==proBatList||proBatList.size()==0){
					break;
				}
				parmap.put("proBatList",proBatList);
				updMap.put("proBatList",proBatList);
				//获取明细数据
				List<Map<String,Object>> proBatDetailList = binBAT153_Service.getBatDetailListNew(parmap);
				//如果明细数据为null
				if(proBatDetailList == null || proBatDetailList.isEmpty()){
					//先将主表数据的synchFlag置为3
					updMap.put("synchFlag_New",SYNCH_FLAG_3);
					// 修改同步状态为3（主表）
					binBAT153_Service.updIFProBat(updMap);
					//记录失败条数
					failCount += proBatList.size();
					//继续执行
					continue;
				}
				// 对主数据进行赋值
				for(Map<String,Object> mainMap : proBatList) {
					mainMap.put("brandCode", comMap.get("brandCode"));
				}
				// 对明细进行赋值
				for(Map<String,Object> detailMap : proBatDetailList) {
					detailMap.put("brandCode", comMap.get("brandCode"));
				}
				updMap.put("synchFlag_New",SYNCH_FLAG_2);

				try {
					// 插入产品入出库批次数据接口表，如果有重复数据的话，这里是不能进行插入的，主键（[BrandCode]，[RelevanceNo]，[TradeNoIF]）
					binBAT153_Service.insertProBat(proBatList);
					//插入接口表入出库批次数据明细
					binBAT153_Service.insertProBatDetailNew(proBatDetailList);
					// 修改同步状态为2（主表）
					binBAT153_Service.updIFProBat(updMap);
					binBAT153_Service.tpifManualCommit();
					binBAT153_Service.manualCommit();
				}catch(Exception ex){
					flag = CherryBatchConstants.BATCH_ERROR;
					//记录失败条数
					failCount += proBatList.size();
					loger.error("新后台入出库批次导出到接口表失败",ex);
					throw ex;
				}
			}
	}

	/**
	 * 读取配置项 是否记录产品入出库成本
	 * @param map
	 * @return
     */
	private String  getConfigValue(Map<String,Object> map){
		String config = binOLCM14_BL.getConfigValue("1365", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		return config;
	}
}

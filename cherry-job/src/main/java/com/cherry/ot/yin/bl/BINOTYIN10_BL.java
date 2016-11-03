/*	
 * @(#)BINOTYIN10_BL.java     1.0 @2013-04-22		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ot.yin.service.BINOTYIN10_Service;

/**
 * 颖通接口：支付方式导入BL
 * 
 * @author menghao
 * 
 * @version 2013-04-22
 */
public class BINOTYIN10_BL {

	/** BATCH LOGGER */
	private static CherryBatchLogger logger = new CherryBatchLogger(
			BINOTYIN10_BL.class);

	@Resource(name = "binOTYIN10_Service")
	private BINOTYIN10_Service binOTYIN10_Service;

	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 查询共通Map参数 */
	private Map<String, Object> comMap;

	/** 插入条数 */
	private int insertCount = 0;
	/** 更新条数 */
	private int updateCount = 0;
	/** 插入失败条数 */
	private int failInsertCount = 0;
	/** 更新失败条数 */
	private int failUpdateCount = 0;

	public int tran_batchImportPayType(Map<String, Object> map)
			throws CherryBatchException {
		// 从颖通接口表中取得支付方式数据
		List<Map<String, Object>> payTypeList = binOTYIN10_Service
				.getPayTypeIFList(map);
		comMap = getComMap(map);
		if (!CherryBatchUtil.isBlankList(payTypeList)) {
			for (Map<String, Object> payTypeMap : payTypeList) {
				payTypeMap.putAll(comMap);
				// payTypeMap中的支付方式在新后台是否存在
				int hvPayTypeCount = binOTYIN10_Service
						.getHvPayTypeCount(payTypeMap);
				// 要导入的支付方式在新后台中已经存在，更新之；
				if (hvPayTypeCount > 0) {
					try {
						binOTYIN10_Service.updateOldPayType(payTypeMap);
						binOTYIN10_Service.manualCommit();
						// 更新条数加1
						updateCount++;
					} catch (Exception e) {
						try {
							binOTYIN10_Service.manualRollback();
							failUpdateCount++;
						} catch (Exception e1) {

						}
						flag = CherryBatchConstants.BATCH_WARNING;

						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						// 更新已存在的支付方式时出现异常
						batchLoggerDTO1.setCode("EOT00023");
						batchLoggerDTO1
								.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 组织ID：
						batchLoggerDTO1
								.addParam(CherryBatchUtil.getString(payTypeMap
										.get(CherryBatchConstants.ORGANIZATIONINFOID)));
						// 品牌ID：
						batchLoggerDTO1
								.addParam(CherryBatchUtil.getString(payTypeMap
										.get(CherryBatchConstants.BRANDINFOID)));
						// 支付方式代码：
						batchLoggerDTO1.addParam(payTypeMap.get("payTypeCode")
								.toString());
						logger.BatchLogger(batchLoggerDTO1);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
								this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);

					}
				} else {// 要导入的支付方式在新后中不存在，插入之；
					try {
						binOTYIN10_Service.insertNewPayType(payTypeMap);
						binOTYIN10_Service.manualCommit();
						// 插入条数加1
						insertCount++;
					} catch (Exception e) {
						try {
							binOTYIN10_Service.manualRollback();
							// 插入失败件数加1
							failInsertCount++;
						} catch (Exception e1) {

						}

						flag = CherryBatchConstants.BATCH_WARNING;

						BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
						// 插入不存在的支付方式时出现异常
						batchLoggerDTO1.setCode("EOT00024");
						batchLoggerDTO1
								.setLevel(CherryBatchConstants.LOGGER_ERROR);
						// 组织ID：
						batchLoggerDTO1
								.addParam(CherryBatchUtil.getString(payTypeMap
										.get(CherryBatchConstants.ORGANIZATIONINFOID)));
						// 品牌ID：
						batchLoggerDTO1
								.addParam(CherryBatchUtil.getString(payTypeMap
										.get(CherryBatchConstants.BRANDINFOID)));
						// 支付方式代码：
						batchLoggerDTO1.addParam(payTypeMap.get("payTypeCode")
								.toString());
						logger.BatchLogger(batchLoggerDTO1);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
								this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					}
				}
			}
		}
		// 输出处理结果信息
		outMessage();
		return flag;
	}

	/**
	 * 共通Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map) {
		Map<String, Object> baseMap = new HashMap<String, Object>();
//		String time = binOTYIN10_Service.getSYSDate();
		// 更新程序名
		baseMap.put(CherryBatchConstants.UPDATEPGM, "BINOTYIN10");
		// 作成程序名
		baseMap.put(CherryBatchConstants.CREATEPGM, "BINOTYIN10");
		// 作成者
		baseMap.put(CherryBatchConstants.CREATEDBY,
				CherryBatchConstants.UPDATE_NAME);
		// 更新者
		baseMap.put(CherryBatchConstants.UPDATEDBY,
				CherryBatchConstants.UPDATE_NAME);
//		// 作成时间
//		baseMap.put(CherryBatchConstants.CREATE_TIME, time);
//		// 更新时间
//		baseMap.put(CherryBatchConstants.UPDATE_TIME, time);
		// 所属组织
		baseMap.put(CherryBatchConstants.ORGANIZATIONINFOID,
				map.get(CherryBatchConstants.ORGANIZATIONINFOID));
		// 品牌
		baseMap.put(CherryBatchConstants.BRANDINFOID,
				map.get(CherryBatchConstants.BRANDINFOID));

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
		batchLoggerDTO1.addParam(String.valueOf(insertCount + updateCount
				+ failInsertCount + failUpdateCount));
		// 插入件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00003");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(insertCount));
		// 更新件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00004");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(updateCount));
		// 插入失败件数
		BatchLoggerDTO batchLoggerDTO4 = new BatchLoggerDTO();
		batchLoggerDTO4.setCode("IIF00008");
		batchLoggerDTO4.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO4.addParam(String.valueOf(failInsertCount));
		// 更新失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00009");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failUpdateCount));
		// 处理总件数
		logger.BatchLogger(batchLoggerDTO1);
		// 插入件数
		logger.BatchLogger(batchLoggerDTO2);
		// 更新件数
		logger.BatchLogger(batchLoggerDTO3);
		// 插入失败件数
		logger.BatchLogger(batchLoggerDTO4);
		// 更新失败件数
		logger.BatchLogger(batchLoggerDTO5);
	}
}

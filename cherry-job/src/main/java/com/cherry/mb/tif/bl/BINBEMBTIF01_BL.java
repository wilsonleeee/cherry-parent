/*	
 * @(#)BINBEMBTIF01_BL.java     1.0 2015/06/24
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
package com.cherry.mb.tif.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;

/**
 * 同步天猫会员处理BL
 * 
 * @author hub
 * @version 1.0 2015/06/24
 */
public class BINBEMBTIF01_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF01_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 同步会员处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_MemSync(Map<String, Object> map) throws Exception {
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode((String) map.get("brandCode"));
		if (null == tmallKey) {
			throw new Exception("can not get brand keys!");
		}
		totalCount = 0;
		failCount = 0;
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 取得需要同步的会员信息List
			List<Map<String, Object>> memSyncList = binBEMBTIF01_Service.getMemSyncList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memSyncList)) {
				try {
					// 执行同步处理
					executeMembers(memSyncList, map);
				} catch (Exception e) {
					logger.error("Member sync exception：" + e.getMessage(),e);
				}
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(memSyncList.size() < dataSize) {
					break;
				} else {
					int mebIdMin = Integer.parseInt(String.valueOf(
							memSyncList.get(memSyncList.size() - 1).get("memberInfoId")));
					map.put("mebIdMin", mebIdMin);
				}
			} else {
				break;
			}
		}
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
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
				.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		return flag;
	}
	
	/**
	 * 执行同步处理
	 * 
	 * @param memSyncList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<Map<String, Object>> memSyncList, Map<String, Object> map) throws Exception {
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		totalCount += memSyncList.size();
		String brandCode = (String) map.get("brandCode");
		for (int i = 0; i < memSyncList.size(); i++) {
			Map<String, Object> memberInfo = memSyncList.get(i);
			int memberInfoId = Integer.parseInt(String.valueOf(memberInfo.get("memberInfoId")));
			try {
				memberInfo.put("brandCode", brandCode);
				memberInfo.put("PgmName", "BINBEMBTIF01");
				// 同步天猫会员
				binOLCM31_BL.syncTmall(memberInfo);
				binBEMBTIF01_Service.manualCommit();
			}catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {
					// 事务回滚
					binBEMBTIF01_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error(e.getMessage(),e);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMI00001");
				// 会员ID
				batchLoggerDTO.addParam(String.valueOf(memberInfoId));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}
}

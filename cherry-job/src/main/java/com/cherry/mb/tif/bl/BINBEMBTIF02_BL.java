/*	
 * @(#)BINBEMBTIF02_BL.java     1.0 2015/06/24
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

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;

/**
 * 天猫会员手机加密处理BL
 * 
 * @author hub
 * @version 1.0 2015/06/24
 */
public class BINBEMBTIF02_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF02_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;
	
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
	public int tran_MixMobile(Map<String, Object> map) throws Exception {
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode((String) map.get("brandCode"));
		if (null == tmallKey) {
			throw new Exception("can not get brand keys!");
		}
		map.put("mTmallKey", tmallKey);
		totalCount = 0;
		failCount = 0;
		try {
			// 去除会员BATCH执行状态
			binBEMBTIF01_Service.updateClearBatchExec(map);
			// 更新会员BATCH执行状态
			binBEMBTIF01_Service.updateMixMobileBatchExec(map);
			// 提交事务
			binBEMBTIF01_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBTIF01_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error(e.getMessage(),e);
		}
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "memberInfoId");
		while (true) {
			// 查询开始位置
			startNum = dataSize * currentNum + 1;
			// 查询结束位置
			endNum = startNum + dataSize - 1;
			// 数据抽出次数累加
			currentNum++;
			// 查询开始位置
			map.put(CherryBatchConstants.START, startNum);
			// 查询结束位置
			map.put(CherryBatchConstants.END, endNum);
			// 取得需要手机加密的会员信息List
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getMemMixPhoneList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					// 执行清零处理
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("Member mix mobile exception：" + e.getMessage(),e);
				}
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(memList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		try {
			// 去除会员BATCH执行状态
			binBEMBTIF01_Service.updateClearBatchExec(map);
			// 提交事务
			binBEMBTIF01_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBTIF01_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error(e.getMessage(),e);
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
	public void executeMembers(List<Map<String, Object>> memList, Map<String, Object> map) throws Exception {
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		totalCount += memList.size();
		TmallKeyDTO mTmallKey = (TmallKeyDTO) map.get("mTmallKey");
		String mixKey = mTmallKey.getMixKey();
		for (int i = 0; i < memList.size(); i++) {
			Map<String, Object> memberInfo = memList.get(i);
			int memberInfoId = Integer.parseInt(String.valueOf(memberInfo.get("memberInfoId")));
			try {
				String brandCode = (String) map.get("brandCode");
				String mobile = CherrySecret.decryptData(brandCode, (String) memberInfo.get("mobilePhone"));
				if (CherryChecker.isPhoneValid(mobile, "^(1[34578])[0-9]{9}$")) {
					memberInfo.put("mix_mobile", DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mobile + mixKey)));
					commParamsForUp(memberInfo);
					int count = binBEMBTIF01_Service.getTempMemCount(memberInfo);
					if (count > 0) {
						memberInfo.put("mergeFlag", 0);
						memberInfo.put("organizationInfoId", map.get("organizationInfoId"));
						memberInfo.put("brandInfoId", map.get("brandInfoId"));
						int upcount = binBEMBTIF01_Service.updateTmallMemMergeInfo(memberInfo);
						if (upcount == 0) {
							binBEMBTIF01_Service.addTmallMemMergeInfo(memberInfo);
						}
					} else {
						binBEMBTIF01_Service.updateMemMixMobile(memberInfo);
					}
					binBEMBTIF01_Service.manualCommit();
				}
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
				batchLoggerDTO.setCode("EMI00002");
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
	
//	public static void main(String[] args) {
//		System.out.println(DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + "13829028011" + "F4rgLk")));
//	}
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF02");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF02");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF02");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF02");
	}
}

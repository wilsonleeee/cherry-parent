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

import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加会员天猫加密手机号（巧迪会员通）处理BL
 * 
 * @author fxb
 * @version 1.0 2016/12/18
 */
public class BINBEMBTIF12_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF12_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 添加会员天猫加密手机号（巧迪会员通）处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_addMixMobile(Map<String, Object> map) throws Exception {
		
		logger.info("执行天猫手机加密入参：" + map);
		totalCount = 0;
		failCount = 0;
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
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getAddMixMobileMemList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					// 执行清零处理
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("Member mix mobile exception：" + e,e);
				}
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(memList.size() < dataSize) {
					break;
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
	 * 执行添加会员天猫加密手机号（巧迪会员通）
	 * 
	 * @param
	 *
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<Map<String, Object>> memList, Map<String, Object> map) throws Exception {
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		totalCount += memList.size();

		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode((String) map.get("brandCode"));
		if (null == tmallKey) {
			return ;
		}
		String mixKey = tmallKey.getMixKey();
		for (int i = 0; i < memList.size(); i++) {
			Map<String, Object> memberInfo = memList.get(i);			
			int memberInfoId = Integer.parseInt(String.valueOf(memberInfo.get("memberInfoId")));
			try {
				String mobile = (String) memberInfo.get("mobilePhone");
				if (!CherryChecker.isNullOrEmpty(mobile)) {
					//对明文手机号进行天猫加密
					memberInfo.put("mix_mobile", DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mobile + mixKey)));
					commParamsForUp(memberInfo);
					//用天猫加密手机号查询会员信息表
					Map<String, Object> newMemberInfo = binBEMBTIF01_Service.getMemberInfoByMixMobile(memberInfo);
					if (newMemberInfo != null && !newMemberInfo.isEmpty()){
						//如果有线下会员的手机号与之重复
						if ("0".equals(ConvertUtil.getString(newMemberInfo.get("memInfoRegFlg")))){
							//并插入一条异常信息到会员信息合并历史表，mergeFlag设为3
							addMemberMergeHistory(memberInfo,newMemberInfo,map);
							binBEMBTIF01_Service.manualCommit();
							//如果有线上会员的天猫加密手机号与之重复
						}else if ("1".equals(ConvertUtil.getString(newMemberInfo.get("memInfoRegFlg")))
								&& !CherryChecker.isNullOrEmpty(newMemberInfo.get("nickName"))){
							//并插入一条合并信息到会员信息合并记录表
							addMemberMergeInfo(memberInfo,newMemberInfo,map);
							binBEMBTIF01_Service.manualCommit();
						}

					}else{
						binBEMBTIF01_Service.updateMemMixMobile(memberInfo);
						binBEMBTIF01_Service.manualCommit();
					}

				}
			}catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {
					// 事务回滚
					binBEMBTIF01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				logger.error(e.toString(),e);
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

	private void addMemberMergeInfo(Map<String, Object> memberInfo, Map<String, Object> newMemberInfo, Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put("memberInfoId",newMemberInfo.get("memberInfoId"));
		paramMap.put("memberCode",newMemberInfo.get("memberCode"));
		paramMap.put("retainMemInfoId",memberInfo.get("memberInfoId"));
		paramMap.put("retainMemCode",memberInfo.get("memberCode"));
		//判断是否已存在该会员的合并记录
		Map<String,Object> mergeInfo = binBEMBTIF01_Service.getMemMergeInfoByIdAndMemCode(paramMap);
		if (mergeInfo == null) {
			binBEMBTIF01_Service.addMemberMergeInfo(paramMap);
		}
	}

	private void addMemberMergeHistory(Map<String, Object> memberInfo, Map<String, Object> newMemberInfo, Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put("deleteMemInfoId",memberInfo.get("memberInfoId"));
		paramMap.put("deleteMemCode",memberInfo.get("memberCode"));
		paramMap.put("retainMemInfoId",newMemberInfo.get("memberInfoId"));
		paramMap.put("retainMemCode",newMemberInfo.get("memberCode"));
		paramMap.put("mergeFlag","3");
		//判断是否已存在该会员的合并历史记录
		Map<String,Object> mergeInfo = binBEMBTIF01_Service.getMemMergeHistory(paramMap);
		if (mergeInfo == null) {
			binBEMBTIF01_Service.addMemberMergeHistory(paramMap);
		}
	}

	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF12");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF12");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF12");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF12");
	}
}

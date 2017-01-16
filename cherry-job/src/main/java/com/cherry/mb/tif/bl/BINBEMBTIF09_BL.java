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

import com.cherry.cm.cmbussiness.service.BINOLCM31_Service;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.SignTool;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;
import com.taobao.api.request.TmallMeiCrmMemberSyncRequest;
import com.taobao.api.response.TmallMeiCrmMemberSyncResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更新会员明文手机号处理BL
 * 
 * @author fxb
 * @version 1.0 2016/12/14
 */
public class BINBEMBTIF09_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF09_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;

	@Resource
	private BINOLCM31_Service binOLCM31_Service;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 更新会员明文手机号处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_updateExpressMobile(Map<String, Object> map) throws Exception {
		
		logger.info("入参：" + map);
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
			// 取得需要更新明文手机号的会员信息List
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getUpdatedMemList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("update expressMobile exception：" + e,e);
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
	 * 更新明文手机号处理
	 * 
	 * @param
	 *
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<Map<String, Object>> memList, Map<String, Object> map) throws Exception {
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		totalCount += memList.size();

		for (int i = 0; i < memList.size(); i++) {
			Map<String, Object> memberInfo = memList.get(i);			
			int memberInfoId = Integer.parseInt(String.valueOf(memberInfo.get("memberInfoId")));
			try {
				map.putAll(memberInfo);
				//同步会员信息,获取明文手机号
				String mobile = syncTmall(map);

				if (!StringUtils.isEmpty(mobile)) {
					memberInfo.put("mobile",mobile);
					commParamsForUp(memberInfo);
					//更新会员明文手机号
					binBEMBTIF01_Service.updateExpressMobile(memberInfo);
					
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

	/**
	 * 同步天猫
	 * @param map
	 * @return
     */
	private String syncTmall(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		//获取的明文手机号
		String mobileNew = "";
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode((String) paramMap.get("brandCode"));
		if (null == tmallKey) {
			return null;
		}
		// 会员ID
		Object memberIdObj = paramMap.get("memberInfoId");
		// 取得会员信息
		Map<String, Object> member = getMemberInfo(paramMap);
		TmallMeiCrmMemberSyncRequest req =
				new TmallMeiCrmMemberSyncRequest();
		// 明文手机号
		String mobile = (String) member.get("mobile");
		if (CherryChecker.isNullOrEmpty(mobile)) {
			String nick = (String) member.get("nick");
			if (CherryChecker.isNullOrEmpty(nick)) {
				String nickName = (String) member.get("tNickName");
				if (CherryChecker.isNullOrEmpty(nickName)) {
					Map<String, Object> registInfo = binOLCM31_Service.getMemRegisInfo(paramMap);
					String mixNick = null;
					if (null != registInfo && !registInfo.isEmpty()) {
						mixNick = (String) registInfo.get("mixNick");
					}
					if (CherryChecker.isNullOrEmpty(mixNick)) {
						logger.error("同步所需参数都为空,不执行同步处理!会员ID：" + memberIdObj);
						return null;
					}
					req.setMixNick(mixNick);
				} else {
					req.setNick(nickName);
				}
			} else {
				req.setNick(nick);
			}
		} else {
			req.setMobile(mobile);
		}
		// 积分值
		req.setPoint(Long.parseLong(String.valueOf(member.get("point"))));
		// 等级
		req.setLevel(Long.parseLong(String.valueOf(member.get("level"))));
		// 版本信息
		long version = System.currentTimeMillis();
		req.setVersion(version);
		req.setExtend("");
		paramMap.put("syncVersion", version);
		// 程序名
		String pgmName = (String) paramMap.get("PgmName");
		if (null == pgmName) {
			pgmName = "BINBEMBTIF09";
		}
		paramMap.put("point", member.get("point"));
		paramMap.put("level", member.get("level"));
		paramMap.put("createdBy", pgmName);
		paramMap.put("createPGM", pgmName);
		paramMap.put("updatedBy", pgmName);
		paramMap.put("updatePGM", pgmName);
		StringBuilder builder = null;
		// 同步处理
		for (int i = 0; i < 4; i++) {
			try {
				TmallMeiCrmMemberSyncResponse response = SignTool.syncResponse(req, tmallKey.getAppKey(), tmallKey.getAppSecret(), tmallKey.getSessionKey());
				if (response.isSuccess()) {
					String body = response.getBody();
					String extraInfo = response.getMeiExtraInfo();
					if (!CherryChecker.isNullOrEmpty(extraInfo)) {
						try {
							Map<String, Object> extraMap = CherryUtil.json2Map(extraInfo);
							String nick = (String) extraMap.get("nick");
							mobileNew = (String) extraMap.get("mobile");
							if (!CherryChecker.isNullOrEmpty(nick)) {
								paramMap.put("taobao_nick", nick);
							}
							if (!CherryChecker.isNullOrEmpty(mobileNew)) {
								paramMap.put("mobileNew", mobileNew);
							}
						} catch (Exception e) {
							logger.error("*********************************天猫同步返回结果转换Map失败！" + extraInfo);
						}
					}
					if (body.length() > 800) {
						body = body.substring(0, 800);
					}
					paramMap.put("resultMsg", body);
					// 成功
					paramMap.put("syncResult", 0);
					paramMap.put("tmallSyncFlg", 1);
				} else {
					// 失败
					paramMap.put("syncResult", 1);
					paramMap.put("errorCode", response.getErrorCode());
					paramMap.put("tmallSyncFlg", 2);
					builder = emptyBuilder(builder);
					// 异常原因
					builder.append("会员ID：").append(memberIdObj)
							.append(" 手机号：").append(mobile)
							.append(" 异常代号：").append(response.getErrorCode())
							.append(" 异常原因：").append(response.getMsg())
							.append(" 子异常代号：").append(response.getSubCode())
							.append(" 子异常原因：").append(response.getSubMsg());
					logger.error(builder.toString());
				}
				// 插入天猫会员同步表
				binOLCM31_Service.addTmallMemSyncInfo(paramMap);
				// 更新会员同步信息
				binOLCM31_Service.updateMemSyncInfo(paramMap);
				break;
			} catch (Exception e) {
				builder = emptyBuilder(builder);
				builder.append("会员ID：").append(memberIdObj)
						.append(" 手机号：").append(mobile)
						.append(" 回调次数：").append(i + 1)
						.append(" 异常信息：").append(e.getMessage());
				logger.error(builder.toString(),e);
				if (i == 3) {
					// 异常
					paramMap.put("syncResult", 1);
					paramMap.put("errorCode", "SYSERROR");
					paramMap.put("tmallSyncFlg", 2);
					// 插入天猫会员同步表
					binOLCM31_Service.addTmallMemSyncInfo(paramMap);
					// 更新会员同步信息
					binOLCM31_Service.updateMemSyncInfo(paramMap);
					break;
				}
			}
		}

		if (CherryChecker.isNullOrEmpty(mobileNew)){
			return null;
		}else{
			return mobileNew;
		}
	}

	private Map<String,Object> getMemberInfo(Map<String, Object> map) {
		String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
		if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
			map.put("tmallCounterArr", tmallCounters.split(","));
		}
		Map<String, Object> memInfo = binOLCM31_Service.getMemberInfo(map);
		if (null != memInfo && !memInfo.isEmpty()) {
			int level = Integer.parseInt(memInfo.get("level").toString());
			if (0 == level) {
				map.put("brandInfoID", map.get("brandInfoId"));
				map.put("organizationInfoID", map.get("organizationInfoId"));
				level = binOLCM31_Service.getDefaultLevel(map);
				level = level > 0? level : 1;
			}
			memInfo.put("level", (long) level);
			double point = Double.parseDouble(memInfo.get("point").toString());
			if (point < 0) {
				memInfo.put("point", 0L);
			} else {
				memInfo.put("point", (long) point);
			}
			String mobile = (String) memInfo.get("mobile");
			memInfo.put("mobile","");
		}
		return memInfo;
	}

	private StringBuilder emptyBuilder(StringBuilder builder) {
		if (null == builder) {
			builder = new StringBuilder();
		} else {
			if (builder.length() > 0) {
				builder.delete(0, builder.length());
			}
		}
		return builder;
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
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF09");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF09");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF09");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF09");
	}
}

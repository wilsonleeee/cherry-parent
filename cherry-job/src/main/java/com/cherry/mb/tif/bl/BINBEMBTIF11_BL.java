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
import com.cherry.cm.util.*;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;
import com.taobao.api.request.TmallMeiCrmMemberSyncRequest;
import com.taobao.api.response.TmallMeiCrmMemberSyncResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员信息合并处理BL
 * 
 * @author fxb
 * @version 1.0 2016/12/14
 */
public class BINBEMBTIF11_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF11_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;

	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 会员信息合并处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_MemberMerge(Map<String, Object> map) throws Exception {
		
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
		map.put(CherryBatchConstants.SORT_ID, "memberMergeInfoId");
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
			// 取得需要合并的会员信息List
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getMergedMemberList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("memberMerge exception：" + e,e);
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
	 * 会员信息合并处理
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
			Map<String, Object> memMergeInfo = memList.get(i);
			int memberMergeInfoId = Integer.parseInt(String.valueOf(memMergeInfo.get("memberMergeInfoId")));
			commParamsForUp(memMergeInfo);
			memMergeInfo.putAll(map);
			try {
				Map<String, Object> deleteMemberInfo = getDeleteMemberInfo(memMergeInfo);
				Map<String, Object> retainMemberInfo = getRetainMemberInfo(memMergeInfo);
				//如果合并的两条会员的卡号有改动,记录合并异常
				if (deleteMemberInfo == null || retainMemberInfo == null){
					memMergeInfo.put("mergeFlag","2");
					memMergeInfo.put("errorMsg","合并的会员信息不存在");
					binBEMBTIF01_Service.addMemberMergeHistory(memMergeInfo);
					binBEMBTIF01_Service.deleteMemberMergeInfo(memMergeInfo);
				}else{
					commParamsForUp(deleteMemberInfo);
					//合并会员信息表：更新线上会员的相关字段到线下会员
					deleteMemberInfo.put("memberInfoId",memMergeInfo.get("retainMemInfoId"));
					binBEMBTIF01_Service.updateMemberInfo(deleteMemberInfo);
					//更新会员注册信息表的BIN_MemberInfoID字段为线下会员ID
					binBEMBTIF01_Service.updateMemRegisterInfo(deleteMemberInfo);

					//更新线下会员的信息到电商订单主表的BIN_MemberInfoID，MemberCode
					deleteMemberInfo.put("memberInfoId",memMergeInfo.get("deleteMemInfoId"));
					deleteMemberInfo.put("memberCode",memMergeInfo.get("deleteMemCode"));
					deleteMemberInfo.put("newMemCode",memMergeInfo.get("retainMemCode"));
					deleteMemberInfo.put("newMemInfoId",memMergeInfo.get("retainMemInfoId"));
					binBEMBTIF01_Service.updateESOrderMain(deleteMemberInfo);
					//更新线下会员的信息到销售主表的BIN_MemberInfoID，MemberCode
					binBEMBTIF01_Service.updateSaleMaster(deleteMemberInfo);
					//更新BIN_MemUsedDetail表的MemCode，BIN_MemberInfoID
					binBEMBTIF01_Service.updateMemUsedDetail(deleteMemberInfo);
					//更新线下会员的BIN_MemberInfoID到会员积分表
					//binBEMBTIF01_Service.updateMemberPoint(deleteMemberInfo);
					//更新线下会员的BIN_MemberInfoID到会员积分变化主表
					binBEMBTIF01_Service.updatePointChange(deleteMemberInfo);
					//发重算MQ，重算积分
					memMergeInfo.put("memberInfoId",memMergeInfo.get("retainMemInfoId"));
					memMergeInfo.put("memberCode",memMergeInfo.get("retainMemCode"));
					memMergeInfo.put("tmallBindTime",deleteMemberInfo.get("tmallBindTime"));
					sendReCalcMsg(memMergeInfo);
					//删除线上会员的会员信息表纪录
					binBEMBTIF01_Service.deleteMemberInfo(deleteMemberInfo);
					//删除线上会员对应的持卡表
					binBEMBTIF01_Service.deleteMemCardInfo(deleteMemberInfo);

					//处理成功后,删除会员合并信息表记录,添加到会员合并信息表
					memMergeInfo.put("mergeFlag","1");
					binBEMBTIF01_Service.addMemberMergeHistory(memMergeInfo);
					binBEMBTIF01_Service.deleteMemberMergeInfo(memMergeInfo);
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
				batchLoggerDTO.addParam(String.valueOf(memberMergeInfoId));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

	private Map<String,Object> getDeleteMemberInfo(Map<String, Object> memMergeInfo) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberInfoId",memMergeInfo.get("deleteMemInfoId"));
		paramMap.put("memberCode",memMergeInfo.get("deleteMemCode"));

		return binBEMBTIF01_Service.getMemberInfoByIdAndMemCode(paramMap);
	}

	private Map<String,Object> getRetainMemberInfo(Map<String, Object> memMergeInfo) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberInfoId",memMergeInfo.get("retainMemInfoId"));
		paramMap.put("memberCode",memMergeInfo.get("retainMemCode"));

		return binBEMBTIF01_Service.getMemberInfoByIdAndMemCode(paramMap);
	}

	private void sendReCalcMsg(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		// 取得重算的时间
		String businessTime = getBusinessTime(paramMap,map);
		try {
			// 单据日期
			paramMap.put("reCalcTime", businessTime);
			paramMap.put("organizationInfoID",map.get("organizationInfoId"));
			paramMap.put("brandInfoID",map.get("brandInfoId"));
			// 保存并发送重算MQ
			binbedrcom01BL.saveAndSendReCalcMsg(paramMap);
		} catch (Exception e) {
			logger.error("重算积分失败:" + e,e);
		}
	}

	private String getBusinessTime(Map<String, Object> paramMap, Map<String, Object> map) {
		String businessTime;
		//取得最早的销售时间
		Map<String,Object> earliestSaleTimeMap = binBEMBTIF01_Service.getEarliestSaleTime(paramMap);
		if ((earliestSaleTimeMap == null || earliestSaleTimeMap.get("saleTime") == null)&& map.get("tmallBindTime") != null){
			businessTime = ConvertUtil.getString(map.get("tmallBindTime"));
		}else if ((earliestSaleTimeMap == null || earliestSaleTimeMap.get("saleTime") == null) && map.get("tmallBindTime") == null){
			return binBEMBTIF01_Service.getSYSDateTime();
		}else if (earliestSaleTimeMap != null && map.get("tmallBindTime") == null){
			businessTime =  ConvertUtil.getString(earliestSaleTimeMap.get("saleTime"));
		}else{
			if (DateUtil.compareDate(ConvertUtil.getString(earliestSaleTimeMap.get("saleTime")),ConvertUtil.getString(map.get("tmallBindTime"))) > 0){
				businessTime =  ConvertUtil.getString(map.get("tmallBindTime"));
			}else{
				businessTime =  ConvertUtil.getString(earliestSaleTimeMap.get("saleTime"));
			}
		}
		return businessTime;
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
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF11");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF11");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF11");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF11");
	}
}

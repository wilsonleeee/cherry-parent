/*	
 * @(#)BINBEDRHAN02_BL.java     1.0 2011/09/13
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
package com.cherry.dr.handler.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.BaseHandler_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.handler.interfaces.BINBEDRHAN02_IF;
import com.cherry.dr.handler.service.BINBEDRHAN02_Service;

/**
 * 清零处理BL
 * 
 * @author hub
 * @version 1.0 2011.09.13
 */
public class BINBEDRHAN02_BL implements BINBEDRHAN02_IF, BaseHandler_IF{
	
	/** 会员活动共通IF */
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 清零处理Service */
	@Resource
	private BINBEDRHAN02_Service binBEDRHAN02_Service;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon05BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 
	 * 清零处理batch主处理
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	@Override
	public int tran_clearData(Map<String, Object> map) throws Exception {
		// 业务日期
		String busDate = binBEDRHAN02_Service.getBussinessDate(map);
		// 系统日期
		String sysDate = binBEDRHAN02_Service.getForwardSYSDate();
		map.put("busDate", busDate);
		map.put("sysDate", sysDate);
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "BIN_MemberInfoID");
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
			// 体验会员
			map.put("memberLevel", 1);
			// 取得会员信息List
			List<CampBaseDTO> campBaseDTOList = binBEDRHAN02_Service.getCampBaseDTOList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(campBaseDTOList)) {
				// 执行清零处理
				executeMembers(campBaseDTOList, map);
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(campBaseDTOList.size() < dataSize) {
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
	 * 执行清零处理
	 * 
	 * @param campBaseDTOList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<CampBaseDTO> campBaseDTOList, Map<String, Object> map) throws Exception {
		totalCount += campBaseDTOList.size();
		// 业务日期
		String busDate = (String) map.get("busDate");
		// 清零日时
		String clearTime = DateUtil.suffixDate(busDate, 1);
		// 系统日期
		String sysDate = (String) map.get("sysDate");
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		// 循环会员信息List
		for (CampBaseDTO campBaseDTO : campBaseDTOList) {
			try {
				// 业务日期
				campBaseDTO.setBusinessDate(busDate);
				// 不需要清零处理
				if (!CampRuleUtil.isNeedClear(campBaseDTO.getJoinDate(), campBaseDTO.getBusinessDate(), 12)) {
					continue;
				}
				// 业务类型
				campBaseDTO.setTradeType(DroolsConstants.TRADETYPE_ZC);
				// 单据产生日期
				campBaseDTO.setTicketDate(clearTime);
				// 所属组织
				map.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
				// 所属品牌
				map.put("brandInfoID", campBaseDTO.getBrandInfoId());
				// 会员ID
				map.put("memberInfoId", campBaseDTO.getMemberInfoId());
				// 等级和化妆次数重算
				map.put("reCalcType", DroolsConstants.RECALCTYPE0);
				// 单据日期
				map.put("saleTime", clearTime);
				// 单据日期
				map.put("tradeType", DroolsConstants.TRADETYPE_ZC);
				// 品牌代码
				map.put("brandCode", campBaseDTO.getBrandCode());
				// 组织代码
				map.put("orgCode", campBaseDTO.getOrgCode());
				// 验证是否需要重算(Batch处理)
				boolean needReCalc = binbedrcom01BL.needReCalcBatch(map);
				if (needReCalc) {
					// 更新重算信息
					updateReCalcInfo(campBaseDTO);
					continue;
				}
				// 根据业务类型，业务时间取得执行次数
				int count = binbedrcom01BL.getCountByType(map);
				if (count > 0) {
					continue;
				}
				// 会员卡号
				String memCard = binbedrcom01BL.getMemCard(map);
				// 会员卡号
				campBaseDTO.setMemCode(memCard);
				// 作成者
				campBaseDTO.setCreatedBy(CherryBatchConstants.PGM_BINBEDRHAN02);
				// 作成程序名
				campBaseDTO.setCreatePGM(CherryBatchConstants.PGM_BINBEDRHAN02);
				// 更新者
				campBaseDTO.setUpdatedBy(CherryBatchConstants.PGM_BINBEDRHAN02);
				// 更新程序名
				campBaseDTO.setUpdatePGM(CherryBatchConstants.PGM_BINBEDRHAN02);
				// 计算日期
				campBaseDTO.setCalcDate(sysDate);
				// 重算区分
				campBaseDTO.setReCalcCount(DroolsConstants.RECALCCOUNT_0);
				// 理由
				campBaseDTO.setReason(DroolsConstants.REASON_2);
				// 获取累计金额, 等级
				binbedrcom01BL.getNewValueLevel(campBaseDTO);
				// 改变前的累计金额
				campBaseDTO.setOldTotalAmount(campBaseDTO.getCurTotalAmount());
				// 获取化妆次数, 可兑换金额(化妆次数用)
				binbedrcom01BL.getNewValueBtimes(campBaseDTO);
				// 改变前的可兑换金额(化妆次数用)
				campBaseDTO.setOldBtimesAmount(campBaseDTO.getCurBtimesAmount());
				// 改变前的会员等级
				campBaseDTO.setOldLevelId(campBaseDTO.getCurLevelId());
				// 改变前的化妆次数
				campBaseDTO.setOldBtimes(campBaseDTO.getCurBtimes());
				if (0 == campBaseDTO.getCurTotalAmount() 
						&& 0 == campBaseDTO.getCurBtimesAmount() && 0 == campBaseDTO.getCurBtimes()) {
					continue;
				}
				// 处理规则文件
				executeRuleFile(campBaseDTO);
				// 取得化妆次数MQ消息体
				MQInfoDTO mqInfoDTO = binbedrcom01BL.getBtimesMQMessage(campBaseDTO);
				if(mqInfoDTO != null) {
					// 发送MQ消息处理
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				}
				boolean amountFlag = campBaseDTO.getOldTotalAmount() != campBaseDTO.getCurTotalAmount();
				boolean btimesFlag = campBaseDTO.getOldBtimes() != campBaseDTO.getCurBtimes();
				// 累计金额或者化妆次数发生变化
				if (amountFlag || btimesFlag) {
					Map<String, Object> updateMap = new HashMap<String, Object>();
					// 会员信息ID
					updateMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
					if (amountFlag) {
						updateMap.put("totalAmounts", campBaseDTO.getCurTotalAmount());
					}
					if (btimesFlag) {
						updateMap.put("curBtimes", campBaseDTO.getCurBtimes());
					}
					// 更新会员信息扩展表
					binOLCM31_BL.updateMemberExtInfo(updateMap);
				}
				// 提交事务
				binBEDRHAN02_Service.manualCommit();
			} catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {					// 事务回滚
					binBEDRHAN02_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EDR00014");
				// 会员ID
				batchLoggerDTO.addParam(String.valueOf(campBaseDTO.getMemberInfoId()));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				// 更新重算信息
				updateReCalcInfo(campBaseDTO);
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}
	
	/**
	 * 更新重算信息
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * @throws CherryBatchException 
	 * 
	 * @throws Exception
	 */
	public void updateReCalcInfo(CampBaseDTO campBaseDTO) throws CherryBatchException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
			// 所属品牌
			map.put("brandInfoID", campBaseDTO.getBrandInfoId());
			// 会员ID
			map.put("memberInfoId", campBaseDTO.getMemberInfoId());
			// 等级和化妆次数重算
			map.put("reCalcType", DroolsConstants.RECALCTYPE0);
			// 单据日期
			map.put("reCalcDate", campBaseDTO.getTicketDate());
			// 更新重算信息表
			binbedrcom01BL.insertReCalcInfo(map);
			// 提交事务
			binBEDRHAN02_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEDRHAN02_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
			batchLoggerDTO.setCode("EDR00015");
			// 会员ID
			batchLoggerDTO.addParam(String.valueOf(campBaseDTO.getMemberInfoId()));
			// 重算日期
			batchLoggerDTO.addParam(campBaseDTO.getTicketDate());
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
					this.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
			flag = CherryBatchConstants.BATCH_WARNING;
		}
	}
	
	/**
	 * 处理规则文件
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * 
	 * @throws Exception
	 */
	@Override
	public void executeRuleFile(CampBaseDTO campBaseDTO) throws Exception {
		// 清零处理
		binbedrjon05BL.ruleExec(campBaseDTO);
	}

}

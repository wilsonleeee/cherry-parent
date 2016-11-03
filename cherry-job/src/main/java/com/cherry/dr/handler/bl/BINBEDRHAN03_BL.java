/*	
 * @(#)BINBEDRHAN03_BL.java     1.0 2012/04/23
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.BaseHandler_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleBatchExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.handler.interfaces.BINBEDRHAN03_IF;
import com.cherry.dr.handler.service.BINBEDRHAN03_Service;

/**
 * 降级处理BL
 * 
 * @author hub
 * @version 1.0 2012.04.23
 */
public class BINBEDRHAN03_BL implements BINBEDRHAN03_IF, BaseHandler_IF{
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	/** 降级处理Service */
	@Resource
	private BINBEDRHAN03_Service binBEDRHAN03_Service;
	
	@Resource
	private CampRuleBatchExec_IF binbedrjon07BL;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;	
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN03_BL.class.getName());
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private boolean isNoClub;
	
	/**
	 * 
	 * 降级处理batch主处理
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	@Override
	public int tran_levelDown(Map<String, Object> map) throws Exception {
		String orgIdStr = String.valueOf(map.get("organizationInfoId"));
		String brandIdStr = String.valueOf(map.get("brandInfoId"));
		isNoClub = "3".equals(binOLCM14_BL.getConfigValue("1299", orgIdStr, brandIdStr));
		if (isNoClub) {
			levelDown(map);
		} else {
			map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE2);
			List<Map<String, Object>> clubList = binOLCM31_BL.selClubRuleList(map);
			if (null == clubList || clubList.isEmpty()) {
				return flag;
			}
			for (Map<String, Object> clubMap : clubList) {
				map.put("memberClubId", clubMap.get("clubId"));
				map.put("clubCode", binOLCM31_BL.getClubCode(map));
				levelDown(map);
			}
		}
		return flag;
	}
	
	private void levelDown(Map<String, Object> map) throws Exception {
		totalCount = 0;
		failCount = 0;
		// 业务日期
		String busDate = binBEDRHAN03_Service.getBussinessDate(map);
		// 系统日期
		String sysDate = binBEDRHAN03_Service.getForwardSYSDate();
		map.put("busDate", busDate);
		map.put("sysDate", sysDate);
		try {
			// 去除会员BATCH执行状态
			binBEDRHAN03_Service.updateClearBatchExec(map);
			if (isNoClub) {
				// 更新会员BATCH执行状态
				binBEDRHAN03_Service.updateMemBatchExec(map);
			} else {
				// 更新会员BATCH执行状态
				binBEDRHAN03_Service.updateClubMemDGBatchExec(map);
			}
			// 提交事务
			binBEDRHAN03_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEDRHAN03_Service.manualRollback();
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
		if (isNoClub) {
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, "BIN_MemberInfoID");
		} else {
			map.put(CherryBatchConstants.SORT_ID, "memberInfoId");
		}
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
			// 取得需要降级处理的会员信息List
			List<CampBaseDTO> levelDownList = null;
			if (isNoClub) {
				levelDownList = binBEDRHAN03_Service.getLevelDownList(map);
			} else {
				levelDownList = binBEDRHAN03_Service.getClubLevelDownList(map);
			}
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(levelDownList)) {
				// 执行清零处理
				executeMembers(levelDownList, map);
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(levelDownList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		try {
			// 去除会员BATCH执行状态
			binBEDRHAN03_Service.updateClearBatchExec(map);
			// 提交事务
			binBEDRHAN03_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEDRHAN03_Service.manualRollback();
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
	}
	
	/**
	 * 执行降级处理
	 * 
	 * @param campBaseDTOList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<CampBaseDTO> levelDownList, Map<String, Object> map) throws Exception {
		totalCount += levelDownList.size();
		// 业务日期
		String busDate = (String) map.get("busDate");
		// 会员有效期截止日时
		String ticketDate = DateUtil.suffixDate(busDate, 1);
		// 系统日期
		String sysDate = (String) map.get("sysDate");
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		// 获取等级信息
		List<Map<String, Object>> memLevels = null;
		// 需要匹配规则的会员列表
		List<CampBaseDTO> memberList = new ArrayList<CampBaseDTO>();
		int memberClubId = 0;
		if (!isNoClub) {
			memberClubId = Integer.parseInt(String.valueOf(map.get("memberClubId")));
		}
		for (int i = 0; i < levelDownList.size(); i++) {
			CampBaseDTO campBaseDTO = levelDownList.get(i);
			if (!isNoClub) {
				campBaseDTO.setMemberClubId(memberClubId);
				campBaseDTO.setClubCode((String) map.get("clubCode"));
			}
			// 会员信息ID
			map.put("memberInfoId", campBaseDTO.getMemberInfoId());
			// 会员卡号
			String memCard = binbedrcom01BL.getMemCard(map);
			// 会员卡号
			campBaseDTO.setMemCode(memCard);
			// 业务日期
			campBaseDTO.setBusinessDate(busDate);
			// 计算日期
			campBaseDTO.setCalcDate(sysDate);
			// 业务类型
			campBaseDTO.setTradeType(DroolsConstants.TRADETYPE_DG);
			// 作成者
			campBaseDTO.setCreatedBy(CherryBatchConstants.PGM_BINBEDRHAN03);
			// 作成程序名
			campBaseDTO.setCreatePGM(CherryBatchConstants.PGM_BINBEDRHAN03);
			// 更新者
			campBaseDTO.setUpdatedBy(CherryBatchConstants.PGM_BINBEDRHAN03);
			// 更新程序名
			campBaseDTO.setUpdatePGM(CherryBatchConstants.PGM_BINBEDRHAN03);
			// 单据产生日期
			campBaseDTO.setTicketDate(ticketDate);
			// 重算区分
			campBaseDTO.setReCalcCount(DroolsConstants.RECALCCOUNT_0);
			if (null == memLevels || memLevels.isEmpty()) {
				// 获取等级信息
				memLevels = binbedrcom01BL.getMemLevelcomList(campBaseDTO);
				if (null == memLevels || memLevels.isEmpty()) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EDR00018");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					throw new CherryBatchException(batchExceptionDTO);
				}
			} 
			campBaseDTO.setMemberLevels(memLevels);
			// 理由
			campBaseDTO.setReason(DroolsConstants.REASON_0);
			// 所属组织
			map.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
			// 所属品牌
			map.put("brandInfoID", campBaseDTO.getBrandInfoId());
			// 等级和化妆次数重算
			map.put("reCalcType", DroolsConstants.RECALCTYPE0);
			// 单据日期
			map.put("saleTime", ticketDate);
			// 单据日期
			map.put("tradeType", DroolsConstants.TRADETYPE_DG);
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
//			// 根据业务类型，业务时间取得执行次数
//			int count = binbedrcom01BL.getCountByType(map);
//			if (count > 0) {
//				levelDownList.remove(i);
//				i--;
//				continue;
//			}
			// 累计金额
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_1);
			// 当前累计金额
			String totalAmount = binbedrcom01BL.getCurNewValueByKbn(campBaseDTO);
			double totalAmountDou = 0;
			if (null != totalAmount && !"".equals(totalAmount)) {
				totalAmountDou = Double.parseDouble(totalAmount);
			}
			// 当前累计金额
			campBaseDTO.setCurTotalAmount(totalAmountDou);
			memberList.add(campBaseDTO);
		}
		if (!memberList.isEmpty()) {
			// 处理规则文件(批处理)
			executeRuleFileMulti(memberList);
		}
		for (CampBaseDTO campBaseDTO : memberList) {
			try {
				// 更新会员信息
				updateMemberInfo(campBaseDTO);
				if (campBaseDTO.isMatchRule()) {
					// 取得等级MQ消息体
					MQInfoDTO mqInfoDTO = binbedrcom01BL.getLevelMQMessage(campBaseDTO);
					if(mqInfoDTO != null) {
						// 需要同步天猫会员
						if (binOLCM31_BL.needSync(campBaseDTO.getMemberInfoId(), campBaseDTO.getBrandCode())) {
							Map<String, Object> tmSyncInfo = new HashMap<String, Object>();
							tmSyncInfo.put("memberInfoId", campBaseDTO.getMemberInfoId());
							tmSyncInfo.put("brandCode", campBaseDTO.getBrandCode());
							tmSyncInfo.put("PgmName", "BINBEDRHAN03");
							// 同步天猫会员
							binOLCM31_BL.syncTmall(tmSyncInfo);
						}
						// 发送MQ消息处理
						binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
						//插入零时表处理
						binOLCM31_BL.addTempAdjustMember(campBaseDTO.getMemberInfoId(), campBaseDTO.getOrganizationInfoId(), campBaseDTO.getBrandInfoId());

					}
				}
				// 提交事务
				binBEDRHAN03_Service.manualCommit();
			} catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {
					// 事务回滚
					binBEDRHAN03_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EDR00016");
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
	 * 处理规则文件
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * 
	 * @throws Exception
	 */
	@Override
	public void executeRuleFile(CampBaseDTO campBaseDTO) throws Exception {
		if (campBaseDTO.getMemberClubId() > 0) {
			// 取得组规则库
			Map<String, Object> groupRule = ruleEngineIF.getGroupRule(campBaseDTO.getOrgCode(), 
					campBaseDTO.getBrandCode(),  DroolsConstants.CAMPAIGN_TYPE2 + "_" + campBaseDTO.getMemberClubId());
			if (null == groupRule || groupRule.isEmpty()) {
				return;
			}
		}
		// 会员有效期截止日
		String levelEndDate = DateUtil.coverTime2YMD(campBaseDTO.getLevelEndDate(), DateUtil.DATE_PATTERN);
		if (null != levelEndDate && 
				levelEndDate.equals(campBaseDTO.getBusinessDate())) {
			List<CampBaseDTO> campBaseList = new ArrayList<CampBaseDTO>();
			// 单据时间
			String ticketDate = DateUtil.suffixDate(campBaseDTO.getBusinessDate(), 1);
			campBaseDTO.setTicketDate(ticketDate);
			if (null == campBaseDTO.getMemberLevels()) {
				// 获取等级信息
				List<Map<String, Object>> memLevels = binbedrcom01BL.getMemLevelcomList(campBaseDTO);
				if (null == memLevels || memLevels.isEmpty()) {
					BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
					batchExceptionDTO.setBatchName(this.getClass());
					batchExceptionDTO.setErrorCode("EDR00018");
					batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
					throw new CherryBatchException(batchExceptionDTO);
				}
				campBaseDTO.setMemberLevels(memLevels);
				// 理由
				campBaseDTO.setReason(DroolsConstants.REASON_0);
			}
			campBaseList.add(campBaseDTO);
			// 处理规则文件(批处理)
			executeRuleFileMulti(campBaseList);
			for (CampBaseDTO campBase : campBaseList) {
				// 更新会员信息
				updateMemberInfo(campBase);
			}
		}
	}
	
	/**
	 * 处理规则文件(批处理)
	 * 
	 * @param campBaseList
	 * 			会员活动基础 List
	 * 
	 * @throws Exception
	 */
	private void executeRuleFileMulti(List<CampBaseDTO> campBaseList) throws Exception {
		// 降级规则执行
		binbedrjon07BL.ruleExec(campBaseList);
	}
	

	/**
	 * 更新会员信息
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * 
	 * @throws Exception
	 */
	private void updateMemberInfo(CampBaseDTO campBaseDTO) throws Exception {
		// 设定等级有效期
		//CampRuleUtil.execLevelValidity(campBaseDTO);
		// 单据时间
		String ticketDate = campBaseDTO.getTicketDate();
		String ticketDateStr = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
		String nextDay = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, ticketDateStr, 1);
		nextDay = DateUtil.suffixDate(nextDay, 0);
		// 取得会员等级有效期开始日和结束日
		Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(campBaseDTO.getTradeType(), ticketDate, campBaseDTO.getCurLevelId(), nextDay, campBaseDTO.getMemberInfoId());
		if (null != levelDateInfo && !levelDateInfo.isEmpty()) {
			// 有效期开始日
			campBaseDTO.setLevelStartDate((String) levelDateInfo.get("levelStartDate"));
			// 有效期结束日
			campBaseDTO.setLevelEndDate((String) levelDateInfo.get("levelEndDate"));
		}
		if (campBaseDTO.isMatchRule()) {
			// 获取单号(BATCH特定业务的单号获取：清零、降级等)
			String ticketNumber = binbedrcom01BL.ticketNumberBatch(campBaseDTO);
			// 获取不到单号时
			if (CherryChecker.isNullOrEmpty(ticketNumber)) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EDR00017");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 会员ID
				batchExceptionDTO.addErrorParam(String.valueOf(campBaseDTO.getMemberInfoId()));
				// 业务类型
				batchExceptionDTO.addErrorParam(campBaseDTO.getTradeType());
				throw new CherryBatchException(batchExceptionDTO);
			}
			campBaseDTO.setBillId(ticketNumber);
			// 重算的时候
			if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1) {
				// 取得原会员卡号信息
				Map<String, Object> oldMemCodeInfo = binbedrcom01BL.getOldMemCodeInfo(campBaseDTO);
				if (null != oldMemCodeInfo && !oldMemCodeInfo.isEmpty()) {
					// 原卡号
					String oldMemCode = (String) oldMemCodeInfo.get("memCode");
					// 单据号
					String billId = (String) oldMemCodeInfo.get("billId");
					campBaseDTO.setMemCode(oldMemCode);
					campBaseDTO.setBillId(billId);
				}
			}
			//if (0 == campBaseDTO.getOldLevelId()) {
				campBaseDTO.setOldLevelId(campBaseDTO.getPrevLevel());
			//}
			// 插入规则执行履历表:等级
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, true);
			// 所有规则
			campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_0);
			String billId = campBaseDTO.getBillId();
			// 入会或者升级首单号
			campBaseDTO.setBillId(null);
			if (campBaseDTO.getMemClubLeveId() > 0) {
				binbedrcom01BL.updClubMemberInfo(campBaseDTO);
			} else {
				// 更新会员信息表
				binbedrcom01BL.updMemberInfo(campBaseDTO);
			}
			campBaseDTO.setBillId(billId);
		} else {
			if (campBaseDTO.getMemberClubId() > 0) {
				// 更新会员等级有效期
				binBEDRHAN03_Service.updateClubLevelValidity(campBaseDTO);
			} else {
				// 更新会员等级有效期
				binBEDRHAN03_Service.updateLevelValidity(campBaseDTO);
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
			if (campBaseDTO.getMemberClubId() > 0) {
				map.put("memberClubId", campBaseDTO.getMemberClubId());
			}
			// 查询是否已登录重算表
			int count = binbedrcom01BL.getBTReCalcCount(map);
			if (count == 0) {
				// 更新重算信息表
				binbedrcom01BL.insertReCalcInfo(map);
				// 提交事务
				binBEDRHAN03_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEDRHAN03_Service.manualRollback();
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
}

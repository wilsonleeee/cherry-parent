/*	
 * @(#)BINBEDRHAN04_BL.java     1.0 2013/05/13
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.BaseHandler_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleBatchExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.handler.interfaces.BINBEDRHAN04_IF;
import com.cherry.dr.handler.service.BINBEDRHAN04_Service;

/**
 * 积分清零处理BL
 * 
 * @author hub
 * @version 1.0 2013.05.13
 */
public class BINBEDRHAN04_BL implements BINBEDRHAN04_IF, BaseHandler_IF{
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 积分清零处理Service */
	@Resource
	private BINBEDRHAN04_Service binBEDRHAN04_Service;
	
	@Resource
	private CampRuleBatchExec_IF binbedrpoi04BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN04_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/** 系统配置项 共通 **/
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	private boolean isNoClub;
	
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
					campBaseDTO.getBrandCode(),  DroolsConstants.CAMPAIGN_TYPE8888 + "_" + campBaseDTO.getMemberClubId());
			if (null == groupRule || groupRule.isEmpty()) {
				return;
			}
		}
		// 取得单个清零的会员信息
		Map<String, Object> memClearInfo = binBEDRHAN04_Service.getMemClearInfo(campBaseDTO);
		if (null != memClearInfo && !memClearInfo.isEmpty()) {
			// 当前总积分
			double curTotalPoint = Double.parseDouble(String.valueOf(memClearInfo.get("curTotalPoint")));
			// 下次积分清零日期
			String curDealDate = (String) memClearInfo.get("curDealDate");
			// 会员积分ID
			int memberPointId = Integer.parseInt(String.valueOf(memClearInfo.get("memberPointId")));
			// 积分信息 DTO
			PointDTO pointInfo = new PointDTO();
			// 会员积分ID
			pointInfo.setMemberPointId(memberPointId);
			if (curTotalPoint <= 0) {
				// 更新积分变化表清零标识
				binBEDRHAN04_Service.updatePointClearFlag(campBaseDTO);
				if (!CherryChecker.isNullOrEmpty(curDealDate)) {
					// 下次清零日期
					pointInfo.setCurDealDate(null);
					// 即将失效积分
					pointInfo.setCurDisablePoint(0);
					// 更新处理区分
					pointInfo.setUpPTKbn("2");
					int clubId = campBaseDTO.getMemberClubId();
					if (clubId > 0) {
						pointInfo.setMemberClubId(clubId);
						pointInfo.setClubIdStr(String.valueOf(clubId));
					}
					// 更新会员积分表
					binBEDRHAN04_Service.updateMemberPoint(pointInfo);
				}
				return;
			}
			if (null != curDealDate) {
				pointInfo.setCurDealDate(curDealDate);
			}
			// 会员信息ID
			memClearInfo.put("memberInfoId", campBaseDTO.getMemberInfoId());
			// 会员卡号
			String memCard = binbedrcom01BL.getMemCard(memClearInfo);
			// 会员卡号
			campBaseDTO.setMemCode(memCard);
			// 业务类型
			campBaseDTO.setTradeType(DroolsConstants.TRADETYPE_PC);
			// 理由
			campBaseDTO.setReason(DroolsConstants.REASON_0);
			// 当前积分
			pointInfo.setCurTotalPoint(curTotalPoint);
			// 累计兑换积分
			pointInfo.setCurTotalChanged(Double.parseDouble(String.valueOf(memClearInfo.get("curTotalChanged"))));
			// 可兑换积分
			pointInfo.setCurChangablePoint(Double.parseDouble(String.valueOf(memClearInfo.get("curChangablePoint"))));
			// 累计失效积分
			pointInfo.setTotalDisablePoint(Double.parseDouble(String.valueOf(memClearInfo.get("totalDisablePoint"))));
			// 前卡积分
			pointInfo.setPreCardPoint(Double.parseDouble(String.valueOf(memClearInfo.get("preCardPoint"))));
			// 上回积分失效日期
			pointInfo.setPreDisableDate((String) memClearInfo.get("preDisableDate"));
			// 即将失效积分
			pointInfo.setCurDisablePoint(Double.parseDouble(String.valueOf(memClearInfo.get("curDisablePoint"))));
			// 积分清零集合
			Map<String, Object> pcMap = new HashMap<String, Object>();
			pcMap.put("fromTime", memClearInfo.get("preDisPointTime"));
			pcMap.put("toTime", campBaseDTO.getTicketDate());
			campBaseDTO.getProcDates().put("PC", pcMap);
			campBaseDTO.setPointInfo(pointInfo);
			List<CampBaseDTO> campBaseList = new ArrayList<CampBaseDTO>();
			campBaseList.add(campBaseDTO);
			// 处理规则文件(批处理)
			executeRuleFileMulti(campBaseList);
			// 更新会员信息
			updateMemberInfo(campBaseDTO);
		}
	}
	/**
	 * 
	 * 积分清零batch主处理
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	@Override
	public int tran_pointClear(Map<String, Object> map) throws Exception {
		String orgIdStr = String.valueOf(map.get("organizationInfoId"));
		String brandIdStr = String.valueOf(map.get("brandInfoId"));
		isNoClub = "3".equals(binOLCM14_BL.getConfigValue("1299", orgIdStr, brandIdStr));
		if (isNoClub) {
			memPointClear(map);
		} else {
			map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE8888);
			List<Map<String, Object>> clubList = binOLCM31_BL.selClubRuleList(map);
			if (null == clubList || clubList.isEmpty()) {
				return flag;
			}
			for (Map<String, Object> clubMap : clubList) {
				map.put("memberClubId", clubMap.get("clubId"));
				map.put("clubCode", binOLCM31_BL.getClubCode(map));
				memPointClear(map);
			}
		}
		return flag;
	}
	
	private void memPointClear(Map<String, Object> map) throws Exception {
		totalCount = 0;
		failCount = 0;
		// 业务日期
		String busDate = binBEDRHAN04_Service.getBussinessDate(map);
		// 系统日期
		String sysDate = binBEDRHAN04_Service.getForwardSYSDate();
		map.put("busDate", busDate);
		map.put("sysDate", sysDate);
		// 是否下发积分清零明细
		String psdKbn = binOLCM14_BL.getConfigValue("1118", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId")));
		map.put("PSDKBN", psdKbn);
		try {
			if (isNoClub) {
				// 去除会员BATCH执行状态
				binBEDRHAN04_Service.updateClearBatchExec(map);
				// 更新会员BATCH执行状态
				binBEDRHAN04_Service.updateMemBatchExec(map);
				// 更新最近有积分产生的会员
				binBEDRHAN04_Service.updatePointBatchExec(map);
			} else {
				// 去除会员BATCH执行状态
				binBEDRHAN04_Service.updateClubClearBatchExec(map);
				// 更新会员BATCH执行状态
				binBEDRHAN04_Service.updateClubMemBatchExec(map);
				// 更新最近有积分产生的会员
				binBEDRHAN04_Service.updateClubPointBatchExec(map);
			}
			// 提交事务
			binBEDRHAN04_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEDRHAN04_Service.manualRollback();
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
			// 取得需要积分清零的会员信息List
			List<Map<String, Object>> memClearList = null;
			if (isNoClub) {
				memClearList = binBEDRHAN04_Service.getMemClearList(map);
			} else {
				memClearList = binBEDRHAN04_Service.getClubMemClearList(map);
			}
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memClearList)) {
				try {
					// 执行清零处理
					executeMembers(memClearList, map);
				} catch (Exception e) {
					logger.error("Points clear exception：" + e.getMessage(),e);
				}
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(memClearList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		try {
			if (isNoClub) {
				// 去除会员BATCH执行状态
				binBEDRHAN04_Service.updateClearBatchExec(map);
			} else {
				// 去除会员BATCH执行状态
				binBEDRHAN04_Service.updateClubClearBatchExec(map);
			}
			// 提交事务
			binBEDRHAN04_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEDRHAN04_Service.manualRollback();
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
	 * 执行清零处理
	 * 
	 * @param campBaseDTOList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<Map<String, Object>> memClearList, Map<String, Object> map) throws Exception {
		totalCount += memClearList.size();
		// 业务日期
		String busDate = (String) map.get("busDate");
		//String nextDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, busDate, 1);
		// 会员有效期截止日时
		String ticketDate = DateUtil.suffixDate(busDate, 0);
		// 系统日期
		String sysDate = (String) map.get("sysDate");
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		// 组织ID
		int organizationInfoId = Integer.parseInt(map.get("organizationInfoId").toString());
		// 品牌ID
		int brandInfoId = Integer.parseInt(map.get("brandInfoId").toString());
		// 需要删除下次清零信息的会员列表
		List<CampBaseDTO> curDealMemList = new ArrayList<CampBaseDTO>();
		// 需要匹配规则的会员列表
		List<CampBaseDTO> clearMemberList = new ArrayList<CampBaseDTO>();
		for (int i = 0; i < memClearList.size(); i++) {
			Map<String, Object> memClearInfo = memClearList.get(i);
			// 当前总积分
			double curTotalPoint = Double.parseDouble(String.valueOf(memClearInfo.get("curTotalPoint")));
			// 下次积分清零日期
			String curDealDate = (String) memClearInfo.get("curDealDate");
			// 会员积分ID
			int memberPointId = Integer.parseInt(String.valueOf(memClearInfo.get("memberPointId")));
			boolean noCurDealDate = CherryChecker.isNullOrEmpty(curDealDate);
			if (curTotalPoint <= 0 && noCurDealDate) {
				continue;
			}
			CampBaseDTO campBaseDTO = new CampBaseDTO();
			// 组织ID
			campBaseDTO.setOrganizationInfoId(organizationInfoId);
			// 品牌ID
			campBaseDTO.setBrandInfoId(brandInfoId);
			// 会员信息ID
			int memberInfoId = Integer.parseInt(memClearInfo.get("memberInfoId").toString());
			campBaseDTO.setMemberInfoId(memberInfoId);
			// 会员信息ID
			map.put("memberInfoId", memberInfoId);
			// 所属柜台ID
			int organizationId = 0;
			if (isNoClub) {
				organizationId = Integer.parseInt(memClearInfo.get("organizationId").toString());
			} else {
				int clubOrgId = Integer.parseInt(memClearInfo.get("clubOrganizationId").toString());
				if (clubOrgId != 0) {
					organizationId = clubOrgId;
				}
			}
			if (0 != organizationId) {
				campBaseDTO.setBelDepartId(organizationId);
			}
			// 会员卡号
			String memCard = binbedrcom01BL.getMemCard(map);
			// 会员卡号
			campBaseDTO.setMemCode(memCard);
			// 业务日期
			campBaseDTO.setBusinessDate(busDate);
			// 计算日期
			campBaseDTO.setCalcDate(sysDate);
			// 业务类型
			campBaseDTO.setTradeType(DroolsConstants.TRADETYPE_PC);
			// 作成者
			campBaseDTO.setCreatedBy(CherryBatchConstants.PGM_BINBEDRHAN04);
			// 作成程序名
			campBaseDTO.setCreatePGM(CherryBatchConstants.PGM_BINBEDRHAN04);
			// 更新者
			campBaseDTO.setUpdatedBy(CherryBatchConstants.PGM_BINBEDRHAN04);
			// 更新程序名
			campBaseDTO.setUpdatePGM(CherryBatchConstants.PGM_BINBEDRHAN04);
			// 入会日期
			String joinDate = null;
			if (isNoClub) {
				joinDate = (String) memClearInfo.get("joinDate");
			} else {
				String clubJoinDate = (String) memClearInfo.get("clubJoinDate");
				if (!CherryChecker.isNullOrEmpty(clubJoinDate)) {
					joinDate = clubJoinDate;
				}
			}
			campBaseDTO.setJoinDate(joinDate);
			// 当前等级
			campBaseDTO.setCurLevelId(Integer.parseInt(String.valueOf(memClearInfo.get("memberLevel"))));
			// 品牌代码
			campBaseDTO.setBrandCode((String) memClearInfo.get("brandCode"));
			// 组织代码
			campBaseDTO.setOrgCode((String) memClearInfo.get("orgCode"));
			// 单据时间
			campBaseDTO.setTicketDate(ticketDate);
			// 重算区分
			campBaseDTO.setReCalcCount(DroolsConstants.RECALCCOUNT_0);
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
			// 积分信息 DTO
			PointDTO pointInfo = new PointDTO();
			if (!isNoClub) {
				String clubIdStr = String.valueOf(map.get("memberClubId"));
				int clubId = Integer.parseInt(clubIdStr);
				campBaseDTO.setMemberClubId(clubId);
				pointInfo.setMemberClubId(clubId);
				pointInfo.setClubIdStr(clubIdStr);
				campBaseDTO.setClubCode((String) map.get("clubCode"));
			}
			// 会员积分ID
			pointInfo.setMemberPointId(memberPointId);
			// 当前积分
			pointInfo.setCurTotalPoint(curTotalPoint);
			// 累计兑换积分
			pointInfo.setCurTotalChanged(Double.parseDouble(String.valueOf(memClearInfo.get("curTotalChanged"))));
			// 可兑换积分
			pointInfo.setCurChangablePoint(Double.parseDouble(String.valueOf(memClearInfo.get("curChangablePoint"))));
			// 累计失效积分
			pointInfo.setTotalDisablePoint(Double.parseDouble(String.valueOf(memClearInfo.get("totalDisablePoint"))));
			// 前卡积分
			pointInfo.setPreCardPoint(Double.parseDouble(String.valueOf(memClearInfo.get("preCardPoint"))));
			// 上回积分失效日期
			pointInfo.setPreDisableDate((String) memClearInfo.get("preDisableDate"));
			// 积分清零集合
			Map<String, Object> pcMap = new HashMap<String, Object>();
			pcMap.put("fromTime", memClearInfo.get("preDisPointTime"));
			pcMap.put("toTime", ticketDate);
			campBaseDTO.getProcDates().put("PC", pcMap);
			campBaseDTO.setPointInfo(pointInfo);
			if (curTotalPoint <= 0 && !noCurDealDate) {
				// 下次清零日期
				pointInfo.setCurDealDate(null);
				// 即将失效积分
				pointInfo.setCurDisablePoint(0);
				curDealMemList.add(campBaseDTO);
			} else {
				// 验证是否需要重算(Batch处理)
				boolean needReCalc = binbedrcom01BL.needReCalcBatch(map);
				if (needReCalc) {
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EDR00051");
					// 会员ID
					batchLoggerDTO.addParam(String.valueOf(campBaseDTO.getMemberInfoId()));
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
							this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					continue;
				}
				// 下次清零日期
				pointInfo.setCurDealDate(curDealDate);
				// 即将失效积分
				pointInfo.setCurDisablePoint(Double.parseDouble(String.valueOf(memClearInfo.get("curDisablePoint"))));
				// 设置会员属性
				binOLCM31_BL.execMemberInfo(campBaseDTO);
				clearMemberList.add(campBaseDTO);
			}
		}
		for (CampBaseDTO campBaseDTO : curDealMemList) {
			try {
				// 积分信息 DTO
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				pointInfo.setUpPTKbn("2");
				// 更新会员积分表
				binBEDRHAN04_Service.updateMemberPoint(pointInfo);
				// 更新积分变化表清零标识
				binBEDRHAN04_Service.updatePointClearFlag(campBaseDTO);
				// 取得等级MQ消息体
				MQInfoDTO mqInfoDTO = binOLCM31_BL.getPointMQMessage(campBaseDTO);
				if(mqInfoDTO != null) {
					// 发送MQ消息处理
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				}
				// 提交事务
				binBEDRHAN04_Service.manualCommit();
			} catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {
					// 事务回滚
					binBEDRHAN04_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error(e.getMessage(),e);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EDR00050");
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
		if (!clearMemberList.isEmpty()) {
			// 处理规则文件(批处理)
			executeRuleFileMulti(clearMemberList);
		}
		for (CampBaseDTO campBaseDTO : clearMemberList) {
			try {
				campBaseDTO.getExtArgs().put("PSDKBN", map.get("PSDKBN"));
				double oldPoint = 0;
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				if (null != pointInfo) {
					oldPoint = pointInfo.getCurTotalPoint();
				}
				// 更新会员信息
				boolean sendFlag = updateMemberInfo(campBaseDTO);
				if (sendFlag) {
					// 需要同步天猫会员
					if (null != pointInfo && pointInfo.getCurTotalPoint() != oldPoint
							&& binOLCM31_BL.needSync(campBaseDTO.getMemberInfoId(), campBaseDTO.getBrandCode())) {
						Map<String, Object> tmSyncInfo = new HashMap<String, Object>();
						tmSyncInfo.put("memberInfoId", campBaseDTO.getMemberInfoId());
						tmSyncInfo.put("brandCode", campBaseDTO.getBrandCode());
						tmSyncInfo.put("PgmName", "BINBEDRHAN04");
						// 同步天猫会员
						binOLCM31_BL.syncTmall(tmSyncInfo);
					}
					// 取得等级MQ消息体
					MQInfoDTO mqInfoDTO = binOLCM31_BL.getPointMQMessage(campBaseDTO);
					if(mqInfoDTO != null) {
						// 发送MQ消息处理
						binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
					}
					// 提交事务
					binBEDRHAN04_Service.manualCommit();
				}
			} catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {
					// 事务回滚
					binBEDRHAN04_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error(e.getMessage(),e);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EDR00050");
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
	 * 处理规则文件(批处理)
	 * 
	 * @param campBaseList
	 * 			会员活动基础 List
	 * 
	 * @throws Exception
	 */
	private void executeRuleFileMulti(List<CampBaseDTO> campBaseList) throws Exception {
		// 积分清零规则执行
		binbedrpoi04BL.ruleExec(campBaseList);
	}
	
	/**
	 * 更新会员信息
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * 
	 * @throws Exception
	 */
	private boolean updateMemberInfo(CampBaseDTO campBaseDTO) throws Exception {
		boolean sendFlag = false;
		// 积分信息 DTO
		PointDTO pointInfo = campBaseDTO.getPointInfo();
		// 改变前的下次清零日
		String oldDealDate = pointInfo.getCurDealDate();
		if (null != oldDealDate && !oldDealDate.isEmpty()) {
			oldDealDate = DateUtil.coverTime2YMD(oldDealDate, DateUtil.DATE_PATTERN);
		}
		// 改变前的下次清零积分
		double oldDisablePoint = pointInfo.getCurDisablePoint();
		// 改变后的下次清零日
		String nextDealDate = null;
		// 改变后的下次清零积分
		double nextDisablePoint = 0;
		// 积分清零集合
		Map<String, Object> pcMap = (Map<String, Object>) campBaseDTO.getProcDates().get("PC");
		if (null != pcMap && !pcMap.isEmpty()) {
			// 清零执行标识
			String pcFlag = (String) pcMap.get("PC_FLAG");
			if ("1".equals(pcFlag)) {
				// 清零明细列表
				List<Map<String, Object>> pcDetailList = (List<Map<String, Object>>) pcMap.get("pcDetailList");
				if (null != pcDetailList && !pcDetailList.isEmpty()) {
					if (pcDetailList.size() > 1) {
						// 按积分清零日期进行排序(升序)
						pcListSort(pcDetailList);
					}
					// 本次清零日
					String ticketDate = campBaseDTO.getTicketDate();
					String ticketDay = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATE_PATTERN);
					Map<String, Object> firstClearMap = pcDetailList.get(0);
					// 最早的清零日
					String firstClearDay = (String) firstClearMap.get("dealDate");
					// 需要清除的积分
					double point = Double.parseDouble(firstClearMap.get("point").toString());
					// 有失效的积分需要清零
					if (CherryChecker.compareDate(ticketDay, firstClearDay) == 0) {
						if (point > 0) {
							// 本次清零最晚的单据时间
							String changeDate = (String) firstClearMap.get("changeDate");
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
							BaseDTO baseDto = new BaseDTO();
							// 作成程序名
							baseDto.setCreatePGM(CherryBatchConstants.PGM_BINBEDRHAN04);
							// 更新程序名
							baseDto.setUpdatePGM(CherryBatchConstants.PGM_BINBEDRHAN04);
							// 作成者
							baseDto.setCreatedBy(CherryBatchConstants.PGM_BINBEDRHAN04);
							// 更新者
							baseDto.setUpdatedBy(CherryBatchConstants.PGM_BINBEDRHAN04);
							campBaseDTO.setBillId(ticketNumber);
							// 会员积分变化主记录
							PointChangeDTO pointChange = new PointChangeDTO();
							int clubId = campBaseDTO.getMemberClubId();
							if (clubId > 0) {
								pointChange.setMemberClubId(clubId);
								pointChange.setClubIdStr(String.valueOf(clubId));
							}
							// 组织ID
							pointChange.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
							// 品牌ID
							pointChange.setBrandInfoId(campBaseDTO.getBrandInfoId());
							// 单据号
							pointChange.setTradeNoIF(ticketNumber);
							// 业务类型
							pointChange.setTradeType(campBaseDTO.getTradeType());
							// 柜台ID
							pointChange.setOrganizationId(campBaseDTO.getBelDepartId());
							// 会员信息ID
							pointChange.setMemberInfoId(campBaseDTO.getMemberInfoId());
							// 会员卡号
							pointChange.setMemCode(campBaseDTO.getMemCode());
							// 积分变化日期
							pointChange.setChangeDate(ticketDate);
							// 积分值
							pointChange.setPoint(-point);
							// 积分清零标识
							String clearFlag = "1";
							if (null != campBaseDTO.getExtArgs().get("RECLEAR_FLAG")) {
								clearFlag = (String) campBaseDTO.getExtArgs().get("RECLEAR_FLAG");
							}
							pointChange.setClearFlag(clearFlag);
							ConvertUtil.convertDTO(pointChange, baseDto, false);
							// 会员积分变化明细记录
							PointChangeDetailDTO changeDetailDTO = new PointChangeDetailDTO();
							// 积分值
							changeDetailDTO.setPoint(-point);
							// 原因
							//changeDetailDTO.setReason(DroolsConstants.POINT_REASON_5);
							// 积分类型
							changeDetailDTO.setPointType(DroolsConstants.POINTTYPE2);
							// 宿主规则ID
							Object subcampIdObj = pcMap.get("SUBCAMPID");
							// 取得规则内容
							String reason = null;
							if (null != subcampIdObj) {
								Integer subcampId = Integer.parseInt(subcampIdObj.toString());
								changeDetailDTO.setMainRuleId(subcampId);
								// 匹配的规则代号
								campBaseDTO.setSubCampCodes((String) pcMap.get("SUBCAMPCODE"));
								// 规则描述ID
								String ruleDptId = (String) pcMap.get("RULEDPT_ID");
								campBaseDTO.setRuleIds(ruleDptId);
								Map<String, Object> searchMap = new HashMap<String, Object>();
								searchMap.put("ruleDptId", ruleDptId);
								// 取得规则内容
								reason = binBEDRHAN04_Service.getRuleContent(searchMap);
								
							}
							if (CherryChecker.isNullOrEmpty(reason)) {
								reason = DroolsConstants.POINT_REASON_5;
							}
							changeDetailDTO.setReason(reason);
							ConvertUtil.convertDTO(changeDetailDTO, baseDto, false);
							List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
							changeDetailList.add(changeDetailDTO);
							pointChange.setChangeDetailList(changeDetailList);
							pointInfo.setPointChange(pointChange);
							// 更新会员积分变化信息
							binBEDRHAN04_Service.updatePointChangeInfo(pointChange);
							// 累计积分
							double totalPoint = DoubleUtil.sub(pointInfo.getCurTotalPoint(), point);
							// 改变前的累计积分
							pointInfo.setOldTotalPoint(pointInfo.getCurTotalPoint());
							// 当前累计积分
							pointInfo.setCurTotalPoint(totalPoint);
							// 插入规则执行履历表:累计积分
							binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_5, true);
							campBaseDTO.emptyRuleIds();
							campBaseDTO.emptySubCampCodes();
							// 改变前的可兑换积分
							pointInfo.setOldChangablePoint(pointInfo.getCurChangablePoint());
							pointInfo.setCurChangablePoint(totalPoint);
							// 插入规则执行履历表:可兑换积分
							binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_7, true);
							// 累计失效积分
							double totalDisPoint = DoubleUtil.add(pointInfo.getTotalDisablePoint(), point);
							// 改变前的累计失效积分
							pointInfo.setOldTotalDisPoint(pointInfo.getTotalDisablePoint());
							// 当前累计失效积分
							pointInfo.setTotalDisablePoint(totalDisPoint);
							// 插入规则执行履历表:累计失效积分
							binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_8, true);
							// 上回积分失效单据截止日期
							pointInfo.setPrePCBillTime(changeDate);
							// 上回积分失效日期
							pointInfo.setPreDisableDate(ticketDate);
							// 插入规则执行履历表:上回积分失效单据截止日期
							binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_9, true);
							// 重算情况
							if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1) {
								// 重算时不更新积分最后变化时间
								pointInfo.setLcTimeKbn("1");
							}
							// 更新处理区分
							pointInfo.setUpPTKbn("1");
							// 更新会员积分表
							binBEDRHAN04_Service.updateMemberPoint(pointInfo);
							Map<String, Object> upMap = new HashMap<String, Object>();
							// 本次积分清零日期
							upMap.put("disPointTime", ticketDate);
							// 开始时间
							upMap.put("fromTime", pcMap.get("fromTime"));
							// 开始时间
							upMap.put("toTime", changeDate);
							// 会员信息ID
							upMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
							// 品牌ID
							upMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
							// 组织ID
							upMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
							if (campBaseDTO.getMemberClubId() > 0) {
								upMap.put("memberClubId", campBaseDTO.getMemberClubId());
							}
							// 更新积分清零日期
							binBEDRHAN04_Service.updatePointDisPointTime(upMap);
							sendFlag = true;
							// 需要下发积分清零明细
							if ("2".equals(campBaseDTO.getExtArgs().get("PSDKBN"))) {
								Map<String, Object> clearMap = new HashMap<String, Object>();
								// 会员信息ID
								clearMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
								// 被清时间
								clearMap.put("clearedTime", pointChange.getChangeDate());
								// 被清积分值
								double clearedPoints = -pointChange.getPoint();
								// 重新下发次数
								int reSendCount = 0;
								// 下发状态
								int sendStatus = 0;
								boolean addFlag = true;
								String memberCode = null;
								// 重算情况
								if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1) {
									// 取得会员积分变化的最大重算次数
									Map<String, Object> clearRecord = binBEDRHAN04_Service.getClearRecordById(clearMap);
									if (null != clearRecord && !clearRecord.isEmpty()) {
										// 被清积分值(历史记录)
										double clearedPointsPre = Double.parseDouble(clearRecord.get("clearedPoints").toString());
										if (clearedPoints != clearedPointsPre) {
											memberCode = (String) clearRecord.get("memCode");
											reSendCount = Integer.parseInt(clearRecord.get("reSendCount").toString());
											reSendCount++;
											sendStatus = 9;
										} else {
											addFlag = false;
										}
									}
								}
								if (addFlag) {
									if (null == memberCode) {
										memberCode = campBaseDTO.getMemCode();
									}
									// 组织ID
									clearMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
									// 品牌ID
									clearMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
									// 会员卡号
									clearMap.put("memCode", memberCode);
									// 清零后积分
									clearMap.put("afterPoints", totalPoint);
									// 被清积分值
									clearMap.put("clearedPoints", clearedPoints);
									// 重新下发次数
									clearMap.put("reSendCount", reSendCount);
									// 下发状态
									clearMap.put("sendStatus", sendStatus);
									// 作成程序名
									clearMap.put(CherryBatchConstants.CREATEPGM, CherryBatchConstants.PGM_BINBEDRHAN04);
									// 更新程序名
									clearMap.put(CherryBatchConstants.UPDATEPGM, CherryBatchConstants.PGM_BINBEDRHAN04);
									// 作成者
									clearMap.put(CherryBatchConstants.CREATEDBY, CherryBatchConstants.PGM_BINBEDRHAN04);
									// 更新者
									clearMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.PGM_BINBEDRHAN04);
									// 插入会员积分变化明细表
									binBEDRHAN04_Service.addPointsClearRecord(clearMap);
								}
							}
						}
						pcDetailList.remove(0);
					}
					
					if (!pcDetailList.isEmpty()) {
						// 下次积分清零信息
						Map<String, Object> nextClearMap = pcDetailList.get(0);
						// 下次的清零日
						nextDealDate = (String) nextClearMap.get("dealDate");
						// 需要清除的积分
						nextDisablePoint = Double.parseDouble(nextClearMap.get("point").toString());
					}
 				}
			}
			// 需要更新下次清零信息
			if (null == oldDealDate && null != nextDealDate || 
					null != oldDealDate && null == nextDealDate || 
							null != oldDealDate && null != nextDealDate && CherryChecker.compareDate(oldDealDate, nextDealDate) != 0 || 
					oldDisablePoint != nextDisablePoint) {
				pointInfo.setCurDealDate(nextDealDate);
				pointInfo.setCurDisablePoint(nextDisablePoint);
				// 更新处理区分
				pointInfo.setUpPTKbn("2");
				// 更新会员积分表
				binBEDRHAN04_Service.updateMemberPoint(pointInfo);
				sendFlag = true;
			}
			// 更新积分变化表清零标识
			binBEDRHAN04_Service.updatePointClearFlag(campBaseDTO);
		}
		return sendFlag;
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
				binBEDRHAN04_Service.manualCommit();
			}
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEDRHAN04_Service.manualRollback();
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
	 * 
	 * 按积分清零日期进行排序(升序)
	 * 
	 * @param list 需要排序的list
	 * 
	 */
	private void pcListSort(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
		 	public int compare(Map<String, Object> detail1, Map<String, Object> detail2) {
		 		// 积分清零日期1
            	String dealDate1 = (String) detail1.get("dealDate");
            	// 积分清零日期2
            	String dealDate2 = (String) detail2.get("dealDate");
            	if(CherryChecker.compareDate(dealDate1, dealDate2) <= 0) {
            		return -1;
            	} else {
            		return 1;
            	}
            }
		});
	}
}

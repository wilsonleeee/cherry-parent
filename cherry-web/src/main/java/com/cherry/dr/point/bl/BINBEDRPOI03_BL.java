/*	
 * @(#)BINBEDRPOI03_BL.java     1.0 2013/04/24
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
package com.cherry.dr.point.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.point.service.BINBEDRPOI01_Service;

/**
 * 历史积分调整处理 BL
 * 
 * @author hub
 * @version 1.0 2013.04.24
 */
public class BINBEDRPOI03_BL implements CampRuleExec_IF{
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	@Resource
	private BINBEDRPOI01_Service binbedrpoi01_Service;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRPOI03_BL.class.getName());

	/**
	 * 历史积分调整处理执行
	 * 
	 * @param campBaseDTO
	 *            会员实体
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		// 积分
		campBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE3);
		campBaseDTO.getExtArgs().put("TZ", "1");
		// 创建积分信息List
		binbedrpoi01_Service.createPointInfoList(campBaseDTO);
		campBaseDTO.getExtArgs().remove("TZ");
		try {
			PointChangeDTO pointChange = campBaseDTO.getPointInfo().getPointChange();
			if (null != pointChange &&
					null != pointChange.getChangeDetailList() 
					&& !pointChange.getChangeDetailList().isEmpty()) {
				// 会员活动batch处理
				ruleEngineIF.executeRuleBatch(campBaseDTO, null);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			List<Map<String, Object>> ruleLogList = (List<Map<String, Object>>) campBaseDTO.getExtArgs().get("RuleLogList");
			if (null != ruleLogList) {
				for (Map<String, Object> ruleLogInfo : ruleLogList) {
					// 日志信息
					String msg = (String) ruleLogInfo.get("msg");
					// 打印日志
					logger.info(msg);
				}
			}
		}
		Map<String, Object> extArgs = campBaseDTO.getExtArgs();
		extArgs.remove("execedRules");
		extArgs.remove("pointList");
		extArgs.remove("RuleLogList");
		extArgs.remove("RuleNameList");
		// 会员积分规则执行后处理
		afterExec(campBaseDTO);
	}
	
	/**
	 * 是否需要进行历史积分调整判断
	 * 
	 * @param campBaseDTO
	 *            会员实体
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception {
		// 取得会员初始积分信息
		Map<String, Object> MemInitialInfo = null;
		int clubId = campBaseDTO.getMemberClubId();
		if (clubId == 0) {
			MemInitialInfo = binOLCM31_BL.getMemPointInitInfo(campBaseDTO.getMemberInfoId());
		} else {
			MemInitialInfo = binOLCM31_BL.getClubMemPointInitInfo(campBaseDTO.getMemberInfoId(), clubId);
		}
		if (null == MemInitialInfo || MemInitialInfo.isEmpty()) {
			// 该会员没有初始导入积分记录
			logger.info(DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00005, new String[] {campBaseDTO.getBillId()}));
			return;
		}
		// 初始导入时间
		String initialTime = (String) MemInitialInfo.get("initialTime");
		if (CherryChecker.isNullOrEmpty(initialTime)) {
			// 该会员没有初始导入积分记录
			logger.info(DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00005, new String[] {campBaseDTO.getBillId()}));
			return;
		}
		// 初始导入日期
		String initialDay = DateUtil.coverTime2YMD(initialTime, DateUtil.DATETIME_PATTERN);
		// 初始导入日前三个月作为截止日
		initialDay = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, initialDay, -3);
		// 单据时间
		String ticketDate = campBaseDTO.getTicketDate();
		// 单据日期
		String ticketDay = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
		// 单据早于截止日不重算积分
		if (DateUtil.compareDate(ticketDay, initialDay) < 0) {
			logger.info(DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00004, new String[] {campBaseDTO.getBillId()}));
			return;
		}
		// 执行历史积分调整处理
		campBaseDTO.getExtArgs().put("POI03_EXEC_KBN", "1");
	}
	
	/**
	 *会员积分规则执行后处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception {
		PointChangeDTO pointChange = campBaseDTO.getPointInfo().getPointChange();
		if (null != pointChange) {
			// 更新会员积分信息(历史积分调整)
			binbedrpoi01_Service.updateHistoryPointInfo(campBaseDTO);
		}
	}

}

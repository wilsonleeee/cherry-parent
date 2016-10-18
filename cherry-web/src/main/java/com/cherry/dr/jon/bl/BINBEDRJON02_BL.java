/*	
 * @(#)BINBEDRJON02_BL.java     1.0 2011/08/23	
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
package com.cherry.dr.jon.bl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM01_Service;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;

/**
 * 会员升降级规则执行 BL
 * 
 * @author lipc
 * @version 1.0 2011.08.23
 */
public class BINBEDRJON02_BL implements CampRuleExec_IF {

	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRJON02_BL.class.getName());

	@Resource
	private RuleEngine_IF ruleEngineIF;

	@Resource
	private BINBEDRCOM01_Service binbedrcom01Service;

	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;

	/**
	 * 会员升降级规则执行
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO)
			throws Exception {
		// 升降级
		campBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE2);
		// 是否打印日志
		boolean isLog = RuleFilterUtil.isRuleLog(campBaseDTO);
		// 开始时间
		long rstartTime = 0;
		// 结束时间
		long rendTime = 0;
		if (isLog) {
			rstartTime = System.currentTimeMillis();
		}
		// 会员活动规则batch处理
		ruleEngineIF.executeRuleBatch(campBaseDTO, null);
		if (isLog) {
			rendTime = System.currentTimeMillis();
			// 运行时间
			double subTime = rendTime - rstartTime;
			// 运行时间日志内容
			String msg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00011, new String[] {String.valueOf(subTime)});
			logger.info(msg);
		}
		if ("1".equals(campBaseDTO.getExtArgs().get("NO_AFTEXEC"))) {
			return;
		}
		// 规则执行后处理
		afterExec(campBaseDTO);
	}

	/**
	 * 规则执行前处理
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void beforExec(CampBaseDTO campBaseDTO)
			throws Exception {
	}

	/**
	 * 规则执行后处理
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void afterExec(CampBaseDTO campBaseDTO)
			throws Exception {
		// 规则匹配成功
		if (campBaseDTO.isMatchRule()) {
			try {
				// 是否打印日志
				boolean isLog = RuleFilterUtil.isRuleLog(campBaseDTO);
				// 开始时间
				long rstartTime = 0;
				// 结束时间
				long rendTime = 0;
				if (isLog) {
					rstartTime = System.currentTimeMillis();
				}
				// 以成为当前等级的时间作为有效期开始时间
				if (RuleFilterUtil.isCurLevelTime(campBaseDTO.getCurLevelId(), campBaseDTO.getMemberLevels())) {
					// 设定等级有效期
					CampRuleUtil.execLevelValidity(campBaseDTO);
					// 以成为正式等级的时间作为有效期开始时间
				} else {
					// 单据时间
					String ticketDate = campBaseDTO.getTicketDate();
					// 取得会员等级有效期开始日和结束日
					Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(campBaseDTO.getTradeType(), ticketDate, campBaseDTO.getCurLevelId(), ticketDate, campBaseDTO.getMemberInfoId());
					if (null != levelDateInfo && !levelDateInfo.isEmpty()) {
						// 有效期开始日
						campBaseDTO.setLevelStartDate((String) levelDateInfo.get("levelStartDate"));
						// 有效期结束日
						campBaseDTO.setLevelEndDate((String) levelDateInfo.get("levelEndDate"));
					}
				}
				if (isLog) {
					rendTime = System.currentTimeMillis();
					// 运行时间
					double subTime = rendTime - rstartTime;
					// 运行时间日志内容
					String msg = DroolsMessageUtil.getMessage(
							DroolsMessageUtil.IDR00009, new String[] {String.valueOf(subTime)});
					logger.info(msg);
				}
				if (0 == campBaseDTO.getMemberClubId()) {
					// 更新会员信息表
					binbedrcom01Service.updMemberInfo(campBaseDTO);
				} else {
					// 更新会员信息表（会员俱乐部等级）
					binbedrcom01Service.updClubMemberInfo(campBaseDTO);
				}
			} catch (Exception e) {
				logger.error(DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00003, new String[] { campBaseDTO
								.getMemCode() }));
				throw e;
			}
			// 所有规则
			campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_0);
		}
		// 插入规则执行履历表:等级
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, campBaseDTO.isMatchRule());
	}
}

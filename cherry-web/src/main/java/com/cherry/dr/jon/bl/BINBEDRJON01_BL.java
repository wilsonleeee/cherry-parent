/*	
 * @(#)BINBEDRJON01_BL.java     1.0 2011/05/12	
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

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.dr.jon.service.BINBEDRJON01_Service;


/**
 * 会员入会规则执行 BL
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class BINBEDRJON01_BL implements CampRuleExec_IF {
	
	private static Logger logger = LoggerFactory
	.getLogger(BINBEDRJON01_BL.class.getName());
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINBEDRJON01_Service binbedrjon01_Service;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 *会员入会规则执行
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception{
		// 入会
		campBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE1);
		// 是否打印日志
		boolean isLog = RuleFilterUtil.isRuleLog(campBaseDTO);
		// 开始时间
		long rstartTime = 0;
		// 结束时间
		long rendTime = 0;
		if (isLog) {
			rstartTime = System.currentTimeMillis();
		}
		// 会员活动batch处理
		ruleEngineIF.executeRuleBatch(campBaseDTO, null);
		if (isLog) {
			rendTime = System.currentTimeMillis();
			// 运行时间
			double subTime = rendTime - rstartTime;
			// 运行时间日志内容
			String msg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00008, new String[] {String.valueOf(subTime)});
			logger.info(msg);
		}
		// 会员入会规则执行后处理
		afterExec(campBaseDTO);
	}
	
	/**
	 *会员入会规则执行前处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception{
	}
	
	/**
	 *会员入会规则执行后处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception{
		// 规则匹配成功
		if (!campBaseDTO.isMatchRule()) {
			// 给予默认等级
			CampRuleUtil.giveDefaultLevel(campBaseDTO);
			// 取得规则ID
			int ruleId = binOLCM31_BL.getRuleIdByCode(CherryConstants.CONTENTCODE1);
			campBaseDTO.addRuleId(String.valueOf(ruleId));
		}
		try {
			// 入会时间调整准则
			if (null == campBaseDTO.getJnDateKbn() 
					|| "".equals(campBaseDTO.getJnDateKbn())) {
				// 入会时间调整准则
				String jnDateKbn = binOLCM14_BL.getConfigValue("1076", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()));
				campBaseDTO.setJnDateKbn(jnDateKbn);
				// 以消费首单为准
				if ("2".equals(jnDateKbn)) {
					// 单据产生日期
					String ticketDate = campBaseDTO.getTicketDate();
					// 取得年月日
					String ticketYMD = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
					// 设置入会日期
					campBaseDTO.setJoinDate(ticketYMD);
				}
			}
			if (0 == campBaseDTO.getMemberClubId()) {
				// 更新会员信息表
				binbedrjon01_Service.updateMemberInfo(campBaseDTO);
			} else {
				// 更新会员信息表(会员俱乐部)
				binbedrjon01_Service.updateMemberInfoClub(campBaseDTO);
				if (0 == campBaseDTO.getMemClubLeveId()) {
					// 插入会员等级信息表(俱乐部)
					binbedrjon01_Service.addMemClubLevel(campBaseDTO);
				} else {
					// 更新会员等级信息表(俱乐部)
					binbedrjon01_Service.updateMemClubLevel(campBaseDTO);
				}
			}
		} catch (Exception e) {
			logger.error(DroolsMessageUtil.getMessage(
					DroolsMessageUtil.EDR00003, new String[] { campBaseDTO
							.getMemCode() }));
			throw e;
		}
		// 所有规则
		campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_0);
		// 插入规则执行履历表:等级
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, campBaseDTO.isMatchRule());
	}
	

}

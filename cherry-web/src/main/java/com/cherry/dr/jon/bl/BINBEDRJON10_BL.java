/*	
 * @(#)BINBEDRJON10_BL.java     1.0 2013/10/11	
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;

/**
 * 会员等级计算规则执行 BL
 * 
 * @author hub
 * @version 1.0 2013.10.11
 */
public class BINBEDRJON10_BL implements CampRuleExec_IF{
	
	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRJON10_BL.class.getName());
			
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/**
	 *会员等级计算规则执行
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		// 入会
		campBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE1);
		campBaseDTO.getExtArgs().put("LCK", "2");
		String busDate = binOLCM31_BL.getBusDate(campBaseDTO);
		campBaseDTO.setTicketDate(busDate + " 00:00:00");
		// 默认等级
		if (CherryConstants.LEVELSTATUS_1.equals(campBaseDTO.getLevelStatus())) {
			// 等级为默认等级时将当前等级至为无等级状态
			campBaseDTO.setCurLevelId(0);
		}
		if (null == campBaseDTO.getMemberLevels()) {
			// 取得会员等级List
			List<Map<String, Object>> memLevelList = binbedrcom01BL.getMemLevelcomList(campBaseDTO);
			campBaseDTO.setMemberLevels(memLevelList);
		}
		// 会员活动batch处理
		ruleEngineIF.executeRuleBatch(campBaseDTO, null);
		// 会员入会规则执行后处理
		afterExec(campBaseDTO);
		
	}

	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception {
		
	}

	@Override
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception {
		if (campBaseDTO.isMatchRule()) {
			campBaseDTO.getExtArgs().put("LCK_ID", campBaseDTO.getRuleIds());
		}
	}

}

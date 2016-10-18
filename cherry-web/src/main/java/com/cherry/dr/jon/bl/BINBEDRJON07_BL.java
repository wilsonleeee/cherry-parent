/*	
 * @(#)BINBEDRJON07_BL.java     1.0 2012/04/23	
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

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.CampRuleBatchExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
/**
 * 降级规则执行 BL
 * 
 * @author hub
 * @version 1.0 2011.08.25
 */
public class BINBEDRJON07_BL implements CampRuleBatchExec_IF{
	
	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRJON07_BL.class.getName());
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
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
	public void afterExec(List<CampBaseDTO> campBaseList) throws Exception {
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
	public void beforExec(List<CampBaseDTO> campBaseList) throws Exception {
		// 等级计算时累计金额统计方式
		String tjkbn = binOLCM14_BL.getConfigValue("1137", String.valueOf(campBaseList.get(0).getOrganizationInfoId()),
				 String.valueOf(campBaseList.get(0).getBrandInfoId()));
		boolean istj = "2".equals(tjkbn);
		// 规则执行之前清空规则匹配记录
		for (CampBaseDTO campBaseDTO : campBaseList) {
			campBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE9999);
//			if (!campBaseDTO.getExtArgs().containsKey("BDKBN") 
//					&& null != campBaseDTO.getBrandCode() && 
//					"ysl".equalsIgnoreCase(campBaseDTO.getBrandCode().trim())) {
//				campBaseDTO.getExtArgs().put("BDKBN", "1");
//			}
			if (istj) {
				campBaseDTO.getExtArgs().put("TJKBN", tjkbn);
			}
		}
		
	}
	
	/**
	 * 降级规则执行
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(List<CampBaseDTO> campBaseList) throws Exception {
		// 规则执行前处理
		beforExec(campBaseList);
		// 是否打印日志
		boolean isLog = false;
		if (!campBaseList.isEmpty()) {
			isLog = RuleFilterUtil.isRuleLog(campBaseList.get(0));
		}
		// 开始时间
		long rstartTime = 0;
		// 结束时间
		long rendTime = 0;
		if (isLog) {
			rstartTime = System.currentTimeMillis();
		}
		// 会员活动batch处理(多个实体)
		ruleEngineIF.executeRuleMulti(campBaseList, null);
		if (isLog) {
			rendTime = System.currentTimeMillis();
			// 运行时间
			double subTime = rendTime - rstartTime;
			// 运行时间日志内容
			String msg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00012, new String[] {String.valueOf(subTime)});
			logger.info(msg);
		}
	}

}

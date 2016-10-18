/*	
 * @(#)BINBEDRJON05_BL.java     1.0 2011/08/25	
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

import com.cherry.cm.core.CherryChecker;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.mq.mes.common.CherryMQException;

/**
 * 清零规则执行 BL
 * 
 * @author hub
 * @version 1.0 2011.08.25
 */
public class BINBEDRJON05_BL implements CampRuleExec_IF{

	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
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
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception {}
	
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
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception {}
	
	/**
	 * 清零规则执行
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		campBaseDTO.emptySubCampCodes();
		campBaseDTO.emptyRuleIds();
//		boolean flag = true;
//		// 非重算的情况
//		if (campBaseDTO.getReCalcFlg() != DroolsConstants.RECALCFLG_1 && campBaseDTO.getCurLevelId() == 1) {
//			flag = CampRuleUtil.isNeedClear(campBaseDTO.getJoinDate(), campBaseDTO.getBusinessDate(), 12);
//		}
//		campBaseDTO.setSendMQFlg(false);
//		if (flag) {
			// 获取单号(BATCH特定业务的单号获取：清零、降级等)
			String ticketNumber = binbedrcom01BL.ticketNumberBatch(campBaseDTO);
			// 获取不到单号时
			if (CherryChecker.isNullOrEmpty(ticketNumber)) {
				// 获取不到规则库
				String errMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00011, new String[] { campBaseDTO.getMemCode() });
				throw new CherryMQException(errMsg);
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
			// 累计金额清零
			campBaseDTO.setCurTotalAmount(0);
			campBaseDTO.setCurBtimesAmount(0);
			campBaseDTO.addRuleId("27");
			// 插入规则执行履历表:累计金额
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_1, campBaseDTO.isMatchRule());
			// 插入规则执行履历表:可兑换金额(化妆次数用)
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_4, campBaseDTO.isMatchRule());
			// 所有规则
			campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_0);
			campBaseDTO.emptyRuleIds();
			// 化妆次数清零
			campBaseDTO.setCurBtimes(0);
			campBaseDTO.addRuleId("28");
			// 插入规则执行履历表:化妆次数
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_2, campBaseDTO.isMatchRule());
			// 所有规则
			campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_2);
			campBaseDTO.emptyRuleIds();
//			campBaseDTO.setSendMQFlg(true);
//		}
	}

}

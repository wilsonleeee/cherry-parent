/*	
 * @(#)LevelBTimesHandler.java     1.0 2011/8/18		
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
package com.cherry.dr.jon.handler;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.BaseHandler_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleHandler_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;


/**
 * 会员等级和化妆次数处理器
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public class LevelBTimesHandler implements RuleHandler_IF, BaseHandler_IF{
	
	@Resource
	private CampRuleExec_IF binbedrjon01BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon02BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon03BL;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon06BL;
	
	@Resource
	private CampRuleExec_IF binbedrpoi01BL;
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/**
	 * 执行会员等级和化妆次数规则文件
	 * 
	 * @param Map
	 *            参数集合
	 * @throws Exception 
	 * 
	 */
	@Override
	public void executeRule(Map<String, Object> map) throws Exception {
		// 验证是否需要执行规则
		boolean isRuleExecFlg = binbedrcom01BL.isRuleExec(map);
		if (!isRuleExecFlg) {
			return; 
		}
		try {
			// 生成执行规则前的DTO
			CampBaseDTO campBaseDTO = binbedrcom01BL.getCampBaseDTO(map);
			// 处理规则文件
			executeRuleFile(campBaseDTO);
			// 取得等级MQ消息体
			MQInfoDTO mqInfoDTO = binbedrcom01BL.getLevelMQMessage(campBaseDTO);
			if(mqInfoDTO != null) {
				// 发送MQ消息处理
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			}
			// 取得化妆次数MQ消息体
			mqInfoDTO = binbedrcom01BL.getBtimesMQMessage(campBaseDTO);
			if(mqInfoDTO != null) {
				// 发送MQ消息处理
				binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
			}
		} catch (Exception e) {
			// 会员卡号
			String memberCode = (String) map.get("memberCode");
			// 会员等级和化妆次数处理器执行规则时发生异常
			String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00008, new String[]{memberCode, e.getMessage()});
			throw new CherryDRException(errMsg, DroolsConstants.ERROR_TYPE_1);
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
		campBaseDTO.initFact(null);
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(campBaseDTO.getTradeType())) {
			// 退货规则处理
			binbedrjon06BL.ruleExec(campBaseDTO);
			// 插入规则执行履历表:（累计金额）
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_1, true);
		} else {
			// 设置条件：等级信息，购买产品等
			binbedrcom01BL.conditionSetting(campBaseDTO);
			// 插入规则执行履历表:（累计金额）
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_1, true);
			// 当前会员等级
			int curLevelId = campBaseDTO.getCurLevelId();
			// 非会员
			if (DroolsConstants.NOT_MEMBER == curLevelId) {
				// 会员入会规则处理
				binbedrjon01BL.ruleExec(campBaseDTO);
			} else {
				// 会员升降级规则处理
				binbedrjon02BL.ruleExec(campBaseDTO);
			}
			// 积分规则处理
			//binbedrpoi01BL.ruleExec(campBaseDTO);
			// 会员化妆次数规则处理
			//binbedrjon03BL.ruleExec(campBaseDTO);
		}
	}

}

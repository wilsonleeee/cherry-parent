/*	
 * @(#)BINBEDRPOI01_BL.java     1.0 2012/03/15	
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

import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.point.service.BINBEDRPOI01_Service;


/**
 * 会员积分规则执行 BL
 * 
 * @author hub
 * @version 1.0 2012.03.15
 */
public class BINBEDRPOI01_BL implements CampRuleExec_IF {
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	@Resource
	private BINBEDRPOI01_Service binbedrpoi01_Service;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRPOI01_BL.class.getName());
	
	/**
	 *会员积分规则执行
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception{
		// 会员积分规则执行前处理
		beforExec(campBaseDTO);
		try {
			// 会员活动batch处理
			ruleEngineIF.executeRuleBatch(campBaseDTO, null);
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
	 *会员积分规则执行前处理
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception{
		// 积分
		campBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE3);
		// 取得组规则库
		Map<String, Object> groupRule = ruleEngineIF.getGroupRule(campBaseDTO.getOrgCode(), 
				campBaseDTO.getBrandCode(), campBaseDTO.getGrpRuleKey());
		if (!campBaseDTO.getExtArgs().containsKey("RGPINFO")) {
			// 配置参数集合
			Map<String, Object> gpInfo = null;
			if (null != groupRule && !groupRule.isEmpty()) {
				for(Map.Entry<String,Object> gr: groupRule.entrySet()){
					Map<String, Object> groupInfo = (Map<String, Object>) gr.getValue();
					gpInfo = (Map<String, Object>) groupInfo.get("GPINFO");
				}
			}
			campBaseDTO.getExtArgs().put("RGPINFO", gpInfo);
		}
		// 创建积分信息List
		binbedrpoi01_Service.createPointInfoList(campBaseDTO);
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
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception{
		// 规则匹配成功
	//	if (campBaseDTO.isMatchRule()) {
		// 积分信息 DTO
			PointDTO pointInfo = campBaseDTO.getPointInfo();
			if (null == pointInfo) {
				return;
			}
			// 会员积分变化主记录
			PointChangeDTO pointChange = pointInfo.getPointChange();
			if (null == pointChange || !"1".equals(pointChange.getMatchKbn())) {
				return;
			}
			// 更新会员积分变化信息
			binbedrpoi01_Service.updatePointChangeInfo(campBaseDTO);
			// 积分值
			double point = pointChange.getPoint();
			// 兑换积分值
			double changePoint = -pointChange.getChangePoint();
			//if (point != 0) {
				// 累计积分
				double totalPoint = DoubleUtil.add(pointInfo.getCurTotalPoint(), point);
				// 改变前的累计积分
				pointInfo.setOldTotalPoint(pointInfo.getCurTotalPoint());
				// 当前累计积分
				pointInfo.setCurTotalPoint(totalPoint);
				if (pointInfo.getOldTotalPoint() != pointInfo.getCurTotalPoint()) {
					// 插入规则执行履历表:累计积分
					binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_5, true);
				}
				// 所有规则
				campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_5);
				campBaseDTO.emptyRuleIds();
				campBaseDTO.emptySubCampCodes();
				// 改变前的可兑换积分
				pointInfo.setOldChangablePoint(pointInfo.getCurChangablePoint());
				pointInfo.setCurChangablePoint(totalPoint);
				if (pointInfo.getOldChangablePoint() != pointInfo.getCurChangablePoint()) {
					// 插入规则执行履历表:可兑换积分
					binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_7, true);
				}
			//}
			//if (changePoint != 0) {
				// 改变前的累计兑换积分
				pointInfo.setOldTotalChanged(pointInfo.getCurTotalChanged());
				// 累计兑换积分
				double TotalChanged = DoubleUtil.add(pointInfo.getCurTotalChanged(), changePoint);
				// 当前累计兑换积分
				pointInfo.setCurTotalChanged(TotalChanged);
				if (pointInfo.getOldTotalChanged() != pointInfo.getCurTotalChanged()) {
					// 插入规则执行履历表:累计兑换积分
					binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_6, true);
				}
			//}
				// 非重算情况并且是前卡产生的积分
				if (point != 0 && campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_0
						&& !binbedrcom01BL.isCurCard(campBaseDTO.getMemberInfoId(), campBaseDTO.getMemCode())) {
					// 累加到前卡积分
					double curPreCardPoint = DoubleUtil.add(pointInfo.getPreCardPoint(), point);
					pointInfo.setPreCardPoint(curPreCardPoint);
				}
			//if (upFlag) {
				// 更新会员积分信息
				binbedrpoi01_Service.updatePointInfo(campBaseDTO);
			//}
			if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1 && 
					"1".equals(campBaseDTO.getExtArgs().get("BIRRD"))) {
				// 生日类规则
				String[] birArr = (String[]) campBaseDTO.getExtArgs().get("BIRARR");
				if (null != birArr && birArr.length > 0) {
					List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
					if (null != changeDetailList) {
						for (PointChangeDetailDTO changeDetail : changeDetailList) {
							Integer mainId = changeDetail.getMainRuleId();
							if (null != mainId) {
								// 匹配到生日类规则
								if (ConvertUtil.isContain(birArr, String.valueOf(mainId))) {
									campBaseDTO.getExtArgs().put("MATDATE", pointChange.getChangeDate());
									break;
								}
							}
						}
					}
				}
			}
		//}
	}
	

}

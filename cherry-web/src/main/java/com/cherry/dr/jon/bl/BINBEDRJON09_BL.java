/*	
 * @(#)BINBEDRJON09_BL.java     1.0 2013/04/09
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;

/**
 * 非关联退货处理 BL
 * 
 * @author hub
 * @version 1.0 2013.04.09
 */
public class BINBEDRJON09_BL implements CampRuleExec_IF{
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	/**
	 * 非关联退货处理执行
	 * 
	 * @param campBaseDTO
	 *            会员实体
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 会员信息ID
		map.put("memberInfoId", campBaseDTO.getMemberInfoId());
		// 获取等级信息
		List<Map<String, Object>> levelList = binbedrcom01BL.getMemLevelcomList(campBaseDTO);
		boolean noExec = true;
		// 当前等级
		int curLevel = campBaseDTO.getCurLevelId();
		for (int i = 0; i < levelList.size(); i++) {
			Map<String, Object> levelInfo = levelList.get(i);
			// 会员等级ID
			int levelId = Integer.parseInt(levelInfo.get("levelId").toString());
			if (levelId == curLevel && i != 0) {
				noExec = false;
			}
		}
		// 已经是最低等级不执行
		if (noExec) {
			return;
		}
		if (campBaseDTO.getMemberClubId() != 0) {
			map.put("memberClubId", campBaseDTO.getMemberClubId());
		}
		boolean isInitAmt = "1".equals(campBaseDTO.getExtArgs().get("INAMT"));
		// 等级变化信息
		List<Map<String, Object>> levelRecordList = binOLCM31_BL.getLevelAllRecordList(map);
		Map<String, Object> searchMap = null;
		if (null != levelRecordList && !levelRecordList.isEmpty()) {
			Map<String, Object> preLevelInfo = null;
			// 组织信息ID
			map.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
			// 品牌ID
			map.put("brandInfoId", campBaseDTO.getBrandInfoId());
			// 当前累计金额
			double curTotalAmount = campBaseDTO.getCurTotalAmount();
			String baseTicketDate = null;
			for (int i = 0; i < levelRecordList.size(); i++) {
				Map<String, Object> levelRecord = levelRecordList.get(i);
				// 业务类型
				String tradeType = (String) levelRecord.get("tradeType");
				String ticketDate = (String) levelRecord.get("ticketDate");
				// 初始导入或者是维护的等级
				if ("IP".equals(tradeType) || "MS".equals(tradeType)) {
					baseTicketDate = ticketDate;
					break;
				}
				// 单号
				map.put("firstBillId", levelRecord.get("billId"));
				// 单据时间
				map.put("fromTime", ticketDate);
				// 取得某个时间点的累计金额
				double totalAmount = 0;
				if (!isInitAmt) {
					// 取得某个时间点的累计金额
					totalAmount = binbedrcom01BL.getTotalAmountByTime(map);
				} else {
					if (null == searchMap) {
						searchMap = new HashMap<String, Object>();
						// 会员ID
						searchMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
					}
					// 结束时间
					searchMap.put("toTime", ticketDate);
					totalAmount = binOLCM31_BL.getTtlAmount(searchMap);
					if (campBaseDTO.getInitAmount() != 0) {
						totalAmount = DoubleUtil.add(totalAmount, campBaseDTO.getInitAmount());
					}
				}
				if (totalAmount <= curTotalAmount) {
					baseTicketDate = ticketDate;
					break;
				} else {
					// 升级记录
					if ("1".equals(levelRecord.get("changeType"))) {
						preLevelInfo = levelRecord;
						preLevelInfo.put("totalAmount", totalAmount);
					}
				}
			}
			if (null != preLevelInfo) {
				// 退货时间
				String srTicketDate = campBaseDTO.getTicketDate();
				int oldLevel = 0;
				// 升级前等级
				if (null != preLevelInfo.get("oldValue")) {
					oldLevel = Integer.parseInt(preLevelInfo.get("oldValue").toString());
				}
				// 还原上次升级前的信息重新执行规则
				CampBaseDTO newCampBaseDTO = new CampBaseDTO();
				ConvertUtil.convertNewDTO(newCampBaseDTO, campBaseDTO, false);
				newCampBaseDTO.setCurLevelId(oldLevel);
				// 单号
				String billId = (String) preLevelInfo.get("billId");
				newCampBaseDTO.setBillId(billId);
				// 单据时间
				String ticketDate = (String) preLevelInfo.get("ticketDate");
				if (!isInitAmt) {
					newCampBaseDTO.setTicketDate(ticketDate);
					// 非关联退货基准点时间
					newCampBaseDTO.setSrRecalcDate(ticketDate);
				} else {
					newCampBaseDTO.setTicketDate(campBaseDTO.getTicketDate());
					// 非关联退货基准点时间
					newCampBaseDTO.setSrRecalcDate(campBaseDTO.getTicketDate());
				}
				// 业务类型
				String tradeType = (String) preLevelInfo.get("tradeType");
				newCampBaseDTO.setTradeType(tradeType);
				newCampBaseDTO.setCurTotalAmount(curTotalAmount);
				
				// 设置条件：等级信息，购买产品等
				binbedrcom01BL.conditionSetting(newCampBaseDTO);
				double amount = 0;
				Map<String, Object> buyInfo = newCampBaseDTO.getBuyInfo();
				if (null != buyInfo && !buyInfo.isEmpty()) {
					double bAmount = Double.parseDouble(buyInfo.get("amount").toString());
					// 累计金额
					double totalAmount = Double.parseDouble(preLevelInfo.get("totalAmount").toString());
					// 差分
					double subAmount = DoubleUtil.sub(totalAmount, curTotalAmount);
					if (bAmount > subAmount) {
						amount = DoubleUtil.sub(bAmount, subAmount);
					}
				}
				newCampBaseDTO.setAmount(amount);
				// 还原后为非会员
				if (DroolsConstants.NOT_MEMBER == oldLevel) {
					// 入会
					newCampBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE1);
				} else {
					// 升降级
					newCampBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE2);
				}
				// 会员活动batch处理
				ruleEngineIF.executeRuleBatch(newCampBaseDTO, null);
				// 重新计算后的等级
				int recalLevel = newCampBaseDTO.getCurLevelId();
				// 如果没有等级给予最低等级
				if (0 == recalLevel) {
					recalLevel = Integer.parseInt(levelList.get(0).get("levelId").toString());
					baseTicketDate = ticketDate;
				}
				if (recalLevel != curLevel) {
					if (newCampBaseDTO.isMatchRule()) {
						baseTicketDate = ticketDate;
					}
					// 新的等级比原等级低
					if (-1 == RuleFilterUtil.compareLevel(recalLevel, curLevel, levelList)) {
						// 升级前会员等级
						campBaseDTO.setPrevLevel(curLevel);
						// 当前会员等级
						campBaseDTO.setCurLevelId(recalLevel);
						// 当前会员等级
						campBaseDTO.setMemberLevel(curLevel);
						// 升级前会员等级
						campBaseDTO.setUpgradeFromLevel(curLevel);
						// 升降级区分:降级
						campBaseDTO.setChangeType(DroolsConstants.UPKBN_2);
						// 等级升降级区分
						campBaseDTO.setLevelChangeType(DroolsConstants.UPKBN_2);
						// 会员等级调整日
						campBaseDTO.setLevelAdjustDay(srTicketDate);
						// 入会或者升级首单号
						campBaseDTO.setFirstBillId(null);
						// 取得规则ID
						int ruleId = binOLCM31_BL.getRuleIdByCode(CherryConstants.CONTENTCODE3);
						campBaseDTO.addRuleId(String.valueOf(ruleId));
						// 子活动代码组
						campBaseDTO.addSubCampCodes(null);
						// 会员等级状态:实际等级
						campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_2);
						campBaseDTO.setMemberLevels(levelList);
						// 取得会员等级有效期开始日和结束日
						Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(tradeType, baseTicketDate, campBaseDTO.getCurLevelId(), srTicketDate, campBaseDTO.getMemberInfoId());
						if (null != levelDateInfo && !levelDateInfo.isEmpty()) {
							// 有效期开始日
							campBaseDTO.setLevelStartDate((String) levelDateInfo.get("levelStartDate"));
							// 有效期结束日
							campBaseDTO.setLevelEndDate((String) levelDateInfo.get("levelEndDate"));
						}
						// 更新会员信息表
						binbedrcom01BL.updMemberInfo(campBaseDTO);
						// 所有规则
						campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_0);
						// 插入规则执行履历表:等级
						binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, true);
					}
				}
			}
		}
	}

	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleHandler_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.interfaces.AnalyzeMemberInitDataMessage_IF;

/**
 * 会员资料处理器
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public class MsgMBHandler implements RuleHandler_IF{
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon10BL;
	
	@Resource(name="binBEMQMES08_BL")
	private AnalyzeMemberInitDataMessage_IF binBEMQMES08_BL;
	
	/**
	 * 会员资料消息处理
	 * 
	 * @param Map
	 *            参数集合
	 * @throws Exception 
	 * 
	 */
	@Override
	public void executeRule(Map<String, Object> map) throws Exception {
		// 明细业务数据行
//		List<Map<String, Object>> detailList = (List<Map<String, Object>>) map.get("detailDataDTOList");
//		if (null == detailList || detailList.isEmpty()) {
//			// 明细业务数据行获取失败
//			String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00006, null);
//			throw new CherryDRException(errMsg);
//		}
//		Map<String, Object> detailMap = (Map<String, Object>) detailList.get(0);
		String clubCode = (String) map.get("clubCode");
		if (CherryChecker.isNullOrEmpty(clubCode, true)) {
			// 会员卡号
			String memberCode = (String) map.get("memberCode");
			// 会员老卡
			String oldMemcode = memberCode;
			// 是否是会员换卡
			boolean isChangeCard = DroolsConstants.MEM_TYPE_CARD_CHANGE.equals(map.get("subType"));
			boolean isClub = false;
			// 会员俱乐部模式
			String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
			// 会员换卡
			if (isChangeCard) {
				memberCode = (String) map.get("newMemcode");
				// 会员卡号
				map.put("memberCode", memberCode);
				if (!"3".equals(clubMod)) {
					isClub = true;
				}
			} else {
				if (!"3".equals(clubMod)) {
					return;
				}
			}
			try {
				// 员工编号
				//map.put("BAcode", detailMap.get("BAcode"));
				// 验证是否是有效的会员
				boolean validFlg = binbedrcom01BL.isValidMember(map);
				if (!validFlg) {
					return;
				}
				// 验证柜台是否需要执行规则
				if (!binOLCM31_BL.isRuleCounter(map)) {
					return;
				}
				if (isClub) {
					// 取得会员所属俱乐部列表
					List<Map<String, Object>> clubList = binbedrcom01BL.getMemClubLevelList(map);
					if (null == clubList || clubList.isEmpty()) {
						return;
					} else {
						for (Map<String, Object> clubMap : clubList) {
							map.putAll(clubMap);
							// 会员资料处理
							mbHander(map, isChangeCard);
						}
					}
				} else {
					// 会员资料处理
					mbHander(map, isChangeCard);
				}
				// 换卡扣积分处理
				if ("1".equals(map.get("isMBPointExec"))) {
					// 组织代码
					String orgCode = (String) map.get("orgCode");
					// 品牌代码
					String brandCode = (String) map.get("brandCode");
					// 建档处理
					CampRuleExec_IF campRuleExec08 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE08);
					// 是否建档
					boolean isMeb = (null != campRuleExec08) ? true : false;
					// 已换卡
					if (checkCardPoint(oldMemcode, memberCode, isMeb)) {
						// 组织ID
						int organizationInfoID = Integer.parseInt(map.get("organizationInfoID").toString());
						// 品牌ID
						int brandInfoID = Integer.parseInt(map.get("brandInfoID").toString());
						// 换卡扣积分
						CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT02);
						if (null != campRuleExec) {
							// 数据来源
							String sourse = (String) map.get("sourse");
							// 员工代号
							String BAcode = (String) map.get("BAcode");
							// 单据号
							String ticketNo = (String) map.get("tradeNoIF");
							// 会员信息
							CampBaseDTO campBaseDTO = new CampBaseDTO();
							campBaseDTO.setOrgCode(orgCode);
							campBaseDTO.setBrandCode(brandCode);
							campBaseDTO.setOrganizationInfoId(organizationInfoID);
							campBaseDTO.setBrandInfoId(brandInfoID);
							campBaseDTO.setChannel(sourse);
							campBaseDTO.setMemCode(memberCode);
							campBaseDTO.setEmployeeCode(BAcode);
							campBaseDTO.setBillId(ticketNo);
							// 单据日期 
							String ticketDateStr = null;
							if (map.containsKey("cardChangeTime")) {
								// 换卡时间
								ticketDateStr = (String) map.get("cardChangeTime");
							} else {
								if (!CherryChecker.isNullOrEmpty(ticketNo)) {
									int startIndex = ticketNo.length() - 17;
									int endIndex = ticketNo.length() - 3;
									if (startIndex > 0) {
										// 单据日期 
										ticketDateStr = ticketNo.substring(startIndex, endIndex);
									}
								}
								// 验证单据日期格式是否正确
								if (CherryChecker.checkDate(ticketDateStr, "yyyyMMddHHmmss")) {
									Date ticketDate = DateUtil.coverString2Date(ticketDateStr, "yyyyMMddHHmmss");
									SimpleDateFormat dateFm = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
									ticketDateStr = dateFm.format(ticketDate);
								}
							}
							campBaseDTO.setTicketDate(ticketDateStr);
							campRuleExec.ruleExec(campBaseDTO);
						}
					}
				}
			} catch (Exception e) {
				// 会员等级和化妆次数处理器执行规则时发生异常
				String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00006, new String[]{memberCode, e.getMessage()});
				throw new CherryMQException(errMsg);
			}
		} else {
			// 会员卡号
			String memberCode = (String) map.get("memberCode");
			try {
				Integer memberClubId = binOLCM31_BL.selMemClubId(map);
				if (null == memberClubId) {
					return;
				}
				map.put("memberClubId", memberClubId);
				// 验证是否是有效的会员
				boolean validFlg = binbedrcom01BL.isValidMember(map);
				if (!validFlg) {
					return;
				}
				// 取得当前会员信息(会员资料)
				CampBaseDTO campBaseDTO = binbedrcom01BL.getCurMemberInfoMZ(map);
				if (null == campBaseDTO) {
					return;
				}
				// 取得等级MQ消息体
				MQInfoDTO mqInfoDTO = binbedrcom01BL.getLevelMQMessage(campBaseDTO);
				if(mqInfoDTO != null) {
					// 发送MQ消息处理
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				}
			} catch (Exception e) {
				// 会员等级和化妆次数处理器执行规则时发生异常
				String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00006, new String[]{memberCode, e.getMessage()});
				throw new CherryMQException(errMsg);
			}
		}
	}
	
	/**
	 * 会员资料处理
	 * 
	 * @param map
	 *			参数集合
	 * @param isChangeCard
	 *			是否换卡
	 * @throws Exception 
	 * 
	 */
	private void mbHander(Map<String, Object> map, boolean isChangeCard) throws Exception {
		if ("1".equals(map.get("isMBRuleExec"))) {
			// 重算时间
			String reCalcTime = (String) map.get("reCalcTime");
			boolean needReCalc = !CherryChecker.isNullOrEmpty(reCalcTime, true);
			boolean changeLevel = "1".equals(map.get("isMBLevelExec"));
			// 需要重算但不需要调整等级
			if (needReCalc && !changeLevel) {
				// 保存并发送重算MQ
				binbedrcom01BL.saveAndSendReCalcMsg(map);
				return;
			}
			// 组织ID
			String orgInfoId = String.valueOf(map.get("organizationInfoID"));
			// 品牌ID
			String brandInfoId = String.valueOf(map.get("brandInfoID"));
			// 会员等级计算方式
			String levelCalcKbn = binOLCM14_BL.getConfigValue("1101", orgInfoId, brandInfoId);
			boolean isRuleExec = "2".equals(levelCalcKbn);
			if (isRuleExec) {
				map.put("isMBLevelExec", "1");
				changeLevel = true;
			}
			// 取得当前会员信息(会员资料)
			CampBaseDTO campBaseDTO = binbedrcom01BL.getCurMemberInfoMB(map);
			if (null == campBaseDTO) {
				return;
			}
			// 需要调用等级维护处理
			if (changeLevel) {
				if (!isRuleExec) {
					// 更新前等级
					Object preMemLevelObj = map.get("preMemLevel");
					if (!CherryChecker.isNullOrEmpty(preMemLevelObj, true)) {
						campBaseDTO.setOldLevelId(Integer.parseInt(preMemLevelObj.toString()));
					}
					// 取得会员等级维护信息
					Map<String, Object> memLevelChangeInfo = binOLCM31_BL.getMemLevelChangeInfo(campBaseDTO);
					if (null != memLevelChangeInfo && !memLevelChangeInfo.isEmpty()) {
						// 会员修改属性处理
						binBEMQMES08_BL.updateMemberExtInfo(memLevelChangeInfo);
					}
				}
				// 需要重算
				if (needReCalc) {
					// 保存并发送重算MQ
					binbedrcom01BL.saveAndSendReCalcMsg(map);
				}
				if (!isRuleExec) {
					return;
				}
			}
			if (isRuleExec) {
				// 等级计算
				binbedrjon10BL.ruleExec(campBaseDTO);
				if (campBaseDTO.isMatchRule()) {
					// 取得会员等级维护信息
					Map<String, Object> memLevelChangeInfo = binOLCM31_BL.getMemLevelChangeInfo(campBaseDTO);
					if (null != memLevelChangeInfo && !memLevelChangeInfo.isEmpty()) {
						// 会员修改属性处理
						binBEMQMES08_BL.updateMemberExtInfo(memLevelChangeInfo);
					}
				}
				return;
			}
			// 组织代码
			String orgCode = campBaseDTO.getOrgCode();
			// 品牌代码
			String brandCode = campBaseDTO.getBrandCode();
			// MQ消息体
			MQInfoDTO mqInfoDTO = null;
			if (0 != campBaseDTO.getCurLevelId()) {
				// 是否需要保级
				boolean keepKbn = false;
				if (isChangeCard) {
					// 建档处理
					CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE08);
					if (null != campRuleExec) {
						// 检查会员等级
						keepKbn = checkLevel(campBaseDTO);
						if (keepKbn) {
							// 发送所有等级履历
							campBaseDTO.getExtArgs().put(DroolsConstants.SEND_RECORDS_KBN, DroolsConstants.SEND_ALL_RECORDS);
						}
					}
				}
				// 取得等级MQ消息体
				mqInfoDTO = binbedrcom01BL.getLevelMQMessage(campBaseDTO);
				if(mqInfoDTO != null) {
					// 发送MQ消息处理
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				}
				if (keepKbn) {
					Map<String, Object> keepMap = (Map<String, Object>) campBaseDTO.getExtArgs().get(DroolsConstants.KEEP_LEVELINFO);
					if (null != keepMap && !keepMap.isEmpty()) {
						// 会员修改属性处理
						binBEMQMES08_BL.updateMemberExtInfo(keepMap);
					}
				}
			}
			if (isChangeCard) {
				// 会员化妆次数规则处理
				CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE03);
				if (null != campRuleExec) {
					if (0 != campBaseDTO.getCurBtimes()) {
						// 取得化妆次数MQ消息体
						mqInfoDTO = binbedrcom01BL.getBtimesMQMessage(campBaseDTO);
						if(mqInfoDTO != null) {
							// 发送MQ消息处理
							binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
						}
					}
				}
				campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT01);
				if (null != campRuleExec) {
					// 取得积分MQ消息体(会员资料上传)
					mqInfoDTO = binbedrcom01BL.getPointMQMessageMB(campBaseDTO, map);
					if(mqInfoDTO != null) {
						// 发送MQ消息处理
						binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
					}
				}
			}
		}
	}
	
	/**
	 * 检查会员等级
	 * 
	 * @param campBaseDTO
	 *				会员实体
	 * @return boolean
	 * 				true: 发送等级MQ  false: 不发送           
	 * @throws Exception 
	 * 
	 */
	private boolean checkLevel(CampBaseDTO campBaseDTO) throws Exception {
		String memCode = campBaseDTO.getMemCode();
		// 芳香等级会员卡
		if (memCode.startsWith("8") 
				|| memCode.startsWith("9")) {
			// 取得会员等级列表
			List<Map<String, Object>> allLevelList = binOLCM31_BL.getAllLevelList(campBaseDTO);
			// 会员等级代号
			String levelCode = RuleFilterUtil.findLevelCode(campBaseDTO.getCurLevelId(), allLevelList);
			// 当前等级为沁香会员，需要矫正等级
			if ("WMLC001".equals(levelCode)) {
				// 芳香会员等级
				int levelId = RuleFilterUtil.findLevelId("WMLC002", allLevelList);
				// 需要保持的等级
				campBaseDTO.getExtArgs().put(DroolsConstants.KEEP_LEVELID, levelId);
				// 取得会员保级信息
				Map<String, Object> keepMap = binOLCM31_BL.getKeepMemLevelInfo(campBaseDTO);
				// 会员保级条件
				campBaseDTO.getExtArgs().put(DroolsConstants.KEEP_LEVELINFO, keepMap);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 检查是否需要执行换卡扣积分
	 * 
	 * @param oldCard
	 *				老卡
	 * @param newCard
	 *				新卡
	 * @return boolean
	 * 				true: 需要扣积分  false: 不需要
	 * 
	 */
	private boolean checkCardPoint(String oldCard, String newCard, boolean isMeb){
		if (!CherryChecker.isNullOrEmpty(oldCard, true) && !CherryChecker.isNullOrEmpty(newCard, true)) {
			if (isMeb) {
				// 非升级换卡
				if (!oldCard.equals(newCard) && 
						!oldCard.toUpperCase().startsWith("F") && !newCard.toUpperCase().startsWith("F")) {
					return true;
				}
			} else {
				return !(oldCard.trim().equalsIgnoreCase((newCard.trim())));
			}
		}
		return false;
	}
}

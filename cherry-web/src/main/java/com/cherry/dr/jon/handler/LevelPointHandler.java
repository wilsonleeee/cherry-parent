/*	
 * @(#)LevelPointHandler.java     1.0 2011/8/18		
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.BaseHandler_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleHandler_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;

/**
 * 会员等级和积分处理器
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public class LevelPointHandler implements RuleHandler_IF, BaseHandler_IF{
	@Resource
	private CampRuleExec_IF binbedrjon01BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon02BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon09BL;
	
	@Resource
	private CampRuleExec_IF binbedrpoi03BL;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;
	
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
	private RuleEngine_IF ruleEngineIF;
	
	private static Logger logger = LoggerFactory
			.getLogger(LevelPointHandler.class.getName());
	
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
		String memCode = (String) map.get("memberCode");
		if (CherryChecker.isNullOrEmpty(memCode, true)) {
			return;
		}
		// 组织代码
		String orgCode = (String) map.get("orgCode");
		// 品牌代码
		String brandCode = (String) map.get("brandCode");
		// 会员俱乐部模式
		String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
		if (!"3".equals(clubMod)) {
			if (CherryChecker.isNullOrEmpty(map.get("clubCode"))) {
				return;
			} else if (MessageConstants.MSG_TRADETYPE_SALE.equalsIgnoreCase(String.valueOf(map.get("tradeType")))){
				Map<String, Object> clubMap = new HashMap<String, Object>();
				clubMap.put("orgCode", orgCode);
				clubMap.put("brandCode", brandCode);
				clubMap.put("brandInfoId", map.get("brandInfoID"));
				clubMap.put("organizationInfoId", map.get("organizationInfoID"));
				clubMap.put("clubCode", map.get("clubCode"));
				clubMap.put("memberCode", map.get("memberCode"));
				binOLCM31_BL.updateClubInfoBySale(clubMap);
			}
		}
		
		// 规则计算最早时间点
		String ruleTime = binOLCM14_BL.getConfigValue("1287", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
		if (CherryChecker.checkDate(ruleTime)) {
			String saleTime = (String) map.get("saleTime");
			if (!CherryChecker.isNullOrEmpty(saleTime)) {
				saleTime = DateUtil.coverTime2YMD(saleTime, DateUtil.DATE_PATTERN);
				// 单据时间早于初始采集日期
				if (DateUtil.compareDate(saleTime, ruleTime) < 0) {
					return;
				}
			}
		}
		// 执行区分
		int execFlag = 0;
		// 积分规则处理
		CampRuleExec_IF campRuleExecPT01 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT01);
		if (null == campRuleExecPT01) {
			// 验证是否需要执行规则
			boolean isRuleExecFlg = binbedrcom01BL.isRuleExec(map);
			if (!isRuleExecFlg) {
				return;
			}
		} else {
			// 验证是否需要执行规则(包含积分处理器)
			execFlag = binbedrcom01BL.isRuleExecPT(map);
			if (-1 == execFlag) {
				return;
			}
		}
		try {
			// 验证柜台是否需要执行规则
			if (!binOLCM31_BL.isRuleCounter(map)) {
				return;
			}
			boolean syncFlag = false;
			MQInfoDTO mqInfoDTO = null;
			// 生成执行规则前的DTO
			CampBaseDTO campBaseDTO = binbedrcom01BL.getCampBaseDTO(map);
			if (campBaseDTO.getMemberClubId() > 0) {
				// 取得组规则库
				Map<String, Object> groupRule = ruleEngineIF.getGroupRule(campBaseDTO.getOrgCode(), 
						campBaseDTO.getBrandCode(),  "1_" + campBaseDTO.getMemberClubId());
				if (null == groupRule || groupRule.isEmpty()) {
					return;
				}
			}
			if (0 == execFlag) {
				// 处理规则文件
				executeRuleFile(campBaseDTO);
				// 需要下发等级MQ
				if (!DroolsConstants.NO_LEVEL_MQ.equals(
						campBaseDTO.getExtArgs().get(DroolsConstants.LEVEL_MQ_KBN))) {
					// 取得等级MQ消息体
					mqInfoDTO = binbedrcom01BL.getLevelMQMessage(campBaseDTO);
					if(mqInfoDTO != null) {
						syncFlag = true;
						// 发送MQ消息处理
						binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
						// 组织ID
						String orgIdStr = String.valueOf(campBaseDTO.getOrganizationInfoId());
						// 品牌ID
						String brandIdStr = String.valueOf(campBaseDTO.getBrandInfoId());
						// 升级
						if (DroolsConstants.UPKBN_1.equals(campBaseDTO.getChangeType()) && 
								0 != campBaseDTO.getPrevLevel()) {
							if (binOLCM14_BL.isConfigOpen("1131", orgIdStr, brandIdStr)) {
								Map<String, Object> gtMap = new HashMap<String, Object>();
								// 组织ID
								gtMap.put("organizationInfoID", orgIdStr);
								// 品牌ID
								gtMap.put("brandInfoID", brandIdStr);
								// 组织代码
								gtMap.put("orgCode", campBaseDTO.getOrgCode());
								// 品牌代码
								gtMap.put("brandCode", campBaseDTO.getBrandCode());
								// 事件ID
								gtMap.put("eventId", campBaseDTO.getMemberInfoId());
								// 事件类型:会员升级
								gtMap.put("eventType", "12");
								// 事件发生时间 
								gtMap.put("eventDate", campBaseDTO.getTicketDate());
								// 信息内容:关联单号
								gtMap.put("messageContents", "");
								// 事件来源
								gtMap.put("sourse", "LevelPointHandler");
								// 取得沟通短信消息体(实时)
								mqInfoDTO = binOLCM31_BL.getGTMQMessage(gtMap);
								// 发送MQ消息处理
								binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
							}
							
							//会员等级调整插入临时表
							binOLCM31_BL.addTempAdjustMember(campBaseDTO.getMemberInfoId(),campBaseDTO.getOrganizationInfoId(),campBaseDTO.getBrandInfoId());
							logger.info("@@@会员升级实时生成活动单据@@@" + campBaseDTO.getMemCode());
							com05IF.makeOrderMQ(campBaseDTO.getOrganizationInfoId()
									, campBaseDTO.getBrandInfoId()
									, orgCode, brandCode,null, campBaseDTO.getMemberInfoId(),"1143");
						}
					}
				}
				// 取得化妆次数MQ消息体
				mqInfoDTO = binbedrcom01BL.getBtimesMQMessage(campBaseDTO);
				if(mqInfoDTO != null) {
					// 发送MQ消息处理
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				}
				boolean amountFlag = campBaseDTO.getOldTotalAmount() != campBaseDTO.getCurTotalAmount();
				boolean btimesFlag = campBaseDTO.getOldBtimes() != campBaseDTO.getCurBtimes();
				// 累计金额或者化妆次数发生变化
				if (amountFlag || btimesFlag) {
					Map<String, Object> updateMap = new HashMap<String, Object>();
					// 会员信息ID
					updateMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
					if (amountFlag) {
						updateMap.put("totalAmounts", campBaseDTO.getCurTotalAmount());
					}
					// 升级所需金额
					if ("1".equals(campBaseDTO.getExtArgs().get("CALCUPATKBN"))) {
						updateMap.put("upLevelAmount", campBaseDTO.getUpLevelAmount());
					}
					if (0 == campBaseDTO.getMemberClubId()) {
						if (btimesFlag) {
							updateMap.put("curBtimes", campBaseDTO.getCurBtimes());
						}
						// 更新会员信息扩展表
						binOLCM31_BL.updateMemberExtInfo(updateMap);
					} else {
						// 会员俱乐部ID
						updateMap.put("memberClubId", campBaseDTO.getMemberClubId());
						// 更新会员俱乐部等级扩展信息
						binOLCM31_BL.updateMemberClubExtInfo(updateMap);
					}
				}
//				if (0 != campBaseDTO.getMemberClubId() && 1 != campBaseDTO.getMemRegFlg()) {
//					// 取得会员俱乐部当前等级信息
//					Map<String, Object> clubLevelMap = binOLCM31_BL.getClubCurLevelInfo(map);
//					if (null != clubLevelMap && !clubLevelMap.isEmpty() &&
//							0 == Integer.parseInt(clubLevelMap.get("organizationId").toString())) {
//						Map<String, Object> mzMap = new HashMap<String, Object>();
//						mzMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
//						mzMap.put("memberClubId", campBaseDTO.getMemberClubId());
//						mzMap.put("mzClubId", campBaseDTO.getMemberClubId());
//						mzMap.put("employeeID", campBaseDTO.getEmployeeId());
//						mzMap.put("BAcode", campBaseDTO.getEmployeeCode());
//						mzMap.put("organizationID", campBaseDTO.getOrganizationId());
//						mzMap.put("counterCode", campBaseDTO.getCounterCode());
//						mzMap.put("joinTime", campBaseDTO.getTicketDate());
//						// 更新会员俱乐部扩展属性
//						binOLCM31_BL.updateClubExtInfo(mzMap);
//						mzMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
//						mzMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
//						// 组织代码
//						mzMap.put("orgCode", campBaseDTO.getOrgCode());
//						// 品牌代码
//						mzMap.put("brandCode", campBaseDTO.getBrandCode());
//						// 发送会员扩展信息MQ消息(全部记录)
//						binOLCM31_BL.sendAllMZMQMsg(mzMap);
//					}
//				}
			} else if (1 == execFlag) {
				// 是否进行历史积分调整处理判断
				binbedrpoi03BL.beforExec(campBaseDTO);
				// 需要进行历史积分调整处理
				if ("1".equals(campBaseDTO.getExtArgs().get("POI03_EXEC_KBN"))) {
					campBaseDTO.getExtArgs().remove("POI03_EXEC_KBN");
					// 关联退货(延时上传)
					if (DroolsConstants.SALESRTYPE_2.equals(map.get("saleSRtype"))) {
						// 关联单号
						String relevantNo = (String) map.get("relevantNo");
						if (CherryChecker.isNullOrEmpty(relevantNo) || relevantNo.indexOf("SR") == 0) {
							// 关联退货单的关联单号不正确
							String errMsg = DroolsMessageUtil.getMessage(
									DroolsMessageUtil.EDR00013, null);
							throw new CherryMQException(errMsg);
						}
						// 取得关联单据信息
						Map<String, Object> relevantSaleMap = binbedrcom01BL.getRelevantSaleInfo(map);
						if (null != relevantSaleMap && !relevantSaleMap.isEmpty()) {
							// 原单单据类型
							campBaseDTO.setTradeType((String) relevantSaleMap.get("saleType"));
							// 原单销售时间
							campBaseDTO.setTicketDate((String) relevantSaleMap.get("ticketDate"));
							campBaseDTO.setBillId(relevantNo);
						}
					}
					campBaseDTO.setReCalcFlg(DroolsConstants.RECALCFLG_1);
					// 设置条件：等级信息，购买产品等
					binbedrcom01BL.conditionSetting(campBaseDTO);
					campBaseDTO.setReCalcFlg(DroolsConstants.RECALCFLG_0);
					// 执行积分计算前的设置
					befPointExec(campBaseDTO);
					// 单据修改次数
					int billModifyCounts = 0;
					Object modifyCountsObj = map.get("billModifyCounts");
					if (null != modifyCountsObj) {
						billModifyCounts = Integer.parseInt(modifyCountsObj.toString().trim());
					}
					campBaseDTO.getExtArgs().put("BILL_MODIFYCOUNTS", billModifyCounts);
					// 历史积分调整处理
					binbedrpoi03BL.ruleExec(campBaseDTO);
				}
			}
			if (null != campRuleExecPT01) {
				// 积分信息
				PointDTO pointDTO = campBaseDTO.getPointInfo();
				if (null != pointDTO) {
					// 单据的积分情况
					PointChangeDTO pointChange = pointDTO.getPointChange();
					if (null != pointChange) {
						if (0 == execFlag) {
							if ("1".equals(pointChange.getMatchKbn())) {
								// 取得积分MQ消息体
								mqInfoDTO = binOLCM31_BL.getPointMQMessage(campBaseDTO);
							}
						} else {
							// 取得积分MQ消息体(历史积分)
							mqInfoDTO = binOLCM31_BL.getPointMQMessageHist(campBaseDTO);
						}
						if(mqInfoDTO != null) {
							// 发送MQ消息处理
							binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
						}
						if (0 == execFlag) {
							if ("1".equals(pointChange.getMatchKbn())) {
								syncFlag = true;
								Map<String, Object> pointRuleCalInfo = new HashMap<String, Object>();
								// 组织代码
								pointRuleCalInfo.put("OrgCode", campBaseDTO.getOrgCode());
								// 品牌代码
								pointRuleCalInfo.put("BrandCode", campBaseDTO.getBrandCode());
								// 业务类型
								pointRuleCalInfo.put("TradeType", "RU");
								// 会员积分变化主ID
								pointRuleCalInfo.put("pointChangeId", pointChange.getPointChangeId());
								// 计算日期
								pointRuleCalInfo.put("changeDate", pointChange.getChangeDate());
								// 引起积分计算的单据号
								pointRuleCalInfo.put("billCode", pointChange.getTradeNoIF());
								// 引起积分计算的业务类型
								pointRuleCalInfo.put("billType", pointChange.getTradeType());
								// 购买金额
								pointRuleCalInfo.put("amount", pointChange.getAmount());
								// 购买数量
								pointRuleCalInfo.put("quantity", pointChange.getQuantity());
								// 获得积分
								pointRuleCalInfo.put("point", pointChange.getPoint());
								// 会员ID
								pointRuleCalInfo.put("memberInfoId", pointChange.getMemberInfoId());
								// 会员名称
								pointRuleCalInfo.put("name", campBaseDTO.getMemName());
								// 部门ID
								pointRuleCalInfo.put("organizationId", campBaseDTO.getOrganizationId());
								// 部门名称
								pointRuleCalInfo.put("departName", campBaseDTO.getDepartName());
								// 所属柜台ID
								pointRuleCalInfo.put("MemOrganizationID", campBaseDTO.getBelDepartId());
								// 会员积分变化明细List
								List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
								if (null != changeDetailList) {
									List<String> subRuleIds = new ArrayList<String>();
									for (PointChangeDetailDTO changeDetail : changeDetailList) {
										// 子活动ID
										Integer subId = changeDetail.getSubCampaignId();
										if (null == subId) {
											continue;
										}
										String ruleId = String.valueOf(subId);
										// 主规则ID
										Integer mainRuleId = changeDetail.getMainRuleId();
										if (null != mainRuleId) {
											ruleId = ruleId + "_" + String.valueOf(mainRuleId);
										}
										boolean flag = true;
										// 判断ID是否已经存在
										for (String subRuleId : subRuleIds) {
											if (subRuleId.equals(ruleId)) {
												flag = false;
											}
										}
										if (flag) {
											subRuleIds.add(ruleId);
										}
									}
									if (!subRuleIds.isEmpty()) {
										pointRuleCalInfo.put("subCampaignId", subRuleIds);
									}
								}
								map.put("pointRuleCalInfo", pointRuleCalInfo);
								// 组织ID
								String orgIdStr = String.valueOf(campBaseDTO.getOrganizationInfoId());
								// 品牌ID
								String brandIdStr = String.valueOf(campBaseDTO.getBrandInfoId());
								// 积分变化需要发送沟通MQ
								if(1 != campBaseDTO.getMemRegFlg() && pointChange.getPoint() != 0) {
									if (binOLCM14_BL.isConfigOpen("1088", orgIdStr, brandIdStr)) {
										// 发送积分变化沟通MQ的最低阀值
										String minPointStr = binOLCM14_BL.getConfigValue("1089", orgIdStr, brandIdStr);
										boolean gtFlag = true;
										if (!CherryChecker.isNullOrEmpty(minPointStr, true)) {
											minPointStr = minPointStr.trim();
											try {
												double minPoint = Double.parseDouble(minPointStr);
												// 当前积分小于最低阈值不发送沟通MQ
												if (pointDTO.getCurTotalPoint() < minPoint) {
													gtFlag = false;
												}
											} catch (Exception e) {
												gtFlag = false;
											}
										}
										if (gtFlag) {
											Map<String, Object> gtMap = new HashMap<String, Object>();
											// 组织ID
											gtMap.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
											// 品牌ID
											gtMap.put("brandInfoID", campBaseDTO.getBrandInfoId());
											// 组织代码
											gtMap.put("orgCode", campBaseDTO.getOrgCode());
											// 品牌代码
											gtMap.put("brandCode", campBaseDTO.getBrandCode());
											// 事件ID
											gtMap.put("eventId", campBaseDTO.getMemberInfoId());
											// 事件类型:积分变化
											gtMap.put("eventType", "7");
											// 事件发生时间 
											gtMap.put("eventDate", pointChange.getChangeDate());
											// 信息内容:关联单号
											gtMap.put("messageContents", pointChange.getTradeNoIF());
											// 事件来源
											gtMap.put("sourse", "LevelPointHandler");
											// 取得沟通短信消息体(实时)
											mqInfoDTO = binOLCM31_BL.getGTMQMessage(gtMap);
											// 发送MQ消息处理
											binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
										}
									}
									// 微信事件处理器
									if (null != binBEMQMES98_BL.getMessageHandler(orgCode, brandCode, CherryConstants.MESSAGE_TYPE_WP)) {
										Map<String, Object> wxMap = new HashMap<String, Object>();
										// 组织ID
										wxMap.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
										// 品牌ID
										wxMap.put("brandInfoID", campBaseDTO.getBrandInfoId());
										// 组织代码
										wxMap.put("orgCode", campBaseDTO.getOrgCode());
										// 品牌代码
										wxMap.put("brandCode", campBaseDTO.getBrandCode());
										// 时间
										wxMap.put("changeTime", pointChange.getChangeDate());
										// 积分类型
										wxMap.put("pointType", pointChange.getPoint() >= 0? "1" : "2");
										// 变动积分
										wxMap.put("newPoint", pointChange.getPoint());
										// 总可用积分
										wxMap.put("totalPoint", pointDTO.getCurTotalPoint());
										// 柜台名称
										wxMap.put("counterName", campBaseDTO.getDepartName());
										// 会员卡号
										wxMap.put("memberCode", campBaseDTO.getMemCode());
										// 会员姓名
										wxMap.put("memName", campBaseDTO.getMemName());
										// 交易类型
										wxMap.put("saleType", pointChange.getTradeType());
										// 交易金额
										wxMap.put("saleAmount", pointChange.getAmount());
										// 取得微信短信消息体(实时)
										mqInfoDTO = binOLCM31_BL.getWXMQMessage(wxMap);
										// 发送MQ消息处理
										binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
									}
								}
							}
						} else {
							Map<String, Object> reCalcMap = new HashMap<String, Object>();
							// 组织代码
							reCalcMap.put("orgCode", orgCode);
							// 品牌代码
							reCalcMap.put("brandCode", brandCode);
							// 组织ID
							reCalcMap.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
							// 品牌ID
							reCalcMap.put("brandInfoID", campBaseDTO.getBrandInfoId());
							// 会员信息ID
							reCalcMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
							// 会员卡号
							reCalcMap.put("memberCode", campBaseDTO.getMemCode());
							// 重算区分
							reCalcMap.put("reCalcType", DroolsConstants.RECALCTYPE0);
							// 重算时间
							reCalcMap.put("reCalcDate", campBaseDTO.getTicketDate());
							if (campBaseDTO.getMemberClubId() > 0) {
								reCalcMap.put("memberClubId", campBaseDTO.getMemberClubId());
							}
							// 插入重算信息表
							binbedrcom01BL.insertReCalcInfo(reCalcMap);
							// 发送MQ重算消息进行实时重算
							binbedrcom01BL.sendReCalcMsg(reCalcMap);
						}
					}
				}
			}
			// 是否需要同步天猫会员
			if (syncFlag && campBaseDTO.getMemberClubId() == 0
					&& binOLCM31_BL.needSync(campBaseDTO.getMemberInfoId(), brandCode)) {
				Map<String, Object> tmSyncInfo = new HashMap<String, Object>();
				tmSyncInfo.put("memberInfoId", campBaseDTO.getMemberInfoId());
				tmSyncInfo.put("brandCode", brandCode);
				map.put("TmSyncInfo", tmSyncInfo);
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
		// 组织代码
		String orgCode = campBaseDTO.getOrgCode();
		// 品牌代码
		String brandCode = campBaseDTO.getBrandCode();
		if (!campBaseDTO.getExtArgs().containsKey("BDKBN") 
				&& "ysl".equalsIgnoreCase(brandCode.trim())) {
			campBaseDTO.getExtArgs().put("BDKBN", "1");
		}
		if (!campBaseDTO.getExtArgs().containsKey("TJKBN")) {
			// 等级计算时累计金额统计方式
			String tjkbn = binOLCM14_BL.getConfigValue("1137", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()));
			campBaseDTO.getExtArgs().put("TJKBN", tjkbn);
		}
		// 建档处理
		CampRuleExec_IF campRuleExec08 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE08);
		boolean lelExec = true;
		if (null != campRuleExec08) {
			campBaseDTO.getExtArgs().put("MJD", "1");
			// 判断是否需要处理等级
			campRuleExec08.beforExec(campBaseDTO);
			// 不需要处理等级的会员
			if ("0".equals(campBaseDTO.getExtArgs().get("LELEXECFLAG"))) {
				lelExec = false;
			}
			campBaseDTO.getExtArgs().remove("LELEXECFLAG");
		}
		// 等级初始日期
		String zdate = null;
		if (!campBaseDTO.getExtArgs().containsKey("ZDL")) {
			// 品牌特殊参数
			String zparams = binOLCM14_BL.getConfigValue("1361", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()));
			if (!CherryChecker.isNullOrEmpty(zparams, true)) {
				Map<String, Object> params = CherryUtil.json2Map(zparams.trim());
				zdate = (String) params.get("zdate");
			}
			campBaseDTO.getExtArgs().put("ZDL", zdate);
		} else {
			zdate = (String) campBaseDTO.getExtArgs().get("ZDL");
		}
		// 比较单据时间和等级初始日期
		if (null != zdate 
				&& DateUtil.compareDate(
						DateUtil.coverTime2YMD(campBaseDTO.getTicketDate(), DateUtil.DATE_PATTERN), zdate) < 0) {
			// 早于初始日期的单据无需计算等级
			lelExec = false;
		}
		// 规则日志
		String ruleLog = (String) campBaseDTO.getExtArgs().get("RULELOG");
		String orgIdStr = String.valueOf(campBaseDTO.getOrganizationInfoId());
		String brandIdStr = String.valueOf(campBaseDTO.getBrandInfoId());
		if (null == ruleLog || "".equals(ruleLog)) {
			// 规则日志
			ruleLog = binOLCM14_BL.getConfigValue("1065", orgIdStr, brandIdStr);
			campBaseDTO.getExtArgs().put("RULELOG", ruleLog);
		}
		// 是否打印日志
		boolean isLog = RuleFilterUtil.isRuleLog(campBaseDTO);
		// 积分规则处理
		CampRuleExec_IF campRuleExecPT1 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT01);
		if (null != campRuleExecPT1) {
			if (!campBaseDTO.getExtArgs().containsKey("NOECT")) {
				String counterBelong = campBaseDTO.getBelCounterCode();
				boolean flg = true;
				if (!CherryChecker.isNullOrEmpty(counterBelong)) {
					String noExecCounters = TmallKeys.getNoExecCounters(brandCode);
					if (!CherryChecker.isNullOrEmpty(noExecCounters) 
							&& ConvertUtil.isContain(noExecCounters.split(","), counterBelong))  {
						campBaseDTO.getExtArgs().put("NOECT", "0");
						campRuleExecPT1 = null;
						flg = false;
					}
				}
				if (flg) {
					campBaseDTO.getExtArgs().put("NOECT", "1");
				}
			} else if ("0".equals(campBaseDTO.getExtArgs().get("NOECT"))){
				campRuleExecPT1 = null;
			}
		}
		// 等级计算是否包含初始累计金额
		if (!campBaseDTO.getExtArgs().containsKey("INAMT")) {
			campBaseDTO.getExtArgs().put("INAMT", binOLCM14_BL.getConfigValue("1305", orgIdStr, brandIdStr));
		}
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(campBaseDTO.getTradeType())) {
			// 取得处理器
			CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE06);
			if (null != campRuleExec) {
				// 退货规则处理
				campRuleExec.ruleExec(campBaseDTO);
			}
			// 插入规则执行履历表:（累计金额）
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_1, true);
			if (lelExec) {
				// 非关联退货处理区分
				String srExecKbn = binOLCM14_BL.getConfigValue("1073", String.valueOf(campBaseDTO.getOrganizationInfoId()), 
						String.valueOf(campBaseDTO.getBrandInfoId()));
				// 需要进行降级判断处理
				if (!"1".equals(srExecKbn) && DroolsConstants.NOT_MEMBER != campBaseDTO.getCurLevelId()) {
					binbedrjon09BL.ruleExec(campBaseDTO);
				}
			}
		} else {
			// 开始时间
			long bfstartTime = 0;
			// 结束时间
			long bfendTime = 0;
			if (isLog) {
				bfstartTime = System.currentTimeMillis();
			}
			if (null == campRuleExecPT1) {
				campBaseDTO.getExtArgs().put("NO_BUYINFO", "0");
			}
			// 设置条件：等级信息，购买产品等
			binbedrcom01BL.conditionSetting(campBaseDTO);
			// 插入规则执行履历表:（累计金额）
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_1, true);
			if (isLog) {
				bfendTime = System.currentTimeMillis();
				// 运行时间
				double subTime = bfendTime - bfstartTime;
				// 运行时间日志内容
				String msg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.IDR00006, new String[] {String.valueOf(subTime)});
				logger.info(msg);
			}
			if (lelExec) {
				// 会员等级计算方式
				String levelCalcKbn = binOLCM14_BL.getConfigValue("1101", String.valueOf(campBaseDTO.getOrganizationInfoId()), 
						String.valueOf(campBaseDTO.getBrandInfoId()));
				if (!"2".equals(levelCalcKbn)) {
					// 会员化妆次数规则处理
					CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE03);
					if (campBaseDTO.getAmount() > 0) {
						// 开始时间
						long rstartTime = 0;
						// 结束时间
						long rendTime = 0;
						if (isLog) {
							rstartTime = System.currentTimeMillis();
						}
						String calcKbn = (String) campBaseDTO.getExtArgs().get("CALCUPATKBN");
						if (null == calcKbn) {
							calcKbn = "0";
							// 计算升级所需金额处理
							CampRuleExec_IF campRuleExec01 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE01);
							if (null != campRuleExec01) {
								calcKbn = "1";
							}
							campBaseDTO.getExtArgs().put("CALCUPATKBN", calcKbn);
						}
						// 当前会员等级
						int curLevelId = campBaseDTO.getCurLevelId();
						// 非会员
						if (DroolsConstants.NOT_MEMBER == curLevelId) {
							// 会员入会规则处理
							binbedrjon01BL.ruleExec(campBaseDTO);
						} else {
							campBaseDTO.getExtArgs().remove("BTIMESKBN");
							if (null != campRuleExec) {
								campBaseDTO.getExtArgs().put("BTIMESKBN", "1");
							}
							// 会员升降级规则处理
							binbedrjon02BL.ruleExec(campBaseDTO);
						}
						if ("1".equals(calcKbn)) {
							double upLevelAmount = 0;
							// 入会或者升级
							if (DroolsConstants.UPKBN_1.equals(campBaseDTO.getChangeType())) {
								// 当前等级非最高级别
								if (!RuleFilterUtil.isHighestLevel(campBaseDTO.getCurLevelId(), campBaseDTO.getMemberLevels())) {
									// 再次匹配升级规则，计算下次升级所需金额
									CampBaseDTO newCampBaseDTO = new CampBaseDTO();
									ConvertUtil.convertNewDTO(newCampBaseDTO, campBaseDTO, false);
									newCampBaseDTO.setAmount(0);
									// 拷贝扩展信息
									Map<String, Object> newExtArgs = new HashMap<String, Object>();
									newExtArgs.putAll(newCampBaseDTO.getExtArgs());
									newExtArgs.remove("UPLELAMOUNT");
									newExtArgs.put("NO_AFTEXEC", "1");
									newCampBaseDTO.setExtArgs(newExtArgs);
									// 会员升降级规则处理
									binbedrjon02BL.ruleExec(newCampBaseDTO);
									// 提取本次计算的升级所需金额
									Object upAmountObj = newExtArgs.get("UPLELAMOUNT");
									if (null != upAmountObj) {
										upLevelAmount = Double.parseDouble(upAmountObj.toString());
									}
								}
							} else {
								Object upAmountObj = campBaseDTO.getExtArgs().get("UPLELAMOUNT");
								if (null != upAmountObj) {
									upLevelAmount = Double.parseDouble(upAmountObj.toString());
								}
							}
							campBaseDTO.setUpLevelAmount(upLevelAmount);
							campBaseDTO.getExtArgs().remove("UPLELAMOUNT");
						}
						if (isLog) {
							rendTime = System.currentTimeMillis();
							// 运行时间
							double subTime = rendTime - rstartTime;
							// 运行时间日志内容
							String msg = DroolsMessageUtil.getMessage(
									DroolsMessageUtil.IDR00007, new String[] {String.valueOf(subTime)});
							logger.info(msg);
						}
					}
					if (null != campRuleExec) {
						campRuleExec.ruleExec(campBaseDTO);
					}
				}
			}
		}
		// 入会或者升级
		if (DroolsConstants.UPKBN_1.equals(campBaseDTO.getChangeType())) {
			if (null != campRuleExec08 && lelExec) {
				campRuleExec08.ruleExec(campBaseDTO);
			}
		}
		if (null != campRuleExecPT1) {
			String initialTime = (String) campBaseDTO.getExtArgs().get("INITIALTIME");
			// 重算的时候
			if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1 
					&& null != initialTime) {
				Calendar cal1 = Calendar.getInstance();
				String ticketDate = campBaseDTO.getTicketDate();
				if (CherryChecker.checkDate(ticketDate)) {
					ticketDate = ticketDate + " 00:00:00";
				} else {
					int index = ticketDate.indexOf(".");
					if (index > 0) {
						ticketDate = ticketDate.substring(0, index);
					}
				}
				cal1.setTime(DateUtil.coverString2Date(ticketDate, DateUtil.DATETIME_PATTERN));
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(DateUtil.coverString2Date(initialTime, DateUtil.DATETIME_PATTERN));
				// 比较单据时间是否在初始导入时间之前
				if (cal1.before(cal2)) {
					return;
				}
			}
			// 退货
			if (DroolsConstants.TRADETYPE_SR.equals(campBaseDTO.getTradeType())) {
				// 设置条件：等级信息，购买产品等
				binbedrcom01BL.conditionSetting(campBaseDTO);
			}
			// 执行积分计算前的设置
			befPointExec(campBaseDTO);
			boolean isExecRef = (0 != campBaseDTO.getReferrerId() && campBaseDTO.getReferrerId() != campBaseDTO.getMemberInfoId()
					&& !DroolsConstants.TRADETYPE_SR.equals(campBaseDTO.getTradeType()));
			if (isExecRef && null != binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05) &&
					!campBaseDTO.getExtArgs().containsKey("RPNUM")) {
				// 取得会员积分明细总数
				campBaseDTO.getExtArgs().put("RPNUM", binOLCM31_BL.getTotalPtlNum(campBaseDTO.getReferrerId()));
			}
			// 开始时间
			long startTime = 0;
			// 结束时间
			long endTime = 0;
			if (isLog) {
				startTime = System.currentTimeMillis();
			}
			campRuleExecPT1.ruleExec(campBaseDTO);
			// 非退货
			if (isExecRef) {
				// 推荐会员积分奖励
				campRuleExecPT1 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05);
				if (null != campRuleExecPT1) {
					campRuleExecPT1.ruleExec(campBaseDTO);
				}
			}
			if (isLog) {
				endTime = System.currentTimeMillis();
				// 运行时间
				double subTime = endTime - startTime;
				// 运行时间日志内容
				String msg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.IDR00002, new String[] {String.valueOf(subTime)});
				logger.info(msg);
			}
		}
	}
	
	/**
	 * 执行积分计算前的设置
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * 
	 * @throws Exception
	 */
	private void befPointExec(CampBaseDTO campBaseDTO) throws Exception {
		// 组织ID
		String organizationInfoId = String.valueOf(campBaseDTO.getOrganizationInfoId());
		// 品牌ID
		String brandInfoId = String.valueOf(campBaseDTO.getBrandInfoId());
		if (!campBaseDTO.getExtArgs().containsKey("DHCPKBN")) {
			// 积分兑礼设置
			String dhcpKbn = binOLCM14_BL.getConfigValue("1052", organizationInfoId, brandInfoId);
			campBaseDTO.getExtArgs().put("DHCPKBN", dhcpKbn);
		}
		if (!campBaseDTO.getExtArgs().containsKey("ROUNDKBN")) {
			// 小数点处理
			String roundKbn = binOLCM14_BL.getConfigValue("1051", organizationInfoId, brandInfoId);
			campBaseDTO.getExtArgs().put("ROUNDKBN", roundKbn);
		}
		if (!campBaseDTO.getExtArgs().containsKey("TZZKPTKBN")) {
			// 单据明细含负金额时是否将这部分抵扣至产品上
			String tzzkKbn = binOLCM14_BL.getConfigValue("1079", organizationInfoId, brandInfoId);
			campBaseDTO.getExtArgs().put("TZZKPTKBN", tzzkKbn);
		}
		// 非重算
		if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_0 &&
				!"SR".equalsIgnoreCase(campBaseDTO.getTradeType()) 
				&& campBaseDTO.getAmount() > 0) {
			// 生日修改时积分是否重算
			String birthKbn = binOLCM14_BL.getConfigValue("1107", organizationInfoId, brandInfoId);
			// 不重算
			if ("2".equals(birthKbn)) {
				// 重设生日
				binOLCM31_BL.resetBirth(campBaseDTO);
			}
		}
		// 非关联退货是否匹配特定商品积分规则
		if ("SR".equalsIgnoreCase(campBaseDTO.getTradeType()) && 
				!campBaseDTO.getExtArgs().containsKey("SRPTKBN")) {
			// 积分兑礼设置
			String srptkbn = binOLCM14_BL.getConfigValue("1142", organizationInfoId, brandInfoId);
			campBaseDTO.getExtArgs().put("SRPTKBN", srptkbn);
		}
		// 规则日志
		String ruleLog = (String) campBaseDTO.getExtArgs().get("RULELOG");
		if (null == ruleLog || "".equals(ruleLog)) {
			// 规则日志
			ruleLog = binOLCM14_BL.getConfigValue("1065", organizationInfoId, brandInfoId);
			campBaseDTO.getExtArgs().put("RULELOG", ruleLog);
		}
		if ("1".equals(ruleLog)) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 品牌ID
			searchMap.put("brandInfoId", brandInfoId);
			// 组织ID
			searchMap.put("organizationInfoId", organizationInfoId);
			// 规则类型
			searchMap.put("campaignType", "3");
			if (0 != campBaseDTO.getMemberClubId()) {
				// 会员俱乐部ID
				searchMap.put("memberClubId", String.valueOf(campBaseDTO.getMemberClubId()));
			}
			List<Map<String, Object>> ruleList = binOLCM31_BL.getCampRuleNameList(searchMap);
			campBaseDTO.getExtArgs().put("RuleNameList", ruleList);
		}
	}
}

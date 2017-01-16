/*	
 * @(#)BINBEDRPOI05_BL.java     1.0 2013/07/12
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM01_Service;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.dr.point.service.BINBEDRPOI01_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 推荐会员积分奖励处理 BL
 * 
 * @author hub
 * @version 1.0 2013.07.12
 */
public class BINBEDRPOI05_BL implements CampRuleExec_IF{
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private BINBEDRCOM01_Service binbedrcom01_Service;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private BINBEDRPOI01_Service binbedrpoi01_Service;
	
	/**
	 * 推荐会员积分奖励处理执行
	 * 
	 * @param campBaseDTO
	 *            会员实体
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		// 积分信息
		PointDTO pointDTO = campBaseDTO.getPointInfo();
		if (null != pointDTO) {
			// 单据的积分情况
			PointChangeDTO pointChange = pointDTO.getPointChange();
			boolean isClub = campBaseDTO.getMemberClubId() > 0? true: false;
			if (null != pointChange) {
				List<Map<String, Object>> rewRuleList = null;
				if (!campBaseDTO.getExtArgs().containsKey("REWRUFLG")) {
					Map<String, Object> seMap = new HashMap<String, Object>();
					// 品牌ID
					seMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
					// 组织信息ID
					seMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
					if (isClub) {
						seMap.put("memberClubId", campBaseDTO.getMemberClubId());
					}
					// 规则类型:推荐积分奖励
					seMap.put("campaignType", "8000");
					rewRuleList = binOLCM31_BL.getRuleCondList(seMap);
					if (null != rewRuleList && !rewRuleList.isEmpty()) {
						campBaseDTO.getExtArgs().put("REWRUFLG", "1");
						campBaseDTO.getExtArgs().put("REWRULIST", rewRuleList);
					} else {
						if (isClub) {
							return;
						}
						campBaseDTO.getExtArgs().put("REWRUFLG", "0");
					}
				}
				// 默认标识
				boolean defFlag = ("0".equals(campBaseDTO.getExtArgs().get("REWRUFLG"))) ? true : false;
				if (!defFlag && null == rewRuleList) {
					rewRuleList = (List<Map<String, Object>>) campBaseDTO.getExtArgs().get("REWRULIST");
				}
				// 积分奖励集合
				Map<String, Object> prMap = (Map<String, Object>) campBaseDTO.getProcDates().get("PR");
				Map<String, Object> checkMap = new HashMap<String, Object>();
				// 会员信息ID
				checkMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
				// 品牌ID
				checkMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
				// 组织信息ID
				checkMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
				if (isClub) {
					checkMap.put("memberClubId", campBaseDTO.getMemberClubId());
				}
				// 开始日期
				checkMap.put("billDate", campBaseDTO.getTicketDate());
				// 单据号
				String billId = campBaseDTO.getBillId();
				checkMap.put("billId", billId);
				checkMap.put("pointChangeId", pointChange.getPointChangeId());
				if (defFlag) {
					// 验证某个单号是否是首单
					if (!binOLCM31_BL.checkFirstBill(checkMap) || pointChange.getAmount() <= 0) {
						if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1
								&& prMap.containsKey(billId)) {
							// 撤销原先的奖励
							Map<String, Object> prBillInfo = (Map<String, Object>) prMap.get(billId);
							// 清除积分变化关联的奖励ID
							binbedrpoi01_Service.upReleUsedNo(checkMap);
							// 更新区分：撤销
							prBillInfo.put("UPFLAG", "0");
						}
						return;
					}
				} else {
					// 验证某个单号是否是首单
					if (!checkRewList(campBaseDTO, rewRuleList) || !binOLCM31_BL.checkFirstBill(checkMap) || pointChange.getAmount() <= 0) {
						if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1
								&& prMap.containsKey(billId)) {
							// 撤销原先的奖励
							Map<String, Object> prBillInfo = (Map<String, Object>) prMap.get(billId);
							// 清除积分变化关联的奖励ID
							binbedrpoi01_Service.upReleUsedNo(checkMap);
							// 更新区分：撤销
							prBillInfo.put("UPFLAG", "0");
						}
						return;
					}
				}
				//业务时间
				String ticketDateStr = campBaseDTO.getTicketDate();
				// 推荐会员信息ID
				int referrerId = campBaseDTO.getReferrerId();
				// 取得会员初始积分信息
				Map<String, Object> MemInitialInfo = binOLCM31_BL.getMemPointInitInfo(referrerId);
				if (null != MemInitialInfo && !MemInitialInfo.isEmpty()) {
					// 初始导入时间
					String initialTime = (String) MemInitialInfo.get("initialTime");
					if (!CherryChecker.isNullOrEmpty(initialTime)) {
						Calendar cal1 = Calendar.getInstance();
						if (CherryChecker.checkDate(ticketDateStr)) {
							ticketDateStr = ticketDateStr + " 00:00:00";
						} else {
							int index = ticketDateStr.indexOf(".");
							if (index > 0) {
								ticketDateStr = ticketDateStr.substring(0, index);
							}
						}
						cal1.setTime(DateUtil.coverString2Date(ticketDateStr, DateUtil.DATETIME_PATTERN));
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(DateUtil.coverString2Date(initialTime, DateUtil.DATETIME_PATTERN));
						// 比较单据时间是否在初始导入时间之前
						if (cal1.before(cal2)) {
							return;
						}
					}
				}
				double point = 0;
				if (defFlag) {
					point = RuleFilterUtil.roundSet(pointChange.getAmount(), "1");
				} else {
					if (null != campBaseDTO.getExtArgs().get("ZPT")) {
						point = Double.parseDouble(campBaseDTO.getExtArgs().get("ZPT").toString());
					} else {
						point = 200;
					}
				}
				
				if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_0) {
					//积分维护明细数据
					List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
					Map<String,Object> detailMap = new HashMap<String,Object>();
					//会员卡号(推荐会员)
					detailMap.put("MemberCode", campBaseDTO.getReferrerCode());
					if (isClub) {
						// 会员俱乐部ID
						detailMap.put("MemberClubId", campBaseDTO.getMemberClubId());
					}
					//当前会员id
					detailMap.put("MemberIdCurrent", campBaseDTO.getMemberInfoId());

					//修改的积分
					detailMap.put("ModifyPoint", point);
					detailMap.put("BusinessTime", ticketDateStr);
					//备注
					String reason = DroolsMessageUtil.getMessage(
							DroolsMessageUtil.IDR00022, new String[] {campBaseDTO.getMemName(),campBaseDTO.getMemCode(),billId});
					if (null != campBaseDTO.getExtArgs().get("XISU")) {
						reason += "积分系数: " + campBaseDTO.getExtArgs().get("XISU");
					}
					detailMap.put("Reason", reason);
					//员工Code
					detailMap.put("EmployeeCode", ConvertUtil.getString(campBaseDTO.getEmployeeCode()));
					detailDataList.add(detailMap);
					//设定MQ消息DTO
					MQInfoDTO mqInfoDTO = new MQInfoDTO();
					// 品牌代码
					String brandCode = campBaseDTO.getBrandCode();
					mqInfoDTO.setBrandCode(brandCode);
					// 组织代码
					String orgCode = campBaseDTO.getOrgCode();
					mqInfoDTO.setOrgCode(orgCode);
					// 组织ID
					mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
					// 品牌ID
					mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
					//单据类型
					String billType = CherryConstants.MESSAGE_TYPE_PT;;
					//单据号
					String billCode = binOLCM03_BL.getTicketNumber(campBaseDTO.getOrganizationInfoId(), 
							campBaseDTO.getBrandInfoId(), "", billType);
					checkMap.put("relevantUsedNo", billCode);
					// 更新积分变化关联的奖励ID
					binbedrpoi01_Service.upReleUsedNo(checkMap);
					// 业务类型
					mqInfoDTO.setBillType(billType);
					// 单据号
					mqInfoDTO.setBillCode(billCode);
					// 消息发送队列名
					mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
					// 设定消息内容
					Map<String,Object> msgDataMap = new HashMap<String,Object>();
					// 设定消息版本号
					msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
					// 设定消息命令类型
					msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
					// 设定消息数据类型
					msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
					// 设定消息的数据行
					Map<String,Object> dataLine = new HashMap<String,Object>();
					// 消息的主数据行
					Map<String,Object> mainData = new HashMap<String,Object>();
					// 品牌代码
					mainData.put("BrandCode", brandCode);
					// 业务类型
					mainData.put("TradeType", billType);
					// 单据号
					mainData.put("TradeNoIF", billCode);
					// 数据来源
					mainData.put("Sourse", "Cherry");
					//修改模式
					mainData.put("SubTradeType","2");
					// 积分类型:奖励积分
					mainData.put("MaintainType", "1");
					// 关联单号
					mainData.put("RelevantNo", billId);
					// 柜台号
					mainData.put("CounterCode", ConvertUtil.getString(campBaseDTO.getCounterCode()));
					// 会员首单销售标记标记
					mainData.put("MemberFirstSale", true);
					dataLine.put("MainData", mainData);
					//积分明细
					dataLine.put("DetailDataDTOList", detailDataList);
					msgDataMap.put("DataLine", dataLine);
					mqInfoDTO.setMsgDataMap(msgDataMap);
					// 设定插入到MongoDB的信息
					DBObject dbObject = new BasicDBObject();
					// 组织代码
					dbObject.put("OrgCode", orgCode);
					// 品牌代码
					dbObject.put("BrandCode", brandCode);
					// 业务类型
					dbObject.put("TradeType", billType);
					// 单据号
					dbObject.put("TradeNoIF", billCode);
					// 数据来源
					dbObject.put("Sourse", "Cherry");
					mqInfoDTO.setDbObject(dbObject);
					// 发送MQ消息
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
				} else {
					// 积分奖励
					Map<String, Object> prBillInfo = (Map<String, Object>) prMap.get(billId);
					if (null == prBillInfo) {
						prBillInfo = new HashMap<String, Object>();
						prMap.put(billId, prBillInfo);
						// 更新区分：新增
						prBillInfo.put("UPFLAG", "2");
					} else {
						// 更新区分：更新
						prBillInfo.put("UPFLAG", "1");
						// 重算前的积分
						double befPoint = 0;
						if (null != prBillInfo.get("point")) {
							befPoint = Double.parseDouble(prBillInfo.get("point").toString());
						}
						int oldMemberId = 0;
						if (null != prBillInfo.get("oldMemberId")) {
							oldMemberId = Integer.parseInt(prBillInfo.get("oldMemberId").toString());
						}
						if (befPoint == point && oldMemberId == campBaseDTO.getReferrerId()) {
							prMap.remove(billId);
							return;
						}
						// 重算前的积分
						prBillInfo.put("befPoint", befPoint);
					}
					// 单据时间
					prBillInfo.put("ticketDate", ticketDateStr);
					// 积分变化ID
					prBillInfo.put("pointChangeId", pointChange.getPointChangeId());
					// 关联单号
					prBillInfo.put("tradeNoIF", billId);
					// 会员卡号
					prBillInfo.put("memCode", campBaseDTO.getMemCode());
					// 员工号
					prBillInfo.put("baCode", campBaseDTO.getEmployeeCode());
					// 柜台号
					prBillInfo.put("countercode", campBaseDTO.getCounterCode());
					// 奖励的积分值
					prBillInfo.put("point", point);
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
	private boolean checkRewList(CampBaseDTO c, List<Map<String, Object>> list) throws Exception {
		if (null != list && !list.isEmpty()) {
			// 上限验证
			String limit = (String) list.get(0).get("limit");
			if (null != limit && !"".equals(limit)) {
				int ilimit = Integer.parseInt(limit);
				Map<String, Object> searchMap = new HashMap<String, Object>();
				// 老会员ID
				searchMap.put("memberInfoId", c.getReferrerId());
				// 关联单号
				searchMap.put("relevantNo", c.getBillId());
				// 奖励已达到上限
				if (binbedrpoi01_Service.getRewCount(searchMap) >= ilimit) {
					return false;
				}
			}
			for (Map<String, Object> map : list) {
				if (checkRew(c, map)) {
					// 设定的积分值
					if (null != map.get("zpoint")) {
						c.getExtArgs().put("ZPT", map.get("zpoint"));
					} else if (null != map.get("zptInfo")) {
						Map<String, Object> zptInfo = (Map<String, Object>) map.get("zptInfo");
						Map<String, Object> smap = new HashMap<String, Object>();
						smap.put("memberInfoId", c.getReferrerId());
						Map<String, Object> refInfo = binbedrcom01_Service.getRefMemLevelInfo(smap);
						if (null == refInfo || refInfo.isEmpty()) {
							return false;
						}
						String level = String.valueOf(refInfo.get("memberLevel"));
						// 取得等级对应的积分系数
						String mult = (String) zptInfo.get(level);
						if (null == mult) {
							return false;
						}
						double multDou = Double.parseDouble(mult);
						double point = RuleFilterUtil.roundSet(DoubleUtil.mul(c.getPointInfo().getPointChange().getAmount(), multDou), "1");
						c.getExtArgs().put("ZPT", point);
						c.getExtArgs().put("XISU", multDou);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean checkRew(CampBaseDTO c, Map<String, Object> map) throws Exception {
		if ("1".equals(c.getExtArgs().get("RPCK"))) {
			return false;
		}
		// 开始时间
		String fromDate = (String) map.get("fromDate");
		// 结束时间
		String toDate = (String) map.get("toDate");
		// 柜台列表
		List<Map<String, Object>> nodesList = (List<Map<String, Object>>) map.get("nodesList");
		String joinDate = c.getJoinDate();
		if (null == map.get("jndtck") || !"0".equals(map.get("jndtck"))) {
			if (CherryChecker.isNullOrEmpty(joinDate, true)) {
				return false;
			}
			// 验证时间范围
			if (!CherryChecker.isNullOrEmpty(fromDate, true) && DateUtil.compareDate(joinDate, fromDate) < 0) {
				return false;
			}
			if (!CherryChecker.isNullOrEmpty(toDate, true) && DateUtil.compareDate(joinDate, toDate) > 0) {
				return false;
			}
		}
		// 验证销售日期
		boolean isSdateck = "1".equals(map.get("sdateck"));
		boolean isEdateck = "1".equals(map.get("edateck"));
		if (isSdateck || isEdateck) {
			// 单据日期
			String ticketDay = DateUtil.coverTime2YMD(c.getTicketDate(), DateUtil.DATE_PATTERN);
			if (isSdateck) {
				// 验证时间范围
				if (!CherryChecker.isNullOrEmpty(fromDate, true) && DateUtil.compareDate(ticketDay, fromDate) < 0) {
					return false;
				}
			}
			if (isEdateck) {
				// 验证时间范围
				if (!CherryChecker.isNullOrEmpty(toDate, true) && DateUtil.compareDate(ticketDay, toDate) > 0) {
					return false;
				}
			}
		}
		// 不包含的柜台列表
		List<Map<String, Object>> noctList = (List<Map<String, Object>>) map.get("noctList");
		if (null != noctList) {
			boolean isNfdate = !CherryChecker.isNullOrEmpty(map.get("ntfDate"));
			boolean isNtdate = !CherryChecker.isNullOrEmpty(map.get("nttDate"));
			boolean isCk = true;
			if (isNfdate || isNtdate) {
				// 单据日期
				String ticketDay = DateUtil.coverTime2YMD(c.getTicketDate(), DateUtil.DATE_PATTERN);
				if (isNfdate) {
					String ntfDate = (String) map.get("ntfDate");
					// 验证时间范围
					if (DateUtil.compareDate(ticketDay, ntfDate) < 0) {
						isCk = false;
					}
				}
				if (isNtdate) {
					String nttDate = (String) map.get("nttDate");
					// 验证时间范围
					if (DateUtil.compareDate(ticketDay, nttDate) > 0) {
						isCk = false;
					}
				}
			}
			if (isCk) {
				String counterCode = c.getCounterCode();
				if (null != counterCode) {
					for (Map<String, Object> noct : noctList) {
						// 柜台号
						String counter = (String) noct.get("counter");
						if (null != counter && counter.trim().equalsIgnoreCase(counterCode.trim())) {
							return false;
						}
					}
				}
			}
		}
		boolean flag = true;
		if (null != nodesList) {
			flag = false;
			String counterCode = c.getCounterCode();
			for (Map<String, Object> nodes : nodesList) {
				// 柜台号
				String counter = (String) nodes.get("counter");
				if (null != counter && null != counterCode && counter.trim().equalsIgnoreCase(counterCode.trim())) {
					flag = true;
					break;
				}
			}
		}
		String amtmin = (String) map.get("amtmin");
		if (!CherryChecker.isNullOrEmpty(amtmin)) {
			PointDTO pointDTO = c.getPointInfo();
			if (pointDTO.getPointChange().getAmount() < Double.parseDouble(amtmin)) {
				return false;
			}
		}
		return flag;
	}

}

/*	
 * @(#)CampBaseDto.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.dto.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;


/**
 * 会员活动基础 DTO
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class CampBaseDTO extends BasisInfoDTO {

	/** 会员信息ID */
	private int memberInfoId;
	
	/** 推荐会员ID */
	private int referrerId;

	/** 会员卡号 */
	private String memCode;
	
	/** 推荐会员卡号 */
	private String referrerCode;

	/** 会员名称 */
	private String memName;

	/** 会员等级 */
	private int memberLevel;
	
	/** 升级前会员等级 */
	private Integer upgradeFromLevel;

	/** 会员等级代码 */
	private String LevelCode;

	/** 会员生日 */
	private String birthday;

	/** 级别 */
	private int grade;
	
	/** 初始导入的等级ID */
	private int initLevel;

	/** 当前会员等级ID */
	private int curLevelId;

	/** 当前化妆次数 */
	private int curBtimes;
	
	/** 初始累计金额 */
	private double initAmount;

	/** 当前累计金额 */
	private double curTotalAmount;

	/** 当前可兑换金额(化妆次数用) */
	private double curBtimesAmount;
	
	/** 升级所需金额 */
	private double upLevelAmount;
	
	/** 当前积分 */
	private double curPoint;

	/** 改变前的会员等级 */
	private int oldLevelId;

	/** 改变前的化妆次数 */
	private int oldBtimes;

	/** 改变前的累计金额 */
	private double oldTotalAmount;

	/** 可兑换金额(化妆次数用) */
	private double oldBtimesAmount;

	/** 改变前的积分 */
	private double oldPoint;

	/** 单次购买消费金额 */
	private double amount;

	/** 入会日期 */
	private String joinDate;
	
	/** 入会时间 */
	private String joinTime;
	
	/** 入会日期 */
	private String jnDateKbn;
	
	/** 入会途径 */
	private String channelCode;
	
	/** 微信绑定时间 */
	private String wechatBindTime;

	/** 会员等级调整日 */
	private String levelAdjustDay;

	/** 入会区分 */
	private int joinKbn;

	/** 升级前等级 */
	private int prevLevel;
	
	/** 会员登记区分 */
	private int MemRegFlg;

	/** 当前会员等级名称 */
	private String curLevelName;

	/** 购买信息 */
	private Map<String, Object> buyInfo;

	/** 规则执行结果集合 */
	private List<RuleResultDTO> ruleResultList;

	/** 活动ID组 */
	private List<String> ruleKeys;
	
	/** 当前匹配的规则条件 */
	private RuleFilterDTO ruleFilter;
	
	/** 规则条件集合 */
	private List<RuleFilterDTO> matchFilters;
	
	/** 规则条件集合:规则执行部分用 */
	private RuleFilterDTO rhsFilter;
	
	/** 优先级索引 */
	private int proIndex;
	
	/** 会员等级状态 */
	private String levelStatus;
	
	/** 等级有效期开始日 */
	private String levelStartDate;
	
	/** 等级有效期结束日 */
	private String levelEndDate;
	
	/** 规则类型 */
	private String ruleType;
	
	/** 入会或者升级首单号 */
	private String firstBillId;
	
	/** 等级升降级区分 */
	private String levelChangeType;
	
	/** 扩展信息 */
	private Map<String, Object> extArgs;
	
	/** 所有规则 */
	private Map<Object, Object> allRules;
	
	/** 是否忽略业务类型为"MB"的记录 */
	private String ignoreMBFlag;
	
	/** 积分 DTO */
	private PointDTO pointInfo;
	
	/** 活动开始日期 */
	private String ruleFromDate;
	
	/** 各类处理日期 */
	private Map<String, Object> procDates;
	
	/** 有关联退货的原单号 */
	private List<String> billCodePreList;
	
	/** 非关联退货基准点时间 */
	private String srRecalcDate;
	
	/** 首单时间 */
	private String firstTicketTime;
	
	/** 会员等级信息表(俱乐部)ID */
	private int memClubLeveId;
	
	/** 会员俱乐部ID */
	private int memberClubId;
	
	/** 会员俱乐部ID(查询用) */
	private String clubIdStr;
	
	/** 会员俱乐部代号 */
	private String clubCode;
	
	public String getRuleFromDate() {
		return ruleFromDate;
	}

	public void setRuleFromDate(String ruleFromDate) {
		this.ruleFromDate = ruleFromDate;
	}

	public String getIgnoreMBFlag() {
		return ignoreMBFlag;
	}

	public void setIgnoreMBFlag(String ignoreMBFlag) {
		this.ignoreMBFlag = ignoreMBFlag;
	}

	public void emptyAllRules() {
		this.allRules.clear();
	}
	
	public void emptyAllRulesByKbn(int recordKbn) {
		this.allRules.remove(recordKbn);
	}
	
	public String getAllRules(int recordKbn) {
		return (String) this.allRules.get(recordKbn);
	}

	public void addAllRules(String ruleId, int recordKbn) {
		if (!CherryChecker.isNullOrEmpty(ruleId)) {
			String rules = (String) this.allRules.get(recordKbn);
			if (CherryChecker.isNullOrEmpty(rules)) {
				rules = ruleId;
			} else {
				rules += "," + ruleId;
			}
			this.allRules.put(recordKbn, ruleId);
		}
	}

	public CampBaseDTO() {
		this.ruleResultList = new ArrayList<RuleResultDTO>();
		this.ruleKeys = new ArrayList<String>();
		this.buyInfo = new HashMap<String, Object>();
		this.allRules = new HashMap<Object, Object>();
		this.extArgs = new HashMap<String, Object>();
	}
	
	/** 入会时会员等级 */
	private int grantMemberLevel;
	
	/** 每种履历下的操作类型和重算次数信息 */
	private Map<String, Object> recordKbnInfo;
	
	public int getProIndex() {
		return proIndex;
	}

	public void setProIndex(int proIndex) {
		this.proIndex = proIndex;
	}

	public List<RuleFilterDTO> getMatchFilters() {
		return matchFilters;
	}

	public void setMatchFilters(List<RuleFilterDTO> matchFilters) {
		this.matchFilters = matchFilters;
	}

	public RuleFilterDTO getRhsFilter() {
		return rhsFilter;
	}

	public void setRhsFilter(RuleFilterDTO rhsFilter) {
		this.rhsFilter = rhsFilter;
	}

	public void clearResults() {
		this.ruleResultList.clear();
		this.ruleKeys = null;
	}
	
	public void initFact(String ruleType) {
		clearResults();
		emptyRuleIds();
		emptySubCampCodes();
		this.ruleType = ruleType;
	}
	
	public RuleFilterDTO getRuleFilter() {
		return ruleFilter;
	}

	public void setRuleFilter(RuleFilterDTO ruleFilter) {
		this.ruleFilter = ruleFilter;
	}
	
	public List<String> getRuleKeys() {
		return ruleKeys;
	}

	public void setRuleKeys(List<String> ruleKeys) {
		this.ruleKeys = ruleKeys;
	}

	public List<RuleResultDTO> getRuleResultList() {
		return ruleResultList;
	}

	public void setRuleResultList(List<RuleResultDTO> ruleResultList) {
		this.ruleResultList = ruleResultList;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(int memberInfoId) {
		this.memberInfoId = memberInfoId;
	}
	
	public int getInitLevel() {
		return initLevel;
	}

	public void setInitLevel(int initLevel) {
		this.initLevel = initLevel;
	}

	public int getCurLevelId() {
		return curLevelId;
	}

	public void setCurLevelId(int curLevelId) {
		this.curLevelId = curLevelId;
	}

	public String getLevelCode() {
		return LevelCode;
	}

	public void setLevelCode(String levelCode) {
		LevelCode = levelCode;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public int getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(int memberLevel) {
		this.memberLevel = memberLevel;
	}

	public int getCurBtimes() {
		return curBtimes;
	}

	public void setCurBtimes(int curBtimes) {
		this.curBtimes = curBtimes;
	}

	public double getCurTotalAmount() {
		return curTotalAmount;
	}

	public void setCurTotalAmount(double curTotalAmount) {
		this.curTotalAmount = curTotalAmount;
	}

	public double getCurBtimesAmount() {
		return curBtimesAmount;
	}

	public void setCurBtimesAmount(double curBtimesAmount) {
		this.curBtimesAmount = curBtimesAmount;
	}

	public double getOldBtimesAmount() {
		return oldBtimesAmount;
	}

	public void setOldBtimesAmount(double oldBtimesAmount) {
		this.oldBtimesAmount = oldBtimesAmount;
	}
	
	public double getUpLevelAmount() {
		return upLevelAmount;
	}

	public void setUpLevelAmount(double upLevelAmount) {
		this.upLevelAmount = upLevelAmount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	
	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public String getLevelAdjustDay() {
		return levelAdjustDay;
	}

	public void setLevelAdjustDay(String levelAdjustDay) {
		this.levelAdjustDay = levelAdjustDay;
	}

	public int getJoinKbn() {
		return joinKbn;
	}

	public void setJoinKbn(int joinKbn) {
		this.joinKbn = joinKbn;
	}

	public int getPrevLevel() {
		return prevLevel;
	}

	public void setPrevLevel(int prevLevel) {
		this.prevLevel = prevLevel;
	}

	public int getOldLevelId() {
		return oldLevelId;
	}

	public void setOldLevelId(int oldLevelId) {
		this.oldLevelId = oldLevelId;
	}

	public int getOldBtimes() {
		return oldBtimes;
	}

	public void setOldBtimes(int oldBtimes) {
		this.oldBtimes = oldBtimes;
	}

	public double getOldTotalAmount() {
		return oldTotalAmount;
	}

	public void setOldTotalAmount(double oldTotalAmount) {
		this.oldTotalAmount = oldTotalAmount;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public double getCurPoint() {
		return curPoint;
	}

	public void setCurPoint(double curPoint) {
		this.curPoint = curPoint;
	}

	public double getOldPoint() {
		return oldPoint;
	}

	public void setOldPoint(double oldPoint) {
		this.oldPoint = oldPoint;
	}

	public String getCurLevelName() {
		return curLevelName;
	}

	public void setCurLevelName(String curLevelName) {
		this.curLevelName = curLevelName;
	}

	public String getLevelStatus() {
		return levelStatus;
	}

	public void setLevelStatus(String levelStatus) {
		this.levelStatus = levelStatus;
	}

	public String getLevelStartDate() {
		return levelStartDate;
	}

	public void setLevelStartDate(String levelStartDate) {
		this.levelStartDate = levelStartDate;
	}

	public String getLevelEndDate() {
		return levelEndDate;
	}

	public void setLevelEndDate(String levelEndDate) {
		this.levelEndDate = levelEndDate;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public int getGrantMemberLevel() {
		return grantMemberLevel;
	}

	public void setGrantMemberLevel(int grantMemberLevel) {
		this.grantMemberLevel = grantMemberLevel;
	}

	public Map<String, Object> getRecordKbnInfo() {
		return recordKbnInfo;
	}

	public void setRecordKbnInfo(Map<String, Object> recordKbnInfo) {
		this.recordKbnInfo = recordKbnInfo;
	}

	public String getFirstBillId() {
		return firstBillId;
	}

	public void setFirstBillId(String firstBillId) {
		this.firstBillId = firstBillId;
	}

	public Integer getUpgradeFromLevel() {
		return upgradeFromLevel;
	}

	public void setUpgradeFromLevel(Integer upgradeFromLevel) {
		this.upgradeFromLevel = upgradeFromLevel;
	}

	public String getLevelChangeType() {
		return levelChangeType;
	}

	public void setLevelChangeType(String levelChangeType) {
		this.levelChangeType = levelChangeType;
	}
	
	public Map<String, Object> getExtArgs() {
		return extArgs;
	}

	public void setExtArgs(Map<String, Object> extArgs) {
		this.extArgs = extArgs;
	}

	public Map<String, Object> getBuyInfo() {
		return buyInfo;
	}

	public void setBuyInfo(Map<String, Object> buyInfo) {
		this.buyInfo = buyInfo;
	}

	public PointDTO getPointInfo() {
		return pointInfo;
	}

	public void setPointInfo(PointDTO pointInfo) {
		this.pointInfo = pointInfo;
	}
	
	public Map<String, Object> getProcDates() {
		if (null == procDates) {
			this.procDates = new HashMap<String, Object>();
		}
		return procDates;
	}

	public void setProcDates(Map<String, Object> procDates) {
		this.procDates = procDates;
	}

	public List<String> getBillCodePreList() {
		return billCodePreList;
	}

	public void setBillCodePreList(List<String> billCodePreList) {
		this.billCodePreList = billCodePreList;
	}

	public int getMemRegFlg() {
		return MemRegFlg;
	}

	public void setMemRegFlg(int memRegFlg) {
		MemRegFlg = memRegFlg;
	}

	public String getSrRecalcDate() {
		return srRecalcDate;
	}

	public void setSrRecalcDate(String srRecalcDate) {
		this.srRecalcDate = srRecalcDate;
	}

	public String getJnDateKbn() {
		return jnDateKbn;
	}

	public void setJnDateKbn(String jnDateKbn) {
		this.jnDateKbn = jnDateKbn;
	}

	public int getReferrerId() {
		return referrerId;
	}

	public void setReferrerId(int referrerId) {
		this.referrerId = referrerId;
	}

	public String getReferrerCode() {
		return referrerCode;
	}

	public void setReferrerCode(String referrerCode) {
		this.referrerCode = referrerCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getFirstTicketTime() {
		return firstTicketTime;
	}

	public void setFirstTicketTime(String firstTicketTime) {
		this.firstTicketTime = firstTicketTime;
	}

	public int getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(int memberClubId) {
		this.memberClubId = memberClubId;
		if (0 != this.memberClubId) {
			setClubIdStr(String.valueOf(this.memberClubId));
		}
	}
	
	public String getGrpRuleKey() {
		return this.ruleType + "_" + this.memberClubId;
	}

	public String getClubIdStr() {
		return clubIdStr;
	}

	public void setClubIdStr(String clubIdStr) {
		this.clubIdStr = clubIdStr;
	}

	public int getMemClubLeveId() {
		return memClubLeveId;
	}

	public void setMemClubLeveId(int memClubLeveId) {
		this.memClubLeveId = memClubLeveId;
	}

	public String getClubCode() {
		return clubCode;
	}

	public void setClubCode(String clubCode) {
		this.clubCode = clubCode;
	}

	public double getInitAmount() {
		return initAmount;
	}

	public void setInitAmount(double initAmount) {
		this.initAmount = initAmount;
	}

	public String getWechatBindTime() {
		return wechatBindTime;
	}

	public void setWechatBindTime(String wechatBindTime) {
		this.wechatBindTime = wechatBindTime;
	}
}

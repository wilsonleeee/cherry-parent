/*	
 * @(#)PointChangeDTO.java     1.0 2012/02/22	
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

import java.util.List;

/**
 * 会员积分变化主 DTO
 * 
 * @author hub
 * @version 1.0 2011.02.22
 */
public class PointChangeDTO extends BaseDTO{
	
	/** 会员积分变化主ID */
	private int pointChangeId;
	
	/** 所属组织ID */
	private int organizationInfoId;
	
	/** 所属品牌ID */
	private int brandInfoId;
	
	/** 组织结构ID */
	private Integer organizationId;
	
	/** 单据号 */
	private String tradeNoIF;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 会员积分类别ID */
	private String pointTypeId;
	
	/** 会员信息ID */
	private int memberInfoId;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 积分变化日期 */
	private String changeDate;
	
	/** 积分值 */
	private double point;
	
	/** 兑换积分值 */
	private double changePoint;
	
	/** 实收金额 */
	private double amount;
	
	/** 规则匹配用的金额 */
	private double ptamount;
	
	/** 销售数量 */
	private double quantity;
	
	/** 实际实收金额 */
	private double actAmount;
	
	/** 实际销售数量 */
	private double actQuantity;
	
	/** 操作员 */
	private Integer employeeId;
	
	/** 计算倍数 */
	//private double calcuTimes;
	
	/** 规则ID */
	private String ruleIds;
	
	/** 折扣率 */
	private double discount;
	
	/** 附属方式 */
	private String subRuleKbn;
	
	/** 赠送范围：单品或者整单 */
	private String rangeKbn;
	
	/** 会员积分变化明细List */
	private List<PointChangeDetailDTO> changeDetailList;
	
	/** 单据修改次数 */
	private int modifiedTimes;
	
	/** 活动开始日期 */
	private String ruleFromDate;
	
	/** 重算次数 */
	private int reCalcCount;
	
	/** 是否包含积分兑礼 */
	private String dhcpFlag;
	
	/** 执行默认规则区分 */
	private String defaultExecKbn;
	
	/** 机器号  */
	private String machineCode;
	
	/** 是否合并了退货单 */
	private String hasBillSR;
	
	/** 积分清零标识 */
	private String clearFlag;
	
	/** 是否参与了促销活动*/
	private String hasAct;
	
	/** 会员俱乐部ID */
	private int memberClubId;
	
	/** 会员俱乐部ID(查询用) */
	private String clubIdStr;
	
	/** 订单区分 */
	private String esFlag;
	
	private String matchKbn;
	
	/** 非特定产品List */
	private List<PointChangeDetailDTO> noSpecList;
	
	public String getRuleFromDate() {
		return ruleFromDate;
	}

	public void setRuleFromDate(String ruleFromDate) {
		this.ruleFromDate = ruleFromDate;
	}
	
	public int getPointChangeId() {
		return pointChangeId;
	}

	public void setPointChangeId(int pointChangeId) {
		this.pointChangeId = pointChangeId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

//	public double getCalcuTimes() {
//		return calcuTimes;
//	}
//
//	public void setCalcuTimes(double calcuTimes) {
//		this.calcuTimes = calcuTimes;
//	}

	public int getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getTradeNoIF() {
		return tradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		this.tradeNoIF = tradeNoIF;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getPointTypeId() {
		return pointTypeId;
	}

	public void setPointTypeId(String pointTypeId) {
		this.pointTypeId = pointTypeId;
	}

	public int getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(int memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public List<PointChangeDetailDTO> getChangeDetailList() {
		return changeDetailList;
	}

	public void setChangeDetailList(List<PointChangeDetailDTO> changeDetailList) {
		this.changeDetailList = changeDetailList;
	}

	public String getRuleIds() {
		return ruleIds;
	}

	public void setRuleIds(String ruleIds) {
		this.ruleIds = ruleIds;
	}
	
	public void addRuleId(String ruleId) {
		if (null == this.ruleIds || "".equals(this.ruleIds)) {
			this.ruleIds = ruleId;
		} else {
			this.ruleIds += "," + ruleId;
		}
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getSubRuleKbn() {
		return subRuleKbn;
	}

	public void setSubRuleKbn(String subRuleKbn) {
		this.subRuleKbn = subRuleKbn;
	}

	public String getRangeKbn() {
		return rangeKbn;
	}

	public void setRangeKbn(String rangeKbn) {
		this.rangeKbn = rangeKbn;
	}

	public int getModifiedTimes() {
		return modifiedTimes;
	}

	public void setModifiedTimes(int modifiedTimes) {
		this.modifiedTimes = modifiedTimes;
	}

	public double getChangePoint() {
		return changePoint;
	}

	public void setChangePoint(double changePoint) {
		this.changePoint = changePoint;
	}

	public int getReCalcCount() {
		return reCalcCount;
	}

	public void setReCalcCount(int reCalcCount) {
		this.reCalcCount = reCalcCount;
	}

	public String getDhcpFlag() {
		return dhcpFlag;
	}

	public void setDhcpFlag(String dhcpFlag) {
		this.dhcpFlag = dhcpFlag;
	}

	public String getDefaultExecKbn() {
		return defaultExecKbn;
	}

	public void setDefaultExecKbn(String defaultExecKbn) {
		this.defaultExecKbn = defaultExecKbn;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getHasBillSR() {
		return hasBillSR;
	}

	public void setHasBillSR(String hasBillSR) {
		this.hasBillSR = hasBillSR;
	}

	public double getActAmount() {
		return actAmount;
	}

	public void setActAmount(double actAmount) {
		this.actAmount = actAmount;
	}

	public double getActQuantity() {
		return actQuantity;
	}

	public void setActQuantity(double actQuantity) {
		this.actQuantity = actQuantity;
	}

	public String getClearFlag() {
		return clearFlag;
	}

	public void setClearFlag(String clearFlag) {
		this.clearFlag = clearFlag;
	}

	public double getPtamount() {
		return ptamount;
	}

	public void setPtamount(double ptamount) {
		this.ptamount = ptamount;
	}

	public String getHasAct() {
		return hasAct;
	}

	public void setHasAct(String hasAct) {
		this.hasAct = hasAct;
	}

	public int getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(int memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getClubIdStr() {
		return clubIdStr;
	}

	public void setClubIdStr(String clubIdStr) {
		this.clubIdStr = clubIdStr;
	}

	public String getEsFlag() {
		return esFlag;
	}

	public void setEsFlag(String esFlag) {
		this.esFlag = esFlag;
	}

	public List<PointChangeDetailDTO> getNoSpecList() {
		return noSpecList;
	}

	public void setNoSpecList(List<PointChangeDetailDTO> noSpecList) {
		this.noSpecList = noSpecList;
	}

	public String getMatchKbn() {
		return matchKbn;
	}

	public void setMatchKbn(String matchKbn) {
		this.matchKbn = matchKbn;
	}
}

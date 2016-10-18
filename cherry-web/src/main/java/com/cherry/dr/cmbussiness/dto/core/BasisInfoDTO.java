/*	
 * @(#)BasisInfoDTO.java     1.0 2011/05/12	
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
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;

/**
 * 基础信息 DTO
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class BasisInfoDTO extends BaseDTO {

	/** 所属组织ID */
	private int organizationInfoId;

	/** 组织代码 */
	private String orgCode;

	/** 所属品牌ID */
	private int brandInfoId;

	/** 品牌代码 */
	private String brandCode;
	
	/** 订单区分 */
	private String esFlag;
	
	/** 单据号 */
	private String billId;

	/** 业务类型 */
	private String tradeType;

	/** 单据产生日期 */
	private String ticketDate;

	/** 计算日期 */
	private String calcDate;

	/** 履历区分 */
	private int recordKbn;

	/** 重算区分 */
	private int reCalcCount;

	/** 理由 */
	private int reason;

	/** 更新前的值 */
	private String oldValue;

	/** 更新后的值 */
	private String newValue;

	/** 规则ID */
	private String ruleIds;

	/** 来源 */
	private String channel;

	/** 重算区分 */
	private int reCalcFlg;
	
	/** 变化类型 */
	private String changeType;

	/** 柜台号 */
	private String counterCode;
	
	/** 柜台城市ID */
	private int counterCityId;
	
	/** 柜台渠道ID */
	private int channelId;
	
	/** 部门名称 */
	private String departName;
	
	/** 柜台ID */
	private Integer organizationId;
	
	/** 柜台号(开卡柜台) */
	private String belCounterCode;
	
	/** 柜台城市ID(开卡柜台) */
	private int belCounterCityId;
	
	/** 柜台渠道ID(开卡柜台) */
	private int belChannelId;
	
	/** 部门名称(开卡柜台) */
	private String belDepartName;
	
	/** 柜台ID(开卡柜台) */
	private Integer belDepartId;

	/** 员工编号 */
	private String employeeCode;
	
	/** 员工ID */
	private Integer employeeId;

	/** 更新可兑换金额(化妆次数)标识 */
	private boolean upButAmountFlg;

	/** 采用默认规则 */
	private boolean useDefRule;

	/** 规则结果描述 */
	private String resultDpt;

	/** 子活动代码组 */
	private String subCampCodes;
	
	/** 会员积分类别ID */
	private int pointTypeId;
	
	/** 测试区分 */
	private int testFlag;

	/** 等级List */
	private List<Map<String, Object>> memberLevels;

	public int getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getOrgCode() {
		return orgCode;
	}
	
	public int getPointTypeId() {
		return pointTypeId;
	}

	public void setPointTypeId(int pointTypeId) {
		this.pointTypeId = pointTypeId;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public List<Map<String, Object>> getMemberLevels() {
		return memberLevels;
	}

	public void setMemberLevels(List<Map<String, Object>> memberLevels) {
		this.memberLevels = memberLevels;
	}
	
	public String getSubCampCodes() {
		return subCampCodes;
	}

	public int getTestFlag() {
		return testFlag;
	}

	public void setTestFlag(int testFlag) {
		this.testFlag = testFlag;
	}

	public void emptySubCampCodes() {
		this.subCampCodes = null;
	}

	public void addSubCampCodes(String subCampCode) {
		if (CherryChecker.isNullOrEmpty(this.subCampCodes)) {
			this.subCampCodes = subCampCode;
		} else {
			String[] tArray = this.subCampCodes.split(",");
			if (!CampRuleUtil.isContain(tArray, subCampCode)) {
				this.subCampCodes += "," + subCampCode;
			}
		}
	}
	
	public void removeSubCode(String subCampCode) {
		if (null != this.subCampCodes && !"".equals(this.subCampCodes)) {
			String[] tArray = this.subCampCodes.split(",");
			StringBuffer buffer = new StringBuffer();
			int index = 0;
			for (String rid : tArray) {
				if (rid.equals(subCampCode)) {
					continue;
				}
				if (0 < index) {
					buffer.append(",");
				}
				buffer.append(rid);
				index++;
			}
			this.subCampCodes = buffer.toString();
		}
	}

	public boolean isUseDefRule() {
		return useDefRule;
	}

	public void setUseDefRule(boolean useDefRule) {
		this.useDefRule = useDefRule;
	}

	public void emptyRuleIds() {
		this.ruleIds = null;
		this.useDefRule = false;
	}

	public boolean isMatchRule() {
		if (null == this.ruleIds || "".equals(this.ruleIds)) {
			if (this.useDefRule) {
				return true;
			}
			return false;
		}
		return true;
	}

	public void addRuleId(String ruleId) {
		if (null == this.ruleIds || "".equals(this.ruleIds)) {
			this.ruleIds = ruleId;
		} else {
			this.ruleIds += "," + ruleId;
		}
	}
	
	public void removeRuleId(String ruleId) {
		if (null != this.ruleIds && !"".equals(this.ruleIds)) {
			String[] tArray = this.ruleIds.split(",");
			StringBuffer buffer = new StringBuffer();
			int index = 0;
			for (String rid : tArray) {
				if (rid.equals(ruleId)) {
					continue;
				}
				if (0 < index) {
					buffer.append(",");
				}
				buffer.append(rid);
				index++;
			}
			this.ruleIds = buffer.toString();
		}
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTicketDate() {
		return ticketDate;
	}

	public void setTicketDate(String ticketDate) {
		this.ticketDate = ticketDate;
	}

	public String getCalcDate() {
		return calcDate;
	}

	public void setCalcDate(String calcDate) {
		this.calcDate = calcDate;
	}

	public String getRuleIds() {
		return ruleIds;
	}

	public void setRuleIds(String ruleIds) {
		this.ruleIds = ruleIds;
	}

	public int getRecordKbn() {
		return recordKbn;
	}

	public void setRecordKbn(int recordKbn) {
		this.recordKbn = recordKbn;
	}

	public int getReCalcCount() {
		return reCalcCount;
	}

	public void setReCalcCount(int reCalcCount) {
		this.reCalcCount = reCalcCount;
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getReCalcFlg() {
		return reCalcFlg;
	}

	public void setReCalcFlg(int reCalcFlg) {
		this.reCalcFlg = reCalcFlg;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public boolean isUpButAmountFlg() {
		return upButAmountFlg;
	}

	public void setUpButAmountFlg(boolean upButAmountFlg) {
		this.upButAmountFlg = upButAmountFlg;
	}

	public String getResultDpt() {
		return resultDpt;
	}

	public void setResultDpt(String resultDpt) {
		this.resultDpt = resultDpt;
	}
	
	public void setSubCampCodes(String subCampCodes) {
		this.subCampCodes = subCampCodes;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public int getCounterCityId() {
		return counterCityId;
	}

	public void setCounterCityId(int counterCityId) {
		this.counterCityId = counterCityId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getBelCounterCode() {
		return belCounterCode;
	}

	public void setBelCounterCode(String belCounterCode) {
		this.belCounterCode = belCounterCode;
	}

	public int getBelCounterCityId() {
		return belCounterCityId;
	}

	public void setBelCounterCityId(int belCounterCityId) {
		this.belCounterCityId = belCounterCityId;
	}

	public int getBelChannelId() {
		return belChannelId;
	}

	public void setBelChannelId(int belChannelId) {
		this.belChannelId = belChannelId;
	}

	public String getBelDepartName() {
		return belDepartName;
	}

	public void setBelDepartName(String belDepartName) {
		this.belDepartName = belDepartName;
	}

	public Integer getBelDepartId() {
		return belDepartId;
	}

	public void setBelDepartId(Integer belDepartId) {
		this.belDepartId = belDepartId;
	}

	public String getEsFlag() {
		return esFlag;
	}

	public void setEsFlag(String esFlag) {
		this.esFlag = esFlag;
	}
}

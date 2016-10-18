package com.cherry.mb.svc.form;

import com.cherry.cm.form.DataTable_BaseForm;
import com.cherry.mb.svc.dto.RechargeRuleDTO;

public class BINOLMBSVC01_Form extends DataTable_BaseForm{
	
	private RechargeRuleDTO rechargeRule;
	/**规则名称*/
	private String ruleName;
	/**规则参与柜台*/
	private String organizationId;
	/**开始日期*/
	private String startDate;
	/**结束日期*/
	private String endDate;
	/**主规则ID*/
	private String discountId;
	/** 子规则ID*/
	private String subDiscountId;
	/** 0：停用；1：启用*/
	private String validFlag;
	/** 柜台选择模式(1：按区域，2：按渠道) */
	private String selMode;
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlag;
	/** 大区ID */
	private String channelRegionId;
	
	/** 允许接受消息的区域柜台节点 */
    private String allowNodesArray;

	public RechargeRuleDTO getRechargeRule() {
		return rechargeRule;
	}

	public void setRechargeRule(RechargeRuleDTO rechargeRule) {
		this.rechargeRule = rechargeRule;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSubDiscountId() {
		return subDiscountId;
	}

	public void setSubDiscountId(String subDiscountId) {
		this.subDiscountId = subDiscountId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getDiscountId() {
		return discountId;
	}

	public void setDiscountId(String discountId) {
		this.discountId = discountId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getSelMode() {
		return selMode;
	}

	public void setSelMode(String selMode) {
		this.selMode = selMode;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public String getChannelRegionId() {
		return channelRegionId;
	}

	public void setChannelRegionId(String channelRegionId) {
		this.channelRegionId = channelRegionId;
	}

	public String getAllowNodesArray() {
		return allowNodesArray;
	}

	public void setAllowNodesArray(String allowNodesArray) {
		this.allowNodesArray = allowNodesArray;
	}
	
	
}

package com.cherry.ss.prm.dto;

import java.sql.Date;

public class FailUploadDataDTO {

	/** 规则编码 */
	private String ruleCode;

	/** 过滤类型 */
	private String filterType;

	/** 条件类别 */
	private String ConditionType;

	/** 操作类型 */
	private String operateType;

	/** 操作区分 */
	private String operateFlag;

	/** 值 */
	private String failJson;

	/** 门槛子卷No */
	private int contentNo;

	/** 有效区分 */
	private String validFlag;

	/** 作成日时 */
	private Date createTime;

	public String getConditionType() {
		return ConditionType;
	}

	public void setConditionType(String conditionType) {
		ConditionType = conditionType;
	}

	public int getContentNo() {
		return contentNo;
	}

	public void setContentNo(int contentNo) {
		this.contentNo = contentNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getFailJson() {
		return failJson;
	}

	public void setFailJson(String failJson) {
		this.failJson = failJson;
	}
}

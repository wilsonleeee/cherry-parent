/*		
 * @(#)BINOLSSPRM73_Form.java     1.0 2016/03/28		
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
package com.cherry.ss.prm.form;

import com.cherry.cm.form.DataTable_BaseForm;
import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.dto.FailUploadDataDTO;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券规则一览Form
 * @author hub
 * @version 1.0 2016.03.28
 */
public class BINOLSSPRM73_Form extends DataTable_BaseForm{
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 品牌List */
	private List<Map<String, Object>> brandList;
	
	/** 规则编码 */
	private String ruleCode;
	
	/** 规则名称 */
	private String ruleName;
	
	/**审核状态 */
	private String status;
	
	/**有效区分*/
	private String ruleValidFlag;
	
	/**发券开始时间 */
	private String sendStartTime;
	
	/**发券结束时间 */
	private String sendEndTime;
	
	/** 上传的文件 */
	private File upExcel;
	
	/** 导入模式 */
	private String upMode;
	
	/** 条件类别 */
	private String conditionType;
	
	private CouponRuleDTO couponRule;
	
	private String useTimeType;
	
	private Map<String, Object> useTimeInfo;
	
	private List<Map<String, Object>> contentInfoList;
	
	private List<Map<String, Object>> counterList;
	
	private List<Map<String, Object>> memberList;
	
	private Map<String, Object> sendCondInfo;
	
	private Map<String, Object> useCondInfo;
	
	private int batchCount;
	
	private String batchMode;
	
	private List<Map<String, Object>> levelList;

	/** exec导入type */
	private int execLoadType;

	/** 子券No */
	private int contentNo;

	/** 过滤类型 黑白名单*/
	private String filterType;

	/** 目标div */
	private String targetDiv;

	/** 失败导入DTO */
	private FailUploadDataDTO failUploadDataDTO;

	/** 产品对象类型 */
	private String prtObjId;

	/** 产品对象ID */
	private String prtObjType;

	/** 导入失败list */
	private List<Map<String,Object>> failList;

	/** 会员手机号码 */
	private String mobile;
	/** 清空选项 */
	private String emptyType;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<Map<String, Object>> getFailList() {
		return failList;
	}

	public void setFailList(List<Map<String, Object>> failList) {
		this.failList = failList;
	}

	public String getPrtObjId() { return prtObjId;	}

	public void setPrtObjId(String prtObjId) {this.prtObjId = prtObjId;}

	public String getPrtObjType() {return prtObjType;}
	private String organizationID;

	public void setPrtObjType(String prtObjType) {
		this.prtObjType = prtObjType;
	}
	public FailUploadDataDTO getFailUploadDataDTO() {
		return failUploadDataDTO;
	}

	public void setFailUploadDataDTO(FailUploadDataDTO failUploadDataDTO) {
		this.failUploadDataDTO = failUploadDataDTO;
	}

	public int getContentNo() {
		return contentNo;
	}

	public void setContentNo(int contentNo) {
		this.contentNo = contentNo;
	}

	public String getTargetDiv() {
		return targetDiv;
	}

	public void setTargetDiv(String targetDiv) {
		this.targetDiv = targetDiv;
	}

	public int getExecLoadType() {
		return execLoadType;
	}

	public void setExecLoadType(int execLoadType) {
		this.execLoadType = execLoadType;
	}
	public int getBatchCount() {
		return batchCount;
	}
	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}
	public String getBatchMode() {
		return batchMode;
	}
	public void setBatchMode(String batchMode) {
		this.batchMode = batchMode;
	}
	public List<Map<String, Object>> getContentInfoList() {
		return contentInfoList;
	}
	public void setContentInfoList(List<Map<String, Object>> contentInfoList) {
		this.contentInfoList = contentInfoList;
	}
	
	public String getUseTimeType() {
		return useTimeType;
	}
	public void setUseTimeType(String useTimeType) {
		this.useTimeType = useTimeType;
	}
	public Map<String, Object> getUseTimeInfo() {
		return useTimeInfo;
	}
	public void setUseTimeInfo(Map<String, Object> useTimeInfo) {
		this.useTimeInfo = useTimeInfo;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}
	
	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRuleValidFlag() {
		return ruleValidFlag;
	}

	public void setRuleValidFlag(String ruleValidFlag) {
		this.ruleValidFlag = ruleValidFlag;
	}

	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public CouponRuleDTO getCouponRule() {
		return couponRule;
	}

	public void setCouponRule(CouponRuleDTO couponRule) {
		this.couponRule = couponRule;
	}
	public File getUpExcel() {
		return upExcel;
	}
	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}
	public String getUpMode() {
		return upMode;
	}
	public void setUpMode(String upMode) {
		this.upMode = upMode;
	}
	public String getConditionType() {
		return conditionType;
	}
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	public List<Map<String, Object>> getCounterList() {
		return counterList;
	}
	public void setCounterList(List<Map<String, Object>> counterList) {
		this.counterList = counterList;
	}
	public Map<String, Object> getSendCondInfo() {
		if (sendCondInfo == null) {
			sendCondInfo = new HashMap<String, Object>();
		}
		return sendCondInfo;
	}
	public void setSendCondInfo(Map<String, Object> sendCondInfo) {
		this.sendCondInfo = sendCondInfo;
	}
	public Map<String, Object> getUseCondInfo() {
		if (useCondInfo == null) {
			useCondInfo = new HashMap<String, Object>();
		}
		return useCondInfo;
	}
	public void setUseCondInfo(Map<String, Object> useCondInfo) {
		this.useCondInfo = useCondInfo;
	}
	public List<Map<String, Object>> getLevelList() {
		return levelList;
	}
	public void setLevelList(List<Map<String, Object>> levelList) {
		this.levelList = levelList;
	}
	public List<Map<String, Object>> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<Map<String, Object>> memberList) {
		this.memberList = memberList;
	}
	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	public String getOrganizationID() {
		return organizationID;
	}
	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public String getEmptyType() {
		return emptyType;
	}

	public void setEmptyType(String emptyType) {
		this.emptyType = emptyType;
	}
}

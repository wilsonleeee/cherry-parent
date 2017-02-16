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
import java.io.File;
import java.util.List;
import java.util.Map;

public class BINOLSSPRM88_Form extends DataTable_BaseForm{
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 上传的文件 */
	private File upExcel;
	
	/** 导入模式 */
	private String upMode;

	/** 导入批次号 */
	private String searchCode;

	/** 导入批次号 */
	private String searchCodeBlack;

	/** 黑名单白名单区分 */
	private String filterType;

	/** 活动code */
	private String activityCode;

	/**黑白名单柜台list */
	private String counterList;

	/** 区分导入柜台，产品，活动 */
	private String operateType;
	/** 失败结果 */
	private List<Map<String, Object>> failList;

	private  String operateFlag;

	/** 柜台白名单已导入数据 */
	private String excelCounter_w;

	/** 柜台黑名单已导入数据 */
	private String excelCounter_b;

	/** 产品白名单已导入数据 */
	private String excelProduct_w;

	/** 产品黑名单已导入数据 */
	private String excelProduct_b;

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	private String campMebInfo;

	private String memberType;

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	private String groupType;

	public String getCampMebInfo() {
		return campMebInfo;
	}

	public void setCampMebInfo(String campMebInfo) {
		this.campMebInfo = campMebInfo;
	}

	public List<Map<String, Object>> getFailList() {
		return failList;
	}

	public void setFailList(List<Map<String, Object>> failList) {
		this.failList = failList;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
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

	public String getCounterList() {
		return counterList;
	}

	public void setCounterList(String counterList) {
		this.counterList = counterList;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getSearchCodeBlack() {return searchCodeBlack;}

	public void setSearchCodeBlack(String searchCodeBlack) {this.searchCodeBlack = searchCodeBlack;}

	public String getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
	}

	public String getExcelCounter_w() {
		return excelCounter_w;
	}

	public void setExcelCounter_w(String excelCounter_w) {
		this.excelCounter_w = excelCounter_w;
	}

	public String getExcelCounter_b() {
		return excelCounter_b;
	}

	public void setExcelCounter_b(String excelCounter_b) {
		this.excelCounter_b = excelCounter_b;
	}

	public String getExcelProduct_w() {
		return excelProduct_w;
	}

	public void setExcelProduct_w(String excelProduct_w) {
		this.excelProduct_w = excelProduct_w;
	}

	public String getExcelProduct_b() {
		return excelProduct_b;
	}

	public void setExcelProduct_b(String excelProduct_b) {
		this.excelProduct_b = excelProduct_b;
	}
}

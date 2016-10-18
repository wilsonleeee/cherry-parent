/*  
 * @(#)BINOLMOCIO02_Form.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOCIO02_Form extends DataTable_BaseForm {

	//问卷类型
	private String paperType;
	//问卷状态
	private String paperStatus;
	//问卷名称
	private String paperName;
	//开始时间
	private String startDate;
	//结束时间
	private String endDate;
	//品牌
	private String brandInfoId;
	//问卷ID
	private String paperId;
	//选中的要启用停用或者删除的问卷的id
	private String checkedPaperIdArr;
	//发布时间
	private String publishTime;
	
	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCheckedPaperIdArr() {
		return checkedPaperIdArr;
	}

	public void setCheckedPaperIdArr(String checkedPaperIdArr) {
		this.checkedPaperIdArr = checkedPaperIdArr;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getPaperType() {
		return paperType;
	}

	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	public String getPaperStatus() {
		return paperStatus;
	}

	public void setPaperStatus(String paperStatus) {
		this.paperStatus = paperStatus;
	}

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
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
	
}

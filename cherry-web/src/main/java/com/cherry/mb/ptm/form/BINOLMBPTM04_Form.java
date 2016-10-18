/*
 * @(#)BINOLMBPTM04_Action.java     1.0 2013/06/03
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
package com.cherry.mb.ptm.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 积分Excel导入Form
 * 
 * @author LUOHONG
 * @version 1.0 2013/06/03
 */
public class BINOLMBPTM04_Form extends DataTable_BaseForm {
	
	/**积分类型*/
	private String pointType ;
	
	/**积分开始时间*/
	private String startTime ;
	
	/**积分结束时间*/
	private String endTime ;
	
	/** 结果区分 */
	private String resultFlag;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 积分导入Id */
	private String memPointId;
	
	/** 积分导入Id */
	private String pointBillNo;
	
	/** 导入批次名称 */
	private String importBatchName;
	
	public String getPointType() {
		return pointType;
	}
	
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMemPointId() {
		return memPointId;
	}

	public void setMemPointId(String memPointId) {
		this.memPointId = memPointId;
	}

	public String getPointBillNo() {
		return pointBillNo;
	}

	public void setPointBillNo(String pointBillNo) {
		this.pointBillNo = pointBillNo;
	}
	
	public String getImportBatchName() {
		return importBatchName;
	}

	public void setImportBatchName(String importBatchName) {
		this.importBatchName = importBatchName;
	}

}

/*
 *@(#)BINOLMOWAT10_Action.java     1.0 2015/7/1 
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
package com.cherry.mo.wat.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * Job运行履历查询
 * 
 * @author ZCF
 * @version 1.0 2015.06.26
 */
public class BINOLMOWAT10_Form extends BINOLCM13_Form{

    /** 所属品牌*/
    private String brandInfoId;
    /** JobCode*/
    private String jobCode;
    /** 运行结果 */
    private String result;
    /** 运行描述 */
    private String comments;
    /** 开始时间 */
    private String putTimeStart;
    /** 结束时间 */
    private String putTimeEnd;
    
    
	
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getPutTimeStart() {
		return putTimeStart;
	}
	public void setPutTimeStart(String putTimeStart) {
		this.putTimeStart = putTimeStart;
	}
	public String getPutTimeEnd() {
		return putTimeEnd;
	}
	public void setPutTimeEnd(String putTimeEnd) {
		this.putTimeEnd = putTimeEnd;
	}

}

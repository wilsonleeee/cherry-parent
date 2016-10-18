/*
 *@(#)BINOLMOWAT10_Action.java     1.0 2015/12/11 
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
 * Job失败履历一览
 * 
 * @author lzs
 * @version 1.0 2015.12.10
 */
public class BINOLMOWAT11_Form extends BINOLCM13_Form{

    /** 所属品牌*/
    private String brandInfoId;
    /** JobCode*/
    private String jobCode;
    /** UnionIndex */
    private String unionIndex;
    /** UnionIndex1 */
    private String unionIndex1;
    /** UnionIndex2 */
    private String unionIndex2;
    /** UnionIndex3 */
    private String unionIndex3;
    /** 错误信息 */
    private String errorMsg;
    /** 运行描述 */
    private String comments;
    
    
    
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
	public String getUnionIndex() {
		return unionIndex;
	}
	public void setUnionIndex(String unionIndex) {
		this.unionIndex = unionIndex;
	}
	public String getUnionIndex1() {
		return unionIndex1;
	}
	public void setUnionIndex1(String unionIndex1) {
		this.unionIndex1 = unionIndex1;
	}
	public String getUnionIndex2() {
		return unionIndex2;
	}
	public void setUnionIndex2(String unionIndex2) {
		this.unionIndex2 = unionIndex2;
	}
	public String getUnionIndex3() {
		return unionIndex3;
	}
	public void setUnionIndex3(String unionIndex3) {
		this.unionIndex3 = unionIndex3;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}

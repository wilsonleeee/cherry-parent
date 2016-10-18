/*
 * @(#)BINOLMBMBM23_Form.java     1.0 2013.08.29
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
package com.cherry.mb.mbm.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员短信沟通明细画面Form
 * 
 * @author WangCT
 * @version 1.0 2013.08.29
 */
public class BINOLMBMBM23_Form extends DataTable_BaseForm {
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 发送时间上限 */
	private String sendTimeStart;
	
	/** 发送时间下限 */
	private String sendTimeEnd;
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 电话 */
	private String mobilePhone;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getSendTimeStart() {
		return sendTimeStart;
	}

	public void setSendTimeStart(String sendTimeStart) {
		this.sendTimeStart = sendTimeStart;
	}

	public String getSendTimeEnd() {
		return sendTimeEnd;
	}

	public void setSendTimeEnd(String sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

}

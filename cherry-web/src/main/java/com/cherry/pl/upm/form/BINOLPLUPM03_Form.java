/*
 * @(#)BINOLPLUPM03_Form.java   1.0 2010/12/24
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
package com.cherry.pl.upm.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 用户查询实体类
 * 用户信息DTO
 * 
 */
public class BINOLPLUPM03_Form extends DataTable_BaseForm {
	
	private String brandInfoId; //品牌信息ID 
    
	private String passWord; //密码
	
	private String userId;   //用户信息ID
	
	private String modifyTime;	//更新时间

    private String modifyCount;  //更新次数
    
    private String confirmPW; //确认密码
    
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	//密码
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getPassWord() {
		return passWord;
	}

	//用户信息ID
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	 //更新次数
	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	//更新时间
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	//确认密码
	public void setConfirmPW(String confirmPW) {
		this.confirmPW = confirmPW;
	}

	public String getConfirmPW() {
		return confirmPW;
	}
}

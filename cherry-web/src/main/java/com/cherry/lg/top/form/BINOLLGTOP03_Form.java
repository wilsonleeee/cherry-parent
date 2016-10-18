/*
 * @(#)BINOLLGTOP03_Form.java   1.0 2011/02/22
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
package com.cherry.lg.top.form;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 用户查询实体类
 * 用户信息DTO
 * 
 */
public class BINOLLGTOP03_Form extends DataTable_BaseForm{
	
	//原始密码
	private String oldPassWord;
	
	//新密码
	private String newPassWord;
	
	//确认密码
	private String confirmPW;
	
	//更新日时
	private String modifyTime;
	
	//更新次数
	private String modifyCount;
	
	//弹出方式
	private String popType;

	public void setOldPassWord(String oldPassWord) {
		this.oldPassWord = oldPassWord;
	}

	public String getOldPassWord() {
		return oldPassWord;
	}

	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}

	public String getNewPassWord() {
		return newPassWord;
	}

	public void setConfirmPW(String confirmPW) {
		this.confirmPW = confirmPW;
	}

	public String getConfirmPW() {
		return confirmPW;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getModifyCount() {
		return modifyCount;
	}

    public String getPopType() {
        return popType;
    }

    public void setPopType(String popType) {
        this.popType = popType;
    }

}

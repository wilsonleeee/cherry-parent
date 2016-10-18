/*
 * @(#)BINOLMBLEL01_Form.java     1.0 2011/07/20
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
package com.cherry.mb.lel.form;

/**
 * 会员等级维护Form
 * 
 * @author lipc
 * @version 1.0 2011/07/20
 */
public class BINOLMBLEL01_Form {
	
	/** 品牌ID */
	private int brandInfoId;
	
	/** 品牌名 */
	private String brandName;
	
	/** 等级Id */
	private String levelMemberId;
	
	/** 会员俱乐部Id */
	private String memberClubId;
	
	/** 会员俱乐部名称 */
	private String memberClubName;
	
	/** 会员有效期信息*/
	private String memberInfo;

	/** 是否改变标志*/
	private String changeFlag;
	
	/** JSON */
	private String json;
	
	/** 删除的会员等级JSON */
	private String delJson;
	
	/** 会员等级明细JSON */
	private String detailJson;
	
	/** 重算开始日 */
	private String reCalcDate;
	
	/** 会员ID*/
    private String[] memberIdArr;
     
    /** 会员是否全选*/
	private String selectMode;

	/** 会员卡号*/
	private String [] memCodeArr;
	
	 public String[] getMemCodeArr() {
		return memCodeArr;
	}

	public void setMemCodeArr(String[] memCodeArr) {
		this.memCodeArr = memCodeArr;
	}
	
    public String getSelectMode() {
		return selectMode;
	}

	public void setSelectMode(String selectMode) {
		this.selectMode = selectMode;
	}
	 public String[] getMemberIdArr() {
		return memberIdArr;
	}

	public void setMemberIdArr(String[] memberIdArr) {
		this.memberIdArr = memberIdArr;
	}

	public String getReCalcDate() {
		return reCalcDate;
	}

	public void setReCalcDate(String reCalcDate) {
		this.reCalcDate = reCalcDate;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getDelJson() {
		return delJson;
	}

	public void setDelJson(String delJson) {
		this.delJson = delJson;
	}

	public String getDetailJson() {
		return detailJson;
	}

	public void setDetailJson(String detailJson) {
		this.detailJson = detailJson;
	}

	public String getLevelMemberId() {
		return levelMemberId;
	}

	public void setLevelMemberId(String levelMemberId) {
		this.levelMemberId = levelMemberId;
	}

	public String getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(String memberInfo) {
		this.memberInfo = memberInfo;
	}

	public String getChangeFlag() {
		return changeFlag;
	}

	public void setChangeFlag(String changeFlag) {
		this.changeFlag = changeFlag;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getMemberClubName() {
		return memberClubName;
	}

	public void setMemberClubName(String memberClubName) {
		this.memberClubName = memberClubName;
	}
}

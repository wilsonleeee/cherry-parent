/*
 * @(#)BINOLMBRPT02_Form.java     1.0 2014/07/17
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
package com.cherry.mb.rpt.form;

/**
 * 会员微信绑定数统计报表Form
 * 
 * @author WangCT
 * @version 1.0 2014/07/17
 */
public class BINOLMBRPT02_Form {
	
	/** 时间范围上限 **/
	private String wechatBindTimeStart;
	
	/** 时间范围下限 **/
	private String wechatBindTimeEnd;
	
	/** 会员活动码 **/
	private String campaignCode;

	public String getWechatBindTimeStart() {
		return wechatBindTimeStart;
	}

	public void setWechatBindTimeStart(String wechatBindTimeStart) {
		this.wechatBindTimeStart = wechatBindTimeStart;
	}

	public String getWechatBindTimeEnd() {
		return wechatBindTimeEnd;
	}

	public void setWechatBindTimeEnd(String wechatBindTimeEnd) {
		this.wechatBindTimeEnd = wechatBindTimeEnd;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

}

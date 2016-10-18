/*  
 * @(#)BINOLBSCHA01_Form.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;


public class BINOLBSCHA01_Form extends DataTable_BaseForm{
	
	/** 渠道ID */
	private String channelId;
	
	/** 渠道类型 */	
	private String status;
	
	/** 渠道名称 */	
	private String channelName;
	
	/** 渠道名称 */	
	private String channelNameForeign;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 加入日期 */
	private String joinDate;

	/** 节日  */
	private String holidays;
	
	private String validFlag;
	
	private String brandInfoId;
	
	private String[] channelIdArr;
	
	private List<Map<String, Object>> channelList;

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channeList) {
		this.channelList = channeList;
	}

	public void enableChannel(Map<String, Object> map) {
		
	}

	public void disableChannel(Map<String, Object> map) {
		
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setChannelNameForeign(String channelNameForeign) {
		this.channelNameForeign = channelNameForeign;
	}

	public String getChannelNameForeign() {
		return channelNameForeign;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setChannelIdArr(String[] channelIdArr) {
		this.channelIdArr = channelIdArr;
	}

	public String[] getChannelIdArr() {
		return channelIdArr;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}
}

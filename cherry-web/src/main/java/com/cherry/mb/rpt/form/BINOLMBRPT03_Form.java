package com.cherry.mb.rpt.form;

/**
 * 会员等级统计Form
 * 
 * @author WangCT
 * @version 1.0 2014/11/13
 */
public class BINOLMBRPT03_Form {
	
	/** 销售时间上限 */
	private String saleDateStart;
	
	/** 销售时间下限 */
	private String saleDateEnd;
	
	/** 渠道ID */
	private String channelId;
	
	/** 柜台部门ID */
	private String organizationId;
	
	/** 渠道名称 */
	private String channelName;
	
	/** 柜台部门名称 */
	private String organizationName;

	public String getSaleDateStart() {
		return saleDateStart;
	}

	public void setSaleDateStart(String saleDateStart) {
		this.saleDateStart = saleDateStart;
	}

	public String getSaleDateEnd() {
		return saleDateEnd;
	}

	public void setSaleDateEnd(String saleDateEnd) {
		this.saleDateEnd = saleDateEnd;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

}

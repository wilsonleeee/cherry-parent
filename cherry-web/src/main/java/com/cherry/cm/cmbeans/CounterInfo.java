package com.cherry.cm.cmbeans;


/**
 * 柜台信息Bean
 * 
 * @author WangCT
 * @version 1.0 2014/09/15
 */
public class CounterInfo {
	
	/** 部门ID **/
	private int organizationId;
	
	/** 柜台号 **/
	private String counterCode;
	
	/** 柜台名称 **/
	private String counterName;

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

}

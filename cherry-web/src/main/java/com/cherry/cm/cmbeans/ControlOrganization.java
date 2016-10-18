/*  
 * @(#)ControlOrganization.java     1.0 2011/05/31      
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
package com.cherry.cm.cmbeans;

/**
 * 组织部门信息
 * @author dingyc
 *
 */
public class ControlOrganization implements java.io.Serializable{

	private static final long serialVersionUID = 5342828322559236842L;
	/**
	 * 组织结构ID
	 */
	private int BIN_OrganizationID;
	
	/**
	 * 部门代码
	 */	
	private String DepartCode;
	/**
	 * 部门名称
	 */
	private String DepartName;
	
	/**
	 * 部门类型
	 */
	private String DepartType;
	/**
	 * 管理类型
	 */
	private String ManageType;
	/**
	 * @return the bIN_OrganizationID
	 */
	public int getBIN_OrganizationID() {
		return BIN_OrganizationID;
	}
	/**
	 * @param bINOrganizationID the bIN_OrganizationID to set
	 */
	public void setBIN_OrganizationID(int bINOrganizationID) {
		BIN_OrganizationID = bINOrganizationID;
	}
	/**
	 * @return the departCode
	 */
	public String getDepartCode() {
		return DepartCode;
	}
	/**
	 * @param departCode the departCode to set
	 */
	public void setDepartCode(String departCode) {
		DepartCode = departCode;
	}
	/**
	 * @return the departName
	 */
	public String getDepartName() {
		return DepartName;
	}
	/**
	 * @param departName the departName to set
	 */
	public void setDepartName(String departName) {
		DepartName = departName;
	}
	/**
	 * @return the departType
	 */
	public String getDepartType() {
		return DepartType;
	}
	/**
	 * @param departType the departType to set
	 */
	public void setDepartType(String departType) {
		DepartType = departType;
	}
	/**
	 * @return the manageType
	 */
	public String getManageType() {
		return ManageType;
	}
	/**
	 * @param manageType the manageType to set
	 */
	public void setManageType(String manageType) {
		ManageType = manageType;
	}
	

}

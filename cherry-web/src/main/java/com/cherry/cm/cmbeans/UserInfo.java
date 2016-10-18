/*  
 * @(#)UserInfo.java     1.0 2011/05/31      
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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 用户信息类
 * 
 * @author dingyc
 *
 */
public class UserInfo implements java.io.Serializable{	

	private static final long serialVersionUID = -4370265772831531165L;
	/**用户  ID */
	private int BIN_UserID;
	
	/**登录时输入的账号 */
	private String loginName;	
	
	/**登录时输入的密码 */
	private String password;
	
	/**用户登录时的语言信息 */
	private String language;
	
	/**雇员  ID */
	private int BIN_EmployeeID;
	
	/**雇员代号 */
	private String employeeCode;
	
	/**雇员名称 */
	private String employeeName;
	
	/** 用户所属的组织  ID	  */
	private int BIN_OrganizationInfoID;
	
	/**用户所属的组织   组织名称 */
	private String orgName;	
	
	private String orgCode;
	
	/**用户所属的组织   组织代码 */
	private String organizationInfoCode;

	/**用户所属的品牌	品牌ID */
	private int BIN_BrandInfoID;
	
	/**用户所属的品牌	品牌code */
	private String brandCode;	
	
	/**用户所属的品牌	品牌名称 */
	private String brandName;
	
	/**
	 * 组织结构ID(所属部门ID)
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
	 * 岗位类别
	 */
	private int BIN_PositionCategoryID;
	
	/**
	 * 岗位类别代码
	 */
	private String categoryCode;
	
	
	/**
	 * 岗位类别名称
	 */
	private String categoryName;
	
	/**
	 * 岗位类别等级
	 */
	private String categoryGrade;
	
	/**登录时间 */
	private Date loginTime;
	
	/**登录时的IP地址*/
	private String loginIP;
	
	/**登录用的浏览器版本*/
	private String userAgent;
	
	/**当前操作的机能*/
	private String currentUnit;
	
	/**
	 * 用户管辖的部门
	 */
	private List<ControlOrganization> controlOrganizationList;

	/**
	 * 拥有的角色
	 */
	private List<RoleInfo> rolelist;
	
	//一个用户能对应多个品牌，或对应多个部门，以下定义可以设定用户在某个操作中使用的品牌部门等，便于参数传递
	
//	/**当前操作所使用的组织信息ID*/
//	private String currentOrganizationInfoID;
	
	/**当前操作所属的品牌ID*/
	private String currentBrandInfoID;
	
	/**当前操作所属的品牌代码*/
//	private String currentBrandCode;
	
	/**当前操作所使用的部门ID*/
	private String currentOrganizationID;
	
	/**当前操作所使用的部门类型*/
	private String currentOrganizationType;
	
	/**当前操作对应的部门代码*/
	private String currentOrganizationCode;
	
	private String sessionID;
	
    public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
     * 比较UserInfo是否相等（用于在线用户列表）
     * @return boolean
     */
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(!(o instanceof UserInfo)){
            return false;
        }
        UserInfo tmp = (UserInfo)o;
        if(this.loginName.equals(tmp.loginName)
                && this.loginIP.equals(tmp.loginIP)
                && this.loginTime.getTime() == tmp.loginTime.getTime()
                && this.userAgent.equals(tmp.userAgent)){
            return true;
        }
        return false;
    }

	/**
	 * @return the bIN_UserID
	 */
	public int getBIN_UserID() {
		return BIN_UserID;
	}

	/**
	 * @param bIN_UserID the bIN_UserID to set
	 */
	public void setBIN_UserID(int bIN_UserID) {
		BIN_UserID = bIN_UserID;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the bIN_EmployeeID
	 */
	public int getBIN_EmployeeID() {
		return BIN_EmployeeID;
	}

	/**
	 * @param bIN_EmployeeID the bIN_EmployeeID to set
	 */
	public void setBIN_EmployeeID(int bIN_EmployeeID) {
		BIN_EmployeeID = bIN_EmployeeID;
	}

	/**
	 * @return the employeeCode
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}

	/**
	 * @param employeeCode the employeeCode to set
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the bIN_OrganizationInfoID
	 */
	public int getBIN_OrganizationInfoID() {
		return BIN_OrganizationInfoID;
	}

	/**
	 * @param bIN_OrganizationInfoID the bIN_OrganizationInfoID to set
	 */
	public void setBIN_OrganizationInfoID(int bIN_OrganizationInfoID) {
		BIN_OrganizationInfoID = bIN_OrganizationInfoID;
	}

	/**
	 * @return the bIN_BrandInfoID
	 */
	public int getBIN_BrandInfoID() {
		return BIN_BrandInfoID;
	}

	/**
	 * @param bIN_BrandInfoID the bIN_BrandInfoID to set
	 */
	public void setBIN_BrandInfoID(int bIN_BrandInfoID) {
		BIN_BrandInfoID = bIN_BrandInfoID;
	}

	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the loginIP
	 */
	public String getLoginIP() {
		return loginIP;
	}

	/**
	 * @param loginIP the loginIP to set
	 */
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	/**
	 * @return the rolelist
	 */
	public List<RoleInfo> getRolelist() {
		return rolelist;
	}

	/**
	 * @param rolelist the rolelist to set
	 */
	public void setRolelist(List<RoleInfo> rolelist) {
		this.rolelist = rolelist;
	}
	

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the currentUnit
	 */
	public String getCurrentUnit() {
		return currentUnit;
	}

	/**
	 * @param currentUnit the currentUnit to set
	 */
	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}

//	/**
//	 * @return the currentOrganizationID
//	 */
//	public String getCurrentOrganizationInfoID() {
//		if("".equals(currentOrganizationInfoID)||currentOrganizationInfoID==null){
//			return String.valueOf(this.BIN_OrganizationInfoID);
//		}
//		return currentOrganizationInfoID;
//	}
//
//	/**
//	 * @param currentOrganizationID the currentOrganizationID to set
//	 */
//	public void setCurrentOrganizationInfoID(String currentOrganizationID) {
//		this.currentOrganizationInfoID = currentOrganizationID;
//	}

	/**
	 * @return the currentBrandID
	 */
	public String getCurrentBrandInfoID() {
		if("".equals(currentBrandInfoID)||currentBrandInfoID==null){
			return String.valueOf(this.BIN_BrandInfoID);
		}
		return currentBrandInfoID;
	}

	/**
	 * @param currentBrandID the currentBrandID to set
	 */
	public void setCurrentBrandInfoID(String currentBrandID) {
		this.currentBrandInfoID = currentBrandID;
	}

	/**
	 * @return the currentOrganizationID
	 */
	public String getCurrentOrganizationID() {
		return currentOrganizationID;
	}

	/**
	 * @param currentOrganizationID the currentOrganizationID to set
	 */
	public void setCurrentOrganizationID(String currentOrganizationID) {
		this.currentOrganizationID = currentOrganizationID;
	}

	/**
	 * @return the currentOrganizationType
	 */
	public String getCurrentOrganizationType() {
		if("".equals(currentOrganizationType)||currentOrganizationType==null){
			Iterator<ControlOrganization> it = controlOrganizationList.iterator();
			while(it.hasNext()){
				ControlOrganization temp = it.next();
				if(String.valueOf(temp.getBIN_OrganizationID()).equals(currentOrganizationID)){
					return temp.getDepartType();
				}
			}
			return "";
		}
		return currentOrganizationType;
	}

	/**
	 * @param currentOrganizationType the currentOrganizationType to set
	 */
	public void setCurrentOrganizationType(String currentOrganizationType) {
		this.currentOrganizationType = currentOrganizationType;
	}

	/**
	 * @return the brandCode
	 */
	public String getBrandCode() {
		return brandCode;
	}

	/**
	 * @param brandCode the brandCode to set
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

//	/**
//	 * @return the currentBrandCode
//	 */
//	public String getCurrentBrandCode() {
//		return currentBrandCode;
//	}
//
//	/**
//	 * @param currentBrandCode the currentBrandCode to set
//	 */
//	public void setCurrentBrandCode(String currentBrandCode) {
//		this.currentBrandCode = currentBrandCode;
//	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the currentCategoryCode
	 */
//	public String getCurrentCategoryCode() {
//		return currentCategoryCode;
//	}
//
//	/**
//	 * @param currentCategoryCode the currentCategoryCode to set
//	 */
//	public void setCurrentCategoryCode(String currentCategoryCode) {
//		this.currentCategoryCode = currentCategoryCode;
//	}

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
	 * @return the bIN_PositionCategoryID
	 */
	public int getBIN_PositionCategoryID() {
		return BIN_PositionCategoryID;
	}

	/**
	 * @param bINPositionCategoryID the bIN_PositionCategoryID to set
	 */
	public void setBIN_PositionCategoryID(int bINPositionCategoryID) {
		BIN_PositionCategoryID = bINPositionCategoryID;
	}

	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the categoryGrade
	 */
	public String getCategoryGrade() {
		return categoryGrade;
	}

	/**
	 * @param categoryGrade the categoryGrade to set
	 */
	public void setCategoryGrade(String categoryGrade) {
		this.categoryGrade = categoryGrade;
	}

	/**
	 * @return the controlOrganizationList
	 */
	public List<ControlOrganization> getControlOrganizationList() {
		return controlOrganizationList;
	}

	/**
	 * @param controlOrganizationList the controlOrganizationList to set
	 */
	public void setControlOrganizationList(
			List<ControlOrganization> controlOrganizationList) {
		this.controlOrganizationList = controlOrganizationList;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the organizationInfoCode
	 */
	public String getOrganizationInfoCode() {
		return organizationInfoCode;
	}

	/**
	 * @param organizationInfoCode the organizationInfoCode to set
	 */
	public void setOrganizationInfoCode(String organizationInfoCode) {
		this.organizationInfoCode = organizationInfoCode;
	}

	/**
	 * @return the currentOrganizationCode
	 */
	public String getCurrentOrganizationCode() {
		return currentOrganizationCode;
	}

	/**
	 * @param currentOrganizationCode the currentOrganizationCode to set
	 */
	public void setCurrentOrganizationCode(String currentOrganizationCode) {
		this.currentOrganizationCode = currentOrganizationCode;
	}

	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}

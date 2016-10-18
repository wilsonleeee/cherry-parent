package com.cherry.wp.wo.set.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 营业员管理Action
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWOSET01_Form extends DataTable_BaseForm {
	
	/** 员工ID **/
	private String employeeId;
	
	/** 员工代码 **/
	private String employeeCode;
	
	/** 员工姓名 **/
	private String employeeName;
	
	/** 身份证 **/
	private String identityCard;
	
	/** 手机 **/
	private String mobilePhone;
	
	/** 查询营业员唯一码（可以为员工代码、身份证、手机中的任意一个） **/
	private String searchKey;
	
	/** 员工代码（查询用） **/
	private String employeeCodeQ;
	
	/** 员工姓名（查询用） **/
	private String employeeNameQ;
	
	/** 身份证（查询用） **/
	private String identityCardQ;
	
	/** 手机（查询用） **/
	private String mobilePhoneQ;
	
	/** 变更前身份证 **/
	private String oldIdentityCard;
	
	/** 变更前手机 **/
	private String oldMobilePhone;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getEmployeeCodeQ() {
		return employeeCodeQ;
	}

	public void setEmployeeCodeQ(String employeeCodeQ) {
		this.employeeCodeQ = employeeCodeQ;
	}

	public String getEmployeeNameQ() {
		return employeeNameQ;
	}

	public void setEmployeeNameQ(String employeeNameQ) {
		this.employeeNameQ = employeeNameQ;
	}

	public String getIdentityCardQ() {
		return identityCardQ;
	}

	public void setIdentityCardQ(String identityCardQ) {
		this.identityCardQ = identityCardQ;
	}

	public String getMobilePhoneQ() {
		return mobilePhoneQ;
	}

	public void setMobilePhoneQ(String mobilePhoneQ) {
		this.mobilePhoneQ = mobilePhoneQ;
	}

	public String getOldIdentityCard() {
		return oldIdentityCard;
	}

	public void setOldIdentityCard(String oldIdentityCard) {
		this.oldIdentityCard = oldIdentityCard;
	}

	public String getOldMobilePhone() {
		return oldMobilePhone;
	}

	public void setOldMobilePhone(String oldMobilePhone) {
		this.oldMobilePhone = oldMobilePhone;
	}

}

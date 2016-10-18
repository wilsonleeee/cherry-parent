/*
 * @(#)BINOLPLSCF06_Form.java     1.0 2011/3/29
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

package com.cherry.pl.scf.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * code值一览Form
 * 
 * @author zhangjie
 * @version 1.0 2011.3.29
 */
public class BINOLPLSCF07_Form extends DataTable_BaseForm {
	
	/** 品牌Code */
	private String brandCode;
	
	/** 组织Code */
	private String orgCode;	

	/** 品牌名字 */
	private String brandName;

	/** 组织名字 */
	private String orgName;

	/** Key */
	private String codeKey;

	/** 值1 */
	private String value1;

	/** 值2 */
	private String value2;

	/** 值3 */
	private String value3;

	/** 级别 */
	private String grade;

	/** Value2说明 */
	private String value2Description;

	/** Value3说明 */
	private String value3Description;

	/** 显示顺序 */
	private String codeOrder;

	/** Code类别 */
	private String codeType;

	/** Code管理ID */
	private String codeManagerID;

	/** Code类别名称 */
	private String codeName;

	/** Key说明 */
	private String keyDescription;

	/** Value1说明 */
	private String value1Description;
	
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getCodeOrder() {
		return codeOrder;
	}

	public void setCodeOrder(String codeOrder) {
		this.codeOrder = codeOrder;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCodeManagerID() {
		return codeManagerID;
	}

	public void setCodeManagerID(String codeManagerID) {
		this.codeManagerID = codeManagerID;
	}

	public String getKeyDescription() {
		return keyDescription;
	}

	public void setKeyDescription(String keyDescription) {
		this.keyDescription = keyDescription;
	}

	public String getValue1Description() {
		return value1Description;
	}

	public void setValue1Description(String value1Description) {
		this.value1Description = value1Description;
	}

	public String getValue2Description() {
		return value2Description;
	}

	public void setValue2Description(String value2Description) {
		this.value2Description = value2Description;
	}

	public String getValue3Description() {
		return value3Description;
	}

	public void setValue3Description(String value3Description) {
		this.value3Description = value3Description;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

}

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
 * code值管理一览Form
 * 
 * @author zhangjie
 * @version 1.0 2011.3.29
 */
public class BINOLPLSCF06_Form extends DataTable_BaseForm {
	
	/**Code管理值ID*/
	private String codeManagerID;
	
	/** 品牌Code */
	private String brandCode;
	
	/** 组织Code */
	private String orgCode;	
	
	/** Code类别 */
	private String codeType;
	
	/** Code类别名称 */
	private String codeName;
	
	/** Key说明 */
	private String keyDescription;
	
	/** Value1说明 */
	private String value1Description;
	
	/** Value2说明 */
	private String value2Description;

	/** Value3说明 */
	private String value3Description;
	
	/**要删除的code值*/
	private String deleteCodeArr;
	
	/**要编辑的code值codeKey*/
	private String[] codeKeyArr;
	
	/**要编辑的code值value1*/
	private String[] value1Arr;
	
	/**要编辑的code值value2*/
	private String[] value2Arr;
	
	/**要编辑的code值value3*/
	private String[] value3Arr;
	
	/**要编辑的code值grade*/
	private String[] gradeArr;
	
	/**要编辑的code值codeOrder*/
	private String[] codeOrderArr;
	
	public String[] getCodeKeyArr() {
		return codeKeyArr;
	}

	public void setCodeKeyArr(String[] codeKeyArr) {
		this.codeKeyArr = codeKeyArr;
	}

	public String[] getValue1Arr() {
		return value1Arr;
	}

	public void setValue1Arr(String[] value1Arr) {
		this.value1Arr = value1Arr;
	}

	public String[] getValue2Arr() {
		return value2Arr;
	}

	public void setValue2Arr(String[] value2Arr) {
		this.value2Arr = value2Arr;
	}

	public String[] getValue3Arr() {
		return value3Arr;
	}

	public void setValue3Arr(String[] value3Arr) {
		this.value3Arr = value3Arr;
	}

	public String[] getGradeArr() {
		return gradeArr;
	}

	public void setGradeArr(String[] gradeArr) {
		this.gradeArr = gradeArr;
	}

	public String[] getCodeOrderArr() {
		return codeOrderArr;
	}

	public void setCodeOrderArr(String[] codeOrderArr) {
		this.codeOrderArr = codeOrderArr;
	}

	public String getCodeManagerID() {
		return codeManagerID;
	}

	public void setCodeManagerID(String codeManagerID) {
		this.codeManagerID = codeManagerID;
	}

	public String getDeleteCodeArr() {
		return deleteCodeArr;
	}

	public void setDeleteCodeArr(String deleteCodeArr) {
		this.deleteCodeArr = deleteCodeArr;
	}

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

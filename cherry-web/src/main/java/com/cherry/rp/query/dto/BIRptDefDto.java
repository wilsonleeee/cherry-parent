/*
 * @(#)BIRptDefDto.java     1.0 2011/03/22
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

package com.cherry.rp.query.dto;

/**
 * BI报表定义信息（包括列信息，行信息，统计信息）
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class BIRptDefDto {
	
	/** 定义类型 */
	private String defType;
	
	/** 设定值 */
	private String defValue;
	
	/** 设定值顺序 */
	private String defSeq;
	
	/** 设定值的列号 */
	private String defColumnNo;
	
	/** 行列显示区分 */
	private String colRowVisible;
	
	/** 小计显示方式 */
	private String colRowTotalVis;
	
	/** 统计方式 */
	private String statisticType;
	
	/** 统计项显示方式 */
	private String statisticVis;
	
	/** 后期计算字段计算式 */
	private String defScript;
	
	/** 是否可用于下钻 */
	private String isDrillDown;
	
	/** 是否可用于钻透 */
	private String isDrillThrough;
	
	/** 设定值（表中的字段名） */
	private String defValueTbl;
	
	/** 显示格式 */
	private String defFormat;

	public String getDefType() {
		return defType;
	}

	public void setDefType(String defType) {
		this.defType = defType;
	}

	public String getDefValue() {
		return defValue;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	public String getDefSeq() {
		return defSeq;
	}

	public void setDefSeq(String defSeq) {
		this.defSeq = defSeq;
	}

	public String getDefColumnNo() {
		return defColumnNo;
	}

	public void setDefColumnNo(String defColumnNo) {
		this.defColumnNo = defColumnNo;
	}

	public String getColRowVisible() {
		return colRowVisible;
	}

	public void setColRowVisible(String colRowVisible) {
		this.colRowVisible = colRowVisible;
	}

	public String getColRowTotalVis() {
		return colRowTotalVis;
	}

	public void setColRowTotalVis(String colRowTotalVis) {
		this.colRowTotalVis = colRowTotalVis;
	}

	public String getStatisticType() {
		return statisticType;
	}

	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}

	public String getStatisticVis() {
		return statisticVis;
	}

	public void setStatisticVis(String statisticVis) {
		this.statisticVis = statisticVis;
	}

	public String getDefScript() {
		return defScript;
	}

	public void setDefScript(String defScript) {
		this.defScript = defScript;
	}

	public String getIsDrillDown() {
		return isDrillDown;
	}

	public void setIsDrillDown(String isDrillDown) {
		this.isDrillDown = isDrillDown;
	}

	public String getIsDrillThrough() {
		return isDrillThrough;
	}

	public void setIsDrillThrough(String isDrillThrough) {
		this.isDrillThrough = isDrillThrough;
	}

	public String getDefValueTbl() {
		return defValueTbl;
	}

	public void setDefValueTbl(String defValueTbl) {
		this.defValueTbl = defValueTbl;
	}

	public String getDefFormat() {
		return defFormat;
	}

	public void setDefFormat(String defFormat) {
		this.defFormat = defFormat;
	}

}

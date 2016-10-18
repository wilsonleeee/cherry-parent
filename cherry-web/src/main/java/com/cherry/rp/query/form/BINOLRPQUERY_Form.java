/*	
 * @(#)BINOLRPQUERY_Form.java     1.0 2010/11/08		
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

package com.cherry.rp.query.form;

import java.util.List;

import com.cherry.cm.form.DataTable_BaseForm;
import com.cherry.rp.query.dto.BIRptDefDto;
import com.cherry.rp.query.dto.BIRptQryDefDto;

/**
 * 查询BI报表共通Form
 * @author WangCT
 *
 */
public class BINOLRPQUERY_Form extends DataTable_BaseForm {
	
	/** BI报表定义信息List */
	private List<BIRptDefDto> biRptDefList;
	
	/** BI报表查询条件List */
	private List<BIRptQryDefDto> biRptQryDefList;
	
	/** BI报表ID */
	private String biRptCode;
	
	/** BI报表查询条件(为钻透时保留) */
	private String biQuery;
	
	/** BI报表查询条件(为钻透时保留,且用来画面显示) */
	private String biQueryDisPlay;
	
	/** BI报表钻透查询条件 */
	private String drillQuery;
	
	/** BI报表钻透URL */
	private String drillUrl;
	
	/** BI报表钻透标题 */
	private String title;

	public List<BIRptDefDto> getBiRptDefList() {
		return biRptDefList;
	}

	public void setBiRptDefList(List<BIRptDefDto> biRptDefList) {
		this.biRptDefList = biRptDefList;
	}

	public List<BIRptQryDefDto> getBiRptQryDefList() {
		return biRptQryDefList;
	}

	public void setBiRptQryDefList(List<BIRptQryDefDto> biRptQryDefList) {
		this.biRptQryDefList = biRptQryDefList;
	}

	public String getBiRptCode() {
		return biRptCode;
	}

	public void setBiRptCode(String biRptCode) {
		this.biRptCode = biRptCode;
	}

	public String getBiQuery() {
		return biQuery;
	}

	public String getBiQueryDisPlay() {
		return biQueryDisPlay;
	}

	public void setBiQueryDisPlay(String biQueryDisPlay) {
		this.biQueryDisPlay = biQueryDisPlay;
	}

	public void setBiQuery(String biQuery) {
		this.biQuery = biQuery;
	}

	public String getDrillQuery() {
		return drillQuery;
	}

	public void setDrillQuery(String drillQuery) {
		this.drillQuery = drillQuery;
	}

	public String getDrillUrl() {
		return drillUrl;
	}

	public void setDrillUrl(String drillUrl) {
		this.drillUrl = drillUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

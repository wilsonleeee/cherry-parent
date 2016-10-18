/*	
 * @(#)BINOLPLGAD02_Form.java     1.0 2013.8.29		
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
package com.cherry.pl.gad.form;

/**
 * 小工具布局配置Form
 *
 * @author WangCT
 * @version 1.0 2013.8.29	
 */
public class BINOLPLGAD02_Form {
	
	/** 小工具所属画面ID **/
	private String pageId;
	
	/** 小工具ID **/
	private String[] gadgetInfoIds;
	
	/** 小工具行 **/
	private String[] rowNumbers;
	
	/** 小工具列 **/
	private String[] columnNumbers;

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String[] getGadgetInfoIds() {
		return gadgetInfoIds;
	}

	public void setGadgetInfoIds(String[] gadgetInfoIds) {
		this.gadgetInfoIds = gadgetInfoIds;
	}

	public String[] getRowNumbers() {
		return rowNumbers;
	}

	public void setRowNumbers(String[] rowNumbers) {
		this.rowNumbers = rowNumbers;
	}

	public String[] getColumnNumbers() {
		return columnNumbers;
	}

	public void setColumnNumbers(String[] columnNumbers) {
		this.columnNumbers = columnNumbers;
	}

}

package com.cherry.wp.wr.srp.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 门店排行榜Form
 * 
 * @author songka
 * @version 1.0 2015/09/07
 */
public class BINOLWRSRP07_Form extends DataTable_BaseForm {
	
	/** 销售开始时间 **/
	private String saleDateStart;
	
	/** 销售结束时间 **/
	private String saleDateEnd;
	
	/** 大分类ID */
	private String bigClassId;
	
	/** 大分类名称 **/
	private String bigClassName;
	
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

	public String getBigClassId() {
		return bigClassId;
	}

	public void setBigClassId(String bigClassId) {
		this.bigClassId = bigClassId;
	}

	public String getBigClassName() {
		return bigClassName;
	}

	public void setBigClassName(String bigClassName) {
		this.bigClassName = bigClassName;
	}
}

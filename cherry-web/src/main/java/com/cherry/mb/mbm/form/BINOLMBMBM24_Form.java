package com.cherry.mb.mbm.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBMBM24_Form extends DataTable_BaseForm{
	/** 开始时间*/
	private String startDate;
	
	/*** 结束时间*/
	private String endDate;
	
	/** 导入名称*/
	private String importName;
	
	/** 导入名称模糊查询*/
	private String importNameStr;
	
	/** 导入结果区分*/
	private String resultFlag ;
	
	/** 会员Code*/
	private String memberCode;
	
	/**导入主表Id*/
	private int memImportId;
	
	/**导入主表Id*/
	private String billNo;

	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public String getImportNameStr() {
		return importNameStr;
	}

	public void setImportNameStr(String importNameStr) {
		this.importNameStr = importNameStr;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public int getMemImportId() {
		return memImportId;
	}

	public void setMemImportId(int memImportId) {
		this.memImportId = memImportId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}

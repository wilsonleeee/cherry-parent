package com.cherry.wp.sal.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL06_Form extends DataTable_BaseForm {
	
	// BA姓名
	private String baName;
	
	// 会员姓名
	private String memberName;
	
	// 柜台号
	private String counterCode;
	
	// 挂单ID
	private String hangBillId;
	
	// 单据列表
	private List<Map<String, Object>> billList;
	
	//单据号
	private String hangBillCode;
	
	//交易开始时间
	private String billHangDateStart;
	
	//交易截止时间
	private String billHangDateEnd;
	
	//是否只显示重试单据的表示
	private String retryDataFlag;
	
	public String getBaName() {
		return baName;
	}

	public void setBaName(String baName) {
		this.baName = baName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getHangBillId() {
		return hangBillId;
	}

	public void setHangBillId(String hangBillId) {
		this.hangBillId = hangBillId;
	}

	public List<Map<String, Object>> getBillList() {
		return billList;
	}

	public void setBillList(List<Map<String, Object>> billList) {
		this.billList = billList;
	}

	
	public String getHangBillCode() {
		return hangBillCode;
	}

	public void setHangBillCode(String hangBillCode) {
		this.hangBillCode = hangBillCode;
	}

	public String getBillHangDateStart() {
		return billHangDateStart;
	}

	public void setBillHangDateStart(String billHangDateStart) {
		this.billHangDateStart = billHangDateStart;
	}

	public String getBillHangDateEnd() {
		return billHangDateEnd;
	}

	public void setBillHangDateEnd(String billHangDateEnd) {
		this.billHangDateEnd = billHangDateEnd;
	}

	public String getRetryDataFlag() {
		return retryDataFlag;
	}

	public void setRetryDataFlag(String retryDataFlag) {
		this.retryDataFlag = retryDataFlag;
	}
	
	
}

package com.cherry.mb.cct.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBCCT01_Form extends DataTable_BaseForm{
	
	private String code;
	/** 品牌ID */
	private String ccBrandInfoId;
	
	/** 客服工号  */
	private String cno;
	
	/** 来电号码 */
	private String customerNumber;
	
	/** 来电号码类型（1.固话/2.手机） */
	private String customerNumberType;
	
	/** 电话所属地区区号 */
	private String customerAreaCode;
	
	/** 呼叫类型 */
	private String calltype;
	
	/** 呼叫唯一标识号 */
	private String callId;
	
	/** 外呼任务标识号 */
	private String taskId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCcBrandInfoId() {
		return ccBrandInfoId;
	}

	public void setCcBrandInfoId(String ccBrandInfoId) {
		this.ccBrandInfoId = ccBrandInfoId;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerNumberType() {
		return customerNumberType;
	}

	public void setCustomerNumberType(String customerNumberType) {
		this.customerNumberType = customerNumberType;
	}

	public String getCustomerAreaCode() {
		return customerAreaCode;
	}

	public void setCustomerAreaCode(String customerAreaCode) {
		this.customerAreaCode = customerAreaCode;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}

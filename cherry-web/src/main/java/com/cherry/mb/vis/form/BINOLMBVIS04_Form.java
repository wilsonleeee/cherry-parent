package com.cherry.mb.vis.form;

import java.util.List;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBVIS04_Form extends DataTable_BaseForm {

	/** 回访类型 */
	private String visitType;

	/** 开始日 */
	private String startDate;

	/** 结束日 */
	private String endDate;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 会员生日（月） */
	private String birthDayMonth;
	
	/** 会员生日（日） */
	private String birthDayDate;

	/** 柜台号 */
	private String counterCode;

	/** BA代号 */
	private String employeeCode;

	/** 回访结果 */
	private String visitResult;
	
	/** 回访执行时间  开始 */
	private String visitTimeStart;
	
	/** 回访执行时间  结束*/
	private String visitTimeEnd;
	
	/** 回访任务IDList */
	private List<String> visitTaskIdList;
	
	/** 回访类型名称 */
	private String visitTypeName;
	
	/** 字符编码 **/
	private String charset;

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
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

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getBirthDayMonth() {
		return birthDayMonth;
	}

	public void setBirthDayMonth(String birthDayMonth) {
		this.birthDayMonth = birthDayMonth;
	}

	public String getBirthDayDate() {
		return birthDayDate;
	}

	public void setBirthDayDate(String birthDayDate) {
		this.birthDayDate = birthDayDate;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getVisitResult() {
		return visitResult;
	}

	public void setVisitResult(String visitResult) {
		this.visitResult = visitResult;
	}

	public String getVisitTimeStart() {
		return visitTimeStart;
	}

	public void setVisitTimeStart(String visitTimeStart) {
		this.visitTimeStart = visitTimeStart;
	}

	public String getVisitTimeEnd() {
		return visitTimeEnd;
	}

	public void setVisitTimeEnd(String visitTimeEnd) {
		this.visitTimeEnd = visitTimeEnd;
	}

	public List<String> getVisitTaskIdList() {
		return visitTaskIdList;
	}

	public void setVisitTaskIdList(List<String> visitTaskIdList) {
		this.visitTaskIdList = visitTaskIdList;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}

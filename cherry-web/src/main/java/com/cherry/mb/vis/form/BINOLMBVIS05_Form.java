package com.cherry.mb.vis.form;


import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMBVIS05_Form extends DataTable_BaseForm {

	/** 回访类型 */
	private String visitType;

	/** 回访结果 */
	private String visitResult;

	/** 回访类型名称 */
	private String visitTypeName;

	/** 员工编号 */
	private String employeeCode;
	
	/** 会员回访ID*/
	private String visitTaskId;
	
	/**会员卡号*/
	private String memCode;
	
	/**第一次与最后一次的购买明细*/
	private List<Map<String,Object>> detail_list;
	
	/**开始时间*/
	private String startTime;
	/**截至时间*/
	private String endTime;
	
	/**会员ID*/
	private String memberID;
	
	/**会员信息*/
	private Map<String,Object> memberInfo;
	
	/**BAList*/
    private List<Map<String,Object>> counterBAList;
    
    /**员工ID*/
    private String tradeEmployeeID;
    
	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getVisitResult() {
		return visitResult;
	}

	public void setVisitResult(String visitResult) {
		this.visitResult = visitResult;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getVisitTaskId() {
		return visitTaskId;
	}

	public void setVisitTaskId(String visitTaskId) {
		this.visitTaskId = visitTaskId;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public List<Map<String, Object>> getDetail_list() {
		return detail_list;
	}

	public void setDetail_list(List<Map<String, Object>> detail_list) {
		this.detail_list = detail_list;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public Map<String, Object> getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(Map<String, Object> memberInfo) {
		this.memberInfo = memberInfo;
	}

	public List<Map<String, Object>> getCounterBAList() {
		return counterBAList;
	}

	public void setCounterBAList(List<Map<String, Object>> counterBAList) {
		this.counterBAList = counterBAList;
	}

	public String getTradeEmployeeID() {
		return tradeEmployeeID;
	}

	public void setTradeEmployeeID(String tradeEmployeeID) {
		this.tradeEmployeeID = tradeEmployeeID;
	}

	
}

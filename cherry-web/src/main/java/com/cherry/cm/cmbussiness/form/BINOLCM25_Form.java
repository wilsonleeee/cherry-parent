package com.cherry.cm.cmbussiness.form;

import java.util.List;
import java.util.Map;

public class BINOLCM25_Form {

	//工作流ID
	private long WorkFlowID;
	//当前步骤id
	private String currentStepId;
	//单据号
	private String BillID;
	//操作业务list
	private List<Map<String,Object>> opLoglist;
	//工作流信息list
	private String stepList;
	//当前流程的值
    private String currentOperateVal;

	// 下一步执行者信息
	private Map audMap;
	
	public long getWorkFlowID() {
		return WorkFlowID;
	}

	public void setWorkFlowID(long workFlowID) {
		WorkFlowID = workFlowID;
	}

	public String getBillID() {
		return BillID;
	}

	public void setBillID(String billID) {
		BillID = billID;
	}

	public List<Map<String, Object>> getOpLoglist() {
		return opLoglist;
	}

	public void setOpLoglist(List<Map<String, Object>> opLoglist) {
		this.opLoglist = opLoglist;
	}

	public String getCurrentStepId() {
		return currentStepId;
	}

	public void setCurrentStepId(String currentStepId) {
		this.currentStepId = currentStepId;
	}

	public String getStepList() {
		return stepList;
	}

	public void setStepList(String stepList) {
		this.stepList = stepList;
	}

	public String getCurrentOperateVal() {
		return currentOperateVal;
	}

	public void setCurrentOperateVal(String currentOperateVal) {
		this.currentOperateVal = currentOperateVal;
	}

	public Map getAudMap() {
		return audMap;
	}

	public void setAudMap(Map audMap) {
		this.audMap = audMap;
	}


}

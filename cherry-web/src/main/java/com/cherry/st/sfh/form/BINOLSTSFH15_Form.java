package com.cherry.st.sfh.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLSTSFH15_Form extends BINOLCM13_Form{
	/** 销售单号 */
	private String saleOrderNo;
	
	/** 客户部门ID */
	private String customerOrganizationId;
	
	/** 销售部门ID */
	private String organizationId;
	
	/** 销售日期（起始） */
	private String startDate;
	
	/** 销售日期（截止）*/
	private String endDate;
	
	/** 单据状态 */
	private String billState;
	
	/** 客户类型 */
	private String customerType;
	
	/** 导入批次 */
	private String importBatch;
	
	/** 汇总信息 */
    private Map<String, Object> sumInfo;
	
	/** 销售单据列表 */
	private List<Map<String, Object>> saleOrdersList;

	public String getSaleOrderNo() {
		return saleOrderNo;
	}

	public void setSaleOrderNo(String saleOrderNo) {
		this.saleOrderNo = saleOrderNo;
	}

	public String getCustomerOrganizationId() {
		return customerOrganizationId;
	}

	public void setCustomerOrganizationId(String customerOrganizationId) {
		this.customerOrganizationId = customerOrganizationId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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

	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public List<Map<String, Object>> getSaleOrdersList() {
		return saleOrdersList;
	}

	public void setSaleOrdersList(List<Map<String, Object>> saleOrdersList) {
		this.saleOrdersList = saleOrdersList;
	}

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

	public String getImportBatch() {
		return importBatch;
	}

	public void setImportBatch(String importBatch) {
		this.importBatch = importBatch;
	}
	
}

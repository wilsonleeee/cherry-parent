package com.cherry.pt.rps.form;

import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLPTRPS03_Form  extends BINOLCM13_Form{
		
	/** 共通条参数 */
	private String params;
	
	/** 产品发货单信息 */
	private String ProductInfo;
	
	/** 收货单号 */
	private String receiveNo;
	
	/** 关联单号 */
	private String relevanceNo;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 审核状态 */
	private String verifiedFlag;
	
	/**产品厂商ID*/
	private String prmVendorId;
	
	/**部门类型*/
	private String departType;
	
	/**收货部门*/
	private String departNameReceive;
    
	/**收货部门id*/
	private String organizationID;
	
	/**发货部门id*/
	private String organizationIDReceive;
	
	/** 产品收发货ID */
	private String deliverId;

	public String getDeliverId() {
		return deliverId;
	}

	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}

	/** 汇总信息 */
    private Map<String, Object> sumInfo;
    
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getProductInfo() {
		return ProductInfo;
	}

	public void setProductInfo(String productInfo) {
		ProductInfo = productInfo;
	}

	public String getReceiveNo() {
		return receiveNo;
	}

	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}

	public String getRelevanceNo() {
		return relevanceNo;
	}

	public void setRelevanceNo(String relevanceNo) {
		this.relevanceNo = relevanceNo;
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

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getDepartNameReceive() {
		return departNameReceive;
	}

	public void setDepartNameReceive(String departNameReceive) {
		this.departNameReceive = departNameReceive;
	}

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public String getOrganizationIDReceive() {
		return organizationIDReceive;
	}

	public void setOrganizationIDReceive(String organizationIDReceive) {
		this.organizationIDReceive = organizationIDReceive;
	}
	

}

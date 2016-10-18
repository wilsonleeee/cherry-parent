package com.cherry.cm.cmbussiness.dto;

public class AgentInfo extends BaseDTO {
	
	/** 组织ID **/
	private int organizationInfoId;
	/** 品牌ID **/
	private int brandInfoId;
	/** 微商手机号 **/
	private String agentMobile;
	/** 微信绑定OpenID **/
	private String agentOpenID;
	/** 微商姓名 **/
	private String agentName;
	/** 微商等级（部门类型） **/
	private String agentLevel;
	/** 省份 **/
	private String agentProvince;
	/** 城市 **/
	private String agentCity;
	/** 省份ID **/
	private String provinceId;
	/** 城市ID **/
	private String cityId;
	/** 上级手机号 **/
	private String superMobile;
	/** 原上级手机号 **/
	private String oldSuperMobile;
	/** 部门节点 **/
	private String orgPath;
	/** 员工节点 **/
	private String empPath;
	/** 部门上级节点 **/
	private String supOrgPath;
	/** 员工上级节点 **/
	private String supEmpPath;
	/** 员工ID **/
	private String employeeId;
	/** 部门ID **/
	private String departId;
	/** 部门名称 **/
	private String departName;

	public int getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getAgentMobile() {
		return agentMobile;
	}

	public void setAgentMobile(String agentMobile) {
		this.agentMobile = agentMobile;
	}

	public String getAgentOpenID() {
		return agentOpenID;
	}

	public void setAgentOpenID(String agentOpenID) {
		this.agentOpenID = agentOpenID;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getAgentProvince() {
		return agentProvince;
	}

	public void setAgentProvince(String agentProvince) {
		this.agentProvince = agentProvince;
	}

	public String getAgentCity() {
		return agentCity;
	}

	public void setAgentCity(String agentCity) {
		this.agentCity = agentCity;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getSuperMobile() {
		return superMobile;
	}

	public void setSuperMobile(String superMobile) {
		this.superMobile = superMobile;
	}

	public String getOldSuperMobile() {
		return oldSuperMobile;
	}

	public void setOldSuperMobile(String oldSuperMobile) {
		this.oldSuperMobile = oldSuperMobile;
	}

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	public String getEmpPath() {
		return empPath;
	}

	public void setEmpPath(String empPath) {
		this.empPath = empPath;
	}

	public String getSupOrgPath() {
		return supOrgPath;
	}

	public void setSupOrgPath(String supOrgPath) {
		this.supOrgPath = supOrgPath;
	}

	public String getSupEmpPath() {
		return supEmpPath;
	}

	public void setSupEmpPath(String supEmpPath) {
		this.supEmpPath = supEmpPath;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

}

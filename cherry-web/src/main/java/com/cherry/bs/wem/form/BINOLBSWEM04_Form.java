package com.cherry.bs.wem.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLBSWEM04_Form extends DataTable_BaseForm {
	
	/** 微商手机号 **/
	private String agentMobile;
	
	private String oldAgentMobile;
	
	/** 微商姓名 **/
	private String agentName;
	/** 省份 **/
	private String agentProvince;
	/** 城市 **/
	private String agentCity;
	/** 省份ID **/
	private String provinceId;
	/** 城市ID **/
	private String cityId;
	/** 微商等级（部门类型） **/
	private String agentLevel;
	/** 上级手机号 **/
	private String superMobile;
	/** 部门ID **/
	private String departId;
	/** 预留号**/
	private String reservedCode;
	/** 微店名称**/
	private String departName;
	/** 前后缀 **/
	private String pre_suffix;
	
	/** 微商帐户信息 */
	private List<Map<String, Object>> agentAccountInfoList;
	
	public String getAgentMobile() {
		return agentMobile;
	}
	public void setAgentMobile(String agentMobile) {
		this.agentMobile = agentMobile;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
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
	public String getAgentLevel() {
		return agentLevel;
	}
	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}
	public String getSuperMobile() {
		return superMobile;
	}
	public void setSuperMobile(String superMobile) {
		this.superMobile = superMobile;
	}
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getOldAgentMobile() {
		return oldAgentMobile;
	}
	public void setOldAgentMobile(String oldAgentMobile) {
		this.oldAgentMobile = oldAgentMobile;
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
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getReservedCode() {
		return reservedCode;
	}
	public void setReservedCode(String reservedCode) {
		this.reservedCode = reservedCode;
	}
	public String getPre_suffix() {
		return pre_suffix;
	}
	public void setPre_suffix(String pre_suffix) {
		this.pre_suffix = pre_suffix;
	}
	public List<Map<String, Object>> getAgentAccountInfoList() {
		return agentAccountInfoList;
	}
	public void setAgentAccountInfoList(List<Map<String, Object>> agentAccountInfoList) {
		this.agentAccountInfoList = agentAccountInfoList;
	}
	
}

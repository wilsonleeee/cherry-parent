/*  
 * @(#)BINOLCM21_Form.java    1.0 2011-9-16     
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.cm.cmbussiness.form;

public class BINOLCM21_Form {

	//所属品牌
	private String brandInfoId;
	//输入柜台查询字符串
	private String counterInfoStr;
	//输入产品查询字符串
	private String productInfoStr;
	//输入子品牌查询字符串
	private String originalBrandStr;
	//输入CodeType查询字符串
	private String codeType;
	// 柜台号
	private String counterCode;
	//输入部门查询字符串
	private String departInfoStr;
	//输入产品分类字符串
	private String prtCatInfoStr;
	//输入仓库查询字符串
	private String deportInfoStr;
	//输入员工查询字符串
	private String employeeInfoStr;
	//输入BA员工查询字符串
	private String baInfoStr;
	//输入区域查询字符串
	private String regionInfoStr;
	//输入逻辑仓库查询字符串
	private String logicInventoryInfoStr;
	// 通过的文本框的查询字符串
	private String textInfoStr;
	// 一、二代理商联动查询时的一级代理商参数
	private String parentValue;
	// 代理商类型
	private String resellerType;
	// 代理商所属省份
	private String provinceId;
	// 代理商所属城市
	private String cityId;
	// 条件类型
	private String conditionType;
	// 条件值
	private String conditionValue;
	//最多显示条数
	private String number;
	//希望被选中的部分
	private String selected;
	//往来单位(合作伙伴)
	private String partnerInfoStr;
	//输入渠道柜台查询字符
	private String channelInfoStr;
	//输入会员问题查询字符串
	private String issueNoKw;
	//标志
	private String flag;
	//只包含非櫃檯倉庫信息
	private String onlyNoneCounterDeport;
	//只包含櫃檯倉庫信息
	private String onlyCounterDeport;
	//是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
	private String privilegeFlag;
	/**查询BA信息时要过滤的特殊编码值**/
	private String filterBaCode;
	//会员ID
	private String memberInfoId;
	
	// 有效区分
	private String validFlag;
	//获取销售业务员范围（01为BA，ALL表示不限）
	private String categoryCode;
	
	/**是否包含部门*/
	private String includeDepart;
	
	/**是否只显示部门*/
    private String onlyDepart;
	
	public String getPartnerInfoStr() {
		return partnerInfoStr;
	}
	public void setPartnerInfoStr(String partnerInfoStr) {
		this.partnerInfoStr = partnerInfoStr;
	}
	public String getDeportInfoStr() {
		return deportInfoStr;
	}
	public void setDeportInfoStr(String deportInfoStr) {
		this.deportInfoStr = deportInfoStr;
	}
	public String getOnlyNoneCounterDeport() {
		return onlyNoneCounterDeport;
	}
	public void setOnlyNoneCounterDeport(String onlyNoneCounterDeport) {
		this.onlyNoneCounterDeport = onlyNoneCounterDeport;
	}
	public String getOnlyCounterDeport() {
		return onlyCounterDeport;
	}
	public void setOnlyCounterDeport(String onlyCounterDeport) {
		this.onlyCounterDeport = onlyCounterDeport;
	}
	public String getDepartInfoStr() {
		return departInfoStr;
	}
	public void setDepartInfoStr(String departInfoStr) {
		this.departInfoStr = departInfoStr;
	}
	public String getPrtCatInfoStr() {
		return prtCatInfoStr;
	}
	public void setPrtCatInfoStr(String prtCatInfoStr) {
		this.prtCatInfoStr = prtCatInfoStr;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getCounterInfoStr() {
		return counterInfoStr;
	}
	public void setCounterInfoStr(String counterInfoStr) {
		this.counterInfoStr = counterInfoStr;
	}
	public String getProductInfoStr() {
		return productInfoStr;
	}
	public void setProductInfoStr(String productInfoStr) {
		this.productInfoStr = productInfoStr;
	}
	public String getOriginalBrandStr() {
		return originalBrandStr;
	}
	public void setOriginalBrandStr(String originalBrandStr) {
		this.originalBrandStr = originalBrandStr;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	public String getEmployeeInfoStr() {
		return employeeInfoStr;
	}
	public void setEmployeeInfoStr(String employeeInfoStr) {
		this.employeeInfoStr = employeeInfoStr;
	}
	public String getBaInfoStr() {
		return baInfoStr;
	}
	public void setBaInfoStr(String baInfoStr) {
		this.baInfoStr = baInfoStr;
	}
	public String getPrivilegeFlag() {
		return privilegeFlag;
	}
	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}
	public String getRegionInfoStr() {
		return regionInfoStr;
	}
	public void setRegionInfoStr(String regionInfoStr) {
		this.regionInfoStr = regionInfoStr;
	}
	public String getLogicInventoryInfoStr() {
		return logicInventoryInfoStr;
	}
	public void setLogicInventoryInfoStr(String logicInventoryInfoStr) {
		this.logicInventoryInfoStr = logicInventoryInfoStr;
	}
	public String getChannelInfoStr() {
		return channelInfoStr;
	}
	public void setChannelInfoStr(String channelInfoStr) {
		this.channelInfoStr = channelInfoStr;
	}
	public String getIssueNoKw() {
		return issueNoKw;
	}
	public void setIssueNoKw(String issueNoKw) {
		this.issueNoKw = issueNoKw;
	}
	public String getMemberInfoId() {
		return memberInfoId;
	}
	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}	
	
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getFilterBaCode() {
		return filterBaCode;
	}
	public void setFilterBaCode(String filterBaCode) {
		this.filterBaCode = filterBaCode;
	}
	public String getTextInfoStr() {
		return textInfoStr;
	}
	public void setTextInfoStr(String textInfoStr) {
		this.textInfoStr = textInfoStr;
	}
	public String getParentValue() {
		return parentValue;
	}
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
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
	public String getConditionType() {
		return conditionType;
	}
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	public String getResellerType() {
		return resellerType;
	}
	public void setResellerType(String resellerType) {
		this.resellerType = resellerType;
	}
    public String getIncludeDepart() {
        return includeDepart;
    }
    public void setIncludeDepart(String includeDepart) {
        this.includeDepart = includeDepart;
    }
    public String getOnlyDepart() {
        return onlyDepart;
    }
    public void setOnlyDepart(String onlyDepart) {
        this.onlyDepart = onlyDepart;
    }
	
}

/*  
 * @(#)BINOLCM02_Form.java     1.0 2011/05/31      
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 促销产品弹出table共通Form
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM02_Form extends DataTable_BaseForm{
	
	/** 参数 */
	private String param;
	
	/** 参数2 */
	private String param1;
	
	/** 参数2 */
	private String param2;
	
	/** 结果List */
	private List resultList;
	
	/** 部门id*/
	private String organizationID;
	
	/** 柜台List */
	private List<Map<String, Object>> counterInfoList;
	
	/** 子品牌List */
	private List<Map<String, Object>> origBrandList;
	
	/** 柜台ID */
	private String counterInfoId;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 厂商List */
	private List factoryList;
	
	/** 生产厂商ID */
	private String[] manuFactId;
	
	/** 部门List */
	private List departInfoList;
	
	/** 员工ID */
	private String[] employeeId;
	
	/** 可BOM产品区分 */
	private String isBOM;
	
	/** 员工List */
	private List employeeList;
	
	/** 人员List */
	private List userList;
	
	/** 部门List */
	private List departList;
	
	/** 活动List */
	private List campaignList;
	
	/** 经销商List */
	private List resellerList;
	
	/** 价格种类*/
	private String priceType;
	
	/** 促销品过期Flag*/
	private String prmExpiredFlag;
	
	/** 单复选类型 */
	private String checkType;
	
	/** 部门类型 */
	private String[] departType;
	
	/** 部门ID */
	private String organizationId;
	
	/** 管理库存 */
	private String isStock;
	
	/** 部门级别标志 0：查询同级部门 1：查询下级部门(不包括自己)2:查询下级部门（包括自己），为空的场合默认查询下级部门 */
	private String levelFlg;
	
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlg;
	
	/** 大小分类区分1：大分类  2：小分类 */
	private String teminalFlag;
	
	/** 业务类型，加权限查询时用 */
	private String businessType;
	
	/** 促销产品类别 */
	private String promotionCateCD;
	
	/** 考核问卷List */
	private List checkPaperList;
	
	/**带有测试部门和正是部门区分**/
	private String testTypeFlg;
	
	/**测试区分*/
	private String testType;
	
	/**套装折扣和积分兑礼单复选类型*/
	private String radioFlag;
	
	/** 老模式区分*/
	private boolean oldModeFlag;
	
	/** 可否用于积分兑换 */
	private String isExchanged;
	
	/** 产品分类List */
	private List<Map<String, Object>> prtCategoryList;
	
	/** 有效区分(加pop可以避免其他有效区分表单污染) */
	private String popValidFlag;
	
	/** 需要剔除的产品/促销品 */
	private String ignorePrtPrmVendorID;
	
	/** 需要剔除产品的产品方案ID */
	private String ignoreSoluId;
	
	/** 需要剔除产品的产品功能开启时间主表ID */
	private String ignorePrtFunId;

	/** 岗位类别 */
	private String categoryCode;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 用来定位的查询值 */
	private String locationPosition;
	
	/** 柜台选择模式(1：按区域，2：按渠道) */
	private String selMode;
	
	/** 大区ID */
	private String channelRegionId;
	
	/** 柜台有效区分 */
	private String popCouValidFlag;
	
	 /** 领用柜台名称模糊查询*/
	 private String counterStr;
	 
	 /** 活动类型（0：会员活动，1：促销活动） */
	 private String campaignMode;
	 
	 /** 活动对象批次Code */
	 private String searchCode;
	 
	 /** 活动对象批次名称 */
	 private String recordName;
	 
	 /** 活动对象数 */
	 private String  recordCount;
	 
	 /** 活动对象类型 */
	 private String customerType;

	/** 销售信息List */
	private List saleRecordList;
	
	/** 会员ID */
	private String memberInfoId;
	
	/** 会员俱乐部ID */
	private String memberClubId;

	/** 活动Coupon批次Code */
	 private String batchCode;
	 
	 /** 柜台弹出框中是否显示包含和排除选项 */ 
	private String hasSelExclusiveFlag;
	
	 /** 是否小店云场景模式  */ 
	private String isPosCloud;
	
	private String userId;
	
	/** 是否云POS库存业务*/
	private String cloudPosFlag;
	
	/** 是否自选有效区分 */
	private String optionalValidFlag;
	

	/** 是否存在POS机*/
	private String isPosFlag;

	/** 产品名称*/
	private String nameTotal;
	
	/** 品牌*/
	private String originalBrand;
	
	/** JSON数据*/
	private String queStr;
	
	/** 查询分类信息List(大分类）*/
	private List<Map<String, Object>> productCategoryList;
	
	/**产品分类ID（大分类） */
	private  String productCategoryId;
	
	private  String productCategoryTemp;
	
	/**是否新品 */
	private String isNewProductFlag;
	 
	/** 品牌List */
	private List<Map<String, Object>> brandCodeListSrh; 
	
	/** 分类List */
	private List<Map<String, Object>> sortListSrh;
	
	/** 浓妆淡抹产品弹出框List */
	private List<Map<String, Object>> popProductInfoTwoList;
	
	/** 当前是第几页 */
	private int pageNo;
	
	/** 当前是每页多少条 */
	private int pageSize;

	/** 最大盘点数 */
	private int maxCount;
	
	public List<Map<String, Object>> getPopProductInfoTwoList() {
		return popProductInfoTwoList;
	}

	public void setPopProductInfoTwoList(
			List<Map<String, Object>> popProductInfoTwoList) {
		this.popProductInfoTwoList = popProductInfoTwoList;
	}

	public List<Map<String, Object>> getBrandCodeListSrh() {
		return brandCodeListSrh;
	}

	public void setBrandCodeListSrh(List<Map<String, Object>> brandCodeListSrh) {
		this.brandCodeListSrh = brandCodeListSrh;
	}

	public List<Map<String, Object>> getSortListSrh() {
		return sortListSrh;
	}

	public void setSortListSrh(List<Map<String, Object>> sortListSrh) {
		this.sortListSrh = sortListSrh;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	/** 会员卡号*/
	private String memCode;
	
	/** 会员名称*/
	private String memName;
	
	/** 会员名称*/
	private String mobilePhone;
	
	/** 会员名称*/
	private String departName;
	
	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getTestTypeFlg() {
		return testTypeFlg;
	}

	public void setTestTypeFlg(String testTypeFlg) {
		this.testTypeFlg = testTypeFlg;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getPrmExpiredFlag() {
		return prmExpiredFlag;
	}

	public void setPrmExpiredFlag(String prmExpiredFlag) {
		this.prmExpiredFlag = prmExpiredFlag;
	}

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public List<Map<String, Object>> getCounterInfoList() {
		return counterInfoList;
	}

	public void setCounterInfoList(List<Map<String, Object>> counterInfoList) {
		this.counterInfoList = counterInfoList;
	}
	
	public List<Map<String, Object>> getOrigBrandList() {
		return origBrandList;
	}

	public void setOrigBrandList(List<Map<String, Object>> origBrandList) {
		this.origBrandList = origBrandList;
	}
	
	public String getCounterInfoId() {
		return counterInfoId;
	}

	public void setCounterInfoId(String counterInfoId) {
		this.counterInfoId = counterInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List getFactoryList() {
		return factoryList;
	}

	public void setFactoryList(List factoryList) {
		this.factoryList = factoryList;
	}

	public String[] getManuFactId() {
		return manuFactId;
	}

	public void setManuFactId(String[] manuFactId) {
		this.manuFactId = manuFactId;
	}

	public List getDepartInfoList() {
		return departInfoList;
	}

	public void setDepartInfoList(List departInfoList) {
		this.departInfoList = departInfoList;
	}

	public void setEmployeeId(String[] employeeId) {
		this.employeeId = employeeId;
	}

	public String[] getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeList(List employeeList) {
		this.employeeList = employeeList;
	}

	public List getEmployeeList() {
		return employeeList;
	}

	public List getCampaignList() {
		return campaignList;
	}

	public void setCampaignList(List campaignList) {
		this.campaignList = campaignList;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getLevelFlg() {
		return levelFlg;
	}

	public void setLevelFlg(String levelFlg) {
		this.levelFlg = levelFlg;
	}

	public String getPrivilegeFlg() {
		return privilegeFlg;
	}

	public void setPrivilegeFlg(String privilegeFlg) {
		this.privilegeFlg = privilegeFlg;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

    public String[] getDepartType() {
        return departType;
    }

    public void setDepartType(String[] departType) {
        this.departType = departType;
    }

	public String getIsBOM() {
		return isBOM;
	}

	public void setIsBOM(String isBOM) {
		this.isBOM = isBOM;
	}

	public String getPromotionCateCD() {
		return promotionCateCD;
	}

	public void setPromotionCateCD(String promotionCateCD) {
		this.promotionCateCD = promotionCateCD;
	}

	public List getCheckPaperList() {
		return checkPaperList;
	}

	public void setCheckPaperList(List checkPaperList) {
		this.checkPaperList = checkPaperList;
	}
	public String getRadioFlag() {
		return radioFlag;
	}

	public void setRadioFlag(String radioFlag) {
		this.radioFlag = radioFlag;
	}
	public String getTeminalFlag() {
		return teminalFlag;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	
	public void setTeminalFlag(String teminalFlag) {
		this.teminalFlag = teminalFlag;
	}

	public boolean isOldModeFlag() {
		return oldModeFlag;
	}

	public void setOldModeFlag(boolean oldModeFlag) {
		this.oldModeFlag = oldModeFlag;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getIsStock() {
		return isStock;
	}

	public void setIsStock(String isStock) {
		this.isStock = isStock;
	}
	
	public String getIsExchanged() {
		return isExchanged;
	}

	public void setIsExchanged(String isExchanged) {
		this.isExchanged = isExchanged;
	}

	public List<Map<String, Object>> getPrtCategoryList() {
		return prtCategoryList;
	}

	public void setPrtCategoryList(List<Map<String, Object>> prtCategoryList) {
		this.prtCategoryList = prtCategoryList;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	public String getPopValidFlag() {
		return popValidFlag;
	}

	public void setPopValidFlag(String popValidFlag) {
		this.popValidFlag = popValidFlag;
	}
	
	public String getIgnorePrtPrmVendorID() {
		return ignorePrtPrmVendorID;
	}

	public void setIgnorePrtPrmVendorID(String ignorePrtPrmVendorID) {
		this.ignorePrtPrmVendorID = ignorePrtPrmVendorID;
	}
	
	public String getIgnoreSoluId() {
		return ignoreSoluId;
	}

	public void setIgnoreSoluId(String ignoreSoluId) {
		this.ignoreSoluId = ignoreSoluId;
	}
	
	public String getIgnorePrtFunId() {
		return ignorePrtFunId;
	}

	public void setIgnorePrtFunId(String ignorePrtFunId) {
		this.ignorePrtFunId = ignorePrtFunId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getLocationPosition() {
		return locationPosition;
	}

	public void setLocationPosition(String locationPosition) {
		this.locationPosition = locationPosition;
	}

	public String getSelMode() {
		return selMode;
	}

	public void setSelMode(String selMode) {
		this.selMode = selMode;
	}

	public String getChannelRegionId() {
		return channelRegionId;
	}

	public void setChannelRegionId(String channelRegionId) {
		this.channelRegionId = channelRegionId;
	}

	public String getPopCouValidFlag() {
		return popCouValidFlag;
	}

	public void setPopCouValidFlag(String popCouValidFlag) {
		this.popCouValidFlag = popCouValidFlag;
	}

	public String getCounterStr() {
		return counterStr;
	}

	public void setCounterStr(String counterStr) {
		this.counterStr = counterStr;
	}

	public String getCampaignMode() {
		return campaignMode;
	}

	public void setCampaignMode(String campaignMode) {
		this.campaignMode = campaignMode;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public List getSaleRecordList() {
		return saleRecordList;
	}

	public void setSaleRecordList(List saleRecordList) {
		this.saleRecordList = saleRecordList;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public List getUserList() {
		return userList;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}

	public List getDepartList() {
		return departList;
	}

	public void setDepartList(List departList) {
		this.departList = departList;
	}

	public String getHasSelExclusiveFlag() {
		return hasSelExclusiveFlag;
	}

	public void setHasSelExclusiveFlag(String hasSelExclusiveFlag) {
		this.hasSelExclusiveFlag = hasSelExclusiveFlag;
	}
	
	public String getIsPosCloud() {
		return isPosCloud;
	}

	public void setIsPosCloud(String isPosCloud) {
		this.isPosCloud = isPosCloud;
	}

	public List getResellerList() {
		return resellerList;
	}

	public void setResellerList(List resellerList) {
		this.resellerList = resellerList;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCloudPosFlag() {
		return cloudPosFlag;
	}

	public void setCloudPosFlag(String cloudPosFlag) {
		this.cloudPosFlag = cloudPosFlag;
	}
	
	public String getOptionalValidFlag() {
		return optionalValidFlag;
	}

	public void setOptionalValidFlag(String optionalValidFlag) {
		this.optionalValidFlag = optionalValidFlag;
	}


	public String getIsPosFlag() {
		return isPosFlag;
	}

	public void setIsPosFlag(String isPosFlag) {
		this.isPosFlag = isPosFlag;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getOriginalBrand() {
		return originalBrand;
	}

	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}

	public String getQueStr() {
		return queStr;
	}

	public void setQueStr(String queStr) {
		this.queStr = queStr;
	}

	public List<Map<String, Object>> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<Map<String, Object>> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getIsNewProductFlag() {
		return isNewProductFlag;
	}

	public void setIsNewProductFlag(String isNewProductFlag) {
		this.isNewProductFlag = isNewProductFlag;
	}

	public String getProductCategoryTemp() {
		return productCategoryTemp;
	}

	public void setProductCategoryTemp(String productCategoryTemp) {
		this.productCategoryTemp = productCategoryTemp;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
}

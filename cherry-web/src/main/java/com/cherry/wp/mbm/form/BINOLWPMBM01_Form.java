package com.cherry.wp.mbm.form;

import java.util.List;

import com.cherry.cm.dto.ExtendPropertyDto;
import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员管理Form
 * 
 * @author WangCT
 * @version 1.0 2014/08/15
 */
public class BINOLWPMBM01_Form extends DataTable_BaseForm {
	
	/** 会员ID **/
	private String memberInfoId;
	
	/** 变更前会员等级 **/
	private String oldMemberLevel;
	
	/** 变更前会员等级名称 **/
	private String oldLevelName;
	
	/** 修改时间 */
	private String modifyTime;
	
	/** 修改次数 */
	private String modifyCount;
	
	/** 假登录会员ID */
	private String newMemberInfoId;
	
	/** 假登录会员地址ID */
	private String newAddressInfoId;
	
	/** 版本号 */
	private String version;
	
	/** 会员老卡号 */
	private String memCodeOld;
	
	/** 会员卡回目 */
	private String cardCount;
	
	/** 会员卡号 **/
	private String memCode;
	
	/** 发卡日期 **/
	private String joinDate;
	
	/** 发卡柜台 ID **/
	private String organizationId;
	
	/** 发卡柜台 CODE **/
	private String organizationCode;
	
	/** 发卡柜台测试区分 **/
	private String counterKind;
	
	/** 发卡BA ID **/
	private String employeeId;
	
	/** 发卡BA CODE **/
	private String employeeCode;
	
	/** 会员姓名 **/
	private String memName;
	
	/** 会员手机 **/
	private String mobilePhone;
	
	/** 会员原手机 **/
	private String mobilePhoneOld;
	
	/** 会员电话 **/
	private String telephone;
	
	/** 会员邮箱 **/
	private String email;
	
	/** 会员生日 **/
	private String birth;
	
	/** 变更前生日 **/
	private String birthOld;
	
	/** 会员性别 **/
	private String gender;
	
	/** 会员身份证 **/
	private String identityCard;
	
	/** 微博号 **/
	private String blogId;
	
	/** 微信号 **/
	private String messageId;
	
	/** 职业 **/
	private String profession;
	
	/** 婚姻状况 **/
	private String maritalStatus;
	
	/** 最佳联络方式 **/
	private List<String> connectTime;
	
	/** 推荐会员卡号 **/
	private String referrer;
	
	/** 变更前推荐会员ID **/
	private String referrerIdOld;
	
	/** 会员类型 **/
	private String memType;
	
	/** 是否接收通知 **/
	private String isReceiveMsg;
	
	/** 激活状态 **/
	private String active;
	
	/** 原激活状态 **/
	private String activeOld;
	
	/** 初始累计金额 **/
	private String initTotalAmount;
	
	/** 变更前初始累计金额 **/
	private String initTotalAmountOld;
	
	/** 会员有效无效状态 **/
	private String status;
	
	/** 入会途径 **/
	private String channelCode;
	
	/** 备注1 **/
	private String memo1;
	
	/** 备注2 **/
	private String memo2;
	
	/** 省ID **/
	private String provinceId;
	
	/** 城市ID **/
	private String cityId;
	
	/** 城市ID **/
	private String countyId;
	
	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	/** 地址 **/
	private String address;
	
	/** 邮编 **/
	private String postcode;
	
	/** 地址ID **/
	private String addressInfoId;
	
	/** 会员扩展属性 */
	private List<ExtendPropertyDto> propertyInfoList;
	
	/** 会员卡号（查询条件） **/
	private String memCodeQ;
	
	/** 会员姓名（查询条件） **/
	private String memNameQ;
	
	/** 会员手机（查询条件） **/
	private String mobilePhoneQ;
	
	/** 会员生日（月）（查询条件） */
	private String birthDayMonthQ;
	
	/** 会员生日（日）（查询条件） */
	private String birthDayDateQ;
	
	/** 会员搜索条件(可以是手机号或者卡号) */
	private String searchStr;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 销售时间最小限制 */
	private String saleTimeStart;
	
	/** 销售时间最大限制 */
	private String saleTimeEnd;
	
	/** 单据号 */
	private String billCode;
	
	/** 交易类型 */
	private String saleType;
	
	/** 销售ID */
	private String saleRecordId;
	
	/** 显示模式（0：单据模式，1：明细模式） */
	private String displayFlag;
	
	/** 系统配置项是否显示唯一码 */
    private String sysConfigShowUniqueCode;
    
    /** 会员折线图横坐标刻度数 */
    private int tickLength;
    
    /** 积分变化日期最小限制 */
	private String changeDateStart;
	
	/** 积分变化日期最大限制 */
	private String changeDateEnd;
	
	/** 关联退货单号 */
	private String relevantSRCode;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 柜台号 */
	private String departCode;
	
	/** 匹配的规则ID */
	private String subCampaignId;
	
	/** 积分ID */
	private String pointChangeId;
	
	/** 积分范围上限 */
	private String memPointStart;
	
	/** 积分范围下限 */
	private String memPointEnd;
	
	/** 历史交易柜台开启 */
	private String openCounter;
	
	/** 卡号手机同步是否开启*/
	private String cardMobileSyn;

	/**生日是否为必填*/
	private String birthFlag;


	/**生日是否允许修改*/
	private String isAllowUpdate;

	public String getCardMobileSyn() {
		return cardMobileSyn;
	}

	public void setCardMobileSyn(String cardMobileSyn) {
		this.cardMobileSyn = cardMobileSyn;
	}

	public String getOpenCounter() {
		return openCounter;
	}

	public void setOpenCounter(String openCounter) {
		this.openCounter = openCounter;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getOldMemberLevel() {
		return oldMemberLevel;
	}

	public void setOldMemberLevel(String oldMemberLevel) {
		this.oldMemberLevel = oldMemberLevel;
	}

	public String getOldLevelName() {
		return oldLevelName;
	}

	public void setOldLevelName(String oldLevelName) {
		this.oldLevelName = oldLevelName;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getNewMemberInfoId() {
		return newMemberInfoId;
	}

	public void setNewMemberInfoId(String newMemberInfoId) {
		this.newMemberInfoId = newMemberInfoId;
	}

	public String getNewAddressInfoId() {
		return newAddressInfoId;
	}

	public void setNewAddressInfoId(String newAddressInfoId) {
		this.newAddressInfoId = newAddressInfoId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMemCodeOld() {
		return memCodeOld;
	}

	public void setMemCodeOld(String memCodeOld) {
		this.memCodeOld = memCodeOld;
	}

	public String getCardCount() {
		return cardCount;
	}

	public void setCardCount(String cardCount) {
		this.cardCount = cardCount;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getCounterKind() {
		return counterKind;
	}

	public void setCounterKind(String counterKind) {
		this.counterKind = counterKind;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public String getMobilePhoneOld() {
		return mobilePhoneOld;
	}

	public void setMobilePhoneOld(String mobilePhoneOld) {
		this.mobilePhoneOld = mobilePhoneOld;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getBirthOld() {
		return birthOld;
	}

	public void setBirthOld(String birthOld) {
		this.birthOld = birthOld;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public List<String> getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(List<String> connectTime) {
		this.connectTime = connectTime;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getReferrerIdOld() {
		return referrerIdOld;
	}

	public void setReferrerIdOld(String referrerIdOld) {
		this.referrerIdOld = referrerIdOld;
	}

	public String getMemType() {
		return memType;
	}

	public void setMemType(String memType) {
		this.memType = memType;
	}

	public String getIsReceiveMsg() {
		return isReceiveMsg;
	}

	public void setIsReceiveMsg(String isReceiveMsg) {
		this.isReceiveMsg = isReceiveMsg;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getActiveOld() {
		return activeOld;
	}

	public void setActiveOld(String activeOld) {
		this.activeOld = activeOld;
	}

	public String getInitTotalAmount() {
		return initTotalAmount;
	}

	public void setInitTotalAmount(String initTotalAmount) {
		this.initTotalAmount = initTotalAmount;
	}

	public String getInitTotalAmountOld() {
		return initTotalAmountOld;
	}

	public void setInitTotalAmountOld(String initTotalAmountOld) {
		this.initTotalAmountOld = initTotalAmountOld;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getMemo1() {
		return memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddressInfoId() {
		return addressInfoId;
	}

	public void setAddressInfoId(String addressInfoId) {
		this.addressInfoId = addressInfoId;
	}

	public List<ExtendPropertyDto> getPropertyInfoList() {
		return propertyInfoList;
	}

	public void setPropertyInfoList(List<ExtendPropertyDto> propertyInfoList) {
		this.propertyInfoList = propertyInfoList;
	}

	public String getMemCodeQ() {
		return memCodeQ;
	}

	public void setMemCodeQ(String memCodeQ) {
		this.memCodeQ = memCodeQ;
	}

	public String getMemNameQ() {
		return memNameQ;
	}

	public void setMemNameQ(String memNameQ) {
		this.memNameQ = memNameQ;
	}

	public String getMobilePhoneQ() {
		return mobilePhoneQ;
	}

	public void setMobilePhoneQ(String mobilePhoneQ) {
		this.mobilePhoneQ = mobilePhoneQ;
	}

	public String getBirthDayMonthQ() {
		return birthDayMonthQ;
	}

	public void setBirthDayMonthQ(String birthDayMonthQ) {
		this.birthDayMonthQ = birthDayMonthQ;
	}

	public String getBirthDayDateQ() {
		return birthDayDateQ;
	}

	public void setBirthDayDateQ(String birthDayDateQ) {
		this.birthDayDateQ = birthDayDateQ;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getSaleTimeStart() {
		return saleTimeStart;
	}

	public void setSaleTimeStart(String saleTimeStart) {
		this.saleTimeStart = saleTimeStart;
	}

	public String getSaleTimeEnd() {
		return saleTimeEnd;
	}

	public void setSaleTimeEnd(String saleTimeEnd) {
		this.saleTimeEnd = saleTimeEnd;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getSaleRecordId() {
		return saleRecordId;
	}

	public void setSaleRecordId(String saleRecordId) {
		this.saleRecordId = saleRecordId;
	}

	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getSysConfigShowUniqueCode() {
		return sysConfigShowUniqueCode;
	}

	public void setSysConfigShowUniqueCode(String sysConfigShowUniqueCode) {
		this.sysConfigShowUniqueCode = sysConfigShowUniqueCode;
	}

	public int getTickLength() {
		return tickLength;
	}

	public void setTickLength(int tickLength) {
		this.tickLength = tickLength;
	}

	public String getChangeDateStart() {
		return changeDateStart;
	}

	public void setChangeDateStart(String changeDateStart) {
		this.changeDateStart = changeDateStart;
	}

	public String getChangeDateEnd() {
		return changeDateEnd;
	}

	public void setChangeDateEnd(String changeDateEnd) {
		this.changeDateEnd = changeDateEnd;
	}

	public String getRelevantSRCode() {
		return relevantSRCode;
	}

	public void setRelevantSRCode(String relevantSRCode) {
		this.relevantSRCode = relevantSRCode;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getSubCampaignId() {
		return subCampaignId;
	}

	public void setSubCampaignId(String subCampaignId) {
		this.subCampaignId = subCampaignId;
	}

	public String getPointChangeId() {
		return pointChangeId;
	}

	public void setPointChangeId(String pointChangeId) {
		this.pointChangeId = pointChangeId;
	}

	public String getMemPointStart() {
		return memPointStart;
	}

	public void setMemPointStart(String memPointStart) {
		this.memPointStart = memPointStart;
	}

	public String getMemPointEnd() {
		return memPointEnd;
	}

	public void setMemPointEnd(String memPointEnd) {
		this.memPointEnd = memPointEnd;
	}

	public String getBirthFlag() {
		return birthFlag;
	}

	public void setBirthFlag(String birthFlag) {
		this.birthFlag = birthFlag;
	}

	public String getIsAllowUpdate() {
		return isAllowUpdate;
	}

	public void setIsAllowUpdate(String isAllowUpdate) {
		this.isAllowUpdate = isAllowUpdate;
	}
}

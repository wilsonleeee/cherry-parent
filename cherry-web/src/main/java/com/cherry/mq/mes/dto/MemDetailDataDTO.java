/**		
 * @(#)DetailDataDTO.java     1.0 2011/05/23		
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
package com.cherry.mq.mes.dto;

/**
 * 会员明细数据行映射DTO
 * 
 * @author huzude
 * 
 */
public class MemDetailDataDTO {

	/** 会员号 */
	private String MemberCode;
	/** 会员名字 */
	private String MemName;
	/** 会员电话 */
	private String MemPhone;
	/** 会员手机 */
	private String MemMobile;
	/** 会员性别 */
	private String MemSex;
	/** 会员省份 */
	private String MemProvince;
	/** 会员城市 */
	private String MemCity;
	/** 会员区县 */
	private String MemCounty;
	/** 会员地址 */
	private String MemAddress;
	/** 会员邮编 */
	private String MemPostcode;
	/** 会员生日 */
	private String MemBirthday;
	/** 会员邮箱 */
	private String MemMail;
	/** 会员开卡时间 */
	private String MemGranddate;
	/** Ba卡号 */
	private String BAcode;
	/** 柜台号 */
	private String Countercode;
	/** 新卡号 */
	private String NewMemcode;
	/** 会员换卡时间 */
	private String MemChangeTime;
	/** 会员等级 */
	private String MemLevel;
	/** 是否更改生日的标志，当此处为T时，不更改生日 */
	private String ModifyBirthdayFlag;
	/** 入会时间 */
	private String JoinTime;
	/** 推荐会员 */
	private String Referrer;
	/** 会员年龄获取方式 */
	private String MemAgeGetMethod;
	/** 版本号 */
	private String Version;
	
	/** 会员备注信息1 */
	private String Memo1;
	
	/** 是否激活 */
	private String Active;
	
	/** 激活时间 */
	private String ActiveDate;
	
	/** 会员等级 */
	private String MemLevelExt;
	
	/** 等级有效开始日期 */
	private String LevelStartDateExt;
	
	/** 等级有效结束日期 */
	private String LevelEndDateExt;
	
	/** 会员密码 */
	private String MemberPassword;
	
	/** 会员备注信息2 */
	private String Memo2;
	
	/**激活途径*/
	private String ActiveChannel;
	
	/**天猫账号*/
	private String TmallAccount;

	/** 微信账号 **/
	private String WechatAccount ;

	/** 职业 **/
	private String Profession ;

	/** 连接时间 **/
	private String ConnectTime ;

	/** 收入 **/
	private String Income ;

	/** 回访方式 **/
	private String ReturnVisit ;

	/** 肤质 **/
	private String SkinType;

	public String getMemberCode() {
		return MemberCode;
	}

	public void setMemberCode(String memberCode) {
		MemberCode = memberCode;
	}

	public String getMemName() {
		return MemName;
	}

	public void setMemName(String memName) {
		MemName = memName;
	}

	public String getMemPhone() {
		return MemPhone;
	}

	public void setMemPhone(String memPhone) {
		MemPhone = memPhone;
	}

	public String getMemSex() {
		return MemSex;
	}

	public void setMemSex(String memSex) {
		MemSex = memSex;
	}

	public String getMemProvince() {
		return MemProvince;
	}

	public void setMemProvince(String memProvince) {
		MemProvince = memProvince;
	}

	public String getMemCity() {
		return MemCity;
	}

	public void setMemCity(String memCity) {
		MemCity = memCity;
	}

	public String getMemCounty() {
		return MemCounty;
	}

	public void setMemCounty(String memCounty) {
		MemCounty = memCounty;
	}

	public String getMemAddress() {
		return MemAddress;
	}

	public void setMemAddress(String memAddress) {
		MemAddress = memAddress;
	}

	public String getMemPostcode() {
		return MemPostcode;
	}

	public void setMemPostcode(String memPostcode) {
		MemPostcode = memPostcode;
	}

	public String getMemBirthday() {
		return MemBirthday;
	}

	public void setMemBirthday(String memBirthday) {
		MemBirthday = memBirthday;
	}

	public String getMemMail() {
		return MemMail;
	}

	public void setMemMail(String memMail) {
		MemMail = memMail;
	}

	public String getMemGranddate() {
		return MemGranddate;
	}

	public void setMemGranddate(String memGranddate) {
		MemGranddate = memGranddate;
	}

	public String getBAcode() {
		return BAcode;
	}

	public void setBAcode(String bAcode) {
		BAcode = bAcode;
	}

	public String getCountercode() {
		return Countercode;
	}

	public void setCountercode(String countercode) {
		Countercode = countercode;
	}

	public String getNewMemcode() {
		return NewMemcode;
	}

	public void setNewMemcode(String newMemcode) {
		NewMemcode = newMemcode;
	}

	public String getMemChangeTime() {
		return MemChangeTime;
	}

	public void setMemChangeTime(String memChangeTime) {
		MemChangeTime = memChangeTime;
	}

	public String getMemLevel() {
		return MemLevel;
	}

	public void setMemLevel(String memLevel) {
		MemLevel = memLevel;
	}

	public String getModifyBirthdayFlag() {
		return ModifyBirthdayFlag;
	}

	public void setModifyBirthdayFlag(String modifyBirthdayFlag) {
		ModifyBirthdayFlag = modifyBirthdayFlag;
	}

	public String getMemMobile() {
		return MemMobile;
	}

	public void setMemMobile(String memMobile) {
		MemMobile = memMobile;
	}

	public String getJoinTime() {
		return JoinTime;
	}

	public void setJoinTime(String joinTime) {
		JoinTime = joinTime;
	}

	public String getReferrer() {
		return Referrer;
	}

	public void setReferrer(String referrer) {
		Referrer = referrer;
	}

	public String getMemAgeGetMethod() {
		return MemAgeGetMethod;
	}

	public void setMemAgeGetMethod(String memAgeGetMethod) {
		MemAgeGetMethod = memAgeGetMethod;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

    public String getMemo1() {
        return Memo1;
    }

    public void setMemo1(String memo1) {
        Memo1 = memo1;
    }

	public String getActive() {
		return Active;
	}

	public void setActive(String active) {
		Active = active;
	}

	public String getActiveDate() {
		return ActiveDate;
	}

	public void setActiveDate(String activeDate) {
		ActiveDate = activeDate;
	}

	public String getMemLevelExt() {
		return MemLevelExt;
	}

	public void setMemLevelExt(String memLevelExt) {
		MemLevelExt = memLevelExt;
	}

	public String getLevelStartDateExt() {
		return LevelStartDateExt;
	}

	public void setLevelStartDateExt(String levelStartDateExt) {
		LevelStartDateExt = levelStartDateExt;
	}

	public String getLevelEndDateExt() {
		return LevelEndDateExt;
	}

	public void setLevelEndDateExt(String levelEndDateExt) {
		LevelEndDateExt = levelEndDateExt;
	}

	public String getMemberPassword() {
		return MemberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		MemberPassword = memberPassword;
	}

	public String getMemo2() {
		return Memo2;
	}

	public void setMemo2(String memo2) {
		Memo2 = memo2;
	}

    public String getActiveChannel() {
        return ActiveChannel;
    }

    public void setActiveChannel(String activeChannel) {
        ActiveChannel = activeChannel;
    }

    public String getTmallAccount() {
        return TmallAccount;
    }

    public void setTmallAccount(String tmallAccount) {
        TmallAccount = tmallAccount;
    }

	public String getWechatAccount() {
		return WechatAccount;
	}

	public void setWechatAccount(String wechatAccount) {
		WechatAccount = wechatAccount;
	}

	public String getProfession() {
		return Profession;
	}

	public void setProfession(String profession) {
		Profession = profession;
	}

	public String getConnectTime() {
		return ConnectTime;
	}

	public void setConnectTime(String connectTime) {
		ConnectTime = connectTime;
	}

	public String getIncome() {
		return Income;
	}

	public void setIncome(String income) {
		Income = income;
	}

	public String getReturnVisit() {
		return ReturnVisit;
	}

	public void setReturnVisit(String returnVisit) {
		ReturnVisit = returnVisit;
	}

	public String getSkinType() {
		return SkinType;
	}

	public void setSkinType(String skinType) {
		SkinType = skinType;
	}
}

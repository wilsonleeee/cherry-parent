package com.cherry.wp.sal.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL13_Form extends DataTable_BaseForm {
	// 卡号
	private String cardCode;
	// 活动信息列表
	private List<Map<String, Object>> rechargeDiscountList;
	// 业务类型
	private String businessType;
	// 等级
	private String cardLevel;
	// 可用余额
	private int amount;
//	// 充值金额
//	private float camount;
	// 实收金额
	private float realAmount;
	// 赠送金额
	private float giveAmount;
	// 备注
	private String memo;
	// 优惠次数或赠送金额
	private int giftAmount;
	// 卡的信息
	private List<Map<String, Object>> cardList;
	// 服务类型
	private List<Map<String, Object>> serverList;
	// 服务类型
	private String serviceJsonList;
	// 单据号
	private String billCode;
	// 预留手机号
	private String mobilePhone;
	// 储值卡密码
	private String password;
	// 会员卡号
	private String memCode;
	// 活动名称
	private String subDiscountName;
	// 查询时间
	private String fromDate;
	private String toDate;
	// 撤销时的验证字段
	private String verificationCode;
	// 验证方式
	private String verificationType;
	// 会员ID
	private String memberInfoId;
	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public List<Map<String, Object>> getRechargeDiscountList() {
		return rechargeDiscountList;
	}

	public void setRechargeDiscountList(
			List<Map<String, Object>> rechargeDiscountList) {
		this.rechargeDiscountList = rechargeDiscountList;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(String cardLevel) {
		this.cardLevel = cardLevel;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getAmount() {
		return amount;
	}

//	public float getCamount() {
//		return camount;
//	}
//
//	public void setCamount(float camount) {
//		this.camount = camount;
//	}

	public float getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(float realAmount) {
		this.realAmount = realAmount;
	}

	public float getGiveAmount() {
		return giveAmount;
	}

	public void setGiveAmount(float giveAmount) {
		this.giveAmount = giveAmount;
	}

	public float getGiftAmount() {
		return giftAmount;
	}

	public List<Map<String, Object>> getServerList() {
		return serverList;
	}

	public List<Map<String, Object>> getCardList() {
		return cardList;
	}

	public void setCardList(List<Map<String, Object>> cardList) {
		this.cardList = cardList;
	}

	public void setServerList(List<Map<String, Object>> serverList) {
		this.serverList = serverList;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getSubDiscountName() {
		return subDiscountName;
	}

	public void setSubDiscountName(String subDiscountName) {
		this.subDiscountName = subDiscountName;
	}

	public String getServiceJsonList() {
		return serviceJsonList;
	}

	public void setServiceJsonList(String serviceJsonList) {
		this.serviceJsonList = serviceJsonList;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setGiftAmount(int giftAmount) {
		this.giftAmount = giftAmount;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

}

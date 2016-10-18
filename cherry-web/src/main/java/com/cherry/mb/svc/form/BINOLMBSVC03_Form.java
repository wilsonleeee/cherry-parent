package com.cherry.mb.svc.form;

import java.util.List;
import java.util.Map;

public class BINOLMBSVC03_Form {
		// 卡号
		private String cardCode;
		// 活动信息列表
		private List<Map<String, Object>> rechargeDiscountList;
		// 类型
		private String cardType;
		// 等级
		private String cardLevel;
		// 可用余额
		private float amount;
		// 充值金额
		private float camount;
		// 实收金额
		private float realAmount;
		// 找零
		private float giveAmount;
		// 备注
		private String memo;
		// 优惠次数或赠送金额
		private float giftAmount;
		// 卡的信息
		private Map<String, Object> card;
		// 服务类型列表
		private List<Map<String, Object>> serverList;
		// 单据号
		private String billCode;
		// 预留手机号
		private String mobilePhone;
		// 储值卡密码
		private String password;
		// 会员卡号
		private String memberCard;
		//柜台号
		private String counterCode;
		//服务类型
		private String serviceType;
		//服务类型数组JSON串
		private String serviceArr;
		//设置优惠规则的类型（GD充折，GP充送）
		private String giftamountType;
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

		public String getCardType() {
			return cardType;
		}

		public void setCardType(String cardType) {
			this.cardType = cardType;
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

		public float getAmount() {
			return amount;
		}

		public void setAmount(float amount) {
			this.amount = amount;
		}

		public float getCamount() {
			return camount;
		}

		public void setCamount(float camount) {
			this.camount = camount;
		}

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

		public void setGiftAmount(float giftAmount) {
			this.giftAmount = giftAmount;
		}

		public List<Map<String, Object>> getServerList() {
			return serverList;
		}

		public Map<String, Object> getCard() {
			return card;
		}

		public void setCard(Map<String, Object> card) {
			this.card = card;
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

		public String getMemberCard() {
			return memberCard;
		}

		public void setMemberCard(String memberCard) {
			this.memberCard = memberCard;
		}

		public String getCounterCode() {
			return counterCode;
		}

		public void setCounterCode(String counterCode) {
			this.counterCode = counterCode;
		}

		public String getServiceType() {
			return serviceType;
		}

		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}

		public String getServiceArr() {
			return serviceArr;
		}

		public void setServiceArr(String serviceArr) {
			this.serviceArr = serviceArr;
		}

		public String getGiftamountType() {
			return giftamountType;
		}

		public void setGiftamountType(String giftamountType) {
			this.giftamountType = giftamountType;
		}
		
}

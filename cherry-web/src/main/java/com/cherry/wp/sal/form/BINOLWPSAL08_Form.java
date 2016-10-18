package com.cherry.wp.sal.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL08_Form extends DataTable_BaseForm {
	
	private String showType;
	
	private String counterCode;
	
	private String memberInfoId;
	
	private String mobilePhone;
	
	private String changablePoint;
	
	private String activityType;
	
	private String subjectCode;
	
	private String activityCode;
	
	private String campaignValid;
	
	private String memberName;
	// 发卡柜台
	private String counterCodeBelong;
	// 首次购买柜台
	private String firstSaleCounter;
	// 活动预约柜台
	private String orderCounterCode;
	// 会员等级
	private String memberLevel;
	// 品牌ID(获取产品时使用)
	private String brandInfoId;
	
	private List<Map<String, Object>> promotionList;
	/**用户输入的校验信息*/
	private String customerText;
	/**销售明细列表 */
	private String saleDetailList;
	/**BA编码*/
	private String baCode;
	/**用户输入的会员卡号*/
	private String memberCode;
	/**总单折扣率 */
	private double totalDiscountRate=100;
	/**计算规则中需要用到的 */
	private String inputdetail_back;//原始的购物车信息
	private String outresult_back;
	private String outproduct_back;
	private String maincode;
	private int checkflag;
	private String activitycode_check;
	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getChangablePoint() {
		return changablePoint;
	}

	public void setChangablePoint(String changablePoint) {
		this.changablePoint = changablePoint;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	
	public String getCampaignValid() {
		return campaignValid;
	}

	public void setCampaignValid(String campaignValid) {
		this.campaignValid = campaignValid;
	}

	public String getCounterCodeBelong() {
		return counterCodeBelong;
	}

	public void setCounterCodeBelong(String counterCodeBelong) {
		this.counterCodeBelong = counterCodeBelong;
	}
	
	public String getFirstSaleCounter() {
		return firstSaleCounter;
	}
	
	public void setFirstSaleCounter(String firstSaleCounter) {
		this.firstSaleCounter = firstSaleCounter;
	}

	public String getOrderCounterCode() {
		return orderCounterCode;
	}

	public void setOrderCounterCode(String orderCounterCode) {
		this.orderCounterCode = orderCounterCode;
	}

	public List<Map<String, Object>> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(List<Map<String, Object>> promotionList) {
		this.promotionList = promotionList;
	}

	public String getSaleDetailList() {
		return saleDetailList;
	}

	public void setSaleDetailList(String saleDetailList) {
		this.saleDetailList = saleDetailList;
	}

	public String getBaCode() {
		return baCode;
	}

	public void setBaCode(String baCode) {
		this.baCode = baCode;
	}

	public double getTotalDiscountRate() {
		return totalDiscountRate;
	}

	public void setTotalDiscountRate(double totalDiscountRate) {
		this.totalDiscountRate = totalDiscountRate;
	}

	public String getOutresult_back() {
		return outresult_back;
	}

	public void setOutresult_back(String outresult_back) {
		this.outresult_back = outresult_back;
	}

	public String getMaincode() {
		return maincode;
	}

	public void setMaincode(String maincode) {
		this.maincode = maincode;
	}
	
	public String getActivitycode_check() {
		return activitycode_check;
	}

	public void setActivitycode_check(String activitycode_check) {
		this.activitycode_check = activitycode_check;
	}

	public int getCheckflag() {
		return checkflag;
	}

	public void setCheckflag(int checkflag) {
		this.checkflag = checkflag;
	}

	public String getInputdetail_back() {
		return inputdetail_back;
	}

	public void setInputdetail_back(String inputdetail_back) {
		this.inputdetail_back = inputdetail_back;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getCustomerText() {
		return customerText;
	}

	public void setCustomerText(String customerText) {
		this.customerText = customerText;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getOutproduct_back() {
		return outproduct_back;
	}

	public void setOutproduct_back(String outproduct_back) {
		this.outproduct_back = outproduct_back;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
}

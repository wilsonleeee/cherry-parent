package com.cherry.wp.sal.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLWPSAL02_Form extends DataTable_BaseForm{
	
	private Map<String, Object> memberInfo;
	
	private String pageType;
	
	private String memberInfoId;
	
	private String searchStr;
	
	private String counterCode;
	
	private String counterName;
	
	private String billCode;
	
	private String saleType;
	
	private String showSaleRows;
	
	private String conditionAmount;
	
	private String useMemberPrice;
	
	private String firstBillPrice;
	
	private String showCollectAfterJoin;
	
	private String autoPrintBill;
	
	private String minDiscount;
	
	private String merge;
	
	private List<Map<String, Object>> baList;

    /** 获取是否为负仓库销售的配置 */
    private String stockType;
    /** 获取上屏是否打折的配置 */
    private String discountType;
    /** 是否允许高于原价销售 **/
    private String highPriceSal;
    /** 是否允许智能促销 **/
    private String smartPromotion;
    /** 是否允许储值卡支付 **/
    private String newCzkPay;
    /** 充值开卡按钮 **/
    private String rechargeAndOpendCardButton;

    private String isDiscountFlag;
    /** 云POS销售是否启用白金价 **/
    private String isPlatinumPrice;
    
    /** 销售主页营业员选择模式 **/
    private String baChooseModel;
    /** 云POS是否允许去掉现金支付*/
    private String isCA;
    /** 云POS是否允许不入会销售*/
    private String isMemberSaleFlag;
    /** 柜台电话*/
    private String counterPhone;
    /** 柜台地址*/
    private String counterAddress;
    /** 云POS直接打印情况，模版区分*/
    private String printBrandType;
    /** 云POS负库存销售时，是否给于提示并且继续销售 */
    private String stockSaleType;
    /** 云POS会员入会生日必填/选填 */
    private String birthFlag;
    /**会员卡号校验规则*/
    private String memCodeRule;
    /**云POS是否忽略领用中的购买门槛*/
    private String isBuyFlag;
    /**从会员查询中带过来的会员号*/
    private String mobilePhoneQ;
	public String getPageType() {
		return pageType;
	}



	public String getStockType() {
		return stockType;
	}


	public void setStockType(String stockType) {
		this.stockType = stockType;
	}


	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public Map<String, Object> getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(Map<String, Object> memberInfo) {
		this.memberInfo = memberInfo;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
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
	
	public String getShowSaleRows() {
		return showSaleRows;
	}

	public void setShowSaleRows(String showSaleRows) {
		this.showSaleRows = showSaleRows;
	}

	public String getConditionAmount() {
		return conditionAmount;
	}

	public void setConditionAmount(String conditionAmount) {
		this.conditionAmount = conditionAmount;
	}

	public String getUseMemberPrice() {
		return useMemberPrice;
	}

	public void setUseMemberPrice(String useMemberPrice) {
		this.useMemberPrice = useMemberPrice;
	}

	public String getFirstBillPrice() {
		return firstBillPrice;
	}

	public void setFirstBillPrice(String firstBillPrice) {
		this.firstBillPrice = firstBillPrice;
	}

	public String getShowCollectAfterJoin() {
		return showCollectAfterJoin;
	}

	public void setShowCollectAfterJoin(String showCollectAfterJoin) {
		this.showCollectAfterJoin = showCollectAfterJoin;
	}

	public String getAutoPrintBill() {
		return autoPrintBill;
	}

	public void setAutoPrintBill(String autoPrintBill) {
		this.autoPrintBill = autoPrintBill;
	}

	public String getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(String minDiscount) {
		this.minDiscount = minDiscount;
	}

	public List<Map<String, Object>> getBaList() {
		return baList;
	}

	public void setBaList(List<Map<String, Object>> baList) {
		this.baList = baList;
	}

	public String getMerge() {
		return merge;
	}

	public void setMerge(String merge) {
		this.merge = merge;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getHighPriceSal() {
		return highPriceSal;
	}

	public void setHighPriceSal(String highPriceSal) {
		this.highPriceSal = highPriceSal;
	}

	public String getSmartPromotion() {
		return smartPromotion;
	}

	public String getNewCzkPay() {
		return newCzkPay;
	}

	public void setNewCzkPay(String newCzkPay) {
		this.newCzkPay = newCzkPay;
	}

	public void setSmartPromotion(String smartPromotion) {
		this.smartPromotion = smartPromotion;
	}

	public String getRechargeAndOpendCardButton() {
		return rechargeAndOpendCardButton;
	}

	public void setRechargeAndOpendCardButton(String rechargeAndOpendCardButton) {
		this.rechargeAndOpendCardButton = rechargeAndOpendCardButton;
	}
	
	public String getIsDiscountFlag() {
		return isDiscountFlag;
	}

	public void setIsDiscountFlag(String isDiscountFlag) {
		this.isDiscountFlag = isDiscountFlag;
	}

	public String getIsPlatinumPrice() {
		return isPlatinumPrice;
	}

	public void setIsPlatinumPrice(String isPlatinumPrice) {
		this.isPlatinumPrice = isPlatinumPrice;
	}

	public String getBaChooseModel() {
		return baChooseModel;
	}

	public void setBaChooseModel(String baChooseModel) {
		this.baChooseModel = baChooseModel;
	}

	public String getIsCA() {
		return isCA;
	}

	public void setIsCA(String isCA) {
		this.isCA = isCA;
	}



	public String getIsMemberSaleFlag() {
		return isMemberSaleFlag;
	}



	public void setIsMemberSaleFlag(String isMemberSaleFlag) {
		this.isMemberSaleFlag = isMemberSaleFlag;
	}



	public String getCounterPhone() {
		return counterPhone;
	}



	public void setCounterPhone(String counterPhone) {
		this.counterPhone = counterPhone;
	}



	public String getPrintBrandType() {
		return printBrandType;
	}



	public void setPrintBrandType(String printBrandType) {
		this.printBrandType = printBrandType;
	}



	public String getCounterAddress() {
		return counterAddress;
	}



	public void setCounterAddress(String counterAddress) {
		this.counterAddress = counterAddress;
	}



	public String getStockSaleType() {
		return stockSaleType;
	}



	public void setStockSaleType(String stockSaleType) {
		this.stockSaleType = stockSaleType;
	}



	public String getBirthFlag() {
		return birthFlag;
	}



	public void setBirthFlag(String birthFlag) {
		this.birthFlag = birthFlag;
	}



	public String getMemCodeRule() {
		return memCodeRule;
	}



	public void setMemCodeRule(String memCodeRule) {
		this.memCodeRule = memCodeRule;
	}



	public String getIsBuyFlag() {
		return isBuyFlag;
	}



	public void setIsBuyFlag(String isBuyFlag) {
		this.isBuyFlag = isBuyFlag;
	}



	public String getMobilePhoneQ() {
		return mobilePhoneQ;
	}



	public void setMobilePhoneQ(String mobilePhoneQ) {
		this.mobilePhoneQ = mobilePhoneQ;
	}
	
	
}

/*
 * @(#)BINOLSSPRM71_Form.java     1.0 2015/09/21	
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
package com.cherry.ss.prm.form;


import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSSPRM74_Form extends DataTable_BaseForm {
	/**加密后的全部Json串*/
	private String paramdata;
	/**返回页面的信息*/
	private Map<String,Object> result_map;
	/**调用计算时传入的第一次保留在页面上的json*/
	private String contentText_json;
	/**计算时传入需要计算的规则列表json*/
	private String rule_json;
	/**代金券发券时需要传入的页面上计算已经计算完成的规则列表json*/
	private String competedRule_json;
	/**原始主单信息Json*/
	private String main_json;
	/**原始购物车信息*/
	private String shoppingcart_json;
	/**需要计算的优惠券*/
	private String coupon_json;
	/**所有优惠券的信息*/
	private String couponAll_json;
	/**品牌代码*/
	private String brandCode;
	/**页面返回信息*/
	private Map<String,Object> resultMessage_map;
	/**原价金额*/
	private String receivableTotal;
	/**优惠金额*/
	private String discountTotal;
	/**应收金额*/
	private String actualTotal;
	/**是否是关闭按钮的标识*/
	private String closeFlag;
	/**数据源名称*/
	private String datasourceName;
	/**优惠券券号*/
	private String couponCode;
	/**非会员用券的手机号*/
	private String memberPhone;
	/**所用积分*/
	private String usePoint;
	/**发券页面是否可以选择多个发券活动*/
	private String sendChooseFlag;
	/**所有促销活动类型为N的活动*/
	private String promotionRule_json;
	/**所有积分兑换类型的活动，类型为P*/
	private String pointRule_json;
	public String getParamdata() {
		return paramdata;
	}
	public void setParamdata(String paramdata) {
		this.paramdata = paramdata;
	}
	public Map<String, Object> getResult_map() {
		return result_map;
	}
	public void setResult_map(Map<String, Object> result_map) {
		this.result_map = result_map;
	}
	public String getContentText_json() {
		return contentText_json;
	}
	public void setContentText_json(String contentText_json) {
		this.contentText_json = contentText_json;
	}
	public String getRule_json() {
		return rule_json;
	}
	public void setRule_json(String rule_json) {
		this.rule_json = rule_json;
	}
	public String getMain_json() {
		return main_json;
	}
	public void setMain_json(String main_json) {
		this.main_json = main_json;
	}
	public String getShoppingcart_json() {
		return shoppingcart_json;
	}
	public void setShoppingcart_json(String shoppingcart_json) {
		this.shoppingcart_json = shoppingcart_json;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public Map<String, Object> getResultMessage_map() {
		return resultMessage_map;
	}
	public void setResultMessage_map(Map<String, Object> resultMessage_map) {
		this.resultMessage_map = resultMessage_map;
	}
	public String getReceivableTotal() {
		return receivableTotal;
	}
	public void setReceivableTotal(String receivableTotal) {
		this.receivableTotal = receivableTotal;
	}
	public String getDiscountTotal() {
		return discountTotal;
	}
	public void setDiscountTotal(String discountTotal) {
		this.discountTotal = discountTotal;
	}
	public String getActualTotal() {
		return actualTotal;
	}
	public void setActualTotal(String actualTotal) {
		this.actualTotal = actualTotal;
	}
	public String getCloseFlag() {
		return closeFlag;
	}
	public void setCloseFlag(String closeFlag) {
		this.closeFlag = closeFlag;
	}
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCoupon_json() {
		return coupon_json;
	}
	public void setCoupon_json(String coupon_json) {
		this.coupon_json = coupon_json;
	}
	public String getCouponAll_json() {
		return couponAll_json;
	}
	public void setCouponAll_json(String couponAll_json) {
		this.couponAll_json = couponAll_json;
	}
	public String getCompetedRule_json() {
		return competedRule_json;
	}
	public void setCompetedRule_json(String competedRule_json) {
		this.competedRule_json = competedRule_json;
	}
	public String getMemberPhone() {
		return memberPhone;
	}
	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}
	public String getUsePoint() {
		return usePoint;
	}
	public void setUsePoint(String usePoint) {
		this.usePoint = usePoint;
	}
	public String getSendChooseFlag() {
		return sendChooseFlag;
	}
	public void setSendChooseFlag(String sendChooseFlag) {
		this.sendChooseFlag = sendChooseFlag;
	}
	public String getPromotionRule_json() {
		return promotionRule_json;
	}
	public void setPromotionRule_json(String promotionRule_json) {
		this.promotionRule_json = promotionRule_json;
	}
	public String getPointRule_json() {
		return pointRule_json;
	}
	public void setPointRule_json(String pointRule_json) {
		this.pointRule_json = pointRule_json;
	}
	
	
}

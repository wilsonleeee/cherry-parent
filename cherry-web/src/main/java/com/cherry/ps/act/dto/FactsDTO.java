/*  
 * @(#)FactsDTO.java     1.0 2011/05/31      
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
package com.cherry.ps.act.dto;

import java.util.Date;

/**
 * 规则比较DTO(根据多条销售记录)
 * @author huzude
 *
 */
public class FactsDTO {
	/** 销售数据属性 */
	// 柜台号
	private String[] CountCode;
	// 产品编码
	private String[] BarCode;
	// 品牌代号
	private String[] Brand;
	// 厂商编码
	private String[] Unitcode;
	// 购买数量
	private int[] Quantity;
	// 单价
	private float[] Price;
	// 交易日期
	private Date[] Txddate;
	// 交易时间
	private Date[] Txdtime;
	// 购买单数序号（属于第几单）
	private int[] DocumentNum;
	// 该笔消费金额总数
	private float[] PriceSum;
	/** 事件属性 */
	// 会员生日事件
	private boolean MemberBirthdayEvent;
	// 推荐会员
	private boolean RecommendMember;
	// 会员入会
	private boolean Membership;
	// 会员开卡
	private boolean CardActivate;
	/** 会员属性 */
	// 会员生日
	private Date MemberBirthday;
	// 会员手机
	private String MemberPhone;
	public String[] getCountCode() {
		return CountCode;
	}
	public void setCountCode(String[] countCode) {
		CountCode = countCode;
	}
	public String[] getBarCode() {
		return BarCode;
	}
	public void setBarCode(String[] barCode) {
		BarCode = barCode;
	}
	public String[] getBrand() {
		return Brand;
	}
	public void setBrand(String[] brand) {
		Brand = brand;
	}
	public String[] getUnitcode() {
		return Unitcode;
	}
	public void setUnitcode(String[] unitcode) {
		Unitcode = unitcode;
	}
	public int[] getQuantity() {
		return Quantity;
	}
	public void setQuantity(int[] quantity) {
		Quantity = quantity;
	}
	public float[] getPrice() {
		return Price;
	}
	public void setPrice(float[] price) {
		Price = price;
	}
	public Date[] getTxddate() {
		return Txddate;
	}
	public void setTxddate(Date[] txddate) {
		Txddate = txddate;
	}
	public Date[] getTxdtime() {
		return Txdtime;
	}
	public void setTxdtime(Date[] txdtime) {
		Txdtime = txdtime;
	}
	public int[] getDocumentNum() {
		return DocumentNum;
	}
	public void setDocumentNum(int[] documentNum) {
		DocumentNum = documentNum;
	}
	public float[] getPriceSum() {
		return PriceSum;
	}
	public void setPriceSum(float[] priceSum) {
		PriceSum = priceSum;
	}
	public boolean isMemberBirthdayEvent() {
		return MemberBirthdayEvent;
	}
	public void setMemberBirthdayEvent(boolean memberBirthdayEvent) {
		MemberBirthdayEvent = memberBirthdayEvent;
	}
	public boolean isRecommendMember() {
		return RecommendMember;
	}
	public void setRecommendMember(boolean recommendMember) {
		RecommendMember = recommendMember;
	}
	public boolean isMembership() {
		return Membership;
	}
	public void setMembership(boolean membership) {
		Membership = membership;
	}
	public boolean isCardActivate() {
		return CardActivate;
	}
	public void setCardActivate(boolean cardActivate) {
		CardActivate = cardActivate;
	}
	public Date getMemberBirthday() {
		return MemberBirthday;
	}
	public void setMemberBirthday(Date memberBirthday) {
		MemberBirthday = memberBirthday;
	}
	public String getMemberPhone() {
		return MemberPhone;
	}
	public void setMemberPhone(String memberPhone) {
		MemberPhone = memberPhone;
	}
	
	
}

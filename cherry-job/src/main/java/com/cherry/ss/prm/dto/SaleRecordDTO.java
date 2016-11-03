package com.cherry.ss.prm.dto;

import java.util.Date;
import java.util.List;

import com.cherry.cm.util.DateUtil;

/**
 * 销售主数据DTO
 * @author huzude
 *
 */
public class SaleRecordDTO {
	// 购买总金额
	private double amount;
	
	// 购买总金额(理论购买金额)
	private double amountOther;

	// 购买地点 (城市)
	private String cityID;

	// 购买地点 (渠道)
	private String channelID;

	// 购买日期
	private Date saleTime;

	// 柜台号
	private String counterCode;

	// 购买总数
	private int quantity;
	
	// 购买总数(理论购买总数)
	private int quantityOther;

	// 销售主表id
	private int saleRecordID;

	// 销售单据号
	private String saleNo;

	// 会员号
	private String memberCode;

	// 销售明细List
	private List<SaleRecordDetailDTO> saleRecordDetailList;
	
	// 促销品差异明细List
	private List<CommodityDTO> defProProductList;
	
	// 促销奖励
	private List<PromotionRewardDTO> promotionRewardList;
	
	// 所属部门ID
	private String organizationID;
	
	// 所属组织ID
	private String organizationInfoID;
	
	// 所属品牌ID
	private String brandInfoID;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public Date getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = DateUtil.coverString2Date(saleTime);
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getSaleRecordID() {
		return saleRecordID;
	}

	public void setSaleRecordID(int saleRecordID) {
		this.saleRecordID = saleRecordID;
	}

	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public List<SaleRecordDetailDTO> getSaleRecordDetailList() {
		return saleRecordDetailList;
	}

	public void setSaleRecordDetailList(List<SaleRecordDetailDTO> saleRecordDetailList) {
		this.saleRecordDetailList = saleRecordDetailList;
	}

	public List<PromotionRewardDTO> getPromotionRewardList() {
		return promotionRewardList;
	}

	public void setPromotionRewardList(List<PromotionRewardDTO> promotionRewardList) {
		this.promotionRewardList = promotionRewardList;
	}

	public List<CommodityDTO> getDefProProductList() {
		return defProProductList;
	}

	public void setDefProProductList(List<CommodityDTO> defProProductList) {
		this.defProProductList = defProProductList;
	}

	public double getAmountOther() {
		return amountOther;
	}

	public void setAmountOther(double amountOther) {
		this.amountOther = amountOther;
	}

	public int getQuantityOther() {
		return quantityOther;
	}

	public void setQuantityOther(int quantityOther) {
		this.quantityOther = quantityOther;
	}

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public String getOrganizationInfoID() {
		return organizationInfoID;
	}

	public void setOrganizationInfoID(String organizationInfoID) {
		this.organizationInfoID = organizationInfoID;
	}

	public String getBrandInfoID() {
		return brandInfoID;
	}

	public void setBrandInfoID(String brandInfoID) {
		this.brandInfoID = brandInfoID;
	}
}

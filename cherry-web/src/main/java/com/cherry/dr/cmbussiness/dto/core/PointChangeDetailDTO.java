/*	
 * @(#)PointChangeDetailDTO.java     1.0 2012/02/22	
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
package com.cherry.dr.cmbussiness.dto.core;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.dr.cmbussiness.util.DoubleUtil;

/**
 * 会员积分变化明细DTO
 * 
 * @author hub
 * @version 1.0 2011.02.22
 */
public class PointChangeDetailDTO extends BaseDTO{
	
	/** 会员积分变化主ID */
	private int pointChangeId;
	
	/** 销售ID */
	private int saleRecId;
	
	/** 销售明细ID */
	private int saleDetailId;
	
	/** 厂商编码 */
	private String unitCode;
	
	/** 产品条码*/
	private String barCode;
	
	/** 促销品/产品厂商ID */
	private int prmPrtVendorId;
	
	/** 销售类型 */
	private String saleType;
	
	/** 积分值 */
	private double point;
	
	/** 定价 */
	private double price;
	
	/** 数量 */
	private double quantity;
	
	/** 金额  */
	private double amount;
	
	/** 积分类型 */
	private String pointType;
	
	/** 理由 */
	private String reason;
	
	/** 促销品/产品类别 */
	private String prmPrtCateCd;
	
	/** 兑换所需积分  */
	private double exPoint;
	
	/** 折扣率 */
	private double discount;
	
	/** 积分有效期(月) */
	private int validMonths;
	
	/** 匹配的规则代号 */
	private String subCampaignCode;
	
	/** 匹配的规则规则ID */
	private Integer subCampaignId;
	
	/** 主规则ID */
	private Integer mainRuleId;
	
	/** 组合规则ID */
	private Integer combRuleId;
	
	/** 默认规则关联ID */
	private Integer defRelationId;
	
	/** 重算次数 */
	private int reCalcCount;
	
	/** 规则描述ID */
	private String ruledptId;
	
	/** 是否是添加的明细 */
	private String detailFlag;
	
	/** 是否是特殊产品 */
	private String prtKbn;
	
	/** 退货单号 */
	private String billCodeSR;
	
	/** 退货单据修改次数 */
	private int modifiedTimesSR;
	
	/** 退货单时间 */
	private String ticketDateSR;
	
	/** 产品分类信息 */
	private List<Map<String, Object>> prtCateList;
	
	/** 扩展信息 */
	private Map<String, Object> extParams;
	
	/** 折扣区分 */
	private String zkFlag;
	
	/** 活动代号 */
	private String actMainCode;
	
	private String noSpecKbn;
	
	private Double amtScale;
	
	private String repKbn;
	
	public String getDetailFlag() {
		return detailFlag;
	}

	public void setDetailFlag(String detailFlag) {
		this.detailFlag = detailFlag;
	}

	public int getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(int saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getPointChangeId() {
		return pointChangeId;
	}

	public void setPointChangeId(int pointChangeId) {
		this.pointChangeId = pointChangeId;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getPrmPrtVendorId() {
		return prmPrtVendorId;
	}

	public void setPrmPrtVendorId(int prmPrtVendorId) {
		this.prmPrtVendorId = prmPrtVendorId;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public void addReason(String reason) {
		if (!CherryChecker.isNullOrEmpty(reason)) {
			if (CherryChecker.isNullOrEmpty(this.reason)) {
				this.reason = reason;
			} else {
				this.reason += ";" + reason;
			}
		}
	}

	public String getPrmPrtCateCd() {
		return prmPrtCateCd;
	}

	public void setPrmPrtCateCd(String prmPrtCateCd) {
		this.prmPrtCateCd = prmPrtCateCd;
	}

	public double getExPoint() {
		return exPoint;
	}

	public void setExPoint(double exPoint) {
		this.exPoint = exPoint;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getValidMonths() {
		return validMonths;
	}

	public void setValidMonths(int validMonths) {
		this.validMonths = validMonths;
	}

	public String getSubCampaignCode() {
		return subCampaignCode;
	}

	public void setSubCampaignCode(String subCampaignCode) {
		this.subCampaignCode = subCampaignCode;
	}

	public int getReCalcCount() {
		return reCalcCount;
	}

	public void setReCalcCount(int reCalcCount) {
		this.reCalcCount = reCalcCount;
	}

	public String getRuledptId() {
		return ruledptId;
	}

	public void setRuledptId(String ruledptId) {
		this.ruledptId = ruledptId;
	}

	public List<Map<String, Object>> getPrtCateList() {
		return prtCateList;
	}

	public void setPrtCateList(List<Map<String, Object>> prtCateList) {
		this.prtCateList = prtCateList;
	}

	public Integer getSubCampaignId() {
		return subCampaignId;
	}

	public void setSubCampaignId(Integer subCampaignId) {
		this.subCampaignId = subCampaignId;
	}

	public Integer getMainRuleId() {
		return mainRuleId;
	}

	public void setMainRuleId(Integer mainRuleId) {
		this.mainRuleId = mainRuleId;
	}

	public Integer getCombRuleId() {
		return combRuleId;
	}

	public void setCombRuleId(Integer combRuleId) {
		this.combRuleId = combRuleId;
	}

	public String getPrtKbn() {
		return prtKbn;
	}

	public void setPrtKbn(String prtKbn) {
		this.prtKbn = prtKbn;
	}

	public String getBillCodeSR() {
		return billCodeSR;
	}

	public void setBillCodeSR(String billCodeSR) {
		this.billCodeSR = billCodeSR;
	}

	public int getModifiedTimesSR() {
		return modifiedTimesSR;
	}

	public void setModifiedTimesSR(int modifiedTimesSR) {
		this.modifiedTimesSR = modifiedTimesSR;
	}

	public String getTicketDateSR() {
		return ticketDateSR;
	}

	public void setTicketDateSR(String ticketDateSR) {
		this.ticketDateSR = ticketDateSR;
	}

	public Integer getDefRelationId() {
		return defRelationId;
	}

	public void setDefRelationId(Integer defRelationId) {
		this.defRelationId = defRelationId;
	}

	public Map<String, Object> getExtParams() {
		return extParams;
	}

	public void setExtParams(Map<String, Object> extParams) {
		this.extParams = extParams;
	}

	public int getSaleRecId() {
		return saleRecId;
	}

	public void setSaleRecId(int saleRecId) {
		this.saleRecId = saleRecId;
	}

	public String getZkFlag() {
		return zkFlag;
	}

	public void setZkFlag(String zkFlag) {
		this.zkFlag = zkFlag;
	}

	public String getActMainCode() {
		return actMainCode;
	}

	public void setActMainCode(String actMainCode) {
		this.actMainCode = actMainCode;
	}

	public String getNoSpecKbn() {
		return noSpecKbn;
	}

	public void setNoSpecKbn(String noSpecKbn) {
		this.noSpecKbn = noSpecKbn;
	}
	
	public Double getAmtScale() {
		return amtScale;
	}

	public void setAmtScale(Double amtScale) {
		this.amtScale = amtScale;
	}
	
	public String getRepKbn() {
		return repKbn;
	}

	public void setRepKbn(String repKbn) {
		this.repKbn = repKbn;
	}

	public void resetQuantity(double quantity) {
		boolean flag = "2".equals(this.getZkFlag());
		double prc = 0;
		if (!"N".equalsIgnoreCase(this.saleType)) {
			double pricez = 0;
			boolean flz = true;
			if (flag) {
				if (null == this.amtScale) {
					pricez = this.amount;
				} else {
					prc = this.amtScale;
					flz = false;
				}
			} else {
				pricez = this.price;
				if (!CherryChecker.isNullOrEmpty(this.billCodeSR)) {
					pricez = -this.price;
				}
			}
			if (flz) {
				prc = DoubleUtil.div(pricez, this.quantity);
				if (flag) {
					this.amtScale = prc;
				}
			}
		} else {
			if (flag) {
				if (null == this.amtScale) {
					prc = DoubleUtil.div(this.amount, this.quantity);
					this.amtScale = prc;
				} else {
					prc = this.amtScale;
				}
			} else {
				prc = this.price;
			}
		}
		this.quantity = quantity;
		this.amount = DoubleUtil.mul(prc, this.quantity);
	}
}

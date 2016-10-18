package com.cherry.mb.svc.dto;

/**
 * 
 * @ClassName: RechargeRuleDTO 
 * @Description: TODO(储值规则信息DTO) 
 * @author menghao
 * @version v1.0.0 2016-6-27 
 *
 */
public class RechargeRuleDTO {
	private String brandInfoId;
	private String organizationInfoId;
	private String discountBeginDate;
	private String discountEndDate;
	private String ruleName;
	private String rechargeType;
	private String applyStoreObject;
	private String maxMatchCount;
	private String comments;
	
	private String rechargeValueActual;
	private String giftAmount;
	private int createBy;
	private int updateBy;
	private String createPGM;
	private String updatePGM;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getOrganizationInfoId() {
		return organizationInfoId;
	}
	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}
	public String getDiscountBeginDate() {
		return discountBeginDate;
	}
	public void setDiscountBeginDate(String discountBeginDate) {
		this.discountBeginDate = discountBeginDate;
	}
	public String getDiscountEndDate() {
		return discountEndDate;
	}
	public void setDiscountEndDate(String discountEndDate) {
		this.discountEndDate = discountEndDate;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRechargeType() {
		return rechargeType;
	}
	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}
	public String getApplyStoreObject() {
		return applyStoreObject;
	}
	public void setApplyStoreObject(String applyStoreObject) {
		this.applyStoreObject = applyStoreObject;
	}
	public String getMaxMatchCount() {
		return maxMatchCount;
	}
	public void setMaxMatchCount(String maxMatchCount) {
		this.maxMatchCount = maxMatchCount;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRechargeValueActual() {
		return rechargeValueActual;
	}
	public void setRechargeValueActual(String rechargeValueActual) {
		this.rechargeValueActual = rechargeValueActual;
	}
	public String getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(String giftAmount) {
		this.giftAmount = giftAmount;
	}
	public int getCreateBy() {
		return createBy;
	}
	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}
	public int getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}
	public String getCreatePGM() {
		return createPGM;
	}
	public void setCreatePGM(String createPGM) {
		this.createPGM = createPGM;
	}
	public String getUpdatePGM() {
		return updatePGM;
	}
	public void setUpdatePGM(String updatePGM) {
		this.updatePGM = updatePGM;
	}
	
}

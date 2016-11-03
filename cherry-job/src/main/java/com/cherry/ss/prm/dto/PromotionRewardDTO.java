package com.cherry.ss.prm.dto;

import java.util.List;

/**
 * 促销奖励结果DTO
 * @author huzude
 *
 */
public class PromotionRewardDTO {
	
	// 促销活动ID
	private String activeId;
	
	// 奖励金额
	private float rewardAmount;
	
	// 促销品奖励List
	private List<CommodityDTO> commodityDTOList;
	
	public String getActiveId() {
		return activeId;
	}

	public void setActiveId(String activeId) {
		this.activeId = activeId;
	}

	public float getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(float rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public List<CommodityDTO> getCommodityDTOList() {
		return commodityDTOList;
	}

	public void setCommodityDTOList(List<CommodityDTO> commodityDTOList) {
		this.commodityDTOList = commodityDTOList;
	}
	
}

/*	
 * @(#)JoinDTO.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.dto.jon;

import java.util.List;

import com.cherry.dr.cmbussiness.dto.core.AmountDTO;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 会员入会共通 DTO
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class JoinDTO extends CampBaseDTO{
	
	/** 改变后的会员等级 */
	private int newLevelId;
	
	/** 改变后的化妆次数 */
	private int newBtimes;
	
	/** 改变后的累计金额 */
	private double newTotalAmount;
	
	/** 累积金额List */
	private List<AmountDTO> amountList;

	public List<AmountDTO> getAmountList() {
		return amountList;
	}

	public void setAmountList(List<AmountDTO> amountList) {
		this.amountList = amountList;
	}

	public int getNewLevelId() {
		return newLevelId;
	}

	public void setNewLevelId(int newLevelId) {
		this.newLevelId = newLevelId;
	}

	public int getNewBtimes() {
		return newBtimes;
	}

	public void setNewBtimes(int newBtimes) {
		this.newBtimes = newBtimes;
	}

	public double getNewTotalAmount() {
		return newTotalAmount;
	}

	public void setNewTotalAmount(double newTotalAmount) {
		this.newTotalAmount = newTotalAmount;
	}
}

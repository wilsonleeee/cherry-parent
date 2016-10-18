/**		
 * @(#)RivalSaleDetailDataDTO.java     1.0 2011/07/06		
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
package com.cherry.mq.mes.dto;

/**
 * 竞争对手明细数据行映射DTO
 * 
 * @author zhhuyi
 * 
 */
public class RivalSaleDetailDataDTO {
	/**竞争对手中文名*/
	private String RivalNameCN;
	/**竞争对手英文名*/
	private String RivalNameEN;
	/**销售金额*/
	private String SaleMoney;
	/**销售数量*/
	private String SaleQuantity;

	public String getRivalNameCN() {
		return RivalNameCN;
	}

	public void setRivalNameCN(String rivalNameCN) {
		RivalNameCN = rivalNameCN;
	}

	public String getRivalNameEN() {
		return RivalNameEN;
	}

	public void setRivalNameEN(String rivalNameEN) {
		RivalNameEN = rivalNameEN;
	}

	public String getSaleMoney() {
		return SaleMoney;
	}

	public void setSaleMoney(String saleMoney) {
		SaleMoney = saleMoney;
	}

	public String getSaleQuantity() {
		return SaleQuantity;
	}

	public void setSaleQuantity(String saleQuantity) {
		SaleQuantity = saleQuantity;
	}

}

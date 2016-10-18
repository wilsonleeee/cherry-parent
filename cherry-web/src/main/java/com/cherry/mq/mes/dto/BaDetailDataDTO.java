/**		
 * @(#)DetailDataDTO.java     1.0 2011/05/23		
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
 * BA明细数据行映射DTO
 * @author huzude
 *
 */
public class BaDetailDataDTO {
	/** BA号 */
	private String BAcode;
	/** BA姓名 */
	private String Baname;
	/** 状态，1表示当班，2表示主管 */
	private String Flag;
	/** 柜台 */
	private String Countercode;
	/** 电话号码 */
	private String BaTel;
	/**身份证号*/
	private String IdentityCard;

	public String getBAcode() {
		return BAcode;
	}

	public void setBAcode(String bAcode) {
		BAcode = bAcode;
	}

	public String getBaname() {
		return Baname;
	}

	public void setBaname(String baname) {
		Baname = baname;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getCountercode() {
		return Countercode;
	}

	public void setCountercode(String countercode) {
		Countercode = countercode;
	}

	public String getBaTel() {
		return BaTel;
	}

	public void setBaTel(String baTel) {
		BaTel = baTel;
	}

    public String getIdentityCard() {
        return IdentityCard;
    }

    public void setIdentityCard(String identityCard) {
        IdentityCard = identityCard;
    }

}

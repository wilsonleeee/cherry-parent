/*	
 * @(#)UdiskBindDetailDTO.java     1.0 2011/12/14	
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
package com.cherry.cm.activemq.dto;

import java.util.List;

/**
 * U盘绑定信息MQ明细业务 DTO
 * 
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public class UdiskBindDetailDTO {
	
	/** U盘序列号 */
	private String udiskSN;
	
	/** 区分（0：表示删除绑定，1：表示新增绑定） */
	private String flag;
	
	/** 柜台号 */
	private List<String> countercode;

	public String getUdiskSN() {
		return udiskSN;
	}

	public void setUdiskSN(String udiskSN) {
		this.udiskSN = udiskSN;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List<String> getCountercode() {
		return countercode;
	}

	public void setCountercode(List<String> countercode) {
		this.countercode = countercode;
	}

}

/**		
 * @(#)DetailDataDTO.java     1.0 2011/07/28		
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
 * BAS考勤明细数据行映射DTO
 * 
 * @author zhhuyi
 * 
 */
public class BasDetailDataDTO {
	/** U盘序列号 */
	private String UDiskSN;
	/** BAS姓名 */
	private String BasName;
	/** 柜台号 */
	private String Countercode;
	/** 柜台名 */
	private String CounterName;
	/** 机器号 */
	private String Machinecode;
	/** 考勤日期 */
	private String AttDate;
	/** 到柜时间 */
	private String ArrTime;
	/** 离柜时间 */
	private String LeaTime;
	/** 到柜/离柜标识 */
	private String AttType;

	public String getUDiskSN() {
		return UDiskSN;
	}

	public void setUDiskSN(String uDiskSN) {
		UDiskSN = uDiskSN;
	}

	public String getBasName() {
		return BasName;
	}

	public void setBasName(String basName) {
		BasName = basName;
	}

	public String getCountercode() {
		return Countercode;
	}

	public void setCountercode(String countercode) {
		Countercode = countercode;
	}

	public String getCounterName() {
		return CounterName;
	}

	public void setCounterName(String counterName) {
		CounterName = counterName;
	}

	public String getMachinecode() {
		return Machinecode;
	}

	public void setMachinecode(String machinecode) {
		Machinecode = machinecode;
	}

	public String getAttDate() {
		return AttDate;
	}

	public void setAttDate(String attDate) {
		AttDate = attDate;
	}

	public String getArrTime() {
		return ArrTime;
	}

	public void setArrTime(String arrTime) {
		ArrTime = arrTime;
	}

	public String getLeaTime() {
		return LeaTime;
	}

	public void setLeaTime(String leaTime) {
		LeaTime = leaTime;
	}

	public String getAttType() {
		return AttType;
	}

	public void setAttType(String attType) {
		AttType = attType;
	}

}

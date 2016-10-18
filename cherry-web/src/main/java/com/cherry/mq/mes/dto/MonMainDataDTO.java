/**		
 * @(#)DetailDataDTO.java     1.0 2011/06/01		
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

public class MonMainDataDTO extends MainDataDTO{
	
	/** 更新时间 */
	private String Updatetime;
	
	public String getUpdatetime() {
		return Updatetime;
	}

	public void setUpdatetime(String updatetime) {
		Updatetime = updatetime;
	}
}

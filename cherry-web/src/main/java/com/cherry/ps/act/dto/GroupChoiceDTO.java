/*  
 * @(#)GroupChoiceDTO.java     1.0 2011/05/31      
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
package com.cherry.ps.act.dto;

public class GroupChoiceDTO {
	private int groupPriority;

	private String choice;
	
	public int getGroupPriority() {
		return groupPriority;
	}
	public void setGroupPriority(int groupPriority) {
		this.groupPriority = groupPriority;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	
	
}

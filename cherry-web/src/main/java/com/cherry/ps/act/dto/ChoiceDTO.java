/*  
 * @(#)ChoiceDTO.java     1.0 2011/05/31      
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

import java.util.List;

public class ChoiceDTO {
	private List<ActiveChoiceDTO> actChoiceList ;
	
	private List<GroupChoiceDTO> grpChoiceList ;
	
	private List<RuleActiveDTO> actList;
	
	private RuleActiveDTO ruleActiveDTOFinally;

	public List<ActiveChoiceDTO> getActChoiceList() {
		return actChoiceList;
	}

	public void setActChoiceList(List<ActiveChoiceDTO> actChoiceList) {
		this.actChoiceList = actChoiceList;
	}

	public List<GroupChoiceDTO> getGrpChoiceList() {
		return grpChoiceList;
	}

	public void setGrpChoiceList(List<GroupChoiceDTO> grpChoiceList) {
		this.grpChoiceList = grpChoiceList;
	}

	public List<RuleActiveDTO> getActList() {
		return actList;
	}

	public void setActList(List<RuleActiveDTO> actList) {
		this.actList = actList;
	}

	public RuleActiveDTO getRuleActiveDTOFinally() {
		return ruleActiveDTOFinally;
	}

	public void setRuleActiveDTOFinally(RuleActiveDTO ruleActiveDTOFinally) {
		this.ruleActiveDTOFinally = ruleActiveDTOFinally;
	}
	
	
}

/*  
 * @(#)ParseDTO.java     1.0 2011/05/31      
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

import java.util.HashMap;
import java.util.List;
@SuppressWarnings("unchecked")
public class ParseDTO {	

	private List conditionKeywordList ;
	
	private HashMap keyMap;
	
	private HashMap pointResultMap;
	
	public HashMap getPointResultMap() {
		return pointResultMap;
	}

	public void setPointResultMap(HashMap pointResultMap) {
		this.pointResultMap = pointResultMap;
	}

	public HashMap getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(HashMap keyMap) {
		this.keyMap = keyMap;
	}

	public List getConditionKeywordList() {
		return conditionKeywordList;
	}

	public void setConditionKeywordList(List conditionKeywordList) {
		this.conditionKeywordList = conditionKeywordList;
	}


}

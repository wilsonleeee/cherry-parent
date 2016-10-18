/*	
 * @(#)BINBEDRJON01_Rule.java     1.0 2011/08/18	
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
package com.cherry.dr.jon.rule;

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.DroolsFileDTO;
import com.cherry.dr.cmbussiness.interfaces.CampRuleIF;

/**
 * 会员入会规则设置
 * 
 * @author hub
 * @version 1.0 2011.08.18
 */
public class BINBEDRJON01_Rule implements CampRuleIF{

	@Override
	public void lastHandle(Object... obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void thenHandle(Object... obj) {
		
	}

	@Override
	public void whenHandle(Object... obj) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 创建规则文件
	 * 
	 * @param map
	 * 		   	参数集合
	 * @return DroolsFileDTO
	 * 			规则文件	
	 */
	@Override
	public DroolsFileDTO createDroolsFile(CampBaseDTO campBaseDTO) {
		
		DroolsFileDTO droolsFileDTO = new DroolsFileDTO();
		droolsFileDTO.setDroolsFileName("JoinRule_1_1");
		return droolsFileDTO;
	}

}

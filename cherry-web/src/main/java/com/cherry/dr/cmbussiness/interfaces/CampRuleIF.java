/*	
 * @(#)CampRuleUtil.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.interfaces;

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.DroolsFileDTO;


/**
 * 会员活动规则共通IF
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public interface CampRuleIF {
	
	/**
	 * 创建规则文件
	 * 
	 * @param map
	 * 		   	参数集合
	 * @return DroolsFileDTO
	 * 			规则文件	
	 */
	public DroolsFileDTO createDroolsFile(CampBaseDTO campBaseDTO);
	
	/**
	 * 规则文件对应的when条件里的处理方法
	 * 
	 * @param obj
	 * 		   	设置参数
	 * @return
	 */
	public void whenHandle(Object... obj);
	
	/**
	 * 规则文件对应的then条件里的处理方法
	 * 
	 * @param obj
	 * 		   	设置参数
	 * @return
	 */
	public void thenHandle(Object... obj);
	
	/**
	 * 规则文件对应的最后调用的处理方法
	 * 
	 * @param obj
	 * 		   	设置参数
	 * @return
	 */
	public void lastHandle(Object... obj);

}

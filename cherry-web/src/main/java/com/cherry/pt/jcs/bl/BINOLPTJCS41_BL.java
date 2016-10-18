/*  
 * @(#)BINOLPTJCS21_BL.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.bl;

import java.util.Map;
import javax.annotation.Resource;

import com.cherry.pt.jcs.interfaces.BINOLPTJCS41_IF;
import com.cherry.pt.jcs.service.BINOLPTJCS41_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

public class BINOLPTJCS41_BL extends SsBaseBussinessLogic implements BINOLPTJCS41_IF{
	@Resource
	private BINOLPTJCS41_Service binOLPTJCS41_Service;
	
	/**
	 * 保存产品功能开启时间表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addPrtFun(Map<String, Object> map) throws Exception{
		return binOLPTJCS41_Service.insertPrtFun(map);
	}
		
	@Override
	public String getCount(Map<String, Object> map) {
			return binOLPTJCS41_Service.getCount(map);
		}
}
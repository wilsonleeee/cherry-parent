/*
 * @(#)BINOLPLSCF05_BL.java     1.0 2010/10/27
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

package com.cherry.pl.scf.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.pl.scf.service.BINOLPLSCF02_Service;

/**
 * 删除审核审批配置信息BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF05_BL {
	
	/** 审核审批配置管理Service */
	@Resource
	private BINOLPLSCF02_Service binOLPLSCF02_Service;
	
	/**
	 * 删除审核审批配置信息
	 * 
	 * @param map 删除条件
	 */
	public void tran_deleteAudti(Map<String, Object> map) throws Exception {
		
		// 删除审核审批配置信息
		int result = binOLPLSCF02_Service.deleteAudti(map);
		if(result == 0) {
			throw new CherryException("ECM00011");
		}
	}

}

/*
 * @(#)BINOLPLSCF04_BL.java     1.0 2010/10/27
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
 * 更新审核审批配置信息BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF04_BL {
	
	/** 审核审批配置管理Service */
	@Resource
	private BINOLPLSCF02_Service binOLPLSCF02_Service;
	
	/**
	 * 查询审核审批配置信息
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置信息
	 */
	public Map<String, Object> getAuditInfo(Map<String, Object> map) {
		
		// 查询审核审批配置信息
		return binOLPLSCF02_Service.getAuditInfo(map);
	}
	
	/**
	 * 更新审核审批配置信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public void tran_updateAudit(Map<String, Object> map) throws Exception {
		
		// 更新审核审批配置信息
		int result = binOLPLSCF02_Service.updateAudit(map);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
	}

}

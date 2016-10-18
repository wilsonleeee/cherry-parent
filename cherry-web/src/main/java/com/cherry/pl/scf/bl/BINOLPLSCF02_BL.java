/*
 * @(#)BINOLPLSCF02_BL.java     1.0 2010/10/27
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.scf.service.BINOLPLSCF02_Service;

/**
 * 审核审批配置一览BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF02_BL {
	
	/** 审核审批配置管理Service */
	@Resource
	private BINOLPLSCF02_Service binOLPLSCF02_Service;
	
	/**
	 * 查询业务类型List
	 * 
	 * @param map 查询条件
	 * @return 业务类型List
	 */
	public List<Map<String, Object>> getBussinessTypeCodeList(Map<String, Object> map) {
		
		// 查询业务类型List
		return binOLPLSCF02_Service.getBussinessTypeCodeList(map);
	}
	
	/**
	 * 查询审核审批配置信息总数
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置信息总数
	 */
	public int getAuditPrivilegeCount(Map<String, Object> map) {
		
		// 查询审核审批配置信息总数
		return binOLPLSCF02_Service.getAuditPrivilegeCount(map);
	}
	
	/**
	 * 查询审核审批配置信息List
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置信息List
	 */
	public List<Map<String, Object>> getAuditPrivilegeList(Map<String, Object> map) {
		
		// 查询审核审批配置信息List
		return binOLPLSCF02_Service.getAuditPrivilegeList(map);
	}

}

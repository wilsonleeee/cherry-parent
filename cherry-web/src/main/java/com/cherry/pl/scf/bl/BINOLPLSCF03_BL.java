/*
 * @(#)BINOLPLSCF03_BL.java     1.0 2010/10/27
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
 * 添加审核审批配置信息BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF03_BL {
	
	/** 审核审批配置管理Service */
	@Resource
	private BINOLPLSCF02_Service binOLPLSCF02_Service;
	
	/**
	 * 添加审核审批配置信息
	 * 
	 * @param map 添加内容
	 */
	public void tran_addAudit(Map<String, Object> map) {
		
		// 添加审核审批配置信息
		binOLPLSCF02_Service.addAudit(map);
	}
	
	/**
	 * 根据身份类型取得身份信息
	 * 
	 * @param map 查询条件
	 */
	public List<Map<String, Object>> getCodeByType(Map<String, Object> map) {
		
		// 身份类型
		String type = (String)map.get("type");
		// 身份类型为用户的场合
		if("1".equals(type)) {
			// 查询用户List
			return binOLPLSCF02_Service.getUserInfoList(map);
		} 
		// 身份类型为岗位的场合
		else if("2".equals(type)) {
			// 查询岗位List
			return binOLPLSCF02_Service.getPosInfoList(map);
		} 
		// 身份类型为部门的场合
		else if("3".equals(type)) {
			// 查询部门List
			return binOLPLSCF02_Service.getOrgInfoList(map);
		}
		return null;
	}
	
	/**
	 * 查询审核审批配置信息是否存在
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置ID
	 */
	public String getAuditVal(Map<String, Object> map) {
		
		// 查询审核审批配置信息是否存在
		return binOLPLSCF02_Service.getAuditVal(map);
	}

}

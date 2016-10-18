/*
 * @(#)BINOLPLRLM05_BL.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.pl.rlm.service.BINOLPLRLM99_Service;

/**
 * 删除角色BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM05_BL {
	
	/** 角色管理Service */
	@Resource
	private BINOLPLRLM99_Service binOLPLRLM99_Service;
	
	/**
	 * 删除角色
	 * 
	 * @param Map 
	 */
	public void tran_deleteRole(Map<String, Object> map) throws Exception {
		
		// 删除角色
		int result = binOLPLRLM99_Service.deleteRole(map);
		if(result == 0) {
			throw new CherryException("ECM00011");
		}
		// 删除组织角色对应关系
		binOLPLRLM99_Service.deleteOrganizationRole(map);
		// 删除岗位类别角色对应关系
		binOLPLRLM99_Service.deletePositionCategoryRole(map);
//		// 删除岗位角色对应关系
//		binOLPLRLM99_Service.deletePositionRole(map);
		// 删除用户角色对应关系
		binOLPLRLM99_Service.deleteUserRole(map);
		// 删除角色已有的的功能资源
		binOLPLRLM99_Service.deleteRoleResource(map);
	}

}

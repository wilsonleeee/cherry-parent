/*
 * @(#)BINOLPLRLM04_BL.java     1.0 2010/10/27
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.rlm.service.BINOLPLRLM99_Service;

/**
 * 更新角色BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM04_BL {
	
	/** 角色管理Service */
	@Resource
	private BINOLPLRLM99_Service binOLPLRLM99_Service;
	
	/**
	 * 取得角色信息
	 * 
	 * @param Map
	 * @return Map 
	 */
	public Map<String, Object> getRoleInfo(Map<String, Object> map) {
		
		// 取得角色信息
		return binOLPLRLM99_Service.getRoleInfo(map);
	}
	
	/**
	 * 更新角色
	 * 
	 * @param Map
	 */
	public void tran_updateRole(Map<String, Object> map) throws Exception {
		
		// 更新日时
		map.put(CherryConstants.UPDATE_TIME, binOLPLRLM99_Service.getSYSDate());
		// 更新角色
		int result = binOLPLRLM99_Service.updateRole(map);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
	}

}

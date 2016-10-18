/*
 * @(#)BINOLBSDEP05_BL.java     1.0 2010/10/27
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

package com.cherry.bs.dep.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.service.BINOLBSDEP05_Service;

/**
 * 停用启用部门BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP05_BL {
	
	/** 停用启用部门Service */
	@Resource
	private BINOLBSDEP05_Service binOLBSDEP05_Service;
	
	/**
	 * 停用启用部门
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public void tran_updateDepartInfo(Map<String, Object> map) throws Exception {
		
		// 取得启用停用flag
		String validFlag = (String)map.get("validFlag");
		// 停用的场合
		if(validFlag != null && "0".equals(validFlag)) {
			map.put("status", "4");
		} else {
			map.put("status", "0");
		}
		// 停用启用部门
		binOLBSDEP05_Service.updateDepartInfo(map);
			
	}

}

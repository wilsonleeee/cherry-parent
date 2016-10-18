/*
 * @(#)BINOLPLPLT04_BL.java     1.0 2010/10/27
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

package com.cherry.pl.plt.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.pl.plt.service.BINOLPLPLT99_Service;

/**
 * 删除权限类型BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT04_BL {
	
	/** 权限类型管理Service */
	@Resource
	private BINOLPLPLT99_Service binOLPLPLT99_Service;
	
	/**
	 * 删除权限类型
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public void tran_deletePlt(Map<String, Object> map) throws Exception {
		
		// 删除权限类型
		int result = binOLPLPLT99_Service.deletePlt(map);
		if(result == 0) {
			throw new CherryException("ECM00011");
		}
	}

}

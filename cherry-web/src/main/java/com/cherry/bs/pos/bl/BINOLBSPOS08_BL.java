/*
 * @(#)BINOLBSPOS08_BL.java     1.0 2010/10/27
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

package com.cherry.bs.pos.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS08_Service;
import com.cherry.cm.core.CherryException;

/**
 * 
 * 更新岗位类别画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS08_BL {
	
	/** 更新岗位类别画面Service */
	@Resource
	private BINOLBSPOS08_Service binOLBSPOS08_Service;
	
	/**
	 * 
	 * 更新岗位类别
	 * 
	 * @param map 更新条件
	 */
	public void tran_updatePosCategory(Map<String, Object> map) throws Exception {
		
		// 更新岗位类别
		int result = binOLBSPOS08_Service.updatePosCategory(map);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
	}
	
	/**
	 * 验证同一组织中是否存在同样的岗位名称
	 * 
	 * @param map 检索条件
	 * @return 返回岗位类别ID
	 */
	public String getPosCategoryNameCheck(Map<String, Object> map) {
		
		// 验证同一组织中是否存在同样的岗位名称
		return binOLBSPOS08_Service.getPosCategoryNameCheck(map);
	}

}

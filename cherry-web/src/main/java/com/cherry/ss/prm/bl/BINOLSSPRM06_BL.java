/*
 * @(#)BINOLSSPRM06_BL.java     1.0 2010/12/01
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
package com.cherry.ss.prm.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.service.BINOLSSPRM06_Service;

/**
 * 促销品分类添加 BL
 * 
 *
 */
public class BINOLSSPRM06_BL {
	
	@Resource
	private BINOLSSPRM06_Service binOLSSPRM06_Service;
	
	/**
	 * 促销品分类添加插表处理
	 * 
	 * @param map
	 * @return 无
	 */
	public void tran_addPrmType(Map<String, Object> map) {
		
//		// 系统时间
//		String sysDate = binOLSSPRM06_Service.getSYSDate();
//		// 作成日时
//		map.put(CherryConstants.CREATE_TIME, sysDate);
//		// 更新日时
//		map.put(CherryConstants.UPDATE_TIME, sysDate);
		
	    //先更新促销品分类表的相应字段，如果返回值为0，插入促销品分类表
	    int count = binOLSSPRM06_Service.updatePrmType(map);
	    if (count == 0){
	        // 插入促销产品分类信息
	        binOLSSPRM06_Service.insertPrmType(map);
	    }
	}

}

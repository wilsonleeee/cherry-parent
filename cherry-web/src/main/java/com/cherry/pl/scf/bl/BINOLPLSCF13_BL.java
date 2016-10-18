/*
 * @(#)BINOLPLSCF13_BL.java     1.0 2010/10/27
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.pl.scf.service.BINOLPLSCF13_Service;

/**
 * code值编辑BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF13_BL {
	
	/** code值编辑Service */
	@Resource
	private BINOLPLSCF13_Service binolplscf13Service;

	/**
	 * 取得code值详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCoderDetail(Map<String, Object> map) {
		
		return binolplscf13Service.getCoderDetail(map);
	}

	/**
	 * 更新code值
	 * 
	 * @param map
	 * @return
	 */	
	public void tran_updateCoder(Map<String, Object> map) {
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF13");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF13");
		// 数据库系统时间
		String sysDate = binolplscf13Service.getSYSDate();
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 更新厂商信息表
		binolplscf13Service.updateCoder(map);
		
	}


}

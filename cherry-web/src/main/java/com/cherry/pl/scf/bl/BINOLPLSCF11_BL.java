/*
 * @(#)BINOLPLSCF10_BL.java     1.0 2010/10/27
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
import com.cherry.pl.scf.service.BINOLPLSCF11_Service;

/**
 * code值添加BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF11_BL {
	
	/** code值添加Service */
	@Resource
	private BINOLPLSCF11_Service binolplscf11Service;

	/**
	 * code值添加
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public void tran_saveCoder(Map<String, Object> map) {
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF11");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF11");
		// 数据库系统时间
		String sysDate = binolplscf11Service.getSYSDateConf();
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		binolplscf11Service.saveCoder(map);
	}


}

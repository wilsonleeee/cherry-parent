/*
 * @(#)BINOLPLUPM02_BL.java     1.0 2010/12/29
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
package com.cherry.pl.upm.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.pl.upm.service.BINOLPLUPM02_Service;
/**
 * 用户添加 BL
 * 
 */
public class BINOLPLUPM02_BL {
	
	@Resource
	private BINOLPLUPM02_Service binolplupm02_Service;
	
	/**
	 * 用户添加插表处理
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_addUser(Map<String, Object> map) throws Exception {
		map = CherryUtil.removeEmptyVal(map);
		// 系统时间
		String sysDate = binolplupm02_Service.getSYSDate();
		// 作成日时
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLUPM02");
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLUPM02");
		// 插入用户信息表
		binolplupm02_Service.insertUser(map);
	}
	
	/**
	 * 取得密码安全配置信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getPassWordConfig (Map<String, Object> map) {
		// 取得发货单信息
		return binolplupm02_Service.getPassWordConfig(map);
	}
	
	/**
	 * 验证是否存在同样的登入账号
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public String getLoginNameCheck(Map<String, Object> map) {
		
		// 验证是否存在同样的用户信息ID
		return binolplupm02_Service.getLoginNameCheck(map);
	}
}

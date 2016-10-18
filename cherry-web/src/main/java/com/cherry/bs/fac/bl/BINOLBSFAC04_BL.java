/*		
 * @(#)BINOLBSFAC04_BL.java     1.0 2011/02/17		
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
package com.cherry.bs.fac.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.fac.service.BINOLBSFAC04_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 生产厂商添加BL
 * 
 * @author lipc
 * @version 1.0 2011.02.17
 */
@SuppressWarnings("unchecked")
public class BINOLBSFAC04_BL {

	@Resource
	private BINOLBSFAC04_Service binolbsfac04Service;

	/**
	 * 取得生产厂商ID
	 * 
	 * @param map
	 * @return
	 */
	public String getFactoryId(Map<String, Object> map) {

		return binolbsfac04Service.getFactoryId(map);
	}

	/**
	 * 插入生产厂商信息
	 * 
	 * @param map
	 * @return
	 */
	public void tran_insertFactory(Map<String, Object> map)
			throws CherryException, Exception {
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSFAC04");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSFAC04");
		// 数据库系统时间
		String sysDate = binolbsfac04Service.getSYSDate();
		// 作成时间
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 插入生产厂商信息表,返回生产厂商ID
		int manufacturerInfoId = binolbsfac04Service.insertFactory(map);
		// 生产厂商ID
		map.put("manufacturerInfoId", manufacturerInfoId);
		// 地址信息
		String addressInfo = (String) map.get("addressInfo");
		if (!CherryChecker.isNullOrEmpty(addressInfo, true)) {
			// 地址List
			List<Map<String, Object>> addrList = (List<Map<String, Object>>) JSONUtil
					.deserialize(addressInfo);
			if (null != addrList) {
				for (Map<String, Object> addr : addrList) {
					Map<String, Object> addrMap = CherryUtil.removeEmptyVal(addr);
					if(null != addrMap.get("address")){
						addrMap.putAll(map);
						// 插入地址信息表，返回地址信息ID
						int addressInfoId = binolbsfac04Service.insertAddrInfo(addrMap);
						addrMap.put("addressInfoId", addressInfoId);
						// 插入生产厂商地址表
						binolbsfac04Service.insertManufacturerAddress(addrMap);
					}
				}
			}
		}
	}
}

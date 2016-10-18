/*		
 * @(#)BINOLBSFAC03_BL.java     1.0 2011/02/22		
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

import com.cherry.bs.fac.service.BINOLBSFAC03_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 厂商编辑BL
 * 
 * @author lipc
 * @version 1.0 2011.02.22
 */
@SuppressWarnings("unchecked")
public class BINOLBSFAC03_BL {

	@Resource
	private BINOLBSFAC03_Service binolbsfac03Service;

	/**
	 * 取得生产厂商ID
	 * 
	 * @param map
	 * @return
	 */
	public String getFactoryId(Map<String, Object> map) {

		return binolbsfac03Service.getFactoryId(map);
	}

	/**
	 * 取得生产厂商基本信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getFacInfo(Map<String, Object> map) {

		return binolbsfac03Service.getFacInfo(map);
	}

	/**
	 * 取得厂商地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAddList(Map<String, Object> map) {

		return binolbsfac03Service.getAddList(map);
	}

	/**
	 * 厂商信息更新
	 * 
	 * @param map
	 * @return
	 */
	public void tran_updateFactory(Map<String, Object> map)
			throws CherryException, Exception {
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLBSFAC03");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLBSFAC03");
		// 数据库系统时间
		String sysDate = binolbsfac03Service.getSYSDate();
		// 更新时间
		map.put(CherryConstants.UPDATE_TIME, sysDate);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 更新厂商信息表
		int count = binolbsfac03Service.updFacInfo(map);
		if (count == 0) {
			throw new CherryException("");
		}
		// 取得厂商地址信息参数
		String addressInfo = (String) map.get("addressInfo");
		if (!CherryChecker.isNullOrEmpty(addressInfo, true)) {
			// 员工地址List
			List<Map<String, Object>> addrList = (List<Map<String, Object>>) JSONUtil
					.deserialize(addressInfo);
			if (null != addrList) {
				for (Map<String, Object> addr : addrList) {
					// 添加,更新,删除操作(0:删除,1:更新,其它:添加)
					String option = (String) addr.get("option");
					addr.putAll(map);
					// 操作类型为删除，或者操作类型为更新但地址为空
					if ("0".equals(option)
							|| ("1".equals(option) && CherryChecker
									.isNullOrEmpty(addr.get("address")))) { // 删除操作
						// 删除厂商地址信息
						binolbsfac03Service.delFacAddress(addr);
						// 删除地址信息
						binolbsfac03Service.delAddrInfo(addr);
					} else if ("1".equals(option)) { // 更新操作
						// 更新厂商地址信息
						binolbsfac03Service.updFacAddress(addr);
						// 更新地址信息
						binolbsfac03Service.updAddrInfo(addr);
					} else { // 添加操作
						if (null != addr.get("address")) {
							// 插入地址信息表，返回地址信息ID
							int addressInfoId = binolbsfac03Service
									.insertAddrInfo(addr);
							addr.put("addressInfoId", addressInfoId);
							// 插入厂商地址信息表
							binolbsfac03Service.insertManufacturerAddress(addr);
						}
					}
				}
			}
		}
	}
}

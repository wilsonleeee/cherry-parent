/*
 * @(#)BINOLSTJCS11_BL.java     1.0 2014/06/20
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
/**
 * 
 * 逻辑仓库与业务关联
 * @author LuoHong
 * @version 1.0 2011.08.25
 * 
 **/
package com.cherry.st.jcs.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * BB柜台维护service
 * @author zhangle
 * @version 1.0 2014.06.20
 */
public class BINOLSTJCS11_Service extends BaseService{
	
	/**
	 * 获取BB柜台count
	 * @param map
	 * @return
	 */
	public int getBBCounterCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS11.getBBcounterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取BB柜台List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBBCounterList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS11.getBBcounterList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取BB柜台List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllBBCounterList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS11.getAllBBCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据ID获取BB柜台
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBBCounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS11.getBBcounter");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 根据code获取柜台
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterInfoByCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS11.getCounterInfoByCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 新增BB柜台
	 * @param map
	 */
	public int addBBCounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS11.addBBcounter");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新BB柜台
	 * @param map
	 */
	public int updateBBcounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS11.updateBBcounter");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 批量更新BB柜台
	 * @param map
	 */
	public void updateBBcounter(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINOLSTJCS11.updateBBcounter");
	}
	
	/**
	 * 批量添加BB柜台
	 * @param list
	 */
	public void addBBCounter(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLSTJCS11.addBBcounterBatch");
	}
}

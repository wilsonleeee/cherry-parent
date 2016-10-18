/*
 * @(#)BINOLSTJCS01_BL.java     1.0 2011/04/11
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
package com.cherry.st.jcs.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.st.jcs.interfaces.BINOLSTJCS01_IF;
import com.cherry.st.jcs.service.BINOLSTJCS01_Service;

public class BINOLSTJCS01_BL implements BINOLSTJCS01_IF{
	
	@Resource
	private BINOLSTJCS01_Service binOLSTJCS01_service;
	
	/**
	 * 取得仓库List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map) {
		
		return binOLSTJCS01_service.getDepotInfoList(map);
	}
	
	/**
	 * 取得实体仓库总数
	 * 
	 * 
	 * */
	public int getDepotInfoCount(Map<String, Object> map) {
		
		return binOLSTJCS01_service.getDepotInfoCount(map);
	}
	
	/**
     * 仓库启用
     * 
     * @param map
     * @return
     * @throws CherryException 
     */
	@Override
    public int tran_enable(Map<String, Object> map) throws CherryException{
        return binOLSTJCS01_service.enable(map);
    }
	
	/**
     * 仓库停用
     * 
     * @param map
     * @return
     * @throws CherryException 
     */
	@Override
    public int tran_disable(Map<String, Object> map) throws CherryException{
        return binOLSTJCS01_service.disable(map);
    }
	
	/**
     * 仓库是否存在
     * 
     * @param map
     * @return
     * @throws CherryException 
     */
	@Override
    public String getCount(Map<String, Object> map) throws CherryException{
        return binOLSTJCS01_service.getCount(map);
    }
	
	public String testType(Map<String, Object> map) throws CherryException{
	        return binOLSTJCS01_service.testType(map);
	}
	
	/**
     * 编辑保存
     * 
     * @param map
     * @return
     * @throws CherryException 
     */
	@Override
    public int editInventoryInfo(Map<String, Object> map) throws CherryException{
        return binOLSTJCS01_service.editInventoryInfo(map);
    }
	
	@Override
    public int editDepot(Map<String, Object> map) throws CherryException{
        return binOLSTJCS01_service.editDepot(map);
    }
	
	/**
     * 添加保存
     * 
     * @param map
     * @return
     * @throws CherryException 
     */
	@Override
    public int add(Map<String, Object> map) throws CherryException{
        return binOLSTJCS01_service.add(map);
    }
	
	/**
	 * 取得仓库List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getInventoryInfoList(Map<String, Object> map) {
		return binOLSTJCS01_service.getInventoryInfoList(map);
	}
	
	/**
	 * 取得仓库详细
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> inventoryInfoDetail(Map<String, Object> map) {
		return binOLSTJCS01_service.inventoryInfoDetail(map);
	}

}

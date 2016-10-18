/*  
 * @(#)BINOLSTJCS01_IF.java     1.0 2011/05/31      
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
package com.cherry.st.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTJCS01_IF extends ICherryInterface{
	/**
	 * 取得仓库List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map);
	
	/**
	 * 取得实体仓库总数
	 * 
	 * 
	 * */
	public int getDepotInfoCount(Map<String, Object> map);
	
	/**
	 * 启用实体仓库
	 * 
	 * */
	public int tran_enable(Map<String, Object> map) throws CherryException;
    
	/**
	 * 停用实体仓库
	 * 
	 * */
    public int tran_disable(Map<String, Object> map) throws CherryException;
    
    /**
     * 判断实体仓库是否存在
     * 
     * */
    public String getCount(Map<String, Object> map) throws CherryException;
    
    public String testType(Map<String, Object> map) throws CherryException;
    
    public int editInventoryInfo(Map<String, Object> map) throws CherryException;
    
    public int editDepot(Map<String, Object> map) throws CherryException;
    
    public int add(Map<String, Object> map) throws CherryException;
    
    public List<Map<String, Object>> getInventoryInfoList(Map<String, Object> map);
    
    public List<Map<String, Object>> inventoryInfoDetail(Map<String, Object> map);
}
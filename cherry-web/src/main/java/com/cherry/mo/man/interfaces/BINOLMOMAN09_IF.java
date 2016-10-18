/*
 * @(#)BINOLMOMAN01_IF.java     1.0 2011/3/15
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
package com.cherry.mo.man.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;


/**
 * 
 * POS菜单管理Interface
 * 
 * @author 
 * @version 1.0 
 */
public interface BINOLMOMAN09_IF extends ICherryInterface{
	
	/**
     * 取得POS菜单总数
     * 
     * @param map
     * @return POS菜单总数
     */
    public int searchMemuInfoCount(Map<String, Object> map);
    
    /**
     * 取得POS菜单List  
     * 
     * @param map
     * @return POS菜单List
     */
    public List<Map<String, Object>> searchPosMemuInfoList(Map<String, Object> map);
    
    /**
	 * 取得POS菜单INFO
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getPosMemu(Map<String, Object> map);
	
	 /**
     * 修改菜单数据
     * 
     * @param map
     */
    public void  tran_updatePosMemu(Map<String, Object> map) throws Exception;

    /**
     * 新增菜单数据
     * 
     * @param map
     */
    public int  tran_addPosMemu(Map<String, Object> map) throws Exception;
    /**
     * 删除菜单数据
     * 
     * @param map
     */
    public void  tran_deletePosMemu(Map<String, Object> map) throws Exception;
    
    public void getLogInvWSMap(Map<String, Object> map)throws Exception;
    
}

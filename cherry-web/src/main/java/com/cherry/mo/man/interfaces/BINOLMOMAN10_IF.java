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

import com.cherry.cm.core.ICherryInterface;


/**
 * 
 * POS品牌菜单管理Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
public interface BINOLMOMAN10_IF extends ICherryInterface{

	 /**
     * 取得POS品牌菜单管理List 
     * 
     * @param map
     * @return POS配置项List
     */
    public List<Map<String, Object>> searchPosMenuBrandInfoList(Map<String, Object> map);
    
    /**
     * 修改POS品牌菜单管理
     * 
     * @param map
     */
    public void  tran_updatePosMenuBrand(Map<String, Object> map) throws Exception;
    
    /**
     * 修改POS品牌菜单管理MenuStatus
     * 
     * @param map
     */
    public void  tran_updatePosMenuBrandMenuStatus(Map<String, Object> map) throws Exception;
    
    /**
     * 新增POS品牌菜单管理
     * 
     * @param map
     */
    public int  tran_addPosMenuBrand(Map<String, Object> map) throws Exception;
    
    /**
     * 取得POS品牌菜单管理List（下发） 
     * 
     * @param map
     * @return POS配置项List
     */
    public void searchPosMenuBrandInfoWithWS(Map<String, Object> map)throws Exception;
    
    public void getLogInvWSMap(Map<String, Object> map)throws Exception;

}

/*
 * @(#)BINOLSSPRM63_IF.java     1.0 2013/01/25
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
package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 入库Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.01.25
 */
public interface BINOLSSPRM63_IF extends ICherryInterface{
    /**
     * 保存入库信息
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception
     */
    public int tran_save(Map<String,Object> map,List<String[]> list,UserInfo userinfo) throws Exception; 
    
    /**
     * 提交入库信息
     * @param map
     * @param list
     * @param userinfo
     * @return
     * @throws Exception
     */
    public int tran_submit(Map<String, Object> map, List<String[]> list, UserInfo userinfo) throws Exception;
    
    /**
     * 根据部门ID取部门编号名称
     * @param map
     * @return
     */
    public String getDepart(Map<String, Object> map);
}
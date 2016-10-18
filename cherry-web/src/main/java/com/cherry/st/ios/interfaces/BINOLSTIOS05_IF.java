/*
 * @(#)BINOLSTIOS05_IF.java     1.0 2011/8/31
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
package com.cherry.st.ios.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 自由盘点Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.8.31
 */
public interface BINOLSTIOS05_IF extends ICherryInterface{
    
    /**
     * 保存盘点信息
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception
     */
    public int tran_save(Map<String,Object> map,List<String[]> list,UserInfo userinfo) throws Exception;
    
    /**
     * 提交盘点信息
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception
     */
    public int tran_submit(Map<String,Object> map,List<String[]> list,UserInfo userinfo) throws Exception;

    /**
     * 取得实体仓库的所属部门
     * @return
     */
    public int getOrganIdByDepotID(Map<String,Object> map);
}

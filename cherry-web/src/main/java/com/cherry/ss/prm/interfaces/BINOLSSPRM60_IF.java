/*
 * @(#)BINOLSSPRM60_IF.java     1.0 2012/09/27
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
 * 促销品移库IF
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public interface BINOLSSPRM60_IF extends ICherryInterface{

    /**
     * 保存移库记录
     * @param map
     * @param list
     * @param userinfo
     * @return
     * @throws Exception
     */
    public int tran_saveShiftRecord(Map<String,Object> map,List<String[]> list,UserInfo userinfo) throws Exception;
}
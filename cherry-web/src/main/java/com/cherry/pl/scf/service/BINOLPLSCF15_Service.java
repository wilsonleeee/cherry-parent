/*
 * @(#)BINOLPLSCF15_Service.java     1.0 2011/12/19
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

package com.cherry.pl.scf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 业务流程配置Service
 * 
 * @author niushunjie
 * @version 1.0 2011.12.19
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF15_Service extends BaseService {
    /**
     * 取得流程配置文件List
     * @param map
     * @return
     */
    public List getFileStoreList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF15.getFileStoreList");
        return baseConfServiceImpl.getList(paramMap);
    }
}

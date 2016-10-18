/*
 * @(#)BINOLPLSCF15_IF.java     1.0 2011/12/19
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
package com.cherry.pl.scf.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 业务流程配置IF
 * 
 * @author niushunjie
 * @version 1.0 2011.12.19
 */
public interface BINOLPLSCF15_IF {

    /**
     * 取得业务流程配置
     * @param map
     * @return
     */
    public List<Map<String,Object>> getFlowList(Map<String,Object> map);
}

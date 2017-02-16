/*
 * @(#)BINOLMBRPT01_Service.java     1.0 2013/10/12
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
package com.cherry.mb.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员信息完善度报表Service
 *
 * @author WangCT
 * @version 1.0 2017/01/23
 */
public class BINOLMBRPT14_Service extends BaseService {

    /**
     * 取得会员信息完善度总数
     *
     * @param map
     * @return
     */
    public int searchMemberCompleteCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT14.searchMemberCompleteCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得会员信息完善度List
     *
     * @param map
     * @return
     */
    public List searchMemberCompleteList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT14.searchMemberCompleteList");
        return baseServiceImpl.getList(map);
    }

}

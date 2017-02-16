/*
 * @(#)BINBEMBTIF01_Service.java     1.0 2015/06/24
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
package com.cherry.mb.arc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.service.BaseService;

/**
 * 计算会员完善度处理Service
 *
 * @author nanjunbo
 * @version 1.0 2017/02/09
 */
public class BINBEMBARC09_Service extends BaseService{

    /**
     * 取得需要计算的会员信息List
     *
     * @param map
     * 			查询参数
     * @return List
     * 			需要计算的会员信息List
     *
     */
    public List<Map<String, Object>> getMemCompleteRuleSyncList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID,
                "BINBEMBARC09.getMemCompleteRuleSyncList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }

    /**
     * 取得当前是否有未执行的规则
     * @param map
     */
    public Map<String, Object> getMemCompleteRuleValid(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC09.getMemCompleteRuleValid");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }

    /**
     *
     * 更新会员完善度规则执行flag
     *
     * @param map
     * @return
     */
    public int updateMemRuleFlag(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC09.updateMemRuleFlag");
        return baseServiceImpl.update(map);
    }

    /**
     * 取得需要执行规则的最大会员的id
     * @param map
     */
    public Map<String, Object> getMemIdMax(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC09.getMemIdMax");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }

    /**
     *
     * 更新需要会员的最大id
     *
     * @param map
     * @return
     */
    public int updateMemRuleMaxNeedMemId(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC09.updateMemRuleMaxNeedMemId");
        return baseServiceImpl.update(map);
    }

    /**
     *
     * 更新现已更新的会员id
     *
     * @param map
     * @return
     */
    public int updateMemRuleMaxMemId(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBARC09.updateMemRuleMemId");
        return baseServiceImpl.update(map);
    }

}

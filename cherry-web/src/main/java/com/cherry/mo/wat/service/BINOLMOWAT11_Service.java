/*
 * @(#)BINOLMOWAT10_Action.java     1.0 2015/12/11 
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
package com.cherry.mo.wat.service;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * Job失败履历信息查询
 * 
 * @author lzs
 * @version 1.0 2015.12.11
 */
public class BINOLMOWAT11_Service extends BaseService{
    /**
     * Job失败履历信息总数
     * 
     * @param map 查询条件
     * @return Job失敗履历信息总数
     */
    public int getCountJobRunFaildHistory(Map<String, Object> map) {
    	
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT11.getCountJobRunFaildHistory");
        return baseServiceImpl.getSum(map);
    }
    
    /**
     * Job失败履历信息List
     * 
     * @param map 查询条件
     * @return Job失敗履历信息List
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getJobRunFaildHistoryList(Map<String, Object> map) {
    	
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOWAT11.getJobRunFaildHistoryList");
    	return baseServiceImpl.getList(map);
    }
   
}

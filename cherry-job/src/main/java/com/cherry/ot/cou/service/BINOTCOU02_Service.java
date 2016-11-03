/*
 * @(#)BINOTCOU02_Service.java     1.0 2014/11/13
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
package com.cherry.ot.cou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * BATCH薇诺娜优惠劵获取Service
 * 
 * @author menghao
 * @version 2014.11.13
 */
public class BINOTCOU02_Service extends BaseService {

	/**
     * 查询电商接口信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getESInterfaceInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTCOU02.getESInterfaceInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 更新电商接口信息表
     * @param map
     * @return
     */
    public int updateESInterfaceInfoLastTime(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTCOU02.updateESInterfaceInfoLastTime");
        return baseServiceImpl.update(paramMap);
    }
    
	/**
	 * 将取得的优惠券写入数据
	 * @param list
	 */
	public int mergeCouponInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTCOU02.mergeCouponInfo");
		return ConvertUtil.getInt(baseServiceImpl.get(paramMap));
	}
}

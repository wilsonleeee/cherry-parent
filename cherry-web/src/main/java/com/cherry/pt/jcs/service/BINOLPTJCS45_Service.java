/*
 * @(#)BINOLPTJCS45_Service.java     1.0 2010/11/03
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
package com.cherry.pt.jcs.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 *方案一览（service）
 * 
 * @author jijw
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS45_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得渠道总数
	 * 
	 * @param map
	 * @return
	 */
	public int getSolutionCount(Map<String, Object> map) {
    	//业务类型   0：基础数据；1：库存数据；2：会员数据； A:全部 等
		map.put("businessType", "0");
    	//操作类型   0：更新（包括新增，删除）；1：查询
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS45.getSolutionCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得方案List
	 * 
	 * @param map
	 * @return
	 */
	public List getSolutionList(Map<String, Object> map) {
    	//业务类型   0：基础数据；1：库存数据；2：会员数据； A:全部 等
		map.put("businessType", "0");
    	//操作类型   0：更新（包括新增，删除）；1：查询
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS45.getSolutionList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 方案停用/启用
     * 
     * @param map
     * @return
     */
    public int disOrEnableSolu(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS45.disOrEnableSolu");
        return baseServiceImpl.update(map);
    }
    
}
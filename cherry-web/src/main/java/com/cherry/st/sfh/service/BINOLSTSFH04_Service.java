/*
 * @(#)BINOLSTSFH04_Service.java     1.0 2011/09/14
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
package com.cherry.st.sfh.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
/**
 * 
 * 产品发货单一览
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH04_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得产品发货单总数
	 * 
	 * @param map
	 * @return 产品发货单总数
	 */
	public int getProductDeliverCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH04.getProductDeliverCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得产品发货单List
	 * 
	 * @param map
	 * @return 产品发货单List
	 */
	public List getProductDeliverList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH04.getProductDeliverList");
		return baseServiceImpl.getList(map);
	}
	
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH04.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
    
	/**
	 * 取得产品发货导出总数
	 * 
	 * @param map
	 * @return 产品发货导出总数
	 */
	public int getDeliverExportCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH04.getDeliverExportCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得产品发货明细导出List
	 * 
	 * @param map
	 * @return 产品发货导出List
	 */
	public List getDeliverExportList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH04.getDeliverExportList");
		return baseServiceImpl.getList(map);
	} 
}

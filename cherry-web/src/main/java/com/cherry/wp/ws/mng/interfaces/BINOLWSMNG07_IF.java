/*
 * @(#)BINOLWSMNG07_IF.java     1.0 2015-10-29
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
package com.cherry.wp.ws.mng.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;

/**
 * 
 * @ClassName: BINOLWSMNG07_IF 
 * @Description: TODO(盘点申请interface) 
 * @author menghao
 * @version v1.0.0 2015-10-29 
 *
 */
public interface BINOLWSMNG07_IF {
	
    /**
     * 查询是否存在需要审核的盘点单或者退库申请单
     * @param map
     * @return
     */
	public List<Map<String, Object>> getAuditBill(Map<String, Object> map);

	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
    public List<Map<String, Object>> getAllCntPrtStockList(Map<String, Object> map) throws Exception;

    /**
     * 盘点指定商品
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getGivenCntPrtStockList(Map<String, Object> map) throws Exception;
    
    /**
     * 保存盘点申请单据
     * @param map
     * @param list
     * @param userinfo
     * @return
     * @throws Exception
     */
	public int tran_save(Map<String, Object> map, List<String[]> list,
			UserInfo userinfo) throws Exception;

	/**
	 * 提交盘点申请单
	 * @param map
	 * @param list
	 * @param userinfo
	 * @return
	 * @throws Exception
	 */
	public int tran_submit(Map<String, Object> map, List<String[]> list,
			UserInfo userinfo) throws Exception;
}
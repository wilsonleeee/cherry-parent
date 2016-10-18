/*
 * @(#)BINOLSSPRM18_BL.java     1.0 2010/11/03
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
package com.cherry.ss.prm.bl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.ss.prm.service.BINOLSSPRM18_Service;

/**
 * 
 * 我的发货单
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM18_BL {
	@Resource
	private BINOLSSPRM18_Service binolssprm18_Service;

	/**
	 * 取得发货单总数
	 * 
	 * @param map
	 * @return
	 */
	public int searchDeliverCount(Map<String, Object> map) {
		// 取得发货单总数
		return binolssprm18_Service.getDeliverCount(map);
	}
	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	public List searchDeliverList(Map<String, Object> map) {
		// 取得发货单List
		return binolssprm18_Service.getDeliverList(map);
	}
	
	   
	/**
     * 取得产品总数量和总金额
     */
    public Map getSumInfo(Map<String,Object> map){
        return binolssprm18_Service.getSumInfo(map);
    }
}

/*
 * @(#)BINOLSTSFH05_Service.java     1.0 2011/09/14
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 产品发货单详细Service
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
@SuppressWarnings("unchecked")
public class BINOLSTSFH05_Service extends BaseService{
    /**
     * 删除发货单明细
     * 
     * */
    public void deleteProductDeliverDetail(Map<String,Object> paramMap){
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	list.add(paramMap);
    	baseServiceImpl.deleteAll(list, "BINOLSTSFH05.deleteProductDeliverDetail");
    }
    
    /**
     * 删除【产品发货单据表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductDeliverLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH05.deleteProductDeliverLogic");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 删除【产品发货单据明细表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductDeliverDetailLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH05.deleteProductDeliverDetailLogic");
        return baseServiceImpl.update(map);
    }
}

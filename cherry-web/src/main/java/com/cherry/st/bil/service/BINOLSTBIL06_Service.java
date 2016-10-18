/*
 * @(#)BINOLSTBIL06_Service.java     1.0 2011/10/20
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
package com.cherry.st.bil.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 报损单明细一览
 * @author niushunjie
 * @version 1.0 2011.10.20
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL06_Service extends BaseService{
    /**
     * 删除【产品报损单据表】伦理删除
     * @param map
     * @return
     */
    public int deleteOutboundFreeLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL06.deleteOutboundFreeLogic");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 删除【产品报损单据明细表】伦理删除
     * @param map
     * @return
     */
    public int deleteOutboundFreeDetailLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL06.deleteOutboundFreeDetailLogic");
        return baseServiceImpl.update(map);
    }
    /**
     * 删除【产品报损单据明细表】
     * @param map
     * @return
     */
    public int deleteOutboundFreeDetail(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL06.deleteOutboundFreeDetail");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 取得实体仓库的所属部门
     * @param map
     * @return
     */
    public List<Map<String,Object>> getOrganIdByDepotInfoID(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL06.getOrganIdByDepotInfoID");
        return baseServiceImpl.getList(map);
    }
}

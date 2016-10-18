/*
 * @(#)BINOLSTCM20_Service.java     1.0 2015-10-8 
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
package com.cherry.st.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLSTCM20_Service 
 * @Description: TODO(大仓加权平均成本价格表操作共通Service) 
 * @author menghao
 * @version v1.0.0 2015-10-8 
 *
 */
public class BINOLSTCM20_Service extends BaseService {

    /**
     * 根据入库单明细，获取相应的入库产品的库存、入库数量、入库价及最新的加权平均价
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductWeightedAvgInfo(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM20.getProductWeightedAvgInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 批量插入大仓加权平均成本价格表
     * @param map
     * @return
     */
    public void insertProductWeightedAvgPrice(List<Map<String, Object>> list){
    	baseServiceImpl.saveAll(list,"BINOLSTCM20.insertProductWeightedAvgPrice");
    }
    
}

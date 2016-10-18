/*
 * @(#)BINOLSSCM08_Service.java     1.0 2012/09/27
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
package com.cherry.ss.common.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

/**
 * 
 * 促销品移库操作共通Service
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSCM08_Service extends BaseService{
    /**
     * 往产品移库主表中插入数据并返回记录ID
     * 
     * 
     * */
    public int insertPromotionShift(Map<String,Object> map){
        return (Integer)baseServiceImpl.saveBackId(map, "BINOLSSCM08.insertPromotionShift");
    }
    
    /**
     * 往移库明细表中插入数据
     * 
     * 
     * */
    public void insertPromotionShiftDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list, "BINOLSSCM08.insertPromotionShiftDetail");
    }
    
    /**
     * 更新移库单主表
     * @param map
     * @return
     */
    public int updatePromotionShiftMain(Map<String,Object> map){        
        return baseServiceImpl.update(map, "BINOLSSCM08.updatePromotionShiftMain");
    }
    
    /**
     * 作废移库单明细
     * @param map
     * @return
     */
    public int deletePromotionShiftDetail(Map<String,Object> map){        
        return baseServiceImpl.update(map, "BINOLSSCM08.deletePromotionShiftDetail");
    }
    
    /**
     * 取得移库单概要信息
     * @param map
     * @return
     */
    public Map<String,Object> getPromotionShiftMainData(Map<String,Object> map){        
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLSSCM08.getPromotionShiftMainData");
    }
    
    /**
     * 取得移库单明细信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPromotionShiftDetailData(Map<String,Object> map){        
        return baseServiceImpl.getList(map, "BINOLSSCM08.getPromotionShiftDetailData");
    }
}
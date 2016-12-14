package com.cherry.tp.duiba.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.Map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 兑吧Service
 */
public class BINOLDUIBA01_Service extends BaseService{

    public Map getMemberInfoByOpenID (Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLDUIBA01.getMemberInfoByOpenID");
        return (Map)baseServiceImpl.get(map);
    }
    /**
     * 查询兑换请求记录表的卡号和兑换积分
     * @param map
     * @return
     */
    public Map getBIN_ExchangeRequest(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLDUIBA01.getBIN_ExchangeRequest");
        return (Map)baseServiceImpl.get(map);
    }

    /**
     * 更新兑换请求记录表
     * @param map
     * @return
     */
    public int updateExchangeRequest(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLDUIBA01.updateExchangeRequest");
        return baseServiceImpl.update(map);
    }

    /**
     * 插入兑吧兑换请求记录表
     * @param
     * @return
     *
     */
    public void insertDuiBaExchangeRequest(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLDUIBA01.insertDuiBaExchangeRequest");
        baseServiceImpl.save(map);
    }
}

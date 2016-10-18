package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLSTSFH15_Service extends BaseService{
	/**
     * 获取销售单据List
     * 
     * @param map
     * @return
     * 		销售单据List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSaleOrdersList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH15.getSaleOrdersList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取销售单据数量
     * 
     * @param map
     * @return
     * 		销售单据数量
     */
	public int getSaleOrdersCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH15.getSaleOrdersCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
    /**
     * 获取销售明细单据数量
     * 
     * @param map
     * @return
     *      销售单据数量
     */
    public int getSaleDetailCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH15.getSaleDetailCount");
        return CherryUtil.obj2int(baseServiceImpl.get(map));
    }
    
    /**
     * 获取销售明细List
     * 
     * @param map
     * @return
     *      销售单据明细List
     */
    public List<Map<String, Object>> getSaleDetailList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH15.getSaleDetailList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH15.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
}

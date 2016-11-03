package com.cherry.webservice.counter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class CounterInfoService extends BaseService{
	
	 /**
     * @author ZhaoCF
     * 根据柜台号取得品牌ID
     * 
     * @param map 查询条件
     * @return 品牌ID 
     *   
     */
    public String getBrandInfoId(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "CounterInfo.getBrandInfoId");
        return (String)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 查询柜台信息
     * 
     * @param map
     * @return 柜台信息
     */
    public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map); 
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "CounterInfo.getCounterList");
		return baseServiceImpl.getList(paramMap);
	}
}

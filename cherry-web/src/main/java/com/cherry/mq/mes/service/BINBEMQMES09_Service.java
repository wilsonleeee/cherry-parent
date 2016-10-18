package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINBEMQMES09_Service extends BaseService{
	
	/**
	 * 更新机器信息
	 * 
	 * @param map
	 * */
	public int updateMachInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES09.updateMachInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
     * 更新机器绑定柜台信息
     * 
     * @param map
     * */
    public int updateMachineCodeCollate(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES09.updateMachineCodeCollate");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 更新柜台产品订货参数表
     * @param map
     * @return 更新件数
     */
    public int updateCounterPrtOrParameter(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES09.updateCounterPrtOrParameter");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入柜台产品订货参数表
     * @param map
     */
    public void insertCounterPrtOrParameter(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES09.insertCounterPrtOrParameter");
        baseServiceImpl.save(parameterMap);
    }
}
package com.cherry.mb.cct.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLMBCCT08_Service extends BaseService{
	/**
     * 获取非会员记录List
     * 
     * @param map
     * @return
     * 		非会员记录List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCustomerList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT08.getCustomerList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取非会员记录数量
     * 
     * @param map
     * @return
     * 		非会员数量
     */
    public int getCustomerCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT08.getCustomerCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
    }
}

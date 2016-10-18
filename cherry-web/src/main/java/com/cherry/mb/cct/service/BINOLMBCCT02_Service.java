package com.cherry.mb.cct.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLMBCCT02_Service extends BaseService{
	/**
     * 根据客户ID获取客户问题记录List
     * 
     * @param map
     * @return
     * 		客户问题记录List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getIssueListByCustomer(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT02.getIssueListByCustomer");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 根据客户ID获取客户问题记录数量
     * 
     * @param map
     * @return
     * 		客户问题记录数量
     */
    public int getIssueCountByCustomer(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT02.getIssueCountByCustomer");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
    }
}

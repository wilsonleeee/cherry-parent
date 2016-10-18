package com.cherry.mb.cct.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLMBCCT10_Service extends BaseService{
	/**
     * 获取问题记录List
     * 
     * @param map
     * @return
     * 		问题记录List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getIssueList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT10.getIssueList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取来电记录数量
     * 
     * @param map
     * @return
     * 		来电记录数量
     */
    public int getIssueCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT10.getIssueCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
    }
    
    /**
     * 获取问题记录详细List
     * 
     * @param map
     * @return
     * 		问题记录详List
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getIssueExcelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT10.getIssueExcelList");
        return baseServiceImpl.getList(paramMap);
    }
    
    public int getIssueExcelCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT10.getIssueExcelCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
    }
}

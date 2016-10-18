package com.cherry.ct.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLCTRPT01_Service extends BaseService{
	/**
     * 历史沟通计划记录List
     * 
     * @param map
     * @return
     * 		沟通计划运行List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPlanRunDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT01.getPlanRunDetailList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 历史沟通计划运行记录统计
     * 
     * @param map
     * @return
     * 		沟通计划运行统计
     */
	public int getPlanRunDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT01.getPlanRunDetailCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 历史沟通计划运行记录统计信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlanRunTotalInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT01.getPlanRunTotalInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
}

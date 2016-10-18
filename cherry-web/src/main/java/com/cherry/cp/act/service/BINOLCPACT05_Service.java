package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLCPACT05_Service extends BaseService{
	/**
     *取得档次信息
     * 
     * @param map
     * @return List
     * 
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubCampList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT05.getSubCampList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 
	 * 查询会员
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 * 
	 */
	public Map<String, Object> getMemInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT05.getMemInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
     *取得礼品信息List
     * 
     * @param map
     * @return List
     * 
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtList(Map<String, Object> map) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.putAll(map);
		p.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT05.getPrtList");
		return baseServiceImpl.getList(p);
	}
	
	/**
	 * 礼品信息Map
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPrtInfo(Map<String, Object> map) {
		Map<String, Object> p = new HashMap<String, Object>(map);
		p.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT05.getPrtInfo");
		return (Map<String, Object>) baseServiceImpl.get(p);
	}
	  /**
     * 取得会员活动信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
    public Map<String, Object> getCampaignInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动子规则ID
		paramMap.put("campaignId", map.get("campaignId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT05.getCampaignInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
	 * 取得柜台部门ID
	 * @param map
	 * @return
	 */
	public Integer getCntDepartId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT05.getCntDepartId");
		return (Integer) baseServiceImpl.get(map);
	}
}

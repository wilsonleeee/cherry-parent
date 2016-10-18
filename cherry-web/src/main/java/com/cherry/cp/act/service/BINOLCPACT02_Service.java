package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cp.common.dto.CampaignDTO;

public class BINOLCPACT02_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
     *取得子活动菜单
     * 
     * @param map
     * @return List
     * 		子活动菜单
     */
    public List<Map<String, Object>> getSubMenuList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getSubMenuList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    /**
     * 取得会员活动信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
    public CampaignDTO getCampaignInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动子规则ID
		paramMap.put("campaignId", map.get("campaignId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCampaignInfo");
        return (CampaignDTO) baseServiceImpl.get(paramMap);
    }
    /**
	 * 取得子活动基本信息
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map<String,Object> getSubBaseInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getSubBaseInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	  /**
     * 取得子活动结果信息
     * 
     * @param map
     * @return Map
     * 		组织品牌代码信息
     */
    public List<Map<String, Object>> getSubResultList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getSubResultList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    
    /**
     * 取得子活动结果信息
     * 
     * @param map
     * @return Map
     * 		组织品牌代码信息
     */
    public List<Map<String, Object>> getSubConditionList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getSubConditionList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    /**
	 * 取得渠道LIST
	 * 
	 * @param map 
	 * 				查询条件
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getChannelList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	 /**
	 * 查询系统List
	 * 
	 * @param map 
	 * 				查询条件
	 */
	public List<Map<String, Object>> getFactionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getFactionList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	/**
	 * 取得城市LIST
	 * 
	 * @param map 
	 * 				查询条件
	 * @return 
	 */
	public List<Map<String, Object>> getCityList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getCityList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台LIST
	 * 
	 * @param map 
	 * 				查询条件
	 */
	public List<Map<String, Object>> getCntList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getCntList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
     * 取得会员活动扩展信息
     * 
     * @param map
     * @return Map
     */
    public Map<String, Object> getCampaignExtInfo(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT02.getCampaignExtInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
}

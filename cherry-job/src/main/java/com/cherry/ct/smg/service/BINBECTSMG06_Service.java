package com.cherry.ct.smg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINBECTSMG06_Service extends BaseService{
	/**
     * 通过会员ID获取会员信息
     * 
     * @param map
     * @return
     * 		会员ID
     */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberInfoById(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG06.getMemberInfoById");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
     * 获取在同一次沟通同一个模板情况下给指定号码发送过的短信数量
     * 
     * @param map
     * @return
     * 		信息发送数量
     */
    public int getSmsSendFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG06.getSmsSendFlag");
        return CherryUtil.obj2int(baseServiceImpl.get(paramMap));
    }
    
    /**
     * 获取实时接口配置信息
     * 
     * @param map
     * @return
     * 		配置信息列表
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getConfigInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG06.getConfigInfo");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
     * 获取搜索记录信息
     * 
     * @param map
     * @return
     * 		搜索记录信息
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getSearchInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG06.getSearchInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
}

package com.cherry.mb.cct.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLMBCCT01_Service extends BaseService{
	/**
     * 根据来电号码获取匹配的会员数量
     * 
     * @param map
     * @return
     * 		匹配会员数量
     */
	public int getMemberCountByPhone(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT01.getMemberCountByPhone");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
     * 根据来电号码获取匹配的会员列表
     * 
     * @param map
     * @return
     * 		匹配会员列表
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberListByPhone(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT01.getMemberListByPhone");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
     * 根据来电号码获取匹配的非会员数量
     * 
     * @param map
     * @return
     * 		匹配的非会员数量
     */
	public int getCustomerCountByPhone(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT01.getCustomerCountByPhone");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
     * 根据来电号码获取匹配的非会员列表
     * 
     * @param map
     * @return
     * 		匹配的非会员列表
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCustomerListByPhone(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT01.getCustomerListByPhone");
        return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 新增来电日志
	 * 
	 * @param map
	 * @return 
	 */
	public void insertCallLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBCCT01.insertCallLog");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新来电日志
	 * 
	 * @param map
	 * @return 
	 */
	public void updateCallLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBCCT01.updateCallLog");
		baseServiceImpl.update(map);
	}
}

package com.cherry.ct.pln.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLCTPLN02_Service extends BaseService{
	/**
	 * 获取沟通事件设置列表
	 * 
	 * @param map
	 * @return 
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getEventSetList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN02.getEventSetList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
	 * 停用事件设置
	 * @param map
	 * @return
	 */
	public int stopEventSet (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN02.stopEventSet");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入沟通事件设置信息
	 * 
	 * @param map
	 * @return 
	 */
	public void insertEventSetInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTPLN02.insertEventSetInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入事件延时设置信息
	 * 
	 * @param map
	 * @return 
	 */
	public void insertDelaySetInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTPLN02.insertDelaySetInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新延时设置信息
	 * 
	 * @param map
	 * @return 
	 */
	public void updateDelaySetInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTPLN02.updateDelaySetInfo");
		baseServiceImpl.update(map);
	}
	
	/**
     * 判断延时设置信息是否已存在
     * 
     * @param map
     * @return
     * 		
     */
    public int getDelaySetCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN02.getDelaySetCount");
        return CherryUtil.obj2int(baseServiceImpl.get(paramMap));
    }
    
    /**
	 * 获取沟通事件延时设置列表
	 * 
	 * @param map
	 * @return 
	 */
	@SuppressWarnings("unchecked")
    public Map<String, Object> getDelaySetInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPLN02.getDelaySetInfo");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
	
	/**
	 * 更新调度有效状态
	 * 
	 * @param map
	 * @return 
	 */
	public void updateEventSchedulesFlag(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTPLN02.updateEventSchedulesFlag");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 删除事件延时设置信息
	 * 
	 * @param map
	 * @return
	 */
	public void deleteDelaySetInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTPLN02.deleteDelaySetInfo");
		baseServiceImpl.remove(map);
	}
}

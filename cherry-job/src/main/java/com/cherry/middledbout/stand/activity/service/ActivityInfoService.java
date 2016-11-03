package com.cherry.middledbout.stand.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 促销活动列表导出Service
 * 
 * @author ZhaoCF
 * 
 */
public class ActivityInfoService extends BaseService{
	
	/**
	 * 删除IF_Activity表中数据
	 * @param map 删除条件
	 * 
	 */
	public void deleteActivityInfo(Map<String, Object> map) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.putAll(map);
		paraMap.put(CherryBatchConstants.IBATIS_SQL_ID, "ActivityInfo.deleteActivityInfo");
		tpifServiceImpl.remove(paraMap);
	}
	
	/**
	 * 从数据库中查询促销活动数据
	 * @param map 查询条件
	 * @return 查询的促销List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getActivityInfo(Map<String, Object> map) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.putAll(map);
		paraMap.put(CherryBatchConstants.IBATIS_SQL_ID, "ActivityInfo.getActivityInfo");
		return baseServiceImpl.getList(paraMap);
	}
	/**
	 * 从接口表中查询促销数据
	 * @param map 查询条件
	 * @return 查询的促销List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getActivity(Map<String, Object> map) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.putAll(map);
		paraMap.put(CherryBatchConstants.IBATIS_SQL_ID, "ActivityInfo.getActivity");
		return tpifServiceImpl.getList(paraMap);
	}
	
	/**
	 * 
	 * 插入促销活动数据
	 * @param 
	 */
	public void insertActivityInfo(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "ActivityInfo.insertActivityInfo");
	}
	/**
	 * 取得品牌code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"ActivityInfo.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
}

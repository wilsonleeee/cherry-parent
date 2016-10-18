package com.cherry.mo.cio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

@SuppressWarnings("unchecked")
public class BINOLMOCIO15_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得竞争对手总数
	 * 
	 * @param map
	 * @return
	 */
	public int getRivalCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO15.getRivalCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得竞争对手List
	 * 
	 * @param map
	 * @return
	 */
	public List getRivalList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO15.getRivalList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 添加竞争对手
	 * 
	 * @param map
	 * @return
	 */
	public int addRival(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO15.addRival");
		 return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新竞争对手信息
	 * @param map
	 */
	public void updateRival(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO15.updateRival");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 根据竞争对手ID取得更新前竞争对手所属品牌(用于更新配置数据库的SCS表)
	 * @param map
	 * @return
	 */
	public String getRivalBrandCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO15.getRivalBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(paramMap));
		
	}
	
	/**
	 * 逻辑删除竞争对手
	 * @param map
	 */
	public void deleteRival(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO15.deleteRival");
		baseServiceImpl.remove(paramMap);
	}
	
	public String getCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO15.getCount");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	public String getBrandCode(String brandInfoId) {
		return (String) baseServiceImpl.get(brandInfoId, "BINOLMOCIO15.getBrandCode");
	}
}

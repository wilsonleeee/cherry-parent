package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTSFH14_Service extends BaseService{
	
	/**
	 * 根据品牌ID获取品牌CODE
	 * 
	 * */
	public String getBrandCode(int brandInfoID){
		return (String)baseServiceImpl.get(brandInfoID,"BINOLSTSFH14.getBrandCode");
	}
	
	/**
	 * 获取系统中可销售产品列表
	 * @param map
	 * @return 可销售产品列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProductList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH14.getProductList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据产品条码获取产品详细信息
	 * @param map
	 * @return 产品详细信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProductInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH14.getProductInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
}

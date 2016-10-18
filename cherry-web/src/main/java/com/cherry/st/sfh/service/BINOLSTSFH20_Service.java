package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTSFH20_Service extends BaseService {
	/**
	 * 获取销售单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getBackstageSaleExcelCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH20.getBackstageSaleExcelCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取销售单（Excel导入）信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBackstageSaleExcelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH20.getBackstageSaleExcelList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 通过ID获取单条后台销售单（Excel导入）明细信息
	 * @param map
	 * @return 
	 */
	public List<Map<String, Object>> getBackstageSaleExcelDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH20.getBackstageSaleExcelDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取后台销售单（Excel导入）主数据
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBackstageSaleExcelInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH20.getBackstageSaleExcelInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询导出数据总数
	 * @param map
	 * @return
	 */
	public int getExportDataCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH20.getExportDataCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 查询导出数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExportDataList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH20.getExportDataList");
		return baseServiceImpl.getList(paramMap);
	}
}

package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTSFH18_Service extends BaseService {
	/**
	 * 获取订货单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getPrtOrderExcelCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH18.getPrtOrderExcelCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取订货单（Excel导入）信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtOrderExcelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH18.getPrtOrderExcelList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 通过ID获取单条订货单（Excel导入）明细信息
	 * @param map
	 * @return 
	 */
	public List<Map<String, Object>> getPrtOrderExcelDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH18.getPrtOrderExcelDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取订货单（Excel导入）主数据
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrtOrderExcelInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH18.getPrtOrderExcelInfo");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH18.getExportDataCount");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH18.getExportDataList");
		return baseServiceImpl.getList(paramMap);
	}
}

package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

public class BINOLSTSFH13_Service extends BaseService {

	/**
	 * 获取发货单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getPrtDeliverExcelCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH13.getPrtDeliverExcelCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取发货单（Excel导入）信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtDeliverExcelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH13.getPrtDeliverExcelList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 通过ID获取单条发货单（Excel导入）明细信息
	 * @param map
	 * @return 
	 */
	public List<Map<String, Object>> getPrtDeliverExcelDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH13.getPrtDeliverExcelDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取发货单（Excel导入）主数据
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrtDeliverExcelInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH13.getPrtDeliverExcelInfo");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH13.getExportDataCount");
		return baseServiceImpl.getSum(paramMap);
	}
}

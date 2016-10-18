package com.cherry.mo.cio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

public class BINOLMOCIO17_Service extends BaseService {
	/**
	 * 查询柜台消息导入信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCntMsgImportList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO17.getCntMsgImportList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询柜台消息导入的柜台消息总数
	 * @param map
	 * @return
	 */
	public int getCntMsgImportCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO17.getCntMsgImportCount");
		return ConvertUtil.getInt(baseServiceImpl.get(paramMap));
	}
	
	/**
	 * 通过ID查询一条柜台消息的（Excel导入）信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCntMsgImportInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO17.getCntMsgImportInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}	
	
	/**
	 * 查询柜台消息明细（Excel导入）
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCntMsgImportDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO17.getCntMsgImportDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询导出数据信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExportDataList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO17.getExportDataList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询导出数据总数
	 * @param map
	 * @return
	 */
	public int getExportDataCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO17.getExportDataCount");
		return ConvertUtil.getInt(baseServiceImpl.get(paramMap));
	}
}

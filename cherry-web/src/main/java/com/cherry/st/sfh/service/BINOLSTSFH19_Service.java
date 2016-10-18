package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTSFH19_Service extends BaseService {
	/**
	 * 获取后台销售单（Excel导入）批次总数
	 * @param map
	 * @return
	 */
	public int getImportBatchCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH19.getImportBatchCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取后台销售单（Excel导入）批次信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH19.getImportBatchList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 判断数据是否重复
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRepeatData(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH19.getRepeatData");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入导入批次表
	 * @param map
	 * @return
	 */
	public int insertImportBatch(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH19.insertImportBatch");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 通过部门编码取得部门信息,目前是不包含权限且不为柜台
	 * @param departCode 部门编码
	 * @param outOrgFlag 标记编码类型为往来单位时
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrgInfoByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH19.getOrgInfoByCode");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 通过产品厂商编码取得产品信息
	 * @param UnitCode 产品厂商编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH19.getPrtInfoByUnitCode");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 通过产品ID取得产品的价格信息
	 * @param BIN_ProductID 产品ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtPrice(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH19.getPrtPrice");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入后台销售（EXCEL导入）主表
	 * @param map
	 * @return
	 */
	public int insertBackstageSaleExcel(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH19.insertBackstageSaleExcel");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入后台销售（EXCEL导入）明细表
	 * @param map
	 */
	public void insertBackstageSaleExcelDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH19.insertBackstageSaleExcelDetail");
		baseServiceImpl.save(paramMap);
	}
}

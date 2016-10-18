package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTSFH12_Service extends BaseService {

	/**
	 * 获取发货单（Excel导入）批次总数
	 * @param map
	 * @return
	 */
	public int getImportBatchCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH12.getImportBatchCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取发货单（Excel导入）批次信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH12.getImportBatchList");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.getRepeatData");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.insertImportBatch");
		return baseServiceImpl.saveBackId(paramMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.getPrtPrice");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 通过部门编码取得部门信息
	 * @param DepartCode 部门编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrgByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.getOrgByCode");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 通过逻辑仓库名称获取逻辑仓库信息
	 * @param Type 类型
	 * @param InventoryName 仓库中文名称或英文名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getLogicInventoryByName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.getLogicInventoryByName");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.getPrtInfoByUnitCode");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入发货Excel导入主表
	 * @param map
	 * @return
	 */
	public int insertPrtDeliverExcel(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.insertPrtDeliverExcel");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入发货Excel导入明细表
	 * @param map
	 */
	public void insertPrtDeliverDetailExcel(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.insertPrtDeliverDetailExcel");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 根据仓库ID判断与其关联的所有部门的下级部门是否存在指定的收货部门(带有权限或不带权限)总数,带有测试部门和正式部门控制
	 * 
	 * */
	public int getChildDepartCount(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.getChildDepartCount");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 * 根据仓库ID判断指定的收货部门是否是对应的部门,带权限
	 * 
	 * */
	public int getDepartInfoCount(Map<String,Object> param){
		param.put("userId", param.get("BIN_UserID"));
		param.put("counterKind", param.get("TestType"));
		param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH12.getDepartInfoCount");
		return baseServiceImpl.getSum(param);
	}
}

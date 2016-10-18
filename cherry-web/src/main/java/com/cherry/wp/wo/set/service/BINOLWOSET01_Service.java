package com.cherry.wp.wo.set.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 营业员管理Service
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWOSET01_Service extends BaseService {
	
	/**
	 * 查询BA数量
	 * 
	 * @param map 查询条件
	 * @return BA数量
	 */
	public int getBAInfoCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getBAInfoCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 查询BA信息List
	 * 
	 * @param map 查询条件
	 * @return BA信息List
	 */
	public List<Map<String, Object>> getBAInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getBAInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询BA是否存在
	 * 
	 * @param map 查询条件
	 * @return 员工信息
	 */
	public Map<String, Object> getEmployeeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getEmployeeId");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 插入员工信息
	 * 
	 * @param map 插入内容
	 * @return 员工ID
	 */
	public int insertEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.insertEmployee");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入BA信息
	 * 
	 * @param map 插入内容
	 */
	public void insertBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.insertBaInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 插入员工柜台关系
	 * 
	 * @param map 插入内容
	 */
	public void insertEmpCou(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.insertEmpCou");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 更新员工信息
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updEmpInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.updEmpInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新BA信息
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updBAInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.updBAInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 删除员工柜台关系
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delEmpCou(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.delEmpCou");
		return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 验证员工柜台关系是否存在
	 * 
	 * @param map 查询条件
	 * @return 员工ID
	 */
	public Integer getEmpCouInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getEmpCouInfo");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewEmpNodeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getNewEmpNodeId");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据岗位代码取得岗位ID
	 * 
	 * @param map 查询条件
	 * @return 岗位ID
	 */
	public Integer getPosIdByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getPosIdByCode");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据岗位代码取得岗位ID
	 * 
	 * @param map 查询条件
	 * @return 岗位ID
	 */
	public Map<String, Object> getEmployeeInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getEmployeeInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}

	public void synaBa(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLWOSET01.synaBa");
	}
	
	/**
	 * 获取当前柜台的BA列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getBaListByOrganizationId(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWOSET01.getBaListByOrganizationId");
		return baseServiceImpl.getList(paramMap);
	}
}

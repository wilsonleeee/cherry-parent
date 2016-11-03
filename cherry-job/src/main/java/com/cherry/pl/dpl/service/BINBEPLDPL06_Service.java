package com.cherry.pl.dpl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 数据权限共通Service
 * 
 * @author WangCT
 * @version 1.0 2015.05.13
 */
public class BINBEPLDPL06_Service extends BaseService {
	
	/**
	 * 查询所有的员工信息
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.getEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询权限类型配置信息
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPrivilegeTypeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.getPrivilegeTypeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 生成部门从属权限
	 * 
	 * @param map 查询条件
	 */
	public void addDepartRelationPL(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addDepartRelationPL");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成员工权限(包括管辖)
	 * 
	 * @param map 查询条件
	 */
	public void addEmployeePL0(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addEmployeePL0");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成员工权限(包括管辖和关注)
	 * 
	 * @param map 查询条件
	 */
	public void addEmployeePL1(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addEmployeePL1");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成员工权限(包括管辖和直接关注)
	 * 
	 * @param map 查询条件
	 */
	public void addEmployeePL2(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addEmployeePL2");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成部门权限(包括管辖)
	 * 
	 * @param map 查询条件
	 */
	public void addDepartPL0(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addDepartPL0");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成部门权限(包括管辖和关注)
	 * 
	 * @param map 查询条件
	 */
	public void addDepartPL1(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addDepartPL1");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成部门权限(包括管辖和直接关注)
	 * 
	 * @param map 查询条件
	 */
	public void addDepartPL2(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addDepartPL2");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成部门权限(包括管辖和关注（所属部门除外）)
	 * 
	 * @param map 查询条件
	 */
	public void addDepartPL3(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.addDepartPL3");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 部门权限从临时表复制到真实表
	 * 
	 * @param map
	 */
	public void copyDataPrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.copyDataPrivilege");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 部门从属权限从临时表复制到真实表
	 * 
	 * @param map
	 */
	public void copyDepartRelation(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.copyDepartRelation");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 人员权限从临时表复制到真实表
	 * 
	 * @param map
	 */
	public void copyEmployeePrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.copyEmployeePrivilege");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 清空部门权限临时表数据
	 * 
	 * @param map
	 */
	public void truncateDepartPrivilegeTemp(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.truncateDepartPrivilegeTemp");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 清空人员权限临时表数据
	 * 
	 * @param map
	 */
	public void truncateEmployeePrivilegeTemp(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL06.truncateEmployeePrivilegeTemp");
		baseServiceImpl.update(parameterMap);
	}
}

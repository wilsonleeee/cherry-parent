package com.cherry.pl.dpl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 部门数据过滤权限共通Service
 * 
 * @author WangCT
 * @version 1.0 2014.11.04
 */
public class BINBEPLDPL05_Service extends BaseService {
	
	/**
	 * 查询某一用户的所有部门权限
	 * 
	 * @param map 查询条件
	 * @return 部门权限List 
	 */
	public List<Map<String, Object>> getOrganizationIdList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.getOrganizationIdList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加数据过滤权限
	 * 
	 * @param list 添加内容
	 */
	public void addDataPrivilege(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBEPLDPL05.addDataPrivilege");
	}
	
	/**
	 * 把部门权限临时表的数据更新到真实表
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updDataPrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.updDataPrivilege");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 查询某一用户的所有人员权限
	 * 
	 * @param map 查询条件
	 * @return 人员权限List 
	 */
	public List<Map<String, Object>> getPositionIDList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.getPositionIDList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加人员数据过滤权限
	 * 
	 * @param list 添加内容
	 */
	public void addPositionPrivilege(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBEPLDPL05.addPositionPrivilege");
	}
	
	/**
	 * 把人员权限临时表的数据更新到真实表
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updPositionPrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.updPositionPrivilege");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 把部门从属权限临时表的数据更新到真实表
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updDepartRelation(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.updDepartRelation");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 清空员工权限类型临时表数据
	 * 
	 * @param map 查询条件
	 */
	public void truncateEmpPLTypeTemp(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.truncateEmpPLTypeTemp");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 添加员工权限类型配置信息
	 * 
	 * @param list 添加内容
	 */
	public void addEmpPLType(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBEPLDPL05.addEmpPLType");
	}
	
	/**
	 * 把员工权限类型临时表的数据更新到真实表
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updEmpPLType(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.updEmpPLType");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 生成部门从属权限
	 * 
	 * @param map 查询条件
	 */
	public void addDepartRelationPL(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.addDepartRelationPL");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成员工权限
	 * 
	 * @param map 查询条件
	 */
	public void addEmployeePL(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.addEmployeePL");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 生成部门权限
	 * 
	 * @param map 查询条件
	 */
	public void addDepartPL(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL05.addDepartPL");
		baseServiceImpl.save(parameterMap);
	}

}

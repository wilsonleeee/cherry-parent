package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 微商管理Service
 * 
 * @author WangCT
 * @version 2015-08-18 1.0.0
 */
public class BINOLCM43_Service extends BaseService {
	
	/**
	 * 通过手机号查询微商是否存在
	 * 
	 */
	public Map<String,Object> getAgentExistByMobile(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.getAgentExistByMobile");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过微信OpenID查询微商是否存在
	 * 
	 */
	public Map<String,Object> getAgentExistByOpenID(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.getAgentExistByOpenID");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询微商信息
	 * 
	 */
	public Map<String,Object> getAgentInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.getAgentInfo");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得新部门节点
	 * 
	 */
	public String getNewOrgNodeId(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.getNewOrgNodeId");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 添加部门信息
	 * 
	 */
	@CacheEvict(value="CherryAllDepartCache",allEntries=true,beforeInvocation=false)
	public int addOrganization(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.addOrganization");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 添加柜台信息
	 * 
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public void addCounterInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.addCounterInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 取得新员工新节点
	 * 
	 */
	public String getNewEmpNodeId(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.getNewEmpNodeId");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 添加员工信息
	 * 
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public void addEmployee(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.addEmployee");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 根据岗位代码取得岗位信息
	 * 
	 */
	public Map<String,Object> getPositionCategoryInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.getPositionCategoryInfo");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 添加仓库
	 * 
	 * @param map 添加内容
	 * 
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public int addDepotInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.addDepotInfo");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 添加部门仓库关系
	 * 
	 * @param map 添加内容
	 * 
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void addInventoryInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.addInventoryInfo");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 更新员工信息
	 * 
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updEmployee(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.updEmployee");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新部门信息
	 * 
	 */
	@CacheEvict(value="CherryAllDepartCache",allEntries=true,beforeInvocation=false)
	public int updDepart(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.updDepart");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 员工节点移动
	 * 
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updEmployeeNode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.updEmployeeNode");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 部门节点移动
	 * 
	 */
	@CacheEvict(value="CherryAllDepartCache",allEntries=true,beforeInvocation=false)
	public int updDepartNode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.updDepartNode");
		return baseServiceImpl.update(map);
	}

	/**
	 * 更新柜台名称
	 * @param counterMap
	 */
	public int updCounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM43.updCounter");
		return baseServiceImpl.update(map);
	}

}

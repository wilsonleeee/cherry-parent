package com.cherry.webservice.employee.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class MaintenEmployeeService extends BaseService {
	/**
	 * 根据coder类型取得coder值
	 * @param map
	 * @return
	 */
	public Map<String,Object> getCoderByCodeType(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getCoderByCodeType");
        return (Map<String,Object>)baseConfServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据员工代码，公众号和手机号查询员工数据是否存在
	 * @param map
	 * @return
	 */
	public Integer getEmployeeNum(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getEmployeeNum");
        return baseServiceImpl.getSum(paramMap);
	}
	/**	
	 * 根据岗位代码查询员工岗位ID
	 * @param map
	 * @return
	 */
	public String getCategoryCodeByCategoryID(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getCategoryCodeByCategoryID");
        return (String)baseServiceImpl.get(paramMap);
	}
	/**	
	 * 取得上级员工的path
	 * @param String
	 * @return
	 */
	public String getNodeEmployeePathByCode(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getNodeEmployeePathByCode");
        return (String)baseServiceImpl.get(paramMap);
	}
	/**	
	 * 取得新节点
	 * @param String
	 * @return
	 */
	public String getNewEmpNodeId(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getNewEmpNodeId");
        return (String)baseServiceImpl.get(paramMap);
	}
	/**
	 * 根据员工代码查询已有员工信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getEmployeeByEmployeeCode(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getEmployeeByEmployeeCode");
        return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	/**
	 * 根据部门代码取得部门ID
	 * @param map
	 * @return
	 */
	public String getOrganizationId(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getOrganizationId");
        return (String)baseServiceImpl.get(paramMap);
	}
	/**
	 * 【更新员工 || 逻辑删除员工】 根据唯一性参数员工代码，公众号和手机号查询员工数据是否存在
	 * @param map
	 * @return
	 */
	public String getEmployeeCode(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.getEmployeeCode");
        return (String)baseServiceImpl.get(paramMap);
	}
	/**
	 * 插入员工信息表
	 * 
	 * @param map
	 * @return int
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int insertEmployee(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "MaintenEmployee.insertEmployee");
		return baseServiceImpl.saveBackId(map);
	}
	/**
	 * 插入营业员信息
	 * 
	 * */
	public void insertBaInfo(Map<String,Object> map){
		
		baseServiceImpl.save(map, "MaintenEmployee.insertBaInfo");
	}
	/**
	 * 更新营业员信息
	 * 
	 * */
	public void updateBaInfo(Map<String,Object> map){
		
		baseServiceImpl.update(map,"MaintenEmployee.updateBaInfo");
	}
	/**
	 * 更新员工信息
	 * 
	 * */
	public void updateEmployee(Map<String,Object> map){
		
		baseServiceImpl.update(map,"MaintenEmployee.updateEmployee");
	}
}

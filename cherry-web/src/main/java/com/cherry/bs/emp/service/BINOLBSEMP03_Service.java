/*	
 * @(#)BINOLBSEMP03_Service.java     1.0 2010/12/30		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.bs.emp.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
/**
 * 
 * 	员工编辑Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.12.30
 */
@SuppressWarnings("unchecked")
public class BINOLBSEMP03_Service extends BaseService{
	
	/**
	 * 员工ID
	 * 
	 * @param map
	 * @return int
	 */
	public String getEmployeeId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.getEmployeeId");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmployeeInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmployeeInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得员工地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpAddressList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmpAddressList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得员工入离职List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEmpQuitList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getEmpQuitList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得员工部门、岗位信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPostDistList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP02.getPostDistList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入地址信息表
	 * 
	 * @param map
	 * @return int
	 */
	public int insertAddrInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertAddrInfo");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 插入员工地址表
	 * 
	 * @param map
	 * @return
	 */
	public void insertEmpAddress(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertEmpAddress");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入员工入退职信息表 
	 * 
	 * @param map
	 * @return
	 */
	public void insertEmpQuit(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertEmpQuit");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入员工岗位分配表 
	 * 
	 * @param map
	 * @return
	 */
	public void insertPostDist(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP04.insertPostDist");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 员工信息更新
	 * 
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updEmpInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updEmpInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 员工地址信息更新 
	 * 
	 * @param map
	 * @return
	 */
	public int updEmpAddr (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updEmpAddr");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 地址信息更新 
	 * 
	 * @param map
	 * @return
	 */
	public int updAddrInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updAddrInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 员工入退职信息更新 
	 * 
	 * @param map
	 * @return
	 */
	public int updQuitInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updQuitInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 员工岗位分配更新 
	 * 
	 * @param map
	 * @return
	 */
	public int updPostDist (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updPostDist");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 员工地址信息删除 
	 * 
	 * @param map
	 * @return
	 */
	public int delEmpAddr (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.delEmpAddr");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 地址信息删除 
	 * 
	 * @param map
	 * @return
	 */
	public int delAddrInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.delAddrInfo");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 员工入退职信息删除 
	 * 
	 * @param map
	 * @return
	 */
	public int delQuitInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.delQuitInfo");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 员工岗位分配删除 
	 * 
	 * @param map
	 * @return
	 */
	public int delPostDist (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.delPostDist");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 人员节点移动
	 * 
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updateEmpNode (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updateEmpNode");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新用户信息
	 * 
	 * @param map
	 * @return
	 */
	public int updateUser (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updateUser");
		return baseServiceImpl.update(map);
	}
	

	/**
	 * 更新用户信息
	 * 
	 * @param map
	 * @return
	 */
	public int updateBangUserValidFlag (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updateBangUserValidFlag");
		return baseConfServiceImpl.update(map);
	}
	
	/**
	 * 更新用户信息(配置数据库)
	 * 
	 * @param map
	 * @return
	 */
	public int updateUserConf (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.updateUserConf");
		return baseConfServiceImpl.update(map);
	}
	
	/**
	 * 删除员工现有的管辖部门信息
	 * 
	 * @param map
	 * @return
	 */
	public int delEmployeeDepart (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.delEmployeeDepart");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除员工现有的关注用户信息
	 * 
	 * @param map
	 * @return
	 */
	public int delLikeEmployee (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.delLikeEmployee");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 根据员工ID取得U盘序列号
	 * 
	 * @param map
	 * @return U盘序列号
	 */
	public String getUdiskSN (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.getUdiskSN");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 根据部门ID取得柜台号List
	 * 
	 * @param map
	 * @return 柜台号List
	 */
	public List<String> getCountercodeList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP03.getCountercodeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据员工ID取得营业员ID
	 * 
	 * */
	public int getBaInfoIdByEmployeeId(Integer employeeId){
		return ConvertUtil.getInt(baseServiceImpl.get(employeeId, "BINOLBSEMP03.getBaInfoIdByEmployeeId"));
	}
}

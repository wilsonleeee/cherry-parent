/*		
 * @(#)BINOLBSCOM01_Service.java     1.0 2010/10/27		
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

package com.cherry.bs.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 基础模块POPUP画面共通Service
 * 
 * @author WangCT
 *
 */
@SuppressWarnings("unchecked")
public class BINOLBSCOM01_Service extends BaseService {
	
	/**
	 * 取得员工总数
	 * 
	 * @param map 查询条件
	 * @return 员工总数
	 */
	public int getEmployeeCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getEmployeeCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得员工List
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getEmployeeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门总数
	 * 
	 * @param map 查询条件
	 * @return 部门总数
	 */
	public int getDepartCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getDepartCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getDepartList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得柜台上级部门总数
	 * 
	 * @param map 查询条件
	 * @return 部门总数
	 */
	public int getHigherOrgCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getHigherOrgCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得柜台上级部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getHigherOrgList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getHigherOrgList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门联系人总数
	 * 
	 * @param map 查询条件
	 * @return 部门联系人总数
	 */
	public int getDepartEmpCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getDepartEmpCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得部门联系人List
	 * 
	 * @param map 查询条件
	 * @return 部门联系人List
	 */
	public List<Map<String, Object>> getDepartEmpList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getDepartEmpList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据部门ID取得部门类型
	 * 
	 * @param map 查询条件
	 * @return 部门类型
	 */
	public String getDeparyType(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getDeparyType");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据岗位类别ID取得岗位类别等级
	 * 
	 * @param map 查询条件
	 * @return 岗位类别等级
	 */
	public String getPosCategoryGrade(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getPosCategoryGrade");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得区域总数
	 * 
	 * @param map 查询条件
	 * @return 区域总数
	 */
	public int getRegionCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getRegionCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	/**
	 * 取得省份或直辖市总数
	 * 
	 * @param map 查询条件
	 * @return 省份或直辖市总数
	 */
	public int getProvinceCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getProvinceCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	/**
	 * 取得区域List
	 * 
	 * @param map 查询条件
	 * @return 区域List
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getRegionList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 省份或直辖市List
	 * 
	 * @param map 查询条件
	 * @return 省份或直辖市List
	 */
	public List<Map<String, Object>> getProvinceList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getProvinceList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 查询区域中品牌节点
	 * 
	 * */
	public String getBrandRegionPath(Map<String,Object> map){
		return (String) baseServiceImpl.get(map, "BINOLBSCOM01.getBrandRegionPath");
	}
	/**
	 * 查询区域节点插入位置
	 * 
	 * */
	public String getNewRegNode(Map<String,Object> map){
		return (String) baseServiceImpl.get(map, "BINOLBSCOM01.getNewRegNode");
	}
	
	/**
	 * 
	 * 插入区域节点
	 * @return 区域ID
	 * 
	 */
	public int insertRegNode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.insertRegNode");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入商场信息
	 * 
	 * */
	public int insertMallInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.insertMallInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入经销商信息并返回自增ID
	 * 
	 * */
	public int insertResellerInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.insertResellerInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 取得品牌下的未知节点
	 * 
	 * @param map 查询条件
	 * @return 品牌下的未知节点
	 * 
	 */
	public Object getUnknownPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCOM01.getUnknownPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得员工所在部门信息
	 * 
	 * */
	public Map<String,Object> getEmployeeOrgInfo(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map,"BINOLBSCOM01.getEmployeeOrgInfo");
	}
	
	/**
	 * 查询组织结构中的柜台信息
	 * 
	 * */
	public List<Map<String,Object>> getOrganizationId(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLBSCOM01.getOrganizationId");
	}
	
	/**
	 * 插入部门信息
	 * 
	 * */
	public int insertDepart(Map<String,Object> map){
		return baseServiceImpl.saveBackId(map, "BINOLBSCOM01.insertDepart");
	}
	
	/**
	 * 
	 * 根据区域ID取得它的上级区域
	 * 
	 * */
	public List<Map<String,Object>> getSuperRegion(int regionId){
		return baseServiceImpl.getList(regionId, "BINOLBSCOM01.getSuperRegion");
	}
	
	/**
	 * 根据员工code和员工名称取得员工信息
	 * 
	 * */
	public List getEmployeeInfoList(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLBSCOM01.getEmployeeInfoList");
	}
	
	/**
	 * 根据员工ID取得员工基本信息，供MQ使用
	 * 
	 * */
	public Map<String,Object> getEmployeeInfo(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLBSCOM01.getEmployeeInfo");
	}
	
	/**
	 * 根据营业员对应的员工ID取得关注和归属的柜台信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCounterInfoByEmplyeeId(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLBSCOM01.getCounterInfoByEmplyeeId");
	}
	
	/**
	 * 根据员工ID取得员工有效性区分
	 * 
	 * */
	public String getEmployeeValidFlag(Integer employeeId){
		return (String) baseServiceImpl.get(employeeId, "BINOLBSCOM01.getEmployeeValidFlag");
	}
	
	/**
	 * 根据员工ID取得直属上级信息
	 * 
	 * */
	public Map<String,Object> getHighterInfo(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map,"BINOLBSCOM01.getHighterInfo");
	}
}

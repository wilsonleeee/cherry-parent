package com.cherry.bs.emp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLBSEMP06_Service extends BaseService {

	/**
	 * 根据BA名称或者CODE查询BA信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBaInfoByNameCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.getBaInfoByNameCode");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}

	/**
	 * 根据柜台名称/CODE取得柜台信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.getCounterInfo");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得员工新节点
	 * 
	 * @param map
	 *            查询条件
	 * @return 新节点
	 */
	public String getNewEmpNodeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.getNewEmpNodeId");
		return (String) baseServiceImpl.get(paramMap);
	}

	/**
	 * 取得在非BA的员工表中存在的导入BAcode的数量
	 * 
	 * @param map
	 * @return
	 */
	public int getEmpCodeCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.getEmpCodeCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 根据岗位code(唯一)取得岗位ID(用于员工表的插入)
	 * 
	 * @param map
	 * @return
	 */
	public int getPositionCategoryIdByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.getPositionCategoryIdByCode");
		return (Integer) baseServiceImpl.get(paramMap);
	}

	/**
	 * 插入员工信息表
	 * 
	 * @param map
	 * @return int
	 */
	@CacheEvict(value = "CherryEmpCache", allEntries = true, beforeInvocation = false)
	public int insertEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.insertEmployee");
		return baseServiceImpl.saveBackId(paramMap);
	}

	/**
	 * 插入员工入职表
	 * 
	 * @param map
	 */
	public void insertEmpQuit(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.insertEmpQuit");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 插入员工管辖部门对应表
	 * 
	 * @param map
	 */
	public void insertEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.insertEmployeeDepart");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 更新员工管辖部门对应表
	 * 
	 * @param map
	 * @return
	 */
	public int updateEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.updateEmployeeDepart");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 更新员工信息表
	 * 
	 * @param map
	 * @return
	 */
	public int updateEmployeeInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.updateEmployeeInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新员工入退职信息表
	 * 
	 * @param map
	 * @return
	 */
	public int updateEmployeeQuit(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.updateEmployeeQuit");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 取得指定员工的员工柜台对应表信息数量
	 * 
	 * @param map
	 * @return
	 */
	public int getEmployeeDepartCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.getEmployeeDepartCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 删除BA柜台对应关系
	 * 
	 * @param map
	 */
	public int delEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLBSEMP06.delEmployeeDepart");
		return baseServiceImpl.remove(paramMap);
	}

	/**
	 * 插入营业员表
	 * 
	 * @param map
	 */
	public void insertBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP06.insertBaInfo");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 更新营业员信息
	 * 
	 * @param map
	 * @return
	 */
	public int updateBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSEMP06.updateBaInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 根据手机号查询BACODE
	 * @param map
	 * @return
	 */
	public List<String> getBaCodeByMobile(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLBSEMP06.getBaCodeByMobile");
		return baseServiceImpl.getList(paramMap);
	}
	
}

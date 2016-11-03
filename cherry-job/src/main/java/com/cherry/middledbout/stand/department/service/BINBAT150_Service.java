package com.cherry.middledbout.stand.department.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 加盟商导入（以柜台形式）Service
 * 
 * @author zw
 * 
 * @version 2016/10/10
 * 
 */
public class BINBAT150_Service extends BaseService {

	
	/**
	 * 
	 * 从接口数据库中查询柜台数据
	 * 
	 * @param map 查询条件
	 * @return 接口表中取得的柜台List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCountersList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getCountersList");
		return tpifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 取得品牌下的未知节点信息
	 * 
	 * @param map 查询条件
	 * @return 未知节点信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSubNodeCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getSubNodeCount");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询组织结构中的柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 部门ID
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrganizationId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getOrganizationId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	
	/**
	 * 
	 * 取得品牌的顶层节点
	 * 
	 * @param map 查询条件
	 * @return 品牌的顶层节点
	 * 
	 */
	public Object getFirstPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getFirstPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 部门新节点
	 * 
	 */
	public Object getNewDepNodeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getNewDepNodeId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入部门信息
	 * 
	 * @param map 插入信息
	 * @return 部门ID
	 * 
	 */
	public int insertDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.insertDepart");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 查询柜台在组织结构表中的插入位置
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */				
	public Object getCounterNodeId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getCounterNodeId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 在组织结构表中插入柜台节点
	 * 
	 * @param counterMap 插入内容
	 * @return Object
	 * 
	 */					
	public Object insertCouOrg(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.insertCouOrg");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 更新在组织结构中的柜台
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateCouOrg(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.updateCouOrg");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 柜台ID
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getCounterId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入柜台信息
	 * 
	 * @param counterMap 插入内容
	 * @return 柜台ID
	 * 
	 */
	public int insertCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.insertCounterInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 插入柜台开始事件信息
	 * 
	 * @param counterMap 插入内容
	 * @return 无
	 * 
	 */
	public void insertCounterEvent(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.insertCounterEvent");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 更新柜台信息表
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.updateCounterInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 判断柜台事件信息是否已经存在
	 * 
	 * @param counterMap 判断条件
	 * @return 柜台事件ID
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCounterEventId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getCounterEventId");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 
	 * 备份标准柜台接口信息表 
	 * 
	 * @param map 需要备份数据的查询条件
	 * @return 无
	 * 
	 */
	public void backupCounters(List<Map<String,Object>> paramList) {
		baseServiceImpl.saveAll(paramList, "BINBAT150.backupCounters");
	}
	/**
	 * 判断仓库编码是否已经存在
	 * 
	 * @param map 查询条件
	 * 
	 */
	public int getDepotCountByCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.getDepotCountByCode");
		return baseServiceImpl.getSum(parameterMap);
	}
	/**
	 * 添加仓库
	 * 
	 * @param map 添加内容
	 * 
	 */
	public int addDepotInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.addDepotInfo");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	/**
	 * 添加部门仓库关系
	 * 
	 * @param map 添加内容
	 * 
	 */
	public void addInventoryInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT150.addInventoryInfo");
		baseServiceImpl.save(parameterMap);
	}
	/**
	 * 
	 * 更新柜台仓库名称
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateDepotInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT150.updateDepotInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得品牌节点Path
	 * @param map
	 * @return
	 */
	public String getBrandPath(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT150.getBrandPath");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
}

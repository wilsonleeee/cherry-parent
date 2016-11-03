package com.cherry.middledbout.stand.allocation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
/**
 * 标准接口：柜台调拨单据数据导出到标准接口表(调拨单)Service
 * @author lzs
 *
 */
public class BINBAT125_Service extends BaseService {
	/**
	 * SynchFlag状态更新
	 * 
	 * @param map
	 * @return
	 */
	public int updateSynchFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT125.updateSynchFlag");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得指定导出状态的调拨单号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPrtAllocatOutListBySynch(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT125.getPrtAllocatOutListBySynch");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 根据新后台的单据号->查询标准接口表的单据号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getExistsPrtAllocatOutList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT125.getExistsPrtAllocatOutList");
		return tpifServiceImpl.getList(paramMap);
	}
	/**
	 * 取得需导出调拨确认单数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtAllocatOut(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT125.getPrtAllocatOut");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 取得需导出调拨确认单明细数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtAllocatOutDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT125.getPrtAllocatOutDetail");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 批量插入list数据
	 * 
	 * @param list
	 */
	public void insertIFAllocation(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT125.insertIFAllocation");
	}
	/**
	 * 批量插入list数据
	 * 
	 * @param list
	 */
	public void insertIFAllocationDetail(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT125.insertIFAllocationDetail");
	}
}

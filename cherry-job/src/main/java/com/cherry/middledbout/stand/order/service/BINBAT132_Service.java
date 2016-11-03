package com.cherry.middledbout.stand.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 标准接口：产品订单导出Service
 * 
 * @author chenkuan
 * 
 * @version 2015/12/15
 * 
 */
public class BINBAT132_Service extends BaseService {
	/**
	 * 取得需导出订货单主数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtOrderList(
			Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBAT132.getPrtOrderList");
		return baseServiceImpl.getList(paramMap);
	}
	
	
	/**
	 * 取得需导出订货单的详细数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtOrderDetailList(
			Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBAT132.getPrtOrderDetailList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * SynchFlag状态更新
	 * 
	 * @param map
	 * @return
	 */
	public int updateSynchFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBAT132.updateSynchFlag");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得指定导出状态的订单号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPrtOrderListBySynch(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBAT132.getPrtOrderListBySynch");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据新后台的单据号->查询标准接口表的单据号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getListFromPrtOrder(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBAT132.getListFromPrtOrder");
		return tpifServiceImpl.getList(paramMap);
	}

	/**
	 * 批量插入list数据(主数据)
	 * 
	 * @param list
	 */
	public void insertOrderBatch(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT132.insertOrders");
	}
	
	
	/**
	 * 批量插入list数据(明细数据)
	 * 
	 * @param list
	 */
	public void insertOrderDeatils(List<Map<String, Object>> orderDeatils) {
		tpifServiceImpl.saveAll(orderDeatils, "BINBAT132.insertOrderDeatils");
	}
}

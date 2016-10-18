package com.cherry.bs.wem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 海名微利润分摊Service
 * 
 * @author songka
 * @version 1.0 2015.09.08
 */
public class BINOLBSWEM06_Service extends BaseService{
	
	/**
	 * 查询返利表，重新利润分摊
	 * @param paramMap 查询条件 分页
	 */
	public List<Map<String, Object>> getSalList(Map<String, Object> pamMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(pamMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM06.getSalList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 删除返利表需要重新分摊的信息
	 * @param paramMap 删除条件
	 */
	public void delRebateDivide(List<Map<String,Object>> salerecordCodeList) {
		baseServiceImpl.updateAll(salerecordCodeList, "BINOLBSWEM06.delRebateDivide");
	}
	/**
	 * 查询返利表，重新利润分摊的数据数量
	 * @param paramMap 查询条件
	 */
	public int getSalListCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM06.getSalListCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 修改销售表
	 * */ 
	public void profitRebateReset(List<Map<String, Object>> salerecordCodeList) {
		baseServiceImpl.updateAll(salerecordCodeList, "BINOLBSWEM06.updateSaleRecordRebateFlag");
	}
}

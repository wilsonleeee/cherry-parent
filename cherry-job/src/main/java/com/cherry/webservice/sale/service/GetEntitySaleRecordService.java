package com.cherry.webservice.sale.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: GetEntitySaleRecordService
 * @Description: TODO(【Webservice接口_Batch】线下销售记录（带建议书版本号）Service)
 * @author menghao
 * @version v1.0.0 2015-12-12
 * 
 */
public class GetEntitySaleRecordService extends BaseService {

	/**
	 * 根据柜台号取得BIN_OrganizationID
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCounterByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"GetEntitySaleRecord.getCounterByCode");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}

	/**
	 * 根据相关参数取得销售记录(包含明细)
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSaleRecordDetailList(
			Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"GetEntitySaleRecord.getSaleRecordDetailList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 获取满足条件的销售记录总数（主单的记录总数）
	 * 
	 * @param map
	 * @return
	 */
	public int getSaleRecordMainCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"GetEntitySaleRecord.getSaleRecordMainCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取code值
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCodeValue(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"GetEntitySaleRecord.getCodeValue");
		return baseServiceImpl.getList(paramMap);
	}

}

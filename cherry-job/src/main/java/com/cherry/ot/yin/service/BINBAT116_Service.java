
package com.cherry.ot.yin.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 颖通接口：礼品领用单据导出(颖通)Service
 * 
 * @author lzs	
 * 
 * @version 2015-07-03
 * 
 */
public class BINBAT116_Service extends BaseService {
	
	/**
	 * 取得需导出礼品领用单的数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> GiftDrawDetailQuery(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT116.GiftDrawDetailQuery");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 *SynchFlag状态更新
	 * 
	 * @param map
	 * @return
	 */
	public int updateSynchFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT116.updateSynchFlag");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 *根据是否协同区分和SynchFlag状态更新礼品领用单数据状态
	 * 
	 * @param map
	 * @return
	 */
	public int updateSynchFlagByCounterSync(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT116.updateSynchFlagByCounterSync");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 取得指定导出状态的礼品领用单List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBillNoListBySynch(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT116.getBillNoListBySynch");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据新后台的礼品领用单单据号->查询SAP销售接口表的单据号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBillNoFromGiftDraw(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT116.getBillNoFromGiftDraw");
		return tpifServiceImpl.getList(paramMap);
	}

	/**
	 * 礼品领用单据数据批量导出至SAP销售接口
	 * 
	 * @param list
	 */
	public void insertCPSImportSales(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT116.insertCPSImportSales");
	}

}

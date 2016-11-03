package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.ss.prm.dto.SaleRecordDetailDTO;
import com.cherry.ss.prm.dto.SaleRecordDTO;

@SuppressWarnings("unchecked")
public class BINBESSPRM04_Service extends BaseService{

	/**
	 * 查询销售主记录
	 * @param map
	 * @return
	 */
	public List<SaleRecordDTO> getSaleRecordList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBESSPRM04.getSaleRecordList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询销售明细记录
	 * @param map
	 * @return
	 */
	public List<SaleRecordDetailDTO> getSaleRecordDetailList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBESSPRM04.getSaleRecordDetailList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询促销品入出库差异主表
	 * @param map
	 * @return
	 */
	public int addPromotionStockDiffInOut(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBESSPRM04.addPromotionStockDiffInOut");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 查询促销品入出库差异明细表
	 * @param map
	 * @return
	 */
	public void addPromotionStockDiffDetail(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBESSPRM04.addPromotionStockDiffDetail");
		baseServiceImpl.save(map);
	}
}

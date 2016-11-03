/**
 * 
 */
package com.cherry.middledbout.stand.sale.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
/**
 * 标准接口：销售数据导出到标准接口表(销售)Service
 * @author lzs
 *
 */
public class SaleInfo_Service extends BaseService {
	/**
	 * 根据同步状态取得销售单据集合
	 * @param map
	 * @return
	 */
	public List getBillCodeSRListSync(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.getBillCodeOfSRListBySync");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 根据新后台BillCode查询销售单接口表的单据号List
	 * @param map
	 * @return
	 */
	public List getBillCodeOfISList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.getBillCodeOfISList");
		return tpifServiceImpl.getList(map);
	}
	/**
	 * 更改销售数据的同步状态
	 * @param map
	 * @return
	 */
	public int upSaleRecordBySync(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.upSaleRecordBySync");
		return baseServiceImpl.update(map);
	}
	/**
	 * 取得新后台销售数据（主数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSaleRecordList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.getSaleRecordList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得失败的新后台销售数据（主数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getFaildSaleRecordList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.getFaildSaleRecordList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 插入销售单接口表
	 * @param listMap
	 */
	public void insertIFSale(List<Map<String,Object>> listMap){
		tpifServiceImpl.saveAll(listMap, "StandSaleInfo.insertIFSale");
	}
	/**
	 * 取得新后台销售数据(明细数据)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSaleRecordDetailList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.getSaleRecordDetailList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得新后台失败的销售数据(明细数据)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getFaildSaleRecordDetailList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.getFaildSaleRecordDetailList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得新后台失败的销售数据(支付数据)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSalePayList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "StandSaleInfo.getSalePayList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 插入销售单明细接口表
	 * @param listMap
	 */
	public void insertIFSaleDetail(List<Map<String,Object>> listMap){
		tpifServiceImpl.saveAll(listMap, "StandSaleInfo.insertIFSaleDetail");
	}
	/**
	 * 插入销售单支付接口表
	 * @param listMap
	 */
	public void insertIFSalePayDetail(List<Map<String,Object>> listMap){
		tpifServiceImpl.saveAll(listMap, "StandSaleInfo.insertIFSalePayDetail");
	}
	/**
	 * 取得品牌code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"StandSaleInfo.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
}

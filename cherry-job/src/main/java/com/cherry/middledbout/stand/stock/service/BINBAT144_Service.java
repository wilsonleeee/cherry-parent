package com.cherry.middledbout.stand.stock.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
/**
 * 标准接口：实时库存数据导出到标准接口表(库存表)Service
 * @author lzs
 *
 */
public class BINBAT144_Service extends BaseService {
	
	/**
	 * 查询所有库存数量大于0的数据
	 * @param map
	 * @return
	 */
	public List getProductStockList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT144.getProductStockList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 删除库存标准接口表数据
	 */
	public void delIFStock(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT144.delIFStock");
		tpifServiceImpl.remove(map);
	}
	/**
	 * 插入数据至标准接口库存表
	 * @param listMap
	 */
	public void insertIFStock(List<Map<String,Object>> list){
		tpifServiceImpl.saveAll(list, "BINBAT144.insertIFStock");
	}
}

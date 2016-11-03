package com.cherry.middledbout.stand.product.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
/**
 * 标准接口:产品信息导出至标准接口表(IF_Product)service
 * @author lzs
 * 下午2:34:52
 */
public class BINBAT119_Service extends BaseService{
	/**
	 * 查询所在业务日期内的产品数据
	 * @param map
	 * @return
	 */
	public List getProductList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT119.getProductList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 插入数据至标准接口产品表
	 * @param listMap
	 */
	public void insertIFProduct(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT119.insertIFProduct");
		tpifServiceImpl.save(map);
	}
	/**
	 * 取得品牌code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT119.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	/**
	 * 查询大中小分类终端类型顺序是否为前三位 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTeminalFlag(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT119.getTeminalFlag");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 删除产品标准接口表数据
	 */
	public void delIFProduct(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT119.delIFProduct");
		tpifServiceImpl.remove(map);
	}
}

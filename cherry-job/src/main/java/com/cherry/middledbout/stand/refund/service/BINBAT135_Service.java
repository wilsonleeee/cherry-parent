package com.cherry.middledbout.stand.refund.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 标准接口：退库单导入Service
 * 
 * @author chenkuan
 * 
 * @version 2015/12/24
 * 
 */
public class BINBAT135_Service extends BaseService {

	
	/**
	 * 取得标准退库单接口表的单据号
	 * @param map
	 * @return
	 */
	public List<String> getBillCodeList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT135.getBillCodeList");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新标准退库单接口表的synchFlag字段
	 * @param map
	 * @return
	 */
	public int updateSynchFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT135.updateSynchFlag");
		return tpifServiceImpl.update(map);
	}
	
	/**
	 * 取得标准退库单接口表数据(主数据)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getExportTransList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT135.getExportTransList");
		return tpifServiceImpl.getList(map);
	}
	
	
	/**
	 * 取得标准退库单接口表数据（单据明细）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getExportTransListdeatils(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT135.getExportTransListdeatils");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌Code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT135.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得 1.业务日期,2.日结标志
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBECMINC99.getBussinessDateMap");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
}

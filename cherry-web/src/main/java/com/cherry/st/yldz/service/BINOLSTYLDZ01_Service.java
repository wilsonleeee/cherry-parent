package com.cherry.st.yldz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 
 * 银联对账
 * 
 * @author songka
 * @version 1.0 2016.1.14
 */
public class BINOLSTYLDZ01_Service  extends BaseService{
	/**
	 * 获取销售总数
	 * @param map
	 * @return
	 */
	public int getSaleListCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTYLDZ01.getSaleListCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	/**
	 * 获取销售数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSaleList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTYLDZ01.getSaleList");
		return baseServiceImpl.getList(map);
	}
	public Map<String, Object> editInit(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTYLDZ01.editInit");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	public int delete(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTYLDZ01.deleteBankBillsByBill");
		return baseServiceImpl.update(map);
	}
	public void addBankBill(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTYLDZ01.addBankBill");
		baseServiceImpl.save(map);
	}
	public int updateBankBill(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTYLDZ01.updateBankBill");
		return baseServiceImpl.update(map);
	}
	public int checkBillCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTYLDZ01.checkBillCode");
		return baseServiceImpl.getSum(map);
	}
}

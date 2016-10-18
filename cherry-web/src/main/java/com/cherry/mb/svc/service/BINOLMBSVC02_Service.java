package com.cherry.mb.svc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

public class BINOLMBSVC02_Service extends BaseService{
	public Map<String, Object> getCardCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getCardCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	public List<Map<String, Object>> getCardList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getCardList");
		List<Map<String,Object>> cardList= baseServiceImpl.getList(paramMap);
		/*for(int i=0;i<cardList.size();i++){
			Map<String,Object> info=cardList.get(i);
			String testType=ConvertUtil.getString(info.get("testType"));
			String dateType=ConvertUtil.getString(info.get("dateType"));
			//正式数据
			if("0".equals(dateType) && "0".equals(testType)){
				cardList.remove(i);
			}else if("1".equals(dateType) && "1".equals(testType)){
				cardList.remove(i);
			}
		}
		return baseServiceImpl.getList(paramMap);*/
		return cardList;
	}
	
	public int updateCardVaild(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.updateCardVaild");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新储值卡vaild以及state
	 */
	public int updateCardStateAndVaild(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.updateCardStateAndVaild");
		return baseServiceImpl.update(paramMap);
	}
	
	public List<Map<String, Object>> getCardDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getCardDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	public int getCardDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getCardDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	public int getSaleDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	public List<Map<String, Object>> getSaleDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	public List<Map<String, Object>> getSaleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleList");
		return baseServiceImpl.getList(paramMap);
	}
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}

	public List<Map<String, Object>> getServiceList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getServiceList");
		return baseServiceImpl.getList(paramMap);
	}
	public Map<String, Object> getServiceCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getServiceCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}

	public Map<String, Object> getSaleByBillCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleByBillCode");
		return (Map)baseServiceImpl.get(paramMap);
	}
	public List<Map<String, Object>> getSaleByCardCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleByCardCode");
		return baseServiceImpl.getList(paramMap);
	}
	public List<Map<String, Object>> getSaleServiceByCardCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleServiceByCardCode");
		return baseServiceImpl.getList(paramMap);
	}
	public Map<String, Object> getSaleDetailCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CherryUtil.removeEmptyVal(map);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC02.getSaleDetailCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
}

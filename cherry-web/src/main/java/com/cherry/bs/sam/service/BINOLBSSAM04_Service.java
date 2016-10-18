package com.cherry.bs.sam.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLBSSAM04_Service extends BaseService{
	
	public List<Map<String, Object>> getSalesBonusRateList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.getSalesBonusRateList");
		return baseServiceImpl.getList(map);
	}

	public int getSalesBonusRateCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.getSalesBonusRateCount");
		return baseServiceImpl.getSum(map);
	}

	public Map<String, Object> editInit(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.getSalesBonusRateMap");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	public void updateSalesBonusRate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.updateSalesBonusRate");
		baseServiceImpl.update(map);
	}

	public void delete(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.deleteSalesBonusRate");
		baseServiceImpl.update(map);
	}

	public void addSalesBonusRate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.addSalesBonusRate");
		baseServiceImpl.save(map);
	}

	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.getPositionCategoryList");
		return baseServiceImpl.getList(map);
	}

	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.getCounterInfoList");
		return baseServiceImpl.getList(map);
	}

	public int checkBonusRate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM04.checkBonusRate");
		return baseServiceImpl.getSum(map);
	}

}

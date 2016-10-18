package com.cherry.bs.sam.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLBSSAM05_Service extends BaseService{
	
	public List<Map<String, Object>> getSalesBonusRateList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.getSalesBonusRateList");
		return baseServiceImpl.getList(map);
	}

	public int getSalesBonusRateCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.getSalesBonusRateCount");
		return baseServiceImpl.getSum(map);
	}

	public Map<String, Object> editInit(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.getSalesBonusRateMap");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	public void updateSalesBonusRate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.updateSalesBonusRate");
		baseServiceImpl.update(map);
	}

	public void delete(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.deleteSalesBonusRate");
		baseServiceImpl.update(map);
	}

	public void addSalesBonusRate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.addSalesBonusRate");
		baseServiceImpl.save(map);
	}

	public Map<String, Object> getEmployeeCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.getEmployeeCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	public void insertAssessmentScore(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.insertAssessmentScore");
		baseServiceImpl.save(map);
	}

	public int getAssessmentCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM05.getAssessmentCount");
		return baseServiceImpl.getSum(map);
	}

}

package com.cherry.bs.sam.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.sam.interfaces.BINOLBSSAM04_IF;
import com.cherry.bs.sam.service.BINOLBSSAM04_Service;

public class BINOLBSSAM04_BL implements BINOLBSSAM04_IF{

	@Resource
	private BINOLBSSAM04_Service binOLBSSAM04_Service;
	public List<Map<String, Object>> getSalesBonusRateList(Map<String, Object> map) {
		return binOLBSSAM04_Service.getSalesBonusRateList(map);
	}
	public int getSalesBonusRateCount(Map<String, Object> map) {
		return binOLBSSAM04_Service.getSalesBonusRateCount(map);
	}
	public Map<String, Object> editInit(Map<String, Object> map) {
		return binOLBSSAM04_Service.editInit(map);
	}
	public void updateSalesBonusRate(Map<String, Object> map) {
		binOLBSSAM04_Service.updateSalesBonusRate(map);
	}
	public void delete(Map<String, Object> map) {
		binOLBSSAM04_Service.delete(map);
	}
	public void addSalesBonusRate(Map<String, Object> map) {
		binOLBSSAM04_Service.addSalesBonusRate(map);
	}
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		return binOLBSSAM04_Service.getPositionCategoryList(map);
	}
	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		return binOLBSSAM04_Service.getCounterInfoList(map);
	}
	public int checkBonusRate(Map<String, Object> map) {
		return binOLBSSAM04_Service.checkBonusRate(map);
	}

}

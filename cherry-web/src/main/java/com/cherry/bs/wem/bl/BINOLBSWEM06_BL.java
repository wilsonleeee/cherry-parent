package com.cherry.bs.wem.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.wem.interfaces.BINOLBSWEM06_IF;
import com.cherry.bs.wem.service.BINOLBSWEM06_Service;

/**
 * 海名微 重新利润分摊BL
 * 
 * @author songka
 * @version 1.0 2015.09.08
 */

public class BINOLBSWEM06_BL implements BINOLBSWEM06_IF{
	
	@Resource
	private BINOLBSWEM06_Service binOLBSWEM06_Service;
	@Override
	public List<Map<String, Object>> search(Map<String, Object> map) {
		return  binOLBSWEM06_Service.getSalList(map);
	}
	@Override
	public int getSalListCount(Map<String, Object> map) {
		return binOLBSWEM06_Service.getSalListCount(map);
	}
	@Override
	public void profitRebateReset(List<Map<String, Object>> salerecordCodeList)throws Exception {
		binOLBSWEM06_Service.profitRebateReset(salerecordCodeList);
	}
	@Override
	public void delRebateDivide(List<Map<String, Object>> salerecordCodeList) {
		binOLBSWEM06_Service.delRebateDivide(salerecordCodeList);
	}
}

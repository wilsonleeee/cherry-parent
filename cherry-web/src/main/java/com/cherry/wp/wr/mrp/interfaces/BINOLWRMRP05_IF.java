package com.cherry.wp.wr.mrp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 有销售无会员资料IF
 * 
 * @author WangCT
 * @version 1.0 2014/10/30
 */
public interface BINOLWRMRP05_IF extends BINOLCM37_IF {
	
	public int getMemSaleCount(Map<String, Object> map);
	
	public Map<String, Object> getMemSaleCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getMemSaleList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

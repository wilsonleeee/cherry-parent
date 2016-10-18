package com.cherry.wp.wr.srp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 销售月报表IF
 * 
 * @author WangCT
 * @version 1.0 2014/10/29
 */
public interface BINOLWRSRP06_IF extends BINOLCM37_IF {
	
	public Map<String, Object> getSaleByDayCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getSaleByDayList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

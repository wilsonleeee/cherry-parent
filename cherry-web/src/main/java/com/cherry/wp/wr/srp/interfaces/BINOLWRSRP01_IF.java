package com.cherry.wp.wr.srp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 业务小结IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public interface BINOLWRSRP01_IF extends BINOLCM37_IF {
	
	public int getBASaleCount(Map<String, Object> map);
	
	public Map<String, Object> getBASaleCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getBASaleList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

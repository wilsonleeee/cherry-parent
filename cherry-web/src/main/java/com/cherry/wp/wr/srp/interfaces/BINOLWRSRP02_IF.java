package com.cherry.wp.wr.srp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 销售明细报表IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/15
 */
public interface BINOLWRSRP02_IF extends BINOLCM37_IF {
	
	public int getSaleRecordCount(Map<String, Object> map);
	
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

package com.cherry.wp.wr.srp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 会员销售排行IF
 * 
 * @author WangCT
 * @version 1.0 2014/10/24
 */
public interface BINOLWRSRP05_IF extends BINOLCM37_IF {
	
	public int getSaleByMemCount(Map<String, Object> map);
	
	public Map<String, Object> getSaleByMemCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getSaleByMemList(Map<String, Object> map)  throws Exception;
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

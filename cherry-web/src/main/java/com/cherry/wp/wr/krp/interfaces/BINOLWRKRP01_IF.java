package com.cherry.wp.wr.krp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 库存报表IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/24
 */
public interface BINOLWRKRP01_IF extends BINOLCM37_IF {
	
	public int getProStockCount(Map<String, Object> map);
	
	public Map<String, Object> getProStockCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getProStockList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

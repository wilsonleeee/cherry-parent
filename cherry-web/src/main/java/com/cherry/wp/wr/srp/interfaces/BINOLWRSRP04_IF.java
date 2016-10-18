package com.cherry.wp.wr.srp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 商品销售排行IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/29
 */
public interface BINOLWRSRP04_IF extends BINOLCM37_IF {
	
	public int getSaleByPrtCount(Map<String, Object> map);
	
	public Map<String, Object> getSaleByPrtCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getSaleByPrtList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

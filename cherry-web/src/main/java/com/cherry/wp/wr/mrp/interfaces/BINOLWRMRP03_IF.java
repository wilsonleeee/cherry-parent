package com.cherry.wp.wr.mrp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 会员生日情况统计IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public interface BINOLWRMRP03_IF extends BINOLCM37_IF {
	
	public int getMemberCount(Map<String, Object> map);
	
	public List<Map<String, Object>> getMemberList(Map<String, Object> map)  throws Exception;
	
	public void setCondition(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

package com.cherry.wp.wr.mrp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 会员积分兑换报表IF
 * 
 * @author WangCT
 * @version 1.0 2014/11/17
 */
public interface BINOLWRMRP06_IF extends BINOLCM37_IF {
	
	public int getPxCount(Map<String, Object> map);
	
	public Map<String, Object> getPxCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getPxInfoList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

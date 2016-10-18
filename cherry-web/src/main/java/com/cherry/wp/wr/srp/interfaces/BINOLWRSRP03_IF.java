package com.cherry.wp.wr.srp.interfaces;

import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 销售分类统计IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/19
 */
public interface BINOLWRSRP03_IF extends BINOLCM37_IF {
	
	public Map<String, Object> getSaleCountByClass(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

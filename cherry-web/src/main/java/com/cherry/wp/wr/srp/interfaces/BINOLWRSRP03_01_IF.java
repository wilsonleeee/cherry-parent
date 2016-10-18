package com.cherry.wp.wr.srp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 销售分类明细统计IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/23
 */
public interface BINOLWRSRP03_01_IF extends BINOLCM37_IF {
	
	public int getClassDetailCount(Map<String, Object> map);
	
	public Map<String, Object> getClassDetaiCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getClassDetailList(Map<String, Object> map);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

}

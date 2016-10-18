package com.cherry.bs.sam.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLBSSAM03_IF extends BINOLCM37_IF{

	public int getPayrollCount(Map<String,Object> param);
	
	public List<Map<String,Object>> getPayrollList(Map<String,Object> param);
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;
}

package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLSTSFH15_IF extends BINOLCM37_IF{

	public int getSaleOrdersCount(Map<String, Object> map);
	
	public List<Map<String, Object>> getSaleOrdersList (Map<String, Object> map) throws Exception;
	
	public Map<String, Object> getExportMap (Map<String, Object> map);
		    	
    public Map<String, Object> getExportDetailMap (Map<String, Object> map);
    
    public Map<String, Object> getSumInfo(Map<String, Object> map);
}

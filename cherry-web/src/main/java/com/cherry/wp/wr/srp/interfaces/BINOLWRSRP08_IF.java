package com.cherry.wp.wr.srp.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

/**
 * 英雄榜IF
 * 
 * @author songka
 * @version 1.0 2015/09/07
 */
public interface BINOLWRSRP08_IF extends BINOLCM37_IF {
	
	public Map<String, Object> getExportParam(Map<String, Object> map);
	
	public String exportCSV(Map<String, Object> map) throws Exception;

	public Map<String, Object> getHeroCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getHeroList(Map<String, Object> map);


}

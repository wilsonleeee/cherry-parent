package com.cherry.mb.cct.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLMBCCT09_IF extends BINOLCM37_IF{
	/**
	 * 获取符合条件的来电记录列表
	 * 
	 * @param map
	 * @return 来电记录列表
	 */
	@SuppressWarnings("rawtypes")
	public List getCallLogList(Map<String, Object> map);
	
	/**
	 * 获取符合条件的来电记录数量
	 * 
	 * @param map
	 * @return 来电记录数量
	 */
	public int getCallLogCount(Map<String, Object> map);
	
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	public String exportCsv(Map<String, Object> map) throws Exception;
}

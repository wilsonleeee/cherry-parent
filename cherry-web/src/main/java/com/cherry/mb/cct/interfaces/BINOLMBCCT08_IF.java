package com.cherry.mb.cct.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLMBCCT08_IF extends BINOLCM37_IF{
	/**
	 * 获取符合条件的非会员列表
	 * 
	 * @param map
	 * @return 非会员列表
	 */
	@SuppressWarnings("rawtypes")
	public List getCustomerList(Map<String, Object> map);
	
	/**
	 * 获取符合条件的非会员数量
	 * 
	 * @param map
	 * @return 非会员数量
	 */
	public int getCustomerCount(Map<String, Object> map);
	
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	public String exportCsv(Map<String, Object> map) throws Exception;
}

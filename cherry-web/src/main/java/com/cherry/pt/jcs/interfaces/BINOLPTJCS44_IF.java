package com.cherry.pt.jcs.interfaces;

import java.util.List;
import java.util.Map;


public interface BINOLPTJCS44_IF {
	/**
	 * 页面查询
	 */
	public Map<String, Object> getDropCountInfo(Map<String, Object> map);
	/**
	 *获取集合 
	 */
	public List<Map<String, Object>> getDropList(Map<String, Object> map);
	
	public int getCntProductCount(Map<String, Object> map);
	
	public List<Map<String, Object>> getCntProductList(Map<String, Object> map);
}

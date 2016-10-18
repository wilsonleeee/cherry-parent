package com.cherry.wp.sal.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWPSAL02_IF {
	
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception;
	
	public String getUserBindCounterCode(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getOrderCounterCode(Map<String, Object> map);
	
	public String getCounterPhone(Map<String,Object> map);
	
	public String getCounterAddress(Map<String,Object> map);
}

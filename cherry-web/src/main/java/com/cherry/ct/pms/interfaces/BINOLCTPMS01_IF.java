package com.cherry.ct.pms.interfaces;

import java.util.List;
import java.util.Map;


public interface BINOLCTPMS01_IF {

	public Map<String, Object> getParamCountInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> getParamList(Map<String, Object> map);
	
	public void editParam(Map<String,Object> map);
}

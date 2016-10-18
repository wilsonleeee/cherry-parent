package com.cherry.mb.svc.interfaces;

import java.util.List;
import java.util.Map;



public interface BINOLMBSVC05_IF{
	public Map<String,Object> getTradeCountInfo(Map<String,Object> map);
	
	public List<Map<String,Object>> getTradeList(Map<String,Object> map);
}

package com.cherry.wp.sal.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWPSAL06_IF {
	
	public int getBillsCount(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getBillList (Map<String, Object> map) throws Exception;
	
	public Map<String, Object> tran_getBillDetail (Map<String, Object> map) throws Exception;
	
	public Map<String, Object> tran_getBillDetailAddSocket(Map<String, Object> map,String entitySocketId) throws Exception;
}

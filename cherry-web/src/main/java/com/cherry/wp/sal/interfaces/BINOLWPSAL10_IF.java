package com.cherry.wp.sal.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWPSAL10_IF {
	
	public Map<String, Object> getCouponOrderInfo (Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getCouponOrderProduct (Map<String, Object> map) throws Exception;
	
}

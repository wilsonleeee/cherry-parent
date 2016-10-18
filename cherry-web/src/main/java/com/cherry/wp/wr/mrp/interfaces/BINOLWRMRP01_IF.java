package com.cherry.wp.wr.mrp.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 会员信息查询IF
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public interface BINOLWRMRP01_IF {
	
	public int getMemCount(Map<String, Object> map);
	
	public Map<String, Object> searchMemInfo(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> searchMemList(Map<String, Object> map);
	
	public List<Map<String, Object>> searchAllMemList(Map<String, Object> map);

}

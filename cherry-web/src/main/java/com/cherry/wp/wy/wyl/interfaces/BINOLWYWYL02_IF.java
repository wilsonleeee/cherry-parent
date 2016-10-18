package com.cherry.wp.wy.wyl.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLWYWYL02_IF {
	// 获取活动单据信息
	public List<Map<String, Object>> getActivityOrderInfo (Map<String, Object> map) throws Exception;
	
	public Map<String, Object> updateMemberInfo(Map<String, Object> map) throws Exception;
	
	public void updateOrderMemberId(Map<String, Object> map) throws Exception;
}

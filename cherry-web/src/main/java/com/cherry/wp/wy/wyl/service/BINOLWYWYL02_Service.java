package com.cherry.wp.wy.wyl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWYWYL02_Service extends BaseService{
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOrderList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWYWYL02.getOrderList");
		return baseServiceImpl.getList(paramMap);
    }
	
	// 更新会员资料
	public int updateMemberInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWYWYL02.updateMemberInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	// 根据微信号获取会员信息
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberInfoByMessageId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWYWYL02.getMemberInfoByMessageId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	// 更新单据中的会员ID和会员卡号
	public int updateOrderMemberId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWYWYL02.updateOrderMemberId");
		return baseServiceImpl.update(paramMap);
	}
}

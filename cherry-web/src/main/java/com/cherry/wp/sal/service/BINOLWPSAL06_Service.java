package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLWPSAL06_Service extends BaseService{
	
	// 获取可提单数量
	public int getBillsCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL06.getBillsCount");
		return CherryUtil.obj2int(baseServiceImpl.get(paramMap));
	}
	
	// 获取可提单列表
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBillList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL06.getBillList");
        return baseServiceImpl.getList(paramMap);
    }
	
	// 获取单据详细信息
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBillInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL06.getBillInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	// 获取单据明细列表
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBillDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL06.getBillDetailList");
        return baseServiceImpl.getList(paramMap);
    }
	
	// 更新提单状态
	public int updateHangBillStatus(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL06.updateHangBillStatus");
		return baseServiceImpl.update(paramMap);
	}
}

package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWPSAL03_Service extends BaseService{
	
	public int saveBillRecord(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.saveBillRecord");
        return baseServiceImpl.saveBackId(paramMap);
    }
	
	public void saveBillDetail(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.saveBillDetail");
        baseServiceImpl.save(paramMap);
    }
	
	public void savePayment(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.savePayment");
        baseServiceImpl.save(paramMap);
    }
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPaymentDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.getPaymentDetailList");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPayPartnerConfig(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.getPayPartnerConfig");
        return baseServiceImpl.getList(paramMap);
    }
	
	public int getCZKPayStateCount(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.getCZKPayStateCount");
        return baseServiceImpl.getSum(paramMap);
	}
	/**
	 * 更新为已经付款与发送MQ成功
	 * @param map
	 */
	public void updateHangBillState(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.updateHangBillState");
        baseServiceImpl.update(paramMap);
	}
	/**
	 * 更新为付款成功
	 * @param map
	 */
	public void updateHangBillCollectState(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.updateHangBillCollectState");
        baseServiceImpl.update(paramMap);
	}
	
	public Map<String,Object> getHangBillInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL03.getHangBillInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
}

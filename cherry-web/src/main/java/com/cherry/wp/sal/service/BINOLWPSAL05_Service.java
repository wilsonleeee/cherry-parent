package com.cherry.wp.sal.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.Map;

public class BINOLWPSAL05_Service extends BaseService{

    public void updateBillRecodeValidFlag(Map<String,Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        baseServiceImpl.update(paramMap,"BINOLWPSAL05.updateBillRecodeValidFlag");
    }

    public void updateBillRecodeDetailValidFlag(Map<String,Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        baseServiceImpl.update(paramMap,"BINOLWPSAL05.updateBillRecodeDetailValidFlag");
    }

	public int insertHangBillRecord(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL05.insertHangBillRecord");
        return baseServiceImpl.saveBackId(paramMap);
    }
	
	public void insertHangBillDetail(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL05.insertHangBillDetail");
        baseServiceImpl.save(paramMap);
    }
	
}

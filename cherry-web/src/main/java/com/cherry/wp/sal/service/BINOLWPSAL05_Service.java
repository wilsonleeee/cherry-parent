package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWPSAL05_Service extends BaseService{
	
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

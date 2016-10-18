package com.cherry.ct.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLCTRPT04_Service extends BaseService{
	/**
     * 沟通信息发送失败明细记录List
     * 
     * @param map
     * @return
     * 		沟通发送信息失败List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getErrorMsgDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT04.getErrorMsgDetailList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 沟通发送信息失败记录统计
     * 
     * @param map
     * @return
     * 		沟通发送信息失败统计
     */
	public int getErrorMsgDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT04.getErrorMsgDetailCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
}
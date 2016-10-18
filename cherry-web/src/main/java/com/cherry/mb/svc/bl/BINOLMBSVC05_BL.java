package com.cherry.mb.svc.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CodeTable;
import com.cherry.mb.svc.interfaces.BINOLMBSVC05_IF;
import com.cherry.mb.svc.service.BINOLMBSVC05_Service;


public class BINOLMBSVC05_BL implements BINOLMBSVC05_IF {
	
	@Resource
	private BINOLMBSVC05_Service binOLMBSVC05_Service;
	
	
	@Resource
	private CodeTable code;


	@Override
	public Map<String, Object> getTradeCountInfo(Map<String, Object> map) {
		return binOLMBSVC05_Service.getTradeCountInfo(map);
	}


	@Override
	public List<Map<String, Object>> getTradeList(Map<String, Object> map) {
		return binOLMBSVC05_Service.getTradeList(map);
	}
	
	

	
}
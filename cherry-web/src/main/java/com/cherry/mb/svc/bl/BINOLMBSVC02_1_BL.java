package com.cherry.mb.svc.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.svc.interfaces.BINOLMBSVC02_1_IF;
import com.cherry.mb.svc.service.BINOLMBSVC02_Service;


public class BINOLMBSVC02_1_BL implements BINOLMBSVC02_1_IF{

	@Resource
	private BINOLMBSVC02_Service binOLMBSVC02_Service;
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLMBSVC02_Service.getSaleDetailList(map);
	}

}

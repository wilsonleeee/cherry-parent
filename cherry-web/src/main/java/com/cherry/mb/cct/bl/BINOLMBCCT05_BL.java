package com.cherry.mb.cct.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.mb.cct.interfaces.BINOLMBCCT05_IF;
import com.cherry.mb.cct.service.BINOLMBCCT05_Service;

public class BINOLMBCCT05_BL implements BINOLMBCCT05_IF{

	@Resource
	private BINOLMBCCT05_Service binolmbcct05_Service;
	
	@Override
	public void saveCustomer(Map<String, Object> map, String type)
			throws Exception {
		// 插表时的共通字段
		Map<String, Object> insertMap = new HashMap<String, Object>();
		// 作成程序名
		insertMap.put(CherryConstants.CREATEPGM, "BINOLMBCCT05");
		// 更新程序名
		insertMap.put(CherryConstants.UPDATEPGM, "BINOLMBCCT05");
		// 增加共通字段
		map.putAll(insertMap);
		if(type.equals("INSERT")){
			binolmbcct05_Service.insertCustomer(map);
		}else{
			binolmbcct05_Service.updateCustomer(map);
		}
	}

	@Override
	public Map<String, Object> getCustomerInfo(Map<String, Object> map)
			throws Exception {
		return binolmbcct05_Service.getCustomerInfo(map);
	}

}

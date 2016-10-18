package com.cherry.ct.pms.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.pms.interfaces.BINOLCTPMS01_IF;
import com.cherry.ct.pms.service.BINOLCTPMS01_Service;

public class BINOLCTPMS01_BL implements BINOLCTPMS01_IF{

	@Resource
	private BINOLCTPMS01_Service bINOLCTPMS01_Service;

	@Override
	public Map<String, Object> getParamCountInfo(Map<String, Object> map) {
		return bINOLCTPMS01_Service.getParamCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getParamList(Map<String, Object> map) {
		List<Map<String, Object>> paramList = bINOLCTPMS01_Service.getParamList(map);
		return paramList;
	}

	@Override
	public void editParam(Map<String, Object> map) {
		if("".equals(ConvertUtil.getString(map.get("paramCode")))){
			return;
		}
		int i=bINOLCTPMS01_Service.checkParam(map);
		if(i>0){
			bINOLCTPMS01_Service.updateParam(map);
		}else{
			bINOLCTPMS01_Service.insertParam(map);
		}
	}
}

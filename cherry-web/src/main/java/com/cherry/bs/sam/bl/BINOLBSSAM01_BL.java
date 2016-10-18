package com.cherry.bs.sam.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.sam.interfaces.BINOLBSSAM01_IF;
import com.cherry.bs.sam.service.BINOLBSSAM01_Service;

public class BINOLBSSAM01_BL implements BINOLBSSAM01_IF{

	@Resource(name="binOLBSSAM01_Service")
	private BINOLBSSAM01_Service bINOLBSSAM01_Service;

	@Override
	public int getScheduleCount(Map<String, Object> params) {
		return bINOLBSSAM01_Service.getScheduleCount(params);
	}

	@Override
	public List<Map<String, Object>> getScheduleList(Map<String, Object> params) {
		return bINOLBSSAM01_Service.getScheduleList(params);
	}
}

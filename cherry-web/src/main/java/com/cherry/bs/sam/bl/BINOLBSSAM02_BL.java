package com.cherry.bs.sam.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.sam.interfaces.BINOLBSSAM02_IF;
import com.cherry.bs.sam.service.BINOLBSSAM02_Service;

public class BINOLBSSAM02_BL  implements BINOLBSSAM02_IF{

	@Resource(name="binOLBSSAM02_Service")
	private BINOLBSSAM02_Service bINOLBSSAM02_Service;

	@Override
	public int getOverTimeCount(Map<String, Object> param) {
		return bINOLBSSAM02_Service.getOverTimeCount(param);
	}

	@Override
	public List<Map<String, Object>> getOverTimeList(Map<String, Object> param) {
		return bINOLBSSAM02_Service.getOverTimeList(param);
	}
}

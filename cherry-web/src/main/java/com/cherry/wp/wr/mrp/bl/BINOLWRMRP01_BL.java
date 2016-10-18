package com.cherry.wp.wr.mrp.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.mbm.bl.BINOLMBMBM02_BL;
import com.cherry.wp.common.service.BINOLWPCM01_Service;
import com.cherry.wp.wr.mrp.interfaces.BINOLWRMRP01_IF;

/**
 * 会员信息查询BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRMRP01_BL implements BINOLWRMRP01_IF {
	
	@Resource(name="binOLWPCM01_Service")
	private BINOLWPCM01_Service binOLWPCM01_Service;
	
	/** 会员详细画面BL */
	@Resource
	private BINOLMBMBM02_BL binOLMBMBM02_BL;

	@Override
	public int getMemCount(Map<String, Object> map) {
		return binOLWPCM01_Service.getMemberCount(map);
	}

	@Override
	public Map<String, Object> searchMemInfo(Map<String, Object> map) throws Exception {
		return binOLMBMBM02_BL.getMemberInfo(map);
	}

	@Override
	public List<Map<String, Object>> searchMemList(Map<String, Object> map) {
		map.put("memOtherInfoFlag", "0");
		return binOLWPCM01_Service.getMemberList(map);
	}

	@Override
	public List<Map<String, Object>> searchAllMemList(Map<String, Object> map) {
		map.put("memOtherInfoFlag", "0");
		return binOLWPCM01_Service.getMemberInfoList(map);
	}

}

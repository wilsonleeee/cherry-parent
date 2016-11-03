package com.cherry.ct.smg.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.ct.smg.interfaces.BINBECTSMG06_IF;
import com.cherry.ct.smg.service.BINBECTSMG06_Service;


public class BINBECTSMG06_BL implements BINBECTSMG06_IF{

	@Resource
	private BINBECTSMG06_Service binBECTSMG06_Service;

	@Override
	public Map<String, Object> getMemberInfoById(Map<String, Object> map)
			throws Exception {
		// 获取沟通模板List
		Map<String, Object> memberInfo = binBECTSMG06_Service.getMemberInfoById(map);
		return memberInfo;
	}

	@Override
	public int getSmsSendFlag(Map<String, Object> map) throws Exception {
		// 查询沟通信息是否发送过
		return binBECTSMG06_Service.getSmsSendFlag(map);
	}

	@Override
	public List<Map<String, Object>> getConfigInfo(Map<String, Object> map)
			throws Exception {
		return binBECTSMG06_Service.getConfigInfo(map);
	}
	
	@Override
	public Map<String, Object> getSearchInfo(Map<String, Object> map)
			throws Exception {
		// 获取搜索记录List
		Map<String, Object> searchInfo = binBECTSMG06_Service.getSearchInfo(map);
		return searchInfo;
	}
}

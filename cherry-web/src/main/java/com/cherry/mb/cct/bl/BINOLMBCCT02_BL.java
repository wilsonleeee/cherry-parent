package com.cherry.mb.cct.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.mb.cct.interfaces.BINOLMBCCT02_IF;
import com.cherry.mb.cct.service.BINOLMBCCT02_Service;

public class BINOLMBCCT02_BL implements BINOLMBCCT02_IF{
	
	@Resource(name="binOLMBCCT02_Service")
	private BINOLMBCCT02_Service binOLMBCCT02_Service;
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getIssueListByCustomer(Map<String, Object> map) {
		// 获取客户问题记录List
		List<Map<String, Object>> issueList = binOLMBCCT02_Service.getIssueListByCustomer(map);
		return issueList;	
	}
	
	@Override
	public int getIssueCountByCustomer(Map<String, Object> map) {
		// 获取客户问题记录数量
		return binOLMBCCT02_Service.getIssueCountByCustomer(map);
	}

}

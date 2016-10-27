package com.cherry.mo.mup.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mo.mup.interfaces.BINOLMOMUP01_IF;
import com.cherry.mo.mup.service.BINOLMOMUP01_Service;

/**
 * 盘点机软件版本更新BL
 * @author Wangminze
 *
 */
public class BINOLMOMUP01_BL implements BINOLMOMUP01_IF{
	
	@Resource
	private BINOLMOMUP01_Service binOLMOMUP01_Service;

	/**
	 * 获取盘点机软件版本信息
	 * @param paramMap KEY(organizationInfoId,brandInfoId)
	 * @return KEY(BIN_SoftwareVersionInfoID,Version,DownloadURL)
	 */
	@Override
	public Map<String, Object> getSoftVersionInfo(Map<String, Object> paramMap) {
		
		List<Map<String,Object>> retList = binOLMOMUP01_Service.getSoftVersionInfo(paramMap);
		
		int sum = retList.size();
		
		//将查询结果放到map中返回
    	Map<String,Object> retMap = new HashMap<String,Object>();
    	retMap.put("retList", retList);
    	retMap.put("sum", sum);
		
		return retMap;
	}

}

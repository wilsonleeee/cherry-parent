package com.cherry.webserviceout.common;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.service.BaseService;

public class WebserviceOutConfig  extends BaseService{	
	public Map<String,Object> getWebserviceOutConfig(Map map) throws Exception{
			// 查询品牌信息			
			HashMap<String,Object> resultMap = (HashMap<String,Object>)baseServiceImpl.get(map,"WebserviceOutConfig.getConfig");
			return resultMap;
	}
}

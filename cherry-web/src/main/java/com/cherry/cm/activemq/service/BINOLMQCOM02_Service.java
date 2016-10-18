package com.cherry.cm.activemq.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.synchro.common.BaseSynchroService;

public class BINOLMQCOM02_Service extends BaseService{
	
	@Resource
	private BaseSynchroService baseSynchroService;
	/**
	 * 调用存储过程往老后台品牌数据库中的MQ_Log表中插入日志
	 * 
	 * 
	 * */	
	@SuppressWarnings("unchecked")
	public Map<String,Object> insertMQ_Log(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMQCOM02.insertMQ_Log");
		return (Map<String,Object>)baseSynchroService.execProcedure(paramMap);
	}
}

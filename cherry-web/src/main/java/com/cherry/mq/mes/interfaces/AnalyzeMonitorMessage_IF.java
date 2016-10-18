package com.cherry.mq.mes.interfaces;

import java.util.Map;

public interface AnalyzeMonitorMessage_IF {

	/**
	 * 对机器连接数据进行处理
	 * @param map
	 */
	public void analyzeMonitorData(Map<String,Object> map)throws Exception;
}

package com.cherry.mq.mes.interfaces;

import java.util.Map;

public interface AnalyzeRivalSaleMessage_IF extends BaseMessage_IF{

	/**
	 * 对竞争对手日销售数据进行处理
	 * @param map
	 */
	public void analyzeRivalSaleData(Map<String,Object> map);
}

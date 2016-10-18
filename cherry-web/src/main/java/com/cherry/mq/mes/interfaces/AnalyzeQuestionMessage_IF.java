package com.cherry.mq.mes.interfaces;

import java.util.Map;

public interface AnalyzeQuestionMessage_IF extends BaseMessage_IF{
	/**
	 * 对会员问卷数据进行处理
	 * @param map
	 */
	public void analyzeMemberQuestionData(Map<String,Object> map);
	
	/**
	 * 对CS考核问卷数据进行处理
	 * @param map
	 */
	public void analyzeCSQuestionData(Map<String,Object> map);
}

package com.cherry.mq.mes.interfaces;

import java.util.Map;

public interface AnalyzeMachMessage_IF extends BaseMessage_IF{

	/**
	 *对机器信息进行处理 
	 * 
	 * @param map
	 */
	public void analyzeMachInfoData(Map<String,Object> map)throws Exception ;
	
	/**
     *对机器绑定柜台信息进行处理 
     * 
     * @param map
     */
    public void analyzeMachCounterData(Map<String,Object> map)throws Exception ;
    
    /**
     *对柜台产品陈列数息进行处理 
     * 
     * @param map
     */
    public void analyzeExhibitQuantityData(Map<String,Object> map)throws Exception ;
}

package com.mqhelper.interfaces;

import java.util.Map;

/*  
 * @(#)MQHelper_IF.java    1.0 2012-2-21     
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
public interface MQHelper_IF {

	/**
	 * 向指定的消息队列中发送MQ消息，并且往witpos系统中的品牌数据库中的MQ_Log日志表中写MQ日志。
	 * 
	 * @param map
	 * 			因为MessageMap中的数据要决定MQ消息体的格式，所以MessageMap中的数据要遵循一定的规则，MessageMap中的Key有以下几种：
	 * 			"Version"，必须有，对应的value的数据类型为String，它对应的值是MQ的版本；
	 * 			"Type"，必须有，对应的value的数据类型为String，对应的是MQ消息类型；
	 * 			"MainLineKey"，必须有，对应的value的数据类型为String[]，将字段放入到String数组中的顺序一定要按照MQ消息体定义的字段顺序；
	 * 			"MainDataLine"，必须有，对应的value的数据类型为String[]，将数据放入到String数组中的索引号要跟MainLineKey中对应的字段相一致；
	 * 			"DetailLineKey"，可选，对应的value的数据类型为String[]，将字段放入到String数组中的顺序一定要按照MQ消息体定义的字段顺序；
	 * 			"DetailDataLine"，对应的value的数据类型为List<String[]>，将数据放入到String数组中的索引号要跟DetailLineKey中对应的字段相一致；
	 * 			"Mq_Log"，必须有，对应的value的数据类型为Map<String,Object>，这里就是往老后台品牌数据库中的MQ_Log表中写数据的，所以对应的key为：BillType、BillCode、CounterCode、Txddate、Txdtime、BeginPuttime、EndPuttime、Source、SendOrRece、ModifyCounts、OriginalMsg
	 *          "DataType"，可选，若值为application/json，MainLineKey、MainDataLine、DetailLineKey、DetailDataLine都不需要传入，但必须传入DataLine（JSON格式的字符串）。
	 * 
	 * @param queueName 要发送的消息队列名称，如果设定为null那么就会发送到默认的消息队列中。
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * */
	public boolean sendData(Map<String,Object> map,String queueName) throws Exception;
	
	/**
	 * 向指定的消息队列中发送MQ消息，并且往witpos系统中的品牌数据库中的MQ_Log日志表中写MQ日志。
	 * 
	 * @param map
	 * 			因为MessageMap中的数据要决定MQ消息体的格式，所以MessageMap中的数据要遵循一定的规则，MessageMap中的Key有以下几种：
	 * 			"Version"，必须有，对应的value的数据类型为String，它对应的值是MQ的版本；
	 * 			"Type"，必须有，对应的value的数据类型为String，对应的是MQ消息类型；
	 * 			"MainLineKey"，必须有，对应的value的数据类型为String[]，将字段放入到String数组中的顺序一定要按照MQ消息体定义的字段顺序；
	 * 			"MainDataLine"，必须有，对应的value的数据类型为String[]，将数据放入到String数组中的索引号要跟MainLineKey中对应的字段相一致；
	 * 			"DetailLineKey"，可选，对应的value的数据类型为String[]，将字段放入到String数组中的顺序一定要按照MQ消息体定义的字段顺序；
	 * 			"DetailDataLine"，对应的value的数据类型为List<String[]>，将数据放入到String数组中的索引号要跟DetailLineKey中对应的字段相一致；
	 * 			"Mq_Log"，必须有，对应的value的数据类型为Map<String,Object>，这里就是往老后台品牌数据库中的MQ_Log表中写数据的，所以对应的key为：BillType、BillCode、CounterCode、Txddate、Txdtime、BeginPuttime、EndPuttime、Source、SendOrRece、ModifyCounts、OriginalMsg
	 *          "DataType"，可选，若值为application/json，MainLineKey、MainDataLine、DetailLineKey、DetailDataLine都不需要传入，但必须传入DataLine（JSON格式的字符串）。
	 * 
	 * @param queueName 要发送的消息队列名称，如果设定为null那么就会发送到默认的消息队列中。
	 * @param groupName JmsGroupID
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * */
	public boolean sendData(Map<String,Object> map,String queueName, String groupName) throws Exception;
}

package com.mqhelper.common.util;

import java.util.List;
import java.util.Map;

import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;

/*  
 * @(#)MessageUtil.java    1.0 2012-2-22     
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
public class MessageUtil {

	/**逗号*/
	public static String comma = ",";
	/**回车*/
	public static String enter = "\r\n";
	/**左中括号*/
	public static String left_bracket = "[";
	/**右中括号*/
	public static String right_bracket = "]";
	
	/**
	 * 共通设定MQ消息体，根据参数中给定的数据来决定MQ消息格式。
	 * @param MessageMap
	 * 			因为MessageMap中的数据要决定MQ消息体的格式，所以MessageMap中的数据要遵循一定的规则，MessageMap中的Key有以下几种：
	 * 			"Version"，可选，对应的value的数据类型为String，它对应的值是MQ的版本，默认为AMQ.001.001；
	 * 			"Type"，必须有，对应的value的数据类型为String，对应的是MQ消息类型；
	 * 			"MainLineKey"，必须有，对应的value的数据类型为String[]，将字段放入到String数组中的顺序一定要按照MQ消息体定义的字段顺序；
	 * 			"MainDataLine"，必须有，对应的value的数据类型为String[]，将数据放入到String数组中的索引号要跟MainLineKey中对应的字段相一致；
	 * 			"DetailLineKey"，可选，对应的value的数据类型为String[]，将字段放入到String数组中的顺序一定要按照MQ消息体定义的字段顺序；
	 * 			"DetailDataLine"，对应的value的数据类型为List<String[]>，将数据放入到String数组中的索引号要跟DetailLineKey中对应的字段相一致；
	 * 			"Mq_Log"，必须有，对应的写MQ_Log日志时需要的一些数据。
	 * 
	 * @return String
	 * 
	 * @throws CherryMQException
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static String getMQMessage(Map<String,Object> MessageMap) throws CherryMQException{
		
		if(null == MessageMap || MessageMap.isEmpty()){
			return null;
		}
		
		StringBuffer message = new StringBuffer();
		//版本号
		String version = null;
		//MQ数据类型
		String type = null;
		
		
		//如果没有设定Version的值或者它的值设定为空则抛出异常
		try{
			version = CherryBatchUtil.getString(MessageMap.get("Version"));
			if(("").equals(version)){
				throw new Exception();
			}
		}catch(Exception e){
			throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为Version没有设定！");
		}
		
		message.append(version);
		message.append(enter);
		
		//如果没有设定消息数据类型抛出异常
		try{
			type = CherryBatchUtil.getString(MessageMap.get("Type"));
			if(("").equals(type)){
				throw new Exception();
			}else{
				message.append(type);
				message.append(enter);
			}
		}catch(Exception e){
			throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为Type没有设定！");
		}
		
		//设定主数据key和值
		try{
			String[] mainLine = (String[])MessageMap.get("MainLineKey");
			if(null == mainLine || mainLine.length == 0){
				throw new Exception("mainLineKeyError");
			}else{
				
				StringBuffer mainStrBuf = new StringBuffer();
				String[] mainDate = (String[])MessageMap.get("MainDataLine");
				if(null == mainDate || mainDate.length == 0){
					throw new Exception("mainLineDataError");
				}
				message.append(left_bracket);
				message.append("CommLine");
				message.append(right_bracket);
				
				mainStrBuf.append(left_bracket);
				mainStrBuf.append(MessageConstants.MAIN_MESSAGE_TITLE);
				mainStrBuf.append(right_bracket);
				
				for(int i = 0 ; i < mainLine.length ; i++){
					message.append(comma);
					message.append(mainLine[i]);
					
					mainStrBuf.append(comma);
					if(null != mainDate[i] && !("").equals(mainDate[i])){
					    //拼装MQ消息前替换特殊字符
						mainStrBuf.append(CherryUtil.replaceMQSpecialChar(mainDate[i]));
					}
					
				}
				
				message.append(enter);
				message.append(mainStrBuf);
				message.append(enter);
			}
		}catch(Exception e){
			if("mainLineKeyError".equals(e.getMessage())){
				throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为消息体主要数据的KEY设定不正确！");
			}else if("mainLineDataError".equals(e.getMessage())){
				throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为消息体主要数据设定不正确！");
			}else{
				throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为消息体主要数据的KEY和其相对应的值数量不匹配，或者参数的数据类型准备的不正确！");
			}
		}
		
		//设定明细数据
		try{
			String[] DetailLine = (String[])MessageMap.get("DetailLineKey");
			List<String[]> detailData = (List<String[]>)MessageMap.get("DetailDataLine");
			if(null == DetailLine || DetailLine.length == 0 || null == detailData || detailData.size() == 0){
				message.append(MessageConstants.END_MESSAGE_SIGN);
				return message.toString();
			}else{
				message.append(left_bracket);
				message.append("CommLine");
				message.append(right_bracket);
				for(int i = 0 ; i < DetailLine.length ; i++){
					message.append(comma);
					message.append(DetailLine[i]);
				}
				
				message.append(enter);
				
				for(String[] tempDetail : detailData){
					message.append(left_bracket);
					message.append(MessageConstants.DETAIL_MESSAGE_TITLE);
					message.append(right_bracket);
					for(int i = 0 ; i < DetailLine.length ; i++){
						message.append(comma);
						if(null != tempDetail[i] && !("").equals(tempDetail[i])){
						    //拼装MQ消息前替换特殊字符
							message.append(CherryUtil.replaceMQSpecialChar(tempDetail[i]));
						}
					}
					message.append(enter);
				}
			}
			
		}catch(Exception e){
			throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，设定明细数据时出错！");
		}
		
		message.append(MessageConstants.END_MESSAGE_SIGN);
		
		return message.toString();
	}
	
    /**
     * 共通设定MQ消息体，根据参数中给定的数据来决定MQ消息格式。JSON格式
     * @param MessageMap
     *          因为MessageMap中的数据要决定MQ消息体的格式，所以MessageMap中的数据要遵循一定的规则，MessageMap中的Key有以下几种：
     *          "Version"，可选，对应的value的数据类型为String，它对应的值是MQ的版本，默认为AMQ.001.001；
     *          "Type"，必须有，对应的value的数据类型为String，对应的是MQ消息类型；
     *          "DataLine"，必须有，值为JSON格式的字符串
     *          "Mq_Log"，必须有，对应的写MQ_Log日志时需要的一些数据。
     * 
     * @return String
     * 
     * @throws CherryMQException
     * 
     * */
    @SuppressWarnings("unchecked")
    public static String getMQMessageJSON(Map<String,Object> MessageMap) throws CherryMQException{
        
        if(null == MessageMap || MessageMap.isEmpty()){
            return null;
        }
        
        StringBuffer message = new StringBuffer();
        //版本号
        String version = null;
        //MQ数据类型
        String type = null;
        
        
        //如果没有设定Version的值或者它的值设定为空则抛出异常
        try{
            version = CherryBatchUtil.getString(MessageMap.get("Version"));
            if(("").equals(version)){
                throw new Exception();
            }
        }catch(Exception e){
            throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为Version没有设定！");
        }
        
        message.append(version);
        message.append(enter);
        
        //如果没有设定消息数据类型抛出异常
        try{
            type = CherryBatchUtil.getString(MessageMap.get("Type"));
            if(("").equals(type)){
                throw new Exception();
            }else{
                message.append(type);
                message.append(enter);
            }
        }catch(Exception e){
            throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为Type没有设定！");
        }
        
        //数据类型-JSON格式
        message.append(left_bracket);
        message.append("DataType");
        message.append(right_bracket);
        message.append(comma);
        message.append(MessageConstants.DATATYPE_APPLICATION_JSON);
        message.append(enter);
        
        //设定DataLine
        try{
            String dataLine = CherryBatchUtil.getString(MessageMap.get("DataLine"));
            if(dataLine.equals("")){
                throw new Exception("dataLineNULL");
            }else{
                message.append(left_bracket);
                message.append("DataLine");
                message.append(right_bracket);
                message.append(comma);
                message.append(dataLine);
                message.append(enter);
            }
        }catch(Exception e){
            if("dataLineNULL".equals(e.getMessage())){
                throw new CherryMQException("MQHelper共通在组装MQ消息体时出错，因为消息体DataLine为空！");
            }
        }
        
        message.append(MessageConstants.END_MESSAGE_SIGN);
        
        return message.toString();
    }
}

package com.cherry.mq.mes.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.DateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.cherry.cm.util.CherryUtil;

public class MessageUtil {
	/**
	 * 插入mq警告表
	 * 
	 * @param map
	 * @throws Exception
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void addMessageWarning(Map map, String errMsg) throws CherryMQException {
		try {
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", map.get("tradeType"));
			// 单据号
			dbObject.put("TradeNoIF", map.get("tradeNoIF"));
			// 会员卡号
			dbObject.put("TradeEntityCode", map.get("memberCode"));
			// 错误类型
			String errType = (String)map.get("errType");
			if(errType == null || "".equals(errType)) {
				errType = "0";
			}
			dbObject.put("ErrType", errType);
			// 消息体
			dbObject.put("MessageBody", (String) map.get("messageBody"));
			// 错误信息
			dbObject.put("ErrInfo", errMsg);
			// 生成时间
			dbObject.put("PutTime", DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
			MongoDB.insert(MessageConstants.MQ_WARN_COLL_NAME, dbObject);
			throw new CherryMQException(errMsg);
		} catch (Exception e) {
			if (!(e instanceof CherryMQException)){
				e.printStackTrace();
				throw new CherryMQException(MessageConstants.MSG_ERROR_12);
			}else{
				String strTradeType="";
				String strTradeNoIF="";
				if(map.get("tradeType")!=null)
				    strTradeType=map.get("tradeType").equals("")?"":"。业务类型为\""+map.get("tradeType")+"\"";
				if(map.get("tradeNoIF")!=null)
				    strTradeNoIF=map.get("tradeNoIF").equals("")?"":"，单据号为\""+map.get("tradeNoIF")+"\"";
				
				throw new CherryMQException(errMsg+strTradeType+strTradeNoIF);
			}
		}
	}
	
	/**
	 * 通过正则表达式匹配消息
	 * @param type 0：主数据；1：明细数据
	 * @param msg
	 * @return
	 */
	public static List<String> matchMessageStr(int type,String msg){
		List<String> dataStrList = new ArrayList<String>();
		String messageTitle = (type == 0 ? MessageConstants.MAIN_MESSAGE_TITLE:MessageConstants.DETAIL_MESSAGE_TITLE);
		String messageSign = (type == 0 ? MessageConstants.MAIN_MESSAGE_SIGN:MessageConstants.DETAIL_MESSAGE_SIGN);
		String regEx = "[\\[]" + messageTitle + "(],{1}(.|\n)*\r\n)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(msg);
		while (m.find()) {
			dataStrList.add(m.group().replace(messageSign, "").replace("\r\n", ""));
		}
		return dataStrList;
	}
	
	/**
	 * 匹配消息体中的某一类数据，以"\r\n"为某一类数据的结束标记【所以不支持在此数据中间有回车字符】
	 * 
	 * @param flag
	 * 			例如:Version,MainDataLine等
	 * @param msg
	 * 			消息体
	 * @return List
	 * 
	 * 
	 * */
	public static List<String> matchMessage(String flag,String msg){
        List<String> dataStrList = new ArrayList<String>();
        String startStr = "["+flag+"],";
        int startIndex = msg.indexOf(startStr);
        if(startIndex >= 0){
            startIndex += startStr.length();
            int endIndex = msg.indexOf("\r\n", startIndex);
            String subStr = msg.substring(startIndex,endIndex);
            dataStrList.add(subStr);
            List<String> subStrList = matchMessage(flag,msg.substring(endIndex));
            dataStrList.addAll(subStrList);
        }
        return dataStrList;
	}
	
	/**
	 * 验证消息体完整性（是否以[Version]开始并且以[End]结束）
	 * 
	 * */
	public static boolean checkMessage(String msg){
		return msg.startsWith(MessageConstants.MESSAGE_VERSION_SIGN)&&msg.endsWith(MessageConstants.END_MESSAGE_SIGN);
	}
	
	/**
	 * 将消息体解析为Map
	 * 注：在解析时回车字符很关键【用于分隔消息类型、主消息、消息明细等信息】，暂时不支持类数据内容中带有回车的消息体
	 * @param msg: 待解析MQ消息字符
	 * @return Map
	 * 
	 */
	public static Map message2Map(String msg) throws CherryMQException{
		try{
			Map<String,Object> messgeMap = new HashMap<String,Object>();
			//验证消息的完整性（是否以[Version]开始并且以[End]结束）
			if(!checkMessage(msg)){
				throw new CherryMQException(MessageConstants.MSG_ERROR_04);
			} else {
				
				//取得消息体的Version
				List<String> versionList = matchMessage(MessageConstants.MESSAGE_VERSION_TITLE,msg);
				if(null != versionList && versionList.size() == 1 && !"".equals(versionList.get(0))){
					messgeMap.put("version", versionList.get(0).trim());
				}else{
					throw new CherryMQException(MessageConstants.MSG_ERROR_01);
				}
				
				//取得消息体类型
				List<String> typeList = matchMessage(MessageConstants.MESSAGE_TYPE_TITLE,msg);
				if(null != typeList && typeList.size() == 1 && !"".equals(typeList.get(0))){
					messgeMap.put("type", typeList.get(0).trim());
				}else{
					throw new CherryMQException(MessageConstants.MSG_ERROR_40);
				}
				
				//取得消息体DataType类型
				List<String> dataTypeList = matchMessage(MessageConstants.MESSAGE_DATATYPE_TITLE,msg);
				if(dataTypeList.size() > 1){
					throw new CherryMQException(MessageConstants.MSG_ERROR_41);
				}
				
				//如果没有设定DataType不处理
				if(dataTypeList.isEmpty()){
					return null;
				}
				
				String dataType = dataTypeList.get(0).trim();
				messgeMap.put("dataType", dataType);
				//不处理的数据类型
				if("".equals(dataType) || MessageConstants.DATATYPE_TEXT_PLAIN.equals(dataType) || "CRM".equals(dataType)){
					return null;
				}else if(MessageConstants.DATATYPE_APPLICATION_JSON.equals(dataType)){
					//取得json数据
					List<String> dataLineList = matchMessage(MessageConstants.DATALINE_JSON_XML,msg);
					if(!dataLineList.isEmpty() && dataLineList.size() == 1 && !"".equals(dataLineList.get(0))){
						String dataLine = dataLineList.get(0);
						Map DataMap = null;
						try{
							//将JSON格式的字符串解析成Map
							DataMap = CherryUtil.json2Map(dataLine);
							if(null == DataMap || DataMap.isEmpty()){
								throw new Exception();
							}
						}catch(Exception e){
							throw new CherryMQException(MessageConstants.MSG_ERROR_43 + e.getMessage());
						}
						
						//遍历maps将key做如下处理：如果第二个字母是大写则不处理，否则将首字母转为小写
						DataMap = CherryUtil.dealMap(DataMap);
						
						//验证是否有主数据
						if(!DataMap.containsKey("mainData")){
							throw new CherryMQException(MessageConstants.MSG_ERROR_46);
						}else{
							//取得MainData数据
							Map mainData = (Map)DataMap.get("mainData");
							messgeMap.putAll(mainData);
							DataMap.remove("mainData");
							messgeMap.putAll(DataMap);
						}
					}else{
						throw new CherryMQException(MessageConstants.MSG_ERROR_42);
					}
				}else if(MessageConstants.DATATYPE_APPLICATION_XML.equals(dataType)){
					throw new CherryMQException(MessageConstants.MSG_ERROR_44);
				}else{
					throw new CherryMQException(MessageConstants.MSG_ERROR_45);
				}
			}
			
			messgeMap.put("messageBody", msg);
			return messgeMap;
		}catch(Exception e){
			if(e instanceof CherryMQException){
				throw (CherryMQException)e;
			}else{
				throw new CherryMQException(e.getMessage());
			}
		}
	}
}

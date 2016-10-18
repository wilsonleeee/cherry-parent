/*  
 * @(#)Java2Drl.java     1.0 2011/05/31      
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
package com.cherry.ps.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class Java2Drl {
	/**
	 * 提取规则关键字
	 * @param conditionKeywordList
	 * @param jsStr
	 */
	public static void getKeywords ( List conditionKeywordList,String jsStr) {
		String[] arrStr = jsStr.split("#");
		for (int i=0;i<arrStr.length;i++){
			if (i%2!=0){
				conditionKeywordList.add(arrStr[i]);
			}
		}
	}
	
	/**
	 * 替换规则关键字
	 * @param keyMap
	 * @param jsStr
	 * @return
	 */
	public static String keyParse ( HashMap keyMap ,String jsStr) {
		Iterator iter = keyMap.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    String key = (String)entry.getKey(); 
		    String val = (String)entry.getValue(); 
		    jsStr  = jsStr.replace("#"+ key +"#", val);
		} 
		return jsStr;
	}
	
	public static String joinConditionStr (){
		StringBuffer sb = new StringBuffer();
		sb.append("rule \"rule1_20100501\" \n");
		sb.append("when \n");
		sb.append("\t $p : FactDTO( p20100501==1 || buyCount > 300 ) \n");
		sb.append("then \n");
		sb.append("\t Event_20100501_DTO event_20100501_DTO = new Event_20100501_DTO() \n");
		sb.append("\t event_20100501_DTO.setFactDto($p); \n");
		sb.append("\t $p.setP20100501(1); \n");
		sb.append("\t insert(event_20100501_DTO);\n");
		sb.append("end \n");
		return sb.toString();
	}
	
	public static String joinConditionStr2 (){
		StringBuffer sb2 = new StringBuffer();
		sb2.append("when \n");
		sb2.append("\t $p : FactDto(); \n");
		sb2.append("\t Event_20100501_DTO (factDto == $p ) \n");
		sb2.append("then \n");
		sb2.append("\t point*2 \n");
		return sb2.toString();
	}
	
	public static String declearStr (){
		StringBuffer sb = new StringBuffer();
		sb.append("declare 2010/05/01_Dto \n ");
		sb.append("\t Txddate : Date \n");
		sb.append("\t Quantity : int \n");
		sb.append("end \n");
		return sb.toString();
	}
}

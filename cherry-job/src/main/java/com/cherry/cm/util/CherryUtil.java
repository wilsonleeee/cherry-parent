/*		
 * @(#)CherryUtil.java     1.0 2010/10/12		
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
package com.cherry.cm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;

/**
 * 共通处理类（与业务无关，主要是常用的java方法）
 * 
 * @author dingyc
 * 
 */
@SuppressWarnings("unchecked")
public class CherryUtil {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/** 字符数组 */
	public static final char[] CHAR_ARRAY = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9' };
	
	/** 数字字符数组 */
	public static final char[] CHAR_NUM_ARRAY = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9' };

	/**
	 * 将String转换为int型，不能转换的将返回0值
	 * 
	 * @param arg
	 * @return
	 */
	public static int string2int(String arg) {
		try {
			if ("null".equals(arg) || "NULL".equals(arg)) {
				return 0;
			}
			return Integer.valueOf(arg);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 将String转换为int型，不能转换的将返回0值
	 * 
	 * @param arg
	 * @return
	 */
	public static int obj2int(Object obj) {
		if (null == obj) {
			return 0;
		}
		if (obj instanceof Number) {
			Number num = (Number)obj;
			return num.intValue();
		}else if (obj instanceof String) {
			return string2int((String) obj);
		} else {
			return 0;
		}
	}

	/**
	 * 将String转换为int型，不能转换的将返回0值
	 * 
	 * @param arg
	 * @return
	 */
	public static double string2double(String arg) {
		try {
			if ("null".equals(arg) || "NULL".equals(arg)) {
				return 0.00;
			}
			return Double.valueOf(arg);
		} catch (Exception ex) {
			return 0.00;
		}
	}

	/**
	 * 取得系统的当前时间，并以指定的格式返回字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getSysDateTime(String format) {
		SimpleDateFormat dateFm = new SimpleDateFormat(format); // 格式化当前系统日期
		String dateTime = dateFm.format(new java.util.Date());
		return dateTime;
	}

	/**
	 * 去除Map中的空值
	 * 
	 * @param Map
	 *            转换前的Map
	 * @return Map 转换后的Map
	 */
	public static Map<String, Object> removeEmptyVal(Map<String, Object> map) {
		if (null != map && !map.isEmpty()) {
			Set keys = map.keySet();
			// Map中所有的key
			Object[] keyArr = keys.toArray();
			Map goalMap = new HashMap();
			for (Object key : keyArr) {
				Object value = map.get(key);
				// 值不为空时放入新的Map中
				if (null != value && !"".equals(value)) {
					goalMap.put(key, value);
				}
			}
			return goalMap;
		}
		return null;
	}

	/**
	 * 去除Map中的空值
	 * 
	 * @param Map
	 *            转换前的Map
	 * @param String
	 *            [] 不处理的字段
	 * 
	 * @return Map 转换后的Map
	 */
	public static Map<String, Object> removeEmptyValIgnored(
			Map<String, Object> map, String[] ignoredKeys) {
		if (null != map && !map.isEmpty()) {
			Set keys = map.keySet();
			// Map中所有的key
			Object[] keyArr = keys.toArray();
			Map goalMap = new HashMap();
			for (Object key : keyArr) {
				Object value = map.get(key);
				if (null != ignoredKeys && 0 != ignoredKeys.length) {
					boolean ignoredFlg = false;
					for (String ignoredKey : ignoredKeys) {
						if (null == ignoredKey && null == key
								|| null != ignoredKey && ignoredKey.equals(key)) {
							ignoredFlg = true;
							break;
						}
					}
					if (ignoredFlg) {
						goalMap.put(key, value);
						continue;
					}
				}
				// 值不为空时放入新的Map中
				if (null != value && !"".equals(value)) {
					goalMap.put(key, value);
				}
			}
			return goalMap;
		}
		return null;
	}

	/**
	 * 设置日期的时分秒毫秒
	 * 
	 * @param String
	 *            转换前的日期 int 日期区分(0:开始日期,1:结束日期)
	 * @return String 转换后的日期
	 */
	public static String suffixDate(String date, int dateKbn) {
		if (null != date && !"".equals(date)) {
			if (CherryChecker.checkDate(date)) {
				if (0 == dateKbn) {
					return date + " 00:00:00.000";
				} else {
					return date + " 23:59:59.000";
				}
			}
		}
		return date;
	}

	/**
	 * 复制文件
	 * 
	 * @param File
	 *            原始文件
	 * 
	 * @param File
	 *            目标文件
	 * @return
	 * @throws Exception
	 */
	public static void copyFileByChannel(File src, File dst) throws Exception {
		int length = CherryConstants.COPYFILE_MAX_SIZE;
		// 输入流
		FileInputStream in = null;
		// 输出流
		FileOutputStream out = null;
		// 输入管道
		FileChannel inC = null;
		// 输出管道
		FileChannel outC = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);
			inC = in.getChannel();
			outC = out.getChannel();
			ByteBuffer buffer = null;
			while (true) {
				if (inC.position() == inC.size()) {
					inC.close();
					outC.close();
					in.close();
					out.close();
					return;
				}
				if ((inC.size() - inC.position()) < length) {
					length = (int) (inC.size() - inC.position());
				} else {
					length = CherryConstants.COPYFILE_MAX_SIZE;
				}
				buffer = ByteBuffer.allocateDirect(length);
				inC.read(buffer);
				buffer.flip();
				outC.write(buffer);
				outC.force(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != inC) {
				inC.close();
			}
			if (null != outC) {
				outC.close();
			}
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}

	}

	/**
	 * 比较两个数字是否相等
	 * 
	 * @param String
	 *            数字1
	 * 
	 * @param String
	 *            数字2
	 * @return boolean 比较结果
	 * 
	 */
	public static boolean equalsDouble(String num1, String num2) {
		if ((null == num1 || "".equals(num1))
				&& (null == num2 || "".equals(num2))) {
			return true;
		}
		try {
			double dou1 = Double.parseDouble(num1.trim());
			double dou2 = Double.parseDouble(num2.trim());
			if (dou1 == dou2) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 生成随机密码
	 * 
	 * @param length
	 *            随机密码长度
	 * 
	 * @return 随机密码
	 * 
	 */
	public static String generateSalt(int length) {

		StringBuffer buffer = new StringBuffer(
				"0123456789abcdefghijklmnopqrstuvwxyz");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++) {
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	/**
	 * 取得len长度的随机字符串
	 * 
	 * @param len
	 * @return
	 */
	public static String getRandomStr(int len) {
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for (int i = 1; i <= len; i++) {
			sb.append(CHAR_ARRAY[r.nextInt(CHAR_ARRAY.length)]);
		}
		return sb.toString();
	}
	
	/**
	 * 取得len长度的随机字符串
	 * 
	 * @param len
	 * @return
	 */
	public static String getNumRandomStr(int len) {
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for (int i = 1; i <= len; i++) {
			sb.append(CHAR_NUM_ARRAY[r.nextInt(CHAR_NUM_ARRAY.length)]);
		}
		return sb.toString();
	}
	
	/**
	 * Map中的值trim处理
	 * 
	 * @param Map
	 */
	public static void trimMap(Map<String, Object> map) {
		if (null != map && !map.isEmpty()) {
			// 循环map剔除value值前后空格
			for(Map.Entry<String,Object> en: map.entrySet()){
				if(en.getValue() instanceof String){
					en.setValue(((String)en.getValue()).trim());
				}
			}
		}
	}
	
	/**
	 * 把不符合JSON对象的字符串转换成符合JSON对象的字符串
	 * 
	 * @param s 不符合JSON对象的字符串
	 * @return 符合JSON对象的字符串
	 */
	public static String convertStrToJson(String s) {
		if(s != null && (s.contains("\"") || s.contains("\\"))) {
			return s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		} else {
			return s;
		}
	}
	
	/**
	 * 利用Jackson将json格式的字符串解析成Map
	 * 
	 * */
	public static Map json2Map(String json) throws Exception{
		Map<String, Object> maps = null;
		try {
			maps = objectMapper.readValue(json, Map.class);
		} catch (Exception e) {
			throw e;
		} 
		return maps;
	}
	
	/**
	 * 利用Jackson将Map转化成json格式的字符串
	 * 
	 * 
	 * */
	public static String map2Json(Map map) throws Exception{
		String dataLine = null;
		try {
			dataLine = objectMapper.writeValueAsString(map);
		} catch (Exception e) {
			throw e;
		} 
		return dataLine;
	}  
	
	/**
	 * 利用Jackson将List转化成json格式的字符串
	 * 
	 * 
	 * */
	public static String list2Json(List<Map<String, Object>> list) throws Exception{
		String dataLine = null;
		try {
			dataLine = objectMapper.writeValueAsString(list);
		} catch (Exception e) {
			throw e;
		} 
		return dataLine;
	}  
	
	public static List<Map<String,Object>> json2ArryList(String json) throws Exception {
		List<Map<String,Object>> list = null;
		try {
			list = objectMapper.readValue(json,ArrayList.class);
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	
	/**
	 * 遍历maps将key做如下处理：如果第二个字母是大写则不处理，否则将首字母转为小写
	 * 并且将map中的key和value分别进行trim，去掉前后的空格
	 * 
	 * */
	public static Map dealMap(Map<String,Object> map){
		
		Map<String,Object> dealedMap = new HashMap<String,Object>();
		
		for(Map.Entry<String, Object> temp : map.entrySet()){
			
			//取得map中的value
			Object value = temp.getValue();
			String key = temp.getKey().trim();
			
			if (key.length() == 1) {
                key = key.toLowerCase();
            } else if (!Character.isUpperCase(key.charAt(1))) {
                key = key.substring(0, 1).toLowerCase() +
                    key.substring(1);
            }
			
			//如果value是集合类型深度递归继续处理
			if(value instanceof List){
				List<Map> list = (List<Map>)value;
				List<Map> list1 = new ArrayList<Map>();
				for(Map temp1 : list){
					Map tempMap1 = dealMap(temp1);
					list1.add(tempMap1);
				}
				dealedMap.put(key, list1);
			}else if(value instanceof Map){
				Map temp2 = dealMap((Map)value);
				dealedMap.put(key, temp2);
			}else if(null == value){
				dealedMap.put(key,"");
			}else{
				dealedMap.put(key,value.toString().trim());
			}
		}
		return dealedMap;
	}
	
	/**
	  * 左补位，右对齐
	  * @param oriStr  原字符串
	  * @param len  目标字符串长度
	  * @param alexin  补位字符
	  * @return  目标字符串
	  */
	public static String padLeft(String oriStr, int len, char alexin) {
		int strlen = oriStr.length();
		if(strlen < len) {
			StringBuffer str = new StringBuffer();
			for(int i = 0; i < len - strlen; i++) {
				str.append(alexin);
			}
			return str.toString() + oriStr;
		}
		return oriStr;
	}
	
	/**
	 * 比较一个list中的String，返回最大的String
	 * @param list
	 * @return
	 */
	public static String getMaxNoInList(List<Integer> list) {
		if(list==null||list.size()==0){
			return "";
		}
		 int retInt = 0;
		 for (int i=0; i<list.size(); i++){
		   if (list.get(i)> retInt ){
			   retInt = list.get(i);
		     }
		 }
		 return String.valueOf(retInt);
	}
	
	/**
	 * 根据年龄和基准时间取得生日年
	 * @param age 年龄
	 * @param standardDate 基准时间（一般取系统时间）
	 * @return 生日年
	 */
	public static String getYearByAge(String age, String standardDate) {
		String year = null;
		if(age != null && !"".equals(age) && standardDate != null && !"".equals(standardDate)) {
			try {
				year = String.valueOf(Integer.parseInt(standardDate.substring(0,4)) - Integer.parseInt(age) + 1);
			} catch (Exception e) {
				return year;
			}
		}
		return year;
	}
	
	public static String obj2Json(Object obj) throws Exception{
		String dataLine = null;
		try {
			dataLine = objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw e;
		} 
		return dataLine;
	} 
	
    /**
     * 替换拼装MQ消息字段的特殊字符（单个字段）
     * @param map
     * @return
     */
    public static String replaceMQSpecialChar(String key){
        if(null != key && !key.equals("")){
            key = key.replaceAll(",", "，").replaceAll("'", "~").replaceAll("\r\n", " ").replaceAll("\n\r", " ").replaceAll("\r", " ").replace("\n", " ");
        }
        return key;
    }
    
    /**
     * 替换拼装MQ消息字段的特殊字符（整个map）
     * @param map
     * @return
     */
    public static Map<String,Object> replaceMQSpecialChar(Map<String,Object> map){
        if (null != map && !map.isEmpty()) {
            for(Map.Entry<String,Object> en: map.entrySet()){
                if(en.getValue() instanceof String){
                    String value = ConvertUtil.getString(en.getValue());
                    en.setValue(replaceMQSpecialChar(value));
                }
            }
        }
        return map;
    }
    /*
	 * 取得子list
	 * 
	 * @param list
	 * @param pageInfo
	 * @return
	 */
	public static <T> List<T> getSubList(List<T> list,
			int fromIndex, int pageLength) {
		int listSize = list.size();
		int toIndex = fromIndex + pageLength;
		List<T> subList = null;
		if (fromIndex < listSize) {
			if (listSize < toIndex) {
				toIndex = listSize;
			}
			subList = list.subList(fromIndex, toIndex);
		}
		return subList;
	}
}

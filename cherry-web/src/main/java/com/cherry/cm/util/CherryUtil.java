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

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static final String[] RANDOM_ALPHABET = new String[]{"M", "u", "e", "o", "Y", "a", "V", "I", "m", "z", "K", "D", "W", "Z", "S", "j", "9", "A", "v", "B", "s", "J", "p", "d", "5", "P", "k", "N", "c", "H", "R", "0", "Q", "q", "O", "1", "7", "x", "T", "l", "L", "g", "U", "b", "f", "8", "t", "4", "n", "G", "i", "6", "w", "F", "E", "y", "2", "C", "X", "r", "3", "h"};

	/**
	 * 一维码校验数据位生成
	 * 第一位为时间戳的最后9位
	 * 第二位为会员编码后四位加时间戳后四位
	 * @param memCode
	 * @param strCurrTime
	 * @return
	 */
	public static String generateOneDimesionEncrptData(String memCode, String strCurrTime) {
		if(!StringUtils.isEmpty(memCode) && !StringUtils.isEmpty(strCurrTime)) {
			int memCodeLength = memCode.length();
			int strCurrTimeLength = strCurrTime.length();

			if(strCurrTimeLength > 9) { // 只去最后9位，int类型最大10位
				strCurrTime = strCurrTime.substring(strCurrTimeLength-9);
			}
			String fisrtEncrpt = generateEncrptData(strCurrTime);

			memCode = memCodeLength > 4 ? memCode.substring(memCodeLength-4) : memCode; // 取memcode的最后4位
			strCurrTime = strCurrTimeLength > 4 ? strCurrTime.substring(strCurrTimeLength-4) : strCurrTime; // 取strCurrTime的最后4位

			StringBuffer secondEncrptSb = new StringBuffer();
			secondEncrptSb.append(memCode).append(strCurrTime);
			String secondEncrpt = generateEncrptData(secondEncrptSb.toString());

			return fisrtEncrpt + secondEncrpt ;

		}
		return null;
	}

	/**
	 * 生成一维码时压缩时间格式，使生成一维码长度减少
	 * 时间格式为"yyMMddHHmmss"
	 * @param strCurrTime
	 */
	public static String compressOneDimesion(String strCurrTime) {
		if(!StringUtils.isEmpty(strCurrTime)) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < strCurrTime.length(); i += 2) {
				String temp = strCurrTime.substring(i,i+2);
				sb.append(RANDOM_ALPHABET[Integer.valueOf(temp)]);
			}
			return sb.toString();
		}
		return null ;
	}

	/**
	 * 还原时间戳
	 * @param strCurrTime
	 * @return
	 */
	public static String expandOneDimesion(String strCurrTime) {
		if(!StringUtils.isEmpty(strCurrTime)) {
			StringBuffer sb = new StringBuffer();
			List<String> randomAlphabetList = Arrays.asList(RANDOM_ALPHABET);
			for (int i = 0; i < strCurrTime.length(); i++) {
				String temp = strCurrTime.substring(i,i+1);
				int index = randomAlphabetList.indexOf(temp);
				String strIndex = index < 10 ? "0"+index : String.valueOf(index);
				sb.append(strIndex);
			}
			return sb.toString();
		}
		return null ;
	}

	/**
	 * 生成加密字符
	 * @param str
	 * @return
	 */
	public static String generateEncrptData(String str) {
		if(!StringUtils.isEmpty(str)) {
			StringBuffer sb = new StringBuffer();
			char[] cArry = str.toCharArray();
			for (int i = 0; i < cArry.length; i++) {
				if (cArry[i] >= 48 && cArry[i] <= 57) { // 去除非数字字符
					sb.append(cArry[i]);
				}
			}
			if(sb.length() > 0) {
				String resultStr = sb.toString().replaceAll("0","6");
				return RANDOM_ALPHABET[(Integer.valueOf(resultStr) % 62)];
			}
		}
		return null;
	}

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
	 * 将String转换为long型，不能转换的将返回0值
	 *
	 * @param arg
	 * @return
	 */
	public static long string2Long(String arg) {
		try {
			if ("NULL".equalsIgnoreCase(arg)) {
				return 0;
			}
			return Long.valueOf(arg);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 将String转换为int型，不能转换的将返回0值
	 *
	 * @param obj
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
	 * 将Object转换为long型，不能转换的将返回0值
	 *
	 * @param obj
	 * @return
	 */
	public static long obj2Long(Object obj) {
		if (null == obj) {
			return 0;
		}
		if (obj instanceof Number) {
			Number num = (Number)obj;
			return num.longValue();
		}else if (obj instanceof String) {
			return string2Long((String) obj);
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
	 * @param map
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
	 * @param map
	 *            转换前的Map
	 * @param ignoredKeys
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
	 * @param date
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
	 * @param src
	 *            原始文件
	 *
	 * @param dst
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
	 * @param num1
	 *            数字1
	 *
	 * @param num2
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
	 * Map中的值trim处理
	 *
	 * @param map
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
	 * 特殊字符处理
	 * @param s
	 * @return
	 */
	public static String convertSpecStr(String s){
		s = String.valueOf(s).replaceAll("\\\\r", "");
		s = String.valueOf(s).replaceAll("\\\\n", "");
		s = String.valueOf(s).replaceAll("\\\\t", "");
		s = String.valueOf(s).replaceAll("\\\\", "");

		return s;

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

	public static String obj2Json(Object obj) throws Exception{
		String dataLine = null;
		try {
			dataLine = objectMapper.writeValueAsString(obj);
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

	/** Cron表达式时间规则常量定义 **/
	public static final String EVERY = "*";
	public static final String ANY = "?";
	public static final String RANGES = "-";
	public static final String INCREMENTS = "/";
	public static final String ADDITIONAL = ",";
	public static final String LAST = "L";
	public static final String WEEKDAY = "W";
	public static final String THENTH = "#";
	public static final String CALENDAR = "C";
	public static final String BLANK = " ";

	/**
	 * 页面设置转为Cron表达式
	 * @param type 沟通时间类型（1：指定时间，2：参考某个时间，3：循环执行，4：条件触发）
	 * @param date 沟通日期值（格式为：yyyy-MM-dd）
	 * @param time 沟通时间值（格式为：HH:mm:ss）
	 * @param frequency 频率（YY：每年，MM：每月，DD：没天）
	 * @return Cron表达式
	 */
	public static String convertDateToCronExp(String type, String date, String time, String frequency) {
		String cronEx = "";
		try {
			if("1".equals(type)) {
				String[] dates = date.split("-");
				String[] times = time.split(":");
				cronEx = times[2] + BLANK + times[1] + BLANK + times[0] + BLANK + dates[2] + BLANK + dates[1] + BLANK + ANY + BLANK + dates[0];
			} else if("2".equals(type)) {
				String[] times = time.split(":");
				cronEx = times[2] + BLANK + times[1] + BLANK + times[0] + BLANK + EVERY + BLANK + EVERY + BLANK + ANY;
			} else if("3".equals(type)) {
				if("YY".equals(frequency)) {
					String[] dates = date.split("-");
					String[] times = time.split(":");
					cronEx = times[2] + BLANK + times[1] + BLANK + times[0] + BLANK + dates[1] + BLANK + dates[0] + BLANK + ANY;
				} else if("MM".equals(frequency)) {
					String[] times = time.split(":");
					cronEx = times[2] + BLANK + times[1] + BLANK + times[0] + BLANK + date + BLANK + EVERY + BLANK + ANY;
				} else if("DD".equals(frequency)) {
					String[] times = time.split(":");
					cronEx = times[2] + BLANK + times[1] + BLANK + times[0] + BLANK + EVERY + BLANK + EVERY + BLANK + ANY;
				}
			} else if("4".equals(type)) {
				String[] times = time.split(":");
				cronEx = times[2] + BLANK + times[1] + BLANK + times[0] + BLANK + EVERY + BLANK + EVERY + BLANK + ANY;
			}
		} catch (Exception e) {

		}
		return cronEx;
	}

	/**
	 * 去除Map中的空值
	 *
	 * @param map
	 *            转换前的Map
	 * @return Map 转换后的Map
	 */
	public static Map<String, Object> remEmptyVal(Map<String, Object> map) {
		if (null != map && !map.isEmpty()) {
			Map<String, Object> goalMap = new HashMap<String, Object>();
			for (String key : map.keySet()) {
				Object value = map.get(key);
				if(value instanceof ArrayList) {
					List _value = (List)value;
					if(_value != null && !_value.isEmpty()) {
						goalMap.put(key, value);
					}
				} else {
					// 值不为空时放入新的Map中
					if (null != value && !"".equals(value)) {
						goalMap.put(key, value);
					}
				}
			}
			return goalMap;
		}
		return null;
	}
	/*
	 * 取得子list
	 *
	 * @param list
	 * @param pageInfo
	 * @return
	 */
	public static List<Map<String, Object>> getSubList(List<Map<String, Object>> list,
													   int fromIndex, int pageLength) {
		int listSize = list.size();
		int toIndex = fromIndex + pageLength;
		List<Map<String, Object>> subList = null;
		if (fromIndex < listSize) {
			if (listSize < toIndex) {
				toIndex = fromIndex + listSize;
			}
			subList = list.subList(fromIndex, toIndex);
		}
		return subList;
	}

	/**
	 * 除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位
	 *
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if(scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 取得资源的值
	 * @param baseName 资源的文件名称（无语言无后缀）。取共通资源传值null或""
	 * @param language 语言
	 * @param key 资源的键
	 */
	public static String getResourceValue(String baseName, String language, String key) {
		try{
			//为空时，查询共通语言资源文件
			String path = "i18n/common/commonText";
			if(null != baseName && !"".equals(baseName)){
				String sysName = baseName.substring(5, 7);
				//Linux下大小写敏感，传入资源的文件名称全是大写，截取的系统名也是大写
				//但是实际文件夹的名称是小写，这样就取不到目标文件
				//为了在兼容Linux，这里把截取后的系统名转成小写。
				sysName = sysName.toLowerCase();
				path = "i18n/"+sysName+"/"+baseName;
			}
			return LocalizedTextUtil.findResourceBundle(path, new Locale(language.substring(0, 2),language.substring(3,5))).getString(key);
		}catch(Exception e){
			return key;
		}
	}

	/**
	 * 取得资源的值
	 * @param pacName 包名
	 * @param name 资源的文件名称
	 * @param language 语言
	 * @param key 资源的键
	 */
	public static String getResourceValue(String pacName, String name, String language, String key) {
		try{
			String path = "i18n/"+pacName+"/"+name;
			return LocalizedTextUtil.findResourceBundle(path, new Locale(language.substring(0, 2),language.substring(3,5))).getString(key);
		}catch(Exception e){
			return key;
		}
	}

	/**
	 * 中英混合字符串的字节长度
	 * @param value
	 * @return
	 */
	public static int mixStrLength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
			String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
				valueLength += 2;
			} else {
                /* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 获取百分比
	 *
	 * @param  p1 被除数
	 * @param  p2 除数
	 * @param  scale 保留的小数位数
	 * @return 百分比
	 */
	public static String percent(double p1, double p2, int scale) {
		String str;
		double p3 = p1/p2;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(scale);
		str = nf.format(p3);
		return str;
	}

	/**
	 * 替换拼装MQ消息字段的特殊字符（单个字段）
	 * @param key
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

	/**
	 * 删除单个文件
	 *
	 * @param sPath 被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件 
	 *
	 * @param sPath 被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false 
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) break;
			}
			else {// 删除子目录
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) break;
			}
		}
		if (!flag) return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除指定session的所有临时文件
	 *
	 * @param sessionId
	 * @return 删除成功返回true，否则返回false 
	 */
	public static boolean deleteTempFile(String sessionId) {
		String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
		tempFilePath = tempFilePath + File.separator + sessionId;
		return deleteDirectory(tempFilePath);
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s){
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 判断是否是空的LIST
	 *
	 * @param list
	 * @return
	 */
	public static boolean isBlankList(List list) {
		if (null == list || list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用*号替换部分字符串
	 *
	 * @param str 原字符串
	 * @param m 前m位保留原值
	 * @param n 后n位保留原值
	 * @return 替换后的字符串
	 */
	public static String replaceSubString(String str, int m, int n) {
		String sub = "";
		try {
			sub = str.substring(0, m);
			int length = str.length()-n-m;
			StringBuffer sb=new StringBuffer();
			for(int i = 0; i < length; i++){
				sb = sb.append("*");
			}
			sub += sb.toString();
			sub += str.substring(str.length()-n, str.length());
		} catch (Exception e) {
			return str;
		}
		return sub;
	}
	/**
	 * 检测指定对象是否为null或者空字符串 如果对象为null或空字符串则返回true，否则返回false
	 *
	 * @param value
	 *            待检测的对象
	 * @param trim
	 *            可选参数，是否对要检查的string先做trim处理
	 * @return
	 */
	public static boolean isEmpty(String value, boolean... trim) {
		if(trim.length > 0 && trim[0] == true){
			return value == null || value.trim().length() == 0;
		}else{
			return value == null || value.length() == 0;
		}
	}
	/**
	 * 检查手机号
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 　　联通：130、131、132、152、155、156、185、186
	 　　电信：133、153、180、189、（1349卫通）
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		if(isEmpty(mobiles)){
			return false;
		}
		// TODO:修改正则表达式
		Pattern p = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 获取月份起始日期
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getMinMonthDate(String date) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(date));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return dateFormat.format(calendar.getTime());
	}
}

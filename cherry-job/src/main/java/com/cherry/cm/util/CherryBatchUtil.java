/**		
 * @(#)BINOLCMBAS01_Action.java     1.0 2010/10/12		
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 共通处理类（与业务无关，主要是常用的java方法）
 * 
 * @author zhangjie
 * 
 */
public class CherryBatchUtil {

	/** 字符数组 */
	public static final char[] CHAR_ARRAY = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9' };

	/**
	 * 将String转换为int型，不能转换的将返回0值
	 * 
	 * @param arg
	 * @return
	 */
	public static int string2int(String arg) {
		try {
			return Integer.valueOf(arg);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
     * 将String转换为double型，不能转换的将返回0值
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
	 * 将Object转换为int型，不能转换的将返回0值
	 * 
	 * @param arg
	 * @return
	 */
	public static int Object2int(Object ob) {
		try {
			return ((Integer) ob).intValue();
		} catch (Exception ex) {
			return 0;
		}
	}
	
	/**
     * 将有小数的Object转换为int型，不能转换的将返回0值
     * 
     * @param arg
     * @return
     */
    public static int decimal2int(Object ob) {
        try {           
            float a = Float.parseFloat(String.valueOf(ob));
            return (int)a;
        } catch (Exception ex) {
            return 0;
        }
    }

	/**
     * 将Object转换为double型，不能转换的将返回0值
     * 
     * @param arg
     * @return
     */
    public static double Object2double(Object ob) {
        try {
            return ((Double) ob).doubleValue();
        } catch (Exception ex) {
            return 0;
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
	 * 判断是否是空的字符串
	 * 
	 * @param Sting
	 * @return
	 */
	public static boolean isBlankString(String st) {
		if (null == st || "".equals(st)) {
			return true;
		} else {
			return false;
		}
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
	 * obj 转换成字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj) {
		if (null == obj) {
			return "";
		}
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return obj.toString();
		}
	}

	/**
	 * obj 转换成float,不能转换的返回0
	 * 
	 * @param obj
	 * @return
	 */
	public static float bigDecimalTofloat(Object obj) {
		try {
			return ((BigDecimal) obj).floatValue();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * <p>
	 * 层级List转换(支持多层转换)
	 * </p>
	 * 
	 * @param List
	 *            转换前List
	 * @param List
	 *            转换后List
	 * @param List
	 *            多层key
	 * @param int 层级
	 * @return 无
	 * 
	 * @throws 无
	 */
	public static void convertList2DeepList(List<Map<String, Object>> list,
			List<Map<String, Object>> rootList, List<String[]> keysList,
			int deep) {
		if (list != null && rootList != null && keysList != null
				&& keysList.size() > deep) {
			String[] keys = keysList.get(deep);
			String id = null;
			if (keys != null && keys.length > 0) {
				id = keys[0];
			}
			for (Map<String, Object> map : list) {
				// 判断是否已经存在了
				boolean isNotEqual = true;
				for (Map<String, Object> rootMap : rootList) {
					// 外层List的id
					Object value = rootMap.get(id);
					/* id相等的时候说明已经存在 */
					if (value != null && value.equals(map.get(id))) {
						// 更新外层List已存在的map
						updateRootList(2, map, rootMap, "list", id, keys);
						isNotEqual = false;
						break;
					}
				}
				if (isNotEqual) {
					Map<String, Object> rootMap = new HashMap<String, Object>();
					rootList.add(rootMap);
					// 往外层List添加map
					updateRootList(1, map, rootMap, "list", id, keys);
				}
			}
			if (deep < keysList.size() - 1) {
				deep++;
				for (Map<String, Object> rootMap : rootList) {
					List<Map<String, Object>> deepList = (List<Map<String, Object>>) rootMap
							.get("list");
					List<Map<String, Object>> deepListNew = new ArrayList<Map<String, Object>>();
					rootMap.put("list", deepListNew);
					convertList2DeepList(deepList, deepListNew, keysList, deep);
				}
			}
		}
	}

	/**
	 * <p>
	 * 层级List转换(两层)
	 * </p>
	 * 
	 * @param List
	 *            转换前List
	 * @param String
	 *            里面一层List的名字
	 * @param String
	 *            外层List的id属性
	 * @param String
	 *            [] 外层List其它的属性
	 * @return String 转换好的层级List
	 * 
	 * @throws 无
	 */
	public static List<Map<String, Object>> convertList2HierarchyList(
			List<Map<String, Object>> list, String depName, String id,
			String... keys) {
		// 外层List
		List<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (Map<String, Object> map : list) {
				// 判断是否已经存在了
				boolean isNotEqual = true;
				for (Map<String, Object> rootMap : rootList) {
					// 外层List的id
					Object value = rootMap.get(id);
					/* id相等的时候说明已经存在 */
					if (value != null && value.equals(map.get(id))) {
						// 更新外层List已存在的map
						updateRootList(2, map, rootMap, depName, id, keys);
						isNotEqual = false;
						break;
					}
				}
				if (isNotEqual) {
					Map<String, Object> rootMap = new HashMap<String, Object>();
					rootList.add(rootMap);
					// 往外层List添加map
					updateRootList(1, map, rootMap, depName, id, keys);
				}

			}
		}
		return rootList;
	}

	/**
	 * <p>
	 * 更新外层List
	 * </p>
	 * 
	 * @param int 更新区分
	 * @param Map
	 *            读取的map
	 * @param String
	 *            外层List的map
	 * @param String
	 *            里面一层List的名字
	 * @param String
	 *            外层List的id属性
	 * @param String
	 *            [] 外层List其它的属性
	 * @return 无
	 * 
	 * @throws 无
	 */

	private static void updateRootList(int updateFlg, Map<String, Object> map,
			Map<String, Object> rootMap, String depName, String id,
			String[] keys) {
		// 里层List
		List<Map<String, Object>> depList = new ArrayList<Map<String, Object>>();
		if (map != null && rootMap != null) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.putAll(map);
			if (updateFlg == 1) {
				// 外层List的id属性
				rootMap.put(id, tempMap.get(id));
				tempMap.remove(id);
				// 往外层List中添加里层List属性
				rootMap.put(depName, depList);
			} else {
				depList = (List<Map<String, Object>>) rootMap.get(depName);
				tempMap.remove(id);
			}
			if (keys != null) {
				/* 往外层List中添加其它属性 */
				for (String key : keys) {
					if (id != null && !id.equals(key)) {
						if (updateFlg == 1) {
							rootMap.put(key, tempMap.get(key));
						}
						tempMap.remove(key);
					}
				}
			}
			Map<String, Object> depMap = new HashMap<String, Object>();
			depMap.putAll(tempMap);
			if (depList != null) {
				depList.add(depMap);
			}
		}
	}

	/**
	 * 设定批处理LOG错误信息
	 * 
	 * @param errCode
	 * @param object
	 * @throws CherryBatchException
	 */
	public static void setErrorLog(String errCode, Object object)
			throws CherryBatchException {
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		batchLoggerDTO.setCode(errCode);
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);

		if (object instanceof String) {
			// CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
			// (String)object);
			// cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(object
					.getClass());
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
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
	 * 设定批处理结果log信息
	 * 
	 * @param successCount
	 * @param failCount
	 * @param object
	 * @throws CherryBatchException
	 */
	public static void setBatchResultLog(int successCount, int failCount,
			Object object) throws CherryBatchException {
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(successCount + failCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));

		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(object
				.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
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
	 * 取得不重复的List
	 * @param srcList
	 * @param key
	 * @return
	 */
	public static List<Map<String,Object>> getNotRepeatList(List<Map<String,Object>> srcList,String key){
		List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
		List<String> keyList = new ArrayList<String>();
		if(null != srcList){
			for(Map<String,Object> map : srcList){
				String value = getString(map.get(key));
				if(!keyList.contains(value)){
					keyList.add(value);
					resList.add(map);
				}
			}
		}
		return resList;
	}
	
	/**
	 * 取得不重复的List
	 * @param srcList
	 * @param key
	 * @return
	 */
	public static List<String> getKeyList(List<Map<String,Object>> srcList, String key, boolean repeat){
		List<String> keyList = new ArrayList<String>();
		if(null != srcList){
			for(Map<String,Object> map : srcList){
				String value = getString(map.get(key));
				if(repeat || !keyList.contains(value)){
					keyList.add(value);
				}
			}
		}
		return keyList;
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
	 * 截取符合长度的中英混合字符串（left）
	 * 适用场合：数据库中存储为varchar数据类型时（中文占2个字节，其他一般1字节）
	 * @param str
	 * @param limit 字符限制长度
	 */
	public static String mixStrsub(String str,int limit){
		if(null == str){
			return null;
		}
		int length = mixStrLength(str);
		if(length > limit){
			str = str.substring(0, str.length()-1);
			return mixStrsub(str,limit);
			
		} else {
			return str;
		}
	}
	
}

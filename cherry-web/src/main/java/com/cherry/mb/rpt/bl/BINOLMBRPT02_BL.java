/*
 * @(#)BINOLMBRPT02_BL.java     1.0 2014/07/17
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
package com.cherry.mb.rpt.bl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM09_BL;
import com.cherry.mb.rpt.service.BINOLMBRPT02_Service;

/**
 * 会员微信绑定数统计报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/07/17
 */
public class BINOLMBRPT02_BL {
	
	/** 会员微信绑定数统计报表Service **/
	@Resource
	private BINOLMBRPT02_Service binOLMBRPT02_Service;
	
	/** 会员搜索画面BL */
	@Resource
	private BINOLMBMBM09_BL binOLMBMBM09_BL;
	
	/** 导出会员信息共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 统计会员绑定数
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public Map<String, Object> getBindCount(Map<String, Object> map) {
		
		// 绑定起始时间
		String wechatBindTimeStart = (String)map.get("wechatBindTimeStart");
		// 绑定结束时间
		String wechatBindTimeEnd = (String)map.get("wechatBindTimeEnd");
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		if(wechatBindTimeStart == null || "".equals(wechatBindTimeStart)) {
			Map<String, Object> wechatBindTimeRange = binOLMBRPT02_Service.getWechatBindTimeRange(map);
			if(wechatBindTimeRange == null) {
				return null;
			}
			// 绑定起始时间
			wechatBindTimeStart = (String)wechatBindTimeRange.get("wechatBindTimeStart");
			if(wechatBindTimeStart == null || "".equals(wechatBindTimeStart)) {
				return null;
			}
			map.put("wechatBindTimeStart", wechatBindTimeStart);
		}
		map.put("recordDateTimeStart", DateUtil.suffixDate(wechatBindTimeStart, 0));
		
		if(wechatBindTimeEnd == null || "".equals(wechatBindTimeEnd)) {
			// 系统时间作为绑定结束时间
			String sysDateTime = binOLMBRPT02_Service.getSYSDateTime();
			wechatBindTimeEnd = sysDateTime.substring(0, 10);
			map.put("wechatBindTimeEnd", wechatBindTimeEnd);
			map.put("recordDateTimeEnd", sysDateTime);
		} else {
			map.put("recordDateTimeEnd", DateUtil.suffixDate(wechatBindTimeEnd, 1));
		}
		
		// 入会时间配置项（0：入会时间，1：销售首单）
		String joinDateFlag = binOLCM14_BL.getConfigValue("1286", String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), 
				String.valueOf(map.get(CherryConstants.BRANDINFOID)));
		map.put("joinDateFlag", joinDateFlag);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 取得会员等级信息List
		List<Map<String, Object>> memLevelList = binOLMBMBM09_BL.getMemberLevelInfoList(map);
		map.put("memLevelList", memLevelList);
		
		// 需要统计的时间段List
		List<Map<String, Object>> dateList = getDateList(map);
		map.put("dateList", dateList);
		
    	map.put("wechatBindTimeStart", dateList.get(0).get("startDate"));
    	map.put("recordDateTimeStart", dateList.get(0).get("startDateTime"));
		// 统计会员绑定数
		Map<String, Object> bindCountMap = binOLMBRPT02_Service.getBindCount(map);
		// 统计新会员数
		Map<String, Object> newMemCountMap = binOLMBRPT02_Service.getNewMemCount(map);
		if(newMemCountMap != null) {
			bindCountMap.putAll(newMemCountMap);
		}
		
		map.put("wechatBindTimeStart", wechatBindTimeStart);
		map.put("recordDateTimeStart", DateUtil.suffixDate(wechatBindTimeStart, 0));
		map.remove("dateList");
		// 统计所有会员绑定数
		Map<String, Object> allBindCountMap = binOLMBRPT02_Service.getBindCount(map);
		// 统计所有新会员数
		Map<String, Object> allNewMemCountMap = binOLMBRPT02_Service.getNewMemCount(map);
		if(allNewMemCountMap != null) {
			allBindCountMap.putAll(allNewMemCountMap);
		}
		
		List<Map<String, Object>> bindCountList = new ArrayList<Map<String,Object>>();
		String space = "";
		Map<String, Object> _bindCountMap = new HashMap<String, Object>();
		_bindCountMap.put("code", "totalBind");
		_bindCountMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_totalBind"));
		bindCountList.add(_bindCountMap);
		
		_bindCountMap = new HashMap<String, Object>();
		_bindCountMap.put("code", "newBind");
		_bindCountMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_newBind"));
		bindCountList.add(_bindCountMap);
		
		_bindCountMap = new HashMap<String, Object>();
		_bindCountMap.put("code", "nb_tb_p");
		_bindCountMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_nb_tb_p"));
		bindCountList.add(_bindCountMap);
		
		_bindCountMap = new HashMap<String, Object>();
		_bindCountMap.put("code", "newMem");
		_bindCountMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_newMem"));
		bindCountList.add(_bindCountMap);
		
		_bindCountMap = new HashMap<String, Object>();
		_bindCountMap.put("code", "nb_nm_p");
		_bindCountMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_nb_nm_p"));
		bindCountList.add(_bindCountMap);
		
		_bindCountMap = new HashMap<String, Object>();
		_bindCountMap.put("code", "oldBind");
		_bindCountMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_oldBind"));
		bindCountList.add(_bindCountMap);
		
		_bindCountMap = new HashMap<String, Object>();
		_bindCountMap.put("code", "ob_tb_p");
		_bindCountMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_ob_tb_p"));
		bindCountList.add(_bindCountMap);
		
		for(int i = 0; i < memLevelList.size(); i++) {
			Map<String, Object> memLevelMap = memLevelList.get(i);
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("code", String.valueOf(memLevelMap.get("memberLevelId")));
			itemMap.put("name", memLevelMap.get("levelName"));
			bindCountList.add(itemMap);
		}
		for(int i = 0; i < bindCountList.size(); i++) {
			_bindCountMap = bindCountList.get(i);
			String code = (String)_bindCountMap.get("code");
			if("nb_tb_p".equals(code)) {
//				int allBindCount = (Integer)allBindCountMap.get("newBind_count");
//				int bindCount = (Integer)bindCountMap.get("newBind_count");
//				int allBindCount_m = (Integer)allBindCountMap.get("totalBind_count");
//				int bindCount_m = (Integer)bindCountMap.get("totalBind_count");
//				if(allBindCount_m - bindCount_m != 0) {
//					_bindCountMap.put("oldBind", CherryUtil.percent(allBindCount-bindCount, allBindCount_m - bindCount_m, 2));
//				} else {
//					_bindCountMap.put("oldBind", "-");
//				}
//				if(allBindCount_m != 0) {
//					_bindCountMap.put("allBind", CherryUtil.percent(allBindCount, allBindCount_m, 2));
//				} else {
//					_bindCountMap.put("allBind", "-");
//				}
				_bindCountMap.put("oldBind", space);
				_bindCountMap.put("allBind", space);
				for(int j = 0; j < dateList.size(); j++) {
					Map<String, Object> _dateMap = dateList.get(j);
					String key = (String)_dateMap.get("key");
					Integer days = (Integer)_dateMap.get("days");
					int _bindCount = (Integer)bindCountMap.get("newBind_"+key);
					int _bindCount_m = (Integer)bindCountMap.get("totalBind_"+key);
					if(days != null) {
						_bindCount = (int)Math.ceil(_bindCount*7.0/days);
						_bindCount_m = (int)Math.ceil(_bindCount_m*7.0/days);
					}
					if(_bindCount_m != 0) {
						_bindCountMap.put(key, CherryUtil.percent(_bindCount, _bindCount_m, 2));
					} else {
						_bindCountMap.put(key, space);
					}
				}
			} else if("nb_nm_p".equals(code)) {
//				int allBindCount = (Integer)allBindCountMap.get("newBind_count");
//				int bindCount = (Integer)bindCountMap.get("newBind_count");
//				int allBindCount_m = (Integer)allBindCountMap.get("newMem_count");
//				int bindCount_m = (Integer)bindCountMap.get("newMem_count");
//				if(allBindCount_m - bindCount_m != 0) {
//					_bindCountMap.put("oldBind", CherryUtil.percent(allBindCount-bindCount, allBindCount_m - bindCount_m, 2));
//				} else {
//					_bindCountMap.put("oldBind", "-");
//				}
//				if(allBindCount_m != 0) {
//					_bindCountMap.put("allBind", CherryUtil.percent(allBindCount, allBindCount_m, 2));
//				} else {
//					_bindCountMap.put("allBind", "-");
//				}
				_bindCountMap.put("oldBind", space);
				_bindCountMap.put("allBind", space);
				for(int j = 0; j < dateList.size(); j++) {
					Map<String, Object> _dateMap = dateList.get(j);
					String key = (String)_dateMap.get("key");
					Integer days = (Integer)_dateMap.get("days");
					int _bindCount = (Integer)bindCountMap.get("newBind_"+key);
					int _bindCount_m = (Integer)bindCountMap.get("newMem_"+key);
					if(days != null) {
						_bindCount = (int)Math.ceil(_bindCount*7.0/days);
						_bindCount_m = (int)Math.ceil(_bindCount_m*7.0/days);
					}
					if(_bindCount_m != 0) {
						_bindCountMap.put(key, CherryUtil.percent(_bindCount, _bindCount_m, 2));
					} else {
						_bindCountMap.put(key, space);
					}
				}
			} else if("oldBind".equals(code)) {
//				int allBindCount = (Integer)allBindCountMap.get("newBind_count");
//				int bindCount = (Integer)bindCountMap.get("newBind_count");
//				int allBindCount_m = (Integer)allBindCountMap.get("totalBind_count");
//				int bindCount_m = (Integer)bindCountMap.get("totalBind_count");
//				_bindCountMap.put("oldBind", (allBindCount_m - bindCount_m) - (allBindCount-bindCount));
//				_bindCountMap.put("allBind", allBindCount_m - allBindCount);
				_bindCountMap.put("oldBind", space);
				_bindCountMap.put("allBind", space);
				for(int j = 0; j < dateList.size(); j++) {
					Map<String, Object> _dateMap = dateList.get(j);
					String key = (String)_dateMap.get("key");
					Integer days = (Integer)_dateMap.get("days");
					int _bindCount = (Integer)bindCountMap.get("newBind_"+key);
					int _bindCount_m = (Integer)bindCountMap.get("totalBind_"+key);
					if(days != null) {
						_bindCount = (int)Math.ceil(_bindCount*7.0/days);
						_bindCount_m = (int)Math.ceil(_bindCount_m*7.0/days);
					}
					_bindCountMap.put(key, _bindCount_m - _bindCount);
				}
			} else if("ob_tb_p".equals(code)) {
//				int allBindCount = (Integer)allBindCountMap.get("newBind_count");
//				int bindCount = (Integer)bindCountMap.get("newBind_count");
//				int allBindCount_m = (Integer)allBindCountMap.get("totalBind_count");
//				int bindCount_m = (Integer)bindCountMap.get("totalBind_count");
//				if(allBindCount_m - bindCount_m != 0) {
//					_bindCountMap.put("oldBind", CherryUtil.percent((allBindCount_m - bindCount_m) - (allBindCount-bindCount), allBindCount_m - bindCount_m, 2));
//				} else {
//					_bindCountMap.put("oldBind", "-");
//				}
//				if(allBindCount_m != 0) {
//					_bindCountMap.put("allBind",  CherryUtil.percent(allBindCount_m - allBindCount, allBindCount_m, 2));
//				} else {
//					_bindCountMap.put("allBind", "-");
//				}
				_bindCountMap.put("oldBind", space);
				_bindCountMap.put("allBind", space);
				for(int j = 0; j < dateList.size(); j++) {
					Map<String, Object> _dateMap = dateList.get(j);
					String key = (String)_dateMap.get("key");
					Integer days = (Integer)_dateMap.get("days");
					int _bindCount = (Integer)bindCountMap.get("newBind_"+key);
					int _bindCount_m = (Integer)bindCountMap.get("totalBind_"+key);
					if(days != null) {
						_bindCount = (int)Math.ceil(_bindCount*7.0/days);
						_bindCount_m = (int)Math.ceil(_bindCount_m*7.0/days);;
					}
					if(_bindCount_m != 0) {
						_bindCountMap.put(key, CherryUtil.percent(_bindCount_m - _bindCount, _bindCount_m, 2));
					} else {
						_bindCountMap.put(key, space);
					}
				}
			} else {
				if("newBind".equals(code)) {
					_bindCountMap.put("oldBind", space);
					_bindCountMap.put("allBind", space);
				} else {
					int allBindCount = (Integer)allBindCountMap.get(code+"_count");
					int bindCount = (Integer)bindCountMap.get(code+"_count");
					_bindCountMap.put("oldBind", allBindCount-bindCount);
					_bindCountMap.put("allBind", allBindCount);
				}
				for(int j = 0; j < dateList.size(); j++) {
					Map<String, Object> _dateMap = dateList.get(j);
					String key = (String)_dateMap.get("key");
					Integer days = (Integer)_dateMap.get("days");
					int _bindCount = (Integer)bindCountMap.get(code+"_"+key);
					if(days != null) {
						_bindCount = (int)Math.ceil(_bindCount*7.0/days);
					}
					_bindCountMap.put(key, _bindCount);
				}
			}
		}
		
		Map<String, Object> dateMap = new HashMap<String, Object>();
    	dateMap.put("startDate", wechatBindTimeStart);
    	dateMap.put("endDate", wechatBindTimeEnd);
    	dateMap.put("key", "allBind");
    	dateMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_cumulate"));
    	dateMap.put("type", 1);
    	dateList.add(dateMap);
    	
    	if(!wechatBindTimeStart.equals(dateList.get(0).get("startDate"))) {
			dateMap = new HashMap<String, Object>();
	    	dateMap.put("key", "oldBind");
	    	int _month = (Integer)dateList.get(0).get("month");
	    	dateMap.put("name", _month+CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_oldCumulate"));
	    	dateMap.put("type", 0);
	    	dateList.add(0, dateMap);
		}
    	
    	resultMap.put("dateList", dateList);
		resultMap.put("bindCountList", bindCountList);
	
		return resultMap;
	}
	
	/**
	 * 按城市统计会员绑定数
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public List<Map<String, Object>> getBindCountByCity(Map<String, Object> map) {
		
		// 绑定起始时间
		String wechatBindTimeStart = (String)map.get("wechatBindTimeStart");
		// 绑定结束时间
		String wechatBindTimeEnd = (String)map.get("wechatBindTimeEnd");
		
		if(wechatBindTimeStart == null || "".equals(wechatBindTimeStart)) {
			Map<String, Object> wechatBindTimeRange = binOLMBRPT02_Service.getWechatBindTimeRange(map);
			if(wechatBindTimeRange == null) {
				return null;
			}
			// 绑定起始时间
			wechatBindTimeStart = (String)wechatBindTimeRange.get("wechatBindTimeStart");
			if(wechatBindTimeStart == null || "".equals(wechatBindTimeStart)) {
				return null;
			}
			map.put("wechatBindTimeStart", wechatBindTimeStart);
		}
		map.put("recordDateTimeStart", DateUtil.suffixDate(wechatBindTimeStart, 0));
		
		if(wechatBindTimeEnd == null || "".equals(wechatBindTimeEnd)) {
			// 系统时间作为绑定结束时间
			String sysDateTime = binOLMBRPT02_Service.getSYSDateTime();
			wechatBindTimeEnd = sysDateTime.substring(0, 10);
			map.put("wechatBindTimeEnd", wechatBindTimeEnd);
			map.put("recordDateTimeEnd", sysDateTime);
		} else {
			map.put("recordDateTimeEnd", DateUtil.suffixDate(wechatBindTimeEnd, 1));
		}
		
		// 入会时间配置项（0：入会时间，1：销售首单）
		String joinDateFlag = binOLCM14_BL.getConfigValue("1286", String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), 
				String.valueOf(map.get(CherryConstants.BRANDINFOID)));
		map.put("joinDateFlag", joinDateFlag);
		
		List<Map<String, Object>> bindByCityList = binOLMBRPT02_Service.getBindCountByCity(map);
		if(bindByCityList != null && !bindByCityList.isEmpty()) {
			String space = "";
			for(int i = 0; i < bindByCityList.size(); i++) {
				Map<String, Object> bindByCityMap = bindByCityList.get(i);
				int totalBindCount = (Integer)bindByCityMap.get("totalBindCount");
				int newBindCount = (Integer)bindByCityMap.get("newBindCount");
				if(totalBindCount != 0) {
					bindByCityMap.put("tb_nb_count", CherryUtil.percent(newBindCount, totalBindCount, 2));
				} else {
					bindByCityMap.put("tb_nb_count", space);
				}
			}
			return bindByCityList;
		}
		
		return null;
	}
	
	/**
	 * 指定活动领用情况统计
	 * 
	 * @param map 检索条件
	 * @return 统计结果
	 */
	public Map<String, Object> getTotalGetCount(Map<String, Object> map) {
		
		// 绑定起始时间
		String wechatBindTimeStart = (String)map.get("wechatBindTimeStart");
		// 绑定结束时间
		String wechatBindTimeEnd = (String)map.get("wechatBindTimeEnd");
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		if(wechatBindTimeStart == null || "".equals(wechatBindTimeStart)) {
			Map<String, Object> wechatBindTimeRange = binOLMBRPT02_Service.getWechatBindTimeRange(map);
			if(wechatBindTimeRange == null) {
				return null;
			}
			// 绑定起始时间
			wechatBindTimeStart = (String)wechatBindTimeRange.get("wechatBindTimeStart");
			map.put("wechatBindTimeStart", wechatBindTimeStart);
		}
		
		if(wechatBindTimeEnd == null || "".equals(wechatBindTimeEnd)) {
			// 系统时间作为绑定结束时间
			wechatBindTimeEnd = binOLMBRPT02_Service.getDateYMD();
			map.put("wechatBindTimeEnd", wechatBindTimeEnd);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 需要统计的时间段List
		List<Map<String, Object>> dateList = getDateList(map);
		map.put("dateList", dateList);
		
		map.put("wechatBindTimeStart", dateList.get(0).get("startDate"));
		// 取得指定活动申请总人数
		Map<String, Object> totalOrderMap = binOLMBRPT02_Service.getTotalOrderCount(map);
		if(totalOrderMap != null) {
			// 取得指定活动领用总人数
			Map<String, Object> totalGetMap = binOLMBRPT02_Service.getTotalGetCount(map);
			if(totalGetMap != null) {
				totalOrderMap.putAll(totalGetMap);
			}
		}
		
		map.put("wechatBindTimeStart", wechatBindTimeStart);
		map.remove("dateList");
		// 取得指定活动申请总人数
		Map<String, Object> allTotalOrderMap = binOLMBRPT02_Service.getTotalOrderCount(map);
		if(allTotalOrderMap != null) {
			// 取得指定活动领用总人数
			Map<String, Object> allTotalGetMap = binOLMBRPT02_Service.getTotalGetCount(map);
			if(allTotalGetMap != null) {
				allTotalOrderMap.putAll(allTotalGetMap);
			}
		}
		
		List<Map<String, Object>> totalGetList = new ArrayList<Map<String,Object>>();
		String space = "";
		if(allTotalOrderMap != null) {
			Map<String, Object> _totalGetMap = new HashMap<String, Object>();
			_totalGetMap.put("code", "totalOrder");
			_totalGetMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_totalOrder"));
			totalGetList.add(_totalGetMap);
			
			_totalGetMap = new HashMap<String, Object>();
			_totalGetMap.put("code", "totalGet");
			_totalGetMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_totalGet"));
			totalGetList.add(_totalGetMap);
			
			_totalGetMap = new HashMap<String, Object>();
			_totalGetMap.put("code", "to_tg");
			_totalGetMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_to_tg"));
			totalGetList.add(_totalGetMap);
			
			for(int i = 0; i < totalGetList.size(); i++) {
				_totalGetMap = totalGetList.get(i);
				String code = (String)_totalGetMap.get("code");
				if("to_tg".equals(code)) {
					int allTotalOrderCount = (Integer)allTotalOrderMap.get("totalOrder_count");
					int totalOrderCount = (Integer)totalOrderMap.get("totalOrder_count");
					int allTotalGetCount = (Integer)allTotalOrderMap.get("totalGet_count");
					int totalGetCount = (Integer)totalOrderMap.get("totalGet_count");
					if(allTotalOrderCount - totalOrderCount != 0) {
						_totalGetMap.put("oldCount", CherryUtil.percent(allTotalGetCount-totalGetCount, allTotalOrderCount - totalOrderCount, 2));
					} else {
						_totalGetMap.put("oldCount", space);
					}
					if(allTotalOrderCount != 0) {
						_totalGetMap.put("allCount", CherryUtil.percent(allTotalGetCount, allTotalOrderCount, 2));
					} else {
						_totalGetMap.put("allCount", space);
					}
					for(int j = 0; j < dateList.size(); j++) {
						Map<String, Object> _dateMap = dateList.get(j);
						String key = (String)_dateMap.get("key");
						Integer days = (Integer)_dateMap.get("days");
						int _totalOrderCount = (Integer)totalOrderMap.get("totalOrder_"+key);
						int _totalGetCount = (Integer)totalOrderMap.get("totalGet_"+key);
						if(days != null) {
							_totalOrderCount = (int)Math.ceil(_totalOrderCount*7.0/days);
							_totalGetCount = (int)Math.ceil(_totalGetCount*7.0/days);
						}
						if(_totalOrderCount != 0) {
							_totalGetMap.put(key, CherryUtil.percent(_totalGetCount, _totalOrderCount, 2));
						} else {
							_totalGetMap.put(key, space);
						}
					}
				} else {
					int allTotalCount = (Integer)allTotalOrderMap.get(code+"_count");
					int totalCount = (Integer)totalOrderMap.get(code+"_count");
					_totalGetMap.put("oldCount", allTotalCount-totalCount);
					_totalGetMap.put("allCount", allTotalCount);
					for(int j = 0; j < dateList.size(); j++) {
						Map<String, Object> _dateMap = dateList.get(j);
						String key = (String)_dateMap.get("key");
						Integer days = (Integer)_dateMap.get("days");
						int count = (Integer)totalOrderMap.get(code+"_"+key);
						if(days != null) {
							count = (int)Math.ceil(count*7.0/days);
						}
						_totalGetMap.put(key, count);
					}
				}
			}
		}
		
		Map<String, Object> dateMap = new HashMap<String, Object>();
    	dateMap.put("startDate", wechatBindTimeStart);
    	dateMap.put("endDate", wechatBindTimeEnd);
    	dateMap.put("key", "allCount");
    	dateMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_cumulate"));
    	dateMap.put("type", 1);
    	dateList.add(dateMap);
		
		if(!wechatBindTimeStart.equals(dateList.get(0).get("startDate"))) {
			dateMap = new HashMap<String, Object>();
	    	dateMap.put("key", "oldCount");
	    	int _month = (Integer)dateList.get(0).get("month");
	    	dateMap.put("name", _month+CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_oldCumulate"));
	    	dateMap.put("type", 0);
	    	dateList.add(0, dateMap);
		}
		
    	resultMap.put("dateList", dateList);
		resultMap.put("totalGetList", totalGetList);
		
		return resultMap;
		
	}
	
	/**
	 * 生成时间段处理
	 * 
	 * @param map 检索条件
	 * @return 时间段List
	 */
	public List<Map<String, Object>> getDateList(Map<String, Object> map) {
		
		// 需要统计的时间段List
		List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
		// 绑定起始时间
		String wechatBindTimeStart = (String)map.get("wechatBindTimeStart");
		// 绑定结束时间
		String wechatBindTimeEnd = (String)map.get("wechatBindTimeEnd");
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		Date curDate = DateUtil.coverString2Date(wechatBindTimeEnd);
		Calendar ca = Calendar.getInstance();
    	ca.setTime(curDate);
    	ca.add(Calendar.MONTH, -2);
    	// 相对系统时间上2个月起止时间设置
    	for(int i = 0; i < 2; i++) {
        	ca.set(Calendar.DAY_OF_MONTH,ca.getActualMinimum(Calendar.DAY_OF_MONTH));
        	String startDate = DateUtil.date2String(ca.getTime(), DateUtil.DATE_PATTERN);
        	ca.set(Calendar.DAY_OF_MONTH,ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        	String endDate = DateUtil.date2String(ca.getTime(), DateUtil.DATE_PATTERN);
        	if(startDate.compareTo(wechatBindTimeStart) >= 0) {
        		Map<String, Object> dateMap = new HashMap<String, Object>();
            	dateMap.put("startDate", startDate);
            	dateMap.put("endDate", endDate);
            	dateMap.put("startDateTime", DateUtil.suffixDate(startDate, 0));
            	dateMap.put("endDateTime", DateUtil.suffixDate(endDate, 1));
            	int month = ca.get(Calendar.MONTH);
            	dateMap.put("key", String.valueOf(month));
            	dateMap.put("name", (month+1)+CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_monthAverage"));
            	dateMap.put("month", month+1);
            	dateMap.put("type", 0);
            	dateMap.put("days", ca.get(Calendar.DATE));
            	dateList.add(dateMap);
        	}
        	ca.add(Calendar.MONTH, 1);
    	}
    	
    	// 相对系统时间当月按周划分起止时间设置
    	Map<String, Object> dateMap = new HashMap<String, Object>();
    	ca.setTime(curDate);
    	int monthLenght = ca.get(Calendar.DAY_OF_MONTH);
    	int month = ca.get(Calendar.MONTH);
    	int week = 0;
    	ca.set(Calendar.DAY_OF_MONTH,ca.getActualMinimum(Calendar.DAY_OF_MONTH));
    	for(int i = 0; i < monthLenght; i++) {
    		String startDate = DateUtil.date2String(ca.getTime(), DateUtil.DATE_PATTERN);
    		if(startDate.compareTo(wechatBindTimeStart) >= 0) {
    			if(week == 0 || ca.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
    				week++;
        			dateMap = new HashMap<String, Object>();
        			dateMap.put("startDate", startDate);
        			dateMap.put("startDateTime", DateUtil.suffixDate(startDate, 0));
        			dateMap.put("key", month+"_"+week);
        			dateMap.put("name", CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_week1")
        					+week+CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_week2"));
        			dateMap.put("month", month+1);
        			dateMap.put("type", 1);
        			dateList.add(dateMap);
        		}
        		if(i == monthLenght-1 || ca.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        			String endDate = DateUtil.date2String(ca.getTime(), DateUtil.DATE_PATTERN);
        			dateMap.put("endDate", endDate);
        			dateMap.put("endDateTime", DateUtil.suffixDate(endDate, 1));
        		}
    		}
    		ca.add(Calendar.DAY_OF_MONTH, 1);
    	}
    	
    	// 相对系统时间设置当月起止时间
    	ca.setTime(curDate);
    	ca.set(Calendar.DAY_OF_MONTH,ca.getActualMinimum(Calendar.DAY_OF_MONTH));
    	String startDate = DateUtil.date2String(ca.getTime(), DateUtil.DATE_PATTERN);
    	if(startDate.compareTo(wechatBindTimeStart) < 0) {
    		startDate = wechatBindTimeStart;
    	}
    	dateMap = new HashMap<String, Object>();
    	dateMap.put("startDate", startDate);
    	dateMap.put("endDate", wechatBindTimeEnd);
    	dateMap.put("startDateTime", DateUtil.suffixDate(startDate, 0));
    	dateMap.put("endDateTime", DateUtil.suffixDate(wechatBindTimeEnd, 1));
    	dateMap.put("key", String.valueOf(month));
    	dateMap.put("name", (month+1)+CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_monthTatol"));
    	dateMap.put("month", month+1);
    	dateMap.put("type", 1);
    	dateList.add(dateMap);
    	
    	return dateList;
	}
	
	/**
     * 导出微信绑定统计报表
     */
    public InputStream exportBindCountRpt(Map<String, Object> map) throws Exception {
    	
    	// 取得微信绑定统计报表
    	Map<String, Object> bindCountMap = this.getBindCount(map);
    	if(bindCountMap == null) {
    		return null;
    	}
    	List<Map<String, Object>> bindCountList = (List)bindCountMap.get("bindCountList");
    	List<Map<String, Object>> dateList = (List)bindCountMap.get("dateList");
    	
    	String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    	
    	Map<String, Object> exportMap = new HashMap<String, Object>();
    	// 所属组织
    	exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
    	exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
    	exportMap.put("dataList", bindCountList);
    	
    	// 绑定起始时间
		String wechatBindTimeStart = (String)map.get("wechatBindTimeStart");
		// 绑定结束时间
		String wechatBindTimeEnd = (String)map.get("wechatBindTimeEnd");
    	// 设定查询条件
    	String header = CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_wechatBindTime")+"："+wechatBindTimeStart+"~"+wechatBindTimeEnd;
    	exportMap.put("header", header);
    	
		// 设定sheet名称
		exportMap.put("sheetName", CherryUtil.getResourceValue("BINOLMBRPT02", language, "sheetName1"));
		
		// 设定标题行
		List<String[][]> titleList = new ArrayList<String[][]>();
		List<String[]> title1 = new ArrayList<String[]>();
		title1.add(new String[]{CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_item"), "1", "2"});
		for(int i = 0; i < dateList.size(); i++) {
			Map<String, Object> dateMap = dateList.get(i);
			int type = (Integer)dateMap.get("type");
			String name = (String)dateMap.get("name");
			if(type == 0) {
				title1.add(new String[]{name, "1", "2"});
			} else {
				title1.add(new String[]{name, "1", "1"});
			}
		}
		titleList.add(title1.toArray(new String[][]{}));
		
		List<String[]> title2 = new ArrayList<String[]>();
		title2.add(new String[]{"", "1", "1"});
		for(int i = 0; i < dateList.size(); i++) {
			Map<String, Object> dateMap = dateList.get(i);
			int type = (Integer)dateMap.get("type");
			String startDate = (String)dateMap.get("startDate");
			String endDate = (String)dateMap.get("endDate");
			if(type == 0) {
				title2.add(new String[]{"", "1", "1"});
			} else {
				title2.add(new String[]{startDate.substring(5, 10)+"~"+endDate.substring(5, 10), "1", "1"});
			}
		}
		titleList.add(title2.toArray(new String[][]{}));
		exportMap.put("titleList", titleList);
		
		// 设定数据行格式
        List<String[]> titleRows = new ArrayList<String[]>();
        titleRows.add(new String[]{"name", "", "40", "", ""});
        for(int i = 0; i < dateList.size(); i++) {
			Map<String, Object> dateMap = dateList.get(i);
			String key = (String)dateMap.get("key");
			titleRows.add(new String[]{key, "", "15", "right", ""});
		}
        exportMap.put("titleRows", titleRows.toArray(new String[][]{}));
        
    	// 导出excel处理
    	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap);
        return new ByteArrayInputStream(byteArray); 
    }
    
    /**
     * 导出微信绑定按城市报表
     */
    public InputStream exportBindByCityRpt(Map<String, Object> map) throws Exception {
    	
    	// 取得微信绑定按城市报表
    	List<Map<String, Object>> bindCountByCityList = this.getBindCountByCity(map);
    	if(bindCountByCityList == null || bindCountByCityList.isEmpty()) {
    		return null;
    	}
    	
    	String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    	
    	Map<String, Object> exportMap = new HashMap<String, Object>();
    	// 所属组织
    	exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
    	exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
    	exportMap.put("dataList", bindCountByCityList);
    	
    	// 绑定起始时间
		String wechatBindTimeStart = (String)map.get("wechatBindTimeStart");
		// 绑定结束时间
		String wechatBindTimeEnd = (String)map.get("wechatBindTimeEnd");
    	// 设定查询条件
    	String header = CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_wechatBindTime")+"："+wechatBindTimeStart+"~"+wechatBindTimeEnd;
    	exportMap.put("header", header);
    	
		// 设定sheet名称
		exportMap.put("sheetName", CherryUtil.getResourceValue("BINOLMBRPT02", language, "sheetName2"));
		
		// 设定标题行
		List<String[][]> titleList = new ArrayList<String[][]>();
		List<String[]> title1 = new ArrayList<String[]>();
		title1.add(new String[]{CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_regionName"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_cityName"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_totalBind"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_newBind"), "1", "1"});
		title1.add(new String[]{CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_nb_tb_p"), "1", "1"});
		titleList.add(title1.toArray(new String[][]{}));
		exportMap.put("titleList", titleList);
		
		// 设定数据行格式
        List<String[]> titleRows = new ArrayList<String[]>();
        titleRows.add(new String[]{"regionName", "", "20", "", ""});
        titleRows.add(new String[]{"cityName", "", "20", "", ""});
        titleRows.add(new String[]{"totalBindCount", "", "15", "right", ""});
        titleRows.add(new String[]{"newBindCount", "", "15", "right", ""});
        titleRows.add(new String[]{"tb_nb_count", "", "30", "right", ""});
        exportMap.put("titleRows", titleRows.toArray(new String[][]{}));
        
    	// 导出excel处理
    	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap);
        return new ByteArrayInputStream(byteArray); 
    }
    
    /**
     * 导出微信活动统计报表
     */
    public InputStream exportTotalGetCountRpt(Map<String, Object> map) throws Exception {
    	
    	// 取得微信活动统计报表
    	Map<String, Object> totalGetCountMap = this.getTotalGetCount(map);
    	if(totalGetCountMap == null) {
    		return null;
    	}
    	List<Map<String, Object>> totalGetList = (List)totalGetCountMap.get("totalGetList");
    	List<Map<String, Object>> dateList = (List)totalGetCountMap.get("dateList");
    	
    	String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    	
    	Map<String, Object> exportMap = new HashMap<String, Object>();
    	// 所属组织
    	exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
    	exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
    	exportMap.put("dataList", totalGetList);
    	
    	// 绑定起始时间
		String wechatBindTimeStart = (String)map.get("wechatBindTimeStart");
		// 绑定结束时间
		String wechatBindTimeEnd = (String)map.get("wechatBindTimeEnd");
    	// 设定查询条件
    	String header = CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_wechatBindTime")+"："+wechatBindTimeStart+"~"+wechatBindTimeEnd;
    	exportMap.put("header", header);
    	
		// 设定sheet名称
		exportMap.put("sheetName", CherryUtil.getResourceValue("BINOLMBRPT02", language, "sheetName3"));
		
		// 设定标题行
		List<String[][]> titleList = new ArrayList<String[][]>();
		List<String[]> title1 = new ArrayList<String[]>();
		title1.add(new String[]{CherryUtil.getResourceValue("BINOLMBRPT02", language, "mbrpt_item"), "1", "2"});
		for(int i = 0; i < dateList.size(); i++) {
			Map<String, Object> dateMap = dateList.get(i);
			int type = (Integer)dateMap.get("type");
			String name = (String)dateMap.get("name");
			if(type == 0) {
				title1.add(new String[]{name, "1", "2"});
			} else {
				title1.add(new String[]{name, "1", "1"});
			}
		}
		titleList.add(title1.toArray(new String[][]{}));
		
		List<String[]> title2 = new ArrayList<String[]>();
		title2.add(new String[]{"", "1", "1"});
		for(int i = 0; i < dateList.size(); i++) {
			Map<String, Object> dateMap = dateList.get(i);
			int type = (Integer)dateMap.get("type");
			String startDate = (String)dateMap.get("startDate");
			String endDate = (String)dateMap.get("endDate");
			if(type == 0) {
				title2.add(new String[]{"", "1", "1"});
			} else {
				title2.add(new String[]{startDate.substring(5, 10)+"~"+endDate.substring(5, 10), "1", "1"});
			}
		}
		titleList.add(title2.toArray(new String[][]{}));
		exportMap.put("titleList", titleList);
		
		// 设定数据行格式
        List<String[]> titleRows = new ArrayList<String[]>();
        titleRows.add(new String[]{"name", "", "40", "", ""});
        for(int i = 0; i < dateList.size(); i++) {
			Map<String, Object> dateMap = dateList.get(i);
			String key = (String)dateMap.get("key");
			titleRows.add(new String[]{key, "", "15", "right", ""});
		}
        exportMap.put("titleRows", titleRows.toArray(new String[][]{}));
        
    	// 导出excel处理
    	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap);
        return new ByteArrayInputStream(byteArray); 
    }

}

/*	
 * @(#)BaseService.java     1.0 2010/10/12		
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
package com.cherry.cm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseConfServiceImpl;
import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.TpifServiceImpl;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;

/**
 * 
 * 	基类（共通）Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2010.10.12
 */

public class BaseService {
	
	@Resource
	protected BaseServiceImpl baseServiceImpl;
	

//	@Resource
//	protected BaseWitServiceImpl baseWitServiceImpl;
	
	@Resource
	protected BaseConfServiceImpl baseConfServiceImpl;
	
	@Resource
	protected TpifServiceImpl tpifServiceImpl;
	
	/**
	 * 取得系统时间
	 * 
	 * @param
	 * @return String
	 */
	public String getSYSDate(){	
		return baseServiceImpl.getSYSDate();
	}
	
	/**
	 * 取得系统时间(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param
	 * @return String
	 */
	public String getSYSDateTime(){	
		return baseServiceImpl.getSYSDateTime();
	}
	
	/**
	 * 取得系统时间(年月日)
	 * 
	 * @param
	 * @return String
	 */
	public String getDateYMD(){	
		return baseServiceImpl.getDateYMD();
	}
	
	/**
	 * 取得系统时间(配置数据库)
	 * 
	 * @param
	 * @return String
	 */
	public String getSYSDateConf(){	
		return baseConfServiceImpl.getSYSDate();
	}
	
	/**
	 * 取得前进的系统时间
	 * 
	 * @param
	 * @return String
	 */
	public String getForwardSYSDate() throws Exception {
		return baseServiceImpl.getForwardSYSDate();
	}
	
	/**
	 * 查询假日
	 * 
	 * @param Map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getHolidays(Map<String, Object> map) {
		Map parameterMap = new HashMap();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getHolidays");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得给定日期的财务日
	 * 
	 * @param orgInfoId
	 * 			组织ID
	 * @param cal
	 * 			日期
	 * @return String
	 */
	public String getFiscalDate(int orgInfoId, Date date){
		Map<String, Object> map = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
		map.put(CherryConstants.YEAR, cal.get(Calendar.YEAR));
		map.put(CherryConstants.MONTH, cal.get(Calendar.MONTH) + 1);
		map.put(CherryConstants.DAY, cal.get(Calendar.DAY_OF_MONTH));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getFiscalDate");
		return (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得给定自然日对应的库存历史截止时间
	 * 
	 * @param map
	 * 
	 * @return String
	 */
	public Object getCutOfDate(Map<String, Object> map,String flag){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.get"+flag+"CutOfDate");
		return baseServiceImpl.get(map);
	}
	
	/**
	 * 取得系统时间
	 * 
	 * @param
	 * @return String
	 */
	public String getBussinessDate(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getBussinessDate");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得 1.业务日期,2.日结标志
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getBussinessDateMap");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得 日结状态确定的业务日期
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	public String getBusDate(Map<String, Object> map){	
		Map<String, Object> busMap = getBussinessDateMap(map);
		// 业务日期
		String busDate = ConvertUtil.getString(busMap.get("businessDate"));
		// 日结标志
		String closeFlag = ConvertUtil.getString(busMap.get("closeFlag"));
		// 当天业务结束，业务日期下一天
		if("1".equals(closeFlag)){
			return DateUtil.addDateByDays(CherryConstants.DATE_PATTERN, busDate, 1);
		}else{
			return busDate;
		}
	}
	
	/**
	 * 查询指定日期所在的财务年月
	 * 
	 * @param map 查询条件
	 * @return 指定日期所在的财务年月
	 */
	public Map<String, Object> getFiscalMonth(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getFiscalMonth");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据指定的自然日，返回该自然日所属财务月对应的日期序列(自然日)
	 * @param map
	 * @return
	 */
	public List<String> getFiscalPeriodByNatural(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getFiscalPeriodByNatural");
		return (List<String>)baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询指定财务月的最小最大自然日
	 * 
	 * @param map 查询条件
	 * @return 指定财务月的最小最大自然日
	 */
	public Map<String, Object> getMinMaxDateValue(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getMinMaxDateValue");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 手动提交事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void manualCommit() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLCMINC99.commit");
		baseServiceImpl.update(paramMap);
	}

	/**
	 * 手动回滚事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void manualRollback() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLCMINC99.rollback");
		baseServiceImpl.update(paramMap);
	}
}

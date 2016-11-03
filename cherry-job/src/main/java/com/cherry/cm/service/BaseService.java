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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseConfServiceImpl;
import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.IfServiceImpl;
import com.cherry.cm.core.SmsServiceImpl;
import com.cherry.cm.core.TpifServiceImpl;
import com.cherry.cm.core.WitBaseServiceImpl;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.DateUtil;

/**
 * 
 * 	基类（共通）Service
 * 
 * 
 * 
 * @author zhangjie
 * @version 1.0 2010.11.09
 */
public class BaseService {
	/** 操作新后台Config数据库的dao实现类 */
	@Resource
	protected BaseConfServiceImpl baseConfServiceImpl;
	
	/** 操作新后台品牌数据库的dao实现类 */
	@Resource
	protected BaseServiceImpl baseServiceImpl;
	
	/** 操作老后台Brand数据库的dao实现类 */
	@Resource
	protected IfServiceImpl ifServiceImpl;
	
	/** 操作老后台品牌数据库witpos_XXX的dao实现类 */
	@Resource
	protected WitBaseServiceImpl witBaseServiceImpl;
	
	/** 操作第三方接口数据库的dao实现类 */
	@Resource
	protected TpifServiceImpl tpifServiceImpl;
	
	/** 操作短信专用数据库的dao实现类 */
	@Resource
	protected SmsServiceImpl smsServiceImpl;
	
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
	 * 取得系统时间
	 * 
	 * @param
	 * @return String
	 */
	public String getSYSDateTime(){	
		return baseServiceImpl.getSYSDateTime();
	}
	
	/**
	 * 取得第三方接口数据库系统时间(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param
	 * @return String
	 */
	public String getTpifSYSDateTime(){	
		return tpifServiceImpl.getSYSDateTime();
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
	 * 手动提交事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void manualCommit() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateCOMMIT");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateROLLBACK");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 接口表手动提交事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void ifManualCommit() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateCOMMIT");
		ifServiceImpl.update(paramMap);
	}

	/**
	 * 接口表手动回滚事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void ifManualRollback() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateROLLBACK");
		ifServiceImpl.update(paramMap);
	}
	
	/**
	 * 第三方接口数据库手动提交事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void tpifManualCommit() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateCOMMIT");
		tpifServiceImpl.update(paramMap);
	}

	/**
	 * 第三方接口数据库手动回滚事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void tpifManualRollback() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateROLLBACK");
		tpifServiceImpl.update(paramMap);
	}
	
	/**
	 * 老后台品牌表手动提交事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void witManualCommit() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateCOMMIT");
		witBaseServiceImpl.update(paramMap);
	}

	/**
	 * 老后台品牌表手动回滚事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void witManualRollback() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateROLLBACK");
		witBaseServiceImpl.update(paramMap);
	}
	
	/**
	 * 短信数据库手动提交事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void smsManualCommit() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateCOMMIT");
		smsServiceImpl.update(paramMap);
	}

	/**
	 * 短信数据库手动回滚事务
	 * 
	 * @param 无
	 * 
	 * @return 无
	 * 
	 */
	public void smsManualRollback() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBECMINC99.updateROLLBACK");
		smsServiceImpl.update(paramMap);
	}
	/**
	 * 取得业务日期
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	public String getBussinessDate(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBECMINC99.getBussinessDate");
		return (String)baseServiceImpl.get(parameterMap);
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
	 * 取得 1.业务日期,2.日结标志
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBECMINC99.getBussinessDateMap");
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
		String busDate = CherryBatchUtil.getString(busMap
				.get(CherryBatchConstants.BUSINESS_DATE));
		// 日结标志
		String closeFlag = CherryBatchUtil.getString(busMap
				.get("closeFlag"));
		// 当天业务结束，业务日期下一天
		if("1".equals(closeFlag)){
			return DateUtil.addDateByDays(
					CherryBatchConstants.DATE_PATTERN, busDate, 1);
		}else{
			return busDate;
		}
	}
	
	public Map<String, Object> getBusDateMap(Map<String, Object> map){	
		Map<String, Object> busMap = getBussinessDateMap(map);
		// 日结标志
		String closeFlag = CherryBatchUtil.getString(busMap
				.get("closeFlag"));
		// 当天业务结束，业务日期下一天
		if("1".equals(closeFlag)){
			// 业务日期
			String busDate = CherryBatchUtil.getString(busMap
					.get(CherryBatchConstants.BUSINESS_DATE));
			busMap.put(CherryBatchConstants.BUSINESS_DATE, DateUtil.addDateByDays(
					CherryBatchConstants.DATE_PATTERN, busDate, 1));
		}
		return busMap;
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
}

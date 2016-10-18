/*
 * @(#)BINOLMBPTM05_Action.java     1.0 2013/05/23
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
package com.cherry.mb.ptm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 积分Excel导入Service
 * 
 * @author LUOHONG
 * @version 1.0 2013/05/23
 */
public class BINOLMBPTM05_Service extends BaseService {
	
	/**
	 * 
	 * 查询会员Id
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 * 
	 */
	public Object getMemberId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.getMemberId");
		return baseServiceImpl.get(paramMap);
	}
	/**
     * 插入导入积分主表，返回总表ID
     * @param map
     * @return
     */
    public int insertMemPointImport(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.insertMemPointImport");
        return baseServiceImpl.saveBackId(parameterMap);
    }
	/**
	 * 取得已经存在的会员积分修改记录
	 * @param map
	 * @return
	 */
	public String getCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.getCount");
		return (String)baseServiceImpl.get(parameterMap);
	}
	/**
	 * 用批处理插入一组数据
	 * 
	 * @param list
	 *            List
	 * @param sqlId
	 * 			  String         
	 * @return 
	 */
	public void saveAll(final List<Map<String, Object>> list) {
		if (null != list && !list.isEmpty()) {
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			int size = list.size();
			while (true) {
				startNum = CherryConstants.BATCH_PAGE_MAX_NUM * currentNum;
				// 查询结束位置
				endNum = startNum + CherryConstants.BATCH_PAGE_MAX_NUM;
				if (endNum > size) {
					endNum = size;
				}
				// 数据抽出次数累加
				currentNum++;	
				List<Map<String, Object>> tempList = list.subList(startNum, endNum);
				baseServiceImpl.saveAll(tempList, "BINOLMBPTM05.insertMemPointImportDetail");
				if (endNum == size) {
					break;
				}
			}
		}
	}
	/**
     * 取得成功信息List
     * 
     * @param map 查询条件
     * 
     */
    public List<Map<String, Object>> getSuccessList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.getSuccessList");
        return baseServiceImpl.getList(parameterMap);
    }
    /**
	 * 
	 * 更新积分导入明细表,相同卡号不导入
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateImportDetail(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.updateImportDetail");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 更新积分导入明细表sendflag=1
	 * @param map
	 * @return
	 */
	public int updateSendflag(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.updateSendflag");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 
	 * 更新积分导入明细表，同一会员，同一时间导入多条，更新为失败
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateImportTimeDetail(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("importResults", CherryConstants.IMPORTRESULT_3);
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.updateImportTimeDetail");
		return baseServiceImpl.update(paramMap);
	}
	/**
	 * 取得导入明细表已经存在的会员积分修改记录
	 * @param map
	 * @return
	 */
	public String getImpCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.getImpCount");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得会员初始导入时间
	 * @param map
	 * @return
	 */
	public String getInitialTime(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM05.getInitialTime");
		return (String)baseServiceImpl.get(parameterMap);
	}
}
